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
package eu.atosresearch.seiplab.yourbpm.dtc.reasoner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

//import eu.soa4all.semanticspace.client.WSSemanticSpaceStub;

/**
 * The configuration that is used for the reasoners; it provides a programmatical interface to the 
 * properties file.
 * As default the properties file "reasoner.properties" is used.
 * 
 * @author Daniel Winkler
 *
 */
public class Config {

	public static final String PROPERTY_FILE = "reasoner.properties";
	private static final Logger logger = Logger.getLogger(Config.class);

	private final Properties properties;
	
	/**
	 * The Semantic Space WS Stub, or {@code null} if {@link #useServiceAnnotationRepository} returns {@code false}.
	 */
//	public final WSSemanticSpaceStub semanticSpace;
	
	public String semanticSpacesMode;
	
	public String semanticSpacesURL;
	
	public String semanticSpaceID;
	
	public String semanticSpacesLocalOntoDir;

	
	/**
	 * The value for the local ontologies directory.
	 */
	public String localOntologiesDirectory;
	
	/**
	 * States whether or not remote ontologies should be loaded from concept names.
	 */
	public final boolean loadRemoteOntologiesFromConceptNames;
	
	/**
	 * States whether or not the semantic space triple store should be used.
	 */
	public final boolean useServiceAnnotationRepository;

	/**
	 * Returns a new configuration object for the default configuration file. 
	 * 
	 * @return A new configuration object for the default configuration file.
	 */
	public static Config getInstance() {
		return getInstance(PROPERTY_FILE);
	}

	/**
	 * Returns a new configuration object for the specified file. 
	 * 
	 * @param propertyFile The file that is used to initialize the configuration
	 * @return A new configuration object for the specified file.
	 */
	public static Config getInstance(String propertyFile) {
		return new Config(propertyFile);
	}

	/**
	 * Private constructor.
	 * Use static factory methods to initialize.
	 * 
	 * @param configFile The file that is used to initialize the configuration.
	 * @see #getInstance()
	 * @see #getInstance(String)
	 */
	private Config(String configFile) {
		properties = new Properties();
		ClassLoader cl = Config.class.getClassLoader();
		try {
			logger.info("Loading config file: " + configFile);
			InputStream propertyStream = cl.getResourceAsStream(configFile);
			
			if (propertyStream == null) {
				String message = "Resource " + configFile + " could not be found";
				logger.error(message);
				throw new IllegalArgumentException(message);
			}
			
			properties.load(propertyStream);
			logger.info("Config file successfully loaded: " + configFile);
		} catch (IOException e) {
			logger.error("IOException with file " + configFile, e);
		}
		
		this.loadRemoteOntologiesFromConceptNames = getBooleanProperty("loadRemoteOntologiesFromConceptNames", "false");
		this.localOntologiesDirectory = getProperty("localOntologiesDirectory", "ontologies");
		this.useServiceAnnotationRepository = getBooleanProperty("useServiceAnnotationRepository", "true");

//		WSSemanticSpaceStub semanticSpace = null;
//		if (useServiceAnnotationRepository) {
//			try {
//				semanticSpace = new WSSemanticSpaceStub();
//			} catch (AxisFault e) {
//				logger.error("Reasoner not created: Could not connect to Semantic Space WS", e);
//				throw new RuntimeException(e);
//			}
//		}
//		this.semanticSpace = semanticSpace;
	}

	/**
	 * Returns the property value for the specified key.
	 * 
	 * @param key the key.
	 * @return the property value for the specified key.
	 * @see Properties#getProperty(String)
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Returns the property value for the specified key.
	 * 
	 * @param key the key.
	 * @param defaultValue the default value, which is returned if the key is not found in the 
	 * @return the property value for the specified key.
	 * @see Properties#getProperty(String, String)
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	/**
	 * Returns {@code true} if the value of the specified key equals "true" (case is ignored).
	 * 
	 * @param key the key.
	 * @return {@code true} if the value of the specified key equals "true" (case is ignored).
	 * @see Properties#getProperty(String)
	 * @see Boolean#parseBoolean(String)
	 */
	public boolean getBooleanProperty(String key, String defaultValue) {
		return Boolean.parseBoolean(properties.getProperty(key, defaultValue)); 
	}

	/**
	 * Returns  {@code true} if the value of the specified key equals "true" (case is ignored).
	 * 
	 * @param key the key.
	 * @param defaultValue the default value, which is returned if the key is not found in the 
	 * @return  {@code true} if the value (or default value) of the specified key equals "true" (case is ignored).
	 * @see Properties#getProperty(String, String)
	 * @see Boolean#parseBoolean(String)
	 */
	public boolean getBooleanProperty(String key) {
		return Boolean.parseBoolean(properties.getProperty(key)); 
	}

	public void setLocalOntologiesDirectory(String ontologies_dir) {
		this.localOntologiesDirectory = ontologies_dir;
	}

	public String getSemanticSpacesMode() {
		return semanticSpacesMode;
	}

	public void setSemanticSpacesMode(String semanticSpacesMode) {
		this.semanticSpacesMode = semanticSpacesMode;
	}

	public String getSemanticSpacesURL() {
		return semanticSpacesURL;
	}

	public void setSemanticSpacesURL(String semanticSpacesURL) {
		this.semanticSpacesURL = semanticSpacesURL;
	}

	public String getSemanticSpaceID() {
		return semanticSpaceID;
	}

	public void setSemanticSpaceID(String semanticSpaceID) {
		this.semanticSpaceID = semanticSpaceID;
	}

	public String getSemanticSpacesLocalOntoDir() {
		return semanticSpacesLocalOntoDir;
	}

	public void setSemanticSpacesLocalOntoDir(String semanticSpacesLocalOntoDir) {
		this.semanticSpacesLocalOntoDir = semanticSpacesLocalOntoDir;
	}

}
