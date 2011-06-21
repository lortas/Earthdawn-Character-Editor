package de.earthdawn.ui2;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundEditorPane extends JEditorPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String backgroundImage=null;

	public BackgroundEditorPane(String backgroundImage) {
		super();
		setOpaque(false);
		// this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
		setBackground(new Color(0,0,0,0));
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
