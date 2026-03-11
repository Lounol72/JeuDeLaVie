package fr.etats;

import fr.game.Game;
import fr.jeu.JeuDeLaVie;
import fr.ui.bouton.Bouton;
import fr.ui.bouton.BoutonAvancer;
import fr.ui.bouton.BoutonChangeurEtat;
import fr.ui.bouton.BoutonPause;
import fr.ui.sliders.Slider;
import fr.utilz.Constants;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/** État grille : affiche le Jeu de la Vie et bouton Retour. */
public class EtatJeuGrille extends Etats {

  private static final int LARGEUR_BOUTON = 200;
  private static final int HAUTEUR_BOUTON = 44;
  private static int maxX;
  private static int maxY;

  /** Taille d'une cellule en pixels ; recalculée dans appliquerDisposition. */
  private int tailleCellule;
  /** Bornes visibles pour clipping ; recalculées avec tailleCellule. */
  private int xMaxVis;
  private int yMaxVis;

  /** Facteur zoom 0.5 à 3.0 ; conservé au resize. */
  private double facteurZoom = 1.0;
  private static final double ZOOM_MIN = 0.5;
  private static final double ZOOM_MAX = 3.0;
  /** Valeur slider 20 → zoom 1.0. */
  private static final int VALEUR_ZOOM_NEUTRE = 20;

  /** Base : 12 updates par génération à vitesse 1.0 (slider=50). */
  private static final int BASE_UPDATES = 12;
  /** Valeur slider correspondant à vitesse 1.0. */
  private static final int VALEUR_NORMALE = 50;

  private static final int LARGEUR_SLIDER = 28;
  private static final int HAUTEUR_SLIDER = 200;
  private static final int MARGIN_SLIDER = 20;
  private static final int GAP_SLIDERS = 20;

  private final JeuDeLaVie jeuDeLaVie;
  private int compteurUpdates = 0;

  private final Slider slider;
  private final Slider sliderZoom;
  /** Cache mis à jour quand le slider change ; évite getValue() à chaque frame. */
  private volatile int updatesParGenerationCache = BASE_UPDATES;

