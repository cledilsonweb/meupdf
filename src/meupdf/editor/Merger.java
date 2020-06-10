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

/**
 *
 * @author cledilsonweb
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
