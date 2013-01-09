package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DisciplineTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel result = new JLabel();
		result.setVerticalAlignment(JLabel.TOP);
		if( column == 0 ) {
			result.setText("<html><center>"+value.toString().replace("\n\n", "<p>").replace("\n", "<br>").replace("\r", "<br>")+"</center><html>");
			result.setFont(new Font("Sans", Font.BOLD, 25));
			result.setHorizontalAlignment(JLabel.CENTER);
		} else if( column == 1 ) {
			result.setText(value.toString());
			result.setFont(new Font("Sans", Font.BOLD, 25));
			result.setHorizontalAlignment(JLabel.CENTER);
		} else if( column == 2 ) {
			result.setText(value.toString());
			result.setFont(new Font("Sans", Font.PLAIN, 12));
			result.setHorizontalAlignment(JLabel.CENTER);
		} else {
			result.setText("<html>"+value.toString().replace("\n\n", "<p>").replace("\n", "<br>").replace("\r", "<br>")+"<html>");
			result.setFont(new Font("Sans", Font.PLAIN, 12));
			result.setHorizontalAlignment(JLabel.LEFT);
		}
		return result;
	}

}
