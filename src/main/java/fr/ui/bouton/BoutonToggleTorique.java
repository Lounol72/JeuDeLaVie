package fr.ui.bouton;

import fr.jeu.JeuDeLaVie;
import java.awt.Graphics;

/**
 * Bouton bascule pour activer ou désactiver la grille torique (wrap-around aux bords).
 */
public class BoutonToggleTorique extends Bouton {

  private final JeuDeLaVie jeu;

  public BoutonToggleTorique(int x, int y, int largeur, int hauteur, JeuDeLaVie jeu) {
    super(x, y, largeur, hauteur);
    this.jeu = jeu;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    dessinerLabel(g, jeu.isTorique() ? "Torique: On" : "Torique: Off");
  }

  @Override
  public void appliquerAction() {
    jeu.setTorique(!jeu.isTorique());
  }
}
