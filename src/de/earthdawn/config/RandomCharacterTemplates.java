package de.earthdawn.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.data.APPEARANCEType;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.ATTRIBUTENameType;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.EDRANDOMCHARACTERTEMPLATE;
import de.earthdawn.data.GenderType;
import de.earthdawn.data.ITEMS;
import de.earthdawn.data.RANDOMATTRIBUTESType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.SPELLDEFType;
import de.earthdawn.data.SPELLType;
import de.earthdawn.data.SpellkindType;
import de.earthdawn.data.StringlistType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.WeightedstringlistType;

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

public class RandomCharacterTemplates {

	private static Random rand = new Random();
	private static final Map<String, EDRANDOMCHARACTERTEMPLATE> RandomCharactertemplateMap = new TreeMap<String, EDRANDOMCHARACTERTEMPLATE>();
	private ITEMS items = null;
	private List<SPELLDEFType> spells = null;

	public void setItems(ITEMS items) {
		this.items = items;
	}

	public void setSpells(List<SPELLDEFType> spells) {
		this.spells = spells;
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
		// Armor
		String armorname = rollSingle(template.getARMOR());
		for( ARMORType armor : items.getARMOR() ) {
			if( armor.getName().equals(armorname) ) {
				character.getProtection().getARMOROrSHIELD().add(armor);
				break;
			}
		}
		// Shield
		String shieldname = rollSingle(template.getSHIELD());
		for( SHIELDType shield : items.getSHIELD() ) {
			if( shield.getName().equals(shieldname) ) {
				character.getProtection().getARMOROrSHIELD().add(shield);
				break;
			}
		}
		// Weapon
		for( String weaponname : rollMulti(template.getWEAPON(),template.getWeaponMin(),template.getWeaponMax()) ){
			for( WEAPONType weapon : items.getWEAPON() ) {
				if( weapon.getName().equals(weaponname) ) {
					character.getWeapons().add(weapon);
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
		return character;
	}

	public static String rollSingle(List<WeightedstringlistType> list) {
		if( list.isEmpty() ) return null;
		List<String> fulllist = weightconcat(list);
		if( fulllist.isEmpty() ) return null;
		return fulllist.get(rand.nextInt(fulllist.size()));
	}

	public static List<String> rollMulti(List<WeightedstringlistType> list, int min, int max) {
		if( list.isEmpty() ) return null;
		List<String> result = new ArrayList<String>();
		List<String> fulllist = weightconcat(list);
		if( fulllist.isEmpty() ) return result;
		int count=0;
		if(max<min){
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
