package de.earthdawn.ui2.tree;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;

public class DisziplinAbilityNodePanel extends AbstractNodePanel<DisziplinAbilityNode> {
	private static final long serialVersionUID = -4670978991951233030L;
	private JSpinner spinnerCount;

	public DisziplinAbilityNodePanel(DisziplinAbilityNode node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][90px]", "[20px:20px:20px]"));
		add(new JLabel(nodeObject.getType().value()), "cell 0 0,alignx right,aligny center");
		spinnerCount = new JSpinner(new SpinnerNumberModel(nodeObject.getDisziplinAbility().getCount(), -99, 99, 1));
		add(spinnerCount, "cell 1 0,alignx left,aligny center");
	}

	@Override
	public void updateObject() {
		nodeObject.getDisziplinAbility().setCount((Integer)spinnerCount.getValue());
	}
}
