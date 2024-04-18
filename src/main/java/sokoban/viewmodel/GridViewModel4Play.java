package sokoban.viewmodel;

import sokoban.model.Board4Play;

/**
 * Classe GridViewModel4Play qui sert de modèle de vue pour la grille de jeu dans Sokoban.
 * Cette classe permet de faciliter les interactions entre la vue de la grille de jeu et le modèle de données sous-jacent.
 */
public class GridViewModel4Play{

    private Board4Play board4Play; // Référence au modèle de plateau de jeu.

    /**
     * Constructeur qui initialise le modèle de vue de la grille pour le jeu avec un plateau spécifié.
     * param board Le plateau de jeu (Board4Play) sur lequel cette vue modèle opérera.
     */
    GridViewModel4Play(Board4Play board) {
        this.board4Play=board;
    }

    /**
     * Fournit un modèle de vue pour une cellule spécifique de la grille basée sur ses coordonnées.
     * Cette méthode crée et retourne une nouvelle instance de CellViewModel4Play pour la cellule spécifiée.
     * param line La ligne de la cellule dans la grille.
     * param col La colonne de la cellule dans la grille.
     * return Une instance de CellViewModel4Play pour la cellule à la ligne et colonne spécifiées.
     */
    public CellViewModel4Play getCellViewModel(int line, int col) {
        return new CellViewModel4Play(line,col,board4Play);
    }

    /**
     * Obtient la largeur de la grille de jeu.
     * Cette méthode renvoie la largeur de la grille telle qu'elle est définie dans le modèle Board4Play.
     * return La largeur de la grille.
     */
    public int getGridWidth() {
        return board4Play.getGrid4Play().getGridWidth();
    }

    /**
     * Obtient la hauteur de la grille de jeu.
     * Cette méthode renvoie la hauteur de la grille telle qu'elle est définie dans le modèle Board4Play.
     * return La hauteur de la grille.
     */
    public int getGridHeight() {
        return board4Play.getGrid4Play().getGridHeight();
    }
}
