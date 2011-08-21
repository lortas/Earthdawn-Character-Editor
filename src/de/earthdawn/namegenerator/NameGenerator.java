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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.earthdawn.data.GenderType;
import de.earthdawn.data.RANDOMNAMERACEType;
import de.earthdawn.data.RandomnameNamesType;

public class NameGenerator {

	private final HashMap<String,HashMap<GenderType,List<List<Syllable>>>> randomnameraceMap = new HashMap<String, HashMap<GenderType,List<List<Syllable>>>>();;

	public NameGenerator( List<RANDOMNAMERACEType> randomnameraces ) {
		for( RANDOMNAMERACEType randomnamerace : randomnameraces ) {
			String currentRaceName = randomnamerace.getRace();
			HashMap<GenderType,List<List<Syllable>>> randomnamesByRace = randomnameraceMap.get(currentRaceName);
			if( randomnamesByRace == null ) {
				randomnamesByRace = new HashMap<GenderType, List<List<Syllable>>>();
				randomnameraceMap.put(currentRaceName, randomnamesByRace);
			}
			for( RandomnameNamesType RandomnameNames : randomnamerace.getRANDOMNAMENAMEPART() ) {
				GenderType currentGender = RandomnameNames.getGender();
				List<List<Syllable>> randomnamesByRaceByGender = randomnamesByRace.get(currentGender);
				if( randomnamesByRaceByGender == null ) {
					randomnamesByRaceByGender = new ArrayList<List<Syllable>>();
					randomnamesByRace.put(currentGender, randomnamesByRaceByGender);
				}
				int currentPart = RandomnameNames.getPart();
				if( currentPart < 0 ) {
					System.err.println("part number is to low: "+currentPart);
					continue;
				}
				while( randomnamesByRaceByGender.size() <= currentPart+1 ) randomnamesByRaceByGender.add(new ArrayList<Syllable>());
				List<Syllable> randomnamesByRaceByGenderByPart = randomnamesByRaceByGender.get(currentPart);
				for( String s : RandomnameNames.getValue().trim().split(RandomnameNames.getDelimiter()) ) {
					if( s.isEmpty() ) continue;
					SyllableType type = SyllableType.BEGIN;
					Syllable lastSyl = null;
					for( String syllable : s.trim().toLowerCase().split(RandomnameNames.getSyllabledelimiter()) ) {
						Syllable syl = new Syllable(syllable.trim(),type);
						if( randomnamesByRaceByGenderByPart.contains(syl) ) {
							int sylidx= randomnamesByRaceByGenderByPart.indexOf(syl);
							syl = randomnamesByRaceByGenderByPart.get(sylidx);
							syl.isalso(type);
						} else {
							randomnamesByRaceByGenderByPart.add(syl);
						}
						if( lastSyl != null ) {
							lastSyl.isalso(SyllableType.MID);
							lastSyl.insertNext(currentRaceName, currentGender, currentPart, syl);
						}
						lastSyl=syl;
						if( type == SyllableType.BEGIN ) type = SyllableType.END;
					}
				}
			}
		}
	}
}
