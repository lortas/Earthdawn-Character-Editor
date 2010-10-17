package de.earthdawn;

import java.io.File;
import java.io.FileWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.data.EDCHARAKTER;
import de.earthdawn.ui.EDFrame;


public class EarthdawnCharacterGenerator {

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
				EDCHARAKTER ec =(EDCHARAKTER)u.unmarshal(new File(args[0]));
				
				// Verarbeiten
				System.out.println("Verarbeite Charaker");
				EDCHARAKTER ecOut = new ECGWorker().verarbeiteCharakter(ec);
				
				// Ausgabe
				System.out.println("Speichere Charakter in " + args[1]);
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(ecOut,new FileWriter( new File(args[1])));
				
			} else {
				// Anzeigen des Hauptdialogs.
				new EDFrame().setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
