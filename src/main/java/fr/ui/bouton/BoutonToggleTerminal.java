package fr.ui.bouton;

import fr.jeu.JeuDeLaVie;
import fr.utilz.Constants;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/** Bouton toggle pour activer/désactiver l'affichage terminal (stats console). */
public class BoutonToggleTerminal extends Bouton {

  private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 16);

  private final JeuDeLaVie jeu;

  public BoutonToggleTerminal(int x, int y, int largeur, int hauteur, JeuDeLaVie jeu) {
    super(x, y, largeur, hauteur);
    this.jeu = jeu;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);
    g.setColor(Constants.COULEUR_TEXTE);
    g.setFont(FONT_LABEL);
    String label = jeu.isTerminalActif() ? "Terminal: On" : "Terminal: Off";
    FontMetrics fm = g.getFontMetrics();
    int lw = fm.stringWidth(label);
    int lx = x + (largeur - lw) / 2;
    int ly = y + (hauteur + fm.getAscent()) / 2 - 2;
    g.drawString(label, lx, ly);
  }

  @Override
  public void appliquerAction() {
    jeu.setTerminalActif(!jeu.isTerminalActif());
  }
}
