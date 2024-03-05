package sokoban.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import sokoban.model.CellValue;

import sokoban.viewmodel.BoardViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class BoardView extends BorderPane {

    // ViewModel
    private final BoardViewModel boardViewModel;

    // Constantes de mise en page
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int GRID_HEIGHT = BoardViewModel.gridHeight();

    private static final int SCENE_MIN_WIDTH = 1000;
    private static final int SCENE_MIN_HEIGHT = 400;

    // Composants principaux
    private final Label headerLabel = new Label("");
    private final HBox headerBox = new HBox();
    private final VBox toolBar = new VBox(); // La VBox pour la barre d'outils avec un espacement de 10
    // Label pour les messages de validation
    private final Label validationLabel = new Label();

    // Ajout pour la barre de menu
    private final MenuBar menuBar = new MenuBar();


    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;

        setLeft(toolBar);
        createHeader(); // Ajoutez le label de validation dans cette méthode
        createGrid();
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
        stage.setTitle("Sokoban");
        initializeToolBar(stage);

        Label validationLabel = new Label();
        validationLabel.textProperty().bind(boardViewModel.validationMessageProperty());
        // Add the label to the UI
        headerBox.getChildren().add(validationLabel);
        // Initialiser le menu Fichier dans la barre de menu
        initializeMenu(stage);
        // Création du container pour le menu et l'en-tête
        VBox topContainer = new VBox(menuBar, createHeader());

        // Ajout du container combiné à l'interface utilisateur
        this.setTop(topContainer);

    }

    private VBox createHeader() {
        headerLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));
        headerLabel.getStyleClass().add("header");

        // Configuration du label de validation
        validationLabel.textProperty().bind(boardViewModel.validationMessageProperty());
        validationLabel.setTextFill(Color.RED);

        // Vérifiez que le texte n'est pas null avant d'appeler isEmpty()
        validationLabel.visibleProperty().bind(
                Bindings.createBooleanBinding(
                        () -> validationLabel.getText() != null && !validationLabel.getText().isEmpty(),
                        validationLabel.textProperty()
                )
        );

        // Ajoutez le label de validation sous le headerLabel
        VBox headerContainer = new VBox(headerLabel, validationLabel);
        headerContainer.setAlignment(Pos.CENTER);
        headerContainer.setPadding(new Insets(10));

        // Retourner le container de l'en-tête au lieu de le placer directement en haut du BorderPane
        return headerContainer;
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
    private ImageView createImageView(String resourcePath, CellValue toolType) {
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
        ImageView terrainTool = createImageView("/ground.png", CellValue.GROUND);
        ImageView wallTool = createImageView("/wall.png", CellValue.WALL);
        ImageView playerTool = createImageView("/player.png", CellValue.PLAYER);
        ImageView boxTool = createImageView("/box.png", CellValue.BOX);
        ImageView goalTool = createImageView("/goal.png", CellValue.GOAL);

        // Ajout des ImageView à la VBox
        toolBar.getChildren().addAll(terrainTool, wallTool, playerTool, boxTool, goalTool);

        // Configuration des événements de sélection d'outil
        terrainTool.setOnMouseClicked(event -> selectTool(CellValue.GROUND));
        wallTool.setOnMouseClicked(event -> selectTool(CellValue.WALL));
        playerTool.setOnMouseClicked(event -> selectTool(CellValue.PLAYER));
        boxTool.setOnMouseClicked(event -> selectTool(CellValue.BOX));
        goalTool.setOnMouseClicked(event -> selectTool(CellValue.GOAL));

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


    private void selectTool(CellValue tool) {
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
    private void initializeMenu(Stage primaryStage) {
        // creation du menu Fichier
        Menu fileMenu = new Menu("File");

        // Creation des element du menu
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");

        // Ajout des éléments de menu au menu Fichier
        fileMenu.getItems().addAll(newItem, openItem, saveAsItem, exitItem);

        // Configuration les actions pour les éléments de menu
        newItem.setOnAction(event -> handleNew());
        openItem.setOnAction(event -> handleOpen(primaryStage));
        saveAsItem.setOnAction(event -> handleSaveAs(primaryStage));
        exitItem.setOnAction(event -> primaryStage.close());

        // Ajout du menu Fichier à la barre de menu
        menuBar.getMenus().add(fileMenu);
    }
    private void handleNew() {
        // Implémentation futur pour la creation du nouveau lvl
    }
    private void handleOpen(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        // Définir le filtre d'extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sokoban files (*.skb)", "*.skb");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue d'ouverture de fichier
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            // Implémenter futur pour ouvrir un niveau à partir du fichier
        }
    }

    private void handleSaveAs(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        // Définir le filtre d'extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sokoban files (*.skb)", "*.skb");
        fileChooser.getExtensionFilters().add(extFilter);

        // Affichage de la ToolBar  de sauvegarde de fichier
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            // Implémention futur pour la logique pour sauvegarder le niveau dans le fichier
        }
    }
}
