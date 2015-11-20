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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.soa4all.lpml.impl.ActivityImpl;
import org.soa4all.lpml.impl.ParameterImpl;
import org.soa4all.lpml.impl.SemanticAnnotationImpl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.Agent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.NewDesignModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.common.namespace.Namespace;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignTransition;
import eu.atosresearch.seiplab.yourbpm.dtc.exception.DTComposerException;
import eu.atosresearch.seiplab.yourbpm.dtc.lpml.legacy.KnowledgeComponent;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;

public class RuleAdapter extends KnowledgeComponent {
	// Logger
	static Logger logger = Logger.getLogger(RuleAdapter.class);
	
	private Collection<IProcessElement> mappings = new ArrayList<IProcessElement>();
	private Collection<ISemanticAnnotation> assumptions = new ArrayList<ISemanticAnnotation>();
	protected Collection<IParameter> inputs = new ArrayList<IParameter>();
	protected Collection<IParameter> outputs = new ArrayList<IParameter>();
	private Object source;
	private Object target;
	protected IActivity ruleGoal = new ActivityImpl();
	protected NewDesignModelEvent dme;
	protected Agent agent;
	private String ruleName;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public RuleAdapter() {
		
	}

	public RuleAdapter(NewDesignModelEvent dme, Agent agent) {
		this.dme = dme;
		this.agent = agent;
	}

	public Collection<IProcessElement> getMappings() {
		return mappings;
	}

	public void setMappings(Collection<IProcessElement> mappings) {
		this.mappings = mappings;
	}

	public Collection<ISemanticAnnotation> getAssumptions() {
		return assumptions;
	}

	public void setAssumptions(Collection<ISemanticAnnotation> assumptions) {
		this.assumptions = assumptions;
	}

	public void addAssumption(ISemanticAnnotation assumption) {
		this.assumptions.add(assumption);
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object origin) {
		this.source = origin;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object destination) {
		this.target = destination;
	}
	
	public Collection<IParameter> getInputs(){
		return this.inputs;
	}
	
	public void addInput (String input) throws URISyntaxException{
		URI inputuri = getQName(input);
		ISemanticAnnotation inputsa = new SemanticAnnotationImpl();
		inputsa.setType(IAnnotationType.META_DATA);
		inputsa.setReferenceURI(inputuri.toString());
		IParameter inputpar = new ParameterImpl ();
		inputpar.getSemanticAnnotations().add(inputsa);
		this.inputs.add(inputpar);
	}
	
	public Collection<IParameter> getOutputs(){
		return this.outputs;
	}
	
	public void addOutput (String output) throws URISyntaxException{
		URI outputuri = getQName(output);
		ISemanticAnnotation outputsa = new SemanticAnnotationImpl();
		outputsa.setType(IAnnotationType.META_DATA);
		outputsa.setReferenceURI(outputuri.toString());
		IParameter outputpar = new ParameterImpl ();
		outputpar.getSemanticAnnotations().add(outputsa);
		this.outputs.add(outputpar);
	}
	

	protected IActivity match(IProcess process, IActivity ruleGoal) throws Exception {
		IActivity target = null;
		Iterator<IActivity> goalsIterator = modelProxy.findAllActivities(process).iterator();
		while (goalsIterator.hasNext()) {
			IActivity activity = (IActivity) goalsIterator.next();
			if (activity.isHumanTask()){
				continue; //HT activities ignored
			}
			if (match(activity, ruleGoal)) {
				target = activity;
				break;
			}
		}
		return target;
	}

