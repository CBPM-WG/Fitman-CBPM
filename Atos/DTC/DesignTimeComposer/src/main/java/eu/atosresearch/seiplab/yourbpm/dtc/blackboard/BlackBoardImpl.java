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
package eu.atosresearch.seiplab.yourbpm.dtc.blackboard;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardControlAgent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardListener;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModelStatusEnum;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignOperatorApplication;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignTransition;


//TODO Consider to model Blackboard models as a tree

public class BlackBoardImpl implements BlackBoard {
	private URI identifier;
	private BlackBoardControlAgent blackBoardControlAgent;
	private ArrayList<DesignModel> designModels;
	private ArrayList<DesignOperatorApplication> designOperatorApplications;
	
	//XXX
	private HashMap<String, DesignModel> designTransitions;
	
	private DesignModel seed;
	private ArrayList<BlackBoardListener> blackBoardListeners;
	private int dm_index;

	public BlackBoardImpl() {
		this.designModels = new ArrayList<DesignModel>();
		this.designOperatorApplications = new ArrayList<DesignOperatorApplication>();
		this.blackBoardListeners = new ArrayList<BlackBoardListener>();
		this.designTransitions = new HashMap<String, DesignModel>();
		dm_index = 0;
	}

	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	public BlackBoardControlAgent getBlackBoardControlAgent() {
		return blackBoardControlAgent;
	}

	public void setBlackBoardControlAgent(
			BlackBoardControlAgent blackBoardControlAgent) {
		this.blackBoardControlAgent = blackBoardControlAgent;
	}

	
	public void addBlackBoardListener(BlackBoardListener blackBoardListener) {
		this.blackBoardListeners.add(blackBoardListener);

	}

	public boolean updateDesignModel(DesignModel oldDesignModel,
			DesignModel newDesignModel, URI designOperatorURI, URI dma, String adapter, String goal, String ruleName) {
		
		boolean result = false;
		
		// Checking if oldDesignModel has been previously analyzed and tagged as invalid
		if (oldDesignModel.getStatus().isInadmissible()){
			return false;
		}
		
		// Checking if oldDesignModel already exist in the blackboard
		if (!designModels.contains(oldDesignModel)){
			return false;
		}
		
		//Check if newDesignModel is not the same as one of already existing models on the BB
		String sdt = serializeDesignTransitions(newDesignModel.getTransitionChain());
		if (sdt != null && designTransitions.containsKey(sdt)) {
			//model is already there, we don't need it
			newDesignModel = designTransitions.get(sdt);
			result = false;
//			return false; //TODO Possible BUG fixed (Should be confirmed by Mateusz): Duplicate transitions are not shown in the viewer for debugging
		} else {
			//model is brand new, we want it on the blackboard
			designTransitions.put(sdt, newDesignModel);
			result = true;
			
			// First we add the new design model to the blackboard
			addDesignModel(newDesignModel);
			// Now we create the link between the old and the new design model,
			// which is represented as a DesignOperatorApplication
			DesignOperatorApplication designOperatorApplication = new DesignOperatorApplication();
			designOperatorApplication.setSourceDesignModel(oldDesignModel);
			designOperatorApplication.setResultDesignModel(newDesignModel);
			designOperatorApplication.setAgentURI(dma);
			designOperatorApplication.setDesignOperatorURI(designOperatorURI);
			// We should also add a time stamp. We consider here the exact time when
			// the blackboard adds the link
			Date date = new Date(System.currentTimeMillis());
			designOperatorApplication.setDate(date);

			// Finally it is added to the collection of DesignOperatorApplications
			this.designOperatorApplications.add(designOperatorApplication);
		}
		
		// Notifying Blackboard Viewer
		notifyNewModelToViewers(oldDesignModel, newDesignModel,
				designOperatorURI, dma.toString(), adapter, goal, ruleName);
		
		return result;
	}
		
	private void addDesignModel(DesignModel designModel) {
		designModel.setIndex (dm_index++);
		this.designModels.add(designModel);
	}

