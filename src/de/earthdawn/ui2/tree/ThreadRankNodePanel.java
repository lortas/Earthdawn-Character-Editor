package de.earthdawn.ui2.tree;

import de.earthdawn.data.THREADRANKType;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;

public class ThreadRankNodePanel extends AbstractNodePanel<THREADRANKType> {
	private static final long serialVersionUID = 6601843917581431482L;
	private JTextField textFieldEffect;
	private JTextField textFieldKeyknowledge;

	public ThreadRankNodePanel(THREADRANKType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][500px,grow]", "[20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Effect"), "cell 0 0,alignx right,aligny center");
		textFieldEffect = new JTextField();
		add(textFieldEffect, "cell 1 0,growx,alignx left,aligny center");
		textFieldEffect.setColumns(12);
		textFieldEffect.setText(nodeObject.getEffect());

		add(new JLabel("Keyknowledge"), "cell 0 1,alignx right,aligny center");
		textFieldKeyknowledge = new JTextField();
		add(textFieldKeyknowledge, "cell 1 1,growx,alignx left,aligny center");
		textFieldKeyknowledge.setColumns(12);
		textFieldKeyknowledge.setText(nodeObject.getKeyknowledge());
	}

	@Override
	public void updateObject() {
		nodeObject.setEffect(textFieldEffect.getText());
		nodeObject.setKeyknowledge(textFieldKeyknowledge.getText());
	}
}
