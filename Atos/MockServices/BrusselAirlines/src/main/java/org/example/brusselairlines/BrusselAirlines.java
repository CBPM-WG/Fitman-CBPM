package org.example.brusselairlines;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2013-07-07T00:29:04.671+02:00
 * Generated source version: 2.4.2
 * 
 */
@WebService(targetNamespace = "http://www.example.org/BrusselAirlines/", name = "BrusselAirlines")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface BrusselAirlines {

    @WebResult(name = "parameters", targetNamespace = "", partName = "parameters")
    @WebMethod(operationName = "SearchFlights", action = "http://atosresearch.org/BrusselAirlines/SearchFlights")
    public FlightsResponseType searchFlights(
        @WebParam(partName = "parameters", name = "SearchFligths", targetNamespace = "http://www.example.org/BrusselAirlines/")
        Flights parameters
    );
}
