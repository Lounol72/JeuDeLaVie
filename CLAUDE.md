# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
mvn clean compile          # Build
mvn package                # Build executable JAR
mvn test                   # Run all tests
mvn exec:java              # Run the application
java -jar target/JeuDeLaVie-1.0.jar  # Run the JAR directly
```

To run a single test class:
```bash
mvn test -Dtest=AppTest
```

## Architecture

This is a Java/Swing implementation of Conway's Game of Life, structured around 5 design patterns as a university assignment.

**Entry point:** `fr.lemansU.App` → creates `JeuDeLaVie` (model) and `JeuDeLaVieUi` (controller/view), then starts the game loop.

### Game Loop (`fr.ui.JeuDeLaVieUi`)

Runs at 60 UPS target with adaptive FPS (60 during simulation, 15 on static screens). Each update delegates to the current application state (`EtatJeu`: MENU, GRILLE, PARAMETRES, QUITTER).

### Design Patterns

1. **State** — `CelluleEtat` (alive/dead cell states as singletons) and `EtatJeu` enum + `MethodesEtats` interface (application screens).

2. **Singleton** — `CelluleEtatVivant`, `CelluleEtatMort`, `VisiteurClassique`, `VisiteurTerminal` use double-checked locking.

3. **Observer** — `JeuDeLaVie` implements `Observable`; `JeuDeLaVieUi` implements `Observateur`. After each generation, the model notifies the UI to repaint.

4. **Command** — After visiting all cells, deferred `CommandeVit`/`CommandeMeurt` commands are executed in batch to avoid modifying cells mid-iteration.

5. **Visitor** — `VisiteurClassique` (standard Conway rules), `VisiteurHighLife`, `VisiteurDayNight` apply rules without coupling to `Cellule`. Each visitor enqueues commands.

### Generation cycle

```
JeuDeLaVieUi.mettreAJour()
  → EtatGrille.mettreAJour()
    → JeuDeLaVie.distribueVisiteur()   // each Cellule.accepte(visiteur) → enqueues commands
    → JeuDeLaVie.executeCommandes()    // applies all state changes
    → JeuDeLaVie.notifieObservateur()  // triggers UI repaint
```

### Package map

| Package | Role |
|---|---|
| `fr.cellule` | `Cellule` entity + `CelluleEtat` state interface & implementations |
| `fr.commande` | `CommandeVit`, `CommandeMeurt` — deferred cell state changes |
| `fr.etats` | Application state machine (menu/grid/parameters) |
| `fr.game` | `Game` interface (breaks circular dependency between model and UI) |
| `fr.inputs` | `IClavier`, `ISouris` input interfaces |
| `fr.jeu` | `JeuDeLaVie` (model), `FenetreJeu`, `PanelJeu` |
| `fr.ui` | `Observable`/`Observateur` interfaces, `JeuDeLaVieUi` |
| `fr.ui.bouton` | Swing buttons (play/pause, step, state navigation, terminal toggle) |
| `fr.ui.sliders` | Speed control slider |
| `fr.visiteur` | Rule-set visitors |
| `fr.utilz` | Constants |
