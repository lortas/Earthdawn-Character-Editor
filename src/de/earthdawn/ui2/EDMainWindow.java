package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.text.EditorKit;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;

import de.earthdawn.CharacterContainer;
import de.earthdawn.CharacteristicStatus;
import de.earthdawn.ECECsvExporter;
import de.earthdawn.ECEPdfExporter;
import de.earthdawn.ECEWorker;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.ATTRIBUTENameType;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.ITEMS;
import de.earthdawn.data.LAYOUTDIMENSIONType;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.LanguageType;
import de.earthdawn.data.MOVEMENTATTRIBUTENameType;
import de.earthdawn.data.OPTIONALRULES;
import de.earthdawn.data.OPTIONALRULESUNITSType;
import de.earthdawn.data.OPTIONALRULEType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.event.CharChangeEventListener;
import java.awt.event.InputEvent;

public class EDMainWindow {

	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final OPTIONALRULES OPTIONALRULES = PROPERTIES.getOptionalRules();
	public static final List<Method> optionalrulesMethods = Arrays.asList(OPTIONALRULES.class.getMethods());
	public static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS");
	public static final String encoding="UTF-8";
	public static final String[] OptionDialog_YesNoOptions = {NLS.getString("Button.Yes.text"),NLS.getString("Button.No.text")};

	private JFrame frame;
	private CharacterContainer character;
	private JTabbedPane tabbedPane;
	private EDGeneral panelERGeneral;
	private EDAttributes panelEDAttributes;
	private EDDisciplines panelEDDisciplines;
	private EDExperience panelEDExperience;
	private EDLanguages panelEDLanguages;
	private EDKarma panelEDKarma;
	private EDDevotionPoints panelEDDevotionPoints;
	private EDInventory panelEDThreadItems;
	private EDSpells panelEDSpells;
	private EDKnacks panelEDKnacks;
	private EDTalents panelEDTalents;
	private File file = null;
	private JSplitPane splitPane;
	private EDSkills panelEDSkills;
	private JEditorPane paneStatus;
	private CharacteristicStatus characteristicStatus;
	private About aboutwindow;
	private ImageIcon yesIcon=null;
	private ImageIcon noIcon=null;
	private EDDicing dicingWindow=null;
	private DefaultOptionalTalent defaultoptionaltalent = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EDMainWindow window = new EDMainWindow(new EDCHARACTER());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void finalize() throws Throwable {
		if( dicingWindow!=null) {
			dicingWindow.setVisible(false);
			dicingWindow.dispose();
			dicingWindow=null;
		}
		if( panelEDThreadItems!=null ) {
			panelEDThreadItems.setVisible(false);
			panelEDThreadItems=null;
		}
		super.finalize();
	}

