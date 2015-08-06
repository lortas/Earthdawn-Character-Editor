package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.SPELLType;

public class SpellComparator implements Comparator<SPELLType> {
	public int compare(SPELLType arg0, SPELLType arg1) {
		// Zuerst nach Name sortieren
		int result = arg0.getName().compareToIgnoreCase(arg1.getName());
		if( result != 0 ) return result;
		// Bei gleichem Name sortiere nach Type
		return arg0.getType().value().compareToIgnoreCase(arg1.getType().value());
	}
}
