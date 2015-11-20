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
package eu.atosresearch.seiplab.yourbpm.dtc.terminology;

import eu.atosresearch.seiplab.yourbpm.dtc.common.namespace.Namespace;

public class Terminology {
	private Namespace namespace;
	private String terminologyLocation;
	
	public Terminology (){

	}
	
	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}

	public void setTerminologyLocation(String terminologyURL) {
		this.terminologyLocation = terminologyURL;
	}

	public Terminology (Namespace namespace, String terminologyLocation){
		this.namespace = namespace;
		this.terminologyLocation = terminologyLocation;
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public String getTerminologyLocation() {
		return terminologyLocation;
	}
	
	@Override
	public boolean equals (Object o){
		return this.getNamespace().equals(((Terminology)o).getNamespace()) 
				&& this.getTerminologyLocation().equals(((Terminology)o).getTerminologyLocation());
	}
}
