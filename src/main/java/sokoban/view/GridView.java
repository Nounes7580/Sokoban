package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel;

class GridView extends GridPane {
    private static final int PADDING = 20;
    private GridViewModel gridViewModel;
    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));
        this.gridViewModel = gridViewModel;


        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellSize, this, cellWidth, cellHeight, i, j);
                this.add(cellView, j, i);
            }
        }


    }

    public void updateGrid() {
        this.getChildren().clear();
        setPadding(new Insets(PADDING));


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
