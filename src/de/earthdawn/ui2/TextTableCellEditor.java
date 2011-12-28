package de.earthdawn.ui2;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TextTableCellEditor extends AbstractCellEditor implements
		TableCellEditor, TableCellRenderer {

	private static final long serialVersionUID = 1642846309475090066L;
	private JScrollPane pane = new JScrollPane();
	private JTextArea text = new JTextArea();

	public TextTableCellEditor() {
		super();
		pane.setOpaque(false);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		text.setLineWrap(true);
		text.setOpaque(false);
		pane.setViewportView(text);
		pane.getViewport().setOpaque(false);
	}

	public void setLineWrap(boolean linewrap) {
		text.setLineWrap(linewrap);
	}

	public void setOpaque(boolean opaque) {
		text.setOpaque(opaque);
		pane.setOpaque(opaque);
		pane.getViewport().setOpaque(opaque);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		text.setText( String.valueOf( value ) );
		return pane;
	}

	@Override
	public Object getCellEditorValue() {
		return text.getText();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		text.setText( String.valueOf( value ) );
		return pane;
	}
}
