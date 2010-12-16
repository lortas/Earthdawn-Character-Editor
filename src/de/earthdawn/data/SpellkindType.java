//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: see version control commit date
//


package de.earthdawn.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for spellkind_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="spellkind_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="elemental"/>
 *     &lt;enumeration value="nether"/>
 *     &lt;enumeration value="illusion"/>
 *     &lt;enumeration value="wizard"/>
 *     &lt;enumeration value="shamane"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "spellkind_type")
@XmlEnum
public enum SpellkindType {

    @XmlEnumValue("elemental")
    ELEMENTAL("elemental"),
    @XmlEnumValue("nether")
    NETHER("nether"),
    @XmlEnumValue("illusion")
    ILLUSION("illusion"),
    @XmlEnumValue("wizard")
    WIZARD("wizard"),
    @XmlEnumValue("shamane")
    SHAMANE("shamane");
    private final String value;

    SpellkindType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SpellkindType fromValue(String v) {
        for (SpellkindType c: SpellkindType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
