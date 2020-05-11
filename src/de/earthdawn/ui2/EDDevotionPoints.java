package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ACCOUNTINGType;
import de.earthdawn.data.DEVOTIONType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.PlusminusType;

public class EDDevotionPoints extends JPanel {
	private static final long serialVersionUID = -9156447851725574501L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final String[] QuestorNames = PROPERTIES.getAllQuestorNames().toArray(new String[0]);
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddDevotionPointsEntry;
	private JButton btnRemoveDevotionPointsEntry;
	private JComboBox FieldPassion;
	private BufferedImage backgroundimage = null;

	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((DevotionPointsTableModel)table.getModel()).setCharacter(character);
		JComboBox<String> comboBoxPlusMinus = new JComboBox<String>();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));		
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("devotionpointselection") ) {
				TableColumn col = table.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout spellselection : "+e.getLocalizedMessage());
		}
		String passion=character.getPassion();
		int pos=QuestorNames.length-1;
		while( !( pos<0 || QuestorNames[pos].equals(passion) ) ) pos--;
		// The ComboBox Itemes are headed with an empty string, so the pos is +1
		// In case of no match the pos ends with -1 and we chose the empty string.
		FieldPassion.setSelectedIndex(pos+1);
}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/devotionpoints.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}

	public EDDevotionPoints() {
		setOpaque(false);
		setLayout(new BorderLayout(0,0));

		toolBar = new JToolBar();
		toolBar.setOpaque(false);
		add(toolBar, BorderLayout.NORTH);
		
		btnAddDevotionPointsEntry = new JButton("Add DevotionPoints");
		btnAddDevotionPointsEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddDevotionPointsEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddDevotionPointsEntry);

		btnRemoveDevotionPointsEntry = new JButton("Remove DevotionPoints");
		btnRemoveDevotionPointsEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveDevotionPointsEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveDevotionPointsEntry);
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		// Create transparent table
		table = new JTable(){
			private static final long serialVersionUID = -2243196651626466627L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer( renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		table.setOpaque(false);
		InputMapUtil.setupInputMap(table);	
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DevotionPointsTableModel(character));
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(false);
		scrollPane.getViewport().setOpaque(false);

		JComboBox<String> comboBoxPlusMinus = new JComboBox<String>();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
		JPanel southPanel= new JPanel();
		southPanel.setOpaque(false);
		add(southPanel,BorderLayout.SOUTH);
		southPanel.add(new JLabel("Passion:"));
		FieldPassion = new JComboBox<>(QuestorNames);
		FieldPassion.insertItemAt("",0);
		FieldPassion.setOpaque(false);
		FieldPassion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_FieldPassion_actionPerformed(arg0);
			}
		});
		southPanel.add(FieldPassion);
	}

	protected void do_FieldPassion_actionPerformed(ActionEvent arg0) {
		if( character == null) return;
		character.setPassion((String) FieldPassion.getSelectedItem());
		character.refesh();
	}

	protected void do_btnAddDevotionPointsEntry_actionPerformed(ActionEvent arg0) {
		ACCOUNTINGType ac = new ACCOUNTINGType();
		ac.setType(PlusminusType.PLUS);
		ac.setWhen(CharacterContainer.getCurrentDateTime());
		ac.setComment(new String(""));
		ac.setValue(0);

		DEVOTIONType devotion = character.getEDCHARACTER().getDEVOTION();
		if( devotion == null) {
			devotion = new DEVOTIONType();
			devotion.setValue(0);
			devotion.setPassion("--");
			character.getEDCHARACTER().setDEVOTION(devotion);
		}
		List<ACCOUNTINGType> devotionpoints = devotion.getDEVOTIONPOINTS();
		devotionpoints.add(ac);
		character.refesh();
	}

	protected void do_btnRemoveDevotionPointsEntry_actionPerformed(ActionEvent arg0) {
		ArrayList<ACCOUNTINGType> expForRemoval = new ArrayList<ACCOUNTINGType> ();
		List<ACCOUNTINGType> devotionpointsList = character.getEDCHARACTER().getDEVOTION().getDEVOTIONPOINTS();
		for(int row :table.getSelectedRows()){
			ACCOUNTINGType devotionPoints =devotionpointsList.get(row);
			expForRemoval.add(devotionPoints);
		}
		devotionpointsList.removeAll(expForRemoval);	
		character.refesh();
	}
}

class DevotionPointsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private String[] columnNames = {"Date", "Comment",  "Type", "Value"};

	public DevotionPointsTableModel(CharacterContainer character) {
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
		EDCHARACTER edcharacter = character.getEDCHARACTER();
		if( edcharacter == null) return 0;
		DEVOTIONType devotion = edcharacter.getDEVOTION();
		if( devotion == null) return 0;
		List<ACCOUNTINGType> devotionpoints = devotion.getDEVOTIONPOINTS();
		if( devotionpoints == null) return 0;
		return devotionpoints.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		// {"Date", "Comment",  "Type", "Value"}
		if(character != null){ 
			ACCOUNTINGType accounting = character.getEDCHARACTER().getDEVOTION().getDEVOTIONPOINTS().get(row);
			switch (col) {
				case 0: return accounting.getWhen();
				case 1: return accounting.getComment();
				case 2: if (accounting.getType().equals(PlusminusType.PLUS)) {
							return new String("+");
						} else{
							return new String("-");
						}
				
				case 3: return Integer.valueOf(accounting.getValue());
				default : return new String("Error not defined");
			}
		} else {
			return 0;
		}
	}

	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	
	
	public void setValueAt(Object value, int row, int col) { 
		ACCOUNTINGType accounting = character.getEDCHARACTER().getDEVOTION().getDEVOTIONPOINTS().get(row);
		// {"Date", "Comment",  "Type", "Value"}
		switch (col) {			
			case 0: accounting.setWhen((String)value); break;
			case 1: accounting.setComment((String)value);  break;
			case 2: if (((String)value).equals("+")) {
						System.out.println("+");
						accounting.setType(PlusminusType.PLUS);
					} else{
						System.out.println("-");
						accounting.setType(PlusminusType.MINUS);
					}
					break;
			case 3: accounting.setValue(((Integer)value).intValue());  break;
		}
		character.refesh();	
	}
}
