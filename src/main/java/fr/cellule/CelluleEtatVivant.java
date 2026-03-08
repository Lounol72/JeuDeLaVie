package fr.cellule;

public class CelluleEtatVivant implements CelluleEtat
{
  private static volatile CelluleEtatVivant instance = null;
  private CelluleEtatVivant()
  {
  }

  public static CelluleEtat getInstance()
  {
    if (instance != null) return instance;
    synchronized (CelluleEtatVivant.class)
    {
      if (instance == null) instance = new CelluleEtatVivant();
    }
    return instance;
  }
  @Override
  public CelluleEtat vit()
  {
    return this;
  }

  @Override
  public CelluleEtat meurt()
  {
    return CelluleEtatMort.getInstance();
  }

  @Override
  public boolean estVivant()
  {
    return true;
  }
}
