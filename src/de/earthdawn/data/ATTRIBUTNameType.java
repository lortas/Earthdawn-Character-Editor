//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.26 at 10:48:51 PM MESZ 
//


package de.earthdawn.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ATTRIBUT_name_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ATTRIBUT_name_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Charisma"/>
 *     &lt;enumeration value="Geschicklichkeit"/>
 *     &lt;enumeration value="St�rke"/>
 *     &lt;enumeration value="Wahrnehmung"/>
 *     &lt;enumeration value="Willenskraft"/>
 *     &lt;enumeration value="Z�higkeit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ATTRIBUT_name_type")
@XmlEnum
public enum ATTRIBUTNameType {

    @XmlEnumValue("Charisma")
    CHARISMA("Charisma"),
    @XmlEnumValue("Geschicklichkeit")
    GESCHICKLICHKEIT("Geschicklichkeit"),
    @XmlEnumValue("St\u00e4rke")
    STAERKE("St\u00e4rke"),
    @XmlEnumValue("Wahrnehmung")
    WAHRNEHMUNG("Wahrnehmung"),
    @XmlEnumValue("Willenskraft")
    WILLENSKRAFT("Willenskraft"),
    @XmlEnumValue("Z\u00e4higkeit")
    ZAEHIGKEIT("Z\u00e4higkeit");
    private final String value;

    ATTRIBUTNameType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ATTRIBUTNameType fromValue(String v) {
        for (ATTRIBUTNameType c: ATTRIBUTNameType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
