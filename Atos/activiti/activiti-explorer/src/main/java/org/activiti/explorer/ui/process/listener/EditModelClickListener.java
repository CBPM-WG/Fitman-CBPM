package org.activiti.explorer.ui.process.listener;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.NotificationManager;
import org.activiti.explorer.util.compel.CompelUtil;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


/**
 * @author Tijs Rademakers
 */
public class EditModelClickListener implements ClickListener {

  private static final long serialVersionUID = 1L;
  
  protected NotificationManager notificationManager;
  protected String modelId;
  
  public EditModelClickListener(String modelId) {
    this.notificationManager = ExplorerApp.get().getNotificationManager(); 
    
    this.modelId = modelId;
  }

  public void buttonClick(ClickEvent event) {
	  //COMPEL: Change the URL to introduce it by a property file
	  ExplorerApp.get().getMainWindow().open(new ExternalResource(CompelUtil.COMPEL_REDIRECT+"?id=" + modelId));
	  
//    ExplorerApp.get().getMainWindow().open(new ExternalResource(
//        ExplorerApp.get().getURL().toString() + "service/editor?id=" + modelId));
  }
}
