package de.earthdawn.namegenerator;
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

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import de.earthdawn.data.GenderType;

public class SyllableComplex {
	private String syl;
	private HashMap<String,HashMap<GenderType,List<List<SyllableComplex>>>> next;
	private SyllableType type;

	public SyllableComplex(String sSyl, String type ) {
		this(sSyl,SyllableType.fromValue(type));
	}

	public SyllableComplex(String sSyl, SyllableType type ) {
		this.syl = sSyl;
		this.type = type;
		this.next = new HashMap<String,HashMap<GenderType,List<List<SyllableComplex>>>>();
	}

	public void isAlso(SyllableType type) {
		if( type.equals(this.type) ) return;
		this.type = SyllableType.MID;
	}

	public String getSyl() {
		return this.syl;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		switch( this.type ) {
		case BEGIN:
			result.append("$BEGIN$");
			result.append(syl);
			result.append(":");
			break;
		case END:
			result.append(syl);
			result.append("$END$");
			return result.toString();
		default:
			result.append(syl);
			result.append(":");
			break;
		}
		for( String racename : this.next.keySet() ) {
			result.append(racename);
			result.append(":");
			HashMap<GenderType, List<List<SyllableComplex>>> race = this.next.get(racename);
			for( GenderType gendertype : race.keySet() ) {
				result.append(gendertype.value());
				result.append(":");
				int partpos = 0;
				for( List<SyllableComplex> part : race.get(gendertype) ) {
					result.append(partpos++);
					for(SyllableComplex s : part) {
						result.append(";");
						result.append(s.hashCode());
					}
				}
			}
		}
		return result.toString();
	}

	public void insertNext(String race, GenderType gender, int part, String syl, SyllableType type ) {
		insertNext(race,gender,part,new SyllableComplex(syl,type));
	}

	public void insertNext(String race, GenderType gender, int part, SyllableComplex syl ) {
		HashMap<GenderType, List<List<SyllableComplex>>> r = this.next.get(race);
		if( r == null ) {
			r = new HashMap<GenderType,List<List<SyllableComplex>>>();
			this.next.put(race, r);
		}
		List<List<SyllableComplex>> g = r.get(gender);
		if( g == null ) {
			g = new ArrayList<List<SyllableComplex>>();
			r.put(gender,g);
		}
		while( g.size() <= part ) g.add(new ArrayList<SyllableComplex>());
		List<SyllableComplex> p = g.get(part);
		if( ! p.contains(syl) ) p.add(syl);
	}

	public List<SyllableComplex> getNext(String race, GenderType gender, int part ) {
		HashMap<GenderType, List<List<SyllableComplex>>> r = this.next.get(race);
		if( r == null ) return null;
		List<SyllableComplex> result = new ArrayList<SyllableComplex>();
		result.addAll(r.get(gender).get(part));
		if( gender.equals(GenderType.MINUS) ) return result;
		List<List<SyllableComplex>> g = r.get(GenderType.MINUS);
		if( g == null ) return result;
		List<SyllableComplex> p = g.get(part);
		if( p == null ) return result;
		result.addAll(p);
		return result;
	}

	public HashMap<GenderType, List<List<SyllableComplex>>> getNext(int race) {
		String[] raceKeys = (String[]) this.next.keySet().toArray();
		if( raceKeys.length == 0 ) return null;
		race %= raceKeys.length;
		return this.next.get(raceKeys[race]);
	}

	public List<List<SyllableComplex>> getNext(int race, int gender) {
		HashMap<GenderType, List<List<SyllableComplex>>> raceValues = this.getNext(race);
		if( raceValues == null ) return null;
		GenderType[] genderKeys = (GenderType[]) raceValues.keySet().toArray();
		if( genderKeys.length == 0 ) return null;
		gender %= genderKeys.length;
		return raceValues.get(genderKeys[gender]); 
	}

	public List<SyllableComplex> getNext(int race, int gender, int part) {
		List<List<SyllableComplex>> s = this.getNext(race,gender);
		part %= s.size();
		return(s.get(part));
	}

	public SyllableComplex getNext(int race, int gender, int part, int syllable ) {
		List<SyllableComplex> s = this.getNext(race,gender,part);
		syllable %= s.size();
		return(s.get(syllable));
	}

	public boolean equals(Object obj) {
		return (obj instanceof SyllableComplex) && ((SyllableComplex)obj).getSyl().equals(this.syl);
	}
}