  public EtatJeuGrille(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();
    boutons.add(
        new BoutonChangeurEtat(
            50,
            Constants.jeu_hauteur - 130,
            LARGEUR_BOUTON,
            HAUTEUR_BOUTON,
            EtatJeu.MENU,
            "Retour"));
    boutons.add(
        new BoutonPause(
            50 + LARGEUR_BOUTON + GAP_BOUTONS,
            Constants.jeu_hauteur - 130,
            LARGEUR_BOUTON,
            HAUTEUR_BOUTON,
            true,
            "Pause"));
    this.jeuDeLaVie = jeu.getJeuDeLaVie();
    maxX = jeuDeLaVie.getMaxX();
    maxY = jeuDeLaVie.getMaxY();
    boutons.add(
        new BoutonAvancer(
            50 + LARGEUR_BOUTON * 2 + 2 * GAP_BOUTONS,
            Constants.jeu_hauteur - 130,
            LARGEUR_BOUTON,
            HAUTEUR_BOUTON,
            "Avancer",
            jeuDeLaVie));

    recalculerTailleEtBornes(Constants.jeu_largeur, Constants.jeu_hauteur);

    int sliderVitesseX = Constants.jeu_largeur - MARGIN_SLIDER - LARGEUR_SLIDER;
    int sliderZoomX = sliderVitesseX - LARGEUR_SLIDER - GAP_SLIDERS;
    int sliderY = (Constants.jeu_hauteur - HAUTEUR_SLIDER) / 2;
    slider =
        new Slider(
            sliderVitesseX,
            sliderY,
            LARGEUR_SLIDER,
            HAUTEUR_SLIDER,
            0,
            100,
            VALEUR_NORMALE,
            v -> {
              int val = Math.max(1, v);
              updatesParGenerationCache = Math.max(1, (BASE_UPDATES * VALEUR_NORMALE) / val);
            });
    sliderZoom =
        new Slider(
            sliderZoomX,
            sliderY,
            LARGEUR_SLIDER,
            HAUTEUR_SLIDER,
            0,
            100,
            VALEUR_ZOOM_NEUTRE,
            v -> {
              facteurZoom = ZOOM_MIN + (v / 100.0) * (ZOOM_MAX - ZOOM_MIN);
              facteurZoom = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, facteurZoom));
              largeurLayoutPrecedente = -1;
            });
  }

  private void recalculerTailleEtBornes(int largeur, int hauteur) {
    double tailleBase =
        Math.max(1.0, Math.min((double) largeur / maxX, (double) hauteur / maxY));
    tailleCellule = Math.max(1, (int) Math.round(tailleBase * facteurZoom));
    xMaxVis = Math.min(maxX, (largeur / tailleCellule) + 1);
    yMaxVis = Math.min(maxY, (hauteur / tailleCellule) + 1);
  }

  @Override
  public void mettreAJour() {
    if (Constants.pause) return;
    compteurUpdates++;
    if (compteurUpdates >= updatesParGenerationCache) {
      compteurUpdates = 0;
      jeuDeLaVie.calculerGenerationSuivante();
    }
  }

  private static final int GAP_BOUTONS = 20;

  @Override
  protected void appliquerDisposition(int largeur, int hauteur) {
    int yBoutons = hauteur - 130;
    boutons.get(0).setBounds(50, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
    boutons
        .get(1)
        .setBounds(50 + LARGEUR_BOUTON + GAP_BOUTONS, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
    boutons
        .get(2)
        .setBounds(
            50 + LARGEUR_BOUTON * 2 + 2 * GAP_BOUTONS, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
    recalculerTailleEtBornes(largeur, hauteur);
    int sliderVitesseX = largeur - MARGIN_SLIDER - LARGEUR_SLIDER;
    int sliderZoomX = sliderVitesseX - LARGEUR_SLIDER - GAP_SLIDERS;
    int sliderY = (hauteur - HAUTEUR_SLIDER) / 2;
    slider.setBounds(sliderVitesseX, sliderY, LARGEUR_SLIDER, HAUTEUR_SLIDER);
    sliderZoom.setBounds(sliderZoomX, sliderY, LARGEUR_SLIDER, HAUTEUR_SLIDER);
  }

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    g.setColor(Constants.COULEUR_FOND_GRILLE);
    g.fillRect(0, 0, Constants.jeu_largeur, Constants.jeu_hauteur);

    if (jeuDeLaVie != null) {
      afficherGrille(g);
    }

    for (Bouton b : boutons) {
      b.dessiner(g);
    }
    sliderZoom.dessiner(g);
    slider.dessiner(g);
  }

  private boolean contientSliderVitesse(MouseEvent e) {
    return slider.getDelimitation().contains(e.getX(), e.getY());
  }

  private boolean contientSliderZoom(MouseEvent e) {
    return sliderZoom.getDelimitation().contains(e.getX(), e.getY());
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
    sliderZoom.setSourisSurvol(contientSliderZoom(e));
    slider.setSourisSurvol(contientSliderVitesse(e));
  }

  @Override
  public void sourisGlissee(MouseEvent e) {
    if (sliderZoom.isEnGlissement()) {
      sliderZoom.glisserVers(e.getY());
    } else if (slider.isEnGlissement()) {
      slider.glisserVers(e.getY());
    }
  }

  @Override
  public void sourisCliquee(MouseEvent e) {}

  @Override
  public void sourisEnfoncee(MouseEvent e) {
    for (Bouton b : boutons) {
      if (contient(e, b)) {
        b.setSourisEnfonce(true);
      }
    }
    if (contientSliderZoom(e)) {
      sliderZoom.setEnGlissement(true);
      sliderZoom.glisserVers(e.getY());
    } else if (contientSliderVitesse(e)) {
      slider.setEnGlissement(true);
      slider.glisserVers(e.getY());
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
    sliderZoom.setEnGlissement(false);
    slider.setEnGlissement(false);
  }

  private void afficherGrille(Graphics g) {
    final int tc = tailleCellule;
    g.setColor(Constants.COULEUR_CELLULE_VIVANTE);

    int py = 0;
    for (int y = 0; y < yMaxVis; y++) {
      int px = 0;
      for (int x = 0; x < xMaxVis; x++) {
        if (jeuDeLaVie.estCelluleVivante(x, y)) {
          g.fillRect(px, py, tc, tc);
        }
        px += tc;
      }
      py += tc;
    }
  }
}
