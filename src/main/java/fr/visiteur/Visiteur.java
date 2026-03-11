package fr.visiteur;

import fr.cellule.Cellule;
import fr.jeu.JeuDeLaVie;

public abstract class Visiteur implements RegleJeu {
  protected JeuDeLaVie jeu;

  protected Visiteur(JeuDeLaVie jeu) {
    this.jeu = jeu;
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    throw new UnsupportedOperationException(
        "Unimplemented method 'visiteCelluleVivante' for abstract class Visiteur");
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    throw new UnsupportedOperationException(
        "Unimplemented method 'visiteCelluleMorte' for abstract class Visiteur");
  }
}
