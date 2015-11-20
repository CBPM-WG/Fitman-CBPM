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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SPARQLPOSMQuery {
	
	
	List<String> FC;
	List<String> NFP;
	HashMap<String, ArrayList<String>> input;
	HashMap<String, ArrayList<String>> output;
	List<String> context;
	boolean isService;
	
	public SPARQLPOSMQuery(List<String> FC, List<String> NFP, HashMap<String, ArrayList<String>> input, HashMap<String, ArrayList<String>> output, List<String> context, boolean isService) {
		this.FC = FC;
		this.NFP = NFP;
		this.input = input;
		this.output = output;
		this.context = context;
		this.isService = isService;
	}
	
	public String constructQuery() {
		String targetObject;
    	if (isService == true) {
    		targetObject = " " + CommonNamespacesSPARQL.posm_Service;
    	} else {
    		targetObject = " " + CommonNamespacesSPARQL.lpml_Process;
    	}
    	
    	//FC string construction
    	StringBuilder fcSubquery = new StringBuilder();
    	buildFCQuery(fcSubquery);
    	
    	//Input substring construction
    	StringBuilder inputMessagesSubquery = new StringBuilder();
		buildInputQuery(inputMessagesSubquery);
    	
		//Output substring construction
    	StringBuilder outputMessagesSubquery = new StringBuilder();
    	buildOutputQuery(outputMessagesSubquery);
    	
    	//main query builder
    	StringBuilder query = buildMainQuery(fcSubquery, inputMessagesSubquery, outputMessagesSubquery);
    	
    	//if we have any context information then we have to include it
//    	buildContextQuery(query);
    	
    	return query.toString();
	}

	//TODO update
//	private void buildContextQuery(StringBuilder query) {
//		StringBuilder contextSubQuery = new StringBuilder();
//    	
//    	if (context != null && context.isEmpty() == false) {
//    		for (String entity : context) {
//    			contextSubQuery.append("and " + CommonNamespacesWSML.s4a_contextAxiom + "(?service, _\"" + entity + "\")\n");
//    		}
//    		query.append(contextSubQuery);
//    	}
//	}

	private StringBuilder buildMainQuery(
			StringBuilder fcSubquery, StringBuilder inputMessagesSubquery,
			StringBuilder outputMessagesSubquery) {
		StringBuilder query = new StringBuilder();
		
//		query.append ("select ?service ?url ?operation ?input ?output ?inputLoweringSchema ?outputLiftingSchema where {");
		query.append ("select ?service ?url ?operation ?input ?output where {");
		//FC
		query.append(fcSubquery);
		//isDefinedBy
		query.append("?service " + CommonNamespacesSPARQL.rdfs_IsDefinedBy + " ?url.");
		//Operations
		query.append("?service " + CommonNamespacesSPARQL.posm_hasOperation + " ?operation.");
		//Input
		query.append("?operation " + CommonNamespacesSPARQL.posm_hasInputMessage + " ?input.");
		query.append(inputMessagesSubquery);
//		query.append("?input " + CommonNamespacesSPARQL.sawsdl_LowSM + " ?inputLoweringSchema.");
		//Output
		query.append("?operation " + CommonNamespacesSPARQL.posm_hasOutputMessage + " ?output.");
		query.append(outputMessagesSubquery.deleteCharAt(outputMessagesSubquery.length()-1)); //Removed final .
//		query.append("?output " + CommonNamespacesSPARQL.sawsdl_LiftSM + " ?outputLiftingSchema");
		
		query.append("}");
		
		return query;
	}

	private void buildOutputQuery(StringBuilder outputMessagesSubquery) {
		Set<String> keys = output.keySet();
		for (String key:keys){
			List<String> annotations = output.get(key);
			for (String an:annotations){
				outputMessagesSubquery.append("?output " + CommonNamespacesSPARQL.sawsdl_MR + " <" + an + ">.");
			}
		}
		//outputMessagesSubquery.deleteCharAt(outputMessagesSubquery.length()-1);
	}

	private void buildInputQuery(StringBuilder inputMessagesSubquery) {
		Set<String> keys = input.keySet();
		for (String key:keys){
			List<String> annotations = input.get(key);
			for (String an:annotations){
				inputMessagesSubquery.append("?input " + CommonNamespacesSPARQL.sawsdl_MR + " <" + an + ">.");
			}
		}
	}

	private StringBuilder buildFCQuery(StringBuilder fcSubquery) {
		for (String fc:FC){
			fcSubquery.append("?service " + CommonNamespacesSPARQL.sawsdl_MR + " <" + fc + ">.");
		}
		return fcSubquery;
	}

//	public String constructQueryFlexible() {
//		String targetObject;
//    	if (isService == true) {
//    		targetObject = " " + CommonNamespaces.posm_Service;
//    	} else {
//    		targetObject = " " + CommonNamespaces.lpml_Process;
//    	}
//
//    	StringBuilder fcSubquery = new StringBuilder();
//    	if (FC.size() == 1) {
//    		fcSubquery.append(" ?fc1 ");
//    	} else {
//    		if (FC.size() > 1) {
//        		fcSubquery.append("{");
//        		for (int i=0; i<FC.size(); i++) {
//        			fcSubquery.append("?fc" + (i+1));
//        			if (i != FC.size()-1) {
//        				fcSubquery.append(", ");
//        			}
//        		}
//        		fcSubquery.append("}");
//    		}
//    	}
//    	
//    	//input substring contruction
//    	StringBuilder inputMessagesSubquery = new StringBuilder();
//		StringBuilder inputMessagesVars = new StringBuilder();
//		Set<String> inputMsgs = input.keySet();
//		
//    	//one input parameter
//		if (input.size() == 1) {
//    		//make a list of annotations
//    		ArrayList<String> annotations;
//    		for (String parameterID: inputMsgs) {
//    			annotations = input.get(parameterID);
//    		
//	    		//annotations string construction
//	        	StringBuilder inputAnnotationSubquery = new StringBuilder();
//	        	if (annotations.size() == 1) {
//	        		inputAnnotationSubquery.append(" ?in1 ");
//	        	} else {
//	        		inputAnnotationSubquery.append("{");
//	        		for (int i=0; i<annotations.size(); i++) {
//	        			inputAnnotationSubquery.append("?in" + (i+1) );
//	        			if (i != annotations.size()-1) {
//	        				inputAnnotationSubquery.append(", ");
//	        			}
//	        		}
//	        		inputAnnotationSubquery.append("}");
//	        	}
//	    		
//	    		inputMessagesSubquery.append("" +
//	    				"?inputdef [ " + CommonNamespaces.sawsdl_MR + " hasValue " + inputAnnotationSubquery + "] memberOf " + CommonNamespaces.posm_Message + "\n" + 
//	        			"and\n" +
//	        			"?inputdef [ " + CommonNamespaces.sawsdl_LowSM + " hasValue ?inputloweringschema ] memberOf " + CommonNamespaces.posm_Message + " " +
//	        			"and\n" + 
//	    				" ");
//	    		
//	    		inputMessagesVars.append(" ?inputdef ");
//    		}
//    	} else if (input.size() > 1){
//    		inputMessagesVars.append(" { ");
//    		ArrayList<String> annotations;
//    		int inputNo = 1;
//    		for (String parameterID: inputMsgs) {
//    			annotations = input.get(parameterID);
//    			
//    			//annotations string construction
//            	StringBuilder inputAnnotationSubquery = new StringBuilder();
//            	if (annotations.size() == 1) {
//            		inputAnnotationSubquery.append(" ?in" + inputNo + " ");
//            	} else {
//            		inputAnnotationSubquery.append("{");
//            		for (int i=0; i<annotations.size(); i++) {
//            			inputAnnotationSubquery.append("?in" + inputNo);
//            			if (i != annotations.size()-1) {
//            				inputAnnotationSubquery.append(", ");
//            			}
//            		}
//            		inputAnnotationSubquery.append("}");
//            	}
//    			
//    			inputMessagesSubquery.append("" +
//    					"?inputdef" + Integer.toString(inputNo) + " [ " + CommonNamespaces.sawsdl_MR + " hasValue " + inputAnnotationSubquery + "] memberOf " + CommonNamespaces.posm_Message + "\n" + 
//            			"and\n" +
//            			"?inputdef" + Integer.toString(inputNo) + " [ " + CommonNamespaces.sawsdl_LowSM + " hasValue ?inputloweringschema" + Integer.toString(inputNo) + " ] memberOf " + CommonNamespaces.posm_Message + " " +
//            			"and\n" + 
//    					"");
//        		inputMessagesVars.append(" ?inputdef" + Integer.toString(inputNo));
//        		if (inputNo != inputMsgs.size()) {
//        			inputMessagesVars.append(", ");
//    			}
//
//        		inputNo++;
//    		}
//    		
//    		inputMessagesVars.append(" } ");
//    	}
//    	
//		//Output substring contruction
//    	StringBuilder outputMessagesSubquery = new StringBuilder();
//		StringBuilder outputMessagesVars = new StringBuilder();
//		Set<String> outputMsgs = output.keySet();
//		
//    	//one input parameter
//		if (output.size() == 1) {
//    		//make a list of annotations
//    		ArrayList<String> annotations;
//    		for (String parameterID: outputMsgs) {
//    			annotations = output.get(parameterID);
//    		
//	    		//annotations string construction
//	        	StringBuilder outputAnnotationSubquery = new StringBuilder();
//	        	if (annotations.size() == 1) {
//	        		outputAnnotationSubquery.append(" ?out1 ");
//	        	} else {
//	        		outputAnnotationSubquery.append("{");
//	        		for (int i=0; i<annotations.size(); i++) {
//	        			outputAnnotationSubquery.append("?out" + (i+1));
//	        			if (i != annotations.size()-1) {
//	        				outputAnnotationSubquery.append(", ");
//	        			}
//	        		}
//	        		outputAnnotationSubquery.append("}");
//	        	}
//	    		
//	    		outputMessagesSubquery.append("" +
//	    				"?outputdef [ " + CommonNamespaces.sawsdl_MR + " hasValue " + outputAnnotationSubquery + "] memberOf " + CommonNamespaces.posm_Message + "\n" + 
//	        			"and\n" +
//	        			"?outputdef [ " + CommonNamespaces.sawsdl_LiftSM + " hasValue ?outputliftingschema ] memberOf " + CommonNamespaces.posm_Message + "\n");
//	    		
//	    		outputMessagesVars.append(" ?outputdef ");
//    		}
//    	} else if (output.size() > 1){
//    		outputMessagesVars.append(" { ");
//    		ArrayList<String> annotations;
//    		int outputNo = 1;
//    		for (String parameterID: outputMsgs) {
//    			annotations = output.get(parameterID);
//    			
//    			//annotations string construction
//            	StringBuilder outputAnnotationSubquery = new StringBuilder();
//            	if (annotations.size() == 1) {
//            		outputAnnotationSubquery.append(" ?out" + outputNo + " ");
//            	} else {
//            		outputAnnotationSubquery.append("{");
//            		for (int i=0; i<annotations.size(); i++) {
//            			outputAnnotationSubquery.append("?out" + outputNo);
//            			if (i != annotations.size()-1) {
//            				outputAnnotationSubquery.append(", ");
//            			}
//            		}
//            		outputAnnotationSubquery.append("}");
//            	}
//    			
//            	outputMessagesSubquery.append("" +
//    					"?outputdef" + Integer.toString(outputNo) + " [ " + CommonNamespaces.sawsdl_MR + " hasValue " + outputAnnotationSubquery + "] memberOf " + CommonNamespaces.posm_Message + "\n" + 
//            			"and\n" +
//            			"?outputdef" + Integer.toString(outputNo) + " [ " + CommonNamespaces.sawsdl_LiftSM + " hasValue ?outputliftingschema" + Integer.toString(outputNo) + " ] memberOf " + CommonNamespaces.posm_Message + " ");
//        		if (outputNo != outputMsgs.size()){
//        			outputMessagesSubquery.append("and\n");
//        		}
//            	
//            	outputMessagesVars.append(" ?outputdef" + Integer.toString(outputNo));
//        		if (outputNo != outputMsgs.size()) {
//        			outputMessagesVars.append(", ");
//    			}
//
//        		outputNo++;
//    		}
//    		
//    		outputMessagesVars.append(" } ");
//    	}
//    	
//    	StringBuilder query = buildMainQuery(targetObject, fcSubquery,
//				inputMessagesSubquery, outputMessagesSubquery);
//    	
//    	//now include subsumption matching for FC
//    	//query.append(" and \n");
//    	for (int i=0; i<FC.size(); i++) {
//    		query.append("and " + CommonNamespaces.s4a_subsumptionAxiom + " ( ?fc" + (i + 1) + ", _\"" + FC.get(i) + "\" ) \n");
//			//if (i != FC.size()-1) {
//			//	query.append(" and \n");
//			//}
//		}
//    	
//    	//now include subsumption matching for Inputs
//    	//query.append(" and \n");
//    	int inputNo = 1;
//    	for (String parameterID: inputMsgs) {
//    		for (int i=0; i<input.get(parameterID).size(); i++) {
//    			query.append("and " + CommonNamespaces.s4a_subsumptionAxiom + " ( ?in" + inputNo + ", _\"" + input.get(parameterID).get(i) + "\" ) \n");
//    			inputNo++;
//    		}
//    	}
//    	
//    	//now include subsumption matching for Outputs	
//    	int outputNo = 1;
//    	for (String parameterID: outputMsgs) {
//    		for (int i=0; i<output.get(parameterID).size(); i++) {
//    			query.append("and " + CommonNamespaces.s4a_subsumptionAxiom + " ( ?out" + outputNo + ", _\"" + output.get(parameterID).get(i) + "\" ) \n");
//    			inputNo++;
//    		}
//    	}
//    	
//    	//if we have any context information then we have to include it
//    	//
//    	//now we will support only payment context
//    	//and only 1 annotation
//    	if (context != null && context.isEmpty() == false) {
//        	query.append("\n" +
//        			"and " + CommonNamespaces.s4a_contextAxiom + "(?service, ?y) \n" +
//        			"and ?y = _\"" + context.get(0) + "\"");
//    	}
//    	
//    	return query.toString();
//	}
}
