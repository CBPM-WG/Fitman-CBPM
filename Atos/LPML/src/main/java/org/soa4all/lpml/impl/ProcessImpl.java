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

import org.soa4all.lpml.Activity;
import org.soa4all.lpml.Flow;
import org.soa4all.lpml.Process;
import org.soa4all.lpml.ProcessElement;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;




public class ProcessImpl extends IdentifiableImpl implements Process, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Set<Flow> flows = new HashSet<Flow>();
	private Set<IProcessElement> elements = new HashSet<IProcessElement>();
	private Set<ISemanticAnnotation> annotations = new HashSet<ISemanticAnnotation>();

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
	public ProcessImpl(){
		super();
	}
	
	/**
	 * Creates an new ProcessImpl with the given ID.
	 * Using this methods it is the responsibility of the caller
	 * to ensure that the IDs of all process elements are unique
	 * within the given process.
	 * 
	 * @param id a non null ID
	 * @throws IllegalArgumentException when null ID is provided
	 */
	public ProcessImpl(String id){
		super(id);
	}

	@Override
	public Set<Flow> getFlows() {
		return flows;
	}

	

	@Override
	public Set<IProcessElement> getProcessElements() {
		return elements;
	}

	@Override
	public Set<ISemanticAnnotation> getSemanticAnnotations() {
		return annotations;
	}
	
	
	/**
	 * Since the equals() method is overriden, we must also override the hashCode() method.
	 */
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
		ProcessImpl other = (ProcessImpl) obj;
		
		if(!flows.equals(other.flows)){
			return false;
		}
		
		if(!elements.equals(other.elements)){
			return false;
		}
		
		if(!annotations.equals(other.annotations)){
			return false;
		}
		
		return true;
	}


	@Override
	public String toString() {
		return "ProcessImpl [" + super.toString() +
			   ", elements=" + elements +
			   ", flows=" + flows +
			   ", annotations=" + annotations + "]";
	}

	@Override
	public Activity getStartElement() {
		return getElement(true);
	}

	@Override
	public Activity getEndElement() {
		
		return getElement(false);
	}

	private Activity getElement(boolean isStartElement) {

		for(IProcessElement element:elements){
			if(element instanceof IActivity){
				Activity a = (Activity)element;
				if(isStartElement && a.isStartElement()){
						return a;
				}
				if(!isStartElement && a.isEndElement()){
					return a;
				}
			}
		}
		return null;
	}
}
