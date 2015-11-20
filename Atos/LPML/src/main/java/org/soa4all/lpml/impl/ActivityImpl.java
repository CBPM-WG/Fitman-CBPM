/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.soa4all.lpml.Activity;
import org.soa4all.lpml.Parameter;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IConnector;


public class ActivityImpl extends ProcessElementImpl implements Activity, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean startElement;
	private boolean endElement;
	private boolean humanTask;
	private boolean synchronous;
	
	private List<IBinding> bindings = new ArrayList<IBinding>();
	
	private Set<Parameter> inputParameters = new HashSet<Parameter> ();	
	private Set<Parameter> outputParameters = new HashSet<Parameter> ();
	
	private Parameter loopParameter;
	private Set<IConnector> connectors = new HashSet<IConnector>();
	
	private float x = Float.NaN, y = Float.NaN, z = Float.NaN;
	
	public ActivityImpl() {
	}
	
	public ActivityImpl(String id){
		super(id);
	}


	public Set<Parameter> getInputParameters() {
		return inputParameters;
	}

	public Set<Parameter> getOutputParameters() {
		return outputParameters;
	}

	public boolean isStartElement() {
		return startElement;
	}

	public void setStartElement(boolean newStartElement) {
		startElement = newStartElement;
	}

	public boolean isEndElement() {
		return endElement;
	}

	public void setEndElement(boolean newEndElement) {
		endElement = newEndElement;
	}

	public boolean isHumanTask() {
		return humanTask;
	}

	public void setHumanTask(boolean newHumanTask) {
		humanTask = newHumanTask;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous(boolean newSynchronous) {
		synchronous = newSynchronous;
	}
	
	@Override
	public List<IBinding> getBindings() {
		return bindings;
	}


	@Override
	public Parameter getLoopParameter() {
		return this.loopParameter;
	}


	@Override
	public void setLoopParameter(IParameter p) {
		this.loopParameter = (Parameter) p;
	}


	@Override
	public Set<IConnector> getConnectors() {
		return connectors;
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
		ActivityImpl other = (ActivityImpl) obj;
		
		if (startElement != other.startElement) {
			return false;
		}
		
		if (endElement != other.endElement) {
			return false;
		}
		
		if (synchronous != other.synchronous) {
			return false;
		}

		if (humanTask != other.humanTask) {
			return false;
		}
		
		if (bindings == null) {
			if (other.bindings != null) {
				return false;
			}
		} else if (!bindings.equals(other.bindings)) {
			return false;
		}

		if (!inputParameters.equals(other.inputParameters)) {
			return false;
		}
		
		if (!outputParameters.equals(other.outputParameters)) {
			return false;
		}
		
		
		if (!connectors.equals(other.connectors)) {
			return false;
		}
		
		if (loopParameter == null) {
			if (other.loopParameter != null) {
				return false;
			}
		} else if (!loopParameter.equals(other.loopParameter)) {
			return false;
		}		
		

		return true;
	}
	
	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public void setX(float position) {
		this.x = position;
		
	}

	@Override
	public void setY(float position) {
		this.y = position;
		
	}

	@Override
	public void setZ(float position) {
		this.z = position;
		
	}

	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();
		result.append("Activity, name=").append(this.getName());
		result.append(", id=").append(this.getID());
		result.append(", startElement=").append(startElement);
		result.append(", endElement=").append(endElement);
		result.append(", humanTask=").append(humanTask);
		result.append(", synchronous=").append(synchronous);
		result.append(", bindings=").append(bindings);
		result.append(", inputs=").append(inputParameters);
		result.append(", outputs=").append(outputParameters);
		result.append(", connectors=").append(connectors);
		result.append(", loop parameter=").append(loopParameter);
		result.append(", annnotations=").append(this.getSemanticAnnotations());
		result.append(')');
		return result.toString();
	}


}