	/**
	 * TODO: Improve method to consider separately matching of FC, I, O
	 * @param targetGoal
	 * @param ruleGoal
	 * @return
	 * @throws Exception
	 */
	protected boolean match(IActivity targetActivity, IActivity ruleGoal) throws Exception {

		// If this is a GoalTemplateAdapter then already bound activities are ignored.
		if (this instanceof GoalTemplateAdapter && !targetActivity.getBindings().isEmpty()){
			return false;
		}
		
		//If this is a GoalWSAdapter we check whether this WS was already bound
		if (this instanceof GoalWSAdapter && isServiceAlreadyBound(targetActivity)){
				return false;
		}
		
		// If targetGoal or template has not functional classification it is an
		// error.
		Collection<ISemanticAnnotation> sasGoal = null;
		if (targetActivity != null && targetActivity.getSemanticAnnotations() != null) {
			sasGoal = targetActivity.getSemanticAnnotations();
		}

		Collection<ISemanticAnnotation> sasRule = null;
		if (ruleGoal != null && ruleGoal.getSemanticAnnotations() != null) {
			sasRule = ruleGoal.getSemanticAnnotations();
		}
		

		if (sasGoal == null || sasRule == null || sasGoal.isEmpty() || sasRule.isEmpty()) {
//			throw new DTComposerException(new Exception("Either process goal or template rule has not functional classification entries"));
			System.err.print("Either process goal or template rule has not functional classification entries");
			return false;
		} else {
			// FC are mandatory, I/O are optional, if set, they are matched, otherwise ignored for overall matching
			// TODO: consider mandatory the following combination: at least FC or I/O
			Collection<ISemanticAnnotation> fcActivity = getFC (sasGoal);
			Collection<ISemanticAnnotation> fcRule = getFC (sasRule);
			Collection<ISemanticAnnotation> inputActivity = getInputs(targetActivity);
			Collection<ISemanticAnnotation> inputRule = getInputsAnnotations();
			Collection<ISemanticAnnotation> outputActivity = getOutputs(targetActivity);
			Collection<ISemanticAnnotation> outputRule = getOutputsAnnotations();
			
			boolean fcmatch = false;
			boolean fcset = false;
			if (!fcActivity.isEmpty() && !fcRule.isEmpty()){
				fcmatch = matchFC(fcActivity, fcRule);
				fcset = true;
			}
			
			boolean iomatch = false;
			boolean ioset = false;
			if (!inputActivity.isEmpty() && !inputRule.isEmpty() && 
				!outputActivity.isEmpty() && !outputRule.isEmpty()){
				iomatch = matchInput (inputActivity, inputRule) && 
			    matchOutput (outputActivity, outputRule);
				ioset = true;
			}
						
			return fcset && fcmatch && ((ioset && iomatch) || !ioset); 
		}

	}

	private Collection<ISemanticAnnotation> getInputsAnnotations() {
		Collection<ISemanticAnnotation> ins = new ArrayList<ISemanticAnnotation>();
		
		for (IParameter par: getInputs()){
			ins.addAll (par.getSemanticAnnotations());
		}
		
		return ins;
	}

	private Collection<ISemanticAnnotation> getOutputsAnnotations() {
		Collection<ISemanticAnnotation> ins = new ArrayList<ISemanticAnnotation>();
		
		for (IParameter par: getOutputs()){
			ins.addAll (par.getSemanticAnnotations());
		}
		
		return ins;
	}
	
	private Collection<ISemanticAnnotation> getInputs(IActivity targetActivity) {
		Collection<ISemanticAnnotation> ins = new ArrayList<ISemanticAnnotation>();
		
		for (IParameter par: targetActivity.getInputParameters()){
			for (ISemanticAnnotation sa: par.getSemanticAnnotations()){
				if (sa.getType().equals(IAnnotationType.META_DATA)){
					ins.add (sa);
				}
			}
		}
		
		return ins;
	}
	
	private Collection<ISemanticAnnotation> getOutputs(IActivity targetActivity) {
		Collection<ISemanticAnnotation> ins = new ArrayList<ISemanticAnnotation>();
		
		for (IParameter par: targetActivity.getOutputParameters()){
			for (ISemanticAnnotation sa: par.getSemanticAnnotations()){
				if (sa.getType().equals(IAnnotationType.META_DATA)){
					ins.add (sa);
				}
			}
		}
		
		return ins;
	}

