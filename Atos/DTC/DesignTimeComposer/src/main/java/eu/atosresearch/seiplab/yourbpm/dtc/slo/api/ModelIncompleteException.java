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

import org.soa4all.lpml.Activity;

public class ModelIncompleteException extends Exception {

	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	private static final long serialVersionUID = 2712599211851748147L;
	private Activity activity;
	private String message;
	
	public ModelIncompleteException(String msg, Activity a) {
		super();

		this.activity = a;
		this.message = msg;
	}
	
	
}
