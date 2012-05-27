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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECharacteristics;
import de.earthdawn.data.*;
import de.earthdawn.event.CharChangeRefresh;
import de.earthdawn.namegenerator.NameGenerator;

public class CharacterContainer extends CharChangeRefresh {
	private EDCHARACTER character = null;
	private static Random rand = new Random();
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final ECECharacteristics PROPERTIES_Characteristics= PROPERTIES.getCharacteristics();
	public static final ATTRIBUTENameType OptionalRule_AttributeBasedMovement=PROPERTIES.getOptionalRules().getATTRIBUTEBASEDMOVEMENT().getAttribute();
	public static final String threadWeavingName = PROPERTIES.getThreadWeavingName();
	public static final String durabilityName = PROPERTIES.getDurabilityName();
	public static final List<String> speakSkillName = PROPERTIES.getLanguageSkillSpeakName();
	public static final List<String> readwriteSkillName = PROPERTIES.getLanguageSkillReadWriteName();
	public static final String DATEFORMAT = PROPERTIES.getOptionalRules().getDATEFORMAT();
	public static final List<String> languageSkillSpeakNames = PROPERTIES.getLanguageSkillSpeakName();
	public static final List<String> languageSkillReadWriteNames = PROPERTIES.getLanguageSkillReadWriteName();

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public CharacterContainer( EDCHARACTER c) {
		character = c;
	}

	public void setEDCHARACTER(EDCHARACTER c) {
		character = c;
	}

	public EDCHARACTER getEDCHARACTER() {
		return character;
	}

	public String getName() {
		return character.getName();
	}

	public String setRandomName() {
		final APPEARANCEType appearance = getAppearance();
		final String race = appearance.getRace();
		final GenderType gender = appearance.getGender();
		NameGenerator namegenerator = new NameGenerator(PROPERTIES.getRandomNamesByRaces());
		String name = namegenerator.generateName(race,gender);
		//String name = namegenerator.generateName2(race,gender);
		//String name = namegenerator.generateName3(race,gender);
		character.setName(name);
		return name;
	}

	public String getPlayer() {
		String player = character.getPlayer();
		if( player == null ) return "";
		return player;
	}

	public APPEARANCEType getAppearance() {
		APPEARANCEType appearance = character.getAPPEARANCE();
		if( appearance != null ) return appearance;
		// If not found: create
		appearance = new APPEARANCEType();
		appearance.setRace("Human");
		appearance.setOrigin("Barsaive");
		appearance.setGender(GenderType.MALE);
		appearance.setEyes("brown");
		appearance.setAge(20);
		appearance.setHair("black");
		appearance.setHeight(5.5f);
		appearance.setSkin("caucasian");
		appearance.setWeight(176);
		character.setAPPEARANCE(appearance);
		return appearance;
	}

	public HashMap<String, ATTRIBUTEType> getAttributes() {
		List<ATTRIBUTEType> attributelist = character.getATTRIBUTE();
		HashMap<String,ATTRIBUTEType> attributes = new HashMap<String,ATTRIBUTEType>();
		for (ATTRIBUTEType attribute : attributelist ) {
			attributes.put(attribute.getName().value(), attribute);
		}
		for( ATTRIBUTENameType name : ATTRIBUTENameType.values() ) {
			// Wenn das Attribut bereits exisitert, kein neues Setzen
			if( attributes.containsKey(name.value())) continue;
			// Das "Attribut" na soll nicht angefügt werden
			if( name.equals(ATTRIBUTENameType.NA) ) continue;
			ATTRIBUTEType attribute = new ATTRIBUTEType();
			attribute.setName(name);
			attributelist.add(attribute);
			attributes.put(attribute.getName().value(), attribute);
		}
		return attributes;
	}

	public int getAttributesCost() {
		int result = 0;
		for (ATTRIBUTEType attribute : character.getATTRIBUTE() ) {
			// Das "Attribut" na soll nicht beachtet werden
			if( attribute.equals(ATTRIBUTENameType.NA) ) continue;
			result += attribute.getCost();
		}
		return result;
	}

	public DEFENSEType getDefence() {
		DEFENSEType defense = character.getDEFENSE();
		if( defense == null ) {
			defense = new DEFENSEType();
			character.setDEFENSE(defense);
		}
		return defense;
	}

	public CALCULATEDLEGENDPOINTSType getCalculatedLegendpoints() {
		CALCULATEDLEGENDPOINTSType calculatedLegendpoints = character.getCALCULATEDLEGENDPOINTS();
		if( calculatedLegendpoints == null ) {
			calculatedLegendpoints = new CALCULATEDLEGENDPOINTSType();
			character.setCALCULATEDLEGENDPOINTS(calculatedLegendpoints);
		}
		return calculatedLegendpoints;
	}

	public void addLegendPointsSpent(CALCULATEDLEGENDPOINTSType oldLP) {
		CALCULATEDLEGENDPOINTSType calculatedLP = getCalculatedLegendpoints();
		List<ACCOUNTINGType> legendpoints = getLegendPoints().getLEGENDPOINTS();
		String currentDateTime=getCurrentDateTime();
		String comment = "-LP spent (automaticly added by ECE)";
		int diff=0;

		diff=calculatedLP.getAttributes()-oldLP.getAttributes();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Attribute"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getDisciplinetalents()-oldLP.getDisciplinetalents();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Disciplinetalents"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getKarma()-oldLP.getKarma();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Karma"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getKnacks()-oldLP.getKnacks();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Knacks"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getMagicitems()-oldLP.getMagicitems();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Magicitems"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getOptionaltalents()-oldLP.getOptionaltalents();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Optionaltalents"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getSkills()-oldLP.getSkills();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Skills"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
		diff=calculatedLP.getSpells()-oldLP.getSpells();
		if( diff>0 ) {
			ACCOUNTINGType entry = new ACCOUNTINGType();
			entry.setComment("Spells"+comment);
			entry.setType(PlusminusType.MINUS);
			entry.setValue(diff);
			entry.setWhen(currentDateTime);
			legendpoints.add(entry);
		}
	}

	public void calculateLegendPointsAndStatus() {
		EXPERIENCEType legendpoints = getLegendPoints();
		List<Integer> lp = CharacterContainer.calculateAccounting(legendpoints.getLEGENDPOINTS());
		legendpoints.setCurrentlegendpoints(lp.get(0)-lp.get(1));
		legendpoints.setTotallegendpoints(lp.get(0));
		CHARACTERISTICSLEGENDARYSTATUS legendstatus = ApplicationProperties.create().getCharacteristics().getLegendaystatus(getDisciplineMaxCircle().getCircle());
		legendpoints.setRenown(legendstatus.getReown());
		legendpoints.setReputation(legendstatus.getReputation());
	}

	public void resetInitiative(STEPDICEType value) {
		INITIATIVEType initiative = new INITIATIVEType();
		initiative.setArmorpenalty(0);
		initiative.setModification(0);
		initiative.setBase(value.getStep());
		initiative.setStep(value.getStep());
		initiative.setDice(value.getDice());
		character.setINITIATIVE(initiative);
	}

	public INITIATIVEType getInitiative() {
		INITIATIVEType initiative = character.getINITIATIVE();
		if( initiative == null ) {
			initiative = new INITIATIVEType();
			character.setINITIATIVE(initiative);
		}
		return initiative;
	}

	public HEALTHType getHealth() {
		HEALTHType health = character.getHEALTH();
		if( health == null ) {
			health = new HEALTHType();
			character.setHEALTH(health);
		}
		return health;
	}

	public DEATHType getDeath() {
		HEALTHType health = getHealth();
		DEATHType death = health.getDEATH();
		if( death == null ) {
			death = new DEATHType();
			health.setDEATH(death);
		}
		return death;
	}

	public DEATHType getUnconsciousness() {
		HEALTHType health = getHealth();
		DEATHType unconsciousness = health.getUNCONSCIOUSNESS();
		if( unconsciousness == null ) {
			unconsciousness = new DEATHType();
			health.setUNCONSCIOUSNESS(unconsciousness);
		}
		return unconsciousness;
	}

	public WOUNDType getWound() {
		HEALTHType health = getHealth();
		WOUNDType wound = health.getWOUNDS();
		if( wound == null ) {
			wound = new WOUNDType();
			health.setWOUNDS(wound);
		}
		return wound;
	}

	public RECOVERYType getRecovery() {
		HEALTHType health = getHealth();
		RECOVERYType recovery = health.getRECOVERY();
		if( recovery == null ) {
			recovery = new RECOVERYType();
			health.setRECOVERY(recovery);
		}
		return recovery;
	}

	public KARMAType getKarma() {
		KARMAType karma = character.getKARMA();
		if( karma == null ) {
			karma = new KARMAType();
			character.setKARMA(karma);
		}
		return karma;
	}

	public MOVEMENTType getMovement() {
		MOVEMENTType movement = character.getMOVEMENT();
		if( movement == null ) {
			movement = new MOVEMENTType();
			character.setMOVEMENT(movement);
		}
		return movement;
	}

	public CARRYINGType getCarrying() {
		CARRYINGType carrying = character.getCARRYING();
		if( carrying == null ) {
			carrying = new CARRYINGType();
			character.setCARRYING(carrying);
		}
		return carrying;
	}

	public PROTECTIONType getProtection() {
		PROTECTIONType protection = character.getPROTECTION();
		if ( protection == null ) {
			protection = new PROTECTIONType();
			character.setPROTECTION(protection);
		}
		return protection;
	}

