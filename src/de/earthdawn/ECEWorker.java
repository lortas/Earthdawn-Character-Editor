package de.earthdawn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import org.apache.commons.configuration.SubnodeConfiguration;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.APPEARANCEType;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.CARRYINGType;
import de.earthdawn.data.DEATHType;
import de.earthdawn.data.DEFENSEType;
import de.earthdawn.data.DiceType;
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

		int attributepoints =25; // TODO: = /OPTIONALRULES/ATTRIBUTE/points
		// Pro Atributt des Charakters werden nun dessen Werte, Stufe, und Würfel bestimmt.
		String query = String.format("/NAMEGIVER[@name='%s']/ATTR_START", race);
		List<?> properties = ApplicationProperties.create().getNamegivers().configurationsAt(query);
		for (Object property : properties) {			
			SubnodeConfiguration subnode = (SubnodeConfiguration) property;

			String id = subnode.getString("/@name");
			ATTRIBUTEType attribute = JAXBHelper.getAttribute(charakter, id);
			int value = attribute.getBasevalue() + attribute.getLpincrease();
			attribute.setCurrentvalue(value);
			StepDice stepdice=attribute2StepAndDice(value);
			attribute.setDice(DiceType.valueOf(stepdice.getdice()));
			attribute.setStep(BigInteger.valueOf(stepdice.getstep()));

			int attributbasis = subnode.getInt("/@value");
			int modifier = attribute.getBasevalue()-attributbasis;
			attribute.setCost(berechneAttriubteCost(modifier));
		}

		DEFENSEType defense = JAXBHelper.getDefence(charakter);
		defense.setPhysical(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "DEX").getCurrentvalue()));
		defense.setSpell(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "PER").getCurrentvalue()));
		defense.setSocial(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "CHA").getCurrentvalue()));

		CARRYINGType carrying = JAXBHelper.getCarrying(charakter);
		int tmp=berechneTraglast(JAXBHelper.getAttribute(charakter, "STR").getCurrentvalue());
		carrying.setCarrying(tmp);
		carrying.setLifting(tmp *2);
		HEALTHType health = JAXBHelper.getHealth(charakter);
		List<Integer> newhealth = bestimmeHealth(JAXBHelper.getAttribute(charakter, "TOU").getCurrentvalue());
		for (JAXBElement<?> elem : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (elem.getName().getLocalPart().equals("DEATH")) {
				((DEATHType)elem.getValue()).setValue(newhealth.get(0));
			} else if (elem.getName().getLocalPart().equals("UNCONSCIOUSNESS")) {
				((DEATHType)elem.getValue()).setValue(newhealth.get(1));
			} else if (elem.getName().getLocalPart().equals("WOUNDS")) {
				((WOUNDType)elem.getValue()).setPenalties(newhealth.get(2));
			} else if (elem.getName().getLocalPart().equals("RECOVERY")) {
				((RECOVERYType)elem.getValue()).setStep(newhealth.get(3));
			}
		}

		
		//TODO:
		// EDCHARAKTER/INITIATIVE/base=JAXBHelper.getAttribute(charakter, "DEX").getStep();
		// EDCHARAKTER/INITIATIVE/step=JAXBHelper.getAttribute(charakter, "DEX").getStep();
		// EDCHARAKTER/INITIATIVE/modification=0;
		// EDCHARAKTER/INITIATIVE/dice=JAXBHelper.getAttribute(charakter, "DEX").getDice();
		//
		// EDCHARAKTER/PROTECTION/physicalarmor=0;
		// EDCHARAKTER/PROTECTION/mysticarmor=0;
		// EDCHARAKTER/PROTECTION/penalty=0;
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/weight=0;
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/used=yes;
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/location="self";
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/physicalarmor=0;
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/penalty0;
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/edn=0;
		// EDCHARAKTER/PROTECTION/ARMOR[name="natural armor"]/mysticarmor=berechneMysticArmor(JAXBHelper.getAttribute(charakter, "WIL").getCurrentvalue());
		// EDCHARAKTER/KARMA/max=JAXBHelper.getKarmaritualRank(charakter) *  ( "/NAMEGIVER[@name='%s']/KARMA/modifier",race ) + AttributeCostPool;

		
		return charakter;
	}
	public int berechneWiederstandskraft(Integer value) {
		int actualRating=0;
		// TODO: übersichtliche Alternative, funktioniert aber wahrscheinlich nicht
//		String query = String.format("/DEFENSERAITING[@defense='%d']/@attribute", value.intValue());
//		int actualRaiting = ApplicationProperties.create().getCharacteristics().getInt(query);

		for (Object defenserating : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/DEFENSERAITING")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) defenserating;
			int attribute = subnode.getInt("/@attribute");
			int defense = subnode.getInt("/@defense");
			if( (value.intValue() <= attribute) && (actualRating<defense) ) {
				actualRating=defense;
			}
		}
		return actualRating;
	}	

	public int berechneTraglast (Integer value) {
		if ( value.intValue()< 1) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			value = 1;
		}
		String query = String.format("/ENCUMBRANCE[@attribute='%d']/@carrying", value.intValue());
		// TODO: Fehlermeldung, wenn "value" größer als es Elemente in der Tabelle ENCUMBRANCE gibt.
		int carrying = ApplicationProperties.create().getCharacteristics().getInt(query);
		return carrying;
	}	

	public int berechneAttriubteCost(int modifier) {
		if ( modifier < -1 ) {
			// TODO Warnung ausgeben, aber weiter machen
			modifier = -1;
		}
		if ( modifier > 8 ) {
			// TODO Warnung ausgeben, aber weiter machen
			modifier = 8;
		}
		String query = String.format("/ATTRIBUTECOST[@modifier='%d']/@cost", modifier);
		int cost = ApplicationProperties.create().getCharacteristics().getInt(query);
		return cost;
	}

	public List<Integer> bestimmeHealth (int wert) {
		for (Object defenserating : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/HEALTHRATING")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) defenserating;
			int value = subnode.getInt("/@value");
			if (wert== value) {
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

	public int berechneMysticArmor(int value) {
		int actualArmor=0;
		for (Object mysticarmor : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/MYSTICARMOR")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) mysticarmor;
			int attribute = subnode.getInt("/@attribute");
			int armor = subnode.getInt("/@armor");
			if( (value <= attribute) && (actualArmor<armor) ) {
				actualArmor=armor;
			}
		}
		return actualArmor;
	}	

	public StepDice attribute2StepAndDice(BigInteger value) {
		int actualStep=0;
		String actualDice="";
		for (Object stepdice : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/STEPDICETABLE")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) stepdice;
			int attribute = subnode.getInt("/@attribute");
			int step = subnode.getInt("/@step");
			String dice = subnode.getString("/@dice");
			if( (value.intValue() <= attribute) && (actualStep<step) ) {
				actualStep = step;
				actualDice = dice;
			}
		}
		return new StepDice(actualStep,actualDice);
	}	
}
