package de.earthdawn.ui2.tree;

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
import java.awt.Font;
import javax.swing.border.LineBorder;


public class ItemNodePanel extends AbstractNodePanel<ITEMType> {
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

	public ItemNodePanel(ITEMType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		
		lblType = new JLabel("Type");
		add(lblType);
		
		comboBoxType = new JComboBox(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType);
		comboBoxType.setSelectedItem(nodeObject.getKind());
		
		lblName = new JLabel("Name");
		add(lblName);
		
		textFieldName = new JTextField();
		add(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.setText(nodeObject.getName());
		
		lblWeight = new JLabel("Weight");
		add(lblWeight);
		
		spinnerWeight = new JSpinner();
		spinnerWeight.setModel(new SpinnerNumberModel(node.getWeight(), 0, 100, 1));
		add(spinnerWeight);
		
		lblLocation = new JLabel("Location");
		add(lblLocation);
		
		textFieldLocation = new JTextField();
		add(textFieldLocation);
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());
		
		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setBackground(Color.WHITE);
		add(chckbxUsed);
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);
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
