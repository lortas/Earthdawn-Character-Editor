package de.earthdawn.ui2.tree;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.TALENTABILITYType;
import javax.swing.JSpinner;

public class ThreadRankTalentNodePanel extends AbstractNodePanel<TALENTABILITYType> {
	private static final long serialVersionUID = 4271333564693831830L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final HashMap<String, TALENTABILITYType> talentByCircle = PROPERTIES.getTalentsByCircle(15);
	public static final List<CAPABILITYType> talents = PROPERTIES.getCapabilities().getTalents();
	private HashMap<String,String[]> talenthash;
	private JComboBox comboBoxTalent;
	private JSpinner spinnerBonus;

	public ThreadRankTalentNodePanel(TALENTABILITYType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][200px,grow][90px][90px][90px][90px]", "[20px:20px:20px]"));

		add(new JLabel("Talent"), "cell 0 0,alignx right,aligny center");
		talenthash = new HashMap<String,String[]>();
		for( CAPABILITYType t : talents ) {
			List<String> limitations = t.getLIMITATION();
			String talentname=t.getName();
			if( limitations.isEmpty() ) {
				talenthash.put(talentname, new String[]{talentname,"",""});
			} else {
				for( String l : limitations ) talenthash.put(talentname+" - "+l, new String[]{talentname,l,""});
			}
		}
		for( String talent : talentByCircle.keySet() ) {
			TALENTABILITYType t = talentByCircle.get(talent);
			talenthash.put(talent, new String[]{t.getName(),t.getLimitation(),t.getPool()});
		}
		Object[] array = talenthash.keySet().toArray();
		Arrays.sort(array);
		comboBoxTalent = new JComboBox(array);
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
		String[] tal = talenthash.get(comboBoxTalent.getSelectedItem());
		if( tal == null ) return;
		nodeObject.setName(tal[0]);
		nodeObject.setLimitation(tal[1]);
		nodeObject.setBonus((Integer)spinnerBonus.getValue());
		//nodeObject.setPool(value);
	}
}
