package sokoban.view;

import javafx.beans.binding.Bindings;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

class GridView extends GridPane {
    private static final int PADDING = 20;
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int GRID_HEIGHT = BoardViewModel.gridHeight();


    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Pour visualiser les limites de la grille
        // setStyle("-fx-background-color: lightgrey");

        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));

        DoubleBinding cellHeight = gridHeight
                .subtract(PADDING * 2)
                .divide(BoardViewModel.gridHeight()); // Utilisez la hauteur de la grille pour le calcul de la hauteur de la cellule


        DoubleBinding cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);

        DoubleBinding cellSize = (DoubleBinding) Bindings.min(gridWidth.divide(GRID_WIDTH), gridHeight.divide(GRID_HEIGHT));
        for (int i = 0; i < GRID_WIDTH; ++i) {
            for (int j = 0; j < GRID_HEIGHT; ++j) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellSize, cellSize);
                add(cellView, j, i); // Note: Ensure that CellView constructor accepts size parameters
            }
        }
    }
}
