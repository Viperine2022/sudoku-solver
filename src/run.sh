#!/bin/bash

#############################################################################
#   Script qui affiche la grille du Sudoku déterminée par le solver SAT     #
#############################################################################


# ARGUMENTS DU SCRIPT
# $1 : Le chemin du fichier sur lequel lancer le solver

if [ -z "$1" ]; then
  echo -e "\nUsage : ./run.sh chemin_fichier \nExemple : ./run.sh files/le-monde.csv\n" && exit 1 ;
fi


# Lancement du solver sur le fichier $1 puis affichage 'pretty' de la grille de sudoku remplie

output=$(java Sudoku.java $1 | grep -B 1 "true" | grep "(" | cut -d " " -f 2 | awk -F "_" '{print $2 "," $3 "," expr $4 + 1}' | sort -n | cut -d "," -f 3 | xargs echo -e | tr -d " " | fold -w 9 | awk -F "" '{print " "$1 " " $2 " " $3 "  " $4 " " $5 " " $6 "  " $7 " " $8 " " $9}' | sed '0~3G') && echo -e "\n${output}\n"
