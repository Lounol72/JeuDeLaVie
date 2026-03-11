package fr.game;

import fr.etats.MethodesEtats;
import fr.jeu.JeuDeLaVie;
import fr.jeu.PanelJeu;

/** Interface du contrôleur de jeu ; évite les dépendances circulaires. */
public interface Game {

  MethodesEtats getEtatCourant();

  PanelJeu getPanneauJeu();

  JeuDeLaVie getJeuDeLaVie();

  void nettoyer();
}
