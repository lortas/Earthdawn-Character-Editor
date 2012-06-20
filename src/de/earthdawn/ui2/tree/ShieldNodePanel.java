package de.earthdawn.ui2.tree;

import de.earthdawn.data.SHIELDType;
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


public class ShieldNodePanel extends AbstractNodePanel<SHIELDType> {
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
	private JLabel lblPhysicalArmor;
	private JSpinner spinnerPhysical;
	private JLabel lblMysticArmor;
	private JSpinner spinnerMystic;
	private JLabel lblPenalty;
	private JSpinner spinnerPenalty;
	private JLabel lblEnchantingDiff;
	private JSpinner spinnerEnchanting;
	private JLabel lblForgedPhysical;
	private JLabel lblForgedMystic;
	private JLabel lblDateForged;
	private JSpinner spinnerForgedPysical;
	private JSpinner spinnerForgedMystic;
	private JTextField textFieldDateForged;

	public ShieldNodePanel(SHIELDType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px][27px][86px][34px][47px,grow][40px][86px][49px]", "[23px][][]"));
		
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
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 8 0,alignx left,aligny top");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);
		
		lblPhysicalArmor = new JLabel("Physical armor");
		add(lblPhysicalArmor, "cell 0 1");
		
		spinnerPhysical = new JSpinner(new SpinnerNumberModel(node.getPhysicalarmor(), 0, 100, 1));
		add(spinnerPhysical, "cell 1 1");
		
		lblMysticArmor = new JLabel("Mystic armor");
		add(lblMysticArmor, "cell 2 1");
		
		spinnerMystic = new JSpinner(new SpinnerNumberModel(node.getMysticarmor(), 0, 100, 1));
		add(spinnerMystic, "cell 3 1");
		
		lblPenalty = new JLabel("Penalty");
		add(lblPenalty, "cell 4 1");
		
		spinnerPenalty = new JSpinner(new SpinnerNumberModel(node.getPenalty(), 0, 100, 1));
		add(spinnerPenalty, "cell 5 1");
		
		lblEnchantingDiff = new JLabel("Enchanting Diff");
		add(lblEnchantingDiff, "cell 6 1");
		
		spinnerEnchanting = new JSpinner(new SpinnerNumberModel(node.getEdn(), 0, 100, 1));
		add(spinnerEnchanting, "cell 7 1");
		
		lblForgedPhysical = new JLabel("Forged physical");
		add(lblForgedPhysical, "cell 0 2");
		
		spinnerForgedPysical = new JSpinner(new SpinnerNumberModel(node.getTimesforgedPhysical(), 0, 100, 1));
		add(spinnerForgedPysical, "cell 1 2");
		
		lblForgedMystic = new JLabel("Forged mystic");
		add(lblForgedMystic, "cell 2 2");
		
		spinnerForgedMystic = new JSpinner(new SpinnerNumberModel(node.getTimesforgedMystic(), 0, 100, 1));
		add(spinnerForgedMystic, "cell 3 2");
		
		lblDateForged = new JLabel("Date forged");
		add(lblDateForged, "cell 4 2,alignx trailing");
		
		textFieldDateForged = new JTextField();
		textFieldDateForged.setText(node.getDateforged());
		add(textFieldDateForged, "cell 5 2,growx");
		textFieldDateForged.setColumns(10);
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
		
		nodeObject.setTimesforgedPhysical((Integer) spinnerForgedPysical.getValue());
		nodeObject.setTimesforgedMystic((Integer) spinnerForgedMystic.getValue());
		nodeObject.setDateforged(textFieldDateForged.getText());
		
		if(chckbxUsed.isSelected()){
			nodeObject.setUsed(YesnoType.YES);
		}
		else{
			nodeObject.setUsed(YesnoType.NO);
		}
		
	}

}
