package fr.utilz;

import java.awt.Color;

public class Constants {
  public static int jeu_largeur = 0;
  public static int jeu_hauteur = 0;
  public static boolean pause = false;

  /** Palette UI : thème sombre avec accents verts (Jeu de la Vie). */
  public static final Color COULEUR_FOND_MENU = new Color(0x1a, 0x23, 0x32);
  public static final Color COULEUR_FOND_GRILLE = new Color(0x0f, 0x14, 0x19);
  public static final Color COULEUR_FOND_PARAMETRES = new Color(0x1a, 0x23, 0x32);
  public static final Color COULEUR_CELLULE_VIVANTE = new Color(0x22, 0xc5, 0x5e);
  public static final Color COULEUR_BOUTON_NORMAL = new Color(0x33, 0x41, 0x55, 200);
  public static final Color COULEUR_BOUTON_SURVOL = new Color(0x47, 0x56, 0x69, 220);
  public static final Color COULEUR_BOUTON_ENFONCE = new Color(0x64, 0x74, 0x8b, 240);
  public static final Color COULEUR_BOUTON_BORDURE = new Color(0x94, 0xa3, 0xb8);
  public static final Color COULEUR_TEXTE = new Color(0xf1, 0xf5, 0xf9);
  public static final Color COULEUR_SLIDER_PISTE = new Color(0x33, 0x41, 0x55, 220);
  public static final Color COULEUR_SLIDER_THUMB = new Color(0x64, 0x74, 0x8b, 240);
  public static final Color COULEUR_SLIDER_THUMB_SURVOL = new Color(0x94, 0xa3, 0xb8, 250);
}
