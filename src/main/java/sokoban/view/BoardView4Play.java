package sokoban.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sokoban.model.Board4Play;
import sokoban.model.element.*;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;


import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class BoardView4Play extends BorderPane {
    private final BoardViewModel4Play boardViewModel4Play;
    private final Label scoreTitleLabel = new Label("Score");
    private static final Label movesLabel = new Label("Number of moves played: 0");
    private final Label goalsLabel = new Label("Number of goals reached: 0 of X"); // X will be dynamically set

    private final HBox headerBox = new HBox();
    private final Label validationLabel = new Label();
    private final VBox toolBar = new VBox();


    private final Label headerLabel = new Label("");
    private final MenuBar menuBar = new MenuBar();
    private final VBox topContainer = new VBox();

    private final HBox playButtonContainer = new HBox();
    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;

    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel4Play) {


        this.boardViewModel4Play = boardViewModel4Play;
        start(primaryStage);
        createFinishButton();

        createHeader(); // Ajoutez le label de validation dans cette méthode
        configMainComponents(primaryStage);
        createGrid();

        /*boardViewModel4Play.gridResetProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                createGrid();
                System.out.println("Grid reset");
                this.boardViewModel4Play.gridResetProperty().set(false);
            }
        });*/

    }

    public void start(Stage stage) {
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);


        stage.setScene(scene);
        stage.setOnShown(event -> {
            createGrid();
        });
        scene.getRoot().requestFocus();
        setupKeyControls(scene);


        stage.show();
        this.requestFocus();  // Request focus on the BorderPane itself

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());


    }

    protected void createGrid() {
        if (getCenter() != null) {
            ((GridPane) getCenter()).getChildren().clear();
        }

        Platform.runLater(() -> {
            //taille d'une case
            NumberBinding gridSizeBinding = Bindings.createDoubleBinding(
                    () -> Math.min(
                            widthProperty().subtract(getToolbarWidth()).divide(boardViewModel4Play.getGridWidth()).get(),
                            heightProperty().subtract(getTopContainerHeight()).subtract(getPlayButtonContainerHeight()).divide(boardViewModel4Play.getGridHeight()).get()
                    ),
                    widthProperty(),
                    heightProperty()
            );


            gridSizeBinding.addListener((obs, oldVal, newVal) -> {
                System.out.println("grid " + newVal);
            });

            DoubleBinding gridWidthBinding = Bindings.createDoubleBinding(
                    () -> gridSizeBinding.doubleValue() * boardViewModel4Play.getGridWidth(),
                    gridSizeBinding
            );

            DoubleBinding gridHeightBinding = Bindings.createDoubleBinding(
                    () -> gridSizeBinding.doubleValue() * boardViewModel4Play.getGridHeight(),
                    gridSizeBinding
            );

            if (boardViewModel4Play instanceof BoardViewModel4Play) {

                GridView4Play gridView = new GridView4Play(boardViewModel4Play.getGridViewModel4Play(), gridWidthBinding, gridHeightBinding);
                System.out.println(gridView);
                gridView.minHeightProperty().bind(gridHeightBinding);
                gridView.maxHeightProperty().bind(gridHeightBinding);

                gridView.minWidthProperty().bind(gridWidthBinding);
                gridView.maxWidthProperty().bind(gridWidthBinding);
                setCenter(gridView);
            }
        });
    }
    public static void updateMovesLabel(int moveCount) {
        // Assuming movesLabel is static or you have an instance to access it
        movesLabel.setText("Number of moves played: " + moveCount);
    }

    private VBox createHeader() {
        scoreTitleLabel.getStyleClass().add("score-title"); // Add style class for big title
        movesLabel.getStyleClass().add("moves-label");
        goalsLabel.getStyleClass().add("goals-label");

        // Debugging line to print out the CSS classes of scoreTitleLabel

        // Arrange labels vertically
        VBox scoreContainer = new VBox(scoreTitleLabel, movesLabel, goalsLabel);
        scoreContainer.setAlignment(Pos.CENTER);

        // Include the scoreContainer in the header
        VBox headerContainer = new VBox(headerLabel, validationLabel, scoreContainer);
        headerContainer.setAlignment(Pos.CENTER);

        return headerContainer;
    }


    protected double getToolbarWidth() {
        return toolBar.getWidth();

    }


    protected double getTopContainerHeight() {
        return topContainer.getHeight();
    }


    protected double getPlayButtonContainerHeight() {
        return playButtonContainer.getHeight();
    }


    private void configMainComponents(Stage stage) {
        // Since we're removing the menu, we don't call initializeMenu here
        initializeToolBar(stage);
        topContainer.getChildren().add(createHeader()); // Removed menuBar from the topContainer
        this.setTop(topContainer);
    }


    private void initializeToolBar(Stage primaryStage) {
        // Définit l'alignement des outils à l'intérieur de la VBox
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(0, 0, 110, 50)); // Ajuste le padding
        toolBar.setSpacing(10);

        // Création et ajout des outils à la VBox en utilisant une méthode modifiée
        addToolToBar("/ground.png", new Ground());
        addToolToBar("/wall.png", new Wall());
        addToolToBar("/player.png", new Player());
        addToolToBar("/box.png", new Box());
        addToolToBar("/goal.png", new Goal());
    }

    private void addToolToBar(String imagePath, Element toolType) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(toolBar.heightProperty().multiply(0.1)); // Ajuste la hauteur de l'image

        StackPane container = new StackPane(imageView);
        container.setPadding(new Insets(5)); // Un peu de padding autour de l'image
        container.setStyle("-fx-border-color: transparent; -fx-border-width: 2; -fx-background-radius: 5;"); // Bordure transparente par défaut

        // Applique un style de bordure bleue au conteneur lors du survol
        container.setOnMouseEntered(e -> container.setStyle("-fx-border-color: lightblue; -fx-border-width: 2; -fx-background-radius: 5;"));
        container.setOnMouseExited(e -> container.setStyle("-fx-border-color: transparent; -fx-border-width: 2; -fx-background-radius: 5;"));

        // Sélectionne l'outil lors du clic sur le conteneur


        // Ajoute le conteneur à la barre d'outils
        toolBar.getChildren().add(container);
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

        exitItem.setOnAction(event -> primaryStage.close());

        // Ajout du menu Fichier à la barre de menu
        menuBar.getMenus().add(fileMenu);
    }

    protected void setupKeyControls(Scene scene) {
        System.out.println("Setting up key controls");
        scene.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode()); // This should output the key pressed

            Board4Play.Direction direction = null;
            switch (event.getCode()) {
                case UP:
                case Z:
                    direction = Board4Play.Direction.UP;
                    break;
                case DOWN:
                case S:
                    direction = Board4Play.Direction.DOWN;
                    break;
                case LEFT:
                case Q:
                    direction = Board4Play.Direction.LEFT;
                    break;
                case RIGHT:
                case D:
                    direction = Board4Play.Direction.RIGHT;
                    break;
            }
            if (direction != null) {
                System.out.println("Moving player in direction: " + direction);
                boardViewModel4Play.movePlayer(direction);
                event.consume();
            }

        });
    }

    private void createFinishButton() {
        Button playButton = new Button("Finish");
        playButton.setOnAction(event -> {
            // Si la grille a été modifiée, demandez si l'utilisateur souhaite sauvegarder les changements




                    Stage stage = (Stage) this.getScene().getWindow();
                    new BoardView4Design(stage,new BoardViewModel4Design(boardViewModel4Play.getBoard()));


        });

        // Désactive le bouton "Play" basé sur le message de validation
       /* playButton.disableProperty().bind(
                boardDesignViewModel.validationMessageProperty().isNotEmpty()
        );

        */

        // Centre le bouton dans le conteneur
        playButtonContainer.getChildren().add(playButton);
        playButtonContainer.setAlignment(Pos.CENTER);
        playButtonContainer.setPadding(new Insets(0, 0, 10, 0));

        // Positionne le conteneur du bouton "Play" en bas du BorderPane
        setBottom(playButtonContainer);
    }


    private TextField createNumericTextField() {
        return new TextField() {
            @Override
            public void replaceText(int start, int end, String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceText(start, end, text);
                }
            }

            @Override
            public void replaceSelection(String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceSelection(text);
                }
            }
        };
    }

    private Label createErrorLabel(String errorMessage) {
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        return errorLabel;
    }

    private Node createDialogButtons(Dialog<Pair<Integer, Integer>> dialog) {
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK

        );
        okButton.setDisable(true);
        return okButton;
    }

    private void validateDimensionInput(String newValue, Label errorLabel, String errorMessage, Node okButton, TextField otherField) {
        if (!newValue.isEmpty()) {
            try {
                int value = Integer.parseInt(newValue);
                boolean isValid = value >= 10 && value <= 50;
                errorLabel.setVisible(!isValid);
                errorLabel.setText(isValid ? "" : errorMessage);
                okButton.setDisable(!isValid || otherField.getText().isEmpty());
            } catch (NumberFormatException e) {
                errorLabel.setVisible(true);
                errorLabel.setText(errorMessage);
                okButton.setDisable(true);
            }
        } else {
            errorLabel.setVisible(false);
            okButton.setDisable(otherField.getText().isEmpty());
        }
    }

}
