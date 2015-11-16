package it.eng.activiti.custommanagementmenu.plugin;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
import org.activiti.explorer.ui.management.ManagementMenuBar;

public class CustomManagementMenuBar extends ManagementMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3694540407051932015L;

	private static final String ADMIN_USER_ID = "admin@msee.eu";

//	@Override
//	protected void addDatabaseToolbarEntry() {
//		if (ADMIN_USER_ID.equals(ExplorerApp.get().getLoggedInUser().getId())) {
//			addToolbarEntry(ENTRY_DATABASE, i18nManager.getMessage(Messages.MGMT_MENU_DATABASE), new ToolbarCommand() {
//				public void toolBarItemSelected() {
//					viewManager.showDatabasePage();
//				}
//			});
//		}
//	}

	@Override
	protected void addUsersToolbarEntry() {
		if (ADMIN_USER_ID.equals(ExplorerApp.get().getLoggedInUser().getId())) {
			addToolbarEntry(ENTRY_USERS, i18nManager.getMessage(Messages.MGMT_MENU_USERS), new ToolbarCommand() {
				private static final long serialVersionUID = -1741992169075780586L;

				public void toolBarItemSelected() {
					viewManager.showUserPage();
				}
			});
		}
	}

	@Override
	protected void addGroupToolbarEntry() {
		if (ADMIN_USER_ID.equals(ExplorerApp.get().getLoggedInUser().getId())) {
			addToolbarEntry(ENTRY_GROUPS, i18nManager.getMessage(Messages.MGMT_MENU_GROUPS), new ToolbarCommand() {
				public void toolBarItemSelected() {
					viewManager.showGroupPage();
				}
			});
		}
	}

}
