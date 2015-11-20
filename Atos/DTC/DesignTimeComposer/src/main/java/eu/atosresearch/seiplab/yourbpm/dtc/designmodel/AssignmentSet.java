/**
 * 
 * Copyright (C) 2006-2010 ATOS ORIGIN
 * http://www.atosorigin.com
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the license LGPL.
 *
 * Authors :
 * Jesus Gorronogoitia. Atos Research and Innovation, Atos Origin SAE
 * @email jesus.gorronogoitia@atosresearch.eu
 * Mateusz Radzimski. Atos Research and Innovation, Atos Origin SAE
 * @email mateusz.radzimski@atosresearch.eu
 */
package eu.atosresearch.seiplab.yourbpm.dtc.designmodel;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;

public class AssignmentSet implements Cloneable {
	private Collection<ISemanticAnnotation> assignments;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);

	public Collection<ISemanticAnnotation> getAssignments() {
		return assignments;
	}

	public void setAssignments(Collection<ISemanticAnnotation> assignments) {
		this.assignments = assignments;
	}

	public void addAssignments(Collection<ISemanticAnnotation> newAssumptions) {
		this.assignments.addAll(newAssumptions);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		AssignmentSet newAssignmentSet = new AssignmentSet();
		Collection<ISemanticAnnotation> newAssignments = new ArrayList<ISemanticAnnotation>();
		for (ISemanticAnnotation assignment : this.assignments) {
			try {
				newAssignments.add(modelProxy.cloneLPMLObject(assignment));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		newAssignmentSet.setAssignments(newAssignments);
		return newAssignmentSet;
	}

	public Collection<ISemanticAnnotation> getPreferences(){
		Collection<ISemanticAnnotation> preferences = new ArrayList<ISemanticAnnotation>();
		for (ISemanticAnnotation a: assignments){
			if (a.getType() == IAnnotationType.PREFERENCE) {
				preferences.add(a);
			}
		}
		return preferences;
	}
	
	public Collection<ISemanticAnnotation> getRequirements(){
		Collection<ISemanticAnnotation> requirements = new ArrayList<ISemanticAnnotation>();
		for (ISemanticAnnotation a: assignments){
			if (a.getType() == IAnnotationType.REQUIREMENT) {
				requirements.add(a);
			}
		}
		return requirements;
	}

}
