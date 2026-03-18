package fr.memento;

/**
 * Snapshot immuable de l'état de la grille (pattern Memento).
 */
public class GrilleMemento {

  private final boolean[][] etat;
  private final int numGeneration;

  public GrilleMemento(boolean[][] etat, int numGeneration) {
    this.etat = etat;
    this.numGeneration = numGeneration;
  }

  public boolean[][] getEtat() {
    return etat;
  }

  public int getNumGeneration() {
    return numGeneration;
  }
}
