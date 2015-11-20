The classes have been generated automatically through the jaxb in the package eu.atosresearch.seiplab.yourbpm.bpmn.standard, based in the bpmn.sxd.

The following manual changes have been done:

1-In the file package-info.java, we have to introduce the xmlns 

@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL",
xmlns = { 
	@javax.xml.bind.annotation.XmlNs(prefix = "", namespaceURI ="http://www.omg.org/spec/BPMN/20100524/MODEL"),
        @javax.xml.bind.annotation.XmlNs(prefix = "bpmndi", namespaceURI ="http://www.omg.org/spec/BPMN/20100524/DI"), 
        @javax.xml.bind.annotation.XmlNs(prefix = "omgdc", namespaceURI = "http://www.omg.org/spec/DD/20100524/DC"),
        @javax.xml.bind.annotation.XmlNs(prefix = "omgdi", namespaceURI = "http://www.omg.org/spec/DD/20100524/DI")
          },
elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package eu.atosresearch.seiplab.yourbpm.bpmn.standard;

If you have to regenerate the java classes, you should introduce manually the xmlns in the package-info.java
xmlns = { 
	@javax.xml.bind.annotation.XmlNs(prefix = "", namespaceURI ="http://www.omg.org/spec/BPMN/20100524/MODEL"),
        @javax.xml.bind.annotation.XmlNs(prefix = "bpmndi", namespaceURI ="http://www.omg.org/spec/BPMN/20100524/DI"), 
        @javax.xml.bind.annotation.XmlNs(prefix = "omgdc", namespaceURI = "http://www.omg.org/spec/DD/20100524/DC"),
        @javax.xml.bind.annotation.XmlNs(prefix = "omgdi", namespaceURI = "http://www.omg.org/spec/DD/20100524/DI")
          }
          
          
2-In the file TDefinitions.java, we have to indicate that it is the XmlRootElement.
@XmlRootElement(name = "definitions")