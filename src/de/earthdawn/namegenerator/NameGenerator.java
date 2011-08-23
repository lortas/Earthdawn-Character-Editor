package de.earthdawn.namegenerator;
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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.earthdawn.data.GenderType;
import de.earthdawn.data.RANDOMNAMERACEType;
import de.earthdawn.data.RandomnameNamesType;

public class NameGenerator {

	private final HashMap<String,HashMap<GenderType,List<List<SyllableComplex>>>> randomnameraceMap = new HashMap<String, HashMap<GenderType,List<List<SyllableComplex>>>>();
	private final HashMap<String,HashMap<GenderType,List<Integer>>> creativityMap = new HashMap<String, HashMap<GenderType,List<Integer>>>();
	private final List<SyllableComplex> randomnameraceList = new ArrayList<SyllableComplex>();
	private List<RANDOMNAMERACEType> randomnameraces = null;
	private static Random rand = new Random();

	public NameGenerator( List<RANDOMNAMERACEType> randomnameraces ) {
		this.randomnameraces  = randomnameraces;
		for( RANDOMNAMERACEType randomnamerace : randomnameraces ) {
			String currentRaceName = randomnamerace.getRace();
			HashMap<GenderType,List<List<SyllableComplex>>> randomnamesByRace = randomnameraceMap.get(currentRaceName);
			if( randomnamesByRace == null ) {
				randomnamesByRace = new HashMap<GenderType, List<List<SyllableComplex>>>();
				randomnameraceMap.put(currentRaceName, randomnamesByRace);
			}
			HashMap<GenderType, List<Integer>> creativityByRace = creativityMap.get(currentRaceName);
			if( creativityByRace == null ) {
				creativityByRace = new HashMap<GenderType, List<Integer>>();
				creativityMap.put(currentRaceName, creativityByRace);
			}
			for( RandomnameNamesType RandomnameNames : randomnamerace.getRANDOMNAMENAMEPART() ) {
				GenderType currentGender = RandomnameNames.getGender();
				List<List<SyllableComplex>> randomnamesByRaceByGender = randomnamesByRace.get(currentGender);
				if( randomnamesByRaceByGender == null ) {
					randomnamesByRaceByGender = new ArrayList<List<SyllableComplex>>();
					randomnamesByRace.put(currentGender, randomnamesByRaceByGender);
				}
				List<Integer> creativityByRaceByGender = creativityByRace.get(currentGender);
				if( creativityByRaceByGender == null ) {
					creativityByRaceByGender = new ArrayList<Integer>();
					creativityByRace.put(currentGender, creativityByRaceByGender);
				}
				int currentPart = RandomnameNames.getPart();
				if( currentPart < 0 ) {
					System.err.println("part number is to low: "+currentPart);
					continue;
				}
				while( randomnamesByRaceByGender.size() <= currentPart ) randomnamesByRaceByGender.add(new ArrayList<SyllableComplex>());
				List<SyllableComplex> randomnamesByRaceByGenderByPart = randomnamesByRaceByGender.get(currentPart);
				while( creativityByRaceByGender.size() <= currentPart ) creativityByRaceByGender.add(new Integer(0));
				creativityByRaceByGender.set(currentPart, RandomnameNames.getCreativity());
				String nameList = RandomnameNames.getValue().trim();
//				for( String s : nameList.split(RandomnameNames.getDelimiter()) ) {
//					if( s.isEmpty() ) continue;
//					SyllableType type = SyllableType.BEGIN;
//					SyllableComplex lastSyl = null;
//					String syllableList = s.trim().toLowerCase();
//					for( String syllable : syllableList.split(RandomnameNames.getSyllabledelimiter()) ) {
//						SyllableComplex syl = new SyllableComplex(syllable.trim(),type);
//						if( randomnamesByRaceByGenderByPart.contains(syl) ) {
//							int sylidx= randomnamesByRaceByGenderByPart.indexOf(syl);
//							syl = randomnamesByRaceByGenderByPart.get(sylidx);
//							syl.isAlso(type);
//						} else {
//							if( randomnameraceList.contains(syl) ) {
//								int sylidx= randomnameraceList.indexOf(syl);
//								syl = randomnameraceList.get(sylidx);
//								syl.isAlso(type);
//							} else {
//								randomnameraceList.add(syl);
//							}
//							randomnamesByRaceByGenderByPart.add(syl);
//						}
//						if( lastSyl != null ) {
//							syl.isAlso(SyllableType.MID);
//							lastSyl.insertNext(currentRaceName, currentGender, currentPart, syl);
//						}
//						lastSyl=syl;
//						if( type == SyllableType.BEGIN ) type = SyllableType.END;
//					}
//				}
			}
		}
	}

