package de.earthdawn.ui2.tree;

import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;
import de.earthdawn.ui2.SpinnerRenderer;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;


public class WeaponNodePanel extends AbstractNodePanel<WEAPONType> {
	private static final long serialVersionUID = 1L;
	private JLabel lblName;
	private JTextField textFieldName;
	private JLabel lblWeight;
	private JSpinner spinnerWeight;
	private JLabel lblLocation;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JLabel lblType;
	private JComboBox comboBoxType;
	private JLabel lblDamagestep;
	private JSpinner spinnerDamage;
	private JLabel lblStrengthmin;
	private JSpinner spinnerStrMin;
	private JLabel lblShortrange;
	private JLabel lblLongrange;
	private JSpinner spinnerRangeShort;
	private JSpinner spinnerRangeLong;
	private JLabel labelForged;
	private JSpinner spinnerForged;
	private JTextField textFieldDateForged;
	private JLabel lblTimesForged;

	public WeaponNodePanel(WEAPONType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));

		this.setPreferredSize(new Dimension(710, 93));
		setLayout(new MigLayout("", "[24px,grow][90.00px][27px][86px,grow][34px][47px][40px][86px][49px]", "[23px][][grow]"));
		lblType = new JLabel("Type");
		add(lblType, "cell 0 0,alignx left,aligny center");
		
		comboBoxType = new JComboBox(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,alignx left,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());
		
		lblName = new JLabel("Name");
		add(lblName, "cell 2 0,alignx left,aligny center");
		
		textFieldName = new JTextField();
		add(textFieldName, "cell 3 0,alignx left,aligny center");
		textFieldName.setColumns(10);
		textFieldName.setText(nodeObject.getName());
		
		lblWeight = new JLabel("Weight");
		add(lblWeight, "cell 4 0,alignx left,aligny center");
		
		spinnerWeight = new JSpinner();
		spinnerWeight.setModel(new SpinnerNumberModel(node.getWeight(), 0, 100, 1));
		add(spinnerWeight, "cell 5 0,alignx left,aligny center");
		
		lblLocation = new JLabel("Location");
		add(lblLocation, "cell 6 0,alignx left,aligny center");
		
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 7 0,alignx left,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());
		
		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setBackground(Color.WHITE);
		add(chckbxUsed, "cell 8 0,alignx left,aligny top");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);
		
		lblDamagestep = new JLabel("Damagestep");
		add(lblDamagestep, "cell 0 1");
		
		spinnerDamage = new JSpinner(new SpinnerNumberModel(node.getDamagestep(), 0, 100, 1));
		add(spinnerDamage, "cell 1 1");
		
		lblStrengthmin = new JLabel("Strengthmin");
		add(lblStrengthmin, "cell 2 1");
		
		spinnerStrMin = new JSpinner(new SpinnerNumberModel(node.getStrengthmin(), 0, 100, 1));
		add(spinnerStrMin, "cell 3 1");
		
		lblShortrange = new JLabel("Shortrange");
		add(lblShortrange, "cell 4 1");
		
		spinnerRangeShort = new JSpinner(new SpinnerNumberModel(node.getShortrange(), 0, 100, 1));
		add(spinnerRangeShort, "cell 5 1");
		
		lblLongrange = new JLabel("Longrange");
		add(lblLongrange, "cell 6 1");
		
		spinnerRangeLong = new JSpinner(new SpinnerNumberModel(node.getLongrange(), 0, 100, 1));
		add(spinnerRangeLong, "cell 7 1");
		
		labelForged = new JLabel("Forged");
		add(labelForged, "cell 0 2");
		
		spinnerForged = new JSpinner(new SpinnerNumberModel(node.getTimesforged(), 0, 100, 1));
		add(spinnerForged, "cell 1 2");
		
		lblTimesForged = new JLabel("Date forged");
		add(lblTimesForged, "cell 2 2,alignx trailing");
		
		textFieldDateForged = new JTextField();
		textFieldDateForged.setText(node.getDateforged());
		add(textFieldDateForged, "cell 3 2,growx");
		textFieldDateForged.setColumns(10);
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());
		nodeObject.setLocation(textFieldLocation.getText());
		nodeObject.setStrengthmin((Integer) spinnerStrMin.getValue());
		nodeObject.setShortrange((Integer) spinnerRangeShort.getValue());
		nodeObject.setLongrange((Integer) spinnerRangeLong.getValue());
		nodeObject.setTimesforged((Integer) spinnerForged.getValue());
		nodeObject.setDateforged(nodeObject.getDateforged());
		
		if(chckbxUsed.isSelected()){
			nodeObject.setUsed(YesnoType.YES);
		}
		else{
			nodeObject.setUsed(YesnoType.NO);
		}
		
	}

}
