
package org.example.brusselairlines;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Flight complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Flight">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Company" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DepartureTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArrivalTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Depart" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Arrive" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Flight", propOrder = {
    "company",
    "departureTime",
    "arrivalTime",
    "depart",
    "arrive"
})
public class Flight {

    @XmlElement(name = "Company", required = true)
    protected String company;
    @XmlElement(name = "DepartureTime", required = true)
    protected String departureTime;
    @XmlElement(name = "ArrivalTime", required = true)
    protected String arrivalTime;
    @XmlElement(name = "Depart", required = true)
    protected String depart;
    @XmlElement(name = "Arrive", required = true)
    protected String arrive;

    /**
     * Gets the value of the company property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the value of the company property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompany(String value) {
        this.company = value;
    }

    /**
     * Gets the value of the departureTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * Sets the value of the departureTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartureTime(String value) {
        this.departureTime = value;
    }

    /**
     * Gets the value of the arrivalTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the value of the arrivalTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrivalTime(String value) {
        this.arrivalTime = value;
    }

    /**
     * Gets the value of the depart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepart() {
        return depart;
    }

    /**
     * Sets the value of the depart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepart(String value) {
        this.depart = value;
    }

    /**
     * Gets the value of the arrive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrive() {
        return arrive;
    }

    /**
     * Sets the value of the arrive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrive(String value) {
        this.arrive = value;
    }

}
