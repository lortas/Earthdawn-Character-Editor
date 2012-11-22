package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ARMORType;
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
	public static final String title="Earthdawn Character -- Item Store";
	private JTree tree;
	private Object currentNode; 
	private TreePath currentPath;
	private EDInventory parent;
	private CharacterContainer character=null;

	public EDItemStore(EDInventory parent) {
		super(title);
		this.parent=parent;
		initialize();
	}

	public EDItemStore(EDInventory parent, GraphicsConfiguration gc) {
		super(title,gc);
		this.parent=parent;
		initialize();
	}

	private void initialize() {
		tree = new JTree(new ItemStoreTreeModel(items));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				do_tree_mouseClicked(arg0);
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

		Image image = null;
		try {
			 image = ImageIO.read(new File("images/background/itemstore.jpg"));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		EDBackGroundPanel bp = new EDBackGroundPanel(image);
		bp.setLayout( new BorderLayout() );
		bp.add(scrollPane);

		setContentPane(bp); 
		setSize(new Dimension(466,672));
		setLocationRelativeTo(this.parent);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void setCharacter(CharacterContainer character) {
		this.character=character;
	}

	protected void do_tree_mouseClicked(MouseEvent event) {
		if( character == null ) return;
		if( event.getButton() != MouseEvent.BUTTON3 ) return;
		currentPath = tree.getPathForLocation(event.getX(), event.getY());
		if( currentPath == null ) return;
		currentNode = currentPath.getLastPathComponent();
		if( currentNode == null ) return;
		JPopupMenu popup = new JPopupMenu();

		Object parrentNode = currentPath.getParentPath().getLastPathComponent();
		// Wenn es kein Eltern gibt, dann sind wir an der Wurzel und eine Wurzel kann nicht eingefügt werden;
		if( parrentNode == null ) return;
		// Prüfe obr Elternknoten ein Wurzelknoten ist, dann sind wir zutief im Baum
		if( !(parrentNode instanceof EDInventoryRootNodeType) ) return;

		if(currentNode instanceof THREADITEMType) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADITEMType item = new THREADITEMType();
					CharacterContainer.copyItem((THREADITEMType)currentNode, item);
					character.getThreadItem().add(item);
					character.refesh();
					parent.scrollPathToVisible(item);
				}
			});
			popup.add(menuitem);
		}
		if( (currentNode instanceof ARMORType) && ((EDInventoryRootNodeType)parrentNode).equals(EDInventoryRootNodeType.ARMOR) ) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ARMORType item = CharacterContainer.copyArmor((ARMORType)currentNode,false);
					character.getProtection().getARMOROrSHIELD().add(item);
					character.refesh();
					parent.scrollPathToVisible(item);
				}
			});
			popup.add(menuitem);
		}
		if( (currentNode instanceof SHIELDType) && ((EDInventoryRootNodeType)parrentNode).equals(EDInventoryRootNodeType.SHIELD) ) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ARMORType item = CharacterContainer.copyArmor((ARMORType)currentNode,false);
					character.getProtection().getARMOROrSHIELD().add(item);
					character.refesh();
					parent.scrollPathToVisible(item);
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof WEAPONType) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					WEAPONType item = CharacterContainer.copyWeapon((WEAPONType)currentNode,false);
					character.getWeapons().add(item);
					character.refesh();
					parent.scrollPathToVisible(item);
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof MAGICITEMType) {
			if( ((EDInventoryRootNodeType)parrentNode).equals(EDInventoryRootNodeType.BLOODCHARMS) ) {
				JMenuItem menuitem = new JMenuItem("Add to Character");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						MAGICITEMType item = new MAGICITEMType();
						CharacterContainer.copyItem((MAGICITEMType)currentNode,item);
						character.getBloodCharmItem().add(item);
						character.refesh();
						parent.scrollPathToVisible(item);
					}
				});
				popup.add(menuitem);
			} else
			if( ((EDInventoryRootNodeType)parrentNode).equals(EDInventoryRootNodeType.COMMONMAGICITEMS) ) {
				JMenuItem menuitem = new JMenuItem("Add to Character");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						MAGICITEMType item = new MAGICITEMType();
						CharacterContainer.copyItem((MAGICITEMType)currentNode,item);
						character.getMagicItem().add(item);
						character.refesh();
						parent.scrollPathToVisible(item);
					}
				});
				popup.add(menuitem);
			}
		}
		if(currentNode instanceof ITEMType && ((EDInventoryRootNodeType)parrentNode).equals(EDInventoryRootNodeType.ITEMS) ) {
			JMenuItem menuitem = new JMenuItem("Add to Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ITEMType item = new ITEMType();
					CharacterContainer.copyItem((ITEMType)currentNode,item);
					character.getItems().add(item);
					character.refesh();
					parent.scrollPathToVisible(item);
				}
			});
			popup.add(menuitem);
		}

		tree.add(popup);
		popup.show(tree, event.getX(), event.getY());
	}

	public static void saveItems(Component parent, ITEMS items) {
		File xmlFile = new File("config"+File.separatorChar+"itemstore","characteritems.xml");
		JFileChooser fc = new JFileChooser(new File("config"+File.separatorChar+"itemstore"));
		fc.setSelectedFile(xmlFile);

		// Show open dialog; this method does not return until the dialog is closed
		if( fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION ) return;
		// Only Save file if OK/Yes was pressed
		File file = fc.getSelectedFile();
		if( file != null ) {
			if( file.exists() ) {
				int a = JOptionPane.showOptionDialog(parent,
						EDMainWindow.NLS.getString("Confirmation.Overwrite.text").replace("##FILE##", file.getName()),
						EDMainWindow.NLS.getString("Confirmation.Overwrite.title"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						EDMainWindow.OptionDialog_YesNoOptions,
						EDMainWindow.OptionDialog_YesNoOptions[0]);
				if( a != 0 ) return;
			}
			try {
				final String encoding = EDMainWindow.encoding;
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Marshaller m = jc.createMarshaller();
				FileOutputStream out = new FileOutputStream(file);
				PrintStream fileio = new PrintStream(out, false, encoding);
				m.setProperty(Marshaller.JAXB_ENCODING, encoding);
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/items ../earthdawnitems.xsd");
				m.setProperty(Marshaller.JAXB_FRAGMENT, true);
				fileio.print("<?xml version=\"1.0\" encoding=\""+encoding+"\" standalone=\"no\"?>");
				m.marshal(items,fileio);
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(parent, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
}
