package de.earthdawn.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.commons.configuration.XMLConfiguration;

/** 
 * Klasse mit Konfigurations-Parametern 
 */
public class ApplicationProperties {
	
    /** Ein- und Ausgabe der Allgemeinen Konfigurationseinstellungen. */
    private static final XMLConfiguration GLOBAL_CONFIG = new XMLConfiguration();

    /** Ein- und Ausgabe der Allgemeinen Konfigurationseinstellungen. */
    private static final XMLConfiguration CAPABILITIES_CONFIG = new XMLConfiguration();
    
    /** Singleton-Instanz dieser Klasse. */
    private static ApplicationProperties theProps = null;

    /** Anzeigetexte (Steuerelemente) */
    private ResourceBundle MESSAGES = null;

    /** Anzeigetexte (Charakterattribute). */
    private static final XMLConfiguration NAMES = new XMLConfiguration();

    /** Disziplinen (Name Label geordnet) */
    private static final Map<String, XMLConfiguration> DISZIPLINES = new TreeMap<String, XMLConfiguration>();

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
	 * Gibt internationalisierten Text zu <code>key</code> zurück.
	 */
	public String getMessage(String key) {
		return MESSAGES.getString(key);
	}
	
	public XMLConfiguration getDisziplin(String name) {
		return DISZIPLINES.get(name);
	}

	private void init() {
		try {
			// globale konfiguration einlesen
			GLOBAL_CONFIG.setValidating(false);
			GLOBAL_CONFIG.load(new File("./config/application.xml"));

			// anzeigetexte (steuerlemente)
			String language = GLOBAL_CONFIG.getString("config.language");
			String country = GLOBAL_CONFIG.getString("config.country");
			MESSAGES = ResourceBundle.getBundle("de.earthdawn.config.messages", new Locale(language, country));
			
			// anzeigetexte (charakterattribute).
			NAMES.setValidating(false);
			NAMES.load(new File("./config/names.xml"));

			// capabilities laden
			CAPABILITIES_CONFIG.setValidating(false);
			CAPABILITIES_CONFIG.load(new File("./config/capabilities.xml"));

			// disziplinen laden
			// --- Bestimmen aller Dateien im Unterordner 'disziplins'
			File[] files = new File("./config/disziplins").listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name != null && name.endsWith(".xml");
				}
			});
			// --- Einlesen der Dateien
			for(File disConfigFile : files) {
				XMLConfiguration configuration = new XMLConfiguration();
				configuration.setValidating(false);
				configuration.load(disConfigFile);
				String label = configuration.getString("[@label]");
				DISZIPLINES.put(label, configuration);
			}
		} catch (Throwable e) {
			// Fehler ist grundsÃ¤tzlicher Natur ...
			throw new RuntimeException(e);
		}
	}
}
