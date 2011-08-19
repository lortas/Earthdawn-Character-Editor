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
 * <p>Java class for RANK_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RANK_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://earthdawn.com/datatypes}STEPDICE_type">
 *       &lt;attribute name="startrank" type="{http://earthdawn.com/datatypes}unsigned_int" default="0" />
 *       &lt;attribute name="realignedrank" type="{http://earthdawn.com/datatypes}unsigned_int" default="0" />
 *       &lt;attribute name="rank" type="{http://earthdawn.com/datatypes}unsigned_int" default="0" />
 *       &lt;attribute name="bonus" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="lpcost" type="{http://earthdawn.com/datatypes}unsigned_int" default="0" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RANK_type")
public class RANKType
    extends STEPDICEType
{

    @XmlAttribute
    protected Integer startrank;
    @XmlAttribute
    protected Integer realignedrank;
    @XmlAttribute
    protected Integer rank;
    @XmlAttribute
    protected Integer bonus;
    @XmlAttribute
    protected Integer lpcost;

    /**
     * Gets the value of the startrank property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getStartrank() {
        if (startrank == null) {
            return  0;
        } else {
            return startrank;
        }
    }

    /**
     * Sets the value of the startrank property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStartrank(Integer value) {
        this.startrank = value;
    }

    /**
     * Gets the value of the realignedrank property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getRealignedrank() {
        if (realignedrank == null) {
            return  0;
        } else {
            return realignedrank;
        }
    }

    /**
     * Sets the value of the realignedrank property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRealignedrank(Integer value) {
        this.realignedrank = value;
    }

    /**
     * Gets the value of the rank property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getRank() {
        if (rank == null) {
            return  0;
        } else {
            return rank;
        }
    }

    /**
     * Sets the value of the rank property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRank(Integer value) {
        this.rank = value;
    }

    /**
     * Gets the value of the bonus property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getBonus() {
        if (bonus == null) {
            return  0;
        } else {
            return bonus;
        }
    }

    /**
     * Sets the value of the bonus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBonus(Integer value) {
        this.bonus = value;
    }

    /**
     * Gets the value of the lpcost property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getLpcost() {
        if (lpcost == null) {
            return  0;
        } else {
            return lpcost;
        }
    }

    /**
     * Sets the value of the lpcost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLpcost(Integer value) {
        this.lpcost = value;
    }

}
