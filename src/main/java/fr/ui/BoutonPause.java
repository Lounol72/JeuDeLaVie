package fr.ui;

import fr.utilz.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class BoutonPause extends Bouton {
  private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 16);
  private boolean etat;
  private final String label;

  public BoutonPause(int x, int y, int largeur, int hauteur, boolean etatDefaut, String label) {
    super(x, y, largeur, hauteur);
    etat = etatDefaut;
    this.label = label;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    g.setColor(Color.BLACK);
    g.setFont(FONT_LABEL);
    FontMetrics fm = g.getFontMetrics();
    int lw = fm.stringWidth(label);
    int lx = x + (largeur - lw) / 2;
    int ly = y + (hauteur + fm.getAscent()) / 2 - 2;
    g.drawString(label, lx, ly);
  }

  @Override
  public void appliquerAction() {
    Constants.pause = !etat;
    etat = Constants.pause;
  }
}

