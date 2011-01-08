package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.util.HashMap;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBElement;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.TALENTSType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.event.CharChangeEventListener;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;

public class EDTalents extends JPanel {
	
	private CharacterContainer character;
	
	private JScrollPane scrollPane;
	private JTable table;
	private String disciplin;
	private JButton buttonAdd;
	private JPopupMenu popupMenuCircle;
	private JPopupMenu popupMenuTalent;
	
	public void setCharacter(final CharacterContainer character) {
		this.character = character;
		((TalentsTableModel)table.getModel()).setCharacter(character);
	}

	public CharacterContainer getCharacter() {
		return character;
	}	
	

	/**
	 * Create the panel.
	 */
	public EDTalents(String disciplin ) {
		this.disciplin  = disciplin;
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setModel(new TalentsTableModel(character, disciplin));
		scrollPane.setViewportView(table);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);
		
		buttonAdd = new JButton("Add optional Talent");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_buttonAdd_actionPerformed(arg0);
			}
		});
		toolBar.add(buttonAdd);
		
		popupMenuTalent = new JPopupMenu();
		toolBar.add(popupMenuTalent);
		
		popupMenuCircle = new JPopupMenu();
		toolBar.add(popupMenuCircle);

	}
	
	protected void do_buttonAdd_actionPerformed(ActionEvent arg0) {
		popupMenuCircle.removeAll();
		character.getO
		List<Integer> l = character.getCircleOfMissingOptionalTalents().get(disciplin);
		for(Integer c : l){
			JMenuItem menuItem = new JMenuItem(String.valueOf(c));
			popupMenuCircle.add(menuItem);	
			
		}
		
		popupMenuCircle.show(buttonAdd,buttonAdd.getX(), buttonAdd.getY()+ buttonAdd.getHeight());
	}
}

class TalentsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private TALENTSType talents;
    private String[] columnNames = {"Circle", "Talentname", "Strain", "Attribute", "Rank", "Step", "Action", "Dice", "Type"};
    private String disciplin = "";
    



	public String getDisciplin() {
		return disciplin;
	}


	public void setDisciplin(String diciplin) {
		this.disciplin = diciplin;
	}




	public TalentsTableModel(CharacterContainer character, String diciplin) {
		super();
		this.character = character;
		this.disciplin = diciplin;
		
	}

	
    
  
	public void setCharacter(CharacterContainer character) {
		this.character = character;
		if (character != null){
			talents =  character.getAllTalentsByDisziplinName().get(disciplin);
		}
		else{
			talents = null;
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
        if (talents  == null){
        	return 0;
        }
        return talents.getDISZIPLINETALENTOrOPTIONALTALENT().size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	JAXBElement<TALENTType> element = talents.getDISZIPLINETALENTOrOPTIONALTALENT().get(row);
    
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
