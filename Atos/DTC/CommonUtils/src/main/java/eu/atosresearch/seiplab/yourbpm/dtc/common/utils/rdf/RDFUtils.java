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
package eu.atosresearch.seiplab.yourbpm.dtc.common.utils.rdf;


public class RDFUtils {

/*
	public static List<String> searchQuery(String query, String field, OntModel m){
		List<String> results = new ArrayList<String>();
		Query q = QueryFactory.create(query);
		QueryExecution qexec = QueryExecutionFactory.create(q,m);

		try {
			ResultSet rs = qexec.execSelect();
			while (rs.hasNext()) {
				QuerySolution qs = rs.nextSolution();
				String artefact = ((Resource) qs.get(field)).getURI();
				results.add(artefact);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			qexec.close();
		}

		return results;
	}
*/
}
