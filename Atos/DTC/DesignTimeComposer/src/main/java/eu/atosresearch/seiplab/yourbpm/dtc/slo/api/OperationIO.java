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

//import org.soa4all.lpml.Activity;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;

//import org.soa4all.lpml.IParameter;
//import org.soa4all.lpml.IBinding;

public class OperationIO {

	private IActivity activity = null;
	private IBinding service = null;
	private String operation = null;
	private IParameter parameter = null;
	
	

	/**
	 * @return the activity
	 */
	public IActivity getActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(IActivity activity) {
		this.activity = activity;
	}
	/**
	 * @return the service
	 */
	public IBinding getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(IBinding service) {
		this.service = service;
	}
	/**
	 * @return the parameter
	 */
	public IParameter getParameter() {
		return parameter;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(IParameter parameter) {
		this.parameter = parameter;
	}

	
	public String toString() {
		String out = "[ Activity: "+activity.getName()+", Service: "+ activity.getBindings().get(0).getServiceReference()+", OperationID: "+parameter.getID() +" ]";
		return out;
	}
}
