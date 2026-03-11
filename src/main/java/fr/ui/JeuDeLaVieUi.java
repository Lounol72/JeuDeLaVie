package fr.ui;

import fr.etats.EtatJeu;
import fr.etats.EtatJeuGrille;
import fr.etats.Menu;
import fr.etats.MethodesEtats;
import fr.etats.Parametres;
import fr.game.Game;
import fr.jeu.FenetreJeu;
import fr.jeu.JeuDeLaVie;
import fr.jeu.PanelJeu;
import fr.visiteur.VisiteurClassique;
import java.awt.Graphics;
import java.util.EnumMap;
import java.util.Map;

public class JeuDeLaVieUi implements Runnable, Observateur, Game {

  private final PanelJeu panneauJeu;
  private final FenetreJeu fenetreJeu;
  private Thread threadBoucleJeu;

  private final JeuDeLaVie jeu;

  private static final int TARGET_UPS = 60;
  private static final int TARGET_FPS = 60;

  private int fpsActuel = 0;
  private int upsActuel = 0;
  private boolean flagRepaint = false;

  private final Menu menu;
  private final EtatJeuGrille etatJeuGrille;
  private final Parametres parametres;

  private final Map<EtatJeu, MethodesEtats> etatsParEnum = new EnumMap<>(EtatJeu.class);

  public JeuDeLaVieUi(final JeuDeLaVie jeu) {
    this.jeu = jeu;
    jeu.initVisiteur(new VisiteurClassique(jeu));

    menu = new Menu(this);
    etatsParEnum.put(EtatJeu.MENU, menu);
    etatJeuGrille = new EtatJeuGrille(this);
    etatsParEnum.put(EtatJeu.GRILLE, etatJeuGrille);
    parametres = new Parametres(this);
    etatsParEnum.put(EtatJeu.PARAMETRES, parametres);

    panneauJeu = new PanelJeu(this);
    fenetreJeu = new FenetreJeu(panneauJeu, this);
    panneauJeu.setFocusable(true);
    panneauJeu.requestFocus();

    EtatJeu.setEtatActuel(EtatJeu.MENU);
    demarrerJeu();
  }

  private void demarrerJeu() {
    threadBoucleJeu = new Thread(this);
    threadBoucleJeu.start();
  }

  @Override
  public MethodesEtats getEtatCourant() {
    EtatJeu e = EtatJeu.getEtatActuel();
    if (e == EtatJeu.QUITTER) {
      fenetreJeu.gererFermeture();
      return etatsParEnum.get(EtatJeu.MENU);
    }
    MethodesEtats etat = etatsParEnum.get(e);
    if (etat == null) {
      throw new IllegalStateException("État de jeu non géré: " + e);
    }
    return etat;
  }

  @Override
  public PanelJeu getPanneauJeu() {
    return panneauJeu;
  }

  @Override
  public JeuDeLaVie getJeuDeLaVie() {
    return jeu;
  }

  private void mettreAJour() {
    getEtatCourant().mettreAJour();
  }

  public void afficher(Graphics g) {
    getEtatCourant().dessiner(g);
  }

  @Override
  public void actualise() {
    panneauJeu.repaint();
  }

  @Override
  public void run() {
    final double nanosParUpdate = 1_000_000_000.0 / TARGET_UPS;
    int images = 0;
    int misesAJour = 0;
    long dernierControleFps = System.currentTimeMillis();
    double accumulateur = 0.0;
    long tempsPrecedent = System.nanoTime();

    while (!Thread.currentThread().isInterrupted()) {
      long tempsActuel = System.nanoTime();
      accumulateur += tempsActuel - tempsPrecedent;
      tempsPrecedent = tempsActuel;

      while (accumulateur >= nanosParUpdate) {
        mettreAJour();
        misesAJour++;
        accumulateur -= nanosParUpdate;
        flagRepaint = true;
      }

      if (flagRepaint) {
        panneauJeu.repaint();
        images++;
        flagRepaint = false;
      }

      long nanosParImage = 1_000_000_000L / TARGET_FPS;
      long dureeImage = System.nanoTime() - tempsActuel;
      long tempsRepos = (nanosParImage - dureeImage) / 1_000_000;
      if (tempsRepos > 0) {
        try {
          Thread.sleep(tempsRepos);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          break;
        }
      }

      long millisActuels = System.currentTimeMillis();
      if (millisActuels - dernierControleFps >= 1000) {
        fpsActuel = images;
        upsActuel = misesAJour;
        images = 0;
        misesAJour = 0;
        dernierControleFps = millisActuels;
      }
    }
  }

  public void nettoyer() {
    if (threadBoucleJeu != null && threadBoucleJeu.isAlive()) {
      threadBoucleJeu.interrupt();
      try {
        threadBoucleJeu.join(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
