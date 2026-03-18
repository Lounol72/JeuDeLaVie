package fr.etats;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import fr.game.Game;
import fr.ui.ComboBoxVisiteur;
import fr.ui.bouton.Bouton;
import fr.ui.bouton.BoutonChangeurEtat;
import fr.ui.bouton.BoutonToggleCouleur;
import fr.ui.bouton.BoutonToggleTerminal;
import fr.ui.bouton.BoutonToggleTorique;
import fr.utilz.Constants;
import fr.visiteur.TypeVisiteur;

/**
 * Écran paramètres : card centrale avec sections Règles et Options.
 *
 * <p>Layout : fond sombre + grille de points, panneau centré avec titre, description de la règle
 * active, toggles terminal/couleur côte à côte, bouton Retour.
 */
public class Parametres extends Etats {

  // -------------------------------------------------------------------------
  // Constantes de layout — card centrale
  // -------------------------------------------------------------------------

  private static final int CARD_W   = 520;
  private static final int CARD_PAD = 28;
  private static final int ELEM_H   = 52;
  private static final int GAP_TOG  = 12;  // espace entre les deux toggles

  // Offsets Y depuis le bord supérieur de la card
  private static final int OFF_TITRE    = 24;
  private static final int OFF_ACCENT   = OFF_TITRE + 30;
  private static final int OFF_H_REGLES = OFF_ACCENT + 2 + 16;
  private static final int OFF_COMBO    = OFF_H_REGLES + 20 + 10;
  private static final int OFF_DESC     = OFF_COMBO + ELEM_H + 8;
  private static final int OFF_SEP2     = OFF_DESC + 36 + 14;
  private static final int OFF_H_OPT    = OFF_SEP2 + 1 + 14;
  private static final int OFF_TOGGLES  = OFF_H_OPT + 20 + 10;
  private static final int OFF_SEP3     = OFF_TOGGLES + ELEM_H + 14;
  private static final int OFF_RETOUR   = OFF_SEP3 + 1 + 14;
  private static final int CARD_H       = OFF_RETOUR + ELEM_H + 24;

  // -------------------------------------------------------------------------
  // Polices
  // -------------------------------------------------------------------------

  private static final Font FONT_TITRE   = new Font("Arial", Font.BOLD,  22);
  private static final Font FONT_SECTION = new Font("Arial", Font.BOLD,  11);
  private static final Font FONT_DESC    = new Font("Arial", Font.PLAIN, 12);

  // -------------------------------------------------------------------------
  // Couleurs spécifiques à la card
  // -------------------------------------------------------------------------

  private static final Color COULEUR_CARD       = new Color(0x0f, 0x17, 0x24, 238);
  private static final Color COULEUR_CARD_BORD  = new Color(0x4a, 0xde, 0x80,  90);
  private static final Color COULEUR_SEP        = new Color(0xff, 0xff, 0xff,  22);
  private static final Color COULEUR_DESC       = new Color(0xff, 0xff, 0xff, 155);
  private static final Color COULEUR_DOTS       = new Color(0x4a, 0xde, 0x80,  18);
  private static final Color COULEUR_HEAD_LINE  = new Color(0x4a, 0xde, 0x80,  45);

  // -------------------------------------------------------------------------
  // Composants
  // -------------------------------------------------------------------------

  private final ComboBoxVisiteur comboVisiteur;

  // -------------------------------------------------------------------------
  // Constructeur
  // -------------------------------------------------------------------------

