package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

/**
 * Règle Replicator — B1357/S1357.
 *
 * <p>Naissance et survie sur les mêmes valeurs impaires {1, 3, 5, 7}. Toute configuration initiale
 * se réplique en plusieurs copies d'elle-même, produisant des motifs fractals auto-similaires.
 */
public class VisiteurReplicator extends Visiteur {

  public VisiteurReplicator(JeuDeLaVie jeu) {
    super(jeu);
  }

  private static boolean impairActif(int v) {
    return v == 1 || v == 3 || v == 5 || v == 7;
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    if (!impairActif(c.nbVoisinsVivants(jeu))) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    if (impairActif(c.nbVoisinsVivants(jeu))) jeu.ajouteCommande(new CommandeVit(c));
  }
}
