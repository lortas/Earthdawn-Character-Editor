package de.earthdawn.ui2;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor { 
	private static final long serialVersionUID = -7602597697122661940L;
	final JSpinner spinner = new JSpinner(); 

	public SpinnerEditor(String[] items) { 
		spinner.setModel(new SpinnerListModel(java.util.Arrays.asList(items)));
	}

	public SpinnerEditor(int min, int max) { 
		spinner.setModel(new SpinnerNumberModel(0, min, max, 1)); 
	}

	// Prepares the spinner component and returns it.
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { 
		spinner.setValue(value);
		return spinner;
	}

	// Enables the editor only for double-clicks. 
	public boolean isCellEditable(EventObject evt) { return true; } 

	// Returns the spinners current value. 
	public Object getCellEditorValue() { return spinner.getValue(); }
}