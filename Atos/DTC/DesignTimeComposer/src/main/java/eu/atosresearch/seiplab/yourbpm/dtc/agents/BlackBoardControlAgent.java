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
package eu.atosresearch.seiplab.yourbpm.dtc.agents;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

//import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import eu.atosresearch.seiplab.yourbpm.dtc.ComposerOperations;
import eu.atosresearch.seiplab.yourbpm.dtc.DTComposerImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.CleanupBlackBoardEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.NewDesignModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.events.TaggedModelEvent;
import eu.atosresearch.seiplab.yourbpm.dtc.blackboard.BlackBoard;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.thread.ThreadPool;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.ModelSolution;
import eu.atosresearch.seiplab.yourbpm.dtc.exception.DTComposerException;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelIOUtils;


public class BlackBoardControlAgent extends Agent implements ApplicationContextAware{
	private Collection<DesignModel> solutions;
	private Collection<DesignModel> incompleteSolutions;
	private ApplicationContext applicationContext; 
	private BlackBoard blackBoard;
	private boolean foundRequiredSolutions=false;
	private boolean findAllSolutions;
	private int maxNumberSolutions;
	private int numberRequestedSolutions;
	private int sizeDMAInvocationPool;
	private int numberFoundSolutions = 0;
	private URI goalTargetURI;
	private ComposerOperations operation;
	private boolean SLODMAActivated = true;
	
	// Logger
	static Logger logger = Logger.getLogger(BlackBoardControlAgent.class);
	
	public int getNumberRequestedSolutions() {
		return numberRequestedSolutions;
	}

	public void setNumberRequestedSolutions(int numberRequestedSolutions) {
		this.numberRequestedSolutions = numberRequestedSolutions;
	}
	
	public int getMaxNumberSolutions() {
		return maxNumberSolutions;
	}

	public void setMaxNumberSolutions(int maxNumberSolutions) {
		this.maxNumberSolutions = maxNumberSolutions;
	}

	public BlackBoardControlAgent(){
		this.solutions = new ArrayList<DesignModel>();
		this.incompleteSolutions = new ArrayList<DesignModel>();
	}
	
