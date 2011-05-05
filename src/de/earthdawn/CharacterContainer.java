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
import de.earthdawn.config.ECECharacteristics;
import de.earthdawn.data.*;
import de.earthdawn.event.CharChangeRefresh;

public class CharacterContainer extends CharChangeRefresh {
	private EDCHARACTER character = null;
	public final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public final ECECharacteristics PROPERTIES_Characteristics= PROPERTIES.getCharacteristics();
	public final ATTRIBUTENameType OptionalRule_AttributeBasedMovement=PROPERTIES.getOptionalRules().getATTRIBUTEBASEDMOVEMENT().getAttribute();

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
		appearance.setGender(GenderType.MALE);
		appearance.setEyes("blue");
		appearance.setAge(20);
		appearance.setHair("blond");
		appearance.setHeight(170);
		appearance.setSkin("blond");
		appearance.setWeight(80);
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

	public CALCULATEDLEGENDPOINTSType resetCalculatedLegendpoints() {
		CALCULATEDLEGENDPOINTSType calculatedLP = getCalculatedLegendpoints();
		int attributes=0;
		int disciplinetalents=0;
		int karma=0;
		int knacks=0;
		int magicitems=0;
		int optionaltalents=0;
		int skills=0;
		int spells=0;
		for( CALCULATEDLEGENDPOINTADJUSTMENTType adjustment : calculatedLP.getCOMMONADJUSTMENT() ) {
			switch(adjustment.getType()) {
			case ATTRIBUTES:        attributes       +=adjustment.getValue(); break;
			case DISCIPLINETALENTS: disciplinetalents+=adjustment.getValue(); break;
			case KARMA:             karma            +=adjustment.getValue(); break;
			case KNACKS:            knacks           +=adjustment.getValue(); break;
			case MAGICITEMS:        magicitems       +=adjustment.getValue(); break;
			case OPTIONALTALENTS:   optionaltalents  +=adjustment.getValue(); break;
			case SKILLS:            skills           +=adjustment.getValue(); break;
			case SPELLS:            spells           +=adjustment.getValue(); break;
			}
		}
		for( NEWDISCIPLINETALENTADJUSTMENTType adjustment : calculatedLP.getNEWDISCIPLINETALENTADJUSTMENT() ) {
			switch(adjustment.getType()) {
			case ATTRIBUTES:        attributes       +=adjustment.getValue(); break;
			case DISCIPLINETALENTS: disciplinetalents+=adjustment.getValue(); break;
			case KARMA:             karma            +=adjustment.getValue(); break;
			case KNACKS:            knacks           +=adjustment.getValue(); break;
			case MAGICITEMS:        magicitems       +=adjustment.getValue(); break;
			case OPTIONALTALENTS:   optionaltalents  +=adjustment.getValue(); break;
			case SKILLS:            skills           +=adjustment.getValue(); break;
			case SPELLS:            spells           +=adjustment.getValue(); break;
			}
		}
		calculatedLP.setAttributes(attributes);
		calculatedLP.setDisciplinetalents(disciplinetalents);
		calculatedLP.setKarma(karma);
		calculatedLP.setKnacks(knacks);
		calculatedLP.setMagicitems(magicitems);
		calculatedLP.setOptionaltalents(optionaltalents);
		calculatedLP.setSkills(skills);
		calculatedLP.setSpells(spells);
		return calculatedLP;
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
		// Solange die letzte Disziplin keinen Kreis hat, wird diese entfernt. 
		while( (! disciplines.isEmpty()) && (disciplines.get(disciplines.size()-1).getCircle()<1) ) {
			disciplines.remove(disciplines.size()-1);
		}
		// Bei allen anderen Disziplinen die keinen Kreis haben wird dieser auf 1 gesetzt.
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

	public List<Integer> getDisciplineCircles() {
		List<Integer> result = new ArrayList<Integer>();
		for( DISCIPLINEType discipline : getDisciplines() ) {
			result.add(discipline.getCircle());
		}
		return result;
	}

	public HashMap<String,Integer> getDisciplineOrderByName() {
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		int order=1;
		for (DISCIPLINEType discipline : getDisciplines()) result.put(discipline.getName(),order++);
		return result;
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

	public DISCIPLINEType getDiciplineMaxCircle() {
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setCircle(0);
		discipline.setName("na");
		for( DISCIPLINEType d : getDisciplines() ) {
			if( d.getCircle() > discipline.getCircle() ) {
				discipline=d;
			}
		}
		return discipline;
	}

	public List<TALENTSType> getAllTalents() {
		return character.getTALENTS();
	}

	public HashMap<Integer,TALENTSType> getAllTalentsByDisziplinOrder() {
		HashMap<String, Integer> disciplines = getDisciplineOrderByName();
		// Hole nun alle TalentListen und speichere sie in der Diszipline Reihnfolge in eine HashMap.
		HashMap<Integer,TALENTSType> result = new HashMap<Integer,TALENTSType>();
		List<TALENTSType> allTalents = getAllTalents();
		List<TALENTSType> remove = new ArrayList<TALENTSType>();
		for (TALENTSType talents : allTalents ) {
			Integer disciplineOrder = disciplines.get(talents.getDiscipline());
			if( disciplineOrder == null ) {
				System.err.println("Remove talents of the discipline '"+talents.getDiscipline()+"' with no discipline entry.");
				remove.add(talents);
			} else {
				result.put(disciplineOrder,talents);
			}
		}
		allTalents.removeAll(remove);
		return result;
	}

	public HashMap<String,TALENTSType> getAllTalentsByDisziplinName() {
		HashMap<String,TALENTSType> alltalents = new HashMap<String,TALENTSType>();
		for (TALENTSType talents : getAllTalents() ) {
			alltalents.put(talents.getDiscipline(),talents);
		}
		return alltalents;
	}

	public List<TALENTType> getTalentByName(String searchTalent) {
		List<TALENTType> result = new ArrayList<TALENTType>();
		for (TALENTSType talents : character.getTALENTS()) {
			for (TALENTType talent : talents.getDISZIPLINETALENT()) {
				if ( talent.getName().equals(searchTalent)) {
					result.add(talent);
				}
			}
			for (TALENTType talent : talents.getOPTIONALTALENT()) {
				if ( talent.getName().equals(searchTalent)) {
					result.add(talent);
				}
			}
		}
		return result;
	}

	public TALENTType getTalentByDisciplinAndName(String disciplin, String searchTalent) {
		for (TALENTSType talents : character.getTALENTS()) {
			if (talents.getDiscipline().equals(disciplin)){
				for (TALENTType talent : talents.getDISZIPLINETALENT()) {
					if ( talent.getName().equals(searchTalent)) {
						return talent;
					}
				}
				List<TALENTType> optionaltalents = talents.getOPTIONALTALENT();
				List<TALENTType> remove = new ArrayList<TALENTType>();
				TALENTType result = null;
				for( TALENTType talent : optionaltalents ) {
					RANKType rank = talent.getRANK();
					if( (rank == null) || (rank.getRank() < 1) ) {
						remove.add(talent);
						continue;
					}
					if ( talent.getName().equals(searchTalent)) {
						result=talent;
					}
				}
				optionaltalents.removeAll(remove);
				if( result != null ) return result;
			}
		}
		// Not found
		return null;
	}	
	
	public List<SKILLType> getSkills() {
		return character.getSKILL();
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
			allCoins.add(coins);
		}
		return allCoins;
	}

	public List<SPELLSType> getAllSpells() {
		return character.getSPELLS();
	}

	public HashMap<Integer,SPELLSType> getAllSpellsByDisziplinOrder() {
		// Erstelle zu erst eine Liste von Disziplinen
		HashMap<String,Integer> alldisciplines = getDisciplineOrderByName();
		// Hole nun alle SpellListen und speichere sie in der Diszipline Reihnfolge in eine HashMap.
		HashMap<Integer,SPELLSType> result = new HashMap<Integer,SPELLSType>();
		List<SPELLSType> allSpells = getAllSpells();
		List<SPELLSType> remove = new ArrayList<SPELLSType>();
		for (SPELLSType spells : allSpells ) {
			Integer disciplineOrder = alldisciplines.get(spells.getDiscipline());
			if( disciplineOrder == null ) {
				System.err.println("Remove spells of the discipline '"+spells.getDiscipline()+"' with no discipline entry.");
				remove.add(spells);
			} else {
				result.put(disciplineOrder,spells);
			}
		}
		allSpells.removeAll(remove);
		return result;
	}

	public HashMap<String,List<List<TALENTType>>> getUsedOptionalTalents() {
		HashMap<String,List<List<TALENTType>>> result = new HashMap<String,List<List<TALENTType>>>();
		for(TALENTSType talents : getAllTalents() ) {
			List<List<TALENTType>> list = new ArrayList<List<TALENTType>>();
			for(int i=0;i<20;i++) list.add(new ArrayList<TALENTType>());
			for( TALENTType talent : talents.getOPTIONALTALENT()) {
				if( talent.getRealigned() < 1 ) list.get(talent.getCircle()).add(talent);
			}
			result.put(talents.getDiscipline(), list);
		}
		return result;
	}

	public static String getFullTalentname(TALENTType talent) {
		String name = talent.getName();
		String limitation = talent.getLimitation();
		if( limitation == null ) return name;
		if( limitation.isEmpty() ) return name;
		return name + " : "+limitation;
	}

	public static String getFullTalentname(TALENTABILITYType talent) {
		String name = talent.getName();
		String limitation = talent.getLimitation();
		if( limitation == null ) return name;
		if( limitation.isEmpty() ) return name;
		return name + " : "+limitation;
	}

	private static void collectTalentsByMultiUse(List<String> usedTalents, HashMap<String, Integer> multiUseTalents, TALENTType talent, boolean isdisciplinetalent) {
		String name = getFullTalentname(talent);
		Integer multiUseCount = multiUseTalents.get(name);
		if( multiUseCount == null ) {
			// Wenn es kein MultiUseTalent ist, dann behandele es ganz normal
			// und füge es in die Liste der Benutzen Talent hinzu
			usedTalents.add(name);
		} else {
			// Wenn es sich aber um ein MultiUseTalent handelt, Zähle den MultiUse-Zähler hinunter,
			// es sei denn er ist bereits auf Eins, dann füge das Talent in die Liste der Benutzen Talent hinzu
			if( multiUseCount > 1 ) multiUseCount--;
			else usedTalents.add(name);
			// Aktuallisiere den MultiUse-Zähler bzw lösche das Talent aus der MultiUse Liste
			if( multiUseCount > 0 ) multiUseTalents.put(name,multiUseCount);
			else multiUseTalents.remove(name);
		}
	}

	public List<TALENTABILITYType> getUnusedOptionalTalents(DISCIPLINE disciplineDefinition, int talentCircleNr) {
		List<TALENTABILITYType> result = new ArrayList<TALENTABILITYType>();
		List<String> usedTalents = new ArrayList<String>();
		HashMap<String, Integer> multiUseTalents = PROPERTIES.getMultiUseTalents();
		String disciplineName = disciplineDefinition.getName();
		for( TALENTSType talents : getAllTalents() ) {
			if( disciplineName.equals(talents.getDiscipline())) {
				// Disziplintalente der selben Disziplin reduzieren zwar den multiUse-Zähler
				// werden aber in keinem Fall in die usedTalents-Liste aufgenommen
				for( TALENTType talent : talents.getDISZIPLINETALENT() ) {
					String name = getFullTalentname(talent);
					Integer multiUseCount = multiUseTalents.get(name);
					if( multiUseCount != null ) {
						multiUseCount--;
						multiUseTalents.put(name,multiUseCount);
					}
				}
				// Optionaltalente der selben Disziplin reduzieren den multiUse-Zähler
				// UND werden in jedem Fall in die usedTalents-Liste aufgenommen
				for( TALENTType talent : talents.getOPTIONALTALENT() ) {
					String name = getFullTalentname(talent);
					usedTalents.add(name);
					Integer multiUseCount = multiUseTalents.get(name);
					if( multiUseCount != null ) {
						multiUseCount--;
						multiUseTalents.put(name,multiUseCount);
					}
				}
			} else {
				for( TALENTType talent : talents.getDISZIPLINETALENT() ) {
					collectTalentsByMultiUse(usedTalents, multiUseTalents, talent, true);
				}
				for( TALENTType talent : talents.getOPTIONALTALENT() ) {
					collectTalentsByMultiUse(usedTalents, multiUseTalents, talent, false);
				}
			}
		}
		int circlenr = 0;
		for( DISCIPLINECIRCLEType disciplineCircle : disciplineDefinition.getCIRCLE() ) {
			circlenr++;
			if( circlenr > talentCircleNr ) break;
			for( TALENTABILITYType talent : disciplineCircle.getOPTIONALTALENT()) {
				String name = getFullTalentname(talent);
				if( usedTalents.contains(name) ) {
					usedTalents.remove(name);
				} else {
					result.add(talent);
				}
			}
		}
		return result;
	}

	public HashMap<String,List<Integer>> getCircleOfMissingOptionalTalents() {
		HashMap<String,List<Integer>> result = new HashMap<String,List<Integer>>();
		HashMap<String,List<List<TALENTType>>> talentsMap = getUsedOptionalTalents();
		HashMap<String, Integer> disciplineOrderByName = getDisciplineOrderByName();
		for(String discipline : talentsMap.keySet() ) {
			List<Integer> list = new ArrayList<Integer>();
			List<List<TALENTType>> talentsList = talentsMap.get(discipline);
			Integer disciplineNumber = disciplineOrderByName.get(discipline);
			if( talentsList == null ) {
				System.err.println("A talent list for the discipline '"+discipline+"' could not be found.");
				talentsList = new ArrayList<List<TALENTType>>();
				for(int i=0;i<20;i++) talentsList.add(new ArrayList<TALENTType>());
			}
			HashMap<String, Integer> defaultOptionalTalents = PROPERTIES.getDefaultOptionalTalents(disciplineNumber);
			int disciplineCircle = getCircleOf(disciplineNumber);
			int circlenr=0;
			for( int numberOfOptionalTalents : PROPERTIES.getNumberOfOptionalTalentsPerCircleByDiscipline(discipline) ) {
				circlenr++;
				if( circlenr > disciplineCircle ) break;
				int freeOptionalTalents=numberOfOptionalTalents;
				List<TALENTType> talents = new ArrayList<TALENTType>();
				for( TALENTType talent : talentsList.get(circlenr) ) {
					Integer c = defaultOptionalTalents.get(talent.getName());
					if( (c!=null) && (c<=circlenr) ) continue;
					talents.add(talent);
				}
				freeOptionalTalents-=isNotLearnedByVersatility(talents);
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
		TALENTSType talents =  new TALENTSType();
		talents.setDiscipline(name);
		character.getDISCIPLINE().add(discipline);
		character.getTALENTS().add(talents);
		ensureDisciplinTalentsExits();
		realignOptionalTalents();
	}

	public void ensureDisciplinTalentsExits() {
		HashMap<String, Integer> disciplineOrderByName = getDisciplineOrderByName();
		List<DISCIPLINEType> allDiciplines = character.getDISCIPLINE();
		List<String> totalListOfDisciplineTalents = new ArrayList<String>();
		List<TALENTSType> allTalents = getAllTalents();
		for( TALENTSType talents : allTalents ) {
			for( TALENTType talent : talents.getDISZIPLINETALENT() ) {
				totalListOfDisciplineTalents.add(getFullTalentname(talent));
			}
		}
		for( TALENTSType talents : allTalents ) {
			String disciplineName = talents.getDiscipline();
			Integer disciplineOrder = disciplineOrderByName.get(disciplineName);
			// Wenn es zu der Talentliste keinen zugehörigen Diszipline eintrag gibt, dann über springe diese Talentliste
			if( disciplineOrder == null ) continue;
			DISCIPLINE disciplineDefinition = PROPERTIES.getDisziplin(disciplineName);
			// Wenn es zu der Disziplin der Talentliste keine Disziplindefinition gibt, dann über springe diese Talentliste
			if( disciplineDefinition == null ) continue;
			DISCIPLINEType discipline = allDiciplines.get(disciplineOrder-1);
			// Eigentlich kann das nicht mehr "null" sein, da disciplineOrder definiert ist.
			if( discipline == null ) continue;
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
						talents.getDISZIPLINETALENT().add(newTalent);
						totalListOfDisciplineTalents.add(newFullTalentName);
					}
				}
			}
		}
	}

