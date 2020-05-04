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

import de.earthdawn.data.DEFENSEABILITYType;
import de.earthdawn.data.DEFENSEType;
import de.earthdawn.data.EffectlayerType;
import java.util.LinkedList;
import java.util.List;

public final class DefenseAbility {
	// We keep defense as List and do not convert it to a Map.
	// So the defense stay a link to the list within the original posstion
	// Updates/Changes here will automaticly also be there.
	private List<DEFENSEType> defenses;

	public DefenseAbility() {
		this.defenses=new LinkedList();
	}

	public DefenseAbility(List<DEFENSEType> defenses) {
		if( defenses == null ) throw new IllegalArgumentException("Undefined Defense List.");
		this.defenses=defenses;
	}

	public int get(EffectlayerType kind) {
		List<DEFENSEType> foundDefenses=new LinkedList();
		for( DEFENSEType defense : defenses ) {
			EffectlayerType k=defense.getKind();
			if( k != null && k.equals(kind)) {
				foundDefenses.add(defense);
			}
		}
		if( foundDefenses.isEmpty() ) return 0;
		DEFENSEType result=foundDefenses.get(0);
		foundDefenses.remove(0);
		// Just in case we found more than one, we sum them
		for( DEFENSEType defense : foundDefenses ) {
			result.setValue(result.getValue()+defense.getValue());
			defenses.remove(defense);
		}
		return result.getValue();
	}

	public void add(DEFENSEType defense) {
		if( defense.getValue() == 0 ) return;
		if( defense.getKind() == null ) return;
		this.defenses.add(defense);
	}

	public void add(EffectlayerType kind, int value) {
		DEFENSEType defense=new DEFENSEType();
		defense.setKind(kind);
		defense.setValue(value);
		add(defense);
	}

	public void add(DEFENSEABILITYType defense) {
		add(defense.getKind(),defense.getBonus());
	}

	public void add(List defenses) {
		for( Object defense : defenses ) {
			if( defense == null ) {
				throw new IllegalArgumentException("Undefined Object");
			} else if( defense instanceof DEFENSEType ) {
				add((DEFENSEType)defense);
			} else if( defense instanceof DEFENSEABILITYType ) {
				add((DEFENSEABILITYType)defense);
			} else {
				throw new IllegalArgumentException("Unsuported Object : "+defense.getClass().toString());
			}
		}
	}

	public void add(DefenseAbility defenses) {
		add(defenses.defenses);
	}

	public void clear(EffectlayerType kind) {
		List<DEFENSEType> foundDefenses=new LinkedList();
		for( DEFENSEType defense : defenses ) {
			EffectlayerType k=defense.getKind();
			if( k == null || k.equals(kind)) {
				foundDefenses.add(defense);
			}
		}
		defenses.removeAll(foundDefenses);
	}

	public void set(EffectlayerType kind, int value) {
		clear(kind);
		add(kind, value);
	}
}
