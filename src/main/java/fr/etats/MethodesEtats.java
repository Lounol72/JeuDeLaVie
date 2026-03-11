package fr.etats;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/** Contrat commun à tous les états du jeu : rendu, mise à jour, entrées clavier/souris. */
public interface MethodesEtats {

  void dessiner(Graphics g);

  void mettreAJour();

  void toucheEnfoncee(KeyEvent e);

  void toucheRelachee(KeyEvent e);

  void toucheTapee(KeyEvent e);

  void sourisDeplacee(MouseEvent e);

  void sourisGlissee(MouseEvent e);

  void sourisCliquee(MouseEvent e);

  void sourisEnfoncee(MouseEvent e);

  void sourisRelachee(MouseEvent e);

  default void mettreAJourTextes() {}

  default void mettreAJourDisposition(int largeur, int hauteur) {}

  /**
   * Appelé quand la molette de la souris tourne sur cet état.
   *
   * <p>L'implémentation par défaut est un no-op. Les états qui gèrent le zoom (ex. grille)
   * surchargent cette méthode pour ajuster le niveau de zoom centré sur la position du curseur.
   *
   * @param rotation    valeur de rotation de la molette (positif = vers le bas = zoom arrière)
   * @param x           position X du curseur au moment de l'événement
   * @param y           position Y du curseur au moment de l'événement
   */
  default void moletteTournee(int rotation, int x, int y) {}
}
