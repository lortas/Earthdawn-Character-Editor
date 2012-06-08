package de.earthdawn.ui2.tree;

import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WOUNDType;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSpinner;

public class ThreadRankNodePanel extends AbstractNodePanel<THREADRANKType> {
	private static final long serialVersionUID = 6601843917581431482L;
	private JTextField textFieldEffect;
	private JTextField textFieldKeyknowledge;
	private JTextField textFieldDeed;
	private JSpinner spinnerNormalWounds;
	private JSpinner spinnerBloodWounds;
	private JSpinner spinnerWoundPenalties;
	private JSpinner spinnerWoundThreshold;

	public ThreadRankNodePanel(THREADRANKType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[100px,grow][20px][100px,grow][20px][100px,grow][20px][100px,grow][20px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Effect"), "cell 0 0,alignx right,aligny center");
		textFieldEffect = new JTextField();
		add(textFieldEffect, "cell 1 0 7 1,growx,alignx left,aligny center");
		textFieldEffect.setColumns(12);
		textFieldEffect.setText(nodeObject.getEffect());

		add(new JLabel("Keyknowledge"), "cell 0 1,alignx right,aligny center");
		textFieldKeyknowledge = new JTextField();
		add(textFieldKeyknowledge, "cell 1 1 7 1,growx,alignx left,aligny center");
		textFieldKeyknowledge.setColumns(12);
		textFieldKeyknowledge.setText(nodeObject.getKeyknowledge());

		add(new JLabel("Deed"), "cell 0 2,alignx right,aligny center");
		textFieldDeed = new JTextField();
		add(textFieldDeed, "cell 1 2 7 1,growx,alignx left,aligny center");
		textFieldDeed.setColumns(12);
		textFieldDeed.setText(nodeObject.getDeed());

		WOUNDType wound = nodeObject.getWOUND();
		if( wound==null ) wound=new WOUNDType();

		add(new JLabel("normal wounds"), "cell 0 3,alignx right,aligny center");
		spinnerNormalWounds = new JSpinner(new SpinnerNumberModel(wound.getNormal(), -99, 99, 1));
		add(spinnerNormalWounds, "cell 1 3");

		add(new JLabel("blood wounds"), "cell 2 3,alignx right,aligny center");
		spinnerBloodWounds = new JSpinner(new SpinnerNumberModel(wound.getBlood(), -99, 99, 1));
		add(spinnerBloodWounds, "cell 3 3");

		add(new JLabel("penalties"), "cell 4 3,alignx right,aligny center");
		spinnerWoundPenalties = new JSpinner(new SpinnerNumberModel(wound.getPenalties(), -99, 99, 1));
		add(spinnerWoundPenalties, "cell 5 3");

		add(new JLabel("threshold"), "cell 6 3,alignx right,aligny center");
		spinnerWoundThreshold = new JSpinner(new SpinnerNumberModel(wound.getThreshold(), -99, 99, 1));
		add(spinnerWoundThreshold, "cell 7 3");
	}

	@Override
	public void updateObject() {
		nodeObject.setEffect(textFieldEffect.getText());
		nodeObject.setKeyknowledge(textFieldKeyknowledge.getText());
		nodeObject.setDeed(textFieldDeed.getText());
		WOUNDType wound = nodeObject.getWOUND();
		if( wound==null ) wound=new WOUNDType();
		wound.setNormal((Integer)spinnerNormalWounds.getValue());
		wound.setBlood((Integer)spinnerBloodWounds.getValue());
		wound.setPenalties((Integer)spinnerWoundPenalties.getValue());
		wound.setThreshold((Integer)spinnerWoundThreshold.getValue());
		if( (wound.getNormal()==0) && (wound.getBlood()==0) && (wound.getPenalties()==0) && (wound.getThreshold()==0) ) wound=null;
		nodeObject.setWOUND(wound);
	}
}
