package de.earthdawn;

import java.util.HashMap;

import javax.xml.bind.JAXBElement;

import de.earthdawn.data.*;

public class CharacterContainer {
	private EDCHARACTER character = null;

	public CharacterContainer( EDCHARACTER c) {
		character = c;
	}
	
	public void setEDCHARACTER(EDCHARACTER c) {
		character = c;
	}
	public EDCHARACTER getEDCHARACTER() {
		return character;
	}

	public String getName() {
		return character.getName();
	}
	public APPEARANCEType getAppearance() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if( element.getName().getLocalPart().equals("APPEARANCE") ) {
				return (APPEARANCEType) element.getValue();
			}
		}
		// Not found
		return null;
	}

	public HashMap<String, ATTRIBUTEType> getAttributes() {
		HashMap<String,ATTRIBUTEType> attributes = new HashMap<String,ATTRIBUTEType>();
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("ATTRIBUTE")) {
				ATTRIBUTEType attribute = (ATTRIBUTEType) element.getValue();
				attributes.put(attribute.getName().value(), attribute);
			}
		}
		return attributes;
	}

	public ATTRIBUTEType getAttributeByName(String name) {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("ATTRIBUTE")) {
				ATTRIBUTEType attribute = (ATTRIBUTEType) element.getValue();
				if (attribute.getName().value().equals(name)) {
					return attribute;
				}
			}
		}
		// Not found
		return null;
	}

	public DEFENSEType getDefence() {
		for (JAXBElement<?> element : character.getATTRIBUTEOrDEFENSEOrHEALTH()) {
			if (element.getName().getLocalPart().equals("DEFENSE")) {
				return (DEFENSEType) element.getValue();
			}
		}
		// Not found
		return null;
	}

}