	private Collection<ISemanticAnnotation> getFC(Collection<ISemanticAnnotation> sas) {
		Collection<ISemanticAnnotation> fcs = new ArrayList<ISemanticAnnotation>();
		
		for (ISemanticAnnotation sa: sas){
			if (sa.getType().equals(IAnnotationType.FUNCTIONAL_CLASSIFICATION)){
				fcs.add (sa);
			}
		}
		
		return fcs;
	}
	
	


	private boolean matchFC(Collection<ISemanticAnnotation> sasGoal,
			Collection<ISemanticAnnotation> sasRule) throws Exception {
		boolean result = true;
		for (ISemanticAnnotation saGoal : sasGoal) {
			boolean match = false;
			for (ISemanticAnnotation saRule : sasRule) {
//				if (!saRule.getType().equals(saGoal.getType())) {
//					continue;
//				}
				if (agent.getReasoner().match(saGoal, saRule)) { // TODO Move to ISemanticAnnotation
					match = true;
					break;
				}
			}
			if (!match) {
				result = false;
				break;
			}
		}

		return result;
	}

	
	private boolean matchInput(Collection<ISemanticAnnotation> sasGoal,
			Collection<ISemanticAnnotation> sasRule) throws Exception {
		boolean result = true;
		
		for (ISemanticAnnotation saRule : sasRule) {
			boolean match = false;
			for (ISemanticAnnotation saGoal : sasGoal) {
//				if (!saRule.getType().equals(saGoal.getType())) {
//					continue;
//				}
				if (agent.getReasoner().match(saGoal, saRule)) { // TODO Move to ISemanticAnnotation
					match = true;
					break;
				}
			}
			if (!match) {
				result = false;
				break;
			}
		}

		return result;
	}
	
	private boolean matchOutput(Collection<ISemanticAnnotation> sasGoal,
			Collection<ISemanticAnnotation> sasRule) throws Exception {
		boolean result = true;
		for (ISemanticAnnotation saGoal : sasGoal) {
			boolean match = false;
			for (ISemanticAnnotation saRule : sasRule) {
//				if (!saRule.getType().equals(saGoal.getType())) {
//					continue;
//				}
				if (agent.getReasoner().match(saRule, saGoal)) { // TODO Move to ISemanticAnnotation
					match = true;
					break;
				}
			}
			if (!match) {
				result = false;
				break;
			}
		}

		return result;
	}
	
	// This operation must be override by any Adapter subclass
	protected boolean isAcceptedOperation() {
		return false;
	}

