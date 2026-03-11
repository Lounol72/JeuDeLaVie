package fr.visiteur;

import fr.cellule.Cellule;
import fr.jeu.JeuDeLaVie;

/**
 * Décorateur d'observation (Decorator pattern).
 *
 * <p>Enveloppe n'importe quelle {@link RegleJeu} et délègue le traitement des cellules
 * à la règle décorée, tout en comptant les cellules vivantes à des fins de reporting console.
 */
public class VisiteurTerminal extends Visiteur {

  private RegleJeu regleDecoree;
  private int cpt = 0;
  private int generations = 0;

  public VisiteurTerminal(JeuDeLaVie jeu, RegleJeu regleDecoree) {
    super(jeu);
    this.regleDecoree = regleDecoree;
  }

  public void setRegleDecoree(RegleJeu regle) {
    this.regleDecoree = regle;
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    regleDecoree.visiteCelluleVivante(c);
    cpt++;
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    regleDecoree.visiteCelluleMorte(c);
  }

  public void nouvelleGeneration() {
    cpt = 0;
    generations++;
  }

  public void reinitialiserCompteur() {
    cpt = 0;
    generations = 0;
  }

  @Override
  public String toString() {
    return "Génération " + generations + " contient " + cpt + " cellules vivantes";
  }
}
