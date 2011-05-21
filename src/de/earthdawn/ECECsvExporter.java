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
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.earthdawn.data.*;

public class ECECsvExporter {

	public void exportSpells(EDCHARACTER edCharakter, File outFile) throws Exception, IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		PrintStream out = new PrintStream(outFile);
		out.println( "In Matrix\tSpell Name\tType\tCircle\tThreads\tWeaving Difficulty\t"+
				"Reattuning Difficulty\tCasting Difficulty\tRange\tDuration\t"+
				"Effect Area\tEffect\tBook Ref" );
		for( SPELLSType spells : character.getAllSpells() ) {
			List<SPELLType> spellList = spells.getSPELL();
			Collections.sort(spellList, new SpellComparator());
			for( SPELLType spell : spellList ) {
				List<String> row = new ArrayList<String>();
				if( spell.getInmatrix().equals(YesnoType.YES)) row.add( "Yes" );
				else row.add( "" );
				row.add( spell.getName() );
				row.add( spell.getType().value() );
				row.add( String.valueOf(spell.getCircle()) );
				if( spell.getThreads() >= 0 ) {
					row.add( String.valueOf(spell.getThreads()) );
				} else row.add( "s. text" );
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
		}
	}

	public void exportTalents(EDCHARACTER edCharakter, File outFile) throws Exception, IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		PrintStream out = new PrintStream(outFile);
		out.println( "Type\tCircle\tTalent Name\tLimitation\tAttribute\tRank\tRealigned Rank\t"+
				"Start Rank\tRank Bonus\tStep\tDice\tLP cost\tAction\tTalent Bonus\t"+
				"Karma\tStrain\tTeacher Name\tTeacher Discipline\tTeachers Talent Circle\t"+
				"Teachers Current Circle\tLearned By Versatility\tComment\tBook Ref" );
		for( TALENTSType allTalents : character.getAllTalents() ) {
			printTalents(out, allTalents.getDISZIPLINETALENT(), true );
			printTalents(out, allTalents.getOPTIONALTALENT(), false );
		}
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
			row.add( talent.getLimitation() );
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
				DiceType dice = rank.getDice();
				if( dice == null ) row.add( "-" );
				else row.add( dice.value() );
				row.add( String.valueOf(rank.getLpcost()) );
			}
			row.add( talent.getAction().value() );
			row.add( String.valueOf(talent.getBonus()) );
			if( discipline ) row.add("-");
			else row.add( String.valueOf(talent.getKarma()) );
			row.add( String.valueOf(talent.getStrain()) );
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

	public static String generaterow(List<String> list) {
		String result="";
		for( Object e : list ) {
			if( ! result.isEmpty() ) result+="\t";
			result+=String.valueOf(e);
		}
		return result;
	}
}
