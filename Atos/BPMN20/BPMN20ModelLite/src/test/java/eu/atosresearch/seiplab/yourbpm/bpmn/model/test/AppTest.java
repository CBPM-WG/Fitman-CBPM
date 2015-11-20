package eu.atosresearch.seiplab.yourbpm.bpmn.model.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import eu.atosresearch.seiplab.yourbpm.bpmn.standard.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
/*    public void testApp()
    {
        try {
           JAXBContext jc = JAXBContext.newInstance(TDefinitions.class.getPackage().getName());
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<TDefinitions> definitioElement = (JAXBElement<TDefinitions>) u.unmarshal(AppTest.class.getResourceAsStream("/procesExampleWS.bpmn20.xml"));
            TDefinitions definition = definitioElement.getValue();
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
            
            
            //recover the list of interface associated.
            List<TInterface> listTInterface = new ArrayList<TInterface>(); 
            
            for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
				TRootElement jaxbElement = iterator.next().getValue();
				if (jaxbElement instanceof TInterface){
					TInterface tInterface = (TInterface) jaxbElement;
					listTInterface.add(tInterface);
				}
			}
            
            //recover the list of message associated.
            List<TMessage> listMessage = new ArrayList<TMessage>(); 
            
            for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
				TRootElement jaxbElement = iterator.next().getValue();
				if (jaxbElement instanceof TMessage){
					TMessage tMessage = (TMessage) jaxbElement;
					listMessage.add(tMessage);
				}
			}
            
            
            
            
            //recover the process
            TProcess process = null;
            
            for (Iterator<JAXBElement<? extends TRootElement>> iterator = listRootElement.iterator(); iterator.hasNext();) {
				TRootElement jaxbElement = iterator.next().getValue();
				if (jaxbElement instanceof TProcess){
					process= (TProcess) jaxbElement;
				}
			}
            
            if (process !=null){
	            List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
	            for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
	            	TFlowElement jaxbElement = iterator.next().getValue();
					if (jaxbElement instanceof TSequenceFlow){
						TSequenceFlow sequenceFlow = (TSequenceFlow) jaxbElement;
						System.out.println(sequenceFlow.getId());
					}else if (jaxbElement instanceof TUserTask){
						TUserTask userTask = (TUserTask) jaxbElement;
						System.out.println(userTask.getId());
						System.out.println(userTask.getName());
						userTask.setName("UserTaskQUIM");
						System.out.println(userTask.getName());
					}else if (jaxbElement instanceof TServiceTask){
						TServiceTask userTask = (TServiceTask) jaxbElement;
						System.out.println("ServiceTask:"+userTask.getId());
						System.out.println("Input"+userTask.getDataInputAssociation().get(0).getTargetRef().toString());
						userTask.setName("UserTaskQUIM");
						System.out.println(userTask.getName());
					}
            }
				
			}
            
            
            try {
            	 
        		Marshaller jaxbMarshaller = jc.createMarshaller();
        		// output pretty printed
        		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        		jaxbMarshaller.marshal(definitioElement, System.out);
         
        	      } catch (Exception e) {
        		e.printStackTrace();
        	      }
            
            
            
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
   	
    	assertTrue( true );
    }
   */    
}
