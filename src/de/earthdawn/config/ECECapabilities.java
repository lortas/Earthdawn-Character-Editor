package de.earthdawn.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.YesnoType;

public class ECECapabilities {
	
	private List<CAPABILITYType> talentList = new ArrayList<CAPABILITYType>();
	private List<CAPABILITYType> skillList = new ArrayList<CAPABILITYType>();
	private HashMap<String,CAPABILITYType> talentMap = new HashMap<String,CAPABILITYType>();
	private HashMap<String,CAPABILITYType> skillMap = new HashMap<String,CAPABILITYType>();
	
	public ECECapabilities(List<JAXBElement<CAPABILITYType>> capabilities) {
		for (JAXBElement<CAPABILITYType> element : capabilities) {
			if (element.getName().getLocalPart().equals("TALENT")) {
				CAPABILITYType talent = (CAPABILITYType)element.getValue();
				talentList.add(talent);
				talentMap.put(talent.getName()+" ## "+talent.getLimitation(), talent);
			} else if (element.getName().getLocalPart().equals("SKILL")) {
				CAPABILITYType skill = (CAPABILITYType)element.getValue();
				skillList.add(skill);
				skillMap.put(skill.getName()+" ## "+skill.getLimitation(),skill);
			} else {
				System.err.println( "Unknown capabilities type: "+element.getName().getLocalPart() );
			}
		}
	}

	public List<CAPABILITYType> getTalents() {
		return talentList;
	}

	public List<CAPABILITYType> getSkills() {
		return skillList;
	}

	public List<CAPABILITYType> getDefaultSkills() {
		List<CAPABILITYType> defaultSkills = new ArrayList<CAPABILITYType>();
		for( CAPABILITYType skill : skillList ) {
			if( skill.getDefault().equals(YesnoType.YES) ) {
				defaultSkills.add(skill);
			}
		}
		return defaultSkills;
	}

	public CAPABILITYType getTalent(String name, String limitation) {
		return talentMap.get(name+" ## "+limitation);
	}

	public CAPABILITYType getSkill(String name, String limitation) {
		return skillMap.get(name+" ## "+limitation);
	}
}
