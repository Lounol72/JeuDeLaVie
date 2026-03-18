package fr.ui.sliders;

import fr.ui.Positionnable;
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
public class Slider implements Positionnable {

  private static final int LARGEUR_PISTE = 8;
  private static final int RAYON_THUMB = 12;

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
    int pisteX = x + (largeur - LARGEUR_PISTE) / 2;
    rectPiste = new RoundRectangle2D.Float(pisteX, y, LARGEUR_PISTE, hauteur, LARGEUR_PISTE, LARGEUR_PISTE);
  }

  private static int clamp(int v, int lo, int hi) {
    return Math.max(lo, Math.min(hi, v));
  }

  /** Dessine le slider (piste + portion active + thumb circulaire). */
  public void dessiner(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int thumbY = valeurVersY(value);
    int pisteX = x + (largeur - LARGEUR_PISTE) / 2;

    // Piste complète
    g2d.setColor(Constants.COULEUR_SLIDER_PISTE);
    g2d.fill(rectPiste);

    // Portion active (du thumb vers le haut = valeur haute)
    Color actif = new Color(
        Constants.COULEUR_CELLULE_VIVANTE.getRed(),
        Constants.COULEUR_CELLULE_VIVANTE.getGreen(),
        Constants.COULEUR_CELLULE_VIVANTE.getBlue(),
        200);
    g2d.setColor(actif);
    int activeH = (y + hauteur) - thumbY;
    if (activeH > 0) {
      g2d.fillRoundRect(pisteX, thumbY, LARGEUR_PISTE, activeH, LARGEUR_PISTE, LARGEUR_PISTE);
    }

    // Thumb circulaire
    boolean survol = sourisSurvol || enGlissement;
    int r = survol ? RAYON_THUMB + 2 : RAYON_THUMB;
    int thumbCx = x + largeur / 2;
    g2d.setColor(survol ? Constants.COULEUR_SLIDER_THUMB_SURVOL : Constants.COULEUR_SLIDER_THUMB);
    g2d.fillOval(thumbCx - r, thumbY - r, r * 2, r * 2);
    g2d.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g2d.drawOval(thumbCx - r, thumbY - r, r * 2, r * 2);
  }

  /** Convertit une valeur en coordonnée Y clampée dans [y+RAYON_THUMB, y+hauteur-RAYON_THUMB]. */
  private int valeurVersY(int v) {
    if (max == min) return y + hauteur / 2;
    double t = (double) (v - min) / (max - min);
    int raw = y + (int) ((1 - t) * hauteur);
    return Math.max(y + RAYON_THUMB, Math.min(y + hauteur - RAYON_THUMB, raw));
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
