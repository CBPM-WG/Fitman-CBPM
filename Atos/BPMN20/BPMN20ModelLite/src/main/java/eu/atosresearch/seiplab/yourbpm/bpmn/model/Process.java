//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.22 at 12:43:24 PM GMT+01:00 
//


package eu.atosresearch.seiplab.yourbpm.bpmn.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}ioSpecification"/>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}startEvent"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}receiveTask"/>
 *           &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}sequenceFlow"/>
 *           &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}serviceTask"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}endEvent"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "", propOrder = {
//    "ioSpecification",
//    "startEvent",
//    "receiveTasks",
//    "sequenceFlows",
//    "serviceTasks",
//    "endEvent"
//})

@XmlRootElement(name = "process")
public class Process {

    @XmlElement(required = false)
    protected IoSpecification ioSpecification;
    @XmlElement(required = false)
    protected StartEvent startEvent;
    @XmlElements({
        @XmlElement(name = "receiveTask", type = ReceiveTask.class)
    })
    protected List<ReceiveTask> receiveTasks;
    @XmlElements({
        @XmlElement(name = "sequenceFlow", type = SequenceFlow.class)
    })
    protected List<SequenceFlow> sequenceFlows;    
    @XmlElements({
        @XmlElement(name = "serviceTask", type = ServiceTask.class)
    })
    protected List<ServiceTask> serviceTasks;
    @XmlElement(required = false)
    protected EndEvent endEvent;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String id;
    @XmlAttribute(name = "name", required = false)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

    /**
     * Gets the value of the ioSpecification property.
     * 
     * @return
     *     possible object is
     *     {@link IoSpecification }
     *     
     */
    public IoSpecification getIoSpecification() {
        return ioSpecification;
    }

    /**
     * Sets the value of the ioSpecification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IoSpecification }
     *     
     */
    public void setIoSpecification(IoSpecification value) {
        this.ioSpecification = value;
    }

    /**
     * Gets the value of the startEvent property.
     * 
     * @return
     *     possible object is
     *     {@link StartEvent }
     *     
     */
    public StartEvent getStartEvent() {
        return startEvent;
    }

    /**
     * Sets the value of the startEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartEvent }
     *     
     */
    public void setStartEvent(StartEvent value) {
        this.startEvent = value;
    }

    /**
     * Gets the value of the receiveTaskOrSequenceFlowOrServiceTask property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiveTaskOrSequenceFlowOrServiceTask property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiveTaskOrSequenceFlowOrServiceTask().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiveTask }
     * {@link SequenceFlow }
     * {@link ServiceTask }
     * 
     * 
     */
    public List<ReceiveTask> getReceiveTasks() {
        if (receiveTasks == null) {
            receiveTasks = new ArrayList<ReceiveTask>();
        }
        return this.receiveTasks;
    }
    
   public List<SequenceFlow> getSequenceFlows() {
        if (sequenceFlows == null) {
            sequenceFlows = new ArrayList<SequenceFlow>();
        }
        return this.sequenceFlows;
    }
    public List<ServiceTask> getServiceTasks() {
        if (serviceTasks == null) {
            serviceTasks = new ArrayList<ServiceTask>();
        }
        return this.serviceTasks;
    }        

    /**
     * Gets the value of the endEvent property.
     * 
     * @return
     *     possible object is
     *     {@link EndEvent }
     *     
     */
    public EndEvent getEndEvent() {
        return endEvent;
    }

    /**
     * Sets the value of the endEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndEvent }
     *     
     */
    public void setEndEvent(EndEvent value) {
        this.endEvent = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
