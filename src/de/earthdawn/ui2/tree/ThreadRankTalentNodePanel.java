package de.earthdawn.ui2.tree;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import net.miginfocom.swing.MigLayout;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.TALENTABILITYType;
import javax.swing.JSpinner;

public class ThreadRankTalentNodePanel extends AbstractNodePanel<TALENTABILITYType> {
	private static final long serialVersionUID = 4271333564693831830L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final HashMap<String, TALENTABILITYType> talents = PROPERTIES.getTalentsByCircle(15);
	private JComboBox comboBoxTalent;
	private JSpinner spinnerBonus;

	public ThreadRankTalentNodePanel(TALENTABILITYType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][200px,grow][90px][90px][90px][90px]", "[20px:20px:20px]"));

		add(new JLabel("Talent"), "cell 0 0,alignx right,aligny center");
		comboBoxTalent = new JComboBox(talents.keySet().toArray());
		comboBoxTalent.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxTalent, "cell 1 0,growx,aligny center");
		comboBoxTalent.setSelectedItem(nodeObject.getName());

		add(new JLabel("Bonus"), "cell 2 0,alignx right,aligny center");
		spinnerBonus = new JSpinner(new SpinnerNumberModel(nodeObject.getBonus(), -99, 99, 1));
		add(spinnerBonus, "cell 3 0");

		add(new JLabel("Pool"), "cell 4 0,alignx right,aligny center");
	}

	@Override
	public void updateObject() {
		TALENTABILITYType tal = talents.get(comboBoxTalent.getSelectedItem());
		if( tal == null ) return;
		nodeObject.setName(tal.getName());
		nodeObject.setLimitation(tal.getLimitation());
		nodeObject.setBonus((Integer)spinnerBonus.getValue());
		//nodeObject.setPool(value);
	}
}
