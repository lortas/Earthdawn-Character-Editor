package de.earthdawn;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.APPEARANCEType;
import de.earthdawn.data.ATTRIBUTENameType;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.CALCULATEDLEGENDPOINTSType;
import de.earthdawn.data.DEATHType;
import de.earthdawn.data.DEVOTIONType;
import de.earthdawn.data.EXPERIENCEType;
import de.earthdawn.data.EffectlayerType;
import de.earthdawn.data.INITIATIVEType;
import de.earthdawn.data.KARMAType;
import de.earthdawn.data.MOVEMENTType;
import de.earthdawn.data.PROTECTIONType;
import de.earthdawn.data.RECOVERYType;
import de.earthdawn.data.USEDSTARTRANKSType;
import de.earthdawn.data.WOUNDType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.util.Map.Entry;

public class CharacteristicStatus {
	public static ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private Map<ATTRIBUTENameType, String> attributesNames = PROPERTIES.getAttributeNames();
	private CharacterContainer character;
	private Configuration cfg;
	private Template template=null;

	public CharacteristicStatus(String filename) {
		try {
			cfg = new freemarker.template.Configuration();
			cfg.setDirectoryForTemplateLoading(new File("./templates"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			template = cfg.getTemplate(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCharacter(CharacterContainer newcharacter) {
		character = newcharacter;
	}

	public void parseTo(Writer out) {
		if( template == null )return;
		if( character == null )return;
		Map<String,Object> node;
		Map<String,Map<String,Object>> root = new TreeMap<String,Map<String,Object>>();

		node = new TreeMap<String,Object>();
		for( Entry<String,String> text : PROPERTIES.getTranslationTextAll().entrySet() ) {
			node.put(text.getKey(),text.getValue());
		}
		root.put("TEXT", node);

		node = new TreeMap<String,Object>();
		Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = character.getAttributes();
		int spendAttributeBuyPoints=0;
		for( ATTRIBUTENameType a : attributes.keySet() ) {
			Map<String,Object> values = new TreeMap<String,Object>();
			node.put(a.value(), values);
			ATTRIBUTEType attribute = attributes.get(a);
			values.put( "name", attributesNames.get(a) );
			values.put( "basevalue", attribute.getBasevalue() );
			values.put( "lpincrease", attribute.getLpincrease() );
			values.put( "currentvalue", attribute.getCurrentvalue() );
			values.put( "step", attribute.getStep() );
			String dice = attribute.getDice();
			values.put( "dice", (dice==null)?"NA":dice );
			spendAttributeBuyPoints+=attribute.getCost();
		}
		node.put("BuyPointsUsed", spendAttributeBuyPoints);
		node.put("BuyPointsMax", PROPERTIES.getOptionalRules().getATTRIBUTE().getPoints());
		root.put("ATTRIBUTE", node);

		node = new TreeMap<String,Object>();
		APPEARANCEType appearance = character.getAppearance();
		node.put( "race", appearance.getRace() );
		node.put( "origin", appearance.getOrigin() );
		node.put( "age", appearance.getAge() );
		node.put( "gender", appearance.getGender().value() );
		node.put( "weight", appearance.getWeight() );
		node.put( "eyes", appearance.getEyes() );
		node.put( "hair", appearance.getHair() );
		node.put( "height", appearance.getHeight() );
		node.put( "skin", appearance.getSkin() );
		root.put("APPEARANCE", node);

		node = new TreeMap<String,Object>();
		DefenseAbility defences = character.getDefence();
		node.put( "physical", defences.get(EffectlayerType.PHYSICAL) );
		node.put( "spell", defences.get(EffectlayerType.MYSTIC) );
		node.put( "social", defences.get(EffectlayerType.SOCIAL) );
		root.put("DEFENSE", node);

		node = new TreeMap<String,Object>();
		PROTECTIONType protection = character.getProtection();
		node.put( "physicalarmor", protection.getPhysicalarmor() );
		node.put( "mysticarmor", protection.getMysticarmor() );
		root.put("PROTECTION", node);

		node = new TreeMap<String,Object>();
		INITIATIVEType initiative = character.getInitiative();
		node.put( "base", initiative.getBase() );
		node.put( "modification", initiative.getModification() );
		node.put( "step", initiative.getStep() );
		String initiativedice = initiative.getDice();
		if( initiativedice == null ) node.put( "dice", "-" );
		else node.put( "dice", initiativedice );
		root.put("INITIATIVE", node);

		node = new TreeMap<String,Object>();
		MOVEMENTType movement = character.getMovement();
		node.put( "ground", movement.getGroundString() );
		node.put( "flight", movement.getFlightString() );
		root.put("MOVEMENT", node);

		node = new TreeMap<String,Object>();
		node.put( "carrying", character.getCarrying().getCarrying() );
		root.put("CARRYING", node);

		node = new TreeMap<String,Object>();
		KARMAType karma = character.getKarma();
		node.put( "current", karma.getCurrent() );
		node.put( "max", karma.getMax() );
		node.put( "step", karma.getStep() );
		node.put( "dice", karma.getDice() );
		node.put( "maxmodificator", karma.getMaxmodificator() );
		root.put("KARMA", node);

		node = new TreeMap<String,Object>();
		EXPERIENCEType legendPoints = character.getLegendPoints();
		node.put( "renown", legendPoints.getRenown() );
		node.put( "reputation", legendPoints.getReputation() );
		node.put( "totallegendpoints", legendPoints.getTotallegendpoints() );
		node.put( "currentlegendpoints", legendPoints.getCurrentlegendpoints() );
		root.put("EXPERIENCE", node);

		node = new TreeMap<String,Object>();
		String passion=character.getPassion();
		if( passion==null || passion.isEmpty() ) {
			node.put( "passion", "-" );
		} else {
			node.put( "passion", passion );
		}
		DEVOTIONType devotionPoints = character.getDevotionPoints();
		if( devotionPoints == null ) {
			node.put( "value", "-" );
		} else {
			node.put( "value", devotionPoints.getValue() );
		}
		root.put("DEVOTION", node);

		node = new TreeMap<String,Object>();
		CALCULATEDLEGENDPOINTSType calculatedLegendpoints = character.getCalculatedLegendpoints();
		node.put( "unused", (legendPoints.getTotallegendpoints()-calculatedLegendpoints.getTotal()) );
		node.put( "total", calculatedLegendpoints.getTotal() );
		node.put( "attributes", calculatedLegendpoints.getAttributes() );
		node.put( "disciplinetalents", calculatedLegendpoints.getDisciplinetalents() );
		node.put( "optionaltalents", calculatedLegendpoints.getOptionaltalents() );
		node.put( "knacks", calculatedLegendpoints.getKnacks() );
		node.put( "spells", calculatedLegendpoints.getSpells() );
		node.put( "skills", calculatedLegendpoints.getSkills() );
		node.put( "karma", calculatedLegendpoints.getKarma() );
		node.put( "magicitems", calculatedLegendpoints.getMagicitems() );
		root.put("CALCULATEDLEGENDPOINTS", node);

		node = new TreeMap<String,Object>();
		USEDSTARTRANKSType usedStartranks = calculatedLegendpoints.getUSEDSTARTRANKS();
		node.put( "skills", -usedStartranks.getSkills() );
		node.put( "talents", -usedStartranks.getTalents() );
		node.put( "spells", -usedStartranks.getSpells() );
		root.put("USEDSTARTRANKS", node);

		node = new TreeMap<String,Object>();
		node.put( "damage", character.getHealth().getDamage() );
		node.put( "blooddamage", character.getHealth().getBlooddamage() );
		node.put( "depatterningrate", character.getHealth().getDepatterningrate() );
		root.put("HEALTH", node);

		node = new TreeMap<String,Object>();
		for( Entry<String,String> text : PROPERTIES.getTranslationHealthAll().entrySet() ) {
			node.put(text.getKey(),text.getValue());
		}
		root.get("HEALTH").put("TEXT", node);

		node = new TreeMap<String,Object>();
		DEATHType death = character.getHealth().getUNCONSCIOUSNESS();
		node.put( "adjustment" , death.getAdjustment() );
		node.put( "base" , death.getBase() );
		node.put( "value" , death.getValue() );
		root.get("HEALTH").put("UNCONSCIOUSNESS", node);

		node = new TreeMap<String,Object>();
		death = character.getHealth().getDEATH();
		node.put( "adjustment" , death.getAdjustment() );
		node.put( "base" , death.getBase() );
		node.put( "value" , death.getValue() );
		root.get("HEALTH").put("DEATH", node);

		node = new TreeMap<String,Object>();
		RECOVERYType recov = character.getHealth().getRECOVERY();
		node.put( "dice" , recov.getDice() );
		node.put( "step" , recov.getStep() );
		node.put( "testsperday" , recov.getTestsperday() );
		root.get("HEALTH").put("RECOVERY", node);

		node = new TreeMap<String,Object>();
		WOUNDType wounds = character.getHealth().getWOUNDS();
		node.put( "blood" , wounds.getBlood());
		node.put( "normal" , wounds.getNormal() );
		node.put( "penalties" , wounds.getPenalties() );
		node.put( "threshold" , wounds.getThreshold() );
		root.get("HEALTH").put("WOUNDS", node);

		try {
			template.process(root, out);
			out.flush();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
