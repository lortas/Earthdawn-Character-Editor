package de.earthdawn.config;

import java.util.ArrayList;
import java.util.List;

import de.earthdawn.data.CharsheettemplatetalentType;

public class CharsheettemplatetalentStack {
	private List<CharsheettemplatetalentType> stack = new ArrayList<CharsheettemplatetalentType>();

	public void push(CharsheettemplatetalentType item) {
		stack.add(item);
	}

	public CharsheettemplatetalentType pull(int discipline) {
		int i=nextIndex(discipline);
		if( i<0 ) return null;
		CharsheettemplatetalentType e = stack.get(i);
		stack.remove(i);
		return e;
	}

	public boolean hasNext(int discipline) {
		if( nextIndex(discipline) < 0 ) return false;
		return true;
	}

	public int nextIndex(int discipline) {
		for( int i=0; i<stack.size(); i++ ) {
			CharsheettemplatetalentType e = stack.get(i);
			if( (discipline==0) || ((discipline==1) && e.isDiscipline()) || ((discipline==-1) && !e.isDiscipline()) ) {
				return i;
			}
		}
		return -1;
	}
}
