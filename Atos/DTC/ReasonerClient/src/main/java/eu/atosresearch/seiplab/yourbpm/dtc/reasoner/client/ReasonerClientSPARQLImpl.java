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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ctc.wstx.evt.WAttribute;

import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.cache.QueryCache;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.IReasoner;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.discovery.SPARQLResultHandler;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLElement;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLMessage;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLOperation;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLService;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLTemplate;

/**
 * @author Mateusz Radzimski
 * @author Yosu Gorroñogoitia
 */
public class ReasonerClientSPARQLImpl implements ReasonerClient{
	// Logger
	static Logger logger = Logger.getLogger(ReasonerClientSPARQLImpl.class);

	private KBProxy kbProxy = null;

	// optimisation
	private static int counter = 0;
	private QueryCache cache;

	public ReasonerClientSPARQLImpl(KBProxy kbProxy, QueryCache cache) {
		this.kbProxy = kbProxy;
		this.cache = cache;
	}

	// public void cleanCache(){
	// cache.cleanCache();
	// }

	/**
	 * 
	 * Do matching against annotations given as paremeters. The last parameter
	 * denote if we are explicitily looking for services or templates. true -
	 * services only, false - templates. The procedure tries to match Services
	 * or Process Fragments using strict mode (query as is) and if none is
	 * found, it relaxes query to allow subsumption matchings.
	 * 
	 * @param FC
	 * @param input
	 * @param output
	 * @param isService
	 * @return returns list of matches (either WSLService or WSLElement)
	 */
	public List<? extends WSLElement> doMSMServiceMatch(List<String> FC,
			List<String> NFP, HashMap<String, ArrayList<String>> input,
			HashMap<String, ArrayList<String>> output, List<String> context,
			boolean isService, int maxFetched) {

		// construct wsml query
		SPARQLPOSMQuery query = new SPARQLPOSMQuery(FC, NFP, input, output,
				context, isService);

		// issue query and retrieve results
		List<POSMResult> results = retrieveMatches(query, maxFetched);

		// if regular query returns no results, try searching by relaxing FC, I,
		// O parameters (include subsumption match)
		// (flexible query)
//		if (results.size() == 0) {
//			results.addAll(retrieveMatchesFlexible(query, maxFetched));
//		}

		// fetch lilo and nfp of services and templates
		if (results.size() != 0) {
			results = fetchOptionalData(results);
		}

		// transform into final return structure
		List<WSLElement> resultList = new ArrayList<WSLElement>();
		for (POSMResult elem : results) {
			resultList.add(elem.getElement());
		}

		return resultList;
	}

	/**
	 * Fetch data that can be optional in Service/Process Framgent description
	 * (NFP) and update description of matches with that data.
	 * 
	 * @param POSMresults
	 * @return
	 */
	
//	select ?nfp WHERE {
//		{
//			<http://www.soa4all.eu/contribution/atos#sap_es_sales_order_create_request_confirmation> <http://www.w3.org/ns/sawsdl#modelReference> ?nfp.
//			?nfp <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.wsmo.org/ontologies/nfp/preferenceOntology#NonFunctionalParameter>
//		} UNION {
//			<http://www.soa4all.eu/contribution/atos#createSalesOrderService> <http://www.w3.org/ns/sawsdl#modelReference> ?nfp.
//			?nfp <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.wsmo.org/ontologies/nfp/preferenceOntology#NonFunctionalParameter>
//		}  
//	}

	
	
	private List<POSMResult> fetchOptionalData(List<POSMResult> POSMresults) {

		int elementNum = 1;
		for (POSMResult row : POSMresults) {
			String serviceURI = row.element.getUri();
			StringBuilder query = new StringBuilder("select ?nfp where {");
			query.append("<" + serviceURI + ">" + CommonNamespacesSPARQL.sawsdl_MR + " ?nfp.");
			query.append("?nfp " + CommonNamespacesSPARQL.rdf_type + CommonNamespacesSPARQL.NFP);
			query.append("}");
			// ask query to the reasoner
			String response = queryWithCache(query.toString());
			List<String> nfp = getNFP (response);
			if (row.getElement() instanceof WSLService) {
				WSLService element = (WSLService) row.getElement();
				if (element.getNFP() == null) {
					List<String> nfps = new ArrayList<String>();
					element.setNFP(nfps);
				}else {
					element.getNFP().addAll(nfp);
				}
			} else {
				WSLTemplate element = (WSLTemplate) row.getElement();
				if (element.getNFP() == null) {
					List<String> nfps = new ArrayList<String>();
					element.setNFP(nfps);
				}else{
					element.getNFP().addAll(nfp);
				}
			}
		}

		// return updated matches
		return POSMresults;
	}

