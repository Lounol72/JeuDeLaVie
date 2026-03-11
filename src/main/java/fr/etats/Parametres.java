package fr.etats;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import fr.game.Game;
import fr.ui.ComboBoxVisiteur;
import fr.ui.bouton.Bouton;
import fr.ui.bouton.BoutonChangeurEtat;
import fr.ui.bouton.BoutonToggleTerminal;
import fr.utilz.Constants;

/** État paramètres : sélecteur de règles, bouton Terminal, bouton Retour. */
public class Parametres extends Etats {

  private static final int LARGEUR_BOUTON = 400;
  private static final int HAUTEUR_BOUTON = 55;
  private static final int ESPACEMENT = 64;
  private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 16);

  private final ComboBoxVisiteur comboVisiteur;

  public Parametres(Game jeu) {
    super(jeu);
    boutons = new ArrayList<>();
    int cx = Constants.jeu_largeur / 2;
    int cy = Constants.jeu_hauteur / 2 - 100;
    int bw = Math.min(LARGEUR_BOUTON, Constants.jeu_largeur / 2);

    comboVisiteur = new ComboBoxVisiteur(cx - bw / 2, cy, bw, HAUTEUR_BOUTON, jeu.getJeuDeLaVie());

    boutons.add(
        new BoutonToggleTerminal(
            cx - bw / 2, cy + ESPACEMENT, bw, HAUTEUR_BOUTON, jeu.getJeuDeLaVie()));
    boutons.add(
        new BoutonChangeurEtat(
            cx - bw / 2, cy + 2 * ESPACEMENT, bw, HAUTEUR_BOUTON, EtatJeu.MENU, "Retour"));
  }

  @Override
  public void mettreAJour() {}

  @Override
  protected void appliquerDisposition(int largeur, int hauteur) {
    int cx = largeur / 2;
    int cy = hauteur / 2 - 100;
    int bw = Math.min(LARGEUR_BOUTON, largeur / 2);
    comboVisiteur.setBounds(cx - bw / 2, cy, bw, HAUTEUR_BOUTON);
    boutons.get(0).setBounds(cx - bw / 2, cy + ESPACEMENT, bw, HAUTEUR_BOUTON);
    boutons.get(1).setBounds(cx - bw / 2, cy + 2 * ESPACEMENT, bw, HAUTEUR_BOUTON);
  }

  @Override
  public void dessiner(Graphics g) {
    appliquerDispositionSiNecessaire();
    comboVisiteur.synchroniser();
    g.setColor(Constants.COULEUR_FOND_PARAMETRES);
    g.fillRect(0, 0, Constants.jeu_largeur, Constants.jeu_hauteur);

    // Label "Règles :"
    g.setFont(FONT_LABEL);
    g.setColor(Constants.COULEUR_TEXTE);
    FontMetrics fm = g.getFontMetrics();
    java.awt.Rectangle comboRect = comboVisiteur.getDelimitation();
    String labelRegles = "Règles :";
    int lx = comboRect.x + (comboRect.width - fm.stringWidth(labelRegles)) / 2;
    int ly = comboRect.y - 10;
    g.drawString(labelRegles, lx, ly);

    for (Bouton b : boutons) {
      b.dessiner(g);
    }
    // Dessiné en dernier pour passer par-dessus les boutons si ouvert
    comboVisiteur.dessiner(g);
  }

  @Override
  public void toucheEnfoncee(KeyEvent e) {}

  @Override
  public void toucheRelachee(KeyEvent e) {}

  @Override
  public void toucheTapee(KeyEvent e) {}

  @Override
  public void sourisDeplacee(MouseEvent e) {
    comboVisiteur.sourisDeplacee(e.getX(), e.getY());
    for (Bouton b : boutons) {
      b.setSourisSurvol(contient(e, b));
    }
  }

  @Override
  public void sourisGlissee(MouseEvent e) {}

  @Override
  public void sourisCliquee(MouseEvent e) {}

  @Override
  public void sourisEnfoncee(MouseEvent e) {
    comboVisiteur.sourisEnfoncee(e.getX(), e.getY());
    for (Bouton b : boutons) {
      if (contient(e, b)) {
        b.setSourisEnfonce(true);
      }
    }
  }

  @Override
  public void sourisRelachee(MouseEvent e) {
    comboVisiteur.sourisRelachee(e.getX(), e.getY());
    for (Bouton b : boutons) {
      if (b.isSourisEnfonce() && contient(e, b)) {
        b.appliquerAction();
      }
      b.setSourisEnfonce(false);
    }
  }
}
