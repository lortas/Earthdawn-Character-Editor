package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBElement;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.DISCIPLINE;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.ObjectFactory;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.TALENTSType;
import de.earthdawn.data.TALENTType;

public class EDDisciplines extends JPanel {
	private CharacterContainer character;
	
	private JScrollPane scrollPane;
	private JTable table;
	private JToolBar toolBar;
	private JButton btnAddDicipline;
	private JPopupMenu popupMenu;
	private JMenuItem menuItem;

	
	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((DisciplinesTableModel)table.getModel()).setCharacter(character);
	}

	public CharacterContainer getCharacter() {
		return character;
	}	
	

	/**
	 * Create the panel.
	 */
	public EDDisciplines() {
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setModel(new DisciplinesTableModel(character));
		table.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor(0, 20));
		scrollPane.setViewportView(table);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);
		
		btnAddDicipline = new JButton("Add Dicipline");
		btnAddDicipline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddDicipline_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddDicipline);
		
		popupMenu = new JPopupMenu();
		addPopup(btnAddDicipline, popupMenu);
		for (String n : ApplicationProperties.create().getAllDisziplinNames()) {
			System.out.println(n);
			menuItem = new JMenuItem(n);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					do_menuItem_actionPerformed(arg0);
				}
			});
			popupMenu.add(menuItem);		
		}


	}
	
	protected void do_btnAddDicipline_actionPerformed(ActionEvent arg0) {
		popupMenu.show(btnAddDicipline, btnAddDicipline.getX(), btnAddDicipline.getY()+ btnAddDicipline.getHeight());
	}
	
	protected void addDiciplin(String name){
		
		if ((character.getAllDiciplinesByOrder().size() < 3) && (character.getAllDiciplinesByName().get(name) == null)){
			DISCIPLINEType value = new DISCIPLINEType();
			value.setName(name);
			value.setCircle(5);
			value.setOrder(character.getAllDiciplinesByOrder().size() +1);
			JAXBElement<DISCIPLINEType> dt = new ObjectFactory().createEDCHARACTERDISCIPLINE(value);
			character.getEDCHARACTER().getATTRIBUTEOrDEFENSEOrHEALTH().add(dt);
			TALENTSType talents =  new TALENTSType();
			talents.setDiscipline(name);
			character.getEDCHARACTER().getATTRIBUTEOrDEFENSEOrHEALTH().add(new ObjectFactory().createEDCHARACTERTALENTS(talents));
			initDisciplinTalents(value.getName(),value.getCircle());
		}
		
	}
	
	private void initDisciplinTalents(String disciplinname, int circle){
		DISCIPLINE d = ApplicationProperties.create().getDisziplin(disciplinname);
		for( JAXBElement<?> element : d.getDURABILITYAndOPTIONALTALENTAndDISCIPLINETALENT() ) {
			if (element.getName().getLocalPart().equals("DISCIPLINETALENT")){
				TALENTABILITYType ta = (TALENTABILITYType) element.getValue();

				if(ta.getCircle() <= circle){
					TALENTType talent = new TALENTType();
					talent.setName(ta.getName());
					talent.setLimitation(ta.getLimitation());
					talent.setCircle(ta.getCircle());
					RANKType rank = new RANKType();
					rank.setRank(1);
					rank.setBonus(1);
					rank.setStep(1);
					talent.setRANK(rank);
					if(character.getTalentByName(ta.getName()) == null){
						character.getAllTalentsByDisziplinName().get(disciplinname).getDISZIPLINETALENTOrOPTIONALTALENT().add(new ObjectFactory().createTALENTSTypeDISZIPLINETALENT(talent));
					}
				}
			}
			
		}			
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	protected void do_menuItem_actionPerformed(ActionEvent arg0) {
		System.out.println(((JMenuItem)arg0.getSource()).getText());
		addDiciplin(((JMenuItem)arg0.getSource()).getText());
        ECEWorker worker = new ECEWorker();
        worker.verarbeiteCharakter(character.getEDCHARACTER());
        character.refesh();
	}
}

class DisciplinesTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;

    private String[] columnNames = {"Disciplinename", "Circle"};




	public DisciplinesTableModel(CharacterContainer character) {
		super();
		this.character = character;

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
        if(character == null){
        	System.out.println("character is null");
        	return 0;
        }
        
       
    	return character.getAllDiciplinesByOrder().size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	character.getAllDiciplinesByOrder().get(new Integer(row+1)).getName();

    	switch (col) {
	        case 0: return  character.getAllDiciplinesByOrder().get(new Integer(row+1)).getName();
	        case 1: return  new Integer(character.getAllDiciplinesByOrder().get(new Integer(row+1)).getCircle());
	
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
    	if(col == 1){
    		return true;
    	}
    	return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {  
    	character.getAllDiciplinesByOrder().get(new Integer(row+1)).setCircle((Integer)value);
        fireTableCellUpdated(row, col);
    }

}