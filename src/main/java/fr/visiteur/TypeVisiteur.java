package fr.visiteur;

public enum TypeVisiteur {
  CLASSIQUE(        "Classique",       "B3/S23 — La règle originale de Conway. Équilibre croissance et mort."),
  HIGH_LIFE(        "HighLife",        "B36/S23 — Comme Conway + naissance sur 6 voisins. Planeurs fréquents."),
  DAY_NIGHT(        "Day & Night",     "B3678/S34678 — Symétrique vie/mort. Structures denses et stables."),
  SEEDS(            "Seeds",           "B2/S — Toutes les cellules meurent. Explosion depuis tout motif."),
  MAZE(             "Maze",            "B3/S12345 — Génère des labyrinthes denses depuis un état aléatoire."),
  REPLICATOR(       "Replicator",      "B1357/S1357 — Tout motif se réplique en copies auto-similaires."),
  LIFE_WITHOUT_DEATH("Life w/o Death", "B3/S∞ — Naît comme Conway mais ne meurt jamais. Croissance infinie.");

  private final String label;
  private final String description;

  TypeVisiteur(String label, String description) {
    this.label = label;
    this.description = description;
  }

  public String getLabel() {
    return label;
  }

  public String getDescription() {
    return description;
  }
}
