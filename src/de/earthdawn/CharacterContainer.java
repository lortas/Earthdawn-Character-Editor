package de.earthdawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

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

	public void clearATTRIBUTEOrDEFENSEOrHEALTH(String tagname) {
		List<JAXBElement<?>> list = character.getATTRIBUTEOrDEFENSEOrHEALTH();
		List<JAXBElement<?>> removelist = new ArrayList<JAXBElement<?>>();
		for (JAXBElement<?> element : list ) {
			if (element.getName().getLocalPart().equals(tagname)) {
				removelist.add(element);
			}
		}
		list.removeAll(removelist);
	}

	public APPEARANCEType getAppearance() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if( element.getName().getLocalPart().equals("APPEARANCE") ) {
				return (APPEARANCEType) element.getValue();
			}
		}
		// If not found: create
		APPEARANCEType appearance = new APPEARANCEType();
		appearance.setRace("Human");
		appearance.setGender(GenderType.MALE);
		appearance.setEyes("blue");
		appearance.setAge(20);
		appearance.setHair("blond");
		appearance.setHeight(170);
		appearance.setSkin("blond");
		appearance.setWeight(80);
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERAPPEARANCE(appearance));
		return appearance;
	}

	public HashMap<String, ATTRIBUTEType> getAttributes() {
		HashMap<String,ATTRIBUTEType> attributes = new HashMap<String,ATTRIBUTEType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("ATTRIBUTE")) {
				ATTRIBUTEType attribute = (ATTRIBUTEType) element.getValue();
				attributes.put(attribute.getName().value(), attribute);
			}
		}
		for( ATTRIBUTENameType name : ATTRIBUTENameType.values() ) {
			if( attributes.containsKey(name.value())) continue;
			ATTRIBUTEType attribute = new ATTRIBUTEType();
			attribute.setName(name);
			character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERATTRIBUTE(attribute));
			attributes.put(name.value(), attribute);
		}
		return attributes;
	}

	public ATTRIBUTEType getAttributeByName(String name) {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("ATTRIBUTE")) {
				ATTRIBUTEType attribute = (ATTRIBUTEType) element.getValue();
				if (attribute.getName().value().equals(name)) {
					return attribute;
				}
			}
		}
		// Not found
		ATTRIBUTEType attribute = new ATTRIBUTEType();
		attribute.setName(ATTRIBUTENameType.valueOf(name));
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERATTRIBUTE(attribute));
		return attribute;
	}

	public DEFENSEType getDefence() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DEFENSE")) {
				return (DEFENSEType) element.getValue();
			}
		}
		// Not found
		DEFENSEType defense = new DEFENSEType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERDEFENSE(defense));
		return defense;
	}

	public CALCULATEDLEGENDPOINTSType getCalculatedLegendpoints() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("CALCULATEDLEGENDPOINTS")) {
				return (CALCULATEDLEGENDPOINTSType) element.getValue();
			}
		}
		// Not found
		CALCULATEDLEGENDPOINTSType calculatedLegendpoints = new CALCULATEDLEGENDPOINTSType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERCALCULATEDLEGENDPOINTS(calculatedLegendpoints));
		return calculatedLegendpoints;
	}

	public INITIATIVEType getInitiative() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("INITIATIVE")) {
				return (INITIATIVEType) element.getValue();
			}
		}
		// Not found
		INITIATIVEType initiative = new INITIATIVEType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERINITIATIVE(initiative));
		return initiative;
	}

	public HEALTHType getHealth() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("HEALTH")) {
				return (HEALTHType) element.getValue();
			}
		}
		// Not found
		HEALTHType health = new HEALTHType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERHEALTH(health));
		return health;
	}

	public DEATHType getDeath() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("DEATH")) {
				return (DEATHType) element.getValue();
			}
		}
		// Not found
		DEATHType death = new DEATHType();
		health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH().add(new ObjectFactory().createHEALTHTypeDEATH(death));
		return death;
	}

	public DEATHType getUnconsciousness() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("UNCONSCIOUSNESS")) {
				return (DEATHType) element.getValue();
			}
		}
		// Not found
		DEATHType unconsciousness = new DEATHType();
		health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH().add(new ObjectFactory().createHEALTHTypeUNCONSCIOUSNESS(unconsciousness));
		return unconsciousness;
	}

	public WOUNDType getWound() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("WOUNDS")) {
				return (WOUNDType) element.getValue();
			}
		}
		// Not found
		WOUNDType wound = new WOUNDType();
		health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH().add(new ObjectFactory().createHEALTHTypeWOUNDS(wound));
		return wound;
	}

	public RECOVERYType getRecovery() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("RECOVERY")) {
				return (RECOVERYType) element.getValue();
			}
		}
		// Not found
		RECOVERYType recovery = new RECOVERYType();
		health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH().add(new ObjectFactory().createHEALTHTypeRECOVERY(recovery));
		return recovery;
	}

	public KARMAType getKarma() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("KARMA")) {
				return (KARMAType) element.getValue();
			}
		}
		// Not found
		KARMAType karma = new KARMAType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERKARMA(karma));
		return karma;
	}

	public MOVEMENTType getMovement() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("MOVEMENT")) {
				return (MOVEMENTType) element.getValue();
			}
		}
		// Not found
		MOVEMENTType movment = new MOVEMENTType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERMOVEMENT(movment));
		return movment;
	}

	public CARRYINGType getCarrying() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("CARRYING")) {
				return (CARRYINGType) element.getValue();
			}
		}
		// Not found
		CARRYINGType carrying = new CARRYINGType();
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERCARRYING(carrying));
		return carrying;
	}

	public PROTECTIONType getProtection() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("PROTECTION")) {
				return (PROTECTIONType) element.getValue();
			}
		}
		JAXBElement<PROTECTIONType> protection = new ObjectFactory().createEDCHARACTERPROTECTION(new PROTECTIONType());
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(protection);
		return protection.getValue();
	}

	public String getAbilities() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("RACEABILITES")) {
				return (String) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public void setAbilities(String newValue) {
		clearATTRIBUTEOrDEFENSEOrHEALTH("RACEABILITES");
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERRACEABILITES(newValue));
		return;
	}

	public HashMap<Integer,DISCIPLINEType> getAllDiciplinesByOrder() {
		HashMap<Integer,DISCIPLINEType> alldisciplines = new HashMap<Integer,DISCIPLINEType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DISCIPLINE")) {
				DISCIPLINEType discipline = (DISCIPLINEType)element.getValue();
				alldisciplines.put(discipline.getOrder(),discipline);
			}
		}
		return alldisciplines;
	}

	public HashMap<String,DISCIPLINEType> getAllDiciplinesByName() {
		HashMap<String,DISCIPLINEType> alldisciplines = new HashMap<String,DISCIPLINEType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DISCIPLINE")) {
				DISCIPLINEType discipline = (DISCIPLINEType)element.getValue();
				alldisciplines.put(discipline.getName(),discipline);
			}
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
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DISCIPLINE")) {
				DISCIPLINEType d = (DISCIPLINEType)element.getValue();
				if( d.getCircle() > discipline.getCircle() ) {
					discipline=d;
				}
			}
		}
		return discipline;
	}

	public List<TALENTSType> getAllTalents() {
		List<TALENTSType> alltalents = new ArrayList<TALENTSType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("TALENTS")) {
				alltalents.add((TALENTSType)element.getValue());
			}
		}
		return alltalents;
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

	public TALENTType getTalentByName(String searchTalent) {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("TALENTS")) {
				for (JAXBElement<TALENTType> talent : ((TALENTSType)element.getValue()).getDISZIPLINETALENTOrOPTIONALTALENT()) {
					TALENTType t = (TALENTType)talent.getValue();
					if ( t.getName().equals(searchTalent)) {
						return t;
					}
				}
			}
		}
		// Not found
		return null;
	}

	public TALENTType getTalentByDisciplinAndName(String disciplin, String searchTalent) {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("TALENTS")) {
				if (((TALENTSType)element.getValue()).getDiscipline().equals(disciplin)){
					for (JAXBElement<TALENTType> talent : ((TALENTSType)element.getValue()).getDISZIPLINETALENTOrOPTIONALTALENT()) {
						TALENTType t = (TALENTType)talent.getValue();
						if ( t.getName().equals(searchTalent)) {
							return t;
						}
					}
				}
			}
		}
		// Not found
		return null;
	}	
	
	public List<SKILLType> getSkills() {
		List<SKILLType> skills = new ArrayList<SKILLType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("SKILL")) {
				skills.add((SKILLType)element.getValue());
			}
		}
		return skills;
	}

	public EXPERIENCEType getLegendPoints() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("EXPERIENCE")) {
				return (EXPERIENCEType)element.getValue();
			}
		}
		JAXBElement<EXPERIENCEType> experience = new ObjectFactory().createEDCHARACTEREXPERIENCE(new EXPERIENCEType());
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(experience);
		experience.getValue().setCurrentlegendpoints(0);
		experience.getValue().setTotallegendpoints(0);
		return experience.getValue();
	}

	public List<WEAPONType> getWeapons() {
		List<WEAPONType> weapons = new ArrayList<WEAPONType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("WEAPON")) {
				weapons.add((WEAPONType)element.getValue());
			}
		}
		return weapons;
	}

	public List<DISCIPLINEBONUSType> getDisciplineBonuses() {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DISCIPLINEBONUS")) {
				bonuses.add((DISCIPLINEBONUSType)element.getValue());
			}
		}
		return bonuses;
	}

	public void clearDisciplineBonuses() {
		clearATTRIBUTEOrDEFENSEOrHEALTH("DISCIPLINEBONUS");
	}

	public void addDisciplineBonuses(List<DISCIPLINEBONUSType> bonuses, int circle) {
		List<JAXBElement<?>> list = character.getATTRIBUTEOrDEFENSEOrHEALTH();
		for( DISCIPLINEBONUSType bonus : bonuses ) {
			if( bonus.getCircle() > circle ) continue;
			DISCIPLINEBONUSType newValue = new DISCIPLINEBONUSType();
			newValue.setBonus(bonus.getBonus());
			newValue.setCircle(bonus.getCircle());
			list.add(new ObjectFactory().createEDCHARACTERDISCIPLINEBONUS(newValue));
		}
	}

	public void addDisciplineKarmaStepBonus(int disciplineKarmaStepBonus) {
		if( disciplineKarmaStepBonus == 0 ) return;
		List<JAXBElement<?>> list = character.getATTRIBUTEOrDEFENSEOrHEALTH();
		DISCIPLINEBONUSType newValue = new DISCIPLINEBONUSType();
		newValue.setBonus("Karma Step + "+disciplineKarmaStepBonus);
		newValue.setCircle(0);
		list.add(new ObjectFactory().createEDCHARACTERDISCIPLINEBONUS(newValue));
	}
	
	public List<COINSType> getAllCoins() {
		List<COINSType> allCoins = new ArrayList<COINSType>();
		List<JAXBElement<?>> list = character.getATTRIBUTEOrDEFENSEOrHEALTH();
		for (JAXBElement<?> element : list) {
			if (element.getName().getLocalPart().equals("COINS")) {
				allCoins.add((COINSType)element.getValue());
			}
		}
		if( allCoins.isEmpty() ) {
			COINSType coins = new COINSType();
			coins.setLocation("self");
			coins.setUsed(YesnoType.YES);
			allCoins.add(coins);
			list.add(new ObjectFactory().createEDCHARACTERCOINS(coins));
		}
		return allCoins;
	}

	public List<SPELLSType> getAllSpells() {
		List<SPELLSType> allspells = new ArrayList<SPELLSType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("SPELLS")) {
				allspells.add((SPELLSType)element.getValue());
			}
		}
		return allspells;
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
	public HashMap<String,List<TALENTType>> getUsedOptionalTalents() {
		HashMap<String,List<TALENTType>> result = new HashMap<String,List<TALENTType>>();
		for(TALENTSType talents : getAllTalents() ) {
			List<TALENTType> list = new ArrayList<TALENTType>();
			for(int i=0;i<20;i++) list.add(null);
			for( JAXBElement<TALENTType> element : talents.getDISZIPLINETALENTOrOPTIONALTALENT()) {
				if (element.getName().getLocalPart().equals("OPTIONALTALENT")) {
					TALENTType talent = element.getValue();
					list.set(talent.getCircle(), talent);
				}
			}
			result.put(talents.getDiscipline(), list);
		}
		return result;
	}

	public List<TALENTABILITYType> getUnusedOptionalTalents(DISCIPLINE discipline) {
		List<TALENTABILITYType> result = new ArrayList<TALENTABILITYType>();
		List<TALENTType> usedOptionalTalents = getUsedOptionalTalents().get(discipline.getName());
		if( usedOptionalTalents == null ) {
			usedOptionalTalents = new ArrayList<TALENTType>();
			System.err.println("No Used Optinal Talents found for discipline '"+discipline.getName()+"'");
		}
		int disciplineCircle = getCircleOf(discipline.getName());
		for( JAXBElement<?> element : discipline.getOPTIONALTALENTOrDISCIPLINETALENTAndSPELL()) {
			if( element.getName().getLocalPart().equals("OPTIONALTALENT") ) {
				TALENTABILITYType talent = (TALENTABILITYType)element.getValue();
				if( talent.getCircle() <= disciplineCircle ) {
					Boolean found=false;
					for( TALENTType usedTalent : usedOptionalTalents ) {
						if(usedTalent != null){
							if( talent.getName().equals(usedTalent.getName()) ) {
								found = true;
								usedOptionalTalents.remove(usedTalent);
								break;
							}
						}
					}
					if( ! found ) result.add( talent );
				}
			}
		}
		return result;
	}

	public HashMap<String,List<Integer>> getCircleOfMissingOptionalTalents() {
		HashMap<String,List<Integer>> result = new HashMap<String,List<Integer>>();
		HashMap<String,List<TALENTType>> talentsMap = getUsedOptionalTalents();
		for(String discipline : talentsMap.keySet() ) {
			List<Integer> list = new ArrayList<Integer>();
			List<TALENTType> talentsList = talentsMap.get(discipline);
			if( talentsList == null ) {
				talentsList = new ArrayList<TALENTType>();
				System.err.println("A talent list for the discipline '"+discipline+"' could not be found.");
			}
			int disciplineCircle = getCircleOf(discipline);
			for( int i=1; i<=disciplineCircle; i++ ) {
				if( talentsList.get(i) == null ) list.add( i );
			}
			result.put(discipline, list);
		}
		return result;
	}
	
	public void addDiciplin(String name){
		if ((this.getAllDiciplinesByOrder().size() < 3) && (this.getAllDiciplinesByName().get(name) == null)){
			DISCIPLINEType value = new DISCIPLINEType();
			value.setName(name);
			value.setCircle(1);
			value.setOrder(getAllDiciplinesByOrder().size() +1);
			JAXBElement<DISCIPLINEType> dt = new ObjectFactory().createEDCHARACTERDISCIPLINE(value);
			getEDCHARACTER().getATTRIBUTEOrDEFENSEOrHEALTH().add(dt);
			TALENTSType talents =  new TALENTSType();
			talents.setDiscipline(name);
			getEDCHARACTER().getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERTALENTS(talents));
			initDisciplinTalents(value.getName(),value.getCircle());
		}
		
	}
	
	public void initDisciplinTalents(String disciplinname, int circle){
		DISCIPLINE d = ApplicationProperties.create().getDisziplin(disciplinname);
		for( JAXBElement<?> element : d.getOPTIONALTALENTOrDISCIPLINETALENTAndSPELL() ) {
			if (element.getName().getLocalPart().equals("DISCIPLINETALENT")){
				TALENTABILITYType ta = (TALENTABILITYType) element.getValue();

				if(ta.getCircle() <= circle){
					TALENTType talent = new TALENTType();
					talent.setName(ta.getName());
					talent.setLimitation(ta.getLimitation());
					talent.setCircle(ta.getCircle());
					RANKType rank = new RANKType();
					rank.setRank(1);
					rank.setBonus(0);
					rank.setStep(1);
					talent.setRANK(rank);
					
					if(getTalentByDisciplinAndName(disciplinname, ta.getName()) == null){
						getAllTalentsByDisziplinName().get(disciplinname).getDISZIPLINETALENTOrOPTIONALTALENT().add(new ObjectFactory().createTALENTSTypeDISZIPLINETALENT(talent));
					}
				}
				else{
					// Disciplin Talente die aus höheren Kreisen löschen
					if(getTalentByName(ta.getName()) != null){
						System.out.println("Remove Talent:" + ta.getName());
						List<JAXBElement<?>> removelist = new ArrayList<JAXBElement<?>>();
						for(JAXBElement<TALENTType> talent  : getAllTalentsByDisziplinName().get(disciplinname).getDISZIPLINETALENTOrOPTIONALTALENT()){
							if(talent.getValue().getName().equals(ta.getName())){
								removelist.add(talent);
							}
						}
						getAllTalentsByDisziplinName().get(disciplinname).getDISZIPLINETALENTOrOPTIONALTALENT().removeAll(removelist);
						
					}
				}
			}
			
		}			
	}
	
	public void addOptionalTalent(String discipline, int circle, TALENTABILITYType talenttype){
		TALENTType talent = new TALENTType();
		talent.setName(talenttype.getName());
		talent.setLimitation(talenttype.getLimitation());
		talent.setCircle(circle);
		
		RANKType rank = new RANKType();
		rank.setRank(1);
		rank.setBonus(0);
		rank.setStep(1);
		talent.setRANK(rank);
		
		getAllTalentsByDisziplinName().get(discipline).getDISZIPLINETALENTOrOPTIONALTALENT().add(new ObjectFactory().createTALENTSTypeOPTIONALTALENT(talent));
	}
	
}
