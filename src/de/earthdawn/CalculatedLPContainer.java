package de.earthdawn;
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

import java.util.List;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECharacteristics;
import de.earthdawn.data.ACCOUNTINGType;
import de.earthdawn.data.CALCULATEDLEGENDPOINTADJUSTMENTType;
import de.earthdawn.data.CALCULATEDLEGENDPOINTSType;
import de.earthdawn.data.CHARACTERISTICSCOST;
import de.earthdawn.data.NEWDISCIPLINETALENTADJUSTMENTType;
import de.earthdawn.data.PlusminusType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.USEDSTARTRANKSType;

public class CalculatedLPContainer {
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final USEDSTARTRANKSType OptionalRule_FreeStartRanks = PROPERTIES.getOptionalRules().getSTARTRANKS();
	public static final ECECharacteristics PROPERTIES_Characteristics= PROPERTIES.getCharacteristics();
	private final CALCULATEDLEGENDPOINTSType calculatedLP;

	public CalculatedLPContainer(CALCULATEDLEGENDPOINTSType calculatedLP) {
		this.calculatedLP = calculatedLP;
	}

	public CALCULATEDLEGENDPOINTSType getCalculatedLP() {
		return this.calculatedLP;
	}

	public CalculatedLPContainer copy() {
		CALCULATEDLEGENDPOINTSType result = new CALCULATEDLEGENDPOINTSType();
		result.setAttributes(calculatedLP.getAttributes());
		result.setDisciplinetalents(calculatedLP.getDisciplinetalents());
		result.setKarma(calculatedLP.getKarma());
		result.setKnacks(calculatedLP.getKnacks());
		result.setMagicitems(calculatedLP.getMagicitems());
		result.setOptionaltalents(calculatedLP.getOptionaltalents());
		result.setSkills(calculatedLP.getSkills());
		result.setSpells(calculatedLP.getSpells());
		result.setTotal(calculatedLP.getTotal());
		USEDSTARTRANKSType oldstartranks = calculatedLP.getUSEDSTARTRANKS();
		USEDSTARTRANKSType newstartranks = new USEDSTARTRANKSType();
		calculatedLP.setUSEDSTARTRANKS(newstartranks);
		if( oldstartranks != null ) {
			newstartranks.setSkills(oldstartranks.getSkills());
			newstartranks.setTalents(oldstartranks.getTalents());
			newstartranks.setSpells(oldstartranks.getSpells());
		}
		List<CALCULATEDLEGENDPOINTADJUSTMENTType> commonadjustment = result.getCOMMONADJUSTMENT();
		for( CALCULATEDLEGENDPOINTADJUSTMENTType e : calculatedLP.getCOMMONADJUSTMENT() ) commonadjustment.add(e);
		List<NEWDISCIPLINETALENTADJUSTMENTType> newdisciplietalentadjustment = result.getNEWDISCIPLINETALENTADJUSTMENT();
		for( NEWDISCIPLINETALENTADJUSTMENTType e : calculatedLP.getNEWDISCIPLINETALENTADJUSTMENT() ) newdisciplietalentadjustment.add(e);
		List<ACCOUNTINGType> newcalculationlp = result.getCALCULATIONLP();
		for( ACCOUNTINGType e : calculatedLP.getCALCULATIONLP() ) newcalculationlp.add(e);
		return new CalculatedLPContainer(result);
	}

