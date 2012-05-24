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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.ui2.EDMainWindow;

public class EarthdawnCharacterEditor {
	public static final String VERSION="0.43";
	/**
	 * Main-Funktion. 
	 */
	public static void main(String[] args) {
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			EDCHARACTER ec = new EDCHARACTER();
			// Erster Parameter wenn vorhanden ist der einzulesende Charakterbogen
			if( args.length > 0 ) {
				System.out.println("Read character from " + args[0]);
				// Einlesen des Charakters
				Unmarshaller u = jc.createUnmarshaller();
				ec =(EDCHARACTER)u.unmarshal(new File(args[0]));
				System.out.println("Processing the character: '" + ec.getName() + "'");
			}
			// Verarbeiten
			EDCHARACTER ecOut = new ECEWorker().verarbeiteCharakter(ec);
			ec=ecOut;
			EDMainWindow window = new EDMainWindow(ec);
			if( args.length < 2 ) { 
				// Anzeigen des Hauptdialogs, wenn nur maximal ein Parameter übergeben wurde
				window.setVisible(true);
			} else {
				// Wenn noch ein zweiter Parameter übergeben wurde, schreibe Charakter dort rein
				window.writeToXml(new File(args[1]));
				// Wenn noch ein dritter Parameter übergeben wurde, schreibe pdf export dort rein
				if( args.length > 2 ) new ECEPdfExporter().exportRedbrickExtended(ec,new File(args[2]));
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
