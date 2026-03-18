package fr.ui.bouton;

import fr.utilz.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.JColorChooser;

public class BoutonCouleur extends Bouton {

  private final String label;
  private final Supplier<Color> getCouleur;
  private final Consumer<Color> setCouleur;

  public BoutonCouleur(int x, int y, int w, int h, String label,
                       Supplier<Color> getCouleur, Consumer<Color> setCouleur) {
    super(x, y, w, h);
    this.label = label;
    this.getCouleur = getCouleur;
    this.setCouleur = setCouleur;
  }

  @Override
  public void dessiner(Graphics g) {
    super.dessiner(g);

    // Pastille colorée à droite
    int swatchSize = 14;
    int swatchX = x + largeur - swatchSize - 6;
    int swatchY = y + (hauteur - swatchSize) / 2;
    g.setColor(getCouleur.get());
    g.fillRect(swatchX, swatchY, swatchSize, swatchSize);
    g.setColor(Constants.COULEUR_BOUTON_BORDURE);
    g.drawRect(swatchX, swatchY, swatchSize, swatchSize);

    dessinerLabel(g, label);
  }

  @Override
  public void appliquerAction() {
    Color choix = JColorChooser.showDialog(null, label, getCouleur.get());
    if (choix != null) setCouleur.accept(choix);
  }
}
