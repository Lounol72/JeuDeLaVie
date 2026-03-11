package fr.ui.sliders;

import fr.utilz.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.function.IntConsumer;

/**
 * Slider vertical personnalisé : dessin via Graphics, interaction souris (clic + glisser).
 * Valeur 0-100, affiché uniquement dans l'état qui le contient.
 */
public class Slider {

  private static final int ARC = 8;
  private static final int EPAISSEUR_THUMB = 12;

  private int x;
  private int y;
  private int largeur;
  private int hauteur;
  private final int min;
  private final int max;
  private int value;
  private Rectangle delimitation;
  private RoundRectangle2D.Float rectPiste;
  private boolean sourisSurvol;
  private boolean enGlissement;

  private final IntConsumer onValueChange;

  /**
   * @param x position X
   * @param y position Y
   * @param largeur largeur du slider
   * @param hauteur hauteur du slider
   * @param min valeur minimale
   * @param max valeur maximale
   * @param value valeur initiale
   * @param onValueChange callback appelé quand la valeur change (peut être null)
   */
  public Slider(
      int x, int y, int largeur, int hauteur, int min, int max, int value, IntConsumer onValueChange) {
    this.x = x;
    this.y = y;
    this.largeur = largeur;
    this.hauteur = hauteur;
    this.min = min;
    this.max = max;
    this.value = clamp(value, min, max);
    this.onValueChange = onValueChange;
    creerFormes();
  }

  private void creerFormes() {
    delimitation = new Rectangle(x, y, largeur, hauteur);
    rectPiste = new RoundRectangle2D.Float(x, y, largeur, hauteur, ARC, ARC);
  }

  private static int clamp(int v, int lo, int hi) {
    return Math.max(lo, Math.min(hi, v));
  }

  /** Dessine le slider (piste + curseur). */
  public void dessiner(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setColor(Constants.COULEUR_SLIDER_PISTE);
    g2d.fill(rectPiste);
    g2d.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g2d.draw(rectPiste);

    int thumbY = valeurVersY(value);
    Color c = (sourisSurvol || enGlissement) ? Constants.COULEUR_SLIDER_THUMB_SURVOL : Constants.COULEUR_SLIDER_THUMB;
    g2d.setColor(c);
    g2d.fillRoundRect(
        x + (largeur - EPAISSEUR_THUMB) / 2,
        thumbY - EPAISSEUR_THUMB / 2,
        EPAISSEUR_THUMB,
        EPAISSEUR_THUMB,
        ARC,
        ARC);
    g2d.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g2d.drawRoundRect(
        x + (largeur - EPAISSEUR_THUMB) / 2,
        thumbY - EPAISSEUR_THUMB / 2,
        EPAISSEUR_THUMB,
        EPAISSEUR_THUMB,
        ARC,
        ARC);
  }

  /** Convertit une valeur en coordonnée Y (0 = haut, max = bas). */
  private int valeurVersY(int v) {
    if (max == min) return y + hauteur / 2;
    double t = (double) (v - min) / (max - min);
    return y + (int) ((1 - t) * hauteur);
  }

  /** Convertit une coordonnée Y en valeur. */
  private int yVersValeur(int mouseY) {
    int relY = mouseY - y;
    if (hauteur <= 0) return value;
    double t = 1 - (double) relY / hauteur;
    return clamp((int) (min + t * (max - min)), min, max);
  }

  /** Met à jour la valeur (avec callback si changement). */
  public void setValue(int v) {
    int prev = value;
    value = clamp(v, min, max);
    if (value != prev && onValueChange != null) {
      onValueChange.accept(value);
    }
  }

  public int getValue() {
    return value;
  }

  public Rectangle getDelimitation() {
    return delimitation;
  }

  public void setBounds(int x, int y, int largeur, int hauteur) {
    this.x = x;
    this.y = y;
    this.largeur = largeur;
    this.hauteur = hauteur;
    creerFormes();
  }

  public void setSourisSurvol(boolean sur) {
    sourisSurvol = sur;
  }

  public void setEnGlissement(boolean glisse) {
    enGlissement = glisse;
  }

  public boolean isEnGlissement() {
    return enGlissement;
  }

  /** Traite un déplacement de souris pendant le glissement. */
  public void glisserVers(int mouseY) {
    setValue(yVersValeur(mouseY));
  }
}
