package de.earthdawn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.earthdawn.data.OPTIONALRULESUNITSType;
import de.earthdawn.data.UNITType;

public class UnitCalculator {
	private List<UNITType> lengthunits=new LinkedList<UNITType>();
	private List<UNITType> timeunits=new LinkedList<UNITType>();
	private List<UNITType> weightunits=new LinkedList<UNITType>();
	private List<Double> lengthsmalldivisor=new ArrayList<Double>();
	private int match = 0;
	public UnitCalculator(Collection <OPTIONALRULESUNITSType> unitsCollection, int maxunits) {
		if( unitsCollection == null ) {
			throw new IllegalArgumentException("There was no units set given.");
		}
		this.match = maxunits;
		Iterator<OPTIONALRULESUNITSType> itr = unitsCollection.iterator();
		while( itr.hasNext() ) {
			OPTIONALRULESUNITSType u = itr.next();
			if( u.isDisplayed() ) {
				this.lengthunits.addAll(u.getLENGTH());
				this.timeunits.addAll(u.getTIME());
				this.weightunits.addAll(u.getWEIGHT());
				this.lengthsmalldivisor.addAll(u.getLENGTHSMALLDIVISOR());
				break;
			}
		}
		if( this.lengthunits==null || this.timeunits==null || this.weightunits==null ) {
			throw new IllegalArgumentException("There was no units set flagged with displayed=true");
		}
		UnitComparator comparator = new UnitComparator();
		Collections.sort(this.lengthunits,comparator);
		Collections.sort(this.weightunits,comparator);
		Collections.sort(this.timeunits,comparator);
	}

	private String formatUnit(double value, Collection<UNITType> units) {
		StringBuilder sb = new StringBuilder();
		int match = 0;
		for( UNITType u : units ) {
			if( value <= 0 ) break;
			int tmp = Double.valueOf(Math.floor(0.07 + value * u.getTranslation())).intValue();
			if( tmp > 0 ) {
				value -= Double.valueOf(tmp).doubleValue() / u.getTranslation();
				sb.append(String.format(" %d%s",tmp,u.getName()));
				match++;
			}
			if( match >= this.match) break;
		}
		String ret = sb.toString().trim();
		if( ret.isEmpty() ) {
			if( value == 0 ) ret="0";
			else ret = String.valueOf(value);
		}
		return ret;
	}

	public String formatLength(double length, int sizemodifier) {
		if( sizemodifier < 0 ) {
			sizemodifier *= -1;
			if( this.lengthsmalldivisor.size() > sizemodifier ) {
				System.err.println(String.format("There was no lengthsmalldividor for value %d defined.",-sizemodifier));
			} else {
				length /= this.lengthsmalldivisor.get(sizemodifier-1);
			}
		} else if( sizemodifier > 0 ) {
			System.err.println(String.format("There was no lengthlargemodifikator for value %d defined.",sizemodifier));
		}
		return formatUnit(length,this.lengthunits);
	}

	public String formatWeight(double length) {
		return formatUnit(length,this.weightunits);
	}

	public String formatTime(double length) {
		return formatUnit(length,this.timeunits);
	}
}
