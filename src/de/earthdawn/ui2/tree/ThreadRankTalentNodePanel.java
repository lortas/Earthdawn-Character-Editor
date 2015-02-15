package de.earthdawn.ui2.tree;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.miginfocom.swing.MigLayout;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.TALENTABILITYType;
import javax.swing.JSpinner;

public class ThreadRankTalentNodePanel extends AbstractNodePanel<TALENTABILITYType> {
	private static final long serialVersionUID = 4271333564693831830L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final Map<String, TALENTABILITYType> talentByCircle = PROPERTIES.getTalentsByCircle(15);
	public static final List<CAPABILITYType> talents = PROPERTIES.getCapabilities().getTalents();
	private Map<String,String[]> talenthash;
	private JComboBox<String> comboBoxTalent;
	private JSpinner spinnerBonus;
	private JTextField textFieldPool;

	public ThreadRankTalentNodePanel(TALENTABILITYType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[50px][200px,grow][50px][50px][50px][200px,grow]", "[20px:20px:20px]"));

		add(new JLabel("Talent"), "cell 0 0,alignx right,aligny center");
		talenthash = new TreeMap<String,String[]>();
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
		String[] array = talenthash.keySet().toArray(new String[1]);
		Arrays.sort(array);
		comboBoxTalent = new JComboBox<String>(array);
		comboBoxTalent.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxTalent, "cell 1 0,growx,aligny center");
		if( (nodeObject.getLimitation()==null) ||  nodeObject.getLimitation().isEmpty() ) {
			comboBoxTalent.setSelectedItem(nodeObject.getName());
		} else {
			comboBoxTalent.setSelectedItem(nodeObject.getName()+" - "+nodeObject.getLimitation());
		}

		add(new JLabel("Bonus"), "cell 2 0,alignx right,aligny center");
		spinnerBonus = new JSpinner(new SpinnerNumberModel(nodeObject.getBonus(), -99, 99, 1));
		add(spinnerBonus, "cell 3 0");

		add(new JLabel("Pool"), "cell 4 0,alignx right,aligny center");
		textFieldPool = new JTextField();
		add(textFieldPool, "cell 5 0,growx,aligny center");
		textFieldPool.setColumns(12);
	}

	@Override
	public void updateObject() {
		nodeObject.setPool(textFieldPool.getText());
		nodeObject.setBonus((Integer)spinnerBonus.getValue());
		String[] tal = talenthash.get(comboBoxTalent.getSelectedItem());
		if( tal == null ) return;
		nodeObject.setName(tal[0]);
		nodeObject.setLimitation(tal[1]);
	}
}
