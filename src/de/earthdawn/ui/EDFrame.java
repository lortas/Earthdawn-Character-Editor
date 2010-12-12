package de.earthdawn.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.EDCHARACTER;

/**
 * Hauptfenster des Earthdawn-Character-Generators. 
 */
@SuppressWarnings("serial")
public class EDFrame extends JFrame implements ActionListener {

    /** Action-Command: Character erstellen */
    public static final String ACTION_NEW 
        = ApplicationProperties.create().getMessage("main.new");
	
    /** Action-Command: Character �ffnen */
    public static final String ACTION_OPEN 
        = ApplicationProperties.create().getMessage("main.open");

    /** Action-Command: Character speichern */
    public static final String ACTION_SAVE 
        = ApplicationProperties.create().getMessage("main.save");

    /** Action-Command: Pfad abfragen, Character speichern */
    public static final String ACTION_SAVE_AS 
        = ApplicationProperties.create().getMessage("main.save.as");

    private File ausgabedatei;

    private EDComponent edComponent;
	
	
	/**
	 * Default-Constructor.
	 */
	public EDFrame() {
		super("EarthdawnCharacterGenerator v0.1");
		
		// Steuerelement der Oberfl�che initialisieren.
		initComponents();
	}

	/**
	 * Wird durch AWT aufgerufen, wenn ein Event f�r dieses Fenster ausgel�st wurde.
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			if (ACTION_OPEN.equals(ae.getActionCommand())) {
				open();
			}
			else if (ACTION_SAVE.equals(ae.getActionCommand())) {
				save();
			}
			else if (ACTION_SAVE_AS.equals(ae.getActionCommand())) {
				saveAs();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void open() throws Exception {
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Unmarshaller u = jc.createUnmarshaller();

		JFileChooser jfc = new JFileChooser();
		if (this.ausgabedatei != null) {
			jfc.setSelectedFile(this.ausgabedatei);
		}
		jfc.setMultiSelectionEnabled(false);
		int state = jfc.showOpenDialog(this);
		if (JFileChooser.APPROVE_OPTION == state) {
			File inFile = jfc.getSelectedFile();
			EDCHARACTER ec =(EDCHARACTER)u.unmarshal(inFile);
			this.edComponent.setEDCHARACTER(ec);
		}
	}

	private void save() throws Exception {
		if (this.ausgabedatei == null) {
			saveAs();

			return;
		}

		saveCharacter();
	}

	private void saveAs() throws Exception {
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(false);
		if (this.ausgabedatei != null) {
			jfc.setSelectedFile(this.ausgabedatei);
		}
		int state = jfc.showSaveDialog(this);
		if (JFileChooser.APPROVE_OPTION == state) {
			this.ausgabedatei = jfc.getSelectedFile();

			saveCharacter();
		}
	}
	
	private void saveCharacter() throws Exception {
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		m.marshal(this.edComponent.getEDCHARACTER(), this.ausgabedatei);
	} 

	protected void exit() {
		// TODO Hier kann nochmal r�ckgefragt werden, wenn es noch ungespeicherte �nderungen gibt ...
	}

	private void handleException(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, "Fehler", e.getMessage(), JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Initialisiert die Steuerlemente der Oberfläche.
	 */
	private void initComponents() {
		// Anwendung beenden, wenn das Fenster geschlossen wird.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Menu hinzuf�gen
		createMenuBar();
		
		// Hinzuf�gen des Anzeigelementes f�r den Charakter.
		this.edComponent = new EDComponent();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.edComponent);
		
		// Layout des Dialogs und damit auch aller seine Steuerlement ansto�en.
		pack();

		setMinimumSize(new Dimension(300, 280));
		
		// Funktion registrieren, die aufgerufen wird, wenn das Fenster geschlossen wird.
        addWindowListener(
                new WindowAdapter() {
                    @Override
    				public void windowClosing(WindowEvent evt) {
                    	exit(); 
                    }
                }
            );
	}

	/**
	 * Erzeugt die Menuleiste.
	 */
	private void createMenuBar() {
        ApplicationProperties props = ApplicationProperties.create();

        JMenuBar menuBar = new JMenuBar();
        String fileMenuText = props.getMessage("main.file");
		JMenu fileMenu = new JMenu(fileMenuText);

        JMenuItem newCharacterItem = new JMenuItem(ACTION_NEW);
        newCharacterItem.addActionListener(this);
        newCharacterItem.setActionCommand(ACTION_NEW);
        fileMenu.add(newCharacterItem);

        JMenuItem openItem = new JMenuItem(ACTION_OPEN);
        openItem.addActionListener(this);
        openItem.setActionCommand(ACTION_OPEN);
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem(ACTION_SAVE);
        saveItem.addActionListener(this);
        saveItem.setActionCommand(ACTION_SAVE);
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem(ACTION_SAVE_AS);
        saveAsItem.addActionListener(this);
        saveAsItem.setActionCommand(ACTION_SAVE_AS);
        fileMenu.add(saveAsItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
	}
}
