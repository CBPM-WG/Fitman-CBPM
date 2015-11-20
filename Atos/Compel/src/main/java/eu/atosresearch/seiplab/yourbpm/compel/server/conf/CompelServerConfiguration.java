package eu.atosresearch.seiplab.yourbpm.compel.server.conf;


import eu.atosresearch.seiplab.yourbpm.compel.server.ConfigurationServiceImpl;
import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationModel;
import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationService;

public class CompelServerConfiguration {
	private ConfigurationModel configuration;
	private static CompelServerConfiguration instance;
	
	public static ConfigurationModel getConfiguration(){
		if (instance == null){ 
			instance = new CompelServerConfiguration();
			ConfigurationService service = new ConfigurationServiceImpl();
			CompelServerConfiguration.setConfiguration(service.loadConfiguration());
		}
		return instance.configuration;
	}
	
	private CompelServerConfiguration(){}
	
	public static void setConfiguration (ConfigurationModel model){
		instance.configuration = model;
	}
	
}
