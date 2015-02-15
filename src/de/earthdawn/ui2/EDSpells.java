package de.earthdawn.ui2;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.DISCIPLINESPELLType;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.SPELLDEFType;
import de.earthdawn.data.SPELLType;
import de.earthdawn.data.SpellkindType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.data.YesnoType;

public class EDSpells extends JPanel {
	private static final long serialVersionUID = 3430848422226809963L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private String disciplin;
	private CharacterContainer character;
	private JScrollPane scrollPane;
	private JTable table;
	private BufferedImage backgroundimage = null;
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
		try {
			((SpellsTableModel)table.getModel()).generateLists(threadweavings);
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("spellselection") ) {
				TableColumn col = table.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout spellselection : "+e.getLocalizedMessage());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/spells.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}
	/**
	 * Create the panel.
	 */
	public EDSpells(CharacterContainer character,String disciplin) {
		this.character = character;
		this.disciplin = disciplin;
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane);

		List<TALENTType> threadweavings = character.getThreadWeavingTalents().get(disciplin);
		table = new JTable(){
			private static final long serialVersionUID = 4544018746309317528L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer(renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		table.setOpaque(false);
		InputMapUtil.setupInputMap(table);
		table.setModel(new SpellsTableModel(character,disciplin,threadweavings));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		sorter = new TableRowSorter<SpellsTableModel>((SpellsTableModel) table.getModel());
		table.setRowSorter(sorter);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);

		List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
	}

	public String getDisciplin() {
		return disciplin;
	}

	public void setDisciplin(String disciplin) {
		this.disciplin = disciplin;
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
	private String[] columnNames = {"Learned", "Free", "Circle", "Element", "Name", "Castingdifficulty", "Threads", "Weavingdifficulty", "Reattuningdifficulty", "Effect", "Effectarea","Range", "Duration"};

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
		if( threadweavings != null) for(TALENTType threadweaving : threadweavings ) threadweavingTypes.addAll(threadweaving.getLIMITATION());
		Map<String, List<List<DISCIPLINESPELLType>>> spellsByDiscipline = PROPERTIES.getSpellsByDiscipline();
		Map<String, SPELLDEFType> spells = PROPERTIES.getSpells();
		Map<SpellkindType, String> spellKindMap = PROPERTIES.getSpellKindMap();
		int maxCircle=character.getCircleOf(this.discipline);
		for( String disciplinename : spellsByDiscipline.keySet() ) {
			int circlenr = 0;
			for( List<DISCIPLINESPELLType> disciplineSpells : spellsByDiscipline.get(disciplinename)) {
				circlenr++;
				if(circlenr>maxCircle) break;
				for( DISCIPLINESPELLType disciplineSpell : disciplineSpells ) {
					// Wenn der SpruchType nicht bekannt ist, dann Überspringe das Einfügen
					if( disciplineSpell.getType() == null) continue;
					String spellKind=spellKindMap.get(disciplineSpell.getType());
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
					spell.setElement(spelldef.getElement());
					spelllist.add(spell);
				}
			}
		}
		if( spelllist.isEmpty() ) {
			throw new IndexOutOfBoundsException("No Spells for the thread waving talent(s): "+threadweavingTypes.toString());
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
			case 1:
				if(character == null) return false;
				else return character.hasSpellLearnedBySpellability(discipline, spelllist.get(row) );
			case 2: return spelllist.get(row).getCircle();
			case 3: return spelllist.get(row).getElement().value();
			case 4: return spelllist.get(row).getName();
			case 5: return spelllist.get(row).getCastingdifficulty();
			case 6: return spelllist.get(row).getThreads();
			case 7: return spelllist.get(row).getWeavingdifficulty();
			case 8: return spelllist.get(row).getReattuningdifficulty();
			case 9: return spelllist.get(row).getEffect();
			case 10: return spelllist.get(row).getEffectarea();
			case 11: return spelllist.get(row).getRange();
			case 12: return spelllist.get(row).getDuration();
			default : return new String("Error not defined");
		}
	}

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
	public Class<?> getColumnClass(int c) { return getValueAt(0, c).getClass(); }

	public boolean isCellEditable(int row, int col) {
		if( (col==0) || (col==1) ) return true;
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		if( character == null ) return;
		SPELLType spell = spelllist.get(row);
		if( character.hasSpellLearned(discipline, spell) ) {
			if( col == 1 ) character.toggleSpellLearnedBySpellability(discipline, spell);
			else character.removeSpell(discipline, spell);
		} else {
			if( col == 1 ) spell.setByspellability(YesnoType.YES);
			character.addSpell(discipline, spell);
		}
		character.refesh();
		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
	}
}
