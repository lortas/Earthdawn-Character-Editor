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

public class SyllableSimple {
	private String syl;
	private List<SyllableSimple> next;
	private SyllableType type;

	public SyllableSimple(String sSyl, String type ) {
		this(sSyl,SyllableType.fromValue(type));
	}

	public SyllableSimple(String sSyl, SyllableType type ) {
		this.syl = sSyl;
		this.type = type;
		this.next = new ArrayList<SyllableSimple>();
	}

	public void isAlso(SyllableType type) {
		if( type.equals(this.type) ) return;
		this.type = SyllableType.MID;
	}

	public String getSyl() {
		return this.syl;
	}

	public boolean startsWith(SyllableSimple syl) {
		return startsWith(syl.syl);
	}

	public boolean startsWith(SyllableComplex syl) {
		return startsWith(syl.getSyl());
	}

	public boolean startsWith(String syl) {
		return this.syl.startsWith(syl);
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		switch( this.type ) {
		case BEGIN:
			result.append("$BEGIN$");
			result.append(syl);
			break;
		case END:
			result.append(syl);
			result.append("$END$");
			return result.toString();
		default:
			result.append(syl);
			break;
		}
		for( SyllableSimple nextSyl : this.next ) {
			result.append("$");
			result.append(nextSyl.hashCode());
		}
		return result.toString();
	}

	public void insertNext(String syl, SyllableType type ) {
		insertNext(new SyllableSimple(syl,type));
	}

	public void insertNext( SyllableSimple syl ) {
		if( this.next.contains(syl) ) return;
		this.next.add(syl);
	}

	public List<SyllableSimple> getNext() {
		return this.next;
	}

	public boolean equals(Object obj) {
		return (obj instanceof SyllableSimple) && ((SyllableSimple)obj).getSyl().equals(this.syl);
	}

	public int hashCode() {
		return 89*this.syl.hashCode();
	}
}
