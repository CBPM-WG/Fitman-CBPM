package it.eng.activiti.cas.plugin;

import java.util.ResourceBundle;

import org.jasig.cas.client.validation.Cas10TicketValidationFilter;

public class TicketValidatorExtended extends Cas10TicketValidationFilter {
	private static ResourceBundle finder = ResourceBundle.getBundle("cas-plugin");

	static {
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
			
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				System.out.println("Host value: "+finder.getString("SSOHostname")+ "Host name: "+hostname);
				if (hostname.equals("localhost") || hostname.equals(finder.getString("SSOHostname"))) {
					return true;
				}
				return false;
			}
		});
	}

}
