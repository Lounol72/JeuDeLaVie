package fr.jeu;

import fr.cellule.Cellule;
import fr.cellule.CelluleEtatMort;
import fr.cellule.CelluleEtatVivant;
import fr.commande.Commande;
import fr.memento.GrilleMemento;
import fr.ui.Observable;
import fr.ui.Observateur;
import fr.visiteur.TypeVisiteur;
import fr.visiteur.Visiteur;
import fr.visiteur.VisiteurCouleur;
import fr.visiteur.VisiteurTerminal;
import java.awt.Color;
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
  private VisiteurTerminal terminal = null;
  private VisiteurCouleur visiteurCouleur = null;

  /** Si true, le terminal affiche les stats en console à chaque génération. */
  private volatile boolean terminalActif = false;

  private volatile boolean couleurActive = false;
  private volatile boolean torique = false;

  private TypeVisiteur typeVisiteurActif = TypeVisiteur.CLASSIQUE;

  private int numGeneration = 0;
  private int population = 0;

  /** Constructeur du jeu de la vie, initialise le jeu avec les variables par "Défaut". */
  public JeuDeLaVie() {
    maxX = 2000;
    maxY = 1000;
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
  public void initialiseGrille() {
    Random rnd = new Random(System.currentTimeMillis());
    grille = new Cellule[maxX][maxY];
    numGeneration = 0;
    int pop = 0;
    for (int i = 0; i < grille.length; i++) {
      for (int j = 0; j < grille[i].length; j++) {
        boolean vivante = rnd.nextBoolean();
        grille[i][j] =
            new Cellule(
                vivante ? CelluleEtatVivant.getInstance() : CelluleEtatMort.getInstance(), i, j);
        if (vivante) pop++;
      }
    }
    population = pop;
  }

  /**
   * Récupère une cellule de la grille par rapport aux coordonnées.
   *
   * @param x int position
   * @param y int position
   * @return Cellule
   */
  public Cellule getGrille(int x, int y) {
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
    Visiteur regle;
    if (couleurActive) {
      regle = visiteurCouleur; // couleur → terminal → visiteur
    } else if (terminalActif) {
      regle = terminal; // terminal → visiteur
    } else {
      regle = visiteur;
    }
    int pop = 0;
    for (Cellule[] list : grille) {
      for (Cellule cellule : list) {
        cellule.accepte(regle);
        if (cellule.estVivante()) pop++;
      }
    }
    population = pop;
  }

  /** Remet toutes les cellules à l'état mort et réinitialise les compteurs. */
  public void clearGrille() {
    numGeneration = 0;
    population = 0;
    for (Cellule[] col : grille) {
      for (Cellule cell : col) {
        cell.meurt();
      }
    }
    notifieObservateur();
  }

  public void calculerGenerationSuivante() {
    numGeneration++;
    distribueVisiteur();
    executeCommandes();
    notifieObservateur();
    if (terminalActif && terminal != null) {
      System.out.println(terminal);
      terminal.nouvelleGeneration();
    }
  }

  public boolean isTerminalActif() {
    return terminalActif;
  }

  public void setTerminalActif(boolean actif) {
    terminalActif = actif;
  }

  public boolean isTorique() {
    return torique;
  }

  public void setTorique(boolean t) {
    torique = t;
  }

  /** Crée un snapshot immuable de l'état courant de la grille (Memento). */
  public GrilleMemento creerMemento() {
    boolean[][] snap = new boolean[maxX][maxY];
    for (int x = 0; x < maxX; x++) {
      for (int y = 0; y < maxY; y++) {
        snap[x][y] = grille[x][y].estVivante();
      }
    }
    return new GrilleMemento(snap, numGeneration);
  }

  /** Restaure la grille à partir d'un snapshot Memento et notifie l'observateur. */
  public void restaurerMemento(GrilleMemento m) {
    boolean[][] snap = m.getEtat();
    for (int x = 0; x < maxX; x++) {
      for (int y = 0; y < maxY; y++) {
        if (snap[x][y]) grille[x][y].vit();
        else grille[x][y].meurt();
      }
    }
    numGeneration = m.getNumGeneration();
    notifieObservateur();
  }

  public void setNumGeneration(int n) {
    numGeneration = n;
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
    if (terminal == null) {
      terminal = new VisiteurTerminal(this, v);
    } else {
      terminal.setRegleDecoree(v);
      terminal.reinitialiserCompteur();
    }
    if (visiteurCouleur == null) {
      visiteurCouleur = new VisiteurCouleur(this, terminal);
    } else {
      visiteurCouleur.setRegleDecoree(terminal);
    }
  }

  public boolean isCouleurActive() {
    return couleurActive;
  }

  public void setCouleurActive(boolean actif) {
    couleurActive = actif;
    if (actif && visiteurCouleur != null) {
      visiteurCouleur.rafraichirCouleurs(this);
    }
  }

  public Color getCouleurCellule(int x, int y) {
    return visiteurCouleur != null ? visiteurCouleur.getCouleur(x, y) : null;
  }

  public TypeVisiteur getTypeVisiteurActif() {
    return typeVisiteurActif;
  }

  public void setTypeVisiteurActif(TypeVisiteur t) {
    typeVisiteurActif = t;
  }

  public int getNumGeneration() {
    return numGeneration;
  }

  public int getPopulation() {
    return population;
  }
}
