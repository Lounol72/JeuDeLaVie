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
}
