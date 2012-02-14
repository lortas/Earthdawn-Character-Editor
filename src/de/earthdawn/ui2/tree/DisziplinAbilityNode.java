package de.earthdawn.ui2.tree;

import java.util.List;
import de.earthdawn.data.DISZIPINABILITYType;

public class DisziplinAbilityNode {
	private List<DISZIPINABILITYType> diziplinAbilityList;
	private int diziplinAbilityindex;
	private DisziplinAbilityNodeType diziplinAbilitytype;

	public DisziplinAbilityNode(List<DISZIPINABILITYType> list, int index, DisziplinAbilityNodeType type) {
		this.diziplinAbilityList=list;
		this.diziplinAbilityindex=index;
		this.diziplinAbilitytype=type;
	}

	public DisziplinAbilityNodeType getType() {
		return diziplinAbilitytype;
	}

	public DISZIPINABILITYType getDisziplinAbility() {
		return diziplinAbilityList.get(diziplinAbilityindex);
	}

	public void setSiziplinAbility(DISZIPINABILITYType diziplinAbility) {
		diziplinAbilityList.set(diziplinAbilityindex,diziplinAbility);
	}

	public String toString() {
		DISZIPINABILITYType a = getDisziplinAbility();
		return diziplinAbilitytype.value()+" : "+a.getCount();
	}
}
