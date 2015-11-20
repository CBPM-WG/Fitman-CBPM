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
package eu.atosresearch.seiplab.yourbpm.dtc.model.lpml;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soa4all.lpml.Activity;

import org.soa4all.lpml.Flow;
import org.soa4all.lpml.Gateway;
import org.soa4all.lpml.Parameter;
import org.soa4all.lpml.Process;
import org.soa4all.lpml.impl.ActivityImpl;
import org.soa4all.lpml.impl.FlowImpl;
import org.soa4all.lpml.impl.ParameterImpl;
import org.soa4all.lpml.impl.ProcessImpl;
import org.soa4all.lpml.ProcessElement;

import com.thoughtworks.xstream.XStream;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IFlow;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IGateway;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.rdf.LPMLXMLMapper;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelIOUtils;

import java.util.*;


public class LPMLProxy implements ModelProxy{

//  YOSU: Commented it out: we only used requirements (former constrains) and preferences (former requirements) 
//	public static Collection<ISemanticAnnotation> getRequirements(Process process){
//		
//		
//		
//		Collection<ISemanticAnnotation> requirements = new ArrayList<ISemanticAnnotation>();
//		for (ISemanticAnnotation a: process.getSemanticAnnotations()){
//			if (a.getType() == IAnnotationType.REQUIREMENT){
//				requirements.add(a);
//			}
//		}
//		return requirements;
//	}
	
	public Collection<ISemanticAnnotation> getRequirements(IProcess process){
		Collection<ISemanticAnnotation> requirements = new ArrayList<ISemanticAnnotation>();
		for (ISemanticAnnotation a: process.getSemanticAnnotations()){
			if (a.getType() == IAnnotationType.REQUIREMENT){
				requirements.add(a);
			}
		}
		return requirements;
	}
	
