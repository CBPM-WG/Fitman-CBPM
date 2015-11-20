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
package eu.atosresearch.seiplab.yourbpm.dtc.designmodel;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import prefuse.util.collections.IncompatibleComparatorException;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPIImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLService;



public class DesignModel implements Comparable{
	private URI identifier;
	private URL url;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	private AssignmentSet assignmentSet;
	private IProcess designStructure;
	private DesignModelStatus status = new DesignModelStatus();
	private int index;
	private URI agentInvokedURI;
	private URI designOperatorAppliedURI;
	private String goalReplaced;
	private String adapterUsed;
	private String appliedRule;
	private Map<URI, Map<String, List<WSLService>>> activitiesProcessedByDMA = new HashMap<URI, Map<String, List<WSLService>>>();
	private boolean dataFlowAdded = false;
	
	/**
	 * @return the dataFlowAdded
	 */
	public boolean isDataFlowAdded() {
		return dataFlowAdded;
	}

	/**
	 * @param dataFlowAdded the dataFlowAdded to set
	 */
	public void setDataFlowAdded(boolean dataFlowAdded) {
		this.dataFlowAdded = dataFlowAdded;
	}

	//recorded all design trasitions.
	private LinkedList<DesignTransition> transitionChain = new LinkedList<DesignTransition>();
	

	/**
	 * @return the transitionChain
	 */
	public LinkedList<DesignTransition> getTransitionChain() {
		return transitionChain;
	}

	/**
	 * @param transitionChain the transitionChain to set
	 */
	public void setTransitionChain(LinkedList<DesignTransition> transitionChain) {
		this.transitionChain = transitionChain;
	}
	
	public LinkedList<DesignTransition> addTransition(DesignTransition dt) {
		this.transitionChain.add(dt);
		return transitionChain;
	}

	public URI getAgentInvokedURI() {
		return agentInvokedURI;
	}

	public void setAgentInvokedURI(URI agentInvokedURI) {
		this.agentInvokedURI = agentInvokedURI;
	}

	public URI getDesignOperatorAppliedURI() {
		return designOperatorAppliedURI;
	}

	public void setDesignOperatorAppliedURI(URI designOperatorAppliedURI) {
		this.designOperatorAppliedURI = designOperatorAppliedURI;
	}

	public String getGoalReplaced() {
		return goalReplaced;
	}

	public String getAdapterUsed() {
		return adapterUsed;
	}

	public void setAdapterUsed(String adapterUsed) {
		this.adapterUsed = adapterUsed;
	}

	public void setGoalReplacedURI(String goalReplaced) {
		this.goalReplaced = goalReplaced;
	}

	public String getAppliedRule() {
		return appliedRule;
	}

	public void setAppliedRule(String appliedRule) {
		this.appliedRule = appliedRule;
	}

	public DesignModelStatus getStatus(){
		return status;
	}

	//@RdfProperty("http://eu.soa4all.wp6.designtimecomposer#hasAssignmentSet")
	public AssignmentSet getAssignmentSet() {
		return assignmentSet;
	}

	public void setAssignmentSet(AssignmentSet assignmentSet) {
		this.assignmentSet = assignmentSet;
	}

	//@Id
	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}
	
	public String getName (){
		return identifier.toString().substring(identifier.toString().indexOf("#")+1, identifier.toString().length());
	}

	//@RdfProperty("http://eu.soa4all.wp6.designtimecomposer#hasDesignStructure")
	public IProcess getDesignStructure() {
		return designStructure;
	}

	public void setDesignStructure(IProcess process) {
		this.designStructure = process;
	}
	
	public boolean hasGoal() {
		
		
		return true;
	}
	
	/**
	 * Detects incompatible global requirements in the assignment set
	 * @param reasoner
	 * @return
	 * @throws IncompatibleComparatorException 
	 */
	public boolean matchRequirements(ReasonerAPIImpl reasoner) throws Exception {
		List<String> localNFP = new ArrayList<String>();
		List<String> globalNFP = new ArrayList<String>();
		for (ISemanticAnnotation req: modelProxy.getRequirements(this.getDesignStructure())){
			globalNFP.add (req.getReferenceURI());
		}
		for (ISemanticAnnotation assignment: assignmentSet.getAssignments()){
			localNFP.add(assignment.getReferenceURI());
		}
		
		//FIXME Use checkNFPIncompatibility based on SPARQL
//		HashMap<String, List<String>> incompatibilities = reasoner.checkNFPIncompatibility (localNFP, globalNFP);
//		return incompatibilities.isEmpty();
		
		return true;
	}
	
	/**
	 * Detects incompatible global preferences in the assignment set
	 * @param reasoner
	 * @return
	 * @throws IncompatibleComparatorException 
	 */
	public boolean matchPreferences(ReasonerAPIImpl reasoner) throws Exception {
		List<String> localNFP = new ArrayList<String>();
		List<String> globalNFP = new ArrayList<String>();
		for (ISemanticAnnotation req: modelProxy.getPreferences(this.getDesignStructure())){
			globalNFP.add (req.getReferenceURI());
		}
		for (ISemanticAnnotation assignment: assignmentSet.getAssignments()){
			localNFP.add(assignment.getReferenceURI());
		}
		
		//FIXME Use checkNFPIncompatibility based on SPARQL
//		HashMap<String, List<String>> incompatibilities = reasoner.checkNFPIncompatibility (localNFP, globalNFP);
//		return incompatibilities.isEmpty();
		
		return true;
	}
	
