package fr.cellule;

// State pattern : chaque etat sait vers quel etat transitionner
public interface CelluleEtat
{
  CelluleEtat vit();
  CelluleEtat meurt();
  boolean estVivant();
}
