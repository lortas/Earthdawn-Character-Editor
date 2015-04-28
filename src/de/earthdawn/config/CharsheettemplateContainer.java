package de.earthdawn.config;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.earthdawn.data.CHARSHEETTEMPLATE;
import de.earthdawn.data.CharsheettemplatebooleanType;
import de.earthdawn.data.CharsheettemplatedisciplinebonusType;
import de.earthdawn.data.CharsheettemplatespellType;
import de.earthdawn.data.CharsheettemplatetalentType;

public class CharsheettemplateContainer {
	private String pdfFilename="";
	private String menuentryname="";
	private Map<String,List<String>> stringentryList = new TreeMap<String,List<String>>(); 
	private Map<String,Iterator<String>> stringentryIterator = new TreeMap<String,Iterator<String>>(); 
	private Map<String,List<CharsheettemplatebooleanType>> booleanEntryList = new TreeMap<String,List<CharsheettemplatebooleanType>>(); 
	private Map<String,Iterator<CharsheettemplatebooleanType>> booleanentryIterator = new TreeMap<String,Iterator<CharsheettemplatebooleanType>>(); 
	private Map<String,List<CharsheettemplatedisciplinebonusType>> disciplinebonusEntryList = new TreeMap<String,List<CharsheettemplatedisciplinebonusType>>();
	private Map<String,Iterator<CharsheettemplatedisciplinebonusType>> disciplinebonusentryIterator = new TreeMap<String,Iterator<CharsheettemplatedisciplinebonusType>>();
	private Map<String,List<CharsheettemplatespellType>> spellEntryList = new TreeMap<String,List<CharsheettemplatespellType>>();
	private Map<String,Iterator<CharsheettemplatespellType>> spellentryIterator = new TreeMap<String,Iterator<CharsheettemplatespellType>>();
	private CharsheettemplatetalentStack talentEntryStack = new CharsheettemplatetalentStack();
	private List<CharsheettemplatetalentType> skillEntryList = new LinkedList<CharsheettemplatetalentType>();
	private Iterator<CharsheettemplatetalentType> skillEntryIterator = null;

