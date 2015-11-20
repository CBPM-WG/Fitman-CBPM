
package org.example.firsthotel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="ClientId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HotelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HotelCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Days" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="GuestNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clientId",
    "hotelName",
    "hotelCity",
    "days",
    "guestNumber"
})
@XmlRootElement(name = "MakeReservation")
public class MakeReservation {

    @XmlElement(name = "ClientId", required = true)
    protected String clientId;
    @XmlElement(name = "HotelName", required = true)
    protected String hotelName;
    @XmlElement(name = "HotelCity", required = true)
    protected String hotelCity;
    @XmlElement(name = "Days")
    protected int days;
    @XmlElement(name = "GuestNumber")
    protected int guestNumber;

    /**
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    /**
     * Gets the value of the hotelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Sets the value of the hotelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHotelName(String value) {
        this.hotelName = value;
    }

    /**
     * Gets the value of the hotelCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHotelCity() {
        return hotelCity;
    }

    /**
     * Sets the value of the hotelCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHotelCity(String value) {
        this.hotelCity = value;
    }

    /**
     * Gets the value of the days property.
     * 
     */
    public int getDays() {
        return days;
    }

    /**
     * Sets the value of the days property.
     * 
     */
    public void setDays(int value) {
        this.days = value;
    }

    /**
     * Gets the value of the guestNumber property.
     * 
     */
    public int getGuestNumber() {
        return guestNumber;
    }

    /**
     * Sets the value of the guestNumber property.
     * 
     */
    public void setGuestNumber(int value) {
        this.guestNumber = value;
    }

}
