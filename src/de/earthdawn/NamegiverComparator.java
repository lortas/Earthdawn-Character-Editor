package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.NAMEGIVERABILITYType;

public class NamegiverComparator implements Comparator<NAMEGIVERABILITYType> {
	public int compare(NAMEGIVERABILITYType arg0, NAMEGIVERABILITYType arg1) {
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}
}