	private void notifyNewModelToViewers(DesignModel oldDesignModel,
			DesignModel newDesignModel, URI designOperatorURI,
			String dma, String adapter, String goal, String ruleName) {
		for (BlackBoardListener blackBoardListener : this.blackBoardListeners) {
			blackBoardListener.notify(oldDesignModel, newDesignModel, designOperatorURI, dma, adapter, goal, ruleName);	
		}
	}
	
	public void refreshModelInViewers(DesignModel designModel) {
		for (BlackBoardListener blackBoardListener : this.blackBoardListeners) {
			blackBoardListener.notify(designModel);
		}
	}

	public void setSeed(DesignModel designModel) {
		addDesignModel(designModel);
		this.seed=designModel;
		
		//notify viewer in order to show seed model:
		notifyNewModelToViewers(designModel, null,
				null, null, null, null, null);
	}

	public DesignModel getSeed() {
		return this.seed;
	}

	
	public Collection<DesignModel> getDesignModels() {

		return this.designModels;
	}

	
	public Collection<DesignOperatorApplication> getDesignOperatorApplications() {

		return this.designOperatorApplications;
	}

	
	public void setDesignOperatorApplications(
			Collection<DesignOperatorApplication> designModel) {
		// TODO Auto-generated method stub

	}

	public void cleanup() {
		this.designModels = new ArrayList<DesignModel>();
		this.designOperatorApplications = new ArrayList<DesignOperatorApplication>();
		for (BlackBoardListener blackBoardListener : this.blackBoardListeners) {
			blackBoardListener.dispose();
		}
		this.blackBoardListeners = new ArrayList<BlackBoardListener>();
		this.seed = null;
		this.dm_index = 0;
		designTransitions = new HashMap<String, DesignModel>();
	}

	@Override
	public Collection<DesignModel> findIncompleteSolutions() {
		Collection<DesignModel> incompleteSolutions = new ArrayList<DesignModel>();
		
		for (DesignModel dm: getDesignModels()){
			if (dm.getStatus().isIncompleteSolution() || dm.getStatus().isIOUncheckedSolution()){
				incompleteSolutions.add(dm);
			}
		}
		
		return incompleteSolutions;
	}

	@Override
	public void tagModelBranchAsInadmissible(DesignModel designModel) {
		Collection<DesignModel> dms = getBranchFromModel (designModel);
		for (DesignModel dm: dms){
			dm.getStatus().changeStatus(DesignModelStatusEnum.INADMISSIBLE);
		}
	}
	
	@Override
	public void tagModelBranchAsNotSuitable(DesignModel designModel) {
		Collection<DesignModel> dms = getBranchFromModel (designModel);
		for (DesignModel dm: dms){
			dm.getStatus().changeStatus(DesignModelStatusEnum.NOT_SUITABLE);
		}
	}

	private Collection<DesignModel> getBranchFromModel(
			DesignModel designModel) {
		Collection<DesignModel> dms = new ArrayList<DesignModel>();
		DesignOperatorApplication dpa = null;
		DesignModel dm = designModel;
		while ((dpa = getDesignOperatorApplicationForDesignModelSource(dm)) != null){
			dm = dpa.getResultDesignModel();
			dms.add(dm);
		}
		return dms;
	}

	private DesignOperatorApplication getDesignOperatorApplicationForDesignModelSource(
			DesignModel dm) {
		DesignOperatorApplication result = null;
		for (DesignOperatorApplication doa:designOperatorApplications){
			if (doa.getSourceDesignModel() == dm){
				result = doa;
				break;
			}
		}
		return result;
	}

	private String serializeDesignTransitions(LinkedList<DesignTransition> dtlist) {
		if (dtlist == null) {
			return null;
		} else {
			java.util.Collections.sort(dtlist);
			String serial = new String();
			for (DesignTransition dt : dtlist) {
				serial = serial + dt.getSourceURI() + ">" + dt.getTargetURI() + "|";
			}
			return serial;
			
		}
	
	}
	

}
