//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: see version control commit date
//


package de.earthdawn.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rulesetversion_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="rulesetversion_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ED3"/>
 *     &lt;enumeration value="ED4"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "rulesetversion_type")
@XmlEnum
public enum RulesetversionType {

    @XmlEnumValue("ED3")
    ED_3("ED3"),
    @XmlEnumValue("ED4")
    ED_4("ED4");
    private final String value;

    RulesetversionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RulesetversionType fromValue(String v) {
        for (RulesetversionType c: RulesetversionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
