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
import de.earthdawn.data.LearnedbyType;

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
		String langName = language.getLanguage();
		LearnedbyType speak = language.getSpeak();
		LearnedbyType readwrite = language.getReadwrite();
		for( CHARACTERLANGUAGEType l : this.languages ) {
			if( l.getLanguage().equals(langName) ) {
				if( speak.equals(LearnedbyType.SKILL) ) switch( l.getSpeak() ) {
				case NO:
					l.setSpeak(LearnedbyType.SKILL);
					speak = LearnedbyType.NO;
					break;
				case SKILL:
					speak = LearnedbyType.NO;
					break;
				case TALENT:
					break;
				case OTHER:
					break;
				}
				if( readwrite.equals(LearnedbyType.SKILL) ) switch( l.getReadwrite() ) {
				case NO:
					l.setReadwrite(LearnedbyType.SKILL);
					readwrite=LearnedbyType.NO;
					break;
				case SKILL:
					readwrite=LearnedbyType.NO;
					break;
				case TALENT:
					break;
				case OTHER:
					break;
				}
			}
		}
		if( speak.equals(LearnedbyType.NO) && readwrite.equals(LearnedbyType.NO) ) return;
		CHARACTERLANGUAGEType l=new CHARACTERLANGUAGEType();
		l.setLanguage(langName);
		l.setSpeak(speak);
		l.setReadwrite(readwrite);
		this.languages.add(l);
	}

	public void removeLanguage(CHARACTERLANGUAGEType language) {
		if( language == null ) return;
		String langName = language.getLanguage();
		LearnedbyType speak = language.getSpeak();
		LearnedbyType readwrite = language.getReadwrite();
		List<CHARACTERLANGUAGEType> remove = new ArrayList<CHARACTERLANGUAGEType>();
		for( CHARACTERLANGUAGEType l : languages ) {
			if( l.getLanguage().equals(langName) ) {
				if( l.getSpeak().equals(speak) ) {
					l.setSpeak(LearnedbyType.NO);
					speak=LearnedbyType.NO;
				}
				if( l.getReadwrite().equals(readwrite) ) {
					l.setReadwrite(LearnedbyType.NO);
					readwrite=LearnedbyType.NO;
				}
			}
			if( l.getSpeak().equals(LearnedbyType.NO) && l.getReadwrite().equals(LearnedbyType.NO) ) remove.add(l);
		}
		languages.removeAll(remove);
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
			newlang.setReadwrite(oldlang.getReadwrite());
			newlang.setSpeak(oldlang.getSpeak());
		}
		return newlang;
	}

	public int[] getCountOfSpeakReadWrite(LearnedbyType learnedby) {
		int[] result = {0,0};
		if( learnedby==null ) for( CHARACTERLANGUAGEType l : languages ) {
			if( ! l.getSpeak().equals(LearnedbyType.NO) ) result[0]++;
			if( ! l.getReadwrite().equals(LearnedbyType.NO) ) result[1]++;
		}
		else for( CHARACTERLANGUAGEType l : languages ) {
			if( l.getSpeak().equals(learnedby) ) result[0]++;
			if( l.getReadwrite().equals(learnedby) ) result[1]++;
		}
		return result;
	}
}
