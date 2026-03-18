package fr.utilz;

import java.awt.Color;

public class Constants {
  public static int jeu_largeur = 0;
  public static int jeu_hauteur = 0;
  public static volatile boolean pause = false;

  /** Palette UI : thème sombre avec accents verts (Jeu de la Vie). */
  // Fonds
  public static final Color COULEUR_FOND_MENU       = new Color(0x0d, 0x14, 0x1f);
  public static Color COULEUR_FOND_GRILLE             = new Color(0x08, 0x0d, 0x12);
  public static final Color COULEUR_FOND_PARAMETRES  = new Color(0x0d, 0x14, 0x1f);

  // Accent principal
  public static Color COULEUR_CELLULE_VIVANTE         = new Color(0x4a, 0xde, 0x80);
  public static Color COULEUR_CELLULE_MORTE           = new Color(0x08, 0x0d, 0x12);

  // Boutons — états bien distincts
  public static final Color COULEUR_BOUTON_NORMAL    = new Color(0x1e, 0x29, 0x3b, 235);
  public static final Color COULEUR_BOUTON_SURVOL    = new Color(0x22, 0x43, 0x2d, 245); // teinté vert
  public static final Color COULEUR_BOUTON_ENFONCE   = new Color(0x14, 0x53, 0x2d, 255); // vert foncé
  public static final Color COULEUR_BOUTON_BORDURE   = new Color(0x4a, 0xde, 0x80, 140); // bordure verte

  // Texte
  public static final Color COULEUR_TEXTE            = new Color(0xff, 0xff, 0xff);

  // Sliders
  public static final Color COULEUR_SLIDER_PISTE        = new Color(0x1e, 0x29, 0x3b);
  public static final Color COULEUR_SLIDER_THUMB        = new Color(0xd1, 0xd9, 0xe0);
  public static final Color COULEUR_SLIDER_THUMB_SURVOL = new Color(0x4a, 0xde, 0x80);

  // Barre de contrôles en bas de la grille
  public static final Color COULEUR_BARRE_CONTROLES  = new Color(0x06, 0x09, 0x10, 215);
}
