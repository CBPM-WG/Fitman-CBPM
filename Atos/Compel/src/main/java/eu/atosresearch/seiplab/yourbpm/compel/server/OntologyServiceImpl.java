package eu.atosresearch.seiplab.yourbpm.compel.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.emory.mathcs.backport.java.util.Arrays;
import eu.atosresearch.seiplab.kb.businesslogic.AnnotationHandler;
import eu.atosresearch.seiplab.yourbpm.compel.client.conf.CompelClientConfiguration;
import eu.atosresearch.seiplab.yourbpm.compel.server.conf.CompelServerConfiguration;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyNode;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyService;
import eu.atosresearch.seiplab.yourbpm.compel.shared.Service;
import eu.atosresearch.seiplab.yourbpm.dtc.client.DTComposerClient;

public class OntologyServiceImpl extends RemoteServiceServlet implements OntologyService {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(OntologyServiceImpl.class);
	
	//Create Service
//	WebN1Service webN1 = new WebN1Service();
	AnnotationHandler ah = new AnnotationHandler();
	
	public OntologyNode buildModelFromOntology(String restURL) throws Exception { 
		logger.debug("Loading ontology from url: "+ restURL);
		String rawData = fetchContent(restURL);

		if (restURL.toLowerCase().endsWith(".wsml")) {
            return GuiModelBuilder.buildFromWSML(rawData);
        }
       	try {
            return GuiModelBuilder.buildFromRDF(rawData);
        	}
        	catch(Throwable err) {
        		logger.error("Error loading ontology from url", err);
        		err.printStackTrace();
        		throw new Exception(err.getMessage());
        	}
    }
	
    private String fetchContent(String url) throws Exception {  

    	GetMethod getMtd = new GetMethod(url);    	
        HttpClient httpclient = new HttpClient();
        try {
            int result = httpclient.executeMethod(getMtd);            
            if (result != 200) {
                if (getMtd.getResponseHeader("Error") != null) {
                    throw new Exception("Error : " + getMtd.getResponseHeader("Error").getValue());
                }
                throw new Exception("Storage service error code - " + result);
            }
            BufferedInputStream response = new BufferedInputStream(getMtd.getResponseBodyAsStream());

            ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();

            byte[] buffer = new byte[1000];
            int i;
            do {
                i = response.read(buffer);
                if (i > 0) {
                    resultBuffer.write(buffer, 0, i);
                }
            }
            while(i > 0);
            getMtd.releaseConnection();
            
            return resultBuffer.toString("UTF-8");
        }
        catch(Exception exc) {
            getMtd.releaseConnection();
            throw new Exception(exc.getClass().getSimpleName() + " : " + exc.getMessage());
        }
    }
    
    /**********
     * Adds Requirement Annotation in the KB   
     */
    public void addRequirement(String id, String annotation){
    	logger.debug("Adding requirement: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addRequirement(id, annotation);
		} catch (Exception e){
			logger.error("Error adding requirement", e);
		}
    }

    /**********
     * Adds Preference Annotation in the KB   
     */
    public void addReference(String id, String annotation){
    	logger.debug("Adding reference: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addReference(id, annotation);
		} catch (Exception e){
			logger.error("Error adding reference", e);
		}
    }
 
    /**********
     * Adds Contextual Information Annotation in the KB   
     */
    public void addContextualInformation(String id, String annotation){
    	logger.debug("Adding contextual information: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addContextualInformation(id, annotation);
		} catch (Exception e){
			logger.error("Error adding contextual information", e);
		}
    }

    /**********
     * Adds Funtional Classification Annotation in the KB   
     */
    public void addFuntionalClassification(String id, String annotation){
    	logger.debug("Adding functional classification: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addFuntionalClassification(id, annotation);
		} catch (Exception e){
			logger.error("Error adding functional classification", e);
		}
    }

    /**********
     * Adds Non Functional Property Annotation in the KB   
     */
    public void addNonFunctionalProperty(String id, String annotation){
    	logger.debug("Adding non functional property: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addNonFunctionalProperty(id, annotation);
    		
		} catch (Exception e){
			logger.error("Error adding non functional property", e);
		}
    }

