package fr.etats;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import fr.game.Game;
import fr.ui.Bouton;
import fr.utilz.Constants;

/** Base abstraite pour tous les écrans/états du jeu (menu, grille, paramètres). */
public abstract class Etats implements MethodesEtats {

  protected Game jeu;
  protected ArrayList<Bouton> boutons;

  protected int largeurLayoutPrecedente = -1;
  protected int hauteurLayoutPrecedente = -1;

  public Etats(Game jeu) {
    this.jeu = jeu;
  }

  /** Indique si le clic souris tombe dans la zone du bouton. */
  public boolean contient(MouseEvent evt, Bouton bouton) {
    return bouton.getDelimitation().contains(evt.getX(), evt.getY());
  }

  /** Compare les dimensions ; si différentes, applique le layout. */
  protected final void appliquerDispositionSiNecessaire() {
    if (largeurLayoutPrecedente != Constants.jeu_largeur
        || hauteurLayoutPrecedente != Constants.jeu_hauteur) {
      mettreAJourDisposition(Constants.jeu_largeur, Constants.jeu_hauteur);
      largeurLayoutPrecedente = Constants.jeu_largeur;
      hauteurLayoutPrecedente = Constants.jeu_hauteur;
    }
  }

  @Override
  public void mettreAJourDisposition(int largeur, int hauteur) {
    appliquerDisposition(largeur, hauteur);
  }

  /** Applique le positionnement des éléments UI pour les dimensions données. */
  protected abstract void appliquerDisposition(int largeur, int hauteur);
}
