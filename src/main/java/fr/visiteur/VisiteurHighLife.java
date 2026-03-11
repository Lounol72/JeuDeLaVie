package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;

public class VisiteurHighLife extends Visiteur {
  private static volatile VisiteurHighLife instance = null;

  private VisiteurHighLife(JeuDeLaVie jeu) {
    super(jeu);
  }

  public static VisiteurHighLife getInstance(JeuDeLaVie jeu) {
    if (instance != null) {
      return instance;
    }
    synchronized (VisiteurHighLife.class) {
      if (instance == null) {
        instance = new VisiteurHighLife(jeu);
      }
      return instance;
    }
  }

  public void visiteCelluleVivante(Cellule c) {
    int voisins = c.nbVoisinsVivants(jeu);
    // Survit si voisins == 2 ou 3
    if (voisins != 3 || voisins != 2) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  public void visiteCelluleMorte(Cellule c) {
    int voisins = c.nbVoisinsVivants(jeu);
    if (voisins == 3 || voisins == 6) jeu.ajouteCommande(new CommandeVit(c));
  }
}

