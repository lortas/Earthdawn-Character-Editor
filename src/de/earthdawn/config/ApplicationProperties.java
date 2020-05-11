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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.CharacterContainer;
import de.earthdawn.UnitCalculator;
import de.earthdawn.data.*;
import de.earthdawn.ui2.EDMainWindow;

/**
 * Klasse mit Konfigurations-Parametern
 */
public class ApplicationProperties {

	// Ein- und Ausgabe der Allgemeinen Konfigurationseinstellungen.
	private static Map<ECERulesetLanguage,CAPABILITIES> CAPABILITIES;
	private static Map<RulesetversionType,TRANSLATIONS> TRANSLATIONS;
	private static Map<String,Map<ECERulesetLanguage,TranslationlabelType>> NAMES;
	private static Map<RulesetversionType,List<TRANSLATIONType>> NAMEGIVERTRANSLATIONS;
	private static Map<ECERulesetLanguage,KNACKS> KNACKS;
	private static Map<ECERulesetLanguage,SPELLS> SPELLS;
	private static Map<ECERulesetLanguage,List<NAMEGIVERABILITYType>> NAMEGIVERS;
	private OPTIONALRULES OPTIONALRULES = new OPTIONALRULES();
	private UnitCalculator unitcalculator = null;
	private static Map<ECERulesetLanguage,ITEMS> ITEMS;
	private static HELP HELP = new HELP();
	private static ECEGUILAYOUT ECEGUILAYOUT = new ECEGUILAYOUT();
	private static ECERulesetLanguage RULESETLANGUAGE = new ECERulesetLanguage(RulesetversionType.ED_3,LanguageType.EN);
	private static EDRANDOMNAME RANDOMNAMES = new EDRANDOMNAME();
	private static Map<ECERulesetLanguage,SPELLDESCRIPTIONS> SPELLDESCRIPTIONS;
	private static Map<RulesetversionType,ECECharacteristics> CHARACTERISTICS;
	private static final File CONFIGDIR = new File("./config");

	// Singleton-Instanz dieser Klasse.
	private static ApplicationProperties theProps = null;
	// Disziplinen (Name Label geordnet)
	private static final Map<ECERulesetLanguage,Map<String, DISCIPLINE>> DISCIPLINES = new TreeMap<ECERulesetLanguage,Map<String, DISCIPLINE>>();
	// Questoren (Name Label geordnet)
	private static final Map<ECERulesetLanguage,Map<String, QUESTOR>> QUESTORS = new TreeMap();
	// Pfade (Name Label geordnet)
	private static final Map<ECERulesetLanguage,Map<String, PATH>> PATHS = new TreeMap();
	// RandomCharacterTemplates (Name Label geordnet)
	private static final RandomCharacterTemplates RANDOMCHARACTERTEMPLATES = new RandomCharacterTemplates();

	private ApplicationProperties() {
		init();
	}

	public final static double getJavaVersion() {
		String javaversionstring = System.getProperty("java.specification.version");
		if( javaversionstring == null ) return 0;
		try {
			return Double.parseDouble(javaversionstring);
		} catch(NumberFormatException e) {
			System.err.println("can not parse '"+javaversionstring+"' as floating point number.");
		}
		return 0;
	}

	/**
	 * Zugriff auf Applikations-Konstanten.
	 *
	 * @return Instanz der Applikations-Konstanten.
	 */
	public static ApplicationProperties create() {
		if( theProps == null ) theProps = new ApplicationProperties();
		return theProps;
	}

	public static void setRulesetLanguage(ECERulesetLanguage rulesetlanguage) {
		RULESETLANGUAGE = rulesetlanguage;
	}
	public void setRulesetLanguage(RulesetversionType rulesetversion, LanguageType language) {
		setRulesetLanguage(new ECERulesetLanguage(rulesetversion,language));
	}
	public ECERulesetLanguage getRulesetLanguage() {
		return RULESETLANGUAGE;
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
		DISCIPLINE discipline = DISCIPLINES.get(RULESETLANGUAGE).get(name);
		if( discipline == null ) {
			System.err.println("Discipline '"+name+"' does not exist.");
		}
		return discipline;
	}

	public Set<String> getAllDisziplinNames() {
		return DISCIPLINES.get(RULESETLANGUAGE).keySet();
	}

	public Map<String,Map<String,?>> getAllDisziplinNamesAsTree() {
		Map<String,Map<String,?>> result = new TreeMap<String, Map<String,?>>();
		for( String s : getAllDisziplinNames() ) result.put(s,new TreeMap());
		return shrinkStringMap(result);
	}

	public Map<String,Map<String,?>> getAllCharacterTemplatesNamesAsTree() {
		Map<String,Map<String,?>> result = new TreeMap<String, Map<String,?>>();
		for( String s : RANDOMCHARACTERTEMPLATES.getAllTemplateNames() ) result.put(s,new TreeMap<String, Map<String,?>>());
		return shrinkStringMap(result);
	}

