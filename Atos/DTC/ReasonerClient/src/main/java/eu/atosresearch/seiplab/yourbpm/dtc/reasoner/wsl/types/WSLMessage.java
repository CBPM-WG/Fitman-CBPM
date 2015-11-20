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

public class WSLMessage extends WSLElement{
	String loweringSchemaMapping;
	String liftingSchemaMapping;
	String[] modelReferences;
	String name;
	
	public String getLoweringSchemaMapping() {
		return loweringSchemaMapping;
	}
	public void setLoweringSchemaMapping(String loweringSchemaMapping) {
		this.loweringSchemaMapping = loweringSchemaMapping;
	}
	public String getLiftingSchemaMapping() {
		return liftingSchemaMapping;
	}
	public void setLiftingSchemaMapping(String liftingSchemaMapping) {
		this.liftingSchemaMapping = liftingSchemaMapping;
	}
	public String[] getModelReferences() {
		return modelReferences;
	}
	public void setModelReferences(String[] modelReferences) {
		this.modelReferences = modelReferences;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer ();
		sb.append("WSLMessage[uri = " + uri);
		sb.append(". LoweringSchemaMapping = " + loweringSchemaMapping);
		sb.append(". LiftingSchemaMapping = " + liftingSchemaMapping);
		
		sb.append(". ModelReferences = [");
		if (modelReferences != null){
			for (String mr: modelReferences){
				sb.append(mr + ",");
			}
		}
		sb.append("]]");
		return sb.toString();
	}
}
