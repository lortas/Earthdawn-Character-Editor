package de.earthdawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import de.earthdawn.data.EDCHARACTER;

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
		acroFields.setField( "Race" , character.getAppearance().getRace() );
		acroFields.setField( "Age" , String.valueOf(character.getAppearance().getAge()) );
		acroFields.setField( "Eyes" , character.getAppearance().getEyes() );
		acroFields.setField( "Gender" , character.getAppearance().getGender().toString() );
		acroFields.setField( "Hair" , character.getAppearance().getHair() );
		acroFields.setField( "Height" , String.valueOf(character.getAppearance().getHeight()) );
		acroFields.setField( "Skin" , character.getAppearance().getSkin() );
		acroFields.setField( "Weight.0.0" , String.valueOf(character.getAppearance().getWeight()) ); // TODO: Falsches Feld
		acroFields.setField( "AttributeBase.0", String.valueOf(character.getAttributes().get("DEX").getCurrentvalue()-character.getAttributes().get("DEX").getLpincrease()) );
		acroFields.setField( "AttributeBase.1", String.valueOf(character.getAttributes().get("STR").getCurrentvalue()-character.getAttributes().get("STR").getLpincrease()) );
		acroFields.setField( "AttributeBase.2", String.valueOf(character.getAttributes().get("TOU").getCurrentvalue()-character.getAttributes().get("TOU").getLpincrease()) );
		acroFields.setField( "AttributeBase.3", String.valueOf(character.getAttributes().get("PER").getCurrentvalue()-character.getAttributes().get("PER").getLpincrease()) );
		acroFields.setField( "AttributeBase.4", String.valueOf(character.getAttributes().get("WIL").getCurrentvalue()-character.getAttributes().get("WIL").getLpincrease()) );
		acroFields.setField( "AttributeBase.5", String.valueOf(character.getAttributes().get("CHA").getCurrentvalue()-character.getAttributes().get("CHA").getLpincrease()) );
		acroFields.setField( "LPIncrease.0", String.valueOf(character.getAttributes().get("DEX").getLpincrease()) );
		acroFields.setField( "LPIncrease.1", String.valueOf(character.getAttributes().get("STR").getLpincrease()) );
		acroFields.setField( "LPIncrease.2", String.valueOf(character.getAttributes().get("TOU").getLpincrease()) );
		acroFields.setField( "LPIncrease.3", String.valueOf(character.getAttributes().get("PER").getLpincrease()) );
		acroFields.setField( "LPIncrease.4", String.valueOf(character.getAttributes().get("WIL").getLpincrease()) );
		acroFields.setField( "LPIncrease.5", String.valueOf(character.getAttributes().get("CHA").getLpincrease()) );
		acroFields.setField( "AttributeCurrent.0", String.valueOf(character.getAttributes().get("DEX").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.1", String.valueOf(character.getAttributes().get("STR").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.2", String.valueOf(character.getAttributes().get("TOU").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.3", String.valueOf(character.getAttributes().get("PER").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.4", String.valueOf(character.getAttributes().get("WIL").getCurrentvalue()) );
		acroFields.setField( "AttributeCurrent.5", String.valueOf(character.getAttributes().get("CHA").getCurrentvalue()) );
		acroFields.setField( "AttributeStep.0", String.valueOf(character.getAttributes().get("DEX").getStep()) );
		acroFields.setField( "AttributeStep.1", String.valueOf(character.getAttributes().get("STR").getStep()) );
		acroFields.setField( "AttributeStep.2", String.valueOf(character.getAttributes().get("TOU").getStep()) );
		acroFields.setField( "AttributeStep.3", String.valueOf(character.getAttributes().get("PER").getStep()) );
		acroFields.setField( "AttributeStep.4", String.valueOf(character.getAttributes().get("WIL").getStep()) );
		acroFields.setField( "AttributeStep.5", String.valueOf(character.getAttributes().get("CHA").getStep()) );
		acroFields.setField( "AttributeDice.0", character.getAttributes().get("DEX").getDice().value() );
		acroFields.setField( "AttributeDice.1", character.getAttributes().get("STR").getDice().value() );
		acroFields.setField( "AttributeDice.2", character.getAttributes().get("TOU").getDice().value() );
		acroFields.setField( "AttributeDice.3", character.getAttributes().get("PER").getDice().value() );
		acroFields.setField( "AttributeDice.4", character.getAttributes().get("WIL").getDice().value() );
		acroFields.setField( "AttributeDice.5", character.getAttributes().get("CHA").getDice().value() );
		acroFields.setField( "DefensePhysical" , String.valueOf(character.getDefence().getPhysical()) );
		acroFields.setField( "DefenseSocial" , String.valueOf(character.getDefence().getSocial()) );
		acroFields.setField( "DefenseSpell" , String.valueOf(character.getDefence().getSpell()) );
		acroFields.setField( "MovementRate" ,  "" );
		acroFields.setField( "Armor" , "" );
		acroFields.setField( "CarryingCapacity" ,  "" );
		acroFields.setField( "CharacterWeight" ,  "" );
		acroFields.setField( "Circle" ,  "" );
		acroFields.setField( "CopperPieces" ,  "" );
		acroFields.setField( "CurrentDamage" ,  "" );
		acroFields.setField( "CurrentLegendPoints" ,  "" );
		acroFields.setField( "DeathAdjustment" ,  "" );
		acroFields.setField( "DeathBase" ,  "" );
		acroFields.setField( "DeathValue" ,  "" );
		acroFields.setField( "Discipline" ,  "" );
		acroFields.setField( "GoldPieces" ,  "" );
		acroFields.setField( "InitiativeDice" ,  "" );
		acroFields.setField( "InitiativeStep" ,  "" );
		acroFields.setField( "KarmaCurrent" ,  "" );
		acroFields.setField( "KarmaMax" ,  "" );
		acroFields.setField( "Mystic Armor" ,  "" );
		acroFields.setField( "Physical Armor" ,  "" );
		acroFields.setField( "Recovery Step" ,  "" );
		acroFields.setField( "RecoveryDice" ,  "" );
		acroFields.setField( "RecoveryTestsPerDay" ,  "" );
		acroFields.setField( "Renown" ,  "" );
		acroFields.setField( "Reputation" ,  "" );
		acroFields.setField( "Shield" ,  "" );
		acroFields.setField( "ShieldDeflectionBonus" ,  "" );
		acroFields.setField( "SilverPieces" ,  "" );
		acroFields.setField( "TotalLegendPoints" ,  "" );
		acroFields.setField( "UnconsciousnessAdjustment" ,  "" );
		acroFields.setField( "UnconsciousnessBase" ,  "" );
		acroFields.setField( "UnconsciousnessValue" ,  "" );
		acroFields.setField( "WoundThreshold" ,  "" );

		stamper.close();
	}
}