	public String getAbilities() {
		String abilities = character.getRACEABILITES();
		if( abilities == null ) {
			abilities = "";
			character.setRACEABILITES(abilities);
		}
		return abilities;
	}

	public void setAbilities(String newValue) {
		character.setRACEABILITES(newValue);
	}

	public List<DISCIPLINEType> getDisciplines() {
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		CALCULATEDLEGENDPOINTSType calculatedlegendpoints = character.getCALCULATEDLEGENDPOINTS();
		// Solange die letzte Disziplin keinen Kreis hat, wird diese entfernt. 
		while( (! disciplines.isEmpty()) && (disciplines.get(disciplines.size()-1).getCircle()<1) ) {
			int disciplineNumber = disciplines.size();
			disciplines.remove(disciplineNumber-1);
			// Wenn es keine Berechneten Legendenpunkte gibt, dann ist hier auch nichts zu tun.
			if( calculatedlegendpoints == null ) continue;
			// Anpasssungen der berechneten Legendenpunkt für Talente einer Disziplin die gelöscht wird, können auch weg
			List<NEWDISCIPLINETALENTADJUSTMENTType> remove = new ArrayList<NEWDISCIPLINETALENTADJUSTMENTType>();
			List<NEWDISCIPLINETALENTADJUSTMENTType> newdisciplinetalentadjustments = calculatedlegendpoints.getNEWDISCIPLINETALENTADJUSTMENT();
			for( NEWDISCIPLINETALENTADJUSTMENTType e : newdisciplinetalentadjustments ) {
				if( e.getDisciplinenumber() == disciplineNumber ) remove.add(e);
			}
			newdisciplinetalentadjustments.removeAll(remove);
		}
		// Bei allen anderen Disziplinen die keinen Kreis haben wird dort der Kreis auf 1 hoch gesetzt.
		for (DISCIPLINEType discipline : disciplines) {
			if( discipline.getCircle() < 1 ) discipline.setCircle(1);
		}
		return disciplines;
	}

