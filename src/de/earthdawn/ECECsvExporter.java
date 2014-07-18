package de.earthdawn;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.earthdawn.data.*;

public class ECECsvExporter {
	private String encoding="UTF-8";

	public ECECsvExporter(String encoding) {
		setEncoding(encoding);
	}

	public void setEncoding(String encoding) {
		this.encoding=encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void exportSpells(EDCHARACTER edCharakter, File outFile) throws IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		PrintStream out = new PrintStream(new FileOutputStream(outFile), false, encoding);
		String[] header = {"In Matrix","Spell Name","Type","Circle","Threads","Weaving Difficulty","Reattuning Difficulty",
				"Casting Difficulty","Range","Duration","Effect Area","Effect","Book Ref"};
		out.println(generaterow(header));
		List<SPELLType> spells = character.getAllSpells();
		Collections.sort(spells, new SpellComparator());
		for( SPELLType spell : spells ) {
			List<String> row = new ArrayList<String>();
			if( spell.getInmatrix().equals(YesnoType.YES)) row.add( "Yes" );
			else row.add( "" );
			row.add( spell.getName() );
			row.add( spell.getType().value() );
			row.add( String.valueOf(spell.getCircle()) );
			row.add( spell.getThreads() );
			row.add( spell.getWeavingdifficulty() );
			row.add( String.valueOf(spell.getReattuningdifficulty()) );
			row.add( spell.getCastingdifficulty() );
			row.add( spell.getRange() );
			row.add( spell.getDuration() );
			row.add( spell.getEffectarea() );
			row.add( spell.getEffect() );
			row.add( spell.getBookref() );
			out.println(generaterow(row));
		}
		out.close();
	}

