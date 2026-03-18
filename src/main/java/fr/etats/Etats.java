package fr.etats;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import fr.game.Game;
import fr.ui.CalculBounds;
import fr.ui.Positionnable;
import fr.ui.bouton.Bouton;
import fr.utilz.Constants;

/** Base abstraite pour tous les écrans/états du jeu (menu, grille, paramètres). */
public abstract class Etats implements MethodesEtats {

  protected Game jeu;
  protected ArrayList<Bouton> boutons;

  private final List<Positionnable> elementsPositionnables = new ArrayList<>();
  private final List<CalculBounds>  calculsPositions       = new ArrayList<>();

  protected int largeurLayoutPrecedente = -1;
  protected int hauteurLayoutPrecedente = -1;

  public Etats(Game jeu) {
    this.jeu = jeu;
  }

  /** Indique si le clic souris tombe dans la zone du bouton. */
  public boolean contient(MouseEvent evt, Bouton bouton) {
    return bouton.getDelimitation().contains(evt.getX(), evt.getY());
  }

  /**
   * Enregistre un élément avec son calcul de position et l'applique immédiatement
   * avec les dimensions courantes de la fenêtre.
   */
  protected void enregistrer(Positionnable element, CalculBounds calcul) {
    elementsPositionnables.add(element);
    calculsPositions.add(calcul);
    int[] b = calcul.calculer(Constants.jeu_largeur, Constants.jeu_hauteur);
    element.setBounds(b[0], b[1], b[2], b[3]);
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

  /** Repositionne tous les éléments enregistrés via {@link #enregistrer}. */
  protected void appliquerDisposition(int largeur, int hauteur) {
    for (int i = 0; i < elementsPositionnables.size(); i++) {
      int[] b = calculsPositions.get(i).calculer(largeur, hauteur);
      elementsPositionnables.get(i).setBounds(b[0], b[1], b[2], b[3]);
    }
  }
}
