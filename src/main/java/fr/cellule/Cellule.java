package fr.cellule;

import fr.jeu.JeuDeLaVie;

public class Cellule
{
  private CelluleEtat etat;

  private int x, y;

  public Cellule()
  {
    // Par defaut, une cellule nait morte
    x = 0;
    y = 0;
    this.etat =  CelluleEtatMort.getInstance();
  }

  public Cellule(CelluleEtat etat, int x, int y)
  {
    this.x = x;
    this.y = y;
    this.etat = etat;
  }

  public Cellule(Cellule other){
    this.etat = other.etat;
    this.x = other.x;
    this.y = other.y;
  }

  public void vit()
  {
    etat = etat.vit();
  }

  public void meurt()
  {
    etat = etat.meurt();
  }

  public boolean estVivante()
  {
    return etat.estVivant();
  }

  public int nbVoisinsVivants(JeuDeLaVie jeuDeLaVie)
  {
    int count = 0;
    for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
            if (dx == 0 && dy == 0) continue; // on ne vérifie la cellule elle-même
            // On vérifie si le voisin est trop grand ou petit
            if (x +dx < 0 || x + dx > jeuDeLaVie.getXMax() || y + dy < 0 || y+dy > jeuDeLaVie.getYMax()) continue;
            if (jeuDeLaVie.getGrille(x + dx, y + dy).estVivante()) count++;
        }
    }
    return count;
  }

  @Override
  public String toString()
  {
    return "Cellule " + x +"|" + y + "est " + (estVivante() ? "vivante" : "morte");
  }
}
