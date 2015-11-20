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


import org.apache.log4j.Logger;

import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.cache.QueryCache;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxyImpl;

/**
 * @author Mateusz Radzimski
 * @author Jesús Gorroñogoitia
 */
public class ReasonerAPISPARQLImpl extends ReasonerAPIImpl {
	
	// Logger
	static Logger logger = Logger.getLogger(ReasonerAPISPARQLImpl.class);	
	

	public ReasonerAPISPARQLImpl(QueryCache cache) {
		super (cache);
		KBProxy kbProxy = KBProxyImpl.getInstance();
		this.reasonerClient = new ReasonerClientSPARQLImpl(kbProxy, cache);
	}

	
	
	public String executeQuery (String query){
		return reasonerClient.executeQuery(query);
	}

}
