package de.earthdawn;
/******************************************************************************\
Copyright (C) 2010-2012  Holger von Rhein <lortas@freenet.de>

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

import de.earthdawn.data.CHARACTERLANGUAGEType;
import de.earthdawn.data.YesnoType;

public class LanguageContainer {
	private List<CHARACTERLANGUAGEType> languages;

	public LanguageContainer() {
		this.languages = new ArrayList<CHARACTERLANGUAGEType>();
	}

	public LanguageContainer(List<CHARACTERLANGUAGEType> languages) {
		if(languages==null) languages = new ArrayList<CHARACTERLANGUAGEType>();
		this.languages = languages;
	}

	public List<CHARACTERLANGUAGEType> getLanguages() {
		return languages;
	}

	public int size() {
		return languages.size();
	}

	public CHARACTERLANGUAGEType getLanguage(int index) {
		return languages.get(index);
	}

	public void insertLanguage(CHARACTERLANGUAGEType language) {
		if( language == null ) return;
		String lang = language.getLanguage();
		YesnoType skill = language.getNotlearnedbyskill();
		for( CHARACTERLANGUAGEType l : this.languages ) {
			if( l.getLanguage().equals(lang) && l.getNotlearnedbyskill().equals(skill)) {
				if( language.getSpeak().equals(YesnoType.YES) ) l.setSpeak(YesnoType.YES);
				if( language.getReadwrite().equals(YesnoType.YES) ) l.setReadwrite(YesnoType.YES);
				return;
			}
		}
		this.languages.add(copyOfLanguage(language));
	}

	public void removeLanguage(CHARACTERLANGUAGEType language) {
		if( language == null ) return;
		String lang = language.getLanguage();
		for( CHARACTERLANGUAGEType l : languages ) {
			if( l.getLanguage().equals(lang) && l.getNotlearnedbyskill().equals(language.getNotlearnedbyskill())) {
				languages.remove(l);
				return;
			}
		}
	}

	public void insertLanguages(List<CHARACTERLANGUAGEType> languages) {
		if( languages == null ) return;
		for( CHARACTERLANGUAGEType l : languages ) insertLanguage(l);
	}

	public void removeAll(List<CHARACTERLANGUAGEType> languages) {
		if( languages == null ) return;
		for( CHARACTERLANGUAGEType l : languages ) removeLanguage(l);
	}

	public LanguageContainer copy() {
		LanguageContainer result = new LanguageContainer();
		for( CHARACTERLANGUAGEType l : languages ) result.insertLanguage(copyOfLanguage(l));
		return result;
	}

	public static CHARACTERLANGUAGEType copyOfLanguage(CHARACTERLANGUAGEType oldlang) {
		CHARACTERLANGUAGEType newlang = new CHARACTERLANGUAGEType();
		if(oldlang!=null) {
			newlang.setLanguage(oldlang.getLanguage());
			newlang.setNotlearnedbyskill(oldlang.getNotlearnedbyskill());
			newlang.setReadwrite(oldlang.getReadwrite());
			newlang.setSpeak(oldlang.getSpeak());
		}
		return newlang;
	}

	public int[] getCountOfSpeakReadWrite(YesnoType skill) {
		int[] result = {0,0};
		for( CHARACTERLANGUAGEType l : languages ) {
			if( (skill==null) || skill.equals(YesnoType.NA) || l.getNotlearnedbyskill().equals(skill) ) {
				if( l.getSpeak().equals(YesnoType.YES) ) result[0]++;
				if( l.getReadwrite().equals(YesnoType.YES) ) result[1]++;
			}
		}
		return result;
	}
}
