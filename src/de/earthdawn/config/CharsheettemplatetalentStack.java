package de.earthdawn.config;

import java.util.Iterator;
import java.util.LinkedList;

import de.earthdawn.data.CharsheettemplatetalentType;

public class CharsheettemplatetalentStack {
	public static class type {
		public boolean isDiscipline=false;
		public boolean isOther=false;
	}
	private LinkedList<CharsheettemplatetalentType> stack = new LinkedList<CharsheettemplatetalentType>();

	public void push(CharsheettemplatetalentType item) {
		stack.add(item);
	}

	public CharsheettemplatetalentType pull(type fieldkind) {
		Iterator<CharsheettemplatetalentType> itr = stack.iterator();
		while( itr.hasNext() ) {
			CharsheettemplatetalentType e = itr.next();
			if( ( fieldkind.isDiscipline && e.isDiscipline() ) || 
				( fieldkind.isOther && e.isOther() ) ) {
				itr.remove();
				return e;
			}
		}
		return null;
	}
}
