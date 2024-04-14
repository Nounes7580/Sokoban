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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sokoban.model.Board4Play;
import sokoban.model.Command;
import sokoban.model.CommandManager;
import sokoban.model.Move;
import sokoban.model.element.*;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;


import java.io.File;
import java.util.Objects;
import java.util.Optional;

import static sokoban.model.CommandManager.*;

public class BoardView4Play extends BorderPane {
    private final BoardViewModel4Play boardViewModel4Play;


    private final Label scoreTitleLabel = new Label("Score");
    private static final Label movesLabel = new Label("Number of moves played: 0");
    private static final Label goalsLabel = new Label("Number of goals reached: 0 of X"); // X will be dynamically set

    private static final Label youWinLabel = new Label("You win! x moves, congratulations!");

    private final Label validationLabel = new Label();
    private final VBox toolBar = new VBox();
    //private final Label goalsLabel = new Label("Number of boxes : x");

    private final Label headerLabel = new Label("");

    private final VBox topContainer = new VBox();

    private final HBox playFinishContainer = new HBox();
    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;
    private Board4Play board4Play;
    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel4Play) {


        this.boardViewModel4Play = boardViewModel4Play;
        initializeTotalGoals(boardViewModel4Play.getTargetCount());
        updateGoalsReached(boardViewModel4Play.getGoalsReached());
        start(primaryStage);
        createFinishButton();
        youWinLabel.setVisible(false);

        createHeader();
        configMainComponents(primaryStage);
        createGrid();


    }


    public static void displayYouWinLabel(int moveCount) {
        moveCount++;
        youWinLabel.setText("You win! " + moveCount + " moves, congratulations!");

        youWinLabel.setVisible(true);
    }


    private void initializeTotalGoals(long targetCount) {
        goalsLabel.setText("Number of goals reached: 0 of " + targetCount);

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
        CommandManager commandManager = new CommandManager();
        setupKeyControls(scene, commandManager);


        stage.show();
        this.requestFocus();

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());


    }

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
    public static void updateMovesLabel(int moveCount) {

        movesLabel.setText("Number of moves played: " + moveCount);
    }

    public static void updateGoalsReached(int goalsReached) {
        String currentText = goalsLabel.getText();
        String newText = currentText.replaceFirst("\\d+(?= of)", String.valueOf(goalsReached));
        goalsLabel.setText(newText);
    }


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


    protected double getToolbarWidth() {
        return toolBar.getWidth();

    }


    protected double getTopContainerHeight() {
        return topContainer.getHeight();
    }


    protected double getPlayButtonContainerHeight() {
        return playFinishContainer.getHeight();
    }


    private void configMainComponents(Stage stage) {

        initializeToolBar(stage);
        topContainer.getChildren().add(createHeader());
        this.setTop(topContainer);
    }


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




    protected void setupKeyControls(Scene scene, CommandManager commandManager) {
        System.out.println("Setting up key controls");
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


            Board4Play.Direction direction = null;
            switch (event.getCode()) {
                case UP:
                case W:
                    direction = Board4Play.Direction.UP;
                    break;
                case DOWN:
                case S:
                    direction = Board4Play.Direction.DOWN;
                    break;
                case LEFT:
                case A:
                    direction = Board4Play.Direction.LEFT;
                    break;
                case RIGHT:
                case D:
                    direction = Board4Play.Direction.RIGHT;
                    break;
                default:

                    return;
            }

            if (direction != null) {

                Command command = new Move(boardViewModel4Play.getBoard4Play(), direction);
                commandManager.executeCommand(command);
                System.out.println("Moving player in direction: " + direction);
                event.consume();
            }
        });
    }

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



}