	public BlackBoard getBlackBoard() {
		return blackBoard;
	}
	public void setBlackBoard(BlackBoard blackBoard) {
		this.blackBoard = blackBoard;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext= applicationContext;
		if (applicationContext.getBeansOfType(SemanticLinkOperatorAgent.class).isEmpty()){
			SLODMAActivated = false;
		}
	}
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}
	
	public String toString() {
		return this.getIdentifier().toString();
	}


	private void notifySolution(DesignModel solutionDesignModel, URI designAnalysisAgentURI) throws IOException{
		
		// Save Solution
		// Filtering initial model
		if (solutionDesignModel.getDesignStructure().getID() == blackBoard.getSeed().getDesignStructure().getID()){
			return;
		}
		
		numberFoundSolutions++;
		
		URL url = ModelIOUtils.saveSolutionModel (solutionDesignModel);
		solutionDesignModel.setUrl(url);
		if (!solutions.contains(solutionDesignModel)){
			solutions.add(solutionDesignModel);
		}
		
		if (!findAllSolutions && numberFoundSolutions >= numberRequestedSolutions){
			foundRequiredSolutions = true;
			if (DTComposerImpl.getInstance().getConfiguration().isThread()){
				ThreadPool.getInstance().shutdown();
			}
		}
		
	}


	public void updateDesignModel(DesignModel oldDesignModel,
			DesignModel newDesignModel, URI designOperatorURI, URI agentURI, String adapter, String goal, String ruleName) {
				
		if (foundRequiredSolutions || numberFoundSolutions >= maxNumberSolutions){
			return;
		}
		
		System.out.println ("*********************** New Model: " + newDesignModel.getIdentifier());
		
		// Adding the new design model to the blackboard
		boolean updateResult = getBlackBoard().updateDesignModel(oldDesignModel, newDesignModel, designOperatorURI, agentURI, adapter, goal, ruleName);

		if (updateResult == true) {
			// Notifying to Agents
			notifyNewDesignModel(newDesignModel);
		}
	}
	
	private Collection<DesignModel> getKnownSolutions(){
		return this.solutions;
	}

	/**
	 * TODO: Improve ranking algorithm
	 * Ranking algorithm:
	 * - selected firstly: admissible and suitable incomplete solutions
	 * - selected secondly: admissible but not suitable incomplete solutions
	 * TODO: Improvement: under same classification, select first that model with higher number of modifications
	 * @param incompleteSolutions
	 * @return
	 */
	private Collection<DesignModel> rank(
			Collection<DesignModel> incompleteSolutions) {
		Collection <DesignModel> result1 = new ArrayList<DesignModel>();
		// First Admissible and Suitable solutions
		for (DesignModel solution: incompleteSolutions){
			if (solution.getStatus().isAdmissible() && solution.getStatus().isSuitable()){
				result1.add(solution);
			}
		}
		Object[] r1 = result1.toArray();
		//TODO Consider to rank then not only for the number of unsolved goals but also by the number of changes applied
		Arrays.sort(r1);
		
		Collection <DesignModel> result2 = new ArrayList<DesignModel>();
		// Second Admissible and not suitable solutions
		for (DesignModel solution: incompleteSolutions){
			if (solution.getStatus().isAdmissible() && solution.getStatus().isNotSuitable()){
				result2.add(solution);
			}
		}
		
		Object[] r2 = result2.toArray();
		Arrays.sort(r2);
		
		Collection <DesignModel> result = new ArrayList<DesignModel>();
		for (int i=0; i<r1.length; i++){
			result.add((DesignModel)r1[i]);
		}
		for (int i=0; i<r2.length; i++){
			result.add((DesignModel)r2[i]);
		}
		
		return result;
	}

	public boolean isFindAllSolutions() {
		return findAllSolutions;
	}

	public void setFindAllSolutions(boolean findAllSolutions) {
		this.findAllSolutions = findAllSolutions;
	}
	
	private void exit () throws MalformedURLException{
		pressAnyKey("Press the enter key to exit");
		
		cleanup ();
		System.exit (0);
	}
	
	public void cleanup() throws MalformedURLException{
		
		// Cleaning posted design models
		String tmpdir = DTComposerImpl.getInstance().getConfiguration().getTEMP_URL();
		File tmp;
		if (tmpdir.startsWith("file:")){
			tmp = new File (
					new URL (
							DTComposerImpl.getInstance().getConfiguration().getTEMP_URL()).getFile());
		} else {
			tmp = new File (DTComposerImpl.getInstance().getConfiguration().getTEMP_URL());
		}
		
		//File tmp = new File ("/models/tmp/");
		File[] tmpfiles = tmp.listFiles();
		for (int i=0; i<tmpfiles.length;i++){
			tmpfiles[i].delete();
		}
		
		// Cleaning blackboardControlAgent
		numberFoundSolutions = 0;
		foundRequiredSolutions = false;
		this.solutions = new ArrayList<DesignModel>();
		this.incompleteSolutions = new ArrayList<DesignModel>();
		resetSizeDMAInvocationPool();
		if (DTComposerImpl.getInstance().getConfiguration().isThread()){
			ThreadPool.getInstance().reset();
		}
		
		// Cleaning blackboard
		blackBoard.cleanup();
		
		// Cleaning up DMA
		cleanupDMA();
	}

	private void cleanupDMA() {
		CleanupBlackBoardEvent ce = new CleanupBlackBoardEvent(this);
		getApplicationContext().publishEvent(ce);
	}

	private void pressAnyKey(String message) {
		Scanner keyIn = new Scanner(System.in);
		System.out.print(message);
		keyIn.nextLine();
	}

	public Collection<ModelSolution> getSolutions() throws IOException, URISyntaxException, DTComposerException, InterruptedException {
		// Block until model full processed or solution found
		if (DTComposerImpl.getInstance().getConfiguration().isThread()){
			waitForBlackboardComplete ();
		}
		
		
		// Collecting found solutions
		int index = 0;
		ArrayList<ModelSolution> solutions = new ArrayList<ModelSolution>();
		for (DesignModel solution: getKnownSolutions()){
			if (index < numberFoundSolutions){
				ModelSolution ms = new ModelSolution();
				ms.setComplete(true);
//				setUri(solution.getUrl(), ms);
				ms.setModel(ModelIOUtils.loadModelAsString(solution.getUrl()));
				solutions.add(ms);
				index++;
			}	
		}
		
		// In case no solutions have been found we collect incomplete solutions
		
		if (getKnownSolutions().isEmpty()){
			incompleteSolutions = rank (blackBoard.findIncompleteSolutions ());
			
			for (DesignModel incompleteSolution: incompleteSolutions){
				// Filtering initial model
				if (incompleteSolution.getDesignStructure().getID() == blackBoard.getSeed().getDesignStructure().getID()){
					continue;
				}
				if (index >= numberRequestedSolutions){
					break;
				}
				URL url = ModelIOUtils.saveSolutionModel(incompleteSolution);
				ModelSolution ms = new ModelSolution();
				ms.setComplete(false);
//				setUri(url, ms);
				ms.setModel (ModelIOUtils.loadModelAsString(url));
				solutions.add(ms);
				index++;
			}
		}
		
		return solutions;
	}

	private void waitForBlackboardComplete() throws InterruptedException {
		// Waiting until model requested found or all models processed
		while (!foundRequiredSolutions && getSizeDMAInvocationPool() != 0){
			Thread.currentThread();
			Thread.sleep(500);
		}
		logger.debug("Found solutions or blackboard complete processed");
	}

