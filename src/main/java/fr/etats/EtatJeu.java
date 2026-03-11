package fr.etats;

/** Énumération des états possibles de l'application (machine à états). */
public enum EtatJeu {
  MENU,
  GRILLE,
  PARAMETRES,
  QUITTER;

  private static EtatJeu etatActuel = MENU;
  private static EtatJeu etatPrecedent = MENU;

  public static EtatJeu getEtatPrecedent() {
    return etatPrecedent;
  }

  public static EtatJeu getEtatActuel() {
    return etatActuel;
  }

  public static void setEtatActuel(EtatJeu etat) {
    etatPrecedent = etatActuel;
    etatActuel = etat;
  }
}
