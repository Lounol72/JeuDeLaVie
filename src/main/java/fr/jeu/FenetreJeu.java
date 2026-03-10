package fr.jeu;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import fr.ui.JeuDeLaVieUi;

/** Fenêtre principale (JFrame) : contient le GamePanel, gère fermeture (sauvegarde) et focus. */
public class FenetreJeu extends JFrame {

  private static final long serialVersionUID = 1L;

  public FenetreJeu(PanelJeu gamePanel, JeuDeLaVieUi game) {
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    add(gamePanel);
    setTitle("GL2 Projet");
    setLocationRelativeTo(null);
    setResizable(true);
    pack();
    setVisible(true);

    addWindowFocusListener(
        new WindowFocusListener() {
          @Override
          public void windowGainedFocus(WindowEvent e) {
            System.out.println("Window gained focus");
          }

          @Override
          public void windowLostFocus(WindowEvent e) {
            System.out.println("Window lost focus");
          }
        });
  }
}
