/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.webservice;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;


/**
 *
 * @author mahdsip
 */
public class WSServer {

    private ServiceSEI bpmnManager;
	private Server server;
	
	protected void initializeFrontEndProcessEngine() {
        bpmnManager = new Service();

        // Create our Server
        ServerFactoryBean svrFactory = new ServerFactoryBean();
        svrFactory.setServiceClass(ServiceSEI.class);
        svrFactory.setAddress("http://localhost:9192/FrontEndBPMNServer");
        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());        
        svrFactory.setServiceBean(bpmnManager);
        svrFactory.create();
    }
	protected void initializeCXFProcessEngine(){
        bpmnManager = new Service();
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(ServiceSEI.class);
        svrFactory.setAddress("http://localhost:9191/BPMNServer");
        svrFactory.setServiceBean(bpmnManager);
        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        svrFactory.create();
	}
    	public static void main (String[] args) throws InterruptedException{
		WSServer test = new WSServer();
		test.initializeFrontEndProcessEngine();
		test.initializeCXFProcessEngine();
	}       
}
