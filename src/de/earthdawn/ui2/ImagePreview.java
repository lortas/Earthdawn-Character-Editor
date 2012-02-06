package de.earthdawn.ui2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreview extends JComponent implements PropertyChangeListener {
	private static final long serialVersionUID = 5539981601504260965L;
	private ImageIcon icon;
	private int width;

	public ImagePreview() {setPreferredSize(new Dimension(100, 100));}
	public ImagePreview(int x, int y) {setPreferredSize(new Dimension(x, y));}
	public ImagePreview(Dimension dim) {setPreferredSize(dim);}

	public void setPreferredSize(Dimension dim) {
		super.setPreferredSize(dim);
		width=(int) dim.getWidth();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		File file=null;
		if( property.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) ) file = (File) event.getNewValue();
		if( file==null ) return;
		icon = null;
		if( isShowing() ) {
			ImageIcon tmp = new ImageIcon(file.getPath());
			if (tmp != null) {
				icon = new ImageIcon(tmp.getImage().getScaledInstance(width,-1,Image.SCALE_DEFAULT));
			}
			repaint();
		}
	}

	protected void paintComponent(Graphics g) {
		if( icon == null ) return;
		int x = getWidth()/2 - icon.getIconWidth()/2;
		int y = getHeight()/2 - icon.getIconHeight()/2;
		if (y < 0) y = 0;
		if (x < 0) x = 0;
		icon.paintIcon(this, g, x, y);
	}
}
