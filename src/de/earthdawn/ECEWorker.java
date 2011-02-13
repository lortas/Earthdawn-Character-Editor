package de.earthdawn;
/******************************************************************************\
Copyright (C) 2010-2011  Holger von Rhein <lortas@freenet.de>

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
\******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		TALENTType karmaritualTalent = null;
		String KARMARUTUAL = ApplicationProperties.create().getKarmaritualName(); 
		if( KARMARUTUAL == null ) {
			System.err.println("Karmaritual talent name is not defined for selected language.");
		} else {
			karmaritualTalent=character.getTalentByName(KARMARUTUAL);
		}
		int calculatedKarmaLP=calculateKarma(character.getKarma(), karmaritualTalent, namegiver.getKarmamodifier(), karmaMaxBonus);
		calculatedLP.setKarma(calculatedLP.getKarma()+calculatedKarmaLP);

		// **MOVEMENT**
		MOVEMENTType movement = character.getMovement();
		movement.setFlight(namegiver.getMovementFlight());
		movement.setGround(namegiver.getMovementGround());

		// **CARRYING**
		CARRYINGType carrying = character.getCarrying();
		int tmp=berechneTraglast(character.getAttributes().get("STR").getCurrentvalue());
		carrying.setCarrying(tmp);
		carrying.setLifting(tmp *2);

		// ** ARMOR **
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

		character.setAbilities(concatStrings(namegiver.getABILITY()));

		// Lösche alle Diziplin Boni, damit diese unten wieder ergänzt werden können ohne auf duplikate Achten zu müssen
		character.clearDisciplineBonuses();
		ECECapabilities capabilities = new ECECapabilities(ApplicationProperties.create().getCapabilities().getSKILLOrTALENT());
		HashMap<Integer,TALENTSType> allTalents = character.getAllTalentsByDisziplinOrder();
		HashMap<String, ATTRIBUTEType> attribute = character.getAttributes();
		HashMap<Integer, DISCIPLINEType> allDisciplines = character.getAllDiciplinesByOrder();
		HashMap<String,Integer> diciplineCircle = new HashMap<String,Integer>();
		// Finde das DURABILITY Talent aus der Talentliste
		String durabilityTalentName = ApplicationProperties.create().getDurabilityName();
		if( durabilityTalentName == null ) {
			System.err.println("Durability in names.xml not defined for selected language. Skipping Health enhancment");
			durabilityTalentName="";
		}
		// Sammle alle Namensgeber spezial Talente in einer Liste zusammen
		HashMap<String,TALENTABILITYType> namegivertalents = new HashMap<String,TALENTABILITYType>();
		for( TALENTABILITYType t : namegiver.getTALENT() ) {
			namegivertalents.put(t.getName(), t);
		}
		int maxKarmaStepBonus=0;
		// Zwei Schleifen:
		// - äußere Schleife über alle Disziplinen und
		// - innere Schleife über alle Talente der Disziplin
		for( Integer disciplinenumber : allTalents.keySet() ) {
			TALENTType durabilityTalent = null;
			List<TALENTType> disTalents = allTalents.get(disciplinenumber).getDISZIPLINETALENT();
			List<TALENTType> rankZeroTalents = new ArrayList<TALENTType>();
			for( TALENTType talent : disTalents ) {
				RANKType rank = talent.getRANK();
				if( rank.getRank() < 1 ) {
					rankZeroTalents.add(talent);
					continue;
				}
				rank.setBonus(0);
				enforceCapabilityParams(talent,capabilities);
				int lpcostfull= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getRank());
				int lpcoststart= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getStartrank());
				rank.setLpcost(lpcostfull-lpcoststart);
				calculatedLP.setDisciplinetalents(calculatedLP.getDisciplinetalents()+rank.getLpcost());
				calculateCapabilityRank(rank,attribute.get(talent.getAttribute().value()));
				if( talent.getName().equals(durabilityTalentName)) {
					durabilityTalent=talent;
				}
				calculateKnacks(calculatedLP, disciplinenumber, talent, rank.getRank());
				if( namegivertalents.containsKey(talent.getName()) ) {
					namegivertalents.remove(talent.getName());
				}
			}
			// Talenete mit Rang == 0 werden entfernt.
			disTalents.removeAll(rankZeroTalents);
			rankZeroTalents.clear();
			List<TALENTType> optTalents = allTalents.get(disciplinenumber).getOPTIONALTALENT();
			for( TALENTType talent : optTalents ) {
				RANKType rank = talent.getRANK();
				if( rank.getRank() < 1 ) {
					rankZeroTalents.add(talent);
					continue;
				}
				rank.setBonus(0);
				enforceCapabilityParams(talent,capabilities);
				int lpcostfull= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getRank());
				int lpcoststart= ApplicationProperties.create().getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getStartrank());
				rank.setLpcost(lpcostfull-lpcoststart);
				calculatedLP.setOptionaltalents(calculatedLP.getOptionaltalents()+rank.getLpcost());
				calculateCapabilityRank(rank,attribute.get(talent.getAttribute().value()));
				if( talent.getName().equals(durabilityTalentName)) {
					durabilityTalent=talent;
				}
				calculateKnacks(calculatedLP, disciplinenumber, talent, rank.getRank());
				if( namegivertalents.containsKey(talent.getName()) ) {
					namegivertalents.remove(talent.getName());
				}
			}
			// Talenete mit Rang == 0 werden entfernt.
			optTalents.removeAll(rankZeroTalents);
			// Alle Dizipline Talente die bis jetzt noch nicht enthalten waren,
			// werden nun den optionalen Talenten beigefügt.
			for( String t : namegivertalents.keySet() ) {
				TALENTType talent = new TALENTType();
				talent.setName(namegivertalents.get(t).getName());
				talent.setLimitation(namegivertalents.get(t).getLimitation());
				talent.setCircle(0);
				enforceCapabilityParams(talent,capabilities);
				RANKType rank = new RANKType();
				rank.setRank(0);
				rank.setBonus(0);
				calculateCapabilityRank(rank,attribute.get(talent.getAttribute().value()));
				talent.setRANK(rank);
				allTalents.get(disciplinenumber).getOPTIONALTALENT().add(talent);
			}
			// Wenn ein Durability-Talent gefunden wurde berechnen aus dessen Rank
			// dier Erhöhung von Todes- und Bewustlosigkeitsschwelle
			DISCIPLINEType discipline = allDisciplines.get(disciplinenumber);
			if( durabilityTalent != null ) {
				DISCIPLINEDURABILITYType durability = ApplicationProperties.create().getDisziplin(discipline.getName()).getDURABILITY();
				death.setAdjustment(death.getAdjustment()+(durability.getDeath()*durabilityTalent.getRANK().getRank()));
				unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*durabilityTalent.getRANK().getRank()));
				durabilityTalent.setLimitation(durability.getDeath()+"/"+durability.getUnconsciousness());
			}
			diciplineCircle.put(discipline.getName(), discipline.getCircle());
			// Nur der höchtse Bonus wird gewertet.
			int currentKarmaStepBonus = getDisciplineKarmaStepBonus(discipline);
			if( currentKarmaStepBonus > maxKarmaStepBonus ) maxKarmaStepBonus = currentKarmaStepBonus;
			List<DISCIPLINEBONUSType> currentBonuses = discipline.getDISCIPLINEBONUS();
			currentBonuses.clear();
			currentBonuses.addAll(getDisciplineBonuses(discipline));
		}

		removeEmptySkills(character.getSkills());
		List<CAPABILITYType> defaultSkills = capabilities.getDefaultSkills(namegiver.getNOTDEFAULTSKILL());
		for( SKILLType skill : character.getSkills() ) {
			int lpcostfull= ApplicationProperties.create().getCharacteristics().getSkillRankTotalLP(skill.getRANK().getRank());
			int lpcoststart= ApplicationProperties.create().getCharacteristics().getSkillRankTotalLP(skill.getRANK().getStartrank());
			skill.getRANK().setLpcost(lpcostfull-lpcoststart);
			skill.getRANK().setBonus(0);
			calculatedLP.setSkills(calculatedLP.getSkills()+skill.getRANK().getLpcost());
			enforceCapabilityParams(skill,capabilities);
			if( skill.getAttribute() != null ) {
				calculateCapabilityRank(skill.getRANK(),attribute.get(skill.getAttribute().value()));
			}
			removeIfContains(defaultSkills,skill.getName());
		}
		// Füge Default Skill hinzu
		for( CAPABILITYType defaultSkill : defaultSkills ) {
			SKILLType skill = new SKILLType();
			RANKType rank = new RANKType();
			rank.setBonus(0);
			rank.setLpcost(0);
			rank.setRank(0);
			skill.setRANK(rank);
			skill.setName(defaultSkill.getName());
			skill.setLimitation(defaultSkill.getLimitation());
			skill.setAction(defaultSkill.getAction());
			skill.setAttribute(defaultSkill.getAttribute());
			skill.setBonus(defaultSkill.getBonus());
			skill.setKarma(defaultSkill.getKarma());
			skill.setStrain(defaultSkill.getStrain());
			skill.setDefault(defaultSkill.getDefault());
			if( skill.getAttribute() != null ) {
				calculateCapabilityRank(rank,attribute.get(skill.getAttribute().value()));
			}
			character.getSkills().add(skill);
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
		
		calculateLegendPointsAndStatus(character.getLegendPoints(),character.getDiciplineMaxCircle().getCircle());

		// ** SPELLS **
		OPTIONALRULEType OptinalRule_SpellLegendPointCost = ApplicationProperties.create().getOptionalRules().getSPELLLEGENDPOINTCOST();
		String firstDisciplineName = "";
		DISCIPLINEType firstDiscipline = allDisciplines.get(1);
		if( firstDiscipline != null ) firstDisciplineName=firstDiscipline.getName();
		int startingSpellLegendPointCost = 0;
		if( OptinalRule_SpellLegendPointCost.getUsed().equals(YesnoType.YES)) {
			// Starting Spell can be from 1st and 2nd circle. Substact these Legendpoints from the legendpoints spend for spells
			ATTRIBUTEType per = character.getAttributes().get("PER");
			startingSpellLegendPointCost = 100 * attribute2StepAndDice(per.getBasevalue()).getStep();
			int lpbonus = 0;
			for( int spellability : getDisciplineSpellAbility(diciplineCircle) ) {
				lpbonus += ApplicationProperties.create().getCharacteristics().getSpellLP(spellability);
			}
			calculatedLP.setSpells(-lpbonus);
		} else {
			calculatedLP.setSpells(0);
		}
		HashMap<String, SPELLDEFType> spelllist = ApplicationProperties.create().getSpells();
		for( SPELLSType spells : character.getAllSpells() ) {
			if( spells.getDiscipline().equals(firstDisciplineName) && OptinalRule_SpellLegendPointCost.getUsed().equals(YesnoType.YES) )  {
				// Wenn die erste Disziplin eine Zauberdisciplin ist und die Optionale Regel, dass Zaubersprüche LP Kosten
				// gewählt wurde, dann reduziere die ZauberLPKosten um die StartZauber
				calculatedLP.setSpells(calculatedLP.getSpells()-startingSpellLegendPointCost);
				// Es ist jetzt schon einmal abgezogen. Stelle nun sicher dass nicht noch ein zweites Mal abgezogen werden kann.
				startingSpellLegendPointCost=0;
			}
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

		if( calculatedLP.getSpells() < 0 ) calculatedLP.setSpells(0);
		calculatedLP.setTotal(calculatedLP.getAttributes()+calculatedLP.getDisciplinetalents()+
				calculatedLP.getKarma()+calculatedLP.getMagicitems()+calculatedLP.getOptionaltalents()+
				calculatedLP.getSkills()+calculatedLP.getSpells()+calculatedLP.getKnacks());
//		System.out.println("Berechnete verbrauchte LPs Attributes: "+calculatedLP.getAttributes());
//		System.out.println("Berechnete verbrauchte LPs Disciplinetalents: "+calculatedLP.getDisciplinetalents());
//		System.out.println("Berechnete verbrauchte LPs Karma: "+calculatedLP.getKarma());
//		System.out.println("Berechnete verbrauchte LPs Magicitems: "+calculatedLP.getMagicitems());
//		System.out.println("Berechnete verbrauchte LPs Optionaltalents: "+calculatedLP.getOptionaltalents());
//		System.out.println("Berechnete verbrauchte LPs Skills: "+calculatedLP.getSkills());
//		System.out.println("Berechnete verbrauchte LPs Spells: "+calculatedLP.getSpells());
//		System.out.println("Berechnete verbrauchte LPs Knacks: "+calculatedLP.getKnacks());
//		System.out.println("Berechnete verbrauchte LPs gesamt: "+calculatedLP.getTotal());
		return charakter;
	}

	public static void removeIfContains(List<CAPABILITYType> defaultSkills, String name) {
		List<CAPABILITYType> remove = new ArrayList<CAPABILITYType>();
		for( CAPABILITYType skill : defaultSkills) {
			if( skill.getName().equals(name)) {
				remove.add(skill);
			}
		}
		defaultSkills.removeAll(remove);
	}

	public static void removeEmptySkills(List<SKILLType> skills) {
		List<SKILLType> remove = new ArrayList<SKILLType>();
		for( SKILLType skill : skills ) {
			RANKType rank = skill.getRANK();
			if( (rank != null) && (rank.getRank() > 0) ) continue;
			remove.add(skill);
		}
		skills.removeAll(remove);
	}

	public static String concatStrings(List<String> strings) {
		String result = "";
		for ( String s : strings ) {
			if( ! result.isEmpty() ) result += ", ";
			result += s;
		}
		return result;
	}

	public static List<Integer> calculateAccounting(List<ACCOUNTINGType> accountings) {
		int plus = 0;
		int minus = 0;
		int zero = 0;
		for( ACCOUNTINGType lp : accountings ) {
			switch( lp.getType() ) {
			case PLUS:  plus  += lp.getValue(); break;
			case MINUS: minus += lp.getValue(); break;
			case ZERO:  zero  += lp.getValue(); break;
			}
		}
		List<Integer> account = new ArrayList<Integer>();
		account.add(plus);  // 0
		account.add(minus); // 1
		account.add(zero);  // 2
		return account;
	}

	private int calculateKarma(KARMAType karma, TALENTType karmaritualTalent, int karmaModifier, int karmaMaxBonus) {
		karma.setMaxmodificator(karmaMaxBonus);
		if( (karmaritualTalent != null) && (karmaritualTalent.getRANK() != null) ) {
			karma.setMax( karmaMaxBonus + (karmaModifier * karmaritualTalent.getRANK().getRank()) );
		} else {
			System.err.println("Skipping MaxKarma calculation.");
		}
		List<Integer> k = calculateAccounting(karma.getKARMAPOINTS());
		karma.setCurrent(k.get(0)-k.get(1));
		return 10*k.get(0); // KarmaLP
	}

	public static void calculateLegendPointsAndStatus(EXPERIENCEType legendpoints, int circle) {
		List<Integer> lp = calculateAccounting(legendpoints.getLEGENDPOINTS());
		legendpoints.setCurrentlegendpoints(lp.get(0)-lp.get(1));
		legendpoints.setTotallegendpoints(lp.get(0));
		CHARACTERISTICSLEGENDARYSTATUS legendstatus = ApplicationProperties.create().getCharacteristics().getLegendaystatus(circle);
		legendpoints.setRenown(legendstatus.getReown());
		legendpoints.setReputation(legendstatus.getReputation());
	}

	private void calculateKnacks(CALCULATEDLEGENDPOINTSType calculatedLP, Integer disciplinenumber, TALENTType talent, int rank) {
		for( KNACKType knack : talent.getKNACK() ) {
			if( knack.getMinrank() > rank ) {
				System.err.println("The rank of the talent '"+talent.getName()+"' is lower than the minimal rank for the kack '"+knack.getName()+"': "
						+rank+" vs. "+knack.getMinrank());
			}
			int lp = ApplicationProperties.create().getCharacteristics().getTalentRankLPIncreaseTable(disciplinenumber,talent.getCircle()).get(knack.getMinrank()).getCost();
			calculatedLP.setKnacks(calculatedLP.getKnacks()+lp);
		}
	}

	private int getDisciplineKarmaStepBonus(DISCIPLINEType discipline) {
		int result = 0;
		int circlenr=0;
		for( DISCIPLINECIRCLEType circle : ApplicationProperties.create().getDisziplin(discipline.getName()).getCIRCLE()) {
			circlenr++;
			if( circlenr > discipline.getCircle() ) break;
			for( DISZIPINABILITYType karmastep : circle.getKARMASTEP()) {
				result += karmastep.getCount();
			}
		}
		return result;
	}

	private int getDisciplineRecoveryTestBonus(HashMap<String,Integer> diciplineCircle) {
		int result = 0;
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			int tmp = 0;
			int circlenr=0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( DISZIPINABILITYType recoverytest : circle.getRECOVERYTEST() ) {
					tmp += recoverytest.getCount();
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
			int circlenr=0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( DISZIPINABILITYType initiative : circle.getINITIATIVE() ) {
					tmp += initiative.getCount();
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
			int circlenr = 0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( DEFENSEABILITYType defense : circle.getDEFENSE() ) {
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
			int circlenr = 0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				for( DISZIPINABILITYType spell : circle.getSPELLABILITY() ) {
					for(int i=0; i<spell.getCount(); i++) result.add(circlenr);
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
		List<Integer> mysticArmorTable = ApplicationProperties.create().getCharacteristics().getMYSTICARMOR();
		int mysticArmor=-1;
		for( int attribute : mysticArmorTable ) {
			if( attribute > value ) break;
			mysticArmor++;
		}
		return mysticArmor;
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

	public static List<DISCIPLINEBONUSType> getDisciplineBonuses(DISCIPLINEType discipline) {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		int circlenr=0;
		for(DISCIPLINECIRCLEType circle : ApplicationProperties.create().getDisziplin(discipline.getName()).getCIRCLE()) {
			circlenr++;
			if( circlenr > discipline.getCircle() ) break;
			for( KARMAABILITYType karma : circle.getKARMA() ) {
				DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
				bonus.setCircle(circlenr);
				bonus.setBonus("Can spend Karma for "+karma.getSpend());
				bonuses.add(bonus);
			}
			for( String ability : circle.getABILITY() ) {
				DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
				bonus.setCircle(circlenr);
				bonus.setBonus("Ability: "+ability);
				bonuses.add(bonus);
			}
		}
		return bonuses;
	}
}
