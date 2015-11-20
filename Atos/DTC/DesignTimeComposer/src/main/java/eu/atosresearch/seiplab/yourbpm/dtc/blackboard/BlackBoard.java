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
import java.util.Collection;

//import thewebsemantic.Id;
//import thewebsemantic.Namespace;
//import thewebsemantic.RdfProperty;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardControlAgent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardListener;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignOperatorApplication;

//@Namespace("http://eu.soa4all.wp6.lightweighbpml#")
public interface BlackBoard {
	public URI getIdentifier();

	public void setIdentifier(URI identifier);

	public BlackBoardControlAgent getBlackBoardControlAgent();

	public void setBlackBoardControlAgent(
			BlackBoardControlAgent blackBoardControlAgent);

	public void addBlackBoardListener(BlackBoardListener blackBoardListener);

	public boolean updateDesignModel(DesignModel oldDesignModel,
			DesignModel newDesignModel, URI designOperatorURI, URI dma, String adapter, String goal, String ruleName);
	
	public void setSeed(DesignModel designModel);
	public DesignModel getSeed();
	

	//@RdfProperty("http://eu.soa4all.wp6.lightweighbpml#hasDesignModel")
	public Collection<DesignModel> getDesignModels();
	
	//@RdfProperty("http://eu.soa4all.wp6.lightweighbpml#hasDesignOperatorApplication")
	public Collection<DesignOperatorApplication> getDesignOperatorApplications();

	
	public void setDesignOperatorApplications(Collection<DesignOperatorApplication> designModel);

	public void cleanup();

	public Collection<DesignModel> findIncompleteSolutions();

	public void tagModelBranchAsInadmissible(DesignModel designModel);

	public void tagModelBranchAsNotSuitable(DesignModel designModel);
	
	public void refreshModelInViewers(DesignModel designModel);
	
}
