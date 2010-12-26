package de.earthdawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.*;

public class CharacterContainer {
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
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createHEALTHTypeDEATH(death));
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
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createHEALTHTypeUNCONSCIOUSNESS(unconsciousness));
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
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createHEALTHTypeWOUNDS(wound));
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
		character.getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createHEALTHTypeRECOVERY(recovery));
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
			alltalents.put(alldisciplines.get(talents.getDiscipline()).getOrder(),talents);
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
}
