package de.earthdawn.ui2.tree;

import de.earthdawn.data.DEFENSEABILITYType;
import de.earthdawn.data.EffectlayerType;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSpinner;

public class DefenseAbilityNodePanel extends AbstractNodePanel<DEFENSEABILITYType> {
	private static final long serialVersionUID = 6601843917581431482L;
	private JSpinner spinnerBonus;
	private JComboBox<EffectlayerType> comboBoxType;

	public DefenseAbilityNodePanel(DEFENSEABILITYType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][20px][90px][20]", "[20px:20px:20px]"));

		add(new JLabel("Defense"), "cell 0 0,alignx right,aligny center");
		comboBoxType = new JComboBox<EffectlayerType>(EffectlayerType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,alignx left,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("Bonus"), "cell 2 0,alignx right,aligny center");
		spinnerBonus = new JSpinner(new SpinnerNumberModel(nodeObject.getBonus(), -99, 99, 1));
		add(spinnerBonus, "cell 3 0,alignx left,aligny center");
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((EffectlayerType) comboBoxType.getSelectedItem());
		nodeObject.setBonus((Integer)spinnerBonus.getValue());
	}
}
