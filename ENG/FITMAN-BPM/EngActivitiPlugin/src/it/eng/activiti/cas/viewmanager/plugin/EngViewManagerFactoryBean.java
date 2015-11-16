package it.eng.activiti.cas.viewmanager.plugin;

import org.activiti.explorer.DefaultViewManager;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.ViewManagerFactoryBean;
import org.activiti.explorer.ui.alfresco.AlfrescoViewManager;

public class EngViewManagerFactoryBean extends ViewManagerFactoryBean{
		
	@Override	
	  public ViewManager getObject()
			    throws Exception
			  {
			    DefaultViewManager viewManagerImpl;
			    if (this.environment.equals("alfresco"))
			      viewManagerImpl = new AlfrescoViewManager();
			    else {
			      viewManagerImpl = new EngDefaultViewManager();
			    }
			    viewManagerImpl.setMainWindow(this.mainWindow);
			    return viewManagerImpl;
			  }
	
	
	
}
