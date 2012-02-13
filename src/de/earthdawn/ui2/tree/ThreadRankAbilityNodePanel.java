package de.earthdawn.ui2.tree;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

public class ThreadRankAbilityNodePanel extends AbstractNodePanel<ThreadRankAbilityNode> {
	private static final long serialVersionUID = 6601843917581431482L;
	private JTextField textFieldAbility;
	
	public ThreadRankAbilityNodePanel(ThreadRankAbilityNode node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][20px][200px,grow]", "[20px:20px:20px]"));

		add(new JLabel("Ability"), "cell 0 0,alignx right,aligny center");
		
		textFieldAbility = new JTextField();
		add(textFieldAbility, "cell 2 0,growx,aligny center");
		textFieldAbility.setColumns(12);
	}

	@Override
	public void updateObject() {
		nodeObject.setAbility(textFieldAbility.getText());
	}
}
