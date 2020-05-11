package de.earthdawn;
/******************************************************************************\
Copyright (C) 2010-2011  Holger von Rhein <lortas@freenet.de>

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
\******************************************************************************/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.itextpdf.text.pdf.codec.Base64;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECharacteristics;
import de.earthdawn.data.*;
import de.earthdawn.event.CharChangeRefresh;
import de.earthdawn.namegenerator.NameGenerator;
import java.util.Arrays;
import java.util.LinkedList;

public class CharacterContainer extends CharChangeRefresh {
	private EDCHARACTER character = null;
	private static Random rand = new Random();
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final ECECharacteristics PROPERTIES_Characteristics= PROPERTIES.getCharacteristics();
	public static MOVEMENTATTRIBUTENameType OptionalRule_AttributeBasedMovement=PROPERTIES.getOptionalRules().getATTRIBUTEBASEDMOVEMENT().getAttribute();
	public static final String threadWeavingName = PROPERTIES.getThreadWeavingName();
	public static final String durabilityName = PROPERTIES.getDurabilityName();
	public static String DATEFORMAT = PROPERTIES.getOptionalRules().getDATEFORMAT();
	public static final String languageSkillSpeakName = PROPERTIES.getLanguageSkillSpeakName();
	public static final String languageSkillReadWriteName = PROPERTIES.getLanguageSkillReadWriteName();

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public CharacterContainer() {
		character = new EDCHARACTER();
		character.setRulesetversion(PROPERTIES.getRulesetLanguage().getRulesetversion());
		character.setLang(PROPERTIES.getRulesetLanguage().getLanguage());
	}

	public CharacterContainer(EDCHARACTER c) {
		character = c;
		if( ! PROPERTIES.getRulesetLanguage().getRulesetversion().equals(character.getRulesetversion()) ) {
			throw new RuntimeException("The character '"+character.getName()+"' has wrong Rulesetversion: '"+character.getRulesetversion().value()+"' != '"+PROPERTIES.getRulesetLanguage().getRulesetversion()+"'");
		}
		if( ! PROPERTIES.getRulesetLanguage().getLanguage().equals(character.getLang()) ) {
			throw new RuntimeException("The character '"+character.getName()+"' has wrong language: '"+character.getLang().value()+"' != '"+PROPERTIES.getRulesetLanguage().getLanguage()+"'");
		}
	}

	public CharacterContainer(File xmlfile) throws IOException, JAXBException, ParserConfigurationException, SAXException, TransformerException {
		// XML-Daten einlesen
		byte[] xmldata = new byte[(int) xmlfile.length()];
		FileInputStream xmlInputStream = new FileInputStream(xmlfile);
		xmlInputStream.read(xmldata);
		xmlInputStream.close();
		this.character=readCharacterFromXml(xmldata);
		if( ! PROPERTIES.getRulesetLanguage().getRulesetversion().equals(character.getRulesetversion()) ) {
			throw new RuntimeException("The file '"+xmlfile.getCanonicalPath()+"' has wrong Rulesetversion: '"+character.getRulesetversion().value()+"' != '"+PROPERTIES.getRulesetLanguage().getRulesetversion()+"'");
		}
		if( ! PROPERTIES.getRulesetLanguage().getLanguage().equals(character.getLang()) ) {
			throw new RuntimeException("The file '"+xmlfile.getCanonicalPath()+"' has wrong language: '"+character.getLang().value()+"' != '"+PROPERTIES.getRulesetLanguage().getLanguage()+"'");
		}
	}

	public CharacterContainer(byte[] xmldata) throws IOException, JAXBException, ParserConfigurationException, SAXException, TransformerException {
		this.character=readCharacterFromXml(xmldata);
		if( ! PROPERTIES.getRulesetLanguage().getRulesetversion().equals(character.getRulesetversion()) ) {
			throw new RuntimeException("The character xml has wrong Rulesetversion: '"+character.getRulesetversion().value()+"' != '"+PROPERTIES.getRulesetLanguage().getRulesetversion()+"'");
		}
		if( ! PROPERTIES.getRulesetLanguage().getLanguage().equals(character.getLang()) ) {
			throw new RuntimeException("The character xml has wrong language: '"+character.getLang().value()+"' != '"+PROPERTIES.getRulesetLanguage().getLanguage()+"'");
		}
	}

	static private EDCHARACTER readCharacterFromXml(byte[] xmldata) throws IOException, JAXBException, ParserConfigurationException, SAXException, TransformerException {
		// XML-Daten unge'typ't parsen
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new ByteArrayInputStream(xmldata));

