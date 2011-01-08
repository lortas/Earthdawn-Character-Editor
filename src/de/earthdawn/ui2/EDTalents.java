package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBElement;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.TALENTSType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.event.CharChangeEventListener;

public class EDTalents extends JPanel {
	
	private CharacterContainer character;
	
	private JScrollPane scrollPane;
	private JTable table;
	
	public void setCharacter(final CharacterContainer character) {
		this.character = character;
		((TalentsTableModel)table.getModel()).setCharacter(character);
		this.character.addCharChangeEventListener(new CharChangeEventListener() {
			@Override
			public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
				System.out.println("Test");
				((TalentsTableModel)table.getModel()).setCharacter(character);

			}
		}
		);
	}

	public CharacterContainer getCharacter() {
		return character;
	}	
	

	/**
	 * Create the panel.
	 */
	public EDTalents() {
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setModel(new TalentsTableModel(character));
		scrollPane.setViewportView(table);

	}
}

class TalentsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private HashMap<Integer,TALENTSType> allTalents;
    private String[] columnNames = {"Circle", "Talentname", "Strain", "Attribute", "Rank", "Step", "Action", "Dice", "Type"};




	public TalentsTableModel(CharacterContainer character) {
		super();
		this.character = character;

	}

	
    
  
	public void setCharacter(CharacterContainer character) {
		this.character = character;
		if (character != null){
			allTalents = character.getAllTalentsByDisziplinOrder();
		}
		else{
			allTalents = null;
		}
		for( Integer disciplinenumber : allTalents.keySet() ) {
			System.out.println(disciplinenumber);
		}
		fireTableStructureChanged();
	}

	public CharacterContainer getCharacter() {
		return character;
	}	

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        if(character == null){
        	System.out.println("character is null");
        	return 0;
        }
        if (allTalents.size() == 0){
        	return 0;
        }
        System.out.println("Size" + allTalents.size());
    	return allTalents.get(1).getDISZIPLINETALENTOrOPTIONALTALENT().size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	JAXBElement<TALENTType> element = allTalents.get(1).getDISZIPLINETALENTOrOPTIONALTALENT().get(row);
    
    	Object result = new String("Error"); 
    	TALENTType talent = element.getValue();
    	switch (col) {
	        case 0:  
	        	try{
	        		result = talent.getCircle();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 1:  
	        	try{
	        		result = talent.getName();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 2:
	        	try{
	        		result = talent.getStrain();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 3: 
	        	try{
	        		result = talent.getAttribute().value();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 4: 
	        	try{
	        		result = talent.getRANK().getRank();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;	        	
	        case 5:
	        	try{
	        		result = talent.getRANK().getStep();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 6:
	        	try{
	        		result = talent.getAction().value();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;	 	        	
	        case 7:
	        	try{
	        		result = talent.getRANK().getDice().value();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;	 	        	
	        case 8:
	        	try{
	        		result = element.getName().getLocalPart();
	        	}
	        	catch(Exception e){
	        		System.err.println("Error: " + talent.getName());
	        	}
	        	break;	 	        	
	        default: return new Integer(0);
        }

    	return result;
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
    	return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {       
        fireTableCellUpdated(row, col);
        fireTableCellUpdated(row, 4);
        fireTableCellUpdated(row, 5);
    }

}
