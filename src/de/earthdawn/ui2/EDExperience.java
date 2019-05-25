package de.earthdawn.ui2;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ACCOUNTINGType;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.PlusminusType;

public class EDExperience extends JPanel {

	private static final long serialVersionUID = 3341440760130899020L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
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
		JComboBox<String> comboBoxPlusMinus = new JComboBox<String>();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("experienceselection") ) {
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
			File file = new File("images/background/experience.jpg");
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
		btnAddEXPEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddEXPEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddEXPEntry);
		
		btnRemoveEXPEntry = new JButton("Remove Experience");
		btnRemoveEXPEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveEXPEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveEXPEntry);

		JButton btnResetSpentLP = new JButton("Reset Spent LP");
		btnResetSpentLP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnResetSpentLP_actionPerformed(arg0);
			}
		});
		toolBar.add(btnResetSpentLP);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		// Create transperant table
		table = new JTable() {
			private static final long serialVersionUID = -8524288405534860950L;
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
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		JComboBox<String> comboBoxPlusMinus = new JComboBox<String>();
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
	protected void do_btnResetSpentLP_actionPerformed(ActionEvent arg0) {
		int a = JOptionPane.showOptionDialog(this,
				EDMainWindow.NLS.getString("Confirmation.ResetSpentLP.text"),
				EDMainWindow.NLS.getString("Confirmation.ResetSpentLP.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				EDMainWindow.OptionDialog_YesNoOptions,
				EDMainWindow.OptionDialog_YesNoOptions[0]);
		if( a != 0 ) return;
		character.clearSpentLegendPoints();
		character.getLegendPoints().getLEGENDPOINTS().addAll(character.getCalculatedLegendpoints().getCALCULATIONLP());
		character.refesh();
	}
}

class ExperienceTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -6994769801045790956L;
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
	    		
			case 3: return Integer.valueOf(character.getEDCHARACTER().getEXPERIENCE().getLEGENDPOINTS().get(row).getValue());
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
	public Class<?> getColumnClass(int c) {
		Object valueAt = getValueAt(0, c);
		if( valueAt == null ) return String.class;
		return valueAt.getClass();
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
