package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sokoban.model.CellValue;
import sokoban.model.element.Element;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.CellViewModel4Design;

public class CellView4Design extends StackPane {

    // Images statiques pour les différents éléments du jeu.
    protected static final Image playerImage = new Image("player.png");
    protected static final Image boxImage = new Image("box.png");
    protected static final Image goalImage = new Image("goal.png");
    protected static final Image groundImage = new Image("ground.png");
    protected static final Image wallImage = new Image("wall.png");

    // Modèle de données de la cellule et propriété de liaison pour la taille de la vue.
    protected final CellViewModel4Design cellViewModel4Design;
    protected final DoubleBinding sizeProperty;

    // Affichage de l'image de fond pour chaque cellule.
    protected final ImageView backgroundImageView = new ImageView(groundImage);
    private ColorAdjust darkenEffect = new ColorAdjust();

    private final ImageView imageView = new ImageView();

    // Grille dans laquelle cette cellule est placée.
    protected final GridPane gridPane;
    protected int line;
    protected int col;

    /**
     * Constructeur qui initialise la vue de la cellule avec les propriétés nécessaires et configure les interactions.
     * param cellViewModel Modèle de la cellule pour la gestion des données.
     * param sizeProperty Propriété de liaison pour la taille de la vue.
     * param gridPane Grille contenant cette cellule.
     * param line Ligne de la grille où la cellule est située.
     * param col Colonne de la grille où la cellule est située.
     */
    public CellView4Design(CellViewModel4Design cellViewModel, DoubleBinding sizeProperty, GridPane gridPane, int line, int col) {
        this.cellViewModel4Design = cellViewModel;
        this.sizeProperty = sizeProperty;
        this.line = line;
        this.col = col;
        this.gridPane = gridPane;
        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
        setupMouseEvents();


        cellViewModel4Design.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }

    /**
     * Disposition initiale des contrôles, ajout de l'image de fond.
     */
    protected void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        backgroundImageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(backgroundImageView);

        cellViewModel4Design.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
        updateView(cellViewModel4Design.valueProperty());
    }

    /**
     * Met à jour l'affichage de la cellule en fonction des éléments qu'elle contient.
     * param list Liste observable des éléments dans la cellule.
     */
    protected void updateView(ObservableList<Element> list) {
        getChildren().clear();
        getChildren().add(backgroundImageView);
        for (Element value : list) {
            addImageViewForCellValue(value.getType());
        }
    }

    /**
     * Ajoute une image à la vue en fonction du type de cellule.
     * param cellValue Type de la cellule.
     */
    protected void addImageViewForCellValue(CellValue cellValue) {
        Image image = switch (cellValue) {
            case PLAYER -> playerImage;
            case BOX -> boxImage;
            case WALL -> wallImage;
            case GOAL -> goalImage;
            default -> null;
        };
        if (image != null) {
            addImageView(image);
        }
    }

    /**
     * Ajoute une ImageView à la cellule.
     * param image Image à ajouter.
     */
    protected void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }

    /**
     * Configure les propriétés d'une ImageView.
     * param imageView L'ImageView à configurer.
     */
    protected void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
    }

    /**
     * Configure les propriétés de liaison et les événements.
     */
    protected void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(sizeProperty);
        backgroundImageView.fitHeightProperty().bind(sizeProperty);
        minWidthProperty().bind(sizeProperty);
        minHeightProperty().bind(sizeProperty);


        this.setOnMouseClicked(e -> cellViewModel4Design.play());

        cellViewModel4Design.valueProperty().addListener((obs, old, newVal) -> updateView( newVal));

        hoverProperty().addListener(this::hoverChanged);


        hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                darkenEffect.setBrightness(-0.1);
                backgroundImageView.setEffect(darkenEffect);
                imageView.setEffect(darkenEffect);
            } else {
                darkenEffect.setBrightness(0);
                backgroundImageView.setEffect(null);
                imageView.setEffect(null);
            }
        });
    }

    /**
     * Configure les événements de souris pour ajouter ou supprimer des objets.
     */
    protected void setupMouseEvents() {
        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {

                cellViewModel4Design.addObject();

            } else if (event.isSecondaryButtonDown()) {
                cellViewModel4Design.deleteObject();

            }
        });

        setOnDragDetected(event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                this.startFullDrag();
                System.out.println("Drag detected for " + (event.getButton() == MouseButton.PRIMARY ? "adding" : "deleting"));
            }
            event.consume();
        });

        setOnMouseDragEntered(event -> {
            System.out.println("Mouse drag entered");
            if (event.getButton() == MouseButton.SECONDARY) {
                cellViewModel4Design.deleteObject();
                System.out.println("Object deleted during drag");
            }

            else if (event.getButton() == MouseButton.PRIMARY) {
                cellViewModel4Design.addObject();
                System.out.println("Object added during drag");
            }
            event.consume();
        });

        this.setOnMouseReleased(event -> {
            cellViewModel4Design.handleMouseReleased();
        });
    }



    /**
     * Gère les changements d'état de survol de la souris.
     * param obs Observable.
     * param oldVal Ancienne valeur.
     * param newVal Nouvelle valeur.
     */
    protected void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        if (!newVal)
            cellViewModel4Design.resetScale();
    }
}
