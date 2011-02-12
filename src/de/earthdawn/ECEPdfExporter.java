package de.earthdawn;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import de.earthdawn.data.ARMORType;
import de.earthdawn.data.ATTRIBUTEType;
import de.earthdawn.data.COINSType;
import de.earthdawn.data.DISCIPLINEBONUSType;
import de.earthdawn.data.DISCIPLINEType;
import de.earthdawn.data.EDCHARACTER;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.MOVEMENTType;
import de.earthdawn.data.PROTECTIONType;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.SKILLType;
import de.earthdawn.data.SPELLSType;
import de.earthdawn.data.SPELLType;
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
		acroFields.setField( "AttributeBase.0", String.valueOf(attributes.get("DEX").getBasevalue()) );
		acroFields.setField( "AttributeBase.1", String.valueOf(attributes.get("STR").getBasevalue()) );
		acroFields.setField( "AttributeBase.2", String.valueOf(attributes.get("TOU").getBasevalue()) );
		acroFields.setField( "AttributeBase.3", String.valueOf(attributes.get("PER").getBasevalue()) );
		acroFields.setField( "AttributeBase.4", String.valueOf(attributes.get("WIL").getBasevalue()) );
		acroFields.setField( "AttributeBase.5", String.valueOf(attributes.get("CHA").getBasevalue()) );
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
		if( character.getHealth().getDamage() > 0 ) {
			acroFields.setField( "CurrentDamage", String.valueOf(character.getHealth().getDamage()) );
		} else {
			acroFields.setField( "CurrentDamage",  "" );
		}
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
			if( ! armor.getUsed().equals(YesnoType.YES) ) continue;
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
		int counterKarmaritual=0;
		for( int order : disciplineOrder ) {
			if( disciplinename == null ) {
				disciplinename = "";
				disciplinecircle = "";
				numberOfFristDiszipline=order;
			} else {
				disciplinename += " / ";
				disciplinecircle += " / ";
			}
			DISCIPLINEType discipline = diciplines.get(order);
			disciplinename += discipline.getName();
			disciplinecircle += String.valueOf(discipline.getCircle());
			for( String description : wrapString(40,discipline.getKARMARITUAL()) ) {
				if( counterKarmaritual > 11 ) {
					System.err.println("Karmaritual description is to long. Only first 12 lines were displayed.");
					break;
				}
				acroFields.setField( "KarmaRitual."+counterKarmaritual, description );
				counterKarmaritual++;
			}
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
			for( TALENTType talent : talents.getDISZIPLINETALENT() ) {
				int counter = 66;
				if( talent.getCircle()>12 ) {
					counter = counterDisciplinetalent_master++;
				} else if( talent.getCircle()>8 ) {
					counter = counterDisciplinetalent_warden++;
				} else if( talent.getCircle()>4 ) {
					counter = counterDisciplinetalent_journayman++;
				} else {
					counter = counterDisciplinetalent_novice++;
				}
				setTalent(acroFields, counter, talent);
			}
			for( TALENTType talent : talents.getOPTIONALTALENT() ) {
				int counter = 66;
				if( talent.getCircle()>8 ) {
					counter = counterOthertalent_warden++;
				} else if( talent.getCircle()>4 ) {
					counter = counterOthertalent_journayman++;
				} else {
					counter = counterOthertalent_novice++;
				}
				setTalent(acroFields, counter, talent);
			}
		}
		List<SKILLType> skills = character.getSkills();
		if( skills != null ) {
			int counter = 0;
			for( SKILLType skill : skills ) {
				RANKType skillrank = skill.getRANK();
				// Skills die keinen Rank haben sollen nicht angezeigt werden.
				if( skillrank.getRank() < 1 ) continue;
				if( skill.getLimitation().isEmpty() ) {
					acroFields.setField( "Skill."+counter, skill.getName());
				} else {
					acroFields.setField( "Skill."+counter, skill.getName()+": "+skill.getLimitation());
				}
				acroFields.setField( "SkillAttribute."+counter, skill.getAttribute().value() );
				acroFields.setField( "SkillStrain."+counter, String.valueOf(skill.getStrain()) );
				switch( skill.getAction() ) {
				case STANDARD  : acroFields.setField( "SkillAction."+counter, "std" ); break;
				case SIMPLE    : acroFields.setField( "SkillAction."+counter, "smpl" ); break;
				case SUSTAINED : acroFields.setField( "SkillAction."+counter, "sstnd" ); break;
				default        : acroFields.setField( "SkillAction."+counter, skill.getAction().value() );
				}
				acroFields.setField( "SkillActionDice."+counter, skillrank.getDice().value() );
				acroFields.setField( "SkillStep."+counter, String.valueOf(skillrank.getStep()) );
				if( skillrank.getBonus() == 0 ) {
					acroFields.setField( "SkillRank."+counter, String.valueOf(skillrank.getRank()) );
				} else {
					acroFields.setField( "SkillRank."+counter, skillrank.getRank()+"+"+skillrank.getBonus() );
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

		int copperPieces = 0;
		int goldPieces = 0;
		int silverPieces = 0;
		for( COINSType coin : character.getAllCoins() ) {
			copperPieces += coin.getCopper();
			silverPieces += coin.getSilver();
			goldPieces += coin.getGold();
		}
		acroFields.setField( "CopperPieces", String.valueOf(copperPieces) );
		acroFields.setField( "SilverPieces", String.valueOf(silverPieces) );
		acroFields.setField( "GoldPieces", String.valueOf(goldPieces) );

		int conterSpells=0;
		for( SPELLSType spells : character.getAllSpells() ) {
			for( SPELLType spell : spells.getSPELL() ) {
				acroFields.setField( "SpellName."+conterSpells, spell.getName() );
				if( spell.getInmatrix().equals(YesnoType.YES)) {
					acroFields.setField( "InMatrix."+conterSpells, "Yes" );
				} else {
					acroFields.setField( "InMatrix."+conterSpells, "" );
				}
				switch( spell.getType() ) {
				case ELEMENTAL: acroFields.setField( "SpellType."+conterSpells, "ele" ); break;
				case ILLUSION:  acroFields.setField( "SpellType."+conterSpells, "illu" ); break;
				case NETHER:    acroFields.setField( "SpellType."+conterSpells, "neth" ); break;
				case SHAMANE:   acroFields.setField( "SpellType."+conterSpells, "sham" ); break;
				case WIZARD:    acroFields.setField( "SpellType."+conterSpells, "wiz" ); break;
				default:   acroFields.setField( "SpellType."+conterSpells, "" ); break;
				}
				acroFields.setField( "SpellCircle."+conterSpells, String.valueOf(spell.getCircle()) );
				if( spell.getThreads() >= 0 ) {
					acroFields.setField( "SpellThreads."+conterSpells, String.valueOf(spell.getThreads()) );
				} else {
					acroFields.setField( "SpellThreads."+conterSpells, "s. text" );
				}
				acroFields.setField( "WeavingDifficulty."+conterSpells, spell.getWeavingdifficulty()+"/"+spell.getReattuningdifficulty() );
				acroFields.setField( "CastingDifficulty."+conterSpells, spell.getCastingdifficulty() );
				acroFields.setField( "SpellRange."+conterSpells, spell.getRange() );
				acroFields.setField( "Duration."+conterSpells, spell.getDuration() );
				acroFields.setField( "Effect."+conterSpells, spell.getEffect() );
				conterSpells++;
			}
		}
		int counterEquipment=0;
		int rowEquipment=0;
		for( ITEMType item : character.getItems() ) {
			acroFields.setField( "Equipment."+counterEquipment+"."+rowEquipment, item.getName() );
			acroFields.setField( "Weight."+counterEquipment+"."+rowEquipment, String.valueOf(item.getWeight()) );
			if( counterEquipment > 33 ) {
				counterEquipment=0;
				if( rowEquipment == 1 ) {
					break;
				} else rowEquipment=1;
			} else {
				counterEquipment++;
			}
		}

		int counterDescription=0;
		for( String description : wrapString(40,character.getDESCRIPTION()) ) {
			acroFields.setField( "ShortDescription."+counterDescription, description );
			counterDescription++;
			if( counterDescription > 7 ) {
				System.err.println("Character description to long. Only first 8 lines were displayed.");
				break;
			}
		}

		stamper.close();
	}

	private void setTalent(AcroFields acroFields, int counter, TALENTType talent) throws DocumentException, IOException {
		if ( talent.getLimitation().isEmpty() ) {
			acroFields.setField( "Talent."+counter, talent.getName());
		} else {
			acroFields.setField( "Talent."+counter, talent.getName()+": "+talent.getLimitation());
			
		}
		acroFields.setField( "Attribute."+counter, talent.getAttribute().value() );
		acroFields.setField( "Strain."+counter, String.valueOf(talent.getStrain()) );
		switch( talent.getAction() ) {
		case STANDARD  : acroFields.setField( "Action."+counter, "std" ); break;
		case SIMPLE    : acroFields.setField( "Action."+counter, "smpl" ); break;
		case SUSTAINED : acroFields.setField( "Action."+counter, "sust" ); break;
		default        : acroFields.setField( "Action."+counter, talent.getAction().value() );
		}
		RANKType talentrank = talent.getRANK();
		if( talentrank.getDice() == null ) {
			acroFields.setField( "ActionDice."+counter, "-" );
		} else {
			acroFields.setField( "ActionDice."+counter, talentrank.getDice().value() );
		}
		acroFields.setField( "Step."+counter, String.valueOf(talentrank.getStep()) );
		if( talentrank.getBonus() == 0 ) {
			acroFields.setField( "Rank."+counter, String.valueOf(talentrank.getRank()) );
		} else {
			acroFields.setField( "Rank."+counter, talentrank.getRank()+"+"+talentrank.getBonus() );
		}
		if( counter > 20) {
			if( talent.getKarma().equals(YesnoType.YES)) {
				acroFields.setField( "KarmaRequired."+(counter-20), "Yes" );
			} else {
				acroFields.setField( "KarmaRequired."+(counter-20), "" );
			}
		}

	}

	public static List<String> wrapString(int maxLength, String string) {
		String wrapChar = " -	"; // Liste von Zeichen wo ein Umbruch erlaubt ist
		List<String> result = new ArrayList<String>();
		if( string == null ) return result;
		while(string.length() > maxLength) {
			// Durchlaufe die Schleife solange wie der Eingabestring größer als die maximale Umbruchlänge ist
			int pos = maxLength;
			while( (pos>10) && (wrapChar.indexOf(string.charAt(pos)) < 0) ) {
				pos--; // Wenn auf der maximal zulässigen Stringlänge kein Umbruchzeichen ist suche eins davor, usw.
			}
			if( pos <= 10 ) pos= maxLength; // Wenn bei kleiner 10 immer noch kein Umbruch gefunden, dann breche bei der Maximalen Länge auch ohne Umbruchzeichen um
			result.add(string.substring(0, pos));
			string = string.substring(pos);
		}
		if( ! string.isEmpty() ) {
			result.add(string);
		}
		return result;
	}
}
