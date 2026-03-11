package fr.visiteur;

import fr.cellule.Cellule;

/**
 * Interface Strategy représentant un jeu de règles applicable aux cellules.
 *
 * <p>Chaque implémentation encapsule un algorithme d'évolution indépendant
 * (Conway classique, HighLife, Day & Night…), interchangeable à l'exécution.
 */
public interface RegleJeu {
  void visiteCelluleVivante(Cellule c);
  void visiteCelluleMorte(Cellule c);
}
