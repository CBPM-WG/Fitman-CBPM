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
package eu.atosresearch.seiplab.yourbpm.dtc.agents.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import eu.atosresearch.seiplab.yourbpm.dtc.agents.Agent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardControlAgent;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;

public class NewDesignModelEvent extends BlackBoardEvent {
	private DesignModel designModel;
	private BlackBoardControlAgent blackBoardControlAgent;
	private Agent agent;
	private Collection<DesignModel> newDesignModels = new ArrayList<DesignModel>();

	public NewDesignModelEvent(Agent source) {
		super(source);
	}

	public DesignModel getDesignModel() {
		return designModel;
	}

	public void setDesignModel(DesignModel designModel) {
		this.designModel = designModel;
	}

	public BlackBoardControlAgent getBlackBoardControlAgent() {
		return blackBoardControlAgent;
	}

	public void setBlackBoardControlAgent(BlackBoardControlAgent blackBoardControlAgent) {
		this.blackBoardControlAgent = blackBoardControlAgent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Agent getAgent() {
		return this.agent;
	}

//	public void addNewDesignModel(DesignModel newDesignModel) {
//		newDesignModels.add(newDesignModel);
//	}
//	
	public Iterator<DesignModel> getNewDesignModels (){
		return newDesignModels.iterator();
	}

}
