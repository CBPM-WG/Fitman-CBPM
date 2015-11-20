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
package eu.atosresearch.seiplab.yourbpm.dtc.slo.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.Agent;

public class SLOApi {

	
	public static CompatibilityInfo SLO(String wsmlS1, String wsmlS2, String wsmlOntology) {
		
		
		return new CompatibilityInfo();
	}
	
	public static Collection<CompatibilityInfo> SLO(IBinding S1, Collection<IBinding> S2) {
		
		Collection<CompatibilityInfo> cci = new ArrayList<CompatibilityInfo>();
		
		return cci;
	}
	
	//FIXME Provide implementation based on SPARQL
// 	public static LinkedList<CompatibilityInfo> SLO(List<OperationIO> ins, 
// 			List<OperationIO> outs, List<String> ontologies, String serviceLocation) {
//		
//		//TODO debug here (compatibilityInfo doesnt return INPUT element.
//		
//		LinkedList<CompatibilityInfo> compatibilityList = new LinkedList<CompatibilityInfo>();
//		
//		ArrayList<String> inputWSML = new ArrayList<String>();
//		ArrayList<String> outputWSML = new ArrayList<String>();
//
//		LinkedList<String> WSMLContent = new LinkedList<String>();
//		
//		ArrayList<String> IOhandler = new ArrayList<String>();
//		
//		
//		//"reverse" hashtable for storing relation: [ISemanticAnnotation] -> [OperationIO]
//		//for quickly getting Operation (and activity) corresponding to given ISemanticAnnotation (provided as a string)
//		//of type <String, OperationIO>
//		Hashtable<String, OperationIO> reverseHT_in = new Hashtable<String, OperationIO>();
//		Hashtable<String, OperationIO> reverseHT_out = new Hashtable<String, OperationIO>();
//		
//		//construct webservice out of parameters description (ins and outs)
//		for (OperationIO in : ins) {
//			CreateWebService cw = new CreateWebService();
//			org.wsmo.service.WebService ws = (org.wsmo.service.WebService) cw.createWebServiceFromAnnotations(in.getParameter().getSemanticAnnotations(), null);
//			//System.out.println("ws (I) created" + ws.hashCode());
//			
//			StringBuffer sb = new StringBuffer();
//			
//	        Serializer serializer = new WSMLSerializerImpl();
//			TopEntity[] tops = { ws };
//			serializer.serialize(tops, sb);
//	        
//			inputWSML.add(sb.toString());
//			
//			WSMLContent.add(sb.toString());
//			IOhandler.add("I");
//			
//			for (ISemanticAnnotation sa : in.getParameter().getSemanticAnnotations()) {
//				if (reverseHT_in.contains(sa.getReferenceURI())) {
//					System.out.println(" @SLOApi SLO3: [INFO] multiple inputs");
//				}
//				reverseHT_in.put(sa.getReferenceURI(), in);	
//			}
//		}
//		
//		for (OperationIO out : outs) {
//			CreateWebService cw = new CreateWebService();
//			org.wsmo.service.WebService ws = (org.wsmo.service.WebService)cw.createWebServiceFromAnnotations(null, out.getParameter().getSemanticAnnotations());
//			//System.out.println("ws (I) created" + ws.hashCode());
//			
//			StringBuffer sb = new StringBuffer();
//			
//	        Serializer serializer = new WSMLSerializerImpl();
//			TopEntity[] tops = { ws };
//			serializer.serialize(tops, sb);
//	        
//			outputWSML.add(sb.toString());
//
//			WSMLContent.add(sb.toString());
//			IOhandler.add("O");
//
//			for (ISemanticAnnotation sa : out.getParameter().getSemanticAnnotations()) {
//				if (reverseHT_out.contains(sa.getReferenceURI())) {
//					System.out.println(" @SLOApi SLO3: [INFO] multiple outputs");
//				}
//				reverseHT_out.put(sa.getReferenceURI(), out);	
//			}
//		}
//		
//		
//		for (String ont : ontologies ) {
//			try {
//				WSMLContent.add(readFile(ont));
//				IOhandler.add("N");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		String[] IOhandlerArray = new String[IOhandler.size()];
//		IOhandlerArray = IOhandler.toArray(IOhandlerArray); 
//
//		String[] WSMLContentArray = new String[WSMLContent.size()];
//		WSMLContentArray = WSMLContent.toArray(WSMLContentArray);
//		
//		
//		//** Log files Initialisation.
//		String fileLog = "tmp/fileLog.log";
//		
//		//** We required a file to store the Dig result.
//		String fileTells = "tmp/tells.xml";
//		
//		//String serviceLocation = "http://nexof-ra.atosorigin.es:8080/axis2/services/SemanticLinkOperator";
////		String serviceLocation = "http://localhost:9080/axis2/services/SemanticLinkOperator";
//		
//    	SemanticLinkOperatorStub stub;
//    	String xmlResponse = null;
//		try {
//			stub = new SemanticLinkOperatorStub(serviceLocation);
//			xmlResponse = invokeSemanticMapping(stub, WSMLContentArray, IOhandlerArray);
//
//		} catch (AxisFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("AXIS initialisation fault");
//			
//		}
//		
//		if (xmlResponse != null) {
//			XStream xstream = new XStream();
//			HashMap<String, HashMap<String, String[]>> resultHM_copy = (HashMap<String, HashMap<String, String[]>>) xstream.fromXML(xmlResponse);
//			
//			for (String out : resultHM_copy.keySet()) {
//				HashMap<String, String[]> values = resultHM_copy.get(out);
//				for (String in : values.keySet()) {
//					CompatibilityInfo ci = new CompatibilityInfo();
//					
//					OperationIO in_operation = reverseHT_in.get(in);
//					ci.setInput(in_operation);
//					
//					OperationIO out_operation = reverseHT_out.get(out);
//					ci.addOutput(out_operation);
//					
//					ci.setCompatibilityScore(Integer.parseInt(values.get(in)[2]));
//
//					Float matching = Float.parseFloat(values.get(in)[0]);
//					
////					if (matching == 1) {
////						ci.setconnectiontype(connectortypeenumeration.exact);
////					} 
////					if (matching == 0.75) {
////						ci.setconnectiontype(connectortypeenumeration.plug_in);
////					}
//					
//					System.out.println("[SLO after invocation] " + out + " satisfies " + in + " with score: " + values.get(in)[2]);
//				
//					compatibilityList.add(ci);
//				}
//			}			
//		} 
//		
//		return compatibilityList;		
//	}
	
