package fr.etats;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import fr.game.Game;
import fr.ui.bouton.Bouton;
import fr.ui.bouton.BoutonChangeurEtat;
import fr.utilz.Constants;

/** État menu principal : boutons Jouer, Paramètres, Quitter. */
public class Menu extends Etats {

  private static final int LARGEUR_BOUTON = 400;
  private static final int HAUTEUR_BOUTON = 55;
  private static final int ESPACEMENT = 64;

  public Menu(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();

    BoutonChangeurEtat jouer      = new BoutonChangeurEtat(0, 0, 0, 0, EtatJeu.GRILLE,     "Jouer");
    BoutonChangeurEtat parametres = new BoutonChangeurEtat(0, 0, 0, 0, EtatJeu.PARAMETRES, "Paramètres");
    BoutonChangeurEtat quitter    = new BoutonChangeurEtat(0, 0, 0, 0, EtatJeu.QUITTER,     "Quitter");
    boutons.add(jouer);
    boutons.add(parametres);
    boutons.add(quitter);

    enregistrer(jouer,      (w, h) -> boutonCentral(w, h, 0));
    enregistrer(parametres, (w, h) -> boutonCentral(w, h, 1));
    enregistrer(quitter,    (w, h) -> boutonCentral(w, h, 2));
  }

  private int[] boutonCentral(int w, int h, int index) {
    int bw = Math.min(LARGEUR_BOUTON, w / 2);
    return new int[]{ w / 2 - bw / 2, h / 2 - 100 + index * ESPACEMENT, bw, HAUTEUR_BOUTON };
  }

  @Override
  public void mettreAJour() {}

  private static final Font POLICE_TITRE = new Font("Arial", Font.BOLD, 48);

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    g.setColor(Constants.COULEUR_FOND_MENU);
    g.fillRect(0, 0, Constants.jeu_largeur, Constants.jeu_hauteur);

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setFont(POLICE_TITRE);
    g2.setColor(Constants.COULEUR_CELLULE_VIVANTE);
    FontMetrics fm = g2.getFontMetrics();
    String titre = "Jeu de la Vie";
    int tx = (Constants.jeu_largeur - fm.stringWidth(titre)) / 2;
    int ty = Constants.jeu_hauteur / 4 + fm.getAscent() / 2;
    g2.drawString(titre, tx, ty);

    for (Bouton b : boutons) {
      b.dessiner(g);
    }
  }

  @Override
  public void toucheEnfoncee(KeyEvent e) {}

  @Override
  public void toucheRelachee(KeyEvent e) {}

  @Override
  public void toucheTapee(KeyEvent e) {}

  @Override public void sourisDeplacee(MouseEvent e) { actualiserSurvol(e); }
  @Override public void sourisGlissee(MouseEvent e)  {}
  @Override public void sourisCliquee(MouseEvent e)  {}
  @Override public void sourisEnfoncee(MouseEvent e) { enfoncerBoutons(e); }
  @Override public void sourisRelachee(MouseEvent e) { relacherBoutons(e); }
}
