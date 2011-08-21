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
import java.util.Vector;

import de.earthdawn.data.GenderType;

public class Syllable {
	private String sSyl;
	private HashMap<String,HashMap<GenderType,List<List<Syllable>>>> next;
	private SyllableType type;

	public Syllable(String sSyl, String type ) {
		this(sSyl,SyllableType.fromValue(type));
	}

	public Syllable(String sSyl, SyllableType type ) {
		this.sSyl = sSyl;
		this.type = type;
		this.next = new HashMap<String,HashMap<GenderType,List<List<Syllable>>>>();
	}

	public void isalso(SyllableType type) {
		if( type.equals(this.type) ) return;
		this.type = SyllableType.MID;
	}

	public String toString() {
		return this.toString(null);
	}

	public String toString(String indent) {
		StringBuffer result = new StringBuffer();
		if( indent != null ) result.append(indent);
		else indent="";
		switch( this.type ) {
		case BEGIN:
			result.append("$BEGIN$");
			result.append(sSyl);
			result.append("\n");
			result.append(indent);
			break;
		case END:
			result.append(sSyl);
			result.append("$END$");
			result.append("\n");
			return result.toString();
		default:
			result.append(sSyl);
			result.append("\n");
			result.append(indent);
			break;
		}
		for( String racename : this.next.keySet() ) {
			result.append(racename);
			result.append(":");
			HashMap<GenderType, List<List<Syllable>>> race = this.next.get(racename);
			for( GenderType gendertype : race.keySet() ) {
				result.append(gendertype.value());
				result.append(":");
				int partpos = 0;
				for( List<Syllable> part : race.get(gendertype) ) {
					result.append(partpos);
					result.append(":\n");
					for( Syllable s : part ) s.toString(indent+" ");
					partpos++;
				}
			}
		}
		return result.toString();
	}

	public void insertNext(String race, GenderType gender, int part, String syl, SyllableType type ) {
		insertNext(race,gender,part,new Syllable(syl,type));
	}

	public void insertNext(String race, GenderType gender, int part, Syllable syl ) {
		HashMap<GenderType, List<List<Syllable>>> r = this.next.get(race);
		if( r == null ) {
			r = new HashMap<GenderType, List<List<Syllable>>>();
			this.next.put(race, r);
		}
		List<List<Syllable>> g = r.get(gender);
		if( g == null ) {
			g = new Vector<List<Syllable>>();
			r.put(gender,g);
		}
		while( g.size() <= part+1 ) g.add(new Vector<Syllable>());
		List<Syllable> p = g.get(part);
		if( ! p.contains(syl) ) p.add(syl);
	}

	public List<Syllable> getNext(String race, GenderType gender, int part ) {
		HashMap<GenderType, List<List<Syllable>>> r = this.next.get(race);
		if( r == null ) return null;
		List<List<Syllable>> g = r.get(gender);
		return g.get(part);
	}

	public HashMap<GenderType, List<List<Syllable>>> getNext(int race) {
		String[] raceKeys = (String[]) this.next.keySet().toArray();
		if( raceKeys.length == 0 ) return null;
		race %= raceKeys.length;
		return this.next.get(raceKeys[race]);
	}

	public List<List<Syllable>> getNext(int race, int gender) {
		HashMap<GenderType, List<List<Syllable>>> raceValues = this.getNext(race);
		if( raceValues == null ) return null;
		GenderType[] genderKeys = (GenderType[]) raceValues.keySet().toArray();
		if( genderKeys.length == 0 ) return null;
		gender %= genderKeys.length;
		return raceValues.get(genderKeys[gender]); 
	}

	public List<Syllable> getNext(int race, int gender, int part) {
		List<List<Syllable>> s = this.getNext(race,gender);
		part %= s.size();
		return(s.get(part));
	}

	public Syllable getNext(int race, int gender, int part, int syllable ) {
		List<Syllable> s = this.getNext(race,gender,part);
		syllable %= s.size();
		return(s.get(syllable));
	}

	public boolean equals(Object obj) {
		return (obj instanceof Syllable) && ((Syllable)obj).sSyl.equals(this.sSyl);
	}
}
