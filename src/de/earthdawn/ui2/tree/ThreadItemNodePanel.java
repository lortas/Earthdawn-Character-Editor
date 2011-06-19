package de.earthdawn.ui2.tree;

import de.earthdawn.data.ITEMType;
import de.earthdawn.data.THREADITEMType;
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


public class ThreadItemNodePanel extends AbstractNodePanel<THREADITEMType> {
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
	private JLabel lblDescription;
	private JTextField textFieldDescription;
	private JLabel lblSpelldefense;
	private JSpinner spinnerSpellDefense;
	private JLabel lblMaxThreads;
	private JSpinner spinnerMaxThreads;
	private JLabel lblLpGrowth;
	private JSpinner spinnerLP;

	public ThreadItemNodePanel(THREADITEMType node) {
		super(node);
		this.setPreferredSize(new Dimension(710, 65));
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px,grow][27px][86px][34px][47px][40px][86px][49px]", "[23px][]"));
		
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
		
		lblDescription = new JLabel("Description");
		add(lblDescription, "cell 0 1,alignx trailing");
		
		textFieldDescription = new JTextField();
		add(textFieldDescription, "cell 1 1,growx");
		textFieldDescription.setColumns(10);
		
		lblSpelldefense = new JLabel("Spelldefense");
		add(lblSpelldefense, "cell 2 1");
		
		spinnerSpellDefense = new JSpinner();
		add(spinnerSpellDefense, "cell 3 1");
		
		lblMaxThreads = new JLabel("Max threads");
		add(lblMaxThreads, "cell 4 1");
		
		spinnerMaxThreads = new JSpinner();
		add(spinnerMaxThreads, "cell 5 1");
		
		lblLpGrowth = new JLabel("LP growth");
		add(lblLpGrowth, "cell 6 1");
		
		spinnerLP = new JSpinner();
		add(spinnerLP, "cell 7 1");
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
