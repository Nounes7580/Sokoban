package sokoban.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sokoban.model.Board4Play;
import sokoban.model.CommandManager;
import sokoban.model.element.*;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;


import java.util.Objects;

public class BoardView4Play extends BorderPane {
    private final BoardViewModel4Play boardViewModel4Play;

    // Labels statiques pour l'affichage des scores et des objectifs.
    private final Label scoreTitleLabel = new Label("Score");
    private static final Label movesLabel = new Label("Number of moves played: 0");
    private static final Label goalsLabel = new Label("Number of goals reached: 0 of X"); // X will be dynamically set

    private static final Label youWinLabel = new Label("You win! x moves, congratulations!");

    private final Label validationLabel = new Label();
    private final VBox toolBar = new VBox();

    private final Label headerLabel = new Label("");

    private final VBox topContainer = new VBox();

    private final HBox playFinishContainer = new HBox();
    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;

    /**
     * Constructeur qui initialise la vue avec les propriétés de la partie en cours, les objectifs, et prépare l'interface utilisateur.
     * param primaryStage Le stage principal sur lequel la scène sera placée.
     * param boardViewModel4Play Le modèle de données de jeu.
     */

    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel4Play) {


        this.boardViewModel4Play = boardViewModel4Play;
        initializeTotalGoals(boardViewModel4Play.getTargetCount());
        updateGoalsReached(boardViewModel4Play.getGoalsReached());
        start(primaryStage);
        createFinishButton();
        //createReplay();
        youWinLabel.setVisible(false);

        createHeader();
        configMainComponents(primaryStage);
        createGrid();


    }

    /**
     * Affiche le label "You Win" avec le nombre de mouvements effectués.
     * param moveCount Nombre de mouvements effectués pour gagner.
     */
    public static void displayYouWinLabel(int moveCount) {
        moveCount++;
        youWinLabel.setText("You win! " + moveCount + " moves, congratulations!");

        youWinLabel.setVisible(true);
    }

    /**
     * Initialise l'affichage du nombre total d'objectifs à atteindre.
     * param targetCount Nombre total d'objectifs.
     */
    private void initializeTotalGoals(long targetCount) {
        goalsLabel.setText("Number of goals reached: 0 of " + targetCount);

    }

    /**
     * Prépare et affiche la scène, configurant les feuilles de style et les propriétés initiales de la fenêtre.
     * param stage Le stage sur lequel la scène sera placée.
     */
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
        this.requestFocus();

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());


    }

    /**
     * Crée la grille de jeu en fonction des dimensions calculées et la place au centre de la vue.
     */
    protected void createGrid() {
        if (getCenter() != null) {
            ((GridPane) getCenter()).getChildren().clear();
        }

        Platform.runLater(() -> {

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

    /**
     * Met à jour le label affichant le nombre de mouvements joués.
     * param moveCount Nombre de mouvements joués.
     */
    public static void updateMovesLabel(int moveCount) {

        movesLabel.setText("Number of moves played: " + moveCount);
    }

    /**
     * Met à jour le label affichant le nombre d'objectifs atteints.
     * param goalsReached Nombre d'objectifs actuellement atteints.
     */
    public static void updateGoalsReached(int goalsReached) {
        String currentText = goalsLabel.getText();
        String newText = currentText.replaceFirst("\\d+(?= of)", String.valueOf(goalsReached));
        goalsLabel.setText(newText);
    }

    /**
     * Crée l'en-tête de l'interface utilisateur, configurant les propriétés de liaison pour les étiquettes.
     * return VBox contenant l'en-tête.
     */
    private VBox createHeader() {
        scoreTitleLabel.getStyleClass().add("score-title");
        movesLabel.getStyleClass().add("moves-label");
        goalsLabel.getStyleClass().add("goals-label");
        youWinLabel.getStyleClass().add("you-win-label");


        VBox scoreContainer = new VBox(scoreTitleLabel, movesLabel, goalsLabel, youWinLabel);
        scoreContainer.setAlignment(Pos.CENTER);


        VBox headerContainer = new VBox(headerLabel, validationLabel, scoreContainer);
        headerContainer.setAlignment(Pos.CENTER);

        return headerContainer;
    }

    /**
     * Retourne la largeur actuelle de la barre d'outils.
     * Cette méthode est utilisée pour calculer l'espace disponible pour la grille de jeu en ajustant les dimensions de la grille en fonction de la largeur de la barre d'outils.
     * return La largeur de la barre d'outils.
     */

    protected double getToolbarWidth() {
        return toolBar.getWidth();

    }

    /**
     * Retourne la hauteur actuelle du conteneur supérieur.
     * Cette mesure est utilisée pour ajuster la hauteur de la grille de jeu en prenant en compte la hauteur occupée par le conteneur supérieur.
     * return La hauteur du conteneur supérieur.
     */
    protected double getTopContainerHeight() {
        return topContainer.getHeight();
    }

    /**
     * Retourne la hauteur du conteneur contenant le bouton de fin de partie.
     * Cette mesure est nécessaire pour ajuster correctement la hauteur de la grille de jeu, en assurant que tout l'espace disponible est utilisé efficacement.
     * return La hauteur du conteneur du bouton de fin de partie.
     */
    protected double getPlayButtonContainerHeight() {
        return playFinishContainer.getHeight();
    }

    /**
     * Configure les composants principaux de l'interface utilisateur, tels que la barre d'outils et l'en-tête, et les ajoute à la partie supérieure de la vue.
     * Cette méthode est appelée lors de l'initialisation de la vue pour organiser correctement tous les composants principaux.
     * param stage Le stage principal utilisé pour la configuration initiale.
     */
    private void configMainComponents(Stage stage) {

        initializeToolBar(stage);
        topContainer.getChildren().add(createHeader());
        this.setTop(topContainer);
    }

    /**
     * Initialise la barre d'outils avec les éléments interactifs pour la modification de la grille.
     * param primaryStage Le stage principal pour contexte.
     */
    private void initializeToolBar(Stage primaryStage) {
        // Définit l'alignement des outils à l'intérieur de la VBox
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(0, 0, 110, 50)); // Ajuste le padding
        toolBar.setSpacing(10);


        addToolToBar("/ground.png", new Ground());
        addToolToBar("/wall.png", new Wall());
        addToolToBar("/player.png", new Player());
        addToolToBar("/box.png", new Box());
        addToolToBar("/goal.png", new Goal());
    }

    /**
     * Ajoute un élément à la barre d'outils en tant que bouton cliquable.
     * param imagePath Chemin de l'image représentant l'outil.
     * param toolType Type de l'élément à ajouter.
     */
    private void addToolToBar(String imagePath, Element toolType) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(toolBar.heightProperty().multiply(0.1)); // Ajuste la hauteur de l'image

        StackPane container = new StackPane(imageView);
        container.setPadding(new Insets(5));
        container.setStyle("-fx-border-color: transparent; -fx-border-width: 2; -fx-background-radius: 5;"); // Bordure transparente par défaut


        container.setOnMouseEntered(e -> container.setStyle("-fx-border-color: lightblue; -fx-border-width: 2; -fx-background-radius: 5;"));
        container.setOnMouseExited(e -> container.setStyle("-fx-border-color: transparent; -fx-border-width: 2; -fx-background-radius: 5;"));


        toolBar.getChildren().add(container);
    }



    /**
     * Configure les contrôles clavier pour la scène, permettant le contrôle du jeu via le clavier.
     * param scene La scène pour laquelle les contrôles clavier sont configurés.
     */
    protected void setupKeyControls(Scene scene) {
        System.out.println("Setting up key controls");
        CommandManager commandManager = boardViewModel4Play.getCommandManager();
        scene.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());

            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.Z) {
                    commandManager.undo();
                    event.consume();
                } else if (event.getCode() == KeyCode.Y) {
                    commandManager.redo();
                    event.consume();
                }
                return;
            }

            BoardViewModel4Play.Direction direction = boardViewModel4Play.getDirection(event.getCode());
            if (direction == null) {
                return;
            }


            if (direction != null) {
                boardViewModel4Play.executeMove(direction);
                System.out.println("Moving player in direction: " + direction);
                event.consume();

            }
        });
    }

    /**
     * Crée le bouton de fin de partie et configure son action pour fermer la fenêtre et réinitialiser le compteur de mouvements.
     */
    private void createFinishButton() {
        Button finishButton = new Button("Finish");

        finishButton.setOnAction(event -> {
            boardViewModel4Play.getBoard4Play().setMoveCount(0);
            BoardView4Play.updateMovesLabel(0);


            Stage currentStage = (Stage) this.getScene().getWindow();
            currentStage.close();

            // Rouvrir BoardView4Design avec l'état sauvegardé
            Stage stage = (Stage) this.getScene().getWindow();
            new BoardView4Design(stage,new BoardViewModel4Design(boardViewModel4Play.getBoard()));


        });



        playFinishContainer.getChildren().add(finishButton);
        playFinishContainer.setAlignment(Pos.CENTER);
        playFinishContainer.setPadding(new Insets(0, 0, 10, 0));


        setBottom(playFinishContainer);
    }

    /*private void createReplay(){
        Button ReplayButton = new Button("Replay");
        ReplayButton.setOnAction(event -> {

            Stage gameStage = (Stage) this.getScene().getWindow();
            BoardView4Play boardView4Play = new BoardView4Play(gameStage, new BoardViewModel4Play(boardViewModel4Play.getBoard()));
            Scene scene = new Scene(boardView4Play);
            gameStage.setScene(scene);
            gameStage.show();

        });

        playFinishContainer.getChildren().add(ReplayButton);
        playFinishContainer.setAlignment(Pos.CENTER);
        playFinishContainer.setPadding(new Insets(0, 0, 10, 0));


        setBottom(playFinishContainer);
        }

     */



}