	public void clear() {
		int attributes=0;
		int disciplinetalents=0;
		int karma=0;
		int knacks=0;
		int magicitems=0;
		int optionaltalents=0;
		int skills=0;
		int spells=0;
		for( CALCULATEDLEGENDPOINTADJUSTMENTType adjustment : calculatedLP.getCOMMONADJUSTMENT() ) {
			switch(adjustment.getType()) {
			case ATTRIBUTES:        attributes       +=adjustment.getValue(); break;
			case DISCIPLINETALENTS: disciplinetalents+=adjustment.getValue(); break;
			case KARMA:             karma            +=adjustment.getValue(); break;
			case KNACKS:            knacks           +=adjustment.getValue(); break;
			case MAGICITEMS:        magicitems       +=adjustment.getValue(); break;
			case OPTIONALTALENTS:   optionaltalents  +=adjustment.getValue(); break;
			case SKILLS:            skills           +=adjustment.getValue(); break;
			case SPELLS:            spells           +=adjustment.getValue(); break;
			}
		}
		for( NEWDISCIPLINETALENTADJUSTMENTType adjustment : calculatedLP.getNEWDISCIPLINETALENTADJUSTMENT() ) {
			switch(adjustment.getType()) {
			case ATTRIBUTES:        attributes       +=adjustment.getValue(); break;
			case DISCIPLINETALENTS: disciplinetalents+=adjustment.getValue(); break;
			case KARMA:             karma            +=adjustment.getValue(); break;
			case KNACKS:            knacks           +=adjustment.getValue(); break;
			case MAGICITEMS:        magicitems       +=adjustment.getValue(); break;
			case OPTIONALTALENTS:   optionaltalents  +=adjustment.getValue(); break;
			case SKILLS:            skills           +=adjustment.getValue(); break;
			case SPELLS:            spells           +=adjustment.getValue(); break;
			}
		}
		calculatedLP.setAttributes(attributes);
		calculatedLP.setDisciplinetalents(disciplinetalents);
		calculatedLP.setKarma(karma);
		calculatedLP.setKnacks(knacks);
		calculatedLP.setMagicitems(magicitems);
		calculatedLP.setOptionaltalents(optionaltalents);
		calculatedLP.setSkills(skills);
		calculatedLP.setSpells(spells);
		calculatedLP.getCALCULATIONLP().clear();
		USEDSTARTRANKSType startranks = calculatedLP.getUSEDSTARTRANKS();
		if( startranks==null ) {
			startranks = new USEDSTARTRANKSType();
			calculatedLP.setUSEDSTARTRANKS(startranks);
		}
		startranks.setSkills(-OptionalRule_FreeStartRanks.getSkills());
		startranks.setTalents(-OptionalRule_FreeStartRanks.getTalents());
		startranks.setSpells(0);
	}

	public int getUsedTalentsStartRanks() {
		return calculatedLP.getUSEDSTARTRANKS().getTalents();
	}

	public void setUsedTalentsStartRanks(int rank) {
		calculatedLP.getUSEDSTARTRANKS().setTalents(rank);
	}

	public int getUsedSkillsStartRanks() {
		return calculatedLP.getUSEDSTARTRANKS().getSkills();
	}

	public void setUsedSkillsStartRanks(int rank) {
		calculatedLP.getUSEDSTARTRANKS().setSkills(rank);
	}

	public void setUsedSpellsStartRanks(int rank) {
		calculatedLP.getUSEDSTARTRANKS().setSpells(rank);
	}

	private void addCalculationLPs(int lp, String comment) {
		addCalculationLPs(lp,comment,CharacterContainer.getCurrentDateTime());
	}

	private void addCalculationLPs(int lp, String comment, String when) {
		ACCOUNTINGType a = new ACCOUNTINGType();
		a.setComment(comment);
		a.setType(PlusminusType.MINUS);
		a.setWhen(when);
		a.setValue(lp);
		calculatedLP.getCALCULATIONLP().add(a);
	}

	public void addAttribute(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setAttributes(calculatedLP.getAttributes()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addKarma(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setKarma(calculatedLP.getKarma()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addSkills(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setSkills(calculatedLP.getSkills()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addSpells(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setSpells(calculatedLP.getSpells()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addKnacks(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setKnacks(calculatedLP.getKnacks()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addDisciplinetalents(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setDisciplinetalents(calculatedLP.getDisciplinetalents()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addMagicitems(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setMagicitems(calculatedLP.getMagicitems()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addOptionaltalents(int lp, String comment) {
		if( lp == 0 ) return;
		calculatedLP.setOptionaltalents(calculatedLP.getOptionaltalents()+lp);
		addCalculationLPs(lp,comment);
	}

	public void addThreadItems(List<THREADITEMType> threaditems) {
		for( THREADITEMType item : threaditems ) {
			String name = item.getName();
			int weaven = item.getWeaventhreadrank();
			int rank=0;
			List<CHARACTERISTICSCOST> LpCosts = PROPERTIES_Characteristics.getTalentRankLPIncreaseTable(1,item.getLpcostgrowth() );
			for( THREADRANKType threadrank : item.getTHREADRANK() ) {
				threadrank.setLpcost( LpCosts.get(rank).getCost() );
				rank++;
				if( weaven > 0 ) {
					addMagicitems(threadrank.getLpcost(),"ThreadItem '"+name+"' rank "+rank);
				}
				weaven--;
			}
		}
	}

	public void calclulateTotal() {
		if( calculatedLP.getSpells() < 0 ) calculatedLP.setSpells(0);
		calculatedLP.setTotal(calculatedLP.getAttributes()+calculatedLP.getDisciplinetalents()+
				calculatedLP.getKarma()+calculatedLP.getMagicitems()+calculatedLP.getOptionaltalents()+
				calculatedLP.getSkills()+calculatedLP.getSpells()+calculatedLP.getKnacks());
	}
}
