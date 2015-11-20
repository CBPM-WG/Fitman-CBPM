/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.rest;

import eu.atosresearch.seiplab.yourbpm.bpmn20.service.BPMNServiceImpl;




/**
 *
 * @author mahdsip
 */

public class Resource implements IResource{

    @Override
    public String CompleteBPMN(String bpmn) {
        BPMNServiceImpl bpmman= new BPMNServiceImpl();
        return bpmman.CompleteBPMN(bpmn);
    }

    
}
