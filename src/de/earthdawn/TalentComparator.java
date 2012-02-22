package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.TALENTType;

public class TalentComparator implements Comparator<TALENTType> {
	public int compare(TALENTType arg0, TALENTType arg1) {
		// Zuerst nach Kreis sortieren (aufsteigend)
		int circle0 = arg0.getCircle();
		int circle1 = arg1.getCircle();
		if( circle0 < circle1 ) return -1;
		if( circle0 > circle1 ) return  1;
		// Bei gleichem Kreis nach Rang sortieren (absteigend)
		int rank0 = 0;
		int rank1 = 0;
		if( arg0.getRANK() != null ) rank0 = arg0.getRANK().getRank();
		if( arg1.getRANK() != null ) rank1 = arg1.getRANK().getRank();
		if( rank0 < rank1 ) return  1;
		if( rank0 > rank1 ) return -1;
		// Bei gleichem Rang sortiere nach Stufe (absteigend)
		int step0 = 0;
		int step1 = 0;
		if( arg0.getRANK() != null ) step0 = arg0.getRANK().getStep();
		if( arg1.getRANK() != null ) step1 = arg1.getRANK().getStep();
		if( step0 < step1 ) return  1;
		if( step0 > step1 ) return -1;
		// Bei gleicher Stufe sortiere nach TalentName
		return arg0.getName().compareTo(arg1.getName());
	}

	static public boolean equal(TALENTType arg0, TALENTType arg1) {
		if( ! arg0.getName().equals(arg1.getName()) ) return false;
		if( ! arg0.getLimitation().equals(arg1.getLimitation()) ) return false;
		return true;
	}
}
