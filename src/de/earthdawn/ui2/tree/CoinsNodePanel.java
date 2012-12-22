package de.earthdawn.ui2.tree;

import de.earthdawn.data.COINSType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;

public class CoinsNodePanel extends AbstractNodePanel<COINSType> {
	private static final long serialVersionUID = -2763493441410217313L;
	private JTextField textFieldName;
	private JTextField textFieldLocation;
	private JComboBox<ItemkindType> comboBoxType;
	private JTextField textFieldBookRef;
	private JSpinner spinnerDepatterningrate;
	private JSpinner spinnerBloodDamage;
	private JSpinner spinnerCopper;
	private JSpinner spinnerSilver;
	private JSpinner spinnerGold;
	private JSpinner spinnerEarth;
	private JSpinner spinnerWater;
	private JSpinner spinnerAir;
	private JSpinner spinnerFire;
	private JSpinner spinnerOrichalcum;
	private JSpinner spinnerGem50;
	private JSpinner spinnerGem100;
	private JSpinner spinnerGem200;
	private JSpinner spinnerGem500;
	private JSpinner spinnerGem1000;

	public CoinsNodePanel(COINSType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[100px,grow][30px,grow][30px,grow][30px,grow][30px,grow][30px,grow][30px,grow][30px,grow][30px,grow][30px,grow]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Type"), "cell 0 0,alignx right,aligny center");
		comboBoxType = new JComboBox<ItemkindType>(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,growx,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("Name"), "cell 0 2,alignx right,aligny center");
		textFieldName = new JTextField();
		add(textFieldName, "cell 1 2,growx,aligny center");
		textFieldName.setColumns(12);
		textFieldName.setText(nodeObject.getName());

		add(new JLabel("Location"), "cell 0 1,alignx right,aligny center");
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 1 1,growx,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());

		add(new JLabel("BookRef"), "cell 0 3,alignx right,aligny center");
		textFieldBookRef = new JTextField();
		textFieldBookRef.setText(node.getBookref());
		add(textFieldBookRef, "cell 1 3,growx,aligny center");
		textFieldBookRef.setColumns(10);

		add(new JLabel("Blood Damage"), "cell 8 2,alignx right,aligny center");
		spinnerBloodDamage = new JSpinner(new SpinnerNumberModel(node.getBlooddamage(), 0, 100, 1));
		add(spinnerBloodDamage, "cell 9 2,alignx left,aligny center");

		add(new JLabel("Depatterningrate"), "cell 8 3,alignx right");
		spinnerDepatterningrate = new JSpinner(new SpinnerNumberModel(node.getDepatterningrate(), 0, 100, 1));
		add(spinnerDepatterningrate, "cell 9 3,alignx left,aligny center");

		add(new JLabel("Copper"), "cell 2 0,alignx right,aligny center");
		spinnerCopper = new JSpinner(new SpinnerNumberModel(node.getCopper(), 0, 9999, 1));
		add(spinnerCopper, "cell 3 0,alignx left,aligny center");

		add(new JLabel("Silver"), "cell 2 1,alignx right,aligny center");
		spinnerSilver = new JSpinner(new SpinnerNumberModel(node.getSilver(), 0, 9999, 1));
		add(spinnerSilver, "cell 3 1,alignx left,aligny center");

		add(new JLabel("Gold"), "cell 2 2,alignx right,aligny center");
		spinnerGold = new JSpinner(new SpinnerNumberModel(node.getGold(), 0, 9999, 1));
		add(spinnerGold, "cell 3 2,alignx left,aligny center");

		add(new JLabel("Earth"), "cell 2 3,alignx right,aligny center");
		spinnerEarth = new JSpinner(new SpinnerNumberModel(node.getEarth(), 0, 9999, 1));
		add(spinnerEarth, "cell 3 3,alignx left,aligny center");

		add(new JLabel("Water"), "cell 4 0,alignx right,aligny center");
		spinnerWater = new JSpinner(new SpinnerNumberModel(node.getWater(), 0, 9999, 1));
		add(spinnerWater, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Air"), "cell 4 1,alignx right,aligny center");
		spinnerAir = new JSpinner(new SpinnerNumberModel(node.getAir(), 0, 9999, 1));
		add(spinnerAir, "cell 5 1,alignx left,aligny center");

		add(new JLabel("Fire"), "cell 4 2,alignx right,aligny center");
		spinnerFire = new JSpinner(new SpinnerNumberModel(node.getFire(), 0, 9999, 1));
		add(spinnerFire, "cell 5 2,alignx left,aligny center");

		add(new JLabel("Orichalcum"), "cell 4 3,alignx right,aligny center");
		spinnerOrichalcum = new JSpinner(new SpinnerNumberModel(node.getOrichalcum(), 0, 9999, 1));
		add(spinnerOrichalcum, "cell 5 3,alignx left,aligny center");

		add(new JLabel("Gem (50)"), "cell 6 0,alignx right,aligny center");
		spinnerGem50 = new JSpinner(new SpinnerNumberModel(node.getGem50(), 0, 9999, 1));
		add(spinnerGem50, "cell 7 0,alignx left,aligny center");

		add(new JLabel("Gem (100)"), "cell 6 1,alignx right,aligny center");
		spinnerGem100 = new JSpinner(new SpinnerNumberModel(node.getGem100(), 0, 9999, 1));
		add(spinnerGem100, "cell 7 1,alignx left,aligny center");

		add(new JLabel("Gem (200)"), "cell 6 2,alignx right,aligny center");
		spinnerGem200 = new JSpinner(new SpinnerNumberModel(node.getGem200(), 0, 9999, 1));
		add(spinnerGem200, "cell 7 2,alignx left,aligny center");

		add(new JLabel("Gem (500)"), "cell 6 3,alignx right,aligny center");
		spinnerGem500 = new JSpinner(new SpinnerNumberModel(node.getGem500(), 0, 9999, 1));
		add(spinnerGem500, "cell 7 3,alignx left,aligny center");

		add(new JLabel("Gem (1000)"), "cell 8 0,alignx right,aligny center");
		spinnerGem1000 = new JSpinner(new SpinnerNumberModel(node.getGem1000(), 0, 9999, 1));
		add(spinnerGem1000, "cell 9 0,alignx left,aligny center");
	}

	@Override
	public void updateObject() {
		nodeObject.setUsed(YesnoType.YES); // Ein Geldbeutetl ist immer used
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setLocation(textFieldLocation.getText());
		nodeObject.setBookref(textFieldBookRef.getText());
		nodeObject.setBlooddamage((Integer) spinnerBloodDamage.getValue());
		nodeObject.setDepatterningrate((Integer) spinnerDepatterningrate.getValue());
		nodeObject.setCopper((Integer)spinnerCopper.getValue());
		nodeObject.setSilver((Integer)spinnerSilver.getValue());
		nodeObject.setGold((Integer)spinnerGold.getValue());
		nodeObject.setEarth((Integer)spinnerEarth.getValue());
		nodeObject.setWater((Integer)spinnerWater.getValue());
		nodeObject.setAir((Integer)spinnerAir.getValue());
		nodeObject.setFire((Integer)spinnerFire.getValue());
		nodeObject.setOrichalcum((Integer)spinnerOrichalcum.getValue());
		nodeObject.setGem50((Integer)spinnerGem50.getValue());
		nodeObject.setGem100((Integer)spinnerGem100.getValue());
		nodeObject.setGem200((Integer)spinnerGem200.getValue());
		nodeObject.setGem500((Integer)spinnerGem500.getValue());
		nodeObject.setGem1000((Integer)spinnerGem1000.getValue());
	}
}
