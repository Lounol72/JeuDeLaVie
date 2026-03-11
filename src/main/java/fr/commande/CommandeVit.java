package fr.commande;

import fr.cellule.Cellule;

public class CommandeVit extends Commande {
  public CommandeVit(Cellule c) {
    this.cellule = c;
  }

  @Override
  public void executer() {
    cellule.vit();
  }
}

