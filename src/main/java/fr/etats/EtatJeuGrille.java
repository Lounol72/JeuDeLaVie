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

/**
 * État actif du jeu : affiche la grille de simulation et ses contrôles.
 *
 * <p>Gère le zoom (slider + molette centrée sur le curseur) et le panoramique (glisser-déposer).
 * Le rendu n'itère que sur les cellules effectivement visibles à l'écran, quelle que soit la
 * taille de la grille.
 *
 * <h3>Contrôles zoom/pan</h3>
 * <ul>
 *   <li><b>Slider zoom</b> (gauche du slider vitesse) — ajuste le facteur, ancré en haut-gauche.</li>
 *   <li><b>Molette souris</b> — zoom centré sur la position du curseur.</li>
 *   <li><b>Glisser-déposer</b> (clic gauche hors UI) — panoramique sur la grille.</li>
 * </ul>
 */
public class EtatJeuGrille extends Etats {

  // -------------------------------------------------------------------------
  // Constantes UI
  // -------------------------------------------------------------------------

  private static final int LARGEUR_BOUTON = 200;
  private static final int HAUTEUR_BOUTON = 44;
  private static final int GAP_BOUTONS = 20;

  private static final int LARGEUR_SLIDER = 28;
  private static final int HAUTEUR_SLIDER = 200;
  private static final int MARGIN_SLIDER = 20;
  private static final int GAP_SLIDERS = 20;

  // -------------------------------------------------------------------------
  // Constantes zoom
  // -------------------------------------------------------------------------

  /** Facteur de zoom minimum (vue d'ensemble). */
  private static final double ZOOM_MIN = 0.2;
  /** Facteur de zoom maximum (vue cellule par cellule). */
  private static final double ZOOM_MAX = 8.0;
  /** Facteur appliqué à chaque cran de molette (15 % de variation). */
  private static final double ZOOM_PAS_ROUE = 1.15;
  /** Valeur slider (0-100) correspondant à zoom neutre (facteur 1.0). */
  private static final int VALEUR_ZOOM_NEUTRE = 20;

  // -------------------------------------------------------------------------
  // Constantes vitesse
  // -------------------------------------------------------------------------

  /** Nombre de mises à jour de boucle par génération à vitesse 1.0. */
  private static final int BASE_UPDATES = 12;
  /** Valeur slider correspondant à la vitesse 1.0. */
  private static final int VALEUR_NORMALE = 50;

  // -------------------------------------------------------------------------
  // État de la grille
  // -------------------------------------------------------------------------

  private static int maxX;
  private static int maxY;

  private final JeuDeLaVie jeuDeLaVie;
  private int compteurUpdates = 0;

  // -------------------------------------------------------------------------
  // Zoom
  // -------------------------------------------------------------------------

  /** Facteur de zoom courant ; dans [ZOOM_MIN, ZOOM_MAX]. */
  private double facteurZoom = 1.0;
  /** Taille d'un côté de cellule en pixels, dérivée de facteurZoom. */
  private int tailleCellule;
  /**
   * Garde anti-réentrance : empêche le callback du slider de recalculer la taille quand c'est
   * la molette qui synchronise le slider.
   */
  private boolean enCoursZoomRoue = false;

  // -------------------------------------------------------------------------
  // Panoramique (pan)
  // -------------------------------------------------------------------------

  /**
   * Décalage en pixels de l'origine de la grille par rapport au coin haut-gauche de l'écran.
   * Toujours ≤ 0 ; borné par {@link #clampOffset}.
   */
  private int offsetPixelX = 0;
  private int offsetPixelY = 0;

  /** Indique qu'un panoramique est en cours. */
  private boolean panEnCours = false;
  /** Position souris au début du panoramique. */
  private int panDepartSourisX;
  private int panDepartSourisY;
  /** Offset au début du panoramique (pour calculer le delta). */
  private int panOffsetDepartX;
  private int panOffsetDepartY;

  // -------------------------------------------------------------------------
  // Sliders et vitesse
  // -------------------------------------------------------------------------

  private final Slider slider;
  private final Slider sliderZoom;
  /** Cache mis à jour par le callback du slider pour éviter getValue() à chaque frame. */
  private volatile int updatesParGenerationCache = BASE_UPDATES;

  // -------------------------------------------------------------------------
  // Constructeur
  // -------------------------------------------------------------------------

  /**
   * Crée l'état grille avec ses boutons et sliders.
   *
   * @param jeu référence au contrôleur principal (Game)
   */
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

