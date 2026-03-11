package fr.etats;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import fr.game.Game;
import fr.ui.Bouton;
import fr.ui.BoutonChangeurEtat;
import fr.utilz.Constants;

/** État paramètres (stub) : bouton Retour. */
public class Parametres extends Etats {

  private static final int LARGEUR_BOUTON = 200;
  private static final int HAUTEUR_BOUTON = 44;

  public Parametres(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();
    boutons.add(new BoutonChangeurEtat(50, Constants.jeu_hauteur - 130, LARGEUR_BOUTON, HAUTEUR_BOUTON, EtatJeu.MENU, "Retour"));
  }

  @Override
  public void mettreAJour() {}

  @Override
  protected void appliquerDisposition(int largeur, int hauteur) {
    boutons.get(0).setBounds(50, hauteur - 130, LARGEUR_BOUTON, HAUTEUR_BOUTON);
  }

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    g.setColor(Color.GRAY);
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
