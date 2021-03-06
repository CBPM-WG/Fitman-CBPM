package org.example.hostelworld;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2013-07-07T00:29:11.600+02:00
 * Generated source version: 2.4.2
 * 
 */
@WebService(targetNamespace = "http://www.example.org/HostelWorld/", name = "HostelWorld")
@XmlSeeAlso({ObjectFactory.class})
public interface HostelWorld {

    @WebResult(name = "OperationResult", targetNamespace = "")
    @RequestWrapper(localName = "Book", targetNamespace = "http://www.example.org/HostelWorld/", className = "org.example.hostelworld.Book")
    @WebMethod(operationName = "Book", action = "http://www.example.org/HostelWorld/Book")
    @ResponseWrapper(localName = "BookResponse", targetNamespace = "http://www.example.org/HostelWorld/", className = "org.example.hostelworld.BookResponse")
    public java.lang.String book(
        @WebParam(name = "HotelName", targetNamespace = "")
        java.lang.String hotelName,
        @WebParam(name = "RoomType", targetNamespace = "")
        java.lang.String roomType,
        @WebParam(name = "nights", targetNamespace = "")
        int nights,
        @WebParam(name = "ClientId", targetNamespace = "")
        java.lang.String clientId
    );
}
