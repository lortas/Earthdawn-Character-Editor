package de.earthdawn.ui2;

public enum EDInventoryRootNodeType {
	ITEMS("Items"),COMMONMAGICITEMS("Common Magic Items"),BLOODCHARMS("Bloodcharms"),WEAPONS("Weapons"),ARMOR("Armor"),SHIELD("Shield"),THREADITEMS("Thread Items"),PATTERNITEMS("Pattern Items"),PURSE("Purse");
	private final String value;

	EDInventoryRootNodeType(String v) { value = v; }

	public String value() { return value; }

	public static EDInventoryRootNodeType fromValue(String v) {
		for (EDInventoryRootNodeType c: EDInventoryRootNodeType.values()) {
			if( c.value.equals(v) ) return c;
		}
		throw new IllegalArgumentException(v);
	}
}
