package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ACCOUNTINGType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.PlusminusType;
import de.earthdawn.data.YesnoType;

public class EDArmor extends JPanel {

	/**
	 * Create the panel.
	 */
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddArmor;
	private JButton btnRemoveArmor;
	
	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		
		this.character = character;
		((ArmorTableModel)table.getModel()).setCharacter(character);
	}

	public EDArmor() {
		
		setLayout(new BorderLayout(0, 0));
		
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		
		btnAddArmor = new JButton("Add Item");
		btnAddArmor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddArmor_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddArmor);
		
		btnRemoveArmor = new JButton("Remove Item");
		btnRemoveArmor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveArmor_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveArmor);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		InputMapUtil.setupInputMap(table);	
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new ArmorTableModel(character));
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(false);
	}
	
	protected void do_btnAddArmor_actionPerformed(ActionEvent arg0) {
		ITEMType item = new ITEMType();
		item.setLocation(new String("self"));
		item.setName(new String(""));
		item.setWeight(new Float("0").floatValue());
		item.setUsed(YesnoType.NO);
		character.getItems().add(item);
		character.refesh();
	}
	
	protected void do_btnRemoveArmor_actionPerformed(ActionEvent arg0) {
		ArrayList<ITEMType> itemsForRemoval = new ArrayList<ITEMType> ();
		for(int row :table.getSelectedRows()){
			ITEMType item = character.getItems().get(row);
			itemsForRemoval.add(item);
		}
		character.getItems().removeAll(itemsForRemoval);	
		character.refesh();
	}
}

class ArmorTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Name", "Weight",  "Location", "Used"};


    
    

	public ArmorTableModel(CharacterContainer character) {
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
        // {"Name", "Weight",  "Location", "Used"}
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getItems().get(row).getName();
	    		case 1: return character.getItems().get(row).getWeight();
	    		case 2: return character.getItems().get(row).getLocation();
	    		case 3: if (character.getItems().get(row).getUsed().equals(YesnoType.YES)) {
	    					return new Boolean(true);
	    				}
	    				else{
	    					return new Boolean(false);
	    				}
	    		
	
	    		default : return new String("Error not defined");
	    	}
        }
	    else{
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
    	// {"Name", "Weight",  "Location", "Used"}
    	switch (col) {    		
			case 0:character.getItems().get(row).setName((String)value); break;
			case 1: character.getItems().get(row).setWeight(((Float)value).floatValue());  break;
			case 2: character.getItems().get(row).setLocation((String)value);  break;
			case 3: if (((Boolean)value)) {
						System.out.println("true");
						character.getItems().get(row).setUsed(YesnoType.YES);
					}
					else{
						System.out.println("false");
						character.getItems().get(row).setUsed(YesnoType.NO);
					}
					break;
			
    	}
    	character.refesh();	
    }

}

