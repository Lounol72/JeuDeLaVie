package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisiteurDayNight extends Visiteur {
  private static volatile VisiteurDayNight instance = null;
  private List<Integer> listeVivant;
  private List<Integer> listeMort;

  private VisiteurDayNight(JeuDeLaVie jeu) {
    super(jeu);
    listeVivant = new ArrayList<Integer>(Arrays.asList(3, 4, 6, 7, 8));
    listeMort = new ArrayList<Integer>(Arrays.asList(3, 6, 7, 8));
  }

  public static VisiteurDayNight getInstance(JeuDeLaVie jeu) {
    if (instance != null) {
      return instance;
    }
    synchronized (VisiteurDayNight.class) {
      if (instance == null) {
        instance = new VisiteurDayNight(jeu);
      }
      return instance;
    }
  }

  public void visiteCelluleVivante(Cellule c) {
    if (!listeVivant.contains(c.nbVoisinsVivants(jeu))) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  public void visiteCelluleMorte(Cellule c) {
    if (listeMort.contains(c.nbVoisinsVivants(jeu))) jeu.ajouteCommande(new CommandeVit(c));
  }
}
