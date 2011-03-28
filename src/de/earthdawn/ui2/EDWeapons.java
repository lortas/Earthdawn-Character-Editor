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
import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.YesnoType;

public class EDWeapons extends JPanel {

	/**
	 * Create the panel.
	 */
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
		((WeaponTableModel)table.getModel()).setCharacter(character);
	}

	public EDWeapons() {
		
		setLayout(new BorderLayout(0, 0));
		
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		
		btnAddItem = new JButton("Add Weapon");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddItem_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddItem);
		
		btnRemoveItem = new JButton("Remove Weapon");
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
		table.setModel(new WeaponTableModel(character));
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(false);
	}
	
	protected void do_btnAddItem_actionPerformed(ActionEvent arg0) {
		WEAPONType weapon = new WEAPONType();
		weapon.setLocation(new String("self"));
		weapon.setName(new String(""));
		weapon.setWeight(new Float("0").floatValue());
		weapon.setUsed(YesnoType.YES);
		
		weapon.setDamagestep(1);
		weapon.setDateforged(new String(""));
		weapon.setSize(1);
		weapon.setStrengthmin(1);
		weapon.setTimesforged(0);
		
		weapon.setShortrange(0);
		weapon.setLongrange(0);
		character.getWeapons().add(weapon);
		character.refesh();
	}
	
	protected void do_btnRemoveItem_actionPerformed(ActionEvent arg0) {
		ArrayList<WEAPONType> weaponsForRemoval = new ArrayList<WEAPONType> ();
		for(int row :table.getSelectedRows()){
			WEAPONType item = character.getWeapons().get(row);
			weaponsForRemoval.add(item);
		}
		character.getWeapons().removeAll(weaponsForRemoval);	
		character.refesh();
	}
}

class WeaponTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Name", "Weight",  "Location", "Damagestep", "Size", "Strengthmin", "Timesforged", "Dateforged", "Shortrange", "Longrange", "Used"};


    
    

	public WeaponTableModel(CharacterContainer character) {
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
    		return character.getWeapons().size();
    	}
    	else{
    		return 0;
    	}
    		
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        // {"Name", "Weight",  "Location", "Damagestep", "Size", "Strengthmin", "Timesforged", "Dateforged", "Shortrange", "Longrange", "Used"};
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getWeapons().get(row).getName();
	    		case 1: return character.getWeapons().get(row).getWeight();
	    		case 2: return character.getWeapons().get(row).getLocation();
	    		case 3: return character.getWeapons().get(row).getDamagestep();
	    		case 4: return character.getWeapons().get(row).getSize();
	    		case 5: return character.getWeapons().get(row).getStrengthmin();
	    		case 6: return character.getWeapons().get(row).getTimesforged();
	    		case 7: return character.getWeapons().get(row).getDateforged();
	    		case 8: return character.getWeapons().get(row).getShortrange();
	    		case 9: return character.getWeapons().get(row).getLongrange();
	    		case 10: if (character.getItems().get(row).getUsed().equals(YesnoType.YES)) {
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
    	// {"Name", "Weight",  "Location", "Damagestep", "Size", "Strengthmin", "Timesforged", "Dateforged", "Shortrange", "Longrange", "Used"};
    	switch (col) {    		
			case 0: character.getWeapons().get(row).setName((String)value); break;
			case 1: character.getWeapons().get(row).setWeight(((Float)value).floatValue());  break;
			case 2: character.getWeapons().get(row).setLocation((String)value);  break;
			
			case 3: character.getWeapons().get(row).setDamagestep((Integer)value);  break;
			case 4: character.getWeapons().get(row).setSize((Integer)value);  break;
			case 5: character.getWeapons().get(row).setStrengthmin((Integer)value);  break;
			case 6: character.getWeapons().get(row).setTimesforged((Integer)value);  break;
			case 7: character.getWeapons().get(row).setDateforged((String)value);  break;
			case 8: character.getWeapons().get(row).setShortrange((Integer)value);  break;
			case 9: character.getWeapons().get(row).setLongrange((Integer)value);  break;
			
			
			case 10: if (((Boolean)value)) {
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

