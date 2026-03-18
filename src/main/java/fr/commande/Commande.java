package fr.commande;

import fr.cellule.Cellule;

public abstract class Commande {

  protected Cellule cellule;

  public abstract void executer();
}
