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

import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLElement;

public class POSMResult {
	WSLElement element;
	double matching;
	
	public WSLElement getElement() {
		return element;
	}
	public void setElement(WSLElement element) {
		this.element = element;
	}
	public double getMatching() {
		return matching;
	}
	public void setMatching(double matching) {
		this.matching = matching;
	}
	
}
