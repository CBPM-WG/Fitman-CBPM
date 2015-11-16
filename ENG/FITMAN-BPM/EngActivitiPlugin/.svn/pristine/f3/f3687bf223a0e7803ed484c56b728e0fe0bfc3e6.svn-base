package it.eng.activiti.custommanagementmenu.plugin;

import org.activiti.explorer.ComponentFactories;
import org.activiti.explorer.ui.mainlayout.MainMenuBarFactory;
import org.activiti.explorer.ui.management.ManagementMenuBarFactory;

public class CustomComponentFactories extends ComponentFactories {

	private static final long serialVersionUID = -5282262436477028317L;

	public CustomComponentFactories() {
		factories.put(MainMenuBarFactory.class, new MainMenuBarFactory());
		if (factories.get(ManagementMenuBarFactory.class) != null) {
			factories.remove(ManagementMenuBarFactory.class);
		}
		factories.put(ManagementMenuBarFactory.class, new CustomManagementMenuBarFactory());
	}

}
