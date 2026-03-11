package fr.commande;

import fr.cellule.Cellule;

public abstract class Commande {

  protected Cellule cellule;

  public void executer() {
    throw new UnsupportedOperationException(
        "Unimplemented method 'executer' for abstract class Commande");
  }
}
