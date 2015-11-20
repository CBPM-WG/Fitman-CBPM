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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.Attributes;

import com.ctc.wstx.evt.WAttribute;

public class SPARQLParser {

	/**
	 * @param args
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		XMLEventReader xmler = xmlif.createXMLEventReader(new FileReader("D:\\devel\\eclipse-workspace\\Advance Prototype\\ReasonerClient\\src\\main\\resources\\input.xml"));
		XMLEvent event;
		
		//structure for stroing xml representation
		SPARQLResult sparqlresult = new SPARQLResult();
		
		boolean readcharacters_mode = false;
		
		HashMap<String,String> single_result = new HashMap<String,String>();
		String binding = "";
		String uri = "";
		
		while (xmler.hasNext()) {
			event = xmler.nextEvent();
			if (event.isStartElement()) {
				//head
				if (event.asStartElement().getName().getLocalPart().compareTo("variable") == 0) {
					Iterator<Attributes> i = event.asStartElement().getAttributes();
					while (i.hasNext()) {
						WAttribute a = (WAttribute) i.next();
						if (a.getName().toString().compareTo("name") == 0) {
							sparqlresult.definition.add(a.getValue());
						}
					}
				}
				
				//results - binding
				if (event.asStartElement().getName().getLocalPart().compareTo("binding") == 0) {
					Iterator<Attributes> i = event.asStartElement().getAttributes();
					while (i.hasNext()) {
						WAttribute a = (WAttribute) i.next();
						if (a.getName().toString().compareTo("name") == 0) {
							binding = a.getValue();
						}
					}
				}
				
				//results - uri
				if (event.asStartElement().getName().getLocalPart().compareTo("uri") == 0) {
					readcharacters_mode = true;
				}

				//System.out.println(event.asStartElement().getName());

			//grab the last part - uri and put sin gle result into array.
			} else if (event.isCharacters()) {
				if (readcharacters_mode == true) {
					single_result.put(binding, event.asCharacters().getData());
					//sparqlresult.results.add(single_result);
					
					readcharacters_mode = false;
				}
				//System.out.println(event.asCharacters().getData());
			} else if (event.isEndElement()) {
				//result ends
				if (event.asEndElement().getName().getLocalPart().compareTo("result") == 0) {
					if (single_result != null) {
						sparqlresult.results.add(single_result);
						single_result = new HashMap<String,String>();
					}
				}
			}
				
		}
		System.out.println(sparqlresult);
	}

}
