package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import sokoban.viewmodel.CellViewModel4Play;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.GridViewModel4Play;

public class GridView4Play extends GridView{
    GridView4Play(GridViewModel4Play gridViewModel4Play, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));



        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);

        for (int i = 0; i < gridViewModel.getGridWidth(); i++) {
            for (int j = 0; j < gridViewModel.getGridHeight(); j++) {
                CellView4Play cellView4Play = new CellView4Play((CellViewModel4Play) gridViewModel4Play.getCellViewModel(i, j), cellSize, this, i, j);
                this.add(cellView4Play, i, j);
            }
        }

    }
}