  public Parametres(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();

    comboVisiteur = new ComboBoxVisiteur(0, 0, 0, ELEM_H, jeu.getJeuDeLaVie());

    BoutonToggleTerminal terminal = new BoutonToggleTerminal(0, 0, 0, 0, jeu.getJeuDeLaVie());
    BoutonToggleCouleur  couleur  = new BoutonToggleCouleur(0, 0, 0, 0, jeu.getJeuDeLaVie());
    BoutonToggleTorique  torique  = new BoutonToggleTorique(0, 0, 0, 0, jeu.getJeuDeLaVie());
    BoutonChangeurEtat   retour   = new BoutonChangeurEtat(0, 0, 0, 0, EtatJeu.MENU, "← Retour");
    boutons.add(terminal);
    boutons.add(couleur);
    boutons.add(torique);
    boutons.add(retour);

    enregistrer(comboVisiteur, (w, h) -> {
      int cw = cw(w); int cx = cx(w, cw); int cy = cy(h);
      return new int[]{ cx + CARD_PAD, cy + OFF_COMBO, cw - CARD_PAD * 2, ELEM_H };
    });
    enregistrer(terminal, (w, h) -> {
      int cw = cw(w); int cx = cx(w, cw); int cy = cy(h);
      int bw = (cw - CARD_PAD * 2 - 2 * GAP_TOG) / 3;
      return new int[]{ cx + CARD_PAD, cy + OFF_TOGGLES, bw, ELEM_H };
    });
    enregistrer(couleur, (w, h) -> {
      int cw = cw(w); int cx = cx(w, cw); int cy = cy(h);
      int bw = (cw - CARD_PAD * 2 - 2 * GAP_TOG) / 3;
      return new int[]{ cx + CARD_PAD + bw + GAP_TOG, cy + OFF_TOGGLES, bw, ELEM_H };
    });
    enregistrer(torique, (w, h) -> {
      int cw = cw(w); int cx = cx(w, cw); int cy = cy(h);
      int bw = (cw - CARD_PAD * 2 - 2 * GAP_TOG) / 3;
      return new int[]{ cx + CARD_PAD + 2 * (bw + GAP_TOG), cy + OFF_TOGGLES, bw, ELEM_H };
    });
    enregistrer(retour, (w, h) -> {
      int cw = cw(w); int cx = cx(w, cw); int cy = cy(h);
      return new int[]{ cx + CARD_PAD, cy + OFF_RETOUR, cw - CARD_PAD * 2, ELEM_H };
    });
  }

  // -------------------------------------------------------------------------
  // Helpers dimensions card
  // -------------------------------------------------------------------------

  private static int cw(int w) { return Math.min(CARD_W, w - 60); }
  private static int cx(int w, int cw) { return (w - cw) / 2; }
  private static int cy(int h) { return (h - CARD_H) / 2; }

  // -------------------------------------------------------------------------
  // Boucle
  // -------------------------------------------------------------------------

  @Override
  public void mettreAJour() {}

  // -------------------------------------------------------------------------
  // Rendu
  // -------------------------------------------------------------------------

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    comboVisiteur.synchroniser();

    int w = Constants.jeu_largeur;
    int h = Constants.jeu_hauteur;
    int cw = cw(w);
    int cx = cx(w, cw);
    int cy = cy(h);

    dessinerFond((Graphics2D) g, w, h);
    dessinerCard((Graphics2D) g, cx, cy, cw);

