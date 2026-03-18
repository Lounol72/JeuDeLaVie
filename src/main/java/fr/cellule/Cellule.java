package fr.cellule;

import fr.jeu.JeuDeLaVie;
import fr.visiteur.RegleJeu;

public class Cellule {
  private CelluleEtat etat;

  private final int posX;
  private final int posY;
  private int nbVoisinsVivants = 0;

  public Cellule() {
    // Par defaut, une cellule nait morte
    posX = 0;
    posY = 0;
    this.etat = CelluleEtatMort.getInstance();
  }

  public Cellule(final CelluleEtat etat, final int x, final int y) {
    this.posX = x;
    this.posY = y;
    this.etat = etat;
  }

  public Cellule(final Cellule other) {
    this.etat = other.etat;
    this.posX = other.posX;
    this.posY = other.posY;
  }

  public void vit() {
    etat = etat.vit();
  }

  public void meurt() {
    etat = etat.meurt();
  }

  public boolean estVivante() {
    return etat.estVivant();
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public int nbVoisinsVivants(final JeuDeLaVie jeuDeLaVie) {
    int compteur = 0;
    final int maxX = jeuDeLaVie.getMaxX();
    final int maxY = jeuDeLaVie.getMaxY();
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        if (dx == 0 && dy == 0) {
          continue;
        }
        int nx, ny;
        if (jeuDeLaVie.isTorique()) {
          nx = ((posX + dx) % maxX + maxX) % maxX;
          ny = ((posY + dy) % maxY + maxY) % maxY;
        } else {
          nx = posX + dx;
          ny = posY + dy;
          if (nx < 0 || nx >= maxX || ny < 0 || ny >= maxY) continue;
        }
        if (jeuDeLaVie.getGrille(nx, ny).estVivante()) {
          compteur++;
        }
      }
    }
    this.nbVoisinsVivants = compteur;
    return compteur;
  }

  @Override
  public String toString() {
    return "Cellule " + posX + "|" + posY + "est " + (estVivante() ? "vivante" : "morte");
  }

  public void accepte(final RegleJeu regle) {
    if (estVivante()) {
      regle.visiteCelluleVivante(this);
    } else {
      regle.visiteCelluleMorte(this);
    }
  }
}
