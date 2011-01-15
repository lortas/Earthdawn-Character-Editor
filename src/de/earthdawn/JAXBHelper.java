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

	public static List<DISCIPLINEBONUSType> getDisciplineBonuses(DISCIPLINEType discipline) {
		List<DISCIPLINEBONUSType> bonuses = new ArrayList<DISCIPLINEBONUSType>();
		for(JAXBElement<?> element : ApplicationProperties.create().getDisziplin(discipline.getName()).getOPTIONALTALENTOrDISCIPLINETALENTAndSPELL()) {
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