	//FIXME Implement this method without using the SLO service
//    public static String invokeSemanticMapping(SemanticLinkOperatorStub stub, String[] files, String[] IOHandler) {
//    	SemanticLinkOperatorStub.DoSemanticMapping dsm = new SemanticLinkOperatorStub.DoSemanticMapping();
//    	dsm.setFiles(files);
//    	dsm.setIo(IOHandler);
    	
//    	String xmlResponse = null;
    	
//    	try {
//			SemanticLinkOperatorStub.DoSemanticMappingResponse dsmResponse = stub.doSemanticMapping(dsm);
//			xmlResponse = dsmResponse.get_return();
//			
//			System.out.println(xmlResponse);
//			
//			
//			
//			//System.out.println(resultHM_copy);
//			
//			
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("Exception");
//		}
//    	System.out.println("DONE!");
    	
//    	return xmlResponse;
//    }
    
//	public static int getScore(OperationIO input, OperationIO output) {
//		if (input.getParameter().getSemanticAnnotations().get(0).getReferenceURI().toString().compareTo(output.getParameter().getSemanticAnnotations().get(0).getReferenceURI().toString()) == 0) {
//			return 1;
//		} else {
//			if ( (input.getParameter().getSemanticAnnotations().get(0).getReferenceURI().toString().compareTo("http://www.soa4all.eu/webshop#webshopcatalogitem") == 0) &&
//				 (output.getParameter().getSemanticAnnotations().get(0).getReferenceURI().toString().compareTo("http://www.soa4all.eu/webshop#webshopcatalog") == 0)) {
//				return 1;
//			}
//			
//			if ( (input.getParameter().getSemanticAnnotations().get(0).getReferenceURI().toString().compareTo("http://www.soa4all.eu/webshop#Catalogue") == 0) &&
//					 (output.getParameter().getSemanticAnnotations().get(0).getReferenceURI().toString().compareTo("http://www.soa4all.eu/webshop#webshopcatalog") == 0)) {
//				return 1;
//			}
//			
//			return 0;
//		}
//	}
	
	/*
	//XXX this is tentative - to remove later
	private static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			// Instead of using default, pass in a decoder.
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
	}
	*/
	
	private static String readFile(String path) throws IOException {
		
		
		
		//FileInputStream stream = new FileInputStream(path);
	    try {
	    	Reader wsReader = null;
			if (path.startsWith("http://") || path.startsWith("file://")){
				wsReader = new InputStreamReader(new URL(path).openConnection().getInputStream());
			} else {
				wsReader = new InputStreamReader(Agent.class.getResourceAsStream(path));
			}
			
	        StringBuilder builder = new StringBuilder();
	        char[] buffer = new char[8192];
	        int read;
	        while ((read = wsReader.read(buffer, 0, buffer.length)) > 0) {
	            builder.append(buffer, 0, read);
	        }
	        return builder.toString();
	    } finally {
	        // Potential issue here: if this throws an IOException,
	        // it will mask any others. Normally I'd use a utility
	        // method which would log exceptions and swallow them
	        //wsReader.close();
	    }        
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
