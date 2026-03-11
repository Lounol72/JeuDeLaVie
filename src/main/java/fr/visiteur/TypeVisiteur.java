package fr.visiteur;

public enum TypeVisiteur {
  CLASSIQUE("Classique"),
  HIGH_LIFE("HighLife"),
  DAY_NIGHT("Day & Night");

  private final String label;

  TypeVisiteur(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
