package de.earthdawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.*;

/**
 * Hilfsklasse zur einfacheren Verarbeitung des JAXB-Baumes
 *
 * @author egon_mueller
 */
public class JAXBHelper {

	public static final String DEFENSE = "DEFENSE";
	public static final String APPEARANCE = "APPEARANCE";
	public static final String CARRYING = "CARRYING";
	public static final String HEALTH = "HEALTH";
	public static final String RACEABILITES = "RACEABILITES";
	public static final String ATTRIBUTE = "ATTRIBUTE";
	public static final String INITIATIVE = "INITIATIVE";
	public static final String MOVEMENT = "MOVEMENT";
	public static final String TALENTS = "TALENTS";
	public static final String KARMA = "KARMA";
	public static final String PROTECTION = "PROTECTION";
	public static final String DISCIPLINE = "DISCIPLINE";
	public static final String SKILL = "SKILL";

	public static CARRYINGType getCarrying(EDCHARACTER charakter) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (CARRYING.equals(element.getName().getLocalPart())) {
				return (CARRYINGType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public static MOVEMENTType getMovement(EDCHARACTER charakter) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (MOVEMENT.equals(element.getName().getLocalPart())) {
				return (MOVEMENTType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public static List<SKILLType> getSkills(EDCHARACTER charakter) {
		List<SKILLType> skills = new ArrayList<SKILLType>();
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (SKILL.equals(element.getName().getLocalPart())) {
				skills.add((SKILLType)element.getValue());
			}
		}
		return skills;
	}

	public static HashMap<Integer,TALENTSType> getAllTalents(EDCHARACTER charakter) {
		// Erstelle zu erst eine Liste von Disziplinen
		HashMap<String,DISCIPLINEType> alldisciplines = new HashMap<String,DISCIPLINEType>();
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (DISCIPLINE.equals(element.getName().getLocalPart())) {
				DISCIPLINEType discipline = (DISCIPLINEType)element.getValue();
				alldisciplines.put(discipline.getName(),discipline);
			}
		}
		// Hole nun alle TalentListen und speichere sie in der Diszipline Reihnfolge in eine HashMap.
		HashMap<Integer,TALENTSType> alltalents = new HashMap<Integer,TALENTSType>();
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (TALENTS.equals(element.getName().getLocalPart())) {
				TALENTSType talents = (TALENTSType)element.getValue();
				alltalents.put(alldisciplines.get(talents.getDiscipline()).getOrder(),talents);
			}
		}
		return alltalents;
	}

	public static TALENTType getTalentByName(EDCHARACTER charakter, String searchTalent) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (TALENTS.equals(element.getName().getLocalPart())) {
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

	public static PROTECTIONType getProtection(EDCHARACTER charakter) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (PROTECTION.equals(element.getName().getLocalPart())) {
				return (PROTECTIONType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public static String getAbilities(EDCHARACTER charakter) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (RACEABILITES.equals(element.getName().getLocalPart())) {
				return (String) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public static void setAbilities(EDCHARACTER charakter, String newValue) {
		List<JAXBElement<?>> list = charakter.getATTRIBUTEOrDEFENSEOrHEALTH();
		for (JAXBElement<?> element : list ) {
			if (RACEABILITES.equals(element.getName().getLocalPart())) {
				((JAXBElement<String>)element).setValue(newValue);

				return;
			}
		}

		// Wenn das Element nicht gefunden wird, dann soll es der Liste angehängt werden
		list.add(new ObjectFactory().createEDCHARACTERRACEABILITES(newValue));
		
		return;
	}

	public static String getNameLang(NAMES names, String name, LanguageType lang) {
		for( JAXBElement<?> element : names.getATTRIBUTESOrDURABILITYOrVERSATILITY() ) {
			if( name.equals(element.getName().getLocalPart()) ) {
				NAMELANGType tmp = (NAMELANGType)element.getValue();
				if( lang.equals(tmp.getLang()) ) {
					return tmp.getName();
				}
			}
		}
		// not found
		return null;
	}
}
