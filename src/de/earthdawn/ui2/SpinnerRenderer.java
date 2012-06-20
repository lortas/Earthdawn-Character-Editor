package de.earthdawn.ui2;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JSpinner;

public class SpinnerRenderer extends JSpinner implements TableCellRenderer {
	private static final long serialVersionUID = 3323328106882968233L;
	SpinnerRenderer(int min, int max) { setModel(new SpinnerNumberModel(0, min, max, 1)); }

	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		setValue(value);
		return this;
	}
}
