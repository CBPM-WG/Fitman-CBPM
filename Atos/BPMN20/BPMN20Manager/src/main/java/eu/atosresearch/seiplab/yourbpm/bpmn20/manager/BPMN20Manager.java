/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.atosresearch.seiplab.yourbpm.bpmn20.manager;

import eu.atosresearch.seiplab.kb.businesslogic.AnnotationHandler;
import eu.atosresearch.seiplab.kb.types.Service;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Assignment;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.DataInput;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.DataInputAssociation;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.DataOutput;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.DataOutputAssociation;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Definitions;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Import;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.InputSet;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Interface;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.IoSpecification;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.ItemDefinition;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Message;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Operation;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.OutputSet;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.Process;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.ReceiveTask;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.SequenceFlow;
import eu.atosresearch.seiplab.yourbpm.bpmn.model.ServiceTask;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.ObjectFactory;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TAssignment;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TDataAssociation;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TDataInput;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TDataInputAssociation;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TDataOutput;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TDataOutputAssociation;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TDefinitions;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TEndEvent;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TExpression;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TFlowElement;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TFormalExpression;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TImport;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TInputOutputSpecification;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TInputSet;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TInterface;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TItemDefinition;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TMessage;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TOperation;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TOutputSet;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TProcess;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TReceiveTask;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TRootElement;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TSequenceFlow;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TServiceTask;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TStartEvent;
import eu.atosresearch.seiplab.yourbpm.bpmn.standard.TUserTask;
import eu.atosresearch.seiplab.yourbpm.bpmn.util.BpmnUtil;
import eu.atosresearch.seiplab.yourbpm.bpmn20.exceptions.IdElementException;
import eu.atosresearch.seiplab.yourbpm.bpmn20.exceptions.IdReferenceException;
import eu.atosresearch.seiplab.yourbpm.wsdl.WSDLManager;
import eu.atosresearch.seiplab.yourbpm.wsdl.param.WsdlParamManager;
import eu.atosresearch.seiplab.yourbpm.wsdl.param.WsdlParameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.wsdl.Definition;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
 /**
 *
 * @author mahdsip
 */
public class BPMN20Manager {

    
    private AnnotationHandler hndl;
    private String BPMN_XML;
    private Boolean stringIsPath;
    private Definitions definitions;
    //TODO remove old version, Standard is the whole version of the BPMN
    private ObjectFactory objectFactory;
    private TDefinitions definitionsStandard;
    public TDefinitions getDefinitionsStandard() {
		return definitionsStandard;
	}

	private List<String> idList;
    private WSDLManager wsdlMan;
    
    private JAXBContext contextStandard;
    private Unmarshaller umStandard;
    private Marshaller mStandard;
    
    private JAXBContext context;
    private Unmarshaller um;
    private Marshaller m;
    private Element bpmndi;
    private Element laneSet;
    private StringWriter finalDocumentWriter;
	private org.w3c.dom.Document initialDocument;
    private org.w3c.dom.Document finalDocument;
    private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
    private Map<String,String> idMap;
    
    private static String TARGET_NAME_SPACE = "http://www.bpmnwithactiviti.org";
    private static String TNS_NAME = "tns";
    private static String TARGET_NAME_SPACE_TNS = "http://www.bpmnwithactiviti.org";
    
    private boolean simpleAproximation=true;
    
    public BPMN20Manager(String ContentOrPathToFile) {
        try {
            BPMN_XML=ContentOrPathToFile;
            hndl = new AnnotationHandler();
            if (BPMN_XML.startsWith("<")) stringIsPath=false;
            else stringIsPath=true;
            
            objectFactory = new ObjectFactory();
            contextStandard = JAXBContext.newInstance(TDefinitions.class);
            umStandard = contextStandard.createUnmarshaller();
            mStandard = contextStandard.createMarshaller();
            
            context = JAXBContext.newInstance(Definitions.class);
            um = context.createUnmarshaller();
            m = context.createMarshaller();
            wsdlMan = new WSDLManager();
            idMap = new HashMap<String,String>();
            finalDocumentWriter = new StringWriter();
            definitions = new Definitions();
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            db = dbf.newDocumentBuilder();
            initialDocument = db.newDocument();
            finalDocument = db.newDocument();   
        } catch (WSDLException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
		}

    }

