/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;


import java.io.Serializable;
import java.net.URI;
import java.net.URL;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;



public class BindingImpl  extends IdentifiableImpl implements IBinding,Serializable {

	private static final long serialVersionUID = 1L;
	private URL serviceReference;
	private URI operation;
	private String name;

	public BindingImpl(){
	}
	
	public BindingImpl(String id){
		super(id);
	}
	
	@Override
	public URL getServiceReference() {
		return serviceReference;
	}


	@Override
	public void setServiceReference(URL value) {
		this.serviceReference = value;
	}	
	

	@Override
	public URI getOperation() {
		return operation;
	}

	@Override
	public void setOperation(URI operation) {
		this.operation = operation;
	}


	
	@Override
	public int hashCode() {
		return getID().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}
		
		if(!super.equals(obj)){
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		BindingImpl other = (BindingImpl) obj;
		
		if (serviceReference == null) {
			if (other.serviceReference != null) {
				return false;
			}
		} else {
			if(other.serviceReference == null){
				return false;
			}
			if (!serviceReference.toString().equals(other.serviceReference.toString())) {
				return false;	
			}
		}		
		
		if (operation == null) {
			if (other.operation != null) {
				return false;
			}
		} else if (!operation.equals(other.operation)) {
			return false;
		}
		
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}

		return true;
	}
	
	@Override
	public String toString() {
		
		StringBuffer result = new StringBuffer(super.toString());
		
		result.append(this.getClass().getName());
		result.append(", name=").append(name);
		result.append(", serviceReference=").append(serviceReference);
		result.append(", operation=").append(operation);
		
		return result.toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	
}
