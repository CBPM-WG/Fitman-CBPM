
package org.example.aircongo;

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
 *         &lt;element name="DepCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArrivCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DepDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArrivDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "depCity",
    "arrivCity",
    "depDate",
    "arrivDate"
})
@XmlRootElement(name = "Search")
public class Search {

    @XmlElement(name = "DepCity", required = true)
    protected String depCity;
    @XmlElement(name = "ArrivCity", required = true)
    protected String arrivCity;
    @XmlElement(name = "DepDate", required = true)
    protected String depDate;
    @XmlElement(name = "ArrivDate", required = true)
    protected String arrivDate;

    /**
     * Gets the value of the depCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepCity() {
        return depCity;
    }

    /**
     * Sets the value of the depCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepCity(String value) {
        this.depCity = value;
    }

    /**
     * Gets the value of the arrivCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrivCity() {
        return arrivCity;
    }

    /**
     * Sets the value of the arrivCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrivCity(String value) {
        this.arrivCity = value;
    }

    /**
     * Gets the value of the depDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepDate() {
        return depDate;
    }

    /**
     * Sets the value of the depDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepDate(String value) {
        this.depDate = value;
    }

    /**
     * Gets the value of the arrivDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrivDate() {
        return arrivDate;
    }

    /**
     * Sets the value of the arrivDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrivDate(String value) {
        this.arrivDate = value;
    }

}
