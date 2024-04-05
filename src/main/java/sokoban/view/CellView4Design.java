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
import sokoban.model.CellValue;
import sokoban.model.element.Element;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.CellViewModel;

public class CellView4Design extends CellView {
    private ColorAdjust darkenEffect = new ColorAdjust();
    private BoardViewModel4Design boardViewModel;
    private final ImageView backgroundImageView = new ImageView(groundImage); // Pour l'image de fond
    private final ImageView goalImageView = new ImageView(goalImage);
    private final ImageView imageView = new ImageView();

    private CellView4Design cellView4Design;

    public CellView4Design(CellViewModel cellViewModel, DoubleBinding sizeProperty, GridPane gridPane, int line, int col) {
        super(cellViewModel, sizeProperty,gridPane, line, col);
        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
        setupMouseEvents();


        // Add a listener to the valueProperty of the viewModel.
        viewModel.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }

    @Override
    protected void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        backgroundImageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(backgroundImageView);

        viewModel.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }
    @Override
    protected void updateView(ObservableList<Element> list) {
        getChildren().clear();
        getChildren().add(backgroundImageView);
        for (Element value : list) {
            addImageViewForCellValue(value.getType());
        }
    }
    @Override
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
    @Override
    protected void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }
    @Override
    protected void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
    }

    @Override
    protected void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(sizeProperty);
        backgroundImageView.fitHeightProperty().bind(sizeProperty);
        minWidthProperty().bind(sizeProperty);
        minHeightProperty().bind(sizeProperty);

        //imageView.fitWidthProperty().bind(sizeProperty.multiply(viewModel.scaleProperty()));

        this.setOnMouseClicked(e -> viewModel.play());

        viewModel.valueProperty().addListener((obs, old, newVal) -> updateView( newVal));

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

    @Override
    protected void setupMouseEvents() {
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

                if (viewModel.getSelectedTool().getType() == CellValue.PLAYER) {
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




    @Override
    protected void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        if (!newVal)
            viewModel.resetScale();
    }
}
