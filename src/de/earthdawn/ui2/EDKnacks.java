package de.earthdawn.ui2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.KNACKBASEType;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.TALENTType;

public class EDKnacks extends JPanel {
	private static final long serialVersionUID = 3430848422226809963L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private CharacterContainer character;
	private JScrollPane scrollPane;
	private JTable table;
	private BufferedImage backgroundimage = null;
	TableRowSorter<KnacksTableModel> sorter;

	public CharacterContainer getCharacter() {
		return character;
	}
	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((KnacksTableModel)table.getModel()).setCharacter(character);
		refresh();
	}

	public void refresh() {
		try {
			((KnacksTableModel)table.getModel()).generateLists();
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("knackselection") ) {
				TableColumn col = table.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout knackselection : "+e.getLocalizedMessage());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/knacks.jpg");
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
	public EDKnacks(CharacterContainer character) {
		this.character = character;
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane);

		table = new JTable(){
			private static final long serialVersionUID = -1156606156873620781L;
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
		table.setModel(new KnacksTableModel(character));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setRowSorter(sorter);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
	}
}

class KnacksTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -2103405769857336996L;
	private CharacterContainer character;
	public final ApplicationProperties PROPERTIES = ApplicationProperties.create();
	private String[] columnNames = {"Learned", "Knack Name", "Limitation", "Talent Name", "Rank", "Strain", "BookRef"};

	HashMap<String,KNACKBASEType> knacklist;
	List<String> knacknames;

	public KnacksTableModel(CharacterContainer character) {
		super();
		this.character = character;
		generateLists();
	}

	public void generateLists(){
		knacklist = new HashMap<String,KNACKBASEType>();
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			for( TALENTType talent : discipline.getDISZIPLINETALENT() ) {
				int talentrank=talent.getRANK().getRank()+2;
				String limitation = talent.getLimitation();
				for( KNACKBASEType knack : PROPERTIES.getTalentKnacks(talent.getName()) ) {
					if( (knack.getLimitation().isEmpty()||knack.getLimitation().equals(limitation)) && knack.getMinrank() <= talentrank ) knacklist.put(CharacterContainer.getFullTalentname(knack), knack);
				}
			}
			for( TALENTType talent : discipline.getOPTIONALTALENT() ) {
				int talentrank=talent.getRANK().getRank();
				String limitation = talent.getLimitation();
				for( KNACKBASEType knack : PROPERTIES.getTalentKnacks(talent.getName()) ) {
					if( (knack.getLimitation().isEmpty()||knack.getLimitation().equals(limitation)) && knack.getMinrank() <= talentrank ) knacklist.put(CharacterContainer.getFullTalentname(knack), knack);
				}
			}
		}
		knacknames = new ArrayList<String>();
		for( String name : new TreeSet<String>(knacklist.keySet()) ) knacknames.add(name);
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
		return knacknames.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		KNACKBASEType knack = knacklist.get(knacknames.get(row));
		switch (col) {
			case 0:
				if(character == null) return false;
				else return character.hasKnackLearned(knack);
			case 1: return knack.getName();
			case 2: return knack.getLimitation();
			case 3: return knack.getBasename();
			case 4: return knack.getMinrank();
			case 5: return knack.getStrain();
			case 6: return knack.getBookref();
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
		if( col == 0 ) return true;
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		if( character == null ) return;
		KNACKBASEType knack = knacklist.get(knacknames.get(row));
		if( character.hasKnackLearned(knack) ) character.removeKnack(knack);
		else character.insertKnack(knack);
		character.refesh();
		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
	}
}