	public void exportTalents(EDCHARACTER edCharakter, File outFile) throws IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		PrintStream out = new PrintStream(new FileOutputStream(outFile), false, encoding);
		String[] header = {"Type","Circle","Talent Name","Limitation","Attribute","Rank","Realigned Rank","Start Rank","Rank Bonus","Step",
				"Dice","LP cost","Action","Talent Bonus","Karma","Strain","Teacher Name","Teacher Discipline","Teachers Talent Circle",
				"Teachers Current Circle","Learned By Versatility","Comment","Book Ref"};
		out.println(generaterow(header));
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			printTalents(out, discipline.getDISZIPLINETALENT(), true );
			printTalents(out, discipline.getOPTIONALTALENT(), false );
		}
	}

	public void exportSkills(EDCHARACTER edCharakter, File outFile) throws IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		PrintStream out = new PrintStream(new FileOutputStream(outFile), false, encoding);
		String[] header = {"Skill Name","Limitation","Attribute","Rank","Realigned Rank","Start Rank","Rank Bonus","Step","Dice","LP cost",
				"Action","Skill Bonus","Strain","Default","Book Ref"};
		out.println(generaterow(header));
		List<SKILLType> skills = character.getSkills();
		if( (skills != null) && (! skills.isEmpty()) ) {
			Collections.sort(skills, new SkillComparator());
			for( SKILLType skill : skills ) {
				if( skill.getRealigned() > 0 ) continue;
				List<String> row = new ArrayList<String>();
				row.add( skill.getName() );
				String limitation="";
				if( skill.getLIMITATION().size()>0 ) limitation=skill.getLIMITATION().get(0);
				row.add( limitation );
				RANKType rank = skill.getRANK();
				row.add( skill.getAttribute().value() );
				if( rank == null ) {
					row.add( "-" );
					row.add( "-" );
					row.add( "-" );
					row.add( "-" );
					row.add( "-" );
					row.add( "-" );
					row.add( "-" );
				} else {
					row.add( String.valueOf(rank.getRank()) );
					row.add( String.valueOf(rank.getRealignedrank()) );
					row.add( String.valueOf(rank.getStartrank()) );
					row.add( String.valueOf(rank.getBonus()) );
					row.add( String.valueOf(rank.getStep()) );
					String dice = rank.getDice();
					if( dice == null ) row.add( "-" );
					else row.add( dice );
					row.add( String.valueOf(rank.getLpcost()) );
				}
				row.add( skill.getAction().value() );
				row.add( String.valueOf(skill.getBonus()) );
				row.add( skill.getStrain() );
				row.add( skill.getDefault().value() );
				row.add( skill.getBookref() );
				out.println(generaterow(row));
			}
		}
		out.close();
	}

	public void exportItems(EDCHARACTER edCharakter, File outFile) throws IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		PrintStream out = new PrintStream(new FileOutputStream(outFile), false, encoding);
		int pursecounter=0;
		String[] header = {"Name","Location","Weight","In Use","Kind","Class"};
		out.println(generaterow(header));
		for( ITEMType item : character.getAllNonVirtualItems() ) {
			List<String> row = new ArrayList<String>();
			if( item instanceof COINSType ) {
				COINSType coins = (COINSType)item;
				String name = coins.getName();
				if( name == null ) {
					name = "Purse #"+String.valueOf(++pursecounter);
				} else {
					if( name.isEmpty() ) name = "Purse #"+String.valueOf(++pursecounter);
					else name = "Purse "+name;
				}
				name += " (c:"+coins.getCopper()+" s:"+coins.getSilver()+" g:"+coins.getGold();
				if( coins.getEarth()>0 )      name += " e:"+coins.getEarth();
				if( coins.getWater()>0 )      name += " w:"+coins.getWater();
				if( coins.getAir()>0 )        name += " a:"+coins.getAir();
				if( coins.getFire()>0 )       name += " f:"+coins.getFire();
				if( coins.getOrichalcum()>0 ) name += " o:"+coins.getOrichalcum();
				name +=")";
				row.add( name );
			} else row.add( item.getName() );
			row.add( item.getLocation() );
			row.add( String.valueOf(item.getWeight()) );
			row.add( item.getUsed().value() );
			row.add( item.getKind().value() );
			row.add( item.getClass().getSimpleName() );
			out.println(generaterow(row));
		}
		out.close();
	}

	private void printTalents(PrintStream out, List<TALENTType> talents, boolean discipline) {
		Collections.sort(talents, new TalentComparator());
		for( TALENTType talent : talents ) {
			if( talent.getRealigned() > 0 ) continue;
			List<String> row = new ArrayList<String>();
			if( discipline ) row.add( "D" );
			else row.add( "O" );
			row.add( String.valueOf(talent.getCircle()) );
			row.add( talent.getName() );
			String limitation="";
			if( talent.getLIMITATION().size()>0 ) limitation=talent.getLIMITATION().get(0);
			row.add( limitation );
			RANKType rank = talent.getRANK();
			row.add( talent.getAttribute().value() );
			if( rank == null ) {
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
			} else {
				row.add( String.valueOf(rank.getRank()) );
				row.add( String.valueOf(rank.getRealignedrank()) );
				row.add( String.valueOf(rank.getStartrank()) );
				row.add( String.valueOf(rank.getBonus()) );
				row.add( String.valueOf(rank.getStep()) );
				String dice = rank.getDice();
				if( dice == null ) row.add( "-" );
				else row.add( dice );
				row.add( String.valueOf(rank.getLpcost()) );
			}
			row.add( talent.getAction().value() );
			row.add( String.valueOf(talent.getBonus()) );
			if( discipline ) row.add("-");
			else row.add( String.valueOf(talent.getKarma()) );
			row.add( talent.getStrain() );
			TALENTTEACHERType teacher = talent.getTEACHER();
			if( teacher == null ) {
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
				row.add( "-" );
			} else {
				row.add( teacher.getName() );
				row.add( teacher.getDiscipline() );
				row.add( String.valueOf(teacher.getTalentcircle()) );
				row.add( String.valueOf(teacher.getTeachercircle()) );
				row.add( teacher.getByversatility().value() );
				row.add( teacher.getComment() );
			}
			row.add( talent.getBookref() );
			out.println(generaterow(row));
		}
	}

	public static String generaterow(List<?> list) {
		return generaterow(list.toArray());
	}

	public static String generaterow(Object[] list) {
		StringBuilder result = new StringBuilder(1024);
		for( Object e : list ) {
			if( result.length() > 0 ) result.append(",");
			result.append("\"");
			result.append(e.toString().replaceAll("\"", "''"));
			result.append("\"");
		}
		return result.toString();
	}
}
