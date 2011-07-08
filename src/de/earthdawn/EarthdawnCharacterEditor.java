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
import java.io.FileWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.ui2.EDMainWindow;


public class EarthdawnCharacterEditor {
	/**
	 * Main-Funktion. 
	 */
	public static void main(String[] args) {
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			EDCHARACTER ec = new EDCHARACTER();
			// Erster Parameter wenn vorhanden ist der einzulesende Charakterbogen
			if( args.length > 0 ) {
				System.out.println("Lese Charaker aus " + args[0]);
				// Einlesen des Charakters
				Unmarshaller u = jc.createUnmarshaller();			
				ec =(EDCHARACTER)u.unmarshal(new File(args[0]));
			}
			// Verarbeiten
			System.out.println("Verarbeite Charaker: '" + ec.getName() + "'");
			EDCHARACTER ecOut = new ECEWorker().verarbeiteCharakter(ec);
			ec=ecOut;
			// Wenn noch ein Zweiter Parameter übergeben wurde schreibe Charakter dort rein
			if (args.length == 2) { 
				// Ausgabe
				File outFile = new File(args[1]);
				System.out.println("Speichere Charakter in " + outFile);
				Marshaller m = jc.createMarshaller();
				FileWriter fileio = new FileWriter(outFile);
				m.setProperty(Marshaller.JAXB_ENCODING, fileio.getEncoding());
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/character earthdawncharacter.xsd");
				m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				fileio.write("<?xml version=\"1.0\" encoding=\""+fileio.getEncoding()+"\" standalone=\"no\"?>\n");
				fileio.write("<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>\n");
				m.marshal(ec,fileio);
				// Ausgabe (PDF)
				new ECEPdfExporter().exportRedbrickExtended(ec, new File(outFile.getParentFile(), chopFilename(outFile)+ ".pdf"));
			} else {
				// Anzeigen des Hauptdialogs.
				EDMainWindow.main(args);
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
