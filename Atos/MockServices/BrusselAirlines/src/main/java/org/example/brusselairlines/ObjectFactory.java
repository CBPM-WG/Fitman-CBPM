
package org.example.brusselairlines;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.brusselairlines package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SearchFligths_QNAME = new QName("http://www.example.org/BrusselAirlines/", "SearchFligths");
    private final static QName _FlightsResponse_QNAME = new QName("http://www.example.org/BrusselAirlines/", "FlightsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.brusselairlines
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FlightsResponseType }
     * 
     */
    public FlightsResponseType createFlightsResponseType() {
        return new FlightsResponseType();
    }

    /**
     * Create an instance of {@link Flight }
     * 
     */
    public Flight createFlight() {
        return new Flight();
    }

    /**
     * Create an instance of {@link NewOperation }
     * 
     */
    public NewOperation createNewOperation() {
        return new NewOperation();
    }

    /**
     * Create an instance of {@link NewOperationResponse }
     * 
     */
    public NewOperationResponse createNewOperationResponse() {
        return new NewOperationResponse();
    }

    /**
     * Create an instance of {@link Flights }
     * 
     */
    public Flights createFlights() {
        return new Flights();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Flights }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/BrusselAirlines/", name = "SearchFligths")
    public JAXBElement<Flights> createSearchFligths(Flights value) {
        return new JAXBElement<Flights>(_SearchFligths_QNAME, Flights.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/BrusselAirlines/", name = "FlightsResponse")
    public JAXBElement<String> createFlightsResponse(String value) {
        return new JAXBElement<String>(_FlightsResponse_QNAME, String.class, null, value);
    }

}
