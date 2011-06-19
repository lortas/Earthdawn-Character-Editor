package de.earthdawn.ui2.tree;

import de.earthdawn.data.BLOODCHARMITEMType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;

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
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;


public class BloodcharmNodePanel extends AbstractNodePanel<BLOODCHARMITEMType> {
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
	private JLabel lblBlooddamage;
	private JSpinner spinnerBloodDamage;
	private JLabel lblEffect;
	private JTextField textFieldEffect;
	private JLabel lblDepatterningrate;
	private JSpinner spinnerDepatterningrate;
	private JLabel lblEnchantingDn;
	private JSpinner spinnerEnchantingdifficultynumber;

	public BloodcharmNodePanel(BLOODCHARMITEMType node) {
		super(node);
		this.setPreferredSize(new Dimension(710, 65));
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px][27px][86px,grow][34px][47px][40px][86px][49px]", "[23px][]"));
		
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
		
		lblBlooddamage = new JLabel("Blooddamage");
		add(lblBlooddamage, "cell 0 1");
		
		spinnerBloodDamage = new JSpinner();
		add(spinnerBloodDamage, "cell 1 1");
		
		lblEffect = new JLabel("Effect");
		add(lblEffect, "cell 2 1,alignx trailing");
		
		textFieldEffect = new JTextField();
		add(textFieldEffect, "cell 3 1,growx");
		textFieldEffect.setColumns(10);
		
		lblDepatterningrate = new JLabel("Depatterningrate");
		add(lblDepatterningrate, "cell 4 1");
		
		spinnerDepatterningrate = new JSpinner();
		add(spinnerDepatterningrate, "cell 5 1");
		
		lblEnchantingDn = new JLabel("Enchanting DN");
		add(lblEnchantingDn, "cell 6 1");
		
		spinnerEnchantingdifficultynumber = new JSpinner();
		add(spinnerEnchantingdifficultynumber, "cell 7 1");
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());
		nodeObject.setLocation(textFieldLocation.getText());
		if(chckbxUsed.isSelected()){
			nodeObject.setUsed(YesnoType.YES);
		}
		else{
			nodeObject.setUsed(YesnoType.NO);
		}
		
	}

}
