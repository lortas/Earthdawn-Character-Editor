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
 * <p>Java class for elementkind_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="elementkind_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="undefined"/>
 *     &lt;enumeration value="wood"/>
 *     &lt;enumeration value="earth"/>
 *     &lt;enumeration value="water"/>
 *     &lt;enumeration value="air"/>
 *     &lt;enumeration value="fire"/>
 *     &lt;enumeration value="illusion"/>
 *     &lt;enumeration value="fear"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "elementkind_type")
@XmlEnum
public enum ElementkindType {

    @XmlEnumValue("undefined")
    UNDEFINED("undefined"),
    @XmlEnumValue("wood")
    WOOD("wood"),
    @XmlEnumValue("earth")
    EARTH("earth"),
    @XmlEnumValue("water")
    WATER("water"),
    @XmlEnumValue("air")
    AIR("air"),
    @XmlEnumValue("fire")
    FIRE("fire"),
    @XmlEnumValue("illusion")
    ILLUSION("illusion"),
    @XmlEnumValue("fear")
    FEAR("fear");
    private final String value;

    ElementkindType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ElementkindType fromValue(String v) {
        for (ElementkindType c: ElementkindType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}