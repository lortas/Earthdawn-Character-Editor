package de.earthdawn.ui2;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import javax.swing.JList;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.LanguageType;
import de.earthdawn.data.OPTIONALRULESDEFAULTOPTIONALTALENT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;

public class DefaultOptionalTalent extends JFrame {
	private static final long serialVersionUID = 3651503383632767118L;
	private static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS"); //$NON-NLS-1$
	private static LanguageType LANGUAGE = LanguageType.EN;
	private String title=NLS.getString("DefaultOptionalTalent.Title.text");
	private static DefaultListModel<de.earthdawn.config.DefaultOptionalTalent> chosenTalents;
	private JList<de.earthdawn.config.DefaultOptionalTalent> chosenTalentList;
	private JComboBox<Integer> cbxDiscipline = new JComboBox<Integer>();
	private JComboBox<Integer> cbxCircle = new JComboBox<Integer>();
	private JComboBox<String> cbxTalent = new JComboBox<String>();

	public DefaultOptionalTalent() throws HeadlessException {
		initiate();
	}

	public DefaultOptionalTalent(GraphicsConfiguration gc) {
		super(gc);
		initiate();
	}

	public DefaultOptionalTalent(String title) throws HeadlessException {
		super(title);
		this.title=title;
		initiate();
	}

	public DefaultOptionalTalent(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.title=title;
		initiate();
	}

	private void initiate() {
		setTitle(title);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500,400);

		JButton btnClose = new JButton(NLS.getString("DefaultOptionalTalent.btnClose.text"));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JButton btnInsertTalent = new JButton(NLS.getString("DefaultOptionalTalent.btnInsert.text"));
		btnInsertTalent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPTIONALRULESDEFAULTOPTIONALTALENT talent = new OPTIONALRULESDEFAULTOPTIONALTALENT();
				talent.setLang(LANGUAGE);
				talent.setCircle((Integer) cbxCircle.getSelectedItem());
				talent.setDiscipline((Integer) cbxDiscipline.getSelectedItem());
				talent.setTalent((String) cbxTalent.getSelectedItem());
				PROPERTIES.getOptionalRules().getDEFAULTOPTIONALTALENT().add(talent);
				updateTalents();
			}
		});
		JButton btnRemoveTalent = new JButton(NLS.getString("DefaultOptionalTalent.btnRemove.text"));
		btnRemoveTalent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for( de.earthdawn.config.DefaultOptionalTalent talentcomponent : chosenTalentList.getSelectedValuesList() ) {
					PROPERTIES.getOptionalRules().getDEFAULTOPTIONALTALENT().remove(talentcomponent.getTalent());
				}
				updateTalents();
			}
		});

		JLabel lblTalent = new JLabel(NLS.getString("DefaultOptionalTalent.lblTalent.text"));
		JLabel lblDiscipline = new JLabel(NLS.getString("DefaultOptionalTalent.lblDiscipline.text"));
		JLabel lblCircle = new JLabel(NLS.getString("DefaultOptionalTalent.lblCircle.text"));

		List<String> talentnames = new ArrayList<String>();
		for(CAPABILITYType cap : PROPERTIES.getCapabilities().getTalents()) {
			String name=cap.getName();
			if( ! talentnames.contains(name) ) talentnames.add(name);
		}
		Collections.sort(talentnames);
		cbxTalent.setModel(new DefaultComboBoxModel<String>(talentnames.toArray(new String[1])));
		cbxDiscipline.removeAllItems();
		for(int i=1; i<10;i++) cbxDiscipline.addItem(i);
		cbxCircle.removeAllItems();
		for(int i=1; i<=15;i++) cbxCircle.addItem(i);

		chosenTalents = new DefaultListModel<de.earthdawn.config.DefaultOptionalTalent>();
		chosenTalentList = new JList<de.earthdawn.config.DefaultOptionalTalent>(chosenTalents);
		JTextArea description = new JTextArea("Description");
		description.setLineWrap(true);
		description.setEditable(false);
		description.setOpaque(false);
		description.setText(NLS.getString("DefaultOptionalTalent.Description.text"));
		description.setPreferredSize(new Dimension(100,50));

		getContentPane().setLayout(new MigLayout("", "[50px,fill][50px,fill][100px:n,grow,fill]", "[50px:n,fill][fill][fill][fill][fill][fill][fill][grow,fill][fill]"));
		getContentPane().add(description, "cell 0 0 3 1,grow");
		getContentPane().add(lblDiscipline,   "cell 0 1,alignx left,aligny bottom");
		getContentPane().add(lblCircle,       "cell 1 1,alignx left,aligny bottom");
		getContentPane().add(cbxDiscipline,   "cell 0 2");
		getContentPane().add(cbxCircle,       "cell 1 2");
		getContentPane().add(lblTalent,       "cell 0 3,alignx left,aligny bottom");
		getContentPane().add(cbxTalent,       "cell 0 4 2 1,grow");
		getContentPane().add(btnInsertTalent, "cell 0 5 2 1,grow");
		getContentPane().add(btnRemoveTalent, "cell 0 6 2 1,grow");
		getContentPane().add(btnClose,        "cell 0 8 2 1,grow");
		getContentPane().add(chosenTalentList, "cell 2 1 1 8,grow");

		updateTalents();
	}

	public void updateTalents() {
		chosenTalents.clear();
		for( OPTIONALRULESDEFAULTOPTIONALTALENT talent : PROPERTIES.getOptionalRules().getDEFAULTOPTIONALTALENT() ) {
			if( ! talent.getLang().equals(LANGUAGE) ) continue;
			de.earthdawn.config.DefaultOptionalTalent talentcomponent = new de.earthdawn.config.DefaultOptionalTalent(talent);
			chosenTalents.addElement(talentcomponent);
		}
	}
}
