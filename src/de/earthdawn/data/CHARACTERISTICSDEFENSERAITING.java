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
 * <p>Java class for CHARACTERISTICS_DEFENSERAITING complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CHARACTERISTICS_DEFENSERAITING">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="attribute" use="required" type="{http://earthdawn.com/datatypes}unsigned_int" />
 *       &lt;attribute name="defense" use="required" type="{http://earthdawn.com/datatypes}unsigned_int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CHARACTERISTICS_DEFENSERAITING", namespace = "http://earthdawn.com/characteristics")
public class CHARACTERISTICSDEFENSERAITING {

    @XmlAttribute(required = true)
    protected int attribute;
    @XmlAttribute(required = true)
    protected int defense;

    /**
     * Gets the value of the attribute property.
     * 
     */
    public int getAttribute() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     * 
     */
    public void setAttribute(int value) {
        this.attribute = value;
    }

    /**
     * Gets the value of the defense property.
     * 
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Sets the value of the defense property.
     * 
     */
    public void setDefense(int value) {
        this.defense = value;
    }

}