    public void addMessageStandard(TMessage mess){
        try {
            // TODO The IDs of the elements aren't.          

                    TMessage _mess = null;
                    
                    

                    for (Iterator<TItemDefinition> iterator = BpmnUtil.getListItemDefinition(this.definitionsStandard).iterator(); iterator.hasNext();) {
                    	TItemDefinition _item = iterator.next();
                    	this.getDefinitions().getTargetNamespace();
                        if (_item.getId().equals(mess.getItemRef().getLocalPart()))
                            _mess=mess;
					}
                    //TODO review the behavior, simply we have made the transformation.
//                    while (it.hasNext()){
//                            
//                        ItemDefinition _item = (ItemDefinition) it.next();
//                        this.getDefinitions().getTargetNamespace();
//                        if (_item.getId().equals(mess.getItemRef().getLocalPart()))
//                            _mess=mess;
//                    }
                    if (mess!=null){ 
                    	//TODO review that the mapping are correct.
                    	//JAXBElement<TMessage> jabxElement = new JAXBElement(mess.getItemRef(), TMessage.class, mess);
                    	JAXBElement<TMessage> jabxElement = objectFactory.createMessage(mess);
                        this.getDefinitionsStandard().getRootElement().add(jabxElement);
                    }
                    else throw new IdReferenceException("ItemRef must exist on bpmn document");
                    if (checkExistingId(mess.getId())) throw new IdElementException("ServiceTaskId already exists");
                    idList.add(mess.getId());
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IdReferenceException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void addMessage(Message mess){
        try {
            // TODO The IDs of the elements aren't.           

                    Message _mess = new Message();
                    Iterator it = this.getDefinitions().getItemDefinition().iterator();
                    while (it.hasNext()){
                            
                        ItemDefinition _item = (ItemDefinition) it.next();
                        this.getDefinitions().getTargetNamespace();
                        if (_item.getId().equals(mess.getItemRef().getLocalPart()))
                            _mess=mess;
                    }
                    if (mess!=null) this.getDefinitions().getMessage().add(mess);
                    else throw new IdReferenceException("ItemRef must exist on bpmn document");
                    if (checkExistingId(mess.getId())) throw new IdElementException("ServiceTaskId already exists");
                    idList.add(mess.getId());
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IdReferenceException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addItemDefinitionStandard(TItemDefinition item){
        try {
            if (checkExistingId(item.getId())) throw new IdElementException("ServiceTaskId already exists");
            idList.add(item.getId()); 
            //TODO review, how the jabxelement is instantiated.
            //JAXBElement<TItemDefinition> jabxElement = new JAXBElement(item.getStructureRef(), TItemDefinition.class, item);
            JAXBElement<TItemDefinition> jabxElement = objectFactory.createItemDefinition(item);
            this.getDefinitionsStandard().getRootElement().add(jabxElement);
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addItemDefinition(ItemDefinition item){
        try {
            if (checkExistingId(item.getId())) throw new IdElementException("ServiceTaskId already exists");
            idList.add(item.getId());        
            this.getDefinitions().getItemDefinition().add(item);
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public void addIOSpecification(eu.atosresearch.seiplab.yourbpm.bpmn.model.Process pr, IoSpecification io) {
        
        pr.setIoSpecification(io);
    }
    public void addIOSpecification(ServiceTask st, IoSpecification io){  
      st.setIoSpecification(io);
    }
    public void addInterface(Interface in){
        if (!checkExistingId(in.getId())) this.getDefinitions().getInterface().add(in);
        else try {
            throw new IdElementException("existing DataInput Id in the bpmn");
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        idList.add(in.getId());        
    }
    public void addSequenceFlow(SequenceFlow sf){
        
        if (!checkExistingId(sf.getId())) this.getDefinitions().getProcess().getSequenceFlows().add(sf);
        else try {
            throw new IdElementException("existing SequenceFlow Id in the bpmn");
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        idList.add(sf.getId());     
    }
    public void addDataInput(IoSpecification io,DataInput di){
        
        if (!checkExistingId(di.getId())) io.setDataInput(di);
        else try {
            throw new IdElementException("existing DataInput Id in the bpmn");
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        idList.add(di.getId());
    }
    public void addDataOutput(IoSpecification io,DataOutput _do){
                
        if (!checkExistingId(_do.getId())) io.setDataOutput(_do);
        else try {
            throw new IdElementException("existing DataOutput Id in the bpmn");
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        idList.add(_do.getId());
        
    }    
    public void addServiceTask(ServiceTask st){
        try {
            Process pr = this.getDefinitions().getProcess();
            if (pr==null) throw new IdElementException("Process id is not present");
            if (checkExistingId(st.getId())) throw new IdElementException("ServiceTaskId already exists");
            Iterator it = pr.getServiceTasks().iterator();
            while (it.hasNext()){
                try {
                    ServiceTask _st= (ServiceTask) it.next();
                    if (_st.getId().equals(st.getId())) throw new IdElementException("ServiceTask Id must be unique");
                    
                } catch (IdElementException ex) {
                    Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            pr.getServiceTasks().add(st);
            idList.add(st.getId());
        } catch (IdElementException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public void addNamespace(String nm){
         //TODO
     }
    public void addImport(Import imp){
         this.getDefinitions().getImport().add(imp);
     }
    
    public void addImportStandard(TImport imp){
        this.getDefinitionsStandard().getImport().add(imp);
    }
//UTILITIES
    public void marshallingStandard(){
	try {
		// output pretty printed
		mStandard.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		mStandard.marshal(definitionsStandard, finalDocumentWriter);
		
        } catch (JAXBException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  //UTILITIES
    public void marshalling(){
	try {
                m.marshal(definitions, finalDocument);
                
                DOMBuilder builder = new DOMBuilder();
                org.jdom.Document document = builder.build(finalDocument);
                
                List list = document.getRootElement().getChildren();
                list.add(getBpmndi());
                Iterator i = list.iterator();
 
                while (i.hasNext()){
                    Element el = (Element) i.next();
                    if (el.getName().equals("process")) {
                    	 //Oryx introduces always a LaneSet in the structure, but Activiti doesn't, so we need to control that it can be null.
                    	if (getLaneSet()!=null)	el.addContent(getLaneSet());	
                    }
                }
                XMLOutputter outputter = new XMLOutputter();
                 
                //If it is xml content convert to string, if not it is a pathToFile.
                if (stringIsPath) outputter.output(document, new FileOutputStream (BPMN_XML+"out.xml"));
                else outputter.output(document, finalDocumentWriter);
                DOMOutputter domOut = new DOMOutputter();
                finalDocument = domOut.output(document);
                
        } catch (JAXBException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
        	Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
	    public void unmarshallingStandard(){
	        try {
	            
	        	InputSource is = new InputSource();
	            if(stringIsPath){ //Its a path to file
	            	File file = new File(BPMN_XML);
	            	is = new InputSource((new FileInputStream(file)));
	            }
	            	
	            else{ //Its a string with XML
	            	is = new InputSource();
	                is.setCharacterStream(new StringReader(BPMN_XML));
	            }
	           
	            JAXBElement<TDefinitions> definitioElement = (JAXBElement<TDefinitions>) umStandard.unmarshal(is);
	            
	            definitionsStandard = definitioElement.getValue();
	            
	            //Modify the targetName since you deploy the tags for mapping the WSLD, the system gives an error 
	            definitionsStandard.setTargetNamespace(TARGET_NAME_SPACE);
	            
	            idList = new ArrayList();
	            fillIdListStandard(definitionsStandard);
	
	    
	    } catch (IOException ex) {
	        Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (JAXBException ex) {
	            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    
	private void fillIdListStandard(TDefinitions definitions) {
		
		//Oryx introduce an id in the definition, but Activiti doesn't, so we need to control that the id can be null.
		if (definitions.getId()!=null) {
			this.idList.add(definitions.getId());
	        this.idMap.put(definitions.getId(), definitions.getClass().getName());	
		}
       
        try {
        	Iterator<TItemDefinition> itDef = BpmnUtil.getListItemDefinition(definitions).iterator();
            TItemDefinition itemDef;
            while (itDef.hasNext()){
                itemDef = itDef.next();
                if (checkExistingId(itemDef.getId())) throw new IdElementException("Duplicate ID:"+itemDef.getId()+"on the source document");
                this.idList.add(itemDef.getId());
                this.idMap.put(itemDef.getId(), itemDef.getClass().getName());
            }
            Iterator<TInterface> itInt = BpmnUtil.getListInterface(definitions).iterator();
            TInterface interf;
            while (itInt.hasNext()){
                interf = itInt.next();
                if (checkExistingId(interf.getId())) throw new IdElementException("Duplicate ID:"+interf.getId()+"on the source document");                
                this.idList.add(interf.getId());
                this.idMap.put(interf.getId(), interf.getClass().getName());
            }
            Iterator<TMessage> itMe = BpmnUtil.getListMessage(definitions).iterator();
            TMessage messg;
            while (itMe.hasNext()){
                messg = itMe.next();
                if (checkExistingId(messg.getId())) throw new IdElementException("Duplicate ID:"+messg.getId()+"on the source document");
                this.idList.add(messg.getId());
                this.idMap.put(messg.getId(), messg.getClass().getName());
            }

            TProcess process= BpmnUtil.getProcess(definitions);
                if (checkExistingId(process.getId())) throw new IdElementException("Duplicate ID:"+process.getId()+"on the source document");
                this.idList.add(process.getId());
                this.idMap.put(process.getId(), process.getClass().getName());
                if (process.getIoSpecification()!=null)
                {
                	//DataInput
                	List<TDataInput> listDataInput = process.getIoSpecification().getDataInput();
                	for (Iterator<TDataInput> iterator = listDataInput.iterator(); iterator.hasNext();) {
						TDataInput tDataInput = iterator.next();
						if (checkExistingId(tDataInput.getId())) throw new IdElementException("Duplicate ID:"+tDataInput.getId()+"on the source document");
	                    this.idList.add(tDataInput.getId());
	                    this.idMap.put(tDataInput.getId(), process.getIoSpecification().getDataInput().getClass().getName());
					}
                	
                	//DataOutput
                	List<TDataOutput> listDataOutput = process.getIoSpecification().getDataOutput();
                	for (Iterator<TDataOutput> iterator = listDataOutput.iterator(); iterator.hasNext();) {
                		TDataOutput tDataOutput = iterator.next();
                		 if (checkExistingId(tDataOutput.getId())) throw new IdElementException("Duplicate ID:"+tDataOutput.getId()+"on the source document");
                         this.idList.add(tDataOutput.getId());            
                         this.idMap.put(tDataOutput.getId(), process.getIoSpecification().getDataOutput().getClass().getName());
					}
                	
                	
                   
                }
                
                List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
                for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
	            	TFlowElement jaxbElement = iterator.next().getValue();
					if (jaxbElement instanceof TReceiveTask) {
						TReceiveTask receiveTask = (TReceiveTask) jaxbElement;
						if (checkExistingId(receiveTask.getId())) throw new IdElementException("Duplicate ID:"+receiveTask.getId()+"on the source document");
	                    this.idList.add(receiveTask.getId());
	                    this.idMap.put(receiveTask.getId(), receiveTask.getClass().getName());
					}else if (jaxbElement instanceof TSequenceFlow){
						TSequenceFlow sequenceFlow = (TSequenceFlow) jaxbElement;
						 if (checkExistingId(sequenceFlow.getId())) throw new IdElementException("Duplicate ID:"+sequenceFlow.getId()+"on the source document");
		                 this.idList.add(sequenceFlow.getId());
		                 this.idMap.put(sequenceFlow.getId(), sequenceFlow.getClass().getName());

					}else if (jaxbElement instanceof TServiceTask){
						TServiceTask serviceTask = (TServiceTask) jaxbElement;
						if (checkExistingId(serviceTask.getId())) throw new IdElementException("Duplicate ID:"+serviceTask.getId()+"on the source document");                        
                        this.idList.add(serviceTask.getId());
                        this.idMap.put(serviceTask.getId(), serviceTask.getClass().getName());						
                        if (serviceTask.getIoSpecification()!=null){
                        	
                        	//DataInput
                        	List<TDataInput> listDataInput = serviceTask.getIoSpecification().getDataInput();
                        	for (Iterator<TDataInput> iteratorInput = listDataInput.iterator(); iterator.hasNext();) {
        						TDataInput tDataInput = iteratorInput.next();
        						if (checkExistingId(tDataInput.getId())) throw new IdElementException("Duplicate ID:"+tDataInput.getId()+"on the source document");
        	                    this.idList.add(tDataInput.getId());
        	                    this.idMap.put(tDataInput.getId(), serviceTask.getIoSpecification().getDataInput().getClass().getName());
        					}
                        	//DataOutput
                        	List<TDataOutput> listDataOutput = serviceTask.getIoSpecification().getDataOutput();
                        	for (Iterator<TDataOutput> iteratorOutput = listDataOutput.iterator(); iterator.hasNext();) {
                        		TDataOutput tDataOutput = iteratorOutput.next();
        						if (checkExistingId(tDataOutput.getId())) throw new IdElementException("Duplicate ID:"+tDataOutput.getId()+"on the source document");
        	                    this.idList.add(tDataOutput.getId());
        	                    this.idMap.put(tDataOutput.getId(), serviceTask.getIoSpecification().getDataOutput().getClass().getName());
        					}
                        }             
					}
            }
                
          } catch (IdElementException ex) {
                Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
            } 
    }
    
	
	public void completeAllTaskStandard(){
   		
		//Binding all the ServiceTask
		try {
			completeTaskStandard();
			//completeTaskIOStandard();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//TODO decide how to manage the exception in the BPMN2.0 module
			e.printStackTrace();
		}
    	
    	
    }
    
	
	public void completeTaskStandard() throws Exception{ 
			
		
		//We are going to review all the serviceTasks that have been bound in the Knowled Base.
		
			TProcess process= BpmnUtil.getProcess(definitionsStandard);
			 List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
	         for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
	        	TFlowElement jaxbElement = iterator.next().getValue();
	         	if (jaxbElement instanceof TServiceTask){
	         		try {
						TServiceTask serviceTask = (TServiceTask) jaxbElement;
						if (!checkExistingId(serviceTask.getId())) throw new IdElementException("Non existing Task id");
						String serviceName = serviceTask.getName();	
						//Substituir cuando funcione KB
				    	Service service =hndl.getSelectedService(serviceTask.getId());   
				    	int index = service.getOperation().indexOf("#OP");
				    	String operationName =  service.getOperation().substring(index+3,service.getOperation().length());
				    	  
				    	String wsdlUri= service.getWSDLURL();	
				    	 
				    	String in=null;
				    	String out=null;
				    	Definition definition = addWSDLStandard(wsdlUri);
				    	Iterator<javax.wsdl.PortType> itPortTypes = definition.getAllPortTypes().entrySet().iterator();
				    	String serviceNamePortType="";
				    	while (itPortTypes.hasNext()){
				    		   Map.Entry e = (Map.Entry)itPortTypes.next();
					    	   PortType port = (javax.wsdl.PortType)e.getValue();
					    	   serviceNamePortType = port.getQName().getLocalPart();
					    	   Iterator<javax.wsdl.Operation> itOperations = port.getOperations().iterator();
					    	   while (itOperations.hasNext()){
					    		   javax.wsdl.Operation op = (javax.wsdl.Operation) itOperations.next();
					    		   if (op.getName().equals(operationName)){
					    			   in=op.getInput().getName();
					    			   if (in==null) in=op.getInput().getMessage().getQName().getLocalPart();
					    			   out=op.getOutput().getName();
					    			   if (out==null) out=op.getOutput().getMessage().getQName().getLocalPart();
					    			   if (in==null|out==null) throw new IdElementException("I/O Operation in WSDL are null");
					    		   }
					    	   }
				    	 }
				    	 
				    	
				    	//References WSDL input/output message
				    	 TItemDefinition itemDefIn = objectFactory.createTItemDefinition();
				         itemDefIn.setId(in+"Item");
				         Map map = definition.getMessages();
				         QName ref = new QName(definition.getTargetNamespace(), in);
				         if (map.containsKey(ref)) {
				           javax.wsdl.Message me = (javax.wsdl.Message)map.get(ref); 
				           Iterator itParts = me.getParts().entrySet().iterator();
				           while (itParts.hasNext()){
				        	   Map.Entry e = (Map.Entry)itParts.next();
					       	   Part part = (Part)e.getValue();
					       	   itemDefIn.setStructureRef((part.getElementName()));
				           }
				        }
				         
				        TItemDefinition itemDefOut = objectFactory.createTItemDefinition();
				        itemDefOut.setId(out+"Item");
				        ref = new QName(definition.getTargetNamespace(), out);
				        if (map.containsKey(ref)) {
				           javax.wsdl.Message me = (javax.wsdl.Message)map.get(ref); 
				           Iterator itParts = me.getParts().entrySet().iterator();
				           while (itParts.hasNext()){
				        	   Map.Entry e = (Map.Entry)itParts.next();
					       	   Part part = (Part)e.getValue();
					       	   itemDefOut.setStructureRef((part.getElementName()));
				           }
				        } 
				        
				        
				        //Defines request message
				        TMessage messageIn = objectFactory.createTMessage();
				        messageIn.setId(in+"Message");
				        messageIn.setItemRef(new QName(TARGET_NAME_SPACE_TNS,itemDefIn.getId(),TNS_NAME));
				        
				        TMessage messageOut = new TMessage();
				        messageOut.setId(out+"Message");
				        messageOut.setItemRef(new QName(TARGET_NAME_SPACE_TNS,itemDefOut.getId(),TNS_NAME));
				        
				        //List<TInterface> listInterface = new ArrayList<TInterface>();
				        
				        //References WSDL port type
				        TInterface interf = objectFactory.createTInterface();
				        interf.setId(serviceNamePortType+"Interface");
				        interf.setName(serviceNamePortType+"Interface");
				        //interf.setId(servicePortType);
				        interf.setImplementationRef(new QName(definition.getTargetNamespace(), serviceNamePortType));
				        
				        TOperation op = objectFactory.createTOperation();
				        op.setId(operationName+"Operation");
				        op.setName(op.getId());
				        op.setImplementationRef(new QName(definition.getTargetNamespace(), operationName));
				        op.setInMessageRef(new QName(TARGET_NAME_SPACE_TNS,messageIn.getId(),TNS_NAME));
				        op.setOutMessageRef(new QName(TARGET_NAME_SPACE_TNS,messageOut.getId(),TNS_NAME));
				        
				        interf.getOperation().add(op);
				        
				        addItemDefinitionStandard(itemDefIn);
				        addItemDefinitionStandard(itemDefOut);           
				        addMessageStandard(messageIn);
				        addMessageStandard(messageOut);
				        
				        //listInterface.add(interf);
				        
				        //recover the input output data in the wsdl

				        Boolean existingInterface=false;
				        Iterator<TInterface> interfaceIt = BpmnUtil.getListInterface(definitionsStandard).iterator();
				        if (!interfaceIt.hasNext()) { 
				           JAXBElement<TInterface> jabxElement = objectFactory.createInterface(interf);
				           this.getDefinitionsStandard().getRootElement().add(jabxElement);
				        }
				        else
				        {
				          //if it exists the interface, it adds the operation and if not exist, it adds a new one.
				          while (interfaceIt.hasNext()){
				        	   TInterface intr=(TInterface) interfaceIt.next();     		   
				        	   if (interf.getId().equals(intr.getId())){
				        		   intr.getOperation().add(op);        	   
				        		   existingInterface=true;
				        	   }
				          }
				          if (!existingInterface) { 
				        		   JAXBElement<TInterface> jabxElement = objectFactory.createInterface(interf);
					               this.getDefinitionsStandard().getRootElement().add(jabxElement);
				          }
				        }
				        
				        		        
				        serviceTask.setName(serviceName);
				        serviceTask.setImplementation("##WebService");
				        serviceTask.setOperationRef(new QName(TARGET_NAME_SPACE_TNS,op.getId(),TNS_NAME));				        
				           
				           
			            TDataInput di = objectFactory.createTDataInput();
			            TInputSet is = objectFactory.createTInputSet();
			            TDataOutput dao = objectFactory.createTDataOutput();
				        TOutputSet os = objectFactory.createTOutputSet();
				        di.setId(in+"Input");
				        di.setItemSubjectRef(messageIn.getItemRef());
				        dao.setId(out+"Output");
				        dao.setItemSubjectRef(messageOut.getItemRef());
				           
				        TItemDefinition dataInputId = objectFactory.createTItemDefinition();
				        dataInputId.setId(di.getId());
				        JAXBElement<Object> jabxElementDataInputRef = objectFactory.createTInputSetDataInputRefs(dataInputId);
				        is.getDataInputRefs().add(jabxElementDataInputRef);
				           
				        TItemDefinition dataOutId = objectFactory.createTItemDefinition();
				        dataOutId.setId(dao.getId());
				        JAXBElement<Object> jabxElementDataOutRef = objectFactory.createTOutputSetDataOutputRefs(dataOutId);
				        os.getDataOutputRefs().add(jabxElementDataOutRef);
				           
				        TInputOutputSpecification io = objectFactory.createTInputOutputSpecification();
				        io.getDataInput().add(di);
				        io.getDataOutput().add(dao);
				        io.getInputSet().add(is);
				        io.getOutputSet().add(os);
				        serviceTask.setIoSpecification(io);
	
				        
						if (simpleAproximation){

							//********
							//Simple approximation
							
							//TODO We must unify the two WSDL Parser. 
							WsdlParamManager manager = new WsdlParamManager(wsdlUri);
							List<WsdlParameter> wsdlParamInputList = manager.getWsdlParameters(in);
							for (Iterator<WsdlParameter> iterator2 = wsdlParamInputList.iterator(); iterator2
									.hasNext();) {
								WsdlParameter wsdlParameterInput = iterator2.next();
							
								TDataInputAssociation diass= objectFactory.createTDataInputAssociation();
							
								//introduce the sourceRef associated to the input
								String sourceRefName = "dataInputOf" + wsdlParameterInput.getName();
								//we need to create the associated itemDefinition to this new field.
								TItemDefinition itemSourceRef = objectFactory.createTItemDefinition();
								itemSourceRef.setId(sourceRefName);
								addItemDefinitionStandard(itemSourceRef);
								TItemDefinition sourceRef = objectFactory.createTItemDefinition();
								sourceRef.setId(sourceRefName);
								JAXBElement<Object> jabxElementSourceRef = objectFactory.createTDataAssociationSourceRef(sourceRef);
								diass.getSourceRef().add(jabxElementSourceRef);
								
								//introduce the targetRef associated to the input
								String targetRefName = wsdlParameterInput.getName();
								TItemDefinition targetRefRef = objectFactory.createTItemDefinition();
								targetRefRef.setId(targetRefName);
								addItemDefinitionStandard(targetRefRef);
								TItemDefinition targetRef = objectFactory.createTItemDefinition();
								targetRef.setId(targetRefName);
								diass.setTargetRef(targetRef);
								
								//associate to the Input/output DataXXXAssociation
								serviceTask.getDataInputAssociation().add(diass);
							}
							
	
							List<WsdlParameter> wsdlParamOutputList = manager.getWsdlParameters(out);
							for (Iterator<WsdlParameter> iterator2 = wsdlParamOutputList.iterator(); iterator2.hasNext();) {
								WsdlParameter wsdlParameterOut = iterator2.next();
	
								TDataOutputAssociation doass= objectFactory.createTDataOutputAssociation();
								//Introduce all the reference for the Output mappings, according with the method of the WSDL 
								//introduce the sourceRef
								String sourceRefNameOut = wsdlParameterOut.getName();
								TItemDefinition itemSourceRefOut = objectFactory.createTItemDefinition();
								itemSourceRefOut.setId(sourceRefNameOut);
								addItemDefinitionStandard(itemSourceRefOut);
								TItemDefinition sourceRefOut = objectFactory.createTItemDefinition();
								sourceRefOut.setId(sourceRefNameOut);
								JAXBElement<Object> jabxElementSourceRefOut = objectFactory.createTDataAssociationSourceRef(sourceRefOut);
								doass.getSourceRef().add(jabxElementSourceRefOut);
								
								//introduce the targeRef associated to the 
				    	    	String targetRefOutName = "dataOutputOf"+op.getId();
				        		//we need to create the associated itemDefinition to this new field targetRefOut.
								TItemDefinition itemTargetRefOut = objectFactory.createTItemDefinition();
								itemTargetRefOut.setId(targetRefOutName);
								addItemDefinitionStandard(itemTargetRefOut);		    	    	
				    	    	TItemDefinition targetRefOut = objectFactory.createTItemDefinition();
								targetRefOut.setId(targetRefOutName);
				        		doass.setTargetRef(targetRefOut);
				        		
								//associate to the Input/output DataXXXAssociation
								serviceTask.getDataOutputAssociation().add(doass);
				        		
							}
							
			        		//End simple approximation	
			        		//*********
						
						}else{
						
							/*complex approximation
							 * 
							 * */
							
							//TODO We must unify the two WSDL Parser. 
							WsdlParamManager manager = new WsdlParamManager(wsdlUri);
							List<WsdlParameter> wsdlParamInputList = manager.getWsdlParameters(in);
							for (Iterator<WsdlParameter> iterator2 = wsdlParamInputList.iterator(); iterator2
									.hasNext();) {
								WsdlParameter wsdlParameterInput = iterator2.next();
							
								TDataInputAssociation diass= objectFactory.createTDataInputAssociation();
		  
								//Fill the dataInputAssociation.
								//introduce the sourceRef associated to the input
								String sourceRefName = "dataInputOf" + serviceTask.getId() + wsdlParameterInput.getName();
								//we need to create the associated itemDefinition to this new field.
								TItemDefinition itemSourceRef = objectFactory.createTItemDefinition();
								itemSourceRef.setId(sourceRefName);
								addItemDefinitionStandard(itemSourceRef);
								TItemDefinition sourceRef = objectFactory.createTItemDefinition();
								sourceRef.setId(sourceRefName);
								JAXBElement<Object> jabxElementSourceRef = objectFactory.createTDataAssociationSourceRef(sourceRef);
								diass.getSourceRef().add(jabxElementSourceRef);
				        		
								
								
								//introduce the targetRef associated to the input
								TItemDefinition targetRef = objectFactory.createTItemDefinition();
								targetRef.setId(serviceTask.getIoSpecification().getDataInput().get(0).getId());
								diass.setTargetRef(targetRef);
								
								//create the Assigment associated to the dataInputAssociation
								TAssignment ass= objectFactory.createTAssignment();
								
		
								//Introduce all the reference for the Inputs mappings, according with the method of the WSDL 
								//it is the internal variable to associate to the services 
								TExpression expform = objectFactory.createTExpression();
								expform.getContent().add("${Inputparameter}"+sourceRefName);
				    	    	ass.setFrom(expform);
				    	    	//it is the mapping with the attribute operation in the WSDL
				    	    	TExpression expto = objectFactory.createTExpression();
				        		StringBuffer contentNameIn = new StringBuffer();
				        		contentNameIn.append("${").append(dataInputId.getId()).append(".").append(wsdlParameterInput.getName()).append("}");
				    	    	expto.getContent().add(contentNameIn.toString());
				    	    	ass.setTo(expto);
				    	    	
				    	    	diass.getAssignment().add(ass);
				    	    	
								//associate to the Input DataInputAssociation
								serviceTask.getDataInputAssociation().add(diass);
							
							}
							
							List<WsdlParameter> wsdlParamOutputList = manager.getWsdlParameters(out);
							for (Iterator<WsdlParameter> iterator2 = wsdlParamOutputList.iterator(); iterator2.hasNext();) {
								WsdlParameter wsdlParameterOut = iterator2.next();
	
								TDataOutputAssociation doass= objectFactory.createTDataOutputAssociation();
								
				    	    	//Introduce all the reference for the Output mappings, according with the method of the WSDL 
								//introduce the targeRef associated to the 
				    	    	String targetRefOutName = "dataOutputOf"+serviceTask.getId();
				        		//we need to create the associated itemDefinition to this new field targetRefOut.
								TItemDefinition itemTargetRefOut = objectFactory.createTItemDefinition();
								itemTargetRefOut.setId(targetRefOutName);
								addItemDefinitionStandard(itemTargetRefOut);		    	    	
				    	    	TItemDefinition targetRefOut = objectFactory.createTItemDefinition();
								targetRefOut.setId(targetRefOutName);
				        		doass.setTargetRef(targetRefOut);
		
				        		
				        		
				        		//introduce the Transformation associated to the output
				        		TFormalExpression formalExpresion = objectFactory.createTFormalExpression();
				        		StringBuffer contentNameOut = new StringBuffer();
				        		contentNameOut.append("${").append(dataOutId.getId()).append(".").append(wsdlParameterOut.getName()).append("}");
								formalExpresion.getContent().add(contentNameOut.toString());
								doass.setTransformation(formalExpresion);
								
								//associate to the Input DataOutputAssociation
								serviceTask.getDataOutputAssociation().add(doass);
							}
							/*
							//end complex approximation
							 * */
						}
					
		         	} catch (IdElementException e) {
		    			//Indicate that this serviceTask has not been introduced in the previous step, I/O operation in WSDL are null.....
		    			Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, e);
		    		} 	
	         	}
	         }
	    }  
	

	
	private void completeTaskIOStandard() {
		
		TProcess process= BpmnUtil.getProcess(definitionsStandard);
		TStartEvent startEvent = BpmnUtil.getStartEvent(process);
		TEndEvent endEvent = BpmnUtil.getEndEvent(process);
		List<TSequenceFlow> listSequenceFlow = BpmnUtil.getListSequenceFlow(process);
		
		List<JAXBElement<? extends TFlowElement>> listFlowElements = process.getFlowElement();
        for (Iterator<JAXBElement<? extends TFlowElement>> iterator = listFlowElements.iterator(); iterator.hasNext();) {
       	TFlowElement jaxbElement = iterator.next().getValue();
        	if (jaxbElement instanceof TServiceTask){
        		try {
					TServiceTask serviceTask = (TServiceTask) jaxbElement;
					//we only complete TasIO for the serviceTask that has been completed
					if (checkExistingId(serviceTask.getId())){
						
						
				        for (Iterator<TSequenceFlow> itSequenceFlow = listSequenceFlow.iterator(); itSequenceFlow.hasNext();) {
				        	TSequenceFlow seq = itSequenceFlow.next();
				        	
							String idSource = ((TFlowElement) seq.getSourceRef()).getId();
							String idTarget = ((TFlowElement) seq.getTargetRef()).getId();
									

							// If the Flow involves the start event and the serviceTask is the Target
							if ((startEvent.getId().equals(idSource))&(serviceTask.getId().equals(idTarget))){
								//TODO We are going to consider that there is only a element of DataInput and one of the Assigment
								TExpression expFrom = objectFactory.createTExpression();
								//expFrom.setId("${ProcessInput}");
								expFrom.getContent().add("${ProcessInput}");
								serviceTask.getDataInputAssociation().get(0).getAssignment().get(0).setFrom(expFrom);
								TExpression expto = objectFactory.createTExpression();
								//expto.setId("${Inputparameter}"+idSource);
								expto.getContent().add("${Inputparameter}"+idSource);
								serviceTask.getDataInputAssociation().get(0).getAssignment().get(0).setTo(expto);
								TItemDefinition sourceRef = objectFactory.createTItemDefinition();
								sourceRef.setId("ProcessInput");
								JAXBElement<Object> jabxElementSourceRef = objectFactory.createTDataAssociationSourceRef(sourceRef);
								serviceTask.getDataInputAssociation().get(0).getSourceRef().add(jabxElementSourceRef);

							}//TODO Only supports 1 EndEvent.
							else if (endEvent.getId().equals(idTarget)&(serviceTask.getId().equals(idSource))){
										//TODO We consider that there is only an element of the DataOutput
										TFormalExpression formalExpresion = objectFactory.createTFormalExpression();
										formalExpresion.getContent().add("${Outparameter}"+idSource);
										serviceTask.getDataOutputAssociation().get(0).setTransformation(formalExpresion);	
										TItemDefinition targetRefOut = objectFactory.createTItemDefinition();
										targetRefOut.setId("ProcessOutput");
										serviceTask.getDataOutputAssociation().get(0).setTargetRef(targetRefOut);
							}else {	
							  		if (serviceTask.getId().equals(idSource)){
							  			for (Iterator<JAXBElement<? extends TFlowElement>> servIt2 = listFlowElements.iterator(); iterator.hasNext();) {
							  		       	TFlowElement jaxbElement2 = servIt2.next().getValue();
							  		        	if (jaxbElement2 instanceof TServiceTask){
							  		        		TServiceTask targetTask = (TServiceTask) jaxbElement2;
							  		        		if (targetTask.getId().equals(idTarget)){
							  		        			JAXBElement<Object> jabxElementSourceRef = objectFactory.createTDataAssociationSourceRef(serviceTask.getIoSpecification().getDataOutput().get(0).getId());
							  		        			targetTask.getDataInputAssociation().get(0).getSourceRef().add(jabxElementSourceRef);			    			
														
							  		        			TItemDefinition targetRefOut = objectFactory.createTItemDefinition();
														targetRefOut.setId(targetTask.getIoSpecification().getDataInput().get(0).getId());
							  		        			serviceTask.getDataOutputAssociation().get(0).setTargetRef(targetRefOut);
										    			
							  		        			//TODO DTC Inference 
														TExpression expFrom = objectFactory.createTExpression();
														expFrom.getContent().add("${Outputparameter}"+idSource);
										    	    	targetTask.getDataInputAssociation().get(0).getAssignment().get(0).setFrom(expFrom);
										    	    	
										    	    	TExpression expto = objectFactory.createTExpression();
														expto.getContent().add("${Inputparameter}"+idTarget);
										    	    	targetTask.getDataInputAssociation().get(0).getAssignment().get(0).setTo(expto);
										    	    	
										    	    	TFormalExpression formalExpresion = objectFactory.createTFormalExpression();
														formalExpresion.getContent().add("${Outputparameter}"+idSource);
										    	    	serviceTask.getDataOutputAssociation().get(0).setTransformation(formalExpresion);
										    		}
							  		        		
							  		        	}
							  				}
							  			
								    	}

								    }   	
								
				
				        	}
		
						}
	         	} catch (Exception e) {
	    			//The process continuous with the following element.
	    			Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, e);
	    		}
        	}
        }
					
					
					

}
	
	
	
	
    public void unmarshalling(){
            try {
                SAXBuilder builder = new SAXBuilder();
                org.jdom.Document doc = new org.jdom.Document();
                if(stringIsPath){ //Its a path to file
                	doc = builder.build(BPMN_XML);
                }
                	
                else{ //Its a string with XML
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(BPMN_XML));
                    doc = builder.build(is);
                }
                DOMOutputter output = new DOMOutputter();
                initialDocument = output.output(doc);
                
                //Separating DSI details from rest of the document
                List list = doc.getRootElement().getChildren();
                Iterator it = list.iterator();
                while (it.hasNext()){
                    Element el = (Element) it.next();
                    if (el.getName().equals("BPMNDiagram")) {
                        setBpmndi(el);
                        it.remove();
                    }
                    if (el.getName().equals("process")){
                        List list2 = el.getChildren();
                        Iterator it2 = list2.iterator();
                        while (it2.hasNext()){
                            Element el2 = (Element) it2.next();
                            if (el2.getName().equals("laneSet")){
                                setLaneSet(el2);
                                it2.remove();
                            }                        
                        }
                    }
                }
//                XMLOutputter outputter = new XMLOutputter();
//                outputter.output(doc, new FileOutputStream ("/home/mahdsip/makeadoJDOM.xml"));
// Paso el documento a DOM para hacer el Unmarshalling                
      
                org.w3c.dom.Document dom = output.output(doc);


                // Prepare the output file
//                Source source = new DOMSource(dom);
//                File file = new File("/home/mahdsip/makeadoDOM.xml");
//                Result result = new StreamResult(file);

                // Write the DOM document to the file
//                Transformer xformer = TransformerFactory.newInstance().newTransformer();
//                xformer.transform(source, result);
                
                definitions = (Definitions)um.unmarshal(dom.getDocumentElement());
 //               definitions = (Definitions) um.unmarshal(new FileReader("/home/mahdsip/bpmn20Bis.xml"));
                idList = new ArrayList();
                fillIdList(definitions);
//            } catch (TransformerException ex) {
//            Logger.getLogger(BPMNManager.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (JDOMException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
                Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


	private void fillIdList(Definitions definitions) {
        //Oryx introduce an id in the definition, but Activiti doesn't, so we need to control that the id can be null.
		if (definitions.getId()!=null) {
			this.idList.add(definitions.getId());
	        this.idMap.put(definitions.getId(), definitions.getClass().getName());	
		}
        Iterator it = this.getDefinitions().getItemDefinition().iterator();
        ItemDefinition itemDef;
        try {
            while (it.hasNext()){
                itemDef = (ItemDefinition)it.next();
                if (checkExistingId(itemDef.getId())) throw new IdElementException("Duplicate ID:"+itemDef.getId()+"on the source document");
                this.idList.add(itemDef.getId());
                this.idMap.put(itemDef.getId(), itemDef.getClass().getName());
            }
            it = this.getDefinitions().getInterface().iterator();
            Interface interf;
            while (it.hasNext()){
                interf = (Interface)it.next();
                if (checkExistingId(interf.getId())) throw new IdElementException("Duplicate ID:"+interf.getId()+"on the source document");                
                this.idList.add(interf.getId());
                this.idMap.put(interf.getId(), interf.getClass().getName());
            }
            it = this.getDefinitions().getMessage().iterator();
            Message messg;
            while (it.hasNext()){
                messg = (Message)it.next();
                if (checkExistingId(messg.getId())) throw new IdElementException("Duplicate ID:"+messg.getId()+"on the source document");
                this.idList.add(messg.getId());
                this.idMap.put(messg.getId(), messg.getClass().getName());
            }
    //        it = this.getDefinitions().getProcess().iterator();
            Process process=this.getDefinitions().getProcess();
    //        while (it.hasNext()){
    //            item4 = (Process)it.next();
                if (checkExistingId(process.getId())) throw new IdElementException("Duplicate ID:"+process.getId()+"on the source document");
                this.idList.add(process.getId());
                this.idMap.put(process.getId(), process.getClass().getName());
                if (process.getIoSpecification()!=null)
                {
                    if (checkExistingId(process.getIoSpecification().getDataInput().getId())) throw new IdElementException("Duplicate ID:"+process.getIoSpecification().getDataInput().getId()+"on the source document");
                    if (checkExistingId(process.getIoSpecification().getDataOutput().getId())) throw new IdElementException("Duplicate ID:"+process.getIoSpecification().getDataOutput().getId()+"on the source document");
                    this.idList.add(process.getIoSpecification().getDataInput().getId());
                    this.idMap.put(process.getIoSpecification().getDataInput().getId(), process.getIoSpecification().getDataInput().getClass().getName());
                    this.idList.add(process.getIoSpecification().getDataOutput().getId());            
                    this.idMap.put(process.getIoSpecification().getDataOutput().getId(), process.getIoSpecification().getDataOutput().getClass().getName());
                }
                Iterator it2 = process.getReceiveTasks().iterator();
                ReceiveTask receiveTask;
                while (it2.hasNext()){
                    receiveTask = (ReceiveTask)it2.next();
                    if (checkExistingId(receiveTask.getId())) throw new IdElementException("Duplicate ID:"+receiveTask.getId()+"on the source document");
                    this.idList.add(receiveTask.getId());
                    this.idMap.put(receiveTask.getId(), receiveTask.getClass().getName());
                }
                it2 = process.getSequenceFlows().iterator();
                SequenceFlow sequenceFlow;
                while (it2.hasNext()){
                    sequenceFlow = (SequenceFlow)it2.next();
                    if (checkExistingId(sequenceFlow.getId())) throw new IdElementException("Duplicate ID:"+sequenceFlow.getId()+"on the source document");
                    this.idList.add(sequenceFlow.getId());
                    this.idMap.put(sequenceFlow.getId(), sequenceFlow.getClass().getName());
                }
                it2 = process.getServiceTasks().iterator();
                ServiceTask serviceTask;
                    while (it2.hasNext()){
                        serviceTask = (ServiceTask)it2.next();
                if (checkExistingId(serviceTask.getId())) throw new IdElementException("Duplicate ID:"+serviceTask.getId()+"on the source document");                        
                        this.idList.add(serviceTask.getId());
                        this.idMap.put(serviceTask.getId(), serviceTask.getClass().getName());
                        if (serviceTask.getIoSpecification()!=null){
                            if (checkExistingId(serviceTask.getIoSpecification().getDataInput().getId())) throw new IdElementException("Duplicate ID:"+serviceTask.getIoSpecification().getDataInput().getId()+"on the source document");
                            if (checkExistingId(serviceTask.getIoSpecification().getDataOutput().getId())) throw new IdElementException("Duplicate ID:"+serviceTask.getIoSpecification().getDataOutput().getId()+"on the source document");                            
                            this.idList.add(serviceTask.getIoSpecification().getDataInput().getId());
                            this.idMap.put(serviceTask.getIoSpecification().getDataInput().getId(), serviceTask.getIoSpecification().getDataInput().getClass().getName());
                            this.idList.add(serviceTask.getIoSpecification().getDataOutput().getId());
                            this.idMap.put(serviceTask.getIoSpecification().getDataOutput().getId(), serviceTask.getIoSpecification().getDataOutput().getClass().getName());
                        }                    
                    }            
          } catch (IdElementException ex) {
                Logger.getLogger(BPMN20Manager.class.getName()).log(Level.SEVERE, null, ex);
            } 
    }
    public boolean checkExistingId(String id){
         Integer i=0;
         while (!(i==idList.size())){
             if (idList.get(i).equals(id)) return true;
             i++;
         }
         return false;
     }
    
 
    public Definition addWSDL(String location){   
        Definition definition = this.wsdlMan.addWSDL(location);
        Import _import= new Import();
        _import.setImportType("http://schemas.xmlsoap.org/wsdl/");
        _import.setLocation(location);
        if (definition!=null)_import.setNamespace(definition.getTargetNamespace());
        this.addImport(_import);
        return definition;
    }
    
    public Definition addWSDLStandard(String location){   
        Definition definition = this.wsdlMan.addWSDL(location);
        
        TImport _import= objectFactory.createTImport();
        _import.setImportType("http://schemas.xmlsoap.org/wsdl/");
        _import.setLocation(location);
        if (definition!=null)_import.setNamespace(definition.getTargetNamespace());
        this.addImportStandard(_import);
        return definition;
    }
  
    public void completeAllTask(){
    	//unmarshalling();
    	Iterator it = definitions.getProcess().getServiceTasks().iterator();
    	ServiceTask st = new ServiceTask();
    	while (it.hasNext()){
    		st = (ServiceTask) it.next();
    		completeTask(st.getId());
    	}
    	Iterator it2 = definitions.getProcess().getServiceTasks().iterator();
    	while (it2.hasNext()){
    		st = (ServiceTask) it2.next();
    		completeTaskIO(st);
    	}
    }
    

    public void completeTask(String idTask){ 
		try {
	    	if (!checkExistingId(idTask)) throw new IdElementException("Non existing Task id");
	
	    	Iterator servIt = this.getDefinitions().getProcess().getServiceTasks().iterator();	    	
	    	ServiceTask st = (ServiceTask)servIt.next();
	    	while (!idTask.equals(st.getId())){
	    		st = (ServiceTask)servIt.next();
	    	}
	    	   
	    	   String serviceName = st.getName();
	//Substituir cuando funcione KB
	    	   Service service =hndl.getSelectedService(idTask);   
	    	   int index = service.getOperation().indexOf("#OP");
	    	   String operationName =  service.getOperation().substring(index+3,service.getOperation().length());
	    	   
	    	   String wsdlUri= service.getWSDLURL();
//	    	   String operationName=new String();
//	    	   String wsdlUri= new String();
//	    	   if (idTask.equals("oryx_9D965EDB-CFE0-429A-B106-AAEA9A92C42D")) {
//	    		   operationName="Book";
//	    		    wsdlUri="/home/a512471/workspaces/eclipse/FIWARE/MockServices/HostelWorld.wsdl";
//	    	   }
//	    	   else {
//	    		   operationName="MakeReservation";
//	    		   wsdlUri="/home/a512471/workspaces/eclipse/FIWARE/MockServices/FirstHotel.wsdl";	    		   
//	    	   }
//	    	   String wsdlUri="/home/mahdsip/workspaces/maven/BPMNManagerService/service/src/main/resources/imath.wsdl";
//	       	   String wsdlUri = Thread.currentThread().getContextClassLoader().getResource("bpmn20.input.xml").getFile();
	    	   String in=null;
	    	   String out=null;
	    	   Definition definition = addWSDL(wsdlUri);
	    	   Iterator<javax.wsdl.PortType> itPortTypes = definition.getAllPortTypes().entrySet().iterator();
	    	   while (itPortTypes.hasNext()){
	    		   Map.Entry e = (Map.Entry)itPortTypes.next();
		    	   PortType port = (javax.wsdl.PortType)e.getValue();
		    	   Iterator<javax.wsdl.Operation> itOperations = port.getOperations().iterator();
		    	   while (itOperations.hasNext()){
		    		   javax.wsdl.Operation op = (javax.wsdl.Operation) itOperations.next();
		    		   if (op.getName().equals(operationName)){
		    			   in=op.getInput().getName();
		    			   if (in==null) in=op.getInput().getMessage().getQName().getLocalPart();
		    			   out=op.getOutput().getName();
		    			   if (out==null) out=op.getOutput().getMessage().getQName().getLocalPart();
		    			   if (in==null|out==null) throw new IdElementException("I/O Operation in WSDL are null");
		    		   }
		    	   }
	    	   }

	           ItemDefinition itemDefIn = new ItemDefinition();
	           itemDefIn.setId(in+"Item");
	           Map map = definition.getMessages();
	           QName ref = new QName(definition.getTargetNamespace(), in);
	           if (map.containsKey(ref)) {
	        	   javax.wsdl.Message me = (javax.wsdl.Message)map.get(ref); 
	        	   Iterator itParts = me.getParts().entrySet().iterator();
	        	   while (itParts.hasNext()){
	        		   Map.Entry e = (Map.Entry)itParts.next();
		        	   Part part = (Part)e.getValue();
		        	   itemDefIn.setStructureRef((part.getElementName()));
	        	   }
	        	}
	           
	           
	           ItemDefinition itemDefOut = new ItemDefinition();
	           itemDefOut.setId(out+"Item");
	           ref = new QName(definition.getTargetNamespace(), out);
	           if (map.containsKey(ref)) {
	        	   javax.wsdl.Message me = (javax.wsdl.Message)map.get(ref); 
	        	   Iterator itParts = me.getParts().entrySet().iterator();
	        	   while (itParts.hasNext()){
	        		   Map.Entry e = (Map.Entry)itParts.next();
		        	   Part part = (Part)e.getValue();
		        	   itemDefOut.setStructureRef((part.getElementName()));
	        	   }
	        	}      

	         
	           
	           Message messageIn = new Message();
	           messageIn.setId(in+"Message");
	           messageIn.setItemRef(itemDefIn.getId());
	
	           
	           Message messageOut = new Message();
	           messageOut.setId(out+"Message");
	           messageOut.setItemRef(itemDefOut.getId());
	           
	           Interface interf = new Interface();
	           interf.setName(operationName+"Interface");
	           interf.setId(interf.getName());
	           
	
	           Operation op = new Operation();
	           op.setName(operationName);
	           op.setImplementationRef(new QName(definition.getTargetNamespace(), operationName));
	           op.setInMessageRef(messageIn.getId());
	           op.setOutMessageRef(messageOut.getId());
	           
	           interf.getOperation().add(op);
	 
	           addItemDefinition(itemDefIn);
	           addItemDefinition(itemDefOut);           
	           addMessage(messageIn);
	           addMessage(messageOut);
	           Interface intr= new Interface();
	           Boolean existingInterface=false;
	           Iterator interfaceIt = definitions.getInterface().iterator();
	           if (!interfaceIt.hasNext()) definitions.getInterface().add(interf);
	           else
	           {
	        	   //Si existe la interfaz le añade la operation y si no existe la añade nueva.
	        	   while (interfaceIt.hasNext()){
	        		   intr=(Interface) interfaceIt.next();     		   
	        		   if (interf.getId().equals(intr.getId())){
	        			   intr.getOperation().add(op);        	   
	        			   existingInterface=true;
	        		   }
	        	   }
	        	   if (!existingInterface) definitions.getInterface().add(interf);
	           }
	           Iterator serviceIt = this.definitions.getProcess().getServiceTasks().iterator();
	           
	           ServiceTask serviceTask;	           
        	   serviceTask = (ServiceTask) serviceIt.next();
	           while (!idTask.equals(serviceTask.getId())){
	        	   serviceTask = (ServiceTask) serviceIt.next();
	           }
	           
	           serviceTask.setName(serviceName);
	           serviceTask.setImplementation("##WebService");
	           serviceTask.setOperationRef(operationName);
	           
	           
	           DataInput di = new DataInput();
	           InputSet is = new InputSet();
	           DataOutput dao = new DataOutput();
	           OutputSet os = new OutputSet();
	           di.setId(in+"Input");
	           di.setItemSubjectRef(itemDefIn.getId());
	           dao.setId(out+"Output");
	           dao.setItemSubjectRef(itemDefOut.getId());
	           is.setDataInputRefs(di.getId());
	           os.setDataOutputRefs(dao.getId());
	           IoSpecification io = new IoSpecification();           
	           io.setDataInput(di);
	           io.setDataOutput(dao);
	           io.setInputSet(is);
	           io.setOutputSet(os);
	           serviceTask.setIoSpecification(io);

	   		DataInputAssociation diass= new DataInputAssociation();
			DataOutputAssociation doass= new DataOutputAssociation();
			diass.setTargetRef(st.getIoSpecification().getDataInput().getId());
	    	st.setDataInputAssociation(diass);
			st.setDataOutputAssociation(doass);
			Assignment ass= new Assignment();
	    	st.getDataInputAssociation().setAssignment(ass);
	           
			} catch (IdElementException e) {
				e.printStackTrace();
			}           
           
    }    
    
    //TODO To be removed
    private String recoverNameInput(String inputNameMessage){
    	
    	if (inputNameMessage.equals("findCustomerAddressInput")) {
			return "customerName";
		} else if(inputNameMessage.equals("isAvailableInput")){
			return "idReference";
		}else if(inputNameMessage.equals("isAvailable")){
			return "idReference";
		}
		else 	return "";
    }

    //TODO To be removed
    private String recoverNameOutput(String outputNameMessage){
    	
    	if (outputNameMessage.equals("findCustomerAddressResponseOutput")) {
			return "address";
		} else if(outputNameMessage.equals("isAvailableResponseOutput")){
			return "isAvailableReturn";
		}else if(outputNameMessage.equals("isAvailableResponse")){
			return "isAvailableReturn";
		}
		else 	return "";
    }

    private void completeTaskIO(ServiceTask st) {
	
		Iterator<SequenceFlow> itFlows = definitions.getProcess().getSequenceFlows().iterator();


		
		while (itFlows.hasNext()){
			SequenceFlow seq = itFlows.next();
			String idSource = seq.getSourceRef();
			String idTarget = seq.getTargetRef();
			ServiceTask targetTask=new ServiceTask();

	    	// If the Flow involves the start event and the serviceTask is the Target
			if ((definitions.getProcess().getStartEvent().getId().equals(idSource))&(st.getId().equals(idTarget))){
		    	st.getDataInputAssociation().getAssignment().setFrom("${ProcessInput}");
		    	st.getDataInputAssociation().getAssignment().setTo("${Inputparameter}"+idSource);
				st.getDataInputAssociation().setSourceRef("ProcessInput");
			}//TODO Only supports 1 EndEvent.
			else if ((definitions.getProcess().getEndEvent().getId().equals(idTarget))&(st.getId().equals(idSource))){
		    	st.getDataOutputAssociation().setTransformation("${Outparameter}"+idSource);	
				st.getDataOutputAssociation().setTargetRef("ProcessOutput");
			}
			else {	
		    		if (st.getId().equals(idSource)){
						Iterator servIt2 = this.getDefinitions().getProcess().getServiceTasks().iterator();
						while (servIt2.hasNext()){
				    		targetTask = (ServiceTask)servIt2.next();
				    		if (targetTask.getId().equals(idTarget)){
				    			targetTask.getDataInputAssociation().setSourceRef(st.getIoSpecification().getDataOutput().getId());			    			
				    			st.getDataOutputAssociation().setTargetRef(targetTask.getIoSpecification().getDataInput().getId());
				    			//TODO DTC Inference 
				    	    	targetTask.getDataInputAssociation().getAssignment().setFrom("${Outputparameter}"+idSource);
				    	    	targetTask.getDataInputAssociation().getAssignment().setTo("${Inputparameter}"+idTarget);
				    	    	st.getDataOutputAssociation().setTransformation("${Outputparameter}"+idSource);
				    		}
						}
		    		}

		    	}   	
		}
}


//GETTER & SETTERS
    public Element getBpmndi() {
        return bpmndi;
    }
    public void setBpmndi(Element bpmndi) {
        this.bpmndi = bpmndi;
    }
    public Element getLaneSet() {
        return laneSet;
    }
    public void setLaneSet(Element laneSet) {
        this.laneSet = laneSet;
    }    
    public Definitions getDefinitions() {
        return definitions;
    }   
    public WSDLManager getWsdlManager() {
        return wsdlMan;
    }  

	public org.w3c.dom.Document getInitialDocument() {
		return initialDocument;
	}


	public org.w3c.dom.Document getFinalDocument() {
		return finalDocument;
	}
	
    public StringWriter getFinalDocumentWriter() {
		return finalDocumentWriter;
	}
}