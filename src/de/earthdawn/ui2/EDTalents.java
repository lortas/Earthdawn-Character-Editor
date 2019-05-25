package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.earthdawn.CharacterContainer;
import de.earthdawn.TalentsContainer;
import de.earthdawn.TalentsContainer.TalentKind;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ATTRIBUTENameType;
import de.earthdawn.data.DISCIPLINE;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.SKILLType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.TALENTTEACHERType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.data.YesnoType;

public class EDTalents extends JPanel {

	private static final long serialVersionUID = -8850440306321140758L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();

	private CharacterContainer character;
	private JScrollPane scrollPane;
	private JTable table;
	private String disciplin;
	private JPopupMenu popupMenuCircle;
	private BufferedImage backgroundimage = null;

	public void setCharacter(final CharacterContainer character) {
		this.character = character;
		((TalentsTableModel)table.getModel()).setCharacter(character);
		JComboBox<Integer> comboBoxZeroToFithteen = new JComboBox<Integer>();
		for(int i=0; i<=15;i++) comboBoxZeroToFithteen.addItem(i);
		JComboBox<Integer> comboBoxZeroToThree = new JComboBox<Integer>();
		for(int i=0; i<=3;i++) comboBoxZeroToThree.addItem(i);
		table.getColumnModel().getColumn(0).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToFithteen));
		table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToThree));
		table.getColumnModel().getColumn(5).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToFithteen));
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("talentselection") ) {
				TableColumn col = table.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout talentselection : "+e.getLocalizedMessage());
		}
	}

	public CharacterContainer getCharacter() {
		return character;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/talents.jpg");
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
	public EDTalents(String disciplin) {
		this.disciplin  = disciplin;
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		// Create transperant table
		table = new JTable(){
			private static final long serialVersionUID = -4787240966765482373L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer( renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		table.setOpaque(false);
		table.setSurrendersFocusOnKeystroke(true);
		table.setModel(new TalentsTableModel(character, disciplin));
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setAutoCreateRowSorter(true);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);

		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);

		JButton button = new JButton("Add optional talent");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_buttonAddOptionalTalent_actionPerformed(arg0);
			}
		});
		toolBar.add(button);

		button = new JButton("Add talent by versatility");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_buttonAddVersalityTalent_actionPerformed(arg0);
			}
		});
		toolBar.add(button);

		button = new JButton("Add random optional talents");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_buttonAddRandomOptionalTalents_actionPerformed(arg0);
			}
		});
		toolBar.add(button);

		popupMenuCircle = new JPopupMenu();
		toolBar.add(popupMenuCircle);
	}

	public String getDisciplin() {
		return disciplin;
	}

	public void setDisciplin(String disciplin) {
		this.disciplin = disciplin;
	}

	protected void do_buttonAddOptionalTalent_actionPerformed(ActionEvent arg0) {
		popupMenuCircle.removeAll();
		JButton button = (JButton) arg0.getSource();
		List<Integer> l = character.getCircleOfMissingOptionalTalents().get(disciplin);
		for(Integer c : l){
			JMenuItem menuItem = new JMenuItem(String.valueOf(c));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					do_menuItemTalent_actionPerformed(arg0);
				}
			});
			popupMenuCircle.add(menuItem);
		}

		popupMenuCircle.show(button,button.getX(), button.getY()+ button.getHeight());
	}

	protected void do_buttonAddRandomOptionalTalents_actionPerformed(ActionEvent arg0) {
		character.fillOptionalTalentsRandom(disciplin);
		character.refesh();
	}

	protected void do_buttonAddVersalityTalent_actionPerformed(ActionEvent arg0) {
		// Wenn kein ungenutze Vielseitigkeit Räng vorhanden, dann abbrechen
		if( character.getUnusedVersatilityRanks() < 1 ) return;
		// Bestimme den höchsten Kreis aller erlernten Disziplinen
		int circle=1; for( int c : character.getDisciplineCircles() ) if( c > circle ) circle=c;
		JButton button = (JButton) arg0.getSource();
		EDCapabilitySelectDialog dialog = new EDCapabilitySelectDialog(EDCapabilitySelectDialog.SELECT_VERSATILITYTALENT,circle,new Rectangle(button.getLocationOnScreen().x, button.getLocationOnScreen().y+button.getHeight(), 450, 300));
		dialog.setSingleSelection(true);
		dialog.setVisible(true);
		Map<String, SKILLType> selected = dialog.getSelectedCapabilitytMap();
		for(String key : selected.keySet()){
			SKILLType cap = selected.get(key);
			if( cap.getLIMITATION().size()<1 ) {
				TALENTABILITYType talent = new TALENTABILITYType();
				talent.setName(cap.getName());
				character.addOptionalTalent(disciplin, circle, talent, true);
			} else for( String limitation : cap.getLIMITATION() ) {
				TALENTABILITYType talent = new TALENTABILITYType();
				talent.setName(cap.getName());
				if( !limitation.isEmpty() ) talent.setLimitation(limitation);
				character.addOptionalTalent(disciplin, circle, talent, true);
			}
			character.refesh();
		}
	}

	protected void do_menuItemTalent_actionPerformed(ActionEvent arg0) {
		JMenuItem source = ((JMenuItem)arg0.getSource());
		Point parentloc = this.getLocationOnScreen();
		int circle = Integer.valueOf(source.getText());
		DISCIPLINE d = ApplicationProperties.create().getDisziplin(disciplin);
		List<TALENTABILITYType> talentlist = character.getUnusedOptionalTalents(d,circle);
		EDCapabilitySelectDialog dialog = new EDCapabilitySelectDialog(EDCapabilitySelectDialog.SELECT_TALENT,circle,talentlist,null,new Rectangle(parentloc.x+source.getX()+source.getWidth(), parentloc.y+source.getY(), 450, 300));
		dialog.setSingleSelection(true);
		dialog.setVisible(true);
		Map<String, SKILLType> selected = dialog.getSelectedCapabilitytMap();
		for(String key : selected.keySet()){
			SKILLType cap = selected.get(key);
			if( cap.getLIMITATION().size()<1 ) {
				TALENTABILITYType talent = new TALENTABILITYType();
				talent.setName(cap.getName());
				character.addOptionalTalent(disciplin, circle, talent, false);
			} else for( String limitation : cap.getLIMITATION() ) {
				TALENTABILITYType talent = new TALENTABILITYType();
				talent.setName(cap.getName());
				if( !limitation.isEmpty() ) talent.setLimitation(limitation);
				character.addOptionalTalent(disciplin, circle, talent, false);
			}
			character.refesh();
		}
	}
}

class TalentsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private TalentsContainer talents;
	private String[] columnNames = {"Circle", "Talentname", "Limitation", "Attribute", "Startrank", "Rank", "Step", "Dice", "Action", "Teacher Dis", "Type", "BookRef" };
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
			//System.out.println("character is null");
			return 0;
		}
		if (talents == null) return 0;
		return talents.getDisciplinetalents().size()+talents.getOptionaltalents().size()+talents.getFreetalents().size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		TALENTType talent = getTalent(row);
		if( talent == null ) return "Error";
		TalentKind talentKind=getTalentKind(row);
		TALENTTEACHERType teacher = talent.getTEACHER();
		switch (col) {
		case 0:
			return Integer.valueOf(talent.getCircle());
		case 1:
			String result = talent.getName();
			if( talent.getRealigned() > 0 ) {
				result = "("+result+")";
			}
			return result;
		case 2:
			if( talent.getLIMITATION().size()>0 ) return talent.getLIMITATION().get(0);
			return "-";
		case 3:
			if( talent.getAttribute() == null ) return ATTRIBUTENameType.NA.value();
			return talent.getAttribute().value();
		case 4: return Integer.valueOf(talent.getRANK().getStartrank());
		case 5: return Integer.valueOf(talent.getRANK().getRank());
		case 6: return talent.getRANK().getStep();
		case 7:
			if( talent.getRANK().getDice() == null ) return "-";
			return talent.getRANK().getDice();
		case 8: return talent.getAction().value();
		case 9:
			if( teacher == null ) return "-";
			return teacher.getDiscipline();
		case 10:
			if( teacher == null ) return "-";
			if( teacher.getByversatility().equals(YesnoType.YES) ) return "Versatility";
			switch(talentKind){
			case DIS : return "DisciplineTalent";
			case FRE : return "FreeTalent";
			default: return "OptionalTalent"; 
			}
		case 11: return talent.getBookref();
		default: return Integer.valueOf(0);
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
		TALENTType talent = getTalent(row);
		if( talent == null ) {
			return false;
		}
		if( getTalentKind(row).equals(TalentKind.FRE) ) {
			return false;
		}
		// Realigned Talents dürfen nicht mehr editiert werden
		if( talent.getRealigned() > 0 ) {
			return false;
		}
		if( col == 4 ) {
			if( talent.getCircle() > 1 ) {
				return false;
			}
			return true;
		}
		if( col == 5 ) return true;
		if( col == 9 ) return true;
		if( col == 0 ) {
			TALENTTEACHERType teacher = talent.getTEACHER();
			if( (teacher!=null) && teacher.getByversatility().equals(YesnoType.YES) ) return true;
		}
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		TALENTType talent = getTalent(row);
		if( talent == null ) return;
		switch (col) {
		case 0:
			talent.setCircle((Integer)value);
			break;
		case 4:
			int v = (Integer)value;
			RANKType rank = talent.getRANK();
			if( (rank.getRank()<v) || (rank.getRank()==rank.getStartrank()) ) {
				rank.setRank(v);
			}
			rank.setStartrank(v);
			break;
		case 5:
			talent.getRANK().setRank((Integer)value);
			break;
		case 9:
			TALENTTEACHERType teacher = talent.getTEACHER();
			if( teacher == null ) {
				teacher = new TALENTTEACHERType();
				talent.setTEACHER(teacher);
			}
			teacher.setDiscipline((String)value);
			break;
		}
		character.refesh();
		if( row < getRowCount() ) { 
			fireTableCellUpdated(row, col);
			fireTableCellUpdated(row, 5);
		}
	}

	private TALENTType getTalent(int row) {
		if(talents.getDisciplinetalents().size() > row ) {
			return talents.getDisciplinetalents().get(row);
		}
		row -= talents.getDisciplinetalents().size();
		if(talents.getOptionaltalents().size() > row ) {
			return talents.getOptionaltalents().get(row);
		}
		row -= talents.getOptionaltalents().size();
		if(talents.getFreetalents().size() > row ) {
			return talents.getFreetalents().get(row);
		}
		return null;
	}

	private TalentsContainer.TalentKind getTalentKind(int row) {
		if(talents.getDisciplinetalents().size() > row ) {
			return TalentsContainer.TalentKind.DIS;
		}
		row -= talents.getDisciplinetalents().size();
		if(talents.getOptionaltalents().size() > row ) {
			return TalentsContainer.TalentKind.OPT;
		}
		row -= talents.getOptionaltalents().size();
		if(talents.getFreetalents().size() > row ) {
			return TalentsContainer.TalentKind.FRE;
		}
		return null;
	}
}
