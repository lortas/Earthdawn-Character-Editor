//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: see version control commit date
//


package de.earthdawn.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HEALTH_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HEALTH_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="RECOVERY" type="{http://earthdawn.com/datatypes}RECOVERY_type"/>
 *         &lt;element name="UNCONSCIOUSNESS" type="{http://earthdawn.com/datatypes}DEATH_type"/>
 *         &lt;element name="DEATH" type="{http://earthdawn.com/datatypes}DEATH_type"/>
 *         &lt;element name="WOUNDS" type="{http://earthdawn.com/datatypes}WOUND_type"/>
 *       &lt;/choice>
 *       &lt;attribute name="damage" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HEALTH_type", propOrder = {
    "recoveryOrUNCONSCIOUSNESSOrDEATH"
})
public class HEALTHType {

    @XmlElementRefs({
        @XmlElementRef(name = "WOUNDS", namespace = "http://earthdawn.com/datatypes", type = JAXBElement.class),
        @XmlElementRef(name = "DEATH", namespace = "http://earthdawn.com/datatypes", type = JAXBElement.class),
        @XmlElementRef(name = "RECOVERY", namespace = "http://earthdawn.com/datatypes", type = JAXBElement.class),
        @XmlElementRef(name = "UNCONSCIOUSNESS", namespace = "http://earthdawn.com/datatypes", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> recoveryOrUNCONSCIOUSNESSOrDEATH;
    @XmlAttribute(required = true)
    protected int damage;

    /**
     * Gets the value of the recoveryOrUNCONSCIOUSNESSOrDEATH property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recoveryOrUNCONSCIOUSNESSOrDEATH property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRECOVERYOrUNCONSCIOUSNESSOrDEATH().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link RECOVERYType }{@code >}
     * {@link JAXBElement }{@code <}{@link WOUNDType }{@code >}
     * {@link JAXBElement }{@code <}{@link DEATHType }{@code >}
     * {@link JAXBElement }{@code <}{@link DEATHType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRECOVERYOrUNCONSCIOUSNESSOrDEATH() {
        if (recoveryOrUNCONSCIOUSNESSOrDEATH == null) {
            recoveryOrUNCONSCIOUSNESSOrDEATH = new ArrayList<JAXBElement<?>>();
        }
        return this.recoveryOrUNCONSCIOUSNESSOrDEATH;
    }

    /**
     * Gets the value of the damage property.
     * 
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the value of the damage property.
     * 
     */
    public void setDamage(int value) {
        this.damage = value;
    }

}
