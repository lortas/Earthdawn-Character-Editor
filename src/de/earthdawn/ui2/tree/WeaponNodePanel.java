package de.earthdawn.ui2.tree;

import de.earthdawn.data.WEAPONType;
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
import net.miginfocom.swing.MigLayout;

public class WeaponNodePanel extends AbstractNodePanel<WEAPONType> {
	private static final long serialVersionUID = -8431309392002111880L;
	private JTextField textFieldName;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JComboBox<ItemkindType> comboBoxType;
	private JSpinner spinnerWeight;
	private JSpinner spinnerSize;
	private JSpinner spinnerDamage;
	private JSpinner spinnerStrMin;
	private JSpinner spinnerDexMin;
	private JSpinner spinnerRangeShort;
	private JSpinner spinnerRangeLong;
	private JSpinner spinnerForged;
	private JTextField textFieldDateForged;
	private JSpinner spinnerBloodDamage;
	private JTextField textFieldBookRef;
	private JSpinner spinnerDepatterningrate;

	public WeaponNodePanel(WEAPONType node) {
		super(node);
		this.setPreferredSize(new Dimension(768, 113));
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px,grow][27px][60px][34px][68.00px][40px][86px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));
		add(new JLabel("Type"), "cell 0 0,alignx right,aligny center");
		comboBoxType = new JComboBox<ItemkindType>(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,growx,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("Name"), "cell 0 1,alignx right,aligny center");
		textFieldName = new JTextField();
		add(textFieldName, "cell 1 1,growx,aligny center");
		textFieldName.setColumns(12);
		textFieldName.setText(nodeObject.getName());

		add(new JLabel("Location"), "cell 0 2,alignx right,aligny center");
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 1 2,growx,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());

		add(new JLabel("Blood Dmg"), "cell 4 2,alignx right,aligny center");
		spinnerBloodDamage = new JSpinner(new SpinnerNumberModel(node.getBlooddamage(), 0, 100, 1));
		add(spinnerBloodDamage, "cell 5 2,alignx left,aligny center");

		add(new JLabel("BookRef"), "cell 0 3,alignx right,aligny center");
		textFieldBookRef = new JTextField();
		textFieldBookRef.setText(node.getBookref());
		add(textFieldBookRef, "cell 1 3,growx,aligny center");
		textFieldBookRef.setColumns(10);

		add(new JLabel("Depat.rate"), "cell 4 3,alignx right,aligny center");
		spinnerDepatterningrate = new JSpinner(new SpinnerNumberModel(node.getDepatterningrate(), 0, 100, 1));
		add(spinnerDepatterningrate, "cell 5 3,alignx left,aligny center");

		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 7 3,alignx right,aligny center");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);

		add(new JLabel("STR min"), "cell 2 0,alignx right,aligny center");
		spinnerStrMin = new JSpinner(new SpinnerNumberModel(node.getStrengthmin(), 0, 100, 1));
		add(spinnerStrMin, "cell 3 0,alignx left,aligny center");

		add(new JLabel("DEX min"), "cell 2 1,alignx right,aligny center");
		spinnerDexMin = new JSpinner(new SpinnerNumberModel(node.getDexteritymin(), 0, 100, 1));
		add(spinnerDexMin, "cell 3 1,alignx left,aligny center");

		add(new JLabel("Short (yard)"), "cell 2 2,alignx right,aligny center");
		spinnerRangeShort = new JSpinner(new SpinnerNumberModel(node.getShortrange(), 0, 100, 1));
		add(spinnerRangeShort, "cell 3 2,alignx left,aligny center");

		add(new JLabel("Long (yard)"), "cell 2 3,alignx right,aligny center");
		spinnerRangeLong = new JSpinner(new SpinnerNumberModel(node.getLongrange(), 0, 100, 1));
		add(spinnerRangeLong, "cell 3 3,alignx left,aligny center");

		add(new JLabel("Size"), "cell 4 0,alignx right,aligny center");
		spinnerSize = new JSpinner(new SpinnerNumberModel(node.getSize(), 0, 100, 1));
		add(spinnerSize, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Weight (lb)"), "cell 4 1,alignx right,aligny center");
		spinnerWeight = new JSpinner(new SpinnerNumberModel(node.getWeight(), 0, 100, 1));
		add(spinnerWeight, "cell 5 1,alignx left,aligny center");

		add(new JLabel("Damagestep"), "cell 6 0,alignx right,aligny center");
		spinnerDamage = new JSpinner(new SpinnerNumberModel(node.getDamagestep(), 0, 100, 1));
		add(spinnerDamage, "cell 7 0,alignx left,aligny center");

		add(new JLabel("Forged"), "cell 6 1,alignx right,aligny center");
		spinnerForged = new JSpinner(new SpinnerNumberModel(node.getTimesforged(), 0, 100, 1));
		add(spinnerForged, "cell 7 1,alignx left,aligny center");

		add(new JLabel("Date forged"), "cell 6 2,alignx left,aligny center");
		textFieldDateForged = new JTextField();
		textFieldDateForged.setText(node.getDateforged());
		add(textFieldDateForged, "cell 7 2,growx,aligny center");
		textFieldDateForged.setColumns(10);
	}

	@Override
	public void updateObject() {
		nodeObject.setBlooddamage((Integer)spinnerBloodDamage.getValue());
		nodeObject.setBookref(textFieldBookRef.getText());
		nodeObject.setDamagestep((Integer) spinnerDamage.getValue());
		nodeObject.setDateforged(textFieldDateForged.getText());
		nodeObject.setDepatterningrate((Integer)spinnerDepatterningrate.getValue());
		nodeObject.setDexteritymin((Integer) spinnerDexMin.getValue());
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setLocation(textFieldLocation.getText());
		nodeObject.setLongrange((Integer) spinnerRangeLong.getValue());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setShortrange((Integer) spinnerRangeShort.getValue());
		nodeObject.setSize((Integer) spinnerSize.getValue());
		nodeObject.setStrengthmin((Integer) spinnerStrMin.getValue());
		nodeObject.setTimesforged((Integer) spinnerForged.getValue());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());

		if(chckbxUsed.isSelected()) nodeObject.setUsed(YesnoType.YES);
		else nodeObject.setUsed(YesnoType.NO);
	}
}
