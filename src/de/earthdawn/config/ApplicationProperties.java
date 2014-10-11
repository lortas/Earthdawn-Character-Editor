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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.*;
import de.earthdawn.ui2.EDMainWindow;

/** 
 * Klasse mit Konfigurations-Parametern 
 */
public class ApplicationProperties {

    /** Ein- und Ausgabe der Allgemeinen Konfigurationseinstellungen. */
	private static List<CAPABILITIES> CAPABILITIES;
	private static Map<RulesetversionType,TRANSLATIONS> TRANSLATIONS;
	private static Map<String,Map<ECERulesetLanguage,TranslationlabelType>> NAMES;
	private static Map<RulesetversionType,List<TRANSLATIONType>> NAMEGIVERTRANSLATIONS;
    private static KNACKS KNACKS = new KNACKS();
	private static Map<ECERulesetLanguage,SPELLS> SPELLS;
    private static Map<ECERulesetLanguage,List<NAMEGIVERABILITYType>> NAMEGIVERS;
    private static OPTIONALRULES OPTIONALRULES = new OPTIONALRULES();
    private static ITEMS ITEMS = new ITEMS();
    private static HELP HELP = new HELP();
    private static ECEGUILAYOUT ECEGUILAYOUT = new ECEGUILAYOUT();
	private static ECERulesetLanguage RULESETLANGUAGE = new ECERulesetLanguage(RulesetversionType.ED_3,LanguageType.EN);
    private static EDRANDOMNAME RANDOMNAMES = new EDRANDOMNAME();
	private static Map<ECERulesetLanguage,SPELLDESCRIPTIONS> SPELLDESCRIPTIONS;
	private static Map<RulesetversionType,ECECharacteristics> CHARACTERISTICS;
	private static File CONFIGDIR = new File("./config");

    /** Singleton-Instanz dieser Klasse. */
    private static ApplicationProperties theProps = null;

    /** Anzeigetexte (Steuerelemente) */
    private ResourceBundle MESSAGES = null;

