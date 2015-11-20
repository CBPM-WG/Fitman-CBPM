
/**
 * DTComposerExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package eu.atosresearch.seiplab.yourbpm.dtc.client.stub;

public class DTComposerExceptionException0 extends java.lang.Exception{
    
    private eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.DTComposerExceptionE faultMessage;
    
    public DTComposerExceptionException0() {
        super("DTComposerExceptionException0");
    }
           
    public DTComposerExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public DTComposerExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.DTComposerExceptionE msg){
       faultMessage = msg;
    }
    
    public eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.DTComposerExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    