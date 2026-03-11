package fr.ui.bouton;

import fr.utilz.Constants;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import fr.etats.EtatJeu;

/** Bouton permettant de changer l'état du jeu. */
public class BoutonChangeurEtat extends Bouton {

  private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 16);

  private final EtatJeu action;
  private final String label;

  public BoutonChangeurEtat(int x, int y, int largeur, int hauteur, EtatJeu action, String label) {
    super(x, y, largeur, hauteur);
    this.action = action;
    this.label = label;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    g.setColor(Constants.COULEUR_TEXTE);
    g.setFont(FONT_LABEL);
    FontMetrics fm = g.getFontMetrics();
    int lw = fm.stringWidth(label);
    int lx = x + (largeur - lw) / 2;
    int ly = y + (hauteur + fm.getAscent()) / 2 - 2;
    g.drawString(label, lx, ly);
  }

  @Override
  public void appliquerAction() {
    EtatJeu.setEtatActuel(action);
  }
}
