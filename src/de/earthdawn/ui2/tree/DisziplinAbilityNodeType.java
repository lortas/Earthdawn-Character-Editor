package de.earthdawn.ui2.tree;

public enum DisziplinAbilityNodeType {
	RECOVERYTEST("Recovery Test"),KARMASTEP("Karma Step"),MAXKARMA("Max Karma"),SPELLABILITY("Spell Ability"),INITIATIVE("Initiative");
	private final String value;

	DisziplinAbilityNodeType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static DisziplinAbilityNodeType fromValue(String v) {
		for (DisziplinAbilityNodeType c: DisziplinAbilityNodeType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