    /**********
     * Adds Input Metadata Annotation in the KB   
     */
    public void addInputMetadata(String id, String annotation){
    	logger.debug("Adding input metadata: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addInputMetadata(id, annotation);
    		//webN1.getWebN1ServiceHttpSoap11Endpoint().addMetadata(id, annotation);
    		
		} catch (Exception e){
			logger.error("Error adding input metadata", e);
		}
    }

    /**********
     * Adds Output Metadata Annotation in the KB   
     */
    public void addOutputMetadata(String id, String annotation){
    	logger.debug("Adding output metadata: "+ annotation + " to BPMN element id: " + id);
    	try {
    		ah.addOutputMetadata(id, annotation);
    		//webN1.getWebN1ServiceHttpSoap11Endpoint().addMetadata(id, annotation);
    		
		} catch (Exception e){
			logger.error("Error adding output metadata", e);
		}
    }

    /**********
     * Retrieves annotations from KB   
     */
    public List<String> retrieveAnnotationOf(String id, String type){
    	logger.debug("Retrieving annotation of type "+ type + " for element with id " + id);
    	try {
    		List<String> anns = Arrays.asList(ah.retrieveAnnotationOf(id, type));

    		return anns;
		} catch (Exception e){
			logger.error("Error retrieving annotations", e);
		}
		return new ArrayList<String>();
    }

    /**********
     * Retrieves services from KB   
     */
    public List<Service> retrieveServicesOf(String id){
    	logger.debug("Retrieving services for element with id " + id);
    	List<Service> services = null;
    	try {
    		services = convertToUIServiceArray(ah.retrieveServicesOf(id));    			
		} catch (Exception e){
			logger.error("Error retrieving services", e);
		}		
    	return services;
    }


	/**********
     * Deletes Annotation from KB   
     */
    public void deleteAnnotation(String id, String type, String annotation){
    	logger.debug("Delete annotation " + annotation + " of type " + type + " for element with id " + id);
    	try {
    		ah.deleteAnnotation(id, type, annotation);    		    		
		} catch (Exception e){
			logger.error("Error deleting annotation", e);
		}		
    }

    /**********
     * Checks on DTC if there are services   
     */
    public boolean invokeDTC(String processId, String taskId){
    	logger.debug("Invoke DTC with processId " + processId + " and taskId " + taskId);
    	
	    
	    DTComposerClient composer;
	    boolean result = false; 
	    
		try {
			composer = new DTComposerClient(CompelServerConfiguration.getConfiguration().getDTCEndpoint());
		    
			return composer.searchForActivityBindings(processId, taskId); 
		} catch (Exception e) {
			logger.error("Error invoking DTC", e);
		}
		return result;
    }
    
    /**********
     * Binds taskId-service
     */
    public void selectService(String idBPMN, String serviceUri){
    	logger.debug("Invoke select service with id " + idBPMN + ", serviceUri " + serviceUri);
    	try {
    		ah.selectService (idBPMN, serviceUri);    		
  	
		} catch (Exception e){
			logger.error("Error invoking select service", e);
		}		
    }
    
