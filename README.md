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
