package de.earthdawn;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;

import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.TALENTType;
import java.util.LinkedList;

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
		List<TALENTType> tals = getTalents(TalentKind.OPT);
		// Optional talents with rank 0 are not learned and will be removed.
		List<TALENTType> remove = new LinkedList<>();
		for( TALENTType t : tals ) {
			RANKType rank = t.getRANK();
			if( rank == null || rank.getRank() < 1 ) {
				remove.add(t);
			}
		}
		tals.removeAll(remove);
		return tals;
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
			List<TALENTType> t;
			if( e == TalentKind.OPT ) t = getOptionaltalents();
			else  t = talents.get(e);
			if( t != null ) result.addAll(t);
		}
		return result;
	}
}
