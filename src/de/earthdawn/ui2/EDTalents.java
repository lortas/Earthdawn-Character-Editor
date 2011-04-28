package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.DISCIPLINE;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.TALENTSType;
import de.earthdawn.data.TALENTTEACHERType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.data.YesnoType;

public class EDTalents extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CharacterContainer character;

	private JScrollPane scrollPane;
	private JTable table;
	private String disciplin;
	private JButton buttonAdd;
	private JButton buttonAdd2;
	private JPopupMenu popupMenuCircle;
	private JPopupMenu popupMenuTalent;

	public void setCharacter(final CharacterContainer character) {
		this.character = character;
		((TalentsTableModel)table.getModel()).setCharacter(character);
		table.getColumnModel().getColumn(4).setCellEditor(new SpinnerEditor(0, 20));
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
		table.setRowSelectionAllowed(false);
		table.setSurrendersFocusOnKeystroke(true);
		table.setModel(new TalentsTableModel(character, disciplin));
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setAutoCreateRowSorter(true);

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

		buttonAdd2 = new JButton("Add Talent by Versatility");
		buttonAdd2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_buttonAdd2_actionPerformed(arg0);
			}
		});
		toolBar.add(buttonAdd2);

		popupMenuTalent = new JPopupMenu();
		toolBar.add(popupMenuTalent);

		popupMenuCircle = new JPopupMenu();
		toolBar.add(popupMenuCircle);
	}

	public String getDisciplin() {
		return disciplin;
	}

	public void setDisciplin(String disciplin) {
		this.disciplin = disciplin;
	}

	protected void do_buttonAdd_actionPerformed(ActionEvent arg0) {
		popupMenuCircle.removeAll();

		List<Integer> l = character.getCircleOfMissingOptionalTalents().get(disciplin);
		for(Integer c : l){
			JMenuItem menuItem = new JMenuItem(String.valueOf(c));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					do_menuItemCircle_actionPerformed(arg0);
				}
			});
			popupMenuCircle.add(menuItem);		
		}
		
		popupMenuCircle.show(buttonAdd,buttonAdd.getX(), buttonAdd.getY()+ buttonAdd.getHeight());
	}

	protected void do_buttonAdd2_actionPerformed(ActionEvent arg0) {
		// Wenn kein ungenutze Vielseitigkeit Räng vorhanden, dann abbrechen
		if( character.getUnusedVersatilityRanks() < 1 ) return;
		EDTalentSelectDialog dialog = new EDTalentSelectDialog();
		dialog.setVisible(true);
		HashMap<String, CAPABILITYType> selected = dialog.getSelectedTalentMap();
		for(String key : selected.keySet()){
			CAPABILITYType cap = selected.get(key);
			TALENTABILITYType talent = new TALENTABILITYType();
			talent.setName(cap.getName());
			character.addOptionalTalent(disciplin, 15, talent, true);
			character.refesh();
		}
	}

	protected void do_menuItemCircle_actionPerformed(ActionEvent arg0) {
		popupMenuTalent.removeAll();
		final JMenuItem source = ((JMenuItem)arg0.getSource());
		DISCIPLINE d = ApplicationProperties.create().getDisziplin(disciplin);
		List<TALENTABILITYType> talentlist = character.getUnusedOptionalTalents(d,Integer.valueOf(source.getText()));
		for( TALENTABILITYType talent : talentlist){
			if (talent != null){
				JMenuItem menuItem = new JMenuItem(talent.getName());
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						do_menuItemTalent_actionPerformed(arg0, source.getText());
					}
				});
				popupMenuTalent.add(menuItem);
			}
		}
		popupMenuTalent.show(buttonAdd,source.getX() + source.getWidth(), source.getY()+10);
		//popupMenuTalent.show(buttonAdd,1,1);
	}

	protected void do_menuItemTalent_actionPerformed(ActionEvent arg0, String circle) {
		JMenuItem source = ((JMenuItem)arg0.getSource());
		DISCIPLINE d = ApplicationProperties.create().getDisziplin(disciplin);
		List<TALENTABILITYType> talentlist = character.getUnusedOptionalTalents(d,Integer.valueOf(circle));
		System.out.println(circle + " " + source.getText() );
		for( TALENTABILITYType talent : talentlist){
			if(talent.getName().equals( source.getText())) {
				System.out.println("add");
				character.addOptionalTalent(disciplin, Integer.valueOf(circle), talent, false);
				character.refesh();
				break;
			}
		}
	}
}

class TalentsTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private TALENTSType talents;
	private String[] columnNames = {"Circle", "Talentname", "Limitation", "Attribute", "Rank", "Step", "Action", "Teacher Dis", "Type"};
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
		return talents.getDISZIPLINETALENT().size()+talents.getOPTIONALTALENT().size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		TALENTType talent = null;
		boolean isDisciplinTalent=true;
		if(talents.getDISZIPLINETALENT().size() > row ) {
			talent=talents.getDISZIPLINETALENT().get(row);
			isDisciplinTalent=true;
		} else {
			talent=talents.getOPTIONALTALENT().get(row-talents.getDISZIPLINETALENT().size());
			isDisciplinTalent=false;
		}
		Object result = new String("Error"); 
		switch (col) {
		case 0:
	        	try{
	        		result = new Integer(talent.getCircle());
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 1:  
	        	try{
	        		result = talent.getName();
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 2:
	        	try{
	        		result = talent.getLimitation();
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 3: 
	        	try{
	        		result = talent.getAttribute().value();
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 4: 
	        	try{
	        		result = new Integer(talent.getRANK().getRank());
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
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 6:
	        	try{
	        		result = talent.getAction().value();
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 7:
	        	try{
	        		TALENTTEACHERType teacher = talent.getTEACHER();
	        		if( teacher == null ) {
	        			result = "-";
	        		} else {
	        			result = teacher.getDiscipline();
	        		}
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
	        	}
	        	break;
	        case 8:
	        	try{
	        		TALENTTEACHERType teacher = talent.getTEACHER();
	        		if( (teacher!=null) && teacher.getByversatility().equals(YesnoType.YES) ) {
	        			result="Versatility";
	        		} else if( isDisciplinTalent ) {
	        			result="DisciplineTalent";
	        		} else {
	        			result="OptionalTalent";
	        		}
	        	}
	        	catch(Exception e){
	        		//System.err.println("Error: " + talent.getName());
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
		if( col == 4 ) return true;
		if( col == 7 ) return true;
		if( col == 0 ) {
			TALENTType talent = null;
			if(talents.getDISZIPLINETALENT().size() > row ) {
				talent=talents.getDISZIPLINETALENT().get(row);
			} else {
				talent=talents.getOPTIONALTALENT().get(row-talents.getDISZIPLINETALENT().size());
			}
			TALENTTEACHERType teacher = talent.getTEACHER();
			if( (teacher!=null) && teacher.getByversatility().equals(YesnoType.YES) ) return true;
		}
		return false;
	}

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
		TALENTType talent = null;
		if(talents.getDISZIPLINETALENT().size() > row ) {
			talent=talents.getDISZIPLINETALENT().get(row);
		} else {
			talent=talents.getOPTIONALTALENT().get(row-talents.getDISZIPLINETALENT().size());
		}
		switch (col) {
		case 0:
			talent.setCircle((Integer)value);
			break;
		case 4:
			talent.getRANK().setRank((Integer)value);
			break;
		case 7:
			TALENTTEACHERType teacher = talent.getTEACHER();
			if( teacher == null ) {
				teacher = new TALENTTEACHERType();
				talent.setTEACHER(teacher);
			}
			teacher.setDiscipline((String)value);
			break;
		}
		character.refesh();
		fireTableCellUpdated(row, col);
		fireTableCellUpdated(row, 5);
		fireTableCellUpdated(row, 7);
	}
}
