package sokoban.view;

import sokoban.model.CellValue;
import sokoban.viewmodel.CellViewModel;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

class CellView extends StackPane {

    private static final Image xImage = new Image("X.png");

    private final CellViewModel viewModel;
    private final DoubleBinding widthProperty;

    private final ImageView imageView = new ImageView();
    private ZoomerView zoomer;

    CellView(CellViewModel cellViewModel, DoubleBinding cellWidthProperty) {
        this.viewModel = cellViewModel;
        this.widthProperty = cellWidthProperty;

        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
    }

    private void layoutControls() {
        imageView.setPreserveRatio(true);

        zoomer = new ZoomerView(viewModel);

        getChildren().addAll(imageView, zoomer);
    }

    private void configureBindings() {
        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(widthProperty);

        // adapte la largeur de l'image à celle de la cellule multipliée par l'échelle
        imageView.fitWidthProperty().bind(widthProperty.multiply(viewModel.scaleProperty()));

        // un clic sur la cellule permet de jouer celle-ci
        this.setOnMouseClicked(e -> viewModel.play());

        // quand la cellule change de valeur, adapter l'image affichée
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(imageView, newVal));

        // gère le survol de la cellule avec la souris
        hoverProperty().addListener(this::hoverChanged);

        // le zoomer n'est visible que si la case contient X et est survolée
        zoomer.visibleProperty().bind(viewModel
                .valueProperty().isEqualTo(CellValue.X)
                .and(hoverProperty()));
    }

    private void setImage(ImageView imageView, CellValue cellValue) {
        imageView.setImage(cellValue == CellValue.EMPTY ? null : xImage);
    }

    private void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        // si on arrête le survol de la cellule, on remet l'échelle à sa valeur par défaut
        if (!newVal)
            viewModel.resetScale();
    }
}
