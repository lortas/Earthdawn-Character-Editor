//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: see version control commit date
//


package de.earthdawn.data;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="ATTRIBUTE" type="{http://earthdawn.com/optionalrules}OPTIONALRULES_ATTRIBUTE"/>
 *         &lt;element name="ATTRIBUTEBASEDMOVEMENT" type="{http://earthdawn.com/optionalrules}OPTIONALRULES_ATTRIBUTEBASEDMOVEMENT"/>
 *         &lt;element name="LEGENDPOINTSFORATTRIBUTEINCREASE" type="{http://earthdawn.com/optionalrules}OPTIONALRULE_type"/>
 *         &lt;element name="SPELLLEGENDPOINTCOST" type="{http://earthdawn.com/optionalrules}OPTIONALRULE_type"/>
 *         &lt;element name="THREADITEMDOSTACK" type="{http://earthdawn.com/optionalrules}OPTIONALRULE_type"/>
 *         &lt;element name="AUTOINCREMENTDICIPLINETALENTS" type="{http://earthdawn.com/optionalrules}OPTIONALRULE_type"/>
 *         &lt;element name="SHOWDEFAULTSKILLS" type="{http://earthdawn.com/optionalrules}OPTIONALRULE_type"/>
 *         &lt;element name="MULTIUSETALENT" type="{http://earthdawn.com/optionalrules}OPTIONALRULES_MULTIUSETALENT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DEFAULTOPTIONALTALENT" type="{http://earthdawn.com/optionalrules}OPTIONALRULES_DEFAULTOPTIONALTALENT" maxOccurs="unbounded" minOccurs="0"/>
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
    "attribute",
    "attributebasedmovement",
    "legendpointsforattributeincrease",
    "spelllegendpointcost",
    "threaditemdostack",
    "autoincrementdiciplinetalents",
    "showdefaultskills",
    "multiusetalent",
    "defaultoptionaltalent"
})
@XmlRootElement(name = "OPTIONALRULES", namespace = "http://earthdawn.com/optionalrules")
public class OPTIONALRULES {

    @XmlElement(name = "ATTRIBUTE", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULESATTRIBUTE attribute;
    @XmlElement(name = "ATTRIBUTEBASEDMOVEMENT", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULESATTRIBUTEBASEDMOVEMENT attributebasedmovement;
    @XmlElement(name = "LEGENDPOINTSFORATTRIBUTEINCREASE", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULEType legendpointsforattributeincrease;
    @XmlElement(name = "SPELLLEGENDPOINTCOST", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULEType spelllegendpointcost;
    @XmlElement(name = "THREADITEMDOSTACK", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULEType threaditemdostack;
    @XmlElement(name = "AUTOINCREMENTDICIPLINETALENTS", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULEType autoincrementdiciplinetalents;
    @XmlElement(name = "SHOWDEFAULTSKILLS", namespace = "http://earthdawn.com/optionalrules", required = true)
    protected OPTIONALRULEType showdefaultskills;
    @XmlElement(name = "MULTIUSETALENT", namespace = "http://earthdawn.com/optionalrules")
    protected List<OPTIONALRULESMULTIUSETALENT> multiusetalent;
    @XmlElement(name = "DEFAULTOPTIONALTALENT", namespace = "http://earthdawn.com/optionalrules")
    protected List<OPTIONALRULESDEFAULTOPTIONALTALENT> defaultoptionaltalent;

    /**
     * Gets the value of the attribute property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULESATTRIBUTE }
     *     
     */
    public OPTIONALRULESATTRIBUTE getATTRIBUTE() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULESATTRIBUTE }
     *     
     */
    public void setATTRIBUTE(OPTIONALRULESATTRIBUTE value) {
        this.attribute = value;
    }

    /**
     * Gets the value of the attributebasedmovement property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULESATTRIBUTEBASEDMOVEMENT }
     *     
     */
    public OPTIONALRULESATTRIBUTEBASEDMOVEMENT getATTRIBUTEBASEDMOVEMENT() {
        return attributebasedmovement;
    }

    /**
     * Sets the value of the attributebasedmovement property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULESATTRIBUTEBASEDMOVEMENT }
     *     
     */
    public void setATTRIBUTEBASEDMOVEMENT(OPTIONALRULESATTRIBUTEBASEDMOVEMENT value) {
        this.attributebasedmovement = value;
    }

    /**
     * Gets the value of the legendpointsforattributeincrease property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public OPTIONALRULEType getLEGENDPOINTSFORATTRIBUTEINCREASE() {
        return legendpointsforattributeincrease;
    }

    /**
     * Sets the value of the legendpointsforattributeincrease property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public void setLEGENDPOINTSFORATTRIBUTEINCREASE(OPTIONALRULEType value) {
        this.legendpointsforattributeincrease = value;
    }

    /**
     * Gets the value of the spelllegendpointcost property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public OPTIONALRULEType getSPELLLEGENDPOINTCOST() {
        return spelllegendpointcost;
    }

    /**
     * Sets the value of the spelllegendpointcost property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public void setSPELLLEGENDPOINTCOST(OPTIONALRULEType value) {
        this.spelllegendpointcost = value;
    }

    /**
     * Gets the value of the threaditemdostack property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public OPTIONALRULEType getTHREADITEMDOSTACK() {
        return threaditemdostack;
    }

    /**
     * Sets the value of the threaditemdostack property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public void setTHREADITEMDOSTACK(OPTIONALRULEType value) {
        this.threaditemdostack = value;
    }

    /**
     * Gets the value of the autoincrementdiciplinetalents property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public OPTIONALRULEType getAUTOINCREMENTDICIPLINETALENTS() {
        return autoincrementdiciplinetalents;
    }

    /**
     * Sets the value of the autoincrementdiciplinetalents property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public void setAUTOINCREMENTDICIPLINETALENTS(OPTIONALRULEType value) {
        this.autoincrementdiciplinetalents = value;
    }

    /**
     * Gets the value of the showdefaultskills property.
     * 
     * @return
     *     possible object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public OPTIONALRULEType getSHOWDEFAULTSKILLS() {
        return showdefaultskills;
    }

    /**
     * Sets the value of the showdefaultskills property.
     * 
     * @param value
     *     allowed object is
     *     {@link OPTIONALRULEType }
     *     
     */
    public void setSHOWDEFAULTSKILLS(OPTIONALRULEType value) {
        this.showdefaultskills = value;
    }

    /**
     * Gets the value of the multiusetalent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the multiusetalent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMULTIUSETALENT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OPTIONALRULESMULTIUSETALENT }
     * 
     * 
     */
    public List<OPTIONALRULESMULTIUSETALENT> getMULTIUSETALENT() {
        if (multiusetalent == null) {
            multiusetalent = new ArrayList<OPTIONALRULESMULTIUSETALENT>();
        }
        return this.multiusetalent;
    }

    /**
     * Gets the value of the defaultoptionaltalent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defaultoptionaltalent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDEFAULTOPTIONALTALENT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OPTIONALRULESDEFAULTOPTIONALTALENT }
     * 
     * 
     */
    public List<OPTIONALRULESDEFAULTOPTIONALTALENT> getDEFAULTOPTIONALTALENT() {
        if (defaultoptionaltalent == null) {
            defaultoptionaltalent = new ArrayList<OPTIONALRULESDEFAULTOPTIONALTALENT>();
        }
        return this.defaultoptionaltalent;
    }

}
