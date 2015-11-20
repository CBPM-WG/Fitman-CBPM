/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;


import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;

import java.io.Serializable;



public class SemanticAnnotationImpl extends IdentifiableImpl implements ISemanticAnnotation, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private IAnnotationType type = IAnnotationType.META_DATA;
	private String referenceURI;
	private String expression;

	public SemanticAnnotationImpl() {
	}
	
	public SemanticAnnotationImpl(String id){
		super(id);
	}
	
	public String getReferenceURI() {
		return referenceURI;
	}

	public void setReferenceURI(String newReferenceURI) {
		referenceURI = newReferenceURI;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String newExpression) {
		expression = newExpression;
	}

	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();
		result.append("SA(");
		result.append(referenceURI);
		result.append(expression!=null?"=" + expression + ')':')');
		return result.toString();
	}

	public void setType(IAnnotationType type) {
		this.type = type;
	}

	public IAnnotationType getType() {
		return this.type;
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
		
		if (!(obj instanceof SemanticAnnotationImpl)) {
			return false;
		}
		
		SemanticAnnotationImpl other = (SemanticAnnotationImpl) obj;
		if (expression == null) {
			if (other.expression != null) {
				return false;
			}
		} else if (!expression.equals(other.expression)) {
			return false;
		}
		
		if (referenceURI == null) {
			if (other.referenceURI != null) {
				return false;
			}
		} else if (!referenceURI.equals(other.referenceURI)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
