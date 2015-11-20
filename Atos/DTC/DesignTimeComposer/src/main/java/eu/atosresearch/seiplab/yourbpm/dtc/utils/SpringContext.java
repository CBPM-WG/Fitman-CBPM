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
package eu.atosresearch.seiplab.yourbpm.dtc.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

	private static ApplicationContext appContext;
	private static SpringContext instance;
	private final static String CONFIG_FILE = "composerconfiguration.xml";

	public static SpringContext getInstance() {
		if (null == instance) {
			instance = new SpringContext();
			appContext = new ClassPathXmlApplicationContext(CONFIG_FILE);
		}
		return instance;
	}

	public Object getBean(String bean) {
		return appContext.getBean(bean);
	}
}
