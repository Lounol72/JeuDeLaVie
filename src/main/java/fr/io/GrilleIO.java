package fr.io;

import fr.jeu.JeuDeLaVie;
import fr.visiteur.TypeVisiteur;
import fr.visiteur.VisiteurFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Sérialisation/désérialisation de la grille au format texte sparse.
 *
 * <p>Format :
 * <pre>
 * # JeuDeLaVie Save
 * DIMENSIONS 800 600
 * GENERATION 42
 * RULE CLASSIQUE
 * TORIQUE false
 * CELLS
 * 12,34
 * 56,78
 * </pre>
 */
public class GrilleIO {

  private GrilleIO() {}

  public static void sauvegarder(JeuDeLaVie jeu, String chemin) throws IOException {
    try (PrintWriter pw = new PrintWriter(new FileWriter(chemin))) {
      pw.println("# JeuDeLaVie Save");
      pw.println("DIMENSIONS " + jeu.getMaxX() + " " + jeu.getMaxY());
      pw.println("GENERATION " + jeu.getNumGeneration());
      pw.println("RULE " + jeu.getTypeVisiteurActif().name());
      pw.println("TORIQUE " + jeu.isTorique());
      pw.println("CELLS");
      for (int x = 0; x < jeu.getMaxX(); x++) {
        for (int y = 0; y < jeu.getMaxY(); y++) {
          if (jeu.estCelluleVivante(x, y)) {
            pw.println(x + "," + y);
          }
        }
      }
    }
  }

  public static void charger(JeuDeLaVie jeu, String chemin) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
      int generation = 0;
      TypeVisiteur regle = TypeVisiteur.CLASSIQUE;
      boolean torique = false;
      boolean sectionCells = false;

      jeu.clearGrille();

      String ligne;
      while ((ligne = br.readLine()) != null) {
        ligne = ligne.trim();
        if (ligne.isEmpty() || ligne.startsWith("#")) continue;

        if (ligne.startsWith("GENERATION ")) {
          generation = Integer.parseInt(ligne.substring(11).trim());
        } else if (ligne.startsWith("RULE ")) {
          String nom = ligne.substring(5).trim();
          try {
            regle = TypeVisiteur.valueOf(nom);
          } catch (IllegalArgumentException ignored) {
            regle = TypeVisiteur.CLASSIQUE;
          }
        } else if (ligne.startsWith("TORIQUE ")) {
          torique = Boolean.parseBoolean(ligne.substring(8).trim());
        } else if (ligne.equals("CELLS")) {
          sectionCells = true;
        } else if (sectionCells) {
          String[] parts = ligne.split(",");
          if (parts.length == 2) {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            if (x >= 0 && x < jeu.getMaxX() && y >= 0 && y < jeu.getMaxY()) {
              jeu.getGrille(x, y).vit();
            }
          }
        }
      }

      jeu.setNumGeneration(generation);
      jeu.setTorique(torique);
      jeu.setTypeVisiteurActif(regle);
      jeu.initVisiteur(VisiteurFactory.creer(regle, jeu));
      jeu.notifieObservateur();
    }
  }
}
