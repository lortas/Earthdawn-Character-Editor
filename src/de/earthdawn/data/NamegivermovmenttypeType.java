//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: see version control commit date
//


package de.earthdawn.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for namegivermovmenttype_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="namegivermovmenttype_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ground"/>
 *     &lt;enumeration value="flight"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "namegivermovmenttype_type", namespace = "http://earthdawn.com/namegiver")
@XmlEnum
public enum NamegivermovmenttypeType {

    @XmlEnumValue("ground")
    GROUND("ground"),
    @XmlEnumValue("flight")
    FLIGHT("flight");
    private final String value;

    NamegivermovmenttypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NamegivermovmenttypeType fromValue(String v) {
        for (NamegivermovmenttypeType c: NamegivermovmenttypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
