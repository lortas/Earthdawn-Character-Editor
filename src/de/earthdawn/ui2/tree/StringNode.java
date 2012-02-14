package de.earthdawn.ui2.tree;

import java.util.List;

import de.earthdawn.ui2.tree.StringNodeType;

public class StringNode {
	private List<String> stringlist;
	private int stringindex;
	private StringNodeType stringtype;

	public StringNode(List<String> list, int index, StringNodeType type) {
		this.stringlist=list;
		this.stringindex=index;
		this.stringtype=type;
	}

	public StringNodeType getType() {
		return stringtype;
	}

	public String getString() {
		return stringlist.get(stringindex);
	}

	public void setString(String string) {
		stringlist.set(stringindex, string);
	}

	public String toString() {
		return stringtype.value()+" : "+getString();
	}
}
