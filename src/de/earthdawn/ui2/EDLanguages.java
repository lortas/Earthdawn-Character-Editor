package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.earthdawn.CharacterContainer;
import de.earthdawn.LanguageContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.CHARACTERLANGUAGEType;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.LearnedbyType;

public class EDLanguages extends JPanel {
	private static final long serialVersionUID = -3029891477607441807L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private CharacterContainer character;
	private JTable tableLanguages;
	private JTable tableLanguageSkills;
	private JButton btnAddLanguage;
	private JButton btnRemoveLanguages;
	private BufferedImage backgroundimage = null;

	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((LanguagesTableModel)tableLanguages.getModel()).setCharacter(character);
		JComboBox<String> comboBoxLearnedby = new JComboBox<String>();
		for( LearnedbyType item : LearnedbyType.values() ) comboBoxLearnedby.addItem(item.value());
		tableLanguages.getColumnModel().getColumn(1).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxLearnedby));
		tableLanguages.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxLearnedby));
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("languageselection") ) {
				TableColumn col = tableLanguages.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout languageselection : "+e.getLocalizedMessage());
		}
		((SkillsTableModel)tableLanguageSkills.getModel()).setCharacter(character);
		JComboBox<Integer> comboBoxZeroToTen = new JComboBox<Integer>();
		for(int i=0; i<=10;i++) comboBoxZeroToTen.addItem(i);
		JComboBox<Integer> comboBoxZeroToSix = new JComboBox<Integer>();
		for(int i=0; i<=6;i++) comboBoxZeroToSix.addItem(i);
		tableLanguageSkills.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToSix));
		tableLanguageSkills.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToTen));
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("skillselection") ) {
				TableColumn col = tableLanguageSkills.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout skillselection : "+e.getLocalizedMessage());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/languages.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}

	public EDLanguages() {
		setOpaque(false);
		setLayout(new GridBagLayout());

		JToolBar toolBar = new JToolBar();
		toolBar.setOpaque(false);

		btnAddLanguage = new JButton("Add Language");
		btnAddLanguage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddLanguage_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddLanguage);

		btnRemoveLanguages = new JButton("Remove Languages");
		btnRemoveLanguages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveLanguages_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveLanguages);

		tableLanguages = new JTable(){
			private static final long serialVersionUID = 5610503410969779287L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer(renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		tableLanguages.setOpaque(false);
		tableLanguages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableLanguages.setModel(new LanguagesTableModel(character));
		tableLanguages.setRowSelectionAllowed(false);
		tableLanguages.setColumnSelectionAllowed(false);
		tableLanguages.getTableHeader().setReorderingAllowed(false);
		InputMapUtil.setupInputMap(tableLanguages);
		JComboBox<String> comboBoxLearnedby = new JComboBox<String>();
		for( LearnedbyType item : LearnedbyType.values() ) comboBoxLearnedby.addItem(item.value());
		tableLanguages.getColumnModel().getColumn(1).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxLearnedby));
		tableLanguages.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxLearnedby));

		tableLanguageSkills = new JTable(){
			private static final long serialVersionUID = 7932454339380763724L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer(renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		tableLanguageSkills.setOpaque(false);
		tableLanguageSkills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableLanguageSkills.setModel(new SkillsTableModel(character,true));
		JComboBox<Integer> comboBoxZeroToTen = new JComboBox<Integer>();
		for(int i=0; i<=10;i++) comboBoxZeroToTen.addItem(i);
		JComboBox<Integer> comboBoxZeroToSix = new JComboBox<Integer>();
		for(int i=0; i<=6;i++) comboBoxZeroToSix.addItem(i);
		tableLanguageSkills.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToSix));
		tableLanguageSkills.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToTen));
		tableLanguageSkills.setRowSelectionAllowed(false);
		tableLanguageSkills.setColumnSelectionAllowed(false);
		tableLanguageSkills.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPaneLanguages = new JScrollPane();
		scrollPaneLanguages.setOpaque(false);
		scrollPaneLanguages.setViewportView(tableLanguages);
		scrollPaneLanguages.getViewport().setOpaque(false);

		JScrollPane southPanel = new JScrollPane();
		southPanel.setOpaque(false);
		southPanel.setViewportView(tableLanguageSkills);
		southPanel.getViewport().setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.BOTH;
		add(toolBar, gbc);

		gbc = (GridBagConstraints) gbc.clone();
		gbc.gridy = 1;
		gbc.weighty = 10;
		add(scrollPaneLanguages, gbc);

		gbc = (GridBagConstraints) gbc.clone();
		gbc.gridy = 2;
		gbc.weighty = 3;
		add(southPanel, gbc);
	}

	protected void do_btnAddLanguage_actionPerformed(ActionEvent arg0) {
		if( character == null ) return;
		CHARACTERLANGUAGEType l = new CHARACTERLANGUAGEType();
		l.setSpeak(LearnedbyType.SKILL);
		l.setReadwrite(LearnedbyType.NO);
		l.setLanguage("");
		character.getLanguages().insertLanguage(l);
		character.refesh();
	}

	protected void do_btnRemoveLanguages_actionPerformed(ActionEvent arg0) {
		if( character == null ) return;
		ArrayList<CHARACTERLANGUAGEType> expForRemoval = new ArrayList<CHARACTERLANGUAGEType> ();
		LanguageContainer languages = character.getLanguages();
		for(int row :tableLanguages.getSelectedRows()){
			CHARACTERLANGUAGEType language = languages.getLanguage(row);
			expForRemoval.add(language);
		}
		languages.removeAll(expForRemoval);	
		character.refesh();
	}
}

class LanguagesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -2330554075615304424L;
	private CharacterContainer character;
	private String[] columnNames = {"Language", "Speak learned by", "Read/Write learned by"};

	public LanguagesTableModel(CharacterContainer character) {
		super();
		this.character = character;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		fireTableStructureChanged();
	}

	public CharacterContainer getCharacter() {
		return character;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if( character == null) return 0;
		return character.getLanguages().size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		if( character == null ) return 0;
		CHARACTERLANGUAGEType language = character.getLanguages().getLanguage(row);
		switch (col) {
		case 0: return language.getLanguage();
		case 1: return language.getSpeak().value();
		case 2: return language.getReadwrite().value();
		default : return "Error not defined";
		}
	}

	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class<?> getColumnClass(int c) { return getValueAt(0, c).getClass(); }

	public boolean isCellEditable(int row, int col) {
		if( character == null ) return false;
		if( row < character.getLanguages().size() ) return true;
		return false;
	}

	public void setValueAt(Object value, int row, int col) { 
		if( character == null ) return;
		CHARACTERLANGUAGEType language = character.getLanguages().getLanguage(row);
		switch (col) {
		case 0:
			language.setLanguage((String)value);
			break;
		case 1:
			language.setSpeak(LearnedbyType.fromValue((String)value));
			break;
		case 2:
			language.setReadwrite(LearnedbyType.fromValue((String)value));
			break;
		}
		character.refesh();
	}
}
