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
import de.earthdawn.data.PlusminusType;

public class EDKarma extends JPanel {

	/**
	 * Create the panel.
	 */
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddKarmaEntry;
	private JButton btnRemoveKarmaEntry;
	
	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		
		this.character = character;
		((KarmaTableModel)table.getModel()).setCharacter(character);
		JComboBox comboBoxPlusMinus = new JComboBox();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));		
	}

	public EDKarma() {
		
		setLayout(new BorderLayout(0, 0));
		
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		
		btnAddKarmaEntry = new JButton("Add Karma");
		btnAddKarmaEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddKarmaEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddKarmaEntry);
		
		btnRemoveKarmaEntry = new JButton("Remove Karma");
		btnRemoveKarmaEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveKarmaEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveKarmaEntry);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		InputMapUtil.setupInputMap(table);	
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new KarmaTableModel(character));
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(false);
		
		JComboBox comboBoxPlusMinus = new JComboBox();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
	}
	
	protected void do_btnAddKarmaEntry_actionPerformed(ActionEvent arg0) {
		SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
		
		
		ACCOUNTINGType ac = new ACCOUNTINGType();
		ac.setType(PlusminusType.PLUS);
		ac.setWhen(format.format(new Date()));
		ac.setComment(new String(""));
		ac.setValue(0);
		character.getEDCHARACTER().getKARMA().getKARMAPOINTS().add(ac);
		character.refesh();
	}
	
	protected void do_btnRemoveKarmaEntry_actionPerformed(ActionEvent arg0) {
		ArrayList<ACCOUNTINGType> expForRemoval = new ArrayList<ACCOUNTINGType> ();
		for(int row :table.getSelectedRows()){
			ACCOUNTINGType karma =character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row);
			expForRemoval.add(karma);
		}
		character.getEDCHARACTER().getKARMA().getKARMAPOINTS().removeAll(expForRemoval);	
		character.refesh();
	}
}

class KarmaTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Date", "Comment",  "Type", "Value"};


    
    

	public KarmaTableModel(CharacterContainer character) {
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
    		return character.getEDCHARACTER().getKARMA().getKARMAPOINTS().size();
    	}
    	else{
    		return 0;
    	}
    		
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        // {"Date", "Comment",  "Type", "Value"}
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getWhen();
	    		case 1: return character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getComment();
	    		case 2: if (character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getType().equals(PlusminusType.PLUS)) {
	    					return new String("+");
	    				}
	    				else{
	    					return new String("-");
	    				}
	    		
	    		case 3: return new Integer(character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getValue());
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
    	 // {"Date", "Comment",  "Type", "Value"}
    	switch (col) {    		
			case 0: character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setWhen((String)value); break;
			case 1: character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setComment((String)value);  break;
			case 2: if (((String)value).equals("+")) {
						System.out.println("+");
						character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setType(PlusminusType.PLUS);
					}
					else{
						System.out.println("-");
						character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setType(PlusminusType.MINUS);
					}
					break;
			case 3: character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setValue(((Integer)value).intValue());  break;
    	}
    	character.refesh();	
    }

}

