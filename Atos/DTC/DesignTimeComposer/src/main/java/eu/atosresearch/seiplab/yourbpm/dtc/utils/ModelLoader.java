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
package eu.atosresearch.seiplab.yourbpm.dtc.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;


public class ModelLoader {

	private HashMap<URI, IProcess> modelCache;

	private ModelLoader() {	
		modelCache = new HashMap<URI, IProcess>();		
	}
	
	private static class ModelLoaderHolder {
		private static final ModelLoader INSTANCE = new ModelLoader();
	}

	public static ModelLoader getInstance() {
		return ModelLoaderHolder.INSTANCE;
	}
	
	public IProcess loadTemplate(URI templateURI) throws URISyntaxException, IOException {
		//first check our cache
		
		if (modelCache.containsKey(templateURI) == true) {
			System.out.println("&&& cache hit: " + templateURI);
			//retrieve from cache
			return modelCache.get(templateURI);
		} else {
			System.out.println("&&& not found in cache: " + templateURI);
			//load template in a regular manner and store it in cache.
			IProcess process = ModelIOUtils.loadTemplate(templateURI);
			modelCache.put(templateURI, process);
			return process;
		}

	}
	
	public void cleanUp() {
		this.modelCache.clear();
	}
		
	
	
}
