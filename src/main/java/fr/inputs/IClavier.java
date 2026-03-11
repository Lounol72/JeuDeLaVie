package fr.inputs;

import fr.jeu.PanelJeu;

/** @deprecated Utiliser {@link AdaptateurClavier} à la place. */
@Deprecated
public class IClavier extends AdaptateurClavier {
  public IClavier(PanelJeu panneauJeu) {
    super(panneauJeu);
  }
}
