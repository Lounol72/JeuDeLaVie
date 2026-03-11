package fr.visiteur;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.cellule.Cellule;
import fr.commande.CommandeMeurt;
import fr.commande.CommandeVit;
import fr.jeu.JeuDeLaVie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests unitaires pour les règles du {@link VisiteurDayNight} (Day & Night B3678/S34678).
 *
 * <p>Règles vérifiées :
 * <ul>
 *   <li>Une cellule vivante survit avec 3, 4, 6, 7 ou 8 voisins.</li>
 *   <li>Une cellule vivante meurt avec 0, 1, 2 ou 5 voisins.</li>
 *   <li>Une cellule morte naît avec 3, 6, 7 ou 8 voisins.</li>
 *   <li>Une cellule morte reste morte avec 0, 1, 2, 4 ou 5 voisins.</li>
 * </ul>
 *
 * <p>Des tests paramétrés ({@link ParameterizedTest}) sont utilisés pour couvrir efficacement
 * les multiples valeurs valides et invalides sans dupliquer le code de test.
 */
class VisiteurDayNightTest {

  private JeuDeLaVie mockJeu;
  private VisiteurDayNight visiteur;
  private Cellule mockCellule;

  @BeforeEach
  void init() {
    mockJeu = mock(JeuDeLaVie.class);
    visiteur = new VisiteurDayNight(mockJeu);
    mockCellule = mock(Cellule.class);
  }

  // -------------------------------------------------------------------------
  // Cellule VIVANTE — valeurs de survie : {3, 4, 6, 7, 8}
  // -------------------------------------------------------------------------

  /**
   * Une cellule vivante survit lorsque son nombre de voisins appartient à {3, 4, 6, 7, 8}.
   * Aucune commande ne doit être émise.
   */
  @ParameterizedTest(name = "voisins={0} → survit")
  @ValueSource(ints = {3, 4, 6, 7, 8})
  void celluleVivante_voisinsSurvie_survit(int voisins) {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(voisins);
    visiteur.visiteCelluleVivante(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }

  /**
   * Une cellule vivante meurt lorsque son nombre de voisins n'appartient pas à {3, 4, 6, 7, 8}.
   */
  @ParameterizedTest(name = "voisins={0} → meurt")
  @ValueSource(ints = {0, 1, 2, 5})
  void celluleVivante_voisinsMort_meurt(int voisins) {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(voisins);
    visiteur.visiteCelluleVivante(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeMeurt.class));
  }

  // -------------------------------------------------------------------------
  // Cellule MORTE — valeurs de naissance : {3, 6, 7, 8}
  // -------------------------------------------------------------------------

  /**
   * Une cellule morte naît lorsque son nombre de voisins appartient à {3, 6, 7, 8}.
   */
  @ParameterizedTest(name = "voisins={0} → naît")
  @ValueSource(ints = {3, 6, 7, 8})
  void celluleMorte_voisinsNaissance_nait(int voisins) {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(voisins);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeVit.class));
  }

  /**
   * Une cellule morte reste morte lorsque son nombre de voisins n'appartient pas à {3, 6, 7, 8}.
   * Aucune commande ne doit être émise.
   */
  @ParameterizedTest(name = "voisins={0} → reste morte")
  @ValueSource(ints = {0, 1, 2, 4, 5})
  void celluleMorte_voisinsNonNaissance_resteMorte(int voisins) {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(voisins);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }

  // -------------------------------------------------------------------------
  // Cas limites
  // -------------------------------------------------------------------------

  /**
   * Vérifie que la valeur limite haute (8 voisins) déclenche bien une naissance pour une cellule
   * morte, conformément à la règle B8 de Day & Night.
   */
  @Test
  void celluleMorte_8voisins_nait() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(8);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeVit.class));
  }

  /**
   * Vérifie que 4 voisins (présent dans la règle de survie mais absent de la règle de naissance)
   * ne déclenche pas de naissance pour une cellule morte.
   */
  @Test
  void celluleMorte_4voisins_resteMorte() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(4);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }
}
