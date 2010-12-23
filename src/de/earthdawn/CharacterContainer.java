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
	public APPEARANCEType getAppearance() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if( element.getName().getLocalPart().equals("APPEARANCE") ) {
				return (APPEARANCEType) element.getValue();
			}
		}
		// Not found
		return null;
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
		return null;
	}

	public DEFENSEType getDefence() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DEFENSE")) {
				return (DEFENSEType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public INITIATIVEType getInitiative() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("INITIATIVE")) {
				return (INITIATIVEType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public HEALTHType getHealth() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("HEALTH")) {
				return (HEALTHType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public DEATHType getDeath() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("DEATH")) {
				return (DEATHType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public DEATHType getUnconsciousness() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("UNCONSCIOUSNESS")) {
				return (DEATHType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public WOUNDType getWound() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("WOUNDS")) {
				return (WOUNDType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public RECOVERYType getRecovery() {
		HEALTHType health = getHealth();
		for (JAXBElement<?> element : health.getRECOVERYOrUNCONSCIOUSNESSOrDEATH()) {
			if (element.getName().getLocalPart().equals("RECOVERY")) {
				return (RECOVERYType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public KARMAType getKarma() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("KARMA")) {
				return (KARMAType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public MOVEMENTType getMovement() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("MOVEMENT")) {
				return (MOVEMENTType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public CARRYINGType getCarrying() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("CARRYING")) {
				return (CARRYINGType) element.getValue();
			}
		}
		// Not found
		return null;
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
		List<JAXBElement<?>> list = character.getATTRIBUTEOrDEFENSEOrHEALTH();
		List<JAXBElement<?>> removelist = new ArrayList<JAXBElement<?>>();
		for (JAXBElement<?> element : list ) {
			if (element.getName().getLocalPart().equals("RACEABILITES")) {
				removelist.add(element);
			}
		}
		list.removeAll(removelist);
		list.add(new ObjectFactory().createEDCHARACTERRACEABILITES(newValue));
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
}
