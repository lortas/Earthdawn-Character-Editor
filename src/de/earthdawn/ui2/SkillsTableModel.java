package de.earthdawn.ui2;
/******************************************************************************\
Copyright (C) 2010-2011  Holger von Rhein <lortas@freenet.de>

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
\******************************************************************************/

import javax.swing.table.AbstractTableModel;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.SKILLType;

public class SkillsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1321139806762229027L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final String languageSkillSpeakName = PROPERTIES.getLanguageSkillSpeakName();
	public static final String languageSkillReadWriteName = PROPERTIES.getLanguageSkillReadWriteName();
	private CharacterContainer character;
	private boolean languageSkills;
	private String[] columnNames = {"Name", "Limitation",  "Attribute", "Startrank", "Rank", "Action", "Step", "Dice", "Bookref"};

	public SkillsTableModel(CharacterContainer character, boolean languageSkills) {
		super();
		this.character = character;
		this.languageSkills = languageSkills;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		fireTableStructureChanged();
	}

	public CharacterContainer getCharacter() {
		return character;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if( character == null ) return 0;
		if( languageSkills ) return character.getOnlyLanguageSkills().size();
		return character.getSkills().size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		if( character == null ) return "";
		// {"Name", "Limitation",  "Attribute", "Rank", "Startrank", "Action", "Step" "Dice"};
		SKILLType skill;
		if( languageSkills ) skill = character.getOnlyLanguageSkills().get(row);
		else skill = character.getSkills().get(row);
		switch (col) {
		case 0: return skill.getName();
		case 1:
			if( skill.getLIMITATION().size()<1 ) return "-";
			return skill.getLIMITATION().get(0);
		case 2: return skill.getAttribute();
		case 3: return Integer.valueOf(skill.getRANK().getStartrank());
		case 4: return Integer.valueOf(skill.getRANK().getRank());
		case 5: return skill.getAction().value();
		case 6: return skill.getRANK().getStep();
		case 7: return skill.getRANK().getDice();
		case 8: return skill.getBookref();
		default : return "Error: not defined";
		}
	}

	public Class<?> getColumnClass(int c) {
		final Object v = getValueAt(0, c);
		if( v == null ) return String.class;
		return v.getClass();
	}

	public boolean isCellEditable(int row, int col) {
		if( col == 3 ) return true;
		if( col == 4 ) return true;
		if( languageSkills ) return false;
		SKILLType skill = character.getSkills().get(row);
		if( languageSkillSpeakName.equals(skill.getName()) ) return false;
		if( languageSkillReadWriteName.equals(skill.getName()) ) return false;
		if( col==1 ) return true;
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		SKILLType skill;
		if( languageSkills ) skill = character.getOnlyLanguageSkills().get(row);
		else skill = character.getSkills().get(row);
		if( (col==1) && !languageSkills ) {
			skill.getLIMITATION().clear();
			skill.getLIMITATION().add((String)value);
		}
		if( col == 3 ) {
			int v = (Integer)value;
			RANKType rank = skill.getRANK();
			if( (rank.getRank()<v) || (rank.getRank()==rank.getStartrank()) ) {
				rank.setRank(v);
			}
			rank.setStartrank(v);
		}
		if(col == 4) skill.getRANK().setRank((Integer) value);
		character.refesh();
		fireTableRowsUpdated(row, row);
	}
}
