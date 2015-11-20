package eu.atosresearch.seiplab.yourbpm.compel.server;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationModel;
import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationService;

public class ConfigurationServiceImpl extends RemoteServiceServlet implements ConfigurationService {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
	
	
	public ConfigurationModel loadConfiguration(){ 
		logger.debug("Loading Compel configuration");
		
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("/compel.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		//Populating model
		ConfigurationModel model = new ConfigurationModel();
		
		for (Object key: properties.keySet()){
			model.set ((String) key, (String) properties.get(key));
		}
		
		return model;
    }
}
