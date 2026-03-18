package fr.ui.bouton;

import fr.ui.Positionnable;
import fr.utilz.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * Classe de base abstraite pour tous les boutons de l'interface utilisateur.
 *
 * <p>Gère la géométrie (position et dimensions), les états visuels (normal, survol, enfoncé)
 * et fournit un rendu de fond arrondi commun. Les sous-classes implémentent
 * {@link #appliquerAction()} et appellent {@link #dessinerLabel(Graphics, String)} pour
 * afficher leur libellé sans dupliquer la logique de centrage.
 */
public abstract class Bouton implements Positionnable {

  /** Police partagée par tous les boutons (Arial gras 17pt). */
  protected static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 17);
  /** Police compacte utilisée en mode ImGui (Arial plain 12pt). */
  protected static final Font FONT_LABEL_COMPACT = new Font("Arial", Font.PLAIN, 12);

  protected int x;
  protected int y;
  protected int largeur;
  protected int hauteur;
  protected Rectangle delimitation;
  protected boolean sourisSurvol;
  protected boolean sourisEnfonce;

  private static final int ARC = 15;
  private static final int ARC_COMPACT = 4;
  private int arc = ARC;
  private boolean compact = false;
  private RoundRectangle2D.Float rect;
  private final Color[] couleursFond;

  public Bouton(int x, int y, int largeur, int hauteur) {
    this.x = x;
    this.y = y;
    this.largeur = largeur;
    this.hauteur = hauteur;
    creerDelimitation();
    couleursFond = new Color[3];
    couleursFond[0] = Constants.COULEUR_BOUTON_NORMAL;
    couleursFond[1] = Constants.COULEUR_BOUTON_SURVOL;
    couleursFond[2] = Constants.COULEUR_BOUTON_ENFONCE;
  }

  protected void creerDelimitation() {
    delimitation = new Rectangle(x, y, largeur, hauteur);
    rect = new RoundRectangle2D.Float(x, y, largeur, hauteur, arc, arc);
  }

  public void setCompact(boolean c) {
    this.compact = c;
    this.arc = c ? ARC_COMPACT : ARC;
    creerDelimitation();
  }

  public abstract void appliquerAction();

  public void dessiner(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    int idx = sourisSurvol ? (sourisEnfonce ? 2 : 1) : 0;
    g2d.setColor(couleursFond[idx]);
    g2d.fill(rect);
    g2d.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g2d.draw(rect);
  }

  public Rectangle getDelimitation() {
    return delimitation;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
    creerDelimitation();
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
    creerDelimitation();
  }

  public int getLargeur() {
    return largeur;
  }

  public void setLargeur(int largeur) {
    this.largeur = largeur;
    creerDelimitation();
  }

  public int getHauteur() {
    return hauteur;
  }

  public void setHauteur(int hauteur) {
    this.hauteur = hauteur;
    creerDelimitation();
  }

  /** Met à jour toutes les dimensions en une seule fois (évite 4 appels à creerDelimitation). */
  public void setBounds(int x, int y, int largeur, int hauteur) {
    this.x = x;
    this.y = y;
    this.largeur = largeur;
    this.hauteur = hauteur;
    creerDelimitation();
  }

  public boolean isSourisEnfonce() {
    return sourisEnfonce;
  }

  public void setSourisEnfonce(boolean sourisEnfonce) {
    this.sourisEnfonce = sourisEnfonce;
  }

  public boolean isSourisSurvol() {
    return sourisSurvol;
  }

  public void setSourisSurvol(boolean sourisSurvol) {
    this.sourisSurvol = sourisSurvol;
  }

  /**
   * Dessine un libellé texte centré horizontalement et verticalement dans les limites du bouton.
   *
   * <p>Utilise {@link #FONT_LABEL} et la couleur {@code Constants.COULEUR_TEXTE}. Cette méthode
   * doit être appelée depuis {@link #dessiner(Graphics)} des sous-classes, après le rendu du fond.
   *
   * @param g     le contexte graphique courant
   * @param label le texte à afficher
   */
  protected void dessinerLabel(Graphics g, String label) {
    g.setColor(Constants.COULEUR_TEXTE);
    g.setFont(compact ? FONT_LABEL_COMPACT : FONT_LABEL);
    FontMetrics fm = g.getFontMetrics();
    int lw = fm.stringWidth(label);
    int lx = x + (largeur - lw) / 2;
    int ly = y + (hauteur + fm.getAscent()) / 2 - 2;
    g.drawString(label, lx, ly);
  }
}
