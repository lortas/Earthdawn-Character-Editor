//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0.1
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: see version control commit date
//


package de.earthdawn.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KNACKATTRIBUTE_type complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="KNACKATTRIBUTE_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="name" use="required" type="{http://earthdawn.com/datatypes}ATTRIBUTE_name_type" /&gt;
 *       &lt;attribute name="min" type="{http://earthdawn.com/datatypes}unsigned_int" default="1" /&gt;
 *       &lt;attribute name="max" type="{http://earthdawn.com/datatypes}unsigned_int" default="0" /&gt;
 *       &lt;attribute name="prerequisite" type="{http://earthdawn.com/datatypes}prerequisitekind_type" default="restriction" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KNACKATTRIBUTE_type", namespace = "http://earthdawn.com/knack")
public class KNACKATTRIBUTEType {

    @XmlAttribute(name = "name", required = true)
    protected ATTRIBUTENameType name;
    @XmlAttribute(name = "min")
    protected Integer min;
    @XmlAttribute(name = "max")
    protected Integer max;
    @XmlAttribute(name = "prerequisite")
    protected PrerequisitekindType prerequisite;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link ATTRIBUTENameType }
     *
     */
    public ATTRIBUTENameType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link ATTRIBUTENameType }
     *
     */
    public void setName(ATTRIBUTENameType value) {
        this.name = value;
    }

    /**
     * Gets the value of the min property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public int getMin() {
        if (min == null) {
            return  1;
        } else {
            return min;
        }
    }

    /**
     * Sets the value of the min property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setMin(Integer value) {
        this.min = value;
    }

    /**
     * Gets the value of the max property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public int getMax() {
        if (max == null) {
            return  0;
        } else {
            return max;
        }
    }

    /**
     * Sets the value of the max property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setMax(Integer value) {
        this.max = value;
    }

    /**
     * Gets the value of the prerequisite property.
     *
     * @return
     *     possible object is
     *     {@link PrerequisitekindType }
     *
     */
    public PrerequisitekindType getPrerequisite() {
        if (prerequisite == null) {
            return PrerequisitekindType.RESTRICTION;
        } else {
            return prerequisite;
        }
    }

    /**
     * Sets the value of the prerequisite property.
     *
     * @param value
     *     allowed object is
     *     {@link PrerequisitekindType }
     *
     */
    public void setPrerequisite(PrerequisitekindType value) {
        this.prerequisite = value;
    }

}
