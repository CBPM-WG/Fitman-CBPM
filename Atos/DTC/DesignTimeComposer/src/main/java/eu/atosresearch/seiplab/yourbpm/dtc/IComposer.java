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
package eu.atosresearch.seiplab.yourbpm.dtc;


import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

//import org.osoa.sca.annotations.Service;

import eu.atosresearch.seiplab.yourbpm.dtc.exception.DTComposerException;

//@Service
public interface IComposer {
	
	// In order to find all solutions, set numberRequestedSolutions=-1
	
	// Specialized methods
	
//	/**
//	 * Request to resolve an unbound Activity (described by a goal annotations) 
//	 * either binding it with found WS or expanding it with a process fragment or template
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param activityTargetURI 
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	ModelSolution[] resolveGoalMS(
//			@WebParam (name="sourceProcessModel", targetNamespace = "http://composer.wp6.soa4all.eu/") String sourceProcessModel,  
//			@WebParam (name="activityTargetURI", targetNamespace = "http://composer.wp6.soa4all.eu/") String activityTargetURI, 
//			@WebParam (name="numberRequestedSolutions", targetNamespace = "http://composer.wp6.soa4all.eu/") int numberRequestedSolutions) throws DTComposerException;

	/**
	 * Request to resolve an unbound Activity (described by a goal annotations) 
	 * either binding it with found WS or expanding it with a process fragment or template
	 * @param sourceProcessModel XStream XMLdocument (as String)
	 * @param activityTargetURI 
	 * @return Found solution (best ranked) as LPML XML serialized
	 * @throws 
	 * @see SOA4ALL D5.4.2
	 */
	@WebResult(name = "LPMLModel", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu")
	@WebMethod
	String resolveActivity(
			@WebParam (name="LPMLModel", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu") String sourceProcessModel,  
			@WebParam (name="activitytURI", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu") String activityTargetURI) throws DTComposerException;
	
//	/**
//	 * Request to resolve an unbound Activity (described by a goal annotations) 
//	 * expanding it with a process fragment or template
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param activityTargetURI
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutionsç
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	ModelSolution[] resolveGoalWithTemplateMS(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel, 
//			@WebParam (name="activityTargetURI") String activityTargetURI, 
//			@WebParam (name="numberRequestedSolutions") int numberRequestedSolutions) throws DTComposerException;
	
//	/**
//	 * Request to resolve an unbound Activity (described by a goal annotations) 
//	 * expanding it with a process fragment or template
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param activityTargetURI
//	 * @return Found solution (best ranked) as LPML XML serialized
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	String resolveGoalWithTemplate(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel, 
//			@WebParam (name="activityTargetURI") String activityTargetURI) throws DTComposerException;
	
//	/**
//	 * Request to resolve an unbound Activity (described by a goal annotations) 
//	 * binding it with found WS
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param activityTargetURI
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	ModelSolution[] resolveGoalWithWSMS(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel,
//			@WebParam (name="activityTargetURI") String activityTargetURI, 
//			@WebParam (name="numberRequestedSolutions") int numberRequestedSolutions) throws DTComposerException;
	
	/**
	 * Request to resolve an unbound Activity (described by a goal annotations) 
	 * binding it with found WS
	 * @param sourceProcessModel XStream XMLdocument (as String)
	 * @param activityTargetURI
	 * @return Found solution (best ranked) as LPML XML serialized
	 * @throws 
	 * @see SOA4ALL D5.4.2
	 */
	@WebResult(name = "LPMLModel", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu")
	@WebMethod
	String bindActivity(
			@WebParam (name="LPMLModel") String sourceProcessModel,
			@WebParam (name="activityURI") String activityTargetURI) throws DTComposerException;
	
	/**
	 * Request to search for possible bindings for a given unbound Activity. 
	 * Matching bindings are associated to the Activity in the KB
	 * @param processURI
	 * @param activityURI
	 * @return Bindings found flag
	 * @throws DTComposerException
	 * @throws  
	 * @see WebN+1 project site
	 */
	@WebResult(name = "BindingsFound", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu")
	@WebMethod
	boolean searchForActivityBindings(@WebParam (name="activityURI") String processURI, @WebParam (name="activityURI") String activityURI) throws DTComposerException;
	
//	/**
//	 * Request to resolve a model
//	 * either binding all its activities with found WS or expanding them with a process fragment or template
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	ModelSolution[] resolveProcessMS(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel, 
//			@WebParam (name="numberRequestedSolutions") int numberRequestedSolutions) throws DTComposerException;
	
	/**
	 * Request to resolve a model
	 * either binding all its activities with found WS or expanding them with a process fragment or template
	 * @param sourceProcessModel XStream XMLdocument (as String)
	 * @return Found solution (best ranked) as LPML XML serialized
	 * @throws 
	 * @see SOA4ALL D5.4.2
	 */
	@WebResult(name = "LPMLModel", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu")
	@WebMethod
	String resolveProcess(
			@WebParam (name="LPMLModel") String sourceProcessModel) throws DTComposerException;
	
//	/**
//	 * Request to resolve a process 
//	 * expanding all its activities with a process fragments or templates
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	ModelSolution[] resolveProcessWithTemplateMS(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel,  
//			@WebParam (name="numberRequestedSolutions") int numberRequestedSolutions) throws DTComposerException;
	
//	/**
//	 * Request to resolve a process 
//	 * expanding all its activities with a process fragments or templates
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @return Found solution (best ranked) as LPML XML serialized
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	String resolveProcessWithTemplate(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel) throws DTComposerException;
	
//	/**
//	 * Request to resolve a model
//	 * expanding all its activities with a process fragment or template
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	ModelSolution[] resolveProcessWithWSMS(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel, 
//			@WebParam (name="numberRequestedSolutions") int numberRequestedSolutions) throws DTComposerException;
//	
//	/**
//	 * Request to resolve a model
//	 * expanding all its activities with a process fragment or template
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @return Found solution (best ranked) as LPML XML serialized
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebResult(name = "Collection<ModelSolution>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	@WebMethod
//	String resolveProcessWithWS(
//			@WebParam (name="sourceProcessModel") String sourceProcessModel) throws DTComposerException;
	
//	/**
//	 * Request to check the semantic compatibility between inputs and outputs of activities 
//	 * logically connected through the work flow by causality.
//	 * NOT IMPLEMENTED YET
//	 * @param sourceProcessModel XStream XMLdocument (as String)
//	 * @return Modified Model solution where incompatible bound WS for each activity has been removed
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	@WebResult(name = "Set<BindingCompatibilityDescriptor>", targetNamespace = "http://composer.wp6.soa4all.eu/")
//	String checkIOSemanticCompatibility (
//			@WebParam (name="Activity") String activity) throws DTComposerException;
	
	/**
	 * Request to generate the process model data flow. It is required a complete process model, 
	 * That is, a process model without abstract activities: all activities bound.
	 * NOT IMPLEMENTED YET
	 * @param process LPML process model to generate the data flow for
	 * @return Updated LPML process model thats includes the generated data flow.
	 * @throws DTComposerExceptionException 
	 * @throws RemoteException 
	 * @see SOA4ALL D6.4.2, D6.4.3
	 */
	@WebMethod
	@WebResult(name = "LPMLModel", targetNamespace = "http://dtc.yourbpm.seiplab.atosresearch.eu")
	String generateDataFlow (
			@WebParam (name="LPMLModel") String sourceProcessModel) throws DTComposerException;
	
	// Management methods
//	/**
//	 * Register within the DTComposer a new DesignModificationAgent (DMA) based on Drools Rules, 
//	 * with concrete domain specific knowledge for modifications (set of Drools rules described by specific SOA4ALL WP6 DSL)
//	 * @param Unique identifier for this DMA
//	 * @param classpath based path or a URL to the domain specific knowledge (rules), based on SOA4ALL DSL.
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void registerDesignModificationRuleAgent(
//			@WebParam (name="uri") String uri, 
//			@WebParam (name="knowledgePath") String knowledgePath) throws DTComposerException;
	
//	/**
//	 * Register within the DTComposer a new DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * with concrete domain specific knowledge for analysis (set of Drools rules described by specific SOA4ALL WP6 DSL)
//	 * @param Unique identifier for this DMA
//	 * @param classpath based path or a URL to the domain specific knowledge (rules), based on SOA4ALL DSL. 
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void registerDesignAnalysisAgent(
//			@WebParam (name="uri") String uri, 
//			@WebParam (name="knowledgePath") String knowledgePath) throws DTComposerException;
	
//	/**
//	 * Register within the DTComposer a new semantic DesignAnalysisAgent (DMA) based on WSMO rules, 
//	 * @param Unique identifier for this DMA
//	 * @param classpath based path or a URL pointing at domain specific knowledge described by  WSMO ontologies 
//	 * @param classpath based path or a URL pointing at domain specific services described by WSMO ontologies 
//	 * @param classpath based path or a URL pointing at domain specific context described by  WSMO ontologies
//	 * @param classpath based path or a URL pointing at domain specific goals described by WSMO ontologies 
//	 * @param domain specific namespace
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void registerDesignModificationSemanticAgent(
//			@WebParam (name="uri") String identifier,
//			@WebParam (name="knowledgeOntologies") String[] knowledgeOntologies, 
//			@WebParam (name="knownServices") String[] knownServices,
//			@WebParam (name="contextOntologies") String[] contextOntologies, 
//			@WebParam (name="goals") String[] goals)
//			throws DTComposerException;
	
//	/**
//	 * Register within the DTComposer a new SLO DesignAnalysisAgent (DMA) based on Semantic Link Operator, 
//	 * @param Unique identifier for this DMA
//	 * @throws DTComposerException
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	public void registerSemanticLinkOperatorAgent(@WebParam (name="uri") URI identifier)
//	throws DTComposerException;
	
//	/**
//	 * Unregister within the DTComposer an identified DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void unregisterDesignModificationRuleAgent(@WebParam (name="uri") String identifier) throws DTComposerException;
	
//	/**
//	 * Unregister within the DTComposer an identified DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void unregisterDesignAnalysisAgent(@WebParam (name="uri") String identifier) throws DTComposerException;
	
//	/**
//	 * Unregister within the DTComposer an identified DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void unregisterDesignModificationSemanticAgent(@WebParam (name="uri") String identifier)
//			throws DTComposerException;
	
//	/**
//	 * Unregister within the DTComposer an identified SLO (DMA) based on Semantic Link Operator, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	@WebMethod
//	void unregisterSemanticLinkOperatorAgent(@WebParam (name="uri") String identifier)
//			throws DTComposerException;
	
}