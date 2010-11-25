//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.25 at 10:51:50 AM MEZ 
//


package de.earthdawn.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.earthdawn.data package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EDCHARAKTERWEAPON_QNAME = new QName("http://earthdawn.de", "WEAPON");
    private final static QName _EDCHARAKTERINITIATIVE_QNAME = new QName("http://earthdawn.de", "INITIATIVE");
    private final static QName _EDCHARAKTERCOMMENT_QNAME = new QName("http://earthdawn.de", "COMMENT");
    private final static QName _EDCHARAKTERSHIELD_QNAME = new QName("http://earthdawn.de", "SHIELD");
    private final static QName _EDCHARAKTERSPELLS_QNAME = new QName("http://earthdawn.de", "SPELLS");
    private final static QName _EDCHARAKTERRACEABILITES_QNAME = new QName("http://earthdawn.de", "RACEABILITES");
    private final static QName _EDCHARAKTEREXPERIENCE_QNAME = new QName("http://earthdawn.de", "EXPERIENCE");
    private final static QName _EDCHARAKTERMAGICITEM_QNAME = new QName("http://earthdawn.de", "MAGICITEM");
    private final static QName _EDCHARAKTERSKILL_QNAME = new QName("http://earthdawn.de", "SKILL");
    private final static QName _EDCHARAKTERKARMARITUAL_QNAME = new QName("http://earthdawn.de", "KARMARITUAL");
    private final static QName _EDCHARAKTERDISCIPLINE_QNAME = new QName("http://earthdawn.de", "DISCIPLINE");
    private final static QName _EDCHARAKTERCOINS_QNAME = new QName("http://earthdawn.de", "COINS");
    private final static QName _EDCHARAKTERMOVEMENT_QNAME = new QName("http://earthdawn.de", "MOVEMENT");
    private final static QName _EDCHARAKTERHEALTH_QNAME = new QName("http://earthdawn.de", "HEALTH");
    private final static QName _EDCHARAKTERKARMA_QNAME = new QName("http://earthdawn.de", "KARMA");
    private final static QName _EDCHARAKTERARMOR_QNAME = new QName("http://earthdawn.de", "ARMOR");
    private final static QName _EDCHARAKTERATTRIBUTE_QNAME = new QName("http://earthdawn.de", "ATTRIBUTE");
    private final static QName _EDCHARAKTERDEFENSE_QNAME = new QName("http://earthdawn.de", "DEFENSE");
    private final static QName _EDCHARAKTERPATTERNITEM_QNAME = new QName("http://earthdawn.de", "PATTERNITEM");
    private final static QName _EDCHARAKTERTALENTS_QNAME = new QName("http://earthdawn.de", "TALENTS");
    private final static QName _EDCHARAKTERCARRYING_QNAME = new QName("http://earthdawn.de", "CARRYING");
    private final static QName _EDCHARAKTERPORTRAIT_QNAME = new QName("http://earthdawn.de", "PORTRAIT");
    private final static QName _EDCHARAKTERITEM_QNAME = new QName("http://earthdawn.de", "ITEM");
    private final static QName _EDCHARAKTERDESCRIPTION_QNAME = new QName("http://earthdawn.de", "DESCRIPTION");
    private final static QName _HEALTHTypeUNCONSCIOUSNESS_QNAME = new QName("http://earthdawn.de", "UNCONSCIOUSNESS");
    private final static QName _HEALTHTypeRECOVERY_QNAME = new QName("http://earthdawn.de", "RECOVERY");
    private final static QName _HEALTHTypeDEATH_QNAME = new QName("http://earthdawn.de", "DEATH");
    private final static QName _HEALTHTypeWOUNDS_QNAME = new QName("http://earthdawn.de", "WOUNDS");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.earthdawn.data
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ITEMType }
     * 
     */
    public ITEMType createITEMType() {
        return new ITEMType();
    }

    /**
     * Create an instance of {@link CARRYINGType }
     * 
     */
    public CARRYINGType createCARRYINGType() {
        return new CARRYINGType();
    }

    /**
     * Create an instance of {@link THREADRANKType }
     * 
     */
    public THREADRANKType createTHREADRANKType() {
        return new THREADRANKType();
    }

    /**
     * Create an instance of {@link INITIATIVEType }
     * 
     */
    public INITIATIVEType createINITIATIVEType() {
        return new INITIATIVEType();
    }

    /**
     * Create an instance of {@link ATTRIBUTEType }
     * 
     */
    public ATTRIBUTEType createATTRIBUTEType() {
        return new ATTRIBUTEType();
    }

    /**
     * Create an instance of {@link HEALTHType }
     * 
     */
    public HEALTHType createHEALTHType() {
        return new HEALTHType();
    }

    /**
     * Create an instance of {@link DEFENSEType }
     * 
     */
    public DEFENSEType createDEFENSEType() {
        return new DEFENSEType();
    }

    /**
     * Create an instance of {@link EDCHARAKTER }
     * 
     */
    public EDCHARAKTER createEDCHARAKTER() {
        return new EDCHARAKTER();
    }

    /**
     * Create an instance of {@link DEATHType }
     * 
     */
    public DEATHType createDEATHType() {
        return new DEATHType();
    }

    /**
     * Create an instance of {@link EXPERIENCEType }
     * 
     */
    public EXPERIENCEType createEXPERIENCEType() {
        return new EXPERIENCEType();
    }

    /**
     * Create an instance of {@link PATTERNITEMType }
     * 
     */
    public PATTERNITEMType createPATTERNITEMType() {
        return new PATTERNITEMType();
    }

    /**
     * Create an instance of {@link SHIELDType }
     * 
     */
    public SHIELDType createSHIELDType() {
        return new SHIELDType();
    }

    /**
     * Create an instance of {@link ARMORType }
     * 
     */
    public ARMORType createARMORType() {
        return new ARMORType();
    }

    /**
     * Create an instance of {@link WEAPONType }
     * 
     */
    public WEAPONType createWEAPONType() {
        return new WEAPONType();
    }

    /**
     * Create an instance of {@link TALENTType }
     * 
     */
    public TALENTType createTALENTType() {
        return new TALENTType();
    }

    /**
     * Create an instance of {@link SPELLSType }
     * 
     */
    public SPELLSType createSPELLSType() {
        return new SPELLSType();
    }

    /**
     * Create an instance of {@link DISCIPLINEType }
     * 
     */
    public DISCIPLINEType createDISCIPLINEType() {
        return new DISCIPLINEType();
    }

    /**
     * Create an instance of {@link SPELLType }
     * 
     */
    public SPELLType createSPELLType() {
        return new SPELLType();
    }

    /**
     * Create an instance of {@link WOUNDType }
     * 
     */
    public WOUNDType createWOUNDType() {
        return new WOUNDType();
    }

    /**
     * Create an instance of {@link SKILLType }
     * 
     */
    public SKILLType createSKILLType() {
        return new SKILLType();
    }

    /**
     * Create an instance of {@link RECOVERYType }
     * 
     */
    public RECOVERYType createRECOVERYType() {
        return new RECOVERYType();
    }

    /**
     * Create an instance of {@link KARMAType }
     * 
     */
    public KARMAType createKARMAType() {
        return new KARMAType();
    }

    /**
     * Create an instance of {@link TALENTSType }
     * 
     */
    public TALENTSType createTALENTSType() {
        return new TALENTSType();
    }

    /**
     * Create an instance of {@link MOVEMENTType }
     * 
     */
    public MOVEMENTType createMOVEMENTType() {
        return new MOVEMENTType();
    }

    /**
     * Create an instance of {@link LEGENDPOINTSType }
     * 
     */
    public LEGENDPOINTSType createLEGENDPOINTSType() {
        return new LEGENDPOINTSType();
    }

    /**
     * Create an instance of {@link MAGICITEMType }
     * 
     */
    public MAGICITEMType createMAGICITEMType() {
        return new MAGICITEMType();
    }

    /**
     * Create an instance of {@link COINSType }
     * 
     */
    public COINSType createCOINSType() {
        return new COINSType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WEAPONType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "WEAPON", scope = EDCHARAKTER.class)
    public JAXBElement<WEAPONType> createEDCHARAKTERWEAPON(WEAPONType value) {
        return new JAXBElement<WEAPONType>(_EDCHARAKTERWEAPON_QNAME, WEAPONType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link INITIATIVEType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "INITIATIVE", scope = EDCHARAKTER.class)
    public JAXBElement<INITIATIVEType> createEDCHARAKTERINITIATIVE(INITIATIVEType value) {
        return new JAXBElement<INITIATIVEType>(_EDCHARAKTERINITIATIVE_QNAME, INITIATIVEType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "COMMENT", scope = EDCHARAKTER.class)
    public JAXBElement<String> createEDCHARAKTERCOMMENT(String value) {
        return new JAXBElement<String>(_EDCHARAKTERCOMMENT_QNAME, String.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SHIELDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "SHIELD", scope = EDCHARAKTER.class)
    public JAXBElement<SHIELDType> createEDCHARAKTERSHIELD(SHIELDType value) {
        return new JAXBElement<SHIELDType>(_EDCHARAKTERSHIELD_QNAME, SHIELDType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SPELLSType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "SPELLS", scope = EDCHARAKTER.class)
    public JAXBElement<SPELLSType> createEDCHARAKTERSPELLS(SPELLSType value) {
        return new JAXBElement<SPELLSType>(_EDCHARAKTERSPELLS_QNAME, SPELLSType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "RACEABILITES", scope = EDCHARAKTER.class)
    public JAXBElement<String> createEDCHARAKTERRACEABILITES(String value) {
        return new JAXBElement<String>(_EDCHARAKTERRACEABILITES_QNAME, String.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EXPERIENCEType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "EXPERIENCE", scope = EDCHARAKTER.class)
    public JAXBElement<EXPERIENCEType> createEDCHARAKTEREXPERIENCE(EXPERIENCEType value) {
        return new JAXBElement<EXPERIENCEType>(_EDCHARAKTEREXPERIENCE_QNAME, EXPERIENCEType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MAGICITEMType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "MAGICITEM", scope = EDCHARAKTER.class)
    public JAXBElement<MAGICITEMType> createEDCHARAKTERMAGICITEM(MAGICITEMType value) {
        return new JAXBElement<MAGICITEMType>(_EDCHARAKTERMAGICITEM_QNAME, MAGICITEMType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SKILLType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "SKILL", scope = EDCHARAKTER.class)
    public JAXBElement<SKILLType> createEDCHARAKTERSKILL(SKILLType value) {
        return new JAXBElement<SKILLType>(_EDCHARAKTERSKILL_QNAME, SKILLType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "KARMARITUAL", scope = EDCHARAKTER.class)
    public JAXBElement<String> createEDCHARAKTERKARMARITUAL(String value) {
        return new JAXBElement<String>(_EDCHARAKTERKARMARITUAL_QNAME, String.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DISCIPLINEType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "DISCIPLINE", scope = EDCHARAKTER.class)
    public JAXBElement<DISCIPLINEType> createEDCHARAKTERDISCIPLINE(DISCIPLINEType value) {
        return new JAXBElement<DISCIPLINEType>(_EDCHARAKTERDISCIPLINE_QNAME, DISCIPLINEType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link COINSType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "COINS", scope = EDCHARAKTER.class)
    public JAXBElement<COINSType> createEDCHARAKTERCOINS(COINSType value) {
        return new JAXBElement<COINSType>(_EDCHARAKTERCOINS_QNAME, COINSType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MOVEMENTType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "MOVEMENT", scope = EDCHARAKTER.class)
    public JAXBElement<MOVEMENTType> createEDCHARAKTERMOVEMENT(MOVEMENTType value) {
        return new JAXBElement<MOVEMENTType>(_EDCHARAKTERMOVEMENT_QNAME, MOVEMENTType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HEALTHType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "HEALTH", scope = EDCHARAKTER.class)
    public JAXBElement<HEALTHType> createEDCHARAKTERHEALTH(HEALTHType value) {
        return new JAXBElement<HEALTHType>(_EDCHARAKTERHEALTH_QNAME, HEALTHType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KARMAType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "KARMA", scope = EDCHARAKTER.class)
    public JAXBElement<KARMAType> createEDCHARAKTERKARMA(KARMAType value) {
        return new JAXBElement<KARMAType>(_EDCHARAKTERKARMA_QNAME, KARMAType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ARMORType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "ARMOR", scope = EDCHARAKTER.class)
    public JAXBElement<ARMORType> createEDCHARAKTERARMOR(ARMORType value) {
        return new JAXBElement<ARMORType>(_EDCHARAKTERARMOR_QNAME, ARMORType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ATTRIBUTEType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "ATTRIBUTE", scope = EDCHARAKTER.class)
    public JAXBElement<ATTRIBUTEType> createEDCHARAKTERATTRIBUTE(ATTRIBUTEType value) {
        return new JAXBElement<ATTRIBUTEType>(_EDCHARAKTERATTRIBUTE_QNAME, ATTRIBUTEType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DEFENSEType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "DEFENSE", scope = EDCHARAKTER.class)
    public JAXBElement<DEFENSEType> createEDCHARAKTERDEFENSE(DEFENSEType value) {
        return new JAXBElement<DEFENSEType>(_EDCHARAKTERDEFENSE_QNAME, DEFENSEType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PATTERNITEMType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "PATTERNITEM", scope = EDCHARAKTER.class)
    public JAXBElement<PATTERNITEMType> createEDCHARAKTERPATTERNITEM(PATTERNITEMType value) {
        return new JAXBElement<PATTERNITEMType>(_EDCHARAKTERPATTERNITEM_QNAME, PATTERNITEMType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TALENTSType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "TALENTS", scope = EDCHARAKTER.class)
    public JAXBElement<TALENTSType> createEDCHARAKTERTALENTS(TALENTSType value) {
        return new JAXBElement<TALENTSType>(_EDCHARAKTERTALENTS_QNAME, TALENTSType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CARRYINGType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "CARRYING", scope = EDCHARAKTER.class)
    public JAXBElement<CARRYINGType> createEDCHARAKTERCARRYING(CARRYINGType value) {
        return new JAXBElement<CARRYINGType>(_EDCHARAKTERCARRYING_QNAME, CARRYINGType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "PORTRAIT", scope = EDCHARAKTER.class)
    public JAXBElement<byte[]> createEDCHARAKTERPORTRAIT(byte[] value) {
        return new JAXBElement<byte[]>(_EDCHARAKTERPORTRAIT_QNAME, byte[].class, EDCHARAKTER.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ITEMType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "ITEM", scope = EDCHARAKTER.class)
    public JAXBElement<ITEMType> createEDCHARAKTERITEM(ITEMType value) {
        return new JAXBElement<ITEMType>(_EDCHARAKTERITEM_QNAME, ITEMType.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "DESCRIPTION", scope = EDCHARAKTER.class)
    public JAXBElement<String> createEDCHARAKTERDESCRIPTION(String value) {
        return new JAXBElement<String>(_EDCHARAKTERDESCRIPTION_QNAME, String.class, EDCHARAKTER.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DEATHType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "UNCONSCIOUSNESS", scope = HEALTHType.class)
    public JAXBElement<DEATHType> createHEALTHTypeUNCONSCIOUSNESS(DEATHType value) {
        return new JAXBElement<DEATHType>(_HEALTHTypeUNCONSCIOUSNESS_QNAME, DEATHType.class, HEALTHType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RECOVERYType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "RECOVERY", scope = HEALTHType.class)
    public JAXBElement<RECOVERYType> createHEALTHTypeRECOVERY(RECOVERYType value) {
        return new JAXBElement<RECOVERYType>(_HEALTHTypeRECOVERY_QNAME, RECOVERYType.class, HEALTHType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DEATHType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "DEATH", scope = HEALTHType.class)
    public JAXBElement<DEATHType> createHEALTHTypeDEATH(DEATHType value) {
        return new JAXBElement<DEATHType>(_HEALTHTypeDEATH_QNAME, DEATHType.class, HEALTHType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WOUNDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://earthdawn.de", name = "WOUNDS", scope = HEALTHType.class)
    public JAXBElement<WOUNDType> createHEALTHTypeWOUNDS(WOUNDType value) {
        return new JAXBElement<WOUNDType>(_HEALTHTypeWOUNDS_QNAME, WOUNDType.class, HEALTHType.class, value);
    }

}
