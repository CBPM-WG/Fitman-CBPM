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

import java.net.URI;

import eu.atosresearch.seiplab.yourbpm.dtc.blackboard.BlackBoard;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;


public interface BlackBoardListener {
	public boolean evaluate(BlackBoard blackBoard);
	public void notify (DesignModel oldDesignModel,
			DesignModel designModel, URI newDesignOperator, String dma, String adapter, String goal, String ruleName);
	public void clean ();
	public void notify(DesignModel designModel);
	public void dispose();
}
