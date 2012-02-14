package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
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
import de.earthdawn.data.RANKType;
import de.earthdawn.data.SKILLType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.YesnoType;

public class EDCapabilitySelectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ApplicationProperties  PROPERTIES = ApplicationProperties.create();
	private static final ECECapabilities capabilities = PROPERTIES.getCapabilities();
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JList list;

	private TreeMap<String,SKILLType> capabilityMap = new TreeMap<String,SKILLType>();
	private HashMap<String,SKILLType> selectedCapabilityMap = new HashMap<String,SKILLType>();

	public HashMap<String, SKILLType> getSelectedCapabilitytMap() {
		return selectedCapabilityMap;
	}
	public static final int SELECT_SKILLS=0;
	public static final int SELECT_TALENT=1;
	public static final int SELECT_VERSATILITYTALENT=2;

	/**
	 * Create the dialog.
	 */
	public EDCapabilitySelectDialog(int talent,int maxcirclenr,Rectangle dim) {
		this(talent,maxcirclenr,null,dim);
	}

	public EDCapabilitySelectDialog(int talent,int maxcirclenr, List<TALENTABILITYType> talentabilities,Rectangle dim) {
		switch( talent ) {
		case SELECT_SKILLS: setTitle("Select one or more skills"); break;
		case SELECT_TALENT: setTitle("Select one talent"); break;
		case SELECT_VERSATILITYTALENT: setTitle("Select one versatility talent"); break;
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setBounds(dim);
		selectedCapabilityMap = new HashMap<String,SKILLType>();
		initCapabilityList(talent,maxcirclenr,talentabilities);
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

	public void initCapabilityList(int talent,int maxcirclenr, List<TALENTABILITYType> talentabilities){
		if( talent == SELECT_SKILLS ) {
			for (CAPABILITYType capability : capabilities.getSkills()) {
				List<String> limitations = capability.getLIMITATION();
				if( limitations.size()==0 ) limitations.add("");
				for( String limitation : limitations ) {
					SKILLType skill = new SKILLType();
					RANKType rank = new RANKType();
					skill.setRANK(rank);
					skill.setName(capability.getName());
					skill.setLimitation(limitation);
					capabilities.enforceCapabilityParams(skill);
					String name = capability.getName();
					if( ! limitation.isEmpty() ) name += " : "+ limitation;
					capabilityMap.put(name,skill);
				}
			}
		} else {
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
				CAPABILITYType capability = capabilities.getTalent(talentabilityName);
				if( capability == null ) {
					System.err.println( "Talent '"+talentabilityName+"' not found in capability list." );
				} else if( (talent==SELECT_TALENT) ||
						((talent==SELECT_VERSATILITYTALENT) && capability.getNotbyversatility().equals(YesnoType.NO)) ) {
					SKILLType cap = new SKILLType();
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
