package fr.jeu;

import fr.game.Game;
import fr.inputs.IClavier;
import fr.inputs.ISouris;
import fr.utilz.Constants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

public class PanelJeu extends JPanel {

  private final ISouris entreesSouris;
  private final Game jeu;

  public PanelJeu(Game jeu) {
    this.entreesSouris = new ISouris(this);
    this.jeu = jeu;

    Constants.jeu_largeur = 1280;
    Constants.jeu_hauteur = 720;
    setPanelSize();
    addResizeListener();
    addKeyListener(new IClavier(this));
    addMouseListener(entreesSouris);
    addMouseMotionListener(entreesSouris);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    jeu.getEtatCourant().dessiner(g);
  }

  public Game getJeu() {
    return this.jeu;
  }

  private void setPanelSize() {
    setPreferredSize(new Dimension(Constants.jeu_largeur, Constants.jeu_hauteur));
  }

  private void addResizeListener() {
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            int largeur = getWidth();
            int hauteur = getHeight();
            if (largeur > 0 && hauteur > 0) {
              Constants.jeu_largeur = largeur;
              Constants.jeu_hauteur = hauteur;
            }
          }
        });
  }
}
