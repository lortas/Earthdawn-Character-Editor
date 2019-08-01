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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.LanguageType;
import de.earthdawn.data.RulesetversionType;
import de.earthdawn.ui2.EDMainWindow;

public class EarthdawnCharacterEditor {
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private static final File LASTEDITEDCHARACTER = new File("lasteditedcharacter.xml");
	private static List<String> commandlineargs = new ArrayList<String>();
	public static final String VERSION=(System.class.getPackage().getImplementationVersion()==null)?"":System.class.getPackage().getImplementationVersion();

	public static void main(String[] args) {
		commandlineargs = new ArrayList<String>(Arrays.asList(args));
		PROPERTIES.setRulesetLanguage(getRulesetversionFromArgs(), getLanguageFromArgs());
		boolean newchar=getNewCharFromArgs();
		try {
			CharacterContainer ec;
			File infile;
			// Erster Parameter, wenn vorhanden, ist der einzulesende Charakterbogen
			if(  commandlineargs.size() > 0 ) {
				infile=new File(commandlineargs.get(0));
			} else {
				infile=LASTEDITEDCHARACTER;
			}
			if( !newchar && infile.canRead() ) {
				System.out.println("Read character from "+infile.getCanonicalPath());
				try {
					ec=new CharacterContainer(infile);
				} catch( RuntimeException e) {
					if( e.getMessage().contains("has wrong Rulesetversion") ) {
						ec=new CharacterContainer();
					} else if( e.getMessage().contains("has wrong language") ) {
						ec=new CharacterContainer();
					} else {
						throw(e);
					}
				}
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
			if( commandlineargs.size() < 2 ) { 
				// Anzeigen des Hauptdialogs, wenn nur maximal ein Parameter übergeben wurde
				window.setVisible(true);
				while( window.isDisplayable() ) Thread.sleep(500);
				//Speichere letzten Charakter extra ab
				window.writeToXml(LASTEDITEDCHARACTER);
			} else {
				// Wenn noch ein zweiter Parameter übergeben wurde, schreibe Charakter dort rein
				window.writeToXml(new File(commandlineargs.get(1)));
				// Wenn noch ein dritter Parameter übergeben wurde, schreibe pdf export dort rein
				if( commandlineargs.size() > 2 ) new ECEPdfExporter().exportRedbrickExtended(window.getEDCharacter(),new File(commandlineargs.get(2)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static RulesetversionType getRulesetversionFromArgs() {
		int i = commandlineargs.indexOf("--rulesetversion");
		if( i < 0 ) commandlineargs.indexOf("-r");
		if( i >= 0 ) {
			commandlineargs.remove(i); // --rulesetversion | -r
			String s = commandlineargs.remove(i); // Parameter von --rulesetversion
			if( s != null ) {
				RulesetversionType rs = RulesetversionType.fromValue(s);
				if( rs != null ) return rs;
			}
		}
		return RulesetversionType.ED_4;
	}

	private static LanguageType getLanguageFromArgs() {
		int i = commandlineargs.indexOf("--language");
		if( i < 0 ) commandlineargs.indexOf("-l");
		if( i >= 0 ) {
			commandlineargs.remove(i); // --language | -l
			String s = commandlineargs.remove(i); // Parameter von --language
			if( s != null ) {
				LanguageType lang = LanguageType.fromValue(s);
				if( lang != null ) return lang;
			}
		}
		return LanguageType.DE;
	}

	private static boolean getNewCharFromArgs() {
		int i = commandlineargs.indexOf("--newchar");
		if( i < 0 ) commandlineargs.indexOf("-n");
		if( i >= 0 ) {
			commandlineargs.remove(i); // --newchar | -n
			return true;
		}
		return false;
	}

	public static String chopFilename(File f){
		String filename = f.getName();
		int dotPlace = filename.lastIndexOf ( '.' );
		if( dotPlace >= 0 ) return filename.substring( 0, dotPlace );
		return filename;
	}
}
