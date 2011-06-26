package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ACCOUNTINGType;
import de.earthdawn.data.PlusminusType;

public class EDExperience extends JPanel {

	/**
	 * Create the panel.
	 */
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddEXPEntry;
	private JButton btnRemoveEXPEntry;
	private BufferedImage backgroundimage = null;
	
	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		
		this.character = character;
		((ExperienceTableModel)table.getModel()).setCharacter(character);
		JComboBox comboBoxPlusMinus = new JComboBox();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));		
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("templates/experience_background.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}

	public EDExperience() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		toolBar = new JToolBar();
		toolBar.setOpaque(false);
		add(toolBar, BorderLayout.NORTH);
		
		btnAddEXPEntry = new JButton("Add Experience");
		btnAddEXPEntry.setOpaque(false);
		btnAddEXPEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddEXPEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddEXPEntry);
		
		btnRemoveEXPEntry = new JButton("Remove Experience");
		btnRemoveEXPEntry.setOpaque(false);
		btnRemoveEXPEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveEXPEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveEXPEntry);
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		// Create transperant table
		table = new JTable(){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer( renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		table.setOpaque(false);
		InputMapUtil.setupInputMap(table);	
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new ExperienceTableModel(character));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
		table.setRowSelectionAllowed(false);
		JComboBox comboBoxPlusMinus = new JComboBox();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
	}
	
	protected void do_btnAddEXPEntry_actionPerformed(ActionEvent arg0) {
		ACCOUNTINGType ac = new ACCOUNTINGType();
		ac.setType(PlusminusType.PLUS);
		ac.setWhen(CharacterContainer.getCurrentDateTime());
		ac.setComment(new String(""));
		ac.setValue(0);
		character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().add(ac);
		character.refesh();
	}
	
	protected void do_btnRemoveEXPEntry_actionPerformed(ActionEvent arg0) {
		ArrayList<ACCOUNTINGType> expForRemoval = new ArrayList<ACCOUNTINGType> ();
		for(int row :table.getSelectedRows()){
			ACCOUNTINGType exp  =character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row);
			expForRemoval.add(exp);
		}
		character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().removeAll(expForRemoval);		
		character.refesh();
	}
}

class ExperienceTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
    private String[] columnNames = {"Date", "Comment",  "Type", "Value"};


    
    

	public ExperienceTableModel(CharacterContainer character) {
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
    	if(character != null){
    		return character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().size();
    	}
    	else{
    		return 0;
    	}
    		
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        // {"Date", "Comment",  "Type", "Value"}
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).getWhen();
	    		case 1: return character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).getComment();
	    		case 2: if (character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).getType().equals(PlusminusType.PLUS)) {
	    					return new String("+");
	    				}
	    				else{
	    					return new String("-");
	    				}
	    		
	    		case 3: return new Integer(character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).getValue());
	    		default : return new String("Error not defined");
	    	}
        }
	    else{
	    	return 0;
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
    	return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    
    
    public void setValueAt(Object value, int row, int col) { 
    	 // {"Date", "Comment",  "Type", "Value"}
    	switch (col) {
			case 0: character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).setWhen((String)value); break;
			case 1: character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).setComment((String)value);  break;
			case 2: if (((String)value).equals("+")) {
						System.out.println("+");
						character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).setType(PlusminusType.PLUS);
					}
					else{
						System.out.println("-");
						character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).setType(PlusminusType.MINUS);
					}
					break;
			case 3: character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).setValue(((Integer)value).intValue());  break;
    	}
    	character.refesh();	
    }

}

