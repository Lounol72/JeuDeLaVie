package fr.cellule;

import fr.jeu.JeuDeLaVie;

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
    int count = 0;
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        if (dx == 0 && dy == 0) {
          continue;
        } // on ne vérifie la cellule elle-même
        // On vérifie si le voisin est trop grand ou petit
        if (posX + dx < 0
            || posX + dx > jeuDeLaVie.getMaxX()
            || posY + dy < 0
            || posY + dy > jeuDeLaVie.getMaxY()) {
          continue;
        }
        if (jeuDeLaVie.getGrille(posX + dx, posY + dy).estVivante()) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public String toString() {
    return "Cellule " + posX + "|" + posY + "est " + (estVivante() ? "vivante" : "morte");
  }
}
