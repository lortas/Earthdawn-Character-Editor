package de.earthdawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import de.earthdawn.data.EDCHARAKTER;

public class ECEPdfExporter {

    public void export(EDCHARAKTER edCharakter, File outFile) throws DocumentException, IOException {
//        PdfReader reader = new PdfReader(new FileInputStream(new File("./config/ed3_character_sheet.pdf")));
            PdfReader reader = new PdfReader(new FileInputStream(new File("./config/ed3_extended_character_sheet.pdf")));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
        AcroFields acroFields = stamper.getAcroFields();

// +++ DEBUG +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
//        HashMap<String, Item> fields = acroFields.getFields();
//        Set<String> fieldNames = fields.keySet();
//        fieldNames = new TreeSet<String>(fieldNames);
//        for( String fieldName : fieldNames ) {
//            System.out.println( fieldName );
//        }
// +++ ~DEBUG ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        acroFields.setField( "Name", edCharakter.getBezeichnung());    
        stamper.close();
    }
}
