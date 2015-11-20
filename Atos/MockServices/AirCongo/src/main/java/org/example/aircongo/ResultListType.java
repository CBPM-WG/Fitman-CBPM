
package org.example.aircongo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resultListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CompanyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Price" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DepTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArrivTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="city1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="city2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultListType", propOrder = {
    "companyName",
    "price",
    "depTime",
    "arrivTime",
    "city1",
    "city2"
})
public class ResultListType {

    @XmlElement(name = "CompanyName", required = true)
    protected String companyName;
    @XmlElement(name = "Price")
    protected int price;
    @XmlElement(name = "DepTime", required = true)
    protected String depTime;
    @XmlElement(name = "ArrivTime", required = true)
    protected String arrivTime;
    @XmlElement(required = true)
    protected String city1;
    @XmlElement(required = true)
    protected String city2;

    /**
     * Gets the value of the companyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the value of the companyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
    }

    /**
     * Gets the value of the price property.
     * 
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     */
    public void setPrice(int value) {
        this.price = value;
    }

    /**
     * Gets the value of the depTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepTime() {
        return depTime;
    }

    /**
     * Sets the value of the depTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepTime(String value) {
        this.depTime = value;
    }

    /**
     * Gets the value of the arrivTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrivTime() {
        return arrivTime;
    }

    /**
     * Sets the value of the arrivTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrivTime(String value) {
        this.arrivTime = value;
    }

    /**
     * Gets the value of the city1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity1() {
        return city1;
    }

    /**
     * Sets the value of the city1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity1(String value) {
        this.city1 = value;
    }

    /**
     * Gets the value of the city2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity2() {
        return city2;
    }

    /**
     * Sets the value of the city2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity2(String value) {
        this.city2 = value;
    }

}
