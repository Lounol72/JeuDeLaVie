package fr.lemansU;

import fr.jeu.JeuDeLaVie;
import fr.ui.JeuDeLaVieUi;

public class App {

  public static void main(String[] args) {
    JeuDeLaVie jeu = new JeuDeLaVie();
    jeu.initialiseGrille();
    JeuDeLaVieUi ui = new JeuDeLaVieUi(jeu);
    jeu.attacheObservateur(ui);
  }
}
