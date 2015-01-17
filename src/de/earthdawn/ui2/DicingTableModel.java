package de.earthdawn.ui2;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.ROLLEDDICEType;

class DicingTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3273868958993125915L;
	public static ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private static final String[] columnNames = {"Step","Dice","Rolling","Result","pathetic","poor","average","good","excellent","extraordinary"};
	private static final int columnCount=columnNames.length;
	private int indexCounter=0;
	private List<Integer> index = new ArrayList<Integer>();
	private List<int[]> steps = new ArrayList<int[]>();
	private List<String[]> dices = new ArrayList<String[]>();
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
		switch(col) {
		case  0: {
			int[] rowSteps = steps.get(row);
			if( (rowSteps==null) || (rowSteps.length<1) ) return "NULL";
			StringBuffer stepresult= new StringBuffer();
			stepresult.append(String.valueOf(rowSteps[0]));
			for(int i=1; i<rowSteps.length;i++) {
				stepresult.append(" + ");
				stepresult.append(String.valueOf(rowSteps[i]));
			}
			return stepresult.toString();
		}
		case  1: {
			String[] rowDices = dices.get(row);
			if( (rowDices==null) || (rowDices.length<1) ) return "NULL";
			StringBuffer dicesresult = new StringBuffer();
			dicesresult.append(rowDices[0]);
			for(int i=1; i<rowDices.length;i++) {
				dicesresult.append(" + ");
				dicesresult.append(rowDices[i]);
			}
			return dicesresult.toString();
		}
		case  2: return rolledDices.get(row).getRolling();
		case  3: return rolledDices.get(row).getResult();
		case  4: return rolledDices.get(row).getPathetic();
		case  5: return rolledDices.get(row).getPoor();
		case  6: return rolledDices.get(row).getAverage();
		case  7: return rolledDices.get(row).getGood();
		case  8: return rolledDices.get(row).getExcellent();
		case  9: return rolledDices.get(row).getExtraordinary();
		}
		return "UNDEF";
	}

	public void add(int[] steps, ROLLEDDICEType rolledDice) {
		this.index.add(indexCounter++);
		this.steps.add(steps);
		String[] diceset = new String[steps.length];
		for(int i=0; i<steps.length;i++) {
			diceset[i] = PROPERTIES.getCharacteristics().getDice(steps[i]);
		}
		this.dices.add(diceset);
		this.rolledDices.add(rolledDice);
		fireTableRowsInserted(index.size()-1, index.size()-1);
	}
}
