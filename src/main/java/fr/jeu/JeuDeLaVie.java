package fr.jeu;

import java.util.Random;

import fr.cellule.Cellule;
import fr.cellule.CelluleEtatVivant;
import fr.cellule.CelluleEtatMort;

public class JeuDeLaVie
{
  private Cellule grille[][];
  private int xMax;
  private int yMax;

  public JeuDeLaVie()
  {
    xMax = 10;
    yMax = 10;
  }

  public void initialseGrille ()
  {
    Random rnd = new Random(System.currentTimeMillis());
    grille = new Cellule[xMax][yMax];
    for (int i = 0; i < grille.length; i++)
      for (int j = 0; j < grille[i].length; j++)
      {
        grille[i][j] = new Cellule(rnd.nextBoolean() ? CelluleEtatVivant.getInstance() : CelluleEtatMort.getInstance(), i, j );
      }

  }

  public Cellule getGrille(int x, int y)
  {
    // Vérification des inputs
    if (x < 0 || x>= xMax)
      throw new IndexOutOfBoundsException("Index " + x + " out of bounds for size " + xMax);
    if (y < 0 || x>= yMax)
      throw new IndexOutOfBoundsException("Index " + y + " out of bounds for size " + yMax);

    return new Cellule(grille[x][y]);
  }

  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < grille.length; i++) {
      for (int j = 0; j < grille[i].length; j++) {
        s.append(grille[i][j].estVivante() ? 'x' : ' ');
      }
      s.append("\n");
    }
    return s.toString();
  }

  public int getXMax()
  {
    return xMax;
  }
  public int getYMax()
  {
    return yMax;
  }
}
