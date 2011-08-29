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

import java.io.PrintStream;
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
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final String durabilityTalentName = PROPERTIES.getDurabilityName();
	public static final String questorTalentName = PROPERTIES.getQuestorTalentName();
	public static final ECECapabilities capabilities = new ECECapabilities(PROPERTIES.getCapabilities().getSKILLOrTALENT());
	public static final List<KNACKBASEType> globalTalentKnackList = PROPERTIES.getTalentKnacks();
	public static final boolean OptionalRule_SpellLegendPointCost=PROPERTIES.getOptionalRules().getSPELLLEGENDPOINTCOST().getUsed().equals(YesnoType.YES);
	public static final boolean OptionalRule_KarmaLegendPointCost=PROPERTIES.getOptionalRules().getKARMALEGENDPOINTCOST().getUsed().equals(YesnoType.YES);
	public static final boolean OptionalRule_ShowDefaultSkills=PROPERTIES.getOptionalRules().getSHOWDEFAULTSKILLS().getUsed().equals(YesnoType.YES);
	public static final boolean OptionalRule_QuestorTalentNeedLegendpoints=PROPERTIES.getOptionalRules().getQUESTORTALENTNEEDLEGENDPOINTS().getUsed().equals(YesnoType.YES);
	public static final boolean OptionalRule_autoincrementDisciplinetalents=PROPERTIES.getOptionalRules().getAUTOINCREMENTDISCIPLINETALENTS().getUsed().equals(YesnoType.YES);
	public static final boolean OptionalRule_LegendpointsForAttributeIncrease=PROPERTIES.getOptionalRules().getLEGENDPOINTSFORATTRIBUTEINCREASE().getUsed().equals(YesnoType.YES);
	public static final boolean OptionalRule_AutoInsertLegendPointSpent=PROPERTIES.getOptionalRules().getAUTOINSERTLEGENDPOINTSPENT().getUsed().equals(YesnoType.YES);
	private HashMap<String, ATTRIBUTEType> characterAttributes=null;
	CALCULATEDLEGENDPOINTSType calculatedLP = null;
	private static PrintStream errorout = System.err;

	/**
	 * Verabeiten eines Charakters.
	 */
	public EDCHARACTER verarbeiteCharakter(EDCHARACTER charakter) {
		CharacterContainer character = new CharacterContainer(charakter);

		// Orignal berechnete LP sichern
		CALCULATEDLEGENDPOINTSType oldcalculatedLP = character.getCopyOfCalculatedLegendpoints();
		// Berechnete LP erstmal zurücksetzen
		calculatedLP = character.resetCalculatedLegendpoints();

		// Benötige Rasseneigenschaften der gewählten Rasse im Objekt "charakter":
		NAMEGIVERABILITYType namegiver = character.getRace();

		// Startgegenstände aus der Charaktererschaffung setzen, wenn gar kein Invetar vorhanden
		List<ITEMType> itemList = character.getItems();
		if( itemList.isEmpty() ) {
			itemList.addAll(PROPERTIES.getStartingItems());
		}

		List<WEAPONType> magicWeapons = character.cutMagicWeaponFromNormalWeaponList();
		List<WEAPONType> weaponList = character.getWeapons();
		if( weaponList.isEmpty() ) {
			// Startwaffen aus der Charaktererschaffung setzen, wenn gar keine Waffen vorhanden
			weaponList.addAll(PROPERTIES.getStartingWeapons());
		}
		weaponList.addAll(magicWeapons);

		// **ATTRIBUTE**
		int karmaMaxBonus = PROPERTIES.getOptionalRules().getATTRIBUTE().getPoints();
		// Der Bonus auf das Maximale Karma ergibt sich aus den übriggebliebenen Kaufpunkten bei der Charaktererschaffung
		characterAttributes = character.getAttributes();
		for (NAMEVALUEType raceattribute : namegiver.getATTRIBUTE()) {
			// Pro Atributt wird nun dessen Werte, Stufe und Würfel bestimmt
			ATTRIBUTEType attribute = characterAttributes.get(raceattribute.getName());
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
			if( OptionalRule_LegendpointsForAttributeIncrease ) calculatedLP.setAttributes(calculatedLP.getAttributes()+PROPERTIES.getCharacteristics().getAttributeTotalLP(attribute.getLpincrease()));
		}
		if( karmaMaxBonus <0 ) {
			errorout.println("The character was generated with to many spent attribute buy points: "+(-karmaMaxBonus));
		}

		// **DEFENSE**
		DEFENSEType defense = character.getDefence();
		defense.setPhysical(berechneWiederstandskraft(characterAttributes.get("DEX").getCurrentvalue()));
		defense.setSpell(berechneWiederstandskraft(characterAttributes.get("PER").getCurrentvalue()));
		defense.setSocial(berechneWiederstandskraft(characterAttributes.get("CHA").getCurrentvalue()));
		for(DEFENSEABILITYType racedefense : namegiver.getDEFENSE() ) {
			switch (racedefense.getKind()) {
			case PHYSICAL: defense.setPhysical(defense.getPhysical()+1); break;
			case SPELL: defense.setSpell(defense.getSpell()+1); break;
			case SOCIAL: defense.setSocial(defense.getSocial()+1); break;
			}
		}

		// **INITIATIVE**
		STEPDICEType initiativeStepdice=attribute2StepAndDice(characterAttributes.get("DEX").getCurrentvalue());
		INITIATIVEType initiative = character.getInitiative();
		// Setze alle Initiative Modifikatoren zurück, da diese im folgenden neu bestimmt werden.
		initiative.setModification(0);
		initiative.setBase(initiativeStepdice.getStep());
		initiative.setStep(initiativeStepdice.getStep());
		initiative.setDice(initiativeStepdice.getDice());

		// **HEALTH**
		CHARACTERISTICSHEALTHRATING newhealth = bestimmeHealth(characterAttributes.get("TOU").getCurrentvalue());
		DEATHType death=character.getDeath();
		DEATHType unconsciousness=character.getUnconsciousness();
		death.setBase(newhealth.getDeath());
		death.setAdjustment(0);
		unconsciousness.setBase(newhealth.getUnconsciousness());
		unconsciousness.setAdjustment(0);
		character.getWound().setThreshold(newhealth.getWound()+namegiver.getWOUND().getThreshold());
		RECOVERYType recovery = character.getRecovery();
		recovery.setTestsperday(newhealth.getRecovery());
		recovery.setStep(characterAttributes.get("TOU").getStep());
		recovery.setDice(characterAttributes.get("TOU").getDice());

		// **KARMA**
		TALENTType karmaritualTalent = null;
		final String KARMARITUAL = PROPERTIES.getKarmaritualName(); 
		if( KARMARITUAL == null ) {
			errorout.println("Karmaritual talent name is not defined for selected language.");
		} else {
			for( TALENTType talent : character.getTalentByName(KARMARITUAL) ) {
				if( talent.getRealigned() == 0 ) {
					karmaritualTalent=talent;
					break;
				}
			}
			if(karmaritualTalent == null ) {
				errorout.println("No Karmaritual ("+KARMARITUAL+") could be found.");
			}
		}
		int calculatedKarmaLP=calculateKarma(character.getKarma(), karmaritualTalent, namegiver.getKarmamodifier(), karmaMaxBonus);
		if( OptionalRule_KarmaLegendPointCost ) {
			calculatedLP.setKarma(calculatedLP.getKarma()+calculatedKarmaLP);
		} else {
			calculatedKarmaLP=0;
			calculatedLP.setKarma(0);
		}

		// **MOVEMENT**
		character.calculateMovement();

		// **CARRYING**
		character.calculateCarrying();

		// Berechne Gewicht aller Münzen
		for( COINSType coins : character.getAllCoins() ) {
			// Mit doppelter Genauigkeit die Gewichte der Münzen addieren,
			double weight = 0;
			// Kupfermünzen: 0,5 Unze (oz)
			weight += coins.getCopper() / 32.0;
			// Silbermünzen: 0,2 Unze (oz)
			weight += coins.getSilver() / 80.0;
			// Goldmünzen: 0,1 Unze (oz)
			weight += coins.getGold() / 160.0;
			// Elementarmünzen: 0,1 Unze (oz)
			weight += (double)( coins.getAir()+coins.getEarth()+coins.getFire()+coins.getWater()+coins.getOrichalcum() ) / 160.0;
			// zum Abspeichern langt die einfache Genaugkeit
			coins.setWeight((float)weight);
		}

		character.setAbilities(concatStrings(namegiver.getABILITY()));

		// Lösche alle Diziplin Boni, damit diese unten wieder ergänzt werden können ohne auf duplikate Achten zu müssen
		character.clearDisciplineBonuses();
		// Stelle sicher dass ale Disziplin Talent eingügt werden
		character.ensureDisciplinTalentsExits();
		// Entferne alle Talente die zuhohle Kreise haben.
		character.removeIllegalTalents();
		// Entferne alle Optionalen Talente ohne Rang.
		character.removeZeroRankOptionalTalents();
		// Prüfe ob Talente realigned weren müssen.
		character.realignOptionalTalents();
		character.updateRealignedTalents();
		// Sammle alle Namensgeber spezial Talente in einer Liste zusammen
		HashMap<String,TALENTABILITYType> namegivertalents = new HashMap<String,TALENTABILITYType>();
		for( TALENTABILITYType t : namegiver.getTALENT() ) {
			namegivertalents.put(t.getName(), t);
		}
		// Wenn ein Charakter Weihepunkte erhalten hat, dann steht ihm  das Questorentalent zur Verfügung
		DEVOTIONType devotionPoints = character.getDevotionPoints();
		if( (devotionPoints!=null) && (devotionPoints.getValue()>0) ) {
			TALENTABILITYType talent = new TALENTABILITYType();
			talent.setName(questorTalentName);
			talent.setLimitation(devotionPoints.getPassion());
			namegivertalents.put(questorTalentName, talent);
		}
		int maxKarmaStepBonus=0;
		List<DISCIPLINEType> allDisciplines = character.getDisciplines();
		HashMap<String,Integer> diciplineCircle = new HashMap<String, Integer>();
		int disciplinenumber=0;
		for( DISCIPLINEType currentDiscipline : allDisciplines ) {
			disciplinenumber++;
			List<TALENTType> durabilityTalents = new ArrayList<TALENTType>();
			TalentsContainer currentTalents = new TalentsContainer(currentDiscipline);
			for( TALENTType talent : currentTalents.getDisciplineAndOptionaltalents() ) ensureRankAndTeacher(talent);
			HashMap<String, Integer> defaultOptionalTalents = PROPERTIES.getDefaultOptionalTalents(disciplinenumber);
			calculateTalents(namegivertalents, defaultOptionalTalents, disciplinenumber, currentDiscipline.getCircle(), durabilityTalents, currentTalents.getDisciplinetalents(), true);
			calculateTalents(namegivertalents, defaultOptionalTalents, disciplinenumber, currentDiscipline.getCircle(), durabilityTalents, currentTalents.getOptionaltalents(), false);
			// Alle Namegiver Talente, die bis jetzt noch nicht enthalten waren,
			// werden nun den optionalen Talenten beigefügt.
			for( String t : namegivertalents.keySet() ) {
				TALENTType talent = new TALENTType();
				talent.setName(namegivertalents.get(t).getName());
				talent.setLimitation(namegivertalents.get(t).getLimitation());
				talent.setCircle(0);
				capabilities.enforceCapabilityParams(talent);
				talent.setTEACHER(new TALENTTEACHERType());
				RANKType rank = new RANKType();
				calculateCapabilityRank(rank,characterAttributes.get(talent.getAttribute().value()));
				talent.setRANK(rank);
				currentTalents.getOptionaltalents().add(talent);
			}
			namegivertalents.clear(); // Ist keine lokale Variable und Namensgebertalent sollen nur bei einer Disziplin einfügt werden
			for( String t : defaultOptionalTalents.keySet() ) {
				// Talente aus späteren Kreisen werden auch erst später eingefügt
				int circle = defaultOptionalTalents.get(t);
				if( circle > currentDiscipline.getCircle() ) continue;
				TALENTType talent = new TALENTType();
				talent.setName(t);
				talent.setCircle(circle);
				capabilities.enforceCapabilityParams(talent);
				talent.setTEACHER(new TALENTTEACHERType());
				RANKType rank = new RANKType();
				calculateCapabilityRank(rank,characterAttributes.get(talent.getAttribute().value()));
				talent.setRANK(rank);
				currentTalents.getOptionaltalents().add(talent);
			}
			// Wenn Durability-Talente gefunden wurden, berechnen aus dessen Rank
			// die Erhöhung von Todes- und Bewustlosigkeitsschwelle
			for( TALENTType durabilityTalent : durabilityTalents ) {
				DISCIPLINE disziplinProperties = PROPERTIES.getDisziplin(currentDiscipline.getName());
				if( disziplinProperties != null ) {
					DISCIPLINEDURABILITYType durability = disziplinProperties.getDURABILITY();
					int rank = durabilityTalent.getRANK().getRank()-durabilityTalent.getRANK().getRealignedrank();
					death.setAdjustment(death.getAdjustment()+(durability.getDeath()*rank));
					unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*rank));
					durabilityTalent.setLimitation(durability.getDeath()+"/"+durability.getUnconsciousness());
				}
			}
			diciplineCircle.put(currentDiscipline.getName(), currentDiscipline.getCircle());
			// Nur der höchtse Bonus wird gewertet.
			int currentKarmaStepBonus = getDisciplineKarmaStepBonus(currentDiscipline);
			if( currentKarmaStepBonus > maxKarmaStepBonus ) maxKarmaStepBonus = currentKarmaStepBonus;
			List<DISCIPLINEBONUSType> currentBonuses = currentDiscipline.getDISCIPLINEBONUS();
			currentBonuses.clear();
			currentBonuses.addAll(getDisciplineBonuses(currentDiscipline));
			// TALENT KNACKS
			for( TALENTType talent : currentTalents.getDisciplineAndOptionaltalents() ) checkTalentKnacks(talent,disciplinenumber);
		}

		// ** ARMOR **
		// Zu erstmal alles entfernen was nicht eine Reale Rüstung ist:
		List<ARMORType> totalarmor = character.removeVirtualArmorFromNormalArmorList();
		// natural ARMOR bestimmen
		ARMORType namegiverArmor = namegiver.getARMOR();
		ARMORType naturalArmor = new ARMORType();
		naturalArmor.setName(namegiverArmor.getName());
		naturalArmor.setMysticarmor(namegiverArmor.getMysticarmor()+berechneMysticArmor(characterAttributes.get("WIL").getCurrentvalue()));
		naturalArmor.setPhysicalarmor(namegiverArmor.getPhysicalarmor());
		naturalArmor.setPenalty(namegiverArmor.getPenalty());
		naturalArmor.setUsed(namegiverArmor.getUsed());
		naturalArmor.setWeight(namegiverArmor.getWeight());
		naturalArmor.setVirtual(YesnoType.YES);
		// Natürliche Rüstung der Liste voranstellen
		totalarmor.add(0, naturalArmor);
		// magischen Rüstung/Rüstungsschutz anhängen:
		totalarmor.addAll(character.getMagicArmor());
		// Bestimme nun den aktuellen Gesamtrüstungsschutz
		int mysticalarmor=0;
		int physicalarmor=0;
		int protectionpenalty=0;
		for (ARMORType armor : totalarmor ) {
			if( armor.getUsed().equals(YesnoType.YES) ) {
				mysticalarmor+=armor.getMysticarmor();
				physicalarmor+=armor.getPhysicalarmor();
				protectionpenalty+=armor.getPenalty();
			}
		}
		PROTECTIONType protection = character.getProtection();
		protection.setMysticarmor(mysticalarmor);
		protection.setPenalty(protectionpenalty);
		protection.setPhysicalarmor(physicalarmor);
		character.readjustInitiativeModifikator(-protectionpenalty);

		// ** KARMA STEP **
		KARMAType karma = character.getKarma();
		karma.setStep(4 + maxKarmaStepBonus); // mindestens d6
		karma.setDice(PROPERTIES.step2Dice(karma.getStep()));

		int skillsStartranks=calculatedLP.getUSEDSTARTRANKS().getSkills();
		character.removeEmptySkills();
		List<SKILLType> skills = character.getSkills();
		if( skills.isEmpty() ) {
			for(SKILLType skilltemplate : PROPERTIES.getStartingSkills() ) {
				SKILLType skill = new SKILLType();
				skill.setName(skilltemplate.getName());
				skill.setLimitation(skilltemplate.getLimitation());
				RANKType rank = new RANKType();
				rank.setRank(skilltemplate.getRANK().getRank());
				rank.setStartrank(skilltemplate.getRANK().getStartrank());
				skill.setRANK(rank);
				skills.add(skill);
			}
		}
		List<CAPABILITYType> defaultSkills = capabilities.getDefaultSkills(namegiver.getNOTDEFAULTSKILL());
		for( SKILLType skill : skills ) {
			RANKType rank = skill.getRANK();
			int startrank = rank.getStartrank();
			skillsStartranks+=startrank;
			int lpcostfull= PROPERTIES.getCharacteristics().getSkillRankTotalLP(rank.getRank());
			int lpcoststart= PROPERTIES.getCharacteristics().getSkillRankTotalLP(startrank);
			rank.setLpcost(lpcostfull-lpcoststart);
			rank.setBonus(0);
			calculatedLP.setSkills(calculatedLP.getSkills()+rank.getLpcost());
			capabilities.enforceCapabilityParams(skill);
			if( skill.getAttribute() != null ) {
				calculateCapabilityRank(rank,characterAttributes.get(skill.getAttribute().value()));
			}
			removeIfContains(defaultSkills,skill.getName());
		}
		calculatedLP.getUSEDSTARTRANKS().setSkills(skillsStartranks);

		// Wenn gewünscht dann zeige auch die DefaultSkills mit an
		if( OptionalRule_ShowDefaultSkills ) {
			for( CAPABILITYType defaultSkill : defaultSkills ) {
				List<String> limitations = defaultSkill.getLIMITATION();
				if( limitations.size()==0 ) limitations.add("");
				for( String limitation : limitations ) {
					SKILLType skill = new SKILLType();
					RANKType rank = new RANKType();
					skill.setRANK(rank);
					skill.setName(defaultSkill.getName());
					skill.setLimitation(limitation);
					capabilities.enforceCapabilityParams(skill);
					if( skill.getAttribute() != null ) {
						calculateCapabilityRank(rank,characterAttributes.get(skill.getAttribute().value()));
					}
					skills.add(skill);
				}
			}
		}

		DEFENSEType disciplineDefense = getDisciplineDefense(diciplineCircle);
		defense.setPhysical(defense.getPhysical()+disciplineDefense.getPhysical());
		defense.setSocial(defense.getSocial()+disciplineDefense.getSocial());
		defense.setSpell(defense.getSpell()+disciplineDefense.getSpell());

		recovery.setStep(recovery.getStep()+getDisciplineRecoveryTestBonus(diciplineCircle));
		recovery.setDice(PROPERTIES.step2Dice(recovery.getStep()));

		character.readjustInitiativeModifikator(getDisciplineInitiative(diciplineCircle));

		// ** SPELLS **
		// Bestimme die wieviele Zaubersprüche bei der Charactererschaffung kostenlos dazu kamen
		// und wieviel ein SpellAbility pro Kreis pro Disziplin kostenlos dazukamen.
		int startingSpellLegendPointCost = 0;
		if( OptionalRule_SpellLegendPointCost ) {
			// Starting Spell can be from 1st and 2nd circle. Substact these Legendpoints from the legendpoints spend for spells
			ATTRIBUTEType per = characterAttributes.get("PER");
			startingSpellLegendPointCost = 100 * attribute2StepAndDice(per.getBasevalue()).getStep();
			int lpbonus = 0;
			for( int spellability : getDisciplineSpellAbility(diciplineCircle) ) {
				lpbonus += PROPERTIES.getCharacteristics().getSpellLP(spellability);
			}
			calculatedLP.setSpells(-lpbonus);
		} else {
			calculatedLP.setSpells(0);
		}
		HashMap<String, SPELLDEFType> spelllist = PROPERTIES.getSpells();
		boolean firstDiscipline=true;
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			if( firstDiscipline && OptionalRule_SpellLegendPointCost ) {
				// Wenn die erste Disziplin eine Zauberdisciplin ist und die Optionale Regel, dass Zaubersprüche LP Kosten
				// gewählt wurde, dann reduziere die ZauberLPKosten um die StartZauber
				calculatedLP.setSpells(calculatedLP.getSpells()-startingSpellLegendPointCost);
				// Es ist jetzt schon einmal abgezogen. Stelle nun sicher dass nicht noch ein zweites Mal abgezogen werden kann.
				startingSpellLegendPointCost=0;
			}
			for( SPELLType spell : discipline.getSPELL() ) {
				SPELLDEFType spelldef = spelllist.get(spell.getName());
				if( spelldef == null ) {
					errorout.println("Unknown Spell '"+spell.getName()+"' in grimour found. Spell is left unmodified in grimour.");
				} else {
					spell.setCastingdifficulty(spelldef.getCastingdifficulty());
					spell.setDuration(spelldef.getDuration());
					spell.setEffect(spelldef.getEffect());
					spell.setEffectarea(spelldef.getEffectarea());
					spell.setRange(spelldef.getRange());
					spell.setReattuningdifficulty(spelldef.getReattuningdifficulty());
					spell.setThreads(spelldef.getThreads());
					spell.setWeavingdifficulty(spelldef.getWeavingdifficulty());
					spell.setBookref(spelldef.getBookref());
				}
				if( OptionalRule_SpellLegendPointCost ) {
					// The cost of spells are equivalent to the cost of increasing a Novice Talent to a Rank equal to the Spell Circle
					int lpcost=PROPERTIES.getCharacteristics().getSpellLP(spell.getCircle());
					calculatedLP.setSpells(calculatedLP.getSpells()+lpcost);
				}
			}
			firstDiscipline=false;
		}

		for( BLOODCHARMITEMType item : character.getBloodCharmItem() ) {
			if( item.getUsed().equals(YesnoType.YES) ) {
				death.setAdjustment(death.getAdjustment()-item.getBlooddamage());
				unconsciousness.setAdjustment(unconsciousness.getAdjustment()-item.getBlooddamage());
			}
		}

		List<TALENTType> firstDisciplineOptionalTalents = null;
		if( ! allDisciplines.isEmpty() ) firstDisciplineOptionalTalents = allDisciplines.get(0).getOPTIONALTALENT();
		for( THREADITEMType item : character.getThreadItem() ) {
			for( int rank=0; rank<item.getWeaventhreadrank(); rank++ ) {
				THREADRANKType threadrank = item.getTHREADRANK().get(rank);
				if( threadrank == null ) {
					errorout.println("Undefined Threadrank for "+item.getName()+" for rank "+rank );
					continue;
				}
				for(DEFENSEABILITYType itemdefense : threadrank.getDEFENSE() ) {
					switch (itemdefense.getKind()) {
					case PHYSICAL: defense.setPhysical(defense.getPhysical()+1); break;
					case SPELL: defense.setSpell(defense.getSpell()+1); break;
					case SOCIAL: defense.setSocial(defense.getSocial()+1); break;
					}
				}
				for(TALENTABILITYType itemtalent : threadrank.getTALENT() ) {
					String limitation = itemtalent.getLimitation();
					boolean notfound=true;
					for( TALENTType talent : character.getTalentByName(itemtalent.getName()) ) {
						if( limitation.isEmpty() || (talent.getLimitation().equals(limitation)) ) {
							notfound=false;
							RANKType talentrank = talent.getRANK();
							talentrank.setBonus(talentrank.getBonus()+1);
							calculateCapabilityRank(talentrank,characterAttributes.get(talent.getAttribute().value()));
						}
					}
					if( notfound && (firstDisciplineOptionalTalents!=null) ) {
						if( limitation.isEmpty() ) limitation ="(#)";
						else limitation += " (#)";
						TALENTType bonusTalent = new TALENTType();
						bonusTalent.setName(itemtalent.getName());
						bonusTalent.setLimitation(limitation);
						bonusTalent.setCircle(0);
						capabilities.enforceCapabilityParams(bonusTalent);
						RANKType bonusrank = new RANKType();
						bonusrank.setRank(0);
						bonusrank.setBonus(1);
						calculateCapabilityRank(bonusrank,characterAttributes.get(bonusTalent.getAttribute().value()));
						bonusTalent.setRANK(bonusrank);
						TALENTTEACHERType teacher = new TALENTTEACHERType();
						teacher.setByversatility(YesnoType.NO);
						teacher.setTalentcircle(rank);
						teacher.setTeachercircle(rank);
						teacher.setName(item.getName());
						bonusTalent.setTEACHER(teacher);
						firstDisciplineOptionalTalents.add(bonusTalent);
					}
				}
				for(DISZIPINABILITYType iteminitiative : threadrank.getINITIATIVE() ) {
					character.readjustInitiativeModifikator(iteminitiative.getCount());
				}
				initiative.setStep(initiative.getBase()+initiative.getModification());
				initiative.setDice(PROPERTIES.step2Dice(initiative.getStep()));
				// TODO: other effects of MagicItems
			}
			// TODO:List<TALENTType> optTalents = allTalents.get(disciplinenumber).getOPTIONALTALENT();
		}

		// Veränderungen am death/unconsciousness adjustment sollen beachtet werden
		death.setValue(death.getBase()+death.getAdjustment());
		unconsciousness.setValue(unconsciousness.getBase()+unconsciousness.getAdjustment());

		if( calculatedLP.getSpells() < 0 ) calculatedLP.setSpells(0);
		calculatedLP.setTotal(calculatedLP.getAttributes()+calculatedLP.getDisciplinetalents()+
				calculatedLP.getKarma()+calculatedLP.getMagicitems()+calculatedLP.getOptionaltalents()+
				calculatedLP.getSkills()+calculatedLP.getSpells()+calculatedLP.getKnacks());

		if( OptionalRule_AutoInsertLegendPointSpent ) character.addLegendPointsSpent(oldcalculatedLP);
		character.calculateLegendPointsAndStatus();
		character.calculateDevotionPoints();

		return charakter;
	}

	private void checkTalentKnacks(TALENTType talent, int disciplinenumber) {
		String talentname = talent.getName();
		for( KNACKType knack : talent.getKNACK() ) {
			String knackname = knack.getName();
			for( KNACKBASEType k : globalTalentKnackList ) {
				if( k.getName().equals(knackname) ) {
					if( k.getBasename().equals(talentname) ) {
						knack.setBookref(k.getBookref());
						knack.setMinrank(k.getMinrank());
						knack.setStrain(k.getStrain());
						break; // Wenn er einmal gefunden wurde, brauch nicht mehr weitergesucht werden.
					} else {
						errorout.println("The knack '"+knackname+"' was learned for the talent '"+talentname+"', but should be learned for talent '"+k.getBasename()+"'. Will not enforce knack default values!");
					}
				}
			}
			int talentrank = talent.getRANK().getRank();
			int knackminrank = knack.getMinrank();
			if( talentrank < knackminrank ) {
				errorout.println("The knack '"+knackname+"' was learned for the talent '"+talentname+"', but talent rank ("+talentrank+") is less than the knack minimum talent rank ("+knackminrank+")!");
			}
			CHARACTERISTICSCOST knackcost = PROPERTIES.getCharacteristics().getTalentRankLPIncreaseTable(disciplinenumber,talent.getCircle()).get(knackminrank);
			if( knackcost == null ) {
				errorout.println("Could not get knack lp costs for disciplinenumber="+disciplinenumber+", talent_circle="+talent.getCircle()+" and knackminrank="+knackminrank);
			} else {
				calculatedLP.setKnacks(calculatedLP.getKnacks()+knackcost.getCost());
			}
		}
	}

	public void ensureRankAndTeacher(TALENTType talent) {
		if( talent.getRANK() == null ) talent.setRANK(new RANKType());
		if( talent.getTEACHER() == null ) talent.setTEACHER(new TALENTTEACHERType());
	}

	private void calculateTalents(HashMap<String, TALENTABILITYType> namegivertalents, HashMap<String, Integer> defaultOptionalTalents, int disciplinenumber, int disciplinecircle, List<TALENTType> durabilityTalents, List<TALENTType> talents, boolean disTalents) {
		int totallpcost=0;
		int startranks=calculatedLP.getUSEDSTARTRANKS().getTalents();
		for( TALENTType talent : talents ) {
			TALENTTEACHERType teacher = talent.getTEACHER();
			if( teacher == null ) {
				teacher = new TALENTTEACHERType();
				talent.setTEACHER(teacher);
			}
			RANKType rank = talent.getRANK();
			if( rank == null ) {
				rank = new RANKType();
				talent.setRANK(rank);
			}
			if( disTalents && OptionalRule_autoincrementDisciplinetalents &&
					(talent.getCircle() < disciplinecircle) && (rank.getRank() < disciplinecircle) ) {
				rank.setRank(disciplinecircle);
			}
			if( rank.getRank() < rank.getStartrank() ) rank.setRank(rank.getStartrank());
			if( rank.getRank() < rank.getRealignedrank() ) rank.setRank(rank.getRealignedrank());
			// Talente aus höheren Kreisen können keine Startranks haben, da Startranks nur bei der Charaktererschaffung vergeben werden.
			if( (talent.getCircle()>1) && (rank.getStartrank()>0) ) rank.setStartrank(0);
			capabilities.enforceCapabilityParams(talent);
			rank.setBonus(talent.getBonus());
			final int lpcostfull=PROPERTIES.getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getRank());
			final int lpcoststart=PROPERTIES.getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getStartrank());
			final int lpcostrealigned=PROPERTIES.getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),rank.getRealignedrank());
			if( lpcostrealigned > lpcoststart ) {
				rank.setLpcost(lpcostfull-lpcostrealigned);
			} else {
				rank.setLpcost(lpcostfull-lpcoststart);
			}
			totallpcost+=rank.getLpcost();
			startranks+=rank.getStartrank();
			ATTRIBUTENameType attr = talent.getAttribute();
			if( attr != null ) calculateCapabilityRank(rank,characterAttributes.get(attr.value()));
			String talentname = talent.getName();
			if( talentname.equals(durabilityTalentName)) durabilityTalents.add(talent);
			calculateKnacks(disciplinenumber, talent, rank.getRank());
			if( namegivertalents.containsKey(talentname) ) {
				namegivertalents.remove(talentname);
			}
			if( defaultOptionalTalents.containsKey(talentname) ) {
				defaultOptionalTalents.remove(talentname);
			}
			if( talent.getNotbyversatility().equals(YesnoType.YES) && teacher.getByversatility().equals(YesnoType.YES) ) {
				errorout.println("Talent '"+talentname+"' was lernead by versatility, but is not allowed to be learned by versatility.");
			}
		}
		calculatedLP.getUSEDSTARTRANKS().setTalents(startranks);
		if( disTalents ) {
			calculatedLP.setDisciplinetalents(calculatedLP.getDisciplinetalents()+totallpcost);
		} else {
			calculatedLP.setOptionaltalents(calculatedLP.getOptionaltalents()+totallpcost);
		}
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

	public static String concatStrings(List<String> strings) {
		String result = "";
		for ( String s : strings ) {
			if( ! result.isEmpty() ) result += ", ";
			result += s;
		}
		return result;
	}

	private static int calculateKarma(KARMAType karma, TALENTType karmaritualTalent, int karmaModifier, int karmaMaxBonus) {
		karma.setMaxmodificator(karmaMaxBonus);
		if( (karmaritualTalent != null) && (karmaritualTalent.getRANK() != null) ) {
			karma.setMax( karmaMaxBonus + (karmaModifier * karmaritualTalent.getRANK().getRank()) );
		} else {
			errorout.println("No karmaritual talent found, skipping maximal karma calculation.");
		}
		List<Integer> k = CharacterContainer.calculateAccounting(karma.getKARMAPOINTS());
		karma.setCurrent(karmaModifier+k.get(0)-k.get(1));
		return 10*k.get(0); // KarmaLP
	}

	private void calculateKnacks(int disciplinenumber, TALENTType talent, int rank) {
		for( KNACKType knack : talent.getKNACK() ) {
			if( knack.getMinrank() > rank ) {
				errorout.println("The rank of the talent '"+talent.getName()+"' is lower than the minimal rank for the kack '"+knack.getName()+"': "
						+rank+" vs. "+knack.getMinrank());
			}
			int lp = PROPERTIES.getCharacteristics().getTalentRankLPIncreaseTable(disciplinenumber,talent.getCircle()).get(knack.getMinrank()).getCost();
			calculatedLP.setKnacks(calculatedLP.getKnacks()+lp);
		}
	}

	private static int getDisciplineKarmaStepBonus(DISCIPLINEType discipline) {
		int result = 0;
		int circlenr=0;
		DISCIPLINE disziplinProperties = ApplicationProperties.create().getDisziplin(discipline.getName());
		if( disziplinProperties == null ) return result;
		for( DISCIPLINECIRCLEType circle : disziplinProperties.getCIRCLE()) {
			circlenr++;
			if( circlenr > discipline.getCircle() ) break;
			for( DISZIPINABILITYType karmastep : circle.getKARMASTEP()) {
				result += karmastep.getCount();
			}
		}
		return result;
	}

	private static int getDisciplineRecoveryTestBonus(HashMap<String,Integer> diciplineCircle) {
		int result = 0;
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			if( d == null ) continue;
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

	private static int getDisciplineInitiative(HashMap<String,Integer> diciplineCircle) {
		int result = 0;
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			if( d == null ) continue;
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
	private static DEFENSEType getDisciplineDefense(HashMap<String,Integer> diciplineCircle) {
		DEFENSEType result = new DEFENSEType();
		result.setPhysical(0);
		result.setSocial(0);
		result.setSpell(0);
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			if( d == null ) continue;
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

	private static List<Integer> getDisciplineSpellAbility(HashMap<String,Integer> diciplineCircle) {
		List<Integer> result = new ArrayList<Integer>();
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = PROPERTIES.getDisziplin(discipline);
			if( d == null ) continue;
			int circlenr = 0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( DISZIPINABILITYType spell : circle.getSPELLABILITY() ) {
					for(int i=0; i<spell.getCount(); i++) result.add(circlenr);
				}
			}
		}
		return result;
	}

	public static int berechneWiederstandskraft(int value) {
		int defense=0;
		for (CHARACTERISTICSDEFENSERAITING defenserating : ApplicationProperties.create().getCharacteristics().getDEFENSERAITING() ) {
			// errorout.println("berechneWiederstandskraft value "+value+" defense "+defense+" defenserating "+defenserating.getAttribute());
			if( (value >= defenserating.getAttribute()) && (defense<defenserating.getDefense()) ) {
				defense=defenserating.getDefense();
			}
		}
		return defense;
	}

	public static int berechneAttriubteCost(int modifier) {
		if ( modifier < -2 ) {
			errorout.println("The generation attribute value was to low. Value will increased to -2.");
			modifier = -2;
		}
		if ( modifier > 8 ) {
			errorout.println("The generation attribute value was to high. Value will be lower down to 8.");
			modifier = 8;
		}
		HashMap<Integer,Integer> attributecost = ApplicationProperties.create().getCharacteristics().getATTRIBUTECOST();
		return attributecost.get(modifier);
	}

	public static CHARACTERISTICSHEALTHRATING bestimmeHealth(int value) {
		HashMap<Integer,CHARACTERISTICSHEALTHRATING> healthrating = ApplicationProperties.create().getCharacteristics().getHEALTHRATING();
		return healthrating.get(value);
	}

	public static int berechneMysticArmor(int value) {
		List<Integer> mysticArmorTable = ApplicationProperties.create().getCharacteristics().getMYSTICARMOR();
		int mysticArmor=-1;
		for( int attribute : mysticArmorTable ) {
			if( attribute > value ) break;
			mysticArmor++;
		}
		return mysticArmor;
	}

	public static STEPDICEType attribute2StepAndDice(int value) {
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

	public void calculateCapabilityRank(RANKType talentRank, ATTRIBUTEType attr) {
		// Da der talent.bonus bereits im talent.rank.bonus enhalten ist, muss er hier
		// explizit nicht mehr weiter beachtet werden.
		if( attr == null ) {
			talentRank.setStep(talentRank.getRank()+talentRank.getBonus());
		} else {
			talentRank.setStep(talentRank.getRank()+talentRank.getBonus()+attr.getStep());
		}
		talentRank.setDice(PROPERTIES.step2Dice(talentRank.getStep()));
	}

	public static List<DISCIPLINEBONUSType> getDisciplineBonuses(DISCIPLINEType discipline) {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		int circlenr=0;
		DISCIPLINE disziplinProperties = ApplicationProperties.create().getDisziplin(discipline.getName());
		if( disziplinProperties == null ) return bonuses;
		for(DISCIPLINECIRCLEType circle : disziplinProperties.getCIRCLE()) {
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

	public PrintStream getErrorout() {
		return errorout;
	}

	public void setErrorout(PrintStream stream) {
		errorout = stream;
	}
}
