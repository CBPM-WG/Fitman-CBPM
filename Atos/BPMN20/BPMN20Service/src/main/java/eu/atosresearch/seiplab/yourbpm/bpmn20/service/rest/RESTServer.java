/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.rest;

/**
 *
 * @author mahdsip
 */

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public class RESTServer {
    
    protected RESTServer() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(Resource.class);
        sf.setResourceProvider(Resource.class, 
            new SingletonResourceProvider(new Resource()));
        sf.setAddress("http://localhost:9000/");

        sf.create();
    }

    public static void main(String args[]) throws Exception {
        new RESTServer();
        System.out.println("Server ready...");

        Thread.sleep(5 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }    
}
