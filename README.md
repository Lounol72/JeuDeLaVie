# Jeu de la vie
Dans le cadre du TP3 de Design Pattern nous allons créer un automate cellulaire appellé Jeu de la vie.

## Qu'est-ce qu'un automate cellulaire ?

Un automate cellulaire est un objet mathématique qui permet de simuler l'évolution d'une population de cellules virtuelles au cours de temps, selon des règles de voisinage. Ces règles sont appliquées à toutes les cellules d'une même génération, produisant ainsi une nouvelle génération, entirèrement dépendante de la génération précédente. Le "Jeu de la Vie" du mathématicien John Conway est un exemple d'automate cellulaire à deux dimensions dont les règles d'évolution sont les suivantes :
 - Si une cellule possède moins de deux voisines, elle va mourir (solitude);
 - Si une cellule possède plus de trois voisines elle va mourir (etouffement);
 - Si un emplacement vide possède trois voisines, une nouvelle cellule va naître;

Nous sommes ordonnés d'implémenter le Jeu de la Vie selon un modèle fondé sur cinq Design Pattern :

- Etat
- Singleton
- Observateur
- Commande
- Visiteur

En séparant structures de données, règles de fonctionnement et interface graphique, la modélisation fondée sur les patrons de conception simplifie considérablement la maintenance du logiciel.

# 1. Implémentation des cellules et leurs états
Premièrement nous allons implémentre la structure de données de Jeu de la Vie à l'aide d'un tableau de cellules, chaque cellule étant caractérisée par son état (vivante ou morte)? Le modèle de référennce est la version minimale du DP Etat. Son instanciation pour le Jeu de la Vie est sur le diagramme d'états ci-dessous. La modification de l'état d'une cellule se fait par invocation des méthodes _vit_ et _meurt_ de la cellule.

![CelluleEtat Diagramme](docs/CelluleEtat.png "CelluleEtat")

Ce modèle représente un inconvénient: celui de multiplier inutilement les instance des classes réprésentant les états vivant et mort des cellules. En effet, les classes ***CelluleEtatVivant***  ***CelluleEtatMort*** n'ont pas de variables d'instances. Leur état (au sens POO) ne peut pas être modifié. Il est donc inutle de disposer de plusieurs instances de ces classes. Vous allez y remédier en implementant le DP Singleton.

# 2. Implémentation de la grille de cellules
