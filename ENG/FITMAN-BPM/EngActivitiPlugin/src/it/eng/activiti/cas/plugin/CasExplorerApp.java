package it.eng.activiti.cas.plugin;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.identity.LoggedInUser;

public class CasExplorerApp extends ExplorerApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6163593909290706243L;
	private boolean isFirstAccess = true;
	private int counter;
	private transient IdentityService identityService;
	private LoggedInUser user = null;
	private static ResourceBundle finder = ResourceBundle.getBundle("cas-plugin");

	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		Locale.setDefault(new Locale("en", "GB"));
		current.set(this);
		String name = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
		if (name != null) {
			// List<User> users =
			// identityService.createUserQuery().userEmail(name.toLowerCase()).list();
			// User currentUser = null;
			// if (users != null && users.size() > 0) {
			// if (name.toLowerCase().equalsIgnoreCase("admin@msee.eu")) {
			// for (User user : users) {
			// if (user.getId().equalsIgnoreCase("admin@msee.eu")) {
			// currentUser = user;
			// break;
			// }
			// }
			// }
			//
			// if (currentUser == null) {
			// currentUser = users.get(0);
			// }
			// }
			User currentUser = identityService.createUserQuery().userId(name.toLowerCase()).singleResult();
			if (currentUser != null) {
				user = loginHandler.authenticate(currentUser.getId(), finder.getString("activiti.password"));
				setUser(user);
			}
		}
		if (user != null) {
			Authentication.setAuthenticatedUserId(user.getId());
			if (isFirstAccess) {
				viewManager.showDefaultPage();
				counter++;
				if (counter == 4) {
					isFirstAccess = false;
				}
			}
		}

	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

}
