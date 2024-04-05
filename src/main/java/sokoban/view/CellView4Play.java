package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sokoban.model.CellValue;
import sokoban.model.element.Element;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.CellViewModel4Play;

public class CellView4Play extends CellView {
    CellView4Play(CellViewModel4Play cellViewModel4Play, DoubleBinding sizeProperty, GridPane gridPane, int line, int col) {
        super(cellViewModel4Play, sizeProperty, gridPane, line, col);
    }

    @Override
    protected void layoutControls() {

    }

    @Override
    protected void addImageView(Image image) {

    }

    @Override
    protected void configureImageView(ImageView imageView) {

    }

    @Override
    protected void updateView(ObservableList<Element> lis) {

    }

    @Override
    protected void addImageViewForCellValue(CellValue cellValue) {

    }

    @Override
    protected void configureBindings() {

    }

    @Override
    protected void setupMouseEvents() {

    }

    @Override
    protected void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {

    }
}
