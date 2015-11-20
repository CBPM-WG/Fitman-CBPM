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
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import eu.atosresearch.seiplab.yourbpm.dtc.common.namespace.Namespace;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPI;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPIImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelLoader;


public class Agent {
	protected Map<String, Namespace> namespaces = new HashMap<String, Namespace>();
	protected URI identifier;
	protected KnowledgeBase knowledgeBase;
	protected ApplicationContext composerApplicationContext;
	protected ReasonerAPIImpl reasoner;
	protected ModelLoader modelLoader;
	
//	protected List<Terminology> terminologies = new ArrayList<Terminology> ();
//	protected WSMLReasoningUtils wru = new WSMLReasoningUtils();
//	
//	public WSMLReasoningUtils getWSMLReasoner() {
//		return wru;
//	}
	
//	public List<Terminology> getTerminologies() {
//		return terminologies;
//	}
	
//	public void addTerminology (Namespace namespace, String terminologyLocation) throws IOException, ParserException, InvalidModelException, InconsistencyException{
//		Terminology terminology = new Terminology (namespace, terminologyLocation);
//		
//		//NOTE: Ontologies are never removed even if the DMA is unregistered
//		if (!terminologies.contains(terminology)){
//			terminologies.add(terminology);
//		}
//	}
	
//	public void registerTerminologies () throws IOException, ParserException, InvalidModelException, InconsistencyException{
//		Collection <String> locations = new ArrayList<String> ();
//		for (Terminology terminology:terminologies){
//			locations.add(terminology.getTerminologyLocation());
//		}
//		//NOTE: All previously register ontologies are replaced by the new ones
//		wru.registerOntologies(locations);
//	}

//	public void registerTerminologiesInDAA() throws IOException, ParserException, InvalidModelException, InconsistencyException {
//		DesignAnalysisAgent daa = (DesignAnalysisAgent) composerApplicationContext.getBean("designAnalysisAgent");
//		for (Terminology terminology:terminologies){
//			daa.addTerminology(terminology.getNamespace(), terminology.getTerminologyLocation());
//		}
//		daa.registerTerminologies();
//	}

	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public void addNamespace (String prefix, String namespace){
		namespaces.put(prefix, new Namespace (prefix, namespace));
	}
	
	public Namespace getNamespace (String prefix){
		return (Namespace)namespaces.get(prefix);
	}

	public ApplicationContext getComposerApplicationContext() {
		return composerApplicationContext;
	}

	public void setComposerApplicationContext(ApplicationContext composerApplicationContext) {
		this.composerApplicationContext = composerApplicationContext;
	}
	
	@Override
	public String toString() {
		return this.getIdentifier().toString();
	}
	
	@Override
	public boolean equals (Object o){
		boolean result = false;
		if (o instanceof Agent){
			result = this.getIdentifier().toString().equalsIgnoreCase(((Agent)o).getIdentifier().toString());
		}
		return result;
	}

	public ReasonerAPIImpl getReasoner() {
		return reasoner;
	}
}
