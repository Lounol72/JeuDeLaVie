package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

public class VisiteurHighLife extends Visiteur {

  public VisiteurHighLife(JeuDeLaVie jeu) {
    super(jeu);
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    int voisins = c.nbVoisinsVivants(jeu);
    if (voisins != 2 && voisins != 3) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    int voisins = c.nbVoisinsVivants(jeu);
    if (voisins == 3 || voisins == 6) jeu.ajouteCommande(new CommandeVit(c));
  }
}
