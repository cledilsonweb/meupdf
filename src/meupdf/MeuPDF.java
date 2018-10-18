/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meupdf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import meupdf.editor.Merger;
import meupdf.editor.RenderPaginator;

/**
 *
 * @author iW Dev
 */
public class MeuPDF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RenderPaginator mp = new RenderPaginator("apostila2.pdf");
        try {
            mp.setFont("HELVETICA_OBLIQUE", 12);
            mp.setPosition(RenderPaginator.X_RIGHT, RenderPaginator.Y_BOTTOM);
            mp.setMargin(20f, 20f);
            mp.setPrefix("Página");
            mp.setFirstPage(2);
            mp.setLastPage(5);
            mp.setTotalPageVisible(true);
            mp.setSeparator("-");
            mp.render("apostila_numerada.pdf");
        } catch (IOException ex) {
            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Merger mg = new Merger();
        try {
            mg.add("apostila.pdf");
            mg.add("apostila_numerada.pdf");
            mg.merge("coisado.pdf");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
