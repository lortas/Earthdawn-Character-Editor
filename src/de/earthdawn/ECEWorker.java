package de.earthdawn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.configuration.SubnodeConfiguration;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.CARRYINGType;
import de.earthdawn.data.DEATHType;
import de.earthdawn.data.DEFENSEABILITYType;
import de.earthdawn.data.DEFENSEType;
import de.earthdawn.data.DiceType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.HEALTHType;
import de.earthdawn.data.INITIATIVEType;
import de.earthdawn.data.KARMAType;
import de.earthdawn.data.MOVEMENTType;
import de.earthdawn.data.NAMEGIVERABILITYType;
import de.earthdawn.data.NAMEVALUEType;
import de.earthdawn.data.PROTECTIONType;
import de.earthdawn.data.RECOVERYType;
import de.earthdawn.data.STEPDICEType;
import de.earthdawn.data.TALENTType;
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
	public EDCHARACTER verarbeiteCharakter(EDCHARACTER charakter) {
		// Benötige Rasseneigenschaften der gewählten Rasse im Objekt "charakter":
		String race = JAXBHelper.getAppearance(charakter).getRace();

		if (race == null) {
			// XXX: Konfigurationsproblem?
			//return charakter;
		}
		NAMEGIVERABILITYType namegiver = null;
		for (NAMEGIVERABILITYType n : ApplicationProperties.create().getNamegivers().getNAMEGIVER()) {
			if( n.getName().equals(race)) {
				namegiver = n;
			}
		}

		// **ATTRIBUTE**
		int karmaMaxBonus =25; // TODO: = /OPTIONALRULES/ATTRIBUTE/points
		// Der Bonus auf das Maximale Karma ergibt sich aus den übriggebliebenen Kaufpunkten bei der Charaktererschaffung
		for (NAMEVALUEType raceattribute : namegiver.getATTRIBUTE()) {
			// Pro Atributt wird nun dessen Werte, Stufe und Würfel bestimmt

			ATTRIBUTEType attribute = JAXBHelper.getAttribute(charakter, raceattribute.getName());
			attribute.setRacevalue(raceattribute.getValue());
			int value = attribute.getRacevalue() + attribute.getGenerationvalue() + attribute.getLpincrease();
			attribute.setCurrentvalue(value);
			attribute.setCost(berechneAttriubteCost(attribute.getLpincrease()));
			STEPDICEType stepdice=attribute2StepAndDice(value);
			attribute.setDice(stepdice.getDice());
			attribute.setStep(stepdice.getStep());
			karmaMaxBonus-=attribute.getCost();
		}
		if( karmaMaxBonus <0 ) {
			// TODO: Warnung ausgeben
		}

		// **DEFENSE**
		DEFENSEType defense = JAXBHelper.getDefence(charakter);
		defense.setPhysical(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "DEX").getCurrentvalue()));
		defense.setSpell(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "PER").getCurrentvalue()));
		defense.setSocial(berechneWiederstandskraft(JAXBHelper.getAttribute(charakter, "CHA").getCurrentvalue()));
		
		for(DEFENSEABILITYType racedefense : namegiver.getDEFENSE() ) {
			switch (racedefense.getKind()) {
			case PHYSICAL: defense.setPhysical(defense.getPhysical()+1); break;
			case SPELL: defense.setSpell(defense.getSpell()+1); break;
			case SOCIAL: defense.setSocial(defense.getSocial()+1); break;
			}
		}

		// **INITIATIVE**
		STEPDICEType initiativeStepdice=attribute2StepAndDice(JAXBHelper.getAttribute(charakter, "DEX").getCurrentvalue());
		INITIATIVEType initiative = JAXBHelper.getInitiative(charakter);
		// Setze alle Initiative Modifikatoren zurück, da diese im folgenden neu bestimmt werden.
		initiative.setModification(0);
		initiative.setBase(initiativeStepdice.getStep());
		initiative.setStep(initiativeStepdice.getStep());
		initiative.setDice(initiativeStepdice.getDice());

		// **HEALTH**
		HEALTHType health = JAXBHelper.getHealth(charakter);
		List<Integer> newhealth = bestimmeHealth(JAXBHelper.getAttribute(charakter, "TOU").getCurrentvalue());
		for (JAXBElement<?> elem : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (elem.getName().getLocalPart().equals("DEATH")) {
				((DEATHType)elem.getValue()).setValue(newhealth.get(0));
				((DEATHType)elem.getValue()).setBase(newhealth.get(0));
				((DEATHType)elem.getValue()).setAdjustment(0);
			} else if (elem.getName().getLocalPart().equals("UNCONSCIOUSNESS")) {
				((DEATHType)elem.getValue()).setValue(newhealth.get(1));
				((DEATHType)elem.getValue()).setBase(newhealth.get(1));
				((DEATHType)elem.getValue()).setAdjustment(0);
			} else if (elem.getName().getLocalPart().equals("WOUNDS")) {
				((WOUNDType)elem.getValue()).setThreshold(newhealth.get(2)+namegiver.getWOUND().getThreshold());
			} else if (elem.getName().getLocalPart().equals("RECOVERY")) {
				((RECOVERYType)elem.getValue()).setStep(newhealth.get(3));
				((RECOVERYType)elem.getValue()).setDice(step2Dice(newhealth.get(3)));
			}
		}

		// **KARMA**
		TALENTType karmaritual=JAXBHelper.getKarmaritual(charakter);
		int maxkarma = namegiver.getKarmamodifier() * karmaritual.getRANK().getRank();
		KARMAType karma=JAXBHelper.getKarma(charakter);
		karma.setMaxmodificator(karmaMaxBonus);
		// Die Übriggebliebenen Kaufpunkte erhöhen das maximale Karma
		maxkarma += karmaMaxBonus;
		karma.setMax(maxkarma);
		
		// **MOVEMENT**
		MOVEMENTType movement = JAXBHelper.getMovement(charakter);
		movement.setFlight(namegiver.getMovementFlight());
		movement.setGround(namegiver.getMovementGround());

		// **CARRYING**
		CARRYINGType carrying = JAXBHelper.getCarrying(charakter);
		int tmp=berechneTraglast(JAXBHelper.getAttribute(charakter, "STR").getCurrentvalue());
		carrying.setCarrying(tmp);
		carrying.setLifting(tmp *2);

		ARMORType naturalArmor = namegiver.getARMOR();
		naturalArmor.setMysticarmor(berechneMysticArmor(JAXBHelper.getAttribute(charakter, "WIL").getCurrentvalue()));
		PROTECTIONType protection = JAXBHelper.getProtection(charakter);
		int mysticalarmor=naturalArmor.getMysticarmor();
		int pysicalarmor=naturalArmor.getPhysicalarmor();
		int protectionpenalty=naturalArmor.getPenalty();
		List <ARMORType> newarmor = new ArrayList<ARMORType>();
		newarmor.add(naturalArmor);
		for (ARMORType armor : protection.getARMOROrSHIELD() ) {
			if( ! armor.getName().equals("natural armor")) {
				newarmor.add(armor);
			}
			mysticalarmor+=armor.getMysticarmor();
			pysicalarmor+=armor.getPhysicalarmor();
			protectionpenalty+=armor.getPenalty();
		}
		protection.setMysticarmor(mysticalarmor);
		protection.setPenalty(protectionpenalty);
		protection.setPhysicalarmor(pysicalarmor);
		protection.getARMOROrSHIELD().clear();
		protection.getARMOROrSHIELD().addAll(newarmor);

		String abilities = JAXBHelper.getAbilities(charakter);
		abilities = abilities.replaceAll(".", ""); // String leeren um ihn neu zu füllen
		for ( String s : namegiver.getABILITY() ) {
			abilities.concat(s);
			abilities.concat(", ");
		}
		// TODO: NAMEGIVER Talente in die Talentliste des Chars aufnehmen.
		// Dabei aber sicher stellen, das sie nicht doppelt enthalten sind
		return charakter;
	}
	public int berechneWiederstandskraft(Integer value) {
		int actualRating=0;
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

	public STEPDICEType attribute2StepAndDice(int value) {
		STEPDICEType tmp = new STEPDICEType();
		tmp.setStep(0);
		tmp.setDice(DiceType.D_6);
		for (Object stepdice : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/STEPDICETABLE")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) stepdice;
			int attribute = subnode.getInt("/@attribute");
			int step = subnode.getInt("/@step");
			String dice = subnode.getString("/@dice");
			if( (value <= attribute) && (tmp.getStep()<step) ) {
				tmp.setStep(step);
				tmp.setDice(DiceType.valueOf(dice));
			}
		}
		return tmp;
	}	

	public DiceType step2Dice(int value) {
		for (Object stepdice : ApplicationProperties.create().getCharacteristics().getList("/CHARACTERISTICS/STEPDICETABLE")) {
			SubnodeConfiguration subnode = (SubnodeConfiguration) stepdice;
			int step = subnode.getInt("/@step");
			String dice = subnode.getString("/@dice");
			if( value == step ) {
				return DiceType.valueOf(dice);
			}
		}
		// Not found
		return null;
	}	
}
