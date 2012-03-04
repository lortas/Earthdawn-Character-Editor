package de.earthdawn.ui2;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class EDBackGroundPanel extends JPanel {
	private static final long serialVersionUID = 7069058498214662662L;
	private Image image;
	
	public EDBackGroundPanel(Image img) {
		setOpaque(false);
		image = img;
	}

	@Override
	public void paintComponent(Graphics g) {
		if( image != null ) g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}
}
