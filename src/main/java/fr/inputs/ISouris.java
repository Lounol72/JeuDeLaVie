package fr.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import fr.jeu.PanelJeu;

/**
 * Écoute les événements souris (clic, mouvement, drag) et les transmet à l’état actuel.
 */
public class ISouris implements MouseListener, MouseMotionListener {

  private final PanelJeu gamePanel;

  public ISouris(PanelJeu gamePanel) {
    this.gamePanel = gamePanel;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    // TODO: implement this method
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    // TODO: implement this method
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO: implement this method
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO: implement this method
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO: implement this method
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO: implement this method
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO: implement this method
  }
}
