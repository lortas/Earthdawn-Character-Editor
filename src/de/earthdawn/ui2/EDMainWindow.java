package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.text.EditorKit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;

import de.earthdawn.CharacterContainer;
import de.earthdawn.CharacteristicStatus;
import de.earthdawn.ECECsvExporter;
import de.earthdawn.ECEPdfExporter;
import de.earthdawn.ECEWorker;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.TALENTType;
import de.earthdawn.event.CharChangeEventListener;

public class EDMainWindow {

	private static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS"); //$NON-NLS-1$
	private static final String encoding="UTF-8";

	private JFrame frame;
	private EDCHARACTER ec;
	private CharacterContainer character;
	private JTabbedPane tabbedPane;
	private EDGeneral panelERGeneral;
	private EDAttributes panelEDAttributes;
	private EDDisciplines panelEDDisciplines;
	private EDExperience panelEDExperience;
	private EDKarma panelEDKarma;
	private EDDevotionPoints panelEDDevotionPoints;
	private EDInventory panelEDThreadItems;
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
				addTalentsAndSpellsTabs();
				refreshTabs();
			}
		});		
		refreshTabs();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Earthdawn Character Editor");
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
		menuBar.add(mntmExport);

		JMenu mntmPdfExport = new JMenu(NLS.getString("EDMainWindow.mntmPdfExport.text")); //$NON-NLS-1$
		mntmExport.add(mntmPdfExport);

		JMenuItem mntmExportRedbrickSimple = new JMenuItem(NLS.getString("EDMainWindow.mntmExportRedbrickSimple.text")); //$NON-NLS-1$
		mntmExportRedbrickSimple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,1);
			}
		});
		mntmPdfExport.add(mntmExportRedbrickSimple);

		JMenuItem mntmExportRedbrickExtended = new JMenuItem(NLS.getString("EDMainWindow.mntmExportRedbrickExtended.text")); //$NON-NLS-1$
		mntmExportRedbrickExtended.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,0);
			}
		});
		mntmPdfExport.add(mntmExportRedbrickExtended);

		JMenuItem mntmExportAjfelmordom = new JMenuItem(NLS.getString("EDMainWindow.mntmExportAjfelmordom.text")); //$NON-NLS-1$
		mntmExportAjfelmordom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,2);
			}
		});
		mntmPdfExport.add(mntmExportAjfelmordom);

		JMenu mntmCsvExport = new JMenu(NLS.getString("EDMainWindow.mntmCsvExport.text")); //$NON-NLS-1$
		mntmExport.add(mntmCsvExport);

		JMenuItem mntmSpellCSV = new JMenuItem(NLS.getString("EDMainWindow.mntmSpellCSV.text")); //$NON-NLS-1$
		mntmSpellCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmSpellCSV_actionPerformed(arg0);
			}
		});
		mntmCsvExport.add(mntmSpellCSV);

		JMenuItem mntmTalentCSV = new JMenuItem(NLS.getString("EDMainWindow.mntmTalentCSV.text")); //$NON-NLS-1$
		mntmTalentCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmTalentCSV_actionPerformed(arg0);
			}
		});
		mntmCsvExport.add(mntmTalentCSV);

		JMenuItem mntmSkillCSV = new JMenuItem(NLS.getString("EDMainWindow.mntmSkillCSV.text")); //$NON-NLS-1$
		mntmSkillCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmSkillCSV_actionPerformed(arg0);
			}
		});
		mntmCsvExport.add(mntmSkillCSV);

		JMenuItem mntmItemCSV = new JMenuItem(NLS.getString("EDMainWindow.mntmItemCSV.text")); //$NON-NLS-1$
		mntmItemCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmItemCSV_actionPerformed(arg0);
			}
		});
		mntmCsvExport.add(mntmItemCSV);

		JMenu mntmJsonExport = new JMenu(NLS.getString("EDMainWindow.mntmJson.text")); //$NON-NLS-1$
		mntmExport.add(mntmJsonExport);

		JMenuItem mntmJson = new JMenuItem(NLS.getString("EDMainWindow.mntmXML2Json.text")); //$NON-NLS-1$
		mntmJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmJson_actionPerformed(arg0);
			}
		});
		mntmJsonExport.add(mntmJson);

		JMenuItem mntmGson = new JMenuItem(NLS.getString("EDMainWindow.mntmObject2Json.text")); //$NON-NLS-1$
		mntmGson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmGson_actionPerformed(arg0);
			}
		});
		mntmJsonExport.add(mntmGson);

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

		JMenu mnExtra = new JMenu(NLS.getString("EDMainWindow.mnExtra.text")); //$NON-NLS-1$
		menuBar.add(mnExtra);

		JMenuItem mntmDicing= new JMenuItem(NLS.getString("EDMainWindow.mntmDicing.text")); //$NON-NLS-1$
		mntmDicing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmDicing_actionPerformed(arg0);
			}
		});
		mnExtra.add(mntmDicing);

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
		panelEDSkills = new EDSkills();
		panelEDExperience = new EDExperience();
		panelEDKarma = new EDKarma();
		panelEDDevotionPoints = new EDDevotionPoints();
		panelEDThreadItems = new EDInventory();

		paneStatus = new BackgroundEditorPane("templates/characteristic_background.jpg");
		JScrollPane editorScrollPane = new JScrollPane(paneStatus);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setMinimumSize(new Dimension(500,300));
		EditorKit kit = paneStatus.getEditorKitForContentType("text/html");
		paneStatus.setEditorKit(kit);
		paneStatus.setEditable(false);
		paneStatus.setFocusable(false);
		characteristicStatus = new CharacteristicStatus("characteristic_layout.html");
		characteristicStatus.setCharacter(character);

		tabbedPane.addTab("General", null, panelERGeneral, null);
		tabbedPane.addTab("Attributes", null, panelEDAttributes, null);
		tabbedPane.addTab("Disciplines", null, panelEDDisciplines, null);
		tabbedPane.addTab("Skills", null, panelEDSkills, null);
		tabbedPane.addTab("Experience", null, panelEDExperience , null);
		tabbedPane.addTab("Karma", null, panelEDKarma , null);
		tabbedPane.addTab("DevotionPoints", null, panelEDDevotionPoints , null);
		//tabbedPane.addTab("Items", null, panelEDItems , null);
		//tabbedPane.addTab("Bloodcharms", null, panelEDBloodCharmItems , null);
		//tabbedPane.addTab("Weapons", null, panelEDWeapons , null);
		//tabbedPane.addTab("Armor/Shields", null, panelEDArmor , null);
		tabbedPane.addTab("Inventory", null, panelEDThreadItems , null);
		
		splitPane.setRightComponent(editorScrollPane);
	}

	private void do_mntmAbout_actionPerformed(ActionEvent arg0) {
		// http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html
		JOptionPane.showMessageDialog(frame, "This menu item is under construction.");
		//TODO Fenster öffnen und HELP.ABOUT darin anzeigen
	}

	private void do_mntmDicing_actionPerformed(ActionEvent arg0) {
		// http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html
		JOptionPane.showMessageDialog(frame, "This menu item is under construction.");
		@SuppressWarnings("unused")
		EDDicing dicingWindow = new EDDicing();
	}

	private void addTalentsAndSpellsTabs(){
		List<String> allTalentTabs = new ArrayList<String>();
		List<String> allSpellTabs = new ArrayList<String>();
		HashMap<String, List<TALENTType>> threadWeavingTalents = character.getThreadWeavingTalents();
		HashMap<String, DISCIPLINEType> allDisciplinesByName = character.getAllDisciplinesByName();
		for(Component co : tabbedPane.getComponents() ) {
			if(co.getClass() == EDTalents.class) {
				// löschen wenn die Disciplin nicht mehr vorhanden ist
				if(!allDisciplinesByName.containsKey(((EDTalents)co).getDisciplin())){
					tabbedPane.remove(co);
				} else{
					allTalentTabs.add(((EDTalents)co).getDisciplin());
				}
			} else if(co.getClass() == EDSpells.class) {
				((EDSpells)co).refresh();
				String diciplineName = ((EDSpells)co).getDisciplin();
				List<TALENTType> list = threadWeavingTalents.get(diciplineName);
				if( (list==null) || list.isEmpty() || (!allDisciplinesByName.containsKey(diciplineName)) ) {
					tabbedPane.remove(co);
				} else {
					allSpellTabs.add(diciplineName);
				}
			}
		}

		int order=3;
		for(DISCIPLINEType discipline : character.getDisciplines()){
			String diciplineName = discipline.getName();
			// wenn tab nicht beteits vorhanden -> hinzufügen
			if(!allTalentTabs.contains(diciplineName)){
				panelEDTalents = new EDTalents(diciplineName);
				panelEDTalents.setCharacter(character);
				tabbedPane.insertTab("Talents (" + diciplineName + ")", null, panelEDTalents, null, order);
			}
			order++;
		}
		for(DISCIPLINEType discipline : character.getDisciplines()){
			String diciplineName = discipline.getName();
			List<TALENTType> list = threadWeavingTalents.get(diciplineName);
			if( (list!=null) && (!list.isEmpty()) ) {
				try {
					// wenn tab nicht beteits vorhanden -> hinzufügen
					if(!allSpellTabs.contains(diciplineName)){
						panelEDSpells = new EDSpells(character,diciplineName);
						tabbedPane.insertTab("Spells (" + diciplineName + ")", null, panelEDSpells, null, order);
					}
					order++;
				} catch(IndexOutOfBoundsException e) {
					String message = e.getLocalizedMessage();
					if( message.startsWith("No Spells for the thread waving talent(s): ")) System.out.println(message);
					else {
						System.err.println(message);
						e.printStackTrace();
					}
				}
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
			if(co.getClass() == EDExperience.class){
				((EDExperience)co).setCharacter(character);
			}
			if(co.getClass() == EDKarma.class){
				((EDKarma)co).setCharacter(character);
			}
			if(co.getClass() == EDDevotionPoints.class){
				((EDDevotionPoints)co).setCharacter(character);
			}
			if(co.getClass() == EDItems.class){
				((EDItems)co).setCharacter(character);
			}
			if(co.getClass() == EDBloodCharmItems.class){
				((EDBloodCharmItems)co).setCharacter(character);
			}
			if(co.getClass() == EDWeapons.class){
				((EDWeapons)co).setCharacter(character);
			}
			if(co.getClass() == EDArmor.class){
				((EDArmor)co).setCharacter(character);
			}
			if(co.getClass() == EDInventory.class){
				((EDInventory)co).setCharacter(character);
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
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	private void writeToXml(File file) throws JAXBException, IOException {
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Marshaller m = jc.createMarshaller();
		FileOutputStream out = new FileOutputStream(file);
		PrintStream fileio = new PrintStream(out, false, encoding);
		m.setProperty(Marshaller.JAXB_ENCODING, encoding);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
		m.setProperty(Marshaller.JAXB_FRAGMENT, true);
		fileio.print("<?xml version=\"1.0\" encoding=\""+encoding+"\" standalone=\"no\"?>\n");
		fileio.print("<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>\n");
		m.marshal(ec,fileio);
		fileio.close();
		out.close();
	}

	private void writeToJson(File file) throws JAXBException, JSONException, IOException {
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Marshaller m = jc.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		m.setProperty(Marshaller.JAXB_ENCODING, encoding);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
		m.setProperty(Marshaller.JAXB_FRAGMENT, false);
		m.marshal(ec,baos);
		JSONObject json = XML.toJSONObject(baos.toString());
		baos.close();
		FileOutputStream out = new FileOutputStream(file);
		OutputStreamWriter fileio = new OutputStreamWriter(out,encoding);
		json.write(fileio);
		fileio.close();
		out.close();
	}

	private void writeToGson(File file) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(ec,new OutputStreamWriter(new FileOutputStream(file),encoding));
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
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
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
					addTalentsAndSpellsTabs();
					refreshTabs();
				}
			});				
			addTalentsAndSpellsTabs();
			characteristicStatus.setCharacter(character);
			refreshTabs();
		}
	}
	protected void do_mntmClose_actionPerformed(ActionEvent arg0) {
		frame.dispose();
	}

	protected void do_mntmExport_actionPerformed(ActionEvent arg0, int v) {
		File selFile = selectFileName(".pdf");
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
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmSpellCSV_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName("_Spells.csv");
		if( selFile != null ) {
			try {
				new ECECsvExporter(encoding).exportSpells(character.getEDCHARACTER(), selFile);
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(selFile);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmTalentCSV_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName("_Talents.csv");
		if( selFile != null ) {
			try {
				new ECECsvExporter(encoding).exportTalents(character.getEDCHARACTER(), selFile);
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(selFile);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmSkillCSV_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName("_Skills.csv");
		if( selFile != null ) {
			try {
				new ECECsvExporter(encoding).exportSkills(character.getEDCHARACTER(), selFile);
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(selFile);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmItemCSV_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName("_Items.csv");
		if( selFile != null ) {
			try {
				new ECECsvExporter(encoding).exportItems(character.getEDCHARACTER(), selFile);
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(selFile);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmJson_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName(".json");
		if( selFile != null ) {
			try {
				writeToJson(selFile);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			} catch (JSONException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmGson_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName(".json");
		if( selFile != null ) {
			try {
				writeToGson(selFile);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	private File selectFileName(String extention) {
		if( file == null ) {
			String name = character.getName();
			if( name == null ) name = "noname";
			file = new File(name.replaceAll(" ", "_") + ".xml");
		}
		File csvFile = new File(file.getParentFile(), chopFilename(file)+ extention);
		JFileChooser fc = new JFileChooser(csvFile);
		fc.setSelectedFile(csvFile);
		fc.showSaveDialog(frame);
		File selFile = fc.getSelectedFile(); // Show save dialog; this method does not return until the dialog is closed fc.showSaveDialog(frame);
		return selFile;
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
			JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
			e.printStackTrace();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
			e.printStackTrace();
		}
}

	protected String chopFilename(File f){
		String choppedFilename;
		String filename = f.getName();
		// where the last dot is. There may be more than one.
		int dotPlace = filename.lastIndexOf ( '.' );

		if ( dotPlace >= 0 ) {
			// possibly empty
			choppedFilename = filename.substring( 0, dotPlace );
		} else {
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
				addTalentsAndSpellsTabs();
				refreshTabs();
			}
		});
		addTalentsAndSpellsTabs();
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
