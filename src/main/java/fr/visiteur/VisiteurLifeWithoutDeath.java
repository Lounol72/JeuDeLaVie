package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

/**
 * Règle Life Without Death — B3/S012345678.
 *
 * <p>Naît avec exactement 3 voisins (comme Conway), mais ne meurt jamais quel que soit son
 * nombre de voisins. La population croît indéfiniment et forme des structures denses.
 * Illustre le rôle fondamental de la mort dans l'équilibre du Jeu de la Vie classique.
 */
public class VisiteurLifeWithoutDeath extends Visiteur {

  public VisiteurLifeWithoutDeath(JeuDeLaVie jeu) {
    super(jeu);
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    // Survie inconditionnelle — aucune commande émise.
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    if (c.nbVoisinsVivants(jeu) == 3) jeu.ajouteCommande(new CommandeVit(c));
  }
}
