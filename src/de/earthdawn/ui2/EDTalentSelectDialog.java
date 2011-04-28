package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.xml.bind.JAXBElement;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.CAPABILITYType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EDTalentSelectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JList list;
	
	
	private TreeMap<String,CAPABILITYType> talentMap = new TreeMap<String,CAPABILITYType>();
	private HashMap<String,CAPABILITYType> selectedTalentMap = new HashMap<String,CAPABILITYType>();

	public HashMap<String, CAPABILITYType> getSelectedTalentMap() {
		return selectedTalentMap;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EDTalentSelectDialog dialog = new EDTalentSelectDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EDTalentSelectDialog() {
		setTitle("Select one talent");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		selectedTalentMap = new  HashMap<String,CAPABILITYType>();
		initTalentList();
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
			Set<String> keys = talentMap.keySet();
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

	public void initTalentList(){
		List<JAXBElement<CAPABILITYType>> capabilities = ApplicationProperties.create().getCapabilities().getSKILLOrTALENT();
		for (JAXBElement<CAPABILITYType> element : capabilities) {
			if (element.getName().getLocalPart().equals("TALENT")) {
				CAPABILITYType talent = (CAPABILITYType)element.getValue();
				talentMap.put(talent.getName(),talent);
			}
		}
	}

	protected void do_okButton_actionPerformed(ActionEvent arg0) {
		selectedTalentMap.clear();
		for(Object talentname : list.getSelectedValues()){
			selectedTalentMap.put((String)talentname,talentMap.get((String)talentname));
		}
		this.dispose();	
	}
	protected void do_cancelButton_actionPerformed(ActionEvent arg0) {
		selectedTalentMap.clear();
		this.dispose();
	}
}
