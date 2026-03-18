package fr.visiteur;

import fr.cellule.Cellule;
import fr.jeu.JeuDeLaVie;
import java.awt.Color;

/**
 * Décorateur d'observation (Decorator pattern).
 *
 * <p>Enveloppe n'importe quelle {@link RegleJeu} et délègue le traitement des cellules à la règle
 * décorée, tout en calculant la couleur de chaque cellule vivante selon son nombre de voisins.
 */
public class VisiteurCouleur extends Visiteur {

  // Dégradé bleu clair (0 voisin) -> bleu foncé (8 voisins).
  private static final Color[] PALETTE = {
    new Color(0xbfdbfe), // 0 voisins - bleu très clair
    new Color(0x93c5fd), // 1 voisin
    new Color(0x60a5fa), // 2 voisins
    new Color(0x3b82f6), // 3 voisins
    new Color(0x2563eb), // 4 voisins
    new Color(0x1d4ed8), // 5 voisins
    new Color(0x1e40af), // 6 voisins
    new Color(0x1e3a8a), // 7 voisins
    new Color(0x172554), // 8 voisins - bleu très foncé
  };

  private RegleJeu regleDecoree;
  private final Color[][] grilleCouleurs;

  public VisiteurCouleur(JeuDeLaVie jeu, RegleJeu regle) {
    super(jeu);
    this.regleDecoree = regle;
    this.grilleCouleurs = new Color[jeu.getMaxX()][jeu.getMaxY()];
  }

  public void setRegleDecoree(RegleJeu regle) {
    this.regleDecoree = regle;
  }

  @Override
  public void visiteCelluleVivante(Cellule c) {
    regleDecoree.visiteCelluleVivante(c);
    int voisins = c.nbVoisinsVivants(jeu);
    grilleCouleurs[c.getPosX()][c.getPosY()] = PALETTE[voisins];
  }

  @Override
  public void visiteCelluleMorte(Cellule c) {
    regleDecoree.visiteCelluleMorte(c);
    // La couleur n'est pas effacée : les cellules mortes ne sont pas rendues,
    // et une cellule renaissante conserve sa dernière couleur jusqu'à la prochaine
    // génération où elle sera revisitée comme vivante.
  }

  /** Retourne la couleur de la cellule en (x, y), ou {@code null} si jamais visitée vivante. */
  public Color getCouleur(int x, int y) {
    return grilleCouleurs[x][y];
  }

  /**
   * Pré-calcule immédiatement les couleurs de toutes les cellules vivantes à partir de leur nombre
   * de voisins courant. À appeler quand le mode couleur est activé alors que la simulation est en
   * pause (grilleCouleurs encore vide).
   */
  public void rafraichirCouleurs(JeuDeLaVie jeuRef) {
    for (int x = 0; x < grilleCouleurs.length; x++) {
      for (int y = 0; y < grilleCouleurs[0].length; y++) {
        if (jeuRef.estCelluleVivante(x, y)) {
          int voisins = jeuRef.getGrille(x, y).nbVoisinsVivants(jeuRef);
          grilleCouleurs[x][y] = PALETTE[voisins];
        }
      }
    }
  }
}
