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

import de.earthdawn.data.GenderType;

class SyllableAttributes {
	private String race = null;
	private GenderType gender = null;
	private int namepart = -1;
	private boolean start=false;

	SyllableAttributes(String race,GenderType gender,int namepart, boolean start) {
		this(race,gender,namepart);
		this.setStart(start);
	}

	SyllableAttributes(String race,GenderType gender,int namepart) {
		this.race=race;
		this.gender=gender;
		this.namepart=namepart;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public int getNamepart() {
		return namepart;
	}

	public void setNamepart(int namepart) {
		this.namepart = namepart;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isStart() {
		return start;
	}

	public boolean equals(Object obj) {
		if( ! (obj instanceof SyllableAttributes) ) return false;
		SyllableAttributes a = ((SyllableAttributes)obj);
		if( ! a.gender.equals(this.gender) ) return false;
		if( ! a.race.equals(this.race) ) return false;
		if( a.namepart != this.namepart ) return false;
		return true;
	}

	public String toString() {
		return this.race+";"+this.gender.toString()+";"+String.valueOf(namepart);
	}

	public String toStringAll() {
		return toString()+";"+String.valueOf(this.start);
	}

	public int hashCode() {
		return toString().hashCode();
	}
}