	public String generateName(String race, GenderType gender) {
		StringBuffer result = new StringBuffer();
		HashMap<GenderType,List<List<SyllableComplex>>> randomnamesByRace = randomnameraceMap.get(race);
		List<List<SyllableComplex>> randomnamesByRaceByGender = randomnamesByRace.get(gender);
		for( int part=0; part<randomnamesByRaceByGender.size(); part++ ) {
			result.append(generateName(race,gender,part));
			result.append(" ");
		}
		return result.toString().trim();
	}

	public String generateName(String race, GenderType gender, int part) {
		StringBuffer result = new StringBuffer();
		HashMap<GenderType,List<List<SyllableComplex>>> randomnamesByRace = randomnameraceMap.get(race);
		List<SyllableComplex> syllables = new ArrayList<SyllableComplex>(); 
		syllables.addAll(randomnamesByRace.get(gender).get(part));
		// Falls ein Geschlecht angegeben ist, dann die Silben für Geschlechtsneutral noch mitnehmen.
		if( ! gender.equals(GenderType.MINUS) ) {
			List<List<SyllableComplex>> noGenderNames = randomnamesByRace.get(GenderType.MINUS);
			if( noGenderNames!= null ) {
				List<SyllableComplex> p = noGenderNames.get(part);
				if( p!=null ) syllables.addAll(p);
			}
		}
		int creativity = creativityMap.get(race).get(gender).get(part);
		while( (syllables!=null) && (syllables.size()>0) ) {
			SyllableComplex syl = syllables.get(rand.nextInt(syllables.size()));
			result.append(syl.getSyl());
			syllables = syl.getNext(race, gender, part);
			if( rand.nextInt(101) > creativity) break;
		};
		return result.toString();
	}

	public String generateName2(String race, GenderType gender) {
		StringBuffer result = new StringBuffer();
		int part=0;
		do {
			String name = generateName2(race,part,gender);
			if( name == null ) break;
			if( name.isEmpty() ) break;
			result.append(" ");
			result.append(name);
			part++;
		} while(true);
		return result.toString().trim();
	}

	public String generateName2(String race, int part, GenderType gender) {
		for( RANDOMNAMERACEType randomnamerace : randomnameraces ) {
			if( randomnamerace.getRace().equals(race) ) return generateName2(randomnamerace,part,gender);
		}
		return null;
	}

	public static String generateName2(RANDOMNAMERACEType randomnamerace, int part, GenderType gender) {
		List<RandomnameNamesType> namelists = new ArrayList<RandomnameNamesType>();
		for( RandomnameNamesType r : randomnamerace.getRANDOMNAMENAMEPART() ) {
			if( r.getPart() == part ) namelists.add(r);
		}
		return generateName2(namelists,gender);
	}

