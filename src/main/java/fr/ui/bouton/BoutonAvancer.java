package fr.ui.bouton;

import fr.jeu.JeuDeLaVie;
import fr.utilz.Constants;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class BoutonAvancer extends Bouton {

  private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 16);
  private JeuDeLaVie jeu;
  private final String label;

  public BoutonAvancer(int x, int y, int largeur, int hauteur, String label, JeuDeLaVie jeu) {
    super(x, y, largeur, hauteur);
    this.label = label;
    this.jeu = jeu;
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
    if (!Constants.pause) return;
    jeu.calculerGenerationSuivante();
  }
}
