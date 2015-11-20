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
package eu.atosresearch.seiplab.yourbpm.dtc.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class ComposerConfiguration {

	private String INSTALLATION_DIR;

	private String INPUT_MODELS_DIR;
	private String OUTPUT_MODELS_DIR;
	private String TEMPLATES_DIR;
	private String TEMP_DIR;
	
	private String ONTOLOGIES_DIR;
	private String GOALS_DIR;
	private String SERVICES_DIR;
	
	private String INPUT_MODELS_URL;
	private String OUTPUT_MODELS_URL;
	private String TEMPLATES_URL;
	private String TEMP_URL;
	
	private String ONTOLOGIES_URL;
	private String GOALS_URL;
	private String SERVICES_URL;
	
	private String SPACES_URL;
	private String SPACES_LOCALONTO_DIR;
	private String SPACE_ID;
	private String spaces_mode;
	
	private boolean debug = false;
	private boolean service = false;
	private boolean thread = false;
	
	public boolean isThread() {
		return thread;
	}

	public void setThread(boolean thread) {
		this.thread = thread;
	}

	private String template_storage_method = "filesystem";
	private String storage_url;
	private String template_repository; 
	

	private String DSLR_DMA;
	private String DSLR_DAA;
	
	public String getINSTALLATION_DIR() {
		//Not set by configuration, set assumed DTComposer project dir from current classpath
//		if (INSTALLATION_DIR == null){ 
//			INSTALLATION_DIR = getWorkingDir() + File.separatorChar;
//			int i;
//			if ((i = INSTALLATION_DIR.indexOf("DesignTimeComposer_v2")) != -1){
//				INSTALLATION_DIR = INSTALLATION_DIR.substring(0, i);
//			}
//		}
		return INSTALLATION_DIR;
	}
	
	public void setINSTALLATION_DIR(String installation_dir) {
		INSTALLATION_DIR = installation_dir;
	}
	
	public String getStorage_url() {
		return storage_url;
	}
	public void setStorage_url(String storage_url) {
		this.storage_url = storage_url;
	}
	/**
	 * @return the iNPUT_MODELS_DIR
	 */
	public String getINPUT_MODELS_DIR() {
		return INPUT_MODELS_DIR;
	}
	/**
	 * @param input_models_dir the iNPUT_MODELS_DIR to set
	 */
	public void setINPUT_MODELS_DIR(String input_models_dir) {
		if (INSTALLATION_DIR != null && !INSTALLATION_DIR.equals("")){
			INPUT_MODELS_DIR = getINSTALLATION_DIR() + "/DesignTimeComposer_v2/src/main/resources/" + input_models_dir;
			setINPUT_MODELS_URL("file:///" + INPUT_MODELS_DIR);
		} else {
			INPUT_MODELS_DIR = input_models_dir;
			setINPUT_MODELS_URL(INPUT_MODELS_DIR);
		}
	}
	/**
	 * @return the oUTPUT_MODELS_DIR
	 */
	public String getOUTPUT_MODELS_DIR() {
		return OUTPUT_MODELS_DIR;
	}
	/**
	 * @param output_models_dir the oUTPUT_MODELS_DIR to set
	 */
	public void setOUTPUT_MODELS_DIR(String output_models_dir) {
		if (INSTALLATION_DIR != null && !INSTALLATION_DIR.equals("")){
			OUTPUT_MODELS_DIR = getINSTALLATION_DIR() + output_models_dir;
			
		} else {
			OUTPUT_MODELS_DIR =  getWorkingDir() + File.separatorChar + output_models_dir;
		}

		setOUTPUT_MODELS_URL("file:///" + OUTPUT_MODELS_DIR);
	}
	/**
	 * @return the tEMPLATES_DIR
	 */
	public String getTEMPLATES_DIR() {
		return TEMPLATES_DIR;
	}
	/**
	 * @param templates_dir the tEMPLATES_DIR to set
	 */
	public void setTEMPLATES_DIR(String templates_dir) {
		if (INSTALLATION_DIR != null && !INSTALLATION_DIR.equals("")){
			TEMPLATES_DIR = getINSTALLATION_DIR() + "/DesignTimeComposer_v2/src/main/resources/" + templates_dir;
			setTEMPLATES_URL("file:///" + TEMPLATES_DIR);
		} else {
			TEMPLATES_DIR = templates_dir;
			setTEMPLATES_URL(TEMPLATES_DIR);
		}
	}
	/**
	 * @return the tEMP_DIR
	 */
	public String getTEMP_DIR() {
		return TEMP_DIR;
	}
	/**
	 * @param temp_dir the tEMP_DIR to set
	 */
	public void setTEMP_DIR(String temp_dir) {
		if (INSTALLATION_DIR != null && !INSTALLATION_DIR.equals("")){
			TEMP_DIR = getINSTALLATION_DIR() + "/" + temp_dir;
			setTEMP_URL("file:///" + TEMP_DIR);
		} else {
			TEMP_DIR =  getWorkingDir()  + File.separatorChar + temp_dir;
			setTEMP_URL("file:///" + TEMP_DIR);
		}
	}
	/**
	 * @return the iNPUT_MODELS_URL
	 */
	public String getINPUT_MODELS_URL() {
		return INPUT_MODELS_URL;
	}
	/**
	 * @param input_models_url the iNPUT_MODELS_URL to set
	 */
	public void setINPUT_MODELS_URL(String input_models_url) {
		INPUT_MODELS_URL = input_models_url;
	}
	/**
	 * @return the oUTPUT_MODELS_URL
	 */
	public String getOUTPUT_MODELS_URL() {
		return OUTPUT_MODELS_URL;
	}
	/**
	 * @param output_models_url the oUTPUT_MODELS_URL to set
	 */
	public void setOUTPUT_MODELS_URL(String output_models_url) {
		OUTPUT_MODELS_URL = output_models_url;
	}
	/**
	 * @return the tEMP_URL
	 */
	public String getTEMP_URL() {
		return TEMP_URL;
	}
	/**
	 * @param temp_url the tEMP_URL to set
	 */
	public void setTEMP_URL(String temp_url) {
		TEMP_URL = temp_url;
	}	
	
	public String getTEMPLATES_URL() {
		return TEMPLATES_URL;
	}
	
	public void setTEMPLATES_URL(String templates_url) {
		TEMPLATES_URL = templates_url;
	}
	
	/**
	 * @return the oNTOLOGIES_URL
	 */
	public String getONTOLOGIES_URL() {
		return ONTOLOGIES_URL;
	}
	/**
	 * @param ontologies_url the oNTOLOGIES_URL to set
	 */
	public void setONTOLOGIES_URL(String ontologies_url) {
		ONTOLOGIES_URL = ontologies_url;
	}
	/**
	 * @return the gOALS_URL
	 */
	public String getGOALS_URL() {
		return GOALS_URL;
	}
	/**
	 * @param goals_url the gOALS_URL to set
	 */
	public void setGOALS_URL(String goals_url) {
		GOALS_URL = goals_url;
	}
	/**
	 * @return the sERVICES_URL
	 */
	public String getSERVICES_URL() {
		return SERVICES_URL;
	}
	/**
	 * @param services_url the sERVICES_URL to set
	 */
	public void setSERVICES_URL(String services_url) {
		SERVICES_URL = services_url;
	}
	/**
	 * @return the oNTOLOGIES_DIR
	 */
	public String getONTOLOGIES_DIR() {
		return ONTOLOGIES_DIR;
	}
	/**
	 * @param ontologies_dir the oNTOLOGIES_DIR to set
	 */
	public void setONTOLOGIES_DIR(String ontologies_dir) {
		if (INSTALLATION_DIR != null && !INSTALLATION_DIR.equals("")){
			ONTOLOGIES_DIR = INSTALLATION_DIR + "/DesignTimeComposer_v2/src/main/resources/" + ontologies_dir;
		} else {
			ONTOLOGIES_DIR = ontologies_dir;
		}
		setONTOLOGIES_URL("file:///" + INSTALLATION_DIR + "/DesignTimeComposer_v2/src/main/resources/" + ontologies_dir);
	}
	/**
	 * @return the gOALS_DIR
	 */
	public String getGOALS_DIR() {
		return GOALS_DIR;
	}
	/**
	 * @param goals_dir the gOALS_DIR to set
	 */
	public void setGOALS_DIR(String goals_dir) {
		GOALS_DIR = getINSTALLATION_DIR() + "DesignTimeComposer_v2/src/main/resources" + goals_dir;
		setGOALS_URL("file:///" + getINSTALLATION_DIR() + "/DesignTimeComposer_v2/src/main/resources/" + goals_dir);
	}
	/**
	 * @return the sERVICES_DIR
	 */
	public String getSERVICES_DIR() {
		return SERVICES_DIR;
	}
	/**
	 * @param services_dir the sERVICES_DIR to set
	 */
	public void setSERVICES_DIR(String services_dir) {
		SERVICES_DIR = services_dir;
		setSERVICES_URL("file:///" + getINSTALLATION_DIR() + "/DesignTimeComposer_v2/src/main/resources/" + services_dir);
	}
	
	
	public String getSPACES_URL() {
		return SPACES_URL;
	}

	public void setSPACES_URL(String sPACESURL) {
		SPACES_URL = sPACESURL;
	}

	public String getSPACES_LOCALONTO_DIR() {
		return SPACES_LOCALONTO_DIR;
	}

	public void setSPACES_LOCALONTO_DIR(String sPACESLOCALONTODIR) {
		SPACES_LOCALONTO_DIR = INSTALLATION_DIR + "/DesignTimeComposer_v2/src/main/resources/" + sPACESLOCALONTODIR;
	}
	
	public String getSPACE_ID() {
		return SPACE_ID;
	}

	public void setSPACE_ID(String sPACEID) {
		SPACE_ID = sPACEID;
	}

	public String getSpaces_mode() {
		return spaces_mode;
	}

	public void setSpaces_mode(String spacesMode) {
		spaces_mode = spacesMode;
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	@SuppressWarnings("unused")
	private void createDir(String dirUrl) {
		try {
			File d = new File (new URL(dirUrl).getFile());
			if (!d.exists()){
				d.mkdirs();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private String getWorkingDir(){
		return System.getProperty("user.dir");
	}
	
	public String getDSLR_DMA() {
		return this.DSLR_DMA;
	}
	
	public void setDSLR_DMA(String DSLR_DMA) {
		this.DSLR_DMA = DSLR_DMA;
	}
	
	public void setDSLR_DAA(String SLR_DAA) {
		this.DSLR_DAA = SLR_DAA;
	}
	
	public String getDSLR_DAA() {
		return DSLR_DAA;
	}
	
	public String getRESOURCES_DIR() {
		return getINSTALLATION_DIR() + "/DesignTimeComposer_v2/src/main/resources/";
	}

	public boolean isService() {
		return service;
	}

	public void setService(boolean service) {
		this.service = service;
	}

	public String getTemplate_storage_method() {
		return template_storage_method;
	}

	public void setTemplate_storage_method(String template_storage_method) {
		this.template_storage_method = template_storage_method;
	}

	public String getTemplate_repository() {
		return template_repository;
	}

	public void setTemplate_repository(String template_repository) {
		this.template_repository = template_repository;
	}

}
