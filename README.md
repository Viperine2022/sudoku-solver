#                                                   	Sudoku Solver



> **Le but de ce projet est de modéliser le remplissage d'une grille de Sudoku sous la forme d'un problème SAT. Ceci permet de remplir toute grille de Sudoku en un temps très court**


<img src="./images/sudoku.png" alt="sudoku" style="zoom:50%;" />

Le **Sudoku** est un jeu en forme de grille de taille 9x9 décomposée en 9 carrés de taille 3x3.
Sur chaque cellule, on peut y écrire un chiffre entre 1 et 9.

Une grille de **Sudoku** remplie doit respecter les trois règles suivantes :

- Chaque colonne doit contenir tous les chiffres de 1 à 9
- Chaque ligne doit contenir tous les chiffres de 1 à 9
- Chaque pavé de 3×3 doit contenir tous les chiffres de 1 à 9

<br>

En ce sens, nous pouvons modéliser le Sudoku en problème SAT. La grille forme une matrice carrée de taille 9 : A = (A<sub>ij</sub>), <sub>i, j ∈ 1..9</sub>.

On associe à chaque cellule A<sub>ij</sub> de la grille 9 variables booléennes. Ceci nous donne en tout 9<sup>3</sup> = 729 variables booléennes. On a :

- ∀ k ∈ 1..9, A<sub>ijk</sub> ne peut prendre que les valeurs *TRUE* ou *FALSE*
- A<sub>ijk</sub> = *TRUE* ⇔  On écrit k sur la grille, sur la colonne i et la ligne j. 
- Par exemple si A<sub>115</sub> = *TRUE*, cela signifie qu'on a placé un 5 tout en haut à gauche de la grille du Sudoku.

Pour garder un certain sens à notre modélisation, on ajoute de nouvelles contraintes (par exemple, on ne peut pas avoir écrit plusieurs chiffres sur une même case)
