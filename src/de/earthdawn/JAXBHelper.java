package de.earthdawn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.*;

/**
 * Hilfsklasse zur einfacheren Verarbeitung des JAXB-Baumes
 *
 * @author egon_mueller
 */
public class JAXBHelper {

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

	public static DISCIPLINEDURABILITYType getDisciplineDurability(DISCIPLINEType discipline) {
		DISCIPLINEDURABILITYType durability = new DISCIPLINEDURABILITYType();
		durability.setCircle(1);
		durability.setDeath(1);
		durability.setUnconsciousness(1);
		for(JAXBElement<?> element : ApplicationProperties.create().getDisziplin(discipline.getName()).getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT()) {
			if (element.getName().getLocalPart().equals("DURABILITY")) {
				DISCIPLINEDURABILITYType d = ((DISCIPLINEDURABILITYType)element.getValue());
				if( d.getCircle() > discipline.getCircle() ) continue;
				if( d.getDeath() < durability.getDeath() ) continue;
				durability=d;
			}
		}
		return durability;
	}
}
