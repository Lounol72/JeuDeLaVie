package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

public class VisiteurClassique extends Visiteur {

  private static volatile VisiteurClassique instance = null;

  private VisiteurClassique(JeuDeLaVie jeu) {
    super(jeu);
  }

  public static VisiteurClassique getInstance(JeuDeLaVie jeu) {
    if (instance != null) {
      return instance;
    }
    synchronized (VisiteurClassique.class) {
      if (instance == null) {
        instance = new VisiteurClassique(jeu);
      }
      return instance;
    }
  }

  public void visiteCelluleVivante(Cellule c) {
    int voisins = c.nbVoisinsVivants(jeu);
    if (voisins < 2 || voisins > 3) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  public void visiteCelluleMorte(Cellule c) {
    int voisins = c.nbVoisinsVivants(jeu);
    if (voisins == 3) jeu.ajouteCommande(new CommandeVit(c));
  }
}
