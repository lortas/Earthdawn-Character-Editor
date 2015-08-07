package de.earthdawn;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import de.earthdawn.data.OPTIONALRULESUNITSType;
import de.earthdawn.data.UNITType;

public class UnitCalculator {
	private LinkedList<UNITType> lengthunits=new LinkedList<UNITType>();
	private LinkedList<UNITType> timeunits=new LinkedList<UNITType>();
	private LinkedList<UNITType> weightunits=new LinkedList<UNITType>();
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
			int tmp = new Double(Math.floor(0.07 + value * u.getTranslation())).intValue();
			if( tmp > 0 ) {
				value -= new Double(tmp).doubleValue() / u.getTranslation();
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

	public String formatLength(double length) {
		return formatUnit(length,this.lengthunits);
	}

	public String formatWeight(double length) {
		return formatUnit(length,this.weightunits);
	}

	public String formatTime(double length) {
		return formatUnit(length,this.timeunits);
	}
}
