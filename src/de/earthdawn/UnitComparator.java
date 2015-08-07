package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.UNITType;

public class UnitComparator implements Comparator<UNITType> {
	public int compare(UNITType arg0, UNITType arg1) {
		double d0 = arg0.getTranslation();
		double d1 = arg1.getTranslation();
		if( d0 < d1 ) return -1;
		if( d0 > d1 ) return 1;
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}
}
