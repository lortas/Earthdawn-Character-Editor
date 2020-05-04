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
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.Image;

import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.CharsheettemplateContainer;
import de.earthdawn.config.CharsheettemplatetalentStack;
import de.earthdawn.data.*;

public class ECEPdfExporter {
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	public static final String[] ARTISAN = PROPERTIES.getArtisanName();
	public static final String[] KNOWLEDGE = PROPERTIES.getKnowledgeName();
	private int counterEquipment=0;
	private AcroFields acroFields = null;
	private static PrintStream errorout = System.err;

	private void exportCommonFields(CharacterContainer character, int maxSkillSpace, int raceAbilitiesLineLength) throws IOException, DocumentException {
		acroFields.setField( "ExportDate", CharacterContainer.getCurrentDateTime());
		acroFields.setField( "Name", character.getName() );
		APPEARANCEType appearance = character.getAppearance();
		if( appearance.getOrigin().isEmpty() ) acroFields.setField( "Race", appearance.getRace() );
		else acroFields.setField( "Race", appearance.getRace()+" ("+appearance.getOrigin()+")" );
		acroFields.setField( "Age", String.valueOf(appearance.getAge()) );
		acroFields.setField( "Eyes", appearance.getEyes() );
		acroFields.setField( "Gender", appearance.getGender().value() );
		acroFields.setField( "Hair", appearance.getHair() );
		acroFields.setField( "Height", appearance.getHeightString() );
		acroFields.setField( "Skin", appearance.getSkin() );
		acroFields.setField( "CharacterWeight" , appearance.getWeightString() );
		acroFields.setField( "Passion" , character.getPassion() );
		acroFields.setField( "PlayerName" , character.getPlayer() );
		Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = character.getAttributes();
		acroFields.setField( "AttributeBase.0", String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getBasevalue()) );
		acroFields.setField( "AttributeBase.1", String.valueOf(attributes.get(ATTRIBUTENameType.STR).getBasevalue()) );
		acroFields.setField( "AttributeBase.2", String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getBasevalue()) );
		acroFields.setField( "AttributeBase.3", String.valueOf(attributes.get(ATTRIBUTENameType.PER).getBasevalue()) );
		acroFields.setField( "AttributeBase.4", String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getBasevalue()) );
		acroFields.setField( "AttributeBase.5", String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getBasevalue()) );
		acroFields.setField( "LPIncrease.0", String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getLpincrease()) );
		acroFields.setField( "LPIncrease.1", String.valueOf(attributes.get(ATTRIBUTENameType.STR).getLpincrease()) );
		acroFields.setField( "LPIncrease.2", String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getLpincrease()) );
		acroFields.setField( "LPIncrease.3", String.valueOf(attributes.get(ATTRIBUTENameType.PER).getLpincrease()) );
		acroFields.setField( "LPIncrease.4", String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getLpincrease()) );
		acroFields.setField( "LPIncrease.5", String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getLpincrease()) );
		setLpincreaseButtons(attributes.get(ATTRIBUTENameType.DEX).getLpincrease(), 0);
		setLpincreaseButtons(attributes.get(ATTRIBUTENameType.STR).getLpincrease(), 1);
		setLpincreaseButtons(attributes.get(ATTRIBUTENameType.TOU).getLpincrease(), 2);
		setLpincreaseButtons(attributes.get(ATTRIBUTENameType.PER).getLpincrease(), 3);
		setLpincreaseButtons(attributes.get(ATTRIBUTENameType.WIL).getLpincrease(), 4);
		setLpincreaseButtons(attributes.get(ATTRIBUTENameType.CHA).getLpincrease(), 5);
		acroFields.setField( "AttributeCurrent.0", String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.1", String.valueOf(attributes.get(ATTRIBUTENameType.STR).getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.2", String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.3", String.valueOf(attributes.get(ATTRIBUTENameType.PER).getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.4", String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.5", String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getCurrentvalue()) );
		acroFields.setField( "AttributeStep.0", String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getStep()) );
		acroFields.setField( "AttributeStep.1", String.valueOf(attributes.get(ATTRIBUTENameType.STR).getStep()) );
		acroFields.setField( "AttributeStep.2", String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getStep()) );
		acroFields.setField( "AttributeStep.3", String.valueOf(attributes.get(ATTRIBUTENameType.PER).getStep()) );
		acroFields.setField( "AttributeStep.4", String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getStep()) );
		acroFields.setField( "AttributeStep.5", String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getStep()) );
		acroFields.setField( "AttributeDice.0", attributes.get(ATTRIBUTENameType.DEX).getDice() );
		acroFields.setField( "AttributeDice.1", attributes.get(ATTRIBUTENameType.STR).getDice() );
		acroFields.setField( "AttributeDice.2", attributes.get(ATTRIBUTENameType.TOU).getDice() );
		acroFields.setField( "AttributeDice.3", attributes.get(ATTRIBUTENameType.PER).getDice() );
		acroFields.setField( "AttributeDice.4", attributes.get(ATTRIBUTENameType.WIL).getDice() );
		acroFields.setField( "AttributeDice.5", attributes.get(ATTRIBUTENameType.CHA).getDice() );
		acroFields.setField( "DefensePhysical", String.valueOf(character.getDefence().get(EffectlayerType.PHYSICAL)) );
		acroFields.setField( "DefenseSocial", String.valueOf(character.getDefence().get(EffectlayerType.SOCIAL)) );
		acroFields.setField( "DefenseSpell", String.valueOf(character.getDefence().get(EffectlayerType.MYSTIC)) );
		acroFields.setField( "DeathAdjustment", String.valueOf(character.getDeath().getAdjustment()) );
		acroFields.setField( "DeathBase", String.valueOf(character.getDeath().getBase()) );
		acroFields.setField( "DeathValue", String.valueOf(character.getDeath().getValue()) );
		acroFields.setField( "UnconsciousnessAdjustment", String.valueOf(character.getUnconsciousness().getAdjustment()) );
		acroFields.setField( "UnconsciousnessBase", String.valueOf(character.getUnconsciousness().getBase()) );
		acroFields.setField( "UnconsciousnessValue", String.valueOf(character.getUnconsciousness().getValue()) );
		acroFields.setField( "Recovery Step", String.valueOf(character.getRecovery().getStep()) );
		acroFields.setField( "RecoveryDice", character.getRecovery().getDice() );
		acroFields.setField( "RecoveryTestsPerDay",  String.valueOf(character.getRecovery().getTestsperday()) );
		acroFields.setField( "WoundThreshold", String.valueOf(character.getWound().getThreshold()) );
		acroFields.setField( "TotalLegendPoints", String.valueOf(character.getLegendPoints().getTotallegendpoints()) );
		acroFields.setField( "CurrentLegendPoints", String.valueOf(character.getLegendPoints().getCurrentlegendpoints()) );
		acroFields.setField( "Renown", String.valueOf(character.getLegendPoints().getRenown()) );
		acroFields.setField( "Reputation", character.getLegendPoints().getReputation() );
		acroFields.setField( "InitiativeDice", character.getInitiative().getDice() );
		String initiativeStep = String.valueOf(character.getInitiative().getStep());
		for( TalentsContainer talents : character.getAllTalents() ) {
			for( TALENTType talent : talents.getAllTalents() ) initiativeStep += initiativeStepOpts(talent);
		}
		for( SKILLType skill : character.getSkills() ) initiativeStep += initiativeStepOpts(skill);

		acroFields.setField( "InitiativeStep", initiativeStep );
		if( character.getHealth().getDamage() > 0 ) {
			acroFields.setField( "CurrentDamage", String.valueOf(character.getHealth().getDamage()) );
		} else {
			acroFields.setField( "CurrentDamage", "" );
		}
		acroFields.setField( "KarmaCurrent", String.valueOf(character.getKarma().getCurrent()) );
		acroFields.setField( "KarmaMax", String.valueOf(character.getKarma().getMax()) );
		acroFields.setField( "MovementRate", character.getMovementAsString() );
		acroFields.setField( "CarryingCapacity", String.valueOf(character.getCarrying().getCarrying()) );
		PROTECTIONType protection = character.getProtection();
		acroFields.setField( "Mystic Armor", String.valueOf(protection.getMysticarmor()) );
		acroFields.setField( "Physical Armor", String.valueOf(protection.getPhysicalarmor()) );
		acroFields.setField( "Penalty Armor", String.valueOf(protection.getPenalty()) );

		int counterBonusAbility=0;
		List<ElementkindType> elements = character.getDisciplinePrimElements();
		if( elements != null ) {
			for( ElementkindType element : elements ) {
				if( element.equals(ElementkindType.UNDEFINED)) continue;
				acroFields.setField("DiscBonusCircle."+counterBonusAbility, "-" );
				acroFields.setField("DiscBonusAbility."+counterBonusAbility, "+2 to all talents, spells, ... relatet to '"+element.value()+"' and -2 to all other" );
				counterBonusAbility++;
			}
		}
		List<DISCIPLINEBONUSType> bonuses = character.getDisciplineBonuses();
		if( bonuses != null ) {
			for( DISCIPLINEBONUSType bonus : bonuses ) {
				acroFields.setField("DiscBonusAbility."+counterBonusAbility, bonus.getBonus() );
				if( bonus.getCircle() > 1 ) {
					acroFields.setField("DiscBonusCircle."+counterBonusAbility, String.valueOf(bonus.getCircle()) );
				} else {
					acroFields.setField("DiscBonusCircle."+counterBonusAbility, "-" );
				}
				counterBonusAbility++;
			}
		}

		List<SKILLType> skills = character.getSkills();
		if( (skills != null) && (! skills.isEmpty()) ) {
			int counter = 0;
			Collections.sort(skills, new SkillComparator());
			for( SKILLType skill : skills ) {
				String skillName=skill.getName();
				if( skill.getLIMITATION().size()>0 ) {
					if( skillName.equals(ARTISAN[0]) ) skillName = ARTISAN[1];
					else if( skillName.equals(KNOWLEDGE[0]) ) skillName = KNOWLEDGE[1];
					skillName += " : "+skill.getLIMITATION().get(0);
				}
				if( ! skill.getBookref().isEmpty() ) skillName += " ["+skill.getBookref()+"]";
				acroFields.setField( "Skill."+counter, skillName);
				acroFields.setField( "SkillStrain."+counter, skill.getStrain() );
				switch( skill.getAction() ) {
				case STANDARD  : acroFields.setField( "SkillAction."+counter, "std" ); break;
				case SIMPLE    : acroFields.setField( "SkillAction."+counter, "smpl" ); break;
				case SUSTAINED : acroFields.setField( "SkillAction."+counter, "sstnd" ); break;
				default        : acroFields.setField( "SkillAction."+counter, skill.getAction().value() );
				}
				RANKType skillrank = skill.getRANK();
				acroFields.setField( "SkillActionDice."+counter, skillrank.getDice() );
				acroFields.setField( "SkillStep."+counter, String.valueOf(skillrank.getStep()) );
				if( skillrank.getBonus() > 0 ) acroFields.setField( "SkillRank."+counter, skillrank.getRank()+"+"+skillrank.getBonus() );
				else if( skillrank.getBonus() < 0 ) acroFields.setField( "SkillRank."+counter, skillrank.getRank()+"-"+(skillrank.getBonus()*-1) );
				else acroFields.setField( "SkillRank."+counter, String.valueOf(skillrank.getRank()) );
				ATTRIBUTENameType attribute = skill.getAttribute();
				acroFields.setField( "SkillAttribute."+counter, attribute.value() );
				if( attribute.equals(ATTRIBUTENameType.NA) ) {
					acroFields.setField( "SkillAttributeStep."+counter, "-" );
				} else {
					acroFields.setField( "SkillAttributeStep."+counter, String.valueOf(attributes.get(attribute).getStep()) );
				}
				counter++;
			}
		}
		acroFields.setField( "RacialAbilities", character.getAbilities() );
		int counterRacialAbilities=0;
		for( String ability : wrapString(raceAbilitiesLineLength, character.getAbilities()) ) {
			acroFields.setField( "RacialAbilities."+counterRacialAbilities, ability );
			counterRacialAbilities++;
		}
	}

	private static String initiativeStepOpts(SKILLType skill) {
		if( ! skill.getIsinitiative().equals(YesnoType.YES) ) return "";
		String initiativeStep = " +"+skill.getRANK().getRank();
		for(String s : skill.getName().split(" ") ) initiativeStep += s.charAt(0);
		return initiativeStep;
	}

	public void exportRedbrickSimple(EDCHARACTER edCharakter, File outFile) throws DocumentException, IOException {
		PdfReader reader = new PdfReader(new FileInputStream(new File("./templates/ed3_character_sheet.pdf")));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		acroFields = stamper.getAcroFields();
		CharacterContainer character = new CharacterContainer(edCharakter);
// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
		//Set<String> fieldNames = acroFields.getFields().keySet();
		//fieldNames = new TreeSet<String>(fieldNames);
		//for( String fieldName : fieldNames ) {
		//	acroFields.setField( fieldName, fieldName );
		//}
// +++ ~DEBUG ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		exportCommonFields(character,16,41);
		acroFields.setField( "Shield", "none" );
		acroFields.setField( "ShieldDeflectionBonus", "na" );
		int armor_max=0;
		int shield_max=0;
		for (ARMORType armor : character.getProtection().getARMOROrSHIELD() ) {
			if( ! armor.getUsed().equals(YesnoType.YES) ) continue;
			if( armor.getClass().getSimpleName().equals("ARMORType") ) {
				if( armor.getPhysicalarmor()>armor_max ) {
					armor_max=armor.getPhysicalarmor();
					acroFields.setField( "Armor" , armor.getName() );
				}
			} else if( armor.getClass().getSimpleName().equals("SHIELDType") ) {
				SHIELDType shield = (SHIELDType)armor;
				if( shield.getPhysicalarmor()>shield_max ) {
					shield_max=armor.getPhysicalarmor();
					acroFields.setField( "Shield", shield.getName() );
					acroFields.setField( "ShieldDeflectionBonus", shield.getPhysicaldeflectionbonus()+"/"+shield.getMysticdeflectionbonus() );
				}
			} else {
				System.err.println( "Unbekannte Rüstungstyp: "+armor.getClass().getSimpleName() );
			}
		}
		acroFields.setField( "Discipline", concat(" / ",character.getDisciplineNames()) );
		acroFields.setField( "Circle", concat(" / ",character.getDisciplineCircles()) );
		int counterKarmaritual=0;
		for( String karmaritual : character.getAllKarmaritual() ) {
			for( String description : wrapString(50,karmaritual) ) {
				if( counterKarmaritual > 11 ) {
					System.err.println("Karmaritual description is to long. Only first 12 lines were displayed.");
					break;
				}
				acroFields.setField( "KarmaRitual."+counterKarmaritual, description );
				counterKarmaritual++;
			}
		}
		List<DISCIPLINEType> disciplines = character.getDisciplines();
		if( disciplines.size()>0 ) {
			DISCIPLINEType discipline1=disciplines.get(0);
			List<TALENTType> disziplinetalents = discipline1.getDISZIPLINETALENT();
			Collections.sort(disziplinetalents, new TalentComparator());
			Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = character.getAttributes();
			int counter = 0;
			for( TALENTType talent : disziplinetalents ) {
				if( (talent.getCircle()>4) && (counter<9) ) {
					counter = 9;
				}
				setSkillOrTalent(getTalentFieldNames(counter), talent, attributes);
				counter++;
			}
			List<TALENTType> optionaltalents = new ArrayList<TALENTType>();
			optionaltalents.addAll(discipline1.getOPTIONALTALENT());
			optionaltalents.addAll(discipline1.getFREETALENT());
			Collections.sort(optionaltalents, new TalentComparator());
			counter = 0;
			for( TALENTType talent : optionaltalents ) {
				if( (talent.getCircle()>4) && (counter<4) ) {
					counter = 4;
				}
				setSkillOrTalent(getTalentFieldNames(13+counter), talent, attributes);
				// Optionale Talente können Karma erfordern
				if( talent.getKarma().equals(YesnoType.YES)) {
					acroFields.setField( "KarmaRequired."+counter, "Yes" );
				} else {
					acroFields.setField( "KarmaRequired."+counter, "" );
				}
				counter++;
			}
		}
		List<WEAPONType> weapons = character.getWeapons();
		if( weapons != null ) {
			int counter = 0;
			for( WEAPONType weapon : weapons ) {
				acroFields.setField( "Weapon."+counter, weapon.getName() );
				acroFields.setField( "WeaponDmgStep."+counter, String.valueOf(weapon.getDamagestep()) );
				acroFields.setField( "Weapon Size."+counter, String.valueOf(weapon.getSize()) );
				acroFields.setField( "WeaponTimesForged."+counter, String.valueOf(weapon.getTimesforged()) );
				acroFields.setField( "WeaponShortRange."+counter, String.valueOf(weapon.getShortrange()) );
				acroFields.setField( "Weapon Long Range."+counter, String.valueOf(weapon.getLongrange()) );
				counter++;
			}
		}

		List<List<SPELLType>> spellslist = new ArrayList<List<SPELLType>>();
		spellslist.add(character.getOpenSpellList());
		for( DISCIPLINEType discipline : disciplines ) spellslist.add(discipline.getSPELL());
		setSpellRedbrick(spellslist);

		counterEquipment=0;
		for( ITEMType item : listArmorAndWeapon(character) ) addEquipment(item.getName(),item.getWeight());
		for( ITEMType item : character.getItems() ) addEquipment(item.getName(),item.getWeight());
		for( MAGICITEMType item : character.getMagicItem() ) {
			StringBuffer text=new StringBuffer(item.getName());
			text.append(" (");
			text.append(item.getBlooddamage());
			text.append("/");
			text.append(item.getDepatterningrate());
			text.append("/");
			text.append(item.getEnchantingdifficultynumber());
			text.append(")");
			addEquipment(text.toString(),item.getWeight());
		}

		int copperPieces = 0;
		int goldPieces = 0;
		int silverPieces = 0;
		for( COINSType coins : character.getAllCoins() ) {
			addEquipment("Purse"+(new Purse(coins)).toString(),coins.getWeight());
			copperPieces += coins.getCopper();
			silverPieces += coins.getSilver();
			goldPieces += coins.getGold();
		}
		acroFields.setField( "CopperPieces", String.valueOf(copperPieces) );
		acroFields.setField( "SilverPieces", String.valueOf(silverPieces) );
		acroFields.setField( "GoldPieces", String.valueOf(goldPieces) );

		int counterDescription=0;
		for( String description : wrapString(50,character.getDESCRIPTION()) ) {
			acroFields.setField( "ShortDescription."+counterDescription, description );
			counterDescription++;
			if( counterDescription > 7 ) {
				System.err.println("Character description to long. Only first 8 lines were displayed.");
				break;
			}
		}

		List<THREADITEMType> magicitems = character.getThreadItem();
		if( ! magicitems.isEmpty() ) {
			THREADITEMType magicitem = character.getThreadItem().get(0);
			if( magicitem != null ) {
				int counterThreadItem=0;
				int weaventhreadrank = magicitem.getWeaventhreadrank();
				acroFields.setField( "MagicalTreasureName", magicitem.getName() );
				acroFields.setField( "MagicalTreasureSpellDefense", String.valueOf(magicitem.getSpelldefense()) );
				acroFields.setField( "MagicalTreasureMaxThreads", String.valueOf(magicitem.getMaxthreads()) );
				int counterMagicItemDescription=0;
				for( String description : wrapString(50,magicitem.getDESCRIPTION()) ) {
					acroFields.setField( "MagicalTreasureDesc."+counterMagicItemDescription, description );
					counterMagicItemDescription++;
					if( counterMagicItemDescription > 2 ) {
						System.err.println("MagicItem description to long. Only first 3 lines were displayed.");
						break;
					}
				}
				int counterMagicItemRank=0;
				for( THREADRANKType rank : magicitem.getTHREADRANK() ) {
					acroFields.setField( "MagicalTreasureRank."+counterMagicItemRank, String.valueOf(counterMagicItemRank+1) );
					acroFields.setField( "MagicalTreasureLPCost."+counterMagicItemRank, String.valueOf(rank.getLpcost()) );
					acroFields.setField( "MagicalTreasureKeyKnowledge."+counterMagicItemRank, rank.getKeyknowledge() );
					acroFields.setField( "MagicalTreasureEffect."+counterMagicItemRank, rank.getEffect() );
					if( counterMagicItemRank < weaventhreadrank ) {
						acroFields.setField( "ThreadMagicTarget."+counterThreadItem, magicitem.getName() );
						acroFields.setField( "ThreadMagicEffect."+counterThreadItem, rank.getEffect() );
						acroFields.setField( "ThreadMagicLPCost."+counterThreadItem, String.valueOf(rank.getLpcost()) );
						acroFields.setField( "ThreadMagicRank."+counterThreadItem, String.valueOf(counterMagicItemRank+1) );
						counterThreadItem++;
					}
					counterMagicItemRank++;
				}
			}
		}

		int counterBloodCharms=0;
		for( MAGICITEMType item : character.getBloodCharmItem() ) {
			acroFields.setField( "BloodMagicType."+counterBloodCharms, item.getName() );
			if( item.getUsed().equals(YesnoType.YES)) {
				acroFields.setField( "BloodMagicDamage."+counterBloodCharms, String.valueOf(item.getBlooddamage()) );
			} else {
				acroFields.setField( "BloodMagicDamage."+counterBloodCharms, "("+item.getBlooddamage()+")" );
			}
			acroFields.setField( "BloodMagicDR."+counterBloodCharms, String.valueOf(item.getDepatterningrate()) );
			acroFields.setField( "BloodMagicEffect."+counterBloodCharms, item.getEffect() );
			counterBloodCharms++;
		}

		stamper.close();
	}

	private CharsheettemplatetalentType getTalentFieldNames(int counter) {
		CharsheettemplatetalentType fieldnames= new CharsheettemplatetalentType();
		fieldnames.setName("Talent."+counter);
		fieldnames.setPage("TalentBookref."+counter);
		fieldnames.setAttribute("Attribute."+counter);
		fieldnames.setAction("Action."+counter);
		fieldnames.setAttributeStep("TalentAttributeStep."+counter);
		fieldnames.setDice("ActionDice."+counter);
		fieldnames.setRank("Rank."+counter);
		fieldnames.setStep("Step."+counter);
		fieldnames.setStrain("Strain."+counter);
		//fieldnames.setKarma("KarmaRequired."+counter);
		return fieldnames;
	}

	public void exportRedbrickExtended(EDCHARACTER edCharakter, File outFile) throws DocumentException, IOException {
		PdfReader reader = new PdfReader(new FileInputStream(new File("./templates/ed3_extended_character_sheet.pdf")));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		acroFields = stamper.getAcroFields();
		CharacterContainer character = new CharacterContainer(edCharakter);
// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
		//Set<String> fieldNames = acroFields.getFields().keySet();
		//fieldNames = new TreeSet<String>(fieldNames);
		//for( String fieldName : fieldNames ) {
		//	acroFields.setField( fieldName, fieldName );
		//	System.out.println( fieldName );
		//}
// +++ ~DEBUG ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		exportCommonFields(character,16,55);
		setButtons(character.getWound().getNormal(), "WoundPenalties.", 9);
		acroFields.setField( "Shield", "none" );
		acroFields.setField( "ShieldDeflectionBonus", "na" );

		// Charakter Potrait-Bild einfügen
		List<Base64BinaryType> potraits = character.getPortrait();
		if( ! potraits.isEmpty() ) {
			Image image = Image.getInstance(potraits.get(0).getValue());
			image.setAbsolutePosition(35f,517f);
			image.scaleAbsolute(165f, 200f);
			PdfContentByte overContent = stamper.getOverContent(2);
			overContent.addImage(image);
		}

		int armor_max=0;
		int shield_max=0;
		for (ARMORType armor : character.getProtection().getARMOROrSHIELD() ) {
			if( ! armor.getUsed().equals(YesnoType.YES) ) continue;
			if( armor.getClass().getSimpleName().equals("ARMORType") ) {
				if( armor.getPhysicalarmor()>armor_max ) {
					armor_max=armor.getPhysicalarmor();
					acroFields.setField( "Armor" , armor.getName() );
				}
			} else if( armor.getClass().getSimpleName().equals("SHIELDType") ) {
				SHIELDType shield = (SHIELDType)armor;
				if( shield.getPhysicalarmor()>shield_max ) {
					shield_max=armor.getPhysicalarmor();
					acroFields.setField( "Shield", shield.getName() );
					acroFields.setField( "ShieldDeflectionBonus", shield.getPhysicaldeflectionbonus()+"/"+shield.getMysticdeflectionbonus() );
				}
			} else {
				System.err.println( "Unbekannte Rüstungstyp: "+armor.getClass().getSimpleName() );
			}
		}
		acroFields.setField( "Discipline", concat(" / ",character.getDisciplineNames()) );
		acroFields.setField( "Circle", concat(" / ",character.getDisciplineCircles()) );
		int counterKarmaritual=0;
		for( String karmaritual : character.getAllKarmaritual() ) {
			for( String description : wrapString(50,karmaritual) ) {
				if( counterKarmaritual > 11 ) {
					System.err.println("Karmaritual description is to long. Only first 12 lines were displayed.");
					break;
				}
				acroFields.setField( "KarmaRitual."+counterKarmaritual, description );
				counterKarmaritual++;
			}
		}
		List<DISCIPLINEType> disciplines = character.getDisciplines();
		if( ! disciplines.isEmpty() ) {
			DISCIPLINEType discipline1 = disciplines.get(0);
			int counter = 0;
			List<TALENTType> disziplinetalents = discipline1.getDISZIPLINETALENT();
			Collections.sort(disziplinetalents, new TalentComparator());
			Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = character.getAttributes();
			for( TALENTType talent : disziplinetalents ) {
				if( (talent.getCircle()>4)  && (counter<9) )  counter =  9;
				if( (talent.getCircle()>8)  && (counter<13) ) counter = 13;
				if( (talent.getCircle()>12) && (counter<17) ) counter = 17;
				setSkillOrTalent(getTalentFieldNames(counter), talent, attributes);
				counter++;
			}
			List<TALENTType> optionaltalents = new ArrayList<TALENTType>();
			optionaltalents.addAll(discipline1.getOPTIONALTALENT());
			optionaltalents.addAll(discipline1.getFREETALENT());
			Collections.sort(optionaltalents, new TalentComparator());
			counter = 0;
			for( TALENTType talent : optionaltalents ) {
				if( (talent.getCircle()>4)  && (counter<7) )  counter = 7;
				if( (talent.getCircle()>8)  && (counter<13) ) counter = 13;
				setSkillOrTalent(getTalentFieldNames(20+counter), talent, attributes);
				// Optionale Talente können Karma erfordern
				if( talent.getKarma().equals(YesnoType.YES)) {
					acroFields.setField( "KarmaRequired."+counter, "Yes" );
				} else {
					acroFields.setField( "KarmaRequired."+counter, "" );
				}
				counter++;
			}
		}
		if( disciplines.size() > 1 ) {
			DISCIPLINEType discipline2 = disciplines.get(1);
			int counter = 36;
			List<TALENTType> disziplinetalents = discipline2.getDISZIPLINETALENT();
			Collections.sort(disziplinetalents, new TalentComparator());
			Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = character.getAttributes();
			for( TALENTType talent : disziplinetalents ) {
				if( (talent.getCircle()>4)  && (counter<44) ) counter = 44;
				if( (talent.getCircle()>8)  && (counter<48) ) counter = 48;
				if( (talent.getCircle()>12) && (counter<52) ) counter = 52;
				setSkillOrTalent(getTalentFieldNames(counter), talent, attributes);
				counter++;
			}
			List<TALENTType> optionaltalents = new ArrayList<TALENTType>();
			optionaltalents.addAll(discipline2.getOPTIONALTALENT());
			optionaltalents.addAll(discipline2.getFREETALENT());
			Collections.sort(optionaltalents, new TalentComparator());
			counter = 16;
			for( TALENTType talent : optionaltalents ) {
				if( (talent.getCircle()>4)  && (counter<22) ) counter = 22;
				if( (talent.getCircle()>8)  && (counter<26) ) counter = 26;
				setSkillOrTalent(getTalentFieldNames(39+counter), talent, attributes);
				// Optionale Talente können Karma erfordern
				if( talent.getKarma().equals(YesnoType.YES)) {
					acroFields.setField( "KarmaRequired."+counter, "Yes" );
				} else {
					acroFields.setField( "KarmaRequired."+counter, "" );
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

		List<List<SPELLType>> spellslist = new ArrayList<List<SPELLType>>();
		spellslist.add(character.getOpenSpellList());
		for( DISCIPLINEType discipline : disciplines ) spellslist.add(discipline.getSPELL());
		setSpellRedbrick(spellslist);

		counterEquipment=0;
		for( ITEMType item : listArmorAndWeapon(character) ) addEquipment(item.getName(),item.getWeight());
		for( ITEMType item : character.getItems() ) addEquipment(item.getName(),item.getWeight());
		for( MAGICITEMType item : character.getMagicItem() ) {
			StringBuffer text=new StringBuffer(item.getName());
			text.append(" (");
			text.append(item.getBlooddamage());
			text.append("/");
			text.append(item.getDepatterningrate());
			text.append("/");
			text.append(item.getEnchantingdifficultynumber());
			text.append(")");
			addEquipment(text.toString(),item.getWeight());
		}

		int copperPieces = 0;
		int goldPieces = 0;
		int silverPieces = 0;
		for( COINSType coins : character.getAllCoins() ) {
			addEquipment("Purse"+(new Purse(coins)).toString(),coins.getWeight());
			copperPieces += coins.getCopper();
			silverPieces += coins.getSilver();
			goldPieces += coins.getGold();
		}
		acroFields.setField( "CopperPieces", String.valueOf(copperPieces) );
		acroFields.setField( "SilverPieces", String.valueOf(silverPieces) );
		acroFields.setField( "GoldPieces", String.valueOf(goldPieces) );

		int counterDescription=0;
		for( String description : wrapString(60,character.getDESCRIPTION()) ) {
			acroFields.setField( "ShortDescription."+counterDescription, description );
			counterDescription++;
			if( counterDescription > 7 ) {
				System.err.println("Character description to long. Only first 8 lines were displayed.");
				break;
			}
		}

		int counterMagicItem=0;
		int counterThreadItem=0;
		for( THREADITEMType item : character.getThreadItem() ) {
			int weaventhreadrank = item.getWeaventhreadrank();
			acroFields.setField( "MagicalTreasureName."+counterMagicItem, item.getName() );
			acroFields.setField( "MagicalTreasureSpellDefense."+counterMagicItem, String.valueOf(item.getSpelldefense()) );
			acroFields.setField( "MagicalTreasureMaxThreads."+counterMagicItem, String.valueOf(item.getMaxthreads()) );
			int counterMagicItemDescription=0;
			for( String description : wrapString(55,item.getDESCRIPTION()) ) {
				acroFields.setField( "MagicalTreasureDesc."+counterMagicItemDescription+"."+counterMagicItem, description );
				counterMagicItemDescription++;
				if( counterMagicItemDescription > 2 ) {
					System.err.println("MagicItem description to long. Only first 3 lines were displayed.");
					break;
				}
			}
			int counterMagicItemRank=0;
			for( THREADRANKType rank : item.getTHREADRANK() ) {
				acroFields.setField( "MagicalTreasureRank."+counterMagicItemRank+"."+counterMagicItem, String.valueOf(counterMagicItemRank+1) );
				acroFields.setField( "MagicalTreasureLPCost."+counterMagicItemRank+"."+counterMagicItem, String.valueOf(rank.getLpcost()) );
				acroFields.setField( "MagicalTreasureKeyKnowledge."+counterMagicItemRank+"."+counterMagicItem, rank.getKeyknowledge() );
				acroFields.setField( "MagicalTreasureEffect."+counterMagicItemRank+"."+counterMagicItem, rank.getEffect() );
				if( counterMagicItemRank < weaventhreadrank ) {
					acroFields.setField( "ThreadMagicTarget."+counterThreadItem, item.getName() );
					acroFields.setField( "ThreadMagicEffect."+counterThreadItem, rank.getEffect() );
					acroFields.setField( "ThreadMagicLPCost."+counterThreadItem, String.valueOf(rank.getLpcost()) );
					acroFields.setField( "ThreadMagicRank."+counterThreadItem, String.valueOf(counterMagicItemRank+1) );
					counterThreadItem++;
				}
				counterMagicItemRank++;
			}
			counterMagicItem++;
		}

		int counterBloodCharms=0;
		for( MAGICITEMType item : character.getBloodCharmItem() ) {
			acroFields.setField( "BloodMagicType."+counterBloodCharms, item.getName() );
			if( item.getUsed().equals(YesnoType.YES)) {
				acroFields.setField( "BloodMagicDamage."+counterBloodCharms, String.valueOf(item.getBlooddamage()) );
			} else {
				acroFields.setField( "BloodMagicDamage."+counterBloodCharms, "("+item.getBlooddamage()+")" );
			}
			acroFields.setField( "BloodMagicDR."+counterBloodCharms, String.valueOf(item.getDepatterningrate()) );
			acroFields.setField( "BloodMagicEffect."+counterBloodCharms, item.getEffect() );
			counterBloodCharms++;
		}

		stamper.close();
	}

	private static List<ITEMType> listArmorAndWeapon(CharacterContainer character) {
		List<ITEMType> result = new ArrayList<ITEMType>();
		for( ARMORType armor : character.getProtection().getARMOROrSHIELD() ) {
			if( armor.getVirtual().equals(YesnoType.YES)) continue;
			String name = armor.getName()+" (";
			name += armor.getPhysicalarmor()+"/";
			name += armor.getMysticarmor()+"/";
			name += armor.getPenalty() + ")";
			ITEMType item = new ITEMType();
			item.setName(name);
			item.setWeight(armor.getWeight());
			item.setLocation(armor.getLocation());
			item.setUsed(armor.getUsed());
			result.add(item);
		}
		for( WEAPONType weapon : character.getWeapons() ) {
			if( weapon.getVirtual().equals(YesnoType.YES)) continue;
			String name = weapon.getName()+" (";
			name += weapon.getDamagestep()+"/";
			name += weapon.getTimesforged()+")";
			ITEMType item = new ITEMType();
			item.setName(name);
			item.setWeight(weapon.getWeight());
			item.setLocation(weapon.getLocation());
			item.setUsed(weapon.getUsed());
			result.add(item);
		}
		return result;
	}

	private void setPdfField(String key, String value) {
		if( (key==null) || key.isEmpty() ) return;
		if( value == null ) value="";
		try {
			acroFields.setField( key, value );
		} catch (IOException | DocumentException e) {
			System.err.println("Failed to write to PDF-Formfield '"+key+":="+value+"' : "+ e.getLocalizedMessage() );
		}
	}

	private void setAllPdfFields( List<String> keys, String value ) {
		for( String k : keys ) setPdfField(k, value);
	}

	private void setCharsheettemplateboolean(CharsheettemplatebooleanType field, boolean istrue) {
		if( field == null ) return;
		setPdfField(field.getValue(),istrue ? field.getTruevalue() : field.getFalsevalue());
	}

	private void setBooleans(CharsheettemplateContainer charsheettemplate, String key, int value) {
		setAllPdfFields(charsheettemplate.getStringList(key+"Value"),String.valueOf(value));
		for( int i=0; i<20; i++ ) {
			setCharsheettemplateboolean(charsheettemplate.getBooleanEntryNext(key),i<value);
		}
	}

	private void setLpincrease(CharsheettemplateContainer charsheettemplate, String attribute,int value) {
		setBooleans(charsheettemplate,"Lpincrease"+attribute,value);
	}

	private void setCharsheettemplatedisciplinebonus(CharsheettemplatedisciplinebonusType field, DISCIPLINEBONUSType bonus) {
		if( field == null ) return;
		if( bonus == null ) return;
		setPdfField(field.getCircle(),String.valueOf(bonus.getCircle()));
		setPdfField(field.getAbility(),bonus.getBonus());
	}

	public void exportGeneric(EDCHARACTER edCharakter, File template, File outFile) throws DocumentException, IOException {
		CharsheettemplateContainer charsheettemplate = new CharsheettemplateContainer(template);
		File pdfinputfile = new File( new File("templates"), charsheettemplate.getPdfFilename() );
		PdfReader reader = new PdfReader(new FileInputStream(pdfinputfile));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		acroFields = stamper.getAcroFields();
		CharacterContainer character = new CharacterContainer(edCharakter);
// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
//		for( String fieldName : acroFields.getFields().keySet() ) acroFields.setField( fieldName, fieldName );
// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		setAllPdfFields(charsheettemplate.getStringList("CurrentDateTime"),CharacterContainer.getCurrentDateTime());
		setAllPdfFields(charsheettemplate.getStringList("Name"),character.getName());
		setAllPdfFields(charsheettemplate.getStringList("Passion"),character.getPassion());
		setAllPdfFields(charsheettemplate.getStringList("Player"),character.getPlayer());
		setAllPdfFields(charsheettemplate.getStringList("Movement"),character.getMovementAsString());
		setAllPdfFields(charsheettemplate.getStringList("Carrying"),String.valueOf(character.getCarrying().getCarrying()));

		APPEARANCEType appearance = character.getAppearance();
		String race;
		if( appearance.getOrigin().isEmpty() ) race = appearance.getRace();
		else race = appearance.getRace()+" ("+appearance.getOrigin()+")";
		setAllPdfFields(charsheettemplate.getStringList("Race"),race);
		setAllPdfFields(charsheettemplate.getStringList("Age"),String.valueOf(appearance.getAge()));
		setAllPdfFields(charsheettemplate.getStringList("Eyes"),appearance.getEyes());
		setAllPdfFields(charsheettemplate.getStringList("Hair"),appearance.getHair());
		setAllPdfFields(charsheettemplate.getStringList("Height"),appearance.getHeightString());
		setAllPdfFields(charsheettemplate.getStringList("Skin"),appearance.getSkin());
		setAllPdfFields(charsheettemplate.getStringList("Weight"),appearance.getWeightString());
		if( appearance.getGender() == GenderType.MINUS ) {
			setAllPdfFields(charsheettemplate.getStringList("Gender"),"-");
		} else {
			setAllPdfFields(charsheettemplate.getStringList("Gender"),PROPERTIES.getTranslationText(appearance.getGender().value()));
		}
		Map<ATTRIBUTENameType, ATTRIBUTEType> attributes = character.getAttributes();
		int strength = attributes.get(ATTRIBUTENameType.STR).getStep();
		setAllPdfFields(charsheettemplate.getStringList("AttributeBaseDex"),String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getBasevalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeBaseStr"),String.valueOf(attributes.get(ATTRIBUTENameType.STR).getBasevalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeBaseTou"),String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getBasevalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeBasePer"),String.valueOf(attributes.get(ATTRIBUTENameType.PER).getBasevalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeBaseWil"),String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getBasevalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeBaseCha"),String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getBasevalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeCurrentDex"),String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getCurrentvalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeCurrentStr"),String.valueOf(attributes.get(ATTRIBUTENameType.STR).getCurrentvalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeCurrentTou"),String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getCurrentvalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeCurrentPer"),String.valueOf(attributes.get(ATTRIBUTENameType.PER).getCurrentvalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeCurrentWil"),String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getCurrentvalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeCurrentCha"),String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getCurrentvalue()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeStepDex"),String.valueOf(attributes.get(ATTRIBUTENameType.DEX).getStep()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeStepStr"),String.valueOf(attributes.get(ATTRIBUTENameType.STR).getStep()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeStepTou"),String.valueOf(attributes.get(ATTRIBUTENameType.TOU).getStep()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeStepPer"),String.valueOf(attributes.get(ATTRIBUTENameType.PER).getStep()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeStepWil"),String.valueOf(attributes.get(ATTRIBUTENameType.WIL).getStep()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeStepCha"),String.valueOf(attributes.get(ATTRIBUTENameType.CHA).getStep()));
		setAllPdfFields(charsheettemplate.getStringList("AttributeDiceDex"),attributes.get(ATTRIBUTENameType.DEX).getDice());
		setAllPdfFields(charsheettemplate.getStringList("AttributeDiceStr"),attributes.get(ATTRIBUTENameType.STR).getDice());
		setAllPdfFields(charsheettemplate.getStringList("AttributeDiceTou"),attributes.get(ATTRIBUTENameType.TOU).getDice());
		setAllPdfFields(charsheettemplate.getStringList("AttributeDicePer"),attributes.get(ATTRIBUTENameType.PER).getDice());
		setAllPdfFields(charsheettemplate.getStringList("AttributeDiceWil"),attributes.get(ATTRIBUTENameType.WIL).getDice());
		setAllPdfFields(charsheettemplate.getStringList("AttributeDiceCha"),attributes.get(ATTRIBUTENameType.CHA).getDice());

		setLpincrease(charsheettemplate,"Dex",attributes.get(ATTRIBUTENameType.DEX).getLpincrease());
		setLpincrease(charsheettemplate,"Str",attributes.get(ATTRIBUTENameType.STR).getLpincrease());
		setLpincrease(charsheettemplate,"Tou",attributes.get(ATTRIBUTENameType.TOU).getLpincrease());
		setLpincrease(charsheettemplate,"Per",attributes.get(ATTRIBUTENameType.PER).getLpincrease());
		setLpincrease(charsheettemplate,"Wil",attributes.get(ATTRIBUTENameType.WIL).getLpincrease());
		setLpincrease(charsheettemplate,"Cha",attributes.get(ATTRIBUTENameType.CHA).getLpincrease());

		for( SKILLType skill : character.getSkills() ) {
			setSkillOrTalent(charsheettemplate.getSkillEntryNext(), skill, attributes);
		}

		DefenseAbility defences = character.getDefence();
		setAllPdfFields(charsheettemplate.getStringList("DefencePhysical"),String.valueOf(defences.get(EffectlayerType.PHYSICAL)));
		setAllPdfFields(charsheettemplate.getStringList("DefenceSocial"),String.valueOf(defences.get(EffectlayerType.SOCIAL)));
		setAllPdfFields(charsheettemplate.getStringList("DefenceMystic"),String.valueOf(defences.get(EffectlayerType.MYSTIC)));

		DEATHType death = character.getDeath();
		setAllPdfFields(charsheettemplate.getStringList("DeathAdjustment"),String.valueOf(death.getAdjustment()));
		setAllPdfFields(charsheettemplate.getStringList("DeathBase"),String.valueOf(death.getBase()));
		setAllPdfFields(charsheettemplate.getStringList("DeathValue"),String.valueOf(death.getValue()));

		DEATHType unconsciousness = character.getUnconsciousness();
		setAllPdfFields(charsheettemplate.getStringList("UnconsciousnessAdjustment"),String.valueOf(unconsciousness.getAdjustment()));
		setAllPdfFields(charsheettemplate.getStringList("UnconsciousnessBase"),String.valueOf(unconsciousness.getBase()));
		setAllPdfFields(charsheettemplate.getStringList("UnconsciousnessValue"),String.valueOf(unconsciousness.getValue()));

		RECOVERYType recovery = character.getRecovery();
		setAllPdfFields(charsheettemplate.getStringList("RecoveryStep"),String.valueOf(recovery.getStep()));
		setAllPdfFields(charsheettemplate.getStringList("RecoveryDice"),recovery.getDice());
		setAllPdfFields(charsheettemplate.getStringList("RecoveryTestsperday"),String.valueOf(recovery.getTestsperday()));

		WOUNDType wound = character.getWound();
		setAllPdfFields(charsheettemplate.getStringList("WoundThreshold"),String.valueOf(wound.getThreshold()));
		setAllPdfFields(charsheettemplate.getStringList("BloodWound"),String.valueOf(wound.getBlood()));
		setBooleans(charsheettemplate, "Wounds", wound.getNormal());

		HEALTHType health = character.getHealth();
		setAllPdfFields(charsheettemplate.getStringList("HealthDamage"),String.valueOf(health.getDamage()));
		setAllPdfFields(charsheettemplate.getStringList("BloodDamage"),String.valueOf(health.getBlooddamage()));

		EXPERIENCEType legendPoints = character.getLegendPoints();
		setAllPdfFields(charsheettemplate.getStringList("LegendPointsTotal"),String.valueOf(legendPoints.getTotallegendpoints()));
		setAllPdfFields(charsheettemplate.getStringList("LegendPointsCurrent"),String.valueOf(legendPoints.getCurrentlegendpoints()));
		setAllPdfFields(charsheettemplate.getStringList("LegendPointsRenown"),String.valueOf(legendPoints.getRenown()));
		setAllPdfFields(charsheettemplate.getStringList("LegendPointsReputation"),String.valueOf(legendPoints.getReputation()));

		INITIATIVEType initiative = character.getInitiative();
		setAllPdfFields(charsheettemplate.getStringList("InitiativeBase"),String.valueOf(initiative.getBase()));
		setAllPdfFields(charsheettemplate.getStringList("InitiativeStep"),String.valueOf(initiative.getStep()));
		setAllPdfFields(charsheettemplate.getStringList("InitiativeDice"),initiative.getDice());

		KARMAType karma = character.getKarma();
		setAllPdfFields(charsheettemplate.getStringList("KarmaCurrent"),String.valueOf(karma.getCurrent()));
		setAllPdfFields(charsheettemplate.getStringList("KarmaMax"),String.valueOf(karma.getMax()));
		setAllPdfFields(charsheettemplate.getStringList("KarmaModifier"),String.valueOf(character.getRace().getKarmamodifier()));

		for( ITEMType item : character.getItems() ) {
			setPdfField(charsheettemplate.getStringEntryNext("InventoryDescription"),item.getName());
		}
		for( ITEMType item : listArmorAndWeapon(character) ) {
			setPdfField(charsheettemplate.getStringEntryNext("InventoryDescription"),item.getName());
		}

		int[] totalArmor=new int[]{0,0,0};
		int[] totalShield=new int[]{0,0,0};
		for (ARMORType armor : character.getProtection().getARMOROrSHIELD() ) {
			if( ! armor.getUsed().equals(YesnoType.YES) ) continue;
			int physicalarmor = armor.getPhysicalarmor();
			int mysticarmor = armor.getMysticarmor();
			int penalty = armor.getPenalty();
			if( (physicalarmor==0) && (mysticarmor==0) && (penalty==0) ) continue;
			if( armor instanceof SHIELDType ) {
				setPdfField(charsheettemplate.getStringEntryNext("ShieldName"),armor.getName());
				setPdfField(charsheettemplate.getStringEntryNext("ShieldPenalties"),String.valueOf(armor.getPenalty()));
				totalShield[0]+=physicalarmor;
				totalShield[1]+=mysticarmor;
				totalShield[2]+=penalty;
			} else {
				setPdfField(charsheettemplate.getStringEntryNext("ArmorName"),armor.getName());
				setPdfField(charsheettemplate.getStringEntryNext("ArmorPenalties"),String.valueOf(armor.getPenalty()));
				totalArmor[0]+=physicalarmor;
				totalArmor[1]+=mysticarmor;
				totalArmor[2]+=penalty;
			}
		}
		setAllPdfFields(charsheettemplate.getStringList("ArmorPhysical"),String.valueOf(totalArmor[0]));
		setAllPdfFields(charsheettemplate.getStringList("ArmorMystic"),String.valueOf(totalArmor[1]));
		setAllPdfFields(charsheettemplate.getStringList("ArmorPenalty"),String.valueOf(totalArmor[2]));
		setAllPdfFields(charsheettemplate.getStringList("ShieldPhysical"),String.valueOf(totalShield[0]));
		setAllPdfFields(charsheettemplate.getStringList("ShieldMystic"),String.valueOf(totalShield[1]));

		for( WEAPONType weapon : character.getWeapons() ) {
			setPdfField(charsheettemplate.getStringEntryNext("WeaponName"),weapon.getName());
			setPdfField(charsheettemplate.getStringEntryNext("WeaponShortrange"),String.valueOf(weapon.getShortrange()));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponLongrange"),String.valueOf(weapon.getLongrange()));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponRange"),String.valueOf(weapon.getShortrange())+" / "+String.valueOf(weapon.getLongrange()));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponDamagestep"),String.valueOf(weapon.getDamagestep()));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponSize"),String.valueOf(weapon.getSize()));
			int damage = strength+weapon.getDamagestep();
			setPdfField(charsheettemplate.getStringEntryNext("WeaponAttackstep"),String.valueOf(attributes.get(ATTRIBUTENameType.STR).getStep()));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponAttribute"),String.valueOf(strength));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponStep"),String.valueOf(damage));
			setPdfField(charsheettemplate.getStringEntryNext("WeaponDice"),PROPERTIES.step2Dice(damage));
		}

		for( SPELLType spell : character.getOpenSpellList() ) {
			setSpell(charsheettemplate.getSpellEntryNext("Spell"),spell);
		}

		CharsheettemplatetalentStack talentForms = charsheettemplate.getTalentStack();
		CharsheettemplatetalentStack.type disciplinetalentkind = new CharsheettemplatetalentStack.type();
		disciplinetalentkind.isDiscipline=true;
		CharsheettemplatetalentStack.type othertalentkind = new CharsheettemplatetalentStack.type();
		othertalentkind.isOther=true;
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			setPdfField(charsheettemplate.getStringEntryNext("DisciplineName"),discipline.getName());
			setPdfField(charsheettemplate.getStringEntryNext("DisciplineCircle"),String.valueOf(discipline.getCircle()));

			List<TALENTType> disziplinetalents = discipline.getDISZIPLINETALENT();
			Collections.sort(disziplinetalents, new TalentComparator());
			for( TALENTType talent : disziplinetalents ) {
				setSkillOrTalent(talentForms.pull(disciplinetalentkind), talent, attributes);
				for( KNACKType knack : talent.getKNACK() ) {
					setPdfField(charsheettemplate.getStringEntryNext("TalentKnackTalent"),talent.getName());
					setPdfField(charsheettemplate.getStringEntryNext("TalentKnackTalent"),knack.getName()+" ["+knack.getStrain()+"]");
				}
			}
			List<TALENTType> optionaltalents = new ArrayList<TALENTType>();
			optionaltalents.addAll(discipline.getOPTIONALTALENT());
			optionaltalents.addAll(discipline.getFREETALENT());
			Collections.sort(optionaltalents, new TalentComparator());
			for( TALENTType talent : optionaltalents ) {
				setSkillOrTalent(talentForms.pull(othertalentkind), talent, attributes);
				for( KNACKType knack : talent.getKNACK() ) {
					setPdfField(charsheettemplate.getStringEntryNext("TalentKnackTalent"),talent.getName());
					setPdfField(charsheettemplate.getStringEntryNext("TalentKnackTalent"),knack.getName()+" ["+knack.getStrain()+"]");
				}
			}
			for( DISCIPLINEBONUSType bonus : discipline.getDISCIPLINEBONUS() ) {
				setCharsheettemplatedisciplinebonus(charsheettemplate.getDisciplinebonusEntryNext("DisciplineBonus"),bonus);
			}
			for( SPELLType spell : discipline.getSPELL() ) {
				setSpell(charsheettemplate.getSpellEntryNext("Spell"),spell);
			}
		}

		Purse money = new Purse();
		for( COINSType coins : character.getAllCoins() ) {
			money.addPurse(new Purse(coins));
		}

		for( Purse.Coinstype c : new Purse.Coinstype[] {Purse.Coinstype.COPPER,Purse.Coinstype.SILVER,Purse.Coinstype.GOLD} ) {
			setAllPdfFields(charsheettemplate.getStringList("Money"+c.value()), String.valueOf(money.getCoin(c)) );
			money.setCoin(c,0);
		}
		setAllPdfFields(charsheettemplate.getStringList("MoneyOther"), money.content() );

		for( THREADITEMType item : character.getThreadItem() ) {
			int rank=1;
			for( THREADRANKType thread : item.getTHREADRANK() ) {
				if( rank > item.getWeaventhreadrank() ) break;
				setPdfField(charsheettemplate.getStringEntryNext("ThreadItemName"),item.getName());
				setPdfField(charsheettemplate.getStringEntryNext("ThreadItemThreadRank"),String.valueOf(rank));
				setPdfField(charsheettemplate.getStringEntryNext("ThreadItemThreadEffect"),thread.getEffect());
				rank++;
			}
		}

		stamper.close();
	}

	public void exportAjfelMordom(EDCHARACTER edCharakter, int pdftype, File outFile) throws DocumentException, IOException {
		File pdfinputfile;
		if( pdftype == 1 ) pdfinputfile = new File("templates/ed3_character_sheet_Ajfel+Mordom_pl.pdf");
		else pdfinputfile = new File("templates/ed3_character_sheet_Ajfel+Mordom.pdf");
		PdfReader reader = new PdfReader(new FileInputStream(pdfinputfile));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		acroFields = stamper.getAcroFields();
		CharacterContainer character = new CharacterContainer(edCharakter);
// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
		//Set<String> fieldNames = acroFields.getFields().keySet();
		//fieldNames = new TreeSet<String>(fieldNames);
		//for( String fieldName : fieldNames ) {
		//	acroFields.setField( fieldName, fieldName );
		//}
// +++ ~DEBUG ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		exportCommonFields(character,16,40);
		setButtons(character.getWound().getNormal(), "Wound.", 7);
		acroFields.setField( "BloodWound" , "D:"+character.getHealth().getBlooddamage()+", W:"+character.getWound().getBlood()+", DR:"+character.getHealth().getDepatterningrate() );

		// Charakter Potrait-Bild einfügen
		List<Base64BinaryType> potraits = character.getPortrait();
		if( ! potraits.isEmpty() ) {
			Image image = Image.getInstance(potraits.get(0).getValue());
			if( image != null ) {
				image.setAbsolutePosition(18.5f,702.5f);
				image.scaleAbsolute(91.5f, 93f);
				PdfContentByte overContent = stamper.getOverContent(2);
				if( overContent != null ) overContent.addImage(image);
				else errorout.println("Unable to insert character image.");
			}
		}

		int counterArmor=0;
		for (ARMORType armor : character.getProtection().getARMOROrSHIELD() ) {
			if( ! armor.getUsed().equals(YesnoType.YES) ) continue;
			int physicalarmor = armor.getPhysicalarmor();
			int mysticarmor = armor.getMysticarmor();
			int penalty = armor.getPenalty();
			if( (physicalarmor==0) && (mysticarmor==0) && (penalty==0) ) continue;
			acroFields.setField( "ArmorName."+counterArmor , armor.getName() );
			acroFields.setField( "ArmorPhysical."+counterArmor , String.valueOf(physicalarmor) );
			acroFields.setField( "ArmorMystic."+counterArmor , String.valueOf(mysticarmor) );
			acroFields.setField( "ArmorPenalty."+counterArmor , String.valueOf(penalty) );
			counterArmor++;
		}

		acroFields.setField( "Discipline", concat(" / ",character.getDisciplineNames()) );
		acroFields.setField( "Circle", concat(" / ",character.getDisciplineCircles()) );
		acroFields.setField( "HalfMagic", character.getAllHalfMagic() );

		List<WEAPONType> weapons = character.getWeapons();
		if( weapons != null ) {
			int counter = 0;
			ATTRIBUTEType str = character.getAttributes().get(ATTRIBUTENameType.STR);
			for( WEAPONType weapon : weapons ) {
				acroFields.setField( "Weapon."+counter, weapon.getName() );
				acroFields.setField( "WeaponStrength."+counter, String.valueOf(str.getStep()) );
				acroFields.setField( "WeaponDamage.0."+counter, String.valueOf(weapon.getDamagestep()) );
				acroFields.setField( "WeaponDamage.1."+counter, String.valueOf(weapon.getDamagestep()+str.getStep()) );
				acroFields.setField( "WeaponRange."+counter, weapon.getShortrange()+" / "+ weapon.getLongrange() );
				counter++;
			}
		}

		counterEquipment=0;
		for( ITEMType item : listArmorAndWeapon(character) ) addEquipment(item.getName(),item.getWeight());
		for( ITEMType item : character.getItems() ) addEquipment(item.getName(),item.getWeight());
		for( MAGICITEMType item : character.getMagicItem() ) {
			StringBuffer text=new StringBuffer(item.getName());
			text.append(" (");
			text.append(item.getBlooddamage());
			text.append("/");
			text.append(item.getDepatterningrate());
			text.append("/");
			text.append(item.getEnchantingdifficultynumber());
			text.append(")");
			addEquipment(text.toString(),item.getWeight());
		}

		String copperPieces = null;
		String goldPieces = null;
		String silverPieces = null;
		int otherPieces = 0;
		for( COINSType coins : character.getAllCoins() ) {
			StringBuffer other=new StringBuffer();
			if( coins.getEarth()>0 )      other.append(" earth:"+coins.getEarth());
			if( coins.getWater()>0 )      other.append(" water:"+coins.getWater());
			if( coins.getAir()>0 )        other.append(" air:"+coins.getAir());
			if( coins.getFire()>0 )       other.append(" fire:"+coins.getFire());
			if( coins.getOrichalcum()>0 ) other.append(" orichalcum:"+coins.getOrichalcum());
			if( coins.getGem50()>0)       other.append(" gem50:"+coins.getGem50());
			if( coins.getGem100()>0)      other.append(" gem100:"+coins.getGem100());
			if( coins.getGem200()>0)      other.append(" gem200:"+coins.getGem200());
			if( coins.getGem500()>0)      other.append(" gem500:"+coins.getGem500());
			if( coins.getGem1000()>0)     other.append(" gem1000:"+coins.getGem1000());
			if( ! other.toString().isEmpty() ) {
				if( ! coins.getName().isEmpty() ) other.append(" ["+coins.getName()+"]");
				acroFields.setField( "Coins."+String.valueOf(otherPieces), other.toString() );
				otherPieces++;
			}
			if( coins.getCopper() != 0 ) {
				if( copperPieces == null ) {
					copperPieces = String.valueOf(coins.getCopper());
				} else {
					copperPieces += "+"+String.valueOf(coins.getCopper());
				}
			}
			if( coins.getSilver() != 0 ) {
				if( silverPieces == null ) {
					silverPieces = String.valueOf(coins.getSilver());
				} else {
					silverPieces += "+"+String.valueOf(coins.getSilver());
				}
			}
			if( coins.getGold() != 0 ) {
				if( goldPieces == null ) {
					goldPieces = String.valueOf(coins.getGold());
				} else {
					goldPieces += "+"+String.valueOf(coins.getGold());
				}
			}
		}
		acroFields.setField( "CopperPieces", copperPieces );
		acroFields.setField( "SilverPieces.0", silverPieces );
		acroFields.setField( "GoldPieces", goldPieces );

		List<List<SPELLType>> spellslist = new ArrayList<List<SPELLType>>();
		spellslist.add(character.getOpenSpellList());
		int counterDisciplinetalent=0;
		int counterOthertalent=0;
		int counterKnack=0;
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			List<TALENTType> disziplinetalents = discipline.getDISZIPLINETALENT();
			Collections.sort(disziplinetalents, new TalentComparator());
			for( TALENTType talent : disziplinetalents ) {
				// Für mehr als 20 Disziplintalente ist kein Platz!
				if( counterDisciplinetalent>20 ) break;
				setSkillOrTalent(getTalentFieldNames(counterDisciplinetalent), talent, character.getAttributes());
				counterDisciplinetalent++;
				for( KNACKType knack : talent.getKNACK() ) {
					acroFields.setField( "TalentKnackTalent."+counterKnack, talent.getName() );
					acroFields.setField( "TalentKnackName."+counterKnack, knack.getName()+" ["+knack.getStrain()+"]" );
					counterKnack++;
				}
			}
			List<TALENTType> optionaltalents = new ArrayList<TALENTType>();
			optionaltalents.addAll(discipline.getOPTIONALTALENT());
			optionaltalents.addAll(discipline.getFREETALENT());
			Collections.sort(optionaltalents, new TalentComparator());
			for( TALENTType talent : optionaltalents ) {
				setSkillOrTalent(getTalentFieldNames(20+counterOthertalent), talent, character.getAttributes());
				if( talent.getKarma().equals(YesnoType.YES)) {
					acroFields.setField( "KarmaRequired."+counterOthertalent, "Yes" );
				} else {
					acroFields.setField( "KarmaRequired."+counterOthertalent, "" );
				}
				counterOthertalent++;
				for( KNACKType knack : talent.getKNACK() ) {
					acroFields.setField( "TalentKnackTalent."+counterKnack, talent.getName() );
					acroFields.setField( "TalentKnackName."+counterKnack, knack.getName()+" ["+knack.getStrain()+"]" );
					counterKnack++;
				}
			}
			spellslist.add(discipline.getSPELL());
		}
		setSpellAjfelMordom(spellslist);

		// Die eventuell gesetzte KarmaBenötigtHarken löschen
		while( counterOthertalent < 17 ) {
			acroFields.setField( "KarmaRequired."+counterOthertalent, "" );
			counterOthertalent++;
		}

		int counterMagicItem=0;
		for( THREADITEMType item : character.getThreadItem() ) {
			int counterMagicItemRank=0;
			for( THREADRANKType rank : item.getTHREADRANK() ) {
				counterMagicItemRank++;
				acroFields.setField( "ThreadMagicObject."+counterMagicItem, item.getName() );
				acroFields.setField( "ThreadMagicRank."+counterMagicItem, String.valueOf(counterMagicItemRank) );
				acroFields.setField( "ThreadMagicLPCost."+counterMagicItem, String.valueOf(rank.getLpcost()) );
				acroFields.setField( "ThreadMagicEffect."+counterMagicItem, rank.getEffect() );
				counterMagicItem++;
			}
		}

		int counterBloodCharms=0;
		for( MAGICITEMType item : character.getBloodCharmItem() ) {
			acroFields.setField( "BloodMagicType."+counterBloodCharms, item.getName() );
			String used ="";
			if( item.getUsed().equals(YesnoType.YES)) used=" (in use)";
			acroFields.setField( "BloodMagicDamage."+counterBloodCharms, item.getBlooddamage()+used );
			acroFields.setField( "BloodMagicEffect."+counterBloodCharms, item.getEffect() );
			counterBloodCharms++;
		}

		acroFields.setField( "ShortDescription", character.getDESCRIPTION() );

		int counterLanguageSpeak=0;
		int counterLanguageReadwrite=0;
		for( CHARACTERLANGUAGEType language : character.getLanguages().getLanguages() ) {
			if( ! language.getSpeak().equals(LearnedbyType.NO) ) {
				acroFields.setField( "LanguagesSpeak."+counterLanguageSpeak, language.getLanguage() );
				counterLanguageSpeak++;
			}
			if( ! language.getReadwrite().equals(LearnedbyType.NO) ) {
				acroFields.setField( "LanguagesReadWrite."+counterLanguageReadwrite, language.getLanguage() );
				counterLanguageReadwrite++;
			}
		}

		stamper.close();
	}

	private void setSpellAjfelMordom(List<List<SPELLType>> spellslist) throws IOException, DocumentException {
		final Map<SpellkindType,String[]> spellkindtranslation=PROPERTIES.getTranslationSpellkindAll();
		int pos=0;
		for( List<SPELLType> spells : spellslist ) {
			Collections.sort(spells, new SpellComparator());
			for( SPELLType spell: spells ) {
				acroFields.setField( "SpellName."+pos, spell.getName() );
				acroFields.setField( "SpellBookref."+pos, String.valueOf(spell.getBookref()) );
				if( spell.getInmatrix().equals(YesnoType.YES) ) {
					acroFields.setField( "SpellInMatrix."+pos, "X" );
				} else {
					acroFields.setField( "SpellInMatrix."+pos, "" );
				}
				if( spell.getElement().equals(ElementkindType.UNDEFINED) ) {
					String[] type = spellkindtranslation.get(spell.getType());
					if( type == null || type.length < 1 || type[0] == null ) {
						acroFields.setField( "SpellType."+pos, "" );
					} else {
						acroFields.setField( "SpellType."+pos, type[0] );
					}
				} else acroFields.setField( "SpellType."+pos, spell.getElement().value() );
				acroFields.setField( "SpellCircle."+pos, String.valueOf(spell.getCircle()) );
				acroFields.setField( "SpellThreads."+pos, spell.getThreads() );
				acroFields.setField( "WeavingDifficulty."+pos, spell.getWeavingdifficulty()+"/"+spell.getReattuningdifficulty() );
				acroFields.setField( "CastingDifficulty."+pos, spell.getCastingdifficulty() );
				acroFields.setField( "SpellRange."+pos, spell.getRange() );
				acroFields.setField( "Duration."+pos, spell.getDuration() );
				acroFields.setField( "SpellEffect."+pos, spell.getEffect() );
				pos++;
			}
		}
	}

	private void setSpellRedbrick(List<List<SPELLType>> spellslist) throws IOException, DocumentException {
		final Map<SpellkindType,String[]> spellkindtranslation=PROPERTIES.getTranslationSpellkindAll();
		int pos=0;
		for( List<SPELLType> spells : spellslist ) {
			Collections.sort(spells, new SpellComparator());
			for( SPELLType spell: spells ) {
				String spellname = spell.getName();
				if( ! spell.getBookref().isEmpty() ) spellname += " ["+spell.getBookref()+"]";
				acroFields.setField( "SpellName."+pos, spellname );
				if( spell.getInmatrix().equals(YesnoType.YES)) {
					acroFields.setField( "InMatrix."+pos, "Yes" );
				} else {
					acroFields.setField( "InMatrix."+pos, "" );
				}
				String[] type = spellkindtranslation.get(spell.getType());
				if( type == null || type.length < 1 || type[0] == null ) {
					acroFields.setField( "SpellType."+pos, "" );
				} else {
					acroFields.setField( "SpellType."+pos, type[0] );
				}
				acroFields.setField( "SpellCircle."+pos, String.valueOf(spell.getCircle()) );
				acroFields.setField( "SpellThreads."+pos, spell.getThreads() );
				acroFields.setField( "WeavingDifficulty."+pos, spell.getWeavingdifficulty()+"/"+spell.getReattuningdifficulty() );
				acroFields.setField( "CastingDifficulty."+pos, spell.getCastingdifficulty() );
				acroFields.setField( "SpellRange."+pos, spell.getRange() );
				acroFields.setField( "Duration."+pos, spell.getDuration() );
				acroFields.setField( "Effect."+pos, spell.getEffect() );
				pos++;
			}
		}
	}

	private void setLpincreaseButtons( int value, int attribute ) throws IOException, DocumentException {
		setButtons( value, "LPIncrease."+attribute+".", 3 );
	}

	private void setButtons( int value, String button, int count ) throws IOException, DocumentException {
		while( count > 0 ) {
			count--;
			if( value > count ) acroFields.setField( button+count, "Yes" );
			else acroFields.setField( button+count, "" );
		}
	}

	private void addEquipment(String name, float weight) throws IOException, DocumentException {
		acroFields.setField( "Equipment."+counterEquipment, name );
		acroFields.setField( "Weight."+counterEquipment, (new DecimalFormat("0.##")).format(weight) );
		counterEquipment++;
	}

	private void setSkillOrTalent(CharsheettemplatetalentType fieldnames, SKILLType talent, Map<ATTRIBUTENameType,ATTRIBUTEType> attributes) {
		if( fieldnames == null ) return;
		String talentname = talent.getName();
		String limitation="";
		if( talent.getLIMITATION().size()>0 ) limitation=talent.getLIMITATION().get(0);
		if ( ! limitation.isEmpty() ) talentname += ": "+limitation;
		setPdfField( fieldnames.getPage(), String.valueOf(talent.getBookref()) );
		if( talent instanceof TALENTType ) {
			TALENTTEACHERType teacher = ((TALENTType)talent).getTEACHER();
			if ( (teacher != null) && teacher.getByversatility().equals(YesnoType.YES) ) talentname += " (v)";
		}
		if ( talent.getRealigned() > 0 ) talentname="("+talentname+")";
		setPdfField( fieldnames.getName(), talentname);
		RANKType talentrank = talent.getRANK();
		ATTRIBUTENameType attribute = talent.getAttribute();
		if( attribute!= null ) {
			setPdfField( fieldnames.getAttribute(), attribute.value() );
			boolean attributeIsNa = attribute.equals(ATTRIBUTENameType.NA);
			if( attributeIsNa ) {
				setPdfField( fieldnames.getAttributeStep(), "-" );
			} else {
				setPdfField( fieldnames.getAttributeStep(), String.valueOf(attributes.get(attribute).getStep()) );
			}
			if( attributeIsNa || (talentrank.getDice()==null) ) {
				setPdfField( fieldnames.getDice(), "-" );
			} else {
				setPdfField( fieldnames.getDice(), talentrank.getDice() );
			}
			if( attributeIsNa ) {
				setPdfField( fieldnames.getStep(), "-" );
			} else {
				setPdfField( fieldnames.getStep(), String.valueOf(talentrank.getStep()) );
			}
		}
		setPdfField( fieldnames.getStrain(), talent.getStrain() );
		switch( talent.getAction() ) {
		case STANDARD  : setPdfField( fieldnames.getAction(), "std" ); break;
		case SIMPLE    : setPdfField( fieldnames.getAction(), "smpl" ); break;
		case SUSTAINED : setPdfField( fieldnames.getAction(), "sust" ); break;
		default        : setPdfField( fieldnames.getAction(), talent.getAction().value() );
		}
		if( talentrank.getBonus() > 0 ) setPdfField( fieldnames.getRank(), talentrank.getRank()+"+"+talentrank.getBonus() );
		else if( talentrank.getBonus() < 0 ) setPdfField( fieldnames.getRank(), talentrank.getRank()+"-"+(talentrank.getBonus()*-1) );
		else setPdfField( fieldnames.getRank(), String.valueOf(talentrank.getRank()) );
		if( talent.getKarma().equals(YesnoType.YES)) {
			setPdfField( fieldnames.getKarma() , "Yes" );
		} else {
			setPdfField( fieldnames.getKarma(), "" );
		}
	}

	private void setSpell(CharsheettemplatespellType fieldnames, SPELLType spell) {
		if( fieldnames==null ) return;
		if( spell==null ) return;
		setPdfField( fieldnames.getName(), spell.getName() );
		setPdfField( fieldnames.getBookref(), spell.getBookref() );
		setPdfField( fieldnames.getCastingdifficulty(), spell.getCastingdifficulty() );
		setPdfField( fieldnames.getReattuningdifficulty(), String.valueOf(spell.getReattuningdifficulty()) );
		setPdfField( fieldnames.getCircle(), String.valueOf(spell.getCircle()) );
		setPdfField( fieldnames.getDuration(),spell.getDuration());
		setPdfField( fieldnames.getEffect(),spell.getEffect());
		setPdfField( fieldnames.getEffectrarea(),spell.getEffectarea());
		setPdfField( fieldnames.getElement(),spell.getElement().value());
		setPdfField( fieldnames.getRange(),spell.getRange());
		setPdfField( fieldnames.getReattuningdifficulty(),String.valueOf(spell.getReattuningdifficulty()));
		setPdfField( fieldnames.getThreads(),spell.getThreads());
		setPdfField( fieldnames.getWeavingdifficulty(),spell.getWeavingdifficulty());
	}

	public static List<String> wrapString(int maxLength, String string) {
		String wrapChar = ", -	"; // Liste von Zeichen wo ein Umbruch erlaubt ist
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

	public static String concat(String sep, List<?> list) {
		String result="";
		for( Object e : list ) {
			if( ! result.isEmpty() ) result+=sep;
			result+=String.valueOf(e);
		}
		return result;
	}

	public void exportSpellcards(EDCHARACTER edCharakter, File outFile, int version) throws DocumentException, IOException {
		CharacterContainer character = new CharacterContainer(edCharakter);
		File template=null; 
		int maxSpellPerPage=1;
		switch(version) {
		case 0:
		default:
			template = new File("./templates/spellcards_portrait_2x2.pdf");
			maxSpellPerPage=4;
			break;
		case 1:
			template = new File("./templates/spellcards_landscape_2x2.pdf");
			maxSpellPerPage=4;
			break;
		}
		String filename=outFile.getCanonicalPath();
		String filenameBegin="";
		String filenameEnd="";
		int dotPosition = filename.lastIndexOf ( '.' );
		if ( dotPosition >= 0 ) {
			filenameBegin = filename.substring( 0, dotPosition );
			filenameEnd = filename.substring( dotPosition );
		} else {
			filenameBegin=filename;
		}
		int counterFile=0;
		int counterSpells=maxSpellPerPage;
		PdfStamper stamper=null;
		PdfReader reader=null;
		Map<String, SpelldescriptionType> spelldescriptions = ApplicationProperties.create().getSpellDescriptions();
		
		List<List<SPELLType>> spellslist = new ArrayList<List<SPELLType>>();
		List<String> disciplineNames = new ArrayList<String>();
		spellslist.add(character.getOpenSpellList());
		disciplineNames.add("");
		for( DISCIPLINEType discipline : character.getDisciplines() ) {
			spellslist.add(discipline.getSPELL());
			disciplineNames.add(discipline.getName());
		}
		int spelllistnr=0;
		for( List<SPELLType> spells : spellslist ) {
			Collections.sort(spells, new SpellComparator());
			for( SPELLType spell: spells ) {
				if( counterSpells < maxSpellPerPage ) {
					counterSpells++;
				} else {
					if( stamper != null ) stamper.close();
					if( reader != null ) reader.close();
					reader = new PdfReader(new FileInputStream(template));
					stamper = new PdfStamper(reader, new FileOutputStream(new File(filenameBegin+String.format("%02d", counterFile)+filenameEnd)));
					acroFields = stamper.getAcroFields();
					counterSpells=1;
					counterFile++;
				}
				acroFields.setField( "Discipline"+counterSpells, disciplineNames.get(spelllistnr) );
				acroFields.setField( "Spell Name"+counterSpells, spell.getName() );
				acroFields.setField( "Spell Circle"+counterSpells, String.valueOf(spell.getCircle()) );
				acroFields.setField( "Spellcasting"+counterSpells, spell.getCastingdifficulty() );
				acroFields.setField( "Threads"+counterSpells, spell.getThreads() );
				acroFields.setField( "Weaving"+counterSpells, spell.getWeavingdifficulty() );
				acroFields.setField( "Reattuning"+counterSpells, String.valueOf(spell.getReattuningdifficulty()) );
				acroFields.setField( "Range"+counterSpells, spell.getRange() );
				acroFields.setField( "Duration"+counterSpells, spell.getDuration() );
				acroFields.setField( "Effect"+counterSpells, spell.getEffect() );
				acroFields.setField( "Page reference"+counterSpells, String.valueOf(spell.getBookref()) );
				acroFields.setField( "Air"+counterSpells, "No" );
				acroFields.setField( "Earth"+counterSpells, "No" );
				acroFields.setField( "Fear"+counterSpells, "No" );
				acroFields.setField( "Fire"+counterSpells, "No" );
				acroFields.setField( "Illusion"+counterSpells, "No" );
				acroFields.setField( "Illusion N"+counterSpells, "Yes" );
				acroFields.setField( "Water"+counterSpells, "No" );
				acroFields.setField( "Wood"+counterSpells, "No" );
				switch(spell.getElement()) {
				case AIR:
					acroFields.setField( "Air"+counterSpells, "Yes" ); break;
				case EARTH:
					acroFields.setField( "Earth"+counterSpells, "Yes" ); break;
				case FEAR:
					acroFields.setField( "Fear"+counterSpells, "Yes" ); break;
				case FIRE:
					acroFields.setField( "Fire"+counterSpells, "Yes" ); break;
				case ILLUSION:
					acroFields.setField( "Illusion"+counterSpells, "Yes" );
					acroFields.setField( "Illusion N"+counterSpells, "No" );
					break;
				case WATER:
					acroFields.setField( "Water"+counterSpells, "Yes" ); break;
				case WOOD:
					acroFields.setField( "Wood"+counterSpells, "Yes" ); break;
				case UNDEFINED:
					break;
				}
				SpelldescriptionType spelldescription = spelldescriptions.get(spell.getName());
				if( (spelldescription==null) || (spelldescription.getValue()==null) ) acroFields.setField( "Spell description"+counterSpells, "" );
				else acroFields.setField( "Spell description"+counterSpells, spelldescription.getValue() );
			}
			spelllistnr++;
		}
		stamper.close();
	}
}
