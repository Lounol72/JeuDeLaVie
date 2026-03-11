package fr.jeu;

import fr.cellule.Cellule;
import fr.cellule.CelluleEtatMort;
import fr.cellule.CelluleEtatVivant;
import fr.commande.Commande;
import fr.ui.Observable;
import fr.ui.Observateur;
import fr.visiteur.Visiteur;
import fr.visiteur.VisiteurTerminal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Représente le jeu de la vie.
 *
 * @author Lounol72
 */
public class JeuDeLaVie implements Observable {
  private Cellule[][] grille;
  private int maxX;
  private int maxY;

  private List<Observateur> observateurs;
  private List<Commande> commandes;
  private Visiteur visiteur = null;
  private VisiteurTerminal terminal = VisiteurTerminal.getInstance(this);
  /** Si true, le terminal affiche les stats en console à chaque génération. */
  private volatile boolean terminalActif = false;

  /** Constructeur du jeu de la vie, initialise le jeu avec les variables par "Défaut". */
  public JeuDeLaVie() {
    maxX = 800;
    maxY = 600;
    initClasses();
  }

  private void initClasses() {
    observateurs = new ArrayList<>();
    commandes = new ArrayList<>();
  }

  /**
   * Initialise la grille avec des Cellules d'etat random grâce à la méthode **rnd.nextBoolean** et
   * selon celà nous lui donnons un état vivant ou mort.
   */
  public void initialseGrille() {
    Random rnd = new Random(System.currentTimeMillis());
    grille = new Cellule[maxX][maxY];
    for (int i = 0; i < grille.length; i++) {
      for (int j = 0; j < grille[i].length; j++) {
        grille[i][j] =
            new Cellule(
                rnd.nextBoolean() ? CelluleEtatVivant.getInstance() : CelluleEtatMort.getInstance(),
                i,
                j);
      }
    }
  }

  /**
   * Récupère une cellule de la grille par rapport aux coordonnées.
   *
   * @param x int position
   * @param y int position
   * @return Cellule
   * @throws throw new IndexOutOfBoundsException("Index " + x + " out of bounds for size " + xMax);
   *     Si l'indice x est en dehors du tableau
   * @throws throw new IndexOutOfBoundsException("Index " + y + " out of bounds for size " + yMax);
   *     Si l'indice y est en dehors du tableau
   */
  public Cellule getGrille(int x, int y) {
    // Vérification des inputs
    if (x < 0 || x >= maxX) {
      throw new IndexOutOfBoundsException("Index " + x + " out of bounds for size " + maxX);
    }
    if (y < 0 || y >= maxY) {
      throw new IndexOutOfBoundsException("Index " + y + " out of bounds for size " + maxY);
    }

    return grille[x][y];
  }

  /** Accès direct pour boucle de rendu (x,y supposés valides). */
  public boolean estCelluleVivante(int x, int y) {
    return grille[x][y].estVivante();
  }

  public void ajouteCommande(Commande c) {
    commandes.add(c);
  }

  public void executeCommandes() {
    commandes.forEach(Commande::executer);
    commandes.clear();
  }

  public void distribueVisiteur() {
    for (Cellule[] list : grille) {
      for (Cellule cellule : list) {
        cellule.accepte(visiteur);
        if (terminalActif) {
          cellule.accepte(terminal);
        }
      }
    }
  }

  public void calculerGenerationSuivante() {
    distribueVisiteur();
    executeCommandes();
    notifieObservateur();
    if (terminalActif) {
      System.out.println(terminal);
    }
    terminal.nouvelleGeneration();
  }

  public boolean isTerminalActif() {
    return terminalActif;
  }

  public void setTerminalActif(boolean actif) {
    terminalActif = actif;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < grille.length; i++) {
      for (int j = 0; j < grille[i].length; j++) {
        s.append(grille[i][j].estVivante() ? 'x' : ' ');
      }
      s.append("\n");
    }
    return s.toString();
  }

  public int getMaxX() {
    return maxX;
  }

  public int getMaxY() {
    return maxY;
  }

  @Override
  public void attacheObservateur(Observateur o) {
    observateurs.add(o);
  }

  @Override
  public void detacheObservateur(Observateur o) {
    observateurs.remove(o);
  }

  @Override
  public void notifieObservateur() {
    for (Observateur o : observateurs) {
      o.actualise();
    }
  }

  public void initVisiteur(Visiteur v) {
    visiteur = v;
  }
}
