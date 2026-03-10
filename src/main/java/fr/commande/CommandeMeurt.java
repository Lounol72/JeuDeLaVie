package fr.commande;

import fr.cellule.Cellule;

public class CommandeMeurt extends Commande {

  /** */
  public CommandeMeurt(Cellule c) {
    this.cellule = c;
  }

  public void execute() {
    cellule.meurt();
  }
}

