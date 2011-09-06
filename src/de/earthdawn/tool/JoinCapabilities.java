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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;

import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.CAPABILITIES;
import de.earthdawn.data.CAPABILITYType;

public class JoinCapabilities {
	private static final String encoding="UTF-8";

	public static int main(String[] args) {
		if( args.length < 2 ) {
			System.out.println("Zuwenige Paramter");
			System.out.println("JoinCapabilities infile1 [infile2 ...] outfile");
			return -1;
		}
		try {
			JAXBContext jc = JAXBContext.newInstance("de.earthdawn.data");
			Unmarshaller u = jc.createUnmarshaller();
			int countInFiles=args.length-1;
			List<CAPABILITYType> talents = new ArrayList<CAPABILITYType>();
			List<CAPABILITYType> skills = new ArrayList<CAPABILITYType>();
			for( int i=0; i<countInFiles; i++ ) {
				ECECapabilities caps = new ECECapabilities(((CAPABILITIES) u.unmarshal(new File(args[i]))).getSKILLOrTALENT());
				talents.addAll(caps.getTalents());
				skills.addAll(caps.getSkills());
			}
			CAPABILITIES outCapabilities = new CAPABILITIES();
			Marshaller m = jc.createMarshaller();
			FileOutputStream out = new FileOutputStream(args[countInFiles]);
			PrintStream fileio = new PrintStream(out, false, encoding);
			m.setProperty(Marshaller.JAXB_ENCODING, encoding);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://earthdawn.com/capability earthdawncapabilities.xsd");
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			fileio.print("<?xml version=\"1.0\" encoding=\""+encoding+"\" standalone=\"no\"?>\n<?xml-stylesheet type=\"text/xsl\" href=\"earthdawncharacter.xsl\"?>");
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
		return 0;
	}
}