		// XML Root Element ermitteln
		Node rootnode = doc.getLastChild();
		if( rootnode == null ) {
			throw new RuntimeException("XML has no root element");
		}
		String[] rootnames = rootnode.getNodeName().split(":");
		String rootname;
		if( rootnames.length>1 ) {
			rootname=rootnames[1];
		} else {
			rootname=rootnames[0];
		}
		if( ! rootname.equals("EDCHARACTER") ) {
			throw new RuntimeException("XML root element '"+rootname+"' is not 'EDCHARACTER'");
		}
		Node version = rootnode.getAttributes().getNamedItem("xsd-version");
		String value = (version==null)?"":version.getNodeValue();
		if( value.isEmpty() ) {
			JOptionPane.showMessageDialog(null, "No xsd-version set. Try to read it as like xsd-version 1.0. Procced at your own risk");
			value="1.0";
		}
		if( value.equals("1.0") ) {
			// XSLT Transformation
			System.out.println("XML mit xsd-version 1.0, transformiere nach aktuellem Schema");
			TransformerFactory tFactory = TransformerFactory.newInstance();
			try {
				Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource("config/convertcharacter_1.0.xsl"));
				ByteArrayOutputStream xmldataNew = new ByteArrayOutputStream();
				transformer.transform(new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xmldata)),new javax.xml.transform.stream.StreamResult(xmldataNew));
				xmldata=xmldataNew.toByteArray();
			} catch (TransformerException e) {
				System.err.print("Transformation alter (1.0) XML-character Version fehlgeschlagen : ");
				System.err.println(e.getLocalizedMessage());
			}
		} else if( value.equals("1.1") ) {
			// Nothing ToDo. This is most current xsd version.
		} else {
			JOptionPane.showMessageDialog(null, "Unknown xsd-version '"+value+"'. But try to read anyway. Procced at your own risk");
		}
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Unmarshaller u = jc.createUnmarshaller();
		return (EDCHARACTER)u.unmarshal(new ByteArrayInputStream(xmldata));
	}

	public void setEDCHARACTER(EDCHARACTER c) {
		character = c;
		if( ! PROPERTIES.getRulesetLanguage().getRulesetversion().equals(character.getRulesetversion()) ) {
			throw new RuntimeException("The character '"+character.getName()+"' has wrong Rulesetversion: '"+character.getRulesetversion().value()+"' != '"+PROPERTIES.getRulesetLanguage().getRulesetversion()+"'");
		}
		if( ! PROPERTIES.getRulesetLanguage().getLanguage().equals(character.getLang()) ) {
			throw new RuntimeException("The character '"+character.getName()+"' has wrong language: '"+character.getLang().value()+"' != '"+PROPERTIES.getRulesetLanguage().getLanguage()+"'");
		}
	}

	public void writeXml(OutputStream out,String encoding) throws JAXBException, UnsupportedEncodingException {
		PrintStream fileio = new PrintStream(out, false, encoding);
		fileio.println("<?xml version=\"1.0\" encoding=\""+encoding+"\" standalone=\"no\"?>");
		fileio.println("<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>");
		// Lösche XSD-Version
		character.setXsdVersion(null);
		// Sieht komisch aus, setzt aber die Default/Fixed XSD-Version
		character.setXsdVersion(character.getXsdVersion());
		toXml(encoding).marshal(character,fileio);
		fileio.close();
	}

	public void writeHtml(OutputStream out,String encoding) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			writeXml(baos, encoding);
		} catch (UnsupportedEncodingException | JAXBException e) {
			System.err.println(e.getLocalizedMessage());
			return;
		}
		String htmlstring="";
		// Transformiere das Character XML mit Hilfe des XSLT nach HTML
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource("config/earthdawncharacter.xsl"));
			transformer.setParameter("lang",character.getLang().value());
			ByteArrayOutputStream html = new ByteArrayOutputStream();
			transformer.transform(new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(baos.toByteArray())),new javax.xml.transform.stream.StreamResult(html));
			htmlstring=html.toString(encoding);
		} catch (TransformerException | UnsupportedEncodingException e) {
			System.err.println(e.getLocalizedMessage());
			return;
		}
		try {
			// Lese CSS ein um die referenz im HTML durch inline zu ersetzen
			File cssfile=new File("config/earthdawncharacter.css");
			byte[] cssdata = new byte[(int) cssfile.length()];
			FileInputStream cssio = new FileInputStream(cssfile);
			cssio.read(cssdata);
			cssio.close();
			// Ersetzte CSS link durch CSS inline
			htmlstring=htmlstring.replaceAll("<link *rel=\"stylesheet\" *type=\"text/css\" *href=\"earthdawncharacter\\.css\" */?>", "<style type=\"text/css\">"+new String(cssdata)+"</style>");
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		// Entferne XSLT Fehler
		htmlstring=htmlstring.replaceAll("(%0A|%09)*;base64,(%0A|%09)*", ";base64,");
		// Ersetze alle Icon Links durch inline Bilder
		for(File iconfile : (new File("icons")).listFiles()) {
			if( iconfile.getName().endsWith(".png") ) {
				try {
					htmlstring=htmlstring.replace(iconfile.toURI().getRawPath(),"data:image/png;base64,"+Base64.encodeFromFile(iconfile.getCanonicalPath()).replace("\r", "").replace("\n", ""));
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		//Schreibe Ergebnis weg
		try {
			PrintStream fileio = new PrintStream(out, false, encoding);
			fileio.print(htmlstring);
			fileio.close();
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		}
	}

	public Marshaller toXml(String encoding) throws JAXBException {
		character.setEditorpath((new File("")).toURI().getRawPath());
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, encoding);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
		m.setProperty(Marshaller.JAXB_FRAGMENT, true);
		return m;
	}

	public EDCHARACTER getEDCHARACTER() {
		return character;
	}

	public String getName() {
		String name = character.getName();
		if( name == null ) return "";
		return name;
	}

	public String setRandomName() {
		final APPEARANCEType appearance = getAppearance();
		final String race = appearance.getRace();
		final GenderType gender = appearance.getGender();
		NameGenerator namegenerator = new NameGenerator(PROPERTIES.getRandomNamesByRaces());
		String name = namegenerator.generateName(race,gender);
		//String name = namegenerator.generateName2(race,gender);
		//String name = namegenerator.generateName3(race,gender);
		character.setName(name);
		return name;
	}

	public String getPlayer() {
		String player = character.getPlayer();
		if( player == null ) return "";
		return player;
	}

	public APPEARANCEType getAppearance() {
		APPEARANCEType appearance = character.getAPPEARANCE();
		if( appearance == null ) {
			appearance = new APPEARANCEType();
			character.setAPPEARANCE(appearance);
		}
		if( appearance.getRace() == null || appearance.getRace().isEmpty() ) appearance.setRace(PROPERTIES.translateNamegiver("Human",LanguageType.EN));
		if( appearance.getOrigin() == null || appearance.getOrigin().isEmpty() ) appearance.setOrigin("Barsaive");
		if( appearance.getEyes() == null || appearance.getEyes().isEmpty() ) appearance.setEyes("brown");
		if( appearance.getEyes() == null || appearance.getEyes().isEmpty() ) appearance.setHair("black");
		if( appearance.getEyes() == null || appearance.getEyes().isEmpty() ) appearance.setSkin("caucasian");
		if( appearance.getGender() == null ) appearance.setGender(GenderType.MALE);
		return appearance;
	}

	public Map<ATTRIBUTENameType, ATTRIBUTEType> getAttributes() {
		List<ATTRIBUTEType> attributelist = character.getATTRIBUTE();
		Map<ATTRIBUTENameType,ATTRIBUTEType> attributes = new TreeMap<ATTRIBUTENameType,ATTRIBUTEType>();
		for (ATTRIBUTEType attribute : attributelist ) {
			attributes.put(attribute.getName(), attribute);
		}
		for( ATTRIBUTENameType name : ATTRIBUTENameType.values() ) {
			// Wenn das Attribut bereits exisitert, kein neues Setzen
			if( attributes.containsKey(name)) continue;
			// Das "Attribut" na soll nicht angefügt werden
			if( name.equals(ATTRIBUTENameType.NA) ) continue;
			ATTRIBUTEType attribute = new ATTRIBUTEType();
			attribute.setName(name);
			attributelist.add(attribute);
			attributes.put(attribute.getName(), attribute);
		}
		return attributes;
	}

	public int getAttributesCost() {
		int result = 0;
		for (ATTRIBUTEType attribute : character.getATTRIBUTE() ) {
			// Das "Attribut" na soll nicht beachtet werden
			if( attribute.getName().equals(ATTRIBUTENameType.NA) ) continue;
			result += attribute.getCost();
		}
		return result;
	}

	public DefenseAbility getDefence() {
	    return new DefenseAbility(character.getDEFENSE());
	}

	public CALCULATEDLEGENDPOINTSType getCalculatedLegendpoints() {
		CALCULATEDLEGENDPOINTSType calculatedLegendpoints = character.getCALCULATEDLEGENDPOINTS();
		if( calculatedLegendpoints == null ) {
			calculatedLegendpoints = new CALCULATEDLEGENDPOINTSType();
			character.setCALCULATEDLEGENDPOINTS(calculatedLegendpoints);
		}
		return calculatedLegendpoints;
	}

	public void addLegendPointsSpent(CALCULATEDLEGENDPOINTSType oldLP) {
		CALCULATEDLEGENDPOINTSType calculatedLP = getCalculatedLegendpoints();
		List<ACCOUNTINGType> legendpoints = getLegendPoints().getLEGENDPOINTS();
		String currentDateTime=getCurrentDateTime();
		String comment = "-LP spent (automaticly added by ECE)";
		int diff=0;

		diff=calculatedLP.getAttributes()-oldLP.getAttributes();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Attribute"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getDisciplinetalents()-oldLP.getDisciplinetalents();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Disciplinetalents"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getKarma()-oldLP.getKarma();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Karma"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getKnacks()-oldLP.getKnacks();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Knacks"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getMagicitems()-oldLP.getMagicitems();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Magicitems"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getOptionaltalents()-oldLP.getOptionaltalents();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Optionaltalents"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getSkills()-oldLP.getSkills();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Skills"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getSpells()-oldLP.getSpells();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Spells"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
	}

	public void calculateLegendPointsAndStatus() {
		EXPERIENCEType legendpoints = getLegendPoints();
		int[] lp = CharacterContainer.calculateAccounting(legendpoints.getLEGENDPOINTS());
		legendpoints.setCurrentlegendpoints(lp[0]-lp[1]);
		legendpoints.setTotallegendpoints(lp[0]);
		CHARACTERISTICSLEGENDARYSTATUS legendstatus = ApplicationProperties.create().getCharacteristics().getLegendaystatus(getDisciplineMaxCircle().getCircle());
		legendpoints.setRenown(legendstatus.getReown());
		legendpoints.setReputation(legendstatus.getReputation());
	}

	public void resetInitiative(STEPDICEType value) {
		INITIATIVEType initiative = new INITIATIVEType();
		initiative.setArmorpenalty(0);
		initiative.setModification(0);
		initiative.setBase(value.getStep());
		initiative.setStep(value.getStep());
		initiative.setDice(value.getDice());
		character.setINITIATIVE(initiative);
	}

	public INITIATIVEType getInitiative() {
		INITIATIVEType initiative = character.getINITIATIVE();
		if( initiative == null ) {
			initiative = new INITIATIVEType();
			character.setINITIATIVE(initiative);
		}
		return initiative;
	}

	public HEALTHType getHealth() {
		HEALTHType health = character.getHEALTH();
		if( health == null ) {
			health = new HEALTHType();
			character.setHEALTH(health);
		}
		return health;
	}

	public DEATHType getDeath() {
		HEALTHType health = getHealth();
		DEATHType death = health.getDEATH();
		if( death == null ) {
			death = new DEATHType();
			health.setDEATH(death);
		}
		return death;
	}

	public DEATHType getUnconsciousness() {
		HEALTHType health = getHealth();
		DEATHType unconsciousness = health.getUNCONSCIOUSNESS();
		if( unconsciousness == null ) {
			unconsciousness = new DEATHType();
			health.setUNCONSCIOUSNESS(unconsciousness);
		}
		return unconsciousness;
	}

	public WOUNDType getWound() {
		HEALTHType health = getHealth();
		WOUNDType wound = health.getWOUNDS();
		if( wound == null ) {
			wound = new WOUNDType();
			health.setWOUNDS(wound);
		}
		return wound;
	}

	public RECOVERYType getRecovery() {
		HEALTHType health = getHealth();
		RECOVERYType recovery = health.getRECOVERY();
		if( recovery == null ) {
			recovery = new RECOVERYType();
			health.setRECOVERY(recovery);
		}
		return recovery;
	}

	public KARMAType getKarma() {
		KARMAType karma = character.getKARMA();
		if( karma == null ) {
			karma = new KARMAType();
			character.setKARMA(karma);
		}
		return karma;
	}

	public MOVEMENTType getMovement() {
		MOVEMENTType movement = character.getMOVEMENT();
		if( movement == null ) {
			movement = new MOVEMENTType();
			character.setMOVEMENT(movement);
		}
		return movement;
	}

	public String getMovementAsString() {
		MOVEMENTType movement = getMovement();
		if( movement.getFlight() > 0 ) {
			return movement.getGroundString() + " / " + movement.getFlightString();
		} else {
			return movement.getGroundString();
		}
	}

	public CARRYINGType getCarrying() {
		CARRYINGType carrying = character.getCARRYING();
		if( carrying == null ) {
			carrying = new CARRYINGType();
			character.setCARRYING(carrying);
		}
		return carrying;
	}

	public PROTECTIONType getProtection() {
		PROTECTIONType protection = character.getPROTECTION();
		if ( protection == null ) {
			protection = new PROTECTIONType();
			character.setPROTECTION(protection);
		}
		return protection;
	}

	public String getAbilities() {
		String abilities = character.getRACEABILITES();
		if( abilities == null ) {
			abilities = "";
			character.setRACEABILITES(abilities);
		}
		return abilities;
	}

	public void setAbilities(String newValue) {
		character.setRACEABILITES(newValue);
	}

	public List<DISCIPLINEType> getDisciplines() {
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		CALCULATEDLEGENDPOINTSType calculatedlegendpoints = character.getCALCULATEDLEGENDPOINTS();
		// Solange die letzte Disziplin keinen Kreis hat, wird diese entfernt. 
		while( (! disciplines.isEmpty()) && (disciplines.get(disciplines.size()-1).getCircle()<1) ) {
			int disciplineNumber = disciplines.size();
			disciplines.remove(disciplineNumber-1);
			// Wenn es keine Berechneten Legendenpunkte gibt, dann ist hier auch nichts zu tun.
			if( calculatedlegendpoints == null ) continue;
			// Anpasssungen der berechneten Legendenpunkt für Talente einer Disziplin die gelöscht wird, können auch weg
			List<NEWDISCIPLINETALENTADJUSTMENTType> remove = new ArrayList<NEWDISCIPLINETALENTADJUSTMENTType>();
			List<NEWDISCIPLINETALENTADJUSTMENTType> newdisciplinetalentadjustments = calculatedlegendpoints.getNEWDISCIPLINETALENTADJUSTMENT();
			for( NEWDISCIPLINETALENTADJUSTMENTType e : newdisciplinetalentadjustments ) {
				if( e.getDisciplinenumber() == disciplineNumber ) remove.add(e);
			}
			newdisciplinetalentadjustments.removeAll(remove);
		}
		// Bei allen anderen Disziplinen die keinen Kreis haben wird dort der Kreis auf 1 hoch gesetzt.
		for (DISCIPLINEType discipline : disciplines) {
			if( discipline.getCircle() < 1 ) discipline.setCircle(1);
		}
		return disciplines;
	}

	public List<String> getDisciplineNames() {
		List<String> result = new ArrayList<String>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(discipline.getName());
		}
		return result;
	}

	public List<String> getAllKarmaritual() {
		List<String> result = new ArrayList<String>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			String karmaritual = discipline.getKARMARITUAL();
			if( karmaritual == null ) {
				result.add(discipline.getName()+": NA");
			} else {
				result.add(discipline.getName()+": "+karmaritual);
			}
		}
		return result;
	}

	public String getAllHalfMagic() {
		StringBuilder result = new StringBuilder();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			String halfmagic = discipline.getHALFMAGIC();
			result.append("[");
			result.append(discipline.getName());
			result.append("]: ");
			if( halfmagic == null ) {
				result.append("NA");
			} else {
				result.append(halfmagic);
			}
			result.append("; ");
		}
		return result.toString();
	}

	public List<Integer> getDisciplineCircles() {
		List<Integer> result = new ArrayList<Integer>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(discipline.getCircle());
		}
		return result;
	}

	public int getDisciplineOrder(String disciplinename) {
		int order=1;
		for (DISCIPLINEType discipline : getDisciplines()) {
			if( disciplinename.equals(discipline.getName()) ) return order;
			order++;
		}
		// Not found
		return 0;
	}

	public Map<String,DISCIPLINEType> getAllDisciplinesByName() {
		Map<String,DISCIPLINEType> result = new TreeMap<String,DISCIPLINEType>();
		for (DISCIPLINEType discipline : getDisciplines()) {
			result.put(discipline.getName(),discipline);
		}
		return result;
	}

	public int getCircleOf(String discipline) {
		DISCIPLINEType usedDiscipline = getAllDisciplinesByName().get(discipline);
		if( usedDiscipline == null ) {
			System.err.println("No discipline '"+discipline+"' is in use.");
			return 0;
		} else {
			return usedDiscipline.getCircle();
		}
	}

	public int getCircleOf(int disciplineNumber) {
		if( disciplineNumber < 1 ) {
			System.err.println("Discipline numer "+disciplineNumber+" is to low!");
			return 0;
		}
		List<DISCIPLINEType> disciplines = getDisciplines();
		if( disciplineNumber > disciplines.size() ) {
			System.err.println("Discipline numer "+disciplineNumber+" is to high!");
			return 0;
		}
		return disciplines.get(disciplineNumber-1).getCircle();
	}

	public DISCIPLINEType getDisciplineMaxCircle() {
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setCircle(0);
		discipline.setName("na");
		for( DISCIPLINEType d : getDisciplines() ) if( d.getCircle() > discipline.getCircle() ) discipline=d;
		return discipline;
	}

	public DISCIPLINEType getDisciplineMinCircle() {
		return getDisciplineMinCircle(0);
	}

	// Bestimme den Kleinsten Kreis einer Diszipline aber ohen die Disziplin mit der Nummer "notDiscipline"
	public DISCIPLINEType getDisciplineMinCircle(int notDiscipline) {
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setCircle(20);
		discipline.setName("na");
		int disciplinenumber=0;
		for( DISCIPLINEType d : getDisciplines() ) {
			disciplinenumber++;
			if( disciplinenumber == notDiscipline ) continue;
			if( d.getCircle() < discipline.getCircle() ) discipline=d;
		}
		return discipline;
	}

	public List<TalentsContainer> getAllTalents() {
		List<TalentsContainer> result = new ArrayList<TalentsContainer>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(new TalentsContainer(discipline));
		}
		return result;
	}

	public Map<String,TalentsContainer> getAllTalentsByDisziplinName() {
		Map<String,TalentsContainer> alltalents = new TreeMap<String,TalentsContainer>();
		for (DISCIPLINEType discipline : getDisciplines() ) {
			alltalents.put(discipline.getName(),new TalentsContainer(discipline));
		}
		return alltalents;
	}

	public List<TALENTType> getTalentByName(String searchTalent) {
		List<TALENTType> result = new ArrayList<TALENTType>();
		for (TalentsContainer talents : getAllTalents()) {
			for (TALENTType talent : talents.getAllTalents() ) {
				if ( talent.getName().equals(searchTalent)) {
					result.add(talent);
				}
			}
		}
		return result;
	}

	public TALENTType getTalentByDisciplinAndName(String disciplin, String searchTalent) {
		for( DISCIPLINEType discipline : getDisciplines() ) {
			if( ! discipline.getName().equals(disciplin) ) continue;
			TalentsContainer alltalents = new TalentsContainer(discipline);
			for (TALENTType talent : alltalents.getAllTalents()) {
				if ( talent.getName().equals(searchTalent)) return talent;
			}
		}
		System.err.println("Character does not have learned the talent '"+searchTalent+"' by the discipline '"+disciplin+"'");
		// Not found
		return null;
	}

	public Map<String,List<TALENTType>> getThreadWeavingTalents() {
		Map<String,List<TALENTType>> result = new TreeMap<String,List<TALENTType>>();
		for (DISCIPLINEType discipline : getDisciplines() ) {
			List<TALENTType> threadweaving = new ArrayList<TALENTType>();
			for(TALENTType talent : (new TalentsContainer(discipline)).getAllTalents() ) if( threadWeavingName.equals(talent.getName()) ) threadweaving.add(talent);
			result.put(discipline.getName(),threadweaving);
		}
		return result;
	}

	public List<SKILLType> getSkills() {
		List<SKILLType> skills = new ArrayList<SKILLType>();
		skills.addAll(character.getSKILL());
		int discount=1;
		for( DISCIPLINEType discipline : getDisciplines() ) {
			for( TALENTType talent : (new TalentsContainer(discipline)).getAllTalents() ) {
				SKILLType skill = talent.getALIGNEDSKILL();
				if( skill != null ) {
					if( skill.getRealigned() == 0 ) skill.setRealigned(discount);
					skills.add(skill);
				}
			}
			discount++;
		}
		// Normiere Limitation für die Skills
		for( SKILLType skill : skills ) {
			List<String> limitations = skill.getLIMITATION();
			Collections.sort(limitations);
			String limitation = join( limitations );
			limitations.clear();
			if( !limitation.isEmpty() ) limitations.add(limitation);
		}
		Collections.sort(skills, new SkillComparator());
		return skills;
	}

	static public String join( List<?> list) { return join( ", ", list ); }

	static public String join( String seperator, List<?> list) {
		Iterator<?> iterator = list.iterator();
		if( ! iterator.hasNext() ) return "";
		StringBuffer result = new StringBuffer();
		while( true ) {
			result.append(iterator.next().toString().replaceAll("\\s\\s*", " ").replaceFirst("^\\s*", "").replaceFirst("\\s*$", ""));
			if( iterator.hasNext() ) result.append(seperator);
			else return result.toString();
		}
	}

	public void addSkill(SKILLType skill) {
		character.getSKILL().add(skill);
	}

	public void removeSkill(SKILLType skill) {
		character.getSKILL().remove(skill);
	}

	public void removeSkill(List<SKILLType> skills) {
		character.getSKILL().removeAll(skills);
	}

	public List<SKILLType> getSpeakSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : getSkills() ) {
			if( languageSkillSpeakName.equals(skill.getName()) ) result.add(skill);
		}
		return result;
	}

	public List<SKILLType> getReadWriteSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : getSkills() ) {
			if( languageSkillReadWriteName.equals(skill.getName()) ) result.add(skill);
		}
		return result;
	}

	public List<SKILLType> getNonLanguageSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : getSkills() ) {
			if( languageSkillSpeakName.equals(skill.getName()) ) continue;
			if( languageSkillReadWriteName.equals(skill.getName()) ) continue;
			result.add(skill);
		}
		return result;
	}

	public List<SKILLType> getOnlyLanguageSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : getSkills() ) {
			if( languageSkillSpeakName.equals(skill.getName()) || languageSkillReadWriteName.equals(skill.getName()) ) result.add(skill);
		}
		return result;
	}

	public void updateAlignedSkills() {
		List<SKILLType> alignedskills = new ArrayList<SKILLType>();
		for( SKILLType skill : character.getSKILL() ) {
			// Skills die keinen Rang haben können übersprungen werden.
			if( (skill.getRANK()==null) || (skill.getRANK().getRank()<1) ) continue;
			String skillname=skill.getName();
			String skilllimitation=join(skill.getLIMITATION());
			int discount=0;
			for( DISCIPLINEType discipline : getDisciplines() ) {
				if( skill == null ) break;
				discount++;
				for( TALENTType talent : (new TalentsContainer(discipline)).getAllTalents() ) {
					// Talente die keine Rang haben können übersprungen werden
					if( (talent.getRANK()==null) || (talent.getRANK().getRank()<1) ) continue;
					String limitation=join(talent.getLIMITATION());
					if( talent.getName().equals(skillname) && limitation.equals(skilllimitation) ) {
						skill.setRealigned(discount);
						alignedskills.add(skill);
						talent.setALIGNEDSKILL(skill);
						skill=null;
						break;
					}
				}
			}
		}
		character.getSKILL().removeAll(alignedskills);
	}

	public EXPERIENCEType getLegendPoints() {
		EXPERIENCEType experience = character.getEXPERIENCE();
		if( experience == null ) {
			experience = new EXPERIENCEType();
			experience.setCurrentlegendpoints(0);
			experience.setTotallegendpoints(0);
			character.setEXPERIENCE(experience);
		}
		return experience;
	}

	public void clearSpentLegendPoints() {
		EXPERIENCEType experience = getLegendPoints();
		List<ACCOUNTINGType> legendpoints = experience.getLEGENDPOINTS();
		List<ACCOUNTINGType> remove =  new ArrayList<ACCOUNTINGType>();
		for( ACCOUNTINGType a : legendpoints ) {
			if( a.getType().equals(PlusminusType.MINUS) ) remove.add(a);
		}
		legendpoints.removeAll(remove);
		experience.setCurrentlegendpoints(experience.getTotallegendpoints());
	}

	public List<WEAPONType> getWeapons() {
		return character.getWEAPON();
	}

	public List<DISCIPLINEBONUSType> getDisciplineBonuses() {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		for( DISCIPLINEType discipline : character.getDISCIPLINE() ) {
			bonuses.addAll(discipline.getDISCIPLINEBONUS());
		}
		return bonuses;
	}

	public List<ElementkindType> getDisciplinePrimElements() {
		List<ElementkindType> elements = new ArrayList<ElementkindType>();
		for( DISCIPLINEType discipline : character.getDISCIPLINE() ) {
			elements.add(discipline.getPrimelement());
		}
		return elements;
	}

	public void clearDisciplineBonuses() {
		for( DISCIPLINEType discipline : character.getDISCIPLINE() ) {
			discipline.getDISCIPLINEBONUS().clear();
		}
	}

	public List<COINSType> getAllCoins() {
		List<COINSType> allCoins = character.getCOINS();
		if( allCoins.isEmpty() ) {
			COINSType coins = new COINSType();
			coins.setSilver(100); // Startguthaben
			coins.setLocation("self");
			coins.setUsed(YesnoType.YES);
			coins.setName("Starting Purse");
			coins.setKind(ItemkindType.COINS);
			allCoins.add(coins);
		}
		return allCoins;
	}

	public List<SPELLType> getAllSpells() {
		List<SPELLType> result = new ArrayList<SPELLType>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			List<SPELLType> spells = discipline.getSPELL();
			if( spells != null ) result.addAll(spells);
		}
		return result;
	}

	public void clearOpenSpellList() {
		character.getOPENSPELL().clear();
	}

	public void addOpenSpell(SPELLType spell) {
		character.getOPENSPELL().add(spell);
	}

	public List<SPELLType> getOpenSpellList() {
		return character.getOPENSPELL();
	}

	/*
	 * Liefert für jede Diszipline des Charakters pro Kreis eine Auflistung der verwendeten Optionalen Talente
	 * Die äußere Map beinhaltet als Key, den Disziplinnamen. Die Values ist eine Liste,
	 * wobei nicht der erste Eintrag für den ersten Kreis steht sondern der Eintrag mit dem Index=1,
	 * das selbe gilt für die anderen Kreise analog
	 * Die Werte der Liste ist wieder rum eine Liste von Talenten.
	 * 
	 */
	public Map<String,List<List<TALENTType>>> getUsedOptionalTalents() {
		Map<String,List<List<TALENTType>>> result = new TreeMap<String,List<List<TALENTType>>>();
		// Schleife über alle Disziplinen des Charakters
		for(DISCIPLINEType discipline : getDisciplines() ) {
			// Erstelle schon mal eine Ausreichende Liste von Leeren Listen um die Talente aufzunehmen.
			List<List<TALENTType>> list = new ArrayList<List<TALENTType>>();
			// !!! ACHTUNG: Kreis 1 hat Index 1 und nicht Index 0 !!!
			for(int i=0;i<=15;i++) list.add(new ArrayList<TALENTType>());
			// Schleife über alle Optionalen Talente
			for( TALENTType talent : discipline.getOPTIONALTALENT()) {
				// Sollte kein Realignment statt gefunden haben, dann handelt es sich um ein "benutzes" Optionales Talent
				if( talent.getRealigned() < 1 ) list.get(talent.getCircle()).add(talent);
			}
			result.put(discipline.getName(), list);
		}
		return result;
	}

	public static String getFullTalentname(TALENTType talent) {
		String name = talent.getName();
		// Falls es das DurabilityTalent ist, dann ignoriere die Limitationsangabe
		if( name.equals(durabilityName) ) return durabilityName;

		if( talent.getLIMITATION().size()<1 ) return name;
		return name + " : "+talent.getLIMITATION().get(0);
	}

	public static String getFullTalentname(TALENTABILITYType talent) {
		String name = talent.getName();
		// Falls es das DurabilityTalent ist, dann ignoriere die Limitationsangabe
		if( name.equals(durabilityName) ) return durabilityName;

		String limitation = talent.getLimitation();
		if( limitation.isEmpty() ) return name;
		return name + " : "+limitation;
	}

	public static String getFullTalentname(KNACKBASEType knack) {
		return knack.getName();
	}

	public List<TALENTABILITYType> getUnusedOptionalTalents(DISCIPLINE disciplineDefinition, int talentCircleNr) {
		List<TALENTABILITYType> result = new ArrayList<TALENTABILITYType>();
		List<TALENTType> usedTalents = new ArrayList<TALENTType>();
		// multiUseTalents sind Talente die mehr als einmal gelernt werden können
		Map<String, Integer> multiUseTalents = PROPERTIES.getMultiUseTalents();
		// Schleife über alle gelernten Disziplinen
		for( DISCIPLINEType discipline : getDisciplines() ) {
			for( TALENTType talent : (new TalentsContainer(discipline)).getAllTalents() ) {
				String name = getFullTalentname(talent);
				Integer multiUseCount = multiUseTalents.get(name);
				if( multiUseCount == null ) {
					// Wenn es kein MultiUseTalent ist, dann behandele es ganz normal
					// und füge es in die Liste der Benutzen Talent hinzu
					usedTalents.add(talent);
				} else {
					// Wenn es sich aber um ein MultiUseTalent handelt, Zähle den MultiUse-Zähler hinunter,
					// es sei denn er ist bereits auf Eins, dann füge das Talent in die Liste der Benutzen Talent hinzu
					if( multiUseCount > 1 ) multiUseCount--;
					else usedTalents.add(talent);
					// Aktuallisiere den MultiUse-Zähler bzw lösche das Talent aus der MultiUse Liste
					if( multiUseCount > 0 ) multiUseTalents.put(name,multiUseCount);
					else multiUseTalents.remove(name);
				}
			}
		}
		int mincircle=1;
		int maxcircle=0;
		// Durchlaufe die Kreisdefinition des gesuchten Disziplin rückwärts und ermittele alle möglichen Optionaltalente
		for( int circlenr=talentCircleNr; circlenr>0; circlenr-- ) {
			DISCIPLINECIRCLEType disciplineCircle = disciplineDefinition.getCIRCLE().get(circlenr-1);
			for( TALENTABILITYType talent : disciplineCircle.getOPTIONALTALENT() ) {
				TALENTType found = null;
				String talentname = getFullTalentname(talent);
				for( TALENTType ut : usedTalents ) if( (ut.getCircle()>=circlenr) && getFullTalentname(ut).equals(talentname) ) found=ut;
				// Wenn das Talent nicht gefunden wurde, steht es dem Charakter als weiteres Optionales Talent zur Verfügung
				if( found == null ) result.add(talent);
				// Wenn das Talent gefunden wurde, steht es dem Charakter NICHT ein weiteres mal als Optionales Talent zur Verfügung.
				// Wird aber aus der Liste entfernt falls nochmal danach gesucht werden sollte, damit es dann nicht gefunden wird.
				else usedTalents.remove(found);
			}
			FOREIGNTALENTSType foreignTalents = disciplineCircle.getFOREIGNTALENTS();
			if( foreignTalents != null ) {
				if(foreignTalents.getMincircle()<mincircle) mincircle=foreignTalents.getMincircle();
				if(foreignTalents.getMaxcircle()>maxcircle) maxcircle=foreignTalents.getMaxcircle();
			}
		}
		if(mincircle<=maxcircle) {
			// Wenn min und max ein gülltiges Intervall ergeben, dann sind FOREIGNTALENTS definiert und müssen eingefügt werden.
			// Um doppelte Auflistung zu vermeiden Erzeuge ein Liste von allen benutzten Talente sowie den Talenten, die bereits
			// als Optionale Talente identifiziert wurden.
			Map<String, TALENTABILITYType> talents = PROPERTIES.getTalentsByCircle(mincircle,maxcircle);
			List<String> potentialTalents = new ArrayList<String>();
			for( TALENTType talent : usedTalents ) potentialTalents.add(getFullTalentname(talent));
			for( TALENTABILITYType talent : result ) potentialTalents.add(getFullTalentname(talent));
			// Erweitere die Ergebnisliste um FOREIGNTALENTS nur wenn diese noch nicht enthalten waren.
			for( TALENTABILITYType talent : talents.values() ) {
				String name = getFullTalentname(talent);
				if( potentialTalents.contains(name) ) potentialTalents.remove(name);
				else result.add(talent);
			}
		}
		return result;
	}

	public Map<String,List<Integer>> getCircleOfMissingOptionalTalents() {
		Map<String,List<Integer>> result = new TreeMap<String,List<Integer>>();
		Map<String,List<List<TALENTType>>> talentsMap = getUsedOptionalTalents();
		// Eine Schleife über alle Disciplinenamen des Charakters
		for(String discipline : talentsMap.keySet() ) {
			List<Integer> list = new ArrayList<Integer>();
			// Hole alle benutzen Optionalen Talente der aktuellen Disziplin
			List<List<TALENTType>> talentsList = talentsMap.get(discipline);
			if( talentsList == null ) {
				System.err.println("A talent list for the discipline '"+discipline+"' could not be found.");
				talentsList = new ArrayList<List<TALENTType>>();
				for(int i=0;i<=15;i++) talentsList.add(new ArrayList<TALENTType>());
			}
			int disciplineNumber = getDisciplineOrder(discipline);
			// Falls in den Optionalen Regel Default Talente festgelegt seine sollte, hole diese
			Map<String, Integer> defaultOptionalTalents = PROPERTIES.getDefaultOptionalTalents(disciplineNumber);
			int disciplineCircle = getCircleOf(disciplineNumber);
			int circlenr=0;
			for( int numberOfOptionalTalents : PROPERTIES.getNumberOfOptionalTalentsPerCircleByDiscipline(discipline) ) {
				circlenr++;
				if( circlenr > disciplineCircle ) break;
				List<TALENTType> talents = new ArrayList<TALENTType>();
				for( TALENTType talent : talentsList.get(circlenr) ) {
					// Ermittele den Kreis für ein Default Optional Talent
					Integer c = defaultOptionalTalents.get(talent.getName());
					// Wenn der Kreis undefinert ist, dann war es kein Default Optionales Talent, sondern ein normales Optionales Talent
					// Nur wenn der Kreis bei einem Default Optionalen Talent nicht über dem aktuellen Kreis liegt darf es eingefügt werden
					if( (c!=null) && (c<=circlenr) ) continue;
					talents.add(talent);
				}
				// Jetzt zählen wir noch wieviele der Optionalen Talente nicht über Vielseitigkeit gelernt wurden
				// und ziehen diese von der Anzahl der möglichen Optionalen Talente ab
				int freeOptionalTalents=numberOfOptionalTalents-isNotLearnedByVersatility(talents);
				// Füge der Anzahl ensprechend viel den aktuellen Kreis in die Ergebnisliste hinzu.
				for( int i=0; i<freeOptionalTalents; i++ ) list.add(circlenr);
			}
			result.put(discipline, list);
		}
		return result;
	}

	public static boolean isLearnedByVersatility(TALENTType talent) {
		TALENTTEACHERType teacher = talent.getTEACHER();
		if( teacher == null ) return false;
		if( teacher.getByversatility().equals(YesnoType.YES) ) return true;
		return false;
	}

	public static int isLearnedByVersatility(List<TALENTType> talents) {
		if( talents == null ) return 0;
		int result=0;
		for( TALENTType talent : talents ) {
			if( isLearnedByVersatility(talent) ) result++;
		}
		return result;
	}

	public static int isNotLearnedByVersatility(List<TALENTType> talents) {
		if( talents == null ) return 0;
		int result=0;
		for( TALENTType talent : talents ) {
			if( ! isLearnedByVersatility(talent) ) result++;
		}
		return result;
	}

	public void addDiciplin(String name){
		// Wenn die Disziplin bereits vorhanden, dann tue nichts
		if( getAllDisciplinesByName().get(name) != null ) return;
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setName(name);
		discipline.setCircle(1);
		character.getDISCIPLINE().add(discipline);
		ensureDisciplinTalentsExits();
		realignOptionalTalents();
	}

	public void removeLastDiciplin(){
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		int size = disciplines.size();
		if( size<1) return;
		// Falls zu Talenten der zu löschenden Disziplin zugeordnete Skill enthalten sind,
		// müssen die Skills zu Skillliste verschoben werden.
		TalentsContainer talents = new TalentsContainer(disciplines.get(size-1));
		for( TALENTType talent : talents.getAllTalents() ) {
			SKILLType skill = talent.getALIGNEDSKILL();
			if( skill != null ) {
				talent.setALIGNEDSKILL(null);
				addSkill(skill);
			}
		}
		disciplines.remove(size-1);
	}

	public void resetFreeTalentsExits() {
		for( DISCIPLINEType discipline : getDisciplines() ) {
			DISCIPLINE disciplineDefinition = PROPERTIES.getDisziplin(discipline.getName());
			// Wenn es zu der Disziplin der Talentliste keine Disziplindefinition gibt, dann über springe diese Talentliste
			if( disciplineDefinition == null ) continue;
			int disciplineCircleNr = discipline.getCircle();
			int circlenr=0;
			// select all Freetalents.
			List<TALENTABILITYType> freetalents=new LinkedList<>();
			for( DISCIPLINECIRCLEType circledef : disciplineDefinition.getCIRCLE() ) {
				circlenr++;
				if( circlenr > disciplineCircleNr ) break;
				for( TALENTABILITYType tnew : circledef.getFREETALENT() ) {
					String name = tnew.getReplace();
					String lim = tnew.getLimitation();
					List<TALENTABILITYType> remove = new LinkedList<>();
					for( TALENTABILITYType told : freetalents ) {
						if( told.getName().equals(name) && told.getLimitation().equals(lim)) {
							// If there is a replacment drop the old entry
							remove.add(told);
						}
					}
					freetalents.removeAll(remove);
					freetalents.add(tnew);
				}
			}
			// Search for freetalents which are already present
			List<TALENTType> removeOld = new LinkedList<>();
			for( TALENTType freetalent : discipline.getFREETALENT() ) {
				String[] limitations = freetalent.getLIMITATION().toArray(new String[0]);
				if( limitations.length == 0) limitations=new String[]{""};
				List<TALENTABILITYType> removeNew = new LinkedList<>();
				for( TALENTABILITYType f : freetalents ) {
					boolean exists=false;
					for( String l : limitations ) {
						if( l.isEmpty() || f.getLimitation().equals(l) ) {
							if( freetalent.getName().equals(f.getReplace()) ) {
								// Some free talents may replaced by other free talents
								freetalent.setName(f.getName());
								exists=true;
							} else if( freetalent.getName().equals(f.getName()) ) {
								exists=true;
							}
						}
					}
					if( exists ) removeNew.add(f);
				}
				if( removeNew.isEmpty() ) {
					// We did not find an free talent definition for an existing free talent.
					removeOld.add(freetalent);
				} else {
					freetalents.removeAll(removeNew);
				}
			}
			discipline.getFREETALENT().removeAll(removeOld);
			// Now insert all missing freetalents
			for( TALENTABILITYType f : freetalents ) {
				TALENTType newTalent = new TALENTType();
				newTalent.setName(f.getName());
				String limitation = f.getLimitation();
				if( !limitation.isEmpty() ) newTalent.getLIMITATION().add(limitation);
				newTalent.setCircle(circlenr);
				discipline.getFREETALENT().add(newTalent);
			}
		}
	}

	public void ensureDisciplinTalentsExits() {
		List<String> totalListOfDisciplineTalents = new ArrayList<String>();
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getDisciplinetalents() ) {
				totalListOfDisciplineTalents.add(getFullTalentname(talent));
			}
		}
		for( DISCIPLINEType discipline : getDisciplines() ) {
			DISCIPLINE disciplineDefinition = PROPERTIES.getDisziplin(discipline.getName());
			// Wenn es zu der Disziplin der Talentliste keine Disziplindefinition gibt, dann über springe diese Talentliste
			if( disciplineDefinition == null ) continue;
			int disciplineCircleNr = discipline.getCircle();
			int circlenr=0;
			for( DISCIPLINECIRCLEType disciplineCircleDefinition : disciplineDefinition.getCIRCLE() ) {
				circlenr++;
				if( circlenr > disciplineCircleNr ) break;
				for( TALENTABILITYType disciplineTalent : disciplineCircleDefinition.getDISCIPLINETALENT()) {
					TALENTType newTalent = new TALENTType();
					newTalent.setName(disciplineTalent.getName());
					String limitation = disciplineTalent.getLimitation();
					if( !limitation.isEmpty() ) newTalent.getLIMITATION().add(limitation);
					newTalent.setCircle(circlenr);
					String newFullTalentName=getFullTalentname(newTalent);
					if( ! totalListOfDisciplineTalents.contains(newFullTalentName) ) {
						discipline.getDISZIPLINETALENT().add(newTalent);
						totalListOfDisciplineTalents.add(newFullTalentName);
					}
				}
			}
		}
	}

	public void realignOptionalTalents() {
		// Talentname von Unempfindlichkeit ermitteln
		final String durabilityName = PROPERTIES.getDurabilityName();
		// Talente ermitteln die mehrfach verwendet werden dürfen.
		Map<String, Integer> multiUseTalents = PROPERTIES.getMultiUseTalents();
		// Liste alle vom Charakter gelernter Disziplinen
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		// Wenn man bei Eins anfängt zu nummerieren, dann ist die Anzahl der Disziplinen auch gleichzeitig die Nummer der letzten Disziplin.
		int maxDisciplineOrder=disciplines.size();
		int disciplineOrder=0;
		for( DISCIPLINEType discipline : disciplines ) {
			// Die Nummerierung der Disziplinen fängt bei Eins an, daher zälen wir hier schon hoch
			disciplineOrder++;
			for( TALENTType disTalent : discipline.getDISZIPLINETALENT() ) {
				// Ermittle für spätere Vergleiche den vollständigen Diszipline namen (mit Limitation Bezeichnung)
				String disTalentName=getFullTalentname(disTalent);
				for( DISCIPLINEType compareDiscipline : disciplines ) {
					// Talentlisten nicht mit sich selbst vergleichen, daher Schleife überspringen, wenn es die selbe Disziplin ist.
					if( discipline == compareDiscipline ) continue;
					// Disziplinetalente können nicht realigned werden, daher ist auch keine gesonderte Prüfung
					// ob dieses Talent bereits ge-realigned ist nicht notwendig und wäre unsinnig
					for( TALENTType optTalent : compareDiscipline.getOPTIONALTALENT() ) {
						String optTalentName = getFullTalentname(optTalent);
						if( multiUseTalents.containsKey(optTalentName) ) {
							// MultiUseTalents können nicht realigned werden.
							// Prüfe, ob es eventuell doch gemacht wurde, wenn ja korigieren wir es hier
							if( optTalent.getRealigned() > 0 ) optTalent.setRealigned(0);
							// Springe zum nächsten Talent, führ diesen Schleifen durchlauf nicht fort.
							continue;
						}
						// Sollte das Optinaltalent auf ein Talent einer nicht mehr exisiterenden Disziplin realgined sind,
						// kann dieses Realgined entfernt werden
						if( optTalent.getRealigned() > maxDisciplineOrder ) optTalent.setRealigned(0);
						// Unterscheide, ob das Disziplintalent das Unempfindlichkeit-Talent ist
						if( durabilityName.equals(disTalent.getName()) ) {
							// Wenn ja, dann ist die Limitation nicht wichtig und wir prüfen nur ob das vergleichende Talent eben falls das Unempfindlichkeit-Talent ist.
							if( durabilityName.equals(optTalent.getName()) ) optTalent.setRealigned(disciplineOrder);
						} else {
							// Wenn nein, dann ist die Limitation wichtig und muss identisch sein. Daher werden die "vollstädnige" Talentnamen verglichen
							if( disTalentName.equals(optTalentName) ) optTalent.setRealigned(disciplineOrder);
						}
					}
				}
			}
		}
	}

	public TALENTType addOptionalTalent(String disciplineName, int circle, TALENTABILITYType talenttype, boolean byVersatility) {
		if( talenttype == null ) return null;
		TALENTType talent = new TALENTType();
		talent.setName(talenttype.getName());
		if( !talenttype.getLimitation().isEmpty() ) talent.getLIMITATION().add(talenttype.getLimitation());
		talent.setCircle(circle);

		RANKType rank = new RANKType();
		rank.setRank(1);
		talent.setRANK(rank);
		TALENTTEACHERType teacher = new TALENTTEACHERType();
		if( byVersatility ) teacher.setByversatility(YesnoType.YES);
		talent.setTEACHER(teacher);

		int disciplineOrder = getDisciplineOrder(disciplineName);
		if( disciplineOrder < 1 ) {
			System.err.println("Discipline '"+disciplineName+"' cound not be found. So, the optional talent '"+talenttype.getName()+"' cound not be inserted.");
			return null;
		}
		// Wenn es sich bei dem neuen OptionalTalent um das Unempfindlichkeitstalent handelt,
		// dann muss geprüft werden, ob dieses bereits in einder anderen Disziplin vorhanden ist.
		//TODO: nicht nur Unempfindlichkeitstalent
		if( durabilityName.equals(talent.getName()) ) {
			for( DISCIPLINEType discipline : getDisciplines() ) {
				// Wenn die Disziplinnamen über einstimmen, dann handelt es sich um die selbe Disziplin
				// und es brauch nichts geprüft werden.
				if( disciplineName.equals(discipline.getName()) ) continue;
				for( TALENTType optTalent : discipline.getOPTIONALTALENT() ) {
					if( ! durabilityName.equals(optTalent.getName()) ) continue;
					optTalent.setRealigned(disciplineOrder);
				}
			}
		}
		getAllTalents().get(disciplineOrder-1).getOptionaltalents().add(talent);
		return talent;
	}

	public void addSpell(String discipline, SPELLType spell){
		for( DISCIPLINEType dis : getDisciplines() ){
			if(dis.getName().equals(discipline)){
				dis.getSPELL().add(spell);
				return;
			}
		}
		System.err.println("Discipline '"+discipline+"' not found, could not add a spells.");
	}

	public void removeSpell(String disciplinename, SPELLType spell) {
		if( spell == null ) return;
		removeSpell(disciplinename, spell.getName());
	}

	public void removeSpell(String disciplinename, String spellname) {
		for( DISCIPLINEType dis : getDisciplines() ){
			if(dis.getName().equals(disciplinename)){
				List<SPELLType> spells = dis.getSPELL();
				List<SPELLType> spelltoremove = new ArrayList<SPELLType>();
				for( SPELLType currentspell : spells ) {
					if( spellname.equals(currentspell.getName()) ) {
						spelltoremove.add(currentspell);
					}
				}
				spells.removeAll(spelltoremove);
				return;
			}
		}
	}
	public boolean hasSpellLearned(String disciplinename, SPELLType spelltype) {
		return hasSpellLearned(disciplinename, spelltype.getName());
	}

	public boolean hasSpellLearned(String disciplinename, String spellname){
		for(DISCIPLINEType discipline : getDisciplines()){
			if( disciplinename.equals(discipline.getName()) ){
				for(SPELLType spell : discipline.getSPELL()){
					if( spellname.equals(spell.getName()) ) return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean hasSpellLearnedBySpellability(String disciplinename, SPELLType spelltype) {
		return hasSpellLearnedBySpellability(disciplinename, spelltype.getName());
	}

	public boolean hasSpellLearnedBySpellability(String disciplinename, String spellname){
		for(DISCIPLINEType discipline : getDisciplines()){
			if( disciplinename.equals(discipline.getName()) ){
				for(SPELLType spell : discipline.getSPELL()){
					if( spellname.equals(spell.getName()) ) return spell.getByspellability().equals(YesnoType.YES);
				}
				return false;
			}
		}
		return false;
	}

	public void toggleSpellLearnedBySpellability(String disciplinename, SPELLType spelltype) {
		toggleSpellLearnedBySpellability(disciplinename, spelltype.getName());
	}

	public void toggleSpellLearnedBySpellability(String disciplinename, String spellname) {
		for(DISCIPLINEType discipline : getDisciplines()){
			if( disciplinename.equals(discipline.getName()) ){
				for(SPELLType spell : discipline.getSPELL()){
					if( spellname.equals(spell.getName()) ) {
						if( spell.getByspellability().equals(YesnoType.YES) ) spell.setByspellability(YesnoType.NO);
						else                                                  spell.setByspellability(YesnoType.YES);
						return;
					}
				}
				return;
			}
		}
	}

	public KNACKType[] getKnacksByName(String knackname) {
		List<KNACKType> result = new LinkedList<>();
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getAllTalents() ) {
				for( KNACKType k : talent.getKNACK() ) {
					if( k.getName().equals(knackname) ) result.add(k);
				}
			}
		}
		return result.toArray(new KNACKType[0]);
	}

	// Works only, if the list of knacks are the same objects to be removed.
	// Use getKnacksByName to get the knacks to select from to be deleted.
	public void removeKnack(KNACKType[] knacks) {
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getAllTalents() ) {
				talent.getKNACK().removeAll(Arrays.asList(knacks));
			}
		}
	}

	public List<ITEMType> getItems() {
		return character.getITEM();
	}

	public String getDESCRIPTION() {
		String result=character.getDESCRIPTION();
		if( result == null ) return "";
		return result;
	}

	public void setDESCRIPTION(String description) {
		character.setDESCRIPTION(description);
	}

	public String getCOMMENT() {
		String result=character.getCOMMENT();
		if( result == null ) return "";
		return result;
	}

	public void setCOMMENT(String comment) {
		character.setCOMMENT(comment);
	}

	public List<MAGICITEMType> getMagicItem() {
		return character.getMAGICITEM();
	}

	public List<THREADITEMType> getThreadItem() {
		return character.getTHREADITEM();
	}

	public List<MAGICITEMType> getBloodCharmItem() {
		return character.getBLOODCHARMITEM();
	}

	public List<PATTERNITEMType> getPatternItem() {
		return character.getPATTERNITEM();
	}

	public List<ARMORType> getMagicArmor() {
		List<ARMORType> magicarmor = new ArrayList<ARMORType>();
		for( THREADITEMType magicitem : getThreadItem() ) {
			String name = magicitem.getName();
			float weight = magicitem.getWeight();
			YesnoType used = magicitem.getUsed();
			ItemkindType kind = magicitem.getKind();
			int weaven = magicitem.getWeaventhreadrank();
			String location = magicitem.getLocation();
			int edn=magicitem.getEnchantingdifficultynumber();
			int blooddamage = magicitem.getBlooddamage();
			int dr = magicitem.getDepatterningrate();
			String bookref = magicitem.getBookref();
			int size = magicitem.getSize();
			int rank=0;
			ARMORType newmagicarmor = null;
			SHIELDType newmagicshield = null;
			List<CHARACTERISTICSCOST> LpCosts = PROPERTIES_Characteristics.getTalentRankLPIncreaseTable(1,magicitem.getLpcostgrowth() );
			for( THREADRANKType threadrank : magicitem.getTHREADRANK() ) {
				threadrank.setLpcost( LpCosts.get(rank).getCost() );
				rank++;
				ARMORType armor = threadrank.getARMOR();
				if( armor != null ) {
					armor.setName(name);
					armor.setWeight(weight);
					armor.setUsed(used);
					armor.setKind(kind);
					armor.setLocation(location);
					armor.setEdn(edn);
					armor.setBlooddamage(blooddamage);
					armor.setDepatterningrate(dr);
					armor.setBookref(bookref);
					armor.setSize(size);
					if( weaven == rank ) newmagicarmor=armor;
				}
				SHIELDType shield = threadrank.getSHIELD();
				if( shield != null ) {
					shield.setName(name);
					shield.setWeight(weight);
					shield.setUsed(used);
					shield.setKind(kind);
					shield.setLocation(location);
					shield.setEdn(edn);
					shield.setBlooddamage(blooddamage);
					shield.setDepatterningrate(dr);
					shield.setBookref(bookref);
					shield.setSize(size);
					if( weaven == rank ) newmagicshield=shield;
				}
			}
			if( newmagicarmor != null ) magicarmor.add(copyArmor(newmagicarmor,true));
			if( newmagicshield != null ) magicarmor.add(copyArmor(newmagicshield,true));
		}
		return magicarmor;
	}

	public static ARMORType copyArmor(ARMORType armor, boolean setvirtual) {
		if( armor == null ) return null;
		ARMORType newarmor;
		if( armor instanceof SHIELDType ) newarmor = new SHIELDType();
		else newarmor = new ARMORType();
		copyItem(armor, newarmor, setvirtual);
		newarmor.setDateforged(armor.getDateforged());
		newarmor.setEdn(armor.getEdn());
		newarmor.setEdnElement(armor.getEdnElement());
		newarmor.setMysticarmor(armor.getMysticarmor());
		newarmor.setPenalty(armor.getPenalty());
		newarmor.setPhysicalarmor(armor.getPhysicalarmor());
		newarmor.setTimesforgedMystic(armor.getTimesforgedMystic());
		newarmor.setTimesforgedPhysical(armor.getPhysicalarmor());
		if( newarmor instanceof SHIELDType ) {
			((SHIELDType)newarmor).setMysticdeflectionbonus(((SHIELDType)armor).getMysticdeflectionbonus());
			((SHIELDType)newarmor).setPhysicaldeflectionbonus(((SHIELDType)armor).getPhysicaldeflectionbonus());
			((SHIELDType)newarmor).setShatterthreshold(((SHIELDType)armor).getShatterthreshold());
			for( DEFENSEType defense : ((SHIELDType)armor).getDEFENSE() ) {
				DEFENSEType newdef=new DEFENSEType();
				newdef.setKind(defense.getKind());
				newdef.setValue(defense.getValue());
				((SHIELDType)newarmor).getDEFENSE().add(newdef);
			}
		}
		return newarmor;
	}

	public List<ARMORType> removeVirtualArmorFromNormalArmorList() {
		List<ARMORType> armors = getProtection().getARMOROrSHIELD();
		List<ARMORType> delete = new ArrayList<ARMORType>();
		for( ARMORType armor : armors) if( armor.getVirtual().equals(YesnoType.YES)) delete.add(armor);
		armors.removeAll(delete);
		return armors;
	}

	public List<WEAPONType> getMagicWeapon() {
		List<WEAPONType> magicweapon = new ArrayList<WEAPONType>();
		for( THREADITEMType magicitem : getThreadItem() ) {
			String name = magicitem.getName();
			float weight = magicitem.getWeight();
			YesnoType used = magicitem.getUsed();
			int weaven = magicitem.getWeaventhreadrank();
			int blooddamage = magicitem.getBlooddamage();
			int dr = magicitem.getDepatterningrate();
			String bookref = magicitem.getBookref();
			int size = magicitem.getSize();
			int rank=0;
			WEAPONType newmagicweapon = null;
			List<CHARACTERISTICSCOST> LpCosts = PROPERTIES_Characteristics.getTalentRankLPIncreaseTable(1,magicitem.getLpcostgrowth() );
			for( THREADRANKType threadrank : magicitem.getTHREADRANK() ) {
				threadrank.setLpcost( LpCosts.get(rank).getCost() );
				rank++;
				WEAPONType weapon = threadrank.getWEAPON();
				if( weapon != null ) {
					weapon.setName(name);
					weapon.setWeight(weight);
					weapon.setUsed(used);
					weapon.setKind(magicitem.getKind());
					weapon.setBlooddamage(blooddamage);
					weapon.setDepatterningrate(dr);
					weapon.setBookref(bookref);
					weapon.setSize(size);
					if( weaven > 0 ) newmagicweapon=weapon;
				}
				weaven--;
			}
			if( newmagicweapon != null ) magicweapon.add(copyWeapon(newmagicweapon,true));
		}
		return magicweapon;
	}

	public static WEAPONType copyWeapon(WEAPONType weapon, boolean setvirtual) {
		if( weapon == null ) return null;
		WEAPONType newweapon = new WEAPONType();
		copyItem(weapon, newweapon, setvirtual);
		newweapon.setDateforged(weapon.getDateforged());
		newweapon.setDepatterningrate(weapon.getDepatterningrate());
		newweapon.setDamagestep(weapon.getDamagestep());
		newweapon.setDexteritymin(weapon.getDexteritymin());
		newweapon.setLongrange(weapon.getLongrange());
		newweapon.setShortrange(weapon.getShortrange());
		newweapon.setStrengthmin(weapon.getStrengthmin());
		newweapon.setTimesforged(weapon.getTimesforged());
		return newweapon;
	}

	public static WOUNDType copyWound(WOUNDType wound) {
		if( wound == null ) return null;
		WOUNDType newwound = new WOUNDType();
		newwound.setBlood(wound.getBlood());
		newwound.setNormal(wound.getNormal());
		newwound.setPenalties(wound.getPenalties());
		newwound.setThreshold(wound.getThreshold());
		return newwound;
	}

	public static DISZIPINABILITYType copyDisciplineAbility(DISZIPINABILITYType disciplineability) {
		if( disciplineability == null ) return null;
		DISZIPINABILITYType newdisciplineability = new DISZIPINABILITYType();
		newdisciplineability.setCount(disciplineability.getCount());
		return newdisciplineability;
	}

	public static DEFENSEABILITYType copyDefenseAbility(DEFENSEABILITYType defenseability) {
		if( defenseability == null ) return null;
		DEFENSEABILITYType newdefenseability = new DEFENSEABILITYType();
		newdefenseability.setBonus(defenseability.getBonus());
		newdefenseability.setKind(defenseability.getKind());
		return newdefenseability;
	}

	public static TALENTABILITYType copyTalentAbility(TALENTABILITYType talentability) {
		if( talentability == null ) return null;
		TALENTABILITYType newtalentability = new TALENTABILITYType();
		newtalentability.setBonus(talentability.getBonus());
		newtalentability.setLimitation(talentability.getLimitation());
		newtalentability.setName(talentability.getName());
		newtalentability.setPool(talentability.getPool());
		return newtalentability;
	}

	public static THREADRANKType copyThreadRank(THREADRANKType rank) {
		if( rank == null ) return null;
		THREADRANKType newrank = new THREADRANKType();
		newrank.setEffect(rank.getEffect());
		newrank.setLpcost(rank.getLpcost());
		newrank.setKeyknowledge(rank.getKeyknowledge());
		newrank.setDeed(rank.getDeed());
		newrank.setARMOR(copyArmor(rank.getARMOR(),false));
		newrank.setSHIELD((SHIELDType)copyArmor(rank.getSHIELD(),false));
		newrank.setWEAPON(copyWeapon(rank.getWEAPON(),false));
		newrank.setWOUND(copyWound(rank.getWOUND()));
		for( String i : rank.getABILITY() ) newrank.getABILITY().add(i);
		for( String i : rank.getSPELL() ) newrank.getSPELL().add(i);
		for( DISZIPINABILITYType i : rank.getINITIATIVE() ) newrank.getINITIATIVE().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getKARMASTEP() ) newrank.getKARMASTEP().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getMAXKARMA() ) newrank.getINITIATIVE().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getRECOVERYTEST() ) newrank.getMAXKARMA().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getSPELLABILITY() ) newrank.getSPELLABILITY().add(copyDisciplineAbility(i));
		for( DEFENSEABILITYType i : rank.getDEFENSE() ) newrank.getDEFENSE().add(copyDefenseAbility(i));
		for( TALENTABILITYType i : rank.getTALENT() ) newrank.getTALENT().add(copyTalentAbility(i));
		return newrank;
	}

	public List<WEAPONType> cutMagicWeaponFromNormalWeaponList() {
		List<WEAPONType> magicWeapon = getMagicWeapon();
		List<WEAPONType> normalWeaponList = character.getWEAPON();
		List<WEAPONType> delete = new ArrayList<WEAPONType>();
		for( WEAPONType weapon : normalWeaponList) {
			String weaponName = weapon.getName();
			for( WEAPONType w : magicWeapon ) {
				w.setVirtual(YesnoType.YES);
				if( weaponName.equals(w.getName()) ) delete.add(weapon);
			}
		}
		normalWeaponList.removeAll(delete);
		return magicWeapon;
	}

	public static int[] calculateAccounting(List<ACCOUNTINGType> accountings) {
		int[] account = new int[2];
		account[0]=0; // plus
		account[1]=0; // minus
		for( ACCOUNTINGType lp : accountings ) {
			switch( lp.getType() ) {
			case PLUS:  account[0] += lp.getValue(); break;
			case MINUS: account[1] += lp.getValue(); break;
			}
		}
		return account;
	}

	public QUESTORType getQuestor() {
		 QUESTORType questor = character.getQUESTOR();
		 if( questor == null ) {
			 questor = new QUESTORType();
			 character.setQUESTOR(questor);
		 }
		 return questor;
	}

	public DEVOTIONType getDevotionPoints() {
		return character.getDEVOTION();
	}

	public String getPassion() {
		final QUESTORType questor = getQuestor();
		final DEVOTIONType devotion = getDevotionPoints();
		String result=questor.getPassion();
		if( ! ( result == null || result.isEmpty() ) ) {
			if( devotion != null && devotion.getPassion() != null ) devotion.setPassion(result);
			return result;
		}
		if( devotion != null ) result=devotion.getPassion();
		if( result == null ) result="";
		if( ! result.isEmpty() ) {
			questor.setPassion(result);
		}
		return result;
	}

	public void setPassion(String passion) {
		getQuestor().setPassion(passion);
		final DEVOTIONType devotion = getDevotionPoints();
		if( devotion != null ) devotion.setPassion(passion);
	}

	public int calculateDevotionPoints() {
		DEVOTIONType devotionpoints=getDevotionPoints();
		if( devotionpoints == null ) return getQuestor().getDevotionpoints();
		List<ACCOUNTINGType> dplist = devotionpoints.getDEVOTIONPOINTS();
		if( dplist.isEmpty() ) {
			ACCOUNTINGType tmp = new ACCOUNTINGType();
			tmp.setValue(getQuestor().getDevotionpoints());
			tmp.setComment("latest current value");
			tmp.setType(PlusminusType.PLUS);
			tmp.setWhen(getCurrentDateTime());
			dplist.add(tmp);
		}
		int[] dp = calculateAccounting(dplist);
		int result = dp[0]-dp[1];
		devotionpoints.setValue(result);
		getQuestor().setDevotionpoints(result);
		return result;
	}

	public int getNumberOfTalentsLearnedByVersatility() {
		List<TalentsContainer> allTalents = getAllTalents();
		if( allTalents == null ) return 0;
		int result = 0;
		for( TalentsContainer talents : allTalents ) {
			result += isLearnedByVersatility(talents.getAllTalents());
		}
		return result;
	}

	public int getUnusedVersatilityRanks() {
		int result=-getNumberOfTalentsLearnedByVersatility();
		List<TALENTType> versatilityList = getTalentByName(PROPERTIES.getVersatilityName());
		if( versatilityList == null ) return result;
		if( versatilityList.isEmpty() ) return result;
		for( TALENTType versatility : versatilityList ) {
			RANKType rank = versatility.getRANK();
			if( rank != null ) {
				result += rank.getRank();
			}
		}
		return result;
	}

	public void removeEmptySkills() {
		List<SKILLType> skills = getSkills();
		List<SKILLType> remove = new ArrayList<SKILLType>();
		for( SKILLType skill : skills ) {
			RANKType rank = skill.getRANK();
			if( (rank != null) && (rank.getRank() > 0) ) {
				// Lösche alle leeren Limitaions, falls welche vorhanden sind
				while( skill.getLIMITATION().remove("") );
				continue;
			}
			remove.add(skill);
		}
		removeSkill(remove);
	}

	// Finde zu allen Talenten, ob es Realigned Talente dazu gibt und aktuallisiere deren Realigned Rank
	public void updateRealignedTalents() {
		Map<String, List<TALENTType>> realignedTalentHash = new TreeMap<String, List<TALENTType>>();
		// Finde alle Talente die als Realigned makiert sind
		for( TalentsContainer talents : getAllTalents() ) {
			insertIfRealigned(realignedTalentHash, talents.getAllTalents() );
		}
		for( String talentName : realignedTalentHash.keySet() ) {
			// Wurde ein Talent Realigned, dann gibt es das Talent unter gleichen Namen mehrfach.
			List<TALENTType> talentsByName = getTalentByName(talentName);
			List<TALENTType> realignedTalentList = realignedTalentHash.get(talentName);
			// Entferne unter der gleichnamigen Talenten alle die realgined wurden
			talentsByName.removeAll(realignedTalentList);
			// Theoretisch sollte in talentsByName nun genau ein Element übrig bleiben.
			// Bestimme für dieses nun den Realigned-Rang
			int maxRealignedRank=0;
			for( TALENTType talent : realignedTalentList ) {
				int currentRealignedRank = talent.getRANK().getRank();
				if( maxRealignedRank < currentRealignedRank ) maxRealignedRank=currentRealignedRank;
			}
			// Auch wenn nur genau ein Talent betroffen sein dürfte, nutzen wir eine Schleife.
			for( TALENTType talent : talentsByName ) {
				RANKType rank = talent.getRANK();
				// An dieser Stelle sollte der rank nicht 'null' sein können, aber eine Prüfung schadet nicht.
				if( rank == null ) {
					rank = new RANKType();
					talent.setRANK(rank);
				}
				rank.setRealignedrank(maxRealignedRank);
			}
		}
	}

	private void insertIfRealigned(Map<String, List<TALENTType>> realignedTalents, List<TALENTType> talents) {
		for( TALENTType talent : talents ) {
			if( talent.getRealigned() > 0 ) {
				String talentName = talent.getName();
				List<TALENTType> list = realignedTalents.get(talentName);
				if( list == null ) {
					list = new ArrayList<TALENTType>();
					realignedTalents.put(talentName, list);
				}
				list.add(talent);
			}
		}
	}

	public void removeZeroRankOptionalTalents() {
		for( TalentsContainer talents : getAllTalents() ) {
			List<TALENTType> rankZeroTalents = new ArrayList<TALENTType>();
			List<TALENTType> optionalTalents = talents.getOptionaltalents();
			for( TALENTType talent : optionalTalents ) {
				RANKType rank = talent.getRANK();
				if( (rank == null) || (rank.getRank() < 1) ) {
					rankZeroTalents.add(talent);
					// Sollte ein Skill zu diesem Tallent Realigned sein, dann löse diese Verbindung bei Rank 0
					SKILLType skill = talent.getALIGNEDSKILL();
					if( skill != null ) {
						talent.setALIGNEDSKILL(null);
						addSkill(skill);
					}
				}
			}
			optionalTalents.removeAll(rankZeroTalents);
		}
	}

	public void removeIllegalTalents() {
		for( DISCIPLINEType discipline : getDisciplines() ) {
			int disciplineCircleNr = discipline.getCircle();
			List<TALENTType> remove = new ArrayList<TALENTType>();
			List<TALENTType> disciplineTalents = discipline.getDISZIPLINETALENT();
			for( TALENTType talent : disciplineTalents ) {
				if( talent.getCircle() > disciplineCircleNr ) remove.add(talent);
			}
			disciplineTalents.removeAll(remove);
			remove.clear();
			List<TALENTType> optionalTalents = discipline.getDISZIPLINETALENT();
			for( TALENTType talent : optionalTalents ) {
				if( talent.getCircle() > disciplineCircleNr ) {
					remove.add(talent);
					continue;
				}
				// Talente bei denen die Limitation auf "(#)" endet kommen von ThreadItems und fliegen erstmal raus
				// Diese werden wieder vom ThreadItem ergänzt, wenn es noch da ist.
				if( join(talent.getLIMITATION()).endsWith("(#)") ) {
					remove.add(talent);
					continue;
				}
			}
			optionalTalents.removeAll(remove);
		}
		for( DISCIPLINEType discipline1 : getDisciplines() ) {
			for( TALENTType talent1 : discipline1.getDISZIPLINETALENT() ) {
				String talentname1 = getFullTalentname(talent1);
				for( DISCIPLINEType discipline2 : getDisciplines() ) {
					if( discipline1 == discipline2 ) continue;
					// in anderen Diszipinen (2) darf es kein Disziplintalent geben,
					// dass bereits in dieser Disziplin (1) ein Disziplintalent ist.
					List<TALENTType> remove = new ArrayList<TALENTType>();
					List<TALENTType> talents2 = discipline2.getDISZIPLINETALENT();
					for( TALENTType talent2 : talents2 ) {
						String talentname2 = getFullTalentname(talent2);
						if( talentname1.equals(talentname2) ) remove.add(talent2);
					}
					talents2.removeAll(remove);
				}
			}
		}
	}

	public NAMEGIVERABILITYType getRace() {
		APPEARANCEType appearance = getAppearance();
		String race = appearance.getRace();
		String origin = appearance.getOrigin();
		return PROPERTIES.searchNamegiver(race, origin);
	}

	public void calculateMovement() {
		int movementFlight = 0;
		int movementGround = 0;
		NAMEGIVERABILITYType namegiver = getRace();
		if( namegiver != null ) {
			movementFlight = namegiver.getMovementFlight();
			movementGround = namegiver.getMovementGround();
		}
		Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = getAttributes();
		ATTRIBUTEType strength = attributes.get(ATTRIBUTENameType.STR);
		ATTRIBUTEType dexterity = attributes.get(ATTRIBUTENameType.DEX);
		int modStr=Math.round( (float)(strength.getCurrentvalue()-strength.getRacevalue()) / 3f );
		int modDex=Math.round( (float)(dexterity.getCurrentvalue()-dexterity.getRacevalue()) / 3f );
		switch(OptionalRule_AttributeBasedMovement) {
		case DEX:
			movementGround+=modDex;
			if(movementFlight>0) movementFlight+=modDex;
			break;
		case STR:
			movementGround+=modStr;
			if(movementFlight>0) movementFlight+=modStr;
			break;
		case STR_DEX:
			int av=Math.round((modDex+modStr)/2f);
			movementGround+=av;
			if(movementFlight>0) movementFlight+=av;
			break;
		case MAX:
			int max=(modDex>modStr)?modDex:modStr;
			movementGround+=max;
			if(movementFlight>0) movementFlight+=max;
			break;
		case NA:
			break;
		}
		MOVEMENTType movement = getMovement();
		movement.setFlight(movementFlight);
		movement.setGround(movementGround);
		movement.setFlightString( PROPERTIES.getUnitCalculator().formatLength( movementFlight, 0 ));
		movement.setGroundString( PROPERTIES.getUnitCalculator().formatLength( movementGround, 0 ));
	}

	public void calculateCarrying() {
		CARRYINGType carrying = getCarrying();
		List<Integer> encumbrance = PROPERTIES_Characteristics.getENCUMBRANCE();
		int strength=getAttributes().get(ATTRIBUTENameType.STR).getCurrentvalue();
		if( strength<1 ) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			strength=1;
		}
		if( strength > encumbrance.size()) {
			strength = encumbrance.size();
			System.err.println("The strength attribute was out of range. The carrying value will now base on: "+strength);
		}
		Integer carryingValue = encumbrance.get(strength);
		carrying.setCarrying(carryingValue);
		carrying.setLifting(carryingValue *2);
	}

	public Map<String,ITEMType> getHashOfAllItems() {
		Map<String,ITEMType> result = new TreeMap<String, ITEMType>();
		for( ITEMType item : character.getITEM() ) result.put( item.getName(), item );
		int pursecounter=0;
		for( COINSType coins : character.getCOINS() ) {
			String name = coins.getName();
			if( name == null ) {
				name = "Purse #"+String.valueOf(++pursecounter);
			} else {
				if( name.isEmpty() ) name = "Purse #"+String.valueOf(++pursecounter);
				else name = "Purse "+name;
			}
			name += " (c:"+coins.getCopper()+" s:"+coins.getSilver()+" g:"+coins.getGold();
			if( coins.getEarth()>0 )      name += " e:"+coins.getEarth();
			if( coins.getWater()>0 )      name += " w:"+coins.getWater();
			if( coins.getAir()>0 )        name += " a:"+coins.getAir();
			if( coins.getFire()>0 )       name += " f:"+coins.getFire();
			if( coins.getOrichalcum()>0 ) name += " o:"+coins.getOrichalcum();
			name +=")";
			result.put( name, coins );
		}
		for( ITEMType item : character.getWEAPON() ) result.put( item.getName(), item );
		PROTECTIONType protection = character.getPROTECTION();
		if( protection != null ) {
			boolean naturalArmor=true; // Der erste Eintrag ist immer die natürliche Rüstung
			for( ITEMType item : protection.getARMOROrSHIELD() ) {
				if( naturalArmor ) {
					// Die natürliche Rüstng nicht als Gegenstand auflisten
					naturalArmor=false;
					continue;
				}
				result.put( item.getName(), item );
			}
		}
		for( ITEMType item : character.getMAGICITEM() )      result.put( item.getName(), item );
		for( ITEMType item : character.getBLOODCHARMITEM() ) result.put( item.getName(), item );
		for( ITEMType item : character.getPATTERNITEM() )    result.put( item.getName(), item );
		for( ITEMType item : character.getTHREADITEM() )     result.put( item.getName(), item );
		return result;
	}

	public List<ITEMType> getAllNonVirtualItems() {
		List<ITEMType> result = new ArrayList<ITEMType>();
		for( ITEMType item : character.getITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getCOINS() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getWEAPON() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		PROTECTIONType protection = character.getPROTECTION();
		if( protection != null ) {
			for( ITEMType item : protection.getARMOROrSHIELD() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		}
		for( ITEMType item : character.getMAGICITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getBLOODCHARMITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getPATTERNITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getTHREADITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		return result;
	}

	public List<Base64BinaryType> getPortrait() {
		List<Base64BinaryType> result = character.getPORTRAIT();
		if( result.isEmpty() ) {
			APPEARANCEType appearance = getAppearance();
			File[] files = new File("images/character").listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if( name==null ) return false;
					name=name.toLowerCase();
					if( !name.startsWith("portrait_") ) return false;
					if( name.endsWith(".jpg") ) return true;
					if( name.endsWith(".png") ) return true;
					if( name.endsWith(".gif") ) return true;
					return false;
				}
			});
			List<List<File>> filescore = new ArrayList<List<File>>();
			filescore.add(new ArrayList<File>()); // 0
			filescore.add(new ArrayList<File>()); // 1
			filescore.add(new ArrayList<File>()); // 2
			filescore.add(new ArrayList<File>()); // 3
			filescore.get(0).add(new File("images/character/portrait.jpg"));
			String race="_"+appearance.getRace().toLowerCase()+"_";
			String gender="_"+appearance.getGender().value().toLowerCase()+"_";
			String origin="_"+appearance.getOrigin().toLowerCase()+"_";
			for( File f : files ) {
				String name=f.getName().toLowerCase().replace(".", "_");
				if( name.contains(race) ) {
					int score=1;
					if( name.contains(gender) ) score++;
					if( name.contains(origin) ) score++;
					filescore.get(score).add(f);
				}
			}
			File file=null;
			for(int score=3;score>=0;score--) {
				if( file != null ) break;
				List<File> fileset = filescore.get(score);
				if( !fileset.isEmpty() ) file = fileset.get(rand.nextInt(fileset.size()));
			}
			if( file!=null ) try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fileInputStream.read(data);
				fileInputStream.close();
				Base64BinaryType base64bin = new Base64BinaryType();
				base64bin.setValue(data);
				final String[] filename = file.getName().split("\\.");
				base64bin.setContenttype("image/"+filename[filename.length-1]);
				result.add(base64bin);
			} catch (FileNotFoundException e) {
				// Wenn Datei nicht gefunden, dann Pech.
				System.err.println("can not insert default portrait : "+e.getLocalizedMessage());
			} catch (IOException e) {
				System.err.println("can not insert default portrait : "+e.getLocalizedMessage());
			}
		}
		return result;
	}

	public void readjustInitiativeModifikator(int adjustment, boolean armor) {
		INITIATIVEType initiative = getInitiative();
		if( armor ) {
			int currentarmorpenalty = initiative.getArmorpenalty();
			if( adjustment > currentarmorpenalty ) {
				initiative.setArmorpenalty(0);
				System.err.println("Armor penalty can not be negativ. Reduce initiative adjustment from "+adjustment+" to "+currentarmorpenalty);
				adjustment=currentarmorpenalty;
			} else {
				initiative.setArmorpenalty(currentarmorpenalty-adjustment);
			}
		}
		initiative.setModification(initiative.getModification()+adjustment);
		initiative.setStep(initiative.getBase()+initiative.getModification());
		initiative.setDice(PROPERTIES.step2Dice(initiative.getStep()));
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getAllTalents() ) readjustSkillInitiativeModifikator(talent,adjustment);
		}
		for( SKILLType skill : getSkills() ) readjustSkillInitiativeModifikator(skill,adjustment);
	}

	private static void readjustSkillInitiativeModifikator(SKILLType skill, int adjustment) {
		// Wenn der Skill gar keine Initiative-Skill ist, tut nichts
		if( ! skill.getIsinitiative().equals(YesnoType.YES) ) return;
		skill.setBonus(skill.getBonus()+adjustment);
		RANKType rank = skill.getRANK();
		if( rank == null ) {
			rank = new RANKType();
			skill.setRANK(rank);
		}
		rank.setBonus(rank.getBonus()+adjustment);
		rank.setStep(rank.getStep()+adjustment);
		rank.setDice(PROPERTIES.step2Dice(rank.getStep()));
	}

	public void clearLanguages() {
		character.getLANGUAGE().clear();
	}

	public LanguageContainer getDefaultLanguages() {
		String origin = getAppearance().getOrigin();
		String race = getAppearance().getRace();
		NAMEGIVERABILITYType namegiver = PROPERTIES.searchNamegiver(race,origin);
		LanguageContainer defaultlanguages = new LanguageContainer(PROPERTIES.getDefaultLanguage(origin));
		defaultlanguages.insertLanguages(namegiver.getDEFAULTLANGUAGE());
		return defaultlanguages;
	}

	public LanguageContainer getLanguages() {
		LanguageContainer defaultlanguages = getDefaultLanguages().copy();
		int[] defaultCountOfSpeakReadWrite = defaultlanguages.getCountOfSpeakReadWrite(null);
		LanguageContainer languages = new LanguageContainer(character.getLANGUAGE());
		for( CHARACTERLANGUAGEType l : defaultlanguages.getLanguages() ) {
			int[] currentCountOfSpeakReadWrite = languages.getCountOfSpeakReadWrite(null);
			if( (currentCountOfSpeakReadWrite[0]>=defaultCountOfSpeakReadWrite[0]) &&
					(currentCountOfSpeakReadWrite[1]>=defaultCountOfSpeakReadWrite[1]) ) break;
			languages.insertLanguage(l);
		}
		return languages;
	}

	public void fillOptionalTalentsRandom(String disciplinename) {
		int circleNr = getCircleOf(disciplinename);
		while(true) {
			List<Integer> l = getCircleOfMissingOptionalTalents().get(disciplinename);
			if( l.isEmpty() ) break;
			int circle = l.get(0);
			DISCIPLINE discipline = PROPERTIES.getDisziplin(disciplinename);
			List<TALENTABILITYType> talentlist = getUnusedOptionalTalents(discipline,circle);
			// Suche nach dem Talent Unempfindlichkeit
			TALENTABILITYType talent=null;
			for( TALENTABILITYType t : talentlist ) {
				if( t.getName().equals(durabilityName) ) {
					talent=t;
					TALENTType newtalent = addOptionalTalent(disciplinename, circle, talent, false);
					newtalent.getRANK().setRank(circleNr);
					break;
				}
			}
			if( talent==null ) {
				talent = talentlist.get(spaeterzufall(talentlist.size()));
				TALENTType newtalent = addOptionalTalent(disciplinename, circle, talent, false);
				newtalent.getRANK().setRank(circleNr-spaeterzufall(circle));
			}
		}
	}

	public static int spaeterzufall(int bereich) {
		int g=1;
		for( int i=0; i<bereich; i++ ) g+=i;
		int r = rand.nextInt(g);
		g=0;
		for( int i=0; i<bereich; i++ ) {
			if( r<=g ) return i;
			g+=i;
		}
		return bereich-1;
	}

	public void clearBloodDamage() {
		HEALTHType health = getHealth();
		// Ermittle aktuellen Blutschaden um die Anpassung der Todes und Bewustlosigkeitsschwelle zu korrigieren
		int blooddamge=health.getBlooddamage();
		DEATHType death = getDeath();
		death.setValue(death.getValue()+blooddamge);
		death.setAdjustment(death.getAdjustment()+blooddamge);
		DEATHType unconsciousness = getUnconsciousness();
		unconsciousness.setValue(unconsciousness.getValue()+blooddamge);
		unconsciousness.setAdjustment(unconsciousness.getAdjustment()+blooddamge);
		// Jetzt setze den Blutschaden auf 0
		health.setBlooddamage(0);
		// Ohne Blutschaden gibts auch keine "Zerfallsrate"
		health.setDepatterningrate(0);
	}

	public void addBloodDamgeFrom(ITEMType item) {
		HEALTHType health = getHealth();
		int itemBloodDamge = item.getBlooddamage();
		health.setBlooddamage(health.getBlooddamage()+itemBloodDamge);
		health.setDepatterningrate(health.getDepatterningrate()+item.getDepatterningrate());
		DEATHType death = health.getDEATH();
		death.setAdjustment(death.getAdjustment()-itemBloodDamge);
		DEATHType unconsciousness = health.getUNCONSCIOUSNESS();
		unconsciousness.setAdjustment(unconsciousness.getAdjustment()-itemBloodDamge);
	}

	public void insertKnack(KNACKType knack, String disciplinename, String talentname) {
		TALENTType talent = getTalentByDisciplinAndName(disciplinename, talentname);
		if( talent == null ) {
			System.err.println( "No talent '"+talentname+"' found within discipline '"+disciplinename+"' to insert knack '"+knack.getName()+"'");
		} else {
			talent.getKNACK().add(knack);
		}
	}

	public RulesetversionType getRulesetversion() {
		return character.getRulesetversion();
	}

	public static void makeMaxForge(ARMORType armor) {
		if( armor == null ) return;
		int physicalarmor = armor.getPhysicalarmor();
		Double p=Math.ceil(Double.valueOf(physicalarmor)/2.0d);
		armor.setPhysicalarmor(physicalarmor+p.intValue());
		int mysticarmor = armor.getMysticarmor();
		Double m=Math.ceil(Double.valueOf(mysticarmor)/2.0d);
		armor.setMysticarmor(mysticarmor+m.intValue());
	}

	public static void makeMaxForge(WEAPONType weapon) {
		if( weapon == null ) return;
		weapon.setDamagestep(weapon.getDamagestep()+weapon.getSize());
	}

	public static void copyItem(ITEMType src, ITEMType dst) { copyItem(src,dst,false); }
	public static void copyItem(ITEMType src, ITEMType dst, boolean setvirtual) {
		dst.setBlooddamage(src.getBlooddamage());
		dst.setBookref(src.getBookref());
		dst.setDepatterningrate(src.getDepatterningrate());
		dst.setDESCRIPTION(src.getDESCRIPTION());
		dst.setKind(src.getKind());
		dst.setLocation(src.getLocation());
		dst.setName(src.getName());
		dst.setPrice(src.getPrice());
		dst.setSize(src.getSize());
		dst.setUsed(src.getUsed());
		dst.setWeight(src.getWeight());
		if( setvirtual ) dst.setVirtual(YesnoType.YES);
		else dst.setVirtual(src.getVirtual());
		List<Base64BinaryType> images = dst.getIMAGE();
		for( Base64BinaryType image : src.getIMAGE()) images.add(copyImage(image));
	}

	public static void copyItem(MAGICITEMType src, MAGICITEMType dst) { copyItem(src,dst,false); }
	public static void copyItem(MAGICITEMType src, MAGICITEMType dst, boolean setvirtual) {
		copyItem((ITEMType)src,(ITEMType)dst,setvirtual);
		dst.setEnchantingdifficultynumber(src.getEnchantingdifficultynumber());
		dst.setEffect(src.getEffect());
		dst.setSpelldefense(src.getSpelldefense());
	}

	public static void copyItem(PATTERNITEMType src, PATTERNITEMType dst) { copyItem(src,dst,false); }
	public static void copyItem(PATTERNITEMType src, PATTERNITEMType dst, boolean setvirtual) {
		copyItem((MAGICITEMType)src,(MAGICITEMType)dst,setvirtual);
		dst.setPatternkind(src.getPatternkind());
		dst.setTruepattern(src.getTruepattern());
		dst.setKeyknowledge(src.getKeyknowledge());
		dst.setWeaventhreadrank(src.getWeaventhreadrank());
	}

	public static void copyItem(THREADITEMType src, THREADITEMType dst) { copyItem(src,dst,false); }
	public static void copyItem(THREADITEMType src, THREADITEMType dst, boolean setvirtual) {
		copyItem((MAGICITEMType)src,(MAGICITEMType)dst,setvirtual);
		dst.setLpcostgrowth(src.getLpcostgrowth());
		dst.setMaxthreads(src.getMaxthreads());
		dst.setWeaventhreadrank(src.getWeaventhreadrank());
		dst.setARMOR(copyArmor(src.getARMOR(), setvirtual));
		dst.setSHIELD((SHIELDType)copyArmor(src.getSHIELD(), setvirtual));
		dst.setWEAPON(copyWeapon(src.getWEAPON(), setvirtual));
		List<THREADRANKType> ranks = dst.getTHREADRANK();
		for( THREADRANKType rank : src.getTHREADRANK() ) {
			ranks.add(copyThreadRank(rank));
		}
	}

	public static Base64BinaryType copyImage(Base64BinaryType image) {
		if( image == null ) return null;
		Base64BinaryType result = new Base64BinaryType();
		result.setContenttype(image.getContenttype());
		result.setValue(image.getValue());
		return result;
	}
}
