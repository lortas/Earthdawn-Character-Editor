package de.earthdawn.ui;

import java.math.BigInteger;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.EDCHARAKTER;

/**
 * Hilfsklasse zur Anzeite eines {@link ATTRIBUTType} in der Oberfläche.
 */
public class UIAttribut {
	
	private String name;
	
	private JSpinner spinWert;

	private JSpinner spinSteig;

	// TODO: Diese Label brauchen einen sprechenderen Namen
	private JLabel lbExt1;
	private JLabel lbExt2;
	
	public UIAttribut(String name) {
		this.name = name;

		this.spinWert = new JSpinner();
		this.spinSteig = new JSpinner(); 
		this.lbExt1 = new JLabel("0");
		this.lbExt2 = new JLabel("6 w11");

		// Wertebereiche für die Spinner festlegen
		SpinnerNumberModel model = new SpinnerNumberModel(30, 0, 30, 1);
		spinWert.setModel(model);
		
		model = new SpinnerNumberModel(1, 0, 5, 1);
		spinSteig.setModel(model);
	}
	
	public String getName() {
		return name;
	}
	
	public JSpinner getSpinSteig() {
		return spinSteig;
	}
	
	public JSpinner getSpinWert() {
		return spinWert;
	}
	
	public JLabel getLbExt1() {
		return lbExt1;
	}
	
	public JLabel getLbExt2() {
		return lbExt2;
	}

	public void addToCharakter(EDCHARAKTER charakter) {
		for (Object o : charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (o instanceof ATTRIBUTEType) {
				ATTRIBUTEType attribut = (ATTRIBUTEType) o;
				if (attribut.getName().value().equals(getName())) {
					attribut.setCurrentvalue(new BigInteger(getSpinWert().getValue().toString()));
//					attribut.setSteig(new BigInteger(getSpinWert().getValue().toString()));
				}
			}
		}
	}
}
