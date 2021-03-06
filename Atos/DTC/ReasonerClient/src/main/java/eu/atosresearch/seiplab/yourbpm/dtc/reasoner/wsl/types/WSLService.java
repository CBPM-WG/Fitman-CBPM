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

public class WSLService extends WSLElement{
	
	private String serviceName; 
	List<WSLOperation> operations = new ArrayList<WSLOperation>();
	private List<String> FC;
	private List<String> NFP;
	private List<String> Precondition;
	private List<String> Effect;
	
	private String serviceReference;	
	
	String definition;
	
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public List<WSLOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<WSLOperation> operations) {
		this.operations = operations;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer ();
		sb.append("WSLService[uri = " + uri);
		sb.append(". Definition = " + definition);
		sb.append(". Operations = [");
		for (WSLOperation op: operations){
			sb.append(op + ",");
		}
		
		sb.append("]]");
		return sb.toString();
	}
	
	/**
	 * @return the fC
	 */
	public List<String> getFC() {
		return FC;
	}
	/**
	 * @param fc the fC to set
	 */
	public void setFC(List<String> fc) {
		FC = fc;
	}
	/**
	 * @return the nFP
	 */
	public List<String> getNFP() {
		return NFP;
	}
	/**
	 * @param nfp the nFP to set
	 */
	public void setNFP(List<String> nfp) {
		NFP = nfp;
	}
	/**
	 * @return the precondition
	 */
	public List<String> getPrecondition() {
		return Precondition;
	}
	/**
	 * @param precondition the precondition to set
	 */
	public void setPrecondition(List<String> precondition) {
		Precondition = precondition;
	}
	/**
	 * @return the effect
	 */
	public List<String> getEffect() {
		return Effect;
	}
	/**
	 * @param effect the effect to set
	 */
	public void setEffect(List<String> effect) {
		Effect = effect;
	}
	/**
	 * @return the serviceReference
	 */
	public String getServiceReference() {
		return serviceReference;
	}
	/**
	 * @param serviceReference the serviceReference to set
	 */
	public void setServiceReference(String serviceReference) {
		this.serviceReference = serviceReference;
	}
	
	public void addFC(String fc) {
		this.FC.add(fc);
	}
	
	public void addOperation(WSLOperation op) {
		this.operations.add(op);
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public void addNFP(String value) {
		if (NFP == null){
			NFP = new ArrayList<String>();
		}
		NFP.add(value);
	}
}
