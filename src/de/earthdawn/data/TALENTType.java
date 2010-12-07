//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.07 at 08:00:39 PM MEZ 
//


package de.earthdawn.data;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TALENT_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TALENT_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://earthdawn.com}SKILL_type">
 *       &lt;attribute name="karma" type="{http://earthdawn.com}yesno_type" default="no" />
 *       &lt;attribute name="circle" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TALENT_type")
public class TALENTType
    extends SKILLType
{

    @XmlAttribute
    protected YesnoType karma;
    @XmlAttribute(required = true)
    protected BigInteger circle;

    /**
     * Gets the value of the karma property.
     * 
     * @return
     *     possible object is
     *     {@link YesnoType }
     *     
     */
    public YesnoType getKarma() {
        if (karma == null) {
            return YesnoType.NO;
        } else {
            return karma;
        }
    }

    /**
     * Sets the value of the karma property.
     * 
     * @param value
     *     allowed object is
     *     {@link YesnoType }
     *     
     */
    public void setKarma(YesnoType value) {
        this.karma = value;
    }

    /**
     * Gets the value of the circle property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCircle() {
        return circle;
    }

    /**
     * Sets the value of the circle property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCircle(BigInteger value) {
        this.circle = value;
    }

}