	public void realignOptionalTalents() {
		final String durabilityName = PROPERTIES.getDurabilityName();
		HashMap<String, Integer> disciplineOrderByName = getDisciplineOrderByName();
		List<DISCIPLINEType> allDiciplines = character.getDISCIPLINE();
		List<TALENTSType> allTalents = getAllTalents();
		int maxDisciplineOrder=allDiciplines.size();
		for( TALENTSType talents1 : allTalents ) {
			String disciplineName = talents1.getDiscipline();
			Integer disciplineOrder = disciplineOrderByName.get(disciplineName);
			// Wenn es zu der Talentliste keinen zugehörigen Diszipline eintrag gibt, dann über springe diese Talentliste
			if( disciplineOrder == null ) continue;
			DISCIPLINEType discipline = allDiciplines.get(disciplineOrder-1);
			// Eigentlich kann das nicht mehr "null" sein, da disciplineOrder definiert ist.
			if( discipline == null ) continue;
			for( TALENTType disTalent : talents1.getDISZIPLINETALENT() ) {
				// Disziplinetalente können nicht realigned werden, daher ist auch keine gesonderte Prüfung
				// ob dieses Talent bereits ge-realigned ist nicht notwendig und unsinnig
				String disTalentName=getFullTalentname(disTalent);
				for( TALENTSType talents2 : allTalents ) {
					// Talentlisten nicht mit sich selbst vergleichen
					if( talents1 == talents2 ) continue;
					for( TALENTType optTalent : talents2.getOPTIONALTALENT() ) {
						// Sollte das Optinaltalent auf ein Talent einer nicht mehr exisiterenden Disziplin realgined sind,
						// kann dieses Realgined entfernt werden
						if( optTalent.getRealigned() > maxDisciplineOrder ) optTalent.setRealigned(0);
						if( durabilityName.equals(disTalent.getName()) ) {
							if( durabilityName.equals(optTalent.getName()) ) optTalent.setRealigned(disciplineOrder);
						} else {
							if( disTalentName.equals(getFullTalentname(optTalent)) ) optTalent.setRealigned(disciplineOrder);
						}
					}
				}
			}
		}
	}

