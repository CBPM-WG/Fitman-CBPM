package org.soa4all.lpml.util;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IIdentifiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.soa4all.lpml.Activity;
import org.soa4all.lpml.Flow;
import org.soa4all.lpml.Process;
import org.soa4all.lpml.ProcessElement;

/**
 * This fail fast" LPML process validator performs 
 * structural and content checks in order to make sure
 * that the process is valid. For example, a flow that does not have
 * source and destination is orphaned and hence invalid.
 * Another example is more than one start element.
 * 
 * Note, the fact that the process is structurally valid does not mean
 * that it is executable. For example unbound activities or missing data
 * flow mapping are not considered invalid although they will cause 
 * failures during execution. This is needed in order to allow the
 * user to save and load incomplete, but structurally valid processes.
 */
public class LPMLValidator {
	
	/**
	 * @param process the process to be validated
	 * @throws LPMLValidationException in case of invalid process. Use the message to figure out what is wrong
	 */
	public void validate(Process process) throws LPMLValidationException {
		
		checkTheNumberOfStartAndEndElementsAndIOParameters(process);
		
		Map<ProcessElement, Set<Flow>> outgoingFlowsMap = getOutgoingFlowsMap(process);
		checkDefaultFlowRules(outgoingFlowsMap);
		checkForDuplicateIDs(process);
		checkBindingsOperationAndServiceURIsForNull(process);
		
	}


	private void checkBindingsOperationAndServiceURIsForNull(Process process) throws LPMLValidationException{
		
		for(IProcessElement e: process.getProcessElements()){
			if(!(e instanceof Activity)){
				continue;
			}
			Activity a = (Activity)e;
			for(IBinding b:a.getBindings()){
				if(b.getOperation() == null){
					throw new LPMLValidationException("null operation found in binding [" + b + "] of activity [" + a + "]");
				}
				if(b.getServiceReference() == null){
					throw new LPMLValidationException("null service reference found in binding [" + b + "] of activity [" + a + "]");
				}
			}
		}
		
	}


	private void checkForDuplicateIDs(Process process) throws LPMLValidationException {
		
		Map<String, IIdentifiable> id2Object = new HashMap<String, IIdentifiable>();
		
		Set<IIdentifiable> allElements = new HashSet<IIdentifiable>();
		allElements.addAll(process.getProcessElements());
		allElements.addAll(process.getFlows());
		allElements.addAll(process.getSemanticAnnotations());
		for(IProcessElement e: process.getProcessElements()){
			allElements.addAll(e.getSemanticAnnotations());
		}
		
		for(IIdentifiable e: allElements){
			
			IIdentifiable found = id2Object.get(e.getID());
			if(found != null){
				throw new LPMLValidationException("Duplicate key found for elements [" + found + "] and [" + e + "]");
			}
			id2Object.put(e.getID(), e);
		}		
	}

	private void checkDefaultFlowRules(Map<ProcessElement, Set<Flow>> outgoingFlowsMap) throws LPMLValidationException {
		
		for(Entry<ProcessElement, Set<Flow>> e:outgoingFlowsMap.entrySet()){
		
			Set<Flow> flows = e.getValue();
			if(flows == null || flows.size() == 0){ // unconnected element
				continue;
			}
			
			int defaultFlowCount = 0;
			for(Flow f:flows){
				if(f.getSequenceNumber() == Integer.MAX_VALUE){
					defaultFlowCount++;
				}
			}
			if(defaultFlowCount > 1){
				throw new LPMLValidationException("More than one default flow [" + defaultFlowCount + "] detected for element: " + e);
			}
			
			if(defaultFlowCount < 1){
				throw new LPMLValidationException("No default flows detected for element: " + e);
			}
		}
		
	}

	private void checkOrphanedFlow(Flow f) throws LPMLValidationException {

			if(f.getSource() == null ){
				throw new LPMLValidationException("Flow f does not have a source element");
			}
			
			if(f.getDestination() == null){
				throw new LPMLValidationException("Flow f does not have a destination element");
			}
	}

	private void checkTheNumberOfStartAndEndElementsAndIOParameters(Process process) throws LPMLValidationException{

		final Collection<Activity> foundStartElements = new HashSet<Activity>();
		final Collection<Activity> foundEndElements = new HashSet<Activity>();
		
		for(IProcessElement element:process.getProcessElements()){
			if(element instanceof Activity){
				Activity a = (Activity) element;
				if(a.isStartElement()){
					foundStartElements.add(a);
				}
				if(a.isEndElement()){
					foundEndElements.add(a);
				}
			}
		}
		
		if(foundStartElements.size() > 1){
			throw new LPMLValidationException("More than one start element found: " + foundStartElements);
		}
		
		if(foundEndElements.size() > 1){
			throw new LPMLValidationException("More than one end element found: " + foundEndElements);
		}
		
		for(Activity startElement:foundStartElements){
			if(startElement.getInputParameters().size() > 0){
				throw new LPMLValidationException("Start event shall not have input parameters: " + startElement);
			}
		}
		
		for(Activity endElement:foundEndElements){
			if(endElement.getOutputParameters().size() > 0){
				throw new LPMLValidationException("End event shall not have output parameters: " + endElement);
			}
		}
	}
	
	private Map<ProcessElement, Set<Flow>> getOutgoingFlowsMap(Process process) throws LPMLValidationException {

		final Map<ProcessElement, Set<Flow>> outgoingFlows = new HashMap<ProcessElement, Set<Flow>>(process.getProcessElements().size());
		
		for(Flow f:process.getFlows()){
			
			checkOrphanedFlow(f);
			checkDefaultFlowWithCondition(f);
			checkDefaultFlowWithAnnotations(f);
			ProcessElement src = f.getSource();
			
			Set<Flow> flows = outgoingFlows.get(src);
			if(flows == null){
				flows = new HashSet<Flow>();
				outgoingFlows.put(src, flows);
			}
			flows.add(f);
		}
		
		return outgoingFlows;
	}

	private void checkDefaultFlowWithAnnotations(Flow f) throws LPMLValidationException {

		if(f.getSequenceNumber() != Integer.MAX_VALUE){
			return;
		}
		
		if(f.getSemanticAnnotations().size() > 0){
			throw new LPMLValidationException("Default flow must not have semantic annotations: " + f);
		}
		
	}

	/**
	 * Having a default flow with a condition indicates wrong intentions of the
	 * person who created the process, because the condition will never be 
	 * taken into account
	 * 
	 * @param f
	 * @throws LPMLValidationException
	 */
	private void checkDefaultFlowWithCondition(Flow f) throws LPMLValidationException {
		
		if(f.getSequenceNumber() != Integer.MAX_VALUE){
			return;
		}
		
		if(f.getCondition() != null){
			throw new LPMLValidationException("Default flow must not have a condition: " + f);
		}
	}
}
