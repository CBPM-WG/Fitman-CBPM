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

import java.util.ArrayList;


public class CompatibilityInfo {

	private OperationIO input;
	private ArrayList<OperationIO> output = new ArrayList<OperationIO>();
	private int compatibilityScore;
	
	//type of connector - TODO to be changed from String to some enumerable class from LPML
	//private String connectionType;
	
	/**
	 * @return the input
	 */
	public OperationIO getInput() {
		return input;
	}
	/**
	 * @param input the input to set
	 */
	public void setInput(OperationIO input) {
		this.input = input;
	}
	/*
	public void addInput(OperationIO input) {
		this.input.add(input);
	}
	public void removeInput(OperationIO input) {
		this.input.remove(input);
	}
	*/
	/**
	 * @return the output
	 */
	public ArrayList<OperationIO> getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(ArrayList<OperationIO> output) {
		this.output = output;
	}
	public void addOutput(OperationIO output) {
		this.output.add(output);
	}
	public void removeOutput(OperationIO output) {
		this.output.remove(output);
	}
	/**
	 * @return the compatibilityScore
	 */
	public int getCompatibilityScore() {
		return compatibilityScore;
	}
	/**
	 * @param compatibilityScore the compatibilityScore to set
	 */
	public void setCompatibilityScore(int compatibilityScore) {
		this.compatibilityScore = compatibilityScore;
	}

}
