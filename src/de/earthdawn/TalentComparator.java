package de.earthdawn;

import java.util.Comparator;
import de.earthdawn.data.TALENTType;

public class TalentComparator implements Comparator<TALENTType> {
	public int compare(TALENTType arg0, TALENTType arg1) {
		// Zuerst nach Kreis sortieren (aufsteigend)
		int circle0 = arg0.getCircle();
		int circle1 = arg1.getCircle();
		if( circle0 < circle1 ) return -1;
		if( circle0 > circle1 ) return  1;
		// Bei gleichem Kreis sortieren wie Skill
		return SkillComparator.compareWORealigned(arg0, arg1);
	}

	static public boolean equal(TALENTType arg0, TALENTType arg1) {
		return SkillComparator.equal(arg0, arg1);
	}
}
