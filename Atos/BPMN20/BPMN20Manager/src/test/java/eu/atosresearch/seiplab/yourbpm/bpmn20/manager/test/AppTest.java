package eu.atosresearch.seiplab.yourbpm.bpmn20.manager.test;

import static org.junit.Assert.*;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.ItemDefinition;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Message;
import eu.atosresearch.seiplab.yourbpm.bpmn20.manager.BPMN20Manager;
import javax.xml.namespace.QName;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {


    /**
     * Rigourous Test :-)
     */
    @Test
    @Ignore
    public void testAdding()
    {
        BPMN20Manager bpmnMan = new BPMN20Manager("src/main/resources/bpmn20.input.xml");
        bpmnMan.unmarshalling();
        bpmnMan.addWSDL("src/main/resources/imath.wsdl");
        bpmnMan.addWSDL("http://localhost:9191/BPMNServer?wsdl");        
        String tn = bpmnMan.getWsdlManager().getDefinitionList().get(0).getTargetNamespace();
        
        ItemDefinition item = new  ItemDefinition();
        item.setId("pruebaItemrra");
        item.setStructureRef(new QName(tn,"pruebarrr"));
        Message mess = new Message();
        mess.setId("messageIDrrra");
        mess.setItemRef(new QName("http://www.omg.org/spec/BPMN/20100524/MODEL","pruebarrr"));
        
        bpmnMan.addItemDefinition(item);
        bpmnMan.addMessage(mess);
        bpmnMan.marshalling();
        assertTrue( true );
    }
    
    @Test
    @Ignore
    public void testCompleteTask(){
        BPMN20Manager bpmnMan = new BPMN20Manager("src/main/resources/OryxBPMN2.0.xml");
        bpmnMan.unmarshalling();    	
//    	bpmnMan.completeTask("oryx_B53C823D-F716-4295-A535-A294C0427899");
        bpmnMan.completeAllTask();
        bpmnMan.marshalling();
    	
    }
}
