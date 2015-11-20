/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service;

import eu.atosresearch.seiplab.yourbpm.bpmn20.manager.BPMN20Manager;


/**
 *
 * @author mahdsip
 */
public class BPMNServiceImpl{

    public String CompleteBPMN(String bpmn){
	bpmn.replace("&lt;", "<");
	bpmn.replace("&gt;",">");
	BPMN20Manager bpmnMan = new BPMN20Manager(bpmn);
	bpmnMan.unmarshallingStandard();
	bpmnMan.completeAllTaskStandard();
	bpmnMan.marshallingStandard();
	String result = bpmnMan.getFinalDocumentWriter().toString();
	result.replace("&lt;", "<");
	result.replace("&gt;",">");			
	return result;			
    }    
}