	public void addOptionalTalent(String disciplineName, int circle, TALENTABILITYType talenttype, boolean byVersatility){
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

		// Wenn es sich bei dem neuen OptionalTalent um das Vielseitigkeitstalent handelt,
		// dann muss geprüft werden ob dieses bereits in einder anderen Disziplin vorhanden ist.
		Integer disciplineOrder = getDisciplineOrderByName().get(disciplineName);
		if( disciplineOrder != null ) {
			final String durabilityName = PROPERTIES.getDurabilityName();
			if( durabilityName.equals(talent.getName()) ) {
				for( TALENTSType talents : getAllTalents() ) {
					// Wenn die Disziplin namen über einstimmen, dann handelt es sich um die selbe Disziplin
					// und es brauch nichts geprüft werden.
					if( disciplineName.equals(talents.getDiscipline()) ) continue;
					for( TALENTType optTalent : talents.getOPTIONALTALENT() ) {
						if( ! durabilityName.equals(optTalent.getName()) ) continue;
						optTalent.setRealigned(disciplineOrder);
					}
				}
			}
		}

		getAllTalentsByDisziplinName().get(disciplineName).getOPTIONALTALENT().add(talent);
	}
	
	public void addSpell(String discipline, SPELLType spell){
		SPELLSType spells = null;
		for( SPELLSType spellstype : getAllSpells()){
			if(spellstype.getDiscipline().equals(discipline)){
				spells = spellstype;
				break;
			}
		}
		if( spells == null ){
			System.out.println("SPELLS not found, adding ...");
			spells = new SPELLSType();
			spells.setDiscipline(discipline);
			character.getSPELLS().add(spells);
		}
		spells.getSPELL().add(spell);
	}
	
	
	