    @Override
	public Collection<ISemanticAnnotation> getPreferences(IProcess process){
		Collection<ISemanticAnnotation> requirements = new ArrayList<ISemanticAnnotation>();
		for (ISemanticAnnotation a: process.getSemanticAnnotations()){
			if (a.getType() == IAnnotationType.PREFERENCE){
				requirements.add(a);
			}
		}
		return requirements;
	}	
	
	
	/**
	 * Substitute goal with *template*
	 * 
	 * @param process
	 * @param activity
	 * @param template
	 */
    @Override
	public void substitute(IProcess process, IActivity activity, IProcess template) {
		//Checking if goal exist in this.process
		if (!process.getProcessElements().contains(activity)){
			return;
		}
		
		Activity GoalActivity = null;
		
		try {
			GoalActivity = (Activity)getFlowObjectByIdentifier(process, new URI(activity.getID()));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		Process processTemplate = null;
		try {
			processTemplate = (Process) cloneProcess(template);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//debug
//		try {
//			ModelIOUtils.saveProcessForDebug(processTemplate);
//			ModelIOUtils.saveProcessForDebug(process);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//take first and last process elements from the template
		//Here we mean all ProcessElement that are not Flows.
		Collection<IProcessElement> firstTemplateElements =  getFirstActivities(processTemplate);
		Collection<IProcessElement> lastTemplateElements = getLastActivities(processTemplate);
		
		//
		//this is to fix the bug, when some activities (Start and End elements) are still attached to the flow
//		for (ProcessElement pe : firstTemplateElements) {
//			Flow toRemove = null;
//			for (Flow f : pe.getFlows()) {
//				if (f.getSource() instanceof Activity) {
//					if (((Activity)f.getSource()).isStartElement() ) {
//						toRemove = f;
//					}
//				}
//			}
//			pe.getFlows().remove(toRemove);
//		}
//
//		for (ProcessElement pe : lastTemplateElements) {
//			Flow toRemove = null;
//			for (Flow f : pe.getFlows()) {
//				if (f.getDestination() instanceof Activity) {
//					if (((Activity)f.getDestination()).isEndElement() ) {
//						toRemove = f;
//					}
//				}
//			}
//
//			pe.getFlows().remove(toRemove);
//		}
		
		//we make a list of flows NOT to attach to the process
		Collection<IFlow> startFlows = getOutgoingSequenceFlowObjects(processTemplate, getStartElement(processTemplate));
		Collection<IFlow> endFlows = getIncommingSequenceFlowObjects(processTemplate, getEndElement(processTemplate));
		
		
		// We add the new elements from template to the process. 
		// Start/End elements and the first/last SequenceFlowObjects of the template must not be added.
		for (IProcessElement pe : processTemplate.getProcessElements()) {
			if (pe instanceof IActivity) {
				IActivity a = (IActivity) pe;
				if (!(a.isStartElement() || a.isEndElement())) {
                                        
                                
                                        Set<IActivity> s = (Set<IActivity>) process.getProcessElements();
                                        s.add(a);
                                        
                                        //FIXME
                                        //process.getProcessElements().add((Activity) a);
                                       // process.getProcessElements().add(a);
//					a.setProcess(process);
				}
				
				//now remove any referenced flows that shouldn't be in the process
				Iterator<IFlow> fi = getFlows (processTemplate, a);
				
				while (fi.hasNext()) {
					IFlow activityFlow = fi.next();
					
					for (IFlow f : startFlows ) {
						if (activityFlow.getID().compareTo(f.getID()) == 0) {
							//a.getFlows().remove(activityFlow);
							fi.remove();
						}
					}
					for (IFlow f : endFlows ) {
						if (activityFlow.getID().compareTo(f.getID()) == 0) {
							//a.getFlows().remove(activityFlow);
							fi.remove();
						}
					}
				}
			}
			
			if (pe instanceof IGateway) {
		
				
				IGateway g = (Gateway) pe;
                                Set<IGateway> s = (Set<IGateway>) process.getProcessElements();
                                s.add(g);
                                //FIXME
				//process.getProcessElements().add(g);
//				g.setProcess(process);
			}
		}
		
		for (Flow f : processTemplate.getFlows()){
				ProcessElement src = ((ProcessElement)f.getSource());
				ProcessElement dst = ((ProcessElement)f.getDestination());
				
				if (!(startFlows.contains(f) || endFlows.contains(f))) {
				    Set<IFlow> s = (Set<IFlow>) process.getFlows();
                                    s.add(f);
                                //FIXME
				//process.getFlows().add(f);
				}
		}
		
		// First we have to get all the links towards the goal to point to the
		// first elements of the process
		// Note that every process should start with the "Start" element and
		// ends with the "End" element.
		
		//take the flow element that flows towards the goal, and point it to the first template element 
		Collection<Flow> flowTowardsGoal = (Collection<Flow>) new FlowImpl(); 
                        //getIncommingSequenceFlowObjects(process, activity);
		for (Flow flowToGoal : flowTowardsGoal) {
			for (IProcessElement nonSequenceFlowObject : firstTemplateElements) {
				IProcessElement pe = getProcessElementByID(process, nonSequenceFlowObject.getID());	
				flowToGoal.setDestination(pe);
				Set<IFlow> s = (Set<IFlow>) process.getFlows();
                                s.add(flowToGoal);
                                //FIXME
				//process.getFlows().add(flowToGoal);
			}
		}
		
		//take the flow element that flows outwards the goal, and change it's source to the last template element
		//FIXED BUG (Yosu): we have to take all flows going to template end element (but once in the process) 
		//and replace their target with the same target of the goal outwards flow target
		//CASES SUPPORTED:
		//A-activity has only one outward flow and template has one or more end flows (those going to the end activity)
		//B-activity has more than one outward flow and the template has only one end flow.
		Collection<IFlow> flowOutwardsGoal = getOutgoingSequenceFlowObjects(process, activity);
		Collection<IProcessElement> activitiesOutwardsGoal = getOutgoingActivities(process, activity);
		
		//Detect default flow. It could be from the template (more than one endFlows) or from the activity
		ProcessElement defaultFlowTarget = null;
		boolean defaultFlowSource = false;
		if (flowOutwardsGoal.size() == 1){
			defaultFlowTarget = detectDefaultFlowSource (endFlows);
			defaultFlowSource = true;
		} else {
			defaultFlowTarget = detectDefaultFlowDestination (flowOutwardsGoal);
		}
		
		//Removing flowOutwardsGoal;
		process.getFlows().removeAll(flowOutwardsGoal);
		
		//Creating new flows
		for (IProcessElement lastTemplateElement : lastTemplateElements) {
			IProcessElement pe = getProcessElementByID(process, lastTemplateElement.getID());
			for (IProcessElement activityOutwardsGoal: activitiesOutwardsGoal){
				Flow f = (Flow) new FlowImpl();
				f.setSource(lastTemplateElement);
				f.setDestination(activityOutwardsGoal);
				if (defaultFlowSource && defaultFlowTarget != null && lastTemplateElement.getID().equals(defaultFlowTarget.getID())){
					f.setCondition(null);
					f.setSequenceNumber(0);
				} else if (!defaultFlowSource && defaultFlowTarget != null && activityOutwardsGoal.getID().equals(defaultFlowTarget.getID())){
					f.setCondition(null);
					f.setSequenceNumber(0);
				}
				
                                Set<IFlow> s = (Set<IFlow>) process.getFlows();
                                s.add(f);
                                //FIXME
				//process.getFlows().add(f);
			}
//			for (Flow flowFromGoal : flowOutwardsGoal) {
//				flowFromGoal.setSource(pe);
//				process.getFlows().add(flowFromGoal);
//			}
		}
		
		//TODO: Set last default flow.
		
		// And finally, the goal is removed
		process.getProcessElements().remove(GoalActivity);
		
		//we still have some elements associated with the flows that are orphaned. we need to remove it.
		//removeOrphans(process);
	}
	
	private ProcessElement detectDefaultFlowSource(
			Collection<IFlow> flowOutwardsGoal) {
		ProcessElement result = null;
		for (IFlow f: flowOutwardsGoal){
			if (f.getSequenceNumber() == 0){
				result = (ProcessElement) f.getSource();
			}
		}
		return result;
	}
	
	private ProcessElement detectDefaultFlowDestination(
			Collection<IFlow> flowOutwardsGoal) {
		ProcessElement result = null;
		for (IFlow f: flowOutwardsGoal){
			if (f.getSequenceNumber() == 0){
				result = (ProcessElement) f.getDestination();
			}
		}
		return result;
	}

    @Override
	public Iterator<IFlow> getFlows(IProcess processTemplate, IProcessElement a) {
		Collection<IFlow> activityFlows = new ArrayList<IFlow>();
		Collection<IFlow> flows = (Collection<IFlow>) processTemplate.getFlows();
		for (IFlow f:flows){
			if (f.getSource().equals(a) || f.getDestination().equals(a)){
				activityFlows.add(f);
			}
		}
		return activityFlows.iterator();
	}

	private void removeOrphans(Process process) {
		//remove "Start" and "End" elements belonging to the Template. Also remove associated Flows.
		
		IActivity start = getStartElement(process);
		IActivity end = getEndElement(process);
		
		Flow toRemove = null;
		
		//Find flows from Start -> Activity
		for (IProcessElement pe : process.getProcessElements()) {
			if (pe instanceof Flow) {
				Flow f = (Flow) pe;
				if (f.getSource() instanceof Activity) {
					Activity a = (Activity) f.getSource();
					
					if (a.isStartElement() == true && a.getID() != start.getID()) {
						f.setSource(null);
						toRemove = f;
					}
				}
			}
		}
		process.getProcessElements().remove(toRemove);
		
		//Find flows from Activity -> End
		for (IProcessElement pe : process.getProcessElements()) {
			if (pe instanceof Flow) {
				Flow f = (Flow) pe;
				if (f.getDestination() instanceof Activity) {
					Activity a = (Activity) f.getDestination();
					
					if (a.isEndElement() == true && a.getID() != end.getID()) {
						f.setDestination(null);
						toRemove = f;
					}
				}
			}
		}
		process.getProcessElements().remove(toRemove);

		
//		for (ProcessElement pe : process.getProcessElements()) {
//			Flow toRemove = null;
//			for (Flow f : pe.getFlows()) {
//				if (f.getDestination() instanceof Activity) {
//					Activity a = (Activity) f.getDestination();
//					if (a.isEndElement()) {
//						if (a.getID() != end.getID()) {
//							f.setDestination(null);
//							toRemove = f;
//						}
//						
//					}
//				}
//			}
//			
//		}
	}

	public IProcessElement getProcessElementByID(IProcess process, String ID) {
		Collection<IProcessElement> allPE = (Collection<IProcessElement>) process.getProcessElements();
		
		Iterator<IProcessElement> ipe = allPE.iterator();
		
		IProcessElement result = null;
		while (ipe.hasNext()) {
			IProcessElement pe = ipe.next();
			if (pe.getID().compareTo(ID) == 0) {
				result = pe;
				return result;
			}
		}
		
		return null;
	}
	
    @Override
	public IActivity getActivityByName(IProcess process, String name) {
		Collection<IProcessElement> allPE = (Collection<IProcessElement>) process.getProcessElements();
		
		Iterator<IProcessElement> ipe = allPE.iterator();
		
		Activity result = null;
		while (ipe.hasNext()) {
			IProcessElement pe = ipe.next();
			if (pe instanceof Activity)
			if (((Activity)pe).getName().equals(name)) {
				result = (Activity)pe;
				return result;
			}
			
		}
		
		return null;
	}
	
    @Override
	public IActivity getActivityById(IProcess process, URI id) {
		Collection<IProcessElement> allPE = (Collection<IProcessElement>) process.getProcessElements();
		
		Iterator<IProcessElement> ipe = allPE.iterator();
		
		Activity result = null;
		while (ipe.hasNext()) {
			IProcessElement pe = ipe.next();
			if (pe instanceof Activity)
			if (((Activity)pe).getID().equals(id.toString())) {
				result = (Activity)pe;
				return result;
			}
		}
		return null;
	}
	
    @Override
	public Collection<IProcessElement> getFirstActivities(IProcess process) {
		Collection<IFlow> flows = new LinkedList<IFlow>();
		Collection<IProcessElement> elem = new LinkedList<IProcessElement>();
		
		Activity startElement = (Activity) getStartElement(process);
		flows = getOutgoingSequenceFlowObjects(process, startElement);
		
		for (IFlow f: flows) {
			elem.add((ProcessElement)f.getDestination());
		}
		//LinkedList<Activity> ll = new LinkedList<Activity>();
		//ll.add(process.getStartElement());
		
		return elem;
	}

    @Override
	public Collection<IProcessElement> getLastActivities(IProcess process) {
		Collection<IFlow> flows = new LinkedList<IFlow>();
		Collection<IProcessElement> elem = new LinkedList<IProcessElement>();
		
		Activity endElement = (Activity) getEndElement(process);
		flows = getIncommingSequenceFlowObjects(process, endElement);
	
		
		for (IFlow f: flows) {
			elem.add((ProcessElement)f.getSource());
		}
		//LinkedList<Activity> ll = new LinkedList<Activity>();
		//ll.add(process.getStartElement());
		
		return elem;
	}

	public IActivity getStartElement(IProcess process) {
		Activity startElement = null;
		for (IProcessElement pe: process.getProcessElements()){
			if (pe instanceof Activity){
				if (((Activity)pe).isStartElement()){
					startElement = (Activity)pe;
					break;
				}
			}
		}
		return startElement;
	}
	
    @Override
	public IActivity getEndElement(IProcess process) {
		Activity endElement = null;
		for (IProcessElement pe: process.getProcessElements()){
			if (pe instanceof Activity){
				if (((Activity)pe).isEndElement()){
					endElement = (Activity)pe;
					break;
				}
			}
		}
		return endElement;
	}

    @Override
	public Collection<IFlow> getIncommingSequenceFlowObjects(IProcess process, IActivity activity) {
		ArrayList<IFlow> incommingSequenceFlowObjects = new ArrayList<IFlow>();
		for (IFlow processFlowObject : process.getFlows()) {

			if (processFlowObject.getDestination().equals(activity)) {
				incommingSequenceFlowObjects.add(processFlowObject);
			}
		}
		return incommingSequenceFlowObjects;
	}

    @Override
	public Collection<IFlow> getOutgoingSequenceFlowObjects(IProcess process, IActivity activity) {
		ArrayList<IFlow> incommingSequenceFlowObjects = new ArrayList<IFlow>();
		
		for (IFlow processFlowObject : process.getFlows()) {

			if (processFlowObject.getSource().equals(activity)) {
				incommingSequenceFlowObjects.add(processFlowObject);
			}
		}
		return incommingSequenceFlowObjects;
	}
	
    @Override
	public Collection<IProcessElement> getOutgoingActivities(IProcess process, IActivity activity) {
		ArrayList<IProcessElement> activities = new ArrayList<IProcessElement>();
		
		for (IFlow processFlowObject : process.getFlows()) {
			if (processFlowObject.getSource().equals(activity)) {
				activities.add((Activity)processFlowObject.getDestination());
			}
		}
		return activities;
	}

    @Override
	public IProcessElement getFlowObjectByIdentifier(IProcess process, URI uri) {
		if (process.getProcessElements() != null) {
			for (IProcessElement pe : process.getProcessElements()) {
				if (pe instanceof ProcessElement) {
					if (pe.getID().equals(uri.toString())) {
						return pe;
					}
					
				}
			}
		}
		
		return null;
	}
	
//	public static FlowObject getFlowObjectByIdentifier(Process process, URI uri) {
//		if (process.getProcessElements() != null) {
//			for (FlowObject flowObject : process.getProcessElements()) {
//				if (flowObject.getIdentifier().equals(uri)) {
//					return flowObject;
//				}
//			}
//		}
//		
//		return null;
//	}
	
	/**
	 * Returns list of Activities that contain goals.
	 * 
	 * @param process - input process
	 * @return List of activities
	 */
    @Override
	public ArrayList<IActivity> findAllActivities(IProcess process) {
		ArrayList<IActivity> listOfGoals = new ArrayList<IActivity>();
		for (IProcessElement pe : process.getProcessElements()) {
			if (pe instanceof Activity) {
				//Filter start/end activities
				if (!((Activity) pe).isStartElement() && !((Activity) pe).isEndElement()){
					listOfGoals.add((Activity)pe);
				}
			}
		}
		return listOfGoals;
	}
	
	/**
	 * Returns list of Activities that contain composite goals.
	 * 
	 * @param process - input process
	 * @return List of activities
	 */
//	public static ArrayList<Activity> findAllCompositeGoals(Process process) {
//		ArrayList<Activity> listOfGoals = new ArrayList<Activity>();
//		for (ProcessElement pe : process.getProcessElements()) {
//			//if (flowObject instanceof Goal) {
//			
//			if (pe instanceof Activity) {
//				Activity a = (Activity) pe;
//				if ( (a.getConversation() != null) && 
//					 (a.getConversation().getGoal() != null) &&
//					 (a.getConversation().isCompositeGoal())
//					 ) {
//					listOfGoals.add(a);
//				}
//			}
//		}
//		return listOfGoals;
//	}
	
	/**
	 * Returns list of Activities that contain atomic goals.
	 * 
	 * @param process - input process
	 * @return List of activities
	 */
//	public static ArrayList<Activity> findAllAtomicGoals(Process process) {
//		ArrayList<Activity> listOfGoals = new ArrayList<Activity>();
//		for (ProcessElement pe : process.getProcessElements()) {
//			//if (flowObject instanceof Goal) {
//			
//			if (pe instanceof Activity) {
//				Activity a = (Activity) pe;
//				if ( (a.getConversation() != null) && 
//					 (a.getConversation().getGoal() != null) &&
//					 (!a.getConversation().isCompositeGoal())
//					 ) {
//					listOfGoals.add(a);
//				}
//			}
//		}
//		return listOfGoals;
//	}
	
	/**
	 * Returns list of non Human Task Activities
	 * 
	 * @param process - input process
	 * @return List of activities
	 */
    @Override
	public Collection<? extends IActivity> findAllNonHTActivities(
			IProcess process) {
		ArrayList<Activity> listOfGoals = new ArrayList<Activity>();
		for (IProcessElement pe : process.getProcessElements()) {
			if (pe instanceof Activity) {
				Activity a = (Activity) pe;
				if (!a.isHumanTask()) {
					listOfGoals.add(a);
				}
			}
		}
		return listOfGoals;
	}
	
	/**
	 * Returns list of Activities that contain unresolved goals.
	 * 
	 * @param process - input process
	 * @return List of activities
	 */
    @Override
	public ArrayList<IActivity> findAllUnboundActivities(IProcess process) {
		ArrayList<IActivity> unboundActivities = new ArrayList<IActivity>();
		for (IProcessElement pe : process.getProcessElements()) {
			//if (flowObject instanceof Goal) {
			
			if (pe instanceof Activity) {
				Activity a = (Activity) pe;
				if (a.getBindings().isEmpty()) {
					unboundActivities.add(a);
				}
			}
		}
		return unboundActivities;
	}
	
	/**
	 * Returns list of unbound Activities (discarding start/end)
	 * 
	 * @param process - input process
	 * @return List of activities
	 */
    @Override
	public ArrayList<IActivity> findAllUnboundActivities2 (IProcess process) {
		ArrayList<IActivity> unboundActivities = new ArrayList<IActivity>();
		for (IProcessElement pe : process.getProcessElements()) {
			
			if (pe instanceof Activity) {
				Activity a = (Activity) pe;
				if (a.getBindings().isEmpty() && !a.isEndElement() && !a.isStartElement()) {
					unboundActivities.add(a);
				}
			}
		}
		return unboundActivities;
	}
	
	/**
	 * Returns all goals from given process in a form of HashMap. 
	 * The return variable is a hashmap that relates Goals to its Activities
	 * 
	 * @param process
	 * @return
	 */
//	public static List<Goal, Activity> findAllGoalsHM(Process process) {
//		HashMap<Goal, Activity> goalMap = new HashMap<Goal, Activity>();
//		//DEBUG
//		String goals = "";
//		for (ProcessElement pe : process.getProcessElements()) {
//			if (pe instanceof Activity) {
//				Activity a = (Activity) pe;
//				if ( (a.getConversation() != null) && (a.getConversation().getGoal() != null) ) {
//					Goal g = a.getConversation().getGoal();
//					goalMap.put(g, a);
//					//DEBUG
//					goals += " " + g.getName() + ", ";
//				}
//			}
//		}
//		//DEBUG
//		System.out.println("@@@ goals in ("+process.getID()+"): "+ goals);
//		
//		return goalMap;
//	}
	
	
//	public static Activity findGoal(Process process, URI goalTarget) {
//		Activity activity = null;
//		for (ProcessElement pe : process.getProcessElements()) {
//			//if (flowObject instanceof Goal) {
//			if (pe instanceof Activity) {
//				Activity a = (Activity) pe;
//				if ( (a.getConversation() != null) && (a.getConversation().getGoal() != null) ) {
//					if (a.getConversation().getGoal().getGoalReference().equals(goalTarget.toString())) {
//						activity = a;
//					}					
//				}
//			}
//		}
//		return activity;		
//	}

//	public Activity findGoalByID(Process process, URI goalTarget) {
//		Activity activity = null;
//		for (ProcessElement pe : process.getProcessElements()) {
//			//if (flowObject instanceof Goal) {
//			if (pe instanceof Activity) {
//				Activity a = (Activity) pe;
//				if (a.getID().equals(goalTarget.toString())) {
//					activity = a;
//				}					
//			}
//		}
//		return activity;		
//	}
	
	/**
	 * Replace a Goal with a Service
	 * @param goal
	 * @param service
	 */
	public boolean substitute(IProcess process, IActivity activity, IBinding binding) {
		boolean modified = false;
		//Checking if goal exist in this.process
		if (!process.getProcessElements().contains(activity)){
			return false;
		} else if (activity != null) {
			// Avoid duplicates
			if (!exist (activity.getBindings(), binding)){
				activity.getBindings().add(binding);
				modified = true;
			}
		}
		return modified;
	}
	
	private boolean exist(List<IBinding> bindings, IBinding binding) {
		boolean result = false;
		
		for (IBinding b: bindings){
			if (b.getOperation().equals(binding.getOperation()) && b.getServiceReference().equals(binding.getServiceReference())){
				result = true;
				break;
			}
		}
		
		return result;
	}

    @Override
	public IProcess cloneProcess(IProcess process) throws Exception {
		XStream xstream = new XStream();
		
		Process clonedProcess = (Process) xstream.fromXML(xstream.toXML(process));
//		clonedProcess.setID(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-"));
		return clonedProcess;
	}
	
	public <T> T cloneLPMLObject(T cloneObject) throws URISyntaxException {
		XStream xstream = new XStream();
		T cloned = (T) xstream.fromXML(xstream.toXML(cloneObject));
//		if (cloned instanceof Process) {
//			((Process)cloned).setID(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-"));	
//		}
//		if (cloned instanceof ProcessElement) {
//			((ProcessElement)cloned).setID(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-"));	
//		}
//		if (cloned instanceof SemanticAnnotation) {
//			((SemanticAnnotation)cloned).setID(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-"));	
//		}
		
		if (cloned instanceof DesignModel) {
			((DesignModel)cloned).getStatus().emptyStatus();
			
			// Setting new model id. LPML does not allow to set the id excepting through the constructor so we create and set a new model
//			Process process = ((DesignModel)cloned).getDesignStructure();
//			Process p = new ProcessImpl();
//			p.getProcessElements().addAll(process.getProcessElements());
//			p.getFlows().addAll(process.getFlows());
//			p.getSemanticAnnotations().addAll(process.getSemanticAnnotations());
//			((DesignModel)cloned).setDesignStructure(p);
			
//			try {
				((DesignModel)cloned).setIdentifier(new URI(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-")));
//				((DesignModel)cloned).getDesignStructure().setID(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-"));
//				
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}	
		}
		return cloned;
	}

	@Override
	public String createProcessWithActivityAnnotations(
			List<ISemanticAnnotation> gsas, List<ISemanticAnnotation> sas, 
			List<ISemanticAnnotation> isas, List<ISemanticAnnotation> osas, String activityURI) {
		
		if (sas.isEmpty() || isas.isEmpty() || osas.isEmpty()){
			return null;
		}
		
		Process p = (Process) new ProcessImpl();
		
		// Global requirements and constraints
		p.getSemanticAnnotations().addAll(gsas);
		
		// Start/End
		Activity start = (Activity) new ActivityImpl();
		start.setStartElement(true);
		start.setName("Start");
		p.getProcessElements().add(start);

		Activity end = (Activity) new ActivityImpl();
		end.setEndElement(true);
		end.setName("End");
		p.getProcessElements().add(end);	
		
		// Activity
		Activity activity = (Activity) new ActivityImpl(activityURI);
		activity.setName("Activity");
		activity.getSemanticAnnotations().addAll(sas);
		p.getProcessElements().add(activity);
		
		// I/O Parameters
		Parameter in_Act = (Parameter) new ParameterImpl();
		Parameter out_Act = (Parameter) new ParameterImpl();
		in_Act.getSemanticAnnotations().addAll(isas);
		out_Act.getSemanticAnnotations().addAll(osas);

		activity.getInputParameters().add(in_Act);
		activity.getOutputParameters().add(out_Act);
		
		//Flows
		Flow flow1 = (Flow) new FlowImpl();
		flow1.setSource(start);
		flow1.setDestination(activity);
		p.getFlows().add(flow1);
		
		Flow flow2 = (Flow) new FlowImpl();
		flow2.setSource(activity);
		flow2.setDestination(end);
		p.getFlows().add(flow2);
		
		return new LPMLXMLMapper().mapToXML(p);
	}

	@Override
	public List<IBinding> getBindings(String model, String activityTargetURI) throws URISyntaxException {
		Process process = (Process) ModelIOUtils.loadModelFromXML(model);
		Activity activity = (Activity) getActivityById(process, new URI (activityTargetURI));
		return activity.getBindings();
	}
	
//	public URI createURI(String Namespace, String ID) throws URISyntaxException {
//		String uri = new String();
//		uri = Namespace + ID.replace("-", "").replace(":", "-");
//		
//		return new URI(uri);
//	}

//	public static void renewIdentifiers(Process process) {
//		Set<ProcessElement> pes = process.getProcessElements();
//		for (ProcessElement pe: pes){
//			pe.setID(new java.rmi.dgc.VMID().toString().replace("-", "").replace(":", "-"));
//		}
//		
//	}
	
}
