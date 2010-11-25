package de.earthdawn;

import javax.xml.bind.JAXBElement;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttributeType;

import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.EDCHARAKTER;

/**
 * Hilfsklasse zur einfacheren Verarbeitung des JAXB-Baumes
 *
 * @author egon_mueller
 */
public class JAXBHelper {

	public static final String RACEABILITES = "RACEABILITES";
	
	public static final String ATTRIBUTE = "ATTRIBUTE";

	public static String getRace(EDCHARAKTER charakter) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().equals(RACEABILITES)) {
				return (String) element.getValue();
			}
		}

		// Not found
		return null;
	}

	public static ATTRIBUTEType getAttribute(EDCHARAKTER charakter, String id) {
		for (JAXBElement<?> element : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().equals(ATTRIBUTE)) {
				
				ATTRIBUTEType attribute = (ATTRIBUTEType) element.getValue();
				if (attribute.getName().value().equals(id)) {
					return attribute;
				}
			}
		}

		// Not found
		return null;
	}
}
