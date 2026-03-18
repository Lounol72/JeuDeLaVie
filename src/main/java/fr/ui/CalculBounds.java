package fr.ui;

/** Lambda calculant les bounds [x, y, largeur, hauteur] en fonction des dimensions de la fenêtre. */
@FunctionalInterface
public interface CalculBounds {
  int[] calculer(int largeur, int hauteur);
}
