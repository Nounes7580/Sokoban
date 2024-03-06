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
    private DoubleBinding cellWidth;
    private DoubleBinding cellHeight;

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Pour visualiser les limites de la grille
        // setStyle("-fx-background-color: lightgrey");

        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));

        this.cellHeight = gridHeight
                .subtract(PADDING * 2)
                .divide(BoardViewModel.gridHeight()); // Utilisez la hauteur de la grille pour le calcul de la hauteur de la cellule


        this.cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);

        DoubleBinding cellSize = (DoubleBinding) Bindings.min(gridWidth.divide(GRID_WIDTH), gridHeight.divide(GRID_HEIGHT));
        for (int line = 0; line < GRID_WIDTH; ++line) {
            for (int col = 0; col < GRID_HEIGHT; ++col) {
                GridPane gridPane = new GridPane();
                CellView cellView = new CellView(gridViewModel.getCellViewModel(line, col), cellSize,this,  cellWidth,  cellHeight, line, col);
                this.add(cellView, col, line); // Note: Ensure that CellView constructor accepts size parameters
            }
        }
    }
}
