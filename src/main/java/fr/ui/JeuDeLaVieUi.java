package fr.ui;

import fr.jeu.FenetreJeu;
import fr.jeu.JeuDeLaVie;
import fr.jeu.PanelJeu;
import java.awt.Graphics;

public class JeuDeLaVieUi implements Runnable, Observateur {
  private final PanelJeu panel;
  private final FenetreJeu fenetre;
  private Thread boucleJeuThread;

  private final JeuDeLaVie jeu;

  public JeuDeLaVieUi(final JeuDeLaVie jeu) {
    initClasses();
    panel = new PanelJeu(this);
    fenetre = new FenetreJeu(panel, this);
    panel.setFocusable(true);
    panel.requestFocus();

    this.jeu = jeu;
    demarrerJeu();
  }

  private void demarrerJeu() {
    boucleJeuThread = new Thread(this);
    boucleJeuThread.start();
  }

  private void initClasses() {}

  @Override
  public void actualise() {
    panel.repaint();
  }

  public void render(final Graphics g) {
    for (int x = 0; x < jeu.getMaxX(); x++) {
      for (int y = 0; y < jeu.getMaxY(); y++) {
        if (jeu.getGrille(x, y).estVivante()) {
          g.fillOval(x * 3, y * 3, 3, 3);
        }
      }
    }
  }

  @Override
  public void run() {
    // TODO: Implement the run method to play the game
  }
}
