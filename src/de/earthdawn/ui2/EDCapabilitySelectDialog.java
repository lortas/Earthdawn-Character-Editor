package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.CAPABILITYType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EDCapabilitySelectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public EDCapabilitySelectDialog(boolean talent) {
		if( talent ) {
			setTitle("Select one talent");
		} else {
			setTitle("Select one or more skills");
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		selectedCapabilityMap = new HashMap<String,CAPABILITYType>();
		initCapabilityList(talent);
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

	public void initCapabilityList(boolean talent){
		ECECapabilities capabilities = new ECECapabilities(ApplicationProperties.create().getCapabilities().getSKILLOrTALENT());
		if( talent ) {
			for (CAPABILITYType capability : capabilities.getTalents()) capabilityMap.put(capability.getName(),capability);
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
}
