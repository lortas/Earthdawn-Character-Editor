package de.earthdawn.ui2;

import javax.imageio.ImageIO;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import de.earthdawn.ui2.tree.ItemTreeModel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String backgroundImage=null;

	public BackgroundTree(String backgroundImage) {
		super();
		setOpaque(false);
		this.backgroundImage=backgroundImage;
	}

	public BackgroundTree(TreeNode root,String backgroundImage) {
		super(root);
		setOpaque(false);
		this.backgroundImage=backgroundImage;
	}

	public BackgroundTree(ItemTreeModel model,String backgroundImage) {
		super(model);
		setOpaque(false);
		this.backgroundImage=backgroundImage;
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			BufferedImage image = ImageIO.read(new File(backgroundImage));
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.paintComponent(g);
	}
}
