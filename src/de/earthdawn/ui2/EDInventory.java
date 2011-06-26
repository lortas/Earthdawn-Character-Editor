package de.earthdawn.ui2;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ui2.tree.ItemTreeCellEditor;
import de.earthdawn.ui2.tree.ItemTreeCellRenderer;
import de.earthdawn.ui2.tree.ItemTreeModel;

import de.earthdawn.data.*;

public class EDInventory extends JPanel {
	private JScrollPane scrollPane;
	private JTree tree;

	private CharacterContainer character;
	private Object currentNode; 
	private TreePath currentPath;
	
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
		if(event.getButton() > 1){
			JPopupMenu popup = new JPopupMenu();
			currentPath = tree.getPathForLocation(event.getX(), event.getY());
			currentNode = currentPath.getLastPathComponent();
			
			// Popup for group nodes
			if(currentNode instanceof String){
				String str = (String)currentNode;
				// add item
				if(str.equals("Items")){
					JMenuItem menuitem = new JMenuItem("Add item");
					menuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							ITEMType item = new ITEMType();
							item.setName("New Item");
							character.getItems().add(item);
							((ItemTreeModel) tree.getModel()).fireAdd(currentPath,item, character.getItems().indexOf(item));
							tree.scrollPathToVisible(currentPath.pathByAddingChild(item));
						}
					});
					popup.add(menuitem);
				}
				// add weapon
				if(str.equals("Weapons")){
					JMenuItem menuitem = new JMenuItem("Add weapon");
					menuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							WEAPONType weapon = new WEAPONType();
							weapon.setName("New weapon");
							character.getWeapons().add(weapon);
							((ItemTreeModel) tree.getModel()).fireAdd(currentPath,weapon, character.getWeapons().indexOf(weapon));
							tree.scrollPathToVisible(currentPath.pathByAddingChild(weapon));
						}
					});
					popup.add(menuitem);
				}		
				//add threaditem
				if(str.equals("Thread Items")){
					JMenuItem menuitem = new JMenuItem("Add Thread Item");
					menuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							THREADITEMType threaditem = new THREADITEMType();
							threaditem.setName("New Thread Item");
							character.getThreadItem().add(threaditem);
							((ItemTreeModel) tree.getModel()).fireAdd(currentPath,threaditem, character.getThreadItem().indexOf(threaditem));
							tree.scrollPathToVisible(currentPath.pathByAddingChild(threaditem));
						}
					});
					popup.add(menuitem);
				}	
				// add armor
				if(str.equals("Armor")){
					JMenuItem menuitem = new JMenuItem("Add Armor");
					menuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							ARMORType armor = new ARMORType();
							armor.setName("New Armor");
							character.getProtection().getARMOROrSHIELD().add(armor);
							((ItemTreeModel) tree.getModel()).fireAdd(currentPath,armor, character.getProtection().getARMOROrSHIELD().indexOf(armor));
							tree.scrollPathToVisible(currentPath.pathByAddingChild(armor));
						}
					});
					popup.add(menuitem);
				}	
				// add bloodcharm
				if(str.equals("Bloodcharms")){
					JMenuItem menuitem = new JMenuItem("Add Bloodcharms");
					menuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							BLOODCHARMITEMType bloodcharm = new BLOODCHARMITEMType();
							bloodcharm.setName("New Bloodcharm");
							character.getBloodCharmItem().add(bloodcharm);
							((ItemTreeModel) tree.getModel()).fireAdd(currentPath,bloodcharm, character.getBloodCharmItem().indexOf(bloodcharm));
							tree.scrollPathToVisible(currentPath.pathByAddingChild(bloodcharm));
						}
					});
					popup.add(menuitem);
				}				
				
			}
			
			//remove
			Object parent = currentPath.getParentPath().getLastPathComponent();
			if(parent instanceof String){
				List temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((String)parent);
				if(temp != null){
					JMenuItem menuitem = new JMenuItem("Remove");
					menuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							Object parent = currentPath.getParentPath().getLastPathComponent();
							List temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((String)parent);
							temp.remove(currentNode); 
							((ItemTreeModel) tree.getModel()).fireRemove(currentPath.getParentPath(),currentNode, 0);
							
						}
					});
					popup.add(menuitem);	
				}
			}
			
			tree.add(popup);
			popup.show(tree, event.getX(), event.getY());
		}
	}
}
