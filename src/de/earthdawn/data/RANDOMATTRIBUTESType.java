//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: see version control commit date
//


package de.earthdawn.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RANDOMATTRIBUTES_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RANDOMATTRIBUTES_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="weight" type="{http://earthdawn.com/datatypes}unsigned_int" default="1" />
 *       &lt;attribute name="attribute" use="required" type="{http://earthdawn.com/datatypes}ATTRIBUTE_name_type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RANDOMATTRIBUTES_type", namespace = "http://earthdawn.com/randomcharactertemplate")
public class RANDOMATTRIBUTESType {

    @XmlAttribute
    protected Integer weight;
    @XmlAttribute(required = true)
    protected ATTRIBUTENameType attribute;

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getWeight() {
        if (weight == null) {
            return  1;
        } else {
            return weight;
        }
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWeight(Integer value) {
        this.weight = value;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * @return
     *     possible object is
     *     {@link ATTRIBUTENameType }
     *     
     */
    public ATTRIBUTENameType getAttribute() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link ATTRIBUTENameType }
     *     
     */
    public void setAttribute(ATTRIBUTENameType value) {
        this.attribute = value;
    }

}
