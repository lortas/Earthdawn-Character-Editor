package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ITEMS;
import de.earthdawn.ui2.tree.ItemStoreTreeModel;
import de.earthdawn.ui2.tree.ItemTreeCellEditor;
import de.earthdawn.ui2.tree.ItemTreeCellRenderer;

public class EDItemStore extends JFrame {
	private static final long serialVersionUID = 7590657160227976859L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final ITEMS items = PROPERTIES.getItems();
	private JTree tree;
	private BufferedImage backgroundimage = null;
	private Object currentNode; 
	private TreePath currentPath;
	private Component parent;

	public EDItemStore(Component parent) throws HeadlessException {
		super("Earthdawn Character Item Store");
		initialize(parent);
	}

	public EDItemStore(Component parent, GraphicsConfiguration gc) {
		super(gc);
		initialize(parent);
	}

	public EDItemStore(Component parent, String title) throws HeadlessException {
		super(title);
		initialize(parent);
	}

	public EDItemStore(Component parent, String title, GraphicsConfiguration gc) {
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

	private void initialize(Component parent) {
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
		setLocationRelativeTo(parent);
		setSize(new Dimension(500,500));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	protected void do_tree_mouseReleased(MouseEvent event) {
		if( event.getButton() < 2 ) return;
		currentPath = tree.getPathForLocation(event.getX(), event.getY());
		if( currentPath == null ) return;
		currentNode = currentPath.getLastPathComponent();
		if( currentNode == null ) return;
		JPopupMenu popup = new JPopupMenu();
		tree.add(popup);
		popup.show(tree, event.getX(), event.getY());
	}
}
