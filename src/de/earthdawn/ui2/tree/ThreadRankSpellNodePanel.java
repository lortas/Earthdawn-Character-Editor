package de.earthdawn.ui2.tree;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;

public class ThreadRankSpellNodePanel extends AbstractNodePanel<ThreadRankSpellNode> {
	private static final long serialVersionUID = 6601843917581431482L;
	private JComboBox comboBoxSpellname;

	public ThreadRankSpellNodePanel(ThreadRankSpellNode node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][20px][90px][20]", "[20px:20px:20px]"));

		add(new JLabel("Spell"), "cell 0 0,alignx right,aligny center");
		comboBoxSpellname = new JComboBox(ThreadRankSpellNode.getSpellList().toArray());
		comboBoxSpellname.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxSpellname, "cell 1 0,alignx left,aligny center");
		comboBoxSpellname.setSelectedItem(nodeObject.getSpellname());
	}

	@Override
	public void updateObject() {
		nodeObject.setSpell((String)comboBoxSpellname.getSelectedItem());
	}
}
