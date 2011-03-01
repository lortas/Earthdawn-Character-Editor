package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.text.EditorKit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import com.itextpdf.text.DocumentException;

import de.earthdawn.CharacterContainer;
import de.earthdawn.CharacteristicStatus;
import de.earthdawn.ECEPdfExporter;
import de.earthdawn.ECEWorker;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.event.CharChangeEventListener;

public class EDMainWindow {

	private static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS"); //$NON-NLS-1$

	private JFrame frame;
	private EDCHARACTER ec;
	private CharacterContainer character;
	private JTabbedPane tabbedPane;
	private EDGeneral panelERGeneral;
	private EDAttributes panelEDAttributes;
	private EDDisciplines panelEDDisciplines;
	private EDSpells panelEDSpells;
	private EDTalents panelEDTalents;
	private File file = null;
	private JSplitPane splitPane;
	private EDSkills panelEDSkills;
	private JEditorPane paneStatus;
	private CharacteristicStatus characteristicStatus;
	
	
	
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
		this.character.addCharChangeEventListener(new CharChangeEventListener() {
			@Override
			public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
				ECEWorker worker = new ECEWorker();
			    worker.verarbeiteCharakter(character.getEDCHARACTER());
				addTalentsTabs();
				refreshTabs();
			}
		});		
		refreshTabs();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 740);
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
		

		JMenu mntmExport = new JMenu(NLS.getString("EDMainWindow.mntmExport.text")); //$NON-NLS-1$
		mnFile.add(mntmExport);

		JMenuItem mntmExportRedbrickSimple = new JMenuItem(NLS.getString("EDMainWindow.mntmExportRedbrickSimple.text")); //$NON-NLS-1$
		mntmExportRedbrickSimple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,1);
			}
		});
		mntmExport.add(mntmExportRedbrickSimple);

		JMenuItem mntmExportRedbrickExtended = new JMenuItem(NLS.getString("EDMainWindow.mntmExportRedbrickExtended.text")); //$NON-NLS-1$
		mntmExportRedbrickExtended.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,0);
			}
		});
		mntmExport.add(mntmExportRedbrickExtended);

		JMenuItem mntmExportAjfelmordom = new JMenuItem(NLS.getString("EDMainWindow.mntmExportAjfelmordom.text")); //$NON-NLS-1$
		mntmExportAjfelmordom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,2);
			}
		});
		mntmExport.add(mntmExportAjfelmordom);

		JMenuItem mntmClose = new JMenuItem(NLS.getString("EDMainWindow.mntmClose.text")); //$NON-NLS-1$
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmClose_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmClose);

		JMenu mnView = new JMenu(NLS.getString("EDMainWindow.mnView.text")); //$NON-NLS-1$
		menuBar.add(mnView);

		JMenuItem mntmWebBrowser= new JMenuItem(NLS.getString("EDMainWindow.mntmWebBrowser.text")); //$NON-NLS-1$
		mntmWebBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmWebBrowser_actionPerformed(arg0);
			}
		});
		mnView.add(mntmWebBrowser);

		JMenu mnHelp = new JMenu(NLS.getString("EDMainWindow.mnHelp.text")); //$NON-NLS-1$
		menuBar.add(mnHelp);

		JMenuItem mntmAbout= new JMenuItem(NLS.getString("EDMainWindow.mntmAbout.text")); //$NON-NLS-1$
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmAbout_actionPerformed(arg0);
			}
		});
		mnHelp.add(mntmAbout);

		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		splitPane.setLeftComponent(tabbedPane);

		panelERGeneral = new EDGeneral();
		panelERGeneral.setMinimumSize(new Dimension(4, 220));
		panelEDAttributes = new EDAttributes();
		panelEDDisciplines = new EDDisciplines();
		panelEDSpells	= new EDSpells();
		panelEDSkills = new EDSkills();
		paneStatus = new JEditorPane();
		EditorKit kit = paneStatus.getEditorKitForContentType("text/html");
		paneStatus.setEditorKit(kit);
		paneStatus.setEditable(false);
		characteristicStatus = new CharacteristicStatus("characteristic_layout.html");
		characteristicStatus.setCharacter(character);

		tabbedPane.addTab("General", null, panelERGeneral, null);
		tabbedPane.addTab("Attributes", null, panelEDAttributes, null);
		tabbedPane.addTab("Disciplines", null, panelEDDisciplines, null);
		tabbedPane.addTab("Spells", null, panelEDSpells, null);
		tabbedPane.addTab("Skills", null, panelEDSkills, null);

		splitPane.setRightComponent(paneStatus);
	}

	private void do_mntmAbout_actionPerformed(ActionEvent arg0) {
		//TODO Fenster öffnen und HELP.ABOUT darin anzeigen
	}

	private void addTalentsTabs(){
		List<String> allTalentTabs = new ArrayList<String>();
		for(Component co  : tabbedPane.getComponents() )
		{
			if(co.getClass() == EDTalents.class){
				// löschen wenn die Disciplin nicht mehr vorhanden ist
				if(!character.getAllDiciplinesByName().containsKey(((EDTalents)co).getDisciplin())){
					tabbedPane.remove(co);
				}
				else{
					allTalentTabs.add(((EDTalents)co).getDisciplin());
				}
			}
		}
			
		HashMap<Integer, DISCIPLINEType> allDicipines = character.getAllDiciplinesByOrder();
		for(Integer key : allDicipines.keySet()){
			String diciplinName = allDicipines.get(key).getName();
			// wenn tab nicht beteits vorhanden -> hinzufügen
			if(!allTalentTabs.contains(diciplinName)){
				panelEDTalents = new EDTalents(diciplinName);
				panelEDTalents.setCharacter(character);
				tabbedPane.addTab("Talents (" + diciplinName + ")", null, panelEDTalents, null);	
			}
		}
	}
	
	private void refreshTabs(){
		StringWriter out = new StringWriter();
		characteristicStatus.parseTo(out);
		paneStatus.setText(out.toString());

		for(Component co  : tabbedPane.getComponents() )
		{
			if(co.getClass() == EDTalents.class){
				((EDTalents)co).setCharacter(character);
			}
			if(co.getClass() == EDGeneral.class){
				((EDGeneral)co).setCharacter(character);
			}
			if(co.getClass() == EDAttributes.class){
				((EDAttributes)co).setCharacter(character);
			}
			if(co.getClass() == EDDisciplines.class){
				((EDDisciplines)co).setCharacter(character);
			}
			if(co.getClass() == EDStatus.class){
				((EDStatus)co).setCharacter(character);
			}
			if(co.getClass() == EDSpells.class){
				((EDSpells)co).setCharacter(character);
			}	
			if(co.getClass() == EDSkills.class){
				((EDSkills)co).setCharacter(character);
			}
		}
	}

	protected void do_mntmSave_actionPerformed(ActionEvent arg0) {
		if( file != null ) {
			try{
				writeToXml(file);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} else {
			do_mntmSaveAs_actionPerformed(arg0);
		}
	}

	protected void do_mntmSaveAs_actionPerformed(ActionEvent arg0) {
		String name = character.getName();
		if( name == null ) name = "noname";
		File xmlFile = new File(name.replaceAll(" ", "_") + ".xml");
		JFileChooser fc = new JFileChooser(new File("."));
		fc.setSelectedFile(xmlFile);

		// Show open dialog; this method does not return until the dialog is closed
		fc.showSaveDialog(frame); 
		File selFile = fc.getSelectedFile(); // Show save dialog; this method does not return until the dialog is closed fc.showSaveDialog(frame);
		//TODO: Wenn abbrechen gedrückt wurde, dann darf auch nicht gespeichert werden
		if( selFile != null ) {
			file = selFile;
			try{
				writeToXml(file);
				copyCharacterAdditionalFiles(file.getParentFile());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void writeToXml(File file) throws JAXBException, PropertyException, IOException {
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		FileWriter fileio = new FileWriter(file);
		fileio.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		fileio.write("<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>\n");
		m.marshal(ec,fileio);
		fileio.close();
	}
	
	protected  void do_mntmOpen_actionPerformed(ActionEvent arg0) {
		String filename = "."; 
		JFileChooser fc = new JFileChooser(new File(filename)); 

		// Show open dialog; this method does not return until the dialog is closed 
		fc.showOpenDialog(frame); 
		File selFile = fc.getSelectedFile(); // Show save dialog; this method does not return until the dialog is closed fc.showSaveDialog(frame);
		if(selFile != null){
			file = selFile;
			try{
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Unmarshaller u = jc.createUnmarshaller();					
				ec =(EDCHARACTER)u.unmarshal(selFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}
				
			character = new CharacterContainer(ec);
			ECEWorker worker = new ECEWorker();
			worker.verarbeiteCharakter(character.getEDCHARACTER());
			this.character.addCharChangeEventListener(new CharChangeEventListener() {
				@Override
				public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
					ECEWorker worker = new ECEWorker();
					worker.verarbeiteCharakter(character.getEDCHARACTER());
					addTalentsTabs();
					refreshTabs();
				}
			});				
			addTalentsTabs();
			characteristicStatus.setCharacter(character);
			refreshTabs();
		}
	}
	protected void do_mntmClose_actionPerformed(ActionEvent arg0) {
		frame.dispose();
	}

	protected void do_mntmExport_actionPerformed(ActionEvent arg0, int v) {
		if( file == null ) {
			String name = character.getName();
			if( name == null ) name = "noname";
			file = new File(name.replaceAll(" ", "_") + ".xml");
		}
		File pdfFile = new File(file.getParentFile(), chopFilename(file)+ ".pdf");
		JFileChooser fc = new JFileChooser(pdfFile);
		fc.setSelectedFile(pdfFile);
		fc.showSaveDialog(frame);
		File selFile = fc.getSelectedFile(); // Show save dialog; this method does not return until the dialog is closed fc.showSaveDialog(frame);
		if( selFile != null ) {
			try {
				switch(v) {
				case 1 : new ECEPdfExporter().exportRedbrickSimple(character.getEDCHARACTER(), selFile); break;
				case 2 : new ECEPdfExporter().exportAjfelMordom(character.getEDCHARACTER(), selFile); break;
				default: new ECEPdfExporter().exportRedbrickExtended(character.getEDCHARACTER(), selFile); break;
				}
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(selFile);
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmWebBrowser_actionPerformed(ActionEvent arg0) {
		if( ! Desktop.isDesktopSupported() ) {
			//TODO: Infofenster öffnen und Fehler anzeigen.
			return;
		}
		try {
			String name = character.getName();
			if( name == null ) name = "noname";
			File tmpfile = File.createTempFile(name.replaceAll(" ", "_"), ".xml");
			writeToXml(tmpfile);
			copyCharacterAdditionalFiles(tmpfile.getParentFile());
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(tmpfile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
}

	protected String chopFilename(File f){
		String choppedFilename;
		String filename = f.getName();
		// where the last dot is. There may be more than one.
		int dotPlace = filename.lastIndexOf ( '.' );

		if ( dotPlace >= 0 )
		   {
		   // possibly empty
		   choppedFilename = filename.substring( 0, dotPlace );
		   }
		else
		   {
		   // was no extension
		   choppedFilename = filename;
		   }	
		return choppedFilename;
	}
	
	protected void do_mntmNew_actionPerformed(ActionEvent arg0) {
		ec = new EDCHARACTER();
		character = new CharacterContainer(ec);
		ECEWorker worker = new ECEWorker();
	    worker.verarbeiteCharakter(character.getEDCHARACTER());
		this.character.addCharChangeEventListener(new CharChangeEventListener() {
			@Override
			public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
				ECEWorker worker = new ECEWorker();
			    worker.verarbeiteCharakter(character.getEDCHARACTER());
				addTalentsTabs();
				refreshTabs();
			}
		});	
		addTalentsTabs();
		characteristicStatus.setCharacter(character);
		refreshTabs();
	}

	public static void copyFile (String source, String destination) throws IOException {
		FileReader in = new FileReader(new File(source));
		FileWriter out = new FileWriter(new File(destination));
		int c=0;
		while ((c = in.read()) != -1) out.write(c);
		in.close();
		out.close();
	}

	public static void copyCharacterAdditionalFiles(File path) throws IOException {
		copyFile("./config/earthdawncharacter.xsd",new File( path, "earthdawncharacter.xsd" ).getCanonicalPath());
		copyFile("./config/earthdawncharacter.xsl",new File( path, "earthdawncharacter.xsl" ).getCanonicalPath());
		copyFile("./config/earthdawncharacter.css",new File( path, "earthdawncharacter.css" ).getCanonicalPath());
		copyFile("./config/earthdawndatatypes.xsd",new File( path, "earthdawndatatypes.xsd" ).getCanonicalPath());
	}
}
