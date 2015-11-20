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

import org.soa4all.lpml.Parameter;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;




public class ParameterImpl extends IdentifiableImpl implements Parameter,Serializable{

	private static final long serialVersionUID = 1L;

	private Set<ISemanticAnnotation> semanticAnnotations = new HashSet<ISemanticAnnotation>();
	private String name;

	private String value;

	public ParameterImpl() {
		super();
	}

	public ParameterImpl(String id) {
		super(id);
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
		
		if (!super.equals(obj)) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		ParameterImpl other = (ParameterImpl) obj;
		
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		
		if(!semanticAnnotations.equals(other.semanticAnnotations)){
			return false;
		}
		
		return true;
	}


	@Override
	public Set<ISemanticAnnotation> getSemanticAnnotations() {
		return this.semanticAnnotations;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
}
