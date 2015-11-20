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
package eu.atosresearch.seiplab.yourbpm.dtc.common.utils.cache;

import java.util.HashMap;

public class QueryCache {
    private HashMap<Object, Object> cache = new HashMap<Object, Object>();
    private boolean useCache;
    
    // NOTE Flag useCache activated by caller
    public QueryCache(boolean useCache) {
		this.useCache = useCache;
	}

	public void cleanCache() {
    	if (useCache) cache.clear();
    }
    
    public Object checkCache(Object query) {
    	if (useCache && cache.containsKey(query)) {
    		return cache.get(query);
    	} else {
    		return null;
    	}
    }

    public void saveCache(Object query, Object result) {
    	if (useCache) cache.put(query, result);
    }
}
