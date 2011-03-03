package de.earthdawn.config;
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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.data.*;

/** 
 * Klasse mit Konfigurations-Parametern 
 */
public class ApplicationProperties {

    /** Ein- und Ausgabe der Allgemeinen Konfigurationseinstellungen. */
    private static CAPABILITIES CAPABILITIES = new CAPABILITIES();
    private static KNACKS KNACKS = new KNACKS();
    private static SPELLS SPELLS = new SPELLS();
    private static NAMEGIVERS NAMEGIVERS = new NAMEGIVERS();
    private static OPTIONALRULES OPTIONALRULES = new OPTIONALRULES();
    private static NAMES NAMES = new NAMES();
    private static HELP HELP = new HELP();
    private static LanguageType LANGUAGE = LanguageType.EN;
    private ECECharacteristics CHARACTERISTICS = null;
    
    /** Singleton-Instanz dieser Klasse. */
    private static ApplicationProperties theProps = null;

    /** Anzeigetexte (Steuerelemente) */
    private ResourceBundle MESSAGES = null;

    /** Disziplinen (Name Label geordnet) */
    private static final Map<String, DISCIPLINE> DISCIPLINES = new TreeMap<String, DISCIPLINE>();

    private ApplicationProperties() {
    	init();
    }

	/**
	 * Zugriff auf Applikations-Konstanten.
	 * 
	 * @return Instanz der Applikations-Konstanten.
	 */
	public static ApplicationProperties create() {
		if (theProps == null) {
			theProps = new ApplicationProperties();
		}
		
		return theProps;
	}

	/**
	 * Gibt internationalisierten Text zu <code>key</code> zur�ck.
	 */
	public String getMessage(String key) {
		return MESSAGES.getString(key);
	}
	
	public DISCIPLINE getDisziplin(String name) {
		DISCIPLINE discipline = DISCIPLINES.get(name);
		if( discipline == null ) {
			System.err.println("Dicipline '"+name+"' does not exist.");
		}
		return discipline;
	}

	public Set<String> getAllDisziplinNames() {
		return DISCIPLINES.keySet();
	}

	public Collection<DISCIPLINE> getAllDisziplines() {
		return DISCIPLINES.values();
	}

	public NAMEGIVERS getNamegivers() {
		return NAMEGIVERS;
	}
	
	public ECECharacteristics getCharacteristics() {
		return CHARACTERISTICS;
	}

	public CAPABILITIES getCapabilities() {
		return CAPABILITIES;
	}

	public static KNACKS getKnacks() {
		return KNACKS;
	}

	public HashMap<String,SPELLDEFType> getSpells() {
		HashMap<String,SPELLDEFType> spellmap = new HashMap<String,SPELLDEFType>();
		for( SPELLDEFType spell : SPELLS.getSPELL() ) {
			spellmap.put(spell.getName(), spell);
		}
		return spellmap;
	}

	public HashMap<String,List<List<DISCIPLINESPELLType>>> getSpellsByDiscipline() {
		HashMap<String,List<List<DISCIPLINESPELLType>>> result = new HashMap<String,List<List<DISCIPLINESPELLType>>>();
		for(DISCIPLINE discipline : getAllDisziplines()){
			List<List<DISCIPLINESPELLType>> spells = new ArrayList<List<DISCIPLINESPELLType>>();
			for( DISCIPLINECIRCLEType circle : discipline.getCIRCLE() ) {
				spells.add(circle.getSPELL());
			}
			if( ! spells.isEmpty() ) result.put(discipline.getName(), spells);
		}
		return result;
	}

