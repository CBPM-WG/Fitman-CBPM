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

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IConnector;

public class ConnectorImpl extends IdentifiableImpl implements IConnector,Serializable {

	private static final long serialVersionUID = 1L;

	private IParameter outputParameter;
	private String semanticMapping;
	private Set<IParameter> inputParams = new HashSet<IParameter>();

	
	public ConnectorImpl() {
		super();
	}
	
	public ConnectorImpl(String id){
		super(id);
	}


	public Set<IParameter> getInputParameters() {
		return (Set<IParameter>) inputParams;
	}

	@Override
	public IParameter getOutputParameter() {
		return outputParameter;
	}
	
	@Override
	public void setOutputParameter(IParameter p) {
		this.outputParameter = (Parameter) p;
	}

	@Override
	public String getSemanticMapping() {
		return semanticMapping;
	}

	@Override
	public void setSemanticMapping(String value) {
		this.semanticMapping = value;
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ConnectorImpl other = (ConnectorImpl) obj;
		
		if (!inputParams.equals(other.inputParams)) {
			return false;
		}
		
		if (outputParameter == null) {
			if (other.outputParameter != null) {
				return false;
			}
		} else if (!outputParameter.equals(other.outputParameter)) {
			return false;
		}
		
		if (semanticMapping == null) {
			if (other.semanticMapping != null) {
				return false;
			}
		} else if (!semanticMapping.equals(other.semanticMapping)) {
			return false;
		}
		
		return true;
	}

	
	@Override
	public String toString() {

		StringBuffer result = new StringBuffer(super.toString());
		
		result.append(this.getClass().getName()).append(super.toString());
		result.append(", uRISemanticMapping=").append(semanticMapping);
		result.append(", Output parameter=").append(outputParameter);
		result.append(", uRI_ListInputParameters=").append(inputParams);
		result.append(')');
		
		return result.toString();
	}


}
