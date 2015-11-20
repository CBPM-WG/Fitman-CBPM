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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.soa4all.lpml.Process;

import com.thoughtworks.xstream.XStream;

public class LPMLXMLMapper {

	String prefixes = " PREFIX tid: <http://www.momocs.org/TID_Ontology.owl#>" + " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + " PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ " PREFIX jenabeans: <http://thewebsemantic.com/>" + " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			+ " PREFIX lwbpml: <http://eu.soa4all.wp6.lightweighbpml#>";

	public void mapToXML(Process process, String filePath) {
		XStream xstream = new XStream();
		String xml;

		xml = xstream.toXML(process);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filePath));
			out.write(xml);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("XML file write error.");
			if (out != null){
				try {
					out.close();
				} catch (IOException e1) {} //Ignored
			}
		}

	}

	public void mapToXML(Process process, URL url) {
		XStream xstream = new XStream();
		String xml;

		xml = xstream.toXML(process);
//		System.out.println ("Storing model xml: " + xml);
		BufferedWriter out = null;
		try {
			// TODO
			out = new BufferedWriter(new FileWriter(URLDecoder.decode(url.getFile(), "UTF-8")));
			out.write(xml);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("XML file write error.");
			if (out != null){
				try {
					out.close();
				} catch (IOException e1) {} //Ignored
			}
		}

	}
	
	public String mapToXML(Process process) {
		XStream xstream = new XStream();
		String xml = xstream.toXML(process);
		return xml;
	}

	public Process mapFromXMLFile(URL file) {
		Process deserialisedProcess;
		XStream xstream = new XStream();
		String xml = new String();

		try {
			BufferedReader input = null;
			if (file.getProtocol().equalsIgnoreCase("http")){
				input = new BufferedReader (new InputStreamReader(file.openConnection().getInputStream()));
			} else if (file.getProtocol().equalsIgnoreCase("file")){
				input = new BufferedReader(new FileReader(URLDecoder.decode(file.getPath(), "UTF-8")));
			}
		
			String line = null;
			try {
				
				while ((line = input.readLine()) != null) {
					xml = xml.concat(line);
					xml = xml.concat(System.getProperty("line.separator"));
				}
			}
			finally {
				input.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		deserialisedProcess = (Process) xstream.fromXML(xml);

		return deserialisedProcess;

	}
	
	public Process mapFromXMLFile(String file) {
		Process deserialisedProcess;
		XStream xstream = new XStream();
		String xml = new String();
		try {
			
			BufferedReader input = null;
			// Loading from classpath
			try{
				input = new BufferedReader(new InputStreamReader(LPMLXMLMapper.class.getResourceAsStream(file)));
			} catch (Throwable e) {
				//Ignored, trying from filesystem
			}
			// Loading from filesystem
			if (input == null){
				try{
					input = new BufferedReader(new InputStreamReader(new FileInputStream(new File (file))));
				} catch (Throwable e) {
					throw e;
				}
			}
			
			String line = null;
			try {
				
				while ((line = input.readLine()) != null) {
					xml = xml.concat(line);
					xml = xml.concat(System.getProperty("line.separator"));
				}
			}
			finally {
				input.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		deserialisedProcess = (Process) xstream.fromXML(xml);

		return deserialisedProcess;
	}
	
	public Process mapFromXML(String xml) {
		XStream xstream = new XStream();
		return (Process) xstream.fromXML(xml);
	}

//	private String getModelURI(OntModel m) {
//		return prefixes;
//	}

	public Process mapFromRDFS(String filePath, URI processURI) {
		Process deserialisedProcess;
		XStream xstream = new XStream();
		String xml = new String();

		try {
			BufferedReader input = new BufferedReader(new FileReader(filePath));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					line = xml.concat(line);
					line = xml.concat(System.getProperty("line.separator"));
				}
			}
			finally {
				input.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		deserialisedProcess = (Process) xstream.fromXML(xml);

		return deserialisedProcess;

	}

	// private OntModel createModel() {
	//
	//
	// }

	// public static void main (String[] args) throws MalformedURLException{
	// Process p = new RDFSMapper().mapFromRDFS(
	// new URL(
	// "file:///home/yosu/projects/soa4all/wp6/workspace/DesignTimeComposerAdvancePrototype/DesignTimeComposer_v2/src/main/resources/models/InputProcess.rdfs"));
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
