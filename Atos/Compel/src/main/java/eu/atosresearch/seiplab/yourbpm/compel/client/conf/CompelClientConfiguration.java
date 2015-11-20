package eu.atosresearch.seiplab.yourbpm.compel.client.conf;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationModel;
import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationService;
import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationServiceAsync;

public class CompelClientConfiguration {
	private ConfigurationModel configuration;
	private static CompelClientConfiguration instance;
	
	public static ConfigurationModel getConfiguration(){
		if (instance == null)
			throw new RuntimeException("Compel configuration not loaded yet");
		return instance.configuration;
	}
	
	public static ConfigurationModel loadConfiguration(AsyncCallback<ConfigurationModel> callback){
		instance = new CompelClientConfiguration(callback);
		return instance.configuration;
	}

	private CompelClientConfiguration(AsyncCallback<ConfigurationModel> callback){
		//Load Compel configuration from server
		ConfigurationServiceAsync configurationService = (ConfigurationServiceAsync) GWT.create(ConfigurationService.class);
		configurationService.loadConfiguration(callback);
	}
	
	public static void setConfiguration (ConfigurationModel model){
		instance.configuration = model;
	}
	
}
