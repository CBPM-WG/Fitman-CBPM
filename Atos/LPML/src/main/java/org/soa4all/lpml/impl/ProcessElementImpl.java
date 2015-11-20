/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.soa4all.lpml.ProcessElement;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;




public abstract class ProcessElementImpl extends IdentifiableImpl implements ProcessElement,Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Set<ISemanticAnnotation> semanticAnnotations = new HashSet<ISemanticAnnotation>();
	
	/**
	 * This constructor uses an UUID generator in order to ensure 
	 * unique process element IDs.
	 * 
	 * NOTE: UUIDs have been known to fail if two distinct java processes
	 * running on the same machine (i.e. with the same MAC address) try to 
	 * generate UUID within a very short period of time. Then the UUIDs might be
	 * the same.
	 * 
	 */
	public ProcessElementImpl(){
		super();
	}
	
	/**
	 * Creates an new ProcessElementImpl with the given ID.
	 * Using this methods it is the responsibility of the caller
	 * to ensure that the IDs of all process elements are unique
	 * within the given process.
	 * 
	 * @param id a non null ID
	 * @throws IllegalArgumentException when null ID is provided
	 */
	public ProcessElementImpl(String id){
		super(id);
	}
	
	public Set<ISemanticAnnotation> getSemanticAnnotations() {
		return semanticAnnotations;
	}

	
	@Override
	public int hashCode() {
		
		/*
		 * IMPORTANT!!!
		 * hash codes must be based only on immutable properties of the
		 * object. Otherwise, if the properties are changed after the object
		 * is added to a hash based structure the latter will behave incorrectly
		 * until rehashed.
		 */
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
		ProcessElementImpl other = (ProcessElementImpl) obj;
		
		if(!semanticAnnotations.equals( other.semanticAnnotations)){
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
		
		StringBuffer result = new StringBuffer(getClass().toString());
		result.append(this.getClass().getName());
		result.append(" superclass:" );
		result.append(super.toString());
		result.append("name=").append(name);
		result.append("annotations=").append(semanticAnnotations);
		return result.toString();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String value) {
		this.name = value;
	}
} 
