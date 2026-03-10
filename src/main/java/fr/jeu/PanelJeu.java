package fr.jeu;

import fr.inputs.IClavier;
import fr.inputs.ISouris;
import fr.ui.JeuDeLaVieUi;
import fr.utilz.Constants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

public class PanelJeu extends JPanel {
  private final ISouris mouseInputs;
  private final JeuDeLaVieUi jeu;

  public PanelJeu(JeuDeLaVieUi jeu) {
    this.mouseInputs = new ISouris(this);
    this.jeu = jeu;

    Constants.jeu_largeur = 1920;
    Constants.jeu_hauteur = 1080;
    setPanelSize();
    addResizeListener();
    addKeyListener(new IClavier(this));
    addMouseListener(mouseInputs);
    addMouseMotionListener(mouseInputs);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    jeu.render(g);
  }

  public JeuDeLaVieUi getGame() {
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
            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
              Constants.jeu_largeur = w;
              Constants.jeu_hauteur = h;
            }
          }
        });
  }
}
