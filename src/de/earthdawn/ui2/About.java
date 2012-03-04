package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class About extends JFrame {
	private static final long serialVersionUID = -2055110507521896101L;
	private BufferedImage backgroundimage = null;
	private JFrame parent;
	public static final String title="Earthdawn Character Editor -- About";

	public About(JFrame parent) throws HeadlessException {
		super(title);
		this.parent=parent;
		initialize();
	}

	public About(JFrame parent,GraphicsConfiguration gc) {
		super(title,gc);
		this.parent=parent;
		initialize();
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	public void setParent(JFrame parent) {
		this.parent=parent;
	}

	private void initialize() {
		
		JEditorPane editorpane = new JEditorPane();
		
		File file = new File("documentation/about.html");
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fileInputStream.read(data);
			fileInputStream.close();
			editorpane.setEditorKit(editorpane.getEditorKitForContentType("text/html"));
			editorpane.setText(new String(data));
		} catch (IOException e) {
			editorpane.setEditorKit(editorpane.getEditorKitForContentType("text"));
			editorpane.setText(e.getLocalizedMessage());
		}
		editorpane.setEditable(false);
		editorpane.setFocusable(false);
		editorpane.setOpaque(false);
		
		JScrollPane scrollPane = new JScrollPane(editorpane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setOpaque(false);
		// this is the key !!!
		scrollPane.getViewport().setOpaque(false);
		
		Image image = null;
		try {
			 image = ImageIO.read(new File("images/background/genralpanel.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		EDBackGroundPanel bp = new EDBackGroundPanel(image);
		bp.setLayout( new BorderLayout() );
		bp.add(scrollPane);
		
		
		setContentPane(bp); 
		setLocationRelativeTo(this.parent);
		setSize(new Dimension(500,500));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	

	
}
