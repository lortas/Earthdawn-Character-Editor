package de.earthdawn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import de.earthdawn.data.COINSType;

public class Purse {
	static public enum Coinstype {
		COPPER("Copper","c"),
		SILVER("Silver","s"),
		GOLD("Gold","g"),
		EARTH("Earth","e"),
		WATER("Water","w"),
		FIRE("Fire","f"),
		AIR("Air","a"),
		ORICALCUM("Orichalcum","o"),
		GEM50("Gem50","g50"),
		GEM100("Gem100","g100"),
		GEM200("Gem200","g200"),
		GEM500("Gem500","g500"),
		GEM1000("Gem1000","g1000");
		private final String value;
		private final String shortcut;
		Coinstype(String v, String s) { value = v; shortcut=s;}
		public String value() { return value; }
		public String shortcut() { return shortcut; }
		public static Coinstype fromValue(String v) {
			for( Coinstype c: Coinstype.values() ) if( c.value.equals(v) ) return c;
			throw new IllegalArgumentException(v);
		}
	}

	private COINSType coins;
	private Method[] methods;
	public Purse() { init(new COINSType()); }
	public Purse(COINSType coins) { init(coins); }

	private void init(COINSType coins) {
		this.coins=(coins==null)?new COINSType():coins;
		this.methods = this.coins.getClass().getMethods();
	}

	public void setCoin(Coinstype type, int value) {
		String setter = "set"+type.value();
		for( Method m : methods ) {
			if( m.getName().equals(setter) ) try {
				m.invoke(coins, value);
				return;
			} catch(IllegalAccessException|InvocationTargetException e) {
				System.err.println(e.getLocalizedMessage());
				throw new IllegalArgumentException(setter);
			}
		}
		throw new IllegalArgumentException(setter);
	}

	public int getCoin(Coinstype type) {
		String getter = "get"+type.value();
		for( Method m : methods ) {
			if( m.getName().equals(getter) ) try {
				return (Integer)m.invoke(coins);
			} catch(IllegalAccessException|InvocationTargetException e) {
				System.err.println(e.getLocalizedMessage());
				throw new IllegalArgumentException(getter);
			}
		}
		throw new IllegalArgumentException(getter);
	}

	public void addPurse(Purse other) {
		if( other == null ) return;
		for( Coinstype c: Coinstype.values() ) {
			int value = this.getCoin(c);
			value += other.getCoin(c);
			this.setCoin(c, value);
		}
	}

	public String content() {
		StringBuilder result = new StringBuilder();
		boolean isfirst=true;
		for( Coinstype c: Coinstype.values() ) {
			int value = getCoin(c);
			if( value != 0 ) {
				if( isfirst ) isfirst=false;
				else result.append(" ");
				result.append(c.shortcut());
				result.append(":");
				result.append(value);
			}
		}
		return result.toString();
	}

	public String toString() {
		return coins.getName()+" ("+content()+")";
	}
}
