package de.earthdawn.ui2;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
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
import de.earthdawn.data.RulesetversionType;
import de.earthdawn.data.KNACKBASECAPABILITYType;
import de.earthdawn.TalentsContainer;
import de.earthdawn.data.ATTRIBUTENameType;
import de.earthdawn.data.CapabilitytypeType;
import de.earthdawn.data.KNACKATTRIBUTEType;
import de.earthdawn.data.KNACKCAPABILITYType;
import de.earthdawn.data.KNACKDEFINITIONType;
import de.earthdawn.data.KNACKDISCIPLINEType;
import de.earthdawn.data.KNACKOTHERKNACKType;
import de.earthdawn.data.KNACKRACEType;
import de.earthdawn.data.KNACKType;

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
	private String[] columnNames = {"Learned", "Knack Name", "Attribute", "Rank", "Strain", "Talent", "Discipline", "BookRef"};

	class KnackTableEntry {
		public String disciplinename;
		public String talentname;
		public int disciplinecircle;
		public KNACKType knack;
		public KnackTableEntry(DISCIPLINEType discipline, TALENTType talent, KNACKDEFINITIONType knack) {
			this.disciplinename=discipline.getName();
			this.talentname=talent.getName();
			this.disciplinecircle=discipline.getCircle();
			this.knack=new KNACKType();
			this.knack.setAction(knack.getAction());
			this.knack.setAttribute(knack.getAttribute());
			this.knack.setBlood(knack.getBlood());
			this.knack.setBookref(knack.getBookref());
			this.knack.setLearnedbymincircle(disciplinecircle);
			this.knack.setName(knack.getName());
			this.knack.setStrain(knack.getStrain());
			List<String> limitations=talent.getLIMITATION();
			int talentrank=talent.getRANK().getRank();
			for( KNACKBASECAPABILITYType base : knack.getBASE() ) {
				if( ! base.getType().equals(CapabilitytypeType.TALENT) ) continue;
				if( ! base.getName().equals(this.talentname) ) continue;
				if( base.getMinrank() >= talentrank ) continue;
				String baselimitation=base.getLimitation();
				if( baselimitation.isEmpty() ) {
					this.knack.setMinrank(base.getMinrank());
				} else {
					for( String limitation : limitations ) {
						if( baselimitation.equals(limitation) ) {
							this.knack.setMinrank(base.getMinrank());
						}
					}
				}
			}
		}
	}
	List<KnackTableEntry> knacklist;

	public KnacksTableModel(CharacterContainer character) {
		super();
		this.character = character;
		generateLists();
	}

	private List<KnackTableEntry> getKnacksForTalents(List <TALENTType> talents, DISCIPLINEType discipline, boolean isdisciplinetalent) {
		List<KnackTableEntry> result = new ArrayList<>();
		for( TALENTType talent : talents ) {
			String talentname=talent.getName();
			int talentrank=talent.getRANK().getRank();
			// In ED3 discipline talents get knacks 2 ranks earlier.
			if( isdisciplinetalent && character.getRulesetversion().equals(RulesetversionType.ED_3) ) talentrank += 2;
			String[] talentlimitations = talent.getLIMITATION().toArray(new String[0]);
			if( talentlimitations.length == 0 ) {
				talentlimitations=new String[]{""};
			}
			for( String limitation : talentlimitations ) {
				for( KNACKDEFINITIONType knack : PROPERTIES.getTalentKnacks(talentname,limitation) ) {
					boolean match=false;
					for( KNACKBASECAPABILITYType base : knack.getBASE() ) {
						if( ! base.getType().equals(CapabilitytypeType.TALENT) ) continue;
						if( ! base.getName().equals(talentname) ) continue;
						if( ! ( limitation.isEmpty() || base.getLimitation().isEmpty() || base.getLimitation().equals(limitation) ) ) continue;
						if( base.getMinrank() >= talentrank ) continue;
						match=true;
					}
					if( ! match ) continue;
					for( KNACKATTRIBUTEType attr : knack.getATTRIBUTE() ) {
						int step=character.getAttributes().get(attr.getName()).getCurrentvalue();
						if( step < attr.getMin() ) match=false;
						if( step > attr.getMax() && attr.getMax() >= attr.getMin() ) match=false;
					}
					if( ! match ) continue;
					// If we have discipline limitations, they must match
					if( knack.getDISCIPLINE().size()>0 ) match=false;
					for( KNACKDISCIPLINEType dis : knack.getDISCIPLINE() ) {
						if( (dis.getName().equals("*") || dis.getName().equals(discipline.getName())) && dis.getCircle() <= discipline.getCircle() ) match=true;
					}
					if( ! match ) continue;
					// If we have other knacks requierd, they must match
					if( knack.getKNACK().size()>0 ) match=false;
					for( KNACKOTHERKNACKType k: knack.getKNACK() ) {
						if( character.getKnacksByName(k.getName()).length>0 ) match=true;
					}
					if( ! match ) continue;
					// If we have race requierd, they must match
					if( knack.getRACE().size()>0 ) match=false;
					for( KNACKRACEType r : knack.getRACE() ) {
						if( character.getRace().getName().equals(r.getName()) ) match=true;
					}
					if( ! match ) continue;
					// If we have additional talent requierd, they must match
					if( knack.getTALENT().size()>0 ) match=false;
					for( KNACKCAPABILITYType tal : knack.getTALENT() ) {
						for( TALENTType t : character.getTalentByName(tal.getName()) ) {
							int r=t.getRANK().getRank();
							match=( r >= tal.getMinrank() && ( r <= tal.getMaxrank() || tal.getMaxrank() < tal.getMinrank() ) );
						}
					}
					if( ! match ) continue;
					result.add(new KnackTableEntry(discipline,talent,knack));
				}
			}
		}
		return result;
	}

	public void generateLists(){
		knacklist = new ArrayList<>();
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			knacklist.addAll(getKnacksForTalents(discipline.getDISZIPLINETALENT(),discipline, true));
			knacklist.addAll(getKnacksForTalents(discipline.getOPTIONALTALENT(),discipline,  false));
			knacklist.addAll(getKnacksForTalents(discipline.getFREETALENT(),discipline,      false));
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
		return knacklist.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		KnackTableEntry knackentry = knacklist.get(row);
		switch (col) {
			case 0:
				if(character == null) return false;
				else return character.getKnacksByName(knackentry.knack.getName()).length>0;
			case 1: return knackentry.knack.getName();
			case 2: return knackentry.knack.getAttribute();
			case 3: return knackentry.knack.getMinrank();
			case 4: return knackentry.knack.getStrain();
			case 5: return knackentry.talentname;
			case 6: return knackentry.disciplinename;
			case 7: return knackentry.knack.getBookref();
			default : return "Error not defined";
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
		KnackTableEntry knack = knacklist.get(row);
		KNACKType[] learnedKnacks = character.getKnacksByName(knack.knack.getName());
		if( learnedKnacks.length>0 ) character.removeKnack(learnedKnacks);
		else character.insertKnack(knack.knack,knack.disciplinename,knack.talentname);
		character.refesh();
		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
	}
}
