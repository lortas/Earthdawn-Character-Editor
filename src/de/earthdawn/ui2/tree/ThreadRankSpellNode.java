package de.earthdawn.ui2.tree;

import java.util.HashMap;
import java.util.Set;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.SPELLDEFType;
import de.earthdawn.data.THREADRANKType;

public class ThreadRankSpellNode {
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final HashMap<String, SPELLDEFType> spells = PROPERTIES.getSpells();
	private THREADRANKType threadrank;
	private int spellindex;
	private SPELLDEFType spell;

	public ThreadRankSpellNode(THREADRANKType threadrank, int spellindex) {
		this.threadrank=threadrank;
		this.spellindex=spellindex;
	}

	public String toString() {
		return spell.getName();
	}

	public void setSpell(String spellname) {
		if( spells.containsKey(spellname) ) spell = spells.get(spellname);
		else {
			spell = new SPELLDEFType();
			spell.setName(spellname);
		}
		threadrank.getSPELL().set(spellindex, spell.getName());
	}

	public static Set<String> getSpellList() {
		return spells.keySet();
	}
}
