package fr.ui;

/** Élément UI dont la position et les dimensions peuvent être définies via setBounds. */
public interface Positionnable {
  void setBounds(int x, int y, int largeur, int hauteur);
}
