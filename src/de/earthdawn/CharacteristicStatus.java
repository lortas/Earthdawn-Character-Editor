package de.earthdawn;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.DEATHType;
import de.earthdawn.data.RECOVERYType;
import de.earthdawn.data.WOUNDType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CharacteristicStatus {

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
		Map<String,Map<String,Object>> root = new HashMap<String,Map<String,Object>>();

		node = new HashMap<String,Object>();
		HashMap<String, ATTRIBUTEType> attributes = character.getAttributes();
		for( String a : attributes.keySet() ) {
			Map<String,Object> values = new HashMap<String,Object>();
			node.put(a, values);
			values.put( "basevalue", attributes.get(a).getBasevalue() );
			values.put( "lpincrease", attributes.get(a).getLpincrease() );
			values.put( "currentvalue", attributes.get(a).getCurrentvalue() );
			values.put( "step", attributes.get(a).getStep() );
			values.put( "dice", attributes.get(a).getDice().value() );
		}
		root.put("ATTRIBUTE", node);

		node = new HashMap<String,Object>();
		node.put( "physical", character.getDefence().getPhysical() );
		node.put( "spell", character.getDefence().getSpell() );
		node.put( "social", character.getDefence().getSocial() );
		root.put("DEFENSE", node);

		node = new HashMap<String,Object>();
		node.put( "physicalarmor", character.getProtection().getPhysicalarmor() );
		node.put( "mysticarmor", character.getProtection().getMysticarmor() );
		root.put("PROTECTION", node);

		node = new HashMap<String,Object>();
		node.put( "base", character.getInitiative().getBase() );
		node.put( "modification", character.getInitiative().getModification() );
		node.put( "step", character.getInitiative().getStep() );
		node.put( "dice", character.getInitiative().getDice().value() );
		root.put("INITIATIVE", node);

		node = new HashMap<String,Object>();
		node.put( "ground", character.getMovement().getGround() );
		node.put( "flight", character.getMovement().getFlight() );
		root.put("MOVEMENT", node);

		node = new HashMap<String,Object>();
		node.put( "carrying", character.getCarrying().getCarrying() );
		root.put("CARRYING", node);

		node = new HashMap<String,Object>();
		node.put( "current", character.getKarma().getCurrent() );
		node.put( "max", character.getKarma().getMax() );
		node.put( "step", character.getKarma().getStep() );
		node.put( "dice", character.getKarma().getDice().value() );
		node.put( "maxmodificator", character.getKarma().getMaxmodificator() );
		root.put("KARMA", node);

		node = new HashMap<String,Object>();
		node.put( "renown", character.getLegendPoints().getRenown() );
		node.put( "reputation", character.getLegendPoints().getReputation() );
		node.put( "totallegendpoints", character.getLegendPoints().getTotallegendpoints() );
		node.put( "currentlegendpoints", character.getLegendPoints().getCurrentlegendpoints() );
		root.put("EXPERIENCE", node);

		node = new HashMap<String,Object>();
		node.put( "total", character.getCalculatedLegendpoints().getTotal() );
		node.put( "attributes", character.getCalculatedLegendpoints().getAttributes() );
		node.put( "disciplinetalents", character.getCalculatedLegendpoints().getDisciplinetalents() );
		node.put( "optionaltalents", character.getCalculatedLegendpoints().getOptionaltalents() );
		node.put( "knacks", character.getCalculatedLegendpoints().getKnacks() );
		node.put( "spells", character.getCalculatedLegendpoints().getSpells() );
		node.put( "skills", character.getCalculatedLegendpoints().getSkills() );
		node.put( "karma", character.getCalculatedLegendpoints().getKarma() );
		node.put( "magicitems", character.getCalculatedLegendpoints().getMagicitems() );
		root.put("CALCULATEDLEGENDPOINTS", node);

		node = new HashMap<String,Object>();
		node.put( "damage", character.getHealth().getDamage() );
		root.put("HEALTH", node);

		node = new HashMap<String,Object>();
		DEATHType death = character.getHealth().getUNCONSCIOUSNESS();
		node.put( "adjustment" , death.getAdjustment() );
		node.put( "base" , death.getBase() );
		node.put( "value" , death.getValue() );
		root.get("HEALTH").put("UNCONSCIOUSNESS", node);

		node = new HashMap<String,Object>();
		death = character.getHealth().getDEATH();
		node.put( "adjustment" , death.getAdjustment() );
		node.put( "base" , death.getBase() );
		node.put( "value" , death.getValue() );
		root.get("HEALTH").put("DEATH", node);

		node = new HashMap<String,Object>();
		RECOVERYType recov = character.getHealth().getRECOVERY();
		node.put( "dice" , recov.getDice().value() );
		node.put( "step" , recov.getStep() );
		node.put( "testsperday" , recov.getTestsperday() );
		root.get("HEALTH").put("RECOVERY", node);

		node = new HashMap<String,Object>();
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
