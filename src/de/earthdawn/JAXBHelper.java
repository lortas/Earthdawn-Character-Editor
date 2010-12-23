package de.earthdawn;

import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.*;

/**
 * Hilfsklasse zur einfacheren Verarbeitung des JAXB-Baumes
 *
 * @author egon_mueller
 */
public class JAXBHelper {

	public static void setAbilities(EDCHARACTER charakter, String newValue) {
		List<JAXBElement<?>> list = charakter.getATTRIBUTEOrDEFENSEOrHEALTH();
		for (JAXBElement<?> element : list ) {
			if (element.getName().getLocalPart().equals("RACEABILITES")) {
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
