package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import sokoban.viewmodel.GridViewModel;

public class GridView4Play extends GridView{
    GridView4Play(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        super(gridViewModel, gridWidth, gridHeight);
    }
}
