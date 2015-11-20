package org.example.firsthotel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2013-07-07T00:29:08.664+02:00
 * Generated source version: 2.4.2
 * 
 */
@WebService(targetNamespace = "http://www.example.org/FirstHotel/", name = "FirstHotel")
@XmlSeeAlso({ObjectFactory.class})
public interface FirstHotel {

    @WebResult(name = "out", targetNamespace = "")
    @RequestWrapper(localName = "MakeReservation", targetNamespace = "http://www.example.org/FirstHotel/", className = "org.example.firsthotel.MakeReservation")
    @WebMethod(operationName = "MakeReservation", action = "http://www.example.org/FirstHotel/MakeReservation")
    @ResponseWrapper(localName = "MakeReservationResponse", targetNamespace = "http://www.example.org/FirstHotel/", className = "org.example.firsthotel.MakeReservationResponse")
    public java.lang.String makeReservation(
        @WebParam(name = "ClientId", targetNamespace = "")
        java.lang.String clientId,
        @WebParam(name = "HotelName", targetNamespace = "")
        java.lang.String hotelName,
        @WebParam(name = "HotelCity", targetNamespace = "")
        java.lang.String hotelCity,
        @WebParam(name = "Days", targetNamespace = "")
        int days,
        @WebParam(name = "GuestNumber", targetNamespace = "")
        int guestNumber
    );
}