	public void removeSpell(String discipline, SPELLType spell){
		boolean blnFound = false;
		SPELLSType spells = null;
		SPELLType spelltoremove = null;
 		for( SPELLSType spellstype : getAllSpells()){
			if(spellstype.getDiscipline().equals(discipline)){
				blnFound = true;
				spells = spellstype;
				break;
			}
		}
		if(blnFound){
			for(SPELLType currentspell : spells.getSPELL()){
				if(currentspell.getName().equals(spell.getName())){
					spelltoremove = currentspell;
					break;
				}
			}
			spells.getSPELL().remove(spelltoremove);
		}
		
	}
	
	public boolean hasSpellLearned(String discipline, SPELLType spelltype){
		List<SPELLSType> spellslist = getAllSpells();
		boolean blnFound = false;
		for(SPELLSType spells : spellslist){
			if(spells.getDiscipline().equals(discipline)){
				for(SPELLType spell : spells.getSPELL()){
					if (spell.getName().equals(spelltype.getName())){
						blnFound = true;
					}
				}
				break;
			}
		}
		
		return blnFound;
	}

	public List<ITEMType> getItems() {
		return character.getITEM();
	}

	public String getDESCRIPTION() {
		String result=character.getDESCRIPTION();
		if( result == null ) return "";
		return result;
	}

