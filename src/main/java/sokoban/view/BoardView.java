package sokoban.view;

import javafx.application.Platform;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import sokoban.model.Board;
import sokoban.model.Board4Play;
import sokoban.model.CellValue;

import sokoban.viewmodel.BoardViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sokoban.viewmodel.CellViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;



public abstract class BoardView extends BorderPane {

    // ViewModel
    protected final BoardViewModel boardViewModel;

    // Constantes de mise en page

    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;

    // Composants principaux
    private final VBox toolBar = new VBox(); // La VBox pour la barre d'outils avec un espacement de 10
    // Label pour les messages de validation

    // Ajout pour la barre de menu
    private final MenuBar menuBar = new MenuBar();

    private final VBox topContainer = new VBox();

    private final HBox playButtonContainer = new HBox();


    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;

        setLeft(toolBar);
       // createHeader(); // Ajoutez le label de validation dans cette méthode
        createGrid();
        start(primaryStage);


    }

    private void start(Stage stage) {
        // Mise en place des composants principaux
        // Mise en place de la scène et affichage de la fenêtre
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);



        stage.setScene(scene);
        setupKeyControls(stage.getScene());

        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());


    }
    protected abstract void setupKeyControls(Scene scene);










   protected void createGrid() {
        if (getCenter() != null) {
            ((GridPane) getCenter()).getChildren().clear();
        }

        //taille d'une case
        NumberBinding gridSizeBinding = Bindings.createDoubleBinding(
                () -> Math.min(
                        widthProperty().subtract(toolBar.widthProperty()).divide(boardViewModel.getGridWidth()).get(),
                        heightProperty().subtract(topContainer.heightProperty()).subtract(playButtonContainer.heightProperty()).divide(boardViewModel.getGridHeight()).get()
                ),
                widthProperty(),
                heightProperty()
        );

        gridSizeBinding.addListener((obs,oldVal,newVal) -> {
            System.out.println("grid " + newVal);

        });


        DoubleBinding gridWidthBinding = Bindings.createDoubleBinding(

                () -> {

                    var width = gridSizeBinding.doubleValue() * boardViewModel.getGridWidth();
                    System.out.println("WIDTH" + width + " " + boardViewModel.getGridWidth());
                    return width;
                },
                gridSizeBinding
        );

        DoubleBinding gridHeightBinding = Bindings.createDoubleBinding(
                () -> {
                    var height = gridSizeBinding.doubleValue() * boardViewModel.getGridHeight();
                    System.out.println("HEIGHT " + height+ " " + boardViewModel.getGridHeight());
                    return height;
                },
                gridSizeBinding
        );

        GridView gridView = new GridView(boardViewModel.getGridViewModel(), gridWidthBinding, gridHeightBinding);

        gridView.minHeightProperty().bind(gridHeightBinding);
        gridView.maxHeightProperty().bind(gridHeightBinding);

        gridView.minWidthProperty().bind(gridWidthBinding);
        gridView.maxWidthProperty().bind(gridWidthBinding);
        setCenter(gridView);
    }












}
