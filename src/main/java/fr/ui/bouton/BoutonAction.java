package fr.ui.bouton;

import java.awt.Graphics;

/**
 * Bouton à libellé fixe déclenchant une action arbitraire via un {@link Runnable}.
 */
public class BoutonAction extends Bouton {

  private final String label;
  private final Runnable action;

  public BoutonAction(int x, int y, int largeur, int hauteur, String label, Runnable action) {
    super(x, y, largeur, hauteur);
    this.label = label;
    this.action = action;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    action.run();
  }
}