	public void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}

	public boolean isDisplayable() {
		return frame.isDisplayable();
	}

	/**
	 * Create the application.
	 */
	public EDMainWindow(EDCHARACTER ec) {
		this(new CharacterContainer(ec));
	}

	public EDMainWindow(CharacterContainer c) {
		character = c;
		new ECEWorker(character).verarbeiteCharakter();
		initialize();
		character.addCharChangeEventListener(new CharChangeEventListener() {
			@Override
			public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
				new ECEWorker(character).verarbeiteCharakter();
				addTalentsAndSpellsTabs();
				refreshTabs();
			}
		});
		refreshTabs();
		JOptionPane.showMessageDialog(frame, NLS.getString("EDMainWindow.startHint.text")); //$NON-NLS-1$
		if( ApplicationProperties.getJavaVersion() < 10 ) {
			JOptionPane.showMessageDialog(frame, NLS.getString("EDMainWindow.deprecatedJavaVersion.text")); //$NON-NLS-1$
		}
	}

	public EDCHARACTER getEDCharacter() {
		return character.getEDCHARACTER();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Earthdawn Character Editor - "+PROPERTIES.getRulesetLanguage().toString());
		//frame.setBounds(10, 50, 1020, 740);
		LAYOUTDIMENSIONType dimMainWindow = PROPERTIES.getGuiLayoutMainWindow();
		LAYOUTSIZESType dimMainWindow_X = dimMainWindow.getX();
		LAYOUTSIZESType dimMainWindow_Y = dimMainWindow.getY();
		if( (dimMainWindow_X!=null) && (dimMainWindow_Y!=null) ) {
			Integer x = dimMainWindow_X.getPreferred();
			Integer y = dimMainWindow_Y.getPreferred();
			if( (x!=null) && (y!=null) ) {
				frame.setPreferredSize(new Dimension(x,y));
				frame.setSize(new Dimension(x,y));
			}
			x = dimMainWindow_X.getMin();
			y = dimMainWindow_Y.getMin();
			if( (x!=null) && (y!=null) ) frame.setMinimumSize(new Dimension(x,y));
			x = dimMainWindow_X.getMax();
			y = dimMainWindow_Y.getMax();
			if( (x!=null) && (y!=null) ) frame.setMaximumSize(new Dimension(x,y));
		}
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(NLS.getString("EDMainWindow.mnFile.text")); //$NON-NLS-1$
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem(NLS.getString("EDMainWindow.mntmNew.text"));
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmNew_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem(NLS.getString("EDMainWindow.mntmOpen.text")); //$NON-NLS-1$
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmOpen_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem(NLS.getString("EDMainWindow.mntmSave.text")); //$NON-NLS-1$
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmSave_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem(NLS.getString("EDMainWindow.mntmSaveAs.text")); //$NON-NLS-1$
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK|InputEvent.SHIFT_DOWN_MASK));
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

		JMenuItem mntmExportAjfelmordomPl = new JMenuItem(NLS.getString("EDMainWindow.mntmExportAjfelmordomPl.text")); //$NON-NLS-1$
		mntmExportAjfelmordomPl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,3);
			}
		});
		mntmPdfExport.add(mntmExportAjfelmordomPl);

		JMenuItem mntmExportGeneric = new JMenuItem(NLS.getString("EDMainWindow.mntmExportGeneric.text")); //$NON-NLS-1$
		mntmExportGeneric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,4);
			}
		});
		mntmPdfExport.add(mntmExportGeneric);

		mntmExportGeneric = new JMenuItem("ED4 FasaGames Novize Char"); //$NON-NLS-1$
		mntmExportGeneric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,5);
			}
		});
		mntmPdfExport.add(mntmExportGeneric);

		mntmExportGeneric = new JMenuItem("ED4 Ulisses Novize Char"); //$NON-NLS-1$
		mntmExportGeneric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,6);
			}
		});
		mntmPdfExport.add(mntmExportGeneric);

		mntmExportGeneric = new JMenuItem("ED4 Ulisses Char"); //$NON-NLS-1$
		mntmExportGeneric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,8);
			}
		});
		mntmPdfExport.add(mntmExportGeneric);

		mntmExportGeneric = new JMenuItem("ED4en"); //$NON-NLS-1$
		mntmExportGeneric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExport_actionPerformed(arg0,7);
			}
		});
		mntmPdfExport.add(mntmExportGeneric);

		JMenuItem mntmExportSpellcards0 = new JMenuItem(NLS.getString("EDMainWindow.mntmExportSpellcards0.text")); //$NON-NLS-1$
		mntmExportSpellcards0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExportSpellcards_actionPerformed(arg0,0);
			}
		});
		mntmPdfExport.add(mntmExportSpellcards0);

		JMenuItem mntmExportSpellcards1 = new JMenuItem(NLS.getString("EDMainWindow.mntmExportSpellcards1.text")); //$NON-NLS-1$
		mntmExportSpellcards1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmExportSpellcards_actionPerformed(arg0,1);
			}
		});
		mntmPdfExport.add(mntmExportSpellcards1);

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

		JMenuItem mntmHtml = new JMenuItem(NLS.getString("EDMainWindow.mntmXML2Html.text")); //$NON-NLS-1$
		mntmHtml.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
		mntmHtml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmHtml_actionPerformed(arg0);
			}
		});
		mntmExport.add(mntmHtml);

		JMenuItem mntmItems = new JMenuItem(NLS.getString("EDMainWindow.mntmItemExport.text")); //$NON-NLS-1$
		mntmItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmItems_actionPerformed(arg0);
			}
		});
		mntmExport.add(mntmItems);

		JMenuItem mntmClose = new JMenuItem(NLS.getString("EDMainWindow.mntmClose.text")); //$NON-NLS-1$
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmClose_actionPerformed(arg0);
			}
		});
		mnFile.add(mntmClose);

		JMenu mnView = new JMenu(NLS.getString("EDMainWindow.mnView.text")); //$NON-NLS-1$
		menuBar.add(mnView);

		JMenuItem mntmUpdateChar= new JMenuItem(NLS.getString("EDMainWindow.mntmUpdateChar.text")); //$NON-NLS-1$
		mntmUpdateChar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		mntmUpdateChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmUpdateChar_actionPerformed(arg0);
			}
		});
		mnView.add(mntmUpdateChar);

		JMenuItem mntmWebBrowser= new JMenuItem(NLS.getString("EDMainWindow.mntmWebBrowser.text")); //$NON-NLS-1$
		mntmWebBrowser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		mntmWebBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmWebBrowser_actionPerformed(arg0);
			}
		});
		mnView.add(mntmWebBrowser);

		JMenuItem mntmFullscreen = new JMenuItem(NLS.getString("EDMainWindow.mntmFullscreen.text")); //$NON-NLS-1$
		mntmFullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		mntmFullscreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( frame.getExtendedState() == JFrame.NORMAL ) {
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				} else {
					frame.setExtendedState(JFrame.NORMAL);
				}
			}
		});
		mnView.add(mntmFullscreen);

		JMenu mnExtra = new JMenu(NLS.getString("EDMainWindow.mnExtra.text")); //$NON-NLS-1$
		menuBar.add(mnExtra);

		JMenuItem mntmDicing= new JMenuItem(NLS.getString("EDMainWindow.mntmDicing.text")); //$NON-NLS-1$
		mntmDicing.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		mntmDicing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmDicing_actionPerformed(arg0);
			}
		});
		mnExtra.add(mntmDicing);

		JMenuItem mntmRandomName= new JMenuItem(NLS.getString("EDMainWindow.mntmRandomName.text")); //$NON-NLS-1$
		mntmRandomName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmRandomName_actionPerformed(arg0);
			}
		});
		mnExtra.add(mntmRandomName);

		JMenu mntmRandomCharacter = mapTreeToMenuTree(null,ApplicationProperties.create().getAllCharacterTemplatesNamesAsTree());
		mntmRandomCharacter.setText(NLS.getString("EDMainWindow.mntmRandomCharacter.text"));
		mnExtra.add(mntmRandomCharacter);

		menuBar.add(createOptionalRuleMenu());

		JMenu mnHelp = new JMenu(NLS.getString("EDMainWindow.mnHelp.text")); //$NON-NLS-1$
		menuBar.add(mnHelp);

		aboutwindow = new About(frame);
		JMenuItem mntmAbout= new JMenuItem(NLS.getString("EDMainWindow.mntmAbout.text")); //$NON-NLS-1$
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aboutwindow.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		JMenuItem mntmWiki= new JMenuItem(NLS.getString("EDMainWindow.mntmWIKI.text")); //$NON-NLS-1$
		mntmWiki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI("https://sourceforge.net/p/ed-char-editor/wiki/"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mnHelp.add(mntmWiki);

		JMenuItem mntmFAQ= new JMenuItem(NLS.getString("EDMainWindow.mntmFAQ.text")); //$NON-NLS-1$
		mntmFAQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI("https://sourceforge.net/p/ed-char-editor/wiki/FAQ/"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mnHelp.add(mntmFAQ);

		JMenuItem mntmNewVersion= new JMenuItem(NLS.getString("EDMainWindow.mntmNewVersion.text")); //$NON-NLS-1$
		mntmNewVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI("https://sourceforge.net/projects/ed-char-editor/files/"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mnHelp.add(mntmNewVersion);

		JMenuItem mntmReportBug= new JMenuItem(NLS.getString("EDMainWindow.mntmReportBug.text")); //$NON-NLS-1$
		mntmReportBug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI("https://sourceforge.net/p/ed-char-editor/tickets/"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mnHelp.add(mntmReportBug);

		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane);

		LAYOUTDIMENSIONType dimTabWindow = PROPERTIES.getGuiLayoutTabWindow();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		LAYOUTSIZESType dimTabWindow_X = dimTabWindow.getX();
		LAYOUTSIZESType dimTabWindow_Y = dimTabWindow.getY();
		if( (dimTabWindow_X!=null) && (dimTabWindow_Y!=null) ) {
			Integer x = dimTabWindow_X.getPreferred();
			Integer y = dimTabWindow_Y.getPreferred();
			if( (x!=null) && (y!=null) ) {
				tabbedPane.setSize(new Dimension(x,y));
				tabbedPane.setPreferredSize(new Dimension(x,y));
			}
			x = dimTabWindow_X.getMin();
			y = dimTabWindow_Y.getMin();
			if( (x!=null) && (y!=null) ) tabbedPane.setMinimumSize(new Dimension(x,y));
			x = dimTabWindow_X.getMax();
			y = dimTabWindow_Y.getMax();
			if( (x!=null) && (y!=null) ) tabbedPane.setMaximumSize(new Dimension(x,y));
		}
		splitPane.setLeftComponent(tabbedPane);

		panelERGeneral = new EDGeneral();
		panelEDDisciplines = new EDDisciplines();
		panelEDAttributes = new EDAttributes();
		panelEDKnacks = new EDKnacks(character);
		panelEDSkills = new EDSkills();
		panelEDExperience = new EDExperience();
		panelEDKarma = new EDKarma();
		panelEDDevotionPoints = new EDDevotionPoints();
		panelEDThreadItems = new EDInventory();
		panelEDLanguages = new EDLanguages();

		paneStatus = new BackgroundEditorPane("images/background/characteristic.jpg");
		JScrollPane editorScrollPane = new JScrollPane(paneStatus);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		LAYOUTDIMENSIONType dimStatusWindow = PROPERTIES.getGuiLayoutStatusWindow();
		LAYOUTSIZESType dimStatusWindow_X = dimStatusWindow.getX();
		LAYOUTSIZESType dimStatusWindow_Y = dimStatusWindow.getY();
		if( (dimStatusWindow_X!=null) && (dimStatusWindow_Y!=null) ) {
			Integer x = dimStatusWindow_X.getPreferred();
			Integer y = dimStatusWindow_Y.getPreferred();
			if( (x!=null) && (y!=null) ) {
				editorScrollPane.setSize(new Dimension(x,y));
				editorScrollPane.setPreferredSize(new Dimension(x,y));
			}
			x = dimStatusWindow_X.getMin();
			y = dimStatusWindow_Y.getMin();
			if( (x!=null) && (y!=null) ) editorScrollPane.setMinimumSize(new Dimension(x,y));
			x = dimStatusWindow_X.getMax();
			y = dimStatusWindow_Y.getMax();
			if( (x!=null) && (y!=null) ) editorScrollPane.setMaximumSize(new Dimension(x,y));
		}
		EditorKit kit = paneStatus.getEditorKitForContentType("text/html");
		paneStatus.setEditorKit(kit);
		paneStatus.setEditable(false);
		paneStatus.setFocusable(false);
		characteristicStatus = new CharacteristicStatus("characteristic_layout.html");
		characteristicStatus.setCharacter(character);

		tabbedPane.addTab(PROPERTIES.getTranslationText("general"), null, new JScrollPane(panelERGeneral), null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("disciplines"), null, panelEDDisciplines, null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("attributes"), null, panelEDAttributes, null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("knacks"), null, panelEDKnacks, null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("skills"), null, panelEDSkills, null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("languages"), null, panelEDLanguages, null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("experience"), null, panelEDExperience , null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("karma"), null, panelEDKarma , null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("devotionpoints"), null, panelEDDevotionPoints , null);
		tabbedPane.addTab(PROPERTIES.getTranslationText("inventory"), null, panelEDThreadItems , null);

		splitPane.setRightComponent(editorScrollPane);
		File icon = new File(new File("images"),"ece-logo.png");
		try {
			String iconfilename = icon.getCanonicalPath();
			if( ! icon.canRead() ) {
				throw new FileNotFoundException(iconfilename);
			}
			frame.setIconImage((new ImageIcon(iconfilename)).getImage());
		} catch (IOException e) {
			System.err.println("Can not locate or read icon file : "+e.getLocalizedMessage()+"\n");
		}
	}

	private JMenu mapTreeToMenuTree(String name, Map<String, Map<String, ?>> tree) {
		JMenu result;
		if( name == null ) {
			result = new JMenu();
		} else {
			result = new JMenu(name);
			JMenuItem menuItem = new JMenuItem(name);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					do_mntmRandomCharacter_actionPerformed(arg0);
				}
			});
			result.add(menuItem);
		}
		SortedSet<String> sortedset= new TreeSet<String>(tree.keySet());
		for( String n : sortedset ) {
			if( tree.get(n).isEmpty() ) {
				JMenuItem menuItem = new JMenuItem(n);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						do_mntmRandomCharacter_actionPerformed(arg0);
					}
				});
				result.add(menuItem);
			} else {
				@SuppressWarnings("unchecked")
				Map<String, Map<String, ?>> submap = (Map<String, Map<String, ?>>)(tree.get(n));
				result.add(mapTreeToMenuTree(n,submap));
			}
		}
		return result;
	}

	private void do_mntmDicing_actionPerformed(ActionEvent arg0) {
		//@SuppressWarnings("unused")
		//EDDicingOld dicingWindowOld = new EDDicingOld();
		if( dicingWindow == null ) {
			JOptionPane.showMessageDialog(frame, "This menu item is under construction.");
			dicingWindow = new EDDicing(frame);
		}
		dicingWindow.setVisible(true);
	}

	private void do_mntmRandomName_actionPerformed(ActionEvent arg0) {
		character.setRandomName();
		refreshTabs();
	}

	private void do_mntmRandomCharacter_actionPerformed(ActionEvent arg0) {
		JMenuItem menuitem = (JMenuItem)arg0.getSource();
		if( menuitem == null ) return;
		CharacterContainer c = PROPERTIES.getRandomCharacter(menuitem.getText());
		if( c == null ) return;
		character=c;
		new ECEWorker(character).verarbeiteCharakter();
		character.addCharChangeEventListener(new CharChangeEventListener() {
			@Override
			public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
				new ECEWorker(character).verarbeiteCharakter();
				addTalentsAndSpellsTabs();
				refreshTabs();
			}
		});
		addTalentsAndSpellsTabs();
		characteristicStatus.setCharacter(character);
		refreshTabs();
	}

	private void addTalentsAndSpellsTabs(){
		List<String> allTalentTabs = new ArrayList<String>();
		List<String> allSpellTabs = new ArrayList<String>();
		Map<String, List<TALENTType>> threadWeavingTalents = character.getThreadWeavingTalents();
		Map<String, DISCIPLINEType> allDisciplinesByName = character.getAllDisciplinesByName();
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
				tabbedPane.insertTab(PROPERTIES.getTranslationText("talents")+" (" + diciplineName + ")", null, panelEDTalents, null, order);
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
						tabbedPane.insertTab(PROPERTIES.getTranslationText("spells")+" (" + diciplineName + ")", null, panelEDSpells, null, order);
					}
					order++;
				} catch(IndexOutOfBoundsException e) {
					String message = e.getLocalizedMessage();
					System.out.println(message);
					if( ! message.startsWith("No Spells for the thread waving talent(s): ")) {
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
		for(Component co : tabbedPane.getComponents() ) componentSetCharacter(co);
	}

	private void componentSetCharacter(Component co) {
		if(co.getClass() == EDTalents.class)        { ((EDTalents)co).setCharacter(character); return; }
		if(co.getClass() == EDGeneral.class)        { ((EDGeneral)co).setCharacter(character); return; }
		if(co.getClass() == EDAttributes.class)     { ((EDAttributes)co).setCharacter(character); return; }
		if(co.getClass() == EDDisciplines.class)    { ((EDDisciplines)co).setCharacter(character); return; }
		if(co.getClass() == EDSpells.class)         { ((EDSpells)co).setCharacter(character); return; }
		if(co.getClass() == EDKnacks.class)         { ((EDKnacks)co).setCharacter(character);  return; }
		if(co.getClass() == EDSkills.class)         { ((EDSkills)co).setCharacter(character); return; }
		if(co.getClass() == EDExperience.class)     { ((EDExperience)co).setCharacter(character); return; }
		if(co.getClass() == EDLanguages.class)      { ((EDLanguages)co).setCharacter(character); return; }
		if(co.getClass() == EDKarma.class)          { ((EDKarma)co).setCharacter(character); return; }
		if(co.getClass() == EDDevotionPoints.class) { ((EDDevotionPoints)co).setCharacter(character); return; }
		if(co.getClass() == EDInventory.class)      { ((EDInventory)co).setCharacter(character); return; }

		if(co.getClass() == JScrollPane.class) componentSetCharacter(((JScrollPane)co).getViewport().getView());
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
		if( name == null || name.isEmpty() ) {
			name = character.getRace().getName();
			for( String s : character.getDisciplineNames() ) name += "_"+s;
		}
		File xmlFile = new File(name.replaceAll(" ", "_") + ".xml");
		JFileChooser fc = new JFileChooser(new File("."));
		fc.setSelectedFile(xmlFile);

		// Show open dialog; this method does not return until the dialog is closed
		if( fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION ) return;
		// Only Save file if OK/Yes was pressed
		File selFile = fc.getSelectedFile();
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

	public void writeToXml(File file) throws JAXBException, IOException {
		if( character == null ) return;
		FileOutputStream out = new FileOutputStream(file);
		character.writeXml(out, encoding);
		out.close();
	}

	private void writeToJson(File file) throws JAXBException, JSONException, IOException {
		if( character == null ) return;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		character.writeXml(baos, encoding);
		JSONObject json = XML.toJSONObject(baos.toString());
		baos.close();
		FileOutputStream out = new FileOutputStream(file);
		OutputStreamWriter fileio = new OutputStreamWriter(out,encoding);
		json.write(fileio);
		fileio.close();
		out.close();
	}

	private void writeToGson(File file) throws IOException {
		if( character == null ) return;
		EDCHARACTER ec = character.getEDCHARACTER();
		if( ec == null ) return;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileOutputStream out = new FileOutputStream(file);
		OutputStreamWriter fileio = new OutputStreamWriter(out,encoding);
		ec.setEditorpath((new File("")).toURI().getRawPath());
		gson.toJson(ec,fileio);
		fileio.close();
		out.close();
	}

	private void writeToHtml(File file) throws JAXBException, IOException, TransformerException {
		if( character == null ) return;
		character.writeHtml(new FileOutputStream(file), encoding);
	}

	protected  void do_mntmOpen_actionPerformed(ActionEvent arg0) {
		String filename = "."; 
		JFileChooser fc = new JFileChooser(new File(filename)); 

		// Show open dialog; this method does not return until the dialog is closed
		if( fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION ) return;
		// Only Open file if OK/Yes was pressed
		File selFile = fc.getSelectedFile();
		if(selFile != null){
			file = selFile;
			try{
				character = new CharacterContainer(selFile);
				//TODO: Besserer Umgang mit Fehlern
			}
			catch(IOException | JAXBException | ParserConfigurationException | SAXException | TransformerException e){
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
			}
			new ECEWorker(character).verarbeiteCharakter();
			this.character.addCharChangeEventListener(new CharChangeEventListener() {
				@Override
				public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
					new ECEWorker(character).verarbeiteCharakter();
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
		int a = JOptionPane.showOptionDialog(frame,
				NLS.getString("Confirmation.Quit.text"),
				NLS.getString("Confirmation.Quit.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				OptionDialog_YesNoOptions,
				OptionDialog_YesNoOptions[0]);
		if( a != 0 ) return;
		frame.dispose();
		if(defaultoptionaltalent!=null) defaultoptionaltalent.dispose();
	}

	protected void do_mntmExport_actionPerformed(ActionEvent arg0, int v) {
		File selFile = selectFileName(".pdf");
		if( selFile != null ) {
			try {
				switch(v) {
				case 1 : new ECEPdfExporter().exportRedbrickSimple(character.getEDCHARACTER(), selFile); break;
				case 2 : new ECEPdfExporter().exportAjfelMordom(character.getEDCHARACTER(), 0, selFile); break;
				case 3 : new ECEPdfExporter().exportAjfelMordom(character.getEDCHARACTER(), 1, selFile); break;
				case 4 : new ECEPdfExporter().exportGeneric(character.getEDCHARACTER(), new File("templates/ED4_de.xml"), selFile); break;
				case 5 : new ECEPdfExporter().exportGeneric(character.getEDCHARACTER(), new File("templates/ed4_character_sheet_fasagames.xml"), selFile); break;
				case 6 : new ECEPdfExporter().exportGeneric(character.getEDCHARACTER(), new File("templates/ed4_character_sheet_ulisses.xml"), selFile); break;
				case 7 : new ECEPdfExporter().exportGeneric(character.getEDCHARACTER(), new File("templates/ed4_character_sheet_Ajfel+Mordom_en.xml"), selFile); break;
				case 8 : new ECEPdfExporter().exportGeneric(character.getEDCHARACTER(), new File("templates/ed4_character_sheet_ulisses_ext.xml"), selFile); break;
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

	protected void do_mntmExportSpellcards_actionPerformed(ActionEvent arg0, int version) {
		File selFile = selectFileName("_Spells.pdf");
		if( selFile != null ) {
			try {
				new ECEPdfExporter().exportSpellcards(character.getEDCHARACTER(), selFile, version);
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

	protected void do_mntmHtml_actionPerformed(ActionEvent arg0) {
		File selFile = selectFileName(".html");
		if( selFile != null ) {
			try {
				writeToHtml(selFile);
				if( Desktop.isDesktopSupported() ) {
					Desktop desktop = Desktop.getDesktop();
					desktop.open(selFile);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	protected void do_mntmItems_actionPerformed(ActionEvent arg0) {
		if( character == null ) return;
		EDCHARACTER ec = character.getEDCHARACTER();
		if( ec == null ) return;
		ITEMS items=new ITEMS();
		items.setLang(LanguageType.EN);
		items.getAMMUNITION().addAll(ec.getAMMUNITION());
		items.getBLOODCHARMITEM().addAll(ec.getBLOODCHARMITEM());
		items.getITEM().addAll(ec.getITEM());
		items.getMAGICITEM().addAll(ec.getMAGICITEM());
		items.getPATTERNITEM().addAll(ec.getPATTERNITEM());
		items.getTHREADITEM().addAll(ec.getTHREADITEM());
		items.getWEAPON().addAll(ec.getWEAPON());
		for( ARMORType as : ec.getPROTECTION().getARMOROrSHIELD() ) {
			if( as instanceof SHIELDType ) items.getSHIELD().add((SHIELDType)as);
			else items.getARMOR().add(as);
		}
		EDItemStore.saveItems(frame, items);
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
		// Show open dialog; this method does not return until the dialog is closed
		if( fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION ) return null;
		// Only Save file if OK/Yes was pressed
		return fc.getSelectedFile();
	}

	protected void do_mntmUpdateChar_actionPerformed(ActionEvent arg0) {
		character.refesh();
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
		int a = JOptionPane.showOptionDialog(frame,
				NLS.getString("Confirmation.NewChar.text"),
				NLS.getString("Confirmation.NewChar.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				OptionDialog_YesNoOptions,
				OptionDialog_YesNoOptions[0]);
		if( a != 0 ) return;
		file = null;
		EDCHARACTER newedchar = new EDCHARACTER();
		newedchar.setRulesetversion(PROPERTIES.getRulesetLanguage().getRulesetversion());
		newedchar.setLang(PROPERTIES.getRulesetLanguage().getLanguage());
		character = new CharacterContainer(newedchar);
		new ECEWorker(character).verarbeiteCharakter();
		character.addCharChangeEventListener(new CharChangeEventListener() {
			@Override
			public void CharChanged(de.earthdawn.event.CharChangeEvent evt) {
				new ECEWorker(character).verarbeiteCharakter();
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
		copyFile(new File("config","earthdawncharacter.xsd").getCanonicalPath(),new File( path, "earthdawncharacter.xsd" ).getCanonicalPath());
		copyFile(new File("config","earthdawncharacter.xsl").getCanonicalPath(),new File( path, "earthdawncharacter.xsl" ).getCanonicalPath());
		copyFile(new File("config","earthdawncharacter.css").getCanonicalPath(),new File( path, "earthdawncharacter.css" ).getCanonicalPath());
	}

	public JMenu createOptionalRuleMenu() {
		JMenu mnOptRules = new JMenu(NLS.getString("EDMainWindow.mntmOptRules.text"));
		File iconfolder = new File("icons");
		try {
			yesIcon = new ImageIcon(ImageIO.read(new File(iconfolder,NLS.getString("Button.Yes.icon"))).getScaledInstance(-1, 10, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		try {
			noIcon = new ImageIcon(ImageIO.read(new File(iconfolder,NLS.getString("Button.No.icon"))).getScaledInstance(-1, 10, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		for( Method method : optionalrulesMethods ) {
			if( ! method.getReturnType().equals(OPTIONALRULEType.class) ) continue;
			String rulename = method.getName().substring(3);
			JMenuItem mntmRule= new JMenuItem();
			mntmRule.setName(rulename);
			mntmRule.setText(NLS.getString("EDMainWindow.mntmOptRule"+rulename+".text"));
			try {
				OPTIONALRULEType r =(OPTIONALRULEType)method.invoke(OPTIONALRULES);
				if( r.getUsed().equals(YesnoType.YES) ) {
					mntmRule.setIcon(yesIcon);
				} else {
					mntmRule.setIcon(noIcon);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			mntmRule.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JMenuItem menuitem = (JMenuItem)arg0.getSource();
					final String optionalrule = menuitem.getName();
					final String gettername="get"+optionalrule;
					Method getter = null;
					for( Method method : optionalrulesMethods ) {
						if( method.getName().equals(gettername) ) getter=method;
					}
					if( getter != null ) try {
						OPTIONALRULEType r =(OPTIONALRULEType)getter.invoke(OPTIONALRULES);
						if( r.getUsed().equals(YesnoType.YES) ) {
							r.setUsed(YesnoType.NO);
							menuitem.setIcon(noIcon);
						} else {
							r.setUsed(YesnoType.YES);
							menuitem.setIcon(yesIcon);
						}
						ECEWorker.refreshOptionalRules();
						new ECEWorker(character).verarbeiteCharakter();
						character.refesh();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			});
			mnOptRules.add(mntmRule);
		}

		JMenu mnMovmentRule= new JMenu();
		mnMovmentRule.setName("ATTRIBUTEBASEDMOVEMENT");
		mnMovmentRule.setText(NLS.getString("EDMainWindow.mntmOptRuleATTRIBUTEBASEDMOVEMENT.text"));
		MOVEMENTATTRIBUTENameType attribute = OPTIONALRULES.getATTRIBUTEBASEDMOVEMENT().getAttribute();

		Map<ATTRIBUTENameType, String> attributesHash = PROPERTIES.getAttributeNames();
		Map<MOVEMENTATTRIBUTENameType,String> attributes = new TreeMap<MOVEMENTATTRIBUTENameType,String>();
		attributes.put(MOVEMENTATTRIBUTENameType.NA, attributesHash.get(ATTRIBUTENameType.NA));
		attributes.put(MOVEMENTATTRIBUTENameType.STR, attributesHash.get(ATTRIBUTENameType.STR));
		attributes.put(MOVEMENTATTRIBUTENameType.DEX, attributesHash.get(ATTRIBUTENameType.DEX));
		attributes.put(MOVEMENTATTRIBUTENameType.STR_DEX, "mid("+attributesHash.get(ATTRIBUTENameType.STR)+","+attributesHash.get(ATTRIBUTENameType.DEX)+")");
		attributes.put(MOVEMENTATTRIBUTENameType.MAX, "max("+attributesHash.get(ATTRIBUTENameType.STR)+","+attributesHash.get(ATTRIBUTENameType.DEX)+")");
		for( MOVEMENTATTRIBUTENameType a : new MOVEMENTATTRIBUTENameType[]{MOVEMENTATTRIBUTENameType.NA,MOVEMENTATTRIBUTENameType.STR,MOVEMENTATTRIBUTENameType.DEX,MOVEMENTATTRIBUTENameType.STR_DEX,MOVEMENTATTRIBUTENameType.MAX} ) {
			JMenuItem item = new JMenuItem();
			item.setName(a.name());
			item.setText(attributes.get(a));
			if( a.equals(attribute) ) item.setIcon(yesIcon);
			else item.setIcon(noIcon);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JMenuItem menuitem = (JMenuItem)arg0.getSource();
					JPopupMenu pmenu = (JPopupMenu)menuitem.getParent();
					for( Component item : pmenu.getComponents() ) {
						if( item instanceof JMenuItem ) ((JMenuItem)item).setIcon(noIcon);
					}
					menuitem.setIcon(yesIcon);
					MOVEMENTATTRIBUTENameType newattribute = MOVEMENTATTRIBUTENameType.valueOf(menuitem.getName());
					if( newattribute != null ) {
						OPTIONALRULES.getATTRIBUTEBASEDMOVEMENT().setAttribute(newattribute);
						CharacterContainer.OptionalRule_AttributeBasedMovement=newattribute;
						new ECEWorker(character).verarbeiteCharakter();
						character.refesh();
					}
				}
			});
			mnMovmentRule.add(item);
		}
		mnOptRules.add(mnMovmentRule);

		JMenu mnUnits= new JMenu();
		mnUnits.setName("UNITS");
		mnUnits.setText(NLS.getString("EDMainWindow.mntmOptRuleUNITS.text"));
		for( OPTIONALRULESUNITSType  u : OPTIONALRULES.getUNITS() ) {
			JMenuItem item = new JMenuItem();
			item.setName(u.getName());
			item.setText(u.getName());
			if( u.isDisplayed() ) item.setIcon(yesIcon);
			else item.setIcon(noIcon);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JMenuItem menuitem = (JMenuItem)arg0.getSource();
					JPopupMenu pmenu = (JPopupMenu)menuitem.getParent();
					for( Component item : pmenu.getComponents() ) {
						if( item instanceof JMenuItem ) ((JMenuItem)item).setIcon(noIcon);
					}
					menuitem.setIcon(yesIcon);
					String name = menuitem.getName();
					for( OPTIONALRULESUNITSType  u : OPTIONALRULES.getUNITS() ) {
						u.setDisplayed( u.getName().equals(name) );
					}
					PROPERTIES.clearUnitCalculator();
				}
			});
			mnUnits.add(item);
		}
		mnOptRules.add(mnUnits);

		JMenuItem mntmDefaultOptionalTalent= new JMenuItem(NLS.getString("EDMainWindow.mntmOptRuleDEFAULTOPTIONALTALENT.text"));
		mntmDefaultOptionalTalent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( defaultoptionaltalent != null ) {
					defaultoptionaltalent.dispose();
				}
				defaultoptionaltalent = new DefaultOptionalTalent();
				defaultoptionaltalent.setVisible(true);
			}
		});
		mnOptRules.add(mntmDefaultOptionalTalent);

		JMenuItem mntmSaveOptRules = new JMenuItem(NLS.getString("EDMainWindow.mntmSaveOptRules.text"));
		mntmSaveOptRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					PROPERTIES.saveOptionalRules();
				} catch (JAXBException e) {
					System.err.println(e.getLocalizedMessage());
				} catch (IOException e) {
					System.err.println(e.getLocalizedMessage());
				}
			}
		});
		mnOptRules.add(mntmSaveOptRules);

		return(mnOptRules);
	}
}
