package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import javax.swing.table.TableColumn;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ACCOUNTINGType;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.PlusminusType;

public class EDKarma extends JPanel {

	private static final long serialVersionUID = -6560310823885670305L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddKarmaEntry;
	private JButton btnRemoveKarmaEntry;
	private BufferedImage backgroundimage = null;

	public CharacterContainer getCharacter() { return character; }

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((KarmaTableModel)table.getModel()).setCharacter(character);
		JComboBox<String> comboBoxPlusMinus = new JComboBox<String>();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("karmaselection") ) {
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
			File file = new File("images/background/karma.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}

	public EDKarma() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		btnAddKarmaEntry = new JButton("Add Karma");
		btnAddKarmaEntry.setOpaque(true);
		btnAddKarmaEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddKarmaEntry_actionPerformed(arg0);
			}
		});
		btnRemoveKarmaEntry = new JButton("Remove Karma");
		btnRemoveKarmaEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveKarmaEntry_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddKarmaEntry);
		toolBar.add(btnRemoveKarmaEntry);
		toolBar.setOpaque(false);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		// Create transparent table
		table = new JTable(){
			private static final long serialVersionUID = -6132298112451086676L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer( renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};

		InputMapUtil.setupInputMap(table);	

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new KarmaTableModel(character));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
		table.setRowSelectionAllowed(false);
		table.setOpaque(false);
		JComboBox<String> comboBoxPlusMinus = new JComboBox<String>();
		comboBoxPlusMinus.addItem("+");
		comboBoxPlusMinus.addItem("-");
		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxPlusMinus));
	}
	
	protected void do_btnAddKarmaEntry_actionPerformed(ActionEvent arg0) {
		ACCOUNTINGType ac = new ACCOUNTINGType();
		ac.setType(PlusminusType.PLUS);
		ac.setWhen(CharacterContainer.getCurrentDateTime());
		ac.setComment(new String(""));
		ac.setValue(0);
		character.getEDCHARACTER().getKARMA().getKARMAPOINTS().add(ac);
		character.refesh();
	}
	
	protected void do_btnRemoveKarmaEntry_actionPerformed(ActionEvent arg0) {
		ArrayList<ACCOUNTINGType> expForRemoval = new ArrayList<ACCOUNTINGType> ();
		for(int row :table.getSelectedRows()){
			ACCOUNTINGType karma =character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row);
			expForRemoval.add(karma);
		}
		character.getEDCHARACTER().getKARMA().getKARMAPOINTS().removeAll(expForRemoval);	
		character.refesh();
	}
}

class KarmaTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 5997526498986521423L;
	private CharacterContainer character;
	private String[] columnNames = {"Date", "Comment",  "Type", "Value"};

	public KarmaTableModel(CharacterContainer character) {
		super();
		this.character = character;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		fireTableStructureChanged();
	}

	public CharacterContainer getCharacter() { return character; }

	public int getColumnCount() { return columnNames.length; }

	public int getRowCount() {
		if( character == null ) return 0;
		return character.getEDCHARACTER().getKARMA().getKARMAPOINTS().size();
	}

	public String getColumnName(int col) { return columnNames[col]; }

	public Object getValueAt(int row, int col) {
		if( character == null) return 0; 
		// {"Date", "Comment",  "Type", "Value"}
		switch (col) {
		case 0:
			return character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getWhen();
		case 1:
			return character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getComment();
		case 2:
			if (character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getType().equals(PlusminusType.PLUS)) return new String("+");
			else return new String("-");
		case 3:
			return Integer.valueOf(character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).getValue());
		default :
			return new String("Error not defined");
		}
	}

	public Class<?> getColumnClass(int c) { return getValueAt(0, c).getClass(); }

	public boolean isCellEditable(int row, int col) { return true; }

	public void setValueAt(Object value, int row, int col) { 
		// {"Date", "Comment",  "Type", "Value"}
		switch (col) {
		case 0:
			character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setWhen((String)value);
			break;
		case 1:
			character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setComment((String)value);
			break;
		case 2:
			if( ((String)value).equals("+") ) character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setType(PlusminusType.PLUS);
			else character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setType(PlusminusType.MINUS);
			break;
		case 3:
			character.getEDCHARACTER().getKARMA().getKARMAPOINTS().get(row).setValue(((Integer)value).intValue());
			break;
		}
		character.refesh();	
	}
}