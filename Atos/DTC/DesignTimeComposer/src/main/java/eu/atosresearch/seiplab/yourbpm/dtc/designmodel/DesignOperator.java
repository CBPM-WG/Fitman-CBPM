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
package eu.atosresearch.seiplab.yourbpm.dtc.designmodel;

import java.net.URI;

//import thewebsemantic.Namespace;
//@Namespace("http://eu.soa4all.wp6.designtimecomposer#")
public class DesignOperator {
	private URI identifier;
	private URI adapter;
	private URI process;
	private AssignmentSet assignmentSet;
	
	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	/**
	 * @param assignmentSet the assignmentSet to set
	 */
	public void setAssignmentSet(AssignmentSet assignmentSet) {
		this.assignmentSet = assignmentSet;
	}

	/**
	 * @return the assignmentSet
	 */
	public AssignmentSet getAssignmentSet() {
		return assignmentSet;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcess(URI process) {
		this.process = process;
	}

	/**
	 * @return the process
	 */
	public URI getProcess() {
		return process;
	}

	/**
	 * @param adapter the adapter to set
	 */
	public void setAdapter(URI adapter) {
		this.adapter = adapter;
	}

	/**
	 * @return the adapter
	 */
	public URI getAdapter() {
		return adapter;
	}
	
}
