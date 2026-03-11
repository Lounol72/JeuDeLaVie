package fr.visiteur;

import fr.cellule.Cellule;
import fr.jeu.JeuDeLaVie;

public class VisiteurTerminal extends Visiteur {

  private static volatile VisiteurTerminal instance = null;
  private int cpt = 0;
  private static int generations = 0;

  private VisiteurTerminal(JeuDeLaVie jeu) {
    super(jeu);
  }

  public static VisiteurTerminal getInstance(JeuDeLaVie jeu) {
    if (instance != null) {
      return instance;
    }
    synchronized (VisiteurTerminal.class) {
      if (instance == null) {
        instance = new VisiteurTerminal(jeu);
      }
      return instance;
    }
  }

  public void visiteCelluleVivante(Cellule c) {
    cpt++;
  }

  public void visiteCelluleMorte(Cellule c) {
    return;
  }

  public void nouvelleGeneration() {
    cpt = 0;
    generations++;
  }

  @Override
  public String toString() {
    return "Génération " + generations + " contient " + cpt + " cellules vivantes";
  }
}
