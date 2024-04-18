package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.GridViewModel4Design;

/**
 * Classe GridView4Design représentant la grille de conception dans l'application .
 * Cette grille permet de positionner et de visualiser les éléments de jeu dans l'environnement de conception.
 */

public class GridView4Design extends GridPane {

    // Définition de la marge interne de la grille.
    protected static final int PADDING = 20;

    /**
     * Constructeur qui initialise la grille avec ses dimensions et la disposition des cellules.
     * param gridViewModel Modèle de vue de la grille qui contient la logique de l'interaction des données de la grille.
     * param gridWidth Liaison DoubleBinding représentant la largeur de la grille.
     * param gridHeight Liaison DoubleBinding représentant la hauteur de la grille.
     */
    GridView4Design(GridViewModel4Design gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Rend les lignes de la grille visibles pour faciliter la conception
        setGridLinesVisible(true);
        // Définit l'espacement interne autour de la grille.
        setPadding(new Insets(PADDING));

        // Calcule la largeur et la hauteur des cellules en prenant en compte les marges.
        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        // Détermine la taille minimale des cellules pour maintenir une grille proportionnelle.
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        // Crée et ajoute chaque cellule de conception à la grille selon les dimensions du modèle de la grille.
        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {

                // Crée une vue pour chaque cellule basée sur le modèle de cellule et ajoute cette vue à la grille.
                CellView4Design cellView = new CellView4Design(gridViewModel.getCellViewModel(i, j), cellSize, this, i, j);
                this.add(cellView, i, j);
            }
        }

    }
}