package fr.ui.bouton;

import fr.jeu.JeuDeLaVie;
import fr.utilz.Constants;
import java.awt.Graphics;

/**
 * Bouton permettant d'avancer la simulation d'une génération lorsqu'elle est en pause.
 *
 * <p>L'action est sans effet si la simulation est en cours d'exécution.
 */
public class BoutonAvancer extends Bouton {

  private final JeuDeLaVie jeu;
  private final String label;

  /**
   * Crée un bouton d'avance pas-à-pas.
   *
   * @param x       position horizontale en pixels
   * @param y       position verticale en pixels
   * @param largeur largeur du bouton en pixels
   * @param hauteur hauteur du bouton en pixels
   * @param label   texte affiché sur le bouton
   * @param jeu     instance du jeu à avancer
   */
  public BoutonAvancer(int x, int y, int largeur, int hauteur, String label, JeuDeLaVie jeu) {
    super(x, y, largeur, hauteur);
    this.label = label;
    this.jeu = jeu;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    // N'avance que si la simulation est effectivement en pause
    if (!Constants.pause) return;
    jeu.calculerGenerationSuivante();
  }
}
