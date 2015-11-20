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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simple SPARQL Result structure for storing variables list, and hashmap for result in a form variable => value.
 * This structure contains a single row of a SPARQL result.
 * 
 * @author Mateusz Radzimski
 * 
 *
 */
public class SPARQLResult {

	public ArrayList<String> definition = new ArrayList<String>();
	public ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String,String>>();
	boolean isBoolean = false;
	boolean booleanValue = false;
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("number of results: " + results.size() + "\n");
		
		result.append("definition:\n");
		
		if (isBoolean == true) {
			result.append("\tBoolean value");
			result.append("\nresult:");
			result.append("\n\t" + booleanValue);
		} else {
			//definition
			result.append("\t" + this.definition.toString());
			result.append("\n");
			
			if (this.results.size() == 0) {
				result.append("results: \n\t[]");
			}
			
			for (HashMap<String, String> hm : this.results) {
				result.append("result:\n");
				for (String key : hm.keySet()) {
					result.append("\t(" + key + ") => " + hm.get(key) + "\n");
				}
			}
			
		}
		
		
		return result.toString();
	}
}
