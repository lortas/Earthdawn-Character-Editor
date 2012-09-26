package de.earthdawn.config;

import de.earthdawn.data.OPTIONALRULESDEFAULTOPTIONALTALENT;

public class DefaultOptionalTalent {
	private OPTIONALRULESDEFAULTOPTIONALTALENT defaultoptinaltalent;
	public DefaultOptionalTalent() {
		this(new OPTIONALRULESDEFAULTOPTIONALTALENT());
	}
	public DefaultOptionalTalent(OPTIONALRULESDEFAULTOPTIONALTALENT defaultoptinaltalent) {
		this.defaultoptinaltalent=defaultoptinaltalent;
	}

	public String toString() {
		return "D:"+defaultoptinaltalent.getDiscipline()+", C:"+defaultoptinaltalent.getCircle()+", "+defaultoptinaltalent.getTalent();
	}

	public String getName() {
		return defaultoptinaltalent.getTalent();
	}

	public void setName(String name) {
		defaultoptinaltalent.setTalent(name);
	}

	public OPTIONALRULESDEFAULTOPTIONALTALENT getTalent() {
		return defaultoptinaltalent;
	}

	public void setTalent(
			OPTIONALRULESDEFAULTOPTIONALTALENT talent) {
		this.defaultoptinaltalent = talent;
	}
}
