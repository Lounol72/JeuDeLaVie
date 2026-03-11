package fr.visiteur;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisiteurDayNight extends Visiteur {
  private List<Integer> listeVivant;
  private List<Integer> listeMort;

  public VisiteurDayNight(JeuDeLaVie jeu) {
    super(jeu);
    listeVivant = new ArrayList<Integer>(Arrays.asList(3, 4, 6, 7, 8));
    listeMort = new ArrayList<Integer>(Arrays.asList(3, 6, 7, 8));
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    if (!listeVivant.contains(c.nbVoisinsVivants(jeu))) jeu.ajouteCommande(new CommandeMeurt(c));
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    if (listeMort.contains(c.nbVoisinsVivants(jeu))) jeu.ajouteCommande(new CommandeVit(c));
  }
}
