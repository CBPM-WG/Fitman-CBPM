/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.service.webservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.cxf.frontend.ClientProxyFactoryBean;

/**
 *
 * @author mahdsip
 */
public class WSClient {
 public static void main (String[] args) throws FileNotFoundException, IOException{
     
                ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
                factory.setServiceClass(ServiceSEI.class);
                factory.setAddress("http://localhost:9191/BPMNServer");
                ServiceSEI client = (ServiceSEI) factory.create();
                
//		String fileName = Thread.currentThread().getContextClassLoader().getResource("bpmn20.input.xml").getFile();
       		String fileName = "src/main/resources/bpmn20.input.xml";
                byte[] buffer = new byte[(int) new File(fileName).length()];
                BufferedInputStream f = new BufferedInputStream(new FileInputStream(fileName));
		f.read(buffer);
		String content = new String(buffer);
		content.replace("<","&lt;");
		content.replace(">","&gt;");
                
                String result = client.CompleteBPMN(content);
                
                System.out.println("The file " +content+ " is completed" 
                + result); 
  }      
}
