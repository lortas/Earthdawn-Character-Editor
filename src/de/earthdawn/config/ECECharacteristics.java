package de.earthdawn.config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;


import de.earthdawn.data.CHARACTERISTICS;
import de.earthdawn.data.CHARACTERISTICSENCUMBRANCE;
import de.earthdawn.data.CHARACTERISTICSATTRIBUTECOST;
import de.earthdawn.data.CHARACTERISTICSDEFENSERAITING;
import de.earthdawn.data.CHARACTERISTICSHEALTHRATING;
import de.earthdawn.data.CHARACTERISTICSMYSTICARMOR;
import de.earthdawn.data.CHARACTERISTICSSTEPDICETABLE;


public class ECECharacteristics {

	private static CHARACTERISTICS CHARACTERISTICS = null;

	public ECECharacteristics(CHARACTERISTICS c) {
		CHARACTERISTICS = c;
	}
	
	public List<Integer> getENCUMBRANCE() {
		List<Integer> result = new ArrayList<Integer>();
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSENCUMBRANCE ) {
				int attriute = ((CHARACTERISTICSENCUMBRANCE)element).getAttribute();
				int carrying = ((CHARACTERISTICSENCUMBRANCE)element).getCarrying();
				result.add(attriute, carrying);
			}
		}
		return result;
	}

	public HashMap<Integer,Integer> getATTRIBUTECOST() {
		HashMap<Integer,Integer> result = new HashMap<Integer,Integer>();
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSATTRIBUTECOST ) {
				int modifier = ((CHARACTERISTICSATTRIBUTECOST)element).getModifier();
				int cost = ((CHARACTERISTICSATTRIBUTECOST)element).getCost();
				result.put(modifier, cost);
			}
		}
		return result;
	}

	public List<CHARACTERISTICSDEFENSERAITING> getDEFENSERAITING() {
		List<CHARACTERISTICSDEFENSERAITING> result = new ArrayList<CHARACTERISTICSDEFENSERAITING>();
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSDEFENSERAITING ) {
				result.add((CHARACTERISTICSDEFENSERAITING)element);
			}
		}
		return result;
	}

	public HashMap<Integer,CHARACTERISTICSHEALTHRATING> getHEALTHRATING() {
		HashMap<Integer,CHARACTERISTICSHEALTHRATING> result = new HashMap<Integer,CHARACTERISTICSHEALTHRATING>();
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSHEALTHRATING ) {
				CHARACTERISTICSHEALTHRATING value = ((CHARACTERISTICSHEALTHRATING)element);
				result.put(value.getValue(), value);
			}
		}
		return result;
	}

	public List<Integer> getMYSTICARMOR() {
		List<Integer> result = new ArrayList<Integer>();
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSMYSTICARMOR ) {
				int armor = ((CHARACTERISTICSMYSTICARMOR)element).getArmor();
				int attribute = ((CHARACTERISTICSMYSTICARMOR)element).getAttribute();
				result.add(armor, attribute);
			}
		}
		return result;
	}

	public CHARACTERISTICSSTEPDICETABLE getSTEPDICEbyStep(int step) {
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSSTEPDICETABLE ) {
				CHARACTERISTICSSTEPDICETABLE result = ((CHARACTERISTICSSTEPDICETABLE)element);
				if( result.getStep() == step ) {
					return result;
				}
			}
		}
		// Not found
		return null;
	}

	public CHARACTERISTICSSTEPDICETABLE getSTEPDICEbyAttribute(int attribute) {
		for (Object element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element instanceof CHARACTERISTICSSTEPDICETABLE ) {
				CHARACTERISTICSSTEPDICETABLE result = ((CHARACTERISTICSSTEPDICETABLE)element);
				if( result.getAttribute() == attribute ) {
					return result;
				}
			}
		}
		// Not found
		return null;
	}
}
