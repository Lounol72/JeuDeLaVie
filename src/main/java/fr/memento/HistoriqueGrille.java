package fr.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Caretaker Memento : deux piles undo/redo avec capacité maximale.
 */
public class HistoriqueGrille {

  private final Deque<GrilleMemento> pileUndo = new ArrayDeque<>();
  private final Deque<GrilleMemento> pileRedo = new ArrayDeque<>();
  private final int capacite;

  public HistoriqueGrille(int capacite) {
    this.capacite = capacite;
  }

  /** Sauvegarde l'état actuel avant une action ; efface le redo. */
  public void sauvegarder(GrilleMemento m) {
    pileUndo.push(m);
    pileRedo.clear();
    while (pileUndo.size() > capacite) {
      pileUndo.removeLast();
    }
  }

  /**
   * Annule : sauvegarde {@code etatActuel} dans le redo, retourne l'état précédent.
   * Retourne {@code null} si rien à annuler.
   */
  public GrilleMemento annuler(GrilleMemento etatActuel) {
    if (pileUndo.isEmpty()) return null;
    pileRedo.push(etatActuel);
    return pileUndo.pop();
  }

  /**
   * Rétablit : sauvegarde {@code etatActuel} dans le undo, retourne l'état rétabli.
   * Retourne {@code null} si rien à rétablir.
   */
  public GrilleMemento retablir(GrilleMemento etatActuel) {
    if (pileRedo.isEmpty()) return null;
    pileUndo.push(etatActuel);
    return pileRedo.pop();
  }

  public boolean peutAnnuler()  { return !pileUndo.isEmpty(); }
  public boolean peutRetablir() { return !pileRedo.isEmpty(); }
}