    /** Disziplinen (Name Label geordnet) */
    private static final Map<ECERulesetLanguage,Map<String, DISCIPLINE>> DISCIPLINES = new HashMap<ECERulesetLanguage,Map<String, DISCIPLINE>>();
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
		if( theProps == null ) theProps = new ApplicationProperties();
		return theProps;
	}

	public void setRulesetLanguage(ECERulesetLanguage rulesetlanguage) {
		RULESETLANGUAGE = rulesetlanguage;
	}
	public void setRulesetLanguage(RulesetversionType rulesetversion, LanguageType language) {
		setRulesetLanguage(new ECERulesetLanguage(rulesetversion,language));
	}
	public ECERulesetLanguage getRulesetLanguage() {
		return RULESETLANGUAGE;
	}

	/**
	 * Gibt internationalisierten Text zu <code>key</code> zurück.
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
		Map<String,Map<String,?>> result = new HashMap<String, Map<String,?>>();
		for( String s : DISCIPLINES.get(RULESETLANGUAGE).keySet() ) result.put(s,new HashMap<String, Map<String,?>>());
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
		return smallStringMap(result, 8);
	}

	private Map<String,Map<String,?>> smallStringMap(Map<String,Map<String,?>> map, int maxsize) {
		List<String> topLevelStrings = Arrays.asList(map.keySet().toArray(new String[0]));
		Collections.sort(topLevelStrings);
		int step=topLevelStrings.size()/maxsize;
		if( step<2 ) return map;
		Map<String,Map<String,?>> result = new HashMap<String, Map<String,?>>();
		Iterator<String> iter = topLevelStrings.iterator();
		String s = null;
		if( iter.hasNext() ) s = iter.next();
		while( s != null ) {
			String title = s.substring(0, 3) + " - ";
			Map<String,Map<String,?>> entry = new HashMap<String, Map<String,?>>();
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

	public HashMap<String,HashMap<String,List<NAMEGIVERABILITYType>>> getNamgiversByType() {
		HashMap<String,HashMap<String,List<NAMEGIVERABILITYType>>> result = new HashMap<String,HashMap<String,List<NAMEGIVERABILITYType>>>();
		for( NAMEGIVERABILITYType namegiver : getNamegivers() ) {
			String type = namegiver.getType();
			List<String> originList = namegiver.getORIGIN();
			if( originList.isEmpty() ) originList.add("");
			for( String origin : originList ) {
				if( ! result.containsKey(origin) ) result.put(origin,new HashMap<String,List<NAMEGIVERABILITYType>>());
				HashMap<String, List<NAMEGIVERABILITYType>> o = result.get(origin);
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

	public String step2Dice(int value) {
		return getCharacteristics().getSTEPDICEbyStep(value).getDice();
	}

	public ECECapabilities getCapabilities() {
		for( CAPABILITIES c : CAPABILITIES ) {
			if( (new ECERulesetLanguage(c.getRulesetversion(),c.getLang())).equals(RULESETLANGUAGE) ) return new ECECapabilities(c.getSKILLOrTALENT());
		}
		return new ECECapabilities();
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

	public List<KNACKBASEType> getTalentKnacks(String talent) {
		List<KNACKBASEType> knacks = new ArrayList<KNACKBASEType>();
		for( KNACKBASEType knack : KNACKS.getTALENTKNACK() ) {
			if( knack.getBasename().equals(talent) ) knacks.add(knack);
		}
		return knacks;
	}

	// Liefert die Definition aller verfügbarer Zauber zurück.
	// Unabhängig von der Diszipin oder dem Fadenweben-Talent
	public HashMap<String,SPELLDEFType> getSpells() {
		HashMap<String,SPELLDEFType> spellmap = new HashMap<String,SPELLDEFType>();
		for( SPELLDEFType spell : SPELLS.get(RULESETLANGUAGE).getSPELL() ) {
			spellmap.put(spell.getName(), spell);
		}
		return spellmap;
	}

	public HashMap<String,SpelldescriptionType> getSpellDescriptions() {
		HashMap<String,SpelldescriptionType> spellmap = new HashMap<String,SpelldescriptionType>();
		for( SpelldescriptionType spell : SPELLDESCRIPTIONS.get(RULESETLANGUAGE).getSPELL() ) spellmap.put(spell.getName(), spell);
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
			if( (talent.getDiscipline() == discipline) && talent.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
				result.put(talent.getTalent(), talent.getCircle());
			}
		}
		return result;
	}

	/* 
	 * Liefert eine HashMap von Talenten die mehr als einmal gelernt werden können.
	 * Der Key enthält dabei den Talentnamen und das Value den Zähler, wie häufig es gelernt werden darf.
	 */
	public HashMap<String,Integer> getMultiUseTalents() {
		HashMap<String,Integer> result = new HashMap<String,Integer>();
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
		System.out.println("Scheibe Konfigurationsdatei: '" + file + "'");
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

	public HashMap<ATTRIBUTENameType,String> getAttributeNames() {
		HashMap<ATTRIBUTENameType,String> result = new HashMap<ATTRIBUTENameType,String>();
		// Fill result HashMap with default values
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

	public HashMap<SpellkindType,String> getSpellKindMap() {
		for( NAMESPELLWEAVINGType name : TRANSLATIONS.get(RULESETLANGUAGE.getRulesetversion()).getSPELLWEAVING() ) {
			if( name.getLang().equals(RULESETLANGUAGE.getLanguage())) {
				HashMap<SpellkindType,String> spellTypeMap = new HashMap<SpellkindType,String>();
				for( NAMESPELLKINDType type : name.getSPELLKIND() ) {
					spellTypeMap.put(type.getType(),type.getWeaving());
				}
				return spellTypeMap;
			}
		}
		System.err.println("Could not find a SpellTypeMap for language='"+RULESETLANGUAGE.getLanguage().value()+"' and rulesetversion='"+RULESETLANGUAGE.getRulesetversion().value()+"'");
		return new HashMap<SpellkindType,String>();
	}

	public HELP getHelp() {
		return HELP;
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
		RANDOMCHARACTERTEMPLATES.setSpells(SPELLS.get(RULESETLANGUAGE).getSPELL());
		RANDOMCHARACTERTEMPLATES.setCapabilities(getCapabilities());
		RANDOMCHARACTERTEMPLATES.setDisciplineDefinitions(DISCIPLINES.get(RULESETLANGUAGE.getLanguage()));
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
			TRANSLATIONS=new HashMap<RulesetversionType,TRANSLATIONS>();
			for(File translation : selectallxmlfiles(new File(CONFIGDIR,"translation"))) {
				System.out.print("Reading config file '" + translation.getCanonicalPath() + "' ... ");
				TRANSLATIONS t1 = (TRANSLATIONS) unmarshaller.unmarshal(translation);
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
				}
				System.out.println(" done.");
			}

			// Zum späteren leichteren Zugriff, bringe die spezifischen Übersetzungen in ein anderes Format.
			NAMEGIVERTRANSLATIONS=new HashMap<RulesetversionType,List<TRANSLATIONType>>();
			NAMES=new HashMap<String,Map<ECERulesetLanguage,TranslationlabelType>>();
			NAMES.put("karmaritual",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("durability",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("versatility",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("threadweaving",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("languageskillspeak",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("languageskillreadwrite",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("questortalent",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("artisan",new HashMap<ECERulesetLanguage,TranslationlabelType>());
			NAMES.put("knowledge",new HashMap<ECERulesetLanguage,TranslationlabelType>());
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
			for(File disConfigFile : selectallxmlfiles(new File(CONFIGDIR,"disciplines"))) {
				System.out.println("Reading config file: '" + disConfigFile.getCanonicalPath() + "'");
				DISCIPLINE dis = (DISCIPLINE) unmarshaller.unmarshal(disConfigFile);
				ECERulesetLanguage rl = new ECERulesetLanguage(dis.getRulesetversion(),dis.getLang());
				Map<String, DISCIPLINE> dis2 = DISCIPLINES.get(rl);
				if( dis2 == null ) {
					dis2 = new TreeMap<String, DISCIPLINE>();
					DISCIPLINES.put(rl,dis2);
				}
				dis2.put(dis.getName(), dis);
			}

			// capabilities laden
			// --- Bestimmen aller Dateien im Unterordner 'capabilities'
			// --- Einlesen der Dateien
			CAPABILITIES=new ArrayList<CAPABILITIES>();
			for(File capa : selectallxmlfiles(new File(CONFIGDIR,"capabilities"))) {
				System.out.print("Reading config file '" + capa.getCanonicalPath() + "' ...");
				CAPABILITIES c1 = (CAPABILITIES) unmarshaller.unmarshal(capa);
				if( c1 == null ) { System.out.println(" parse error."); continue; }
				boolean notfound=true;
				for( CAPABILITIES c2 : CAPABILITIES ) {
					if( c2.getLang().equals(c1.getLang()) && c2.getRulesetversion().equals(c1.getRulesetversion()) ){
						notfound=false;
						c2.getSKILLOrTALENT().addAll(c1.getSKILLOrTALENT());
					}
				}
				if( notfound ) CAPABILITIES.add(c1);
				System.out.println(" done.");
			}

			// spells laden
			// --- Bestimmen aller Dateien im Unterordner 'spells'
			// --- Einlesen der Dateien
			SPELLS=new HashMap<ECERulesetLanguage,SPELLS>();
			SPELLDESCRIPTIONS=new HashMap<ECERulesetLanguage,SPELLDESCRIPTIONS>();
			File spelldescriptionsdir = new File(CONFIGDIR, "spelldescriptions");
			for(File spells : selectallxmlfiles(new File(CONFIGDIR,"spells"))) {
				System.out.print("Reading config file '" + spells.getCanonicalPath() + "' ...");
				SPELLS s = (SPELLS) unmarshaller.unmarshal(spells);
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
				System.out.println(" done.");
				Stack<String> filepath = new Stack<String>();
				File file = spells;
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
					System.out.println("Reading config file '" + spelldescriptionsfile.getCanonicalPath() + "'");
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
					System.out.println("Scheibe Konfigurationsdatei: '" + spelldescriptionsfile.getCanonicalPath() + "'");
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
			KNACKS=new KNACKS();
			KNACKS.setLang(RULESETLANGUAGE.getLanguage());
			for(File knacks : selectallxmlfiles(new File(CONFIGDIR,"knacks"))) {
				System.out.print("Reading config file '" + knacks.getCanonicalPath() + "' ...");
				KNACKS k = (KNACKS) unmarshaller.unmarshal(knacks);
				if( k == null ) { System.out.println(" parse error."); continue; }
				if( k.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
					KNACKS.getTALENTKNACK().addAll(k.getTALENTKNACK());
					KNACKS.getSKILLKNACK().addAll(k.getSKILLKNACK());
					System.out.println(" done.");
				} else {
					System.out.println(" skipped. Wrong language: "+k.getLang().value()+" != "+RULESETLANGUAGE.getLanguage().value() );
				}
			}

			// itemstore laden
			// --- Bestimmen aller Dateien im Unterordner 'disciplines'
			// --- Einlesen der Dateien
			ITEMS=new ITEMS();
			ITEMS.setLang(RULESETLANGUAGE.getLanguage());
			for(File items : selectallxmlfiles(new File(CONFIGDIR,"itemstore"))) {
				System.out.println("Reading config file '" + items.getCanonicalPath() + "' ...");
				ITEMS i = (ITEMS) unmarshaller.unmarshal(items);
				if( i == null ) { System.out.println(" parse error."); continue; }
				if( i.getLang().equals(RULESETLANGUAGE.getLanguage()) ) {
					ITEMS.getARMOR().addAll(i.getARMOR());
					ITEMS.getBLOODCHARMITEM().addAll(i.getBLOODCHARMITEM());
					ITEMS.getITEM().addAll(i.getITEM());
					ITEMS.getMAGICITEM().addAll(i.getMAGICITEM());
					ITEMS.getPATTERNITEM().addAll(i.getPATTERNITEM());
					ITEMS.getSHIELD().addAll(i.getSHIELD());
					ITEMS.getTHREADITEM().addAll(i.getTHREADITEM());
					ITEMS.getWEAPON().addAll(i.getWEAPON());
					System.out.println(" done.");
				} else {
					System.out.println(" skipped. Wrong language: "+i.getLang().value()+" != "+RULESETLANGUAGE.getLanguage().value() );
				}
			}
			for( ITEMType i : ITEMS.getARMOR() )          { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getBLOODCHARMITEM() ) { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getITEM() )           { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getMAGICITEM() )      { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getPATTERNITEM() )    { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getSHIELD() )         { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getTHREADITEM() )     { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }
			for( ITEMType i : ITEMS.getWEAPON() )         { i.setVirtual(YesnoType.NO); i.setUsed(YesnoType.NO); i.setLocation("self"); }

			// randomcharactertemplates laden
			// --- Bestimmen aller Dateien im Unterordner 'randomcharactertemplates'
			// --- Einlesen der Dateien
			for(File configFile : selectallxmlfiles(new File(CONFIGDIR,"randomcharactertemplates"))) {
				System.out.println("Reading config file '" + configFile.getCanonicalPath() + "'");
				EDRANDOMCHARACTERTEMPLATE t = (EDRANDOMCHARACTERTEMPLATE) unmarshaller.unmarshal(configFile);
				if( t.getLang().equals(RULESETLANGUAGE.getLanguage()) ) RANDOMCHARACTERTEMPLATES.put(t.getName(), t);
			}

			NAMEGIVERS=new HashMap<ECERulesetLanguage,List<NAMEGIVERABILITYType>>();
			for(File configFile : selectallxmlfiles(new File(CONFIGDIR,"namegivers"))) {
				System.out.println("Reading config file '" + configFile.getCanonicalPath() + "'");
				NAMEGIVERS t = (NAMEGIVERS) unmarshaller.unmarshal(configFile);
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

			CHARACTERISTICS=new HashMap<RulesetversionType,ECECharacteristics>();
			for(File configFile : selectallxmlfiles(new File(CONFIGDIR,"characteristics"))) {
				System.out.println("Reading config file '" + configFile.getCanonicalPath() + "'");
				CHARACTERISTICS t = (CHARACTERISTICS) unmarshaller.unmarshal(configFile);
				CHARACTERISTICS.put(t.getRulesetversion(), new ECECharacteristics(t));
			}

			File filename=new File(CONFIGDIR,"optionalrules.xml");
			System.out.println("Reading config file '" + filename.getCanonicalPath() + "'");
			OPTIONALRULES = (OPTIONALRULES) unmarshaller.unmarshal(filename);
			filename=new File(CONFIGDIR,"help.xml");
			System.out.println("Reading config file '" + filename.getCanonicalPath() + "'");
			HELP = (HELP) unmarshaller.unmarshal(filename);
			filename=new File(CONFIGDIR,"randomnames.xml");
			System.out.println("Reading config file '" + filename.getCanonicalPath() + "'");
			RANDOMNAMES = (EDRANDOMNAME) unmarshaller.unmarshal(filename);
			filename=new File(CONFIGDIR,"eceguilayout.xml");
			System.out.println("Reading config file '" + filename.getCanonicalPath() + "'");
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

	public static List<File> selectallxmlfiles(File folder) {
		List<File> files = new ArrayList<File>();
		List<File> folders = new ArrayList<File>();
		for( File file : folder.listFiles() ) {
			String name=file.getName();
			if( name == null ) continue;
			if( file.isFile() && name.endsWith(".xml") ) files.add(file);
			else if( file.isDirectory() ) folders.add(file);
		}
		for( File dir : folders ) files.addAll(selectallxmlfiles(dir));
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
		for( CAPABILITIES c : CAPABILITIES ) {
			ECERulesetLanguage rl = new ECERulesetLanguage(c.getRulesetversion(),c.getLang());
			HashMap<String,List<TranslationlabelType>> translation = new HashMap<String,List<TranslationlabelType>>();
			HashMap<String,Integer> count = new HashMap<String,Integer>();
					for( TRANSLATIONType i : TRANSLATIONS.get(rl.getRulesetversion()).getCAPABILITY() ) {
						for( TranslationlabelType j : i.getLABEL() ) {
							if( j.getLang().equals(rl.getLanguage()) ) {
								translation.put(j.getValue(), i.getLABEL());
								count.put(j.getValue(), 0);
							}
						}
			}
			for( JAXBElement<CAPABILITYType> t : c.getSKILLOrTALENT() ) {
				String name = t.getValue().getName();
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
