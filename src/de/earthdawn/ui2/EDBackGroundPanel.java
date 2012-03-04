package de.earthdawn.ui2;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class EDBackGroundPanel extends JPanel {
	private Image image;
	
	public EDBackGroundPanel(Image ing)
	{
		setOpaque(false);
		image = ing;
		
	}

	@Override
	public void paintComponent(Graphics g)
	{
		if(image != null)
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}

}
