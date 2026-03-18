package fr.pattern;

/**
 * Configurations classiques du Jeu de la Vie, stockées comme coordonnées relatives au centre.
 */
public enum PatternType {

  GLIDER("Glider", new int[][]{
      {0, -1}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}
  }),

  BLINKER("Blinker", new int[][]{
      {-1, 0}, {0, 0}, {1, 0}
  }),

  PULSAR("Pulsar", new int[][]{
      // Rangée y=-6
      {-4,-6},{-3,-6},{-2,-6},{2,-6},{3,-6},{4,-6},
      // Colonnes y=-4,-3,-2
      {-6,-4},{-1,-4},{1,-4},{6,-4},
      {-6,-3},{-1,-3},{1,-3},{6,-3},
      {-6,-2},{-1,-2},{1,-2},{6,-2},
      // Rangée y=-1
      {-4,-1},{-3,-1},{-2,-1},{2,-1},{3,-1},{4,-1},
      // Rangée y=+1
      {-4, 1},{-3, 1},{-2, 1},{2, 1},{3, 1},{4, 1},
      // Colonnes y=+2,+3,+4
      {-6, 2},{-1, 2},{1, 2},{6, 2},
      {-6, 3},{-1, 3},{1, 3},{6, 3},
      {-6, 4},{-1, 4},{1, 4},{6, 4},
      // Rangée y=+6
      {-4, 6},{-3, 6},{-2, 6},{2, 6},{3, 6},{4, 6}
  }),

  LWSS("LWSS", new int[][]{
      // .O..O  (dy=-2)
      {-1,-2},{2,-2},
      // O....  (dy=-1)
      {-2,-1},
      // O...O  (dy=0)
      {-2, 0},{2, 0},
      // OOOO.  (dy=+1)
      {-2, 1},{-1, 1},{0, 1},{1, 1}
  });

  private final String label;
  private final int[][] cellules;

  PatternType(String label, int[][] cellules) {
    this.label = label;
    this.cellules = cellules;
  }

  public String getLabel() { return label; }
  public int[][] getCellules() { return cellules; }
}
