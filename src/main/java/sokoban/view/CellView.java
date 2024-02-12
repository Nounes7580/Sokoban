package sokoban.view;

import javafx.scene.effect.ColorAdjust;
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
    private final DoubleBinding sizeProperty;

    private final ImageView backgroundImageView = new ImageView(groundImage); // Pour l'image de fond
    private final ImageView imageView = new ImageView();
    // Effet pour assombrir l'image lorsque la souris survole la cellule
    private final ImageView terrainImageView = new ImageView(groundImage); // Pour l'image de fond du terrain
    private final ImageView wallImageView = new ImageView(); // Pour l'image du mur
    private final ImageView playerImageView = new ImageView(); // Pour l'image du joueur
    private final ImageView boxImageView = new ImageView(); // Pour l'image de la caisse
    private final ImageView goalImageView = new ImageView(); // Pour l'image de la cible

    private final ColorAdjust darkenEffect = new ColorAdjust();

    CellView(CellViewModel cellViewModel, DoubleBinding sizeProperty) {
        this.viewModel = cellViewModel;
        this.sizeProperty = sizeProperty;

        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
    }

    private void layoutControls() {

        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);
        imageView.setPreserveRatio(true);


        getChildren().addAll( backgroundImageView, imageView);
    }

    private void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(sizeProperty);
        backgroundImageView.fitHeightProperty().bind(sizeProperty);
        minWidthProperty().bind(sizeProperty);
        minHeightProperty().bind(sizeProperty);

        // adapte la largeur de l'image à celle de la cellule multipliée par l'échelle
        imageView.fitWidthProperty().bind(sizeProperty.multiply(viewModel.scaleProperty()));

        // un clic sur la cellule permet de jouer celle-ci
        this.setOnMouseClicked(e -> viewModel.play());

        // quand la cellule change de valeur, adapter l'image affichée
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(imageView, newVal));

        // gère le survol de la cellule avec la souris
        hoverProperty().addListener(this::hoverChanged);


        // Lier l'effet d'assombrissement à la propriété hover de la cellule
        hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                darkenEffect.setBrightness(-0.1); // Assombrir l'image
                backgroundImageView.setEffect(darkenEffect);
                imageView.setEffect(darkenEffect);
            } else {
                darkenEffect.setBrightness(0); // Retour à la normale
                backgroundImageView.setEffect(null);
                imageView.setEffect(null);
            }
        });
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
