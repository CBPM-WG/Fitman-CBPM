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
package eu.atosresearch.seiplab.yourbpm.dtc.agents.adapter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import org.apache.log4j.Logger;



import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.ComposerOperations;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.Agent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.NewDesignModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignTransition;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelLoader;

import org.soa4all.lpml.impl.ProcessImpl;

public class GoalTemplateAdapter extends RuleAdapter {
	// Logger
	static Logger logger = Logger.getLogger(GoalTemplateAdapter.class);
	
	//model loader 
	static ModelLoader modelLoader = ModelLoader.getInstance();
	
	private URI templateURI;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);
	
	public GoalTemplateAdapter (NewDesignModelEvent dme, Agent agent){
		super (dme, agent);
	}
	
	public void initAdapter (String ruleName) {
		setRuleName(ruleName);
		String adapterURI = agent.getIdentifier() + "GTAdapter_" + ruleName;
		try {
			IProcess process = new ProcessImpl(getTemplateURI().toString());
			this.setIdentifier(new URI(adapterURI));
			this.setTarget(process);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("createAdapter fail");
			e.printStackTrace();
		}
	}
	
	public URI getTemplateURI() {
		return templateURI;
	}
	public void setTemplateURI(String templateName) throws URISyntaxException {
		if (agent.getIdentifier().getPath().endsWith("/")){
			this.templateURI = new URI (agent.getIdentifier() + templateName);
		} else {
			this.templateURI = new URI (agent.getIdentifier() + "/" + templateName);
		}
	}
	
	@Override
	protected boolean isAcceptedOperation() {
		// Accepted operations for this Adapter are those which request to resolve goals with templates
		boolean result = false;
		ComposerOperations operation = dme.getBlackBoardControlAgent().getOperation();
		if (operation == ComposerOperations.RESOLVE_PROCESS ||
			operation == ComposerOperations.RESOLVE_GOAL ||
			operation == ComposerOperations.RESOLVE_GOAL_WITH_TEMPLATE || 
			operation == ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE
			){
			result = true;
		}
		return result;
	}


	public boolean applyAdapter() {
		boolean result = false;
		
		try {			
			DesignModel designModel = dme.getDesignModel();
							
			// Clone designModel
			DesignModel newDesignModel = (DesignModel)modelProxy.cloneLPMLObject(designModel);

			//BugFix - we need to obtain Goal from the cloned model, not the original one.
			IActivity goal = (IActivity) modelProxy.getFlowObjectByIdentifier(
					newDesignModel.getDesignStructure(), new URI(((IActivity) getSource()).getID()));
			
			// Load Template
			//Process template = ModelIOUtils.loadTemplate(new URI(((IProcess) getTarget()).getID()));
			IProcess template = modelLoader.loadTemplate(new URI(((IProcess) getTarget()).getID()));

			// Replacing Goal with Template
			modelProxy.substitute(newDesignModel.getDesignStructure(), (IActivity) goal, template);
			
			result = true;
			
			// Adding Adapter assignments (Assumptions) to new model
			addAssumptions (getAssumptions(), newDesignModel.getAssignmentSet().getAssignments());
//			newDesignModel.getAssignmentSet().addAssignments((getAssumptions()));
			
			// Adding Template annotations as assignments
			newDesignModel.getAssignmentSet().addAssignments(template.getSemanticAnnotations());

			//add DesignTransition information
			DesignTransition dt = new DesignTransition();
			dt.setSourceURI(Integer.toString(goal.getID().hashCode()));
			dt.setTargetURI(Integer.toString(((IProcess)getTarget()).getID().hashCode()));
			newDesignModel.addTransition(dt);
			
			// Notifying new model to blackboard agent
			newDesignModel.setAppliedRule(getRuleName());
			dme.getBlackBoardControlAgent().updateDesignModel(designModel,
					newDesignModel, agent.getIdentifier(), agent.getIdentifier(), "GoalTemplateAdapter",((IActivity) getSource()).getName() ,getRuleName());

		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		return result;
	}
	
	private void addAssumptions(Collection<ISemanticAnnotation> assumptions,
			Collection<ISemanticAnnotation> assignments) {
		for (ISemanticAnnotation assumption: assumptions){
			if (!contains (assignments, assumption)){
				assignments.add (assumption);
			}
		}
		
	}

	private boolean contains(Collection<ISemanticAnnotation> assignments,
			ISemanticAnnotation assumption) {
		boolean result = false;
		for (ISemanticAnnotation assignment: assignments){
			if (assignment.getReferenceURI().equalsIgnoreCase(assumption.getReferenceURI())){
				result = true;
				break;
			}
		}
		return result;
	}
}
