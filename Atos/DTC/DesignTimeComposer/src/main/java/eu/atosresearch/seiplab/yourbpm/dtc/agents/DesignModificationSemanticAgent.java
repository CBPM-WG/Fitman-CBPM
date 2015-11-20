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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;


import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.ComposerOperations;
import eu.atosresearch.seiplab.yourbpm.dtc.DTComposerImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.adapter.GoalTemplateAdapter;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.adapter.GoalWSAdapter;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.adapter.RuleAdapter;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.CleanupBlackBoardEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.NewDesignModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.TaggedModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.thread.ThreadPool;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPI;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPIImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLElement;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLService;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLTemplate;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelLoader;

import org.soa4all.lpml.impl.BindingImpl;
import org.soa4all.lpml.impl.SemanticAnnotationImpl;
import org.soa4all.lpml.impl.ProcessImpl;

public class DesignModificationSemanticAgent extends Agent implements
		ApplicationListener, DesignModificationAgent, InitializingBean, Runnable {
	//Limiting the number of bindings in order to avoid long solution space traversing.
	//TODO: Set this constant in Configuration
	//TODO: consider to set up a DTC max computation time to avoid traversing long solution space
	private static final int MAX_NUMBER_BINDINGS = 3;
	private static final int MAX_NUMBER_FETCHED_RESULTS = 3;
	
	static Logger logger = Logger.getLogger(DesignModificationSemanticAgent.class);
	NewDesignModelEvent newDesignModelEvent;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);
	
	public DesignModificationSemanticAgent(ReasonerAPIImpl reasoner) {
		this.reasoner = reasoner;
		this.modelLoader = ModelLoader.getInstance();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// executed after constructor and spring configuration
		// initializeAgent();
	}

	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof TaggedModelEvent) {

			newDesignModelEvent = (NewDesignModelEvent) applicationEvent;
			if (DTComposerImpl.getInstance().getConfiguration().isThread()){
				ThreadPool.getInstance().execute(this);
			} else {
				run();
			}
			
		}
		else if (applicationEvent instanceof CleanupBlackBoardEvent){
			this.reasoner.cleanUp();
			this.modelLoader.cleanUp();
		}
	}
	
	@Override
	public void run() {
//		newDesignModelEvent.getBlackBoardControlAgent().increaseSizeDMAInvocationPool();
		if (!newDesignModelEvent.getBlackBoardControlAgent().isFoundRequiredSolutions()){
			runSemanticAgent();
		}
		newDesignModelEvent.getBlackBoardControlAgent().decreaseSizeDMAInvocationPool();
	}
	
	private void runSemanticAgent (){
		try {

			// Checking if designModel has been previously analyzed and
			// tagged as inadmissible
			if (newDesignModelEvent.getDesignModel().getStatus()
					.isInadmissible()) {
				return;
			}
			
			// Checking if designModel has been previously analyzed and tagged as IOUNCHECKED_SOLUTION
			if (newDesignModelEvent.getDesignModel().getStatus()
					.isIOUncheckedSolution()) {
				return;
			}

			ComposerOperations operation = newDesignModelEvent
					.getBlackBoardControlAgent().getOperation();

			List<RuleAdapter> adapters = new ArrayList<RuleAdapter>();

			// check if we have to resolve whole process or only selected
			// goal
			if (newDesignModelEvent.getBlackBoardControlAgent()
					.isProcessTarget() == false) {
				// resolve only selected goal

				// locate target goal in the process
				IActivity goalActivity = modelProxy.getActivityById(
						newDesignModelEvent.getDesignModel()
								.getDesignStructure(), newDesignModelEvent
								.getBlackBoardControlAgent()
								.getGoalTarget());

				if (goalActivity != null) {
					// resolve goal
					adapters = resolveGoal(newDesignModelEvent,
							goalActivity, operation);
				}

			} else {
				// resolve whole process
				adapters = resolveProcess(newDesignModelEvent, operation);
			}

			// after resolving, apply selected adapter
			if (adapters != null && !adapters.isEmpty()) {
				for (RuleAdapter adapter: adapters){
					adapter.applyAdapter();
				}
			}

			// Notifying end of work, and returning design model
			(newDesignModelEvent.getBlackBoardControlAgent())
					.notifyAgentResponse(newDesignModelEvent);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Method for resolving whole process. It searches for unresolved goals,
	 * takes first and applies resolveGoal method.
	 * 
	 * @param newDesignModelEvent
	 * @param operation
	 * @return adapter for converting resolved goal into either service or
	 *         template
	 */
	private List<RuleAdapter> resolveProcess(NewDesignModelEvent newDesignModelEvent,
			ComposerOperations operation) {

		// result list
		List<RuleAdapter> results = new LinkedList<RuleAdapter>();

		// get actual process structure from received design model
		IProcess process = newDesignModelEvent.getDesignModel()
				.getDesignStructure();

		try {

			Iterator<IActivity> goalsIterator = modelProxy.findAllActivities(process).iterator();
			while (goalsIterator.hasNext()) {
				final IActivity currGoal = (IActivity) goalsIterator.next();

				// goal selection criterion - we resolve only activities with no
				// services already bound.
				// TODO this should be refined later.
				

				logger.debug("trying to resolve goal: "
						+ currGoal.getName() + " using WSML-based agent "
						+ this.identifier);

				List<RuleAdapter> adapters = resolveGoal(newDesignModelEvent, currGoal, operation);

//				boolean canAct = false;

				//compare results obtained from "resolveGoal" (adapters) with binding already present in the goal
				//and remove unnecessary adapters
				if (currGoal.getBindings()!=null && !currGoal.getBindings().isEmpty() && adapters!= null && !adapters.isEmpty()) {
					for (Iterator<RuleAdapter> iter = adapters.iterator(); iter.hasNext();) {
					RuleAdapter ra = iter.next();
						if (ra instanceof GoalWSAdapter) {
							GoalWSAdapter swsa = (GoalWSAdapter) ra;
							IBinding service = swsa.getService();
							for (IBinding b : currGoal.getBindings()) {
								if (b.getServiceReference().toString().equals(service.getServiceReference().toString())) {
									iter.remove();
								}
							}
						}
					}
				}
				
				//if (canAct != true) {
				//	continue;
				//}
				
				
				// if we find some solutions for the goal, we add it to the
				// list, and finish the loop.
				
				if (adapters != null && !adapters.isEmpty()) {
					for (RuleAdapter adapter: adapters){
						results.add(adapter);
//						if (results.size() == 1) {
//							break;
//						}
					}
					break; // Currently only 1 activity is resolved at once
				}
			}

		} catch (Throwable t) {
			logger.error(t + " resolveGoals() Fail");
			t.printStackTrace();
			return null;
		}

		return results;
	}

	/**
	 * Method for resolving single goal. It tries to find matches that are
	 * compatible with selected goal's annotations. It takes into account
	 * current operation requested from DTC, and only performs according to
	 * that.
	 * 
	 * @param dme
	 * @param goalTarget
	 * @param operation
	 * @return adapter to transform goal into Service or Template
	 * @throws MalformedURLException 
	 */
	private List<RuleAdapter> resolveGoal(NewDesignModelEvent dme,
			IActivity goalTarget, ComposerOperations operation) throws MalformedURLException {
		List<RuleAdapter> results = new ArrayList<RuleAdapter>();
		
		// list of found results
		List<WSLElement> ws = new ArrayList<WSLElement>();
		
		//check if there are any annotations attached
		if (isAnnotated(goalTarget) == false) {
			return null;
		}

		// resolve goal target into Webservice
		if (operation == ComposerOperations.RESOLVE_GOAL_WITH_WS
				|| operation == ComposerOperations.RESOLVE_PROCESS_WITH_WS) {

			List<WSLService> matches = reasoner.matchGoalMSM_WS(dme.getDesignModel().getDesignStructure(),
					goalTarget, MAX_NUMBER_FETCHED_RESULTS);
			if (matches != null) {
				ws.addAll(matches);
			} else {
				logger.debug("no match found");
			}
		}

		// resolve goal target into Template
		if (operation == ComposerOperations.RESOLVE_GOAL_WITH_TEMPLATE
				|| operation == ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE) {
			List<WSLTemplate> matches = reasoner.matchGoalMSM_Template(dme.getDesignModel().getDesignStructure(),
					goalTarget, MAX_NUMBER_FETCHED_RESULTS);
			if (matches != null) {
				ws.addAll(matches);
			} else {
				logger.debug("no match found");
			}
		}

		// resolve goal target into either Service or Template
		if (operation == ComposerOperations.RESOLVE_GOAL
				|| operation == ComposerOperations.RESOLVE_PROCESS) {
			// List<WSLElement> matches = reasoner.matchGoalMSM(null,
			// goalTarget);
			List<WSLService> serviceMatches = null;
			if (goalTarget.getBindings().size() < MAX_NUMBER_BINDINGS) { 
				serviceMatches = reasoner.matchGoalMSM_WS(dme.getDesignModel().getDesignStructure(),
					goalTarget, MAX_NUMBER_FETCHED_RESULTS);
			}
			List<WSLTemplate> templateMatches = null;
			if (goalTarget.getBindings().size() == 0){ // We resolve with templates only unbound activities.
				templateMatches = reasoner.matchGoalMSM_Template(
					dme.getDesignModel().getDesignStructure(), goalTarget, MAX_NUMBER_FETCHED_RESULTS);
			}
			if (serviceMatches != null) {
				ws.addAll(serviceMatches);
			} else {
				logger.debug("no service matches found...");
			}

			//if ((templateMatches != null) && (goalTarget.getBindings().size() == 0)) {
			if ((templateMatches != null) && (goalTarget.getBindings().size() == 0)) {
				ws.addAll(templateMatches);
			} else {
				logger.debug("no template matches found...");
			}
		}

		// we check if any match has been found
		if (ws == null || (ws != null && ws.size() == 0)) {
			return results;
		} else {
			// TODO here we can do any kind of ranking of results
			// We use all matchings but not ranked


			RuleAdapter adapter = null;
			
			for (WSLElement element:ws ){
				if (element instanceof WSLService) {
					// create adapter for goal -> Webservice transoformation
					adapter = createGoalWSAdapter(dme, this, goalTarget,
							"http://www.soa4all.eu#Adapter", (WSLService)element);
				}
	
				if (element instanceof WSLTemplate) {
					// create adapter for goal -> Webservice transoformation
					adapter = createGoalTemplateAdapter(dme, this, goalTarget,
							"http://www.soa4all.eu#Adapter", ((WSLTemplate) element));
				}
		
				results.add(adapter);
			}
		}
		return results;
	}

	private boolean isAnnotated(IActivity goalTarget) {
		
		boolean inputParam = true;
		boolean outputParam = true;
		boolean FC = true;
		
		//check if there are some input parameters, and if they are annotated.
		if (goalTarget.getInputParameters() == null || goalTarget.getInputParameters().size() == 0) {
			//logger.debug("%%&&");
			inputParam = false;
		} else {
			for (IParameter p : goalTarget.getInputParameters()) {
				if (p.getSemanticAnnotations() == null || p.getSemanticAnnotations().size() == 0 ) {
					inputParam = false;
				}
			}
		}
		
		//check if there are some output parameters, and if they are annotated.
		if (goalTarget.getOutputParameters() == null || goalTarget.getInputParameters().size() == 0) {
			outputParam = false;
		} else {
			for (IParameter p : goalTarget.getOutputParameters()) {
				if (p.getSemanticAnnotations() == null || p.getSemanticAnnotations().size() == 0 ) {
					outputParam = false;
				}
			}
		}

		//check if there is any FC
		if (goalTarget.getSemanticAnnotations() == null || goalTarget.getSemanticAnnotations().size() == 0) {
			FC = false;
		} 

		if (inputParam == false && outputParam == false && FC == false) {
			return false;
		} else {
			return true;
		}
	}

	// Create a templateURL given the templateURI assuming all templates located
	// in ComposerConfiguration.TEMPLATES_URL
	// with identical names to templateURI removing the namespace
	private URL getTemplateURL(URI templateURI) throws MalformedURLException {
		String templateName = templateURI.getFragment();
		URL url = null;
		try {
			url = new URL(DTComposerImpl.getInstance().getConfiguration()
					.getTEMPLATES_URL()
					+ templateName + ".xml");
		} catch (Exception e) {
			logger.error("not a URL, try loading from classpath.");
		} // Ignored

		return url;
	}

	// Create a template location given the template name assuming all templates
	// located in ComposerConfiguration.TEMPLATES_URL
	// with identical names to template name removing the namespace
	private String getTemplateLocation(String templateName)
			throws MalformedURLException {
		return DTComposerImpl.getInstance().getConfiguration()
				.getTEMPLATES_URL()
				+ templateName + ".xml";
	}

	private boolean checkApplicabilityConditions(IProcess process,
			IActivity goalActivity) {
		// TODO Rethink this method
		return true;
	}

	public RuleAdapter createGoalTemplateAdapter(NewDesignModelEvent dme,
			Agent agent, IActivity g, String a, WSLTemplate wsltemplate) {

		IProcess process = new ProcessImpl(wsltemplate.getTemplateReference());
		GoalTemplateAdapter adapter = new GoalTemplateAdapter(dme, agent);

		try {
//			process.setID(template);
//			Activity goalActivity = new ActivityImpl();
//			goalActivity.setID(g.getID());
//			goalActivity.setName(g.getName());
			
			// Assumptions (NFP)
			if (wsltemplate.getNFP() != null && !wsltemplate.getNFP().isEmpty()){
				for (String nfp:wsltemplate.getNFP()){
					ISemanticAnnotation sa = new SemanticAnnotationImpl();
					//ISSUE: NFP in service descriptions are not classified as REQUIREMENT | PREFERENCE
					sa.setType(IAnnotationType.META_DATA);
					sa.setReferenceURI(nfp);
					adapter.getAssumptions().add(sa);
				}
			}

			adapter.setIdentifier(new URI(a));
			adapter.setSource(g);
			adapter.setTarget(process);
//			adapter.setTemplateURI(template);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error("createAdapter fail");
			e.printStackTrace();
		}

		return adapter;
	}

	public GoalWSAdapter createGoalWSAdapter(NewDesignModelEvent dme,
			Agent agent, IActivity g, String a, WSLService wslservice) throws MalformedURLException {
		
		String serviceRef = wslservice.getServiceReference();
		String operation = wslservice.getOperations().get(0).getOperationName();
		String serviceName = wslservice.getServiceName();
		
		IBinding service = new BindingImpl();
		GoalWSAdapter adapter = new GoalWSAdapter(dme, agent);

		try {
			
			adapter.setIdentifier(new URI(a));
			adapter.setSource(g);
			adapter.setTarget(service);
			adapter.setRuleName("Semantic Matching");
			
			// Assumptions (NFP)
			if (wslservice.getNFP() != null && !wslservice.getNFP().isEmpty()){
				for (String nfp:wslservice.getNFP()){
					ISemanticAnnotation sa = new SemanticAnnotationImpl();
					//ISSUE: NFP in service descriptions are not classified as REQUIREMENT | PREFERENCE
					//ISSUE: avoid duplicated assigments
					sa.setType(IAnnotationType.META_DATA);
					sa.setReferenceURI(nfp);
					adapter.getAssumptions().add(sa);
				}
			}
			
			//Removing namespace from operation
			operation = operation.substring(operation.lastIndexOf("#") + 1, operation.length());
			adapter.setServiceOperation(operation);
			adapter.setServiceDefinitionURL(serviceRef);
			adapter.setServiceName(serviceName);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error("createAdapter fail");
			e.printStackTrace();
		}

		return adapter;
	}

	public void addAssumption(RuleAdapter adapter, String reference_uri,
			String expression) {
		try {
			ISemanticAnnotation sa = new SemanticAnnotationImpl();
			sa.setReferenceURI(reference_uri);
			sa.setExpression(expression);
			adapter.addAssumption(sa);
		} catch (Throwable t) {
			logger.error("add assumption fail");
			t.printStackTrace();
		}
	}

	public void applyConstraints(DesignModel ndm, String g, String a, String p) {
		try {

		} catch (Throwable t) {

		}

	}

	@Override
	public String toString() {
		return this.getIdentifier().toString();
	}

}
