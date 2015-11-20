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
package eu.atosresearch.seiplab.yourbpm.dtc.exception;

public class DTComposerException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3912097415485364074L;
	String message;
	
	public DTComposerException(String msg, Exception e) {
		message = msg + ". " + e.getMessage();
	}
	
	public DTComposerException(String msg) {
		message = msg;
	}

	public String getMessage (){
		return message;
	}
}
