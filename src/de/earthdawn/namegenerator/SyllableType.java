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

public enum SyllableType {
	BEGIN("begin"),END("end"),MID("middle");
	private final String value;

	SyllableType(String value) {
		this.value=value;
	}

	public static SyllableType fromValue(String value) {
		for (SyllableType c: SyllableType.values()) if (c.value.equals(value)) return c;
		throw new IllegalArgumentException(value);
	}
}
