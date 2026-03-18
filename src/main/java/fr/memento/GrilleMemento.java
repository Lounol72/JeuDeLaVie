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

  /** Retourne une copie défensive du snapshot (le Memento reste immuable). */
  public boolean[][] getEtat() {
    boolean[][] copie = new boolean[etat.length][];
    for (int i = 0; i < etat.length; i++) copie[i] = etat[i].clone();
    return copie;
  }

  public int getNumGeneration() {
    return numGeneration;
  }
}