	public List<MAGICITEMType> getMagicItem() {
		return character.getMAGICITEM();
	}

	public List<THREADITEMType> getThreadItem() {
		return character.getTHREADITEM();
	}

	public List<BLOODCHARMITEMType> getBloodCharmItem() {
		return character.getBLOODCHARMITEM();
	}

	public List<ARMORType> getMagicArmor() {
		List<ARMORType> magicarmor = new ArrayList<ARMORType>();
		int calculatedLP=0;
		for( THREADITEMType magicitem : getThreadItem() ) {
			String name = magicitem.getName();
			float weight = magicitem.getWeight();
			YesnoType used = magicitem.getUsed();
			int weaven = magicitem.getWeaventhreadrank();
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
					if( weaven > 0 ) newmagicarmor=armor;
				}
				SHIELDType shield = threadrank.getSHIELD();
				if( shield != null ) {
					shield.setName(name);
					shield.setWeight(weight);
					shield.setUsed(used);
					if( weaven > 0 ) newmagicshield=shield;
				}
				if( weaven > 0 ) calculatedLP+=threadrank.getLpcost();
				weaven--;
			}
			if( (newmagicarmor != null) && newmagicarmor.getUsed().equals(YesnoType.YES) ) magicarmor.add(newmagicarmor);
			if( (newmagicshield != null) && newmagicshield.getUsed().equals(YesnoType.YES) ) magicarmor.add(newmagicshield);
		}
		character.getCALCULATEDLEGENDPOINTS().setMagicitems(calculatedLP);
		return magicarmor;
	}

	public List<ARMORType> cutMagicArmornFromNormalArmorList() {
		List<ARMORType> magicArmor = getMagicArmor();
		List<ARMORType> normalArmorList = character.getPROTECTION().getARMOROrSHIELD();
		List<ARMORType> delete = new ArrayList<ARMORType>();
		for( ARMORType armor : normalArmorList) {
			String armorName = armor.getName();
			for( ARMORType a : magicArmor ) {
				if( armorName.equals(a.getName()) ) delete.add(armor);
			}
		}
		normalArmorList.removeAll(delete);
		return magicArmor;
	}

	public List<WEAPONType> getMagicWeapon() {
		List<WEAPONType> magicweapon = new ArrayList<WEAPONType>();
		int calculatedLP=0;
		for( THREADITEMType magicitem : getThreadItem() ) {
			String name = magicitem.getName();
			float weight = magicitem.getWeight();
			YesnoType used = magicitem.getUsed();
			int weaven = magicitem.getWeaventhreadrank();
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
					if( weaven > 0 ) newmagicweapon=weapon;
				}
				if( weaven > 0 ) calculatedLP+=threadrank.getLpcost();
				weaven--;
			}
			if( newmagicweapon != null ) magicweapon.add(newmagicweapon);
		}
		character.getCALCULATEDLEGENDPOINTS().setMagicitems(calculatedLP);
		return magicweapon;
	}

	public List<WEAPONType> cutMagicWeaponFromNormalWeaponList() {
		List<WEAPONType> magicWeapon = getMagicWeapon();
		List<WEAPONType> normalWeaponList = character.getWEAPON();
		List<WEAPONType> delete = new ArrayList<WEAPONType>();
		for( WEAPONType weapon : normalWeaponList) {
			String weaponName = weapon.getName();
			for( WEAPONType w : magicWeapon ) {
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
		List<TALENTSType> allTalents = getAllTalents();
		int result = 0;
		if( allTalents == null ) return result;
		for( TALENTSType talents : allTalents ) {
			result += isLearnedByVersatility(talents.getDISZIPLINETALENT());
			result += isLearnedByVersatility(talents.getOPTIONALTALENT());
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
		skills.removeAll(remove);
	}

	public void updateRealignedTalents() {
		HashMap<Integer,TALENTSType> allTalents = getAllTalentsByDisziplinOrder();
		HashMap<String, List<TALENTType>> realignedTalentHash = new HashMap<String, List<TALENTType>>();
		for( Integer disciplinenumber : allTalents.keySet() ) {
			insertIfRealigned(realignedTalentHash, allTalents.get(disciplinenumber).getDISZIPLINETALENT());
			insertIfRealigned(realignedTalentHash, allTalents.get(disciplinenumber).getOPTIONALTALENT());
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
		for( TALENTSType talents : getAllTalents() ) {
			List<TALENTType> rankZeroTalents = new ArrayList<TALENTType>();
			List<TALENTType> optionalTalents = talents.getOPTIONALTALENT();
			for( TALENTType talent : optionalTalents ) {
				RANKType rank = talent.getRANK();
				if( (rank == null) || (rank.getRank() < 1) ) rankZeroTalents.add(talent);
			}
			optionalTalents.removeAll(rankZeroTalents);
		}
	}

	public void removeIllegalTalents() {
		HashMap<String, Integer> disciplineOrderByName = getDisciplineOrderByName();
		List<DISCIPLINEType> allDiciplines = character.getDISCIPLINE();
		for( TALENTSType talents : getAllTalents() ) {
			int disciplineCircleNr = 0;
			String disciplineName = talents.getDiscipline();
			Integer disciplineOrder = disciplineOrderByName.get(disciplineName);
			if( disciplineOrder != null ) {
				DISCIPLINEType discipline = allDiciplines.get(disciplineOrder-1);
				// Eigentlich kann das nicht mehr "null" sein, da disciplineOrder definiert ist.
				if( discipline != null ) {
					disciplineCircleNr = discipline.getCircle();
				}
			}
			List<TALENTType> remove = new ArrayList<TALENTType>();
			List<TALENTType> disciplineTalents = talents.getDISZIPLINETALENT();
			for( TALENTType talent : disciplineTalents ) {
				if( talent.getCircle() > disciplineCircleNr ) remove.add(talent);
			}
			disciplineTalents.removeAll(remove);
			remove.clear();
			List<TALENTType> optionalTalents = talents.getDISZIPLINETALENT();
			for( TALENTType talent : optionalTalents ) {
				if( talent.getCircle() > disciplineCircleNr ) remove.add(talent);
			}
			optionalTalents.removeAll(remove);
		}
	}

	public NAMEGIVERABILITYType getRace() {
		String race = getAppearance().getRace();
		for (NAMEGIVERABILITYType n : PROPERTIES.getNamegivers().getNAMEGIVER()) {
			if( n.getName().equals(race)) return n;
		}
		// not found
		return null;
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
}
