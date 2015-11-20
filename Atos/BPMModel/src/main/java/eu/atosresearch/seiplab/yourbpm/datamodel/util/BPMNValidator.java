package eu.atosresearch.seiplab.yourbpm.datamodel.util;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IFlow;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IIdentifiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;



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
public class BPMNValidator {
	
	/**
	 * @param process the process to be validated
	 * @throws BPMNValidationException in case of invalid process. Use the message to figure out what is wrong
	 */
	public void validate(IProcess process) throws BPMNValidationException {
		
		checkTheNumberOfStartAndEndElementsAndIOParameters(process);
		
		Map<IProcessElement, Set<IFlow>> outgoingFlowsMap = getOutgoingFlowsMap(process);
		checkDefaultFlowRules(outgoingFlowsMap);
		checkForDuplicateIDs(process);
		checkBindingsOperationAndServiceURIsForNull(process);
		
	}


	private void checkBindingsOperationAndServiceURIsForNull(IProcess process) throws BPMNValidationException{
		
		for(IProcessElement e: process.getProcessElements()){
			if(!(e instanceof IActivity)){
				continue;
			}
			IActivity a = (IActivity)e;
			for(IBinding b:a.getBindings()){
				if(b.getOperation() == null){
					throw new BPMNValidationException("null operation found in binding [" + b + "] of activity [" + a + "]");
				}
				if(b.getServiceReference() == null){
					throw new BPMNValidationException("null service reference found in binding [" + b + "] of activity [" + a + "]");
				}
			}
		}
		
	}


	private void checkForDuplicateIDs(IProcess process) throws BPMNValidationException {
		
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
				throw new BPMNValidationException("Duplicate key found for elements [" + found + "] and [" + e + "]");
			}
			id2Object.put(e.getID(), e);
		}		
	}

	private void checkDefaultFlowRules(Map<IProcessElement, Set<IFlow>> outgoingFlowsMap) throws BPMNValidationException {
		
		for(Entry<IProcessElement, Set<IFlow>> e:outgoingFlowsMap.entrySet()){
		
			Set<IFlow> flows = e.getValue();
			if(flows == null || flows.size() == 0){ // unconnected element
				continue;
			}
			
			int defaultFlowCount = 0;
			for(IFlow f:flows){
				if(f.getSequenceNumber() == Integer.MAX_VALUE){
					defaultFlowCount++;
				}
			}
			if(defaultFlowCount > 1){
				throw new BPMNValidationException("More than one default flow [" + defaultFlowCount + "] detected for element: " + e);
			}
			
			if(defaultFlowCount < 1){
				throw new BPMNValidationException("No default flows detected for element: " + e);
			}
		}
		
	}

	private void checkOrphanedFlow(IFlow f) throws BPMNValidationException {

			if(f.getSource() == null ){
				throw new BPMNValidationException("Flow f does not have a source element");
			}
			
			if(f.getDestination() == null){
				throw new BPMNValidationException("Flow f does not have a destination element");
			}
	}

	private void checkTheNumberOfStartAndEndElementsAndIOParameters(IProcess process) throws BPMNValidationException{

		final Collection<IActivity> foundStartElements = new HashSet<IActivity>();
		final Collection<IActivity> foundEndElements = new HashSet<IActivity>();
		
		for(IProcessElement element:process.getProcessElements()){
			if(element instanceof IActivity){
				IActivity a = (IActivity) element;
				if(a.isStartElement()){
					foundStartElements.add(a);
				}
				if(a.isEndElement()){
					foundEndElements.add(a);
				}
			}
		}
		
		if(foundStartElements.size() > 1){
			throw new BPMNValidationException("More than one start element found: " + foundStartElements);
		}
		
		if(foundEndElements.size() > 1){
			throw new BPMNValidationException("More than one end element found: " + foundEndElements);
		}
		
		for(IActivity startElement:foundStartElements){
			if(startElement.getInputParameters().size() > 0){
				throw new BPMNValidationException("Start event shall not have input parameters: " + startElement);
			}
		}
		
		for(IActivity endElement:foundEndElements){
			if(endElement.getOutputParameters().size() > 0){
				throw new BPMNValidationException("End event shall not have output parameters: " + endElement);
			}
		}
	}
	
	private Map<IProcessElement, Set<IFlow>> getOutgoingFlowsMap(IProcess process) throws BPMNValidationException {

		final Map<IProcessElement, Set<IFlow>> outgoingFlows = new HashMap<IProcessElement, Set<IFlow>>(process.getProcessElements().size());
		
		for(IFlow f:process.getFlows()){
			
			checkOrphanedFlow(f);
			checkDefaultFlowWithCondition(f);
			checkDefaultFlowWithAnnotations(f);
			IProcessElement src = f.getSource();
			
			Set<IFlow> flows = outgoingFlows.get(src);
			if(flows == null){
				flows = new HashSet<IFlow>();
				outgoingFlows.put(src, flows);
			}
			flows.add(f);
		}
		
		return outgoingFlows;
	}

	private void checkDefaultFlowWithAnnotations(IFlow f) throws BPMNValidationException {

		if(f.getSequenceNumber() != Integer.MAX_VALUE){
			return;
		}
		
		if(f.getSemanticAnnotations().size() > 0){
			throw new BPMNValidationException("Default flow must not have semantic annotations: " + f);
		}
		
	}

	/**
	 * Having a default flow with a condition indicates wrong intentions of the
	 * person who created the process, because the condition will never be 
	 * taken into account
	 * 
	 * @param f
	 * @throws BPMNValidationException
	 */
	private void checkDefaultFlowWithCondition(IFlow f) throws BPMNValidationException {
		
		if(f.getSequenceNumber() != Integer.MAX_VALUE){
			return;
		}
		
		if(f.getCondition() != null){
			throw new BPMNValidationException("Default flow must not have a condition: " + f);
		}
	}
}
