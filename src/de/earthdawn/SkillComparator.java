package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.SKILLType;

public class SkillComparator implements Comparator<SKILLType> {
	public int compare(SKILLType arg0, SKILLType arg1) {
		// Zuerst nach Rang sortieren
		int rank0 = 0;
		int rank1 = 0;
		if( arg0.getRANK() != null ) rank0 = arg0.getRANK().getRank();
		if( arg1.getRANK() != null ) rank1 = arg1.getRANK().getRank();
		if( rank0 < rank1 ) return  1;
		if( rank0 > rank1 ) return -1;
		// Bei gleichem Rang sortiere nach Stufe
		int step0 = 0;
		int step1 = 0;
		if( arg0.getRANK() != null ) step0 = arg0.getRANK().getStep();
		if( arg1.getRANK() != null ) step1 = arg1.getRANK().getStep();
		if( step0 < step1 ) return  1;
		if( step0 > step1 ) return -1;
		// Bei gleicher Stufe sortiere nach SkillName
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}
}
