//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0.1
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: see version control commit date
//


package de.earthdawn.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PATHKNACK" type="{http://earthdawn.com/path}PATHKNACK_type"/&gt;
 *         &lt;element name="PATHTALENT" type="{http://earthdawn.com/path}PATHTALENT_type"/&gt;
 *         &lt;element name="PATHRANK" type="{http://earthdawn.com/path}PATHRANK_type" maxOccurs="15" minOccurs="8"/&gt;
 *         &lt;element name="KARMARITUAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HALFMAGIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="lang" use="required" type="{http://earthdawn.com/datatypes}language_type" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="bookref" type="{http://www.w3.org/2001/XMLSchema}string" default="" /&gt;
 *       &lt;attribute name="rulesetversion" type="{http://earthdawn.com/datatypes}rulesetversion_type" default="ED4" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pathknack",
    "pathtalent",
    "pathrank",
    "karmaritual",
    "halfmagic"
})
@XmlRootElement(name = "PATH", namespace = "http://earthdawn.com/path")
public class PATH {

    @XmlElement(name = "PATHKNACK", namespace = "http://earthdawn.com/path", required = true)
    protected PATHKNACKType pathknack;
    @XmlElement(name = "PATHTALENT", namespace = "http://earthdawn.com/path", required = true)
    protected PATHTALENTType pathtalent;
    @XmlElement(name = "PATHRANK", namespace = "http://earthdawn.com/path", required = true)
    protected List<PATHRANKType> pathrank;
    @XmlElement(name = "KARMARITUAL", namespace = "http://earthdawn.com/path")
    protected String karmaritual;
    @XmlElement(name = "HALFMAGIC", namespace = "http://earthdawn.com/path")
    protected String halfmagic;
    @XmlAttribute(name = "lang", required = true)
    protected LanguageType lang;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "bookref")
    protected String bookref;
    @XmlAttribute(name = "rulesetversion")
    protected RulesetversionType rulesetversion;

    /**
     * Gets the value of the pathknack property.
     *
     * @return
     *     possible object is
     *     {@link PATHKNACKType }
     *
     */
    public PATHKNACKType getPATHKNACK() {
        return pathknack;
    }

    /**
     * Sets the value of the pathknack property.
     *
     * @param value
     *     allowed object is
     *     {@link PATHKNACKType }
     *
     */
    public void setPATHKNACK(PATHKNACKType value) {
        this.pathknack = value;
    }

    /**
     * Gets the value of the pathtalent property.
     *
     * @return
     *     possible object is
     *     {@link PATHTALENTType }
     *
     */
    public PATHTALENTType getPATHTALENT() {
        return pathtalent;
    }

    /**
     * Sets the value of the pathtalent property.
     *
     * @param value
     *     allowed object is
     *     {@link PATHTALENTType }
     *
     */
    public void setPATHTALENT(PATHTALENTType value) {
        this.pathtalent = value;
    }

    /**
     * Gets the value of the pathrank property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pathrank property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPATHRANK().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PATHRANKType }
     *
     *
     */
    public List<PATHRANKType> getPATHRANK() {
        if (pathrank == null) {
            pathrank = new ArrayList<PATHRANKType>();
        }
        return this.pathrank;
    }

    /**
     * Gets the value of the karmaritual property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getKARMARITUAL() {
        return karmaritual;
    }

    /**
     * Sets the value of the karmaritual property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setKARMARITUAL(String value) {
        this.karmaritual = value;
    }

    /**
     * Gets the value of the halfmagic property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHALFMAGIC() {
        return halfmagic;
    }

    /**
     * Sets the value of the halfmagic property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHALFMAGIC(String value) {
        this.halfmagic = value;
    }

    /**
     * Gets the value of the lang property.
     *
     * @return
     *     possible object is
     *     {@link LanguageType }
     *
     */
    public LanguageType getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     *
     * @param value
     *     allowed object is
     *     {@link LanguageType }
     *
     */
    public void setLang(LanguageType value) {
        this.lang = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the bookref property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBookref() {
        if (bookref == null) {
            return "";
        } else {
            return bookref;
        }
    }

    /**
     * Sets the value of the bookref property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBookref(String value) {
        this.bookref = value;
    }

    /**
     * Gets the value of the rulesetversion property.
     *
     * @return
     *     possible object is
     *     {@link RulesetversionType }
     *
     */
    public RulesetversionType getRulesetversion() {
        if (rulesetversion == null) {
            return RulesetversionType.ED_4;
        } else {
            return rulesetversion;
        }
    }

    /**
     * Sets the value of the rulesetversion property.
     *
     * @param value
     *     allowed object is
     *     {@link RulesetversionType }
     *
     */
    public void setRulesetversion(RulesetversionType value) {
        this.rulesetversion = value;
    }

}
