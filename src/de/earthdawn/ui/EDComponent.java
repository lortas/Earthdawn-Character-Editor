package de.earthdawn.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.EDCHARACTER;

/**
 * Anzeigeelement f�r einen Earthdawn Charakter. 
 */
@SuppressWarnings("serial")
public class EDComponent extends JComponent implements ActionListener {

	private static final String ACTION_RASSE_GEWAEHLT = "__RASSE_GEWAEHLT";

	/** Instanz des aktuellen Charakters. */
	private EDCHARACTER charakter;

	/** Panel zur Anzeige der Attribute eines Charakters. */
	private AttributePanel attributePanel;

	/** Panel zur Anzeige der Allgemeinen Eigenschaften eines Charakters */
	private AllgemeineEigenschaftenPanel allgemeineEigenschaftenPanel;

	/**
	 * Default Konstruktor.
	 */
	public EDComponent() {
		// Default: Leerer Charakter
		this.charakter = new EDCHARACTER();
		initComponents();
	}

	public void setEDCHARACTER(EDCHARACTER charakter) {
		this.charakter = charakter;
		charakterAnzeigen();
	}

	public EDCHARACTER getEDCHARACTER() {
		charakterDatenUebernehemen();
		return this.charakter;
	}

	/**
	 * Benarichtigung durch AWT, dass ein Event f�r diese Component ausgel�st wurde.
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ACTION_RASSE_GEWAEHLT.equals(ae.getActionCommand())) {
			rasseItemGewaehlt();
		}
	}

	private void charakterAnzeigen() {
		// TODO: Ist Bezeichung der Name?
		this.allgemeineEigenschaftenPanel.charakterAnzeigen(this.charakter);
		
		for (Object o : this.charakter.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (o instanceof ATTRIBUTEType) {
				this.attributePanel.attributAnzeigen((ATTRIBUTEType) o);
			}
			/*else if (o instanceof AUSRUESTUNGType) {
				// TODO ...
			}*/
			
			// TODO weitere Merkmale anzeigen ...
		}
	}

	private void charakterDatenUebernehemen() {
		// Allgemeine Eigenschaften �bernehmen
		this.allgemeineEigenschaftenPanel.addToCharakter(this.charakter);
		
		// �bernehmen der Attribute
		this.attributePanel.addToCharakter(this.charakter);
	}
	
	private void rasseItemGewaehlt() {
		// TODO: Hier m�ssen dann die Talente, Zauber usw. entsprechend angepasst werden ...
		JOptionPane.showMessageDialog(this, "Rasse gew�hlt: " + 
				this.allgemeineEigenschaftenPanel.getComboRasse().getSelectedItem());
	}

	/**
	 * Initialisiert die Steuerelemente dieser Komponente.
	 */
	private void initComponents() {
		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.setTabPlacement(SwingConstants.LEFT);
		
		this.allgemeineEigenschaftenPanel = new AllgemeineEigenschaftenPanel();
		// Darauf reagieren, wenn eine andere Rasse ausgew�hlt wird.
		this.allgemeineEigenschaftenPanel.getComboRasse().setActionCommand(ACTION_RASSE_GEWAEHLT);
		this.allgemeineEigenschaftenPanel.getComboRasse().addActionListener(this);

		tabbedPane.addTab("Allgemeines", this.allgemeineEigenschaftenPanel);
		tabbedPane.addTab("Charakteristika", new JPanel());
		tabbedPane.addTab("Attribute", this.attributePanel = new AttributePanel());
	}
}
