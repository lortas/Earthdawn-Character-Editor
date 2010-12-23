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

	public static List<DISCIPLINEBONUSType> getDisciplineBonuses(DISCIPLINEType discipline) {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		for(JAXBElement<?> element : ApplicationProperties.create().getDisziplin(discipline.getName()).getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT()) {
			if (element.getName().getLocalPart().equals("KARMA")) {
				KARMAABILITYType karma = ((KARMAABILITYType)element.getValue());
				if( karma.getCircle() > discipline.getCircle() ) continue;
				DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
				bonus.setCircle(karma.getCircle());
				bonus.setBonus("Can spend Karma for "+karma.getSpend());
				bonuses.add(bonus);
			} else if (element.getName().getLocalPart().equals("ABILITY")) {
				CIRCLENAMEType ability = ((CIRCLENAMEType)element.getValue());
				if( ability.getCircle() > discipline.getCircle() ) continue;
				DISCIPLINEBONUSType bonus = new DISCIPLINEBONUSType();
				bonus.setCircle(ability.getCircle());
				bonus.setBonus("Ability: "+ability.getName());
				bonuses.add(bonus);
			}
		}
		return bonuses;
	}
}
