package de.earthdawn.ui2.tree;

import java.util.List;

public class ThreadRankAbilityNode {
	private List<String> abilitylist;
	private int abilityindex;

	public ThreadRankAbilityNode(List<String> list, int index) {
		this.abilitylist=list;
		this.abilityindex=index;
	}

	public String getAbility() {
		return abilitylist.get(abilityindex);
	}

	public void setAbility(String ability) {
		abilitylist.set(abilityindex,ability);
	}

	public String toString() {
		return getAbility();
	}
}