	private List<String> getNFP(String response) {
		List<String> nfps = new ArrayList<String>(); 
		StringTokenizer st = new StringTokenizer (response.substring(1, response.length()-1), ",");
		while (st.hasMoreTokens()){
			String token = st.nextToken();
			nfps.add (token.substring(1, token.length()-1));
		}
		return nfps;
	}

	/**
	 * 
	 * Retrieve Service or Process Fragments that match specified query (FC,
	 * I/O, context) allowing for subsumption matches with regard to FC, Input
	 * and Output
	 * 
	 * @param query
	 *            WSMLPOSMQuery structure
	 * @return Returns a list of matches, with weights attached
	 */
	private List<POSMResult> retrieveMatchesFlexible(SPARQLPOSMQuery query, int maxFetched) {
		// construct flexible WSML query
		// TODO Activate
//		String response = null;
		String response = queryWithCache(query.constructQuery());

		// extract properties from result and build a list of matches
		List<? extends WSLElement> resultList = processResults(response, query, maxFetched);

		// create list of matches with weights attached.
		// TODO assign proper weights
		List<POSMResult> posmResults = new ArrayList<POSMResult>();
		for (WSLElement elem : resultList) {
			POSMResult singleResult = new POSMResult();
			singleResult.setElement(elem);
			singleResult.setMatching(1);
			posmResults.add(singleResult);
		}

		return posmResults;
	}

	/**
	 * Retrieve Service or Process Fragments that match specified query (FC,
	 * I/O, context) The matching is strict which means that only
	 * Services/Process Fragments that matches exactly the query will be
	 * returned.
	 * 
	 * @param query
	 *            WSMLPOSMQuery structure
	 * @return Returns a list of matches, with weights attached
	 */
	private List<POSMResult> retrieveMatches(SPARQLPOSMQuery query, int maxFetched) {
		// construct standard WSML query
		String response = queryWithCache(query.constructQuery());

		// extract properties from result and build a list of matches
		List<? extends WSLElement> resultList = processResults(response, query, maxFetched);

		// create list of matches with weights attached.
		// TODO assign proper weights
		List<POSMResult> posmResults = new ArrayList<POSMResult>();
		for (WSLElement elem : resultList) {
			POSMResult singleResult = new POSMResult();
			singleResult.setElement(elem);
			singleResult.setMatching(1);
			posmResults.add(singleResult);
		}

		return posmResults;
	}

	/**
	 * Retrieve Service or Process Fragments that match specified query (FC,
	 * I/O, context) The matching is strict which means that only
	 * Services/Process Fragments that matches exactly the query will be
	 * returned.
	 * 
	 * @param query
	 *            WSMLPOSMQuery structure
	 * @return Returns a list of matches, with weights attached
	 */
	//TODO update to SPARQL
	public List<POSMResult> retrieveServiceDetails(String query) {
		// construct standard WSML query
		String response = queryWithCache(query);

		// extract properties from result and build a list of matches
		List<WSLService> resultList = processServiceDescription(response);

		// create list of matches with weights attached.
		// TODO assign proper weights
		List<POSMResult> posmResults = new ArrayList<POSMResult>();
		for (WSLElement elem : resultList) {
			POSMResult singleResult = new POSMResult();
			singleResult.setElement(elem);
			singleResult.setMatching(1);
			posmResults.add(singleResult);
		}

		return posmResults;
	}

