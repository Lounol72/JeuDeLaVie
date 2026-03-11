package fr.cellule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.jeu.JeuDeLaVie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour {@link Cellule#nbVoisinsVivants(JeuDeLaVie)}.
 *
 * <p>La grille simulée est de taille 3×3 afin de couvrir à la fois les cellules en bordure et les
 * cellules centrales. {@link JeuDeLaVie} est mocké pour contrôler précisément quelles cellules
 * voisines sont considérées comme vivantes.
 */
class CelluleTest {

  /** Taille de la grille de test (3×3). */
  private static final int TAILLE = 3;

  private JeuDeLaVie mockJeu;
  private Cellule celluleMorte;
  private Cellule celluleVivante;

  /** Initialise le mock de grille et deux cellules de référence avant chaque test. */
  @BeforeEach
  void init() {
    mockJeu = mock(JeuDeLaVie.class);
    when(mockJeu.getMaxX()).thenReturn(TAILLE);
    when(mockJeu.getMaxY()).thenReturn(TAILLE);

    celluleMorte = new Cellule(CelluleEtatMort.getInstance(), 1, 1);
    celluleVivante = new Cellule(CelluleEtatVivant.getInstance(), 1, 1);
  }

  // -------------------------------------------------------------------------
  // Cas : aucun voisin vivant
  // -------------------------------------------------------------------------

  /**
   * Une cellule centrale sans aucun voisin vivant doit retourner 0.
   */
  @Test
  void nbVoisinsVivants_aucunVoisin_retourneZero() {
    Cellule voisinMort = new Cellule(CelluleEtatMort.getInstance(), 0, 0);
    when(mockJeu.getGrille(anyInt(), anyInt())).thenReturn(voisinMort);

    assertEquals(0, celluleVivante.nbVoisinsVivants(mockJeu));
  }

  // -------------------------------------------------------------------------
  // Cas : tous les voisins vivants
  // -------------------------------------------------------------------------

  /**
   * Une cellule centrale entourée de 8 voisins vivants doit retourner 8.
   */
  @Test
  void nbVoisinsVivants_tousVoisinsVivants_retourneHuit() {
    Cellule voisinVivant = new Cellule(CelluleEtatVivant.getInstance(), 0, 0);
    when(mockJeu.getGrille(anyInt(), anyInt())).thenReturn(voisinVivant);

    assertEquals(8, celluleVivante.nbVoisinsVivants(mockJeu));
  }

  // -------------------------------------------------------------------------
  // Cas : nombre partiel de voisins vivants
  // -------------------------------------------------------------------------

  /**
   * Une cellule avec exactement 3 voisins vivants (sur 8 possibles) doit retourner 3.
   *
   * <p>Le mock retourne une cellule vivante pour les positions (0,0), (0,1) et (0,2),
   * et une cellule morte pour toutes les autres.
   */
  @Test
  void nbVoisinsVivants_troisVoisinsVivants_retourneTrois() {
    Cellule mort = new Cellule(CelluleEtatMort.getInstance(), 0, 0);
    Cellule vivant = new Cellule(CelluleEtatVivant.getInstance(), 0, 0);

    // Par défaut : tous les voisins sont morts
    when(mockJeu.getGrille(anyInt(), anyInt())).thenReturn(mort);
    // Trois voisins vivants sur la colonne gauche
    when(mockJeu.getGrille(0, 0)).thenReturn(vivant);
    when(mockJeu.getGrille(0, 1)).thenReturn(vivant);
    when(mockJeu.getGrille(0, 2)).thenReturn(vivant);

    assertEquals(3, celluleVivante.nbVoisinsVivants(mockJeu));
  }

  // -------------------------------------------------------------------------
  // Cas : cellule en coin (bordure)
  // -------------------------------------------------------------------------

  /**
   * Une cellule placée dans le coin (0,0) ne doit compter que ses 3 voisins valides,
   * et ignorer toutes les positions hors-grille.
   */
  @Test
  void nbVoisinsVivants_celluleEnCoin_ignoreBordures() {
    Cellule voisinVivant = new Cellule(CelluleEtatVivant.getInstance(), 0, 0);
    when(mockJeu.getGrille(anyInt(), anyInt())).thenReturn(voisinVivant);

    // La cellule est en (0,0) : seuls (0,1), (1,0) et (1,1) sont valides
    Cellule celluleEnCoin = new Cellule(CelluleEtatVivant.getInstance(), 0, 0);
    assertEquals(3, celluleEnCoin.nbVoisinsVivants(mockJeu));
  }

  // -------------------------------------------------------------------------
  // Cas : cellule sur un bord (pas un coin)
  // -------------------------------------------------------------------------

  /**
   * Une cellule placée sur le bord supérieur (0,1) ne doit compter que 5 voisins au maximum.
   */
  @Test
  void nbVoisinsVivants_celluleSurBord_cinqVoisinsMax() {
    Cellule voisinVivant = new Cellule(CelluleEtatVivant.getInstance(), 0, 0);
    when(mockJeu.getGrille(anyInt(), anyInt())).thenReturn(voisinVivant);

    Cellule celluleSurBord = new Cellule(CelluleEtatVivant.getInstance(), 0, 1);
    assertEquals(5, celluleSurBord.nbVoisinsVivants(mockJeu));
  }
}
