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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.data.*;

public class RandomCharacterTemplates {

	private static Random rand = new Random();
	private static final Map<String, EDRANDOMCHARACTERTEMPLATE> RandomCharactertemplateMap = new TreeMap<String, EDRANDOMCHARACTERTEMPLATE>();
	private ITEMS items = null;
	private ECECapabilities capabilities = null;
	private List<SPELLDEFType> spells = null;

	public void setItems(ITEMS items) {
		this.items = items;
	}

	public void setSpells(List<SPELLDEFType> spells) {
		this.spells = spells;
	}

	public void setCapabilities(ECECapabilities capabilities) {
		this.capabilities = capabilities;
	}

	public EDRANDOMCHARACTERTEMPLATE get(String key) {
		return RandomCharactertemplateMap.get(key);
	}

	public void put(String key, EDRANDOMCHARACTERTEMPLATE value) {
		RandomCharactertemplateMap.put(key, value);
	}

	public Set<String> getAllTemplateNames() {
		return RandomCharactertemplateMap.keySet();
	}

	public CharacterContainer generateRandomCharacter(String templateName) {
		EDRANDOMCHARACTERTEMPLATE template = get(templateName);
		if( template == null ) return null;
		CharacterContainer character = new CharacterContainer(new EDCHARACTER());
		ECEWorker worker = new ECEWorker();
		worker.verarbeiteCharakter(character.getEDCHARACTER());
		character.setRandomName();
		APPEARANCEType appearance = character.getAppearance();
		String race = rollSingle(template.getRACES());
		if( race != null ) appearance.setRace(race);
		if( rand.nextBoolean() ) appearance.setGender(GenderType.MALE);
		else appearance.setGender(GenderType.FEMALE);
		// Discipline
		String discipline = rollSingle(template.getDISCIPLINES());
		if( discipline != null ) character.addDiciplin(discipline);
		int circle = template.getCircleMin()+rand.nextInt(template.getCircleMax()-template.getCircleMin());
		character.getDisciplines().get(0).setCircle(circle);
		character.fillOptionalTalentsRandom(discipline);
		// Atributes
		int attributeTotalWeight = 0;
		HashMap<String, ATTRIBUTEType> attributes = character.getAttributes();
		for( RANDOMATTRIBUTESType attr : template.getATTRIBUTES() ) {
			attributeTotalWeight+=attr.getWeight();
		}
		// Am KarmaMaxModificator ist zu erkennen ob zuviele Kaufpunkte ausgegeben wurden.
		while( character.getKarma().getMaxmodificator() > 0 ) {
			ATTRIBUTEType a = null;
			int r = rand.nextInt(attributeTotalWeight);
			for( RANDOMATTRIBUTESType attr : template.getATTRIBUTES() ) {
				r-=attr.getWeight();
				if( r<0 ) {
					a = attributes.get(attr.getAttribute().value());
					a.setGenerationvalue(a.getGenerationvalue()+1);
					break;
				}
			}
			worker.verarbeiteCharakter(character.getEDCHARACTER());
			if( a!= null && character.getKarma().getMaxmodificator() < 0 ) {
				// Um eine Endlosschleife unwahrscheinlcher zu machen, gehe 2 Stufen runter
				a.setGenerationvalue(a.getGenerationvalue()-2);
				worker.verarbeiteCharakter(character.getEDCHARACTER());
			}
		}
		for( int i=0; i<circle; ) {
			int r = rand.nextInt(attributeTotalWeight);
			for( RANDOMATTRIBUTESType attr : template.getATTRIBUTES() ) {
				r-=attr.getWeight();
				if( r<0 ) {
					ATTRIBUTEType a = attributes.get(attr.getAttribute().value());
					if( a.getLpincrease() < 2 ) {
						a.setLpincrease(a.getLpincrease()+1);
						i++;
					}
					break;
				}
			}
		}
		// Spells
		for( String spellname : rollMulti(template.getSPELLS(),1,10) ){
			for( SPELLDEFType spelldef : spells ) {
				if( spelldef.getName().equals(spellname) ) {
					SPELLType spell = new SPELLType();
					spell.setCircle(1); // TODO: genauer Kreis muss noch ermittelt werden
					spell.setType(SpellkindType.WIZARD); // TODO: genauer Spruchtyp muss noch ermittelt werden
					spell.setCastingdifficulty(spelldef.getCastingdifficulty());
					spell.setDuration(spelldef.getDuration());
					spell.setEffect(spelldef.getEffect());
					spell.setEffectarea(spelldef.getEffectarea());
					spell.setName(spelldef.getName());
					spell.setRange(spelldef.getRange());
					spell.setReattuningdifficulty(spelldef.getReattuningdifficulty());
					spell.setThreads(spelldef.getThreads());
					spell.setWeavingdifficulty(spelldef.getWeavingdifficulty());
					character.getDisciplines().get(0).getSPELL().add(spell);
					break;
				}
			}
		}
		if( capabilities == null ) {
			System.err.println("RandomCharacterTemplate: capabilities not defined. Skipping Skills");
		} else {
			int skillMin = template.getSkillMin();
			int skillMax = template.getSkillMax();
			List<TALENTType> optionaltalents = character.getDisciplines().get(0).getOPTIONALTALENT();
			List<WeightedstringlistType> skills = template.getSKILLS();
			while( skillMax>0 && skillMin>=0 ) {
				// Wenn nach einem Schleifendurchlauf die verfügbare Auswahlliste von Skills leer ist, dann breche Schleife ab. Fertig.
				if( skills.isEmpty() ) break;
				List<String> skillnames = rollMulti(skills,skillMin,skillMax);
				// Wenn beim "Würfel" raus kam, das kein Skill raus kam, dann breche die Schleife ab. Fertig.
				if( skillnames.isEmpty() ) break;
				List<String> skilltalentnames = new ArrayList<String>();
				for( String skillname : skillnames ) {
					for( TALENTType optionaltalent : optionaltalents ) {
						if( optionaltalent.getName().equals(skillname) ) {
							skilltalentnames.add(skillname);
							break;
						}
					}
				}
				// Wenn der Skill bereits als OptionalTalent gelernt wurde, dann lerne diesen Skill nicht noch zusätzlich.
				skillnames.removeAll(skilltalentnames);
				if( skillnames.isEmpty() ) continue;
				for( CAPABILITYType skilldef : capabilities.getSkills() ) {
					String skilldefName = skilldef.getName();
					for( String skillname : skillnames ) {
						if( skillname.equals(skilldefName)) {
							SKILLType skill = new SKILLType();
							skill.setAction(skilldef.getAction());
							skill.setAttribute(skilldef.getAttribute());
							skill.setDefault(skilldef.getDefault());
							skill.setStrain(skilldef.getStrain());
							skill.setName(skilldefName);
							RANKType rank = new RANKType();
							int rankvalue = 1+rand.nextInt(template.getCircleMin()-1);
							if( rankvalue > 10 ) rankvalue=10;
							rank.setRank(rankvalue);
							skill.setRANK(rank);
							character.getSkills().add(skill);
							skillMin--;
							skillMax--;
							skillnames.remove(skillname);
							break;
						}
					}
					if( skillnames.isEmpty() ) break;
				}
			}
		}
		for( RANDOMITEMCATEGORYType itemcategory : template.getITEMCATEGORY() ) {
			//String categoryname=itemcategory.getName();
			for( String itemname : rollMulti(itemcategory.getITEMS(),itemcategory.getMin(),itemcategory.getMin()) ) {
				boolean found=false;
				for( WEAPONType item : items.getWEAPON() ) {
					if( item.getName().equals(itemname) ) {
						character.getWeapons().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( ARMORType item : items.getARMOR() ) {
					if( item.getName().equals(itemname) ) {
						character.getProtection().getARMOROrSHIELD().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( SHIELDType item : items.getSHIELD() ) {
					if( item.getName().equals(itemname) ) {
						character.getProtection().getARMOROrSHIELD().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( THREADITEMType item : items.getTHREADITEM() ) {
					if( item.getName().equals(itemname) ) {
						character.getThreadItem().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( BLOODCHARMITEMType item : items.getBLOODCHARMITEM() ) {
					if( item.getName().equals(itemname) ) {
						character.getBloodCharmItem().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( PATTERNITEMType item : items.getPATTERNITEM() ) {
					if( item.getName().equals(itemname) ) {
						character.getPatternItem().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( MAGICITEMType item : items.getMAGICITEM() ) {
					if( item.getName().equals(itemname) ) {
						character.getMagicItem().add(item);
						found=true;
						break;
					}
				}
				if( found ) continue;
				for( ITEMType item : items.getITEM() ) {
					if( item.getName().equals(itemname) ) {
						character.getItems().add(item);
						found=true;
						break;
					}
				}
			}
		}
		return character;
	}

	public static String rollSingle(List<WeightedstringlistType> list) {
		if( list.isEmpty() ) return null;
		List<String> fulllist = weightconcat(list);
		if( fulllist.isEmpty() ) return null;
		return fulllist.get(rand.nextInt(fulllist.size()));
	}

	public static List<String> rollMulti(List<WeightedstringlistType> list, int min, int max) {
		List<String> result = new ArrayList<String>();
		if( list.isEmpty() ) return result;
		if( min < 0 ) return result;
		if( max < 1 ) return result;
		List<String> fulllist = weightconcat(list);
		if( fulllist.isEmpty() ) return result;
		int count=0;
		if(max<=min){
			count=min;
		} else {
			count = min+rand.nextInt(max-min);
		}
		for( int i=0; i<count; i++) {
			String e = fulllist.get(rand.nextInt(fulllist.size()));
			result.add(e);
			// Lösche alle Elemente e aus der Liste
			while( fulllist.remove(e) );
			if( fulllist.isEmpty() ) return result;
		}
		return result;
	}

	public static List<String> weightconcat(List<WeightedstringlistType> stringlist) {
		List<String> result = new ArrayList<String>();
		for( WeightedstringlistType l : stringlist ) {
			List<String> s = splitStringlist(l);
			for( int i=0; i<l.getWeight(); i++ ) result.addAll(s);
		}
		return result;
	}

	public static List<String> splitStringlist(StringlistType stringlist) {
		List<String> result = new ArrayList<String>();
		for( String s : stringlist.getValue().split(stringlist.getDelimiter()) ) result.add(s);
		return result;
	}

}
