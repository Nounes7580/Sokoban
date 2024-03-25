package sokoban.view;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseButton;
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

    private final ColorAdjust darkenEffect = new ColorAdjust();
    private int line;
    private int col;
    double initialDragX;
    double initialDragY;
    private BoardViewModel boardViewModel;

    private GridPane gridPane;

    CellView(CellViewModel cellViewModel, DoubleBinding sizeProperty,GridPane gridPane,  int line, int col) {
        this.viewModel = cellViewModel;
        this.sizeProperty = sizeProperty;
        this.line = line;
        this.col = col;
        this.gridPane = gridPane;
        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
        setupMouseEvents();


        // Add a listener to the valueProperty of the viewModel.
        viewModel.valueProperty().addListener((obs, oldVal, newVal) -> setImage(imageView, newVal));

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

        backgroundImageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(backgroundImageView);

        viewModel.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }

    private void updateView(CellValue cellValue) {
        getChildren().clear();
        getChildren().add(backgroundImageView);


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
                addImageView(goalImage);
                break;
            case BOX_ON_GOAL:
                addImageView(boxImage);
                addImageView(goalImage);
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

        //imageView.fitWidthProperty().bind(sizeProperty.multiply(viewModel.scaleProperty()));

        this.setOnMouseClicked(e -> viewModel.play());

        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(imageView, newVal));

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
                imageView.setImage(null);
                break;
        }
    }
    private void setupMouseEvents() {

        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() ) {

                int startCol = (int) Math.round(event.getX() / sizeProperty.get());
                int startLine = (int) Math.round(event.getY() / sizeProperty.get());
                System.out.println("Start Line: " + startLine);
                System.out.println("Start Column: " + startCol);

            }
        });
        setOnDragDetected(event -> {
            System.out.println("Drag detected");
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                this.startFullDrag(); // Prépare l'élément pour le suivi du glissement
            }
            event.consume();
        });

        setOnMouseDragEntered(event -> {
            System.out.println("Mouse drag entered");
            if (event.getButton() == MouseButton.SECONDARY) {
                viewModel.deleteObject(); // Gestion de la suppression
                System.out.println("Object deleted");
            }

            else if (event.getButton() == MouseButton.PRIMARY) {

                if (viewModel.getSelectedTool() == CellValue.PLAYER) {
                    if (!boardViewModel.hasPlayer()) {
                        viewModel.addObject();
                        System.out.println("Player added");
                    } else {
                        System.out.println("Cannot add another player.");
                    }
                } else {

                    viewModel.addObject(); // Ajoute l'objet
                    System.out.println("Object added");
                }
            }
            event.consume();
        });


        this.setOnMouseReleased(event -> {
            viewModel.handleMouseReleased();
        });
    }


    private void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {

        if (!newVal)
            viewModel.resetScale();
    }


}




