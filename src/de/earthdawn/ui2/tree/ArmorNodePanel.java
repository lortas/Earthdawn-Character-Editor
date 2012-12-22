package de.earthdawn.ui2.tree;

import de.earthdawn.data.ARMORType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;
import de.earthdawn.data.ElementkindType;
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

public class ArmorNodePanel extends AbstractNodePanel<ARMORType> {
	private static final long serialVersionUID = -8722091567075441494L;
	private JTextField textFieldName;
	private JSpinner spinnerWeight;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JComboBox<ItemkindType> comboBoxType;
	private JSpinner spinnerPhysical;
	private JSpinner spinnerMystic;
	private JSpinner spinnerPenalty;
	private JSpinner spinnerEnchanting;
	private JSpinner spinnerForgedPysical;
	private JSpinner spinnerForgedMystic;
	private JTextField textFieldDateForged;
	private JSpinner spinnerBloodDamage;
	private JTextField textFieldBookRef;
	private JComboBox<ElementkindType> comboBoxEdnElement;
	private JSpinner spinnerDepatterningrate;

	public ArmorNodePanel(ARMORType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px,grow][27px][60px][34px][47px,grow][40px][86px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Type"), "cell 0 0,alignx left,aligny center");
		comboBoxType = new JComboBox<ItemkindType>(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,growx,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("Enchanting Diff"), "cell 4 0,alignx left,aligny center");
		spinnerEnchanting = new JSpinner(new SpinnerNumberModel(node.getEdn(), 0, 100, 1));
		add(spinnerEnchanting, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Name"), "cell 0 1,alignx left,aligny center");
		textFieldName = new JTextField();
		add(textFieldName, "cell 1 1,growx,aligny center");
		textFieldName.setColumns(12);
		textFieldName.setText(nodeObject.getName());

		add(new JLabel("Forged mystic"), "cell 6 1,alignx left,aligny center");
		spinnerForgedMystic = new JSpinner(new SpinnerNumberModel(node.getTimesforgedMystic(), 0, 100, 1));
		add(spinnerForgedMystic, "cell 7 1,alignx left,aligny center");

		add(new JLabel("Location"), "cell 0 2,alignx left,aligny center");
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 1 2,growx,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());

		add(new JLabel("Physical armor"), "cell 2 0,alignx left,aligny center");
		spinnerPhysical = new JSpinner(new SpinnerNumberModel(node.getPhysicalarmor(), 0, 100, 1));
		add(spinnerPhysical, "cell 3 0");

		add(new JLabel("Mystic armor"), "cell 2 1,alignx left,aligny center");
		spinnerMystic = new JSpinner(new SpinnerNumberModel(node.getMysticarmor(), 0, 100, 1));
		add(spinnerMystic, "cell 3 1");

		add(new JLabel("Forged physical"), "cell 6 0,alignx left,aligny center");
		spinnerForgedPysical = new JSpinner(new SpinnerNumberModel(node.getTimesforgedPhysical(), 0, 100, 1));
		add(spinnerForgedPysical, "cell 7 0,alignx left,aligny center");

		add(new JLabel("Penalty"), "cell 2 2,alignx left,aligny center");
		spinnerPenalty = new JSpinner(new SpinnerNumberModel(node.getPenalty(), 0, 100, 1));
		add(spinnerPenalty, "cell 3 2");

		add(new JLabel("Blood Damage"), "cell 4 2,alignx left,aligny center");
		spinnerBloodDamage = new JSpinner(new SpinnerNumberModel(node.getBlooddamage(), 0, 100, 1));
		add(spinnerBloodDamage, "cell 5 2,alignx left,aligny center");

		add(new JLabel("Date forged"), "cell 6 2,alignx left,aligny center");
		textFieldDateForged = new JTextField();
		textFieldDateForged.setText(node.getDateforged());
		add(textFieldDateForged, "cell 7 2,growx,aligny center");
		textFieldDateForged.setColumns(10);

		add(new JLabel("BookRef"), "cell 0 3,alignx trailing,aligny center");
		textFieldBookRef = new JTextField();
		textFieldBookRef.setText(node.getBookref());
		add(textFieldBookRef, "cell 1 3,growx,aligny center");
		textFieldBookRef.setColumns(10);

		add(new JLabel("Weight"), "cell 2 3,alignx left,aligny center");
		spinnerWeight = new JSpinner(new SpinnerNumberModel(node.getWeight(), 0, 100, 1));
		add(spinnerWeight, "cell 3 3,alignx left,aligny center");

		add(new JLabel("Edn Element"), "cell 4 1,alignx left,aligny center");
		comboBoxEdnElement = new JComboBox<ElementkindType>(ElementkindType.values());
		comboBoxEdnElement.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxEdnElement, "cell 5 1,growx,aligny center");
		comboBoxEdnElement.setSelectedItem(nodeObject.getEdnElement());

		add(new JLabel("Depatterningrate"), "cell 4 3");
		spinnerDepatterningrate = new JSpinner(new SpinnerNumberModel(node.getDepatterningrate(), 0, 100, 1));
		add(spinnerDepatterningrate, "cell 5 3,alignx left,aligny center");

		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 7 3,alignx right,aligny center");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());
		nodeObject.setLocation(textFieldLocation.getText());
		nodeObject.setPhysicalarmor((Integer) spinnerPhysical.getValue());
		nodeObject.setMysticarmor((Integer) spinnerMystic.getValue());
		nodeObject.setPenalty((Integer) spinnerPenalty.getValue());
		nodeObject.setEdn((Integer) spinnerEnchanting.getValue());
		nodeObject.setEdnElement((ElementkindType)comboBoxEdnElement.getSelectedItem());
		nodeObject.setTimesforgedPhysical((Integer) spinnerForgedPysical.getValue());
		nodeObject.setTimesforgedMystic((Integer) spinnerForgedMystic.getValue());
		nodeObject.setDateforged(textFieldDateForged.getText());
		nodeObject.setBlooddamage((Integer)spinnerBloodDamage.getValue());
		nodeObject.setDepatterningrate((Integer)spinnerDepatterningrate.getValue());
		nodeObject.setBookref(textFieldBookRef.getText());
		if(chckbxUsed.isSelected()) nodeObject.setUsed(YesnoType.YES);
		else nodeObject.setUsed(YesnoType.NO);
	}
}
