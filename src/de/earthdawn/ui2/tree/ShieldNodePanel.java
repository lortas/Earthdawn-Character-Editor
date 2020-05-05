package de.earthdawn.ui2.tree;

import de.earthdawn.DefenseAbility;
import de.earthdawn.data.EffectlayerType;
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
	private static final long serialVersionUID = -3332807171808480805L;
	private JTextField textFieldName;
	private JSpinner spinnerWeight;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JComboBox<ItemkindType> comboBoxType;
	private JSpinner spinnerPhysicalArmor;
	private JSpinner spinnerMysticArmor;
	private JSpinner spinnerPenalty;
	private JSpinner spinnerEnchanting;
	private JSpinner spinnerForgedPysical;
	private JSpinner spinnerForgedMystic;
	private JTextField textFieldDateForged;
	private JSpinner spinnerPhysicalDefense;
	private JSpinner spinnerMysticDefense;

	public ShieldNodePanel(SHIELDType node) {
		super(node);
		DefenseAbility defenses = new DefenseAbility(node.getDEFENSE());

		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px][27px][86px][34px][47px,grow][40px][86px]", "[][][]"));

		add(new JLabel("Type"), "cell 0 0,alignx left,aligny center");
		comboBoxType = new JComboBox<ItemkindType>();
		for( ItemkindType item : ItemkindType.values() ) comboBoxType.addItem(item);
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,alignx left,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("Name"), "cell 0 1,alignx left,aligny center");
		textFieldName = new JTextField();
		add(textFieldName, "cell 1 1,growx,aligny center");
		textFieldName.setColumns(10);
		textFieldName.setText(nodeObject.getName());

		add(new JLabel("Location"), "cell 0 2,alignx left,aligny center");
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 1 2,growx,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());

		add(new JLabel("Armor (physical/mystic)"), "cell 2 0");
		spinnerPhysicalArmor = new JSpinner(new SpinnerNumberModel(node.getPhysicalarmor(), 0, 100, 1));
		add(spinnerPhysicalArmor, "cell 3 0");
		add(new JLabel("/"), "cell 3 0");
		spinnerMysticArmor = new JSpinner(new SpinnerNumberModel(node.getMysticarmor(), 0, 100, 1));
		add(spinnerMysticArmor, "cell 3 0");

		add(new JLabel("Defense (physical/mystic)"), "cell 2 1");
		spinnerPhysicalDefense = new JSpinner(new SpinnerNumberModel(defenses.get(EffectlayerType.PHYSICAL), 0, 100, 1));
		add(spinnerPhysicalDefense, "cell 3 1");
		add(new JLabel("/"), "cell 3 1");
		spinnerMysticDefense = new JSpinner(new SpinnerNumberModel(defenses.get(EffectlayerType.MYSTIC), 0, 100, 1));
		add(spinnerMysticDefense, "cell 3 1");

		add(new JLabel("Forged (physical/mystic)"), "cell 2 2");
		spinnerForgedPysical = new JSpinner(new SpinnerNumberModel(node.getTimesforgedPhysical(), 0, 100, 1));
		add(spinnerForgedPysical, "cell 3 2");
		add(new JLabel("/"), "cell 3 2");
		spinnerForgedMystic = new JSpinner(new SpinnerNumberModel(node.getTimesforgedMystic(), 0, 100, 1));
		add(spinnerForgedMystic, "cell 3 2");

		add(new JLabel("Weight"), "cell 4 0,alignx left,aligny center");
		spinnerWeight = new JSpinner();
		spinnerWeight.setModel(new SpinnerNumberModel(node.getWeight(), 0, 100, 1));
		add(spinnerWeight, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Penalty"), "cell 4 1");
		spinnerPenalty = new JSpinner(new SpinnerNumberModel(node.getPenalty(), 0, 100, 1));
		add(spinnerPenalty, "cell 5 1");

		add(new JLabel("Date forged"), "cell 6 0,alignx trailing");
		textFieldDateForged = new JTextField();
		textFieldDateForged.setText(node.getDateforged());
		add(textFieldDateForged, "cell 7 0,growx");
		textFieldDateForged.setColumns(10);

		add(new JLabel("Enchanting Diff"), "cell 6 1");
		spinnerEnchanting = new JSpinner(new SpinnerNumberModel(node.getEdn(), 0, 100, 1));
		add(spinnerEnchanting, "cell 7 1");

		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 7 2,alignx left,aligny top");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());
		nodeObject.setLocation(textFieldLocation.getText());
		
		nodeObject.setPhysicalarmor((Integer) spinnerPhysicalArmor.getValue());
		nodeObject.setMysticarmor((Integer) spinnerMysticArmor.getValue());
		DefenseAbility defenses = new DefenseAbility(nodeObject.getDEFENSE());
		defenses.set(EffectlayerType.PHYSICAL, (Integer) spinnerPhysicalDefense.getValue());
		defenses.set(EffectlayerType.MYSTIC, (Integer) spinnerMysticDefense.getValue());
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
