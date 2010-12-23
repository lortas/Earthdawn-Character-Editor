package de.earthdawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.*;


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
		CharacterContainer character = new CharacterContainer(charakter);
		int totalCalculatedLPSpend=0;

		// Benötige Rasseneigenschaften der gewählten Rasse im Objekt "charakter":
		NAMEGIVERABILITYType namegiver = null;
		String race = character.getAppearance().getRace();
		for (NAMEGIVERABILITYType n : ApplicationProperties.create().getNamegivers().getNAMEGIVER()) {
			if( n.getName().equals(race)) {
				namegiver = n;
			}
		}
		
		// **ATTRIBUTE**
		int karmaMaxBonus =ApplicationProperties.create().getOptionalRules().getATTRIBUTE().getPoints();
		// Der Bonus auf das Maximale Karma ergibt sich aus den übriggebliebenen Kaufpunkten bei der Charaktererschaffung
		for (NAMEVALUEType raceattribute : namegiver.getATTRIBUTE()) {
			// Pro Atributt wird nun dessen Werte, Stufe und Würfel bestimmt

			ATTRIBUTEType attribute = character.getAttributes().get(raceattribute.getName());
			attribute.setRacevalue(raceattribute.getValue());
			int value = attribute.getRacevalue() + attribute.getGenerationvalue() + attribute.getLpincrease();
			attribute.setCurrentvalue(value);
			attribute.setCost(berechneAttriubteCost(attribute.getGenerationvalue()));
			STEPDICEType stepdice=attribute2StepAndDice(value);
			attribute.setDice(stepdice.getDice());
			attribute.setStep(stepdice.getStep());
			karmaMaxBonus-=attribute.getCost();
			totalCalculatedLPSpend+=ApplicationProperties.create().getCharacteristics().getAttributeTotalLP(attribute.getLpincrease());
		}
		if( karmaMaxBonus <0 ) {
			System.err.println("The character was generated with to many spent attribute buy points: "+(-karmaMaxBonus));
		}

		// **DEFENSE**
		DEFENSEType defense = character.getDefence();
		defense.setPhysical(berechneWiederstandskraft(character.getAttributes().get("DEX").getCurrentvalue()));
		defense.setSpell(berechneWiederstandskraft(character.getAttributes().get("PER").getCurrentvalue()));
		defense.setSocial(berechneWiederstandskraft(character.getAttributes().get("CHA").getCurrentvalue()));
		
		for(DEFENSEABILITYType racedefense : namegiver.getDEFENSE() ) {
			switch (racedefense.getKind()) {
			case PHYSICAL: defense.setPhysical(defense.getPhysical()+1); break;
			case SPELL: defense.setSpell(defense.getSpell()+1); break;
			case SOCIAL: defense.setSocial(defense.getSocial()+1); break;
			}
		}

		// **INITIATIVE**
		STEPDICEType initiativeStepdice=attribute2StepAndDice(character.getAttributes().get("DEX").getCurrentvalue());
		INITIATIVEType initiative = character.getInitiative();
		// Setze alle Initiative Modifikatoren zurück, da diese im folgenden neu bestimmt werden.
		initiative.setModification(0);
		initiative.setBase(initiativeStepdice.getStep());
		initiative.setStep(initiativeStepdice.getStep());
		initiative.setDice(initiativeStepdice.getDice());

		// **HEALTH**
		CHARACTERISTICSHEALTHRATING newhealth = bestimmeHealth(character.getAttributes().get("TOU").getCurrentvalue());
		DEATHType death=character.getDeath();
		DEATHType unconsciousness=character.getUnconsciousness();
		death.setBase(newhealth.getDeath());
		death.setAdjustment(0);
		unconsciousness.setBase(newhealth.getUnconsciousness());
		unconsciousness.setAdjustment(0);
		character.getWound().setThreshold(newhealth.getWound()+namegiver.getWOUND().getThreshold());
		RECOVERYType recovery = character.getRecovery();
		recovery.setTestsperday(newhealth.getRecovery());
		recovery.setStep(character.getAttributes().get("TOU").getStep());
		recovery.setDice(character.getAttributes().get("TOU").getDice());

		// **KARMA**
		KARMAType karma=character.getKarma();
		karma.setMaxmodificator(karmaMaxBonus);
		String KARMARUTUAL = JAXBHelper.getNameLang(ApplicationProperties.create().getNames(), "KARMARUTUAL", LanguageType.EN);
		if( KARMARUTUAL == null ) {
			System.err.println("Karmaritual in names.xml not defined for selected language. Skipping MaxKarma calculation");
		} else {
			TALENTType karmaritual=character.getTalentByName(KARMARUTUAL);
			int maxkarma = namegiver.getKarmamodifier() * karmaritual.getRANK().getRank();
			// Die Übriggebliebenen Kaufpunkte erhöhen das maximale Karma
			maxkarma += karmaMaxBonus;
			karma.setMax(maxkarma);
		}
		
		// **MOVEMENT**
		MOVEMENTType movement = character.getMovement();
		movement.setFlight(namegiver.getMovementFlight());
		movement.setGround(namegiver.getMovementGround());

		// **CARRYING**
		CARRYINGType carrying = character.getCarrying();
		int tmp=berechneTraglast(character.getAttributes().get("STR").getCurrentvalue());
		carrying.setCarrying(tmp);
		carrying.setLifting(tmp *2);

		ARMORType naturalArmor = namegiver.getARMOR();
		naturalArmor.setMysticarmor(berechneMysticArmor(character.getAttributes().get("WIL").getCurrentvalue()));
		PROTECTIONType protection = character.getProtection();
		int mysticalarmor=naturalArmor.getMysticarmor();
		int pysicalarmor=naturalArmor.getPhysicalarmor();
		int protectionpenalty=naturalArmor.getPenalty();
		List <ARMORType> newarmor = new ArrayList<ARMORType>();
		newarmor.add(naturalArmor);
		for (ARMORType armor : protection.getARMOROrSHIELD() ) {
			if( ! armor.getName().equals(naturalArmor.getName())) {
				newarmor.add(armor);
				if( armor.getUsed().equals(YesnoType.YES) ) {
					mysticalarmor+=armor.getMysticarmor();
					pysicalarmor+=armor.getPhysicalarmor();
					protectionpenalty+=armor.getPenalty();
					initiative.setModification(initiative.getModification()-armor.getPenalty());
					initiative.setStep(initiative.getBase()+initiative.getModification());
					initiative.setDice(step2Dice(initiative.getStep()));
				}
			}
		}
		protection.setMysticarmor(mysticalarmor);
		protection.setPenalty(protectionpenalty);
		protection.setPhysicalarmor(pysicalarmor);
		protection.getARMOROrSHIELD().clear();
		protection.getARMOROrSHIELD().addAll(newarmor);

		String abilities = "";
		for ( String s : namegiver.getABILITY() ) {
			if( ! abilities.isEmpty() ) {
				abilities +=", ";
			}
			abilities +=s;
		}
		character.setAbilities(abilities);

		ECECapabilities capabilities = new ECECapabilities(ApplicationProperties.create().getCapabilities().getSKILLOrTALENT());
		HashMap<Integer,TALENTSType> allTalents = character.getAllTalentsByDisziplinOrder();
		HashMap<String, ATTRIBUTEType> attribute = character.getAttributes();
		HashMap<String,TALENTABILITYType> namegivertalents = new HashMap<String,TALENTABILITYType>();
		HashMap<Integer, DISCIPLINEType> allDisciplines = character.getAllDiciplinesByOrder();
		String durabilityTalentName = JAXBHelper.getNameLang(ApplicationProperties.create().getNames(), "DURABILITY", LanguageType.EN);
		if( durabilityTalentName == null ) {
			System.err.println("Durability in names.xml not defined for selected language. Skipping Health enhancment");
			durabilityTalentName="";
		}
		for( TALENTABILITYType t : namegiver.getTALENT() ) {
			namegivertalents.put(t.getName(), t);
		}
		for( Integer disciplinenumber : allTalents.keySet() ) {
			TALENTType durabilityTalent = null;
			for( JAXBElement<TALENTType> element : allTalents.get(disciplinenumber).getDISZIPLINETALENTOrOPTIONALTALENT() ) {
				TALENTType talent = element.getValue();
				enforceCapabilityParams(talent,capabilities);
				int lpcostfull= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(talent.getCircle(),talent.getRANK().getRank());
				int lpcoststart= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(talent.getCircle(),talent.getRANK().getStartrank());
				talent.getRANK().setLpcost(lpcostfull-lpcoststart);
				totalCalculatedLPSpend += talent.getRANK().getLpcost();
				calculateCapabilityRank(talent.getRANK(),attribute.get(talent.getAttribute().value()));
				if( namegivertalents.containsKey(talent.getName()) ) {
					namegivertalents.remove(talent.getName());
				}
				if( talent.getName().equals(durabilityTalentName)) {
					durabilityTalent=talent;
				}
			}
			for( String t : namegivertalents.keySet() ) {
				TALENTType talent = new TALENTType();
				talent.setName(namegivertalents.get(t).getName());
				talent.setLimitation(namegivertalents.get(t).getLimitation());
				talent.setCircle(namegivertalents.get(t).getCircle());
				enforceCapabilityParams(talent,capabilities);
				RANKType rank = new RANKType();
				rank.setRank(0);
				calculateCapabilityRank(rank,attribute.get(talent.getAttribute().value()));
				talent.setRANK(rank);
				allTalents.get(disciplinenumber).getDISZIPLINETALENTOrOPTIONALTALENT().add(new ObjectFactory().createTALENTSTypeOPTIONALTALENT(talent));
			}
			if( durabilityTalent != null ) {
				DISCIPLINEDURABILITYType durability = JAXBHelper.getDisciplineDurability(allDisciplines.get(disciplinenumber));
				death.setAdjustment(death.getAdjustment()+(durability.getDeath()*durabilityTalent.getRANK().getRank()));
				unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*durabilityTalent.getRANK().getRank()));
				durabilityTalent.setLimitation(durability.getDeath()+"/"+durability.getUnconsciousness());
			}
		}

		for( SKILLType skill : character.getSkills() ) {
			int lpcostfull= ApplicationProperties.create().getCharacteristics().getSkillRankTotalLP(skill.getRANK().getRank());
			int lpcoststart= ApplicationProperties.create().getCharacteristics().getSkillRankTotalLP(skill.getRANK().getStartrank());
			skill.getRANK().setLpcost(lpcostfull-lpcoststart);
			totalCalculatedLPSpend += skill.getRANK().getLpcost();
			enforceCapabilityParams(skill,capabilities);
			calculateCapabilityRank(skill.getRANK(),attribute.get(skill.getAttribute().value()));
		}

		EXPERIENCEType legendpoints = character.getLegendPoints();
		int legendpointsPLUS = 0;
		int legendpointsMINUS = 0;
		int legendpointsZERO = 0;
		for( LEGENDPOINTSType lp : legendpoints.getLEGENDPOINTS() ) {
			switch( lp.getType() ) {
			case PLUS:  legendpointsPLUS  += lp.getLegendpoints(); break;
			case MINUS: legendpointsMINUS += lp.getLegendpoints(); break;
			case ZERO:  legendpointsZERO  += lp.getLegendpoints(); break;
			}
		}
		legendpoints.setCurrentlegendpoints(legendpointsPLUS-legendpointsMINUS);
		legendpoints.setTotallegendpoints(legendpointsPLUS);
		CHARACTERISTICSLEGENDARYSTATUS legendstatus = ApplicationProperties.create().getCharacteristics().getLegendaystatus(character.getDiciplineMaxCircle().getCircle());
		legendpoints.setRenown(legendstatus.getReown());
		legendpoints.setReputation(legendstatus.getReputation());

		// TODO: Spells
		// TODO: MagicItems

		// Veränderungen am death/unconsciousness adjustment sollen beachtet werden
		death.setValue(death.getBase()+death.getAdjustment());
		unconsciousness.setValue(unconsciousness.getBase()+unconsciousness.getAdjustment());

		System.out.println("Berechnete verbrauchte LPs: "+totalCalculatedLPSpend);
		return charakter;
	}

	public int berechneWiederstandskraft(int value) {
		int defense=0;
		for (CHARACTERISTICSDEFENSERAITING defenserating : ApplicationProperties.create().getCharacteristics().getDEFENSERAITING() ) {
			// System.err.println("berechneWiederstandskraft value "+value+" defense "+defense+" defenserating "+defenserating.getAttribute());
			if( (value >= defenserating.getAttribute()) && (defense<defenserating.getDefense()) ) {
				defense=defenserating.getDefense();
			}
		}
		return defense;
	}

	public int berechneTraglast (int value) {
		List<Integer> encumbrance = ApplicationProperties.create().getCharacteristics().getENCUMBRANCE();
		if( value< 1) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			value = 1;
		}
		if( value > encumbrance.size()) {
			value = encumbrance.size();
			System.err.println("The strength attribute was out of range. The carrying value will now base on: "+value);
		}
		return encumbrance.get(value);
	}

	public int berechneAttriubteCost(int modifier) {
		if ( modifier < -2 ) {
			System.err.println("The generation attribute value was to low. Value will increased to -2.");
			modifier = -2;
		}
		if ( modifier > 8 ) {
			System.err.println("The generation attribute value was to high. Value will be lower down to 8.");
			modifier = 8;
		}
		HashMap<Integer,Integer> attributecost = ApplicationProperties.create().getCharacteristics().getATTRIBUTECOST();
		return attributecost.get(modifier);
	}

	public CHARACTERISTICSHEALTHRATING bestimmeHealth (int value) {
		HashMap<Integer,CHARACTERISTICSHEALTHRATING> healthrating = ApplicationProperties.create().getCharacteristics().getHEALTHRATING();
		return healthrating.get(value);
	}

	public int berechneMysticArmor(int value) {
		return ApplicationProperties.create().getCharacteristics().getMYSTICARMOR().indexOf(value);
	}

	public STEPDICEType attribute2StepAndDice(int value) {
		while( value >= 0 ) {
			CHARACTERISTICSSTEPDICETABLE result = ApplicationProperties.create().getCharacteristics().getSTEPDICEbyAttribute(value);
			if( result != null ) {
				return result;
			}
			value--;
		}
		// Not found
		return null;
	}

	public DiceType step2Dice(int value) {
		return ApplicationProperties.create().getCharacteristics().getSTEPDICEbyStep(value).getDice();
	}

	public void calculateCapabilityRank(RANKType talentRank, ATTRIBUTEType attr) {
		if( attr == null ) {
			talentRank.setStep(talentRank.getRank());
		} else {
			talentRank.setStep(talentRank.getRank()+attr.getStep());
		}
		talentRank.setDice(step2Dice(talentRank.getStep()));
		
	}

	private void enforceCapabilityParams(CAPABILITYType capability, ECECapabilities capabilities) {
		CAPABILITYType replacment = null;
		if( capability instanceof TALENTType ) {
			replacment=capabilities.getTalent(capability.getName());
		} else {
			replacment=capabilities.getSkill(capability.getName());
		}
		if( replacment == null ) {
			System.err.println("Capability '"+capability.getName()+"' not found : "+capability.getClass().getSimpleName());
		} else {
			capability.setAction(replacment.getAction());
			capability.setAttribute(replacment.getAttribute());
			capability.setBonus(replacment.getBonus());
			capability.setKarma(replacment.getKarma());
			capability.setStrain(replacment.getStrain());
		}
	}
}
