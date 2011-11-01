package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.DISCIPLINEType;

public class EDDisciplines extends JPanel {

	private static final long serialVersionUID = 1L;
	private CharacterContainer character;
	private JScrollPane scrollPane;
	private JTable table;
	private JToolBar toolBar;
	private JButton btnAddDiscipline;
	private JButton btnRemoveDiscipline;
	private JPopupMenu popupMenuCircle;
	private BufferedImage backgroundimage = null;

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((DisciplinesTableModel)table.getModel()).setCharacter(character);
		table.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor(0, 15));
	}

	public CharacterContainer getCharacter() {
		return character;
	}	

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("templates/disciplines_background.jpg");
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
	public EDDisciplines() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));

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

		table.setModel(new DisciplinesTableModel(character));
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setOpaque(false);
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);
		toolBar.setOpaque(false);

		btnAddDiscipline = new JButton("Add Discipline");
		btnAddDiscipline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddDiscipline_actionPerformed(arg0);
			}
		});
		btnAddDiscipline.setOpaque(false);
		toolBar.add(btnAddDiscipline);
		btnRemoveDiscipline = new JButton("Remove Last Discipline");
		btnRemoveDiscipline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveDiscipline_actionPerformed(arg0);
			}
		});
		btnRemoveDiscipline.setOpaque(false);
		toolBar.add(btnRemoveDiscipline);
		popupMenuCircle = mapTreeToMenuTree(null,ApplicationProperties.create().getAllDisziplinNamesAsTree()).getPopupMenu();
	}

	private JMenu mapTreeToMenuTree(String name, Map<String, Map<String, ?>> tree) {
		JMenu result;
		if( name == null ) {
			result = new JMenu();
		} else {
			result = new JMenu(name);
			JMenuItem menuItem = new JMenuItem(name);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					do_menuItem_actionPerformed(arg0);
				}
			});
			result.add(menuItem);
		}
		SortedSet<String> sortedset= new TreeSet<String>(tree.keySet());
		for( String n : sortedset ) {
			if( tree.get(n).isEmpty() ) {
				JMenuItem menuItem = new JMenuItem(n);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						do_menuItem_actionPerformed(arg0);
					}
				});
				result.add(menuItem);
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Map<String, ?>> submap = (Map<String, Map<String, ?>>)(tree.get(n));
				result.add(mapTreeToMenuTree(n,submap));
			}
		}
		return result;
	}

	protected void do_btnAddDiscipline_actionPerformed(ActionEvent arg0) {
		popupMenuCircle.show(btnAddDiscipline, btnAddDiscipline.getX(), btnAddDiscipline.getY()+ btnAddDiscipline.getHeight());
	}

	protected void do_btnRemoveDiscipline_actionPerformed(ActionEvent arg0) {
			character.removeLastDiciplin();
			ECEWorker worker = new ECEWorker();
			worker.verarbeiteCharakter(character.getEDCHARACTER());
			character.refesh();
	}

	protected void do_menuItem_actionPerformed(ActionEvent arg0) {
		System.out.println(((JMenuItem)arg0.getSource()).getText());
		character.addDiciplin(((JMenuItem)arg0.getSource()).getText());
		ECEWorker worker = new ECEWorker();
		worker.verarbeiteCharakter(character.getEDCHARACTER());
		character.refesh();
	}
}

class DisciplinesTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private CharacterContainer character;

	private String[] columnNames = {"Discipline Name", "Circle"};

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
		if( character == null ) return 0;
		return character.getDisciplines().size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		DISCIPLINEType discipline = character.getDisciplines().get(row);
		switch (col) {
		case 0:  return discipline.getName();
		case 1:  return new Integer(discipline.getCircle());
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
		if( col == 1 ) return true;
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void setValueAt(Object value, int row, int col) {  
		DISCIPLINEType discipline = character.getDisciplines().get(row);
		switch (col) {
		case 0: discipline.setName((String)value);
		case 1: discipline.setCircle((Integer)value);
		}
		character.refesh();
		fireTableCellUpdated(row, col);
	}

}
