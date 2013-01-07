package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.SKILLType;

public class SkillComparator implements Comparator<SKILLType> {
	public int compare(SKILLType arg0, SKILLType arg1) {
		// Zu erst nur die nicht Realigned Skills
		int realigned0 = arg0.getRealigned();
		int realigned1 = arg1.getRealigned();
		if( (realigned0>0) && (realigned1<=0) ) return 1;
		if( (realigned0<=0) && (realigned1>0) ) return -1;
		return compareWORealigned(arg0,arg1);
	}

	static public int compareWORealigned(SKILLType arg0, SKILLType arg1) {
		// Nach Rang sortieren
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
		int cmp = arg0.getName().compareToIgnoreCase(arg1.getName());
		if( cmp != 0 ) return cmp;
		// Bei gleichen Namen sortiere nach Limitations
		int limitationSize0 = arg0.getLIMITATION().size();
		int limitationSize1 = arg1.getLIMITATION().size();
		int minLimitationSize=(limitationSize0<limitationSize1)?limitationSize0:limitationSize1;
		for( int i=0; i<minLimitationSize; i++) {
			cmp = arg0.getLIMITATION().get(i).compareToIgnoreCase(arg1.getLIMITATION().get(i));
			if( cmp != 0 ) return cmp;
		}
		if( limitationSize0 < limitationSize1 ) return  1;
		if( limitationSize0 > limitationSize1 ) return -1;
		return 0;
	}

	static public boolean equal(SKILLType arg0, SKILLType arg1) {
		if( ! arg0.getName().equals(arg1.getName()) ) return false;
		int limitationSize = arg0.getLIMITATION().size();
		if( arg1.getLIMITATION().size() != limitationSize ) return false;
		for( int i=0; i<limitationSize; i++) {
			if( ! arg0.getLIMITATION().get(i).equals(arg1.getLIMITATION().get(i)) ) return false;
		}
		return true;
	}
}