	/**
	 * Return true is the target is process or source goal matches requested
	 * goal
	 * 
	 * @param goal
	 * @return
	 * @throws DTComposerException
	 * @throws InconsistencyException
	 * @throws InvalidModelException
	 * @throws ParserException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public boolean matchTarget() throws URISyntaxException, IOException,
			DTComposerException {
		return dme.getBlackBoardControlAgent().isProcessTarget()
				|| dme.getBlackBoardControlAgent().getGoalTarget().toString().equals(((IActivity) getSource()).getID());
	}

	public boolean matchGoal() {		

		boolean found = false;

		// Checking if designModel has been previously analyzed and tagged as inadmissible
		if (dme.getDesignModel().getStatus().isInadmissible()){
			return false;
		}
		
		// CheckingOperation
		if (!isAcceptedOperation()) {
			return false;
		}

		try {
			DesignModel designModel = dme.getDesignModel();

			// If target at particular goal, first apply the rule only to that
			// goal
			// Otherwise we apply rule to every goal in the model
			// HT Activities are ignored
			
			IActivity targetActivity = null;
			if (dme.getBlackBoardControlAgent().isProcessTarget()) {
				if ((targetActivity = match(designModel.getDesignStructure(), ruleGoal)) != null) {
					setSource(targetActivity);
				}
			} else {
				targetActivity = (IActivity) modelProxy.getActivityById(designModel.getDesignStructure(), dme.getBlackBoardControlAgent().getGoalTarget());
				if (targetActivity != null && !targetActivity.isHumanTask() && match(targetActivity, ruleGoal)) {
					setSource(targetActivity);
				}
			}

			if (getSource() != null) {
				found = true;
			}

		} catch (Throwable t) {
			System.out.println(t + " matchRule() Fail");
			t.printStackTrace();
			return found;
		}

		if (found){
			logger.info("***** Rule-DMA: " + agent.getIdentifier() + ". Matched Goal: " + 
				((IActivity)getSource()).getName() + 
				" with Rule: " + getRuleName() + " ******");
			logger.info("***** Goal Annotations: " + 
					((IActivity)getSource()).getSemanticAnnotations() +  " ******");
			logger.info("***** Goal Input Annotations: ");
			for (IParameter p:((IActivity)getSource()).getInputParameters()){
				logger.info("***** Input Annotations: " + p.getSemanticAnnotations());
			}
			logger.info("***** Goal Output Annotations: ");
			for (IParameter p:((IActivity)getSource()).getOutputParameters()){
				logger.info("***** Output Annotations: " + p.getSemanticAnnotations());
			}
			logger.info("***** Rule Annotations: " + 
					ruleGoal.getSemanticAnnotations() +  " ******");
			logger.info("***** Rule Input Annotations: " + 
					getInputsAnnotations() +  " ******");
			logger.info("***** Rule Output Annotations: " + 
					getOutputsAnnotations() +  " ******");
		}
		
		return found;
	}


	protected String getGoalName(URI goalURI) {
		String templateName = goalURI.toString();
		return templateName;
	}

	public void addFunctionalClassification(String fcname) throws URISyntaxException {
		URI fcuri = getQName(fcname);
		ISemanticAnnotation fc = new SemanticAnnotationImpl();
		fc.setType(IAnnotationType.FUNCTIONAL_CLASSIFICATION);
		fc.setReferenceURI(fcuri.toString());
		this.ruleGoal.getSemanticAnnotations().add(fc);
	}

	private URI getQName(String fcname) throws URISyntaxException {
		String prefix = fcname.substring(0, fcname.indexOf(":"));
		String concept = fcname.substring(fcname.indexOf(":") + 1);
		Namespace ns = agent.getNamespace(prefix);
		URI fcuri = new URI(ns.getNamespace() + concept);
		return fcuri;
	}

	public void addGlobalRequirementAsAssumption(String reference_name, String expression) {
		addAssumption(reference_name, expression, IAnnotationType.REQUIREMENT);
	}

	public void addGlobalConstraintAsAssumption(String reference_name, String expression) {
		addAssumption(reference_name, expression, IAnnotationType.CONSTRAINT);
	}

	private void addAssumption(String reference_name, String expression, IAnnotationType type) {
		try {
			String nsprefix = reference_name.substring(0, reference_name.indexOf(":"));
			String referenceConcept = reference_name.substring(reference_name.indexOf(":") + 1);
			String ns = agent.getNamespace(nsprefix).getNamespace();
			String referenceURI = ns + referenceConcept;

			ISemanticAnnotation sa = new SemanticAnnotationImpl();
			sa.setType(type);
			sa.setReferenceURI(referenceURI);
			sa.setExpression(expression);
			addAssumption(sa);
		} catch (Throwable t) {
			System.out.println("add assumption fail");
			t.printStackTrace();
		}
	}
	
	//this is the method to be overwritten by extending classes 
	public boolean applyAdapter() {
		boolean result = false;
		try {			
			DesignModel designModel = dme.getDesignModel();
							
			// Clone designModel
			DesignModel newDesignModel = (DesignModel)modelProxy.cloneLPMLObject(designModel);

			//BugFix - we need to obtain Goal from the cloned model, not the original one.
			IActivity goal = (IActivity) modelProxy.getFlowObjectByIdentifier(
					newDesignModel.getDesignStructure(), new URI(((IActivity) getSource()).getID()));
			
			loadAndReplace();
			
			result = true;


			//XXX add DesignTransition information
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
	
	private void loadAndReplace() {

	}

	// Must be overrided by GoalWSAdapter
	protected boolean isServiceAlreadyBound(IActivity activity) {
		// TODO Auto-generated method stub
		return false;
	}
}
