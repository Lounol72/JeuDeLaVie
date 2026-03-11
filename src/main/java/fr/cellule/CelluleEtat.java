package fr.cellule;

import fr.visiteur.Visiteur;

// State pattern : chaque etat sait vers quel etat transitionner
public interface CelluleEtat {
  CelluleEtat vit();

  CelluleEtat meurt();

  boolean estVivant();

  void accepte(Visiteur v, Cellule c);
}
