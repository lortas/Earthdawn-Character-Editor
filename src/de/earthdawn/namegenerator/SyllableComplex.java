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

import java.util.List;
import java.util.ArrayList;

import de.earthdawn.data.GenderType;

public class SyllableComplex {
	private String syl;
	private List<SyllableAttributes> attributes = new ArrayList<SyllableAttributes>();
	private List<SyllableComplex> next = new ArrayList<SyllableComplex>();

	public SyllableComplex(String sSyl) {
		this.syl = sSyl;
	}

	public String getSyl() {
		return this.syl;
	}

	public boolean startsWith(SyllableSimple syl) {
		return startsWith(syl.getSyl());
	}

	public boolean startsWith(SyllableComplex syl) {
		return startsWith(syl.syl);
	}

	public boolean startsWith(String syl) {
		return this.syl.startsWith(syl);
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(syl);
		result.append("$");
		for( SyllableAttributes a : this.attributes ) {
			result.append("(");
			result.append(a.toStringAll());
			result.append(")");
		}
		result.append("$");
		return result.toString();
	}

	// Prüfe nach, ob die Silbe für eine bestimmt, Rassen, Geschlecht und Namensteil eine Startsilbe ist
	public boolean isStartFor(String race, GenderType gender, int namepart) {
		return isStartFor(new SyllableAttributes(race, gender, namepart, false));
	}

	public boolean isStartFor(SyllableAttributes attr) {
		int idx=this.attributes.indexOf(attr);
		if( idx < 0 ) return false;
		return this.attributes.get(idx).isStart();
	}

	// Prüfe nach, ob die Silbe für eine bestimmt, Rassen, Geschlecht und Namensteil eine Silbe ist
	public boolean isSyllableFor(SyllableAttributes attr) {
		return this.attributes.contains(attr);
	}

	public void insertAttributes(String race, GenderType gender, int namepart, boolean start) {
		insertAttributes(new SyllableAttributes(race, gender, namepart, start));
	}

	public void insertAttributes(SyllableAttributes attributes) {
		// Füge die Silbeneigenschaft nur ein, wenn Sie noch nicht vorhanden sein sollte.
		int idx = this.attributes.indexOf(attributes);
		if( idx < 0 ) this.attributes.add(attributes);
		else if( attributes.isStart() ){
			// Stelle sicher, dass die gespeicherte Silbeneigenschaft auch eine Startsilbe ist,
			// wenn es die einzugügende auch ist.
			this.attributes.get(idx).setStart(true);
		}
	}

	public void insertNext(SyllableComplex next ) {
		// Füge die nächste Silbe nur ein, wenn Sie noch nicht eingefügt sein sollte
		if( ! this.next.contains(next) ) this.next.add(next);
	}

	public List<SyllableComplex> getNextUnisex(String race, GenderType gender, int namepart ) {
		List<SyllableComplex> result = new ArrayList<SyllableComplex>();
		result.addAll(getNext(new SyllableAttributes(race, gender, namepart, false)));
		for(SyllableComplex syl : getNext(new SyllableAttributes(race, GenderType.MINUS, namepart, false))) {
			if( ! result.contains(syl) ) result.add(syl); 
		}
		return result;
	}

	public List<SyllableComplex> getNext(String race, GenderType gender, int namepart ) {
		SyllableAttributes attr = new SyllableAttributes(race, gender, namepart, false);
		return getNext(attr);
	}

	public List<SyllableComplex> getNext(SyllableAttributes attr) {
		List<SyllableComplex> result = new ArrayList<SyllableComplex>();
		for( SyllableComplex s : this.next ) {
			if( s.isSyllableFor(attr) ) result.add(s);
		}
		return result;
	}

	public boolean equals(Object obj) {
		return (obj instanceof SyllableComplex) && ((SyllableComplex)obj).syl.equals(this.syl);
	}

	public int hashCode() {
		return 97*this.syl.hashCode();
	}
}
