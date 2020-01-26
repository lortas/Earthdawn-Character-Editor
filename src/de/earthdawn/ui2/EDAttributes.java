package de.earthdawn.ui2;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ATTRIBUTENameType;
import de.earthdawn.data.ATTRIBUTEType;
import java.util.Map;

public class EDAttributes extends JPanel {
	private static final long serialVersionUID = -6319723413147452999L;
	private CharacterContainer character;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final int lpincrease_max = PROPERTIES.getOptionalRules().getATTRIBUTE().getLimitlpincrease();
	private JScrollPane scrollPane;
	private JTable table;
	private BufferedImage backgroundimage = null;

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/attributes.jpg");
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
	public EDAttributes() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setOpaque(false);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane);

		table = new JTable(){
			private static final long serialVersionUID = -8611738411462826052L;
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
		table.setModel(new AttributesTableModel());

		JComboBox<Integer> comboBoxBuyPoints = new JComboBox<Integer>();
		for(int i=-2; i<=8;i++) comboBoxBuyPoints.addItem(i);
		comboBoxBuyPoints.setMaximumRowCount(11);
		JComboBox<Integer> comboBoxLpincrease = new JComboBox<Integer>();
		for(int i=0; i<=lpincrease_max;i++) comboBoxLpincrease.addItem(i);

		table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxBuyPoints));
		table.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxLpincrease));
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		((AttributesTableModel)table.getModel()).setCharacter(character);
	}

	public CharacterContainer getCharacter() {
		return character;
	}	
}

class AttributesTableModel extends AbstractTableModel {
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private static final long serialVersionUID = -6909371939387453930L;
	private CharacterContainer character;
	private final Map<ATTRIBUTENameType, String> attributesNames = PROPERTIES.getAttributeNames();
	private final String[] columnNames = {
		PROPERTIES.getTranslationText("attribute"),
		PROPERTIES.getTranslationText("base"),
		PROPERTIES.getTranslationText("buy"),
		PROPERTIES.getTranslationText("lpincrease"),
		"Final",
		PROPERTIES.getTranslationText("step"),
		PROPERTIES.getTranslationText("dice")
	};
	private Object[][] data = {
	{"", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), ""},
	{"", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), ""},
	{"", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), ""},
	{"", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), ""},
	{"", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), ""},
	{"", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), ""},
	};
	private ATTRIBUTENameType[] columnKey = {ATTRIBUTENameType.DEX, ATTRIBUTENameType.STR, ATTRIBUTENameType.TOU, ATTRIBUTENameType.PER, ATTRIBUTENameType.WIL, ATTRIBUTENameType.CHA};
	public void setCharacter(CharacterContainer character) {
		this.character = character;
	}

	public CharacterContainer getCharacter() {
		return character;
	}	

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		if (character == null){
			return data[row][col];
		}
		ATTRIBUTEType attribute = character.getAttributes().get(columnKey[row]);
		switch (col) {
		case 0:  return attributesNames.get(columnKey[row]);
		case 1:  return Integer.valueOf(attribute.getRacevalue());
		case 2:  return Integer.valueOf(attribute.getGenerationvalue());
		case 3:  return Integer.valueOf(attribute.getLpincrease());
		case 4:  return Integer.valueOf(attribute.getCurrentvalue());
		case 5:  return Integer.valueOf(attribute.getStep());
		case 6:  return (attribute.getDice()==null)?"NA":attribute.getDice();
		default: return Integer.valueOf(0);
	}
}

	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if ((col == 2) || (col == 3)) {
			return true;
		} else {
			return false;
		}
	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		switch (col) {
			case 2:  character.getAttributes().get(columnKey[row]).setGenerationvalue((Integer)value); break;
			case 3:  character.getAttributes().get(columnKey[row]).setLpincrease((Integer)value); break;
			default: break;
		}
		new ECEWorker(character).verarbeiteCharakter();
		character.refesh();
		fireTableCellUpdated(row, col);
		fireTableCellUpdated(row, 4);
		fireTableCellUpdated(row, 5);
		fireTableCellUpdated(row, 6);
	}
}
