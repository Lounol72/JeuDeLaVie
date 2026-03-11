package fr.ui.bouton;

import fr.utilz.Constants;
import java.awt.Graphics;

/**
 * Bouton bascule permettant de mettre la simulation en pause ou de la reprendre.
 *
 * <p>Synchronise son état interne avec {@link Constants#pause} à chaque clic.
 */
public class BoutonPause extends Bouton {

  private boolean etat;
  private final String label;

  /**
   * Crée un bouton pause/reprise.
   *
   * @param x          position horizontale en pixels
   * @param y          position verticale en pixels
   * @param largeur    largeur du bouton en pixels
   * @param hauteur    hauteur du bouton en pixels
   * @param etatDefaut état initial ({@code true} = en pause)
   * @param label      texte affiché sur le bouton
   */
  public BoutonPause(int x, int y, int largeur, int hauteur, boolean etatDefaut, String label) {
    super(x, y, largeur, hauteur);
    etat = etatDefaut;
    Constants.pause = etat;
    this.label = label;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    Constants.pause = !etat;
    etat = Constants.pause;
  }
}
