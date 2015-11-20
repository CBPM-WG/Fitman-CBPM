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
package eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client;

public class CommonNamespacesSPARQL {
	static final String nsRDFS = "http://www.w3.org/2000/01/rdf-schema#";
	static final String nsRDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	static final String nsWSMOLite = "http://www.wsmo.org/ns/wsmo-lite#";
	static final String nsSAWSDL = "http://www.w3.org/ns/sawsdl#";
	public static String nsRBR = "http://www.wsmo.org/ontologies/nfp/preferenceOntology#";

	// WSMOLite literals
	static final String rdfs_IsDefinedBy = "<" + nsRDFS + "isDefinedBy>";
	static final String lpml_Process = "<http://www.soa4all.eu/lpml#Process>";
	static final String posm_namespace = "http://www.wsmo.org/ns/posm/0.2#";
	
	static final String posm_Service = "<" + posm_namespace + "Service>";
	static final String posm_hasOperation = "<" + posm_namespace + "hasOperation>";
	static final String posm_hasInputMessage = "<" + posm_namespace + "hasInput>";
	static final String posm_hasOutputMessage = "<" + posm_namespace + "hasOutput>";
	static final String posm_Operation = "<" + posm_namespace + "Operation>";
	static final String posm_Message = "<" + posm_namespace + "Message>";

	static final String sawsdl_MR = "<" + nsSAWSDL + "modelReference>";
	static final String sawsdl_LiftSM = "<" + nsSAWSDL + "liftingSchemaMapping>";
	static final String sawsdl_LowSM = "<" + nsSAWSDL + "loweringSchemaMapping>";

	static final String rdf_type = "<" + nsRDF + "type>";
	static final String NFP = "<" + nsRBR + "NonFunctionalParameter>";
	
	// common relations
	//TODO Update them
	static final String s4a_subsumptionAxiom = "_\"http://www.soa4all.eu/ontology/eGovernment/axioms#subsumption\"";
	static final String s4a_incompatibilityAxiom = "_\"http://www.soa4all.eu/ontology/eGovernment/axioms#incompatibility\"";
	static final String s4a_contextAxiom = "_\"http://www.soa4all.eu/ontology/eGovernment/axioms#serviceswithcontext\"";
	

}
