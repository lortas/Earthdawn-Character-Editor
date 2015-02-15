package de.earthdawn.ui2.tree;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Map;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.SPELLDEFType;
import de.earthdawn.ui2.tree.StringNodeType;
import net.miginfocom.swing.MigLayout;

public class StringNodePanel extends AbstractNodePanel<StringNode> {
	private static final long serialVersionUID = 4048257772310202634L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final Map<String, SPELLDEFType> spelldefs = PROPERTIES.getSpells();
	private StringNodeType nodeType;
	private JComboBox<String> comboBoxString;
	private JTextField textFieldString;

	public StringNodePanel(StringNode node) {
		super(node);
		nodeType = node.getType();
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[90px][200px]", "[20px:20px:20px]"));

		switch( nodeType ) {
		case SPELL:
			add(new JLabel("Spell"), "cell 0 0,alignx right,aligny center");
			String[] array = spelldefs.keySet().toArray(new String[1]);
			Arrays.sort(array);
			comboBoxString = new JComboBox<String>(array);
			comboBoxString.setFont(new Font("Tahoma", Font.PLAIN, 10));
			add(comboBoxString, "cell 1 0,alignx left,aligny center");
			comboBoxString.setSelectedItem(nodeObject.getString());
			break;
		case ABILITY:
			add(new JLabel("Ability"), "cell 0 0,alignx right,aligny center");
			textFieldString = new JTextField(nodeObject.getString());
			add(textFieldString, "cell 1 0,growx,aligny center");
			textFieldString.setColumns(12);
			break;
		default:
			add(new JLabel("String"), "cell 0 0,alignx right,aligny center");
			textFieldString = new JTextField();
			add(textFieldString, "cell 1 0,growx,aligny center");
			textFieldString.setColumns(12);
			break;
		}
	}

	@Override
	public void updateObject() {
		switch( nodeType ) {
		case SPELL:
			nodeObject.setString((String)comboBoxString.getSelectedItem());
			break;
		default:
			nodeObject.setString(textFieldString.getText());
			break;
		}
	}
}
