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
import meupdf.editor.Paginator;

/**
 *
 * @author CledilsonWeb
 */
public class MeuPDF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
//        Paginator mp = new Paginator("apostila2.pdf");
//        try {
//            mp.setFont("HELVETICA_OBLIQUE", 12);
//            mp.setPosition(Paginator.X_RIGHT, Paginator.Y_BOTTOM);
//            mp.setMargin(20f, 20f);
//            mp.setPrefix("Página");
//            mp.setFirstPage(2);
//            mp.setLastPage(5);
//            mp.setTotalPageVisible(true);
//            mp.setSeparator("-");
//            mp.render("apostila_numerada.pdf");
//        } catch (IOException ex) {
//            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        Merger mg = new Merger();
//        try {
//            mg.add("apostila.pdf");
//            mg.add("apostila_numerada.pdf");
//            mg.merge("coisado.pdf");
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MeuPDF.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
}
