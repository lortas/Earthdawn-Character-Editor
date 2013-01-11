package de.earthdawn.ui2;

import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.KeyStroke;

public class InputMapUtil {
	static void setupInputMap(JTable table){
		//TODO: das müsste mal überarbeitet werden
		for ( KeyStroke key : table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).allKeys()){
			//System.out.println("Key :" + key.toString() + " -> " + table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(key) );
		}
		
		table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextRow");
		table.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startEditing");
	
	}

}
