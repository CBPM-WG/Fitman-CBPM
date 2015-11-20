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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.soa4all.lpml.impl.ActivityImpl;
import org.soa4all.lpml.impl.ConnectorImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IFlow;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IConnector;
import eu.atosresearch.seiplab.yourbpm.dtc.ComposerOperations;
import eu.atosresearch.seiplab.yourbpm.dtc.DTComposerImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.TaggedModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.thread.ThreadPool;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignTransition;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;
import eu.atosresearch.seiplab.yourbpm.dtc.slo.api.CompatibilityInfo;
import eu.atosresearch.seiplab.yourbpm.dtc.slo.api.ModelIncompleteException;
import eu.atosresearch.seiplab.yourbpm.dtc.slo.api.OperationIO;
import eu.atosresearch.seiplab.yourbpm.dtc.slo.api.SemanticCompatibilityException;

public class SemanticLinkOperatorAgent extends LinkOperatorAgent implements ApplicationListener, InitializingBean, Runnable {
	static Logger logger = Logger.getLogger(SemanticLinkOperatorAgent.class);
	private String[] knowledgeOntologies;
	
	private List<String> ontologyKnowledge;
	//private String identifier;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);
	
	public List<String> getOntologyKnowledge() {
		return ontologyKnowledge;
	}

	private String SLOEndpoint;
	
	public String getSLOEndpoint() {
		return SLOEndpoint;
	}

	private TaggedModelEvent newDesignModelEvent;
	
	public SemanticLinkOperatorAgent(List<String> initialKnowledge, String SLOEndpoint) {
		//super(identifier);
		
		this.ontologyKnowledge = initialKnowledge;
		
		
		this.knowledgeOntologies = new String[]{
				//update paths
				"DesignTimeComposer_v2/src/main/resources/ontologies/WP9/domain-specific-ontologies/ShopMaintenanceOntology.wsml",
				"DesignTimeComposer_v2/src/main/resources/ontologies/WP9/context/SupplierCoverage.wsml",
				"DesignTimeComposer_v2/src/main/resources/ontologies/WP9/context/SeasonOntology.wsml",
				"DesignTimeComposer_v2/src/main/resources/ontologies/WP9/FC/ShopClassification.wsml",
				
				"DesignTimeComposer_v2/src/main/resources/ontologies/wp7-io.wsml"
				
				
				};
		
		this.SLOEndpoint = SLOEndpoint;
	}

