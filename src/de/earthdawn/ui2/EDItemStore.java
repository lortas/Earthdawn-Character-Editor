package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.ITEMS;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.MAGICITEMType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.ui2.tree.ItemStoreTreeModel;
import de.earthdawn.ui2.tree.ItemTreeCellRenderer;

public class EDItemStore extends JFrame {
	private static final long serialVersionUID = 7590657160227976859L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final ITEMS items = PROPERTIES.getItems();
	private JTree tree;
	private BufferedImage backgroundimage = null;
	private Object currentNode; 
	private TreePath currentPath;
	private EDInventory parent;
	private CharacterContainer character=null;

	public EDItemStore(EDInventory parent) throws HeadlessException {
		super("Earthdawn Character Item Store");
		initialize(parent);
	}

	public EDItemStore(EDInventory parent, GraphicsConfiguration gc) {
		super(gc);
		initialize(parent);
	}

	public EDItemStore(EDInventory parent, String title) throws HeadlessException {
		super(title);
		initialize(parent);
	}

	public EDItemStore(EDInventory parent, String title, GraphicsConfiguration gc) {
		super(title, gc);
		initialize(parent);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		Graphics g = getGraphics();
		if( g == null ) return;
		if( backgroundimage == null ) {
			File file = new File("templates/itemstore_background.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
	}

	private void initialize(EDInventory parent) {
		this.parent=parent;
		tree = new JTree(new ItemStoreTreeModel(items));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setOpaque(false);
		tree.setRootVisible(false);
		tree.setEditable(false);
		tree.setCellRenderer(new ItemTreeCellRenderer());
		tree.setInvokesStopCellEditing(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setViewportView(tree);
		scrollPane.getViewport().setOpaque(false);
		setContentPane(scrollPane);
		setLocationRelativeTo(this.parent);
		setSize(new Dimension(500,500));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void setCharacter(CharacterContainer character) {
		this.character=character;
	}

	protected void do_tree_mouseReleased(MouseEvent event) {
		if( character == null ) return;
		if( event.getButton() < 2 ) return;
		currentPath = tree.getPathForLocation(event.getX(), event.getY());
		if( currentPath == null ) return;
		currentNode = currentPath.getLastPathComponent();
		if( currentNode == null ) return;
		JPopupMenu popup = new JPopupMenu();

		if(currentNode instanceof THREADITEMType) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					character.getThreadItem().add((THREADITEMType)currentNode);
					character.refesh();
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof ARMORType) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					character.getProtection().getARMOROrSHIELD().add((ARMORType)currentNode);
					character.refesh();
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof SHIELDType) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					character.getProtection().getARMOROrSHIELD().add((SHIELDType)currentNode);
					character.refesh();
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof WEAPONType) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					character.getWeapons().add((WEAPONType)currentNode);
					character.refesh();
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof MAGICITEMType) {
			Object parrentNode = currentPath.getParentPath().getLastPathComponent();
			if( (parrentNode instanceof String) && ((String)parrentNode).equals("Bloodcharms")) {
				JMenuItem menuitem = new JMenuItem("Add to Character");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						character.getBloodCharmItem().add((MAGICITEMType)currentNode);
						character.refesh();
					}
				});
				popup.add(menuitem);
			}
		}
		if(currentNode instanceof ITEMType) {
			Object parrentNode = currentPath.getParentPath().getLastPathComponent();
			if( (parrentNode instanceof String) && ((String)parrentNode).equals("Items")) {
				JMenuItem menuitem = new JMenuItem("Add to Character");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						character.getItems().add((ITEMType)currentNode);
						character.refesh();
					}
				});
				popup.add(menuitem);
			}
		}

		tree.add(popup);
		popup.show(tree, event.getX(), event.getY());
	}
}
