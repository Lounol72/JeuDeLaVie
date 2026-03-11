package fr.jeu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import fr.game.Game;

/** Fenêtre principale (JFrame) : contient le panneau, gère fermeture directe. */
public class FenetreJeu extends JFrame {

  private static final long serialVersionUID = 1L;
  private final Game jeu;

  public FenetreJeu(PanelJeu panneauJeu, Game jeu) {
    this.jeu = jeu;
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    add(panneauJeu);
    setTitle("Jeu de la Vie");
    setLocationRelativeTo(null);
    setResizable(true);
    pack();
    setVisible(true);

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            gererFermeture();
          }
        });

    addWindowFocusListener(
        new WindowFocusListener() {
          @Override
          public void windowGainedFocus(WindowEvent e) {}

          @Override
          public void windowLostFocus(WindowEvent e) {}
        });
  }

  public void gererFermeture() {
    jeu.nettoyer();
    System.exit(0);
  }
}
