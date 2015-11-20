/**
 * 
 * Copyright (C) 2006-2010 ATOS ORIGIN
 * http://www.atosorigin.com
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the license LGPL.
 *
 * Authors :
 * Jesus Gorronogoitia. Atos Research and Innovation, Atos Origin SAE
 * @email jesus.gorronogoitia@atosresearch.eu
 * Mateusz Radzimski. Atos Research and Innovation, Atos Origin SAE
 * @email mateusz.radzimski@atosresearch.eu
 */
package eu.atosresearch.seiplab.yourbpm.dtc.agents;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.soa4all.lpml.Activity;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import prefuse.util.collections.IncompatibleComparatorException;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.dtc.DTComposerImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.CleanupBlackBoardEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.NewDesignModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.TaggedModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.thread.ThreadPool;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModelStatusEnum;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPIImpl;

public class DesignAnalysisAgent extends RuleAgent implements
		ApplicationListener, Runnable {
	// Logger
	static Logger logger = Logger.getLogger(DesignAnalysisAgent.class);
	NewDesignModelEvent newDesignModelEvent;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);
	
	public DesignAnalysisAgent(String ruleBasePath, String domainSpecificLanguage, ReasonerAPIImpl reasoner) {
		super(ruleBasePath, domainSpecificLanguage);
    	this.reasoner = reasoner;
	}


	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if ((applicationEvent instanceof NewDesignModelEvent) && !(applicationEvent instanceof TaggedModelEvent)) {
			newDesignModelEvent = (NewDesignModelEvent) applicationEvent;
			if (DTComposerImpl.getInstance().getConfiguration().isThread()){
				ThreadPool.getInstance().execute(this);
			} else {
				run();
			}
		}
		else if (applicationEvent instanceof CleanupBlackBoardEvent){
			this.clearWorkingMemory();
			this.reasoner.cleanUp();
		}
	}
	
	@Override
	public void run (){
		try {
//			newDesignModelEvent.getBlackBoardControlAgent().increaseSizeDMAInvocationPool();
			logger.info("Analysing model " + newDesignModelEvent.getDesignModel().getIndex());
			
			// Detect if model has been previosly visited
			if (!newDesignModelEvent.getDesignModel().getStatus().isSet()){
				this.insertFact(newDesignModelEvent);
				this.fireRules();
			}
			
			// Notifying acknowledge
			newDesignModelEvent.setAgent(this);
			(newDesignModelEvent.getBlackBoardControlAgent()).notifyAgentResponse(newDesignModelEvent);
			
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			newDesignModelEvent.getBlackBoardControlAgent().decreaseSizeDMAInvocationPool();
		}
	}
	
	/**
	 * A Design Model (DM) is complete when:
	 * - if target is a goal: when the goal has been resolved, that is, bound to a WS
	 * - if target is a process: when all goals in the process have been resolved
	 * @param dme
	 * @throws URISyntaxException 
	 */
	public void checkModelCompletness(NewDesignModelEvent dme) throws URISyntaxException {
		boolean result = false;
		
		DesignModel designModel = dme.getDesignModel();
		
		//Model is complete if depending on the requested operation the target has been resolve
		URI goalTarget = dme.getBlackBoardControlAgent().getGoalTarget();
		
		// Requested to resolve all goals
		if (dme.getBlackBoardControlAgent().isProcessTarget()){
			if (allGoalsResolved(designModel)) {
				result = true;
			}
		}else{// Requested to resolve one goal
			if (goalResolved(designModel, goalTarget)) {
				result = true;
			}
		}
		
		designModel.getStatus().changeStatus(result?DesignModelStatusEnum.COMPLETE:DesignModelStatusEnum.INCOMPLETE);
	}
	
	/**
	 * A Design Model (DM) is suitable when:
	 * - if target is a goal: when
	 * 	 global assignments (requirements) set by that goal rule are compatible with requested global requirements
	 * - if target is a process: when
		 all global requirements are met: all global assignments (requirements) met global requirements
	 * @param dme
	 * @throws IncompatibleComparatorException
	 * @throws URISyntaxException 
	 */
	public void checkModelSuitability(NewDesignModelEvent dme) throws Exception, URISyntaxException {
		boolean result = false;
		
		DesignModel designModel = dme.getDesignModel();
		
		// Model is tagged as not suitable as soon as one global preference is not satisfied
		if (designModel.matchPreferences(reasoner)){
			result = true;
		}
		
		designModel.getStatus().changeStatus(result?DesignModelStatusEnum.SUITABLE:DesignModelStatusEnum.NOT_SUITABLE);
	}

	/**
	 * A Design Model (DM) is admissible when:
	 * - if target is a goal: when
	 * 	 any goal rule assignment (constraint) set is compatible with all global constraints.
	 * - if target is a process:  when
	 *   any goal rule assignment (constraint) set is compatible with all global constraints.
	 * @param dme
	 * @throws IncompatibleComparatorException
	 */
	public void checkModelAdmissibility(NewDesignModelEvent dme) throws Exception {
		boolean result = false;
		
		DesignModel designModel = dme.getDesignModel();
		
		// Model is tagged as inadmissible as soon as one global requirement is not satisfied
		if (designModel.matchRequirements(reasoner)) {
			result = true;
		} 
			
		designModel.getStatus().changeStatus(result?DesignModelStatusEnum.ADMISSIBLE:DesignModelStatusEnum.INADMISSIBLE);
	}
	
	public void isDataFlowAdded(NewDesignModelEvent dme) throws Exception  {
		
		DesignModel designModel = dme.getDesignModel();
		
		boolean result = designModel.isDataFlowAdded();
		designModel.getStatus().changeStatus(result?DesignModelStatusEnum.IO_COMPATIBLE:DesignModelStatusEnum.IO_UNCHECKED_SOLUTION);
	}
	
	@Override
	public String toString() {
		return this.getIdentifier().toString();
	}

	/*
	private boolean allGoalsResolved(DesignModel designModel){
		return LPMLProxy.findAllGoals(designModel.getDesignStructure()).size() == 0;
	}
	*/

	private boolean allGoalsResolved(DesignModel designModel){
		boolean resolved = true;
		
		for (IActivity goalActivity : modelProxy.findAllActivities(designModel.getDesignStructure())) {
			if (goalActivity.getBindings().isEmpty()) {
				resolved = false;
			} 
			if (!resolved){
				break;
			}
		}
		
		return resolved;
	}

	private boolean goalResolved(DesignModel designModel, URI goalTarget){
		IActivity activity = 
			(Activity) modelProxy.getActivityById(designModel.getDesignStructure(), goalTarget);
		return activity==null || !activity.getBindings().isEmpty();
	}

}
