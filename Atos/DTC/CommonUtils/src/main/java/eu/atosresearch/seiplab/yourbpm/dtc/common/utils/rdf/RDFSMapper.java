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




public class RDFSMapper {     

/*
	String prefixes = 
		" PREFIX tid: <http://www.momocs.org/TID_Ontology.owl#>" +
	    " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
	    " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
	    " PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
	    " PREFIX jenabeans: <http://thewebsemantic.com/>" +
	    " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
	    " PREFIX lwbpml: <http://eu.soa4all.wp6.lightweighbpml#>"
	   ;
	
	public void mapToRDFS(Process process, String filePath) {
		Model m = createModel();
		Bean2RDF writer = new Bean2RDF(m);
		//writer.save(process);
		writer.saveDeep(process);
		System.out.println(" - model "+ filePath + " saved");
		try {
			OutputStream fileOutputStream = new FileOutputStream(filePath);
			m.write(fileOutputStream, "RDF/XML-ABBREV");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mapToRDFS(Process process, URL url) {
		Model m = createModel();
		Bean2RDF writer = new Bean2RDF(m);
		//writer.save(process);
		writer.saveDeep(process);
		try {
			OutputStream fileOutputStream = new FileOutputStream(url.getFile());
			m.write(fileOutputStream, "RDF/XML-ABBREV");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Process mapFromRDFS(URL file) {
		OntModel m = ModelFactory.createOntologyModel();
		//new approach
		
		try {
			m.read(file.openStream(), "");
		} catch (Throwable e) {

			e.printStackTrace();
		}
		
		//Get processURI
		String processURI = getModelURI(m);
		
		RDF2Bean reader = new RDF2Bean(m);
		Process process = reader.loadDeep(Process.class, processURI);
		return process;
	}

	private String getModelURI(OntModel m) {
		String q = prefixes + " select ?p where {?p rdf:type lwbpml:Process}";
		List<String> r = RDFUtils.searchQuery (q, "p", m);
		return r.get(0);
	}
	

	
	public Process mapFromRDFS(String filePath, URI processURI) {
		OntModel m = ModelFactory.createOntologyModel();
		//new approach
		
		try {
			InputStream inputStream = RDFSMapper.class.getResourceAsStream(filePath);
			m.read(inputStream, "");
		} catch (Throwable e) {

			e.printStackTrace();
		}
		
		// DEBUG
//		StmtIterator iter = m.listStatements();
//		while (iter.hasNext()){
//			Statement i = (Statement) iter.next();
//			if (i== null){
//				System.out.println ("Null Statement");
//			} else if (i.getSubject() == null){
//				System.out.println ("Null Subject");
//			} else if (i.getSubject().getURI() == null){
//				System.out.println ("Null URI");
//				System.out.println (i.getSubject().toString());
//				System.out.println (i);
//			} else {
//				System.out.println (i.getSubject().getURI().toString());
//				System.out.println (i);
//			}
//		}
		// END DEBUG
		
		RDF2Bean reader = new RDF2Bean(m);
		Process process = reader.loadDeep(Process.class, processURI);
		return process;
	}

	private OntModel createModel() {
		// We create the ontology model with the basic namespaces that we will
		// use
		OntModel ontologyModel = ModelFactory.createOntologyModel();
		ontologyModel.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		ontologyModel.setNsPrefix("rdf",
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		ontologyModel.setNsPrefix("lwbpml",
				"http://eu.soa4all.wp6.lightweighbpml#");
		ontologyModel.setNsPrefix("jenabeans", "http://thewebsemantic.com/");
		return ontologyModel;
	}
	
	public static void main (String[] args) throws MalformedURLException{
		Process p = new RDFSMapper().mapFromRDFS(
		 new URL(
		  "file:///home/yosu/projects/soa4all/wp6/workspace/DesignTimeComposerAdvancePrototype/DesignTimeComposer_v2/src/main/resources/models/InputProcess.rdfs"));
	}
*/
}