	public List<String> getDisciplineNames() {
		List<String> result = new ArrayList<String>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(discipline.getName());
		}
		return result;
	}

	public List<String> getAllKarmaritual() {
		List<String> result = new ArrayList<String>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			String karmaritual = discipline.getKARMARITUAL();
			if( karmaritual == null ) {
				result.add(discipline.getName()+": NA");
			} else {
				result.add(discipline.getName()+": "+karmaritual);
			}
		}
		return result;
	}

	public String getAllHalfMagic() {
		StringBuilder result = new StringBuilder();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			String halfmagic = discipline.getHALFMAGIC();
			result.append("[");
			result.append(discipline.getName());
			result.append("]: ");
			if( halfmagic == null ) {
				result.append("NA");
			} else {
				result.append(halfmagic);
			}
			result.append("; ");
		}
		return result.toString();
	}

	public List<Integer> getDisciplineCircles() {
		List<Integer> result = new ArrayList<Integer>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(discipline.getCircle());
		}
		return result;
	}

	public int getDisciplineOrder(String disciplinename) {
		int order=1;
		for (DISCIPLINEType discipline : getDisciplines()) {
			if( disciplinename.equals(discipline.getName()) ) return order;
			order++;
		}
		// Not found
		return 0;
	}

	public HashMap<String,DISCIPLINEType> getAllDisciplinesByName() {
		HashMap<String,DISCIPLINEType> result = new HashMap<String,DISCIPLINEType>();
		for (DISCIPLINEType discipline : getDisciplines()) {
			result.put(discipline.getName(),discipline);
		}
		return result;
	}

	public int getCircleOf(String discipline) {
		DISCIPLINEType usedDiscipline = getAllDisciplinesByName().get(discipline);
		if( usedDiscipline == null ) {
			System.err.println("No discipline '"+discipline+"' is in use.");
			return 0;
		} else {
			return usedDiscipline.getCircle();
		}
	}

	public int getCircleOf(int disciplineNumber) {
		if( disciplineNumber < 1 ) {
			System.err.println("Discipline numer "+disciplineNumber+" is to low!");
			return 0;
		}
		List<DISCIPLINEType> disciplines = getDisciplines();
		if( disciplineNumber > disciplines.size() ) {
			System.err.println("Discipline numer "+disciplineNumber+" is to high!");
			return 0;
		}
		return disciplines.get(disciplineNumber-1).getCircle();
	}

	public DISCIPLINEType getDisciplineMaxCircle() {
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setCircle(0);
		discipline.setName("na");
		for( DISCIPLINEType d : getDisciplines() ) if( d.getCircle() > discipline.getCircle() ) discipline=d;
		return discipline;
	}

	public DISCIPLINEType getDisciplineMinCircle() {
		return getDisciplineMinCircle(0);
	}

	// Bestimme den Kleinsten Kreis einer Diszipline aber ohen die Disziplin mit der Nummer "notDiscipline"
	public DISCIPLINEType getDisciplineMinCircle(int notDiscipline) {
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setCircle(20);
		discipline.setName("na");
		int disciplinenumber=0;
		for( DISCIPLINEType d : getDisciplines() ) {
			disciplinenumber++;
			if( disciplinenumber == notDiscipline ) continue;
			if( d.getCircle() < discipline.getCircle() ) discipline=d;
		}
		return discipline;
	}

	public List<TalentsContainer> getAllTalents() {
		List<TalentsContainer> result = new ArrayList<TalentsContainer>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(new TalentsContainer(discipline));
		}
		return result;
	}

	public HashMap<String,TalentsContainer> getAllTalentsByDisziplinName() {
		HashMap<String,TalentsContainer> alltalents = new HashMap<String,TalentsContainer>();
		for (DISCIPLINEType discipline : getDisciplines() ) {
			alltalents.put(discipline.getName(),new TalentsContainer(discipline));
		}
		return alltalents;
	}

	public List<TALENTType> getTalentByName(String searchTalent) {
		List<TALENTType> result = new ArrayList<TALENTType>();
		for (TalentsContainer talents : getAllTalents()) {
			for (TALENTType talent : talents.getDisciplineAndOptionaltalents() ) {
				if ( talent.getName().equals(searchTalent)) {
					result.add(talent);
				}
			}
		}
		return result;
	}

	public TALENTType getTalentByDisciplinAndName(String disciplin, String searchTalent) {
		for( DISCIPLINEType discipline : getDisciplines() ) {
			if( ! discipline.getName().equals(disciplin) ) continue;
			for (TALENTType talent : discipline.getDISZIPLINETALENT()) {
				if ( talent.getName().equals(searchTalent)) return talent;
			}
			List<TALENTType> optionaltalents = discipline.getOPTIONALTALENT();
			List<TALENTType> remove = new ArrayList<TALENTType>();
			TALENTType result = null;
			for( TALENTType talent : optionaltalents ) {
				RANKType rank = talent.getRANK();
				if( (rank == null) || (rank.getRank() < 1) ) {
					remove.add(talent);
					continue;
				}
				if( talent.getName().equals(searchTalent) ) result=talent;
			}
			optionaltalents.removeAll(remove);
			if( result != null ) return result;
		}
		// Not found
		return null;
	}

	public HashMap<String,List<TALENTType>> getThreadWeavingTalents() {
		HashMap<String,List<TALENTType>> result = new HashMap<String,List<TALENTType>>();
		for (DISCIPLINEType discipline : getDisciplines() ) {
			List<TALENTType> threadweaving = new ArrayList<TALENTType>();
			for(TALENTType talent : (new TalentsContainer(discipline)).getDisciplineAndOptionaltalents() ) if( threadWeavingName.equals(talent.getName()) ) threadweaving.add(talent);
			result.put(discipline.getName(),threadweaving);
		}
		return result;
	}

	public List<SKILLType> getSkills() {
		List<SKILLType> skills = new ArrayList<SKILLType>();
		skills.addAll(character.getSKILL());
		int discount=1;
		for( DISCIPLINEType discipline : getDisciplines() ) {
			for( TALENTType talent : (new TalentsContainer(discipline)).getDisciplineAndOptionaltalents() ) {
				SKILLType skill = talent.getALIGNEDSKILL();
				if( skill != null ) {
					if( skill.getRealigned() == 0 ) skill.setRealigned(discount);
					skills.add(skill);
				}
			}
			discount++;
		}
		Collections.sort(skills, new SkillComparator());
		return skills;
	}

	public void addSkill(SKILLType skill) {
		character.getSKILL().add(skill);
	}

	public void removeSkill(SKILLType skill) {
		character.getSKILL().remove(skill);
	}

	public void removeSkill(List<SKILLType> skills) {
		character.getSKILL().removeAll(skills);
	}

	public List<SKILLType> getSpeakSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : getSkills() ) {
			if( speakSkillName.contains(skill.getName()) ) result.add(skill);
		}
		return result;
	}

	public List<SKILLType> getReadWriteSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : getSkills() ) {
			if( readwriteSkillName.contains(skill.getName()) ) result.add(skill);
		}
		return result;
	}

	public List<SKILLType> getNonLanguageSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : character.getSKILL() ) {
			result.add(skill);
		}
		return result;
	}

	public List<SKILLType> getOnlyLanguageSkills() {
		List<SKILLType> result = new ArrayList<SKILLType>();
		for( SKILLType skill : character.getSKILL() ) {
			if( languageSkillSpeakNames.contains(skill.getName()) || languageSkillReadWriteNames.contains(skill.getName()) ) result.add(skill);
		}
		return result;
	}

	public EXPERIENCEType getLegendPoints() {
		EXPERIENCEType experience = character.getEXPERIENCE();
		if( experience == null ) {
			experience = new EXPERIENCEType();
			experience.setCurrentlegendpoints(0);
			experience.setTotallegendpoints(0);
			character.setEXPERIENCE(experience);
		}
		return experience;
	}

	public void clearSpentLegendPoints() {
		EXPERIENCEType experience = getLegendPoints();
		List<ACCOUNTINGType> legendpoints = experience.getLEGENDPOINTS();
		List<ACCOUNTINGType> remove =  new ArrayList<ACCOUNTINGType>();
		for( ACCOUNTINGType a : legendpoints ) {
			if( a.getType().equals(PlusminusType.MINUS) ) remove.add(a);
		}
		legendpoints.removeAll(remove);
		experience.setCurrentlegendpoints(experience.getTotallegendpoints());
	}

	public List<WEAPONType> getWeapons() {
		return character.getWEAPON();
	}

	public List<DISCIPLINEBONUSType> getDisciplineBonuses() {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		for( DISCIPLINEType discipline : character.getDISCIPLINE() ) {
			bonuses.addAll(discipline.getDISCIPLINEBONUS());
		}
		return bonuses;
	}

	public List<ElementkindType> getDisciplinePrimElements() {
		List<ElementkindType> elements = new ArrayList<ElementkindType>();
		for( DISCIPLINEType discipline : character.getDISCIPLINE() ) {
			elements.add(discipline.getPrimelement());
		}
		return elements;
	}

	public void clearDisciplineBonuses() {
		for( DISCIPLINEType discipline : character.getDISCIPLINE() ) {
			discipline.getDISCIPLINEBONUS().clear();
		}
	}

	public List<COINSType> getAllCoins() {
		List<COINSType> allCoins = character.getCOINS();
		if( allCoins.isEmpty() ) {
			COINSType coins = new COINSType();
			coins.setSilver(100); // Startguthaben
			coins.setLocation("self");
			coins.setUsed(YesnoType.YES);
			coins.setName("Starting Purse");
			coins.setKind(ItemkindType.COINS);
			allCoins.add(coins);
		}
		return allCoins;
	}

	public List<SPELLType> getAllSpells() {
		List<SPELLType> result = new ArrayList<SPELLType>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			List<SPELLType> spells = discipline.getSPELL();
			if( spells != null ) result.addAll(spells);
		}
		return result;
	}

	public void clearOpenSpellList() {
		character.getOPENSPELL().clear();
	}

	public void addOpenSpell(SPELLType spell) {
		character.getOPENSPELL().add(spell);
	}

	public List<SPELLType> getOpenSpellList() {
		return character.getOPENSPELL();
	}

	/*
	 * Liefert für jede Diszipline des Charakters pro Kreis eine Auflistung der verwendeten Optionalen Talente
	 * Die äußere HashMap beinhaltet als Key, den Disziplinnamen. Die Values ist eine Liste,
	 * wobei nicht der erste Eintrag für den ersten Kreis steht sondern der Eintrag mit dem Index=1,
	 * das selbe gilt für die anderen Kreise analog
	 * Die Werte der Liste ist wieder rum eine Liste von Talenten.
	 * 
	 */
	public HashMap<String,List<List<TALENTType>>> getUsedOptionalTalents() {
		HashMap<String,List<List<TALENTType>>> result = new HashMap<String,List<List<TALENTType>>>();
		// Schleife über alle Disziplinen des Charakters
		for(DISCIPLINEType discipline : getDisciplines() ) {
			// Erstelle schon mal eine Ausreichende Liste von Leeren Listen um die Talente aufzunehmen.
			List<List<TALENTType>> list = new ArrayList<List<TALENTType>>();
			// !!! ACHTUNG: Kreis 1 hat Index 1 und nicht Index 0 !!!
			for(int i=0;i<=15;i++) list.add(new ArrayList<TALENTType>());
			// Schleife über alle Optionalen Talente
			for( TALENTType talent : discipline.getOPTIONALTALENT()) {
				// Sollte kein Realignment statt gefunden haben, dann handelt es sich um ein "benutzes" Optionales Talent
				if( talent.getRealigned() < 1 ) list.get(talent.getCircle()).add(talent);
			}
			result.put(discipline.getName(), list);
		}
		return result;
	}

	public static String getFullTalentname(TALENTType talent) {
		String name = talent.getName();
		// Falls es das DurabilityTalent ist, dann ignoriere die Limitationsangabe
		if( name.equals(durabilityName) ) return durabilityName;

		String limitation = talent.getLimitation();
		if( limitation == null ) return name;
		if( limitation.isEmpty() ) return name;
		return name + " : "+limitation;
	}

	public static String getFullTalentname(TALENTABILITYType talent) {
		String name = talent.getName();
		// Falls es das DurabilityTalent ist, dann ignoriere die Limitationsangabe
		if( name.equals(durabilityName) ) return durabilityName;

		String limitation = talent.getLimitation();
		if( limitation == null ) return name;
		if( limitation.isEmpty() ) return name;
		return name + " : "+limitation;
	}

	public static String getFullTalentname(KNACKBASEType knack) {
		String name = knack.getName();
		// Falls es das DurabilityTalent ist, dann ignoriere die Limitationsangabe
		if( name.equals(durabilityName) ) return durabilityName;

		String limitation = knack.getLimitation();
		if( limitation == null ) return name;
		if( limitation.isEmpty() ) return name;
		return name + " : "+limitation;
	}

	public List<TALENTABILITYType> getUnusedOptionalTalents(DISCIPLINE disciplineDefinition, int talentCircleNr) {
		List<TALENTABILITYType> result = new ArrayList<TALENTABILITYType>();
		List<TALENTType> usedTalents = new ArrayList<TALENTType>();
		// multiUseTalents sind Talente die mehr als einmal gelernt werden können
		HashMap<String, Integer> multiUseTalents = PROPERTIES.getMultiUseTalents();
		String disciplineName = disciplineDefinition.getName();
		// Schleife über alle gelernten Disziplinen
		for( DISCIPLINEType discipline : getDisciplines() ) {
			// Prüfe ob eine der gelernten Disziplinen die Disziplin ist, für die wir die ungenutzen Optionalen Talente ermitteln wollen.
			if( discipline.getName().equals(disciplineName) ) {
				// Disziplintalente der selben Disziplin reduzieren zwar den multiUse-Zähler
				// werden aber in keinem Fall in die usedTalents-Liste aufgenommen
				for( TALENTType talent : discipline.getDISZIPLINETALENT() ) {
					String name = getFullTalentname(talent);
					Integer multiUseCount = multiUseTalents.get(name);
					if( multiUseCount != null ) multiUseTalents.put(name,multiUseCount-1);
				}
				// Optionaltalente der selben Disziplin reduzieren den multiUse-Zähler
				// UND werden in jedem Fall in die usedTalents-Liste aufgenommen
				for( TALENTType talent : discipline.getOPTIONALTALENT() ) {
					usedTalents.add(talent);
					String name = getFullTalentname(talent);
					Integer multiUseCount = multiUseTalents.get(name);
					if( multiUseCount != null ) multiUseTalents.put(name,multiUseCount-1);
				}
			} else {
				// Diese gelernte Disziplin ist nicht die Disziplin, für die wir die ungenutzen Optionalen Talente ermitteln wollen
				for( TALENTType talent : (new TalentsContainer(discipline)).getDisciplineAndOptionaltalents() ) {
					String name = getFullTalentname(talent);
					Integer multiUseCount = multiUseTalents.get(name);
					if( multiUseCount == null ) {
						// Wenn es kein MultiUseTalent ist, dann behandele es ganz normal
						// und füge es in die Liste der Benutzen Talent hinzu
						usedTalents.add(talent);
					} else {
						// Wenn es sich aber um ein MultiUseTalent handelt, Zähle den MultiUse-Zähler hinunter,
						// es sei denn er ist bereits auf Eins, dann füge das Talent in die Liste der Benutzen Talent hinzu
						if( multiUseCount > 1 ) multiUseCount--;
						else usedTalents.add(talent);
						// Aktuallisiere den MultiUse-Zähler bzw lösche das Talent aus der MultiUse Liste
						if( multiUseCount > 0 ) multiUseTalents.put(name,multiUseCount);
						else multiUseTalents.remove(name);
					}
				}
			}
		}
		int mincircle=1;
		int maxcircle=0;
		// Durclaufe die Kreisdefinition des gesuchten Disziplin rückwärts und ermittele alle möglichen Optionaltalente
		for( int circlenr=talentCircleNr; circlenr>0; circlenr-- ) {
			DISCIPLINECIRCLEType disciplineCircle = disciplineDefinition.getCIRCLE().get(circlenr-1);
			for( TALENTABILITYType talent : disciplineCircle.getOPTIONALTALENT() ) {
				TALENTType found = null;
				String talentname = getFullTalentname(talent);
				for( TALENTType ut : usedTalents ) if( (ut.getCircle()>=circlenr) && getFullTalentname(ut).equals(talentname) ) found=ut;
				// Wenn das Talent nicht gefunden wurde, steht es dem Charakter als weiteres Optionales Talent zur Verfügung
				if( found == null ) result.add(talent);
				// Wenn das Talent gefunden wurde, steht es dem Charakter NICHT ein weiteres mal als Optionales Talent zur Verfügung.
				// Wird aber aus der Liste entfernt falls nochmal danach gesucht werden sollte, damit es dann nicht gefunden wird.
				else usedTalents.remove(found);
			}
			FOREIGNTALENTSType foreignTalents = disciplineCircle.getFOREIGNTALENTS();
			if( foreignTalents != null ) {
				if(foreignTalents.getMincircle()<mincircle) mincircle=foreignTalents.getMincircle();
				if(foreignTalents.getMaxcircle()>maxcircle) maxcircle=foreignTalents.getMaxcircle();
			}
		}
		if(mincircle<=maxcircle) {
			// Wenn min und max ein gülltiges Intervall ergeben, dann sind FOREIGNTALENTS definiert und müssen eingefügt werden.
			// Um doppelte Auflistung zu vermeiden Erzeuge ein Liste von allen benutzten Talente sowie den Talenten, die bereits
			// als Optionale Talente identifiziert wurden.
			HashMap<String, TALENTABILITYType> talents = PROPERTIES.getTalentsByCircle(mincircle,maxcircle);
			List<String> potentialTalents = new ArrayList<String>();
			for( TALENTType talent : usedTalents ) potentialTalents.add(getFullTalentname(talent));
			for( TALENTABILITYType talent : result ) potentialTalents.add(getFullTalentname(talent));
			// Erweitere die Ergebnisliste um FOREIGNTALENTS nur wenn diese noch nicht enthalten waren.
			for( TALENTABILITYType talent : talents.values() ) {
				String name = getFullTalentname(talent);
				if( potentialTalents.contains(name) ) potentialTalents.remove(name);
				else result.add(talent);
			}
		}
		return result;
	}

	public HashMap<String,List<Integer>> getCircleOfMissingOptionalTalents() {
		HashMap<String,List<Integer>> result = new HashMap<String,List<Integer>>();
		HashMap<String,List<List<TALENTType>>> talentsMap = getUsedOptionalTalents();
		// Eine Schleife über alle Disciplinenamen des Charakters
		for(String discipline : talentsMap.keySet() ) {
			List<Integer> list = new ArrayList<Integer>();
			// Hole alle benutzen Optionalen Talente der aktuellen Disziplin
			List<List<TALENTType>> talentsList = talentsMap.get(discipline);
			if( talentsList == null ) {
				System.err.println("A talent list for the discipline '"+discipline+"' could not be found.");
				talentsList = new ArrayList<List<TALENTType>>();
				for(int i=0;i<=15;i++) talentsList.add(new ArrayList<TALENTType>());
			}
			int disciplineNumber = getDisciplineOrder(discipline);
			// Falls in den Optionalen Regel Default Talente festgelegt seine sollte, hole diese
			HashMap<String, Integer> defaultOptionalTalents = PROPERTIES.getDefaultOptionalTalents(disciplineNumber);
			int disciplineCircle = getCircleOf(disciplineNumber);
			int circlenr=0;
			for( int numberOfOptionalTalents : PROPERTIES.getNumberOfOptionalTalentsPerCircleByDiscipline(discipline) ) {
				circlenr++;
				if( circlenr > disciplineCircle ) break;
				List<TALENTType> talents = new ArrayList<TALENTType>();
				for( TALENTType talent : talentsList.get(circlenr) ) {
					// Ermittele den Kreis für ein Default Optional Talent
					Integer c = defaultOptionalTalents.get(talent.getName());
					// Wenn der Kreis undefinert ist, dann war es kein Default Optionales Talent, sondern ein normales Optionales Talent
					// Nur wenn der Kreis bei einem Default Optionalen Talent nicht über dem aktuellen Kreis liegt darf es eingefügt werden
					if( (c!=null) && (c<=circlenr) ) continue;
					talents.add(talent);
				}
				// Jetzt zählen wir noch wieviele der Optionalen Talente nicht über Vielseitigkeit gelernt wurden
				// und ziehen diese von der Anzahl der möglichen Optionalen Talente ab
				int freeOptionalTalents=numberOfOptionalTalents-isNotLearnedByVersatility(talents);
				// Füge der Anzahl ensprechend viel den aktuellen Kreis in die Ergebnisliste hinzu.
				for( int i=0; i<freeOptionalTalents; i++ ) list.add(circlenr);
			}
			result.put(discipline, list);
		}
		return result;
	}

	public static boolean isLearnedByVersatility(TALENTType talent) {
		TALENTTEACHERType teacher = talent.getTEACHER();
		if( teacher == null ) return false;
		if( teacher.getByversatility().equals(YesnoType.YES) ) return true;
		return false;
	}

	public static int isLearnedByVersatility(List<TALENTType> talents) {
		if( talents == null ) return 0;
		int result=0;
		for( TALENTType talent : talents ) {
			if( isLearnedByVersatility(talent) ) result++;
		}
		return result;
	}

	public static int isNotLearnedByVersatility(List<TALENTType> talents) {
		if( talents == null ) return 0;
		int result=0;
		for( TALENTType talent : talents ) {
			if( ! isLearnedByVersatility(talent) ) result++;
		}
		return result;
	}

	public void addDiciplin(String name){
		// Wenn die Disziplin bereits vorhanden, dann tue nichts
		if( getAllDisciplinesByName().get(name) != null ) return;
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setName(name);
		discipline.setCircle(1);
		character.getDISCIPLINE().add(discipline);
		ensureDisciplinTalentsExits();
		realignOptionalTalents();
	}

	public void removeLastDiciplin(){
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		int size = disciplines.size();
		if( size>0 ) disciplines.remove(size-1);
	}

	public void ensureDisciplinTalentsExits() {
		List<String> totalListOfDisciplineTalents = new ArrayList<String>();
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getDisciplinetalents() ) {
				totalListOfDisciplineTalents.add(getFullTalentname(talent));
			}
		}
		for( DISCIPLINEType discipline : getDisciplines() ) {
			DISCIPLINE disciplineDefinition = PROPERTIES.getDisziplin(discipline.getName());
			// Wenn es zu der Disziplin der Talentliste keine Disziplindefinition gibt, dann über springe diese Talentliste
			if( disciplineDefinition == null ) continue;
			int disciplineCircleNr = discipline.getCircle();
			int circlenr=0;
			for( DISCIPLINECIRCLEType disciplineCircleDefinition : disciplineDefinition.getCIRCLE() ) {
				circlenr++;
				if( circlenr > disciplineCircleNr ) break;
				for( TALENTABILITYType disciplineTalent :disciplineCircleDefinition.getDISCIPLINETALENT()) {
					TALENTType newTalent = new TALENTType();
					newTalent.setName(disciplineTalent.getName());
					newTalent.setLimitation(disciplineTalent.getLimitation());
					newTalent.setCircle(circlenr);
					String newFullTalentName=getFullTalentname(newTalent);
					if( ! totalListOfDisciplineTalents.contains(newFullTalentName) ) {
						discipline.getDISZIPLINETALENT().add(newTalent);
						totalListOfDisciplineTalents.add(newFullTalentName);
					}
				}
			}
		}
	}

	public void realignOptionalTalents() {
		// Talentname von Unempfindlichkeit ermitteln
		final String durabilityName = PROPERTIES.getDurabilityName();
		// Talente ermitteln die mehrfach verwendet werden dürfen.
		HashMap<String, Integer> multiUseTalents = PROPERTIES.getMultiUseTalents();
		// Liste alle vom Charakter gelernter Disziplinen
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		// Wenn man bei Eins anfängt zu nummerieren, dann ist die Anzahl der Disziplinen auch gleichzeitig die Nummer der letzten Disziplin.
		int maxDisciplineOrder=disciplines.size();
		int disciplineOrder=0;
		for( DISCIPLINEType discipline : disciplines ) {
			// Die Nummerierung der Disziplinen fängt bei Eins an, daher zälen wir hier schon hoch
			disciplineOrder++;
			for( TALENTType disTalent : discipline.getDISZIPLINETALENT() ) {
				// Disziplinetalente können nicht realigned werden, daher ist auch keine gesonderte Prüfung
				// ob dieses Talent bereits ge-realigned ist nicht notwendig und wäre unsinnig
				// Ermittle für spätere Vergleiche den vollständigen Diszipline namen (mit Limitation Bezeichnung)
				String disTalentName=getFullTalentname(disTalent);
				for( DISCIPLINEType compareDiscipline : disciplines ) {
					// Talentlisten nicht mit sich selbst vergleichen, daher Schleife überspringen, wenn es die selbe Disziplin ist.
					if( discipline == compareDiscipline ) continue;
					for( TALENTType optTalent : compareDiscipline.getOPTIONALTALENT() ) {
						String optTalentName = getFullTalentname(optTalent);
						if( multiUseTalents.containsKey(optTalentName) ) {
							// MultiUseTalents können nicht realigned werden.
							// Prüfe, ob es eventuell doch gemacht wurde, wenn ja korigieren wir es hier
							if( optTalent.getRealigned() > 0 ) optTalent.setRealigned(0);
							// Springe zum nächsten Talent, führ diesen Schleifen durchlauf nicht fort.
							continue;
						}
						// Sollte das Optinaltalent auf ein Talent einer nicht mehr exisiterenden Disziplin realgined sind,
						// kann dieses Realgined entfernt werden
						if( optTalent.getRealigned() > maxDisciplineOrder ) optTalent.setRealigned(0);
						// Unterscheide, ob das Disziplintalent das Unempfindlichkeit-Talent ist
						if( durabilityName.equals(disTalent.getName()) ) {
							// Wenn ja, dann ist die Limitation nicht wichtig und wir prüfen nur ob das vergleichende Talent eben falls das Unempfindlichkeit-Talent ist.
							if( durabilityName.equals(optTalent.getName()) ) optTalent.setRealigned(disciplineOrder);
						} else {
							// Wenn nein, dann ist die Limitation wichtig und muss identisch sein. Daher werden die "vollstädnige" Talentnamen verglichen
							if( disTalentName.equals(optTalentName) ) optTalent.setRealigned(disciplineOrder);
						}
					}
				}
			}
		}
	}

	public TALENTType addOptionalTalent(String disciplineName, int circle, TALENTABILITYType talenttype, boolean byVersatility) {
		TALENTType talent = new TALENTType();
		talent.setName(talenttype.getName());
		talent.setLimitation(talenttype.getLimitation());
		talent.setCircle(circle);

		RANKType rank = new RANKType();
		rank.setRank(1);
		talent.setRANK(rank);
		TALENTTEACHERType teacher = new TALENTTEACHERType();
		if( byVersatility ) teacher.setByversatility(YesnoType.YES);
		talent.setTEACHER(teacher);

		int disciplineOrder = getDisciplineOrder(disciplineName);
		if( disciplineOrder < 1 ) {
			System.err.println("Discipline '"+disciplineName+"' cound not be found. So, the optional talent '"+talenttype.getName()+"' cound not be inserted.");
			return null;
		}
		// Wenn es sich bei dem neuen OptionalTalent um das Unempfindlichkeitstalent handelt,
		// dann muss geprüft werden, ob dieses bereits in einder anderen Disziplin vorhanden ist.
		//TODO: nicht nur Unempfindlichkeitstalent
		if( durabilityName.equals(talent.getName()) ) {
			for( DISCIPLINEType discipline : getDisciplines() ) {
				// Wenn die Disziplinnamen über einstimmen, dann handelt es sich um die selbe Disziplin
				// und es brauch nichts geprüft werden.
				if( disciplineName.equals(discipline.getName()) ) continue;
				for( TALENTType optTalent : discipline.getOPTIONALTALENT() ) {
					if( ! durabilityName.equals(optTalent.getName()) ) continue;
					optTalent.setRealigned(disciplineOrder);
				}
			}
		}
		getAllTalents().get(disciplineOrder-1).getOptionaltalents().add(talent);
		return talent;
	}

	public void addSpell(String discipline, SPELLType spell){
		for( DISCIPLINEType dis : getDisciplines() ){
			if(dis.getName().equals(discipline)){
				dis.getSPELL().add(spell);
				return;
			}
		}
		System.err.println("Discipline '"+discipline+"' not found, could not add a spells.");
	}

	public void removeSpell(String disciplinename, SPELLType spell) {
		if( spell == null ) return;
		removeSpell(disciplinename, spell.getName());
	}

	public void removeSpell(String disciplinename, String spellname) {
		for( DISCIPLINEType dis : getDisciplines() ){
			if(dis.getName().equals(disciplinename)){
				List<SPELLType> spells = dis.getSPELL();
				List<SPELLType> spelltoremove = new ArrayList<SPELLType>();
				for( SPELLType currentspell : spells ) {
					if( spellname.equals(currentspell.getName()) ) {
						spelltoremove.add(currentspell);
					}
				}
				spells.removeAll(spelltoremove);
				return;
			}
		}
	}
	public boolean hasSpellLearned(String disciplinename, SPELLType spelltype) {
		return hasSpellLearned(disciplinename, spelltype.getName());
	}

	public boolean hasSpellLearned(String disciplinename, String spellname){
		for(DISCIPLINEType discipline : getDisciplines()){
			if( disciplinename.equals(discipline.getName()) ){
				for(SPELLType spell : discipline.getSPELL()){
					if( spellname.equals(spell.getName()) ) return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean hasSpellLearnedBySpellability(String disciplinename, SPELLType spelltype) {
		return hasSpellLearnedBySpellability(disciplinename, spelltype.getName());
	}

	public boolean hasSpellLearnedBySpellability(String disciplinename, String spellname){
		for(DISCIPLINEType discipline : getDisciplines()){
			if( disciplinename.equals(discipline.getName()) ){
				for(SPELLType spell : discipline.getSPELL()){
					if( spellname.equals(spell.getName()) ) return spell.getByspellability().equals(YesnoType.YES);
				}
				return false;
			}
		}
		return false;
	}

	public void toggleSpellLearnedBySpellability(String disciplinename, SPELLType spelltype) {
		toggleSpellLearnedBySpellability(disciplinename, spelltype.getName());
	}

	public void toggleSpellLearnedBySpellability(String disciplinename, String spellname) {
		for(DISCIPLINEType discipline : getDisciplines()){
			if( disciplinename.equals(discipline.getName()) ){
				for(SPELLType spell : discipline.getSPELL()){
					if( spellname.equals(spell.getName()) ) {
						if( spell.getByspellability().equals(YesnoType.YES) ) spell.setByspellability(YesnoType.NO);
						else                                                  spell.setByspellability(YesnoType.YES);
						return;
					}
				}
				return;
			}
		}
	}

	public boolean hasKnackLearned(KNACKBASEType knack) {
		String knackName = knack.getName();
		String knackLimitation = knack.getLimitation();
		boolean noKnackLimitation = knackLimitation.isEmpty();
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getDisciplineAndOptionaltalents() ) {
				if( noKnackLimitation || talent.getLimitation().equals(knackLimitation)) for( KNACKType k : talent.getKNACK() ) {
					if( k.getName().equals(knackName) ) return true;
				}
			}
		}
		return false;
	}

	public void removeKnack(KNACKBASEType knack) {
		String knackName = knack.getName();
		String knackLimitation = knack.getLimitation();
		boolean noKnackLimitation = knackLimitation.isEmpty();
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getDisciplineAndOptionaltalents() ) {
				if( noKnackLimitation || talent.getLimitation().equals(knackLimitation)) {
					List<KNACKType> talentknacks = talent.getKNACK();
					List<KNACKType> remove = new ArrayList<KNACKType>();
					for( KNACKType k : talentknacks ) if( k.getName().equals(knackName) ) remove.add(k);
					talentknacks.removeAll(remove);
				}
			}
		}
	}

	public List<ITEMType> getItems() {
		return character.getITEM();
	}

	public String getDESCRIPTION() {
		String result=character.getDESCRIPTION();
		if( result == null ) return "";
		return result;
	}

	public void setDESCRIPTION(String description) {
		character.setDESCRIPTION(description);
	}

	public String getCOMMENT() {
		String result=character.getCOMMENT();
		if( result == null ) return "";
		return result;
	}

	public void setCOMMENT(String comment) {
		character.setCOMMENT(comment);
	}

	public List<MAGICITEMType> getMagicItem() {
		return character.getMAGICITEM();
	}

	public List<THREADITEMType> getThreadItem() {
		return character.getTHREADITEM();
	}

	public List<MAGICITEMType> getBloodCharmItem() {
		return character.getBLOODCHARMITEM();
	}

	public List<PATTERNITEMType> getPatternItem() {
		return character.getPATTERNITEM();
	}

	public List<ARMORType> getMagicArmor() {
		List<ARMORType> magicarmor = new ArrayList<ARMORType>();
		for( THREADITEMType magicitem : getThreadItem() ) {
			String name = magicitem.getName();
			float weight = magicitem.getWeight();
			YesnoType used = magicitem.getUsed();
			ItemkindType kind = magicitem.getKind();
			int weaven = magicitem.getWeaventhreadrank();
			String location = magicitem.getLocation();
			int edn=magicitem.getEnchantingdifficultynumber();
			int blooddamage = magicitem.getBlooddamage();
			int dr = magicitem.getDepatterningrate();
			String bookref = magicitem.getBookref();
			int size = magicitem.getSize();
			int rank=0;
			ARMORType newmagicarmor = null;
			SHIELDType newmagicshield = null;
			List<CHARACTERISTICSCOST> LpCosts = PROPERTIES_Characteristics.getTalentRankLPIncreaseTable(1,magicitem.getLpcostgrowth() );
			for( THREADRANKType threadrank : magicitem.getTHREADRANK() ) {
				threadrank.setLpcost( LpCosts.get(rank).getCost() );
				rank++;
				ARMORType armor = threadrank.getARMOR();
				if( armor != null ) {
					armor.setName(name);
					armor.setWeight(weight);
					armor.setUsed(used);
					armor.setKind(kind);
					armor.setLocation(location);
					armor.setEdn(edn);
					armor.setBlooddamage(blooddamage);
					armor.setDepatterningrate(dr);
					armor.setBookref(bookref);
					armor.setSize(size);
					if( weaven == rank ) newmagicarmor=armor;
				}
				SHIELDType shield = threadrank.getSHIELD();
				if( shield != null ) {
					shield.setName(name);
					shield.setWeight(weight);
					shield.setUsed(used);
					shield.setKind(kind);
					shield.setLocation(location);
					shield.setEdn(edn);
					shield.setBlooddamage(blooddamage);
					shield.setDepatterningrate(dr);
					shield.setBookref(bookref);
					shield.setSize(size);
					if( weaven == rank ) newmagicshield=shield;
				}
			}
			if( newmagicarmor != null ) magicarmor.add(copyArmor(newmagicarmor,true));
			if( newmagicshield != null ) magicarmor.add(copyArmor(newmagicshield,true));
		}
		return magicarmor;
	}

	public static ARMORType copyArmor(ARMORType armor, boolean setvirtual) {
		if( armor == null ) return null;
		ARMORType newarmor;
		if( armor instanceof SHIELDType ) newarmor = new SHIELDType();
		else newarmor = new ARMORType();
		newarmor.setBlooddamage(armor.getBlooddamage());
		newarmor.setBookref(armor.getBookref());
		newarmor.setDateforged(armor.getDateforged());
		newarmor.setDepatterningrate(armor.getDepatterningrate());
		newarmor.setEdn(armor.getEdn());
		newarmor.setEdnElement(armor.getEdnElement());
		newarmor.setKind(armor.getKind());
		newarmor.setLocation(armor.getLocation());
		newarmor.setMysticarmor(armor.getMysticarmor());
		newarmor.setName(armor.getName());
		newarmor.setPenalty(armor.getPenalty());
		newarmor.setPhysicalarmor(armor.getPhysicalarmor());
		newarmor.setTimesforgedMystic(armor.getTimesforgedMystic());
		newarmor.setTimesforgedPhysical(armor.getPhysicalarmor());
		newarmor.setUsed(armor.getUsed());
		newarmor.setWeight(armor.getWeight());
		newarmor.setSize(armor.getSize());
		if( newarmor instanceof SHIELDType ) {
			((SHIELDType)newarmor).setMysticdeflectionbonus(((SHIELDType)armor).getMysticdeflectionbonus());
			((SHIELDType)newarmor).setPhysicaldeflectionbonus(((SHIELDType)armor).getPhysicaldeflectionbonus());
			((SHIELDType)newarmor).setShatterthreshold(((SHIELDType)armor).getShatterthreshold());
		}
		if( setvirtual ) newarmor.setVirtual(YesnoType.YES);
		else newarmor.setVirtual(armor.getVirtual());
		return newarmor;
	}

	public List<ARMORType> removeVirtualArmorFromNormalArmorList() {
		List<ARMORType> armors = getProtection().getARMOROrSHIELD();
		List<ARMORType> delete = new ArrayList<ARMORType>();
		for( ARMORType armor : armors) if( armor.getVirtual().equals(YesnoType.YES)) delete.add(armor);
		armors.removeAll(delete);
		return armors;
	}

	public List<WEAPONType> getMagicWeapon() {
		List<WEAPONType> magicweapon = new ArrayList<WEAPONType>();
		for( THREADITEMType magicitem : getThreadItem() ) {
			String name = magicitem.getName();
			float weight = magicitem.getWeight();
			YesnoType used = magicitem.getUsed();
			int weaven = magicitem.getWeaventhreadrank();
			int blooddamage = magicitem.getBlooddamage();
			int dr = magicitem.getDepatterningrate();
			String bookref = magicitem.getBookref();
			int size = magicitem.getSize();
			int rank=0;
			WEAPONType newmagicweapon = null;
			List<CHARACTERISTICSCOST> LpCosts = PROPERTIES_Characteristics.getTalentRankLPIncreaseTable(1,magicitem.getLpcostgrowth() );
			for( THREADRANKType threadrank : magicitem.getTHREADRANK() ) {
				threadrank.setLpcost( LpCosts.get(rank).getCost() );
				rank++;
				WEAPONType weapon = threadrank.getWEAPON();
				if( weapon != null ) {
					weapon.setName(name);
					weapon.setWeight(weight);
					weapon.setUsed(used);
					weapon.setKind(magicitem.getKind());
					weapon.setBlooddamage(blooddamage);
					weapon.setDepatterningrate(dr);
					weapon.setBookref(bookref);
					weapon.setSize(size);
					if( weaven > 0 ) newmagicweapon=weapon;
				}
				weaven--;
			}
			if( newmagicweapon != null ) magicweapon.add(copyWeapon(newmagicweapon,true));
		}
		return magicweapon;
	}

	public static WEAPONType copyWeapon(WEAPONType weapon, boolean setvirtual) {
		if( weapon == null ) return null;
		WEAPONType newweapon = new WEAPONType();
		newweapon.setBlooddamage(weapon.getBlooddamage());
		newweapon.setBookref(weapon.getBookref());
		newweapon.setDateforged(weapon.getDateforged());
		newweapon.setDepatterningrate(weapon.getDepatterningrate());
		newweapon.setKind(weapon.getKind());
		newweapon.setLocation(weapon.getLocation());
		newweapon.setName(weapon.getName());
		newweapon.setUsed(weapon.getUsed());
		newweapon.setWeight(weapon.getWeight());
		newweapon.setDamagestep(weapon.getDamagestep());
		newweapon.setDexteritymin(weapon.getDexteritymin());
		newweapon.setLongrange(weapon.getLongrange());
		newweapon.setShortrange(weapon.getShortrange());
		newweapon.setSize(weapon.getSize());
		newweapon.setStrengthmin(weapon.getStrengthmin());
		newweapon.setTimesforged(weapon.getTimesforged());
		if( setvirtual ) newweapon.setVirtual(YesnoType.YES);
		else newweapon.setVirtual(weapon.getVirtual());
		return newweapon;
	}

	public static WOUNDType copyWound(WOUNDType wound) {
		if( wound == null ) return null;
		WOUNDType newwound = new WOUNDType();
		newwound.setBlood(wound.getBlood());
		newwound.setNormal(wound.getNormal());
		newwound.setPenalties(wound.getPenalties());
		newwound.setThreshold(wound.getThreshold());
		return newwound;
	}

	public static DISZIPINABILITYType copyDisciplineAbility(DISZIPINABILITYType disciplineability) {
		if( disciplineability == null ) return null;
		DISZIPINABILITYType newdisciplineability = new DISZIPINABILITYType();
		newdisciplineability.setCount(disciplineability.getCount());
		return newdisciplineability;
	}

	public static DEFENSEABILITYType copyDefenseAbility(DEFENSEABILITYType defenseability) {
		if( defenseability == null ) return null;
		DEFENSEABILITYType newdefenseability = new DEFENSEABILITYType();
		newdefenseability.setBonus(defenseability.getBonus());
		newdefenseability.setKind(defenseability.getKind());
		return newdefenseability;
	}

	public static TALENTABILITYType copyTalentAbility(TALENTABILITYType talentability) {
		if( talentability == null ) return null;
		TALENTABILITYType newtalentability = new TALENTABILITYType();
		newtalentability.setBonus(talentability.getBonus());
		newtalentability.setLimitation(talentability.getLimitation());
		newtalentability.setName(talentability.getName());
		newtalentability.setPool(talentability.getPool());
		return newtalentability;
	}

	public static THREADRANKType copyThreadRank(THREADRANKType rank) {
		if( rank == null ) return null;
		THREADRANKType newrank = new THREADRANKType();
		newrank.setEffect(rank.getEffect());
		newrank.setLpcost(rank.getLpcost());
		newrank.setKeyknowledge(rank.getKeyknowledge());
		newrank.setARMOR(copyArmor(rank.getARMOR(),false));
		newrank.setSHIELD((SHIELDType)copyArmor(rank.getSHIELD(),false));
		newrank.setWEAPON(copyWeapon(rank.getWEAPON(),false));
		newrank.setWOUND(copyWound(rank.getWOUND()));
		for( String i : rank.getABILITY() ) newrank.getABILITY().add(i);
		for( String i : rank.getSPELL() ) newrank.getSPELL().add(i);
		for( DISZIPINABILITYType i : rank.getINITIATIVE() ) newrank.getINITIATIVE().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getKARMASTEP() ) newrank.getKARMASTEP().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getMAXKARMA() ) newrank.getINITIATIVE().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getRECOVERYTEST() ) newrank.getMAXKARMA().add(copyDisciplineAbility(i));
		for( DISZIPINABILITYType i : rank.getSPELLABILITY() ) newrank.getSPELLABILITY().add(copyDisciplineAbility(i));
		for( DEFENSEABILITYType i : rank.getDEFENSE() ) newrank.getDEFENSE().add(copyDefenseAbility(i));
		for( TALENTABILITYType i : rank.getTALENT() ) newrank.getTALENT().add(copyTalentAbility(i));
		return newrank;
	}

	public List<WEAPONType> cutMagicWeaponFromNormalWeaponList() {
		List<WEAPONType> magicWeapon = getMagicWeapon();
		List<WEAPONType> normalWeaponList = character.getWEAPON();
		List<WEAPONType> delete = new ArrayList<WEAPONType>();
		for( WEAPONType weapon : normalWeaponList) {
			String weaponName = weapon.getName();
			for( WEAPONType w : magicWeapon ) {
				w.setVirtual(YesnoType.YES);
				if( weaponName.equals(w.getName()) ) delete.add(weapon);
			}
		}
		normalWeaponList.removeAll(delete);
		return magicWeapon;
	}

	public static List<Integer> calculateAccounting(List<ACCOUNTINGType> accountings) {
		int plus = 0;
		int minus = 0;
		for( ACCOUNTINGType lp : accountings ) {
			switch( lp.getType() ) {
			case PLUS:  plus  += lp.getValue(); break;
			case MINUS: minus += lp.getValue(); break;
			}
		}
		List<Integer> account = new ArrayList<Integer>();
		account.add(plus);  // 0
		account.add(minus); // 1
		return account;
	}

	public DEVOTIONType getDevotionPoints() {
		return character.getDEVOTION();
	}

	public String getPassion() {
		DEVOTIONType devotion = getDevotionPoints();
		if( devotion == null ) return "";
		String passion = devotion.getPassion();
		if( passion == null ) return "";
		return passion;
	}

	public int calculateDevotionPoints() {
		DEVOTIONType devotionpoints=getDevotionPoints();
		if( devotionpoints == null ) return 0;
		List<Integer> dp = calculateAccounting(devotionpoints.getDEVOTIONPOINTS());
		int result = dp.get(0)-dp.get(1);
		devotionpoints.setValue(result);
		return result;
	}

	public int getNumberOfTalentsLearnedByVersatility() {
		List<TalentsContainer> allTalents = getAllTalents();
		if( allTalents == null ) return 0;
		int result = 0;
		for( TalentsContainer talents : allTalents ) {
			result += isLearnedByVersatility(talents.getDisciplineAndOptionaltalents());
		}
		return result;
	}

	public int getUnusedVersatilityRanks() {
		int result=-getNumberOfTalentsLearnedByVersatility();
		List<TALENTType> versatilityList = getTalentByName(PROPERTIES.getVersatilityName());
		if( versatilityList == null ) return result;
		if( versatilityList.isEmpty() ) return result;
		for( TALENTType versatility : versatilityList ) {
			RANKType rank = versatility.getRANK();
			if( rank != null ) {
				result += rank.getRank();
			}
		}
		return result;
	}

	public void removeEmptySkills() {
		List<SKILLType> skills = getSkills();
		List<SKILLType> remove = new ArrayList<SKILLType>();
		for( SKILLType skill : skills ) {
			RANKType rank = skill.getRANK();
			if( (rank != null) && (rank.getRank() > 0) ) continue;
			remove.add(skill);
		}
		removeSkill(remove);
	}

	public void updateRealignedTalents() {
		HashMap<String, List<TALENTType>> realignedTalentHash = new HashMap<String, List<TALENTType>>();
		for( TalentsContainer talents : getAllTalents() ) {
			insertIfRealigned(realignedTalentHash, talents.getDisciplineAndOptionaltalents() );
		}
		for( String talentName : realignedTalentHash.keySet() ) {
			List<TALENTType> talentsByName = getTalentByName(talentName);
			List<TALENTType> realignedTalentList = realignedTalentHash.get(talentName);
			// Entferne alle Talente die wir bereits haben.
			talentsByName.removeAll(realignedTalentList);
			// Theoretisch sollte in talentsByName nur noch ein Element übrig bleiben.
			int maxRealignedRank=0;
			for( TALENTType talent : realignedTalentList ) {
				int currentRealignedRank = talent.getRANK().getRank();
				if( maxRealignedRank < currentRealignedRank ) maxRealignedRank=currentRealignedRank;
			}
			for( TALENTType talent : talentsByName ) {
				RANKType rank = talent.getRANK();
				if( rank == null ) {
					rank = new RANKType();
					talent.setRANK(rank);
				}
				rank.setRealignedrank(maxRealignedRank);
			}
		}
	}

	private void insertIfRealigned(HashMap<String, List<TALENTType>> realignedTalents, List<TALENTType> talents) {
		for( TALENTType talent : talents ) {
			if( talent.getRealigned() > 0 ) {
				String talentName = talent.getName();
				List<TALENTType> list = realignedTalents.get(talentName);
				if( list == null ) {
					list = new ArrayList<TALENTType>();
					realignedTalents.put(talentName, list);
				}
				list.add(talent);
			}
		}
	}

	public void removeZeroRankOptionalTalents() {
		for( TalentsContainer talents : getAllTalents() ) {
			List<TALENTType> rankZeroTalents = new ArrayList<TALENTType>();
			List<TALENTType> optionalTalents = talents.getOptionaltalents();
			for( TALENTType talent : optionalTalents ) {
				RANKType rank = talent.getRANK();
				if( (rank == null) || (rank.getRank() < 1) ) rankZeroTalents.add(talent);
			}
			optionalTalents.removeAll(rankZeroTalents);
		}
	}

	public void removeIllegalTalents() {
		int disciplineOrder=0;
		for( DISCIPLINEType discipline : getDisciplines() ) {
			disciplineOrder++;
			int disciplineCircleNr = discipline.getCircle();
			List<TALENTType> remove = new ArrayList<TALENTType>();
			List<TALENTType> disciplineTalents = discipline.getDISZIPLINETALENT();
			for( TALENTType talent : disciplineTalents ) {
				if( talent.getCircle() > disciplineCircleNr ) remove.add(talent);
			}
			disciplineTalents.removeAll(remove);
			remove.clear();
			List<TALENTType> optionalTalents = discipline.getDISZIPLINETALENT();
			for( TALENTType talent : optionalTalents ) {
				if( talent.getCircle() > disciplineCircleNr ) {
					remove.add(talent);
					continue;
				}
				// Talente bei denen die Limitation auf "(#)" endet kommen von ThreadItems und fliegen erstmal raus
				// Diese werden wieder vom ThreadItem ergänzt, wenn es noch da ist.
				if( talent.getLimitation().endsWith("(#)") ) {
					remove.add(talent);
					continue;
				}
			}
			optionalTalents.removeAll(remove);
		}
	}

	public NAMEGIVERABILITYType getRace() {
		APPEARANCEType appearance = getAppearance();
		String race = appearance.getRace();
		String origin = appearance.getOrigin();
		NAMEGIVERABILITYType fallback = null;
		for (NAMEGIVERABILITYType n : PROPERTIES.getNamegivers()) {
			if( n.getName().equals(race)) {
				// wenn der Rassenname übereinstimmt, dann könnte es schon mal passen
				// und da wir "first-match" und nicht "last-match" haben wollen, setzen wir den fallback nur wenn er noch nicht gesetzt wurde
				if( fallback==null ) fallback = n;
				// aber erst wenn auch das Ursprungsgebiet zusammen passt haben wir unsere Rassendefinition
				if( n.getORIGIN().contains(origin) ) return n;
			}
		}
		return fallback;
	}

	public void calculateMovement() {
		int movementFlight = 0;
		int movementGround = 0;
		NAMEGIVERABILITYType namegiver = getRace();
		if( namegiver != null ) {
			movementFlight = namegiver.getMovementFlight();
			movementGround = namegiver.getMovementGround();
		}
		HashMap<String, ATTRIBUTEType> attributes = getAttributes();
		ATTRIBUTEType strength = attributes.get("STR");
		switch(OptionalRule_AttributeBasedMovement) {
		case DEX:
			int modDex = attributes.get("DEX").getStep()-6;
			movementGround+=modDex;
			if(movementFlight>0) movementFlight+=modDex;
			break;
		case STR:
			int d=strength.getCurrentvalue()-strength.getRacevalue();
			int modStr=0;
			if( d < 0 ) {
				modStr = -1 - (int) ((((double)d)/2.0)+0.99);
			} else {
				modStr = (int) ((((double)d)/4.0)+0.99) - 1;
			}
			movementGround+=modStr;
			if(movementFlight>0) movementFlight+=modStr;
			break;
		case CHA:
			break;
		case PER:
			break;
		case TOU:
			break;
		case WIL:
			break;
		}
		MOVEMENTType movement = getMovement();
		movement.setFlight(movementFlight);
		movement.setGround(movementGround);

	}

	public void calculateCarrying() {
		CARRYINGType carrying = getCarrying();
		List<Integer> encumbrance = PROPERTIES_Characteristics.getENCUMBRANCE();
		int strength=getAttributes().get("STR").getCurrentvalue();
		if( strength<1 ) {
			// wenn Wert kleiner 1, dann keine Fehlermedung sondern einfach nur den Wert korrigieren 
			strength=1;
		}
		if( strength > encumbrance.size()) {
			strength = encumbrance.size();
			System.err.println("The strength attribute was out of range. The carrying value will now base on: "+strength);
		}
		Integer carryingValue = encumbrance.get(strength);
		carrying.setCarrying(carryingValue);
		carrying.setLifting(carryingValue *2);
	}

	public HashMap<String,ITEMType> getHashOfAllItems() {
		HashMap<String,ITEMType> result = new HashMap<String, ITEMType>();
		for( ITEMType item : character.getITEM() ) result.put( item.getName(), item );
		int pursecounter=0;
		for( COINSType coins : character.getCOINS() ) {
			String name = coins.getName();
			if( name == null ) {
				name = "Purse #"+String.valueOf(++pursecounter);
			} else {
				if( name.isEmpty() ) name = "Purse #"+String.valueOf(++pursecounter);
				else name = "Purse "+name;
			}
			name += " (c:"+coins.getCopper()+" s:"+coins.getSilver()+" g:"+coins.getGold();
			if( coins.getEarth()>0 )      name += " e:"+coins.getEarth();
			if( coins.getWater()>0 )      name += " w:"+coins.getWater();
			if( coins.getAir()>0 )        name += " a:"+coins.getAir();
			if( coins.getFire()>0 )       name += " f:"+coins.getFire();
			if( coins.getOrichalcum()>0 ) name += " o:"+coins.getOrichalcum();
			name +=")";
			result.put( name, coins );
		}
		for( ITEMType item : character.getWEAPON() ) result.put( item.getName(), item );
		PROTECTIONType protection = character.getPROTECTION();
		if( protection != null ) {
			boolean naturalArmor=true; // Der erste Eintrag ist immer die natürliche Rüstung
			for( ITEMType item : protection.getARMOROrSHIELD() ) {
				if( naturalArmor ) {
					// Die natürliche Rüstng nicht als Gegenstand auflisten
					naturalArmor=false;
					continue;
				}
				result.put( item.getName(), item );
			}
		}
		for( ITEMType item : character.getMAGICITEM() )      result.put( item.getName(), item );
		for( ITEMType item : character.getBLOODCHARMITEM() ) result.put( item.getName(), item );
		for( ITEMType item : character.getPATTERNITEM() )    result.put( item.getName(), item );
		for( ITEMType item : character.getTHREADITEM() )     result.put( item.getName(), item );
		return result;
	}

	public List<ITEMType> getAllNonVirtualItems() {
		List<ITEMType> result = new ArrayList<ITEMType>();
		for( ITEMType item : character.getITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getCOINS() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getWEAPON() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		PROTECTIONType protection = character.getPROTECTION();
		if( protection != null ) {
			for( ITEMType item : protection.getARMOROrSHIELD() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		}
		for( ITEMType item : character.getMAGICITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getBLOODCHARMITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getPATTERNITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		for( ITEMType item : character.getTHREADITEM() ) if( item.getVirtual().equals(YesnoType.NO) ) result.add( item );
		return result;
	}

	public List<Base64BinaryType> getPortrait() {
		List<Base64BinaryType> result = character.getPORTRAIT();
		if( result.isEmpty() ) {
			APPEARANCEType appearance = getAppearance();
			File[] files = new File("images/character").listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if( name==null ) return false;
					name=name.toLowerCase();
					if( !name.startsWith("portrait_") ) return false;
					if( name.endsWith(".jpg") ) return true;
					if( name.endsWith(".png") ) return true;
					if( name.endsWith(".gif") ) return true;
					return false;
				}
			});
			List<List<File>> filescore = new ArrayList<List<File>>();
			filescore.add(new ArrayList<File>()); // 0
			filescore.add(new ArrayList<File>()); // 1
			filescore.add(new ArrayList<File>()); // 2
			filescore.add(new ArrayList<File>()); // 3
			filescore.get(0).add(new File("images/character/portrait.jpg"));
			String race="_"+appearance.getRace().toLowerCase()+"_";
			String gender="_"+appearance.getGender().value().toLowerCase()+"_";
			String origin="_"+appearance.getOrigin().toLowerCase()+"_";
			for( File f : files ) {
				String name=f.getName().toLowerCase().replace(".", "_");
				if( name.contains(race) ) {
					int score=1;
					if( name.contains(gender) ) score++;
					if( name.contains(origin) ) score++;
					filescore.get(score).add(f);
				}
			}
			File file=null;
			for(int score=3;score>=0;score--) {
				if( file != null ) break;
				List<File> fileset = filescore.get(score);
				if( !fileset.isEmpty() ) file = fileset.get(rand.nextInt(fileset.size()));
			}
			if( file!=null ) try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fileInputStream.read(data);
				fileInputStream.close();
				Base64BinaryType base64bin = new Base64BinaryType();
				base64bin.setValue(data);
				final String[] filename = file.getName().split("\\.");
				base64bin.setContenttype("image/"+filename[filename.length-1]);
				result.add(base64bin);
			} catch (FileNotFoundException e) {
				// Wenn Datei nicht gefunden, dann Pech.
				System.err.println("can not insert default portrait : "+e.getLocalizedMessage());
			} catch (IOException e) {
				System.err.println("can not insert default portrait : "+e.getLocalizedMessage());
			}
		}
		return result;
	}

	public void readjustInitiativeModifikator(int adjustment, boolean armor) {
		INITIATIVEType initiative = getInitiative();
		if( armor ) {
			int currentarmorpenalty = initiative.getArmorpenalty();
			if( adjustment > currentarmorpenalty ) {
				initiative.setArmorpenalty(0);
				System.err.println("Armor penalty can not be negativ. Reduce initiative adjustment from "+adjustment+" to "+currentarmorpenalty);
				adjustment=currentarmorpenalty;
			} else {
				initiative.setArmorpenalty(currentarmorpenalty-adjustment);
			}
		}
		initiative.setModification(initiative.getModification()+adjustment);
		initiative.setStep(initiative.getBase()+initiative.getModification());
		initiative.setDice(PROPERTIES.step2Dice(initiative.getStep()));
		for( TalentsContainer talents : getAllTalents() ) {
			for( TALENTType talent : talents.getDisciplineAndOptionaltalents() ) readjustSkillInitiativeModifikator(talent,adjustment);
		}
		for( SKILLType skill : getSkills() ) readjustSkillInitiativeModifikator(skill,adjustment);
	}

	private static void readjustSkillInitiativeModifikator(SKILLType skill, int adjustment) {
		// Wenn der Skill gar keine Initiative-Skill ist, tut nichts
		if( ! skill.getIsinitiative().equals(YesnoType.YES) ) return;
		skill.setBonus(skill.getBonus()+adjustment);
		RANKType rank = skill.getRANK();
		if( rank == null ) {
			rank = new RANKType();
			skill.setRANK(rank);
		}
		rank.setBonus(rank.getBonus()+adjustment);
		rank.setStep(rank.getStep()+adjustment);
		rank.setDice(PROPERTIES.step2Dice(rank.getStep()));
	}

	public void clearLanguages() {
		character.getLANGUAGE().clear();
	}

	public LanguageContainer getDefaultLanguages() {
		String origin = getAppearance().getOrigin();
		String race = getAppearance().getRace();
		LanguageContainer defaultlanguages = new LanguageContainer(PROPERTIES.getDefaultLanguage(origin));
		for( NAMEGIVERABILITYType namegiver : PROPERTIES.getNamegivers() ) {
			if( namegiver.getName().equals(race) ) {
				defaultlanguages.insertLanguages(namegiver.getDEFAULTLANGUAGE());
			}
		}
		return defaultlanguages;
	}

	public LanguageContainer getLanguages() {
		LanguageContainer defaultlanguages = getDefaultLanguages().copy();
		int[] defaultCountOfSpeakReadWrite = defaultlanguages.getCountOfSpeakReadWrite(null);
		LanguageContainer languages = new LanguageContainer(character.getLANGUAGE());
		for( CHARACTERLANGUAGEType l : defaultlanguages.getLanguages() ) {
			int[] currentCountOfSpeakReadWrite = languages.getCountOfSpeakReadWrite(null);
			if( (currentCountOfSpeakReadWrite[0]>=defaultCountOfSpeakReadWrite[0]) &&
					(currentCountOfSpeakReadWrite[1]>=defaultCountOfSpeakReadWrite[1]) ) break;
			languages.insertLanguage(l);
		}
		return languages;
	}

	public void fillOptionalTalentsRandom(String disciplinename) {
		int circleNr = getCircleOf(disciplinename);
		while(true) {
			List<Integer> l = getCircleOfMissingOptionalTalents().get(disciplinename);
			if( l.isEmpty() ) break;
			int circle = l.get(0);
			DISCIPLINE discipline = PROPERTIES.getDisziplin(disciplinename);
			List<TALENTABILITYType> talentlist = getUnusedOptionalTalents(discipline,circle);
			// Suche nach dem Talent Unempfindlichkeit
			TALENTABILITYType talent=null;
			for( TALENTABILITYType t : talentlist ) {
				if( t.getName().equals(durabilityName) ) {
					talent=t;
					TALENTType newtalent = addOptionalTalent(disciplinename, circle, talent, false);
					newtalent.getRANK().setRank(circleNr);
					break;
				}
			}
			if( talent==null ) {
				talent = talentlist.get(spaeterzufall(talentlist.size()));
				TALENTType newtalent = addOptionalTalent(disciplinename, circle, talent, false);
				newtalent.getRANK().setRank(circleNr-spaeterzufall(circle));
			}
		}
	}

	public static int spaeterzufall(int bereich) {
		int g=1;
		for( int i=0; i<bereich; i++ ) g+=i;
		int r = rand.nextInt(g);
		g=0;
		for( int i=0; i<bereich; i++ ) {
			if( r<=g ) return i;
			g+=i;
		}
		return bereich-1;
	}

	public void clearBloodDamage() {
		HEALTHType health = getHealth();
		// Ermittle aktuellen Blutschaden um die Anpassung der Todes und Bewustlosigkeitsschwelle zu korrigieren
		int blooddamge=health.getBlooddamage();
		DEATHType death = getDeath();
		death.setValue(death.getValue()+blooddamge);
		death.setAdjustment(death.getAdjustment()+blooddamge);
		DEATHType unconsciousness = getUnconsciousness();
		unconsciousness.setValue(unconsciousness.getValue()+blooddamge);
		unconsciousness.setAdjustment(unconsciousness.getAdjustment()+blooddamge);
		// Jetzt setze den Blutschaden auf 0
		health.setBlooddamage(0);
		// Ohne Blutschaden gibts auch keine "Zerfallsrate"
		health.setDepatterningrate(0);
	}

	public void addBloodDamgeFrom(ITEMType item) {
		HEALTHType health = getHealth();
		int itemBloodDamge = item.getBlooddamage();
		health.setBlooddamage(health.getBlooddamage()+itemBloodDamge);
		health.setDepatterningrate(health.getDepatterningrate()+item.getDepatterningrate());
		DEATHType death = health.getDEATH();
		death.setAdjustment(death.getAdjustment()-itemBloodDamge);
		DEATHType unconsciousness = health.getUNCONSCIOUSNESS();
		unconsciousness.setAdjustment(unconsciousness.getAdjustment()-itemBloodDamge);
	}

	public void insertKnack(KNACKBASEType knack) {
		String limitation=knack.getLimitation();
		String knackname = knack.getName();
		boolean noLimitation=limitation.isEmpty();
		for( TALENTType talent : getTalentByName(knack.getBasename()) ) {
			if( noLimitation || talent.getLimitation().equals(limitation) ) {
				List<KNACKType> talentknacks = talent.getKNACK();
				for( KNACKType k : talentknacks ) {
					if( k.getName().equals(knackname) ) return;
				}
				KNACKType k = new KNACKType();
				k.setName(knackname);
				k.setMinrank(knack.getMinrank());
				k.setStrain(knack.getStrain());
				k.setBookref(knack.getBookref());
				talentknacks.add(k);
				return;
			}
		}
	}
}
