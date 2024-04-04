package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import sokoban.viewmodel.GridViewModel;

public class GridView4Design extends GridView {
    GridView4Design(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        super(gridViewModel, gridWidth, gridHeight);
    }
}
