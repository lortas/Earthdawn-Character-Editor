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