    // Calcul initial de la taille de cellule AVANT de positionner les sliders
    recalculerTailleCellule(Constants.jeu_largeur, Constants.jeu_hauteur);

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
              // Ignoré quand la molette synchronise le slider pour éviter la réentrance
              if (enCoursZoomRoue) return;
              facteurZoom = ZOOM_MIN + (v / 100.0) * (ZOOM_MAX - ZOOM_MIN);
              facteurZoom = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, facteurZoom));
              recalculerTailleCellule(Constants.jeu_largeur, Constants.jeu_hauteur);
              clampOffset(Constants.jeu_largeur, Constants.jeu_hauteur);
            });
  }

  // -------------------------------------------------------------------------
  // Zoom — méthodes internes
  // -------------------------------------------------------------------------

  /**
   * Recalcule {@link #tailleCellule} à partir de {@link #facteurZoom} et des dimensions actuelles.
   *
   * <p>La taille de base est choisie pour que la grille entière tienne dans la fenêtre à zoom 1.0,
   * puis multipliée par le facteur de zoom.
   *
   * @param largeur largeur de la fenêtre en pixels
   * @param hauteur hauteur de la fenêtre en pixels
   */
  private void recalculerTailleCellule(int largeur, int hauteur) {
    double tailleBase =
        Math.max(1.0, Math.min((double) largeur / maxX, (double) hauteur / maxY));
    tailleCellule = Math.max(1, (int) Math.round(tailleBase * facteurZoom));
  }

  /**
   * Borne les offsets de panoramique pour qu'aucune zone vide n'apparaisse au-delà de la grille.
   *
   * <p>Si la grille est plus petite que l'écran dans une dimension, l'offset est ramené à 0
   * (alignement haut-gauche).
   *
   * @param largeur largeur de la fenêtre en pixels
   * @param hauteur hauteur de la fenêtre en pixels
   */
  private void clampOffset(int largeur, int hauteur) {
    int tailleGrilleX = maxX * tailleCellule;
    int tailleGrilleY = maxY * tailleCellule;
    if (tailleGrilleX <= largeur) {
      offsetPixelX = 0;
    } else {
      offsetPixelX = Math.min(0, Math.max(offsetPixelX, largeur - tailleGrilleX));
    }
    if (tailleGrilleY <= hauteur) {
      offsetPixelY = 0;
    } else {
      offsetPixelY = Math.min(0, Math.max(offsetPixelY, hauteur - tailleGrilleY));
    }
  }

  /**
   * Applique un zoom centré sur un point écran arbitraire.
   *
   * <p>Calcule la cellule grille sous le point {@code (cx, cy)}, applique le nouveau facteur,
   * puis ajuste l'offset pour que cette même cellule reste sous le même point écran.
   *
   * @param nouveauFacteur facteur de zoom cible (sera borné dans [ZOOM_MIN, ZOOM_MAX])
   * @param cx             coordonnée X du centre de zoom (en pixels écran)
   * @param cy             coordonnée Y du centre de zoom (en pixels écran)
   */
  private void appliquerZoomCentre(double nouveauFacteur, int cx, int cy) {
    double ancienTc = tailleCellule;

    // Position dans la grille (en cellules flottantes) sous le curseur
    double gridX = (cx - offsetPixelX) / ancienTc;
    double gridY = (cy - offsetPixelY) / ancienTc;

    facteurZoom = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, nouveauFacteur));
    recalculerTailleCellule(Constants.jeu_largeur, Constants.jeu_hauteur);

    // Repositionne pour que gridX/gridY reste sous le curseur
    offsetPixelX = (int) Math.round(cx - gridX * tailleCellule);
    offsetPixelY = (int) Math.round(cy - gridY * tailleCellule);
    clampOffset(Constants.jeu_largeur, Constants.jeu_hauteur);

    // Synchronise le slider visuellement sans déclencher son callback (garde actif)
    enCoursZoomRoue = true;
    int sliderVal = (int) Math.round((facteurZoom - ZOOM_MIN) / (ZOOM_MAX - ZOOM_MIN) * 100);
    sliderZoom.setValue(sliderVal);
    enCoursZoomRoue = false;
  }

  // -------------------------------------------------------------------------
  // Boucle de jeu
  // -------------------------------------------------------------------------

  @Override
  public void mettreAJour() {
    if (Constants.pause) return;
    compteurUpdates++;
    if (compteurUpdates >= updatesParGenerationCache) {
      compteurUpdates = 0;
      jeuDeLaVie.calculerGenerationSuivante();
    }
  }

  // -------------------------------------------------------------------------
  // Rendu
  // -------------------------------------------------------------------------

  @Override
  protected void appliquerDisposition(int largeur, int hauteur) {
    int yBoutons = hauteur - 130;
    boutons.get(0).setBounds(50, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
    boutons.get(1).setBounds(
        50 + LARGEUR_BOUTON + GAP_BOUTONS, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);
    boutons.get(2).setBounds(
        50 + LARGEUR_BOUTON * 2 + 2 * GAP_BOUTONS, yBoutons, LARGEUR_BOUTON, HAUTEUR_BOUTON);

    recalculerTailleCellule(largeur, hauteur);
    clampOffset(largeur, hauteur);

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

  /**
   * Dessine uniquement les cellules vivantes visibles à l'écran, en tenant compte de l'offset de
   * panoramique. Les cellules hors de la zone visible sont ignorées.
   */
  private void afficherGrille(Graphics g) {
    final int tc = tailleCellule;
    final int largeur = Constants.jeu_largeur;
    final int hauteur = Constants.jeu_hauteur;

    g.setColor(Constants.COULEUR_CELLULE_VIVANTE);

    // Première et dernière colonne/ligne visible (avec marge d'une cellule pour les partielles)
    int startX = Math.max(0, (-offsetPixelX) / tc);
    int startY = Math.max(0, (-offsetPixelY) / tc);
    int endX = Math.min(maxX, startX + (largeur / tc) + 2);
    int endY = Math.min(maxY, startY + (hauteur / tc) + 2);

    for (int cy = startY; cy < endY; cy++) {
      int py = offsetPixelY + cy * tc;
      for (int cx = startX; cx < endX; cx++) {
        if (jeuDeLaVie.estCelluleVivante(cx, cy)) {
          int px = offsetPixelX + cx * tc;
          g.fillRect(px, py, tc, tc);
        }
      }
    }
  }

  // -------------------------------------------------------------------------
  // Entrées clavier
  // -------------------------------------------------------------------------

  @Override
  public void toucheEnfoncee(KeyEvent e) {}

  @Override
  public void toucheRelachee(KeyEvent e) {}

  @Override
  public void toucheTapee(KeyEvent e) {}

  // -------------------------------------------------------------------------
  // Entrées souris
  // -------------------------------------------------------------------------

  /**
   * Applique un zoom centré sur la position du curseur à chaque cran de molette.
   *
   * <p>Un cran vers le bas (rotation positive) réduit le zoom ; vers le haut (rotation négative)
   * l'augmente.
   *
   * @param rotation rotation de la molette (positif = zoom arrière)
   * @param x        position X du curseur
   * @param y        position Y du curseur
   */
  @Override
  public void moletteTournee(int rotation, int x, int y) {
    // rotation > 0 : scroll vers le bas → zoom arrière
    double etape = (rotation > 0) ? (1.0 / ZOOM_PAS_ROUE) : ZOOM_PAS_ROUE;
    appliquerZoomCentre(facteurZoom * etape, x, y);
  }

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
    } else if (panEnCours) {
      // Panoramique : décalage par rapport à la position souris de départ
      offsetPixelX = panOffsetDepartX + (e.getX() - panDepartSourisX);
      offsetPixelY = panOffsetDepartY + (e.getY() - panDepartSourisY);
      clampOffset(Constants.jeu_largeur, Constants.jeu_hauteur);
    }
  }

  @Override
  public void sourisCliquee(MouseEvent e) {}

  @Override
  public void sourisEnfoncee(MouseEvent e) {
    boolean surSlider = contientSliderZoom(e) || contientSliderVitesse(e);
    boolean surBouton = false;
    for (Bouton b : boutons) {
      if (contient(e, b)) {
        b.setSourisEnfonce(true);
        surBouton = true;
      }
    }

    if (contientSliderZoom(e)) {
      sliderZoom.setEnGlissement(true);
      sliderZoom.glisserVers(e.getY());
    } else if (contientSliderVitesse(e)) {
      slider.setEnGlissement(true);
      slider.glisserVers(e.getY());
    }

    // Démarre le panoramique uniquement si le clic est sur la zone grille (hors UI)
    if (!surSlider && !surBouton) {
      panEnCours = true;
      panDepartSourisX = e.getX();
      panDepartSourisY = e.getY();
      panOffsetDepartX = offsetPixelX;
      panOffsetDepartY = offsetPixelY;
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
    panEnCours = false;
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private boolean contientSliderVitesse(MouseEvent e) {
    return slider.getDelimitation().contains(e.getX(), e.getY());
  }

  private boolean contientSliderZoom(MouseEvent e) {
    return sliderZoom.getDelimitation().contains(e.getX(), e.getY());
  }
}
