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
	private SyllableType type=null;

	SyllableAttributes(String race,GenderType gender,int namepart, SyllableType type) {
		this.race=race;
		this.gender=gender;
		this.namepart=namepart;
		this.type=type;
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

	public SyllableType getType() {
		return type;
	}

	public void setType(SyllableType type) {
		this.type = type;
	}
}
