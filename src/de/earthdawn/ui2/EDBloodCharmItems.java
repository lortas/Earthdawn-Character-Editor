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
import de.earthdawn.data.BLOODCHARMITEMType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.PlusminusType;
import de.earthdawn.data.YesnoType;

public class EDBloodCharmItems extends JPanel {

	/**
	 * Create the panel.
	 */
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddBloodCharm;
	private JButton btnRemoveBloodCharm;
	
	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		
		this.character = character;
		((BloodCharmItemTableModel)table.getModel()).setCharacter(character);
	}

	public EDBloodCharmItems() {
		
		setLayout(new BorderLayout(0, 0));
		
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		
		btnAddBloodCharm = new JButton("Add Item");
		btnAddBloodCharm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddBloodCharm_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddBloodCharm);
		
		btnRemoveBloodCharm = new JButton("Remove Item");
		btnRemoveBloodCharm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveBloodCharm_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveBloodCharm);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		InputMapUtil.setupInputMap(table);	
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new BloodCharmItemTableModel(character));
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(false);
	}
	
	protected void do_btnAddBloodCharm_actionPerformed(ActionEvent arg0) {
		BLOODCHARMITEMType charm = new BLOODCHARMITEMType();
		charm.setLocation(new String("self"));
		charm.setName(new String(""));
		charm.setWeight(new Float("0").floatValue());
		
		charm.setEffect(new String("+ ?"));
		charm.setBlooddamage(new Integer(0));
		charm.setDepatterningrate(new Integer(0));
		charm.setEnchantingdifficultynumber(new Integer(0));
		
		charm.setUsed(YesnoType.YES);
		
		character.getBloodCharmItem().add(charm);
		character.refesh();
	}
	
	protected void do_btnRemoveBloodCharm_actionPerformed(ActionEvent arg0) {
		ArrayList<BLOODCHARMITEMType> itemsForRemoval = new ArrayList<BLOODCHARMITEMType> ();
		for(int row :table.getSelectedRows()){
			BLOODCHARMITEMType item = character.getBloodCharmItem().get(row);
			itemsForRemoval.add(item);
		}
		character.getBloodCharmItem().removeAll(itemsForRemoval);	
		character.refesh();
	}
}

class BloodCharmItemTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Name", "Weight",  "Location", "Blooddamage", "Effect", "Depatterningrate", "Enchantingdifficultynumber", "Used"};


    
    

	public BloodCharmItemTableModel(CharacterContainer character) {
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
    		return character.getBloodCharmItem().size();
    	}
    	else{
    		return 0;
    	}
    		
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        // {"Name", "Weight",  "Location", "Blooddamage", "Effect", "Depatterningrate", "Enchantingdifficultynumber", "Used"};
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getBloodCharmItem().get(row).getName();
	    		case 1: return character.getBloodCharmItem().get(row).getWeight();
	    		case 2: return character.getBloodCharmItem().get(row).getLocation();
	    		case 3: return character.getBloodCharmItem().get(row).getBlooddamage();
	    		case 4: return character.getBloodCharmItem().get(row).getEffect();
	    		case 5: return character.getBloodCharmItem().get(row).getDepatterningrate();
	    		case 6: return character.getBloodCharmItem().get(row).getEnchantingdifficultynumber();
	    		case 7: if (character.getItems().get(row).getUsed().equals(YesnoType.YES)) {
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
    	// {"Name", "Weight",  "Location", "Blooddamage", "Effect", "Depatterningrate", "Enchantingdifficultynumber", "Used"};
    	switch (col) {    		
			case 0: character.getBloodCharmItem().get(row).setName((String)value); break;
			case 1: character.getBloodCharmItem().get(row).setWeight(((Float)value).floatValue());  break;
			case 2: character.getBloodCharmItem().get(row).setLocation((String)value);  break;
			
			case 3: character.getBloodCharmItem().get(row).setBlooddamage((Integer)value);  break;
			case 4: character.getBloodCharmItem().get(row).setEffect((String)value);  break;
			case 5: character.getBloodCharmItem().get(row).setDepatterningrate((Integer)value);  break;
			case 6: character.getBloodCharmItem().get(row).setEnchantingdifficultynumber((Integer)value);  break;
			case 7: if (((Boolean)value)) {
						System.out.println("true");
						character.getBloodCharmItem().get(row).setUsed(YesnoType.YES);
					}
					else{
						System.out.println("false");
						character.getBloodCharmItem().get(row).setUsed(YesnoType.NO);
					}
					break;
			
    	}
    	character.refesh();	
    }

}

