package it.eng.activiti.cas.viewmanager.plugin;

import org.activiti.explorer.DefaultViewManager;
import org.activiti.explorer.ui.AbstractTablePage;

public class EngDefaultViewManager extends DefaultViewManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void switchView(AbstractTablePage page, String mainMenuActive, String subMenuActive) {
//		this.currentPage= page; //FIXME
		this.mainWindow.setMainNavigation(mainMenuActive);
		this.mainWindow.switchView(page);
		if ((subMenuActive != null) && (page.getToolBar() != null))
			page.getToolBar().setActiveEntry(subMenuActive);
	}	
}