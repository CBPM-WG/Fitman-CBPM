/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.rest;

import org.apache.cxf.resource.URIResolver;
import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;


/**
 *
 * @author mahdsip
 */
public class RESTClient {

   public static void main(String args[]) throws Exception {

        RESTClient client = new RESTClient();


        // Sent HTTP POST request to add customer
        System.out.println("\n");
        System.out.println("Sent HTTP POST request to complete BPMN2.0");
        String inputFile = "src/main/resources/bpmn20.input.xml";
        URIResolver resolver = new URIResolver(inputFile);
        File input = new File(resolver.getURI());
        PostMethod post = new PostMethod("http://localhost:8080/BPMN20Module/bpmn20Resource/bpmnservice/complete");
        post.addRequestHeader("Accept" , "text/xml");
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        post.setRequestEntity(entity);
        HttpClient httpclient = new HttpClient();

        try {
            int result = httpclient.executeMethod(post);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
        } finally {
            // Release current connection to the connection pool once you are
            // done
            post.releaseConnection();
        }

        System.out.println("\n");
        System.exit(0);
    }    
}
