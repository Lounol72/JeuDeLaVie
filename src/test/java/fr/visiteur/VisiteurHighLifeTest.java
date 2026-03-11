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

class VisiteurHighLifeTest {

  private JeuDeLaVie mockJeu;
  private VisiteurHighLife visiteur;
  private Cellule mockCellule;

  @BeforeEach
  void init() {
    mockJeu = mock(JeuDeLaVie.class);
    visiteur = new VisiteurHighLife(mockJeu);
    mockCellule = mock(Cellule.class);
  }

  @Test
  void celluleVivante_1voisin_meurt() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(1);
    visiteur.visiteCelluleVivante(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeMeurt.class));
  }

  @Test
  void celluleVivante_2voisins_survit() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(2);
    visiteur.visiteCelluleVivante(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }

  @Test
  void celluleVivante_3voisins_survit() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(3);
    visiteur.visiteCelluleVivante(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }

  @Test
  void celluleVivante_4voisins_meurt() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(4);
    visiteur.visiteCelluleVivante(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeMeurt.class));
  }

  @Test
  void celluleMorte_2voisins_resteMorte() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(2);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }

  @Test
  void celluleMorte_3voisins_nait() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(3);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeVit.class));
  }

  @Test
  void celluleMorte_5voisins_resteMorte() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(5);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }

  @Test
  void celluleMorte_6voisins_nait() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(6);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu).ajouteCommande(any(CommandeVit.class));
  }

  @Test
  void celluleMorte_7voisins_resteMorte() {
    when(mockCellule.nbVoisinsVivants(mockJeu)).thenReturn(7);
    visiteur.visiteCelluleMorte(mockCellule);
    verify(mockJeu, never()).ajouteCommande(any());
  }
}
