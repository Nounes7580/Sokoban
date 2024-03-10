package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel;

class GridView extends GridPane {
    private static final int PADDING = 20;
    private GridViewModel gridViewModel; // Declare gridViewModel as an instance variable

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));
        this.gridViewModel = gridViewModel; // Initialize the instance variable with the constructor parameter

        // Use bindings to dynamically adjust to the grid size
        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellSize, this, cellWidth, cellHeight, i, j);
                this.add(cellView, j, i);
            }
        }

        // Optional: Listen for changes in grid dimensions to update the view
    }

    public void updateGrid() {
        this.getChildren().clear(); // Clear existing CellViews
        setPadding(new Insets(PADDING)); // Re-apply padding if needed

        // Recreate CellViews based on the current state of gridViewModel
        DoubleBinding cellWidth = widthProperty().subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = heightProperty().subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellSize, this, cellWidth, cellHeight, i, j);
                this.add(cellView, j, i);
            }
        }
    }
}