	private List<WSLService> processServiceDescription(String response) {

		SPARQLResult sparqlresult = null;

		List<WSLService> serviceList = new ArrayList<WSLService>();
		// List<POSMResult> resultList = new ArrayList<POSMResult>();

		// service name -> WSMService
		HashMap<String, WSLService> serviceMap = new HashMap<String, WSLService>();
		// operation name -> WSLOperation
		HashMap<String, WSLOperation> operationMap = new HashMap<String, WSLOperation>();
		// lifting/lowering schema -> wslmessage
		HashMap<String, WSLMessage> messageMap = new HashMap<String, WSLMessage>();

		try {
			// we use parse SPARQL into a structure (variable->value)
			sparqlresult = parseSPARQL(response);

			// System.out.println(query);
			logger.debug(sparqlresult.toString());

			// if (sparqlresult.results.size() == 0) {
			// return serviceList;
			// }

			// iterate over single result rows
			for (HashMap<String, String> row : sparqlresult.results) {

				// service
				WSLService service;
				if (serviceMap.containsKey(row.get("service"))) {
					service = serviceMap.get(row.get("service"));
				} else {
					service = new WSLService();
					service.setServiceName(row.get("service"));
					service.setUri(row.get("service"));
					serviceMap.put(row.get("service"), service);
					serviceList.add(service);
				}

				WSLOperation operation = new WSLOperation();
				// WSLMessage inputmessage = new WSLMessage();
				// WSLMessage outputmessage = new WSLMessage();

				// operation.addInput(inputmessage);

				// operation
				operation.setOperationName(row.get("operation"));
				service.addOperation(operation);

				// input message
				if (messageMap.containsKey(row.get("inputmessage")) == true) {
					WSLMessage inputMesg = messageMap.get(row
							.get("inputmessage"));

					String[] modelref = inputMesg.getModelReferences();
					String[] modelref_new = new String[modelref.length + 1];
					for (int i = 0; i < modelref.length; i++) {
						modelref_new[i] = modelref[i];
					}
					modelref_new[modelref.length] = row
							.get("inputmsgsawsdlmodelref");
					inputMesg.setModelReferences(modelref_new);
				} else {
					WSLMessage inputMesg = new WSLMessage();

					inputMesg.setLoweringSchemaMapping(row
							.get("inputloweringschema"));
					inputMesg.setName(row.get("inputmessage"));
					String[] modelref = new String[1];
					modelref[0] = row.get("inputmsgsawsdlmodelref");
					inputMesg.setModelReferences(modelref);
					operation.addInput(inputMesg);
					messageMap.put(row.get("inputmessage"), inputMesg);
				}

				// output message
				if (messageMap.containsKey(row.get("outputmessage")) == true) {
					WSLMessage inputMesg = messageMap.get(row
							.get("outputmessage"));

					String[] modelref = inputMesg.getModelReferences();
					String[] modelref_new = new String[modelref.length + 1];
					for (int i = 0; i < modelref.length; i++) {
						modelref_new[i] = modelref[i];
					}
					modelref_new[modelref.length] = row
							.get("inputmsgsawsdlmodelref");
					inputMesg.setModelReferences(modelref_new);
				} else {
					WSLMessage inputMesg = new WSLMessage();

					inputMesg.setLoweringSchemaMapping(row
							.get("outputliftingschema"));
					inputMesg.setName(row.get("outputmessage"));
					String[] modelref = new String[1];
					modelref[0] = row.get("outputmsgsawsdlmodelref");
					inputMesg.setModelReferences(modelref);
					operation.setOutput(inputMesg);
					messageMap.put(row.get("outputmessage"), inputMesg);
				}

				// POSMResult singleResult = new POSMResult();
				// singleResult.setElement(service);
				// singleResult.setMatching(1);

			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return serviceList;

	}

	/**
	 * Analyse query results, and feed the WSLService or WSLTemplate structure.
	 * 
	 * @param response
	 * @param query
	 * @return
	 */
	public List<? extends WSLElement> processResults(String response,
			SPARQLPOSMQuery query, int maxFetched) {

		SPARQLResult sparqlresult = null;

		List<WSLService> serviceList = new ArrayList<WSLService>();
		List<WSLTemplate> templateList = new ArrayList<WSLTemplate>();
		// List<POSMResult> resultList = new ArrayList<POSMResult>();

		// service name -> WSMService
		HashMap<String, WSLService> serviceMap = new HashMap<String, WSLService>();
		// operation name -> WSLOperation
		HashMap<String, WSLOperation> operationMap = new HashMap<String, WSLOperation>();
		// lifting/lowering schema -> wslmessage
		HashMap<String, WSLMessage> messageMap = new HashMap<String, WSLMessage>();

		try {
			// we use parse SPARQL into a structure (variable->value)
			sparqlresult = parseSPARQL(response);

			// System.out.println(query);
			logger.debug(sparqlresult.toString());

			// if (sparqlresult.results.size() == 0) {
			// return serviceList;
			// }

			// iterate over single result rows
			int i = 0;
			for (HashMap<String, String> row : sparqlresult.results) {
				if (maxFetched > 0 &&  ++i > maxFetched){ // Limiting max number of fetched results
					break;
				}
				
				WSLService service = new WSLService();
				WSLTemplate template = new WSLTemplate();
				WSLOperation operation = new WSLOperation();
				// WSLMessage inputmessage = new WSLMessage();
				WSLMessage outputmessage = new WSLMessage();

				// operation.addInput(inputmessage);
				operation.setOutput(outputmessage);

				if (query.isService == true) {
					service.addOperation(operation);
				} else {
					template.addOperation(operation);
				}

				// get result description
				for (String key : row.keySet()) {
					String value = row.get(key);

					// get servicename
					if (key.startsWith("service") == true) {
						if (serviceMap.containsKey(value) == true) {
							// service = serviceMap.get(value);

						} else {

							if (query.isService == true) {
								service.setServiceName(value);
								service.setUri(value);
							} else {
								template.setTemplateName(value);
								template.setUri(value);
							}
						}
					}

					// dont need FC
					// if (key.compareTo("FC") == 0) {
					// service.addFC(value);
					// }

					// get service nfp
					if (key.startsWith("nfp") == true) {
						if (query.isService == true) {
							service.addNFP(value);
						} else {
							template.addNFP(value);
						}
					}

					// get service reference
					if (key.compareTo("servicereference") == 0) {
						if (query.isService == true) {
							service.setServiceReference(value);
						} else {
							template.setTemplateReference(value);
						}

					}

					// get operation name
					if (key.compareTo("operation") == 0) {
						if (operationMap.containsKey(value) == true) {
							//
						} else {
							operation.setOperationName(value);
						}
					}

					// set input lowering schema
					if (key.startsWith("inputloweringschema") == true) {
						if (messageMap.containsKey(value) == true) {
							// WSLMessage inputmessage = new WSLMessage();
							// inputmessage.setLoweringSchemaMapping(value);
							// operation.addInput(inputmessage);
						} else {
							// message = new WSLMessage();
						}
					}

					// set output lifting schema
					if (key.startsWith("outputliftingschema") == true) {
						if (messageMap.containsKey(value) == true) {
							outputmessage.setLiftingSchemaMapping(value);
						} else {
							// message = new WSLMessage();
						}
					}

				}

				if (query.isService == true) {
					serviceList.add(service);
					// POSMResult singleResult = new POSMResult();
					// singleResult.setElement(service);
					// singleResult.setMatching(1);

				} else {
					templateList.add(template);
					// POSMResult singleResult = new POSMResult();
					// singleResult.setElement(template);
					// singleResult.setMatching(1);
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		if (query.isService == true) {
			return serviceList;
		} else {
			return templateList;
		}

	}

	/**
	 * Internal method for parsing SPARQL output. It accepts SPARQL XML result
	 * as a string, and returns SPARQLResult object with list of results of map
	 * variable=>value.
	 * 
	 * @param result
	 *            Result XML string conforming to SPARQL response XML schema.
	 * @return Returns result in a form of SPARQLResult, with
	 * @throws XMLStreamException
	 */
	private SPARQLResult parseSPARQL(String result) throws XMLStreamException {
		logger.debug("parsing SPARQL result: " + result);
		SPARQLResult sparqlresult = new SPARQLResult();
		if (result.isEmpty()){
			return sparqlresult;
		}
		
		result = result.substring(1, result.length()-1);
		StringTokenizer st = new StringTokenizer(result, ",");
		
		int i = 0;
		sparqlresult.definition.add("service");
		sparqlresult.definition.add("servicereference");
		sparqlresult.definition.add("operation");
		sparqlresult.definition.add("inputdef");
		sparqlresult.definition.add("outputdef");
//		sparqlresult.definition.add("inputloweringschema");
//		sparqlresult.definition.add("outputliftingschema");
		while (st.hasMoreTokens()){
			HashMap<String, String> single_result = new HashMap<String, String>();
			String token = st.nextToken();
			i = token.indexOf("service=");
			String service = token.substring(i + "service=".length(), token.indexOf(";", i));
			single_result.put ("service", service);
			i = token.indexOf("url=");
			String servicereference = token.substring(i + "url=".length(), token.indexOf(";", i));
			single_result.put ("servicereference", servicereference);
			i = token.indexOf("operation=");
			String operation = token.substring(i + "operation=".length(), token.indexOf(";", i));
			single_result.put ("operation", operation);
			i = token.indexOf("input=");
			String inputdef = token.substring(i + "input=".length(), token.indexOf(";", i));
			single_result.put ("inputdef", inputdef);
			i = token.indexOf("output=");
			String outputdef = token.substring(i + "output=".length(), token.indexOf("]", i));
			single_result.put ("outputdef", outputdef);
//			i = token.indexOf("inputLoweringSchema=");
//			String inputloweringschema = token.substring(i + "inputLoweringSchema=".length(), token.indexOf(";", i));
//			single_result.put ("inputloweringschema", inputloweringschema);
//			i = token.indexOf("outputLiftingSchema=");
//			String outputliftingschema = token.substring(i + "outputLiftingSchema=".length(), token.indexOf("]", i));
//			single_result.put ("outputliftingschema", outputliftingschema);
			sparqlresult.results.add(single_result);
		}

		return sparqlresult;
	}

	public Set<String> getSubClasses(String superClass) {
		String query = "?x subConceptOf _\"" + superClass + "\"";
		Set<String> results = queryWithCacheSimpleParsed(query);
		return results;
	}

	/**
	 * Common methods to issue cached queries to the reasoner
	 * 
	 * @param query
	 *            WSML query string
	 * @return returns SPARQL result XML structure as a string
	 */
	// TODO: connect to KBProxy
	private String queryWithCache(String query) {
		// Cache queries
		// cache queries in a simple Map: query->result
		String response = null;
		String cacheResult = (String) cache.checkCache(query.toString());

		if (cacheResult != null) {
			logger.debug("### from cache");

			response = cacheResult;
		} else {
			ReasonerClientSPARQLImpl.counter++;
			logger.debug("### query no: " + ReasonerClientSPARQLImpl.counter);
			logger.info("Sending query to reasoner: " + query);
			response = kbProxy.query(query.toString());
			cache.saveCache(query.toString(), response);
		}
		return response;
	}

	private Set<String> queryWithCacheSimpleParsed(String query) {
		String response = queryWithCache(query);
		Set<String> results = parseResultSimple(response);
		return results;
	}

	public String executeQuery(String str) {
		return kbProxy.query(str);
	}

	public String genericQuery(String str) throws FileNotFoundException,
			XMLStreamException {
		logger.debug("Consulting SPARQL query: " + str);

		String response = kbProxy.query(str);
		SPARQLResult sparqlresult = parseSPARQL(response);

		// System.out.println(sparqlresult.toString());

		return sparqlresult.toString();
	}

	/**
	 * c1 == c2, according to the ontology
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean exactMatchClasses(String c1, String c2) {
		return getSubClasses(c1).contains(c2) && getSubClasses(c2).contains(c1);
	}

	/**
	 * c1 plugin c2? true: false
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean pluginMatchClasses(String c1, String c2) {
		return getSubClasses(c2).contains(c1);
	}

	private Set<String> parseResultSimple(String result) {
		SPARQLResultHandler handler = new SPARQLResultHandler();
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(result)));
			return handler.getResult();
		} catch (SAXException e) {
			return new HashSet<String>();
		} catch (IOException e) {
			return new HashSet<String>();
		}
	}

	public boolean matchClasses(String[] c1s, String[] c2s) {
		boolean match = true;
		for (int i = 0; i < c1s.length; i++) {
			boolean match2 = false;
			for (int j = 0; j < c2s.length; j++) {
				if (matchClasses(c1s[i], c2s[j])) {
					match2 = true;
					break;
				}
			}
			if (!match2) {
				match = false;
				break;
			}
		}
		return match;
	}

	public boolean matchClasses(String c1, String c2) {
		return exactMatchClasses(c1, c2) || pluginMatchClasses(c1, c2);
	}

}
