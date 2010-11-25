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
			String id = subnode.getString("/@id");
			int attributbasis = subnode.getInt("/@value");
			
			ATTRIBUTEType attribute = JAXBHelper.getAttribute(charakter, id);		
			int v = attributbasis + attribute.getBasevalue().intValue() + attribute.getStep().intValue();
			attribute.setCurrentvalue(BigInteger.valueOf(v));
		}
		
		// charakter.setWiederstandskraft.koerperlich(berechneWiederstandskraft(charakter.getAttribut("DEX","wert")));
		// charakter.setWiederstandskraft.magisch(berechneWiederstandskraft(charakter.getAttribut("PER","wert")));
		// charakter.setWiederstandskraft.sozial(berechneWiederstandskraft(charakter.getAttribut("CHA","wert")));
		// charakter.setTraglast(berechneTraglast(charakter.getAttribut("STR","wert")))
		// charakter.setHebelimit(berechneHebelimit(charakter.getAttribut("STR","wert")))
		return charakter;
	}
	public int berechneWiederstandskraft (int wert) {
		int[] array = {2,3,3,4,4,4,5,5,6,6,7,7,7,8,8,9,9,10,10,10,11,11,12,12,13,13,13,14,14,15};
		// TODO: prüfe ob wert im wertebereich liegt
		return array[wert];
	}	
	public int berechneTraglast (int wert) {
		int[] array = {10,15,20,25,30,35,40,50,60,70,80,90,105,125,145,165,200,230,270,315,360,430,500,580,675,790,920,1075,1200,1450};
		// TODO: prüfe ob wert im wertebereich liegt
		return array[wert];
	}	
	public int berechneHebelimit (int wert) {
		int[] array = {20,30,40,50,65,75,85,100,115,135,160,185,210,250,290,310,400,460,540,630,735,860,1000,1160,1350,1580,1840,2150,2500,2900};
		// TODO: prüfe ob wert im wertebereich liegt
		return array[wert];
	}	
};

