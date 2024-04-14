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
    protected static final Image playerImage = new Image("player.png");
    protected static final Image boxImage = new Image("box.png");
    protected static final Image goalImage = new Image("goal.png");
    protected static final Image groundImage = new Image("ground.png");
    protected static final Image wallImage = new Image("wall.png");

    protected final CellViewModel4Design cellViewModel4Design;
    protected final DoubleBinding sizeProperty;

    protected final ImageView backgroundImageView = new ImageView(groundImage);
    private ColorAdjust darkenEffect = new ColorAdjust();
    private BoardViewModel4Design boardViewModel;


    private final ImageView imageView = new ImageView();


    protected final GridPane gridPane;
    protected int line;
    protected int col;

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


        // Add a listener to the valueProperty of the viewModel.
        cellViewModel4Design.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }


    protected void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        backgroundImageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(backgroundImageView);

        cellViewModel4Design.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
        updateView(cellViewModel4Design.valueProperty());
    }

    protected void updateView(ObservableList<Element> list) {
        getChildren().clear();
        getChildren().add(backgroundImageView);
        for (Element value : list) {
            addImageViewForCellValue(value.getType());
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

    protected void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }

    protected void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
    }


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


    protected void setupMouseEvents() {
        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                // Start position calculation only necessary if it affects dragging logic or initial placement
                int startCol = (int) Math.round(event.getX() / sizeProperty.get());
                int startLine = (int) Math.round(event.getY() / sizeProperty.get());
                cellViewModel4Design.addObject(); // Adds the object where the drag begins
                this.startFullDrag(); // Start drag for left-click to allow adding objects by dragging
            } else if (event.isSecondaryButtonDown()) {
                cellViewModel4Design.deleteObject(); // Deletes the object where the right-click occurs
                this.startFullDrag(); // Also start drag for right-click for consistent drag detection
            }
        });

        setOnDragDetected(event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                this.startFullDrag(); // Prepare the element for drag tracking on both clicks
                System.out.println("Drag detected for " + (event.getButton() == MouseButton.PRIMARY ? "adding" : "deleting"));
            }
            event.consume();
        });

        setOnMouseDragEntered(event -> {
            System.out.println("Mouse drag entered");
            if (event.getButton() == MouseButton.SECONDARY) {
                cellViewModel4Design.deleteObject(); // Handle deletion during drag with right-click
                System.out.println("Object deleted during drag");
            }
            // Optionally, handle continuous adding while dragging with left-click
            else if (event.getButton() == MouseButton.PRIMARY) {
                cellViewModel4Design.addObject(); // Adds the object continuously while dragging
                System.out.println("Object added during drag");
            }
            event.consume();
        });

        this.setOnMouseReleased(event -> {
            cellViewModel4Design.handleMouseReleased();
        });
    }




    protected void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        if (!newVal)
            cellViewModel4Design.resetScale();
    }
}
