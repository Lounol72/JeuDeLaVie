package fr.visiteur;

import fr.jeu.JeuDeLaVie;

/**
 * Fabrique de visiteurs (Factory Method).
 *
 * <p>Centralise la création des visiteurs selon le type de règle choisi.
 * Remplace le pattern Singleton paramétré qui est un anti-pattern : après le premier appel,
 * le paramètre {@code jeu} était silencieusement ignoré.
 */
public class VisiteurFactory {

  private VisiteurFactory() {}

  public static Visiteur creer(TypeVisiteur type, JeuDeLaVie jeu) {
    switch (type) {
      case HIGH_LIFE:        return new VisiteurHighLife(jeu);
      case DAY_NIGHT:        return new VisiteurDayNight(jeu);
      case SEEDS:            return new VisiteurSeeds(jeu);
      case MAZE:             return new VisiteurMaze(jeu);
      case REPLICATOR:       return new VisiteurReplicator(jeu);
      case LIFE_WITHOUT_DEATH: return new VisiteurLifeWithoutDeath(jeu);
      case CLASSIQUE:
      default:               return new VisiteurClassique(jeu);
    }
  }
}
