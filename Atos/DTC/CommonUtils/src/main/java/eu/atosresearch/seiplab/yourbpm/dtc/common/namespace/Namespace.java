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
package eu.atosresearch.seiplab.yourbpm.dtc.common.namespace;

public class Namespace {
	public static Namespace DEFAULT = new Namespace ("dtc", "http://www.soa4all.eu/wp6/composer#");
	public static Namespace WP6_COMPOSER_TEST = new Namespace("dtct", "http://www.soa4all.eu/wp6/composer/tests#");
	public static Namespace WP6_COMPOSER_OLD_NAMESPACE = new Namespace("dtco", "http://www.soa4all.eu/wp6/1stReview#");
	public static Namespace WP7_NAMESPACE = new Namespace("wp7", "http://www.soa4all.eu/eGoverment/ontology#");
	public static Namespace WP8_NAMESPACE = new Namespace("wp8", "http://www.soa4all.eu/wp8#");
	public static Namespace WP9_NAMESPACE = new Namespace("wp9", "http://www.soa4all.eu/wp9#");
	public static Namespace WEBSHOP_NAMESPACE = new Namespace("ws", "http://www.soa4all.eu/webshop#");
	
	public Namespace (String prefix, String namespace){
		this.prefix = prefix;
		this.namespace = namespace;
	}
	
	String namespace;
	String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String toString(){
		return prefix + ":" + namespace;
	}
}
