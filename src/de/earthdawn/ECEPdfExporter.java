package de.earthdawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import de.earthdawn.data.ARMORType;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.DISCIPLINEBONUSType;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.MOVEMENTType;
import de.earthdawn.data.PROTECTIONType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.SKILLType;
import de.earthdawn.data.TALENTSType;
import de.earthdawn.data.TALENTType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.YesnoType;

public class ECEPdfExporter {

	public void export(EDCHARACTER edCharakter, File outFile) throws DocumentException, IOException {
		//PdfReader reader = new PdfReader(new FileInputStream(new File("./config/ed3_character_sheet.pdf")));
		PdfReader reader = new PdfReader(new FileInputStream(new File("./config/ed3_extended_character_sheet.pdf")));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		AcroFields acroFields = stamper.getAcroFields();
		CharacterContainer character = new CharacterContainer(edCharakter);
// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
		//Set<String> fieldNames = fields.keySet();
		//fieldNames = new TreeSet<String>(fieldNames);
		//for( String fieldName : fieldNames ) {
		//	System.out.println( fieldName );
		//}
// +++ ~DEBUG ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		acroFields.setField( "Name", character.getName());
		acroFields.setField( "Race", character.getAppearance().getRace() );
		acroFields.setField( "Age", String.valueOf(character.getAppearance().getAge()) );
		acroFields.setField( "Eyes", character.getAppearance().getEyes() );
		acroFields.setField( "Gender", character.getAppearance().getGender().value() );
		acroFields.setField( "Hair", character.getAppearance().getHair() );
		acroFields.setField( "Height", String.valueOf(character.getAppearance().getHeight()) );
		acroFields.setField( "Skin", character.getAppearance().getSkin() );
		acroFields.setField( "CharacterWeight" , String.valueOf(character.getAppearance().getWeight()) );
		HashMap<String, ATTRIBUTEType> attributes = character.getAttributes();
		acroFields.setField( "AttributeBase.0", String.valueOf(attributes.get("DEX").getCurrentvalue()-attributes.get("DEX").getLpincrease()) );
		acroFields.setField( "AttributeBase.1", String.valueOf(attributes.get("STR").getCurrentvalue()-attributes.get("STR").getLpincrease()) );
		acroFields.setField( "AttributeBase.2", String.valueOf(attributes.get("TOU").getCurrentvalue()-attributes.get("TOU").getLpincrease()) );
		acroFields.setField( "AttributeBase.3", String.valueOf(attributes.get("PER").getCurrentvalue()-attributes.get("PER").getLpincrease()) );
		acroFields.setField( "AttributeBase.4", String.valueOf(attributes.get("WIL").getCurrentvalue()-attributes.get("WIL").getLpincrease()) );
		acroFields.setField( "AttributeBase.5", String.valueOf(attributes.get("CHA").getCurrentvalue()-attributes.get("CHA").getLpincrease()) );
		acroFields.setField( "LPIncrease.0", String.valueOf(attributes.get("DEX").getLpincrease()) );
		acroFields.setField( "LPIncrease.1", String.valueOf(attributes.get("STR").getLpincrease()) );
		acroFields.setField( "LPIncrease.2", String.valueOf(attributes.get("TOU").getLpincrease()) );
		acroFields.setField( "LPIncrease.3", String.valueOf(attributes.get("PER").getLpincrease()) );
		acroFields.setField( "LPIncrease.4", String.valueOf(attributes.get("WIL").getLpincrease()) );
		acroFields.setField( "LPIncrease.5", String.valueOf(attributes.get("CHA").getLpincrease()) );
		acroFields.setField( "AttributeCurrent.0", String.valueOf(attributes.get("DEX").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.1", String.valueOf(attributes.get("STR").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.2", String.valueOf(attributes.get("TOU").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.3", String.valueOf(attributes.get("PER").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.4", String.valueOf(attributes.get("WIL").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.5", String.valueOf(attributes.get("CHA").getCurrentvalue()) );
		acroFields.setField( "AttributeStep.0", String.valueOf(attributes.get("DEX").getStep()) );
		acroFields.setField( "AttributeStep.1", String.valueOf(attributes.get("STR").getStep()) );
		acroFields.setField( "AttributeStep.2", String.valueOf(attributes.get("TOU").getStep()) );
		acroFields.setField( "AttributeStep.3", String.valueOf(attributes.get("PER").getStep()) );
		acroFields.setField( "AttributeStep.4", String.valueOf(attributes.get("WIL").getStep()) );
		acroFields.setField( "AttributeStep.5", String.valueOf(attributes.get("CHA").getStep()) );
		acroFields.setField( "AttributeDice.0", attributes.get("DEX").getDice().value() );
		acroFields.setField( "AttributeDice.1", attributes.get("STR").getDice().value() );
		acroFields.setField( "AttributeDice.2", attributes.get("TOU").getDice().value() );
		acroFields.setField( "AttributeDice.3", attributes.get("PER").getDice().value() );
		acroFields.setField( "AttributeDice.4", attributes.get("WIL").getDice().value() );
		acroFields.setField( "AttributeDice.5", attributes.get("CHA").getDice().value() );
		acroFields.setField( "DefensePhysical", String.valueOf(character.getDefence().getPhysical()) );
		acroFields.setField( "DefenseSocial", String.valueOf(character.getDefence().getSocial()) );
		acroFields.setField( "DefenseSpell", String.valueOf(character.getDefence().getSpell()) );
		acroFields.setField( "InitiativeDice", character.getInitiative().getDice().value() );
		acroFields.setField( "InitiativeStep", String.valueOf(character.getInitiative().getStep()) );
		acroFields.setField( "DeathAdjustment", String.valueOf(character.getDeath().getAdjustment()) );
		acroFields.setField( "DeathBase", String.valueOf(character.getDeath().getBase()) );
		acroFields.setField( "DeathValue", String.valueOf(character.getDeath().getValue()) );
		acroFields.setField( "UnconsciousnessAdjustment", String.valueOf(character.getUnconsciousness().getAdjustment()) );
		acroFields.setField( "UnconsciousnessBase", String.valueOf(character.getUnconsciousness().getBase()) );
		acroFields.setField( "UnconsciousnessValue", String.valueOf(character.getUnconsciousness().getValue()) );
		acroFields.setField( "Recovery Step", String.valueOf(character.getRecovery().getStep()) );
		acroFields.setField( "RecoveryDice", character.getRecovery().getDice().value() );
		acroFields.setField( "RecoveryTestsPerDay",  String.valueOf(character.getRecovery().getTestsperday()) );
		acroFields.setField( "WoundThreshold", String.valueOf(character.getWound().getThreshold()) );
		acroFields.setField( "CurrentDamage",  "" );
		acroFields.setField( "KarmaCurrent", String.valueOf(character.getKarma().getCurrent()) );
		acroFields.setField( "KarmaMax", String.valueOf(character.getKarma().getMax()) );
		MOVEMENTType movement = character.getMovement();
		if( movement.getFlight()>0 ) {
			acroFields.setField( "MovementRate", movement.getGround() +"/"+ movement.getFlight() );
		} else {
			acroFields.setField( "MovementRate", String.valueOf(movement.getGround()) );
		}
		acroFields.setField( "CarryingCapacity", String.valueOf(character.getCarrying().getCarrying()) );
		PROTECTIONType protection = character.getProtection();
		acroFields.setField( "Mystic Armor", String.valueOf(protection.getMysticarmor()) );
		acroFields.setField( "Physical Armor", String.valueOf(protection.getPhysicalarmor()) );
		acroFields.setField( "Shield", "none" );
		acroFields.setField( "ShieldDeflectionBonus", "na" );
		int armor_max=0;
		int shield_max=0;
		for (ARMORType armor : protection.getARMOROrSHIELD() ) {
			if( ! armor.getUsed().equals(YesnoType.YES) ) { continue; }
			if( armor.getClass().getSimpleName().equals("ARMORType") ) {
				if( armor.getPhysicalarmor()>armor_max ) {
					armor_max=armor.getPhysicalarmor();
					acroFields.setField( "Armor" , armor.getName() );
				}
			} else if( armor.getClass().getSimpleName().equals("SHIELDType") ) {
				if( armor.getPhysicalarmor()>shield_max ) {
					shield_max=armor.getPhysicalarmor();
					acroFields.setField( "Shield", armor.getName() );
					acroFields.setField( "ShieldDeflectionBonus", ((SHIELDType)armor).getDeflectionbonus() );
				}
			} else {
				System.err.println( "Unbekannte Rüstungstyp: "+armor.getClass().getSimpleName() );
			}
		}
		acroFields.setField( "RacialAbilities.0", character.getAbilities() );
		HashMap<Integer,DISCIPLINEType> diciplines = character.getAllDiciplinesByOrder();
		String disciplinecircle = null;
		String disciplinename = null;
		Set<Integer> disciplineOrder = diciplines.keySet();
		disciplineOrder = new TreeSet<Integer>(disciplineOrder);
		int numberOfFristDiszipline=0;
		for( int order : disciplineOrder ) {
			if( disciplinename == null ) {
				disciplinename = "";
				disciplinecircle = "";
				numberOfFristDiszipline=order;
			} else {
				disciplinename += " / ";
				disciplinecircle += " / ";
			}
			disciplinename += diciplines.get(order).getName();
			disciplinecircle += String.valueOf(diciplines.get(order).getCircle());
		}
		acroFields.setField( "Discipline", disciplinename );
		acroFields.setField( "Circle", disciplinecircle );
		HashMap<Integer, TALENTSType> allTalents = character.getAllTalentsByDisziplinOrder();
		TALENTSType talents = allTalents.get(numberOfFristDiszipline);
		if( talents != null ) {
			int counterDisciplinetalent_novice=0;
			int counterDisciplinetalent_journayman=9;
			int counterDisciplinetalent_warden=13;
			int counterDisciplinetalent_master=17;
			int counterOthertalent_novice=20;
			int counterOthertalent_journayman=27;
			int counterOthertalent_warden=33;
			for( JAXBElement<TALENTType> element : talents.getDISZIPLINETALENTOrOPTIONALTALENT() ) {
				TALENTType talent = element.getValue();
				int counter = 66;
				if( element.getName().getLocalPart().equals("DISZIPLINETALENT") ) {
					if( talent.getCircle()>12 ) {
						counter = counterDisciplinetalent_master++;
					} else if( talent.getCircle()>8 ) {
						counter = counterDisciplinetalent_warden++;
					} else if( talent.getCircle()>4 ) {
						counter = counterDisciplinetalent_journayman++;
					} else {
						counter = counterDisciplinetalent_novice++;
					}
				} else if( element.getName().getLocalPart().equals("OPTIONALTALENT") ) {
					if( talent.getCircle()>8 ) {
						counter = counterOthertalent_warden++;
					} else if( talent.getCircle()>4 ) {
						counter = counterOthertalent_journayman++;
					} else {
						counter = counterOthertalent_novice++;
					}
				} else {
					System.err.println( "Unbekannte Talentstyp: "+element.getName().getLocalPart() );
				}
				if ( talent.getLimitation().isEmpty() ) {
					acroFields.setField( "Talent."+counter, talent.getName());
				} else {
					acroFields.setField( "Talent."+counter, talent.getName()+": "+talent.getLimitation());
					
				}
				acroFields.setField( "ActionDice."+counter, talent.getRANK().getDice().value() );
				acroFields.setField( "Attribute."+counter, talent.getAttribute().value() );
				acroFields.setField( "Step."+counter, String.valueOf(talent.getRANK().getStep()) );
				acroFields.setField( "Rank."+counter, String.valueOf(talent.getRANK().getRank()) );
				acroFields.setField( "Strain."+counter, String.valueOf(talent.getStrain()) );
				switch( talent.getAction() ) {
				case STANDARD  : acroFields.setField( "Action."+counter, "std" ); break;
				case SIMPLE    : acroFields.setField( "Action."+counter, "smpl" ); break;
				case SUSTAINED : acroFields.setField( "Action."+counter, "sust" ); break;
				default        : acroFields.setField( "Action."+counter, talent.getAction().value() );
				}
				if( counter > 20) {
					if( talent.getKarma().equals(YesnoType.YES)) {
						acroFields.setField( "KarmaRequired."+(counter-20), "Yes" );
					} else {
						acroFields.setField( "KarmaRequired."+(counter-20), "" );
					}
				}
			}
		}
		List<SKILLType> skills = character.getSkills();
		if( skills != null ) {
			int counter = 0;
			for( SKILLType skill : skills ) {
				if( skill.getLimitation().isEmpty() ) {
					acroFields.setField( "Skill."+counter, skill.getName());
				} else {
					acroFields.setField( "Skill."+counter, skill.getName()+": "+skill.getLimitation());
				}
				acroFields.setField( "SkillActionDice."+counter, skill.getRANK().getDice().value() );
				acroFields.setField( "SkillAttribute."+counter, skill.getAttribute().value() );
				acroFields.setField( "SkillStep."+counter, String.valueOf(skill.getRANK().getStep()) );
				acroFields.setField( "SkillRank."+counter, String.valueOf(skill.getRANK().getRank()) );
				acroFields.setField( "SkillStrain."+counter, String.valueOf(skill.getStrain()) );
				switch( skill.getAction() ) {
				case STANDARD  : acroFields.setField( "SkillAction."+counter, "std" ); break;
				case SIMPLE    : acroFields.setField( "SkillAction."+counter, "smpl" ); break;
				case SUSTAINED : acroFields.setField( "SkillAction."+counter, "sstnd" ); break;
				default        : acroFields.setField( "SkillAction."+counter, skill.getAction().value() );
				}
				counter++;
			}
		}
		List<WEAPONType> weapons = character.getWeapons();
		if( weapons != null ) {
			int counter_melee = 0;
			int counter_range = 0;
			for( WEAPONType weapon : weapons ) {
				if( weapon.getShortrange() > 0 ) {
					acroFields.setField( "RangedWeapon."+counter_range, weapon.getName() );
					// TODO: Bug im PDF: Position "3" hat ein Leerzeichen "Ranged Weapon.3"
					acroFields.setField( "RangedWeaponDmgStep."+counter_range, String.valueOf(weapon.getDamagestep()) );
					acroFields.setField( "RangedWeapon Size."+counter_range, String.valueOf(weapon.getSize()) );
					acroFields.setField( "RangedWeaponTimesForged."+counter_range, String.valueOf(weapon.getTimesforged()) );
					acroFields.setField( "WeaponShortRange."+counter_range, String.valueOf(weapon.getShortrange()) );
					acroFields.setField( "Weapon Long Range."+counter_range, String.valueOf(weapon.getLongrange()) );
					counter_range++;
				} else {
					acroFields.setField( "Weapon."+counter_melee, weapon.getName() );
					acroFields.setField( "WeaponDmgStep."+counter_melee, String.valueOf(weapon.getDamagestep()) );
					acroFields.setField( "Weapon Size."+counter_melee, String.valueOf(weapon.getSize()) );
					acroFields.setField( "WeaponTimesForged."+counter_melee, String.valueOf(weapon.getTimesforged()) );
					counter_melee++;
				}
			}
		}

		acroFields.setField( "TotalLegendPoints", String.valueOf(character.getLegendPoints().getTotallegendpoints()) );
		acroFields.setField( "CurrentLegendPoints", String.valueOf(character.getLegendPoints().getCurrentlegendpoints()) );
		acroFields.setField( "Renown", String.valueOf(character.getLegendPoints().getRenown()) );
		acroFields.setField( "Reputation", character.getLegendPoints().getReputation() );

		List<DISCIPLINEBONUSType> bonuses = character.getDisciplineBonuses();
		if( bonuses != null ) {
			int counter=0;
			for( DISCIPLINEBONUSType bonus : bonuses ) {
				acroFields.setField("DiscBonusAbility."+counter, bonus.getBonus() );
				if( bonus.getCircle() > 1 ) {
					acroFields.setField("DiscBonusCircle."+counter, String.valueOf(bonus.getCircle()) );
				} else {
					acroFields.setField("DiscBonusCircle."+counter, "-" );
				}
				counter++;
			}
		}
		
		acroFields.setField( "CopperPieces",  "" );
		acroFields.setField( "GoldPieces",  "" );
		acroFields.setField( "SilverPieces",  "" );

		stamper.close();
	}
}
