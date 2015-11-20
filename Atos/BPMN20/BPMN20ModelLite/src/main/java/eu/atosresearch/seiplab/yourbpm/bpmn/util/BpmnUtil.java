package eu.atosresearch.seiplab.yourbpm.bpmn.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import eu.atosresearch.seiplab.yourbpm.bpmn.standard.*;



/**
 * @author S270094
 * 
 * The BpmnUtil allows to work with the generated jaxb classes.
 *
 */
public class BpmnUtil {
	
	/** get the process included in the definition root.
	 * @param definition 
	 * @return TProcess
	 */
	public static TProcess getProcess(TDefinitions definition){
		
        List<JAXBElement<? extends TRootElement>>  listRootElement = definition.getRootElement();
        
        TProcess process = null;
        
        for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
			TRootElement jaxbElement = iterator.next().getValue();
			if (jaxbElement instanceof TProcess){
				process = (TProcess) jaxbElement;
			}
		}
        
        return process;
		
	}
	
	
	/** get the List of ItemDefinition included in the definition root.
	 * @param definition 
	 * @return List<TItemDefinition>
	 */
	public static List<TItemDefinition> getListItemDefinition(TDefinitions definition){
		
		List<JAXBElement<? extends TRootElement>>  listRootElement = definition.getRootElement();
		
        //recover the list of message associated.
        List<TItemDefinition> listItemDefinition = new ArrayList<TItemDefinition>(); 
        
        for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
			TRootElement jaxbElement = iterator.next().getValue();
			if (jaxbElement instanceof TItemDefinition){
				TItemDefinition itemDefinition = (TItemDefinition) jaxbElement;
				listItemDefinition.add(itemDefinition);
			}
		}
        
        return listItemDefinition;
		
	}
	
	/** get the List of Interface included in the definition root.
	 * @param definition 
	 * @return List<TInterface>
	 */
	public static List<TInterface> getListInterface(TDefinitions definition){
		
		List<JAXBElement<? extends TRootElement>>  listRootElement = definition.getRootElement();
		
        //recover the list of interface associated.
        List<TInterface> listTInterface = new ArrayList<TInterface>(); 
        
        for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
			TRootElement jaxbElement = iterator.next().getValue();
			if (jaxbElement instanceof TInterface){
				TInterface tInterface = (TInterface) jaxbElement;
				listTInterface.add(tInterface);
			}
		}
        
        return listTInterface;
		
	}
	
	/** get the List of Message included in the definition root.
	 * @param definition 
	 * @return List<TMessage>
	 */
	public static List<TMessage> getListMessage(TDefinitions definition){
		
		List<JAXBElement<? extends TRootElement>>  listRootElement = definition.getRootElement();
		
        //recover the list of message associated.
        List<TMessage> listMessage = new ArrayList<TMessage>(); 
        
        for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
			TRootElement jaxbElement = iterator.next().getValue();
			if (jaxbElement instanceof TMessage){
				TMessage tMessage = (TMessage) jaxbElement;
				listMessage.add(tMessage);
			}
		}
        
        return listMessage;
		
	}
	
	
	/** get the start event included in the process element.
	 * @param process 
	 * @return TStartEvent
	 */
	public static TStartEvent getStartEvent(TProcess process){
		
		//Theoretically there is only one StartEvent.
		TStartEvent startEvent = null;
        if (process !=null){
            List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
            for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
            	TFlowElement jaxbElement = iterator.next().getValue();
				if (jaxbElement instanceof TStartEvent){
					startEvent = (TStartEvent) jaxbElement;
				}
         }
			
		}
        
        return startEvent;
		
	}
	
	
	/** get the List of SequenceFlow included in the process element.
	 * @param process 
	 * @return List<TSequenceFlow>
	 */
	public static List<TSequenceFlow> getListSequenceFlow(TProcess process){
		
		 //recover the list of message associated.
        List<TSequenceFlow> listSequenceFlow = new ArrayList<TSequenceFlow>(); 
        if (process !=null){
            List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
            for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
            	TFlowElement jaxbElement = iterator.next().getValue();
				if (jaxbElement instanceof TSequenceFlow){
					TSequenceFlow sequenceFlow = (TSequenceFlow) jaxbElement;
					listSequenceFlow.add(sequenceFlow);
				}
         }
			
		}
        
        return listSequenceFlow;
		
	}
	
	
	/** get the end event included in the process element.
	 * @param process 
	 * @return TStartEvent
	 */
	public static TEndEvent getEndEvent(TProcess process){
		
		//For the moment, we only assume one EndEvent.
		TEndEvent endEvent = null;
        if (process !=null){
            List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
            for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
            	TFlowElement jaxbElement = iterator.next().getValue();
				if (jaxbElement instanceof TEndEvent){
					endEvent = (TEndEvent) jaxbElement;
				}
         }
			
		}
        
        return endEvent;
		
	}
	

}
