package fr.visiteur;

import fr.cellule.Cellule;
import fr.jeu.JeuDeLaVie;

public abstract class Visiteur implements RegleJeu {
  protected JeuDeLaVie jeu;

  protected Visiteur(JeuDeLaVie jeu) {
    this.jeu = jeu;
  }

  @Override
  public abstract void visiteCelluleVivante(Cellule c);

  @Override
  public abstract void visiteCelluleMorte(Cellule c);
}
