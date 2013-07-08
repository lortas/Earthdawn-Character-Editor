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
import de.earthdawn.ui2.EDMainWindow;

public class EarthdawnCharacterEditor {
	private static final File LASTEDITEDCHARACTER = new File("lasteditedcharacter.xml");
	public static final String VERSION="0.47";
	/**
	 * Main-Funktion. 
	 */
	public static void main(String[] args) {
		try {
			CharacterContainer ec;
			File infile;
			// Erster Parameter wenn vorhanden ist der einzulesende Charakterbogen
			if( args.length > 0 ) {
				infile=new File(args[0]);
			} else {
				infile=LASTEDITEDCHARACTER;
			}
			if( infile.canRead() ) {
				System.out.println("Read character from "+infile.getCanonicalPath());
				ec=new CharacterContainer(infile);
				String name=ec.getName();
				if( name==null || name.isEmpty() ) {
					name=infile.getName();
				}
				System.out.println("Processing the character: '" + name + "'");
			} else {
				ec = new CharacterContainer();
			}
			// Verarbeiten
			EDMainWindow window = new EDMainWindow(new ECEWorker(ec).verarbeiteCharakter());
			if( args.length < 2 ) { 
				// Anzeigen des Hauptdialogs, wenn nur maximal ein Parameter übergeben wurde
				window.setVisible(true);
				while( window.isDisplayable() ) Thread.sleep(500);
				//Speichere letzten Charakter extra ab
				window.writeToXml(LASTEDITEDCHARACTER);
			} else {
				// Wenn noch ein zweiter Parameter übergeben wurde, schreibe Charakter dort rein
				window.writeToXml(new File(args[1]));
				// Wenn noch ein dritter Parameter übergeben wurde, schreibe pdf export dort rein
				if( args.length > 2 ) new ECEPdfExporter().exportRedbrickExtended(window.getEDCharacter(),new File(args[2]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String chopFilename(File f){
		String filename = f.getName();
		int dotPlace = filename.lastIndexOf ( '.' );
		if( dotPlace >= 0 ) return filename.substring( 0, dotPlace );
		return filename;
	}
}
