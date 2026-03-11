package fr.etats;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import fr.game.Game;
import fr.ui.bouton.Bouton;
import fr.ui.bouton.BoutonChangeurEtat;
import fr.utilz.Constants;

/** État menu principal : boutons Jouer, Paramètres, Quitter. */
public class Menu extends Etats {

  private static final int LARGEUR_BOUTON = 400;
  private static final int HAUTEUR_BOUTON = 55;
  private static final int ESPACEMENT = 64;

  public Menu(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();
    int cx = Constants.jeu_largeur / 2;
    int cy = Constants.jeu_hauteur / 2 - 100;
    int bw = Math.min(LARGEUR_BOUTON, Constants.jeu_largeur / 2);
    int bh = HAUTEUR_BOUTON;
    int gap = ESPACEMENT;

    boutons.add(new BoutonChangeurEtat(cx - bw / 2, cy, bw, bh, EtatJeu.GRILLE, "Jouer"));
    boutons.add(new BoutonChangeurEtat(cx - bw / 2, cy + gap, bw, bh, EtatJeu.PARAMETRES, "Paramètres"));
    boutons.add(new BoutonChangeurEtat(cx - bw / 2, cy + 2 * gap, bw, bh, EtatJeu.QUITTER, "Quitter"));
  }

  @Override
  public void mettreAJour() {}

  @Override
  protected void appliquerDisposition(int largeur, int hauteur) {
    int cx = largeur / 2;
    int cy = hauteur / 2 - 100;
    int bw = Math.min(LARGEUR_BOUTON, largeur / 2);
    int bh = HAUTEUR_BOUTON;
    int gap = ESPACEMENT;

    for (int i = 0; i < boutons.size(); i++) {
      boutons.get(i).setBounds(cx - bw / 2, cy + i * gap, bw, bh);
    }
  }

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    g.setColor(Constants.COULEUR_FOND_MENU);
    g.fillRect(0, 0, Constants.jeu_largeur, Constants.jeu_hauteur);
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
}