//	public boolean hasIncompatiblePreferences(Agent agent, ReasonerAPI reasoner) throws Exception {
//		Collection<SemanticAnnotation> incompatiblePreferences = new ArrayList<SemanticAnnotation>();
//		//ArrayList<Activity> activities = ;
//		
//		ArrayList<String> local = new ArrayList<String>();
//		ArrayList<String> global = new ArrayList<String>();
//		
//		for(ISemanticAnnotation preference: LPMLProxy.getPreferences(this.getDesignStructure())){
//			global.add(preference.getReferenceURI());
//		}
//		
//		if (global.isEmpty()){ //No global requirements to check
//			return false;
//		}
//		
//		for (Activity activity : LPMLProxy.findAllGoals(this.designStructure)) {
//			for (ISemanticAnnotation localSA: activity.getSemanticAnnotations()){
//				if (localSA.getType() == IAnnotationType.PREFERENCE) {
//					local.add(localSA.getReferenceURI());
//				}
//			}
//		}
//
//		if (reasoner.getReasonerClient().checkNFPIncompatibility(local, global).isEmpty() == true) {
//			//there is no incompatible requirements
//			return false;
//		} else {
//			//incompatible requirements found!
//			return true;
//		}
//	}

	/*
	public boolean hasIncompatibleRequirements(Agent agent, ReasonerAPI reasoner) throws Exception {
		Collection<SemanticAnnotation> incompatibleRequirements = new ArrayList<SemanticAnnotation>();
		for(ISemanticAnnotation requirement: LPMLProxy.getRequirements(this.getDesignStructure())){
			for (ISemanticAnnotation assignment: assignmentSet.getRequirements()){
				if(!reasoner.match(assignment, requirement)){
					incompatibleRequirements.add(requirement);
				}
			}
		}
		return !incompatibleRequirements.isEmpty();
	}
	*/

	
	public String toString(){
		return "DesignModel[" + this.getIdentifier() + "]";
	}

	public void setIndex(int i) {
		this.index = i;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	// Ranking is based on number of unbound Activities (Goals)
	// TODO: Consider to rank according to the number of transformations applied.
	// and more complex ranking approaches based on sort of matching
	public int compareTo(Object o) {
		int result = 0;
		if (!(o instanceof DesignModel)){
			System.err.println("Trying to compare a DesignModel with other class: " + o.getClass());
		}
		int i1 = modelProxy.findAllUnboundActivities(this.getDesignStructure()).size();
		int i2 = modelProxy.findAllUnboundActivities(((DesignModel)o).getDesignStructure()).size();
		
		if (i1>i2) result = 1;
		else if (i1<i2)result = -1;
		else if (i1==i2){ //number of transformations applied.
			result = this.getTransitionChain().size() >= ((DesignModel)o).getTransitionChain().size()? -1: +1;
		}
		
		return result;
	}

	public void addActivityProcessedByDMA(URI dmaId, String activityId, List<WSLService> services) {
		Map<String, List<WSLService>> map = activitiesProcessedByDMA.get(dmaId);
		if (map == null){
			map = new HashMap<String, List<WSLService>>();
			map.put(activityId, services);
			activitiesProcessedByDMA.put(dmaId, map);
		}
		map.put(activityId, services);
		
	}

	public Map<String, List<WSLService>> getActivitiesProcessedByDMA(URI dmaId) {
		return activitiesProcessedByDMA.get(dmaId);
	}

}
