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

import eu.atosresearch.seiplab.yourbpm.dtc.agents.DesignModificationSemanticAgent;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPI;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPIImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client.ReasonerAPISPARQLImpl;

public class DesignModificationSPARQLAgent extends DesignModificationSemanticAgent {

	public DesignModificationSPARQLAgent(ReasonerAPISPARQLImpl reasoner) {
		super((ReasonerAPIImpl)reasoner);
	}
}
