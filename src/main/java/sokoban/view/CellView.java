package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sokoban.model.CellValue;
import sokoban.model.element.Element;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.CellViewModel;

public abstract class CellView extends StackPane {
    protected static final Image playerImage = new Image("player.png");
    protected static final Image boxImage = new Image("box.png");
    protected static final Image goalImage = new Image("goal.png");
    protected static final Image groundImage = new Image("ground.png");
    protected static final Image wallImage = new Image("wall.png");

    protected final CellViewModel viewModel;
    protected final DoubleBinding sizeProperty;

    protected final ImageView backgroundImageView = new ImageView(groundImage);
    protected ColorAdjust darkenEffect = new ColorAdjust();
    protected BoardViewModel boardViewModel;
    protected final ImageView goalImageView = new ImageView(goalImage);
    protected final ImageView imageView = new ImageView();
    protected final GridPane gridPane;
    protected int line;
    protected int col;

    CellView(CellViewModel cellViewModel, DoubleBinding sizeProperty, GridPane gridPane, int line, int col) {
        this.viewModel = cellViewModel;
        this.sizeProperty = sizeProperty;
        this.line = line;
        this.col = col;
        this.gridPane = gridPane;


    }



    protected abstract void layoutControls();

    protected abstract void addImageView(Image image);
    protected abstract void configureImageView(ImageView imageView);

    protected abstract void updateView(ObservableList<Element> lis);
    protected abstract void addImageViewForCellValue(CellValue cellValue);



    protected abstract void configureBindings();
    protected abstract void setupMouseEvents();
    protected abstract void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal);
}
