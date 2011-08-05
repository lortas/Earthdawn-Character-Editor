package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.TALENTABILITYType;

public class EDCapabilitySelectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ApplicationProperties  PROPERTIES = ApplicationProperties.create();
	private static final ECECapabilities capabilities = new ECECapabilities(PROPERTIES.getCapabilities().getSKILLOrTALENT());
	private static final List<CAPABILITYType> talents = capabilities.getVersatilityTalents();
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JList list;

	private TreeMap<String,CAPABILITYType> capabilityMap = new TreeMap<String,CAPABILITYType>();
	private HashMap<String,CAPABILITYType> selectedCapabilityMap = new HashMap<String,CAPABILITYType>();

	public HashMap<String, CAPABILITYType> getSelectedCapabilitytMap() {
		return selectedCapabilityMap;
	}

	/**
	 * Create the dialog.
	 */
	public EDCapabilitySelectDialog(boolean talent,int maxcirclenr) {
		this(talent,maxcirclenr,null);
	}

	public EDCapabilitySelectDialog(boolean talent,int maxcirclenr, List<TALENTABILITYType> talentabilities) {
		if( talent ) {
			setTitle("Select one talent");
		} else {
			setTitle("Select one or more skills");
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		selectedCapabilityMap = new HashMap<String,CAPABILITYType>();
		initCapabilityList(talent,maxcirclenr,talentabilities);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		list = new JList();
		list.setModel(new AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Set<String> keys = capabilityMap.keySet();
			String[] array = keys.toArray(new String[0]);
			public int getSize() {
				return array.length;
			}
			public Object getElementAt(int index) {
				return array[index];
			}
		});
		scrollPane.setViewportView(list);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						do_okButton_actionPerformed(arg0);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						do_cancelButton_actionPerformed(arg0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void initCapabilityList(boolean talent,int maxcirclenr, List<TALENTABILITYType> talentabilities){
		if( talent ) {
			if( talentabilities == null ) {
				talentabilities=new ArrayList<TALENTABILITYType>();
				HashMap<String, TALENTABILITYType> alltalentabilities = PROPERTIES.getTalentsByCircle(maxcirclenr);
				for( String name : alltalentabilities.keySet() ) {
					talentabilities.add(alltalentabilities.get(name));
				}
			}
			for( TALENTABILITYType talentabilitiy : talentabilities ) {
				String talentabilityName = talentabilitiy.getName();
				String limitation = talentabilitiy.getLimitation();
				int found=0;
				for( CAPABILITYType capability : talents ) {
					if( talentabilityName.equals(capability.getName()) ) {
						CAPABILITYType cap = new CAPABILITYType();
						cap.setName(talentabilityName);
						cap.setLimitation(limitation);
						cap.setAction(capability.getAction());
						cap.setAttribute(capability.getAttribute());
						cap.setBonus(capability.getBonus());
						cap.setDefault(capability.getDefault());
						cap.setIsinitiative(capability.getIsinitiative());
						cap.setKarma(capability.getKarma());
						cap.setNotbyversatility(capability.getNotbyversatility());
						cap.setRealigned(capability.getRealigned());
						cap.setStrain(capability.getStrain());
						if( limitation.isEmpty() ) capabilityMap.put(talentabilityName,cap);
						else capabilityMap.put(talentabilityName+" - "+limitation,cap);
						found++;
					}
				}
				if( found == 0 ) {
					System.err.println( "Talent '"+talentabilityName+"' not found in capability list." );
				} else if( found > 1 ) {
					System.err.println("Talent '"+talentabilityName+"' was found "+found+"times in capability list.");
				}
			}
		} else {
			for (CAPABILITYType capability : capabilities.getSkills()) capabilityMap.put(capability.getName(),capability);
		}
	}

	protected void do_okButton_actionPerformed(ActionEvent arg0) {
		selectedCapabilityMap.clear();
		for(Object capabilityname : list.getSelectedValues()){
			selectedCapabilityMap.put((String)capabilityname,capabilityMap.get((String)capabilityname));
		}
		this.dispose();	
	}
	protected void do_cancelButton_actionPerformed(ActionEvent arg0) {
		selectedCapabilityMap.clear();
		this.dispose();
	}

	public void setSingleSelection( boolean selection ) {
		if( selection ) {
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		} else {
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
	}
}
