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
package eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types;

import java.util.ArrayList;
import java.util.List;

public class WSLOperation extends WSLElement {
	
	List<WSLMessage> input = new ArrayList<WSLMessage>();
	WSLMessage output;
	String[] modelReferences;
	String operationName;
	
	public String[] getModelReferences() {
		return modelReferences;
	}
	public void setModelReferences(String[] modelReferences) {
		this.modelReferences = modelReferences;
	}
	public List<WSLMessage> getInput() {
		return input;
	}
	public void setInput(List<WSLMessage> input) {
		this.input = input;
	}
	public void addInput(WSLMessage input) {
		this.input.add(input);
	}
	
	public WSLMessage getOutput() {
		return output;
	}
	public void setOutput(WSLMessage output) {
		this.output = output;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer ();
		sb.append("WSLOperation[uri = " + uri);
		sb.append(". Input = " + input);
		sb.append(". Output = " + output);
		
		sb.append(". ModelReferences = [");
		if (modelReferences != null){
			for (String mr: modelReferences){
				sb.append(mr + ",");
			}
		}
		sb.append("]]");
		return sb.toString();
	}
	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}
	/**
	 * @param operationName the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	
}
