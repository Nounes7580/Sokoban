package sokoban.view;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import sokoban.model.CellValue;
import sokoban.model.Grid;
import sokoban.viewmodel.BoardViewModel;
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
    private final ImageView goalImageView = new ImageView(goalImage);
    private final ImageView imageView = new ImageView();
    // Effet pour assombrir l'image lorsque la souris survole la cellule
    private final ColorAdjust darkenEffect = new ColorAdjust();
    private int line; // La ligne de cette CellView dans la grille
    private int col;
    double initialDragX;
    double initialDragY;
    private BoardViewModel boardViewModel;

    private GridPane gridPane;

    CellView(CellViewModel cellViewModel, DoubleBinding sizeProperty,GridPane gridPane,  DoubleBinding cellWidth, DoubleBinding cellHeight,int line, int col) {
        this.viewModel = cellViewModel;
        this.sizeProperty = sizeProperty;
        this.line = line;
        this.col = col;
        this.gridPane = gridPane;
        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
        setupMouseEvents(cellWidth, cellHeight);
    }
    public int getLine() {
        return line;
    }

    public int getColumn() {
        return col;
    }

    private void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // Assume that the ground image is the default background.
        backgroundImageView.setImage(groundImage);

        imageView.setPreserveRatio(true);

        // Ajouter le backgroundImageView en tant qu'enfant de la StackPane
        getChildren().add(backgroundImageView);

        // Écouter les changements sur la valeur de la cellule pour mettre à jour la vue
        viewModel.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }

    private void updateView(CellValue cellValue) {
        getChildren().clear();
        getChildren().add(backgroundImageView); // Toujours ajouter le fond.

        // Gérer l'affichage basé sur l'état de la cellule.
        switch (cellValue) {
            case PLAYER:
            case BOX:
            case WALL:
                addImageViewForCellValue(cellValue);
                break;
            case GOAL:
                addImageView(goalImage);
                break;
            case PLAYER_ON_GOAL:
                addImageView(playerImage);
                addImageView(goalImage); // Superposition explicite.
                break;
            case BOX_ON_GOAL:
                addImageView(boxImage);
                addImageView(goalImage); // Superposition explicite.
                break;
        }
    }

    private void addImageViewForCellValue(CellValue cellValue) {
        Image image = switch (cellValue) {
            case PLAYER -> playerImage;
            case BOX -> boxImage;
            case WALL -> wallImage;
            default -> null;
        };
        if (image != null) {
            addImageView(image);
        }
    }

    private void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }

    private void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
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
    private void setupMouseEvents(DoubleBinding cellWidth, DoubleBinding cellHeight) {

        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() ) {
                //System.out.println("Raw Mouse X: " + event.getX() + ", Raw Mouse Y: " + event.getY());
               // System.out.println("GridPane Width: " + gridPane.getWidth() + ", GridPane Height: " + gridPane.getHeight());
                //System.out.println("size cell : " + cellWidth.get() +" ," + cellHeight.get());

                int startCol = (int) Math.round(event.getX() / cellWidth.get());
                int startLine = (int) Math.round(event.getY() / cellHeight.get());
                System.out.println("Start Line: " + startLine);
                System.out.println("Start Column: " + startCol);

            }
        });

        this.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                updateMousePositionInGrid(event);
                Point2D localPoint = gridPane.sceneToLocal(event.getSceneX(), event.getSceneY());
                double offsetX = localPoint.getX();
                double offsetY = localPoint.getY();

                int newCol = (int) Math.floor(offsetX / cellWidth.get());
                int newLine = (int) Math.floor(offsetY / cellHeight.get());

                if (newLine != this.line || newCol != this.col) {
                    // Mise à jour des indices actuels
                    this.line = newLine;
                    this.col = newCol;


                    viewModel.handleMouseDragged(newLine, newCol);
                }
            }
        });


        this.setOnMouseReleased(event -> {
            viewModel.handleMouseReleased();
        });
    }


    private void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        // si on arrête le survol de la cellule, on remet l'échelle à sa valeur par défaut
        if (!newVal)
            viewModel.resetScale();
    }
    private void updateMousePositionInGrid(MouseEvent event) {
        double cellWidth = 0;
        double cellHeight = 0;

        Point2D localPoint = gridPane.sceneToLocal(event.getSceneX(), event.getSceneY());
        double offsetX = localPoint.getX();
        double offsetY = localPoint.getY();

        int newCol = (int) Math.floor(offsetX / cellWidth);
        int newLine = (int) Math.floor(offsetY / cellHeight);


        if (newLine >= 0 && newLine < Grid.getGridHeight() && newCol >= 0 && newCol < Grid.getGridWidth()) {
          
            if (newLine != this.line || newCol != this.col) {
                this.line = newLine;
                this.col = newCol;


                System.out.println("Mouse is at position in grid: line=" + newLine + ", col=" + newCol);

            }
        }
    }
}