//	private void setUri(URL url, ModelSolution ms) throws DTComposerException, MalformedURLException {
//		if (DTComposerImpl.getInstance().getConfiguration().getStorage().equalsIgnoreCase("filesystem")){
////			ms.setUri(UriBuilder.fromPath(url.getPath()).port(url.getPort()).scheme(url.getProtocol()).build());
//			ms.setUri(url.getPath());
//		} else if (DTComposerImpl.getInstance().getConfiguration().getStorage().equalsIgnoreCase("webserver")){
//			String filename = url.getPath().substring(url.getPath().lastIndexOf('/')+1);
//			url = new URL (DTComposerImpl.getInstance().getConfiguration().getUrlbase() + filename);
////			ms.setUri(UriBuilder.fromPath(url.getPath()).host(url.getHost()).port(url.getPort()).scheme(url.getProtocol()).build());
//			ms.setUri(url.toString());
//		} else if (DTComposerImpl.getInstance().getConfiguration().getStorage().equalsIgnoreCase("semanticspace")){
//			throw new DTComposerException (new Exception ("SemanticSpace storage not supported yet"));
//		}
//	}

	public void setInitialModel(DesignModel initialModel) {
		// Set initialModel on blackboard
		resetSizeDMAInvocationPool();
		blackBoard.setSeed(initialModel);
		notifyNewDesignModel(initialModel);
	}

	private void notifyNewDesignModel(DesignModel initialModel) {
		// Notify new design model event in the blackboard
		NewDesignModelEvent newDesignModelEvent = new NewDesignModelEvent(this);
		newDesignModelEvent.setDesignModel(initialModel);
		newDesignModelEvent.setBlackBoardControlAgent(this);
		
		increaseSizeDMAInvocationPool(getNumberNotifiedAgents());
		
		getApplicationContext().publishEvent(
				newDesignModelEvent);
		
//		// Disposal
//		newDesignModelEvent = null;
	}
	
	private void notifyTaggedModel(DesignModel initialModel) {
		TaggedModelEvent taggedModelEvent = new TaggedModelEvent(this);
		taggedModelEvent.setDesignModel(initialModel);
		taggedModelEvent.setBlackBoardControlAgent(this);
		
		increaseSizeDMAInvocationPool(getNumberNotifiedAgents());
		
		getApplicationContext().publishEvent(
				taggedModelEvent);
	}
	
	public void notifyAgentResponse (NewDesignModelEvent event) throws IOException, URISyntaxException{
		// Response from DesignModificationAgent
//		if (event.getAgent() instanceof DesignModificationAgent){
//			processDesignModificationAgentResponse(event);
//		}
		
		// Response from DesignAnalysisAgent
		if (event.getAgent() instanceof DesignAnalysisAgent) {
			processDesignAnalysisAgentResponse(event);
		}
	}

	private void processDesignModificationAgentResponse(NewDesignModelEvent event) {
		DesignModel oldDesignModel = event.getDesignModel();
		
		// Checking if notification of modification 
		Iterator<DesignModel> iter = event.getNewDesignModels();
		if (!iter.hasNext()){
			// No modifications
			return;
		}
		
		while (iter.hasNext()){
			DesignModel newDesignModel = iter.next();
			updateDesignModel(oldDesignModel, newDesignModel, 
					newDesignModel.getDesignOperatorAppliedURI(), newDesignModel.getAgentInvokedURI(), newDesignModel.getAdapterUsed(), newDesignModel.getGoalReplaced(), newDesignModel.getAppliedRule());
		}

	}

	private void processDesignAnalysisAgentResponse(NewDesignModelEvent event) throws IOException, URISyntaxException {
		DesignModel oldDesignModel = event.getDesignModel();
		
		// Checking if notification of solution
		// Solution
		if (oldDesignModel.getStatus().isSolution()){
			notifySolution(oldDesignModel, event.getAgent().getIdentifier());	
		}// Unchecked solution: If SLO DMA is NOT activated
		else if (oldDesignModel.getStatus().isIOUncheckedSolution() && !isSLODMAActivated()){
			notifySolution(oldDesignModel, event.getAgent().getIdentifier());
		} else if (oldDesignModel.getStatus().isIOUncheckedSolution() && !isProcessTarget()){
			notifySolution(oldDesignModel, event.getAgent().getIdentifier());
		}
		
		// Checking not suitable or not admissible model. Reject that reasoning path
		// TODO We are not yet considering violation of constraints so inadmissible checking has not been debugged
		// TODO Consider to amend both inadmissible and not suitable models (reasoning paths)
		if (oldDesignModel.getStatus().isInadmissible()){
			blackBoard.tagModelBranchAsInadmissible(oldDesignModel);
		}
		
// Yosu Bug: don't tag model branch as not suitable
//		if (oldDesignModel.getStatus().isNotSuitable()){
//			blackBoard.tagModelBranchAsNotSuitable(oldDesignModel);
//		}
		
		// Update Blackboard Viewer
		blackBoard.refreshModelInViewers(oldDesignModel);
		
		//now notify agents with "tagged model event"
		this.notifyTaggedModel(event.getDesignModel());
		
	}

	
	private boolean isSLODMAActivated() {
		return SLODMAActivated;
	}

	private int getNumberNotifiedAgents (){
		return getApplicationContext().getBeanNamesForType(ApplicationListener.class).length;
	}
	


	public void setGoalTarget(URI goalTargetURI) {
		this.goalTargetURI = goalTargetURI;
	}
	
	public URI getGoalTarget() {
		return this.goalTargetURI;
	}

	public void setOperation(ComposerOperations operation) {
		this.operation = operation;
	}
	
	public ComposerOperations getOperation() {
		return this.operation;
	}
	
	public boolean isProcessTarget()
	throws URISyntaxException {
		boolean result = false;
		ComposerOperations operation = getOperation();
		URI goalTarget = getGoalTarget();

		// Target not set, complete model is processed

		// special case is "RESOLVE_DATAFLOW", that is analogical to RESOLVE_PROCESS* 
		// where we also don't specify target activity.
		if (goalTarget == null && (
				operation == ComposerOperations.RESOLVE_PROCESS || 
				operation == ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE || 
				operation == ComposerOperations.RESOLVE_PROCESS_WITH_WS ||
				operation == ComposerOperations.RESOLVE_DATAFLOW
			)) {
			result = true;
		}
		return result;
	}
	
//	public synchronized void  increaseSizeDMAInvocationPool (){
//		sizeDMAInvocationPool++;
//		logger.debug("Increased sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
//	}
	
	public synchronized void  increaseSizeDMAInvocationPool (int size){
		sizeDMAInvocationPool+= size;
		logger.debug("Increased sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
	}
	
	public synchronized void decreaseSizeDMAInvocationPool (){
		sizeDMAInvocationPool--;
		logger.debug("Decreased sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
	}
	
	public synchronized int getSizeDMAInvocationPool (){
		return sizeDMAInvocationPool;
	}
	
	public synchronized void resetSizeDMAInvocationPool (){
		sizeDMAInvocationPool = 0;
		logger.debug("Reset sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
	}
	
	public boolean isFoundRequiredSolutions (){
		return foundRequiredSolutions;
	}
}