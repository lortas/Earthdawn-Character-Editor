package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.ItemkindType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.ui2.tree.ItemTreeCellEditor;
import de.earthdawn.ui2.tree.ItemTreeCellRenderer;
import de.earthdawn.ui2.tree.ItemTreeModel;

public class EDInventory extends JPanel {
	private JScrollPane scrollPane;
	private JTree tree;

	private CharacterContainer character;
	

	public CharacterContainer getCharacter() {
		return character;
		
	}

	public void setCharacter(CharacterContainer character) {	
		this.character = character;
		//initTree();
		tree = new BackgroundTree(new ItemTreeModel(character),"templates/inventory_background.jpg");
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setRootVisible(false);
		System.out.println("Set:" + character);
		scrollPane.setViewportView(tree);
		tree.setEditable(true);
		tree.setCellRenderer(new ItemTreeCellRenderer());
		tree.setCellEditor(new ItemTreeCellEditor());
		tree.setInvokesStopCellEditing(true); 
	}

	/**
	 * Create the panel.
	 */
	public EDInventory() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		scrollPane = new JScrollPane();
		add(scrollPane);
			
		tree = new BackgroundTree(new DefaultMutableTreeNode("Empty"),"templates/inventory_background.jpg");
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		scrollPane.add(tree);
		scrollPane.setViewportView(tree);
	}
	protected void do_tree_mouseReleased(MouseEvent event) {
		System.out.println(event.getButton());
		if(event.getButton() > 1){
			PopupMenu popup = new PopupMenu("Action");
			popup.add(new MenuItem("Bala"));
			tree.add(popup);
			System.out.println("Trigger");
			popup.show(tree, event.getX(), event.getY());
		}
	}
}
