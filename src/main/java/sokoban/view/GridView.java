package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel;

class GridView extends GridPane {
    private static final int PADDING = 20;

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));

        // Use bindings to dynamically adjust to the grid size
        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellSize, cellSize);
                this.add(cellView, j, i);
            }
        }

        // Optional: Listen for changes in grid dimensions to update the view
    }

    private void updateGrid(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Implement logic to update the grid view when dimensions change, similar to the constructor logic
    }
}
