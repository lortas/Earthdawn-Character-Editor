package de.earthdawn.config;
/******************************************************************************\
Copyright (C) 2010-2011  Holger von Rhein <lortas@freenet.de>

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
\******************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.CHARACTERISTICS;
import de.earthdawn.data.CHARACTERISTICSCIRCLE;
import de.earthdawn.data.CHARACTERISTICSCOST;
import de.earthdawn.data.CHARACTERISTICSDISCIPLINENR;
import de.earthdawn.data.CHARACTERISTICSENCUMBRANCE;
import de.earthdawn.data.CHARACTERISTICSATTRIBUTECOST;
import de.earthdawn.data.CHARACTERISTICSDEFENSERAITING;
import de.earthdawn.data.CHARACTERISTICSHEALTHRATING;
import de.earthdawn.data.CHARACTERISTICSLEGENDARYSTATUS;
import de.earthdawn.data.CHARACTERISTICSMYSTICARMOR;
import de.earthdawn.data.CHARACTERISTICSRESULTLEVEL;
import de.earthdawn.data.CHARACTERISTICSSTEPDICETABLE;
import de.earthdawn.data.ROLLEDDICEType;


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
		return new CHARACTERISTICSSTEPDICETABLE();
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

	public int getTalentRankTotalLP(int discipline, int circle, int rank) {
		if( rank < 1 ) return 0;
		List<CHARACTERISTICSCOST> costs = getTalentRankLPIncreaseTable(discipline,circle);
		if( costs == null ) return 0;
		int result = 0;
		for( CHARACTERISTICSCOST talentcost : costs ) {
			result += talentcost.getCost();
			rank--;
			if( rank < 1 ) break;
		}
		return result;
	}

	public int getSkillRankTotalLP(int rank) {
		if( rank < 1 ) return 0;
		// Summiere alle LegendenPunkte bis einschließlich "rank"
		int sum=0;
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("SKILLLPCOST") ) {
				CHARACTERISTICSCOST tmp = ((CHARACTERISTICSCOST)element.getValue());
				sum += tmp.getCost();
				rank--;
				if( rank < 1 ) break;
			}
		}
		return sum;
	}

	public int getAttributeTotalLP(int lpincrease) {
		if( lpincrease < 1 ) return 0;
		// Summiere alle LegendenPunkte
		int sum=0;
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("ATTRIBUTELPCOST") ) {
				CHARACTERISTICSCOST tmp = ((CHARACTERISTICSCOST)element.getValue());
				sum += tmp.getCost();
				lpincrease--;
				if( lpincrease < 1 ) return sum;
			}
		}
		System.err.println("The attribute value was increased "+lpincrease+" times to often.");
		return sum;
	}

	public CHARACTERISTICSDISCIPLINENR getTalentRankLPIncreaseTable(int discipline) {
		if( discipline < 1 ) {
			System.err.println("Discipline number was smaller than one. Increase number to one.");
			discipline=1;
		}
		for( JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR() ) {
			if( element.getName().getLocalPart().equals("DISCIPLINENR") ) {
				discipline--;
				if( discipline == 0) return (CHARACTERISTICSDISCIPLINENR)element.getValue();
			}
		}
		//Not Found
		System.err.println("Can not find LP for talents of discipline number "+discipline);
		return null;
	}

	public List<CHARACTERISTICSCOST> getTalentRankLPIncreaseTable(int discipline, int circle) {
		CHARACTERISTICSDISCIPLINENR disciplinenr = getTalentRankLPIncreaseTable(discipline);
		if( disciplinenr == null ) return null;
		CHARACTERISTICSCIRCLE circlenr = null;
		for (CHARACTERISTICSCIRCLE tmp : disciplinenr.getCIRCLE()) {
			if( tmp.getCircle() > circle ) break;
			circlenr = tmp;
		}
		//System.out.println("getTalentRankLPIncreaseTable: circle: searchfor="+circle+" found="+circlenr.getCircle());
		if( circlenr == null ) {
			System.err.println("Can not find LP for talents of discipline number "+discipline+" and cirlce number "+circle);
			return null;
		}
		return circlenr.getTALENTLPCOST();
	}

	public int getSpellLP(int circle) {
		if( circle < 1 ) return 0;
		return getTalentRankLPIncreaseTable(1, 1).get(circle-1).getCost();
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

	public ROLLEDDICEType getResultLevel(int diceresult) {
		int difficultyNumber=0;
		ROLLEDDICEType rolledDice = new ROLLEDDICEType();
		rolledDice.setResult(diceresult);
		rolledDice.setPathetic(100);
		rolledDice.setPoor(0);
		rolledDice.setAverage(0);
		rolledDice.setGood(0);
		rolledDice.setExcellent(0);
		rolledDice.setExtraordinary(0);
		for (JAXBElement<?> element : CHARACTERISTICS.getENCUMBRANCEOrDEFENSERAITINGOrMYSTICARMOR()) {
			if( element.getName().getLocalPart().equals("RESULTLEVEL") ) {
				CHARACTERISTICSRESULTLEVEL rl = (CHARACTERISTICSRESULTLEVEL)element.getValue();
				if( diceresult >= rl.getExtraordinary() ) rolledDice.setExtraordinary(difficultyNumber);
				if( diceresult >= rl.getExcellent() ) rolledDice.setExcellent(difficultyNumber);
				if( diceresult >= rl.getGood() ) rolledDice.setGood(difficultyNumber);
				if( diceresult >= rl.getAverage() ) rolledDice.setAverage(difficultyNumber);
				if( diceresult >= rl.getPoor() ) {
					rolledDice.setPoor(difficultyNumber);
				} else {
					if( rolledDice.getPathetic() > difficultyNumber ) rolledDice.setPathetic(difficultyNumber);
				}
				
				difficultyNumber++;
			}
		}
		return rolledDice;
	}

}
