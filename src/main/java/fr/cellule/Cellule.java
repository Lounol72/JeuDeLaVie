package fr.cellule;

import fr.jeu.JeuDeLaVie;
import fr.visiteur.RegleJeu;

public class Cellule {
  private CelluleEtat etat;

  private int posX;
  private int posY;

  public Cellule() {
    // Par defaut, une cellule nait morte
    posX = 0;
    posY = 0;
    this.etat = CelluleEtatMort.getInstance();
  }

  public Cellule(CelluleEtat etat, int x, int y) {
    this.posX = x;
    this.posY = y;
    this.etat = etat;
  }

  public Cellule(Cellule other) {
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

  public int nbVoisinsVivants(JeuDeLaVie jeuDeLaVie) {
    int compteur = 0;
    int maxX = jeuDeLaVie.getMaxX();
    int maxY = jeuDeLaVie.getMaxY();
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        if (dx == 0 && dy == 0) {
          continue;
        }
        int nx = posX + dx;
        int ny = posY + dy;
        if (nx < 0 || nx >= maxX || ny < 0 || ny >= maxY) {
          continue;
        }
        if (jeuDeLaVie.getGrille(nx, ny).estVivante()) {
          compteur++;
        }
      }
    }
    return compteur;
  }

  @Override
  public String toString() {
    return "Cellule " + posX + "|" + posY + "est " + (estVivante() ? "vivante" : "morte");
  }

  public void accepte(RegleJeu regle) {
    if (estVivante()) {
      regle.visiteCelluleVivante(this);
    } else {
      regle.visiteCelluleMorte(this);
    }
  }
}
