package eu.atosresearch.seiplab.yourbpm.compel.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Service implements IsSerializable{

	String URI;
	String WSDLURL;
	String operation;
	boolean bound;
	boolean process = false;
	
	public void setURI(String URI) {
		this.URI = URI;
	}

	public void setWSDLURL(String WSDLURL) {
		this.WSDLURL = WSDLURL;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public void setBound(boolean bound) {
		this.bound = bound;
	}
	
	public String getURI() {
		return this.URI;
	}

	public String getWSDLURL() {
		return this.WSDLURL;
	}

	public String getOperation() {
		return this.operation;
	}
	
	public boolean isBound(){
		return this.bound;
	}
	
	public boolean isProcess(){
		return this.process;
	}

}
