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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;


import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.ComposerOperations;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.Agent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.NewDesignModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignTransition;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;

import org.soa4all.lpml.impl.BindingImpl;

public class GoalWSAdapter extends RuleAdapter {
	// Logger
	static Logger logger = Logger.getLogger(GoalWSAdapter.class);
	
	private IBinding service  = new BindingImpl ();
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);

	public GoalWSAdapter (NewDesignModelEvent dme, Agent agent){
		super (dme, agent);
	}
	
	public void initAdapter (String ruleName) {
		setRuleName(ruleName);
		String adapterURI = agent.getIdentifier() + "GWSAdapter_" + ruleName;
		try {
			this.setIdentifier(new URI(adapterURI));
			this.setTarget(service);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("createAdapter fail");
			e.printStackTrace();
		}
	}
	
	public IBinding getService() {
		return service;
	}
	
	public void setServiceURI(String serviceURI) throws URISyntaxException {
		//leave for compatibility reason
		//TODO remove later complately
		
		//service.setID(agent.getNamespace().getNamespace() + serviceURI);
		//service.setID(serviceURI);
	}
	
	public void setServiceDefinitionURL (String definitionURL) throws MalformedURLException{
		this.service.setServiceReference(new URL(definitionURL));
	}
	
	public void setServiceOperation (String operation) throws URISyntaxException{
		this.service.setOperation(new URI (operation));
	}
	
	public void setServiceName (String name) throws URISyntaxException{
		this.service.setName(name);
	}
	
	@Override
	protected boolean isAcceptedOperation() {
		// Accepted operations for this Adapter are those which request to resolve goals with WS
		boolean result = false;
		ComposerOperations operation = dme.getBlackBoardControlAgent().getOperation();
		if (operation == ComposerOperations.RESOLVE_PROCESS ||
			operation == ComposerOperations.RESOLVE_GOAL ||
			operation == ComposerOperations.RESOLVE_GOAL_WITH_WS || 
			operation == ComposerOperations.RESOLVE_PROCESS_WITH_WS
			){
			result = true;
		}
		return result;
	}
	
	
	public boolean applyAdapter() {
		boolean result = false;
		try {			
			
			// Checking this rule was not previously applied
			//Checking IBinding is not included already in IBinding list
			if (isAdapterApplied ()){
				//
				return result;
			}
			
			DesignModel designModel = dme.getDesignModel();
			
			// Clone designModel
			DesignModel newDesignModel = (DesignModel)modelProxy.cloneLPMLObject(designModel);


			
			//BugFix - we need to obtain Goal from the cloned model, not the original one.
			IActivity goal = (IActivity) modelProxy.getFlowObjectByIdentifier(
					newDesignModel.getDesignStructure(), new URI(((IActivity) getSource()).getID()));

			
			IActivity goal_prev = (IActivity) modelProxy.getFlowObjectByIdentifier(
					designModel.getDesignStructure(), new URI(((IActivity) getSource()).getID()));
//			System.out.println(goal_prev.hashCode());
//			System.out.println(goal.hashCode());

			
			// Replacing Goal with Service
			result = modelProxy.substitute(newDesignModel.getDesignStructure(), goal, service);
			if (!result){
				//Not modified, no posted a new model
				return result;
			}
			
			// Adding Adapter assignments (Assumptions) to new model
			addAssumptions (getAssumptions(), newDesignModel.getAssignmentSet().getAssignments());
//			newDesignModel.getAssignmentSet().addAssignments((getAssumptions()));

			//XXX add DesignTransition information
			DesignTransition dt = new DesignTransition();
			dt.setSourceURI(Integer.toString(goal.getID().hashCode()));
			dt.setTargetURI(Integer.toString(service.getServiceReference().hashCode()));
			newDesignModel.addTransition(dt);
			
			// Notifying new model to blackboard agent
			newDesignModel.setAppliedRule(getRuleName());
			dme.getBlackBoardControlAgent().updateDesignModel(designModel,
					newDesignModel, agent.getIdentifier(), agent.getIdentifier(), "GoalWSAdapter",((IActivity) getSource()).getName() ,getRuleName());

			
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

	private boolean isAdapterApplied() throws URISyntaxException{
		IActivity goal = (IActivity) modelProxy.getFlowObjectByIdentifier(
				dme.getDesignModel().getDesignStructure(), new URI(((IActivity) getSource()).getID()));
		return hasBinding (goal.getBindings(), service);
	}

	private boolean hasBinding(List<IBinding> bindings, IBinding service) {
		boolean result = false;
		for (IBinding b: bindings){
			if (b.getServiceReference().toString().equals(service.getServiceReference().toString()) &&
				b.getOperation().toString().equals(service.getOperation().toString())){
				result = true;
				break;
			}
		}
		return result;	
	}

	private String getServiceName(URI serviceURI) {
		String serviceName = serviceURI.toString().substring(agent.getIdentifier().toString().length());
		return serviceName;
	}

	@Override
	protected boolean isServiceAlreadyBound (IActivity activity){
		boolean result = false;
		if (!activity.getBindings().isEmpty()){
			for (IBinding b : activity.getBindings()){
				if (b.getServiceReference().equals(service.getServiceReference())){
					result = true;
				}
			}
		}
		return result;
	}
}
