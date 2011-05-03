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
import de.earthdawn.data.*;
import de.earthdawn.event.CharChangeRefresh;

public class CharacterContainer extends CharChangeRefresh {
	private EDCHARACTER character = null;

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
		for( CALCULATEDLEGENDPOINTADJUSTMENTType adjustment : calculatedLP.getADJUSTMENT() ) {
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

	public HashMap<Integer,DISCIPLINEType> getAllDiciplinesByOrder() {
		HashMap<Integer,DISCIPLINEType> alldisciplines = new HashMap<Integer,DISCIPLINEType>();
		for (DISCIPLINEType discipline : character.getDISCIPLINE()) {
			alldisciplines.put(discipline.getOrder(),discipline);
		}
		return alldisciplines;
	}

	public HashMap<String,DISCIPLINEType> getAllDiciplinesByName() {
		HashMap<String,DISCIPLINEType> alldisciplines = new HashMap<String,DISCIPLINEType>();
		for (DISCIPLINEType discipline : character.getDISCIPLINE()) {
			alldisciplines.put(discipline.getName(),discipline);
		}
		return alldisciplines;
	}

	public int getCircleOf(String discipline) {
		DISCIPLINEType usedDiscipline = getAllDiciplinesByName().get(discipline);
		if( usedDiscipline == null ) {
			System.err.println("No discipline '"+discipline+"' is in use.");
			return 0;
		} else {
			return usedDiscipline.getCircle();
		}
	}

	public DISCIPLINEType getDiciplineMaxCircle() {
		DISCIPLINEType discipline = new DISCIPLINEType();
		discipline.setCircle(0);
		discipline.setName("na");
		discipline.setOrder(1);
		for (DISCIPLINEType d : character.getDISCIPLINE()) {
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
		// Erstelle zu erst eine Liste von Disziplinen
		HashMap<String,DISCIPLINEType> alldisciplines = getAllDiciplinesByName();
		// Hole nun alle TalentListen und speichere sie in der Diszipline Reihnfolge in eine HashMap.
		HashMap<Integer,TALENTSType> alltalents = new HashMap<Integer,TALENTSType>();
		for (TALENTSType talents : getAllTalents() ) {
			DISCIPLINEType discipline = alldisciplines.get(talents.getDiscipline());
			if( discipline == null ) {
				System.err.println("Could not find a discipline entry for the talents of the discipline '"+talents.getDiscipline()+"'");
			} else {
				alltalents.put(discipline.getOrder(),talents);
			}
		}
		return alltalents;
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
		HashMap<String,DISCIPLINEType> alldisciplines = getAllDiciplinesByName();
		// Hole nun alle TalentListen und speichere sie in der Diszipline Reihnfolge in eine HashMap.
		HashMap<Integer,SPELLSType> allspells = new HashMap<Integer,SPELLSType>();
		for (SPELLSType spells : getAllSpells() ) {
			allspells.put(alldisciplines.get(spells.getDiscipline()).getOrder(),spells);
		}
		return allspells;
	}
	public HashMap<String,List<List<TALENTType>>> getUsedOptionalTalents() {
		HashMap<String,List<List<TALENTType>>> result = new HashMap<String,List<List<TALENTType>>>();
		for(TALENTSType talents : getAllTalents() ) {
			List<List<TALENTType>> list = new ArrayList<List<TALENTType>>();
			for(int i=0;i<20;i++) list.add(new ArrayList<TALENTType>());
			for( TALENTType talent : talents.getOPTIONALTALENT()) {
				list.get(talent.getCircle()).add(talent);
			}
			result.put(talents.getDiscipline(), list);
		}
		return result;
	}

	public List<TALENTABILITYType> getUnusedOptionalTalents(DISCIPLINE discipline, int circle) {
		List<TALENTABILITYType> result = new ArrayList<TALENTABILITYType>();
		List<List<TALENTType>> usedOptionalTalentsList = getUsedOptionalTalents().get(discipline.getName());
		if( usedOptionalTalentsList == null ) {
			System.err.println("No Used Optinal Talents found for discipline '"+discipline.getName()+"'");
			usedOptionalTalentsList = new ArrayList<List<TALENTType>>();
			for(int i=0;i<20;i++) usedOptionalTalentsList.add(new ArrayList<TALENTType>());
		}
		int circlenr = 0;
		for( DISCIPLINECIRCLEType disciplineCircle : discipline.getCIRCLE() ) {
			circlenr++;
			if( circlenr > circle ) break;
			for( TALENTABILITYType talent :disciplineCircle.getOPTIONALTALENT()) {
				boolean found=false;
				for( List<TALENTType> usedOptionalTalents : usedOptionalTalentsList ) {
					for( TALENTType usedOptionalTalent : usedOptionalTalents ) {
						if( talent.getName().equals(usedOptionalTalent.getName()) ) {
							found=true;
							usedOptionalTalents.remove(usedOptionalTalent);
							break;
						}
						if( found ) break;
					}
				}
				if( ! found ) result.add(talent);
			}
		}
		return result;
	}

	public static List<Integer> getNumberOfOptionalTalentsPerCircle(String discipline) {
		return getNumberOfOptionalTalentsPerCircle(ApplicationProperties.create().getDisziplin(discipline));
	}

	public static List<Integer> getNumberOfOptionalTalentsPerCircle(DISCIPLINE discipline) {
		List<Integer> result = new ArrayList<Integer>();
		if( discipline == null ) return result;
		List<DISCIPLINECIRCLEType> circleList = discipline.getCIRCLE();
		if( circleList == null ) return result;
		for( DISCIPLINECIRCLEType circle : circleList ) {
			result.add(circle.getTALENTABILITY().getCount());
		}
		return result;
	}

	public HashMap<String,List<Integer>> getCircleOfMissingOptionalTalents() {
		HashMap<String,List<Integer>> result = new HashMap<String,List<Integer>>();
		HashMap<String,List<List<TALENTType>>> talentsMap = getUsedOptionalTalents();
		for(String discipline : talentsMap.keySet() ) {
			List<Integer> list = new ArrayList<Integer>();
			List<List<TALENTType>> talentsList = talentsMap.get(discipline);
			if( talentsList == null ) {
				System.err.println("A talent list for the discipline '"+discipline+"' could not be found.");
				talentsList = new ArrayList<List<TALENTType>>();
				for(int i=0;i<20;i++) talentsList.add(new ArrayList<TALENTType>());
			}
			int disciplineCircle = getCircleOf(discipline);
			int circlenr=0;
			for( int numberOfOptionalTalents : getNumberOfOptionalTalentsPerCircle(discipline) ) {
				circlenr++;
				if( circlenr > disciplineCircle ) break;
				int freeOptionalTalents=numberOfOptionalTalents;
				freeOptionalTalents-=isNotLearnedByVersatility(talentsList.get(circlenr));
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
		List<DISCIPLINEType> disciplines = character.getDISCIPLINE();
		if( (disciplines.size() < 3) && (getAllDiciplinesByName().get(name) == null) ){
			DISCIPLINEType discipline = new DISCIPLINEType();
			discipline.setName(name);
			discipline.setCircle(1);
			discipline.setOrder(disciplines.size() +1);
			disciplines.add(discipline);
			TALENTSType talents =  new TALENTSType();
			talents.setDiscipline(name);
			character.getTALENTS().add(talents);
			initDisciplinTalents(name,1);
		}
		
	}

	public void initDisciplinTalents(String disciplinename, int circle){
		DISCIPLINE d = ApplicationProperties.create().getDisziplin(disciplinename);
		TALENTSType talents = getAllTalentsByDisziplinName().get(disciplinename);
		int circlenr=0;
		for( DISCIPLINECIRCLEType disciplincircle : d.getCIRCLE() ) {
			circlenr++;
			if( circlenr > circle ) break;
			for( TALENTABILITYType disciplinetalent :disciplincircle.getDISCIPLINETALENT()) {
				if(getTalentByDisciplinAndName(disciplinename, disciplinetalent.getName()) == null) {
					TALENTType talent = new TALENTType();
					talent.setName(disciplinetalent.getName());
					talent.setLimitation(disciplinetalent.getLimitation());
					talent.setCircle(circlenr);
					
					RANKType rank = new RANKType();
					rank.setRank(1);
					rank.setBonus(0);
					rank.setStep(1);
					talent.setRANK(rank);
					
					talents.getDISZIPLINETALENT().add(talent);
				}
			}
			List<TALENTType> remove = new ArrayList<TALENTType>();
			for (TALENTType talent : talents.getDISZIPLINETALENT()) {
				if ( talent.getCircle() > circle ) {
					remove.add(talent);
				}
			}
			talents.getDISZIPLINETALENT().removeAll(remove);
			remove.clear();
			for (TALENTType talent : talents.getOPTIONALTALENT()) {
				if ( talent.getCircle() > circle ) {
					remove.add(talent);
				}
			}
			talents.getOPTIONALTALENT().removeAll(remove);
		}
	}
	
	public void addOptionalTalent(String discipline, int circle, TALENTABILITYType talenttype, boolean byVersatility){
		TALENTType talent = new TALENTType();
		talent.setName(talenttype.getName());
		talent.setLimitation(talenttype.getLimitation());
		talent.setCircle(circle);

		RANKType rank = new RANKType();
		rank.setRank(1);
		rank.setBonus(0);
		rank.setStep(1);
		talent.setRANK(rank);

		TALENTTEACHERType teacher = new TALENTTEACHERType();
		if( byVersatility ) teacher.setByversatility(YesnoType.YES);
		talent.setTEACHER(teacher);

		getAllTalentsByDisziplinName().get(discipline).getOPTIONALTALENT().add(talent);
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
			List<CHARACTERISTICSCOST> LpCosts = ApplicationProperties.create().getCharacteristics().getTalentRankLPIncreaseTable(1,magicitem.getLpcostgrowth() );
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
			List<CHARACTERISTICSCOST> LpCosts = ApplicationProperties.create().getCharacteristics().getTalentRankLPIncreaseTable(1,magicitem.getLpcostgrowth() );
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
		List<TALENTType> versatilityList = getTalentByName(ApplicationProperties.create().getVersatilityName());
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
				int currentRealignedRank = talent.getRANK().getRealignedrank();
				if( maxRealignedRank < currentRealignedRank ) maxRealignedRank=currentRealignedRank;
			}
			for( TALENTType talent : talentsByName ) {
				talent.getRANK().setRealignedrank(maxRealignedRank);
			}
		}
	}

	private void insertIfRealigned(HashMap<String, List<TALENTType>> realignedTalents, List<TALENTType> disTalents) {
		for( TALENTType talent : disTalents ) {
			RANKType rank = talent.getRANK();
			if( rank == null ) {
				rank = new RANKType();
				talent.setRANK(rank);
			}
			if( rank.getRealignedrank() > 0 ) {
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
}
