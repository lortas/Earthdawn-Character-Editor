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

		// Berechnete LP erstmal zurück setzen
		CALCULATEDLEGENDPOINTSType calculatedLP = character.getCalculatedLegendpoints();
		calculatedLP.setAttributes(0);
		calculatedLP.setDisciplinetalents(0);
		calculatedLP.setKarma(0);
		calculatedLP.setMagicitems(0);
		calculatedLP.setOptionaltalents(0);
		calculatedLP.setSkills(0);
		calculatedLP.setSpells(0);
		calculatedLP.setKnacks(0);

		// Benötige Rasseneigenschaften der gewählten Rasse im Objekt "charakter":
		NAMEGIVERABILITYType namegiver = null;
		String race = character.getAppearance().getRace();
		for (NAMEGIVERABILITYType n : ApplicationProperties.create().getNamegivers().getNAMEGIVER()) {
			if( n.getName().equals(race)) {
				namegiver = n;
			}
		}

		// **ATTRIBUTE**
		int karmaMaxBonus = ApplicationProperties.create().getOptionalRules().getATTRIBUTE().getPoints();
		// Der Bonus auf das Maximale Karma ergibt sich aus den übriggebliebenen Kaufpunkten bei der Charaktererschaffung
		for (NAMEVALUEType raceattribute : namegiver.getATTRIBUTE()) {
			// Pro Atributt wird nun dessen Werte, Stufe und Würfel bestimmt

			ATTRIBUTEType attribute = character.getAttributes().get(raceattribute.getName());
			attribute.setRacevalue(raceattribute.getValue());
			attribute.setCost(berechneAttriubteCost(attribute.getGenerationvalue()));
			int value = attribute.getRacevalue() + attribute.getGenerationvalue();
			attribute.setBasevalue(value);
			value += attribute.getLpincrease();
			attribute.setCurrentvalue(value);
			STEPDICEType stepdice=attribute2StepAndDice(value);
			attribute.setDice(stepdice.getDice());
			attribute.setStep(stepdice.getStep());
			karmaMaxBonus-=attribute.getCost();
			calculatedLP.setAttributes(calculatedLP.getAttributes()+ApplicationProperties.create().getCharacteristics().getAttributeTotalLP(attribute.getLpincrease()));
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
		int karmapointsPLUS = 0;
		int karmapointsMINUS = 0;
		int karmapointsZERO = 0;
		for( ACCOUNTINGType lp : karma.getKARMAPOINTS() ) {
			switch( lp.getType() ) {
			case PLUS:  karmapointsPLUS  += lp.getValue(); break;
			case MINUS: karmapointsMINUS += lp.getValue(); break;
			case ZERO:  karmapointsZERO  += lp.getValue(); break;
			}
		}
		karma.setCurrent(karmapointsPLUS-karmapointsMINUS);
		calculatedLP.setKarma(calculatedLP.getKarma()+(karmapointsPLUS*10));
		String KARMARUTUAL = JAXBHelper.getNameLang(ApplicationProperties.create().getNames(), "KARMARUTUAL", LanguageType.EN);
		if( KARMARUTUAL == null ) {
			System.err.println("Karmaritual in names.xml not defined for selected language. Skipping MaxKarma calculation");
		} else {
			int maxkarma = 0;
			TALENTType karmaritual=character.getTalentByName(KARMARUTUAL);
			if( karmaritual != null ) {
				maxkarma += namegiver.getKarmamodifier() * karmaritual.getRANK().getRank();
			}
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

		// Lösche alle Diziplin Boni, damit diese unten wieder ergänzt werden können ohne auf duplikate Achten zu müssen
		character.clearDisciplineBonuses();
		ECECapabilities capabilities = new ECECapabilities(ApplicationProperties.create().getCapabilities().getSKILLOrTALENT());
		HashMap<Integer,TALENTSType> allTalents = character.getAllTalentsByDisziplinOrder();
		HashMap<String, ATTRIBUTEType> attribute = character.getAttributes();
		HashMap<Integer, DISCIPLINEType> allDisciplines = character.getAllDiciplinesByOrder();
		HashMap<String,Integer> diciplineCircle = new HashMap<String,Integer>();
		// Finde das DURABILITY Talent aus der Talentliste
		String durabilityTalentName = JAXBHelper.getNameLang(ApplicationProperties.create().getNames(), "DURABILITY", LanguageType.EN);
		if( durabilityTalentName == null ) {
			System.err.println("Durability in names.xml not defined for selected language. Skipping Health enhancment");
			durabilityTalentName="";
		}
		// Sammle alle Namensgeber spezial Talente in einer Liste zusammen
		HashMap<String,TALENTABILITYType> namegivertalents = new HashMap<String,TALENTABILITYType>();
		for( TALENTABILITYType t : namegiver.getTALENT() ) {
			namegivertalents.put(t.getName(), t);
		}
		// Zwei Schleifen:
		// - äußere Schleife über alle Disziplinen und
		// - innere Schleife über alle Talente der Disziplin
		for( Integer disciplinenumber : allTalents.keySet() ) {
			TALENTType durabilityTalent = null;
			for( JAXBElement<TALENTType> element : allTalents.get(disciplinenumber).getDISZIPLINETALENTOrOPTIONALTALENT() ) {
				TALENTType talent = element.getValue();
				talent.getRANK().setBonus(0);
				enforceCapabilityParams(talent,capabilities);
				int lpcostfull= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(talent.getCircle(),talent.getRANK().getRank());
				int lpcoststart= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(talent.getCircle(),talent.getRANK().getStartrank());
				talent.getRANK().setLpcost(lpcostfull-lpcoststart);
				if( element.getName().getLocalPart().equals("DISZIPLINETALENT")) {
					calculatedLP.setDisciplinetalents(calculatedLP.getDisciplinetalents()+talent.getRANK().getLpcost());
				} else {
					calculatedLP.setOptionaltalents(calculatedLP.getOptionaltalents()+talent.getRANK().getLpcost());
				}
				calculateCapabilityRank(talent.getRANK(),attribute.get(talent.getAttribute().value()));
				if( namegivertalents.containsKey(talent.getName()) ) {
					namegivertalents.remove(talent.getName());
				}
				if( talent.getName().equals(durabilityTalentName)) {
					durabilityTalent=talent;
				}
				for( KNACKType knack : talent.getKNACK() ) {
					if( knack.getMinrank() > talent.getRANK().getRank() ) {
						System.err.println("The rank of the talent '"+talent.getName()+"' is lower than the minimal rank for the kack '"+knack.getName()+"': "
								+talent.getRANK().getRank()+" vs. "+knack.getMinrank());
					}
					int lp = ApplicationProperties.create().getCharacteristics().getTalentRankIncreaseLP(talent.getCircle(),knack.getMinrank());
					calculatedLP.setKnacks(calculatedLP.getKnacks()+lp);
				}
			}
			// Alle Dizipline Talente die bis jetzt noch nicht enthalten waren,
			// werden nun den optionalen Talenten beigefügt.
			for( String t : namegivertalents.keySet() ) {
				TALENTType talent = new TALENTType();
				talent.setName(namegivertalents.get(t).getName());
				talent.setLimitation(namegivertalents.get(t).getLimitation());
				talent.setCircle(namegivertalents.get(t).getCircle());
				enforceCapabilityParams(talent,capabilities);
				RANKType rank = new RANKType();
				rank.setRank(0);
				rank.setBonus(0);
				calculateCapabilityRank(rank,attribute.get(talent.getAttribute().value()));
				talent.setRANK(rank);
				allTalents.get(disciplinenumber).getDISZIPLINETALENTOrOPTIONALTALENT().add(new ObjectFactory().createTALENTSTypeOPTIONALTALENT(talent));
			}
			// Wenn ein Durability-Talent gefunden wurde berechnen aus dessen Rank
			// dier Erhöhung von Todes- und Bewustlosigkeitsschwelle
			DISCIPLINEType discipline = allDisciplines.get(disciplinenumber);
			if( durabilityTalent != null ) {
				DISCIPLINEDURABILITYType durability = JAXBHelper.getDisciplineDurability(discipline);
				death.setAdjustment(death.getAdjustment()+(durability.getDeath()*durabilityTalent.getRANK().getRank()));
				unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*durabilityTalent.getRANK().getRank()));
				durabilityTalent.setLimitation(durability.getDeath()+"/"+durability.getUnconsciousness());
			}
			diciplineCircle.put(discipline.getName(), discipline.getCircle());
			List<DISCIPLINEBONUSType> bonuses = JAXBHelper.getDisciplineBonuses(discipline);
			character.addDisciplineBonuses(bonuses,discipline.getCircle());
		}

		for( SKILLType skill : character.getSkills() ) {
			int lpcostfull= ApplicationProperties.create().getCharacteristics().getSkillRankTotalLP(skill.getRANK().getRank());
			int lpcoststart= ApplicationProperties.create().getCharacteristics().getSkillRankTotalLP(skill.getRANK().getStartrank());
			skill.getRANK().setLpcost(lpcostfull-lpcoststart);
			skill.getRANK().setBonus(0);
			calculatedLP.setSkills(calculatedLP.getSkills()+skill.getRANK().getLpcost());
			enforceCapabilityParams(skill,capabilities);
			calculateCapabilityRank(skill.getRANK(),attribute.get(skill.getAttribute().value()));
		}

		DEFENSEType disciplineDefense = getDisciplineDefense(diciplineCircle);
		defense.setPhysical(defense.getPhysical()+disciplineDefense.getPhysical());
		defense.setSocial(defense.getSocial()+disciplineDefense.getSocial());
		defense.setSpell(defense.getSpell()+disciplineDefense.getSpell());

		initiative.setModification(initiative.getModification()+getDisciplineInitiative(diciplineCircle));
		initiative.setStep(initiative.getBase()+initiative.getModification());
		initiative.setDice(step2Dice(initiative.getStep()));

		recovery.setStep(recovery.getStep()+getDisciplineRecoveryTestBonus(diciplineCircle));
		recovery.setDice(step2Dice(recovery.getStep()));
		
		character.addDisciplineKarmaStepBonus(getDisciplineKarmaStepBonus(diciplineCircle));

		EXPERIENCEType legendpoints = character.getLegendPoints();
		int legendpointsPLUS = 0;
		int legendpointsMINUS = 0;
		int legendpointsZERO = 0;
		for( ACCOUNTINGType lp : legendpoints.getLEGENDPOINTS() ) {
			switch( lp.getType() ) {
			case PLUS:  legendpointsPLUS  += lp.getValue(); break;
			case MINUS: legendpointsMINUS += lp.getValue(); break;
			case ZERO:  legendpointsZERO  += lp.getValue(); break;
			}
		}
		legendpoints.setCurrentlegendpoints(legendpointsPLUS-legendpointsMINUS);
		legendpoints.setTotallegendpoints(legendpointsPLUS);
		CHARACTERISTICSLEGENDARYSTATUS legendstatus = ApplicationProperties.create().getCharacteristics().getLegendaystatus(character.getDiciplineMaxCircle().getCircle());
		legendpoints.setRenown(legendstatus.getReown());
		legendpoints.setReputation(legendstatus.getReputation());

		// ** SPELLS **
		OPTIONALRULEType OptinalRule_SpellLegendPointCost = ApplicationProperties.create().getOptionalRules().getSPELLLEGENDPOINTCOST();
		if( OptinalRule_SpellLegendPointCost.getUsed().equals(YesnoType.YES)) {
			// Starting Spell can be from 1st and 2nd circle. Substact these Legendpoints from the legendpoints spend for spells
			ATTRIBUTEType per = character.getAttributes().get("PER");
			int lpbonus = 100 * attribute2StepAndDice(per.getBasevalue()).getStep();
			for( int spellability : getDisciplineSpellAbility(diciplineCircle) ) {
				lpbonus += ApplicationProperties.create().getCharacteristics().getSpellLP(spellability);
			}
			calculatedLP.setSpells(-lpbonus);
		} else {
			calculatedLP.setSpells(0);
		}
		HashMap<String, SPELLDEFType> spelllist = ApplicationProperties.create().getSpells();
		for( SPELLSType spells : character.getAllSpells() ) {
			for( SPELLType spell : spells.getSPELL() ) {
				SPELLDEFType spelldef = spelllist.get(spell.getName());
				if( spelldef == null ) {
					System.err.println("Unknown Spell '"+spell.getName()+"' in grimour found. Spell is left unmodified in grimour.");
				} else {
					spell.setCastingdifficulty(spelldef.getCastingdifficulty());
					spell.setDuration(spelldef.getDuration());
					spell.setEffect(spelldef.getEffect());
					spell.setEffectarea(spelldef.getEffectarea());
					spell.setRange(spelldef.getRange());
					spell.setReattuningdifficulty(spelldef.getReattuningdifficulty());
					spell.setThreads(spelldef.getThreads());
					spell.setWeavingdifficulty(spelldef.getWeavingdifficulty());
				}
				if( OptinalRule_SpellLegendPointCost.getUsed().equals(YesnoType.YES)) {
					// The cost of spells are equivalent to the cost of increasing a Novice Talent to a Rank equal to the Spell Circle
					int lpcost=ApplicationProperties.create().getCharacteristics().getSpellLP(spell.getCircle());
					calculatedLP.setSpells(calculatedLP.getSpells()+lpcost);
				}
			}
		}

		// TODO: MagicItems

		// Veränderungen am death/unconsciousness adjustment sollen beachtet werden
		death.setValue(death.getBase()+death.getAdjustment());
		unconsciousness.setValue(unconsciousness.getBase()+unconsciousness.getAdjustment());

		calculatedLP.setTotal(calculatedLP.getAttributes()+calculatedLP.getDisciplinetalents()+
				calculatedLP.getKarma()+calculatedLP.getMagicitems()+calculatedLP.getOptionaltalents()+
				calculatedLP.getSkills()+calculatedLP.getSpells()+calculatedLP.getKnacks());
		System.out.println("Berechnete verbrauchte LPs gesamt: "+calculatedLP.getTotal());
		return charakter;
	}

	private int getDisciplineKarmaStepBonus(HashMap<String,Integer> diciplineCircle) {
		int result = 0;
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			int tmp = 0;
			for( JAXBElement<?> element : d.getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT() ) {
				if( element.getName().getLocalPart().equals("KARMASTEP")) {
					DISZIPINABILITYType karmastep = (DISZIPINABILITYType)element.getValue();
					if( karmastep.getCircle() > diciplineCircle.get(discipline) ) continue;
					tmp++;
				}
			}
			if( tmp > result ) result=tmp;
		}
		return result;
	}

	private int getDisciplineRecoveryTestBonus(HashMap<String,Integer> diciplineCircle) {
		int result = 0;
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			int tmp = 0;
			for( JAXBElement<?> element : d.getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT() ) {
				if( element.getName().getLocalPart().equals("RECOVERYTEST")) {
					DISZIPINABILITYType recoverytest = (DISZIPINABILITYType)element.getValue();
					if( recoverytest.getCircle() > diciplineCircle.get(discipline) ) continue;
					tmp++;
				}
			}
			if( tmp > result ) result=tmp;
		}
		return result;
	}

	private int getDisciplineInitiative(HashMap<String,Integer> diciplineCircle) {
		int result = 0;
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			int tmp = 0;
			for( JAXBElement<?> element : d.getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT() ) {
				if( element.getName().getLocalPart().equals("INITIATIVE")) {
					DISZIPINABILITYType initiative = (DISZIPINABILITYType)element.getValue();
					if( initiative.getCircle() > diciplineCircle.get(discipline) ) continue;
					tmp++;
				}
			}
			if( tmp > result ) result=tmp;
		}
		return result;
	}

	// Der Defense Bonus wird nicht über Alle Disziplinen addiert, sondern
	// der Character erhält von des Disziplinen nur den jeweils höchsten DefenseBonus
	private DEFENSEType getDisciplineDefense(HashMap<String,Integer> diciplineCircle) {
		DEFENSEType result = new DEFENSEType();
		result.setPhysical(0);
		result.setSocial(0);
		result.setSpell(0);
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			DEFENSEType tmp = new DEFENSEType();
			tmp.setPhysical(0);
			tmp.setSocial(0);
			tmp.setSpell(0);
			for( JAXBElement<?> element : d.getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT() ) {
				if( element.getName().getLocalPart().equals("DEFENSE")) {
					DEFENSEABILITYType defense = (DEFENSEABILITYType)element.getValue();
					if( defense.getCircle() > diciplineCircle.get(discipline) ) continue;
					switch( defense.getKind() ) {
					case PHYSICAL: tmp.setPhysical(tmp.getPhysical()+1); break;
					case SOCIAL:   tmp.setSocial(tmp.getSocial()+1); break;
					case SPELL:    tmp.setSpell(tmp.getSpell()+1); break;
					}
				}
			}
			if( tmp.getPhysical() > result.getPhysical() ) result.setPhysical(tmp.getPhysical());
			if( tmp.getSocial()   > result.getSocial()   ) result.setSocial(tmp.getSocial());
			if( tmp.getSpell()    > result.getSpell()    ) result.setSpell(tmp.getSpell());
		}
		return result;
	}

	private List<Integer> getDisciplineSpellAbility(HashMap<String,Integer> diciplineCircle) {
		List<Integer> result = new ArrayList<Integer>();
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			for( JAXBElement<?> element : d.getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT() ) {
				if( element.getName().getLocalPart().equals("SPELLABILITY")) {
					DISZIPINABILITYType spell = (DISZIPINABILITYType)element.getValue();
					if( spell.getCircle() > diciplineCircle.get(discipline) ) continue;
					result.add(spell.getCircle());
				}
			}
		}
		return result;
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
			talentRank.setStep(talentRank.getRank()+talentRank.getBonus());
		} else {
			talentRank.setStep(talentRank.getRank()+talentRank.getBonus()+attr.getStep());
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
