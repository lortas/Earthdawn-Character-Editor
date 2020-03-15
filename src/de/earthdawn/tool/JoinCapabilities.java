package de.earthdawn.tool;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.CAPABILITIES;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.LanguageType;

public class JoinCapabilities {
	private static final String encoding="UTF-8";

	public static void main(String[] args) {
		if( args.length < 2 ) {
			System.out.println("not engouth paramter.");
			System.out.println("use JoinCapabilities infile1 [infile2 ...] outfile");
			return;
		}
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			Unmarshaller u = jc.createUnmarshaller();
			int countInFiles=args.length-1;
			Map<String,CAPABILITYType> talents = new TreeMap<String,CAPABILITYType>();
			Map<String,CAPABILITYType> skills = new TreeMap<String,CAPABILITYType>();
			LanguageType language=null;
			for( int i=0; i<countInFiles; i++ ) {
				File inFile = new File(args[i]);
				System.out.println("Reading Capabilities from "+inFile.getCanonicalFile());
				CAPABILITIES capabilities = (CAPABILITIES) u.unmarshal(inFile);
				ECECapabilities caps = new ECECapabilities(capabilities.getSKILLOrTALENTOrDEVOTION(),null);
				if( language == null ) language = capabilities.getLang();
				else if( ! capabilities.getLang().equals(language) ) System.err.println("Languages are not identical: '"+language.value()+"'!='"+capabilities.getLang().value()+"'");
				for( CAPABILITYType t : caps.getTalents() ) {
					String name = t.getName().replaceAll("[ '()]", "").toUpperCase();
					if( talents.containsKey(name) ) {
						System.out.print("Talent '"+t.getName()+"' is dupplicated. Keeping previous version");
						String prevName = talents.get(name).getName();
						if( prevName.equals(t.getName()) ) System.out.println(".");
						else System.out.println(": "+prevName);
					} else talents.put(name, t);
				}
				for( CAPABILITYType t : caps.getSkills() ) {
					String name = t.getName().replaceAll("[ '()]", "").toUpperCase();
					if( skills.containsKey(name) ) {
						System.out.println("Skill '"+t.getName()+"' is dupplicated. Keeping previous version");
						String prevName = skills.get(name).getName();
						if( prevName.equals(t.getName()) ) System.out.println(".");
						else System.out.println(": "+prevName);
					} else skills.put(name, t);
				}
			}
			CAPABILITIES outCapabilities = new CAPABILITIES();
			outCapabilities.setLang(language);
			List<JAXBElement<?>> skillOrTalent = outCapabilities.getSKILLOrTALENTOrDEVOTION();
			TreeSet<String> capnames = new TreeSet<String>(skills.keySet());
			for( String skillname : capnames ) {
				QName qName = new QName("http://earthdawn.com/capability","SKILL");
				JAXBElement<CAPABILITYType> jaxb = new JAXBElement<CAPABILITYType>(qName, CAPABILITYType.class, skills.get(skillname));
				skillOrTalent.add(jaxb);
			}
			capnames = new TreeSet<String>(talents.keySet());
			for( String talentname : capnames ) {
				QName qName = new QName("http://earthdawn.com/capability","TALENT");
				JAXBElement<CAPABILITYType> jaxb = new JAXBElement<CAPABILITYType>(qName, CAPABILITYType.class, talents.get(talentname));
				skillOrTalent.add(jaxb);
			}
			Marshaller m = jc.createMarshaller();
			// Das letzte Element ist die Zieldatei, in der die Ausgabe hinein geschrieben wird.
			System.out.println("Writing Capabilities to "+args[countInFiles]);
			FileOutputStream out = new FileOutputStream(args[countInFiles]);
			PrintStream fileio = new PrintStream(out, false, encoding);
			m.setProperty(Marshaller.JAXB_ENCODING, encoding);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/capability earthdawncapabilities.xsd");
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			fileio.print("<?xml version=\"1.0\" encoding=\""+encoding+"\" standalone=\"no\"?>");
			m.marshal(outCapabilities,fileio);
			fileio.close();
			out.close();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
