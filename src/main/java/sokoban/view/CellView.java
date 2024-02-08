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

    private static final Image playerImage = new Image("player.png");
    private static final Image boxImage = new Image("box.png");
    private static final Image goalImage = new Image("goal.png");
    private static final Image groundImage = new Image("ground.png");


    private static final Image wallImage = new Image("wall.png");

    private final CellViewModel viewModel;
    private final DoubleBinding widthProperty;

    private final ImageView backgroundImageView = new ImageView(groundImage); // Pour l'image de fond

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
        backgroundImageView.setFitWidth(50); // Ajustez la taille selon vos besoins
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setSmooth(true);
        imageView.setPreserveRatio(true);

        zoomer = new ZoomerView(viewModel);

        getChildren().addAll( backgroundImageView, imageView);
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
        switch (cellValue) {
            case WALL:
                imageView.setImage(wallImage);
                break;
            case PLAYER:
                imageView.setImage(playerImage);
                break;
            case BOX:
                imageView.setImage(boxImage);
                break;
            case GOAL:
                imageView.setImage(goalImage);
                break;
            case GROUND:
                imageView.setImage(groundImage);
                break;
            case EMPTY:
                imageView.setImage(null); // Aucune image pour une cellule vide
                break;
        }
    }


    private void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        // si on arrête le survol de la cellule, on remet l'échelle à sa valeur par défaut
        if (!newVal)
            viewModel.resetScale();
    }
}
