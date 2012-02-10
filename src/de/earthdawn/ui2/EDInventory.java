package de.earthdawn.ui2;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ui2.tree.ItemTreeCellEditor;
import de.earthdawn.ui2.tree.ItemTreeCellRenderer;
import de.earthdawn.ui2.tree.ItemTreeModel;

import de.earthdawn.data.*;

public class EDInventory extends JPanel {
	private static final long serialVersionUID = 2284943257141442648L;
	private JScrollPane scrollPane;
	private JTree tree;

	private CharacterContainer character;
	private Object currentNode; 
	private TreePath currentPath;
	private BufferedImage backgroundimage = null;

	public CharacterContainer getCharacter() {
		return character;

	}

	public void setCharacter(CharacterContainer character) {	
		this.character = character;
		tree = new JTree(new ItemTreeModel(character));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setOpaque(false);
		tree.setRootVisible(false);
		scrollPane.setViewportView(tree);
		scrollPane.getViewport().setOpaque(false);
		tree.setEditable(true);
		tree.setCellRenderer(new ItemTreeCellRenderer());
		tree.setCellEditor(new ItemTreeCellEditor());
		tree.setInvokesStopCellEditing(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("templates/inventory_background.jpg");
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
	public EDInventory() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setOpaque(false);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane);

		tree = new JTree(new ItemTreeModel(character));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setOpaque(false);
		scrollPane.add(tree);
		scrollPane.setViewportView(tree);
		scrollPane.getViewport().setOpaque(false);
	}

	protected void do_tree_mouseReleased(MouseEvent event) {
		if( event.getButton() < 2 ) return;
		currentPath = tree.getPathForLocation(event.getX(), event.getY());
		if( currentPath == null ) return;
		currentNode = currentPath.getLastPathComponent();
		if( currentNode == null ) return;
		JPopupMenu popup = new JPopupMenu();

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
						MAGICITEMType bloodcharm = new MAGICITEMType();
						bloodcharm.setName("New Bloodcharm");
						character.getBloodCharmItem().add(bloodcharm);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,bloodcharm, character.getBloodCharmItem().indexOf(bloodcharm));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(bloodcharm));
					}
				});
				popup.add(menuitem);
			}
			// add purse
			if(str.equals("Purse")){
				JMenuItem menuitem = new JMenuItem("Add Purse");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						COINSType coins = new COINSType();
						coins.setKind(ItemkindType.GEMS);
						character.getAllCoins().add(coins);
						coins.setName("Purse #"+(1+character.getAllCoins().indexOf(coins)));
						((ItemTreeModel) tree.getModel()).fireNewCoins(currentPath,character.getAllCoins());
						tree.scrollPathToVisible(currentPath.pathByAddingChild(coins));
					}
				});
				popup.add(menuitem);
			}
		}
		if(currentNode instanceof THREADITEMType) {
			JMenuItem menuitem = new JMenuItem("Add Rank");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADITEMType item = (THREADITEMType) currentNode;
					THREADRANKType rank = new THREADRANKType();
					rank.setEffect("");
					rank.setKeyknowledge("");
					item.getTHREADRANK().add(rank);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,item, item.getTHREADRANK().indexOf(rank));
					tree.scrollPathToVisible(currentPath.pathByAddingChild(rank));
				}
			});
			popup.add(menuitem);
		}

		//remove
		Object parent = currentPath.getParentPath().getLastPathComponent();
		if(parent instanceof String){
			List<?> temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((String)parent);
			if(temp != null){
				JMenuItem menuitem = new JMenuItem("Remove");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Object parent = currentPath.getParentPath().getLastPathComponent();
						List<?> temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((String)parent);
						int i =  temp.indexOf(currentNode);
						temp.remove(currentNode);
						((ItemTreeModel) tree.getModel()).fireRemove(currentPath.getParentPath(),currentNode,i );
					}
				});
				popup.add(menuitem);	
			}
		}

		tree.add(popup);
		popup.show(tree, event.getX(), event.getY());
	}
}
