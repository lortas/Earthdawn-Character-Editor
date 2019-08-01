package de.earthdawn.ui2.tree;

import de.earthdawn.data.PATTERNITEMType;
import de.earthdawn.data.PatternkindType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class PatternitemNodePanel extends AbstractNodePanel<PATTERNITEMType> {
	private static final long serialVersionUID = 1823040866728888240L;
	private JSpinner spinnerWeight;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JComboBox<ItemkindType> comboBoxType;
	private JComboBox<PatternkindType> comboBoxPatternKind;
	private JTextField textFieldBookRef;
	private JSpinner spinnerDepatterningrate;
	private JSpinner spinnerBloodDamage;
	private JSpinner spinnerEdn;
	private JSpinner spinnerSize;
	private JSpinner spinnerSpelldefense;
	private JSpinner spinnerWaventhreadrank;
	private JTextField textFieldName;
	private JTextField textFieldDescription;
	private JTextField textFieldEffect;
	private JTextField textFieldKeyknowledge;
	private JTextField textFieldTruepattern;

	public PatternitemNodePanel(PATTERNITEMType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px,grow][24px][128px,grow 20][27px][60px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Type"), "cell 0 0,alignx left,aligny center");
		comboBoxType = new JComboBox<ItemkindType>(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,growx,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("Pattern Type"), "cell 2 0,alignx left,aligny center");
		comboBoxPatternKind = new JComboBox<PatternkindType>(PatternkindType.values());
		comboBoxPatternKind.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxPatternKind, "cell 3 0,growx,aligny center");
		comboBoxPatternKind.setSelectedItem(nodeObject.getPatternkind());

		add(new JLabel("Weight"), "cell 2 1,alignx left,aligny center");
		spinnerWeight = new JSpinner(new SpinnerNumberModel(node.getWeight(), 0, 100, 1));
		add(spinnerWeight, "cell 3 1,alignx left,aligny center");

		add(new JLabel("EDN"), "cell 2 2,alignx left,aligny center");
		spinnerEdn = new JSpinner(new SpinnerNumberModel(node.getEnchantingdifficultynumber(), 0, 100, 1));
		add(spinnerEdn, "cell 3 2,alignx left,aligny center");

		add(new JLabel("Size"), "cell 2 3,alignx left,aligny center");
		spinnerSize = new JSpinner(new SpinnerNumberModel(node.getSize(), 0, 10, 1));
		add(spinnerSize, "cell 3 3,alignx left,aligny center");

		add(new JLabel("Spell Defense"), "cell 4 2,alignx left,aligny center");
		spinnerSpelldefense = new JSpinner(new SpinnerNumberModel(node.getSpelldefense(), 0, 100, 1));
		add(spinnerSpelldefense, "cell 5 2,alignx left,aligny center");

		add(new JLabel("Thread Rank"), "cell 4 3,alignx left,aligny center");
		spinnerWaventhreadrank = new JSpinner(new SpinnerNumberModel(node.getWeaventhreadrank(), 0, 15, 1));
		add(spinnerWaventhreadrank, "cell 5 3,alignx left,aligny center");

		add(new JLabel("Name"), "cell 0 2,alignx left,aligny center");
		textFieldName = new JTextField();
		add(textFieldName, "cell 1 2,growx,aligny center");
		textFieldName.setColumns(12);
		textFieldName.setText(nodeObject.getName());

		add(new JLabel("Location"), "cell 0 1,alignx left,aligny center");
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 1 1,growx,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());

		add(new JLabel("Blood Damage"), "cell 4 0,alignx left,aligny center");
		spinnerBloodDamage = new JSpinner(new SpinnerNumberModel(node.getBlooddamage(), 0, 100, 1));
		add(spinnerBloodDamage, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Depatterningrate"), "cell 4 1");
		spinnerDepatterningrate = new JSpinner(new SpinnerNumberModel(node.getDepatterningrate(), 0, 100, 1));
		add(spinnerDepatterningrate, "cell 5 1,alignx left,aligny center");

		add(new JLabel("BookRef"), "cell 0 3,alignx left,aligny center");
		textFieldBookRef = new JTextField();
		textFieldBookRef.setText(node.getBookref());
		add(textFieldBookRef, "cell 1 3,growx,aligny center");
		textFieldBookRef.setColumns(10);

		add(new JLabel("Description"), "cell 0 4");
		textFieldDescription = new JTextField();
		add(textFieldDescription, "cell 1 4 5 1,growx,aligny center");
		textFieldDescription.setColumns(12);
		textFieldDescription.setText(nodeObject.getDESCRIPTION());

		add(new JLabel("True Pattern"), "cell 0 5");
		textFieldTruepattern = new JTextField();
		add(textFieldTruepattern, "cell 1 5 5 1,growx,aligny center");
		textFieldTruepattern.setColumns(12);
		textFieldTruepattern.setText(nodeObject.getTruepattern());

		add(new JLabel("Key Knowledge"), "cell 0 6");
		textFieldKeyknowledge = new JTextField();
		add(textFieldKeyknowledge, "cell 1 6 5 1,growx,aligny center");
		textFieldKeyknowledge.setColumns(12);
		textFieldKeyknowledge.setText(nodeObject.getKeyknowledge());

		add(new JLabel("Effect"), "cell 0 7");
		textFieldEffect = new JTextField();
		add(textFieldEffect, "cell 1 7 4 1,growx,aligny center");
		textFieldEffect.setColumns(12);
		textFieldEffect.setText(nodeObject.getEffect());

		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 5 6,alignx right,aligny center");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());
		nodeObject.setLocation(textFieldLocation.getText());
		nodeObject.setBookref(textFieldBookRef.getText());
		nodeObject.setBlooddamage((Integer) spinnerBloodDamage.getValue());
		nodeObject.setDepatterningrate((Integer) spinnerDepatterningrate.getValue());
		nodeObject.setEnchantingdifficultynumber((Integer) spinnerEdn.getValue());
		nodeObject.setEffect(textFieldEffect.getText());
		nodeObject.setKeyknowledge(textFieldKeyknowledge.getText());
		nodeObject.setPatternkind((PatternkindType)comboBoxPatternKind.getSelectedItem());
		nodeObject.setSpelldefense((Integer) spinnerSpelldefense.getValue());
		nodeObject.setTruepattern(textFieldTruepattern.getText());
		nodeObject.setWeaventhreadrank((Integer) spinnerWaventhreadrank.getValue());
		nodeObject.setSize((Integer) spinnerSize.getValue());
		nodeObject.setDESCRIPTION(textFieldDescription.getText());
		if(chckbxUsed.isSelected()) nodeObject.setUsed(YesnoType.YES);
		else nodeObject.setUsed(YesnoType.NO);
	}
}
