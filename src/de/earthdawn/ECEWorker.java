package de.earthdawn;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.configuration.SubnodeConfiguration;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.EDCHARAKTER;

/**
 * Hilfsklasse zur Verarbeitung eines Earthdawn-Charakters. 
 * 
 * @author lortas
 */
public class ECEWorker {

	/**
	 * Verabeiten eines Charakters.
	 */
	public EDCHARAKTER verarbeiteCharakter(EDCHARAKTER charakter) {
		// Benötige Rasseneigenschaften der gewählten Rasse im Objekt "charakter":
		String race = JAXBHelper.getRace(charakter);

		if (race == null) {
			// XXX: Konfigurationsproblem?
			//return charakter;
			race = "Dwarf";
		}

		// Pro Atributt des Charakters werden nun dessen Werte, Stufe, und Würfel bestimmt.
		List<?> properties = ApplicationProperties.create().getNamegivers().configurationsAt(String.format("/NAMEGIVER[@name='%s']/ATTR_START", race));
		for (Object property : properties) {			
			SubnodeConfiguration subnode = (SubnodeConfiguration) property;
			String id = subnode.getString("/@name");
			int attributbasis = subnode.getInt("/@value");
			
			ATTRIBUTEType attribute = JAXBHelper.getAttribute(charakter, id);		
			int v = attributbasis + attribute.getBasevalue().intValue() + attribute.getStep().intValue();
			attribute.setCurrentvalue(BigInteger.valueOf(v));
		}
		// EDCHARAKTER/DEFENSE/physical = berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "DEX").getCurrentvalue());
		// EDCHARAKTER/DEFENSE/spell = berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "PER").getCurrentvalue());
		// EDCHARAKTER/DEFENSE/social = berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "CHA").getCurrentvalue());
		// int tmp=berechneTraglast(JAXBHelper.getAttribute(charakter, "STR").getCurrentvalue())
		// EDCHARAKTER/CARRYING/carrying= tmp;
		// EDCHARAKTER/CARRYING/lifting = tmp *2;
		// EDCHARAKTER/HEALTH = bestimmeHealth(JAXBHelper.getAttribute(charakter, "TOU").getCurrentvalue())
		return charakter;
	}
	public int berechneWiederstandskraft (BigInteger wert) {
		// TODO: getDefensraiting zerlegt die Leerzeichen separierte Liste in ein Array (oder Liste?)
		List<?> defensrating = ApplicationProperties.create().getDefensraiting().configurationsAt(String.format("/CHARACTERISTICS/DEFENSERAITING"));
		// TODO: Fehlermeldung, wenn wert größer als Elemete in der Tabelle DEFENSERAITING
		if ( wert < 1) {
			// wenn Wert kleiner 1, dann keine Fehlermedung, sondern nur den Wert korrigieren 
			wert = 1;
		}
		return defensrating.get(wert);
	}	
	public int berechneTraglast (BigInteger wert) {
		// TODO: getDefensraiting zerlegt die Leerzeichen separierte Liste in ein Array (oder Liste?)
		List<?> encumbrance = ApplicationProperties.create().getDefensraiting().configurationsAt(String.format("/CHARACTERISTICS/ENCUMBRANCE"));
		// TODO: Fehlermeldung, wenn wert größer als Elemete in der Tabelle DEFENSERAITING
		if ( wert < 1) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			wert = 1;
		}
		return encumbrance.get(wert);
	}	
	public health bestimmeHealth (BigInteger wert) {
		// TODO: Fehlermeldung, wenn wert größer als Elemete in der Tabelle DEFENSERAITING
		if ( wert < 1) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			wert = 1;
		}
		return ApplicationProperties.create().getHealthraiting().configurationsAt(String.format("/CHARACTERISTICS/HEALTHRATING[@value='%d']",wert));
	}	
};