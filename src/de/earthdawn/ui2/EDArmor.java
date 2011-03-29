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
import de.earthdawn.data.ARMORType;
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
		ARMORType armor = new ARMORType();
		armor.setLocation(new String("self"));
		armor.setName(new String(""));
		armor.setWeight(new Float("0").floatValue());
		
		armor.setPhysicalarmor(1);
		armor.setMysticarmor(1);
		armor.setTimesforgedMystic(0);
		armor.setTimesforgedPhysical(0);
		
		armor.setPenalty(0);
		armor.setEdn(0);
		armor.setDateforged("");
		
		
		armor.setUsed(YesnoType.YES);
		
		character.getProtection().getARMOROrSHIELD().add(armor);
		character.refesh();
	}
	
	protected void do_btnRemoveArmor_actionPerformed(ActionEvent arg0) {
		ArrayList<ARMORType> armorForRemoval = new ArrayList<ARMORType> ();
		for(int row :table.getSelectedRows()){
			ARMORType armor = character.getProtection().getARMOROrSHIELD().get(row);
			armorForRemoval.add(armor);
		}
		character.getProtection().getARMOROrSHIELD().removeAll(armorForRemoval);	
		character.refesh();
	}
}

class ArmorTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Name", "Weight",  "Location", "Physicalarmor", "Mysticarmor" , "Penalty", "Enchantingdifficultynumber", "TimesforgedPhysical", "TimesforgedMystic",  "Dateforged", "Used"};


    
    

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
    		return character.getProtection().getARMOROrSHIELD().size();
    	}
    	else{
    		return 0;
    	}
    		
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        // {"Name", "Weight",  "Location", "Physicalarmor", "Mysticarmor" , "Penalty", "Enchantingdifficultynumber", "TimesforgedPhysical", "TimesforgedMystic",  "Dateforged", "Used"}
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getProtection().getARMOROrSHIELD().get(row).getName();
	    		case 1: return character.getProtection().getARMOROrSHIELD().get(row).getWeight();
	    		case 2: return character.getProtection().getARMOROrSHIELD().get(row).getLocation();
	    		case 3: return character.getProtection().getARMOROrSHIELD().get(row).getPhysicalarmor();
	    		case 4: return character.getProtection().getARMOROrSHIELD().get(row).getMysticarmor();
	    		case 5: return character.getProtection().getARMOROrSHIELD().get(row).getPenalty();
	    		case 6: return character.getProtection().getARMOROrSHIELD().get(row).getEdn();
	    		case 7: return character.getProtection().getARMOROrSHIELD().get(row).getTimesforgedPhysical();
	    		case 8: return character.getProtection().getARMOROrSHIELD().get(row).getTimesforgedMystic();
	    		case 9: return character.getProtection().getARMOROrSHIELD().get(row).getDateforged();
	    		case 10: if (character.getProtection().getARMOROrSHIELD().get(row).getUsed().equals(YesnoType.YES)) {
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
    	// {"Name", "Weight",  "Location", "Physicalarmor", "Mysticarmor" , "Penalty", "Enchantingdifficultynumber", "TimesforgedPhysical", "TimesforgedMystic", "Dateforged", "Used"}
    	switch (col) {    		
			case 0: character.getProtection().getARMOROrSHIELD().get(row).setName((String)value); break;
			case 1: character.getProtection().getARMOROrSHIELD().get(row).setWeight(((Float)value).floatValue());  break;
			case 2: character.getProtection().getARMOROrSHIELD().get(row).setLocation((String)value);  break;
			case 3: character.getProtection().getARMOROrSHIELD().get(row).setPhysicalarmor((Integer)value);  break;
			case 4: character.getProtection().getARMOROrSHIELD().get(row).setMysticarmor((Integer)value);  break;
			case 5: character.getProtection().getARMOROrSHIELD().get(row).setPenalty((Integer)value);  break;
			case 6: character.getProtection().getARMOROrSHIELD().get(row).setEdn((Integer)value);  break;
			case 7: character.getProtection().getARMOROrSHIELD().get(row).setTimesforgedPhysical((Integer)value);  break;
			case 8: character.getProtection().getARMOROrSHIELD().get(row).setTimesforgedMystic((Integer)value);  break;
			case 9: character.getProtection().getARMOROrSHIELD().get(row).setDateforged((String)value);  break;
			
			case 10: if (((Boolean)value)) {
						System.out.println("true");
						character.getProtection().getARMOROrSHIELD().get(row).setUsed(YesnoType.YES);
					}
					else{
						System.out.println("false");
						character.getProtection().getARMOROrSHIELD().get(row).setUsed(YesnoType.NO);
					}
					break;
			
    	}
    	character.refesh();	
    }

}

