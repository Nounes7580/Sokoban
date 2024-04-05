package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel;

public abstract class GridView extends GridPane {
    protected static final int PADDING = 20;
    protected GridViewModel gridViewModel;

}
