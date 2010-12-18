package de.earthdawn.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

import de.earthdawn.data.CAPABILITIES;
import de.earthdawn.data.DISCIPLINE;
import de.earthdawn.data.KNACKS;
import de.earthdawn.data.NAMEGIVERS;
import de.earthdawn.data.SPELLS;

/** 
 * Klasse mit Konfigurations-Parametern 
 */
public class ApplicationProperties {

    /** Ein- und Ausgabe der Allgemeinen Konfigurationseinstellungen. */
    private static final XMLConfiguration GLOBAL_CONFIG = new XMLConfiguration();

    private static CAPABILITIES CAPABILITIES = new CAPABILITIES();
    private static KNACKS KNACKS = new KNACKS();
    private static SPELLS SPELLS = new SPELLS();
    private static NAMEGIVERS NAMEGIVERS = new NAMEGIVERS();
    
    /** Singleton-Instanz dieser Klasse. */
    private static ApplicationProperties theProps = null;

    /** Anzeigetexte (Steuerelemente) */
    private ResourceBundle MESSAGES = null;

    /** Anzeigetexte (Charakterattribute). */
    private static final XMLConfiguration NAMES = new XMLConfiguration();

    /** Konfiguration für die einzelnen Races. */
    private static final XMLConfiguration CHARACTERISTICS = new XMLConfiguration();

    /** Disziplinen (Name Label geordnet) */
    private static final Map<String, DISCIPLINE> DISCIPLINES = new TreeMap<String, DISCIPLINE>();

    private ApplicationProperties() {
    	init();
    }

	/**
	 * Zugriff auf Applikations-Konstanten.
	 * 
	 * @return Instanz der Applikations-Konstanten.
	 */
	public static ApplicationProperties create() {
		if (theProps == null) {
			theProps = new ApplicationProperties();
		}
		
		return theProps;
	}

	/**
	 * Gibt internationalisierten Text zu <code>key</code> zur�ck.
	 */
	public String getMessage(String key) {
		return MESSAGES.getString(key);
	}
	
	public DISCIPLINE getDisziplin(String name) {
		return DISCIPLINES.get(name);
	}

	public NAMEGIVERS getNamegivers() {
		return NAMEGIVERS;
	}
	
	public XMLConfiguration getCharacteristics() {
		return CHARACTERISTICS;
	}
	
	public static CAPABILITIES getCapabilities() {
		return CAPABILITIES;
	}

	public static KNACKS getKnacks() {
		return KNACKS;
	}

	public static SPELLS getSpells() {
		return SPELLS;
	}

	private void init() {
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			Unmarshaller u = jc.createUnmarshaller();
			String filename="";

			// globale konfiguration einlesen
			filename="./config/application.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			GLOBAL_CONFIG.setValidating(false);
			GLOBAL_CONFIG.load(new File(filename));

			// anzeigetexte (steuerlemente)
			String language = GLOBAL_CONFIG.getString("config.language");
			String country = GLOBAL_CONFIG.getString("config.country");
			MESSAGES = ResourceBundle.getBundle("de.earthdawn.config.messages", new Locale(language, country));
			
			// anzeigetexte (charakterattribute).
			filename="./config/names.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			NAMES.setValidating(false);
			NAMES.load(new File(filename));
			
			// Konfiguration für die RACES einlesen.
			filename="./config/characteristics.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			CHARACTERISTICS.setValidating(false);
			CHARACTERISTICS.load(new File(filename));
			CHARACTERISTICS.setExpressionEngine(new XPathExpressionEngine());

			// disziplinen laden
			// --- Bestimmen aller Dateien im Unterordner 'disziplins'
			File[] files = new File("./config/disciplines").listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name != null && name.endsWith(".xml");
				}
			});
			// --- Einlesen der Dateien
			for(File disConfigFile : files) {
				System.out.println("Lese Konfigurationsdatei: '" + disConfigFile.getCanonicalPath() + "'");
				DISCIPLINE dis = (DISCIPLINE) u.unmarshal(disConfigFile);
				DISCIPLINES.put(dis.getName(), dis);
			}

			filename="./config/capabilities.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			CAPABILITIES = (CAPABILITIES) u.unmarshal(new File(filename));
			filename="./config/knacks.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			KNACKS = (KNACKS) u.unmarshal(new File(filename));
			filename="./config/spells.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			SPELLS = (SPELLS) u.unmarshal(new File(filename));

			filename="./config/namegivers.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			NAMEGIVERS = (NAMEGIVERS) u.unmarshal(new File(filename));

		} catch (Throwable e) {
			// Fehler ist grundsätzlicher Natur ...
			throw new RuntimeException(e);
		}
	}
}
