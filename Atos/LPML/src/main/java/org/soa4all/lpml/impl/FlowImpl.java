/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;


import java.io.Serializable;

import org.soa4all.lpml.Flow;
import org.soa4all.lpml.ProcessElement;


import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;

public class FlowImpl extends ProcessElementImpl implements Flow,Serializable{

	private static final long serialVersionUID = 1L;

	private String condition;
	private ProcessElement source;
	private ProcessElement destination;
	
	// if not specified explicitly it is a default flow
	private int sequenceNumber = Integer.MAX_VALUE;
	

	
	public FlowImpl() {
	}
	
	public FlowImpl(String id){
		super(id);
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String newCondition) {
		condition = newCondition;
	}


	@Override
        public ProcessElement getDestination() {
		return this.destination;
	}

	@Override
        public ProcessElement getSource() {
		return this.source;
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
		FlowImpl other = (FlowImpl) obj;
		
		if (condition == null) {
			if (other.condition != null) {
				return false;
			}
		} else if (!condition.equals(other.condition)) {
			return false;
		}
		if (destination == null) {
			if (other.destination != null) {
				return false;
			}
		} else if (!destination.equals(other.destination)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		
		if(this.sequenceNumber != other.sequenceNumber){
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {

		StringBuffer result = new StringBuffer();
		result.append("Flow, name=").append(getName());
		result.append(" id=").append(getID());
		result.append(" condition=").append(condition);
		result.append(" sequenceNumber=").append(sequenceNumber);
		result.append(" src=").append(source);
		result.append(" dest=").append(destination);
		result.append(" annotations=").append(getSemanticAnnotations());
		return result.toString();
		
	}

	@Override
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	@Override
	public void setSequenceNumber(int number) {
		this.sequenceNumber = number;
		
	}
	@Override
	public void setSource(IProcessElement source) {
		this.source = (ProcessElement)source;
	}
	@Override
	public void setDestination(IProcessElement destination) {
		this.destination = (ProcessElement) destination;
	}
}
