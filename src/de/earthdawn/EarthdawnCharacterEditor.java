package de.earthdawn;
/*****************************************************************\
Copyright (C) 2010-2011  Holger von Rhein <lortas@freenet.de>

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA  02110-1301, USA.
\*****************************************************************/

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
			// TODO: Kommandozeilenparameter vorgeben! Momentan: Param1: Input; Param2: Output
			if (args.length == 2) { 
				System.out.println("Lese Charaker aus " + args[0]);
				// Einlesen des Charakters
				JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
				Unmarshaller u = jc.createUnmarshaller();			
				EDCHARACTER ec =(EDCHARACTER)u.unmarshal(new File(args[0]));
				// ec = new EDCHARACTER(); // Test ob auch ein Leerer Bogen verarbeitet werden kann.
				
				// Verarbeiten
				System.out.println("Verarbeite Charaker: '" + ec.getName() + "'");
				EDCHARACTER ecOut = new ECEWorker().verarbeiteCharakter(ec);
				
				// Ausgabe
				File outFile = new File(args[1]);
				System.out.println("Speichere Charakter in " + outFile);
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(ecOut,new FileWriter(outFile));

				// Ausgabe (PDF)
				new ECEPdfExporter().export(ecOut, new File(outFile.getParentFile(), outFile.getName() + ".pdf"));
			} else {
				// Anzeigen des Hauptdialogs.
				EDMainWindow.main(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
