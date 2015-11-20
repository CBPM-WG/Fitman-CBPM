/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author mahdsip
 */
@Path("/bpmnservice/")
public interface IResource {
    @POST
    @Path("/complete/")    
    public String CompleteBPMN(String bpmn);    
}
