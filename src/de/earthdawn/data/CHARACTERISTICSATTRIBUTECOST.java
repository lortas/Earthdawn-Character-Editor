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
 * <p>Java class for CHARACTERISTICS_ATTRIBUTECOST complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CHARACTERISTICS_ATTRIBUTECOST">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="modifier" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="cost" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CHARACTERISTICS_ATTRIBUTECOST", namespace = "http://earthdawn.com/characteristics")
public class CHARACTERISTICSATTRIBUTECOST {

    @XmlAttribute(required = true)
    protected int modifier;
    @XmlAttribute(required = true)
    protected int cost;

    /**
     * Gets the value of the modifier property.
     * 
     */
    public int getModifier() {
        return modifier;
    }

    /**
     * Sets the value of the modifier property.
     * 
     */
    public void setModifier(int value) {
        this.modifier = value;
    }

    /**
     * Gets the value of the cost property.
     * 
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the value of the cost property.
     * 
     */
    public void setCost(int value) {
        this.cost = value;
    }

}
