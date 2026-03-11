package fr.ui.bouton;

import fr.utilz.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/** Base abstraite pour les boutons UI : position, zone de clic, survol/enfoncement. */
public abstract class Bouton {

  protected int x;
  protected int y;
  protected int largeur;
  protected int hauteur;
  protected Rectangle delimitation;
  protected boolean sourisSurvol;
  protected boolean sourisEnfonce;

  private static final int ARC = 15;
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
    rect = new RoundRectangle2D.Float(x, y, largeur, hauteur, ARC, ARC);
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
}
