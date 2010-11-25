package de.earthdawn.ui;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.math.BigInteger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.earthdawn.data.EDCHARAKTER;

/**
 * Panel zur Anzeige der allgemeinen Eigenschaften eines Charakters.
 * 
 * @author mh
 */
@SuppressWarnings("serial")
public class AllgemeineEigenschaftenPanel extends JPanel {

	/** Textfeld zur Anzeige/Eingabe des Charakternamens. */
	private JTextField tfCharaktername;

	/** ComboBox zur Auswahl der Rasse. */
	private JComboBox comboRasse;

	/** ComboBox zur Auswahl der ersten Disziplin. */
	private JComboBox comboErsteDisziplinBezeichnung;
	
	private JSpinner spinErsteDisziplinNr;
	
	/** ComboBox zur Auswahl der zweiten Disziplin. */
	private JComboBox comboZweiteDisziplinBezeichnung;
	
	private JSpinner spinZweiteDisziplinNr;

	private JTextField tfAlter;

	private JTextField tfGroesse;
	
	public AllgemeineEigenschaftenPanel() {
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraintsBuilder gbcb = new GridBagConstraintsBuilder();
		
		// --- Name
		this.tfCharaktername = new JTextField();
		add(new JLabel("Charaktername:"), gbcb.nextCell(1, 0));
		add(this.tfCharaktername, gbcb.nextCell(3, 10));
		gbcb.nextLine();
		
		// --- Rasse
		this.comboRasse = new JComboBox();		
		// TODO: Die Auswahlmöglichkeiten aus Konfigurationsdatei übernehmen ...
		this.comboRasse.addItem("Elf");
		this.comboRasse.addItem("Mensch");
		this.comboRasse.addItem("Obsidianer");		
		add(new JLabel("Rasse:"), gbcb.nextCell(1, 0));
		add(this.comboRasse, gbcb.nextCell(3, 10));
		gbcb.nextLine();
		
		// --- 1. Disziplin
		this.comboErsteDisziplinBezeichnung = new JComboBox();
/* TODO: Read values from config-file
		for (DisziplinlistType disziplin : DisziplinlistType.values()) {
			this.comboErsteDisziplinBezeichnung.addItem(disziplin.value());
		}
*/
		this.spinErsteDisziplinNr = new JSpinner();
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 15, 1);
		this.spinErsteDisziplinNr.setModel(model);
		add(new JLabel("1. Disziplin:"), gbcb.nextCell(1, 0));
		add(this.comboErsteDisziplinBezeichnung, gbcb.nextCell(1, 10));
		JPanel dummy = new JPanel(new FlowLayout());
		dummy.add(new JLabel("("));
		dummy.add(this.spinErsteDisziplinNr);
		dummy.add(new JLabel(")"));
		add(dummy, gbcb.nextCell(1, 0));
		
		gbcb.nextLine();
		
		// --- 2. Disziplin
		this.comboZweiteDisziplinBezeichnung = new JComboBox();
/* TODO: Read values from config-file
		for (DisziplinlistType disziplin : DisziplinlistType.values()) {
			this.comboZweiteDisziplinBezeichnung.addItem(disziplin.value());
		}
*/
		this.spinZweiteDisziplinNr = new JSpinner();
		model = new SpinnerNumberModel(1, 1, 15, 1);
		this.spinZweiteDisziplinNr.setModel(model);
		add(new JLabel("2. Disziplin:"), gbcb.nextCell(1, 0));
		add(this.comboZweiteDisziplinBezeichnung, gbcb.nextCell(1, 10));
		dummy = new JPanel(new FlowLayout());
		dummy.add(new JLabel("("));
		dummy.add(this.spinZweiteDisziplinNr);
		dummy.add(new JLabel(")"));
		add(dummy, gbcb.nextCell(1, 0));
		
		gbcb.nextLine();
	
		// --- Alter
		this.tfAlter = new JTextField();
		add(new JLabel("Alter:"), gbcb.nextCell(1, 0));
		add(this.tfAlter, gbcb.nextCell(2, 10));
		add(new JLabel("J"), gbcb.nextCell(1, 0));
		
		gbcb.nextLine();
		
		// --- Alter
		this.tfGroesse = new JTextField();
		add(new JLabel("Größe:"), gbcb.nextCell(1, 0));
		add(this.tfGroesse, gbcb.nextCell(2, 10));
		add(new JLabel("cm"), gbcb.nextCell(1, 0));
		
	}

	public void charakterAnzeigen(EDCHARAKTER charakter) {
//		this.tfCharaktername.setText(charakter.get());
//		this.comboRasse.setSelectedItem(charakter.getRasse());
//	
//		// TODO: Disziplinen
//		
//		this.tfAlter.setText(charakter.getAlter().toString(10));
//		this.tfGroesse.setText(charakter.getGroesse().toString(10));
	}

	public void addToCharakter(EDCHARAKTER charakter) {
//		charakter.setBezeichnung(this.tfCharaktername.getText());
//		charakter.setRasse((String) this.comboRasse.getSelectedItem());
		
		// TODO: Disziplinen
		
//		charakter.setAlter(new BigInteger(this.tfAlter.getText(), 10));
//		charakter.setGroesse(new BigInteger(this.tfGroesse.getText(), 10));
	}
	
	public JComboBox getComboRasse() {
		return comboRasse;
	}
}
