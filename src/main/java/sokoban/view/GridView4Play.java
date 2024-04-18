package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.CellViewModel4Play;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.GridViewModel4Play;

/**
 * Classe GridView4Play représente la grille de jeu dans l'application.
 * Cette grille affiche les cellules de jeu actives pendant la partie.
 */
public class GridView4Play extends GridPane {

    // Constante pour la marge interne utilisée dans la grille.
    protected static final int PADDING = 20;


    /**
     * Constructeur qui initialise la grille avec ses dimensions et la disposition des cellules de jeu.
     * param gridViewModel4Play Modèle de vue de la grille qui contient les informations et la logique pour l'interaction avec les données de la grille de jeu.
     * param gridWidth Liaison DoubleBinding représentant la largeur totale de la grille.
     * param gridHeight Liaison DoubleBinding représentant la hauteur totale de la grille.
     */

    GridView4Play(GridViewModel4Play gridViewModel4Play, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Rend les lignes de la grille visibles pour une meilleure visualisation des cellules.
        setGridLinesVisible(true);
        // Définit l'espacement interne autour de la grille pour assurer une marge uniforme.
        setPadding(new Insets(PADDING));

        // Calcule la largeur et la hauteur de chaque cellule en prenant en compte les marges.
        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel4Play.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel4Play.getGridHeight());
        // Utilise la plus petite dimension pour garantir que chaque cellule reste carrée.
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);
        System.out.println(gridViewModel4Play.getGridWidth());
        // Crée et ajoute les cellules à la grille en fonction des dimensions du modèle de la grille.
        for (int i = 0; i < gridViewModel4Play.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel4Play.getGridHeight(); j++) {
                // Crée une vue pour chaque cellule basée sur le modèle de cellule et ajoute cette vue à la grille.
                CellView4Play cellView4Play = new CellView4Play( gridViewModel4Play.getCellViewModel(i, j), cellSize, this, i, j);
                this.add(cellView4Play, i, j);
            }
        }

    }
}
