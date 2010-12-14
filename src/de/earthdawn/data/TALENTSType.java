//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.14 at 08:14:21 PM MEZ 
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
 * <p>Java class for TALENTS_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TALENTS_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="DISZIPLINETALENT" type="{http://earthdawn.com/datatypes}TALENT_type" maxOccurs="unbounded"/>
 *         &lt;element name="OPTIONALTALENT" type="{http://earthdawn.com/datatypes}TALENT_type" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *       &lt;attribute name="discipline" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TALENTS_type", namespace = "http://earthdawn.com/character", propOrder = {
    "disziplinetalentOrOPTIONALTALENT"
})
public class TALENTSType {

    @XmlElementRefs({
        @XmlElementRef(name = "OPTIONALTALENT", namespace = "http://earthdawn.com/character", type = JAXBElement.class),
        @XmlElementRef(name = "DISZIPLINETALENT", namespace = "http://earthdawn.com/character", type = JAXBElement.class)
    })
    protected List<JAXBElement<TALENTType>> disziplinetalentOrOPTIONALTALENT;
    @XmlAttribute(required = true)
    protected String discipline;

    /**
     * Gets the value of the disziplinetalentOrOPTIONALTALENT property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disziplinetalentOrOPTIONALTALENT property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDISZIPLINETALENTOrOPTIONALTALENT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TALENTType }{@code >}
     * {@link JAXBElement }{@code <}{@link TALENTType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<TALENTType>> getDISZIPLINETALENTOrOPTIONALTALENT() {
        if (disziplinetalentOrOPTIONALTALENT == null) {
            disziplinetalentOrOPTIONALTALENT = new ArrayList<JAXBElement<TALENTType>>();
        }
        return this.disziplinetalentOrOPTIONALTALENT;
    }

    /**
     * Gets the value of the discipline property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscipline() {
        return discipline;
    }

    /**
     * Sets the value of the discipline property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscipline(String value) {
        this.discipline = value;
    }

}
