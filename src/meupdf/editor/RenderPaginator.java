
package meupdf.editor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDMMType1Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author cledilsonweb
 */
public class RenderPaginator {

    private String in;
    private float fontSize;
    private float x, y, x_margin = 0, y_margin = 0;

    public static final int X_LEFT = 0;
    public static final int X_CENTER = 1;
    public static final int X_RIGHT = 2;
    public static final int Y_TOP = 0;
    public static final int Y_BOTTOM = 1;

    private int x_position = 1, y_position = 1;

    private int lastPage = 0, firstPage = 1;
    private boolean printTotalPage = false;
    private String prefix = null, sufix = null, separator = "/";

    private PDFont font;

    public RenderPaginator(String input) {
        this.in = input;
        font = PDType1Font.HELVETICA_BOLD;
        fontSize = 12.0f;
        x = (PDRectangle.A4.getUpperRightX() / 2); //PADRÃO CENTRO
        y = (PDRectangle.A4.getLowerLeftY() + 30); //PADRÃO INFERIOR
    }

    /**
     * Define o nome e tamanho da fonte
     * @param fname Nome da fonte sem espaços. Ex.: HELVETICA_BOLD.
     * @param size
     * @throws Exception 
     */
    public void setFont(String fname, float size) throws Exception {
        fontSize = size;
        try {
            Field field = PDType1Font.class.getField(fname);
            Class<?> c = PDType1Font.HELVETICA.getClass();
            font = (PDFont) field.get(c);
        } catch (NoSuchFieldException ex) {
            throw new Exception("Font " + fname + " not found.");
        } catch (SecurityException ex) {
            throw new Exception("Font " + fname + ": invalid.");
        } catch (IllegalArgumentException ex) {
            throw new Exception("Font " + fname + ": invalid name.");
        } catch (IllegalAccessException ex) {
            throw new Exception("Font " + fname + ": internal error.");
        }
    }

    public void setMargin(float mx, float my) {
        this.x_margin = mx;
        this.y_margin = my;
    }

    public void setPosition(int px, int py) {
        this.x_position = px;
        this.y_position = py;
    }

    public void setPrefix(String pre) {
        this.prefix = pre;
    }

    public void setTotalPageVisible(boolean suf) {
        this.printTotalPage = suf;
    }

    public void setLastPage(int last) {
        this.lastPage = last;
    }

    public void setFirstPage(int first) {
        this.firstPage = first;
    }
    
    public void setSeparator(String sep){
        this.separator = sep;
    }

    /**
     * Aplica a paginação do documento PDF informado
     * @param output Endereço relativo ou exato do documento .pdf
     * @throws IOException 
     */
    public void render(String output) throws IOException {

        PDDocument doc = null;
        try {
            File file = new File(this.in);
            doc = PDDocument.load(file);

            PDPageTree allPages = doc.getDocumentCatalog().getPages();
            int totalPages = allPages.getCount();
            if (lastPage == 0) {
                lastPage = totalPages;
            }
            for (int i = (firstPage - 1); i < lastPage; i++) {
                String text = "";
                if (prefix != null) {
                    text = prefix + " ";
                }
                text = text + (i + 1);
                if (printTotalPage) {
                    text = text + separator + totalPages;
                }
                //calcula a posição do texto
                calculatePosition(text);

                PDPage page = (PDPage) allPages.get(i);
                PDPageContentStream footercontentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
                footercontentStream.beginText();
                footercontentStream.setFont(font, fontSize);
                footercontentStream.newLineAtOffset(x, y);
                footercontentStream.showText(text);
                footercontentStream.endText();
                footercontentStream.close();
            }
            doc.save(output);
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }

    /**
     * Calcula a posição do texto usando os caracteres informados
     * @see Ver como fazer para calcular quando a página estiver deitada, e também
     * outros padrões além do A4
     * @param text 
     */
    private void calculatePosition(String text) {
        switch (this.x_position) {
            case RenderPaginator.X_LEFT:
                x = PDRectangle.A4.getLowerLeftX() + x_margin;
                break;
            case RenderPaginator.X_CENTER:
                x = PDRectangle.A4.getUpperRightX() / 2;
                break;
            case RenderPaginator.X_RIGHT: {
                try {
                    x = PDRectangle.A4.getUpperRightX() - (x_margin + (font.getStringWidth(text) / 1000.0f) * fontSize);
                } catch (IOException ex) {
                    Logger.getLogger(RenderPaginator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }

        switch (this.y_position) {
            case RenderPaginator.Y_TOP:
                y = PDRectangle.A4.getUpperRightY() - (y_margin + (font.getFontDescriptor().getCapHeight()) / 1000 * fontSize);
                break;
            case RenderPaginator.Y_BOTTOM: {
                y = PDRectangle.A4.getLowerLeftY() + y_margin;
            }
            break;
        }
    }
}