    for (Bouton b : boutons) b.dessiner(g);
    // Dessiné en dernier pour passer par-dessus les boutons si ouvert
    comboVisiteur.dessiner(g);
  }

  /** Fond uni + grille de points verts très subtile. */
  private void dessinerFond(Graphics2D g2, int w, int h) {
    g2.setColor(Constants.COULEUR_FOND_PARAMETRES);
    g2.fillRect(0, 0, w, h);

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(COULEUR_DOTS);
    final int spacing = 28;
    for (int px = spacing; px < w; px += spacing) {
      for (int py = spacing; py < h; py += spacing) {
        g2.fillOval(px - 1, py - 1, 2, 2);
      }
    }
  }

  /** Card centrale : fond, bordure, titre, séparateurs, sections, description. */
  private void dessinerCard(Graphics2D g2, int cx, int cy, int cw) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    int innerX = cx + CARD_PAD;
    int innerW = cw - CARD_PAD * 2;

    // Fond
    g2.setColor(COULEUR_CARD);
    g2.fillRoundRect(cx, cy, cw, CARD_H, 18, 18);

    // Bordure verte
    g2.setStroke(new BasicStroke(1.5f));
    g2.setColor(COULEUR_CARD_BORD);
    g2.drawRoundRect(cx, cy, cw, CARD_H, 18, 18);
    g2.setStroke(new BasicStroke(1f));

    // ---- Titre ----
    g2.setFont(FONT_TITRE);
    g2.setColor(Constants.COULEUR_TEXTE);
    FontMetrics fmT = g2.getFontMetrics();
    g2.drawString("PARAMÈTRES", innerX, cy + OFF_TITRE + fmT.getAscent());

    // Barre accent vert sous le titre
    g2.setColor(Constants.COULEUR_CELLULE_VIVANTE);
    g2.fillRect(innerX, cy + OFF_ACCENT, innerW, 2);

    // ---- Section RÈGLES ----
    dessinerSectionHeader(g2, innerX, cy + OFF_H_REGLES, "RÈGLES", innerW);

    // Description de la règle active (sous le combo)
    TypeVisiteur actif = jeu.getJeuDeLaVie().getTypeVisiteurActif();
    g2.setFont(FONT_DESC);
    g2.setColor(COULEUR_DESC);
    FontMetrics fmD = g2.getFontMetrics();
    g2.drawString(actif.getDescription(), innerX, cy + OFF_DESC + fmD.getAscent());

    // ---- Séparateur ----
    g2.setColor(COULEUR_SEP);
    g2.fillRect(innerX, cy + OFF_SEP2, innerW, 1);

    // ---- Section OPTIONS ----
    dessinerSectionHeader(g2, innerX, cy + OFF_H_OPT, "OPTIONS", innerW);

    // ---- Séparateur ----
    g2.setColor(COULEUR_SEP);
    g2.fillRect(innerX, cy + OFF_SEP3, innerW, 1);
  }

  /** Header de section en vert capitalisé + ligne décorative. */
  private void dessinerSectionHeader(Graphics2D g2, int x, int y, String texte, int innerW) {
    g2.setFont(FONT_SECTION);
    FontMetrics fm = g2.getFontMetrics();
    g2.setColor(Constants.COULEUR_CELLULE_VIVANTE);
    g2.drawString(texte, x, y + fm.getAscent());

    int lineX = x + fm.stringWidth(texte) + 10;
    int lineY = y + fm.getAscent() / 2 + 2;
    g2.setColor(COULEUR_HEAD_LINE);
    g2.drawLine(lineX, lineY, x + innerW, lineY);
  }

  // -------------------------------------------------------------------------
  // Entrées
  // -------------------------------------------------------------------------

  @Override public void toucheEnfoncee(KeyEvent e) {}
  @Override public void toucheRelachee(KeyEvent e) {}
  @Override public void toucheTapee(KeyEvent e) {}

  @Override
  public void sourisDeplacee(MouseEvent e) {
    comboVisiteur.sourisDeplacee(e.getX(), e.getY());
    for (Bouton b : boutons) b.setSourisSurvol(contient(e, b));
  }

  @Override public void sourisGlissee(MouseEvent e) {}
  @Override public void sourisCliquee(MouseEvent e) {}

  @Override
  public void sourisEnfoncee(MouseEvent e) {
    boolean comboEtaitOuvert = comboVisiteur.isOuvert();
    comboVisiteur.sourisEnfoncee(e.getX(), e.getY());
    // Le combo absorbe le clic quand il est ouvert (évite de déclencher les boutons derrière)
    if (comboEtaitOuvert) return;
    for (Bouton b : boutons) { if (contient(e, b)) b.setSourisEnfonce(true); }
  }

  @Override
  public void sourisRelachee(MouseEvent e) {
    boolean comboEtaitOuvert = comboVisiteur.isOuvert();
    comboVisiteur.sourisRelachee(e.getX(), e.getY());
    if (comboEtaitOuvert) return;
    for (Bouton b : boutons) {
      if (b.isSourisEnfonce() && contient(e, b)) b.appliquerAction();
      b.setSourisEnfonce(false);
    }
  }
}