    /********************
     * Obtiene el modelo de Oryx en json => xml
     * @param modelS
     */
    public void receiveJson(String modelS){
		
    	//Obtenemos valor del fichero de propiedades
    	String fileDir=obtieneValorProp("postMethDirec");
    	
    	//Hacemos la invocacion post
    	HttpClient client = new HttpClient();
		BufferedReader br = null;
		//PostMethod method = new PostMethod("http://localhost:8088/oryx/bpmn2_0serialization");
		PostMethod method = new PostMethod(fileDir);
		method.addParameter("data", modelS);
		try{
			int statusCode = client.executeMethod(method);
			if(statusCode == HttpStatus.SC_NOT_IMPLEMENTED) { 
				method.getResponseBodyAsString(); } 
			else { 
				br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream())); 
				String readLine=br.readLine(); 
				//while(((readLine = br.readLine()) != null)) { 
				//}
				String resul = "{\""+readLine.substring(readLine.indexOf("xml"));
				
				//Obtenemos el XML MODIFICADO
				modifyXML(resul);
			}
		}
		catch(Exception e){

		}
    	
    }
    
    /********************
     * Obtiene el modelo de Oryx modificado(xml con las modificaciones que necesitamos)
     * @param modelS
     */
    public void modifyXML(String xml){
    	boolean req=false;
    	boolean resp=false;
    	String mOut="";
    	String mIn="";
    	String interface_def="";
    	String ioSpecification="";
    	
    	
    	/*Cambio xmlns*/
    	String xmlnsNew=obtieneValorProp("xmlnsNew");
    	xml=xml.replace(obtieneValorProp("xmlnsOld"), xmlnsNew);

    	//A�ado xmlns:xsd
    	String xsd=obtieneValorProp("xsd");
    	xml=xml.replace(obtieneValorProp("targetNamespace"),xsd);
    	
    	String xmlAux = xml;
    	//Obtenemos las tareas del xml => Para cada una de ellas 
    	while(xmlAux.indexOf("serviceTask") > -1){
    		xmlAux = xmlAux.substring(xmlAux.indexOf("serviceTask"));
    		String task= xmlAux.substring(xmlAux.indexOf("serviceTask"),xmlAux.indexOf("/>")+2);    				
    		xml=xml.replace(task, task.replace("webService", "##WebService"));
    		task= task.replace("webService", "##WebService");
    		xmlAux = xmlAux.substring(xmlAux.indexOf("id"));
    		xmlAux = xmlAux.substring(xmlAux.indexOf("\"")+1);
    		String sId = xmlAux.substring(0, xmlAux.indexOf("\"")-1);
    	
    		//A�ado import
    		String aImportar=obtieneValorProp("aImportar");

    		sId="20";
    		/*Obtenemos el WS_WSDL_URL:URL del WSDL del servicio seleccionado*/ 
			//LO ESTAMOS HACIENDO PRA EL ID DE UNA TAREA. 
    		Service service = getSelectedService(sId);

			if(service!=null){

    			/*Lo sustituimos en aImportar*/
    			String wsdlURL =service.getWSDLURL(); 
    			aImportar = aImportar.replace(obtieneValorProp("WS_WSDL_URL"), wsdlURL);
    	
    			/*Obtenemos el WS_WSDL_Namespace*/
    			String nS=getNamespace(wsdlURL);

    			/*Lo sustituimos en aImportar*/
    			if(nS != null){
    				aImportar = aImportar.replace(obtieneValorProp("WS_WSDL_Namespace"), nS);
    			}
    	
    			/*A�adimos el import*/
    			xml=xml.replace(obtieneValorProp("process"), aImportar);

    			//A�ado xmlns:%random%
    			String tag = generateString(new Random(), obtieneValorProp("cadena"), 10);
    			String nTag=obtieneValorProp("nTag");
    			nTag=nTag.replace("+tag+", tag);
    			nTag=nTag.replace("+nS+", nS);
    			xml=xml.replace(obtieneValorProp("targetNamespace"),nTag);

    			//Obtenemos ahora la operacion
    			String op=service.getOperation();

                /*ItemDefinition*/
    			String itemDef=obtieneValorProp("itemDef");
    			String itemDefResp=itemDef;
    			
    			/*MessageDefinition*/
    			String messageDef=obtieneValorProp("messageDef");
    			String messageDefResp=messageDef;
    			
    			/*Cambiamos el name*/
    			itemDef=itemDef.replace(obtieneValorProp("name"), op+obtieneValorProp("reqItem"));
				messageDef=messageDef.replace(obtieneValorProp("itemName"), obtieneValorProp("tns")+op+obtieneValorProp("reqItem"));
				messageDef=messageDef.replace(obtieneValorProp("messageName"), op+obtieneValorProp("reqMessage"));
				
    			itemDefResp=itemDefResp.replace(obtieneValorProp("name"), op+obtieneValorProp("resItem"));
				messageDefResp=messageDefResp.replace(obtieneValorProp("itemName"), obtieneValorProp("tns")+op+obtieneValorProp("resItem"));
				messageDefResp=messageDefResp.replace(obtieneValorProp("messageName"), op+obtieneValorProp("resMessage"));

    			/*Obtenemos el WS_WSDL_Namespace*/    			
    			try{
    				WSDLFactory fac = WSDLFactory.newInstance();
    				WSDLReader rd = fac.newWSDLReader();
    				Definition def = rd.readWSDL(null,wsdlURL);    				
    				Map ports = def.getPortTypes();    				
    				Iterator   iter = ports.values().iterator();

    				while(iter.hasNext())
    				{
    					PortType port = (PortType)iter.next();
   						List operationList = port.getOperations();   						
   						for(int i=0; i < operationList.size(); i++){
   							Operation oper = ((Operation) operationList.get(i));  //.getOperation();
							if(oper.getName().equals(op)){  //Obtenemos el input/output message de la operaci�n
								Input in=oper.getInput();
   								if(in!=null){
   									mIn=in.getMessage().getQName().toString();
									mIn=mIn.substring(mIn.indexOf("}")+1);
									itemDef=itemDef.replace(obtieneValorProp("qName"), tag+":"+mIn);
									req=true;
   								}   							

   								Output out= oper.getOutput();
   								if(out!=null){
   									mOut=out.getMessage().getQName().toString();
									mOut=mOut.substring(mOut.indexOf("}")+1);
									itemDefResp=itemDefResp.replace(obtieneValorProp("qName"), tag+":"+mOut);
									resp=true;
   								}
   								
   								if(resp){
   									/*Interface Definition*/
   									interface_def =getInterfaceDefinition(port,op, tag, true);

									/*Add ioSpecification*/
   									ioSpecification=getIoSpecification(sId,tag, mIn, mOut, true);
   								}else{
   									/*Interface Definition*/
   									interface_def =getInterfaceDefinition(port,op, tag, false);
 
   									/*Add ioSpecification*/
   									ioSpecification=getIoSpecification(sId,tag, mIn, mOut, false);
   								}
								xml=xml.replace(obtieneValorProp("processFin"),obtieneValorProp("processFin")+interface_def+"\\n");
								xml=xml.replace(obtieneValorProp("processFin"),obtieneValorProp("processFin")+ioSpecification+"\\n");

								if(resp){
									xml=xml.replace(obtieneValorProp("processFin"),obtieneValorProp("processFin")+messageDefResp+"\\n");
									xml=xml.replace(obtieneValorProp("processFin"),obtieneValorProp("processFin")+itemDefResp+"\\n");

									resp=false;
								}

								if(req){
									xml=xml.replace(obtieneValorProp("processFin"),obtieneValorProp("processFin")+messageDef+"\\n");
									xml=xml.replace(obtieneValorProp("processFin"),obtieneValorProp("processFin")+itemDef+"\\n");

									req=false;
								}
							}
   						}   						
    				}
    				/*Add Operation Reference*/
    				String newTask = task.replace("/>", " operationRef="+tag+":"+op+"/>");

    				xml=xml.replace(task, newTask);
    				
    			}catch(WSDLException e){
    				e.printStackTrace();
    			} 
    		}
			else {
				System.out.println("No hay servicios asociados a la tarea "+sId);
			}
    	}

    }
    
    /**********
     * getSelectedService of the KB: Obtiene el servicio seleccionado al hacer el bind(selectService)  
     */
    public Service getSelectedService(String id){
    	logger.debug("Invoke get selected service with id " + id);
    	Service s = null;
    	try {
    		//invoke
    		s = convertToUIService(ah.getSelectedService(id));
		} catch (Exception e){
			logger.debug("Error getting selected service ", e);
		}
		return s;
    }
    
    /********
     * Genera una cadena de caracteres aleatoria 
     * @param rng
     * @param characters: Caracteres a utilizar
     * @param length: Longitud de la cadena
     * @return
     */
    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    /**************
     * Obtiene valores del fichero de propiedades
     * @param propiedad: Propiedad del valor que se quiere obtener
     * @return
     */
    public String obtieneValorProp(String propiedad){       	
    	
    	Properties prop = new Properties();
    	InputStream is = null;
    	try{
    	  is=new FileInputStream("d:\\properties.txt");
    	  prop.load(is);
    	}catch(IOException ioe) {
    	  ioe.printStackTrace();
    	}
    	return(prop.getProperty(propiedad));
    }
    
    /***************
     * Obtiene el Target Namespace de un wsdl
     * @param wsdlURL
     * @return
     */
    public String getNamespace(String wsdlURL){
    	try{
    		WSDLFactory fac = WSDLFactory.newInstance();
    		WSDLReader rd = fac.newWSDLReader();
    		
    		Definition def = rd.readWSDL(null,wsdlURL);

    		return def.getTargetNamespace(); 
    				
    	}catch(WSDLException e){
    		e.printStackTrace();
    		return "";
    	} 
    }
    
    /***************
     * Obtiene la cadena del InterfaceDefinition, a anadir al xml(para cada serviceTask)
     * @param port
     * @param op
     * @param tag
     * @param resp
     * @return
     */
    public String getInterfaceDefinition(PortType port,String op, String tag, boolean resp){
			String interface_def = "";
			String portName=port.getQName().toString();
			
			if(resp)
				interface_def = obtieneValorProp("interfaceDef");
			else
				interface_def = obtieneValorProp("interfaceDefSO");
			interface_def=interface_def.replace(obtieneValorProp("interface_id"), portName.substring(portName.indexOf("}")+1));
			interface_def=interface_def.replace(obtieneValorProp("operation_id"), op+obtieneValorProp("operation"));
			interface_def=interface_def.replace(obtieneValorProp("operation_name"), op);
			interface_def=interface_def.replace(obtieneValorProp("operation_ref"), tag+":"+op);
			interface_def=interface_def.replace(obtieneValorProp("operation_input_message"), obtieneValorProp("tns")+op+obtieneValorProp("reqItem"));
			//Solo anado el output, si hay response
			if(resp)
				interface_def=interface_def.replace(obtieneValorProp("operation_output_message"), obtieneValorProp("tns")+op+obtieneValorProp("resItem"));
			
			return interface_def;
    }

    /***************
     * Obtiene la cadena del IoSpecification, a anadir al xml(para cada serviceTask)
     * @param port
     * @param op
     * @param tag
     * @param resp
     * @return
     */
    public String getIoSpecification(String sId,String tag,String mIn,String mOut, boolean resp){
    	String ioSpecification="";
		
    	if(resp)
    		ioSpecification=obtieneValorProp("ioSpecification");
    	else
    		ioSpecification=obtieneValorProp("ioSpecificationSO");
    	
    	ioSpecification=ioSpecification.replace(obtieneValorProp("task_input_id"), sId+obtieneValorProp("input"));
    	//Solo anado el output, si hay response
    	if(resp)
			ioSpecification=ioSpecification.replace(obtieneValorProp("task_output_id"), sId+obtieneValorProp("output"));
		ioSpecification=ioSpecification.replace(obtieneValorProp("task_input_item_ref"), tag+":"+mIn);
		if(resp)
			ioSpecification=ioSpecification.replace(obtieneValorProp("task_output_item_ref"), tag+":"+mOut);
		
		return ioSpecification;
    }
    
    
    private List<Service> convertToUIServiceArray(
			List<eu.atosresearch.seiplab.kb.types.Service> services) {
		List<Service> result = new ArrayList<Service>();
		for (eu.atosresearch.seiplab.kb.types.Service s: services)
			result.add (convertToUIService(s));
		
		return result;
	}
    
    private Service convertToUIService(
			eu.atosresearch.seiplab.kb.types.Service service) {
		Service s = new Service();
		
		s.setURI(service.getURI());
		s.setWSDLURL(service.getWSDLURL());
		s.setOperation(service.getOperation());
		s.setBound(service.isBound());
		
		return s;
	}
}
