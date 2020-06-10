package meupdf.editor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author cledilsonweb
 */
public class Paginator {

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
    private float scale;

    public Paginator(String input) {
        this.in = input;
        font = PDType1Font.HELVETICA_BOLD;
        fontSize = 12.0f;
        x = (PDRectangle.A4.getUpperRightX() / 2); //PADRÃO CENTRO
        y = (PDRectangle.A4.getLowerLeftY() + 30); //PADRÃO INFERIOR
    }

    /**
     * Define o nome e tamanho da fonte
     *
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

    public void setSeparator(String sep) {
        this.separator = sep;
    }

    /**
     * Aplica a paginação do documento PDF informado
     *
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

                PDPage page = (PDPage) allPages.get(i);
                PDPageContentStream footercontentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);

                //calcula a posição do texto
                calculatePosition(text, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                //recalcula o tamanho da fonte de acordo a proporção da página em relação a uma página A4
                float tempFontSize = fontSize * scale;
                footercontentStream.beginText();
                footercontentStream.setFont(font, tempFontSize);
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
     *
     * @see Ver como fazer para calcular quando a página estiver deitada, e
     * também outros padrões além do A4
     * @param text
     */
    private void calculatePosition(String text, float w, float h) {

        scale = w / PDRectangle.A4.getUpperRightX();

        switch (this.x_position) {
            case Paginator.X_LEFT:
                x = 0 + (x_margin * scale);
                break;
            case Paginator.X_CENTER:
                x = w / 2;
                break;
            case Paginator.X_RIGHT: {
                try {
                    x = w - ((x_margin * scale) + (font.getStringWidth(text) / 1000.0f) * (fontSize * scale));
                } catch (IOException ex) {
                    Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }

        switch (this.y_position) {
            case Paginator.Y_TOP:
                y = h - ((y_margin * scale) + ((font.getFontDescriptor().getCapHeight()) / 1000 * (fontSize * scale)));
                break;
            case Paginator.Y_BOTTOM: {
                y = 0 + (y_margin * scale);
            }
            break;
        }
    }
}
