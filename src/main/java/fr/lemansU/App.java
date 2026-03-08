package fr.lemansU;

import fr.jeu.JeuDeLaVie;
/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    JeuDeLaVie jeu = new JeuDeLaVie();
    System.out.println("Jeu créer");
    jeu.initialseGrille();
    System.out.println("Affichage Jeu :");
    System.out.println(jeu);
  }
}