	public CharsheettemplateContainer(File template) {
		System.out.println("Reading template file: '" + template.getAbsolutePath() + "'");
		CHARSHEETTEMPLATE fieldmap;
		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance("de.earthdawn.data").createUnmarshaller();
			fieldmap = (CHARSHEETTEMPLATE) unmarshaller.unmarshal(template);
			pdfFilename = fieldmap.getFilename();
			menuentryname = fieldmap.getMenuentryname();
			harvestElements(fieldmap);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private void harvestElements(CHARSHEETTEMPLATE fieldmap) {
		for( JAXBElement<?> f : fieldmap.getCurrentDateTimeOrNameOrDisciplineName() ) {
			String key = f.getName().getLocalPart();
			Class<? extends Object> valueClass = f.getValue().getClass();
			if( valueClass.equals(String.class) ) {
				addStringEntry(key,(String)f.getValue());
			} else if( valueClass.equals(CharsheettemplatebooleanType.class) ) {
				addBooleanEntry(key,(CharsheettemplatebooleanType)f.getValue());
			} else if( valueClass.equals(CharsheettemplatedisciplinebonusType.class) ) {
				addDisciplinebonusEntry(key,(CharsheettemplatedisciplinebonusType)f.getValue());
			} else if( valueClass.equals(CharsheettemplatespellType.class) ) {
				addSpellEntry(key,(CharsheettemplatespellType)f.getValue());
			} else if( valueClass.equals(CharsheettemplatetalentType.class) ) {
				if( key.equals("Talent") ) {
					this.talentEntryStack.push((CharsheettemplatetalentType)f.getValue());
				} else if( key.equals("Skill") ) {
					this.skillEntryList.add((CharsheettemplatetalentType)f.getValue());
				} else {
					System.err.println("Unhandled fieldname '"+key+"' of class '"+valueClass+"'.");
				}
			} else {
				System.err.println("Unhandled fieldname '"+key+"' of class '"+valueClass+"'.");
			}
		}
	}

	public String getPdfFilename() {
		return pdfFilename;
	}

	public String getMenuentryname() {
		return menuentryname;
	}

	private void addStringEntry(String key, String entry) {
		getStringList(key).add(entry);
	}

	private void addDisciplinebonusEntry(String key, CharsheettemplatedisciplinebonusType entry) {
		getDisciplinebonusList(key).add(entry);
	}

	private void addSpellEntry(String key, CharsheettemplatespellType entry) {
		getSpellList(key).add(entry);
	}

	private void addBooleanEntry(String key, CharsheettemplatebooleanType entry) {
		getBooleanList(key).add(entry);
	}

	public List<String> getStringList(String key) {
		List<String> values = this.stringentryList.get(key);
		if( values == null ) {
			values = new LinkedList<String>();
			this.stringentryList.put(key, values);
		}
		return values;
	}

	public String getStringEntryNext(String key) {
		Iterator<String> iterator = this.stringentryIterator.get(key);
		if( iterator == null ) {
			iterator = getStringList(key).iterator();
			this.stringentryIterator.put(key, iterator);
		}
		if( iterator.hasNext() ) {
			return iterator.next();
		} else {
			return null;
		}
	}

	public List<CharsheettemplatedisciplinebonusType> getDisciplinebonusList(String key) {
		List<CharsheettemplatedisciplinebonusType> values = this.disciplinebonusEntryList.get(key);
		if( values == null ) {
			values = new LinkedList<CharsheettemplatedisciplinebonusType>();
			this.disciplinebonusEntryList.put(key, values);
		}
		return values;
	}

	public CharsheettemplatedisciplinebonusType getDisciplinebonusEntryNext(String key) {
		Iterator<CharsheettemplatedisciplinebonusType> iterator = this.disciplinebonusentryIterator.get(key);
		if( iterator == null ) {
			iterator = getDisciplinebonusList(key).iterator();
			this.disciplinebonusentryIterator.put(key, iterator);
		}
		if( iterator.hasNext() ) {
			return iterator.next();
		} else {
			return null;
		}
	}

	public List<CharsheettemplatespellType> getSpellList(String key) {
		List<CharsheettemplatespellType> values = this.spellEntryList.get(key);
		if( values == null ) {
			values = new LinkedList<CharsheettemplatespellType>();
			this.spellEntryList.put(key, values);
		}
		return values;
	}

	public CharsheettemplatespellType getSpellEntryNext(String key) {
		Iterator<CharsheettemplatespellType> iterator = this.spellentryIterator.get(key);
		if( iterator == null ) {
			iterator = getSpellList(key).iterator();
			this.spellentryIterator.put(key, iterator);
		}
		if( iterator.hasNext() ) {
			return iterator.next();
		} else {
			return null;
		}
	}

	private List<CharsheettemplatebooleanType> getBooleanList(String key) {
		List<CharsheettemplatebooleanType> values = this.booleanEntryList.get(key);
		if( values == null ) {
			values = new LinkedList<CharsheettemplatebooleanType>();
			this.booleanEntryList.put(key, values);
		}
		return values;
	}

	public CharsheettemplatebooleanType getBooleanEntryNext(String key) {
		Iterator<CharsheettemplatebooleanType> iterator = this.booleanentryIterator.get(key);
		if( iterator == null ) {
			iterator = getBooleanList(key).iterator();
			this.booleanentryIterator.put(key, iterator);
		}
		if( iterator.hasNext() ) {
			return iterator.next();
		} else {
			return null;
		}
	}

	public CharsheettemplatetalentType getSkillEntryNext() {
		if( this.skillEntryIterator == null ) {
			this.skillEntryIterator = this.skillEntryList.iterator();
		}
		if( this.skillEntryIterator.hasNext() ) {
			return this.skillEntryIterator.next();
		} else {
			return null;
		}
	}

	public CharsheettemplatetalentStack getTalentStack() {
		return this.talentEntryStack;
	}
}
