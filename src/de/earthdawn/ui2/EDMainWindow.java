package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.TALENTSType;
import de.earthdawn.event.CharChangeEventListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import java.awt.Dimension;

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
	private EDStatus panelEDStatus;
	private EDSkills panelEDSkills;
	
	
	
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
		frame.setBounds(100, 100, 620, 550);
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
		mntmPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTalentsTabs();
			}
		});
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
		
		tabbedPane.addTab("General", null, panelERGeneral, null);
		tabbedPane.addTab("Attributes", null, panelEDAttributes, null);
		tabbedPane.addTab("Disciplines", null, panelEDDisciplines, null);
		tabbedPane.addTab("Spells", null, panelEDSpells, null);
		tabbedPane.addTab("Skills", null, panelEDSkills, null);
		
		panelEDStatus = new EDStatus();
		splitPane.setRightComponent(panelEDStatus);
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
		panelEDStatus.setCharacter(character);
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
	
	

	protected  void do_mntmSave_actionPerformed(ActionEvent arg0) {
		if( file != null ) {
			try{
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
				m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				FileWriter fileio = new FileWriter(file);
				fileio.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
				fileio.write("<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>\n");
				m.marshal(ec,fileio);
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
		if( selFile != null ) {
			file = selFile;
			try{
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
				m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				FileWriter fileio = new FileWriter(selFile);
				fileio.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
				fileio.write("<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>\n");
				m.marshal(ec,fileio);
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
			refreshTabs();
		}
	}
	protected void do_mntmClose_actionPerformed(ActionEvent arg0) {
		frame.dispose();
	}
	protected void do_mntmExport_actionPerformed(ActionEvent arg0) {
		try {
			new ECEPdfExporter().export(character.getEDCHARACTER(), new File(file.getParentFile(), chopFilename(file)+ ".pdf"));
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
		refreshTabs();
	}
}
