package sokoban.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sokoban.model.ToolType;
import sokoban.viewmodel.BoardViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;
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
    private final VBox toolBar = new VBox(); // La VBox pour la barre d'outils avec un espacement de 10

    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;

        setLeft(toolBar);
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
        initializeToolBar(stage);
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
    private ImageView createImageView(String resourcePath, ToolType toolType) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(resourcePath)));
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(toolBar.prefWidthProperty()); // Lie la largeur de l'ImageView à celle de la VBox

        return imageView;
    }
    private void initializeToolBar(Stage primaryStage) {
        // Définit l'alignement des outils à l'intérieur de la VBox
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(0, 0, 0, 50)); // Ajoute un padding à gauche de la toolBar
        toolBar.setSpacing(10);

        // Création des ImageView pour chaque outil
        ImageView terrainTool = createImageView("/ground.png", ToolType.TERRAIN);
        ImageView wallTool = createImageView("/wall.png", ToolType.MUR);
        ImageView playerTool = createImageView("/player.png", ToolType.JOUEUR);
        ImageView boxTool = createImageView("/box.png", ToolType.CAISSE);
        ImageView goalTool = createImageView("/goal.png", ToolType.CIBLE);

        // Ajout des ImageView à la VBox
        toolBar.getChildren().addAll(terrainTool, wallTool, playerTool, boxTool, goalTool);

        // Configuration des événements de sélection d'outil
        terrainTool.setOnMouseClicked(event -> selectTool(ToolType.TERRAIN));
        wallTool.setOnMouseClicked(event -> selectTool(ToolType.MUR));
        playerTool.setOnMouseClicked(event -> selectTool(ToolType.JOUEUR));
        boxTool.setOnMouseClicked(event -> selectTool(ToolType.CAISSE));
        goalTool.setOnMouseClicked(event -> selectTool(ToolType.CIBLE));

        // Assurez-vous que les proportions sont préservées et que les images sont ajustées correctement
        for (ImageView tool : Arrays.asList(terrainTool, wallTool, playerTool, boxTool, goalTool)) {
            tool.setPreserveRatio(true);

            StackPane container = new StackPane(tool); // Créez un conteneur pour chaque ImageView
            container.setOnMouseEntered(event -> {
                // Appliquez un style de bordure bleue au conteneur
                container.setStyle("-fx-border-color: lightblue; -fx-border-width: 4; -fx-effect: dropshadow(gaussian, lightblue, 10, 0.5, 0, 0);");
            });

            container.setOnMouseExited(event -> {
                // Retirez le style appliqué lors du survol
                container.setStyle("");
            });

            // Ajoutez le conteneur à la barre d'outils au lieu de l'ImageView directement
            toolBar.getChildren().add(container);
            tool.fitHeightProperty().bind(toolBar.heightProperty().multiply(0.1));
        }

        }


    private void selectTool(ToolType tool) {
        boardViewModel.setSelectedTool(tool); // Mettez à jour l'outil sélectionné dans le ViewModel
        updateToolHighlights(); // Optionnel: Mettez à jour l'interface utilisateur pour refléter l'outil sélectionné
    }

    private void updateToolHighlights() {
        for (Node child : toolBar.getChildren()) {
            if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;
                String id = imageView.getId();
                if (id != null && id.equals(boardViewModel.getSelectedTool().name())) {
                    imageView.setStyle("-fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                } else {
                    imageView.setStyle(null);
                }
            }
        }
    }
}
