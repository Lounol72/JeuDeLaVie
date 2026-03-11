package fr.etats;

import fr.game.Game;
import fr.jeu.JeuDeLaVie;
import fr.ui.Bouton;
import fr.ui.BoutonChangeurEtat;
import fr.ui.BoutonPause;
import fr.utilz.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/** État grille : affiche le Jeu de la Vie et bouton Retour. */
public class EtatJeuGrille extends Etats {

  private static final int LARGEUR_BOUTON = 200;
  private static final int HAUTEUR_BOUTON = 44;
  private static int maxX;
  private static int maxY;
  private static int tailleCellule;

  /** Nombre de mises à jour avant une nouvelle génération (60 UPS / 12 = 5 gen/s). */
  private static final int UPDATES_PAR_GENERATION = 12;

  private final JeuDeLaVie jeuDeLaVie;
  private int compteurUpdates = 0;

  public EtatJeuGrille(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();
    boutons.add(
        new BoutonChangeurEtat(
            50,
            Constants.jeu_hauteur - 130,
            LARGEUR_BOUTON,
            HAUTEUR_BOUTON,
            EtatJeu.MENU,
            "Retour"));
    boutons.add(
        new BoutonPause(
            50 + LARGEUR_BOUTON + GAP_BOUTONS,
            Constants.jeu_hauteur - 130,
            LARGEUR_BOUTON,
            HAUTEUR_BOUTON,
            false,
            "Pause"));
    this.jeuDeLaVie = jeu.getJeuDeLaVie();
    maxX = jeuDeLaVie.getMaxX();
    maxY = jeuDeLaVie.getMaxY();
    tailleCellule =
        Math.max(1, Math.min(Constants.jeu_largeur / maxX, Constants.jeu_hauteur / maxY));
  }

  @Override
  public void mettreAJour() {
    if (Constants.pause) return;
    compteurUpdates++;
    if (compteurUpdates >= UPDATES_PAR_GENERATION) {
      compteurUpdates = 0;
      jeuDeLaVie.calculerGenerationSuivante();
    }
  }

  private static final int GAP_BOUTONS = 20;

  @Override
  protected void appliquerDisposition(int largeur, int hauteur) {
    int yBoutons = hauteur - 130;
    boutons.get(0).setBounds(50, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
    boutons
        .get(1)
        .setBounds(50 + LARGEUR_BOUTON + GAP_BOUTONS, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
  }

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, Constants.jeu_largeur, Constants.jeu_hauteur);

    if (jeuDeLaVie != null) {
      afficherGrille(g);
    }

    for (Bouton b : boutons) {
      b.dessiner(g);
    }
  }

  @Override
  public void toucheEnfoncee(KeyEvent e) {}

  @Override
  public void toucheRelachee(KeyEvent e) {}

  @Override
  public void toucheTapee(KeyEvent e) {}

  @Override
  public void sourisDeplacee(MouseEvent e) {
    for (Bouton b : boutons) {
      b.setSourisSurvol(contient(e, b));
    }
  }

  @Override
  public void sourisGlissee(MouseEvent e) {}

  @Override
  public void sourisCliquee(MouseEvent e) {}

  @Override
  public void sourisEnfoncee(MouseEvent e) {
    for (Bouton b : boutons) {
      if (contient(e, b)) {
        b.setSourisEnfonce(true);
      }
    }
  }

  @Override
  public void sourisRelachee(MouseEvent e) {
    for (Bouton b : boutons) {
      if (b.isSourisEnfonce() && contient(e, b)) {
        b.appliquerAction();
      }
      b.setSourisEnfonce(false);
    }
  }

  private void afficherGrille(Graphics g) {

    for (int x = 0; x < maxX; x++) {
      for (int y = 0; y < maxY; y++) {
        if (jeuDeLaVie.getGrille(x, y).estVivante()) {
          g.setColor(Color.WHITE);
          g.fillRect(x * tailleCellule, y * tailleCellule, tailleCellule, tailleCellule);
        }
      }
    }
  }
}
