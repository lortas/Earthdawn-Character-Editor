package de.earthdawn.config;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.CHARACTERISTICS;
import de.earthdawn.data.CHARACTERISTICSENCUMBRANCE;
import de.earthdawn.data.CHARACTERISTICSATTRIBUTECOST;
import de.earthdawn.data.CHARACTERISTICSDEFENSERAITING;
import de.earthdawn.data.CHARACTERISTICSHEALTHRATING;
import de.earthdawn.data.CHARACTERISTICSINCREASECOST;
import de.earthdawn.data.CHARACTERISTICSINCREASECOSTCIRCLE;
import de.earthdawn.data.CHARACTERISTICSLEGENDARYSTATUS;
import de.earthdawn.data.CHARACTERISTICSMYSTICARMOR;
import de.earthdawn.data.CHARACTERISTICSSTEPDICETABLE;


public class ECECharacteristics {

	private static CHARACTERISTICS CHARACTERISTICS = null;

	public ECECharacteristics(CHARACTERISTICS c) {
		CHARACTERISTICS = c;
	}
	
	public List<Integer> getENCUMBRANCE() {
		List<Integer> result = new ArrayList<Integer>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("ENCUMBRANCE") ) {
				int attriute = ((CHARACTERISTICSENCUMBRANCE)element.getValue()).getAttribute();
				int carrying = ((CHARACTERISTICSENCUMBRANCE)element.getValue()).getCarrying();
				result.add(attriute, carrying);
			}
		}
		return result;
	}

	public HashMap<Integer,Integer> getATTRIBUTECOST() {
		HashMap<Integer,Integer> result = new HashMap<Integer,Integer>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("ATTRIBUTECOST") ) {
				int modifier = ((CHARACTERISTICSATTRIBUTECOST)element.getValue()).getModifier();
				int cost = ((CHARACTERISTICSATTRIBUTECOST)element.getValue()).getCost();
				result.put(modifier, cost);
			}
		}
		return result;
	}

	public List<CHARACTERISTICSDEFENSERAITING> getDEFENSERAITING() {
		List<CHARACTERISTICSDEFENSERAITING> result = new ArrayList<CHARACTERISTICSDEFENSERAITING>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("DEFENSERAITING") ) {
				result.add((CHARACTERISTICSDEFENSERAITING)element.getValue());
			}
		}
		return result;
	}

	public HashMap<Integer,CHARACTERISTICSHEALTHRATING> getHEALTHRATING() {
		HashMap<Integer,CHARACTERISTICSHEALTHRATING> result = new HashMap<Integer,CHARACTERISTICSHEALTHRATING>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("HEALTHRATING") ) {
				CHARACTERISTICSHEALTHRATING value = ((CHARACTERISTICSHEALTHRATING)element.getValue());
				result.put(value.getValue(), value);
			}
		}
		return result;
	}

	public List<Integer> getMYSTICARMOR() {
		List<Integer> result = new ArrayList<Integer>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("MYSTICARMOR") ) {
				int armor = ((CHARACTERISTICSMYSTICARMOR)element.getValue()).getArmor();
				int attribute = ((CHARACTERISTICSMYSTICARMOR)element.getValue()).getAttribute();
				result.add(armor, attribute);
			}
		}
		return result;
	}

	public CHARACTERISTICSSTEPDICETABLE getSTEPDICEbyStep(int step) {
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("STEPDICETABLE") ) {
				CHARACTERISTICSSTEPDICETABLE result = ((CHARACTERISTICSSTEPDICETABLE)element.getValue());
				if( result.getStep() == step ) {
					return result;
				}
			}
		}
		// Not found
		return null;
	}

	public CHARACTERISTICSSTEPDICETABLE getSTEPDICEbyAttribute(int attribute) {
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("STEPDICETABLE") ) {
				CHARACTERISTICSSTEPDICETABLE result = ((CHARACTERISTICSSTEPDICETABLE)element.getValue());
				if( result.getAttribute() == attribute ) {
					return result;
				}
			}
		}
		// Not found
		return null;
	}

	public int getTalentRankTotalLP(int circle, int rank) {
		// Summiere alle LegendenPunkte bis einschließlich "rank" pro Kreis
		HashMap<Integer,Integer> resultByCircle = new HashMap<Integer,Integer>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("TALENTLPCOST") ) {
				CHARACTERISTICSINCREASECOSTCIRCLE tmp = ((CHARACTERISTICSINCREASECOSTCIRCLE)element.getValue());
				if( tmp.getIncrease() <= rank ) {
					int sum = 0;
					if( resultByCircle.containsKey(tmp.getCircle()) ) {
						sum = resultByCircle.get(tmp.getCircle());
					}
					resultByCircle.put( tmp.getCircle() , sum+tmp.getCost() );
				}
			}
		}
		// Wähle nun den richtigen Kreis aus und gebe dessen Summe zurück
		while( circle >= 0) {
			if( resultByCircle.containsKey(circle) ) {
				return resultByCircle.get(circle);
			}
			// Für den gesuchten Kreis gab es keine LP-Summe, damit schauen wir nach einem kleinern Kreisr
			circle--;
		}
		// Not found
		return 0;
	}

	public int getSkillRankTotalLP(int rank) {
		// Summiere alle LegendenPunkte bis einschließlich "rank"
		int sum=0;
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("SKILLLPCOST") ) {
				CHARACTERISTICSINCREASECOST tmp = ((CHARACTERISTICSINCREASECOST)element.getValue());
				if( tmp.getIncrease() <= rank ) {
					sum += tmp.getCost();
				}
			}
		}
		return sum;
	}

	public int getAttributeTotalLP(int lpincrease) {
		// Summiere alle LegendenPunkte
		int sum=0;
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("ATTRIBUTELPCOST") ) {
				CHARACTERISTICSINCREASECOST tmp = ((CHARACTERISTICSINCREASECOST)element.getValue());
				if( tmp.getIncrease() <= lpincrease ) {
					sum += tmp.getCost();
				}
			}
		}
		return sum;
	}

	public CHARACTERISTICSLEGENDARYSTATUS getLegendaystatus(int circle) {
		HashMap<Integer,CHARACTERISTICSLEGENDARYSTATUS> status = new HashMap<Integer,CHARACTERISTICSLEGENDARYSTATUS>();
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("LEGENDARYSTATUS") ) {
				CHARACTERISTICSLEGENDARYSTATUS st = (CHARACTERISTICSLEGENDARYSTATUS)element.getValue();
				status.put(st.getCircle(),st);
			}
		}
		while( circle >= 0 ) {
			if( status.containsKey(circle)) {
				return status.get(circle);
			}
			circle--;
		}
		// Not found
		return null;
	}
}
