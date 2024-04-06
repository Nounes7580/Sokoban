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
import sokoban.viewmodel.BoardViewModel4Play;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.CellViewModel4Play;

public class CellView4Play extends StackPane {
    protected static final Image playerImage = new Image("player.png");
    protected static final Image boxImage = new Image("box.png");
    protected static final Image goalImage = new Image("goal.png");
    protected static final Image groundImage = new Image("ground.png");
    protected static final Image wallImage = new Image("wall.png");

    protected final CellViewModel4Play cellViewModel4Play;
    protected final DoubleBinding sizeProperty;

    protected final ImageView backgroundImageView = new ImageView(groundImage);
    protected ColorAdjust darkenEffect = new ColorAdjust();
    protected BoardViewModel4Play boardViewModel;
    protected final ImageView goalImageView = new ImageView(goalImage);
    protected final ImageView imageView = new ImageView();
    protected final GridPane gridPane;
    protected int line;
    protected int col;
    CellView4Play(CellViewModel4Play cellViewModel4Play, DoubleBinding sizeProperty, GridPane gridPane, int line, int col) {
        this. cellViewModel4Play = cellViewModel4Play;
        this.sizeProperty = sizeProperty;
        this.line = line;
        this.col = col;
        this.gridPane = gridPane;
        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
        setupMouseEvents();


        // Add a listener to the valueProperty of the viewModel.
        cellViewModel4Play.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }

    protected void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        backgroundImageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(backgroundImageView);

        cellViewModel4Play.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
        updateView(cellViewModel4Play.valueProperty());
    }

    protected void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }


    protected void updateView(ObservableList<Element> list) {
        System.out.println("Updating view for cell at (" + line + ", " + col + ")");
        getChildren().clear();  // Clear current view
        getChildren().add(backgroundImageView);  // Add the background image

        for (Element value : list) {
            System.out.println("Element type: " + value.getType());
            switch (value.getType()) {
                case PLAYER:
                    System.out.println("Adding player image to cell at (" + line + ", " + col + ")");
                    addImageView(playerImage);
                    break;
                case GROUND:
                    System.out.println("Ground cell at (" + line + ", " + col + ")");
                    // If you have a specific ground image, ensure it's displayed here.
                    break;
                // Handle other cases similarly...
                case BOX:
                    System.out.println("Adding box image to cell at (" + line + ", " + col + ")");
                    addImageView(boxImage);
                    break;
                    case WALL:
                    System.out.println("Adding wall image to cell at (" + line + ", " + col + ")");
                    addImageView(wallImage);
                    break;
                case GOAL:
                    System.out.println("Adding goal image to cell at (" + line + ", " + col + ")");
                    addImageView(goalImage);
                    break;
            }
        }
    }

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

    protected void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
        System.out.println("Image view size: " + imageView.getFitWidth() + "x" + imageView.getFitHeight());
    }

    protected void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(sizeProperty);
        backgroundImageView.fitHeightProperty().bind(sizeProperty);
        minWidthProperty().bind(sizeProperty);
        minHeightProperty().bind(sizeProperty);

        //imageView.fitWidthProperty().bind(sizeProperty.multiply(viewModel.scaleProperty()));

        this.setOnMouseClicked(e -> cellViewModel4Play.play());

        cellViewModel4Play.valueProperty().addListener((obs, old, newVal) -> updateView( newVal));

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

    protected void setupMouseEvents() {
        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() ) {

                int startCol = (int) Math.round(event.getX() / sizeProperty.get());
                int startLine = (int) Math.round(event.getY() / sizeProperty.get());

            }
        });
        setOnDragDetected(event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                this.startFullDrag(); // Prépare l'élément pour le suivi du glissement
            }
            event.consume();
        });

        setOnMouseDragEntered(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                cellViewModel4Play.deleteObject(); // Gestion de la suppression
            }

            else if (event.getButton() == MouseButton.PRIMARY) {

                if (cellViewModel4Play.getSelectedTool().getType() == CellValue.PLAYER) {
                    if (!boardViewModel.hasPlayer()) {
                        cellViewModel4Play.addObject();
                        System.out.println("Player added");
                    } else {
                        System.out.println("Cannot add another player.");
                    }
                } else {

                    cellViewModel4Play.addObject(); // Ajoute l'objet
                }
            }
            event.consume();
        });


        this.setOnMouseReleased(event -> {
            cellViewModel4Play.handleMouseReleased();
        });
    }




    protected void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        if (!newVal)
            cellViewModel4Play.resetScale();
    }
}
