package de.earthdawn.ui2;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.itextpdf.text.DocumentException;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEPdfExporter;
import de.earthdawn.ECEWorker;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EDMainWindow {

	private static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS"); //$NON-NLS-1$

	private JFrame frame;
	private EDCHARACTER ec;
	private CharacterContainer character;
	private JTabbedPane tabbedPane;
	private EDGeneral panelERGeneral;
	private EDAttributes panelEDAttributes;
	private EDDisciplines panelEDDisciplines;
	private EDTalents panelEDTalents;
	private File file = null;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EDMainWindow window = new EDMainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EDMainWindow() {
		ec = new EDCHARACTER();
		character = new CharacterContainer(ec);
		ECEWorker worker = new ECEWorker();
	    worker.verarbeiteCharakter(character.getEDCHARACTER());
	    initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu(NLS.getString("EDMainWindow.mnFile.text")); //$NON-NLS-1$
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem(NLS.getString("EDMainWindow.mntmNew.text"));
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmNew_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem(NLS.getString("EDMainWindow.mntmOpen.text")); //$NON-NLS-1$
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmOpen_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem(NLS.getString("EDMainWindow.mntmSave.text")); //$NON-NLS-1$
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmSave_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem(NLS.getString("EDMainWindow.mntmSaveAs.text")); //$NON-NLS-1$
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmSaveAs_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmSaveAs);
		
		JMenuItem mntmPrint = new JMenuItem(NLS.getString("EDMainWindow.mntmPrint.text")); //$NON-NLS-1$
		mnFile.add(mntmPrint);
		
		JMenuItem mntmExport = new JMenuItem(NLS.getString("EDMainWindow.mntmExport.text")); //$NON-NLS-1$
		mntmExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmExport);
		
		JMenuItem mntmClose = new JMenuItem(NLS.getString("EDMainWindow.mntmClose.text")); //$NON-NLS-1$
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmClose_actionPerformed(arg0);
			}
		});

		mnFile.add(mntmClose);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		panelERGeneral = new EDGeneral();
		panelEDAttributes = new EDAttributes();
		panelEDTalents = new EDTalents();
		panelEDDisciplines = new EDDisciplines();
		
		tabbedPane.addTab("General", null, panelERGeneral, null);
		tabbedPane.addTab("Attributes", null, panelEDAttributes, null);
		tabbedPane.addTab("Disciplines", null, panelEDDisciplines, null);
		tabbedPane.addTab("Talents", null, panelEDTalents, null);
		
		panelERGeneral.setCharacter(character);
		panelEDAttributes.setCharacter(character);
		panelEDTalents.setCharacter(character);
		panelEDDisciplines.setCharacter(character);
	
	}
	
	

	protected  void do_mntmSave_actionPerformed(ActionEvent arg0) {
		System.out.println("Save");
		if(file != null){
			System.out.println(file.getName());
			try{
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(ec,new FileWriter(file));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	protected void do_mntmSaveAs_actionPerformed(ActionEvent arg0) {
		String filename = "."; 
		JFileChooser fc = new JFileChooser(new File(filename)); 

		// Show open dialog; this method does not return until the dialog is closed 
		fc.showSaveDialog(frame); 
		File selFile = fc.getSelectedFile(); // Show save dialog; this method does not return until the dialog is closed fc.showSaveDialog(frame);
		if(selFile != null){
			file = selFile;
			System.out.println(selFile.getName());
			try{
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(ec,new FileWriter(selFile));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	protected  void do_mntmOpen_actionPerformed(ActionEvent arg0) {
		String filename = "."; 
		JFileChooser fc = new JFileChooser(new File(filename)); 

		// Show open dialog; this method does not return until the dialog is closed 
		fc.showOpenDialog(frame); 
		File selFile = fc.getSelectedFile(); // Show save dialog; this method does not return until the dialog is closed fc.showSaveDialog(frame);
		if(selFile != null){
			file = selFile;
			System.out.println(selFile.getName());
			try{
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Unmarshaller u = jc.createUnmarshaller();					
				ec =(EDCHARACTER)u.unmarshal(selFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Name: " + ec.getName());	
			character = new CharacterContainer(ec);
			panelERGeneral.setCharacter(character);
			panelEDAttributes.setCharacter(character);
			panelEDTalents.setCharacter(character);
			panelEDDisciplines.setCharacter(character);
		}
	}
	protected void do_mntmClose_actionPerformed(ActionEvent arg0) {
		frame.dispose();
		System.out.println("Close") ;
	}
	protected void do_mntmExport_actionPerformed(ActionEvent arg0) {
		try {
			new ECEPdfExporter().export(character.getEDCHARACTER(), new File(file.getParentFile(), chopFilename(file)+ ".pdf"));
			System.out.println("Export");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected String chopFilename(File f){
		String choppedFilename;
		String filename = f.getName();

		// extension without the dot
		String ext;

		// where the last dot is. There may be more than one.
		int dotPlace = filename.lastIndexOf ( '.' );

		if ( dotPlace >= 0 )
		   {
		   // possibly empty
		   choppedFilename = filename.substring( 0, dotPlace );

		   // possibly empty
		   ext = filename.substring( dotPlace + 1 );
		   }
		else
		   {
		   // was no extension
		   choppedFilename = filename;
		   ext = "";
		   }	
		return choppedFilename;
	}
	
	protected void do_mntmNew_actionPerformed(ActionEvent arg0) {
		ec = new EDCHARACTER();
		character = new CharacterContainer(ec);
		ECEWorker worker = new ECEWorker();
	    worker.verarbeiteCharakter(character.getEDCHARACTER());
		panelERGeneral.setCharacter(character);
		panelEDAttributes.setCharacter(character);
		panelEDTalents.setCharacter(character);
		panelEDDisciplines.setCharacter(character);
	}
}
