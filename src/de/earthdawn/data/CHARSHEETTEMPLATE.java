//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
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
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="CurrentDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DisciplineName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DisciplineCircle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Race" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Age" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Eyes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Hair" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Height" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Skin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Passion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Player" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeBaseDex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeBaseStr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeBaseTou" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeBasePer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeBaseWil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeBaseCha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeCurrentDex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeCurrentStr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeCurrentTou" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeCurrentPer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeCurrentWil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeCurrentCha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeStepDex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeStepStr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeStepTou" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeStepPer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeStepWil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeStepCha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeDiceDex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeDiceStr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeDiceTou" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeDicePer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeDiceWil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeDiceCha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LpincreaseDex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LpincreaseStr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LpincreaseTou" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LpincreasePer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LpincreaseWil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LpincreaseCha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DefencePhysical" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DefenceSocial" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DefenceMystic" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeathAdjustment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeathBase" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeathValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnconsciousnessAdjustment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnconsciousnessBase" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnconsciousnessValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RecoveryStep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RecoveryDice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RecoveryTestsperday" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WoundThreshold" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BloodWound" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LegendPointsTotal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LegendPointsCurrent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LegendPointsRenown" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LegendPointsReputation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InitiativeDice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InitiativeStep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Talent" type="{http://earthdawn.com/charsheettemplate}charsheettemplatetalent_type"/>
 *         &lt;element name="Skill" type="{http://earthdawn.com/charsheettemplate}charsheettemplatetalent_type"/>
 *         &lt;element name="HealthDamage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KarmaCurrent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KarmaMax" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Movement" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Carrying" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArmorMystic" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArmorPhysical" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShieldMystic" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShieldPhysical" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArmorPenalty" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DisciplineBonus" type="{http://earthdawn.com/charsheettemplate}charsheettemplatedisciplinebonus_type"/>
 *         &lt;element name="WeaponName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponDamagestep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponSize" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponTimesforged" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponShortrange" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponLongrange" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponAttackstep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WeaponAttribute" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *       &lt;attribute name="menuentryname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="filename" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "currentDateTimeOrNameOrDisciplineName"
})
@XmlRootElement(name = "CHARSHEETTEMPLATE", namespace = "http://earthdawn.com/charsheettemplate")
public class CHARSHEETTEMPLATE {

    @XmlElementRefs({
        @XmlElementRef(name = "Name", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ShieldMystic", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "InitiativeDice", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeCurrentCha", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeCurrentTou", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UnconsciousnessValue", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DeathValue", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "RecoveryStep", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LpincreaseStr", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DisciplineCircle", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LpincreaseDex", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LegendPointsTotal", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Hair", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeStepStr", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Weight", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeCurrentDex", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DefencePhysical", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeDiceCha", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Player", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Gender", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ShieldPhysical", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DisciplineBonus", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeBaseDex", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeStepDex", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeCurrentWil", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Skin", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeStepCha", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LpincreaseWil", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LegendPointsRenown", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UnconsciousnessBase", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeBasePer", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponTimesforged", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ArmorMystic", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LegendPointsCurrent", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeDiceTou", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeDicePer", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponAttackstep", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Skill", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeStepWil", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "KarmaMax", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeCurrentStr", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WoundThreshold", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeBaseStr", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DefenceMystic", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ArmorPhysical", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeStepPer", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "BloodWound", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeBaseWil", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LpincreaseCha", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "KarmaCurrent", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DisciplineName", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Race", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CurrentDateTime", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Eyes", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "HealthDamage", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponShortrange", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponSize", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeDiceDex", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ArmorPenalty", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeStepTou", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Carrying", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeCurrentPer", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Talent", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Height", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "RecoveryTestsperday", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeDiceStr", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponLongrange", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DefenceSocial", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "RecoveryDice", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Movement", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeBaseCha", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponName", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LpincreaseTou", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Passion", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponDamagestep", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Age", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeDiceWil", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "UnconsciousnessAdjustment", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AttributeBaseTou", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DeathAdjustment", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LpincreasePer", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DeathBase", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "InitiativeStep", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LegendPointsReputation", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "WeaponAttribute", namespace = "http://earthdawn.com/charsheettemplate", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> currentDateTimeOrNameOrDisciplineName;
    @XmlAttribute(name = "menuentryname", required = true)
    protected String menuentryname;
    @XmlAttribute(name = "filename", required = true)
    protected String filename;

    /**
     * Gets the value of the currentDateTimeOrNameOrDisciplineName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the currentDateTimeOrNameOrDisciplineName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCurrentDateTimeOrNameOrDisciplineName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link CharsheettemplatedisciplinebonusType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link CharsheettemplatetalentType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link CharsheettemplatetalentType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getCurrentDateTimeOrNameOrDisciplineName() {
        if (currentDateTimeOrNameOrDisciplineName == null) {
            currentDateTimeOrNameOrDisciplineName = new ArrayList<JAXBElement<?>>();
        }
        return this.currentDateTimeOrNameOrDisciplineName;
    }

    /**
     * Gets the value of the menuentryname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMenuentryname() {
        return menuentryname;
    }

    /**
     * Sets the value of the menuentryname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMenuentryname(String value) {
        this.menuentryname = value;
    }

    /**
     * Gets the value of the filename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of the filename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

}
