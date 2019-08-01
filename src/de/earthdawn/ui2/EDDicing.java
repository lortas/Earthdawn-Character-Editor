package de.earthdawn.ui2;

import java.util.ResourceBundle;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class EDDicing extends JFrame {
	private static final long serialVersionUID = 3273868958993125914L;
	private static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS"); //$NON-NLS-1$
	private static final String frameName = NLS.getString("EDMainWindow.mntmDicing.text"); //$NON-NLS-1$
	private JPanel panelTop = new JPanel();
	private JPanel panelLeft = new JPanel();
	private JPanel panelRight = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	JComboBox<String> comboBoxNewDiceStep = new JComboBox<String>();
	JComboBox<String> comboBoxDestinationDiceCup = new JComboBox<String>();

	public EDDicing(JFrame parent) {
		super(frameName);
		setSize(new Dimension(672, 672));
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panelLeft, BorderLayout.WEST);
		getContentPane().add(panelRight, BorderLayout.EAST);
		getContentPane().add(panelTop, BorderLayout.NORTH);
		panelRight.add(scrollPane);
		panelRight.setMinimumSize(new Dimension(300,300));

		panelLeft.setLayout(new MigLayout("", "[100px:n]", "[20px:n][20px:n][20px:n][20px:n][20px:n][20px:n]"));

		JComboBox<String> comboBoxAmountDiceCup = new JComboBox<String>();
		for( int i=1; i<20; i++ ) {
			comboBoxAmountDiceCup.addItem(String.valueOf(i));
		}

		JTextArea descriptionText = new JTextArea();
		descriptionText.setLineWrap(true);
		descriptionText.setText("Folgende Würfelstufe einem Würfelbecher beifügen");
		panelLeft.add(descriptionText, "cell 0 0");
		panelLeft.add(new JLabel(NLS.getString("EDDicingWindow.stepselection.text")), "cell 0 1");
		panelLeft.add(new JLabel(NLS.getString("EDDicingWindow.dicecupselection.text")), "cell 0 2");
		panelLeft.add(new JLabel(NLS.getString("EDDicingWindow.amountdicecup.text")), "cell 0 4");
		panelLeft.add(comboBoxAmountDiceCup, "cell 0 5");

		panelTop.setLayout(new MigLayout("", "[][][][][][][][]", "[20px:n]"));

		JTable table = new JTable();
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		table.setSurrendersFocusOnKeystroke(true);
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.setAutoCreateRowSorter(false);
		table.setModel(new DicingTableModel());
		scrollPane.setMinimumSize(new Dimension(200, 100));
		scrollPane.setViewportView(table);
	}

	private Component createCupButton(int i) {
		Button b = new Button(NLS.getString("EDDicingWindow.dicecup.text")+" " + i);
		b.addActionListener(null);//TODO: ActionListener der den Würfelbecher würfel läßt
		comboBoxDestinationDiceCup.addItem(String.valueOf(i));
		return b;
	}

	private void do_comboBoxAmountDiceCup_itemStateChanged(ItemEvent arg0) {
		if( panelTop == null ) return;
		if( arg0 == null ) return;
		if(arg0.getStateChange() == 1) {
			int amount = Integer.valueOf((String)arg0.getItem());
			while( panelTop.getComponentCount() < amount ) {
				panelTop.add(createCupButton(panelTop.getComponentCount()+1));
			}
			while( panelTop.getComponentCount() > amount ) {
				panelTop.remove(panelTop.getComponentCount()-1);
			}
			panelTop.validate();
		}
	}
}
