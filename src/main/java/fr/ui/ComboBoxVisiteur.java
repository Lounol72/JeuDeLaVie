package fr.ui;

import fr.jeu.JeuDeLaVie;
import fr.utilz.Constants;
import fr.visiteur.TypeVisiteur;
import fr.visiteur.Visiteur;
import fr.visiteur.VisiteurFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * Menu déroulant custom dessiné en Graphics2D pour sélectionner le visiteur actif.
 *
 * <p>Fermé : affiche la sélection courante + symbole ▼. Ouvert : liste toutes les options par
 * dessus le reste de l'UI.
 */
public class ComboBoxVisiteur {

  private static final int ARC = 15;
  private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 16);

  private final TypeVisiteur[] options = TypeVisiteur.values();
  private TypeVisiteur selectionCourante;
  private boolean ouvert = false;
  private int indexSurvol = -1;

  private int x;
  private int y;
  private int largeur;
  private int hauteurLigne;
  private Rectangle delimitation;

  private final JeuDeLaVie jeu;

  public ComboBoxVisiteur(int x, int y, int largeur, int hauteurLigne, JeuDeLaVie jeu) {
    this.x = x;
    this.y = y;
    this.largeur = largeur;
    this.hauteurLigne = hauteurLigne;
    this.jeu = jeu;
    this.selectionCourante = jeu.getTypeVisiteurActif();
    delimitation = new Rectangle(x, y, largeur, hauteurLigne);
  }

  public void setBounds(int x, int y, int largeur, int hauteurLigne) {
    this.x = x;
    this.y = y;
    this.largeur = largeur;
    this.hauteurLigne = hauteurLigne;
    delimitation = new Rectangle(x, y, largeur, hauteurLigne);
  }

  public Rectangle getDelimitation() {
    return delimitation;
  }

  public int hauteurTotaleOuverte() {
    return hauteurLigne + options.length * hauteurLigne;
  }

  public void dessiner(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Color fondFerme =
        ouvert ? Constants.COULEUR_BOUTON_ENFONCE : Constants.COULEUR_BOUTON_NORMAL;
    g2d.setColor(fondFerme);
    g2d.fill(new RoundRectangle2D.Float(x, y, largeur, hauteurLigne, ARC, ARC));
    g2d.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g2d.draw(new RoundRectangle2D.Float(x, y, largeur, hauteurLigne, ARC, ARC));

    g2d.setFont(FONT_LABEL);
    g2d.setColor(Constants.COULEUR_TEXTE);
    FontMetrics fm = g2d.getFontMetrics();
    String label = selectionCourante.getLabel();
    int lx = x + (largeur - fm.stringWidth(label)) / 2 - 10;
    int ly = y + (hauteurLigne + fm.getAscent()) / 2 - 2;
    g2d.drawString(label, lx, ly);

    String fleche = ouvert ? "▲" : "▼";
    int fx = x + largeur - fm.stringWidth(fleche) - 12;
    g2d.drawString(fleche, fx, ly);

    if (ouvert) {
      dessinerListe(g2d, fm);
    }
  }

  private void dessinerListe(Graphics2D g2d, FontMetrics fm) {
    int listY = y + hauteurLigne;
    int totalHauteur = options.length * hauteurLigne;

    g2d.setColor(new Color(0x1a, 0x23, 0x32, 245));
    g2d.fillRoundRect(x, listY, largeur, totalHauteur, ARC, ARC);
    g2d.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g2d.drawRoundRect(x, listY, largeur, totalHauteur, ARC, ARC);

    for (int i = 0; i < options.length; i++) {
      int oy = listY + i * hauteurLigne;
      TypeVisiteur opt = options[i];

      Color fond;
      if (opt == selectionCourante) {
        fond = Constants.COULEUR_CELLULE_VIVANTE.darker();
      } else if (i == indexSurvol) {
        fond = Constants.COULEUR_BOUTON_SURVOL;
      } else {
        fond = Constants.COULEUR_BOUTON_NORMAL;
      }
      g2d.setColor(fond);
      if (i == 0) {
        g2d.fillRoundRect(x, oy, largeur, hauteurLigne, ARC, ARC);
        g2d.fillRect(x, oy + hauteurLigne / 2, largeur, hauteurLigne / 2);
      } else if (i == options.length - 1) {
        g2d.fillRect(x, oy, largeur, hauteurLigne / 2);
        g2d.fillRoundRect(x, oy, largeur, hauteurLigne, ARC, ARC);
      } else {
        g2d.fillRect(x, oy, largeur, hauteurLigne);
      }

      g2d.setFont(FONT_LABEL);
      g2d.setColor(Constants.COULEUR_TEXTE);
      String lbl = opt.getLabel();
      int lx = x + (largeur - fm.stringWidth(lbl)) / 2;
      int ly = oy + (hauteurLigne + fm.getAscent()) / 2 - 2;
      g2d.drawString(lbl, lx, ly);
    }
  }

  public void sourisDeplacee(int mx, int my) {
    if (ouvert) {
      int listY = y + hauteurLigne;
      indexSurvol = -1;
      for (int i = 0; i < options.length; i++) {
        int oy = listY + i * hauteurLigne;
        if (mx >= x && mx <= x + largeur && my >= oy && my < oy + hauteurLigne) {
          indexSurvol = i;
          break;
        }
      }
    }
  }

  public void sourisEnfoncee(int mx, int my) {
    if (ouvert) {
      int listY = y + hauteurLigne;
      for (int i = 0; i < options.length; i++) {
        int oy = listY + i * hauteurLigne;
        if (mx >= x && mx <= x + largeur && my >= oy && my < oy + hauteurLigne) {
          selectionner(options[i]);
          return;
        }
      }
      ouvert = false;
    } else {
      if (delimitation.contains(mx, my)) {
        ouvert = true;
        indexSurvol = -1;
      }
    }
  }

  public void sourisRelachee(int mx, int my) {}

  private void selectionner(TypeVisiteur type) {
    selectionCourante = type;
    ouvert = false;
    jeu.initVisiteur(VisiteurFactory.creer(type, jeu));
    jeu.setTypeVisiteurActif(type);
    jeu.initialiseGrille();
  }

  /** Synchronise la sélection affichée avec le type actif dans le jeu. */
  public void synchroniser() {
    selectionCourante = jeu.getTypeVisiteurActif();
  }
}
