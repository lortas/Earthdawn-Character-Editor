package de.earthdawn.ui2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import de.earthdawn.DiceCups;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.DiceType;
import de.earthdawn.data.ROLLEDDICEType;

public class EDDicing {
	private static final ResourceBundle NLS = ResourceBundle.getBundle("de.earthdawn.ui2.NLS"); //$NON-NLS-1$
	private JFrame frame;
	private JScrollPane scrollPane;
	private Box topPanel;
	private JTable table;

	public EDDicing() {
		String frameName = NLS.getString("EDMainWindow.mntmDicing.text"); //$NON-NLS-1$
		frame = new JFrame(frameName);
		frame.setBounds(200, 200, 900, 500);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		scrollPane = new JScrollPane();
		topPanel = Box.createHorizontalBox();
		for (int i = 0; i < 6; i++) {
			Button b = new Button("Würfelbecher " + i);
			b.addActionListener(null);//TODO: ActionListener der den Würfelbecher würfel läßt
			topPanel.add(b);
		}

		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(topPanel,BorderLayout.NORTH);
		
		table = new JTable();
		table.setRowSelectionAllowed(false);
		table.setSurrendersFocusOnKeystroke(true);
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.setAutoCreateRowSorter(false);
		DicingTableModel dicingTable = new DicingTableModel();
		table.setModel(dicingTable);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(1).setMaxWidth(200);
		table.getColumnModel().getColumn(3).setMaxWidth(60);
		table.getColumnModel().getColumn(4).setMaxWidth(60);
		table.getColumnModel().getColumn(5).setMaxWidth(60);
		table.getColumnModel().getColumn(6).setMaxWidth(60);
		table.getColumnModel().getColumn(7).setMaxWidth(60);
		table.getColumnModel().getColumn(8).setMaxWidth(60);
		table.getColumnModel().getColumn(9).setMaxWidth(60);
		scrollPane.setViewportView(table);
		for(int j=0; j<5;j++) {
			int step=4;
			for(int i=0; i<10; i++) {
				int[] steps={step++,step++};
				dicingTable.add(steps,(new DiceCups(steps)).toss());
			}
		}
	}
}

class DicingTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final int columnCount=10;
	private static final String[] columnNames = {"Step","Dice","Rolling","Result","pathetic","poor","average","good","excellent","extraordinary"};
	private int indexCounter=0;
	private List<Integer> index = new ArrayList<Integer>();
	private List<int[]> steps = new ArrayList<int[]>();
	private List<DiceType[]> dices = new ArrayList<DiceType[]>();
	private List<ROLLEDDICEType> rolledDices = new ArrayList<ROLLEDDICEType>();

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public int getRowCount() {
		return index.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		ROLLEDDICEType rowData = rolledDices.get(row);
		switch(col) {
		case  0: {
			int[] rowSteps = steps.get(row);
			if( rowSteps == null ) return "NULL";
			String stepresult=String.valueOf(rowSteps[0]);
			for(int i=1; i<rowSteps.length;i++) stepresult += " + "+String.valueOf(rowSteps[i]);
			return stepresult;
		}
		case  1: {
			DiceType[] rowDices = dices.get(row);
			if( rowDices == null ) return "NULL";
			String dicesresult=rowDices[0].value();
			for(int i=1; i<rowDices.length;i++) dicesresult += " + "+rowDices[i].value();
			return dicesresult;
		}
		case  2: return rowData.getRolling();
		case  3: return rowData.getResult();
		case  4: return rowData.getPathetic();
		case  5: return rowData.getPoor();
		case  6: return rowData.getAverage();
		case  7: return rowData.getGood();
		case  8: return rowData.getExcellent();
		case  9: return rowData.getExtraordinary();
		}
		return "UNDEF";
	}

	public void add(int[] steps, ROLLEDDICEType rolledDice) {
		this.index.add(indexCounter++);
		this.steps.add(steps);
		DiceType[] diceset = new DiceType[steps.length];
		for(int i=0; i<steps.length;i++) {
			diceset[i] = ApplicationProperties.create().getCharacteristics().getSTEPDICEbyStep(steps[i]).getDice();
		}
		this.dices.add(diceset);
		this.rolledDices.add(rolledDice);
		fireTableRowsInserted(index.size()-1, index.size()-1);
	}
}