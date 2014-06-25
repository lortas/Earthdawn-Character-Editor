package de.earthdawn.config;
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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.data.YesnoType;

public class ECECapabilities {

	private static PrintStream errorout = System.err;
	private List<CAPABILITYType> talentList = new ArrayList<CAPABILITYType>();
	private List<CAPABILITYType> skillList = new ArrayList<CAPABILITYType>();
	private List<CAPABILITYType> versatilitytalentList = null;
	private HashMap<String,CAPABILITYType> talentMap = new HashMap<String,CAPABILITYType>();
	private HashMap<String,CAPABILITYType> skillMap = new HashMap<String,CAPABILITYType>();
	
	public ECECapabilities(){}

	public ECECapabilities(List<JAXBElement<CAPABILITYType>> capabilities) {
		for (JAXBElement<CAPABILITYType> element : capabilities) {
			if (element.getName().getLocalPart().equals("TALENT")) {
				CAPABILITYType talent = (CAPABILITYType)element.getValue();
				talentList.add(talent);
				talentMap.put(talent.getName(), talent);
			} else if (element.getName().getLocalPart().equals("SKILL")) {
				CAPABILITYType skill = (CAPABILITYType)element.getValue();
				skillList.add(skill);
				skillMap.put(skill.getName(),skill);
			} else {
				System.err.println( "Unknown capabilities type: "+element.getName().getLocalPart() );
			}
		}
	}

	public List<CAPABILITYType> getTalents() {
		return talentList;
	}

	public List<CAPABILITYType> getVersatilityTalents() {
		if( versatilitytalentList == null ) {
			versatilitytalentList = new ArrayList<CAPABILITYType>();
			for( CAPABILITYType t : talentList ) {
				if( ! t.getNotbyversatility().equals(YesnoType.YES) ) versatilitytalentList.add(t);
			}
		}
		return versatilitytalentList;
	}

	public List<CAPABILITYType> getSkills() {
		return skillList;
	}

	public List<CAPABILITYType> getDefaultSkills(List<String> exclude) {
		List<CAPABILITYType> defaultSkills = new ArrayList<CAPABILITYType>();
		for( CAPABILITYType skill : skillList ) {
			if( skill.getDefault().equals(YesnoType.YES) ) {
				boolean insert=true;
				for( String s : exclude ) {
					if( skill.getName().equals(s) ) insert=false;
				}
				if( insert ) defaultSkills.add(skill);
			}
		}
		return defaultSkills;
	}

	public CAPABILITYType getTalent(String name) {
		return talentMap.get(name);
	}

	public CAPABILITYType getSkill(String name) {
		return skillMap.get(name);
	}

	public void enforceCapabilityParams(CAPABILITYType capability) {
		CAPABILITYType replacment = null;
		if( capability instanceof TALENTType ) {
			replacment=this.getTalent(capability.getName());
		} else {
			replacment=this.getSkill(capability.getName());
		}
		if( replacment == null ) {
			errorout.println("Capability '"+capability.getName()+"' not found : "+capability.getClass().getSimpleName());
		} else {
			capability.setAction(replacment.getAction());
			capability.setAttribute(replacment.getAttribute());
			capability.setBonus(replacment.getBonus());
			capability.setKarma(replacment.getKarma());
			capability.setStrain(replacment.getStrain());
			capability.setBookref(replacment.getBookref());
			capability.setIsinitiative(replacment.getIsinitiative());
			capability.setNotbyversatility(replacment.getNotbyversatility());
			capability.setDefault(replacment.getDefault());
		}
	}
}
