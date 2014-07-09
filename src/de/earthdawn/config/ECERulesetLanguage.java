package de.earthdawn.config;
/******************************************************************************\
Copyright (C) 2014  Holger von Rhein <lortas@freenet.de>

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

import de.earthdawn.data.LanguageType;
import de.earthdawn.data.RulesetversionType;

public class ECERulesetLanguage implements Comparable<ECERulesetLanguage> {

	private RulesetversionType rulesetversion = RulesetversionType.ED_3;
	private LanguageType language = LanguageType.EN;

	public ECERulesetLanguage(){}

	public ECERulesetLanguage(RulesetversionType rulesetversion, LanguageType language) {
		this.rulesetversion=(rulesetversion==null)?RulesetversionType.ED_3:rulesetversion;
		this.language=(language==null)?LanguageType.EN:language;
	}

	public LanguageType getLanguage() {
		return this.language;
	}

	public RulesetversionType getRulesetversion() {
		return this.rulesetversion;
	}

	@Override
	public int compareTo(ECERulesetLanguage a) {
		int r = this.rulesetversion.value().compareTo(a.rulesetversion.value());
		if( r != 0 ) return r;
		return this.language.value().compareTo(a.language.value());
	}

	@Override
	public String toString() {
		return this.rulesetversion.value()+this.language.value();
	}

	@Override
	public boolean equals(Object o) {
		if( ! (o instanceof ECERulesetLanguage) ) return false;
		ECERulesetLanguage e = (ECERulesetLanguage)o;
		if( ! e.getRulesetversion().equals(this.getRulesetversion()) ) return false;
		if( ! e.getLanguage().equals(this.getLanguage()) ) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
