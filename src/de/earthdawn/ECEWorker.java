package de.earthdawn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.configuration.SubnodeConfiguration;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.CARRYINGType;
import de.earthdawn.data.DEATHType;
import de.earthdawn.data.DEFENSEType;
import de.earthdawn.data.EDCHARAKTER;
import de.earthdawn.data.HEALTHType;
import de.earthdawn.data.RECOVERYType;
import de.earthdawn.data.WOUNDType;

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
		String race = JAXBHelper.getAppearance(charakter).getRace();

		if (race == null) {
			// XXX: Konfigurationsproblem?
			//return charakter;
		}

		// Pro Atributt des Charakters werden nun dessen Werte, Stufe, und Würfel bestimmt.
		String query = String.format("/NAMEGIVER[@name='%s']/ATTR_START", race);
		List<?> properties = ApplicationProperties.create().getNamegivers().configurationsAt(query);
		for (Object property : properties) {			
			SubnodeConfiguration subnode = (SubnodeConfiguration) property;
			String id = subnode.getString("/@name");
			int attributbasis = subnode.getInt("/@value");

			ATTRIBUTEType attribute = JAXBHelper.getAttribute(charakter, id);		
			int v = attributbasis + attribute.getBasevalue().intValue() + attribute.getStep().intValue();
			attribute.setCurrentvalue(BigInteger.valueOf(v));
		}

		DEFENSEType defense = JAXBHelper.getDefence(charakter);
		defense.setPhysical(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "DEX").getCurrentvalue()));
		defense.setSpell(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "PER").getCurrentvalue()));
		defense.setSocial(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "CHA").getCurrentvalue()));

		CARRYINGType carrying = JAXBHelper.getCarrying(charakter);
		BigInteger tmp=berechneTraglast(JAXBHelper.getAttribute(charakter, "STR").getCurrentvalue());
		carrying.setCarrying(tmp);
		carrying.setLifting(tmp.multiply(BigInteger.valueOf(2)));
		HEALTHType health = JAXBHelper.getHealth(charakter);
		List<Integer> newhealth = bestimmeHealth(JAXBHelper.getAttribute(charakter, "TOU").getCurrentvalue());
		//TODO:
		
		for (JAXBElement<?> elem : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (elem.getName().getLocalPart().equals("DEATH")) {
				((DEATHType)elem.getValue()).setValue(BigInteger.valueOf(newhealth.get(0)));
			} else if (elem.getName().getLocalPart().equals("UNCONSCIOUSNESS")) {
				((DEATHType)elem.getValue()).setValue(BigInteger.valueOf(newhealth.get(1)));
			} else if (elem.getName().getLocalPart().equals("WOUNDS")) {
				((WOUNDType)elem.getValue()).setPenalties(BigInteger.valueOf(newhealth.get(2)));
			} else if (elem.getName().getLocalPart().equals("RECOVERY")) {
				((RECOVERYType)elem.getValue()).setStep(BigInteger.valueOf(newhealth.get(3)));
			}
		}
		
		return charakter;
	}
	public BigInteger berechneWiederstandskraft(BigInteger value) {
		int actualRating=0;

		// TODO:  Vielleicht übersichtlichere Variante:
//		String query = String.format("/DEFENSERAITING[@defense='%d']/@attribute", wert.intValue());
//		int actualRaiting = ApplicationProperties.create().getCharacteristics().getInt(query);

		for (Object defenserating : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/DEFENSERAITING")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) defenserating;
			int attribute = subnode.getInt("/@attribute");
			int defense = subnode.getInt("/@defense");
			if( (value.intValue() <= attribute) && (actualRating<defense) ) {
				actualRating=defense;
			}
		}
		return BigInteger.valueOf(actualRating);
	}	

	public BigInteger berechneTraglast (BigInteger value) {
		// TODO: getDefensraiting zerlegt die Leerzeichen separierte Liste in ein Array (oder Liste?)
		List<?> encumbrance = ApplicationProperties.create().getCharacteristics().configurationsAt(String.format("/CHARACTERISTICS/ENCUMBRANCE"));
		// TODO: Fehlermeldung, wenn wert größer als Elemete in der Tabelle DEFENSERAITING
		if ( value.intValue()< 1) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			value = BigInteger.ONE;
		}
//		return encumbrance.get(value.intValue());
		
		return null;
		
	}	

	public List<Integer> bestimmeHealth (BigInteger wert) {
		for (Object defenserating : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/HEALTHRATING")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) defenserating;
			int value = subnode.getInt("/@value");
			if( wert.intValue() == value) {
				List<Integer> health = new ArrayList<Integer>();
				health.add(subnode.getInt("/@death"));
				health.add(subnode.getInt("/@unconsciousness"));
				health.add(subnode.getInt("/@wound"));
				health.add(subnode.getInt("/@recovery"));

				return health;
			}
		}
		// not found
		return null;
	}	
}

