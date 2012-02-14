package de.earthdawn.ui2.tree;

public enum StringNodeType {
	SPELL("Spell"),ABILITY("Ability");
	private final String value;

	StringNodeType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static StringNodeType fromValue(String v) {
		for (StringNodeType c: StringNodeType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
