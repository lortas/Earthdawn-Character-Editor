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
import java.util.Map;
import java.util.List;
import java.util.TreeMap;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.*;

/**
 * Hilfsklasse zur Verarbeitung eines Earthdawn-Charakters. 
 * 
 * @author lortas
 */
public class ECEWorker {
	public static ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final String durabilityTalentName = PROPERTIES.getDurabilityName();
	public static final ECECapabilities capabilities = PROPERTIES.getCapabilities();
	public static final String karmaritualName = PROPERTIES.getKarmaritualName();
	public static OPTIONALRULES OptionalRule=PROPERTIES.getOptionalRules();
	public static boolean OptionalRule_SpellLegendPointCost=OptionalRule.getSPELLLEGENDPOINTCOST().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_KarmaLegendPointCost=OptionalRule.getKARMALEGENDPOINTCOST().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_autoincrementDisciplinetalents=OptionalRule.getAUTOINCREMENTDISCIPLINETALENTS().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_LegendpointsForAttributeIncrease=OptionalRule.getLEGENDPOINTSFORATTRIBUTEINCREASE().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_AutoInsertLegendPointSpent=OptionalRule.getAUTOINSERTLEGENDPOINTSPENT().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_KeepLegendPointSync=OptionalRule.getKEEPLEGENDPOINTSYNC().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_EnduringArmorByStrength=OptionalRule.getENDURINGARMORBYSTRENGTH().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_AligningTalentsAndSkills=OptionalRule.getALIGNINGTALENTSANDSKILLS().getUsed().equals(YesnoType.YES);
	public static boolean OptionalRule_NoNegativeKarmaMax=PROPERTIES.getOptionalRules().getATTRIBUTE().getLimitoneway().equals(YesnoType.YES);
	public static int OptionalRule_MaxAttributeBuyPoints=PROPERTIES.getOptionalRules().getATTRIBUTE().getPoints();
	public static final Map<String, SPELLDEFType> spelllist = PROPERTIES.getSpells();
	private Map<ATTRIBUTENameType, ATTRIBUTEType> characterAttributes=null;
	CalculatedLPContainer calculatedLP = null;
	private static PrintStream errorout = System.err;
	private CharacterContainer character;

	public static void refreshOptionalRules() {
		OptionalRule=PROPERTIES.getOptionalRules();
		OptionalRule_SpellLegendPointCost=OptionalRule.getSPELLLEGENDPOINTCOST().getUsed().equals(YesnoType.YES);
		OptionalRule_KarmaLegendPointCost=OptionalRule.getKARMALEGENDPOINTCOST().getUsed().equals(YesnoType.YES);
		OptionalRule_autoincrementDisciplinetalents=OptionalRule.getAUTOINCREMENTDISCIPLINETALENTS().getUsed().equals(YesnoType.YES);
		OptionalRule_LegendpointsForAttributeIncrease=OptionalRule.getLEGENDPOINTSFORATTRIBUTEINCREASE().getUsed().equals(YesnoType.YES);
		OptionalRule_AutoInsertLegendPointSpent=OptionalRule.getAUTOINSERTLEGENDPOINTSPENT().getUsed().equals(YesnoType.YES);
		OptionalRule_KeepLegendPointSync=OptionalRule.getKEEPLEGENDPOINTSYNC().getUsed().equals(YesnoType.YES);
		OptionalRule_EnduringArmorByStrength=OptionalRule.getENDURINGARMORBYSTRENGTH().getUsed().equals(YesnoType.YES);
		OptionalRule_AligningTalentsAndSkills=OptionalRule.getALIGNINGTALENTSANDSKILLS().getUsed().equals(YesnoType.YES);
		OptionalRule_NoNegativeKarmaMax=PROPERTIES.getOptionalRules().getATTRIBUTE().getLimitoneway().equals(YesnoType.YES);
		OptionalRule_MaxAttributeBuyPoints=PROPERTIES.getOptionalRules().getATTRIBUTE().getPoints();
	}

	public ECEWorker(CharacterContainer character) {
		super();
		this.character = character;
	}
	public ECEWorker(EDCHARACTER charakter) {
		this(new CharacterContainer(charakter));
	}

