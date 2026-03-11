package fr.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.BiConsumer;

import fr.etats.MethodesEtats;
import fr.jeu.PanelJeu;

/** Écoute les événements clavier et les transmet à l'état actuel. */
public class IClavier implements KeyListener {

  private final PanelJeu panneauJeu;

  public IClavier(PanelJeu panneauJeu) {
    this.panneauJeu = panneauJeu;
  }

  private void gererEvenementClavier(KeyEvent evt, BiConsumer<MethodesEtats, KeyEvent> action) {
    MethodesEtats etat = panneauJeu.getJeu().getEtatCourant();
    action.accept(etat, evt);
  }

  @Override
  public void keyPressed(KeyEvent evt) {
    gererEvenementClavier(evt, (etat, evenement) -> etat.toucheEnfoncee(evenement));
  }

  @Override
  public void keyReleased(KeyEvent evt) {
    gererEvenementClavier(evt, (etat, evenement) -> etat.toucheRelachee(evenement));
  }

  @Override
  public void keyTyped(KeyEvent evt) {
    gererEvenementClavier(evt, (etat, evenement) -> etat.toucheTapee(evenement));
  }
}
