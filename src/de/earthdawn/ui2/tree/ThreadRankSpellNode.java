package de.earthdawn.ui2.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.SPELLDEFType;

public class ThreadRankSpellNode {
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final HashMap<String, SPELLDEFType> spelldefs = PROPERTIES.getSpells();
	private List<String> spelllist;
	private int spellindex;
	private SPELLDEFType spell;

	public ThreadRankSpellNode(List<String> list, int spellindex) {
		this.spelllist=list;
		this.spellindex=spellindex;
	}

	public String toString() {
		return spell.getName();
	}

	public String getSpellname() {
		return spell.getName();
	}

	public void setSpell(String spellname) {
		if( spelldefs.containsKey(spellname) ) spell = spelldefs.get(spellname);
		else {
			spell = new SPELLDEFType();
			spell.setName(spellname);
		}
		spelllist.set(spellindex, spell.getName());
	}

	public static Set<String> getSpellList() {
		return spelldefs.keySet();
	}
}
