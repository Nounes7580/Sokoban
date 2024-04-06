package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.CellViewModel4Play;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.GridViewModel4Play;


public class GridView4Play extends GridPane {
    protected static final int PADDING = 20;

    GridView4Play(GridViewModel4Play gridViewModel4Play, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));


        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(gridViewModel4Play.getGridWidth());
        DoubleBinding cellHeight = gridHeight.subtract(PADDING * 2).divide(gridViewModel4Play.getGridHeight());
        DoubleBinding cellSize = (DoubleBinding) Bindings.min(cellWidth, cellHeight);
        System.out.println(gridViewModel4Play.getGridWidth());
        for (int i = 0; i < gridViewModel4Play.getGridWidth(); i++) {

            for (int j = 0; j < gridViewModel4Play.getGridHeight(); j++) {
                CellView4Play cellView4Play = new CellView4Play( gridViewModel4Play.getCellViewModel(i, j), cellSize, this, i, j);
                this.add(cellView4Play, i, j);
            }
        }

    }
}
