/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meupdf.editor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author iW Dev
 */
public class Merger {

    PDFMergerUtility merger = new PDFMergerUtility();
    private List<String> pdfs = new ArrayList();
    
    public void add(String file) throws FileNotFoundException{
        merger.addSource(file);
    }
    
    public void add(File file) throws FileNotFoundException{
        merger.addSource(file);
    }
    
    public void merge(String output) throws IOException {
        String pdfPath = output;
        OutputStream bout2 = new BufferedOutputStream(new FileOutputStream(pdfPath));
        merger.setDestinationStream(bout2);
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }

}
