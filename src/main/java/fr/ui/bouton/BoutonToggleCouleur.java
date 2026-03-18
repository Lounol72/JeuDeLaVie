package fr.ui.bouton;

import fr.jeu.JeuDeLaVie;
import java.awt.Graphics;

/**
 * Bouton bascule pour activer ou désactiver le mode couleur des cellules.
 *
 * <p>Le libellé reflète l'état courant : {@code "Couleur: On"} ou {@code "Couleur: Off"}.
 */
public class BoutonToggleCouleur extends Bouton {

  private final JeuDeLaVie jeu;

  public BoutonToggleCouleur(int x, int y, int largeur, int hauteur, JeuDeLaVie jeu) {
    super(x, y, largeur, hauteur);
    this.jeu = jeu;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    String label = jeu.isCouleurActive() ? "Couleur: On" : "Couleur: Off";
    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    jeu.setCouleurActive(!jeu.isCouleurActive());
  }
}
