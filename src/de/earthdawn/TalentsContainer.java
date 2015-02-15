package de.earthdawn;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;

import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.TALENTType;

public class TalentsContainer {
	public enum TalentKind { DIS, OPT, FRE };
	private Map<TalentKind,List<TALENTType>> talents=new TreeMap<TalentKind,List<TALENTType>>();

	public TalentsContainer() {}

	public TalentsContainer(DISCIPLINEType discipline) {
		setTalents(TalentKind.DIS,discipline.getDISZIPLINETALENT());
		setTalents(TalentKind.OPT,discipline.getOPTIONALTALENT());
		setTalents(TalentKind.FRE,discipline.getFREETALENT());
	}

	public void setTalents(TalentKind talentkind, List<TALENTType> talents) {
		for( TALENTType talent : talents ) {
			List<String> limitations = talent.getLIMITATION();
			String limitation=CharacterContainer.join( limitations );
			limitations.clear();
			if( !limitation.isEmpty() ) limitations.add(limitation);
		}
		this.talents.put(talentkind,talents);
	}
	public List<TALENTType> getDisciplinetalents() {
		return getTalents(TalentKind.DIS);
	}
	public List<TALENTType> getOptionaltalents() {
		return getTalents(TalentKind.OPT);
	}
	public List<TALENTType> getFreetalents() {
		return getTalents(TalentKind.FRE);
	}
	public List<TALENTType> getTalents(TalentKind kind) {
		return talents.get(kind);
	}
	public List<TALENTType> getAllTalents() {
		List<TALENTType> result = new ArrayList<TALENTType>();
		for( TalentKind e : TalentKind.values() ) {
			List<TALENTType> t = talents.get(e);
			if( t == null ) continue;
			result.addAll(t);
		}
		return result;
	}
}
