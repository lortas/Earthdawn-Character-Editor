package de.earthdawn;

import java.util.ArrayList;
import java.util.List;

import de.earthdawn.data.ROLLEDDICEType;
import de.earthdawn.data.STEPDICEType;

public class DiceCups {
	private List<DiceCup> diceCups= new ArrayList<DiceCup>();

	public DiceCups( int[] steps ) {
		setStep(steps);
	}

	public void setStep(int[] steps) {
		diceCups.clear();
		for( int s : steps ) {
			diceCups.add(new DiceCup(s));
		}
	}

	public ROLLEDDICEType toss() {
		int sum=0;
		String path=null;
		for( DiceCup cup : diceCups ) {
			ROLLEDDICEType r = cup.toss();
			sum += r.getResult();
			if( path == null ) {
				path = r.getRolling();
			} else {
				path += " + "+r.getRolling();
			}
		}
		ROLLEDDICEType result = new ROLLEDDICEType();
		result.setResult(sum);
		result.setRolling(path);
		return result;
	}

	public List<STEPDICEType> getStepDices() {
		List<STEPDICEType> result = new ArrayList<STEPDICEType>();
		for( DiceCup cup : diceCups ) result.add(cup.getStepDice());
		return result;
	}
}
