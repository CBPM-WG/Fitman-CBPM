/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.webservice;

import eu.atosresearch.seiplab.yourbpm.bpmn20.service.BPMNServiceImpl;

import javax.jws.WebService;

/**
 *
 * @author mahdsip
 */
@WebService(endpointInterface="eu.atosresearch.seiplab.yourbpm.bpmn20.service.webservice.ServiceSEI",
            serviceName="BPMNManager")
public class Service implements ServiceSEI{

    @Override
    public String CompleteBPMN(String bpmn) {
        BPMNServiceImpl bpmman= new BPMNServiceImpl();
        return bpmman.CompleteBPMN(bpmn);
    }
    
}
