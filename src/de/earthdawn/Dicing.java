package de.earthdawn;

import java.util.Random;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.DiceType;
import de.earthdawn.data.ROLLEDDICEType;
import de.earthdawn.data.STEPDICEType;

public class Dicing {
	private static Random rand = new Random();
	private STEPDICEType stepDice = null;
	public Dicing( int step ) {
		setStep(step);
	}
	public void setStep(int step) {
		DiceType dice = ApplicationProperties.create().getCharacteristics().getSTEPDICEbyStep(step).getDice();
		stepDice = new STEPDICEType();
		stepDice.setStep(step);
		stepDice.setDice(dice);
	}

	public ROLLEDDICEType roleDiceSet() {
		return roleDiceSet(stepDice.getDice());
	}

	public static ROLLEDDICEType roleDiceSet(int step) {
		DiceType dice = ApplicationProperties.create().getCharacteristics().getSTEPDICEbyStep(step).getDice();
		return roleDiceSet(dice);
	}

	public static ROLLEDDICEType roleDiceSet(DiceType diceset) {
		int sum=0;
		String path="";
		for( String dice : diceset.value().split("\\+") ) {
			String[] d = dice.split("d");
			if( d.length != 2 ) {
				System.err.println("illigal dice '"+dice+"'");
				continue;
			}
			int count=1;
			if( ! d[0].isEmpty() ) count=Integer.valueOf(d[0]); 
			for( int i=0; i<count; i++ ) {
				int r=singleDice(Integer.valueOf(d[1]));
				sum+=r;
				if( ! path.isEmpty() ) path+=" + ";
				path+=r+"(d"+d[1]+")";
			}
		}
		ROLLEDDICEType result = new ROLLEDDICEType();
		result.setDice(diceset);
		result.setResult(sum);
		result.setRolling(path);
		return result;
	}

	public static int singleDice(int side) {
		int sum=0;
		while( true ) {
			int i = rand.nextInt(side)+1;
			sum +=i;
			if( i < side) break;
		}
		return sum;
	}
}
