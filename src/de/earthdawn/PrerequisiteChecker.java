/*
 * Copyright (C) 2020 holger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.earthdawn;

import de.earthdawn.data.PrerequisitekindType;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author holger
 */
public class PrerequisiteChecker {

	Map<PrerequisitekindType,Map<String,List<Boolean>>> data;
	
	public PrerequisiteChecker() {
		this.data = new EnumMap(PrerequisitekindType.class);
		for( PrerequisitekindType kind : PrerequisitekindType.values() ) {
			this.data.put(kind, new TreeMap());
		}
	}

	public List<Boolean> getMatches(PrerequisitekindType kind,String pool) {
		List<Boolean> matches = data.get(kind).get(pool);
		if( matches == null ) {
			matches = new LinkedList();
			data.get(kind).put(pool, matches);
		}
		return matches;
	}

	public void add(PrerequisitekindType kind,String pool,boolean match) {
		getMatches(kind,pool).add(match);
	}

	public boolean doMatchByPool(String pool) {
		// Check first the required matches (all must match)
		if (!getMatches(PrerequisitekindType.REQUIREMENT,pool).stream().noneMatch((match) -> ( ! match ))) return false;
		List<Boolean> matches = getMatches(PrerequisitekindType.RESTRICTION,pool);
		// Check if we have a restricted match list (one must match)
		// If not we are done
		if( matches.isEmpty() ) return true;
		// Otherwise we search for one match;
		return matches.stream().anyMatch((match) -> ( match ));
	}

	public Set<String> getAllPools() {
		Set<String> results=new TreeSet();
		for( PrerequisitekindType kind : PrerequisitekindType.values() ) {
			data.get(kind).keySet().forEach( (k) -> { results.add(k); } );
		}
		return results;
	}

	public boolean testMatch() {
		return getAllPools().stream().noneMatch((pool) -> ( ! doMatchByPool(pool) ));
	}
}
