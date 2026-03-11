package fr.ui.bouton;

import fr.jeu.JeuDeLaVie;
import java.awt.Graphics;

/**
 * Bouton bascule pour activer ou désactiver l'affichage des statistiques en console.
 *
 * <p>Le libellé reflète l'état courant du terminal : {@code "Terminal: On"} ou
 * {@code "Terminal: Off"}.
 */
public class BoutonToggleTerminal extends Bouton {

  private final JeuDeLaVie jeu;

  /**
   * Crée un bouton de bascule du terminal console.
   *
   * @param x       position horizontale en pixels
   * @param y       position verticale en pixels
   * @param largeur largeur du bouton en pixels
   * @param hauteur hauteur du bouton en pixels
   * @param jeu     instance du jeu dont on contrôle le terminal
   */
  public BoutonToggleTerminal(int x, int y, int largeur, int hauteur, JeuDeLaVie jeu) {
    super(x, y, largeur, hauteur);
    this.jeu = jeu;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    // Le libellé est dynamique : il reflète l'état courant du terminal
    String label = jeu.isTerminalActif() ? "Terminal: On" : "Terminal: Off";
    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    jeu.setTerminalActif(!jeu.isTerminalActif());
  }
}