	public SemanticLinkOperatorAgent(String[] initialKnowledge) {
		this.ontologyKnowledge = Arrays.asList(initialKnowledge);
	}
	
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof TaggedModelEvent) {
			newDesignModelEvent = (TaggedModelEvent) applicationEvent;
			if (DTComposerImpl.getInstance().getConfiguration().isThread()){
				ThreadPool.getInstance().execute(this);
			} else {
				run();
			}
		}
	}
	
	@Override
	public void run() {
//		newDesignModelEvent.getBlackBoardControlAgent().increaseSizeDMAInvocationPool();
		try {
			runSLOAgent();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newDesignModelEvent.getBlackBoardControlAgent().decreaseSizeDMAInvocationPool();
	}
	
	private void runSLOAgent() throws URISyntaxException{
		//System.out.println(newDesignModelEvent.getBlackBoardControlAgent().getOperation());
		//System.out.println("SLO DMA was notified about model " + newDesignModelEvent.toString() + ". Checking... ");
		//System.out.println(newDesignModelEvent.getDesignModel().getStatus());
		logger.info("SLO DMA was notified about model " + newDesignModelEvent.toString() + ". Checking... ");
		logger.debug(newDesignModelEvent.getDesignModel().getStatus());
		logger.debug(newDesignModelEvent.getDesignModel().getIndex());
		
		ComposerOperations operation = newDesignModelEvent.getBlackBoardControlAgent().getOperation();
		
		//if it's neither RESOLVE_PROCESS nor RESOLVE_DATAFLOW then return.
		if (!(operation == ComposerOperations.RESOLVE_PROCESS || operation == ComposerOperations.RESOLVE_DATAFLOW))  {
			return;
		}
		
		// Checking if designModel has been previously analyzed and
		// tagged as inadmissible
		if (newDesignModelEvent.getDesignModel().getStatus().isInadmissible()) {
			return;
		}
		
		if (newDesignModelEvent.getDesignModel().getStatus().isSolution()) {
			return;
		}
	
		// We should check completeness regardless requested operation by analyzing whether there is still a unbound activity or not
		// since design model completeness depends on the target (process or operation)
		if (newDesignModelEvent.getDesignModel().getStatus().isComplete() == false || 
				!allGoalsResolved(newDesignModelEvent.getDesignModel())) {
			return;
		}
		
		if (newDesignModelEvent.getDesignModel().getStatus().isSuitable() == false) {
			return;
		}
		
	
		//the main routine for analysing Model's IO and creating connectors 
		//System.out.println("SemanticLinkOperator acts upon model " + newDesignModelEvent.toString());
		logger.info("SemanticLinkOperator acts upon model " + newDesignModelEvent.toString() + " " + newDesignModelEvent.getDesignModel().getIndex());
		
		//clone model
		//by convenience, we clone model before and then we perform semantic matchings on clonned one,
		//so later we don't need to map I/O parameters between old model and new one.
		//if there's no compatible IOs, the new model should be garbage collected.
		DesignModel previousDesignModel = newDesignModelEvent.getDesignModel();
		DesignModel newDesignModel = (DesignModel)modelProxy.cloneLPMLObject(previousDesignModel);

		//retrieve matchings between Outputs and Inputs
		HashMap<OperationIO, CompatibilityInfo> matchings = analyseSemanticCompatibility(newDesignModel);
		
		if (matchings.isEmpty() != true) {
			//add connectors to the new model
			createConnectors(newDesignModel, matchings);
			
			//add design transition info to the model
			DesignTransition dt = new DesignTransition();
			dt.setSourceURI(Integer.toString(previousDesignModel.getDesignStructure().getID().hashCode()));
			dt.setTargetURI(Integer.toString(newDesignModel.getDesignStructure().getID().hashCode()));
			newDesignModel.addTransition(dt);
			
			newDesignModel.setDataFlowAdded(true);
			
			// Notifying new model to blackboard agent
			newDesignModel.setAppliedRule("SLO");
			newDesignModelEvent.getBlackBoardControlAgent().updateDesignModel(previousDesignModel,
					newDesignModel, this.getIdentifier(), this.getIdentifier(), "SLOAgent", "IO Connectors", "IO Connectors");
			//send model
			//(newDesignModelEvent.getBlackBoardControlAgent()).notifyAgentResponse(newDesignModelEvent);
			//(newDesignModelEvent.getBlackBoardControlAgent()).notifyAgentResponse(newDesignModelEvent);
		} 
	}

	/**
	 * Method for calculating semantic compatibility between Outputs and Inputs in the model.
	 * It analyses process structure and activities' sequence and checking if activity's Inputs
	 * are satisfied by preceding activities' Outputs.
	 * 
	 * @param designmodel
	 * @return matching map between all process Outputs and Inputs
	 */
	private HashMap<OperationIO, CompatibilityInfo> analyseSemanticCompatibility(DesignModel designmodel) {
		IProcess process = designmodel.getDesignStructure(); 
		
		LinkedList<ActivityImpl> activities = getActivitiesOrder(process);
		
		HashMap<OperationIO, CompatibilityInfo> allMatchings = new HashMap<OperationIO, CompatibilityInfo>();
		try {
			if (activities != null ) {
				for (ActivityImpl a : activities) {
					ArrayList<ActivityImpl> r = getPrecedingActivities(process, a);
					
					HashMap<OperationIO, CompatibilityInfo> localMatchings = AnalyseIO(a, r);

					if (localMatchings != null) {
						allMatchings.putAll(localMatchings);
					}
				}
			}
			
		} catch (SemanticCompatibilityException e) {
			//exception - not all Inputs are satisfied.
			logger.error(" * From: SemanticLinkOperatorAgent | I/O matching failed for model " + e.getActivity().getName());
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			//model is not yet complete (in normal cases that situation shouldn't happen)
			logger.error(" * From: SemanticLinkOperatorAgent | model is missing services in activity " + e.getActivity().getName());
			e.printStackTrace();
		}
		
		return allMatchings;
		
	}
	
	private void createConnectors(DesignModel newDesignModel, HashMap<OperationIO, CompatibilityInfo> matchings) {
		
		for (OperationIO input : matchings.keySet()) {
			IConnector connector = new ConnectorImpl();
			
			CompatibilityInfo iomapping = matchings.get(input);
			
			connector.setOutputParameter(iomapping.getInput().getParameter());
			
			for (OperationIO out : iomapping.getOutput()) {
				//connector.addInputParameter(out.getParameter());
				//FIXME Next line does not compile
//				connector.getInputParameters().add(out.getParameter());
			}
			
			//TODO implement connector type
			//if (iomapping.getConnectionType() != null) {
			//	connector.setConnectorType(iomapping.getConnectionType());
			//}
			
			//TODO attach connector to the parameter
			//newDesignModel.getDesignStructure().addProcessElement(connector);
			input.getActivity().getConnectors().add(connector);
		}
	}

	/**
	 * Main method for analysing if Inputs for given activity is satisfied by preceding Outputs.
	 * For low-level checking, it invokes the SLO Api service that prepares the data for SLO invocation.
	 * If I and O are compatible, necessary mapping is created. 
	 * 
	 * @param activity
	 * @param combinedOutputs
	 * @return
	 * @throws SemanticCompatibilityException
	 * @throws ModelIncompleteException
	 */
//	private HashMap<OperationIO, LinkedList<OperationIO>> AnalyseIO(Activity activity, ArrayList<Activity> combinedOutputs) throws SemanticCompatibilityException, ModelIncompleteException {
	private HashMap<OperationIO, CompatibilityInfo> AnalyseIO(ActivityImpl activity, ArrayList<ActivityImpl> combinedOutputs) throws SemanticCompatibilityException, ModelIncompleteException {

		//lists of I/O
		LinkedList<OperationIO> inputs = new LinkedList<OperationIO>();
		LinkedList<OperationIO> outputs = new LinkedList<OperationIO>();
		
		LinkedList<CompatibilityInfo> compatInfo = null; 
		
		//helper mapping between input (operationIO) and corresponding best matchings: Outs + related score (compatibilityInfo)
		HashMap<OperationIO, CompatibilityInfo> matchings = new HashMap<OperationIO, CompatibilityInfo>();
		
		try {
			//create list of all inputs that must be satisfied
			for (IParameter par : activity.getInputParameters()) {
				OperationIO in = new OperationIO();
				in.setActivity(activity);
				
				//check if services are bound
//				if (activity.getConversation() == null || activity.getConversation().getServices() == null || activity.getConversation().getServices().size() == 0) {
//					return null;
//				}
				if (activity.getBindings() == null || activity.getBindings().size() == 0) {
					return null;
				}
				
				//as it is now we use only 1 service
				in.setService(activity.getBindings().get(0));
				in.setParameter(par);
				inputs.add(in);
				
				//debug - create webservice
				//CreateWebService cw = new CreateWebService();
				//WebService ws = cw.createWebServiceFromAnnotations(par.getSemanticAnnotations(), null);
				//System.out.println("ws (I) created" + ws.hashCode());
			}
			
			//create list of all available outputs
			for (ActivityImpl a : combinedOutputs) {
				for (IParameter par : a.getOutputParameters()) {
					
					OperationIO out = new OperationIO();
					out.setActivity(a);
					
					//check if services are bound
//					if (a.getConversation() == null || a.getConversation().getServices() == null || a.getConversation().getServices().size() == 0) {
//						return null;
//					}
					if (a.getBindings() == null || a.getBindings().size() == 0) {
						return null;
					}
					
					out.setService(a.getBindings().get(0));
					out.setParameter(par);
					outputs.add(out);

					//debug - create webservice
//					CreateWebService cw = new CreateWebService();
//					WebService ws = cw.createWebServiceFromAnnotations(null, par.getSemanticAnnotations());
//					System.out.println("ws (O) created" + ws.hashCode());
				}
			}
			
			if (inputs.size() == 0) {
				//we don't need to satisfy any input
				return null;
			}
			
			//we compute i/o rating
			//XXX here we can turn on/off SLO
			//FIXME Create SLOAPI.SLO method using SPARQL
// 			compatInfo = SLOApi.SLO(inputs, outputs, new LinkedList<String>(ontologyKnowledge), SLOEndpoint);
			
			//now we need to choose best i/o matching
			for (CompatibilityInfo c : compatInfo) {
				if (c.getCompatibilityScore() == 1) {
					if (matchings.containsKey(c.getInput()) == false) {
						matchings.put(c.getInput(), c);
						//TODO
						//consider rewriting to include more IO matching possibilities
					} 		
				}
			}
			
			// to check if ALL inputs are satisfied by outputs from preceding activities use
			//if (matchings.size() != inputs.size()) {
			// WARNING - currently this feature is disabled, allowing for incomplete IO matchings.

			//check if ANY matchig has been found
			if (matchings.size() == 0) {
				//none of Inputs are satisfied. 
				return null;
			} else {
				return matchings;
			}
			
		} catch (IndexOutOfBoundsException e) {
			//ModelIncompleteException mie = new ModelIncompleteException("Activity in model is missing service", activity);
			logger.error("It shouldn't have happened!");
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Analyse process elements, and retrieve all activities in a form of the list
	 * 
	 * @param process
	 * @return
	 */
	private LinkedList<ActivityImpl> getActivitiesOrder(IProcess process) {
		LinkedList<ActivityImpl> alist = new LinkedList<ActivityImpl>();
		LinkedList<IProcessElement> pe = new LinkedList<IProcessElement>();
		
//		IProcessElement curr = process.getStartElement();
//		Activity end = process.getEndElement();

		IProcessElement curr = modelProxy.getStartElement(process);
		IActivity end = modelProxy.getEndElement(process);
		
		pe.add(curr);	
		int index = 0;
		
		do {
			curr = pe.get(index);
			
			//for (Flow f : LPMLProxy.getFlows(process, curr)) {
			for (Iterator<IFlow> it = modelProxy.getFlows(process, curr); it.hasNext(); ) {
				IFlow f = it.next();
				if (! pe.contains(f.getDestination())) {
					pe.add(f.getDestination());
					//System.out.println("Added!");
				} else {
					//System.out.println("A is already there!");
				}

				if ((f.getDestination() instanceof IActivity) && (! alist.contains((IActivity) f.getDestination()))) {
					
					if (!(f.getDestination().getID().compareTo(end.getID()) == 0)) {
						alist.add((ActivityImpl) f.getDestination()); 	
					}
				}
			}
			index++;
			
		} while ( (pe.size() > index));
		
		return alist;
	}

	/**
	 * Utility method for retrieving all preceding activities for the one given as a parameter
	 * 
	 * @param process
	 * @param a activity
	 * @return list of preceding activities
	 */
	private ArrayList<ActivityImpl> getPrecedingActivities(IProcess process, IActivity a) {
		//ArrayList<Activity> result = new ArrayList<Activity>();
		
		IProcessElement currActivity = a;
		LinkedList<IProcessElement> elements = new LinkedList<IProcessElement>();
		
		int currentPosition = 0;
		
		elements.add(a);
		
		while (currentPosition < elements.size()) {
	
			currActivity = elements.get(currentPosition);
			
			//for (Flow f : currActivity.getFlows()) {
			for (Iterator<IFlow> it = modelProxy.getFlows(process, currActivity); it.hasNext(); ) {
				IFlow f = it.next();
				if (f.getDestination() == currActivity) {
					
					//if (f.getSource() instanceof Activity) {
					IProcessElement precAct = f.getSource();
					if (elements.contains(precAct) == false) {
						elements.add(precAct);
					}
				}
			}
			
			currentPosition++;
		}
		
		ArrayList<ActivityImpl> result = new ArrayList<ActivityImpl>();
		
		for (IProcessElement pe : elements) {
			if ((pe instanceof ActivityImpl) && (pe.getID().compareTo(a.getID()) != 0)) {
				result.add((ActivityImpl)pe);	
			}
		}
		
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
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
}
