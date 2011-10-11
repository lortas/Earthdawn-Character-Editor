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

import de.earthdawn.CharacterContainer;
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
    private static ITEMS ITEMS = new ITEMS();
    private static NAMES NAMES = new NAMES();
    private static HELP HELP = new HELP();
    private static LanguageType LANGUAGE = LanguageType.EN;
    private static EDRANDOMNAME RANDOMNAMES = new EDRANDOMNAME();
    private static SPELLDESCRIPTIONS SPELLDESCRIPTIONS = new SPELLDESCRIPTIONS();
    private ECECharacteristics CHARACTERISTICS = null;
    
    /** Singleton-Instanz dieser Klasse. */
    private static ApplicationProperties theProps = null;

    /** Anzeigetexte (Steuerelemente) */
    private ResourceBundle MESSAGES = null;

    /** Disziplinen (Name Label geordnet) */
    private static final Map<String, DISCIPLINE> DISCIPLINES = new TreeMap<String, DISCIPLINE>();
    /** RandomCharacterTemplates (Name Label geordnet) */
    private static final RandomCharacterTemplates RANDOMCHARACTERTEMPLATES = new RandomCharacterTemplates();

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
	
	public static List<Integer> getNumberOfOptionalTalentsPerCircle(DISCIPLINE discipline) {
		List<Integer> result = new ArrayList<Integer>();
		if( discipline == null ) return result;
		List<DISCIPLINECIRCLEType> circleList = discipline.getCIRCLE();
		if( circleList == null ) return result;
		for( DISCIPLINECIRCLEType circle : circleList ) {
			result.add(circle.getTALENTABILITY().getCount());
		}
		return result;
	}

	public List<Integer> getNumberOfOptionalTalentsPerCircleByDiscipline(String discipline) {
		return getNumberOfOptionalTalentsPerCircle(getDisziplin(discipline));
	}

	public DISCIPLINE getDisziplin(String name) {
		DISCIPLINE discipline = DISCIPLINES.get(name);
		if( discipline == null ) {
			System.err.println("Discipline '"+name+"' does not exist.");
		}
		return discipline;
	}

	public Set<String> getAllDisziplinNames() {
		return DISCIPLINES.keySet();
	}

	public Map<String,Map<String,?>> getAllDisziplinNamesAsTree() {
		Map<String,Map<String,?>> result = new HashMap<String, Map<String,?>>();
		for( String s : DISCIPLINES.keySet() ) result.put(s,new HashMap<String, Map<String,?>>());
		return shrinkStringMap(result);
	}

	public Map<String,Map<String,?>> getAllCharacterTemplatesNamesAsTree() {
		Map<String,Map<String,?>> result = new HashMap<String, Map<String,?>>();
		for( String s : RANDOMCHARACTERTEMPLATES.getAllTemplateNames() ) result.put(s,new HashMap<String, Map<String,?>>());
		return shrinkStringMap(result);
	}

	private Map<String,Map<String,?>> shrinkStringMap(Map<String,Map<String,?>> map) {
		Map<String,Map<String,?>> result = new HashMap<String, Map<String,?>>();
		for( String newkey : map.keySet() ) {
			boolean insert=false;
			Map<String, Map<String, ?>> newvalue = new HashMap<String, Map<String,?>>();
			List<String> removelist = new ArrayList<String>();
			for( String s : result.keySet() ) {
				if( newkey.startsWith(s) ) {
					//Der neue String fängt genauso an wie ein bereits eingefügter String,
					//damit kann der neue der Liste der Vorhandenen angefügt werden
					@SuppressWarnings("unchecked")
					Map<String,Map<String,?>> element = (Map<String,Map<String,?>>)(result.get(s));
					element.put(newkey, newvalue);
					insert=true;
					break;
				} else if( s.startsWith(newkey) ) {
					removelist.add(s);
					newvalue.put(s, new HashMap<String, Map<String,?>>());
				}
			}
			for( String s : removelist ) result.remove(s);
			if( ! insert ) result.put(newkey, newvalue);
		}
		for( String s : result.keySet() ) {
			@SuppressWarnings("unchecked")
			Map<String,Map<String,?>> element = (Map<String,Map<String,?>>)(result.get(s));
			if( ! element.isEmpty() ) result.put( s, shrinkStringMap(element) );
		}
		return result;
	}

	public Collection<DISCIPLINE> getAllDisziplines() {
		return DISCIPLINES.values();
	}

	public List<NAMEGIVERABILITYType> getNamegivers() {
		List<NAMEGIVERABILITYType> result = new ArrayList<NAMEGIVERABILITYType>();
		for( NAMEGIVERABILITYType namegiver : NAMEGIVERS.getNAMEGIVER()) {
			if( namegiver.getLang().equals(LANGUAGE) ) result.add(namegiver);
		}
		return result;
	}
	
	public ECECharacteristics getCharacteristics() {
		return CHARACTERISTICS;
	}

	public DiceType step2Dice(int value) {
		return getCharacteristics().getSTEPDICEbyStep(value).getDice();
	}

	public CAPABILITIES getCapabilities() {
		return CAPABILITIES;
	}

	public HashMap<String,TALENTABILITYType> getTalentsByCircle(int maxcirclenr) {
		return getTalentsByCircle(1,maxcirclenr);
	}

	public HashMap<String,TALENTABILITYType> getTalentsByCircle(int mincirclenr, int maxcirclenr) {
		HashMap<String,TALENTABILITYType> result = new HashMap<String,TALENTABILITYType>();
		for(DISCIPLINE discipline : getAllDisziplines()) {
			int circlenr=0;
			for( DISCIPLINECIRCLEType circle : discipline.getCIRCLE() ) {
				circlenr++;
				if( circlenr < mincirclenr) continue;
				if( circlenr > maxcirclenr) break;
				for( TALENTABILITYType talent : circle.getOPTIONALTALENT() ) {
					String name = talent.getName();
					String limitation = talent.getLimitation();
					if( limitation.isEmpty() ) result.put(name,talent);
					else result.put(name+" - "+limitation,talent);
				}
				for( TALENTABILITYType talent : circle.getDISCIPLINETALENT() ) {
					String name = talent.getName();
					String limitation = talent.getLimitation();
					if( limitation.isEmpty() ) result.put(name,talent);
					else result.put(name+" - "+limitation,talent);
				}
			}
		}
		return result;
	}

	public List<KNACKBASEType> getTalentKnacks() {
		return KNACKS.getTALENTKNACK();
	}

	// Liefert die Definition aller verfügbarer Zauber zurück.
	// Unabhängig von der Diszipin oder dem Fadenweben-Talent
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

	public HashMap<String,Integer> getDefaultOptionalTalents(int discipline) {
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		for( OPTIONALRULESDEFAULTOPTIONALTALENT talent : OPTIONALRULES.getDEFAULTOPTIONALTALENT() ) {
			if( (talent.getDiscipline() == discipline) && talent.getLang().equals(LANGUAGE) ) {
				result.put(talent.getTalent(), talent.getCircle());
			}
		}
		return result;
	}

	public HashMap<String,Integer> getMultiUseTalents() {
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		for( OPTIONALRULESMULTIUSETALENT talent : OPTIONALRULES.getMULTIUSETALENT() ) {
			if( talent.getLang().equals(LANGUAGE) ) {
				result.put(talent.getTalent(), talent.getCount());
			}
		}
		return result;
	}

	public List<CHARACTERLANGUAGEType> getDefaultLanguage() {
		List<CHARACTERLANGUAGEType> result = new ArrayList<CHARACTERLANGUAGEType>();
		for( OPTIONALRULESDEFAULTLANGUAGE language : OPTIONALRULES.getDEFAULTLANGUAGE() ) {
			if( language.getLang().equals(LANGUAGE) ) {
				CHARACTERLANGUAGEType l = new CHARACTERLANGUAGEType();
				l.setLanguage(language.getLanguage());
				l.setReadwrite(language.getReadwrite());
				l.setSpeak(language.getSpeak());
				result.add(language);
			}
		}
		return result;
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
		return "";
	}

	public String getVersatilityName() {
		for( NAMELANGType name : NAMES.getVERSATILITY() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getName();
		}
		// Not found
		return null;
	}

	public String getThreadWeavingName() {
		for( NAMELANGType name : NAMES.getTHREADWEAVING() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getName();
		}
		// Not found
		return "";
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

	public List<SKILLType> getStartingSkills() {
		for( NAMESTARTINGSKILLSType skills : NAMES.getSTARTINGSKILLS() ) {
			if( skills.getLang().equals(LANGUAGE) ) return skills.getSKILL();
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

	public String getQuestorTalentName() {
		for( NAMELANGType name : NAMES.getQUESTORTALENT() ) {
			if( name.getLang().equals(LANGUAGE) ) return name.getName();
		}
		// Not found
		return "";
	}

	public ITEMS getItems() {
		return ITEMS;
	}

	public List<RANDOMNAMERACEType> getRandomNamesByRaces() {
		return RANDOMNAMES.getRANDOMNAMERACE();
	}

	public CharacterContainer getRandomCharacter(String template) {
		if( template == null ) return null; 
		RANDOMCHARACTERTEMPLATES.setItems(ITEMS);
		RANDOMCHARACTERTEMPLATES.setSpells(SPELLS.getSPELL());
		RANDOMCHARACTERTEMPLATES.setCapabilities(new ECECapabilities(getCapabilities().getSKILLOrTALENT()));
		RANDOMCHARACTERTEMPLATES.setDisciplineDefinitions(DISCIPLINES);
		return RANDOMCHARACTERTEMPLATES.generateRandomCharacter(template);
	}

	private void init() {
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			Unmarshaller u = jc.createUnmarshaller();
			String filename="";

			// disziplinen laden
			// --- Bestimmen aller Dateien im Unterordner 'disciplines'
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

			// randomcharactertemplates laden
			// --- Bestimmen aller Dateien im Unterordner 'randomcharactertemplates'
			files = new File("./config/randomcharactertemplates").listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name != null && name.endsWith(".xml");
				}
			});
			// --- Einlesen der Dateien
			for(File configFile : files) {
				System.out.println("Lese Konfigurationsdatei: '" + configFile.getCanonicalPath() + "'");
				EDRANDOMCHARACTERTEMPLATE t = (EDRANDOMCHARACTERTEMPLATE) u.unmarshal(configFile);
				if( t.getLang().equals(LANGUAGE) ) RANDOMCHARACTERTEMPLATES.put(t.getName(), t);
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
			filename="./config/items.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			ITEMS = (ITEMS) u.unmarshal(new File(filename));
			filename="./config/names.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			NAMES = (NAMES) u.unmarshal(new File(filename));
			filename="./config/help.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			HELP = (HELP) u.unmarshal(new File(filename));
			filename="./config/randomnames.xml";
			System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
			RANDOMNAMES = (EDRANDOMNAME) u.unmarshal(new File(filename));

			filename="./config/spelldescriptions.xml";
			File spelldescriptionsfile = new File(filename);
			if( spelldescriptionsfile.canRead() ) {
				System.out.println("Lese Konfigurationsdatei: '" + filename + "'");
				SPELLDESCRIPTIONS = (SPELLDESCRIPTIONS) u.unmarshal(spelldescriptionsfile);
			} else {
				System.out.println("Überspringe Konfigurationsdatei: '" + filename + "'");
			}

		} catch (Throwable e) {
			// Fehler ist grundsätzlicher Natur ...
			throw new RuntimeException(e);
		}
	}
}
