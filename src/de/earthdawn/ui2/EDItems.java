package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.ItemkindType;
import de.earthdawn.data.YesnoType;

public class EDItems extends JPanel {
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddItem;
	private JButton btnRemoveItem;

	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((ItemTableModel)table.getModel()).setCharacter(character);
		JComboBox comboBoxPlusMinus = new JComboBox();
		for( ItemkindType kind : ItemkindType.values() ) comboBoxPlusMinus.addItem(kind.value());
		table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
	}

	public EDItems() {
		setLayout(new BorderLayout(0, 0));
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddItem_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddItem);
		
		btnRemoveItem = new JButton("Remove Item");
		btnRemoveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveItem_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveItem);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		InputMapUtil.setupInputMap(table);	
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new ItemTableModel(character));
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(false);
		JComboBox comboBoxPlusMinus = new JComboBox();
		for( ItemkindType kind : ItemkindType.values() ) comboBoxPlusMinus.addItem(kind.value());
		table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
	}

	protected void do_btnAddItem_actionPerformed(ActionEvent arg0) {
		ITEMType item = new ITEMType();
		item.setLocation(new String("self"));
		item.setName(new String(""));
		item.setWeight(new Float("0").floatValue());
		item.setUsed(YesnoType.NO);
		character.getItems().add(item);
		character.refesh();
	}
	
	protected void do_btnRemoveItem_actionPerformed(ActionEvent arg0) {
		ArrayList<ITEMType> itemsForRemoval = new ArrayList<ITEMType> ();
		for(int row :table.getSelectedRows()){
			ITEMType item = character.getItems().get(row);
			itemsForRemoval.add(item);
		}
		character.getItems().removeAll(itemsForRemoval);	
		character.refesh();
	}
}

class ItemTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Name", "Weight",  "Location", "Used", "Type"};

	public ItemTableModel(CharacterContainer character) {
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
    	if(character != null){
    		return character.getItems().size();
    	}
    	else{
    		return 0;
    	}
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

	public Object getValueAt(int row, int col) {
		// {"Name", "Weight",  "Location", "Used", "Type" }
		if(character != null){ 
			switch (col) {
				case 0: return character.getItems().get(row).getName();
				case 1: return character.getItems().get(row).getWeight();
				case 2: return character.getItems().get(row).getLocation();
				case 3: if (character.getItems().get(row).getUsed().equals(YesnoType.YES)) {
							return new Boolean(true);
						} else{
							return new Boolean(false);
						}
				case 4: return character.getItems().get(row).getKind().value();
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
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	public void setValueAt(Object value, int row, int col) { 
		// {"Name", "Weight",  "Location", "Used"}
		switch (col) {    		
			case 0:character.getItems().get(row).setName((String)value); break;
			case 1: character.getItems().get(row).setWeight(((Float)value).floatValue());  break;
			case 2: character.getItems().get(row).setLocation((String)value);  break;
			case 3: if( ((Boolean)value) ) {
						character.getItems().get(row).setUsed(YesnoType.YES);
					} else{
						character.getItems().get(row).setUsed(YesnoType.NO);
					}
					break;
			case 4: character.getItems().get(row).setKind(ItemkindType.fromValue((String)value));
		}
		character.refesh();	
	}
}
