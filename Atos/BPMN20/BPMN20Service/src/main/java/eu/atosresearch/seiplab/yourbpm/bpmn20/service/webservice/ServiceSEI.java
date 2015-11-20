/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

/**
 *
 * @author mahdsip
 */
@WebService
@SOAPBinding(style=Style.DOCUMENT,use=Use.LITERAL,parameterStyle=ParameterStyle.BARE)
public interface ServiceSEI {

    @WebMethod(operationName="completeBPMN")
    @WebResult(name="result")
    public String CompleteBPMN(@WebParam String bpmn);
    //public Definitions CompleteBPMN(Definitions bpmn);    
}
