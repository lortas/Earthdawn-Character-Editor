package de.earthdawn;

import java.util.Random;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECharacteristics;
import de.earthdawn.data.ROLLEDDICEType;
import de.earthdawn.data.STEPDICEType;

public class DiceCup {
	private static final ECECharacteristics CHARACTERISTICS = ApplicationProperties.create().getCharacteristics();
	private static Random rand = new Random();
	private STEPDICEType stepDice = null;

	public DiceCup( int step ) {
		setStep(step);
	}

	public void setStep(int step) {
		String dice = CHARACTERISTICS.getDice(step);
		stepDice = new STEPDICEType();
		stepDice.setStep(step);
		stepDice.setDice(dice);
	}

	public ROLLEDDICEType toss() {
		return toss(stepDice.getDice());
	}

	public static ROLLEDDICEType toss(int step) {
		String dice = CHARACTERISTICS.getDice(step);
		return toss(dice);
	}

	public static ROLLEDDICEType toss(String diceset) {
		int sum=0;
		String path="";
		for( String dice : diceset.split("\\+") ) {
			String[] d = dice.split("d");
			if( d.length != 2 ) {
				System.err.println("illigal dice '"+dice+"'");
				continue;
			}
			int count=1;
			if( ! d[0].isEmpty() ) count=Integer.valueOf(d[0]); 
			for( int i=0; i<count; i++ ) {
				int r=tossSingleDice(Integer.valueOf(d[1]));
				sum+=r;
				if( ! path.isEmpty() ) path+=" + ";
				path+=r+"(d"+d[1]+")";
			}
		}
		ROLLEDDICEType result = new ROLLEDDICEType();
		result.setResult(sum);
		result.setDice(diceset);
		result.setRolling(path);
		return result;
	}

	public static int tossSingleDice(int side) {
		int sum=0;
		while( true ) {
			int i = rand.nextInt(side)+1;
			sum +=i;
			if( i < side) break;
		}
		return sum;
	}

	public STEPDICEType getStepDice() {
		return stepDice;
	}
}
