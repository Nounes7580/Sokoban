package sokoban.view;

import sokoban.viewmodel.BoardViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;

public class BoardView extends BorderPane {

    // ViewModel
    private final BoardViewModel boardViewModel;

    // Constantes de mise en page
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int GRID_HEIGHT = BoardViewModel.gridHeight();

    private static final int SCENE_MIN_WIDTH = 1000;
    private static final int SCENE_MIN_HEIGHT = 420;

    // Composants principaux
    private final Label headerLabel = new Label("");
    private final HBox headerBox = new HBox();

    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
        start(primaryStage);
    }

    private void start(Stage stage) {
        // Mise en place des composants principaux
        configMainComponents(stage);

        // Mise en place de la scène et affichage de la fenêtre
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }

    private void configMainComponents(Stage stage) {
        stage.setTitle("Grid");

        createGrid();
        createHeader();
    }

    private void createHeader() {
        headerLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));
        headerLabel.getStyleClass().add("header");
        headerBox.getChildren().add(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        setTop(headerBox);
    }

    private void createGrid() {
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var width = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(width / GRID_WIDTH) * GRID_WIDTH;
                },
                widthProperty(),
                heightProperty(),
                headerBox.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var height = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(height / GRID_HEIGHT) * GRID_HEIGHT;
                },
                widthProperty(),
                heightProperty(),
                headerBox.heightProperty());

        GridView gridView = new GridView(boardViewModel.getGridViewModel(), gridWidth, gridHeight);

        // Grille rectangulaire, contraintes de hauteur ajoutées
        gridView.minHeightProperty().bind(gridHeight);
        gridView.prefHeightProperty().bind(gridHeight); // Vous pouvez vouloir ajouter cela pour préférence
        gridView.maxHeightProperty().bind(gridHeight);

        // La largeur doit également être ajustée si votre GridView n'était pas prévu pour un format non-carré
        gridView.minWidthProperty().bind(gridWidth);
        gridView.prefWidthProperty().bind(gridWidth); // Ajustement pour la largeur préférée
        gridView.maxWidthProperty().bind(gridWidth);

        setCenter(gridView);
    }

}
