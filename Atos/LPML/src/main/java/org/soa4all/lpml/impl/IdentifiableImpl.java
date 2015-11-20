package org.soa4all.lpml.impl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IIdentifiable;

import java.io.Serializable;
import java.util.UUID;



public abstract class IdentifiableImpl implements IIdentifiable,Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	public IdentifiableImpl(){
		this.id = UUID.randomUUID().toString();
	}
	
	public IdentifiableImpl(String id){
		if(id == null){
			throw new IllegalArgumentException("id cannot be null");
		}
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if ( !(obj instanceof IIdentifiable) ) {
			return false;
		}
		
		IIdentifiable other = (IIdentifiable) obj;
		return id.equals(other.getID());
		
	}
	
	@Override
	public String toString() {
		
		StringBuffer result = new StringBuffer(getClass().toString());
		result.append(this.getClass().getName());
		result.append(" id=").append(id);
		return result.toString();
	}
	
}
