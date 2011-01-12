package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.SPELLType;
import de.earthdawn.data.SpellkindType;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class EDSpells extends JPanel {
	private CharacterContainer character;
	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private JTable table;
	private JLabel lblFilter;
	private JComboBox comboBox;
	TableRowSorter<SpellsTableModel> sorter;
	
	
	public CharacterContainer getCharacter() {
		return character;
	}
	public void setCharacter(CharacterContainer character) {
		this.character = character;
	}
	/**
	 * Create the panel.
	 */
	public EDSpells() {
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane);
		
		table = new JTable();
		table.setModel(new SpellsTableModel(character));
		scrollPane.setViewportView(table);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		sorter = new TableRowSorter<SpellsTableModel>((SpellsTableModel) table.getModel());
		table.setRowSorter(sorter);

		List <RowSorter.SortKey> sortKeys 
	    	= new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys); 


		
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		
		lblFilter = new JLabel("Filter     ");
		toolBar.add(lblFilter);
		
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_comboBox_itemStateChanged(arg0);
			}
		});
		
		
		Set<String> disciplineset = ApplicationProperties.create().getAllDisziplinNames();
		ArrayList<String> arrayList =  new ArrayList<String>();
		
		for ( SpellkindType spellkind : SpellkindType.values()){
			arrayList.add(0, spellkind.name());
		}
		arrayList.add(0, "All");
		String[] disciplinearray =  (String[]) arrayList.toArray(new String[arrayList.size()]);
		
		comboBox.setModel(new DefaultComboBoxModel(disciplinearray));
		toolBar.add(comboBox);

		


		
		
	}
	
	private void newFilter(String filter) {
		System.out.println("Filter: " + filter);
		RowFilter<SpellsTableModel, Object> rf = null;
	    //If current expression doesn't parse, don't update.
	    try {
	        rf = RowFilter.regexFilter( filter);
	    } catch (java.util.regex.PatternSyntaxException e) {
	    	  return;
	    }
	    sorter.setRowFilter(rf);
	}


	protected void do_comboBox_itemStateChanged(ItemEvent arg0) {
		String item = (String) arg0.getItem();
		if(item.equals("All")){
			newFilter("");
		}
		else{
			newFilter(item);
		}
	}
}



class SpellsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Learned",  "Circle", "Discipline", "Name", "Castingdifficulty", "Threads", "Weavingdifficulty", "Reattuningdifficulty", "Effect", "Effectarea","Range", "Duration"};

    List<SPELLType> spelllist;

    
    

	public SpellsTableModel(CharacterContainer character) {
		super();
		this.character = character;
		spelllist =  ApplicationProperties.create().getSpells4Grimoir();

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
        return spelllist.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	switch (col) {
        	case 0: return new Boolean(false);
        	case 1: return spelllist.get(row).getCircle();
        	case 2:	return spelllist.get(row).getType().name();
        	case 3: return spelllist.get(row).getName();
        	case 4: return spelllist.get(row).getCastingdifficulty();
        	case 5: return spelllist.get(row).getThreads();
        	case 6: return spelllist.get(row).getWeavingdifficulty();
        	case 7: return spelllist.get(row).getReattuningdifficulty();
        	case 8: return spelllist.get(row).getEffect();
        	case 9: return spelllist.get(row).getEffectarea();
        	case 10: return spelllist.get(row).getRange();
        	case 11: return spelllist.get(row).getDuration();
        	default : return new String("Error not defined");
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
    	if (col == 0 ){
    		return true;
    	}
    	return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {   
    	
    	
    	character.refesh();
    	
        fireTableCellUpdated(row, col);
    }

}
