# Projet ANC3 2324 - Groupe c04 - Sokoban

## Notes de version itération 1

### Liste des bugs connus

* la boite de dialogue pour demander à l'utilisateur de sauvegarder avant de creer une nouvelle partie ne s'affiche pas correectement, checker le binding pour faire en sorte que si 1 élément est placé,  le msg s'affiche
* open file ouvre le fichier, affiche les elements mais ne resize pas la grille 
### Liste des fonctionnalités à faire 

* apres exit, boite de dialogue qui  demande si on veut sauvegarder avant de quitter
* limiter le nombre d'éléments à la moitié de la taille de la grille arrondi à l'entier inférieur
* corriger l'affichage relatif à la taille de la grille[main](src%2Fmain)

### Liste des fonctionnalités supplémentaires
*  ajouter un bouton pour reset la grille



### Divers

## Notes de version itération 2

* lorsqu'on lance un niveau qu on fait des mouvement avec le joueur puis qu'on appuie sur finish et qu'on
  retourne sur le niveau . le nbre de mouvement commence a 0 puis apres un mouvement . cela reprend le nombre
  de mouvement de l'ancien niveau.


## Notes de version itération 3

* lorsqu'on place une box sur un goal et qu'on fait un ctrl+Z depuis ce goal. le number of goal reached
  reste a 1 jusqu a ce qu on deplace une autre boite et ca redescend a 0.
*  quand je fait ctrlz et que je passe par un goal avec la box la box disparait
   sur le goal.