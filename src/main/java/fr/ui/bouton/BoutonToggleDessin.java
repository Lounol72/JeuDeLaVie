package fr.ui.bouton;

import java.awt.Graphics;
import java.util.function.Supplier;

/**
 * Bouton bascule générique pour activer ou désactiver un mode via des lambdas.
 *
 * <p>Le libellé reflète l'état courant : {@code "Dessin: On"} ou {@code "Dessin: Off"}.
 */
public class BoutonToggleDessin extends Bouton {

  private final Supplier<Boolean> etatGetter;
  private final Runnable toggle;

  public BoutonToggleDessin(int x, int y, int largeur, int hauteur,
                             Supplier<Boolean> etatGetter, Runnable toggle) {
    super(x, y, largeur, hauteur);
    this.etatGetter = etatGetter;
    this.toggle = toggle;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    dessinerLabel(g, etatGetter.get() ? "Dessin: On" : "Dessin: Off");
  }

  @Override
  public void appliquerAction() {
    toggle.run();
  }
}
