package fr.cellule;

import fr.visiteur.Visiteur;

public class CelluleEtatMort implements CelluleEtat {
  private static volatile CelluleEtatMort instance = null;

  private CelluleEtatMort() {}

  public static CelluleEtat getInstance() {
    if (instance != null) return instance;
    synchronized (CelluleEtatMort.class) {
      if (instance == null) instance = new CelluleEtatMort();
    }
    return instance;
  }

  @Override
  public CelluleEtat vit() {
    return CelluleEtatVivant.getInstance();
  }

  @Override
  public CelluleEtat meurt() {
    return this;
  }

  @Override
  public boolean estVivant() {
    return false;
  }

  @Override
  public void accepte(Visiteur v, Cellule c) {
    v.visiteCelluleMorte(c);
  }
}
