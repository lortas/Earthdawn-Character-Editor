package de.earthdawn.ui2;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.LAYOUTTABLECOLUMNType;
import de.earthdawn.data.OPTIONALRULEType;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.SKILLType;
import de.earthdawn.data.YesnoType;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ListSelectionModel;

public class EDSkills extends JPanel {

	private static final long serialVersionUID = -8901862501177055512L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddSkill;
	private JButton btnRemoveSkill;
	private JButton btnToggleDefaultSkill;
	private BufferedImage backgroundimage = null;

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("templates/skills_background.jpg");
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
	public EDSkills() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));

		toolBar = new JToolBar();
		toolBar.setOpaque(false);
		add(toolBar, BorderLayout.NORTH);

		btnAddSkill = new JButton("Add Skill");
		btnAddSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddSkill_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddSkill);

		btnRemoveSkill = new JButton("Remove Skill");
		btnRemoveSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveSkill_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveSkill);

		btnToggleDefaultSkill = new JButton("Toggle Default Skills");
		btnToggleDefaultSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnToggleDefaultSkill_actionPerformed(arg0);
			}
		});
		toolBar.add(btnToggleDefaultSkill);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable(){
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

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new SkillsTableModel(character));
		table.getColumnModel().getColumn(3).setCellEditor(new SpinnerEditor(0, 10));
		table.getColumnModel().getColumn(4).setCellEditor(new SpinnerEditor(0, 10));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
	}

	public void setCharacter(final CharacterContainer character) {
		this.character = character;
		((SkillsTableModel)table.getModel()).setCharacter(character);
		table.getColumnModel().getColumn(3).setCellEditor(new SpinnerEditor(0, 10));
		table.getColumnModel().getColumn(4).setCellEditor(new SpinnerEditor(0, 10));
		try {
			int c=0;
			for( LAYOUTTABLECOLUMNType width : PROPERTIES.getGuiLayoutTabel("skillselection") ) {
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

	public CharacterContainer getCharacter() {
		return character;
	}

	protected void do_btnAddSkill_actionPerformed(ActionEvent arg0) {
		EDCapabilitySelectDialog dialog = new EDCapabilitySelectDialog(EDCapabilitySelectDialog.SELECT_SKILLS,0, new Rectangle(100, 100, 450, 300) );
		dialog.setVisible(true);
		HashMap<String, SKILLType> selected = dialog.getSelectedCapabilitytMap();
		for(Object key : selected.keySet()){
			SKILLType cap = selected.get(key);
			SKILLType skill = new SKILLType();
			skill.setAction(cap.getAction());
			skill.setAttribute(cap.getAttribute());
			skill.setDefault(cap.getDefault());
			skill.setStrain(cap.getStrain());
			skill.setName(cap.getName());
			skill.setLimitation(cap.getLimitation());
			RANKType rank = new RANKType();
			rank.setRank(1);
			skill.setRANK(rank);
			character.getSkills().add(skill);
			character.refesh();
		}
	}

	protected void do_btnRemoveSkill_actionPerformed(ActionEvent arg0) {
		ArrayList<SKILLType> skillsForRemoval = new ArrayList<SKILLType> ();
		for(int row :table.getSelectedRows()){
			SKILLType skill = character.getSkills().get(row);
			skillsForRemoval.add(skill);
		}
		character.getSkills().removeAll(skillsForRemoval);
		character.refesh();
	}

	protected void do_btnToggleDefaultSkill_actionPerformed(ActionEvent arg0) {
		OPTIONALRULEType showdefaultskills = PROPERTIES.getOptionalRules().getSHOWDEFAULTSKILLS();
		YesnoType used = showdefaultskills.getUsed();
		if( used.equals(YesnoType.YES) ) {
			showdefaultskills.setUsed(YesnoType.NO);
			btnToggleDefaultSkill.setText("Show Default Skills");
		}
		else {
			showdefaultskills.setUsed(YesnoType.YES);
			btnToggleDefaultSkill.setText("Hide Default Skills");
		}
		character.refesh();
	}
}

class SkillsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1321139806762229027L;
	private CharacterContainer character;
	private String[] columnNames = {"Name", "Limitation",  "Attribute", "Startrank", "Rank", "Action", "Step", "Dice", "Bookref"};

	public SkillsTableModel(CharacterContainer character) {
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
    		return character.getSkills().size();
    	}
    	else{
    		return 0;
    	}
    		
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        // {"Name", "Limitation",  "Attribute", "Rank", "Startrank", "Action", "Step" "Dice"};
        if(character != null){ 
	    	switch (col) {
	    		case 0: return character.getSkills().get(row).getName();
	    		case 1: return character.getSkills().get(row).getLimitation();
	    		case 2: return character.getSkills().get(row).getAttribute();
	    		case 3: return new Integer(character.getSkills().get(row).getRANK().getStartrank());
	    		case 4: return new Integer(character.getSkills().get(row).getRANK().getRank());
	    		case 5: return character.getSkills().get(row).getAction().value();
	    		case 6: return character.getSkills().get(row).getRANK().getStep();
	    		case 7: return character.getSkills().get(row).getRANK().getDice().value();
	    		case 8: return character.getSkills().get(row).getBookref();
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
		if( col == 1 ) return true;
		if( col == 3 ) return true;
		if( col == 4 ) return true;
		return false;
	}

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */

	public void setValueAt(Object value, int row, int col) {
		if(col == 1) character.getSkills().get(row).setLimitation((String)value);
		if(col == 4) character.getSkills().get(row).getRANK().setRank((Integer) value);
		if( col == 3 ) {
			character.getSkills().get(row).getRANK().setStartrank((Integer) value);
			if( character.getSkills().get(row).getRANK().getRank() < (Integer) value ) {
				character.getSkills().get(row).getRANK().setRank((Integer) value);
			}
		}
		character.refesh();
		fireTableCellUpdated(row, 6);
		fireTableCellUpdated(row, 7);
		fireTableCellUpdated(row, col);
	}
}
