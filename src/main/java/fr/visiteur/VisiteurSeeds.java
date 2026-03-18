package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

/**
 * Règle Seeds — B2/S.
 *
 * <p>Aucune cellule ne survit d'une génération à l'autre. Une cellule morte naît si elle a
 * exactement 2 voisins vivants. Comportement explosif : depuis un motif quelconque, la population
 * explose en quelques générations avant de se stabiliser en oscillateurs ou de s'éteindre.
 */
public class VisiteurSeeds extends Visiteur {

  public VisiteurSeeds(JeuDeLaVie jeu) {
    super(jeu);
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    // Aucune cellule ne survit — toutes meurent implicitement via CommandeMeurt.
    // On n'émet pas de commande : la cellule sera tuée par clearGrille ou par le cycle suivant.
    // En pratique on doit quand même émettre la commande de mort.
    jeu.ajouteCommande(new fr.commande.CommandeMeurt(c));
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    if (c.nbVoisinsVivants(jeu) == 2) jeu.ajouteCommande(new CommandeVit(c));
  }
}
