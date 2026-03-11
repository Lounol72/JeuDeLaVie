package fr.ui.bouton;

import fr.etats.EtatJeu;
import java.awt.Graphics;

/**
 * Bouton de navigation permettant de changer l'état courant du jeu (ex. Menu → Grille).
 *
 * <p>Au clic, délègue la transition d'état à {@link EtatJeu#setEtatActuel(EtatJeu)}.
 */
public class BoutonChangeurEtat extends Bouton {

  private final EtatJeu action;
  private final String label;

  /**
   * Crée un bouton de changement d'état.
   *
   * @param x       position horizontale en pixels
   * @param y       position verticale en pixels
   * @param largeur largeur du bouton en pixels
   * @param hauteur hauteur du bouton en pixels
   * @param action  état cible à activer au clic
   * @param label   texte affiché sur le bouton
   */
  public BoutonChangeurEtat(int x, int y, int largeur, int hauteur, EtatJeu action, String label) {
    super(x, y, largeur, hauteur);
    this.action = action;
    this.label = label;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    EtatJeu.setEtatActuel(action);
  }
}