	/**
	 * Verabeiten eines Charakters.
	 */
	public EDCHARACTER verarbeiteCharakter() {
		// Orignal berechnete LP sichern
		calculatedLP = new CalculatedLPContainer(character.getCalculatedLegendpoints());
		CalculatedLPContainer oldcalculatedLP = calculatedLP.copy();
		// Berechnete LP erstmal zurücksetzen
		calculatedLP.clear();

		character.getEDCHARACTER().setName(character.getName().replaceAll("[^-+ '\"A-Za-z0-9]", ""));
		character.getEDCHARACTER().setPlayer(character.getPlayer().replaceAll("[^-+ '\"A-Za-z0-9]", ""));
		character.getAppearance().setHeightString( PROPERTIES.getUnitCalculator().formatLength( character.getAppearance().getHeight(), -1 ) );
		character.getAppearance().setWeightString( PROPERTIES.getUnitCalculator().formatWeight( character.getAppearance().getWeight() ) );

		// Die OpenSpell List ist eine generierte Liste und muss daher am Anfang gelöscht werden
		character.clearOpenSpellList();

		// Benötige Rasseneigenschaften der gewählten Rasse im Objekt "charakter":
		NAMEGIVERABILITYType namegiver = character.getRace();
		List<String> namegiverAbilities = new ArrayList<String>();
		for( String a : namegiver.getABILITY() ) namegiverAbilities.add(a);

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
		int spendAttributeBuyPoints=0;
		// Der Bonus auf das Maximale Karma ergibt sich aus den übriggebliebenen Kaufpunkten bei der Charaktererschaffung
		characterAttributes = character.getAttributes();
		for (ATTRIBUTENAMEVALUEType raceattribute : namegiver.getATTRIBUTE()) {
			// Pro Atributt wird nun dessen Werte, Stufe und Würfel bestimmt
			ATTRIBUTEType attribute = characterAttributes.get(raceattribute.getName());
			if( attribute == null ) {
				attribute=new ATTRIBUTEType();
				attribute.setName(raceattribute.getName());
				characterAttributes.put(raceattribute.getName(), attribute);
				System.err.println("Character '"+character.getName()+"' do not have any attribute '"+raceattribute.getName()+"'");
			}
			attribute.setRacevalue(raceattribute.getValue());
			attribute.setCost(berechneAttriubteCost(attribute.getGenerationvalue()));
			int value = attribute.getRacevalue() + attribute.getGenerationvalue();
			attribute.setBasevalue(value);
			value += attribute.getLpincrease();
			attribute.setCurrentvalue(value);
			STEPDICEType stepdice=attribute2StepAndDice(value);
			attribute.setDice(stepdice.getDice());
			attribute.setStep(stepdice.getStep());
			spendAttributeBuyPoints+=attribute.getCost();
			if( OptionalRule_LegendpointsForAttributeIncrease ) {
				calculatedLP.addAttribute(
						PROPERTIES.getCharacteristics().getAttributeTotalLP(attribute.getLpincrease()),
						"Attribute "+attribute.getName().value()+" increased by one"
						);
			}
		}
		if( spendAttributeBuyPoints > OptionalRule_MaxAttributeBuyPoints ) {
			errorout.println("The character was generated with to many spent attribute buy points: "+spendAttributeBuyPoints+" > "+OptionalRule_MaxAttributeBuyPoints);
		}

		// **DEFENSE**
		DefenseAbility defenses = character.getDefence();
		defenses.set(EffectlayerType.PHYSICAL,berechneWiederstandskraft(characterAttributes.get(ATTRIBUTENameType.DEX).getCurrentvalue()));
		defenses.set(EffectlayerType.MYSTIC,berechneWiederstandskraft(characterAttributes.get(ATTRIBUTENameType.PER).getCurrentvalue()));
		defenses.set(EffectlayerType.SOCIAL,berechneWiederstandskraft(characterAttributes.get(ATTRIBUTENameType.CHA).getCurrentvalue()));
		for(DEFENSEABILITYType racedefense : namegiver.getDEFENSE() ) {
			switch (racedefense.getKind()) {
			case PHYSICAL:
				defenses.add(EffectlayerType.PHYSICAL,racedefense.getBonus());
				namegiverAbilities.add("physical defense +"+racedefense.getBonus()+" *");
				break;
			case MYSTIC:
				defenses.add(EffectlayerType.MYSTIC,racedefense.getBonus());
				namegiverAbilities.add("spell defense +"+racedefense.getBonus()+" *");
				break;
			case SOCIAL:
				defenses.add(EffectlayerType.SOCIAL,racedefense.getBonus());
				namegiverAbilities.add("social defense +"+racedefense.getBonus()+" *");
				break;
			}
		}

		// **INITIATIVE**
		// Setze alle Initiative Modifikatoren zurück, da diese im folgenden neu bestimmt werden.
		character.resetInitiative(attribute2StepAndDice(characterAttributes.get(ATTRIBUTENameType.DEX).getCurrentvalue()));

		// **HEALTH**
		CHARACTERISTICSHEALTHRATING newhealth = bestimmeHealth(characterAttributes.get(ATTRIBUTENameType.TOU).getCurrentvalue());
		character.clearBloodDamage();
		DEATHType death=character.getDeath();
		DEATHType unconsciousness=character.getUnconsciousness();
		death.setBase(newhealth.getDeath());
		death.setAdjustment(0);
		unconsciousness.setBase(newhealth.getUnconsciousness());
		unconsciousness.setAdjustment(0);
		int namegiverWoundThresholdBonus = namegiver.getWOUND().getThreshold();
		WOUNDType wound = character.getWound();
		wound.setThreshold(newhealth.getWound()+namegiverWoundThresholdBonus);
		if( namegiverWoundThresholdBonus > 0) namegiverAbilities.add("wound threshold +"+namegiverWoundThresholdBonus+" *");
		else if( namegiverWoundThresholdBonus < 0) namegiverAbilities.add("wound threshold "+namegiverWoundThresholdBonus+" *");
		int totalwounds=wound.getNormal()+wound.getBlood();
		if( totalwounds>1 ) wound.setPenalties(totalwounds-1);
		else wound.setPenalties(0);
		RECOVERYType recovery = character.getRecovery();
		recovery.setTestsperday(newhealth.getRecovery());
		recovery.setStep(characterAttributes.get(ATTRIBUTENameType.TOU).getStep());
		recovery.setDice(characterAttributes.get(ATTRIBUTENameType.TOU).getDice());

		// **KARMA**
		List<DISCIPLINEType> allDisciplines = character.getDisciplines();
		if( (karmaritualName==null)||(karmaritualName.isEmpty()) ) {
			errorout.println("Karmaritual talent name is not defined for selected language.");
		} else if( ! allDisciplines.isEmpty() ) {
			TALENTType karmaritualTalent = null;
			if(PROPERTIES.getRulesetLanguage().getRulesetversion().equals(RulesetversionType.ED_3)) {
				for( TALENTType talent : character.getTalentByName(karmaritualName) ) {
					if( talent.getRealigned() == 0 ) {
						karmaritualTalent=talent;
						break;
					}
				}
			} else if(PROPERTIES.getRulesetLanguage().getRulesetversion().equals(RulesetversionType.ED_4)) {
				karmaritualTalent = new TALENTType();
				karmaritualTalent.setName(karmaritualName);
				RANKType rank = new RANKType();
				rank.setRank(0);
				karmaritualTalent.setRANK(rank);
				for( DISCIPLINEType d : allDisciplines ) {
					rank.setRank(rank.getRank()+d.getCircle());
				}
			}

			if(karmaritualTalent == null ) {
				errorout.println("No Karmaritual ("+karmaritualName+") could be found.");
			}
			int calculatedKarmaLP=calculateKarma(character.getKarma(), karmaritualTalent, namegiver.getKarmamodifier(), OptionalRule_MaxAttributeBuyPoints - spendAttributeBuyPoints);
			if( (calculatedKarmaLP!=0) && OptionalRule_KarmaLegendPointCost ) {
				calculatedLP.addKarma(calculatedKarmaLP,"LPs spent for Karma");
			}
		}

		// **MOVEMENT**
		character.calculateMovement();

		// **CARRYING**
		character.calculateCarrying();

		// Berechne Gewicht aller Münzen
		for( COINSType coins : character.getAllCoins() ) {
			// Mit doppelter Genauigkeit die Gewichte der Münzen addieren,
			double weight = 0;
			// Kupfermünzen: 1/3 Unze (oz)
			weight += coins.getCopper() / 48.0;
			// Silbermünzen: 1/4 Unze (oz)
			weight += coins.getSilver() / 64.0;
			// Goldmünzen: 1/5 Unze (oz)
			weight += coins.getGold() / 80.0;
			// Edelsteine mit Wert 50 Silber : 1/10 Unze (oz)
			weight += coins.getGem50() / 160.0;
			// Edelsteine mit Wert 100 Silber : 1/5 Unze (oz)
			weight += coins.getGem100() / 80.0;
			// Edelsteine mit Wert 200 Silber : 1/3 Unze (oz)
			weight += coins.getGem200() / 48.0;
			// Edelsteine mit Wert 500 Silber : 2/3 Unze (oz)
			weight += coins.getGem500() / 24.0;
			// Edelsteine mit Wert 1000 Silber : 1 3/5 Unzen (oz)
			weight += coins.getGem1000()/ 10.0;
			// Elementarmünzen: 1/10 Unze (oz)
			weight += (double)( coins.getAir()+coins.getEarth()+coins.getFire()+coins.getWater()+coins.getOrichalcum() ) / 160.0;
			// zum Abspeichern langt die einfache Genaugkeit
			coins.setWeight((float)weight);
		}

		// **SKILL**
		calculateSkills(character, characterAttributes, namegiverAbilities, calculatedLP);

		// Lösche alle Diziplin Boni, damit diese unten wieder ergänzt werden können ohne auf Duplikate achten zu müssen
		character.clearDisciplineBonuses();
		// Stelle sicher dass alle Disziplin Talent eingügt werden
		character.ensureDisciplinTalentsExits();
		character.resetFreeTalentsExits();
		// Entferne alle Talente die zuhohle Kreise haben.
		character.removeIllegalTalents();
		// Entferne alle Optionalen Talente ohne Rang.
		character.removeZeroRankOptionalTalents();
		// Prüfe ob Talente realigned weren müssen.
		character.realignOptionalTalents();
		// Finde zu allen Talenten, ob es Realigned Talente dazu gibt und aktuallisiere deren Realigned Rank
		character.updateRealignedTalents();
		// Sammle alle Namensgeber spezial Talente in einer Liste zusammen
		Map<String,TALENTABILITYType> namegivertalents = new TreeMap<String,TALENTABILITYType>();
		for( TALENTABILITYType t : namegiver.getTALENT() ) {
			String name = t.getName();
			namegivertalents.put(name, t);
			namegiverAbilities.add("extra optional talent '"+name+"' *");
		}
		// Wenn ein Charakter Weihepunkte erhalten hat, dann steht ihm das Questorentalent zur Verfügung
		DEVOTIONType devotionPoints = character.getDevotionPoints();
		if( (devotionPoints!=null) && (devotionPoints.getValue()>0) ) {
			String questorTalentName = PROPERTIES.getQuestorTalentName();
			TALENTABILITYType talent = new TALENTABILITYType();
			talent.setName(questorTalentName);
			talent.setLimitation(devotionPoints.getPassion());
			namegivertalents.put(questorTalentName, talent);
		}
		int maxKarmaStepBonus=0;
		Map<String,Integer> diciplineCircle = new TreeMap<String, Integer>();
		int disciplinenumber=0;
		for( DISCIPLINEType currentDiscipline : allDisciplines ) {
			disciplinenumber++;
			List<TALENTType> durabilityTalents = new ArrayList<TALENTType>();
			TalentsContainer currentTalents = new TalentsContainer(currentDiscipline);
			Map<String, Integer> defaultOptionalTalents = PROPERTIES.getDefaultOptionalTalents(disciplinenumber);
			int currentCircle = currentDiscipline.getCircle();
			int minDisciplineCircle=character.getDisciplineMinCircle(disciplinenumber).getCircle();
			for( TALENTType t : currentTalents.getAllTalents() ) capabilities.enforceCapabilityParams(t);
			ensureRankAndTeacher(currentTalents.getAllTalents());
			checkStartrank(disciplinenumber, currentTalents.getAllTalents());
			autoincrementTalentRank(currentCircle, currentTalents.getDisciplinetalents());
			calculateTalents(namegivertalents, defaultOptionalTalents, disciplinenumber, currentCircle, minDisciplineCircle, durabilityTalents, currentTalents.getDisciplinetalents(), TalentsContainer.TalentKind.DIS);
			calculateTalents(namegivertalents, defaultOptionalTalents, disciplinenumber, currentCircle, minDisciplineCircle, durabilityTalents, currentTalents.getOptionaltalents(), TalentsContainer.TalentKind.OPT);
			calculateTalents(namegivertalents, defaultOptionalTalents, disciplinenumber, currentCircle, minDisciplineCircle, durabilityTalents, currentTalents.getFreetalents(), TalentsContainer.TalentKind.FRE);
			// Alle Namegiver Talente, die bis jetzt noch nicht enthalten waren,
			// werden nun den optionalen Talenten beigefügt.
			for( String t : namegivertalents.keySet() ) {
				TALENTType talent = new TALENTType();
				talent.setName(namegivertalents.get(t).getName());
				talent.getLIMITATION().add(namegivertalents.get(t).getLimitation());
				if( talent.getName().equals(PROPERTIES.getQuestorTalentName()) ) {
					talent.setCircle(5);
				} else {
					talent.setCircle(0);
				}
				capabilities.enforceCapabilityParams(talent);
				talent.setTEACHER(new TALENTTEACHERType());
				RANKType rank = new RANKType();
				ATTRIBUTENameType attribute = talent.getAttribute();
				if( attribute != null ) {
					calculateCapabilityRank(rank,characterAttributes.get(attribute));
				}
				talent.setRANK(rank);
				currentTalents.getOptionaltalents().add(talent);
			}
			namegivertalents.clear(); // Ist keine lokale Variable und Namensgebertalent sollen nur bei einer Disziplin einfügt werden
			for( String t : defaultOptionalTalents.keySet() ) {
				// Talente aus späteren Kreisen werden auch erst später eingefügt
				int circle = defaultOptionalTalents.get(t);
				if( circle > currentCircle ) continue;
				TALENTType talent = new TALENTType();
				talent.setName(t);
				talent.setCircle(circle);
				capabilities.enforceCapabilityParams(talent);
				talent.setTEACHER(new TALENTTEACHERType());
				RANKType rank = new RANKType();
				calculateCapabilityRank(rank,characterAttributes.get(talent.getAttribute()));
				talent.setRANK(rank);
				currentTalents.getOptionaltalents().add(talent);
			}
			DISCIPLINE disziplinProperties = PROPERTIES.getDisziplin(currentDiscipline.getName());
			if( disziplinProperties != null ) {
				// Wenn Durability-Talente gefunden wurden, berechnen aus dessen Rank
				// die Erhöhung von Todes- und Bewustlosigkeitsschwelle
				if( currentDiscipline.getCircle() >= disziplinProperties.getDURABILITY().getCircle() ) {
					death.setAdjustment(death.getAdjustment()+((disziplinProperties.getDURABILITY().getValue()+1)*currentDiscipline.getCircle()));
					unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(disziplinProperties.getDURABILITY().getValue()*currentDiscipline.getCircle()));
				}
				for( TALENTType durabilityTalent : durabilityTalents ) {
					DISCIPLINEDURABILITYType durability = disziplinProperties.getDURABILITY();
					int rank = durabilityTalent.getRANK().getRank()-durabilityTalent.getRANK().getRealignedrank();
					death.setAdjustment(death.getAdjustment()+(durability.getDeath()*rank));
					unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*rank));
					durabilityTalent.getLIMITATION().clear();
					durabilityTalent.getLIMITATION().add(durability.getDeath()+"/"+durability.getUnconsciousness());
				}
				String halfmagic=currentDiscipline.getHALFMAGIC();
				if( (halfmagic==null) || (halfmagic.isEmpty()) ) {
					currentDiscipline.setHALFMAGIC(disziplinProperties.getHALFMAGIC());
				}
				String karmaritual=currentDiscipline.getKARMARITUAL();
				if( (karmaritual==null) || (karmaritual.isEmpty()) ) {
					currentDiscipline.setKARMARITUAL(disziplinProperties.getKARMARITUAL());
				}
			}
			diciplineCircle.put(currentDiscipline.getName(), currentCircle);
			// Nur der höchtse Bonus wird gewertet.
			int currentKarmaStepBonus = getDisciplineKarmaStepBonus(currentDiscipline);
			if( currentKarmaStepBonus > maxKarmaStepBonus ) maxKarmaStepBonus = currentKarmaStepBonus;
			List<DISCIPLINEBONUSType> currentBonuses = currentDiscipline.getDISCIPLINEBONUS();
			currentBonuses.clear();
			currentBonuses.addAll(getDisciplineBonuses(currentDiscipline));
			// TALENT KNACKS
			for( TALENTType talent : currentTalents.getAllTalents() ) checkTalentKnacks(talent,disciplinenumber);
		}

		// ** ARMOR **
		// Zu erstmal alles entfernen was nicht eine Reale Rüstung ist:
		List<ARMORType> totalarmor = character.removeVirtualArmorFromNormalArmorList();
		// natural ARMOR bestimmen
		ARMORType namegiverArmor = namegiver.getARMOR();
		int namegiverPhysicalArmor = namegiverArmor.getPhysicalarmor();
		int namegiverMysticArmor = namegiverArmor.getMysticarmor();
		int namgiverArmorPenalty = namegiverArmor.getPenalty();
		if(namegiverPhysicalArmor>0) namegiverAbilities.add("physical armor +"+namegiverPhysicalArmor+" *");
		else if(namegiverPhysicalArmor<0) namegiverAbilities.add("physical armor "+namegiverPhysicalArmor+" *");
		if(namegiverMysticArmor>0) namegiverAbilities.add("mystic armor +"+namegiverMysticArmor+" *");
		else if(namegiverMysticArmor<0) namegiverAbilities.add("mystic armor "+namegiverMysticArmor+" *");
		if(namgiverArmorPenalty>0) namegiverAbilities.add("armor penalty +"+namgiverArmorPenalty+" *");
		else if(namgiverArmorPenalty<0) namegiverAbilities.add("armor penalty "+namgiverArmorPenalty+" *");
		ARMORType naturalArmor = new ARMORType();
		naturalArmor.setName(namegiverArmor.getName());
		naturalArmor.setMysticarmor(namegiverMysticArmor+berechneMysticArmor(characterAttributes.get(ATTRIBUTENameType.WIL).getCurrentvalue()));
		naturalArmor.setPhysicalarmor(namegiverPhysicalArmor);
		naturalArmor.setPenalty(namgiverArmorPenalty);
		naturalArmor.setUsed(namegiverArmor.getUsed());
		naturalArmor.setWeight(namegiverArmor.getWeight());
		naturalArmor.setVirtual(YesnoType.YES);
		// Natürliche Rüstung der Liste voranstellen
		totalarmor.add(0, naturalArmor);
		// Rüstung aus dem Dizipline Bonus
		totalarmor.add(getDisciplineArmor(diciplineCircle));
		// magischen Rüstung/Rüstungsschutz anhängen:
		totalarmor.addAll(character.getMagicArmor());
		// Bestimme nun den aktuellen Gesamtrüstungsschutz
		int mysticarmor=0;
		int physicalarmor=0;
		int mysticdefense=0;
		int physicaldefense=0;
		int protectionpenalty=0;
		for (ARMORType armor : totalarmor ) {
			if( armor.getUsed().equals(YesnoType.YES) ) {
				mysticarmor+=armor.getMysticarmor();
				physicalarmor+=armor.getPhysicalarmor();
				protectionpenalty+=armor.getPenalty();
				if( armor.getVirtual().equals(YesnoType.NO) ) {
					character.addBloodDamgeFrom(armor);
				}
				if( armor instanceof SHIELDType ) {
					SHIELDType shield = (SHIELDType) armor;
					DefenseAbility d = new DefenseAbility(shield.getDEFENSE());
					physicaldefense+=d.get(EffectlayerType.PHYSICAL);
					mysticdefense+=d.get(EffectlayerType.MYSTIC);
				}
			}
			if(armor.getKind().equals(ItemkindType.UNDEFINED)) armor.setKind(ItemkindType.ARMOR);
		}
		defenses.add(EffectlayerType.PHYSICAL,physicaldefense);
		defenses.add(EffectlayerType.MYSTIC,mysticdefense);
		PROTECTIONType protection = character.getProtection();
		protection.setMysticarmor(mysticarmor);
		protection.setPenalty(protectionpenalty);
		protection.setPhysicalarmor(physicalarmor);
		character.readjustInitiativeModifikator(-protectionpenalty,true);

		// ** KARMA STEP **
		KARMAType karma = character.getKarma();
		karma.setStep(4 + maxKarmaStepBonus); // mindestens d6
		karma.setDice(PROPERTIES.step2Dice(karma.getStep()));

		defenses.add(getDisciplineDefense(diciplineCircle));

		int[] recoverytestbonus = getDisciplineRecoveryTestBonus(diciplineCircle);
		recovery.setTestsperday(recovery.getTestsperday()+recoverytestbonus[0]);
		recovery.setStep(recovery.getStep()+recoverytestbonus[1]);
		recovery.setDice(PROPERTIES.step2Dice(recovery.getStep()));

		character.readjustInitiativeModifikator(getDisciplineInitiative(diciplineCircle),false);

		// ** SPELLS **
		// Bestimme die wieviele Zaubersprüche bei der Charactererschaffung kostenlos dazu kamen
		// und wieviel ein SpellAbility pro Kreis pro Disziplin kostenlos dazukamen.
		int freespellranks = attribute2StepAndDice(characterAttributes.get(ATTRIBUTENameType.PER).getBasevalue()).getStep();
		for( int sa : getDisciplineSpellAbility(diciplineCircle) ) freespellranks+=sa;
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			int usedSpellabilities=0;
			for( SPELLType spell : discipline.getSPELL() ) {
				updateSpell(spell);
				// Wenn ein Zauber duch Spellability gelernt wurde, dann kostet er keine LPs
				if( spell.getByspellability().equals(YesnoType.YES) ) {
					usedSpellabilities++;
					freespellranks-=spell.getCircle();
				} else if( OptionalRule_SpellLegendPointCost ) {
					// The cost of spells are equivalent to the cost of increasing a Novice Talent to a Rank equal to the Spell Circle
					int lpcost=PROPERTIES.getCharacteristics().getSpellLP(spell.getCircle());
					calculatedLP.addSpells(lpcost,"LPs for spell '"+spell.getName()+"' circle "+spell.getCircle());
				}
			}
			discipline.setUsedspellabilities(usedSpellabilities);
		}
		calculatedLP.setUsedSpellsStartRanks(-freespellranks);

		for( ITEMType item : character.getBloodCharmItem() ) {
			if( item.getUsed().equals(YesnoType.YES) && item.getVirtual().equals(YesnoType.NO) ) {
				character.addBloodDamgeFrom(item);
			}
		}
		for( ITEMType item : character.getItems() ) {
			if( item.getUsed().equals(YesnoType.YES) && item.getVirtual().equals(YesnoType.NO) ) {
				character.addBloodDamgeFrom(item);
			}
		}

		for( THREADITEMType item : character.getThreadItem() ) {
			// Falls es Fadengegenstände gibt die man mit Blutmagie noch erst aktivieren muss
			// prüfe, ob genutzt
			if( item.getUsed().equals(YesnoType.YES) && item.getVirtual().equals(YesnoType.NO) ) {
				character.addBloodDamgeFrom(item);
			}
			int rank = item.getWeaventhreadrank();
			List<THREADRANKType> threadranks = item.getTHREADRANK();
			if( rank > threadranks.size() ) {
				rank=threadranks.size();
				item.setWeaventhreadrank(rank);
			}
			// If no thread is weaven to the this thread item, skip the rest
			if( rank < 1 ) continue;
			THREADRANKType threadrank = threadranks.get(rank-1);
			while( (threadrank==null) && (rank>1) ) {
				// Wenn der Fadenrang nicht definiert ist, obwohl der Rang größer 1 ist, wähle den Fadenrang einen Rang kleiner.
				if( threadrank == null ) {
					errorout.println("Undefined Threadrank for "+item.getName()+" for rank "+rank );
				}
				rank--;
				threadrank = threadranks.get(rank-1);
			}
			if( threadrank == null ) {
				errorout.println("No Threadranks for "+item.getName()+" for rank "+item.getWeaventhreadrank()+" or less at all." );
				continue;
			}
			defenses.add(threadrank.getDEFENSE());
			if( ! allDisciplines.isEmpty() ) for(TALENTABILITYType itemtalent : threadrank.getTALENT() ) {
				String limitation = itemtalent.getLimitation();
				String talentname = itemtalent.getName();
				int itembonus = itemtalent.getBonus();
				boolean found=false;
				for( DISCIPLINEType discipline : allDisciplines ) {
					DISCIPLINE disziplinProperties = PROPERTIES.getDisziplin(discipline.getName());
					for( TALENTType talent : (new TalentsContainer(discipline).getAllTalents()) ) {
						boolean sameLimitations = false;
						if( limitation.isEmpty() ) sameLimitations = true;
						else if( talent.getLIMITATION().size()>0 ) {
							sameLimitations = talent.getLIMITATION().get(0).equals(limitation);
						}
						if( talent.getName().equals(talentname) && sameLimitations ) {
							found=true;
							RANKType talentrank = talent.getRANK();
							talentrank.setBonus(talentrank.getBonus()+itembonus);
							calculateCapabilityRank(talentrank,characterAttributes.get(talent.getAttribute()));
							if( talentname.equals(durabilityTalentName) && (disziplinProperties!=null) ) {
								// Wenn Durability-Talente gefunden wurden, berechnen die Erhöhung von Todes- und Bewustlosigkeitsschwelle
								DISCIPLINEDURABILITYType durability = disziplinProperties.getDURABILITY();
								death.setAdjustment(death.getAdjustment()+(durability.getDeath()*itembonus));
								unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*itembonus));
							}
							break;
						}
						if( found ) break;
					}
					if( found ) break;
				}
				if( !found ) {
					DISCIPLINEType discipline = allDisciplines.get(0);
					DISCIPLINE disziplinProperties = PROPERTIES.getDisziplin(discipline.getName());
					if( limitation.isEmpty() ) limitation ="(#)";
					else limitation += " (#)";
					TALENTType bonusTalent = new TALENTType();
					bonusTalent.setName(itemtalent.getName());
					bonusTalent.getLIMITATION().add(limitation);
					bonusTalent.setCircle(0);
					capabilities.enforceCapabilityParams(bonusTalent);
					RANKType bonusrank = new RANKType();
					bonusrank.setRank(0);
					bonusrank.setBonus(itembonus);
					calculateCapabilityRank(bonusrank,characterAttributes.get(bonusTalent.getAttribute()));
					bonusTalent.setRANK(bonusrank);
					TALENTTEACHERType teacher = new TALENTTEACHERType();
					teacher.setByversatility(YesnoType.NO);
					teacher.setTalentcircle(rank);
					teacher.setTeachercircle(rank);
					teacher.setName(item.getName());
					bonusTalent.setTEACHER(teacher);
					if( talentname.equals(durabilityTalentName) && (disziplinProperties!=null) ) {
						// Wenn Durability-Talente gefunden wurden, berechnen die Erhöhung von Todes- und Bewustlosigkeitsschwelle
						DISCIPLINEDURABILITYType durability = disziplinProperties.getDURABILITY();
						death.setAdjustment(death.getAdjustment()+(durability.getDeath()*itembonus));
						unconsciousness.setAdjustment(unconsciousness.getAdjustment()+(durability.getUnconsciousness()*itembonus));
					}
					discipline.getOPTIONALTALENT().add(bonusTalent);
				}
			}
			for(DISZIPINABILITYType iteminitiative : threadrank.getINITIATIVE() ) {
				character.readjustInitiativeModifikator(iteminitiative.getCount(),false);
			}
			for( String spellname : threadrank.getSPELL() ) {
				SPELLType spell = new SPELLType();
				spell.setName(spellname);
				updateSpell(spell);
				character.addOpenSpell(spell);
			}
			for( DISZIPINABILITYType recov : threadrank.getRECOVERYTEST() ) {
				recovery.setTestsperday(recovery.getTestsperday()+recov.getCount());
			}
			for( DISZIPINABILITYType k : threadrank.getKARMASTEP() ) {
				karma.setStep(karma.getStep() + k.getCount());
				karma.setDice(PROPERTIES.step2Dice(karma.getStep()));
			}
			for( DISZIPINABILITYType k : threadrank.getMAXKARMA() ) {
				karma.setMax(karma.getMax() + k.getCount());
				karma.setMaxmodificator(karma.getMaxmodificator() + k.getCount());
			}
			WOUNDType threadwound = threadrank.getWOUND();
			if( threadwound != null ) {
				wound.setBlood(wound.getBlood()+threadwound.getBlood());
				wound.setNormal(wound.getNormal()+threadwound.getNormal());
				wound.setThreshold(wound.getThreshold()+threadwound.getThreshold());
				totalwounds=wound.getNormal()+wound.getBlood();
				if( totalwounds>1 ) wound.setPenalties(totalwounds-1+threadwound.getPenalties());
				else wound.setPenalties(threadwound.getPenalties());
			}
			// TODO: other effects of MagicItems
			// TODO:List<TALENTType> optTalents = allTalents.get(disciplinenumber).getOPTIONALTALENT();
		}

		if( OptionalRule_EnduringArmorByStrength ) {
			int currentarmorpenalty = character.getInitiative().getArmorpenalty();
			if( currentarmorpenalty>0 ) {
				float strength = characterAttributes.get(ATTRIBUTENameType.STR).getCurrentvalue();
				strength *= namegiver.getEnduringarmorfactor();
				// neuer Stärkewert aufrunden und in der Tabelle für Mystische Armor nachschlagen
				int relief=berechneMysticArmor(Double.valueOf(Math.ceil(strength)).intValue());
				// Dies stellt nun den Stärke bassierenden Modifikator dar, mit dem der Charakter besser mit einer Rüstung zurecht kommt
				// Es darf keine negative Rüstungsbehinderung geben
				if( currentarmorpenalty < relief ) relief = currentarmorpenalty;
				character.readjustInitiativeModifikator(relief,true);
			}
		}

		// Veränderungen am death/unconsciousness adjustment sollen beachtet werden
		death.setValue(death.getBase()+death.getAdjustment());
		unconsciousness.setValue(unconsciousness.getBase()+unconsciousness.getAdjustment());

		character.setAbilities(CharacterContainer.join(namegiverAbilities));

		calculatedLP.addThreadItems(character.getThreadItem());
		calculatedLP.calclulateTotal();

		if( OptionalRule_AutoInsertLegendPointSpent ) character.addLegendPointsSpent(oldcalculatedLP.getCalculatedLP());
		if( OptionalRule_KeepLegendPointSync ) {
			character.clearSpentLegendPoints();
			character.getLegendPoints().getLEGENDPOINTS().addAll(character.getCalculatedLegendpoints().getCALCULATIONLP());
		}
		character.calculateLegendPointsAndStatus();
		character.calculateDevotionPoints();

		return character.getEDCHARACTER();
	}

	public static void calculateSkills(CharacterContainer character, Map<ATTRIBUTENameType,ATTRIBUTEType> characterAttributes, List<String> namegiverAbilities, CalculatedLPContainer calculatedLP) {
		// StartRänge und MindestRänge für die SprachenSkills bestimmen
		int[] startSpeakReadWrite = character.getDefaultLanguages().getCountOfSpeakReadWrite(LearnedbyType.SKILL);
		int[] currentSpeakReadWrite = character.getLanguages().getCountOfSpeakReadWrite(LearnedbyType.SKILL);
		List<SKILLType> speakSkills = character.getSpeakSkills();
		List<SKILLType> readwriteSkills = character.getReadWriteSkills();
		if( ! speakSkills.isEmpty() ) {
			RANKType rank = speakSkills.get(0).getRANK();
			if( rank.getStartrank() < startSpeakReadWrite[0] ) rank.setStartrank(startSpeakReadWrite[0]);
			if( rank.getRank() < currentSpeakReadWrite[0] ) rank.setRank(currentSpeakReadWrite[0]);
		}
		if( ! readwriteSkills.isEmpty() ) {
			RANKType rank = readwriteSkills.get(0).getRANK();
			if( rank.getStartrank() < startSpeakReadWrite[1] ) rank.setStartrank(startSpeakReadWrite[1]);
			if( rank.getRank() < currentSpeakReadWrite[1] ) rank.setRank(currentSpeakReadWrite[1]);
		}

		int skillsStartranks=calculatedLP.getUsedSkillsStartRanks();
		// Entferne alle Skills mit Rang 0, wenn voranden.
		character.removeEmptySkills();
		// Sollte die Skillliste leer sein, füge die Startskills ein
		List<SKILLType> skills = character.getSkills();
		if( skills.isEmpty() ) {
			for(SKILLType skilltemplate : PROPERTIES.getStartingSkills() ) {
				SKILLType skill = new SKILLType();
				skill.setName(skilltemplate.getName());
				skill.getLIMITATION().addAll(skilltemplate.getLIMITATION());
				RANKType rank = new RANKType();
				rank.setRank(skilltemplate.getRANK().getRank());
				rank.setStartrank(skilltemplate.getRANK().getStartrank());
				skill.setRANK(rank);
				character.addSkill(skill);
			}
		}
		// Die DefaultSkills werden ermittelt
		List<String> namgiverNotdefaultskills = character.getRace().getNOTDEFAULTSKILL();
		for( String skill : namgiverNotdefaultskills ) namegiverAbilities.add("'"+skill+"' is not a default skill");
		List<CAPABILITYType> defaultSkills = capabilities.getDefaultSkills(namgiverNotdefaultskills);

		// Finde heraus, ob für die Erstdisziplin Skills definiert sind, die wie Novice Talene gelernt werden.
		List<String> easyskills;
		if( !character.getDisciplines().isEmpty() ) {
			DISCIPLINE disziplinProperties = ApplicationProperties.create().getDisziplin(character.getDisciplines().get(0).getName());
			easyskills = disziplinProperties.getEASYSKILL();
		} else {
			easyskills = new ArrayList<String>();
		}

		// Berechne nun alle LP und Ränge aller vorhandenen Skills und entferne diese aus der Liste der Defaultskills, dabereits vorhanden.
		for( SKILLType skill : skills ) {
			RANKType rank = skill.getRANK();
			if( rank == null ) continue;
			if( rank.getRank() < 1) continue;
			int startrank = rank.getStartrank();
			skillsStartranks+=startrank;
			int lpcostfull;
			int lpcoststart;
			String skillname = skill.getName();
			if( easyskills.contains(skillname) ) {
				lpcostfull= PROPERTIES.getCharacteristics().getTalentRankTotalLP(1,1,rank.getRank());
				lpcoststart= PROPERTIES.getCharacteristics().getTalentRankTotalLP(1,1,startrank);
			} else {
				lpcostfull= PROPERTIES.getCharacteristics().getSkillRankTotalLP(rank.getRank());
				lpcoststart= PROPERTIES.getCharacteristics().getSkillRankTotalLP(startrank);
			}
			rank.setLpcost(lpcostfull-lpcoststart);
			rank.setBonus(0);
			if( skill.getLIMITATION().size()<1 ) {
				calculatedLP.addSkills(rank.getLpcost(),"LP cost for Skill '"+skillname+"'");
			} else {
				calculatedLP.addSkills(rank.getLpcost(),"LP cost for Skill '"+skillname+" ("+CharacterContainer.join(skill.getLIMITATION())+")'");
			}
			capabilities.enforceCapabilityParams(skill);
			if( skill.getAttribute() != null ) {
				calculateCapabilityRank(rank,characterAttributes.get(skill.getAttribute()));
			}
			removeIfContains(defaultSkills,skillname);
		}
		calculatedLP.setUsedSkillsStartRanks(skillsStartranks);

		// Wenn gewünscht dann füge auch die bis jetzt noch nicht vorhanden DefaultSkills ein
		if( OptionalRule.getSHOWDEFAULTSKILLS().getUsed().equals(YesnoType.YES) ) {
			for( CAPABILITYType defaultSkill : defaultSkills ) {
				List<String> limitations = defaultSkill.getLIMITATION();
				if( limitations.size()==0 ) limitations.add("");
				for( String limitation : limitations ) {
					SKILLType skill = new SKILLType();
					RANKType rank = new RANKType();
					skill.setRANK(rank);
					skill.setName(defaultSkill.getName());
					if( !limitation.isEmpty() ) skill.getLIMITATION().add(limitation);
					capabilities.enforceCapabilityParams(skill);
					if( skill.getAttribute() != null ) {
						calculateCapabilityRank(rank,characterAttributes.get(skill.getAttribute()));
					}
					character.addSkill(skill);
				}
			}
		}
		// Wenn die optionale Regel "AligningTalensAndSkills" ausgewält wurde, dann Ordner die Skills ihren Talenten zu.
		if( OptionalRule_AligningTalentsAndSkills ) character.updateAlignedSkills();
	}

	private void updateSpell(SPELLType spell) {
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
			spell.setElement(spelldef.getElement());
		}
	}

	private void checkTalentKnacks(TALENTType talent, int disciplinenumber) {
		String talentname = talent.getName();
		String limitation = "";
		if( talent.getLIMITATION().size()>0 ) limitation = talent.getLIMITATION().get(0);
		for( KNACKType knack : talent.getKNACK() ) {
			boolean knackstatsupdated=false;
			for( KNACKDEFINITIONType k : PROPERTIES.getKnacksByName(knack.getName()) ) {
				for( KNACKBASECAPABILITYType base : k.getBASE() ) {
					String lim = base.getLimitation();
					boolean nolim = lim.isEmpty();
					if( base.getType().equals(CapabilitytypeType.TALENT) && base.getName().equals(talentname) && (nolim||lim.equals(limitation))) {
						knack.setAction(k.getAction());
						knack.setAttribute(k.getAttribute());
						knack.setBlood(k.getBlood());
						knack.setBookref(k.getBookref());
						knack.setMinrank(base.getMinrank());
						knack.setStrain(k.getStrain());
						knackstatsupdated=true;
					}
				}
			}
			if( ! knackstatsupdated ) {
				errorout.println("The knack '"+knack.getName()+"' learned for the talent '"+talentname+" ("+limitation+")', but not found in this combination. Will not enforce knack default values!");
			}
		}
	}

	private static void ensureRankAndTeacher(List<TALENTType> talents) {
		for( TALENTType talent : talents ) {
			if( talent.getRANK() == null ) talent.setRANK(new RANKType());
			if( talent.getTEACHER() == null ) talent.setTEACHER(new TALENTTEACHERType());
		}
	}

	private void calculateTalents(Map<String, TALENTABILITYType> namegivertalents, Map<String, Integer> defaultOptionalTalents, int disciplinenumber, int disciplinecircle, int minDisciplineCircle, List<TALENTType> durabilityTalents, List<TALENTType> talents, TalentsContainer.TalentKind talentKind) {
		int startranks=calculatedLP.getUsedTalentsStartRanks();
		for( TALENTType talent : talents ) {
			TALENTTEACHERType teacher = talent.getTEACHER();
			RANKType rank = talent.getRANK();
			if( rank.getRank() < rank.getRealignedrank() ) rank.setRank(rank.getRealignedrank());
			rank.setBonus(talent.getBonus());
			if( rank.getRank() < 1 ) {
				// Wenn kein Rang exisitert, dann auch keine Rangvergangenheit.
				talent.getRANKHISTORY().clear();
				// Sollte ein Skill zu diesem Tallent Realigned sein, dann löse diese Verbindung bei Rank 0
				SKILLType skill = talent.getALIGNEDSKILL();
				if( skill != null ) {
					talent.setALIGNEDSKILL(null);
					character.addSkill(skill);
				}
			} else if( (disciplinenumber>1) && (minDisciplineCircle<5) ) {
				// Wenn Talente von weiteren (nicht die erste) Disciplinen gelernt wurden,
				// obwohl der kleinste Disziplinkreis nocht nicht 5 ist, muss dies erfasst werden.
				List<RANKHISTORYType> rankHistories = talent.getRANKHISTORY();
				RANKHISTORYType rankhistory;
				if( rankHistories.isEmpty() ) {
					// Wenn noch garkeine Rang-Vergangenheit erfasst wurde, füge ein erstes Element ein.
					rankhistory = new RANKHISTORYType();
					rankhistory.setMincircle(minDisciplineCircle);
					rankHistories.add(rankhistory);
				} else {
					// Es exisitert bereits eine Rang-Vergangenheit, hole letztes Element
					rankhistory = rankHistories.get(rankHistories.size()-1);
					if( (rankhistory.getMincircle()!=minDisciplineCircle) && (rankhistory.getRank()!=rank.getRank()) ) {
						// Sollte das letzte Element nicht dem aktuellen Disziplinkreis entsprechen und der Talentrang
						// sich verändert haben, dann erstelle ein neues Element.
						rankhistory = new RANKHISTORYType();
						rankhistory.setMincircle(minDisciplineCircle);
						rankHistories.add(rankhistory);
					}
				}
				rankhistory.setRank(rank.getRank());
			}
			// Prüfe ob extra LP Kosten entstanden sind, da eine neue Disziplin "zu früh" erlernt wurde.
			int newDisciplineTalentCost=0;
			List<CHARACTERISTICSCOST> newDisciplineTalentCosts = PROPERTIES.getCharacteristics().getNewDisciplineTalentCost(disciplinenumber);
			// Prüfe ob wir ein Kostentabelle haben
			if( newDisciplineTalentCosts != null ) {
				List<RANKHISTORYType> rankhistory = talent.getRANKHISTORY();
				// Prüfe ob die RangHistory nicht leer ist
				if( ! rankhistory.isEmpty() ) {
					// Erstes Element enthält den kleines Disziplinkreis als der erste Ranges dieses Talents gelernt wurde
					int mincircle = rankhistory.get(0).getMincircle()-1;
					if( mincircle < newDisciplineTalentCosts.size() ) {
						newDisciplineTalentCost = newDisciplineTalentCosts.get(mincircle).getCost();
					}
				}
			}
			// Wenn zu dem Talent ein Skill aligned wurde, dann kann es dazu keinen Startrank geben.
			if( talent.getALIGNEDSKILL() != null ) {
				rank.setStartrank(0);
				errorout.println("The talent '"+talent.getName()+"' has an aligned skill and can't have any start rank. Clear start rank.");
			}
			// Der Startrank bassiert entweder von dem gesetzen "Startrank" (bei Charaktererschaffung) oder auf dem Rank ab Realigned
			int startrank=rank.getStartrank();
			if( startrank < rank.getRealignedrank() ) startrank = rank.getRealignedrank();
			if( talentKind.equals(TalentsContainer.TalentKind.FRE) ) {
				startrank=0;
				rank.setStartrank(0);
				rank.setRank(disciplinecircle);
			}
			// Sind Skills bereits Realigned dann beachte es auch
			// Unabhängig von der Optionalen Regel. Die bestimmt nur ob Skills Realigned werden.
			if( rank.getRank()>0 ) {
				SKILLType skill = talent.getALIGNEDSKILL();
				if( skill != null ) {
					RANKType skillRank = skill.getRANK();
					if( skillRank != null ) {
						int skillRealignedRank = skillRank.getRank();
						// Je höherstufig das Talent ist desto weniger zählen die Skillränge
						if( talent.getCircle() > 4 ) skillRealignedRank--;
						if( talent.getCircle() > 8 ) skillRealignedRank--;
						if( talent.getCircle() > 12 ) skillRealignedRank--;
						// Man zahlt LP für Rang 0 auf 1 bekomt aber den Talentrang auf "skillRealignedRank"
						newDisciplineTalentCost+=PROPERTIES.getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),0,1);
						if( skillRealignedRank > startrank ) startrank=skillRealignedRank;
					}
				}
			}
			if( talentKind.equals(TalentsContainer.TalentKind.FRE) ) {
				rank.setLpcost(0);
			} else {
				rank.setLpcost(newDisciplineTalentCost+PROPERTIES.getCharacteristics().getTalentRankTotalLP(disciplinenumber,talent.getCircle(),startrank,rank.getRank()));
				if( talentKind.equals(TalentsContainer.TalentKind.DIS) ) {
					calculatedLP.addDisciplinetalents(rank.getLpcost(),"LPs for discipline talent '"+talent.getName()+"'");
				} else {
					calculatedLP.addOptionaltalents(rank.getLpcost(),"LPs for optional talent '"+talent.getName()+"'");
				}
			}
			startranks+=rank.getStartrank();
			ATTRIBUTENameType attr = talent.getAttribute();
			if( attr != null ) calculateCapabilityRank(rank,characterAttributes.get(attr));
			String talentname = talent.getName();
			if( talentname.equals(durabilityTalentName)) durabilityTalents.add(talent);
			calculateKnacks(disciplinenumber, talent, talentKind.equals(TalentsContainer.TalentKind.DIS));
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
		calculatedLP.setUsedTalentsStartRanks(startranks);
	}

	private static void checkStartrank(int disciplinenumber, List<TALENTType> talents) {
		if( disciplinenumber == 1 ) {
			for( TALENTType talent : talents ) {
				RANKType rank = talent.getRANK();
				// Talente aus höheren Kreisen können keine Startranks haben, da Startranks nur bei der Charaktererschaffung vergeben werden.
				if( (talent.getCircle()>1) && (rank.getStartrank()>0) ) {
					rank.setStartrank(0);
					errorout.println("The talent '"+talent.getName()+"' is from circle "+talent.getCircle()+". Only talents of circle 1 of the first discipline can have start ranks. The talent start rank was cleared fit this sittuation.");
				}
				if( rank.getRank() < rank.getStartrank() ) rank.setRank(rank.getStartrank());
			}
		} else {
			// Nur in der Erstdisziplin kann ein Startrang existieren.
			for( TALENTType talent : talents ) {
				RANKType rank = talent.getRANK();
				if( rank.getStartrank()!=0 ) {
					rank.setStartrank(0);
					errorout.println("The talent '"+talent.getName()+"' is from "+disciplinenumber+". discipline and can't have any start rank. Clear start rank.");
				}
			}
		}
	}

	// Die Funktion geht davon aus das alle Talente ein RANK Object haben.
	private static void autoincrementTalentRank(int disciplinecircle, List<TALENTType> talents ) {
		if( ! OptionalRule_autoincrementDisciplinetalents ) return;
		for( TALENTType talent : talents ) {
			RANKType rank = talent.getRANK();
			// Wenn der Rank bereits größer als der Disciplin Kreis ist. Dann tue nichts.
			if( rank.getRank() > disciplinecircle ) continue;
			int talentcircle = talent.getCircle();
			if( talentcircle < disciplinecircle ) {
				rank.setRank(disciplinecircle);
			} else if (talentcircle == disciplinecircle) {
				if( rank.getRank() < 1) rank.setRank(1);
			} else {
				rank.setRank(0);
				errorout.println("The talent '"+talent.getName()+"' is from circle "+talentcircle+", but the circe of the discipline is only "+disciplinecircle+". The talent rank was cleared to fit this sittuation.");
			}
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

	private static int calculateKarma(KARMAType karma, TALENTType karmaritualTalent, int karmaModifier, int karmaMaxBonus) {
		if( (karmaMaxBonus < 0) && OptionalRule_NoNegativeKarmaMax ) {
			errorout.println("The to many spent attribute buy points will not result in a negative karma maximum.");
			karmaMaxBonus=0;
		}
		karma.setMaxmodificator(karmaMaxBonus);
		if( karmaritualTalent == null ) {
			errorout.println("No karmaritual talent found, skipping maximal karma calculation.");
		} else {
			int rank = ( karmaritualTalent.getRANK() == null ) ? 0 : karmaritualTalent.getRANK().getRank();
			karma.setMax( karmaMaxBonus + (karmaModifier * rank) );
		}
		int[] k = CharacterContainer.calculateAccounting(karma.getKARMAPOINTS());
		karma.setCurrent(karmaModifier+k[0]-k[1]);
		if(PROPERTIES.getRulesetLanguage().getRulesetversion().equals(RulesetversionType.ED_3)) {
			return 10*k[0]; // KarmaLP
		} else {
			// in ED4 sind Karma punkte kosten los. Damit benötigt man keine Buchhaltung der Karmapunkte.
			karma.getKARMAPOINTS().clear();
			// Wir gehen einfachmal davon aus, dass der Charakter täglich sein Karmaritual macht.
			karma.setCurrent(karma.getMax());
			return 0;
		}
	}

	private void calculateKnacks(int disciplinenumber, TALENTType talent, boolean disTalents) {
		int trank=talent.getRANK().getRank();
		for( KNACKType knack : talent.getKNACK() ) {
			int krank = knack.getMinrank();
			if( character.getRulesetversion().equals(RulesetversionType.ED_3) && disTalents ) krank -= 2;
			if( krank > trank ) {
				errorout.println("The rank of the talent '"+talent.getName()+"' is lower than the minimal rank for the kack '"+knack.getName()+"': "
						+trank+" vs. "+krank);
			}
			int lp = PROPERTIES.getCharacteristics().getTalentRankLPIncreaseTable(disciplinenumber,talent.getCircle()).get(krank).getCost();
			calculatedLP.addKnacks(lp,"LPs for talent knack '"+knack.getName()+"' of talent '"+talent.getName()+"'");
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

	private static int[] getDisciplineRecoveryTestBonus(Map<String,Integer> diciplineCircle) {
		int[] result = {0,0};
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			if( d == null ) continue;
			int recoverytestperday = 0;
			int recoveryteststep = 0;
			int circlenr=0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( DISZIPINABILITYType r : circle.getRECOVERYTESTPERDAY() ) {
					recoverytestperday += r.getCount();
				}
				for( DISZIPINABILITYType r : circle.getRECOVERYTESTSTEP() ) {
					recoveryteststep += r.getCount();
				}
			}
			if( recoverytestperday > result[0] ) result[0]=recoverytestperday;
			if( recoveryteststep > result[1] ) result[1]=recoveryteststep;
		}
		return result;
	}

	private static int getDisciplineInitiative(Map<String,Integer> diciplineCircle) {
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
	private static DefenseAbility getDisciplineDefense(Map<String,Integer> diciplineCircle) {
		DefenseAbility result = new DefenseAbility();
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			if( d == null ) continue;
			DefenseAbility tmp = new DefenseAbility();
			int circlenr = 0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( DEFENSEABILITYType defense : circle.getDEFENSE() ) {
					tmp.add(defense.getKind(),defense.getBonus());
				}
			}
			for( EffectlayerType kind : EffectlayerType.values() ) {
				int diff=tmp.get(kind)-result.get(kind);
				if(diff>0) result.add(kind, diff);
			}
		}
		return result;
	}

	// Der Armor Bonus wird nicht über alle Disziplinen addiert, sondern
	// der Character erhält von den Disziplinen nur den jeweils höchsten ArmorBonus
	private static ARMORType getDisciplineArmor(Map<String,Integer> diciplineCircle) {
		ARMORType result = new ARMORType();
		result.setPhysicalarmor(0);
		result.setMysticarmor(0);
		result.setVirtual(YesnoType.YES);
		result.setUsed(YesnoType.YES);
		result.setName("Dicipline Bonus");
		for( String discipline : diciplineCircle.keySet() ) {
			DISCIPLINE d = ApplicationProperties.create().getDisziplin(discipline);
			if( d == null ) continue;
			ARMORType tmp = new ARMORType();
			tmp.setPhysicalarmor(0);
			tmp.setMysticarmor(0);
			int circlenr = 0;
			for( DISCIPLINECIRCLEType circle : d.getCIRCLE() ) {
				circlenr++;
				if( circlenr > diciplineCircle.get(discipline) ) break;
				for( ARMORType armor : circle.getARMOR() ) {
					tmp.setPhysicalarmor(tmp.getPhysicalarmor()+armor.getPhysicalarmor());
					tmp.setMysticarmor(tmp.getMysticarmor()+armor.getMysticarmor());
				}
			}
			if( tmp.getPhysicalarmor() > result.getPhysicalarmor() ) {
				result.setPhysicalarmor(tmp.getPhysicalarmor());
			}
			if( tmp.getMysticarmor() > result.getMysticarmor() ) {
				result.setMysticarmor(tmp.getMysticarmor());
			}
		}
		return result;
	}

	private static List<Integer> getDisciplineSpellAbility(Map<String,Integer> diciplineCircle) {
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
		Map<Integer,Integer> attributecost = ApplicationProperties.create().getCharacteristics().getATTRIBUTECOST();
		return attributecost.get(modifier);
	}

	public static CHARACTERISTICSHEALTHRATING bestimmeHealth(int value) {
		Map<Integer,CHARACTERISTICSHEALTHRATING> healthrating = ApplicationProperties.create().getCharacteristics().getHEALTHRATING();
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

	public static void calculateCapabilityRank(RANKType talentRank, ATTRIBUTEType attr) {
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
		for(String skill : disziplinProperties.getEASYSKILL() ) {
			DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
			bonus.setCircle(1);
			bonus.setBonus(PROPERTIES.getTranslationText("disziplineasyskill1")+" '"+skill+"'-"+PROPERTIES.getTranslationText("disziplineasyskill2"));
			bonuses.add(bonus);
		}
		for(DISCIPLINECIRCLEType circle : disziplinProperties.getCIRCLE()) {
			circlenr++;
			if( circlenr > discipline.getCircle() ) break;
			for( KARMAABILITYType karma : circle.getKARMA() ) {
				DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
				bonus.setCircle(circlenr);
				bonus.setBonus(PROPERTIES.getTranslationText("canspendkarmafor")+" "+karma.getSpend());
				bonuses.add(bonus);
			}
			for( String ability : circle.getABILITY() ) {
				DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
				bonus.setCircle(circlenr);
				bonus.setBonus(PROPERTIES.getTranslationText("ability")+": "+ability);
				bonuses.add(bonus);
			}
		}
		return bonuses;
	}

	public PrintStream getErrorout() {
		return errorout;
	}

	public static void setErrorout(PrintStream stream) {
		errorout = stream;
	}
}
