package de.earthdawn.ui2;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;

public class EDAttributes extends JPanel {
	
	private CharacterContainer character;
	
	private JScrollPane scrollPane;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public EDAttributes() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		scrollPane = new JScrollPane();
		add(scrollPane);

		table = new JTable();
		InputMapUtil.setupInputMap(table);
		table.setModel(new AttributesTableModel());
		table.getColumnModel().getColumn(2).setCellEditor(new SpinnerEditor(-2, 8));
		table.getColumnModel().getColumn(3).setCellEditor(new SpinnerEditor(0, 3));
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		scrollPane.setViewportView(table);
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((AttributesTableModel)table.getModel()).setCharacter(character);
	}

	public CharacterContainer getCharacter() {
		return character;
	}	
}

class AttributesTableModel extends AbstractTableModel {
	
	private CharacterContainer character;
	
    private String[] columnNames = {"Attribute", "Base", "Buy", "Circle", "Final", "Step"};
    private Object[][] data = {
    {"Dexterity"	, new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
    {"Strength"		, new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)},    
    {"Toughness"	, new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
    {"Perception"	, new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
    {"Willpower"	, new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
    {"Charisma"		, new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)},
    };
    
    private String[] columnKey = {"DEX", "STR", "TOU", "PER", "WIL", "CHA"};
    
  
	public void setCharacter(CharacterContainer character) {
		this.character = character;
	}

	public CharacterContainer getCharacter() {
		return character;
	}	

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	if (character == null){
    		return data[row][col];
    	}
    		
    
        switch (col) {
	        case 0:  return data[row][col]; 
	        case 1:  return new Integer(character.getAttributes().get(columnKey[row]).getRacevalue());
	        case 2:  return new Integer(character.getAttributes().get(columnKey[row]).getGenerationvalue());
	        case 3:  return new Integer(character.getAttributes().get(columnKey[row]).getLpincrease());
	        case 4:  return new Integer(character.getAttributes().get(columnKey[row]).getCurrentvalue());
	        case 5:  return new Integer(character.getAttributes().get(columnKey[row]).getStep());
	        default: return new Integer(0);
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
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if ((col == 2) || (col == 3)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        
        switch (col) {
	        case 2:  character.getAttributes().get(columnKey[row]).setGenerationvalue((Integer)value); break;
	        case 3:  character.getAttributes().get(columnKey[row]).setLpincrease((Integer)value); break;
	        default: break;
        }
        ECEWorker worker = new ECEWorker();
        worker.verarbeiteCharakter(character.getEDCHARACTER());
        character.refesh();
        fireTableCellUpdated(row, col);
        fireTableCellUpdated(row, 4);
        fireTableCellUpdated(row, 5);
    }

}


