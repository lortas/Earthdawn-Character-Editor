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
				if( spell.getInmatrix().equals(YesnoType.YES)) out.print( "Yes\t" );
				else out.print( "\t" );
				out.print( spell.getName() + "\t" );
				out.print( spell.getType().value() + "\t" );
				out.print( String.valueOf(spell.getCircle()) + "\t" );
				if( spell.getThreads() >= 0 ) {
					out.print( String.valueOf(spell.getThreads()) + "\t");
				} else out.print( "s. text\t" );
				out.print( spell.getWeavingdifficulty() + "\t" );
				out.print( spell.getReattuningdifficulty() + "\t" );
				out.print( spell.getCastingdifficulty() + "\t" );
				out.print( spell.getRange() + "\t" );
				out.print( spell.getDuration() + "\t" );
				out.print( spell.getEffectarea() + "\t" );
				out.print( spell.getEffect() + "\t");
				out.print( spell.getBookref() );
				out.println();
			}
		}
	}
}
