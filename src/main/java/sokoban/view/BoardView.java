package sokoban.view;

import javafx.application.Platform;
import javafx.beans.binding.NumberBinding;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


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

        Platform.runLater(this::createGrid);



    }
    protected abstract void setupKeyControls(Scene scene);








    protected void createGrid() {
        if (getCenter() != null) {
            ((GridPane) getCenter()).getChildren().clear();
        }
        NumberBinding availableWidth = widthProperty().subtract(getToolbarWidth());

        //taille d'une case
        NumberBinding gridSizeBinding = Bindings.createDoubleBinding(
                () -> {
                    double additionalHeight = this instanceof BoardView4Design ? ((BoardView4Design) this).getAdditionalHeightToSubtract() : 0;
                    double toolbarWidth = this instanceof BoardView4Design ? ((BoardView4Design) this).getToolbarWidth() : 0;
                    return Math.min(
                            widthProperty().subtract(getToolbarWidth()).divide(boardViewModel.getGridWidth()).get(),
                            heightProperty().subtract(additionalHeight).divide(boardViewModel.getGridHeight()).get()
                    );
                },
                widthProperty(),
                heightProperty(),
                toolBar.widthProperty()
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

        if (boardViewModel instanceof BoardViewModel4Design) {
            GridView gridView = new GridView4Design(((BoardViewModel4Design) boardViewModel).getGridViewModel(), gridWidthBinding, gridHeightBinding);

            gridView.minHeightProperty().bind(gridHeightBinding);
            gridView.maxHeightProperty().bind(gridHeightBinding);

            gridView.minWidthProperty().bind(gridWidthBinding);
            gridView.maxWidthProperty().bind(gridWidthBinding);
            setCenter(gridView);
        } else {
            // Handle the case where boardViewModel is not an instance of BoardViewModel4Design
            // This might involve creating a default GridView or handling the error appropriately
        }
    }


    protected abstract double getAdditionalHeightToSubtract();


    protected abstract double getToolbarWidth();
}
