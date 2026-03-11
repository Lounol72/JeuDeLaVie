package fr.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.BiConsumer;

import fr.etats.MethodesEtats;
import fr.jeu.PanelJeu;

/** Écoute les événements souris et les transmet à l'état actuel. */
public class ISouris implements MouseListener, MouseMotionListener {

  private final PanelJeu panneauJeu;

  public ISouris(PanelJeu panneauJeu) {
    this.panneauJeu = panneauJeu;
  }

  private void gererEvenementSouris(MouseEvent evt, BiConsumer<MethodesEtats, MouseEvent> action) {
    MethodesEtats etat = panneauJeu.getJeu().getEtatCourant();
    action.accept(etat, evt);
  }

  @Override
  public void mouseDragged(MouseEvent evt) {
    gererEvenementSouris(evt, (etat, evenement) -> etat.sourisGlissee(evenement));
  }

  @Override
  public void mouseMoved(MouseEvent evt) {
    gererEvenementSouris(evt, (etat, evenement) -> etat.sourisDeplacee(evenement));
  }

  @Override
  public void mouseClicked(MouseEvent evt) {
    gererEvenementSouris(evt, (etat, evenement) -> etat.sourisCliquee(evenement));
  }

  @Override
  public void mouseEntered(MouseEvent evt) {}

  @Override
  public void mouseExited(MouseEvent evt) {}

  @Override
  public void mousePressed(MouseEvent evt) {
    gererEvenementSouris(evt, (etat, evenement) -> etat.sourisEnfoncee(evenement));
  }

  @Override
  public void mouseReleased(MouseEvent evt) {
    gererEvenementSouris(evt, (etat, evenement) -> etat.sourisRelachee(evenement));
  }
}
