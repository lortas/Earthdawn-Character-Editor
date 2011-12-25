package de.earthdawn.ui2.tree;

import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.LineBorder;

import com.sun.java.swing.plaf.gtk.GTKConstants.TextDirection;

import net.miginfocom.swing.MigLayout;

public class ThreadItemNodePanel extends AbstractNodePanel<THREADITEMType> {
	private static final long serialVersionUID = -1042688290869805755L;
	private JTextField textFieldName;
	private JSpinner spinnerWeight;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JComboBox comboBoxType;
	private JTextField textFieldDescription;
	private JSpinner spinnerSpellDefense;
	private JSpinner spinnerMaxThreads;
	private JSpinner spinnerLP;
	private JTextField textFieldBookRef;
	private JSpinner spinnerDepatterningrate;
	private JSpinner spinnerBloodDamage;
	private JSpinner spinnerEdn;
	private JTextField textFieldEffect;
	private JSpinner spinnerWeaventhreadrank;

	public ThreadItemNodePanel(THREADITEMType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px,grow][24px][60px:60px:60px][30px][60px:60px:60px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Type"), "cell 0 0,alignx left,aligny center");
		comboBoxType = new JComboBox(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,growx,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("EDN"), "cell 2 0,alignx left,aligny center");
		spinnerEdn = new JSpinner(new SpinnerNumberModel(node.getEnchantingdifficultynumber(), 0, 99, 1));
		add(spinnerEdn, "cell 3 0,alignx left,aligny center");

		add(new JLabel("Weight"), "cell 2 1,alignx left,aligny center");
		spinnerWeight = new JSpinner(new SpinnerNumberModel(node.getWeight(), 0, 99, 1));
		add(spinnerWeight, "cell 3 1,alignx left,aligny center");

		add(new JLabel("LP cost growth"), "cell 4 2");
		spinnerLP = new JSpinner(new SpinnerNumberModel(node.getLpcostgrowth(), 0, 15, 1));
		add(spinnerLP, "cell 5 2,alignx left,aligny center");

		add(new JLabel("BookRef"), "cell 0 3,alignx left,aligny center");
		textFieldBookRef = new JTextField();
		textFieldBookRef.setText(node.getBookref());
		add(textFieldBookRef, "cell 1 3,growx,aligny center");
		textFieldBookRef.setColumns(10);

		add(new JLabel("Max Threads"), "cell 4 3");
		spinnerMaxThreads = new JSpinner(new SpinnerNumberModel(node.getMaxthreads(), 0, 15, 1));
		add(spinnerMaxThreads, "cell 5 3,alignx left,aligny center");

		add(new JLabel("Spell Defense"), "cell 2 2");
		spinnerSpellDefense = new JSpinner(new SpinnerNumberModel(node.getSpelldefense(), 0, 99, 1));
		add(spinnerSpellDefense, "cell 3 2,alignx left,aligny center");

		add(new JLabel("Thread Rank"), "cell 2 3");
		spinnerWeaventhreadrank = new JSpinner(new SpinnerNumberModel(node.getWeaventhreadrank(), 0, 15, 1));
		add(spinnerWeaventhreadrank, "cell 3 3,alignx left,aligny center");

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
		spinnerBloodDamage = new JSpinner(new SpinnerNumberModel(node.getBlooddamage(), 0, 99, 1));
		add(spinnerBloodDamage, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Depatterningrate"), "cell 4 1");
		spinnerDepatterningrate = new JSpinner(new SpinnerNumberModel(node.getDepatterningrate(), 0, 99, 1));
		add(spinnerDepatterningrate, "cell 5 1,alignx left,aligny center");

		add(new JLabel("Effect"), "cell 0 4");
		textFieldEffect = new JTextField();
		add(textFieldEffect, "cell 1 4 4 1,growx,aligny center");
		textFieldEffect.setColumns(12);
		textFieldEffect.setText(nodeObject.getEffect());

		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 5 4,alignx right,aligny center");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);

		add(new JLabel("Description"), "cell 0 5");
		textFieldDescription = new JTextField();
		add(textFieldDescription, "cell 1 5 5 1,growx,aligny center");
		textFieldDescription.setColumns(12);
		textFieldDescription.setText(nodeObject.getDescription());
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
		if(chckbxUsed.isSelected()) nodeObject.setUsed(YesnoType.YES);
		else nodeObject.setUsed(YesnoType.NO);
		nodeObject.setDescription(textFieldDescription.getText());
		nodeObject.setLpcostgrowth((Integer)spinnerLP.getValue());
		nodeObject.setMaxthreads((Integer)spinnerMaxThreads.getValue());
		nodeObject.setSpelldefense((Integer)spinnerSpellDefense.getValue());
		nodeObject.setWeaventhreadrank((Integer)spinnerWeaventhreadrank.getValue());
	}
}
