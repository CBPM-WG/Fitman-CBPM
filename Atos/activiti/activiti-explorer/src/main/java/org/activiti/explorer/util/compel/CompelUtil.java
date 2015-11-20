package org.activiti.explorer.util.compel;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class CompelUtil {

	
	//COMPEL 20130313-JIY Variable to return to Compel context
	public static String COMPEL_REDIRECT = "";
	public static String BPMNSERVICE_ENDPOINT;
	

	public void init() {
		try{
	        if (COMPEL_REDIRECT == null || COMPEL_REDIRECT.isEmpty()) {
	        	 PropertiesConfiguration config = new PropertiesConfiguration("yourbpm.properties");
	        	 COMPEL_REDIRECT = config.getString("compel.root");
	        }
	        if (BPMNSERVICE_ENDPOINT == null || BPMNSERVICE_ENDPOINT.isEmpty()) {
	        	 PropertiesConfiguration config = new PropertiesConfiguration("yourbpm.properties");
	        	 BPMNSERVICE_ENDPOINT = config.getString("bpmnservice.endpoint");
	        }
	        
	         
	       } catch (ConfigurationException e1) {
	     			// TODO Auto-generated catch block
	     			e1.printStackTrace();
	       }   
	}
	
	
	
}