	private Map<String,Map<String,?>> shrinkStringMap(Map<String,Map<String,?>> map) {
		Map<String,Map<String,?>> result = new TreeMap<String, Map<String,?>>();
		for( String newkey : map.keySet() ) {
			boolean insert=false;
			Map<String, Map<String, ?>> newvalue = new TreeMap<String, Map<String,?>>();
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
					newvalue.put(s, new TreeMap<String, Map<String,?>>());
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
		return smallStringMap(result, 8);
	}

	private Map<String,Map<String,?>> smallStringMap(Map<String,Map<String,?>> map, int maxsize) {
		List<String> topLevelStrings = Arrays.asList(map.keySet().toArray(new String[0]));
		Collections.sort(topLevelStrings);
		int step=topLevelStrings.size()/maxsize;
		if( step<2 ) return map;
		Map<String,Map<String,?>> result = new TreeMap<String, Map<String,?>>();
		Iterator<String> iter = topLevelStrings.iterator();
		String s = null;
		if( iter.hasNext() ) s = iter.next();
		while( s != null ) {
			String title = s.substring(0, 3) + " - ";
			Map<String,Map<String,?>> entry = new TreeMap<String, Map<String,?>>();
			entry.put(s, map.get(s));
			int count = step;
			while( count > 1) {
				count--;
				if( iter.hasNext() ) s=iter.next();
				else {
					s=null;
					break;
				}
				entry.put(s, map.get(s));
			}
			if( s != null ) {
				String last = s.substring(0,3);
				title += last;
				while( true ) {
					if( ! iter.hasNext() ) {
						s=null;
						break;
					}
					s = iter.next();
					if( ! s.startsWith(last) ) break;
					entry.put( s, map.get(s) );
				}
			}
			result.put(title,entry);
		}
		return result;
	}

	public Collection<DISCIPLINE> getAllDisziplines() {
		return DISCIPLINES.get(RULESETLANGUAGE).values();
	}

	public Set<String> getAllPathNames() {
		return PATHS.get(RULESETLANGUAGE).keySet();
	}

	public Map<String,Map<String,?>> getAllPathNamesAsTree() {
		Map<String,Map<String,?>> result = new TreeMap();
		for( String s : getAllPathNames() ) result.put(s,new TreeMap());
		return shrinkStringMap(result);
	}

	public Collection<PATH> getAllPaths() {
		return PATHS.get(RULESETLANGUAGE).values();
	}

	public Set<String> getAllQuestorNames() {
		return QUESTORS.get(RULESETLANGUAGE).keySet();
	}

	public Map<String,Map<String,?>> getAllQuestorNamesAsTree() {
		Map<String,Map<String,?>> result = new TreeMap();
		for( String s : getAllQuestorNames() ) result.put(s,new TreeMap());
		return shrinkStringMap(result);
	}

	public Collection<QUESTOR> getAllQuestors() {
		return QUESTORS.get(RULESETLANGUAGE).values();
	}

	public List<NAMEGIVERABILITYType> getNamegivers() {
		List<NAMEGIVERABILITYType> result = NAMEGIVERS.get(RULESETLANGUAGE);
		if( result == null ) {
			throw new RuntimeException("There are no namegiver defined for rulesetversion '"+RULESETLANGUAGE.getRulesetversion()+"' and language '"+RULESETLANGUAGE.getLanguage()+"'.");
		}
		return result;
	}

	public NAMEGIVERABILITYType searchNamegiver(String namegiver, String origin) {
		NAMEGIVERABILITYType fallbackresult=null;
		for( NAMEGIVERABILITYType ng : getNamegivers()) {
			if( ng.getName().equals(namegiver) ) {
				if( ng.getORIGIN().contains(origin) ) return ng;
				if( fallbackresult == null ) fallbackresult=ng;
			}
		}
		if( fallbackresult == null ) {
			throw new RuntimeException("There is no namegiver definition for: namegiver='"+namegiver+"'.");
		}
		System.err.println("There is no namegiver definition for '"+namegiver+"' locatet in '"+origin+"'. Using '"+fallbackresult.getName()+"' locatet in '"+fallbackresult.getORIGIN()+"'.");
		return fallbackresult;
	}

	public Map<String,Map<String,List<NAMEGIVERABILITYType>>> getNamgiversByType() {
		Map<String,Map<String,List<NAMEGIVERABILITYType>>> result = new TreeMap<String,Map<String,List<NAMEGIVERABILITYType>>>();
		for( NAMEGIVERABILITYType namegiver : getNamegivers() ) {
			String type = namegiver.getType();
			List<String> originList = namegiver.getORIGIN();
			if( originList.isEmpty() ) originList.add("");
			for( String origin : originList ) {
				if( ! result.containsKey(origin) ) result.put(origin,new TreeMap<String,List<NAMEGIVERABILITYType>>());
				Map<String, List<NAMEGIVERABILITYType>> o = result.get(origin);
				if( ! o.containsKey(type) ) o.put(type,new ArrayList<NAMEGIVERABILITYType>());
				o.get(type).add(namegiver);
			}
		}
		return result;
	}

	public ECECharacteristics getCharacteristics() {
		ECECharacteristics characteristics = CHARACTERISTICS.get(RULESETLANGUAGE.getRulesetversion());
		if( characteristics == null ) {
			throw new RuntimeException("Did not find any characteristics for ruleset '"+RULESETLANGUAGE.getRulesetversion()+"'.");
		}
		return characteristics;
	}

	public String step2Dice(int step) {
		return getCharacteristics().getDice(step);
	}

	public ECECapabilities getCapabilities() {
		CAPABILITIES c = CAPABILITIES.get(RULESETLANGUAGE);
		if( c == null ) {
			c = new CAPABILITIES();
			CAPABILITIES.put(RULESETLANGUAGE,c);
		}
		return new ECECapabilities(c.getSKILLOrTALENTOrDEVOTION(),getKnacks());
	}

	public Map<String,TALENTABILITYType> getTalentsByCircle(int maxcirclenr) {
		return getTalentsByCircle(1,maxcirclenr);
	}

	public Map<String,TALENTABILITYType> getTalentsByCircle(int mincirclenr, int maxcirclenr) {
		Map<String,TALENTABILITYType> result = new TreeMap<String,TALENTABILITYType>();
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

	public List<KNACKDEFINITIONType> getKnacks() {
		KNACKS knacks = KNACKS.get(RULESETLANGUAGE);
		if( knacks == null ) {
			knacks = new KNACKS();
		}
		return knacks.getKNACK();
	}

	public List<KNACKDEFINITIONType> getKnacksByName(String name) {
		List<KNACKDEFINITIONType> knacks = new ArrayList<>();
		for( KNACKDEFINITIONType knack : getKnacks() ) {
			if( knack.getName().equals(name) ) knacks.add(knack);
		}
		return knacks;
	}

	public List<KNACKDEFINITIONType> getKnacks(CapabilitytypeType type, String name, String limitation) {
		List<KNACKDEFINITIONType> knacks = new ArrayList<>();
		for( KNACKDEFINITIONType knack : getKnacks() ) {
			for( KNACKBASECAPABILITYType base : knack.getBASE() ) {
				if( ! base.getType().equals(type) ) continue;
				if( ! base.getName().equals(name) ) continue;
				if( limitation.isEmpty() || base.getLimitation().isEmpty() || base.getLimitation().equals(limitation) ) {
					knacks.add(knack);
				}
			}
		}
		return knacks;
	}

	public List<KNACKDEFINITIONType> getTalentKnacks(String talent,String limitation) {
		return getKnacks(CapabilitytypeType.TALENT,talent,limitation);
	}

	// Liefert die Definition aller verfügbarer Zauber zurück.
	// Unabhängig von der Diszipin oder dem Fadenweben-Talent
	public Map<String,SPELLDEFType> getSpells() {
		Map<String,SPELLDEFType> spellmap = new TreeMap<String,SPELLDEFType>();
		de.earthdawn.data.SPELLS spells = SPELLS.get(RULESETLANGUAGE);
		if( spells == null ) {
			System.err.println("No Spells defined found in ruleset "+RULESETLANGUAGE.toString());
		} else for( SPELLDEFType spell : spells.getSPELL() ) {
			spellmap.put(spell.getName(), spell);
		}
		return spellmap;
	}

	public Map<String,SpelldescriptionType> getSpellDescriptions() {
		Map<String,SpelldescriptionType> spellmap = new TreeMap<String,SpelldescriptionType>();
		for( SpelldescriptionType spell : SPELLDESCRIPTIONS.get(RULESETLANGUAGE).getSPELL() ) spellmap.put(spell.getName(), spell);
		return spellmap;
	}

	public Map<String,List<List<DISCIPLINESPELLType>>> getSpellsByDiscipline() {
		Map<String,List<List<DISCIPLINESPELLType>>> result = new TreeMap<String,List<List<DISCIPLINESPELLType>>>();
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
		Map<String, List<List<DISCIPLINESPELLType>>> spellsByDiscipline = getSpellsByDiscipline();
		Map<String, SPELLDEFType> spells = getSpells();
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

	public Map<String,Integer> getDefaultOptionalTalents(int discipline) {
		Map<String,Integer> result = new TreeMap<String,Integer>();
		for( OPTIONALRULESDEFAULTOPTIONALTALENT talent : OPTIONALRULES.getDEFAULTOPTIONALTALENT() ) {
			if( (talent.getDiscipline() == discipline) && talent.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
				result.put(talent.getTalent(), talent.getCircle());
			}
		}
		return result;
	}

	/*
	 * Liefert eine Map von Talenten die mehr als einmal gelernt werden können.
	 * Der Key enthält dabei den Talentnamen und das Value den Zähler, wie häufig es gelernt werden darf.
	 */
	public Map<String,Integer> getMultiUseTalents() {
		Map<String,Integer> result = new TreeMap<String,Integer>();
		for( OPTIONALRULESMULTIUSETALENT talent : OPTIONALRULES.getMULTIUSETALENT() ) {
			if( talent.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
				result.put(talent.getTalent(), talent.getCount());
			}
		}
		return result;
	}

	public List<CHARACTERLANGUAGEType> getDefaultLanguage(String origin) {
		List<CHARACTERLANGUAGEType> result = new ArrayList<CHARACTERLANGUAGEType>();
		for( OPTIONALRULESORIGIN o : OPTIONALRULES.getORIGIN() ) {
			if( o.getName().equals(origin) ) for( OPTIONALRULESDEFAULTLANGUAGE language : o.getDEFAULTLANGUAGE() ) {
				if( language.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
					CHARACTERLANGUAGEType l = new CHARACTERLANGUAGEType();
					l.setLanguage(language.getLanguage());
					l.setReadwrite(language.getReadwrite());
					l.setSpeak(language.getSpeak());
					result.add(language);
				}
			}
		}
		return result;
	}

	public void saveOptionalRules() throws JAXBException, IOException {
		if( OPTIONALRULES == null ) return;
		final String file=(new File(CONFIGDIR,"optionalrules.xml")).getCanonicalPath();
		System.out.println("Scheibe Konfigurationsdatei: " + file);
		JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
		Marshaller m = jc.createMarshaller();
		FileOutputStream out = new FileOutputStream(file);
		PrintStream fileio = new PrintStream(out, false, EDMainWindow.encoding);
		m.setProperty(Marshaller.JAXB_ENCODING, EDMainWindow.encoding);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/optionalrules earthdawnoptionalrules.xsd");
		m.setProperty(Marshaller.JAXB_FRAGMENT, true);
		fileio.print("<?xml version=\"1.0\" encoding=\""+EDMainWindow.encoding+"\" standalone=\"no\"?>");
		m.marshal(OPTIONALRULES,fileio);
		fileio.close();
		out.close();
	}

	public Map<ATTRIBUTENameType,String> getAttributeNames() {
		Map<ATTRIBUTENameType,String> result = new TreeMap<ATTRIBUTENameType,String>();
		// Fill result Map with default values
		for( ATTRIBUTENameType attr : ATTRIBUTENameType.values() ) result.put(attr,attr.value());
		// Search for translation in the config
		for( NAMESATTRIBUTESType attributes : TRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()).getATTRIBUTES() ) {
			if( attributes.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
				for( NAMESATTRIBUTEType attribute : attributes.getATTRIBUTE() ) {
					result.put(attribute.getAttribute(),attribute.getName());
				}
			}
		}
		return result;
	}

	public String getKarmaritualName() {
		TranslationlabelType translationlabel = NAMES.get("karmaritual").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String getDurabilityName() {
		TranslationlabelType translationlabel = NAMES.get("durability").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String getVersatilityName() {
		TranslationlabelType translationlabel = NAMES.get("versatility").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String getThreadWeavingName() {
		TranslationlabelType translationlabel = NAMES.get("threadweaving").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String getLanguageSkillSpeakName() {
		TranslationlabelType translationlabel = NAMES.get("languageskillspeak").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String getLanguageSkillReadWriteName() {
		TranslationlabelType translationlabel = NAMES.get("languageskillreadwrite").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String getQuestorTalentName() {
		TranslationlabelType translationlabel = NAMES.get("questortalent").get(RULESETLANGUAGE);
		if( translationlabel== null ) return "";
		return translationlabel.getValue();
	}

	public String[] getArtisanName() {
		TranslationlabelType name = NAMES.get("artisan").get(RULESETLANGUAGE);
		if( name == null ) return new String[]{"",""};
		return new String[]{name.getValue(),name.getAcronym()};
	}

	public String[] getKnowledgeName() {
		TranslationlabelType name = NAMES.get("knowledge").get(RULESETLANGUAGE);
		if( name == null ) return new String[]{"",""};
		return new String[]{name.getValue(),name.getAcronym()};
	}

	public Map<String,String> getTranslationHealthAll() {
		Map<String,String> result = new TreeMap<String,String>();
		for( GENERALTEXTType health : TRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()).getHEALTH() ) {
			for( TranslationlabelType label : health.getLABEL() ) {
				if( label.getLang() == RULESETLANGUAGE.getLanguage() ) {
					result.put(health.getName(), label.getValue());
				}
			}
		}
		return result;
	}

	public Map<SpellkindType,String[]> getTranslationSpellkindAll() {
		Map<SpellkindType,String[]> result = new TreeMap<SpellkindType,String[]>();
		for( NAMESPELLWEAVINGType spellweaving : TRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()).getSPELLWEAVING() ) {
			if( spellweaving.getLang() != RULESETLANGUAGE.getLanguage() ) continue;
			for( NAMESPELLKINDType spell : spellweaving.getSPELLKIND() ) {
				result.put(spell.getType(),new String[]{spell.getAcronym(),spell.getWeaving()});
			}
		}
		return result;
	}

	public Map<String,String> getTranslationTextAll() {
		Map<String,String> result = new TreeMap<String,String>();
		for( GENERALTEXTType translation : TRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()).getTEXT() ) {
				for( TranslationlabelType label : translation.getLABEL() ) {
					if( label.getLang() == RULESETLANGUAGE.getLanguage() ) {
						result.put(translation.getName(), label.getValue());
					}
				}
		}
		return result;
	}

	public String getTranslationText(String name) {
		String result=getTranslationTextAll().get(name);
		if( result == null ) {
			return "##"+RULESETLANGUAGE.toString()+":"+name+"##";
		}
		return result;
	}

	public List<ITEMType> getStartingItems() {
		for( NAMESTARTINGITEMSType name : OPTIONALRULES.getSTARTINGITEMS() ) {
			if( name.getLang().equals(RULESETLANGUAGE.getLanguage()) ) return name.getITEM();
		}
		return null;
	}

	public List<WEAPONType> getStartingWeapons() {
		for( NAMESTARTINGWEAPONSType name : OPTIONALRULES.getSTARTINGWEAPONS() ) {
			if( name.getLang().equals(RULESETLANGUAGE.getLanguage()) ) return name.getWEAPON();
		}
		return null;
	}

	public List<SKILLType> getStartingSkills() {
		for( NAMESTARTINGSKILLSType skills : OPTIONALRULES.getSTARTINGSKILLS() ) {
			if( skills.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
				List<SKILLType> skill = skills.getSKILL();
				for( SKILLType s : skill ) {
					while( s.getLIMITATION().remove("") );
				}
				return skill;
			}
		}
		return new ArrayList<SKILLType>();
	}

	public Map<SpellkindType,String> getSpellKindMap() {
		for( NAMESPELLWEAVINGType name : TRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()).getSPELLWEAVING() ) {
			if( name.getLang().equals(RULESETLANGUAGE.getLanguage())) {
				Map<SpellkindType,String> spellTypeMap = new TreeMap<SpellkindType,String>();
				for( NAMESPELLKINDType type : name.getSPELLKIND() ) {
					spellTypeMap.put(type.getType(),type.getWeaving());
				}
				return spellTypeMap;
			}
		}
		System.err.println("Could not find a SpellTypeMap for language='"+RULESETLANGUAGE.getLanguage().value()+"' and rulesetversion='"+RULESETLANGUAGE.getRulesetversion().value()+"'");
		return new TreeMap<SpellkindType,String>();
	}

	public HELP getHelp() {
		return HELP;
	}

	public ITEMS getItems() {
		return ITEMS.get(RULESETLANGUAGE);
	}

	public UnitCalculator getUnitCalculator() {
		if( this.unitcalculator == null ) {
			this.unitcalculator=new UnitCalculator(getOptionalRules().getUNITS(),2);
		}
		return this.unitcalculator;
	}

	public void clearUnitCalculator() {
		this.unitcalculator = null;
	}

	public List<RANDOMNAMERACEType> getRandomNamesByRaces() {
		return RANDOMNAMES.getRANDOMNAMERACE();
	}

	public CharacterContainer getRandomCharacter(String template) {
		if( template == null ) return null;
		RANDOMCHARACTERTEMPLATES.setItems(getItems());
		RANDOMCHARACTERTEMPLATES.setSpells(SPELLS.get(RULESETLANGUAGE).getSPELL());
		RANDOMCHARACTERTEMPLATES.setCapabilities(getCapabilities());
		RANDOMCHARACTERTEMPLATES.setDisciplineDefinitions(DISCIPLINES.get(RULESETLANGUAGE));
		return RANDOMCHARACTERTEMPLATES.generateRandomCharacter(template);
	}

	public LAYOUTDIMENSIONType getGuiLayoutMainWindow() {
		return ECEGUILAYOUT.getMAINWINDOW();
	}

	public LAYOUTDIMENSIONType getGuiLayoutStatusWindow() {
		return ECEGUILAYOUT.getSTATUSWINDOW();
	}

	public LAYOUTDIMENSIONType getGuiLayoutTabWindow() {
		return ECEGUILAYOUT.getTABWINDOW();
	}

	public List<LAYOUTSIZESType> getGuiLayoutTabel(String name) {
		for( LAYOUTTABLEType table : ECEGUILAYOUT.getTABLE() ) {
			if( table.getName().equals(name) ) return table.getCOLUMN();
		}
		return new ArrayList<LAYOUTSIZESType>();
	}

	private void init() {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = JAXBContext.newInstance("de.earthdawn.data").createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
			unmarshaller=null;
			return;
		}
		try {
			// translation laden
			// --- Bestimmen aller Dateien im Unterordner 'translation'
			// --- Einlesen der Dateien
			TRANSLATIONS=new TreeMap<RulesetversionType,TRANSLATIONS>();
			for(Path translation : selectallxmlfiles(new File(CONFIGDIR,"translation").toPath())) {
				System.out.println("Reading config file: " + translation.toString());
				TRANSLATIONS t1 = (TRANSLATIONS) unmarshaller.unmarshal(translation.toFile());
				if( t1 == null ) { System.out.println(" parse error."); continue; }
				TRANSLATIONS t2 = TRANSLATIONS.get(t1.getRulesetversion());
				if( t2 == null ) {
					TRANSLATIONS.put(t1.getRulesetversion(), t1);
				} else {
					t2.getACTIONS().addAll(t1.getACTIONS());
					t2.getARTISAN().addAll(t1.getARTISAN());
					t2.getATTRIBUTES().addAll(t1.getATTRIBUTES());
					t2.getCAPABILITY().addAll(t1.getCAPABILITY());
					t2.getDISCIPLINE().addAll(t1.getDISCIPLINE());
					t2.getDURABILITY().addAll(t1.getDISCIPLINE());
					t2.getITEM().addAll(t1.getITEM());
					t2.getKARMARUTUAL().addAll(t1.getKARMARUTUAL());
					t2.getKNOWLEDGE().addAll(t1.getKNOWLEDGE());
					t2.getLANGUAGESKILLREADWRITE().addAll(t1.getLANGUAGESKILLREADWRITE());
					t2.getLANGUAGESKILLSPEAK().addAll(t1.getLANGUAGESKILLSPEAK());
					t2.getQUESTORTALENT().addAll(t1.getQUESTORTALENT());
					t2.getSPELL().addAll(t1.getSPELL());
					t2.getSPELLWEAVING().addAll(t1.getSPELLWEAVING());
					t2.getTHREADWEAVING().addAll(t1.getTHREADWEAVING());
					t2.getVERSATILITY().addAll(t1.getVERSATILITY());
					t2.getTEXT().addAll(t1.getTEXT());
					t2.getHEALTH().addAll(t1.getHEALTH());
					t2.getAPPEARANCE().addAll(t1.getAPPEARANCE());
				}
			}

			// Zum späteren leichteren Zugriff, bringe die spezifischen Übersetzungen in ein anderes Format.
			NAMEGIVERTRANSLATIONS=new TreeMap<RulesetversionType,List<TRANSLATIONType>>();
			NAMES=new TreeMap<String,Map<ECERulesetLanguage,TranslationlabelType>>();
			NAMES.put("karmaritual",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("durability",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("versatility",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("threadweaving",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("languageskillspeak",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("languageskillreadwrite",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("questortalent",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("artisan",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("knowledge",new TreeMap<ECERulesetLanguage,TranslationlabelType>());
			for( RulesetversionType rulesetversion : TRANSLATIONS.keySet() ) {
				Map<ECERulesetLanguage, TranslationlabelType> n;
				n = NAMES.get("karmaritual");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getKARMARUTUAL() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("durability");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getDURABILITY() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("versatility");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getVERSATILITY() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("threadweaving");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getTHREADWEAVING() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("languageskillspeak");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getLANGUAGESKILLSPEAK() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("languageskillreadwrite");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getLANGUAGESKILLREADWRITE() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("questortalent");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getQUESTORTALENT() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("artisan");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getARTISAN() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				n = NAMES.get("knowledge");
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getKNOWLEDGE() ) {
					for( TranslationlabelType label : translation.getLABEL() ) {
						n.put(new ECERulesetLanguage(rulesetversion,label.getLang()),label);
					}
				}
				List<TRANSLATIONType> namegiver = NAMEGIVERTRANSLATIONS.get(rulesetversion);
				if( namegiver == null ) {
					namegiver = new ArrayList<TRANSLATIONType>();
					NAMEGIVERTRANSLATIONS.put(rulesetversion,namegiver);
				}
				for( TRANSLATIONType translation : TRANSLATIONS.get(rulesetversion).getNAMEGIVER() ) {
					namegiver.add(translation);
				}
			}

			// disziplinen laden
			// --- Bestimmen aller Dateien im Unterordner 'disciplines'
			// --- Einlesen der Dateien
			for(Path disConfigFile : selectallxmlfiles(new File(CONFIGDIR,"disciplines").toPath())) {
				System.out.println("Reading config file: " + disConfigFile.toString());
				DISCIPLINE dis = (DISCIPLINE) unmarshaller.unmarshal(disConfigFile.toFile());
				ECERulesetLanguage rl = new ECERulesetLanguage(dis.getRulesetversion(),dis.getLang());
				Map<String, DISCIPLINE> dis2 = DISCIPLINES.get(rl);
				if( dis2 == null ) {
					dis2 = new TreeMap<String, DISCIPLINE>();
					DISCIPLINES.put(rl,dis2);
				}
				dis2.put(dis.getName(), dis);
			}

			// querstors laden
			// --- Bestimmen aller Dateien im Unterordner 'questors'
			// --- Einlesen der Dateien
			for(Path disConfigFile : selectallxmlfiles(new File(CONFIGDIR,"questors").toPath())) {
				System.out.println("Reading config file: " + disConfigFile.toString());
				QUESTOR questor = (QUESTOR) unmarshaller.unmarshal(disConfigFile.toFile());
				ECERulesetLanguage rl = new ECERulesetLanguage(questor.getRulesetversion(),questor.getLang());
				Map<String, QUESTOR> path2 = QUESTORS.get(rl);
				if( path2 == null ) {
					path2 = new TreeMap();
					QUESTORS.put(rl,path2);
				}
				path2.put(questor.getPassion(), questor);
			}

			// pathes laden
			// --- Bestimmen aller Dateien im Unterordner 'paths'
			// --- Einlesen der Dateien
			for(Path disConfigFile : selectallxmlfiles(new File(CONFIGDIR,"paths").toPath())) {
				System.out.println("Reading config file: " + disConfigFile.toString());
				PATH path = (PATH) unmarshaller.unmarshal(disConfigFile.toFile());
				ECERulesetLanguage rl = new ECERulesetLanguage(path.getRulesetversion(),path.getLang());
				Map<String, PATH> path2 = PATHS.get(rl);
				if( path2 == null ) {
					path2 = new TreeMap();
					PATHS.put(rl,path2);
				}
				path2.put(path.getName(), path);
			}

			// capabilities laden
			// --- Bestimmen aller Dateien im Unterordner 'capabilities'
			// --- Einlesen der Dateien
			CAPABILITIES=new TreeMap<>();
			for(Path capa : selectallxmlfiles(new File(CONFIGDIR,"capabilities").toPath())) {
				System.out.println("Reading config file: " + capa.toString());
				CAPABILITIES c1 = (CAPABILITIES) unmarshaller.unmarshal(capa.toFile());
				if( c1 == null ) { System.out.println(" parse error."); continue; }
				ECERulesetLanguage rsl=new ECERulesetLanguage(c1.getRulesetversion(),c1.getLang());
				CAPABILITIES c2 = CAPABILITIES.get(rsl);
				if( c2 == null ) {
					CAPABILITIES.put(rsl,c1);
				} else {
					c2.getSKILLOrTALENTOrDEVOTION().addAll(c1.getSKILLOrTALENTOrDEVOTION());
				}
			}

			// spells laden
			// --- Bestimmen aller Dateien im Unterordner 'spells'
			// --- Einlesen der Dateien
			SPELLS=new TreeMap<ECERulesetLanguage,SPELLS>();
			SPELLDESCRIPTIONS=new TreeMap<ECERulesetLanguage,SPELLDESCRIPTIONS>();
			File spelldescriptionsdir = new File(CONFIGDIR, "spelldescriptions");
			for(Path spells : selectallxmlfiles(new File(CONFIGDIR,"spells").toPath())) {
				System.out.println("Reading config file: " + spells.toString() + "' ...");
				SPELLS s = (SPELLS) unmarshaller.unmarshal(spells.toFile());
				if( s == null ) { System.out.println(" parse error."); continue; }
				ECERulesetLanguage rsl=new ECERulesetLanguage(s.getRulesetversion(),s.getLang());
				SPELLS s2 = SPELLS.get(rsl);
				if( s2 == null ) {
					s2=new SPELLS();
					s2.setLang(rsl.getLanguage());
					s2.setRulesetversion(rsl.getRulesetversion());
					SPELLS.put(rsl, s2);
				}
				s2.getSPELL().addAll(s.getSPELL());
				Stack<String> filepath = new Stack<String>();
				File file = spells.toFile();
				while(!file.equals(CONFIGDIR)) {
					filepath.push(file.getName());
					file=file.getParentFile();
				}
				filepath.pop(); // "./config/spells/"
				File spelldescr=spelldescriptionsdir;
				while(filepath.size()>1) {
					spelldescr=new File(spelldescr,filepath.pop());
				}
				spelldescr.mkdirs();
				File spelldescriptionsfile=new File(spelldescr,filepath.pop());
				boolean neworchanged=false;
				SPELLDESCRIPTIONS spelldescriptions;
				if( spelldescriptionsfile.canRead() ) {
					System.out.println("Reading config file: " + spelldescriptionsfile.toString());
					spelldescriptions = (SPELLDESCRIPTIONS) unmarshaller.unmarshal(spelldescriptionsfile);
					ECERulesetLanguage rsl_sd=new ECERulesetLanguage(spelldescriptions.getRulesetversion(),spelldescriptions.getLang());
					if( !rsl.equals(rsl_sd) ) {
						System.err.println("config file has diffrent rulesetversion or language ("+rsl+"!="+rsl_sd+")");
					}
					if( fillSpellDescription(spelldescriptions,s) ) {
						// Nicht zu allen Spells eine SpellDescriptions gefunden.
						neworchanged=true;
					}
					SPELLDESCRIPTIONS sd = SPELLDESCRIPTIONS.get(rsl);
					if( sd == null ) {
						sd=new SPELLDESCRIPTIONS();
						sd.setLang(rsl.getLanguage());
						sd.setRulesetversion(rsl.getRulesetversion());
						SPELLS.put(rsl, s2);
					}
					sd.getSPELL().addAll(spelldescriptions.getSPELL());
				} else {
					System.out.println("Überspringe Konfigurationsdatei: '" + spelldescriptionsfile.getCanonicalPath() + "' da nicht vorhanden oder nicht lesbar.");
					spelldescriptions = new SPELLDESCRIPTIONS();
					spelldescriptions.setLang(rsl.getLanguage());
					spelldescriptions.setRulesetversion(rsl.getRulesetversion());
					fillSpellDescription(spelldescriptions,s);
					neworchanged=true;
				}
				if( neworchanged ) {
					System.out.println("Scheibe Konfigurationsdatei: " + spelldescriptionsfile.toString());
					JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
					Marshaller m = jc.createMarshaller();
					FileOutputStream out = new FileOutputStream(spelldescriptionsfile);
					PrintStream fileio = new PrintStream(out, false, EDMainWindow.encoding);
					m.setProperty(Marshaller.JAXB_ENCODING, EDMainWindow.encoding);
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/spelldescription spelldescription.xsd");
					m.setProperty(Marshaller.JAXB_FRAGMENT, true);
					fileio.print("<?xml version=\"1.0\" encoding=\""+EDMainWindow.encoding+"\" standalone=\"no\"?>");
					m.marshal(spelldescriptions,fileio);
					fileio.close();
					out.close();
				}
			}

			// knacks laden
			// --- Bestimmen aller Dateien im Unterordner 'knacks'
			// --- Einlesen der Dateien
			KNACKS=new TreeMap<ECERulesetLanguage,KNACKS>();
			for(Path knacks : selectallxmlfiles(new File(CONFIGDIR,"knacks").toPath())) {
				System.out.println("Reading config file: " + knacks.toString());
				KNACKS k = (KNACKS) unmarshaller.unmarshal(knacks.toFile());
				if( k == null ) { System.out.println(" parse error."); continue; }
				ECERulesetLanguage rsl=new ECERulesetLanguage(k.getRulesetversion(),k.getLang());
				KNACKS knack = KNACKS.get(rsl);
				if( knack == null ) {
					knack = new KNACKS();
					KNACKS.put(rsl, knack);
				}
				knack.getKNACK().addAll(k.getKNACK());
			}

			// itemstore laden
			// --- Bestimmen aller Dateien im Unterordner 'disciplines'
			// --- Einlesen der Dateien
			ITEMS=new TreeMap<ECERulesetLanguage,ITEMS>();
			for(Path items : selectallxmlfiles(new File(CONFIGDIR,"itemstore").toPath())) {
				System.out.println("Reading config file: " + items.toString());
				ITEMS i = (ITEMS) unmarshaller.unmarshal(items.toFile());
				if( i == null ) { System.out.println(" parse error."); continue; }
				RulesetversionType[] rulesets = i.getRULESETCONSTRAINT().toArray(new RulesetversionType[0]);
				if( rulesets.length == 0 ) { rulesets = RulesetversionType.values(); }
				for( RulesetversionType ruleset : rulesets ) {
					ECERulesetLanguage rulesetlang = new ECERulesetLanguage(ruleset,i.getLang());
					ITEMS j=ITEMS.get(rulesetlang);
					if( j == null ) {
						j=new ITEMS();
						ITEMS.put(rulesetlang,j);
					}
					for( ARMORType k : i.getARMOR() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getARMOR().add(k);
					}
					for( MAGICITEMType k : i.getBLOODCHARMITEM() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getBLOODCHARMITEM().add(k);
					}
					for( ITEMType k : i.getITEM() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getITEM().add(k);
					}
					for( MAGICITEMType k : i.getMAGICITEM() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getMAGICITEM().add(k);
					}
					for( PATTERNITEMType k : i.getPATTERNITEM() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getPATTERNITEM().add(k);
					}
					for( SHIELDType k : i.getSHIELD() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getSHIELD().add(k);
					}
					for( THREADITEMType k : i.getTHREADITEM() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getTHREADITEM().add(k);
					}
					for( WEAPONType k : i.getWEAPON() ) {
						k.setVirtual(YesnoType.NO);
						k.setUsed(YesnoType.NO);
						k.setLocation("");
						j.getWEAPON().add(k);
					}
				}
			}

			// randomcharactertemplates laden
			// --- Bestimmen aller Dateien im Unterordner 'randomcharactertemplates'
			// --- Einlesen der Dateien
			for(Path configFile : selectallxmlfiles(new File(CONFIGDIR,"randomcharactertemplates").toPath())) {
				System.out.println("Reading config file: " + configFile.toString());
				EDRANDOMCHARACTERTEMPLATE t = (EDRANDOMCHARACTERTEMPLATE) unmarshaller.unmarshal(configFile.toFile());
				if( t.getLang().equals(RULESETLANGUAGE.getLanguage()) ) RANDOMCHARACTERTEMPLATES.put(t.getName(), t);
			}

			NAMEGIVERS=new TreeMap<ECERulesetLanguage,List<NAMEGIVERABILITYType>>();
			for(Path configFile : selectallxmlfiles(new File(CONFIGDIR,"namegivers").toPath())) {
				System.out.println("Reading config file: " + configFile.toString());
				NAMEGIVERS t = (NAMEGIVERS) unmarshaller.unmarshal(configFile.toFile());
				RulesetversionType rulesetversion = t.getRulesetversion();
				for( NAMEGIVERABILITYType n : t.getNAMEGIVER() ) {
					ECERulesetLanguage key = new ECERulesetLanguage(rulesetversion,n.getLang());
					List<NAMEGIVERABILITYType> ng = NAMEGIVERS.get(key);
					if( ng == null ) {
						ng=new ArrayList<NAMEGIVERABILITYType>();
						NAMEGIVERS.put(key, ng);
					}
					ng.add(n);
				}
			}

			CHARACTERISTICS=new TreeMap<RulesetversionType,ECECharacteristics>();
			for(Path configFile : selectallxmlfiles(new File(CONFIGDIR,"characteristics").toPath())) {
				System.out.println("Reading config file: " + configFile.toString());
				CHARACTERISTICS t = (CHARACTERISTICS) unmarshaller.unmarshal(configFile.toFile());
				CHARACTERISTICS.put(t.getRulesetversion(), new ECECharacteristics(t));
			}

			File filename=new File(CONFIGDIR,"optionalrules.xml");
			System.out.println("Reading config file: " + filename.toString());
			OPTIONALRULES = (OPTIONALRULES) unmarshaller.unmarshal(filename);
			filename=new File(CONFIGDIR,"help.xml");
			System.out.println("Reading config file: " + filename.toString());
			HELP = (HELP) unmarshaller.unmarshal(filename);
			filename=new File(CONFIGDIR,"randomnames.xml");
			System.out.println("Reading config file: " + filename.toString());
			RANDOMNAMES = (EDRANDOMNAME) unmarshaller.unmarshal(filename);
			filename=new File(CONFIGDIR,"eceguilayout.xml");
			System.out.println("Reading config file: " + filename.toString());
			ECEGUILAYOUT = (ECEGUILAYOUT) unmarshaller.unmarshal(filename);
		} catch (JAXBException|IOException e) {
			e.printStackTrace();
		}
		//checktranslation();
	}

	public static boolean fillSpellDescription(SPELLDESCRIPTIONS spelldescriptions, SPELLS spelllist) {
		boolean modified=false;
		List<SpelldescriptionType> spelldesciptionspells = spelldescriptions.getSPELL();
		for( SPELLDEFType spell: spelllist.getSPELL() ) {
			String spellname = spell.getName();
			boolean notfound = true;
			for( SpelldescriptionType sd : spelldesciptionspells ) if( sd.getName().equals(spellname) ) notfound=false;
			if( notfound ) {
				modified=true;
				SpelldescriptionType sd = new SpelldescriptionType();
				sd.setName(spellname);
				sd.setValue("take text from "+spell.getBookref());
				spelldesciptionspells.add(sd);
			}
		}
		return modified;
	}

	public static List<Path> selectallxmlfiles(Path folderpath) {
		List<Path> files = new LinkedList<Path>();
		try {
			List<Path> folders = new LinkedList<Path>();
			for( Path path : Files.newDirectoryStream(folderpath) ) {
				if( Files.isDirectory(path) ) {
					folders.add(path);
				} else if( Files.isRegularFile(path)) {
					String name=path.toString();
					if( (name!=null) && name.endsWith(".xml") ) files.add(path);
				}
			}
			for( Path dir : folders ) files.addAll(selectallxmlfiles(dir));
		} catch( IOException e) {
			System.err.println("Can not get xml-files from '"+folderpath.toString()+"' : "+e.getLocalizedMessage());
		}
		return files;
	}

	public String translateNamegiver(String namegiver, LanguageType language) {
		for( TRANSLATIONType ng : NAMEGIVERTRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()) ) {
			boolean found=false;
			String destination=null;
			for( TranslationlabelType label : ng.getLABEL() ) {
				if( label.getLang().equals(language) && label.getValue().equals(namegiver)) {
					found=true;
				}
				if( label.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
					destination = label.getValue();
				}
			}
			if( found && destination!=null ) return destination;
		}
		return "";
	}

	private void checktranslation() {
		for( Map.Entry<ECERulesetLanguage, CAPABILITIES> c : CAPABILITIES.entrySet() ) {
			ECERulesetLanguage rl = c.getKey();
			Map<String,List<TranslationlabelType>> translation = new TreeMap<String,List<TranslationlabelType>>();
			Map<String,Integer> count = new TreeMap<String,Integer>();
					for( TRANSLATIONType i : TRANSLATIONS.get(rl.getRulesetversion()).getCAPABILITY() ) {
						for( TranslationlabelType j : i.getLABEL() ) {
							if( j.getLang().equals(rl.getLanguage()) ) {
								translation.put(j.getValue(), i.getLABEL());
								count.put(j.getValue(), 0);
							}
						}
			}
			for( JAXBElement<?> t : c.getValue().getSKILLOrTALENTOrDEVOTION() ) {
				String name;
				if( t.getName().getLocalPart().equals("DEVOTION") ) {
					name = ((DEVOTIONCAPABILITYType)(t.getValue())).getName();
				} else {
					name = ((CAPABILITYType)(t.getValue())).getName();
				}
				List<TranslationlabelType> labels = translation.get(name);
				if( labels == null ) {
					count.put(name, -1);
				} else {
					count.put(name,count.get(name)+1);
				}
			}
			List <String> keys = new ArrayList<String>(Arrays.asList(count.keySet().toArray(new String[0])));
			Collections.sort(keys);
			System.out.println("The following translation were used:");
			for( String key : keys ) {
				if( count.get(key) > 0 ) {
					System.out.println("	<CAPABILITY>");
					for( TranslationlabelType ll : translation.get(key) ) {
						System.out.println("		<LABEL lang=\""+ll.getLang().value()+"\">"+ll.getValue()+"</LABEL>");
					}
					System.out.println("	</CAPABILITY>");
				}
			}
			System.out.println("The following translation were not used:");
			for( String key : keys ) {
				if( count.get(key) == 0 ) {
					System.out.println("	<CAPABILITY>");
					for( TranslationlabelType ll : translation.get(key) ) {
						System.out.println("		<LABEL lang=\""+ll.getLang().value()+"\">"+ll.getValue()+"</LABEL>");
					}
					System.out.println("	</CAPABILITY>");
				}
			}
			System.out.println("The following translation were missing:");
			for( String key : keys ) {
				if( count.get(key) < 0 ) {
					System.out.println("	<CAPABILITY>");
					System.out.println("		<LABEL lang=\""+rl.getLanguage().value()+"\">"+key+"</LABEL>");
					System.out.println("	</CAPABILITY>");
				}
			}
		}
	}
}
