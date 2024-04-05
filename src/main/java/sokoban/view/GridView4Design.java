package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import sokoban.viewmodel.GridViewModel;

public class GridView4Design extends GridView {
    GridView4Design(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));
        this.gridViewModel = gridViewModel;


        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {
                CellView cellView = new CellView4Design(gridViewModel.getCellViewModel(i, j), cellSize, this, i, j);
                this.add(cellView, i, j);
            }
        }

    }
}