	public static String generateName2(List<RandomnameNamesType> namelists, GenderType gender) {
		if( namelists == null ) return null;
		if( namelists.isEmpty() ) return "";
		int creativity=0;
		List<List<String>> names = new ArrayList<List<String>>();
		for( RandomnameNamesType namelist : namelists ) {
			if( namelist.getGender().equals(GenderType.MINUS) || namelist.getGender().equals(gender) ) {
				if( namelist.getCreativity() > creativity ) creativity=namelist.getCreativity();
				for( String name : namelist.getValue().trim().split(namelist.getDelimiter()) ) {
					if( name.isEmpty() ) continue;
					List<String> syllist=new ArrayList<String>();
					for( String syl : name.trim().split(namelist.getSyllabledelimiter()) ) {
						syllist.add(syl.toLowerCase().trim());
					}
					names.add(syllist);
				}
			}
		}
		return generateName2(names,creativity);
	}

	public static String generateName2(List<List<String>> names, int creativity) {
		List<SyllableSimple> syllables = new ArrayList<SyllableSimple>();
		for( List<String> name : names ) {
			if( name.isEmpty() ) continue;
			SyllableType type = SyllableType.BEGIN;
			SyllableSimple lastSyl = null;
			for( String syllable : name ) {
				SyllableSimple syl = new SyllableSimple(syllable,type);
				if( syllables.contains(syl) ) {
					int sylidx= syllables.indexOf(syl);
					syl = syllables.get(sylidx);
					syl.isAlso(type);
				} else {
					syllables.add(syl);
				}
				if( lastSyl != null ) {
					syl.isAlso(SyllableType.MID);
					lastSyl.insertNext(syl);
				}
				lastSyl=syl;
				if( type == SyllableType.BEGIN ) type = SyllableType.END;
			}
		}

		StringBuffer result = new StringBuffer();
		while( (syllables!=null) && (syllables.size()>0) ) {
			SyllableSimple syl = syllables.get(rand.nextInt(syllables.size()));
			result.append(syl.getSyl());
			syllables = syl.getNext();
			if( rand.nextInt(101) > creativity) break;
		};
		return result.toString();
	}

	public void printAllSyllable() {
		printAllSyllable(System.out);
	}

	public void printAllSyllable(PrintStream out) {
		for( SyllableComplex syl : randomnameraceList ) {
			out.println(syl.toString());
		}
	}

	public String generateName3(String race, GenderType gender ) {
		List<List<String>> namesAllRaces = new ArrayList<List<String>>();
		List<List<String>> namesOneRace = new ArrayList<List<String>>();
		final int maxPart=4;
		for( int i=0; i<maxPart; i++ ) {
			namesAllRaces.add(new ArrayList<String>());
			namesOneRace.add(new ArrayList<String>());
		}
		for( RANDOMNAMERACEType randomnames: this.randomnameraces ) {
			boolean isRace=randomnames.getRace().equals(race);
			for( RandomnameNamesType nameparts : randomnames.getRANDOMNAMENAMEPART() ) {
				int part=nameparts.getPart();
				if( part < 0 ) continue;
				if( part >= maxPart ) continue;
				GenderType g = nameparts.getGender();
				if( gender.equals(GenderType.MINUS) || g.equals(GenderType.MINUS) || g.equals(gender) ) {
					List<String> splitNameParts = new ArrayList<String>();
					for( String s : nameparts.getValue().trim().split(nameparts.getDelimiter()) ) {
						if( s.isEmpty() ) continue;
						StringBuffer buf = new StringBuffer();
						for( String sylalable : s.trim().split(nameparts.getSyllabledelimiter()) ) buf.append(sylalable.trim());
						String concat = buf.toString();
						splitNameParts.add(concat.substring(0,1).toUpperCase()+concat.substring(1).toLowerCase());
					}
					namesAllRaces.get(part).addAll(splitNameParts);
					if( isRace ) namesOneRace.get(part).addAll(splitNameParts);
				}
			}
		}
		StringBuffer namebuf = new StringBuffer();
		for( int i=0; i<maxPart; i++ ) {
			List<String> list = namesOneRace.get(i);
			if( list.isEmpty() ) list = namesAllRaces.get(i);
			if( list.isEmpty() ) continue;
			namebuf.append(" ");
			namebuf.append(list.get(rand.nextInt(list.size())));
		}
		return namebuf.toString().trim();
	}
}
