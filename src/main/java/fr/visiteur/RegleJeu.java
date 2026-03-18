package fr.visiteur;

import fr.cellule.Cellule;

/**
 * Interface du pattern Visiteur représentant un jeu de règles applicable aux cellules.
 *
 * <p>Chaque implémentation définit le comportement à appliquer selon l'état de la cellule
 * (Conway classique, HighLife, Day & Night…). {@link fr.cellule.Cellule#accepte(RegleJeu)}
 * dispatche vers {@code visiteCelluleVivante} ou {@code visiteCelluleMorte} selon l'état courant.
 */
public interface RegleJeu {
  void visiteCelluleVivante(Cellule c);
  void visiteCelluleMorte(Cellule c);
}