	public List<SPELLType> getSpells4Grimoir() {
		List<SPELLType> result = new ArrayList<SPELLType>();
		HashMap<String, List<List<DISCIPLINESPELLType>>> spellsByDiscipline = getSpellsByDiscipline();
		HashMap<String, SPELLDEFType> spells = getSpells();
		for( String discipline : spellsByDiscipline.keySet() ) {
			int circlenr=0;
			for( List<DISCIPLINESPELLType> disciplineSpells : spellsByDiscipline.get(discipline)) {
				circlenr++;
				for( DISCIPLINESPELLType s : disciplineSpells ) {
					SPELLDEFType spelldef = spells.get(s.getName());
					if(spelldef != null) {
						SPELLType spell = new SPELLType();
						spell.setCastingdifficulty(spelldef.getCastingdifficulty());
						spell.setCircle(circlenr);
						spell.setDuration(spelldef.getDuration());
						spell.setEffect(spelldef.getEffect());
						spell.setEffectarea(spelldef.getEffectarea());
						spell.setName(spelldef.getName());
						spell.setRange(spelldef.getRange());
						spell.setReattuningdifficulty(spelldef.getReattuningdifficulty());
						spell.setThreads(spelldef.getThreads());
						spell.setType(s.getType());
						spell.setWeavingdifficulty(spelldef.getWeavingdifficulty());
						spell.setType(s.getType());
						result.add(spell);
					} else{
						System.err.println("Spell " + s.getName() + "(" + discipline +") not found!" );
					}
				}
			}
		}
		return result;
	}

	public OPTIONALRULES getOptionalRules() {
		return OPTIONALRULES;
	}

	public String getKarmaritualName() {
		for( NAMELANGType name : NAMES.getKARMARUTUAL() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getName();
		}
		// Not found
		return null;
	}

	public String getDurabilityName() {
		for( NAMELANGType name : NAMES.getDURABILITY() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getName();
		}
		// Not found
		return null;
	}

	public List<ITEMType> getStartingItems() {
		for( NAMESTARTINGITEMSType name : NAMES.getSTARTINGITEMS() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getITEM();
		}
		// Not found
		return null;
	}

	public List<WEAPONType> getStartingWeapons() {
		for( NAMESTARTINGWEAPONSType name : NAMES.getSTARTINGWEAPONS() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getWEAPON();
		}
		// Not found
		return null;
	}

	public HashMap<SpellkindType,String> getSpellKindMap() {
		for( NAMESPELLWEAVINGType name : NAMES.getSPELLWEAVING() ) {
			if( name.getLang().equals(LANGUAGE)) {
				HashMap<SpellkindType,String> spellTypeMap = new HashMap<SpellkindType,String>();
				for( NAMESPELLKINDType type : name.getSPELLKIND() ) {
					spellTypeMap.put(type.getType(),type.getWeaving());
				}
				return spellTypeMap;
			}
		}
		// Not found
		return null;
	}

	public HELP getHelp() {
		return HELP;
	}

	private void init() {
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			Unmarshaller u = jc.createUnmarshaller();
			String filename="";

			// disziplinen laden
			// --- Bestimmen aller Dateien im Unterordner 'disziplins'
			File[] files = new File("./config/disciplines").listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name != null && name.endsWith(".xml");
				}
			});
			// --- Einlesen der Dateien
			for(File disConfigFile : files) {
				System.out.println("Lese Konfigurationsdatei: '" + disConfigFile.getCanonicalPath() + "'");
				DISCIPLINE dis = (DISCIPLINE) u.unmarshal(disConfigFile);
				DISCIPLINES.put(dis.getName(), dis);
			}

			filename="./config/characteristics.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			CHARACTERISTICS = new ECECharacteristics((CHARACTERISTICS) u.unmarshal(new File(filename)));
			filename="./config/capabilities.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			CAPABILITIES = (CAPABILITIES) u.unmarshal(new File(filename));
			filename="./config/knacks.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			KNACKS = (KNACKS) u.unmarshal(new File(filename));
			filename="./config/spells.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			SPELLS = (SPELLS) u.unmarshal(new File(filename));
			filename="./config/namegivers.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			NAMEGIVERS = (NAMEGIVERS) u.unmarshal(new File(filename));
			filename="./config/optionalrules.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			OPTIONALRULES = (OPTIONALRULES) u.unmarshal(new File(filename));
			filename="./config/names.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			NAMES = (NAMES) u.unmarshal(new File(filename));
			filename="./config/help.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			HELP = (HELP) u.unmarshal(new File(filename));

		} catch (Throwable e) {
			// Fehler ist grundsätzlicher Natur ...
			throw new RuntimeException(e);
		}
	}
}
