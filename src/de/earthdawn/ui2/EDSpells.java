package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
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
import de.earthdawn.data.DISCIPLINESPELLType;
import de.earthdawn.data.SPELLDEFType;
import de.earthdawn.data.SPELLType;
import de.earthdawn.data.SpellkindType;
import de.earthdawn.data.TALENTType;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class EDSpells extends JPanel {
	private String disciplin;
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
		((SpellsTableModel)table.getModel()).setCharacter(character);
		refresh();
	}

	public void refresh() {
		List<TALENTType> threadweavings = character.getThreadWeavingTalents().get(disciplin);
		((SpellsTableModel)table.getModel()).generateLists(threadweavings);
	}

	/**
	 * Create the panel.
	 */
	public EDSpells(CharacterContainer character,String disciplin) {
		this.character = character;
		this.disciplin  = disciplin;
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		add(scrollPane);

		List<TALENTType> threadweavings = character.getThreadWeavingTalents().get(disciplin);
		table = new JTable();
		InputMapUtil.setupInputMap(table);
		table.setModel(new SpellsTableModel(character,disciplin,threadweavings));
		scrollPane.setViewportView(table);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		sorter = new TableRowSorter<SpellsTableModel>((SpellsTableModel) table.getModel());
		table.setRowSorter(sorter);

		List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
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

		for ( String dis : disciplineset) arrayList.add(dis);
		arrayList.add(0, "All");
		String[] disciplinearray = (String[]) arrayList.toArray(new String[arrayList.size()]);

		comboBox.setModel(new DefaultComboBoxModel(disciplinearray));
		toolBar.add(comboBox);
	}

	public String getDisciplin() {
		return disciplin;
	}

	public void setDisciplin(String disciplin) {
		this.disciplin = disciplin;
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
		if(item.equals("All")) newFilter("");
		else newFilter(item);
	}
}


class SpellsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private String discipline;
	public final ApplicationProperties PROPERTIES = ApplicationProperties.create();
	private String[] columnNames = {"Learned",  "Circle", "Type", "Name", "Castingdifficulty", "Threads", "Weavingdifficulty", "Reattuningdifficulty", "Effect", "Effectarea","Range", "Duration"};

	List<SPELLType> spelllist;

	public SpellsTableModel(CharacterContainer character, String discipline, List<TALENTType> threadweavings) {
		super();
		this.character = character;
		this.discipline = discipline;
		generateLists(threadweavings);
	}

	public void generateLists(List<TALENTType> threadweavings){
		spelllist = new ArrayList<SPELLType>();

		List<String> threadweavingTypes = new ArrayList<String>();
		for(TALENTType threadweaving : threadweavings ) threadweavingTypes.add(threadweaving.getLimitation());

		HashMap<String, List<List<DISCIPLINESPELLType>>> spellsByDiscipline = PROPERTIES.getSpellsByDiscipline();
		HashMap<String, SPELLDEFType> spells = PROPERTIES.getSpells();
		HashMap<SpellkindType, String> spellKindMap = PROPERTIES.getSpellKindMap();
		int maxCircle=character.getCircleOf(discipline);
		for( String discipline : spellsByDiscipline.keySet() ) {
			int circlenr = 0;
			for( List<DISCIPLINESPELLType> disciplineSpells : spellsByDiscipline.get(discipline)) {
				circlenr++;
				if(circlenr>maxCircle) break;
				for( DISCIPLINESPELLType disciplineSpell : disciplineSpells ) {
					String spellKind = spellKindMap.get(disciplineSpell.getType());
					// Wenn der SpruchType nicht bekannt ist, dann Überspringe das Einfügen
					if( spellKind == null ) continue;
					// Wenn der SpruchType nicht zu einem der FadenwebenTalente passt, dann Überspringe das Einfügen
					if( !threadweavingTypes.contains(spellKind)) continue;
					SPELLDEFType spelldef = spells.get(disciplineSpell.getName());
					if( spelldef == null ) {
						// Wenn es zu dem Zauberspruch keine Definition gibt, dann Überspringe das Einfügen
						System.err.println("Spell " + disciplineSpell.getName() + "(" + discipline +") not found!" );
						continue;
					}
					SPELLType spell = new SPELLType();
					spell.setCastingdifficulty(spelldef.getCastingdifficulty());
					spell.setCircle(circlenr);
					spell.setDuration(spelldef.getDuration());
					spell.setEffect(spelldef.getEffect());
					spell.setEffectarea(spelldef.getEffectarea());
					spell.setName(spelldef.getName());
					spell.setRange(spelldef.getRange());
					spell.setReattuningdifficulty(spelldef.getReattuningdifficulty());
					spell.setThreads(spelldef.getThreads());
					spell.setType(disciplineSpell.getType());
					spell.setWeavingdifficulty(spelldef.getWeavingdifficulty());

					spelllist.add(spell);
				}
			}
		}
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
			case 0:
				if(character == null) return false;
				else return character.hasSpellLearned(discipline, spelllist.get(row) );
			case 1: return spelllist.get(row).getCircle();
			case 2: return spelllist.get(row).getType().value();
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
		if( character == null ) return;
		if( character.hasSpellLearned(discipline, spelllist.get(row)) ) {
			character.removeSpell(discipline, spelllist.get(row));
		} else {
			character.addSpell(discipline, spelllist.get(row));
		}
		character.refesh();
		fireTableCellUpdated(row, col);
	}
}
