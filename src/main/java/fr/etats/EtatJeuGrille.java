package fr.etats;

import fr.cellule.Cellule;
import fr.game.Game;
import fr.io.GrilleIO;
import fr.jeu.JeuDeLaVie;
import fr.memento.GrilleMemento;
import fr.memento.HistoriqueGrille;
import fr.pattern.PatternType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import fr.ui.bouton.Bouton;
import fr.ui.bouton.BoutonAvancer;
import fr.ui.bouton.BoutonChangeurEtat;
import fr.ui.bouton.BoutonPause;
import fr.ui.bouton.BoutonAction;
import fr.ui.bouton.BoutonToggleDessin;
import fr.ui.sliders.Slider;
import fr.utilz.Constants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
  private final HistoriqueGrille historique = new HistoriqueGrille(30);
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

  // -------------------------------------------------------------------------
  // Mode dessin
  // -------------------------------------------------------------------------

  private boolean modeDessin = false;
  /** Direction de peinture en cours de drag : true = peindre vivant, false = peindre mort, null = pas de drag. */
  private Boolean peindreVivant = null;
  /** Dernière cellule modifiée (évite de retraiter la même cellule lors d'un drag). */
  private int dernierecelX = -1;
  private int dernierecelY = -1;

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

    this.jeuDeLaVie = jeu.getJeuDeLaVie();
    maxX = jeuDeLaVie.getMaxX();
    maxY = jeuDeLaVie.getMaxY();

    // Calcul initial de la taille de cellule AVANT de positionner les sliders
    recalculerTailleCellule(Constants.jeu_largeur, Constants.jeu_hauteur);

    BoutonChangeurEtat retour    = new BoutonChangeurEtat(0, 0, 0, 0, EtatJeu.MENU, "Retour");
    BoutonPause        pause     = new BoutonPause(0, 0, 0, 0, true, "Pause");
    BoutonAvancer      avancer   = new BoutonAvancer(0, 0, 0, 0, "Avancer", jeuDeLaVie);
    BoutonToggleDessin dessiner  = new BoutonToggleDessin(0, 0, 0, 0, () -> modeDessin, () -> modeDessin = !modeDessin);

    BoutonAction aleatoire = new BoutonAction(0, 0, 0, 0, "Aléatoire", () -> {
      historique.sauvegarder(jeuDeLaVie.creerMemento());
      jeuDeLaVie.initialiseGrille();
      jeuDeLaVie.notifieObservateur();
    });
    BoutonAction effacer = new BoutonAction(0, 0, 0, 0, "Effacer", () -> {
      historique.sauvegarder(jeuDeLaVie.creerMemento());
      jeuDeLaVie.clearGrille();
    });
    BoutonAction undo = new BoutonAction(0, 0, 0, 0, "\u21A9 Undo", this::annuler);
    BoutonAction redo = new BoutonAction(0, 0, 0, 0, "\u21AA Redo", this::retablir);

    BoutonAction sauv = new BoutonAction(0, 0, 0, 0, "Sauv.", () -> {
      JFileChooser fc = new JFileChooser();
      fc.setSelectedFile(new File("sauvegarde.jdlv"));
      if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        try {
          GrilleIO.sauvegarder(jeuDeLaVie, fc.getSelectedFile().getAbsolutePath());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
    BoutonAction char_ = new BoutonAction(0, 0, 0, 0, "Char.", () -> {
      JFileChooser fc = new JFileChooser();
      if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        try {
          historique.sauvegarder(jeuDeLaVie.creerMemento());
          GrilleIO.charger(jeuDeLaVie, fc.getSelectedFile().getAbsolutePath());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });

    BoutonAction glider  = new BoutonAction(0, 0, 0, 0, "Glider",  () -> placerPattern(PatternType.GLIDER));
    BoutonAction blinker = new BoutonAction(0, 0, 0, 0, "Blinker", () -> placerPattern(PatternType.BLINKER));
    BoutonAction pulsar  = new BoutonAction(0, 0, 0, 0, "Pulsar",  () -> placerPattern(PatternType.PULSAR));
    BoutonAction lwss    = new BoutonAction(0, 0, 0, 0, "LWSS",    () -> placerPattern(PatternType.LWSS));

    boutons.add(retour);
    boutons.add(pause);
    boutons.add(avancer);
    boutons.add(dessiner);
    boutons.add(aleatoire);
    boutons.add(effacer);
    boutons.add(undo);
    boutons.add(redo);
    boutons.add(sauv);
    boutons.add(char_);
    boutons.add(glider);
    boutons.add(blinker);
    boutons.add(pulsar);
    boutons.add(lwss);

    slider =
        new Slider(
            0, 0, LARGEUR_SLIDER, HAUTEUR_SLIDER, 0, 100, VALEUR_NORMALE,
            v -> {
              int val = Math.max(1, v);
              updatesParGenerationCache = Math.max(1, (BASE_UPDATES * VALEUR_NORMALE) / val);
            });

    sliderZoom =
        new Slider(
            0, 0, LARGEUR_SLIDER, HAUTEUR_SLIDER, 0, 100, VALEUR_ZOOM_NEUTRE,
            v -> {
              if (enCoursZoomRoue) return;
              facteurZoom = ZOOM_MIN + (v / 100.0) * (ZOOM_MAX - ZOOM_MIN);
              facteurZoom = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, facteurZoom));
              recalculerTailleCellule(Constants.jeu_largeur, Constants.jeu_hauteur);
              clampOffset(Constants.jeu_largeur, Constants.jeu_hauteur);
            });

    // --- Row 1 : Retour / Pause / Avancer / Dessin  (h - 130) ---
    enregistrer(retour,   (w, h) -> row4(w, h, 0, h - 130));
    enregistrer(pause,    (w, h) -> row4(w, h, 1, h - 130));
    enregistrer(avancer,  (w, h) -> row4(w, h, 2, h - 130));
    enregistrer(dessiner, (w, h) -> row4(w, h, 3, h - 130));

    // --- Row 2 : Aléatoire / Effacer / Undo / Redo  (h - 194) ---
    enregistrer(aleatoire, (w, h) -> row4(w, h, 0, h - 194));
    enregistrer(effacer,   (w, h) -> row4(w, h, 1, h - 194));
    enregistrer(undo,      (w, h) -> row4(w, h, 2, h - 194));
    enregistrer(redo,      (w, h) -> row4(w, h, 3, h - 194));

    // --- Row 3 : Sauv. / Char. / Glider / Blinker  (h - 258) ---
    enregistrer(sauv,    (w, h) -> row4(w, h, 0, h - 258));
    enregistrer(char_,   (w, h) -> row4(w, h, 1, h - 258));
    enregistrer(glider,  (w, h) -> row4(w, h, 2, h - 258));
    enregistrer(blinker, (w, h) -> row4(w, h, 3, h - 258));

    // --- Row 4 : Pulsar / LWSS  (h - 322) ---
    enregistrer(pulsar, (w, h) -> row4(w, h, 0, h - 322));
    enregistrer(lwss,   (w, h) -> row4(w, h, 1, h - 322));

    enregistrer(slider,    (w, h) -> new int[]{ w - MARGIN_SLIDER - LARGEUR_SLIDER, (h - HAUTEUR_SLIDER) / 2, LARGEUR_SLIDER, HAUTEUR_SLIDER });
    enregistrer(sliderZoom,(w, h) -> new int[]{ w - MARGIN_SLIDER - LARGEUR_SLIDER * 2 - GAP_SLIDERS, (h - HAUTEUR_SLIDER) / 2, LARGEUR_SLIDER, HAUTEUR_SLIDER });
  }

  /** Retourne les dimensions d'un bouton dans une rangée de 4 éléments (indice 0-3). */
  private static int[] row4(int w, int h, int index, int rowY) {
    int bw = Math.min(LARGEUR_BOUTON, (w - GAP_BOUTONS * 3) / 4);
    int totalW = bw * 4 + GAP_BOUTONS * 3;
    int sx = (w - totalW) / 2;
    return new int[]{ sx + index * (bw + GAP_BOUTONS), rowY, bw, HAUTEUR_BOUTON };
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
    super.appliquerDisposition(largeur, hauteur);
    recalculerTailleCellule(largeur, hauteur);
    clampOffset(largeur, hauteur);
  }

  private static final Font POLICE_HUD   = new Font("Arial", Font.PLAIN, 14);
  private static final Font POLICE_PAUSE = new Font("Arial", Font.BOLD,  20);
  private static final Font POLICE_LABEL = new Font("Arial", Font.BOLD,  13);
  private static final Color COULEUR_HUD_FOND   = new Color(0x06, 0x09, 0x10, 220);
  private static final Color COULEUR_PAUSE_FOND = new Color(0, 0, 0, 160);

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    g.setColor(Constants.COULEUR_FOND_GRILLE);
    g.fillRect(0, 0, Constants.jeu_largeur, Constants.jeu_hauteur);

    if (jeuDeLaVie != null) {
      afficherGrille(g);
    }

    dessinerHud(g);

    if (Constants.pause) {
      dessinerPauseOverlay(g);
    }

    // Barre de contrôles — fond semi-transparent en bas
    int barreH = 340;
    g.setColor(Constants.COULEUR_BARRE_CONTROLES);
    g.fillRect(0, Constants.jeu_hauteur - barreH, Constants.jeu_largeur, barreH);

    dessinerLabelsSliders(g);

    for (Bouton b : boutons) {
      b.dessiner(g);
    }
    sliderZoom.dessiner(g);
    slider.dessiner(g);
  }

  private void dessinerHud(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setFont(POLICE_HUD);
    FontMetrics fm = g2.getFontMetrics();
    int lineH = fm.getHeight();

    int gen  = jeuDeLaVie.getNumGeneration();
    int pop  = jeuDeLaVie.getPopulation();
    int fps  = jeu.getFps();
    double vit = slider.getValue() / (double) VALEUR_NORMALE;

    String ligne1 = String.format("Gen. %d | Pop. %d", gen, pop);
    String ligne2 = String.format("FPS: %d", fps);
    String ligne3 = String.format("Zoom: %.1fx | Vit.: %.1fx%s", facteurZoom, vit, modeDessin ? " | [Dessin]" : "");

    int paddingH = 12;
    int paddingV = 8;
    int boxW = Math.max(fm.stringWidth(ligne1), Math.max(fm.stringWidth(ligne2), fm.stringWidth(ligne3))) + paddingH * 2;
    int boxH = lineH * 3 + paddingV * 2;
    int boxX = 10;
    int boxY = 10;

    g2.setColor(COULEUR_HUD_FOND);
    g2.fillRoundRect(boxX, boxY, boxW, boxH, 6, 6);

    g2.setColor(Constants.COULEUR_TEXTE);
    int textX = boxX + paddingH;
    int textY = boxY + paddingV + fm.getAscent();
    g2.drawString(ligne1, textX, textY);
    g2.drawString(ligne2, textX, textY + lineH);
    g2.drawString(ligne3, textX, textY + lineH * 2);
  }

  private void dessinerPauseOverlay(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setFont(POLICE_PAUSE);
    FontMetrics fm = g2.getFontMetrics();
    String texte = "\u23F8 PAUSE";
    int tw = fm.stringWidth(texte);
    int th = fm.getHeight();
    int boxW = tw + 20;
    int boxH = th + 10;
    int boxX = (Constants.jeu_largeur - boxW) / 2;
    int boxY = 10;
    g2.setColor(COULEUR_PAUSE_FOND);
    g2.fillRoundRect(boxX, boxY, boxW, boxH, 8, 8);
    g2.setColor(Color.WHITE);
    g2.drawString(texte, boxX + 10, boxY + 5 + fm.getAscent());
  }

  private void dessinerLabelsSliders(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setFont(POLICE_LABEL);
    FontMetrics fm = g2.getFontMetrics();

    java.awt.Rectangle rzoom = sliderZoom.getDelimitation();
    g2.setColor(Constants.COULEUR_TEXTE);
    String labelZoom = "Zoom";
    g2.drawString(labelZoom, rzoom.x + (rzoom.width - fm.stringWidth(labelZoom)) / 2, rzoom.y - 7);
    g2.setColor(Constants.COULEUR_CELLULE_VIVANTE);
    String valZoom = String.format("%.1fx", facteurZoom);
    g2.drawString(valZoom, rzoom.x + (rzoom.width - fm.stringWidth(valZoom)) / 2, rzoom.y + rzoom.height + 17);

    java.awt.Rectangle rvit = slider.getDelimitation();
    g2.setColor(Constants.COULEUR_TEXTE);
    String labelVit = "Vit.";
    g2.drawString(labelVit, rvit.x + (rvit.width - fm.stringWidth(labelVit)) / 2, rvit.y - 7);
    g2.setColor(Constants.COULEUR_CELLULE_VIVANTE);
    String valVit = String.format("%.1fx", slider.getValue() / (double) VALEUR_NORMALE);
    g2.drawString(valVit, rvit.x + (rvit.width - fm.stringWidth(valVit)) / 2, rvit.y + rvit.height + 17);
  }

  /**
   * Dessine uniquement les cellules vivantes visibles à l'écran, en tenant compte de l'offset de
   * panoramique. Les cellules hors de la zone visible sont ignorées.
   */
  private void afficherGrille(Graphics g) {
    final int tc = tailleCellule;
    final int largeur = Constants.jeu_largeur;
    final int hauteur = Constants.jeu_hauteur;

    // Première et dernière colonne/ligne visible (avec marge d'une cellule pour les partielles)
    int startX = Math.max(0, (-offsetPixelX) / tc);
    int startY = Math.max(0, (-offsetPixelY) / tc);
    int endX = Math.min(maxX, startX + (largeur / tc) + 2);
    int endY = Math.min(maxY, startY + (hauteur / tc) + 2);

    boolean couleurActive = jeuDeLaVie.isCouleurActive();

    for (int cy = startY; cy < endY; cy++) {
      int py = offsetPixelY + cy * tc;
      for (int cx = startX; cx < endX; cx++) {
        if (jeuDeLaVie.estCelluleVivante(cx, cy)) {
          if (couleurActive) {
            Color c = jeuDeLaVie.getCouleurCellule(cx, cy);
            g.setColor(c != null ? c : Constants.COULEUR_CELLULE_VIVANTE);
          } else {
            g.setColor(Constants.COULEUR_CELLULE_VIVANTE);
          }
          int px = offsetPixelX + cx * tc;
          g.fillRect(px, py, tc, tc);
        }
      }
    }
  }

  // -------------------------------------------------------------------------
  // Mode dessin — helpers
  // -------------------------------------------------------------------------

  private int[] pixelVersGrille(int px, int py) {
    int cx = (px - offsetPixelX) / tailleCellule;
    int cy = (py - offsetPixelY) / tailleCellule;
    return new int[]{cx, cy};
  }

  private boolean estSurGrille(int cx, int cy) {
    return cx >= 0 && cx < maxX && cy >= 0 && cy < maxY;
  }

  private void peindreGrille(int mouseX, int mouseY) {
    int[] pos = pixelVersGrille(mouseX, mouseY);
    int cx = pos[0], cy = pos[1];
    if (!estSurGrille(cx, cy)) return;
    if (cx == dernierecelX && cy == dernierecelY) return;
    dernierecelX = cx;
    dernierecelY = cy;
    Cellule cell = jeuDeLaVie.getGrille(cx, cy);
    if (peindreVivant) cell.vit(); else cell.meurt();
    jeuDeLaVie.notifieObservateur();
  }

  // -------------------------------------------------------------------------
  // Entrées clavier
  // -------------------------------------------------------------------------

  @Override
  public void toucheEnfoncee(KeyEvent e) {
    if (e.isControlDown()) {
      if (e.getKeyCode() == KeyEvent.VK_Z) annuler();
      else if (e.getKeyCode() == KeyEvent.VK_Y) retablir();
    }
  }

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
    if (modeDessin && peindreVivant != null) {
      peindreGrille(e.getX(), e.getY());
      return;
    }
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

    // Clic sur la zone grille (hors UI)
    if (!surSlider && !surBouton) {
      if (modeDessin) {
        int[] pos = pixelVersGrille(e.getX(), e.getY());
        if (estSurGrille(pos[0], pos[1])) {
          historique.sauvegarder(jeuDeLaVie.creerMemento());
          Cellule cell = jeuDeLaVie.getGrille(pos[0], pos[1]);
          peindreVivant = !cell.estVivante();
          dernierecelX = pos[0];
          dernierecelY = pos[1];
          if (peindreVivant) cell.vit(); else cell.meurt();
          jeuDeLaVie.notifieObservateur();
        }
      } else {
        panEnCours = true;
        panDepartSourisX = e.getX();
        panDepartSourisY = e.getY();
        panOffsetDepartX = offsetPixelX;
        panOffsetDepartY = offsetPixelY;
      }
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
    peindreVivant = null;
    dernierecelX = -1;
    dernierecelY = -1;
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

  // -------------------------------------------------------------------------
  // Undo / Redo
  // -------------------------------------------------------------------------

  private void annuler() {
    GrilleMemento m = historique.annuler(jeuDeLaVie.creerMemento());
    if (m != null) jeuDeLaVie.restaurerMemento(m);
  }

  private void retablir() {
    GrilleMemento m = historique.retablir(jeuDeLaVie.creerMemento());
    if (m != null) jeuDeLaVie.restaurerMemento(m);
  }

  // -------------------------------------------------------------------------
  // Patterns prédéfinis
  // -------------------------------------------------------------------------

  private void placerPattern(PatternType p) {
    historique.sauvegarder(jeuDeLaVie.creerMemento());
    int cx = (-offsetPixelX + Constants.jeu_largeur / 2) / tailleCellule;
    int cy = (-offsetPixelY + Constants.jeu_hauteur / 2) / tailleCellule;
    for (int[] coord : p.getCellules()) {
      int px = cx + coord[0];
      int py = cy + coord[1];
      if (estSurGrille(px, py)) jeuDeLaVie.getGrille(px, py).vit();
    }
    jeuDeLaVie.notifieObservateur();
  }
}
