package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

/**
 * Règle Maze — B3/S12345.
 *
 * <p>Naît avec exactement 3 voisins ; survit avec 1 à 5 voisins. Depuis un état aléatoire, cette
 * règle génère des structures labyrinthiques denses qui se stabilisent rapidement.
 */
public class VisiteurMaze extends Visiteur {

  public VisiteurMaze(JeuDeLaVie jeu) {
    super(jeu);
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    int v = c.nbVoisinsVivants(jeu);
    if (v < 1 || v > 5) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    if (c.nbVoisinsVivants(jeu) == 3) jeu.ajouteCommande(new CommandeVit(c));
  }
}
