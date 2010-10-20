package de.earthdawn.config;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

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

    /** Anzeigetexte */
    private ResourceBundle MESSAGES = null;
    
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

	private void init() {
		try {
			// globale konfiguration einlesen
			GLOBAL_CONFIG.setValidating(false);
			GLOBAL_CONFIG.load(new File("./config/application.xml"));

			// anzeigetexte
			String language = GLOBAL_CONFIG.getString("config.language");
			String country = GLOBAL_CONFIG.getString("config.country");
			MESSAGES = ResourceBundle.getBundle("de.earthdawn.config.messages", new Locale(language, country));
			
			// capabilities laden
			CAPABILITIES_CONFIG.setValidating(false);
			CAPABILITIES_CONFIG.load(new File("./config/capabilities.xml"));
			
			// disziplinen laden
			
		} catch (Throwable e) {
			// Fehler ist grundsÃ¤tzlicher Natur ...
			throw new RuntimeException(e);
		}
	}
}
