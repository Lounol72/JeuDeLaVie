package fr.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.jeu.PanelJeu;

/**
 * Écoute les événements clavier et les transmet à l’état actuel (Start, Menu, etc.).
 */
public class IClavier implements KeyListener {

  private final PanelJeu gamePanel;

  public IClavier(PanelJeu gamePanel) {
    this.gamePanel = gamePanel;
  }


  @Override
  public void keyPressed(KeyEvent e) {
    // TODO: implement this method
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO: implement this method
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO: implement this method
  }
}
