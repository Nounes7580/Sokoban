package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sokoban.model.CellValue;
import sokoban.model.Grid4Design;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Play;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.CellViewModel4Play;
import javafx.scene.text.Font;

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
        System.out.println("Updating view for cell at (" + line + ", " + col + ") with elements: " + list);
        getChildren().clear();
        getChildren().add(backgroundImageView);

        for (Element value : list) {
            System.out.println("Processing element type: " + value.getType() + " in cell at (" + line + ", " + col + ")");
            switch (value.getType()) {
                case PLAYER:
                    System.out.println("Adding player image to cell at (" + line + ", " + col + ")");
                    addImageView(playerImage);
                    break;
                case BOX:
                    System.out.println("Adding box image to cell at (" + line + ", " + col + ")");
                    addImageView(boxImage);
                    Box box = (Box) value;
                    if (box.getId() == 0) { // If the box doesn't have an ID yet
                        int newId = Grid4Design.incrementBoxCount(); // Increment the box count
                        box.setId(newId); // Assign the new ID to the box
                    }
                    Label label = new Label(String.valueOf(box.getId()));
                    label.setFont(new Font("Arial", 20));
                    label.setTextFill(Color.BLACK);
                    label.setStyle("-fx-background-color: white; -fx-padding: 5px;");
                    getChildren().add(label);
                    break;
                case GOAL:
                    System.out.println("Adding goal image to cell at (" + line + ", " + col + ")");
                    addImageView(goalImage);
                    break;
                    case WALL:

                    System.out.println("Adding wall image to cell at (" + line + ", " + col + ")");
                    addImageView(wallImage);
                    break;
                case GROUND:
                    System.out.println("Adding ground image to cell at (" + line + ", " + col + ")");
                    addImageView(groundImage);
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





    protected void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        if (!newVal)
            cellViewModel4Play.resetScale();
    }
}
