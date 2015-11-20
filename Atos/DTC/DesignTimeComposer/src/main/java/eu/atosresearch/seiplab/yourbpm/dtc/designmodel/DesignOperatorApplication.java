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
package eu.atosresearch.seiplab.yourbpm.dtc.designmodel;

import java.net.URI;
import java.util.Date;

//import thewebsemantic.Namespace;

//@Namespace("http://eu.soa4all.wp6.designtimecomposer#")
public class DesignOperatorApplication {
	private DesignModel sourceDesignModel;
	private DesignModel resultDesignModel;
	private URI agentURI;
	private URI designOperatorURI;
	private Date date;

	public DesignModel getSourceDesignModel() {
		return sourceDesignModel;
	}

	public void setSourceDesignModel(DesignModel sourceDesignModel) {
		this.sourceDesignModel = sourceDesignModel;
	}

	public DesignModel getResultDesignModel() {
		return resultDesignModel;
	}

	public void setResultDesignModel(DesignModel resultDesignModel) {
		this.resultDesignModel = resultDesignModel;
	}

	public URI getAgentURI() {
		return this.agentURI;
	}

	public void setAgentURI(URI agentURI) {
		this.agentURI = agentURI;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public URI getDesignOperatorURI() {
		return designOperatorURI;
	}

	public void setDesignOperatorURI(URI designOperatorURI) {
		this.designOperatorURI = designOperatorURI;
	}
}
