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

public class TaggedModelEvent extends NewDesignModelEvent {

	public TaggedModelEvent(Agent source) {
		super(source);
	}


}
