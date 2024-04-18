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

public class BoardView4Design extends BorderPane {


    // Initialisation des composants de l'interface utilisateur
    private final BoardViewModel4Design boardDesignViewModel;

    private final HBox headerBox = new HBox();
    private final Label validationLabel = new Label();
    private final VBox toolBar = new VBox();

    private final Label headerLabel = new Label("");
    private final MenuBar menuBar = new MenuBar();
    private final VBox topContainer = new VBox();

    private final HBox playButtonContainer = new HBox();
    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;
    /**
     * Constructeur qui initialise la vue, configure les propriétés et écouteurs, et lance les composants initiaux.
     * param primaryStage Le stage principal sur lequel la scène sera placée.
     * param boardViewModel Le modèle de données lié à cette vue.
     */
    public BoardView4Design(Stage primaryStage, BoardViewModel4Design boardViewModel) {


        this.boardDesignViewModel = boardViewModel;
        primaryStage.setTitle("Sokoban");
        start(primaryStage);

        createPlayButton();
        setLeft(toolBar);
        createHeader();
        configMainComponents(primaryStage);

        // Écouteur pour réinitialiser la grille quand nécessaire
        boardViewModel.gridResetProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                createGrid();
                this.boardDesignViewModel.gridResetProperty().set(false);
            }
        });

    }

    /**
     * Initialise la scène, y applique une feuille de style, et configure les contrôles clavier et les propriétés de la fenêtre.
     * param stage Le stage sur lequel la scène sera placée.
     */

    public void start(Stage stage) {
        boardDesignViewModel.isGridChangedProperty().addListener((observable,oldValue,newValue)-> {
            if(newValue){
                stage.setTitle("Sokoban(*)");
            }else {
                stage.setTitle("Sokoban");
            }
        });

        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);

        stage.setScene(scene);

        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        Platform.runLater(this::createGrid);
    }

    /**
     * Crée et configure la grille de conception en calculant les dimensions nécessaires et en appliquant les propriétés de liaison.
     */
    protected void createGrid() {
        if (getCenter() != null) {
            ((GridPane) getCenter()).getChildren().clear();
        }



            NumberBinding gridSizeBinding = Bindings.createDoubleBinding(
                    () -> Math.min(
                            widthProperty().subtract(getToolbarWidth()).divide(boardDesignViewModel.getGridWidth()).get(),
                            heightProperty().subtract(getTopContainerHeight()).subtract(getPlayButtonContainerHeight()).divide(boardDesignViewModel.getGridHeight()).get()
                    ),
                    widthProperty(),
                    heightProperty()
            );



            DoubleBinding gridWidthBinding = Bindings.createDoubleBinding(
                    () -> gridSizeBinding.doubleValue() * boardDesignViewModel.getGridWidth(),
                    gridSizeBinding
            );

            DoubleBinding gridHeightBinding = Bindings.createDoubleBinding(
                    () -> gridSizeBinding.doubleValue() * boardDesignViewModel.getGridHeight(),
                    gridSizeBinding
            );

            if (boardDesignViewModel != null) {
                GridView4Design gridView = new GridView4Design((boardDesignViewModel).getGridViewModel(), gridWidthBinding, gridHeightBinding);

                gridView.minHeightProperty().bind(gridHeightBinding);
                gridView.maxHeightProperty().bind(gridHeightBinding);

                gridView.minWidthProperty().bind(gridWidthBinding);
                gridView.maxWidthProperty().bind(gridWidthBinding);
                setCenter(gridView);
            }

    }

    /**
     * Crée l'en-tête de l'interface utilisateur, configurant les propriétés de liaison pour les étiquettes.
     * return Retourne le conteneur VBox contenant les étiquettes de l'en-tête.
     */

    private VBox createHeader() {


        headerLabel.textProperty().bind(Bindings.createStringBinding(() ->
                        "Number of filled cells: " + boardDesignViewModel.filledCellsCountProperty().get() + " of " + boardDesignViewModel.maxFilledCells(),
                boardDesignViewModel.filledCellsCountProperty(),
                boardDesignViewModel.maxFilledCellsProperty()
        ));
        headerLabel.getStyleClass().add("header");


        validationLabel.textProperty().bind(boardDesignViewModel.validationMessageProperty());
        validationLabel.setTextFill(Color.RED);

        validationLabel.visibleProperty().bind(
                Bindings.createBooleanBinding(
                        () -> validationLabel.getText() != null && !validationLabel.getText().isEmpty(),
                        validationLabel.textProperty()
                )
        );

        VBox headerContainer = new VBox(headerLabel, validationLabel);
        headerContainer.setAlignment(Pos.CENTER);
        headerContainer.setPadding(new Insets(10));

        return headerContainer;
    }

    /**
     * Retourne la largeur actuelle de la barre d'outils.
     * Cette méthode est utile pour calculer l'espace disponible pour d'autres composants.
     * @return La largeur de la barre d'outils.
     */

    protected double getToolbarWidth() {
        return toolBar.getWidth();

    }

    /**
     * Retourne la hauteur actuelle du conteneur supérieur.
     * Utilisé pour ajuster la disposition des autres composants dans la vue.
     * @return La hauteur du conteneur supérieur.
     */
    protected double getTopContainerHeight() {
        return topContainer.getHeight();
    }

    /**
     * Retourne la hauteur du conteneur contenant le bouton de jeu.
     * Cette mesure est nécessaire pour ajuster le reste de l'interface utilisateur en conséquence.
     * @return La hauteur du conteneur du bouton de jeu.
     */
    protected double getPlayButtonContainerHeight() {
        return playButtonContainer.getHeight();
    }




    /**
     * Configure les composants principaux de l'interface utilisateur, comme la barre d'outils, le menu et l'en-tête.
     * Cette méthode est appelée lors de l'initialisation de la vue pour organiser les composants.
     * @param stage Le stage principal utilisé pour la configuration initiale.
     */
    private void configMainComponents(Stage stage) {
        initializeToolBar(stage);
        Label validationLabel = new Label();
        validationLabel.textProperty().bind(boardDesignViewModel.validationMessageProperty());
        headerBox.getChildren().add(validationLabel);
        initializeMenu(stage);
        topContainer.getChildren().addAll(menuBar, createHeader());
        this.setTop(topContainer);
    }

    /**
     * Crée le bouton de jeu et configure son action. Affiche une boîte de dialogue pour sauvegarder avant de jouer.
     */
    private void createPlayButton() {
        Button playButton = new Button("Play");
        playButton.setOnAction(event -> {

                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation Dialog");
                confirmationDialog.setHeaderText("Your board has been modified.");
                confirmationDialog.setContentText("Do you want to save your changes?");

                ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
                ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);


                Optional<ButtonType> result = confirmationDialog.showAndWait();


                if (result.isPresent() && result.get() == buttonTypeYes) {

                    handleSaveAs((Stage) this.getScene().getWindow());
                }


                if (!result.isPresent() || result.get() == buttonTypeCancel) {
                    return;
                }
            


            showGameWindowDirectly();
        });


        playButton.disableProperty().bind(
                boardDesignViewModel.validationMessageProperty().isNotEmpty()
        );


        playButtonContainer.getChildren().add(playButton);
        playButtonContainer.setAlignment(Pos.CENTER);
        playButtonContainer.setPadding(new Insets(0, 0, 10, 0));
        setBottom(playButtonContainer);
    }

    /**
     * Affiche directement la fenêtre de jeu. Elle est lié avec la méthode juste au dessus
     */
    private void showGameWindowDirectly() {
        Stage gameStage = (Stage) this.getScene().getWindow();
        BoardView4Play boardView4Play = new BoardView4Play(gameStage, new BoardViewModel4Play(boardDesignViewModel.getBoard()));
        Scene scene = new Scene(boardView4Play);
        gameStage.setScene(scene);
        gameStage.show();
    }

    /**
     * Initialise la barre d'outils avec les éléments interactifs pour la modification de la grille.
     * param primaryStage Le stage principal pour contexte.
     */

    private void initializeToolBar(Stage primaryStage) {

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
        imageView.fitHeightProperty().bind(toolBar.heightProperty().multiply(0.1));

        StackPane container = new StackPane(imageView);
        container.setPadding(new Insets(5));
        container.setStyle("-fx-border-color: transparent; -fx-border-width: 2; -fx-background-radius: 5;");


        container.setOnMouseEntered(e -> container.setStyle("-fx-border-color: lightblue; -fx-border-width: 2; -fx-background-radius: 5;"));
        container.setOnMouseExited(e -> container.setStyle("-fx-border-color: transparent; -fx-border-width: 2; -fx-background-radius: 5;"));

        container.setOnMouseClicked(e -> selectTool(toolType));

        toolBar.getChildren().add(container);
    }


    /**
     * Sélectionne un outil de la barre d'outils et met à jour les surlignages.
     * param tool L'outil à sélectionner.
     */
    private void selectTool(Element tool) {
        boardDesignViewModel.setSelectedTool(tool);
        updateToolHighlights();
    }

    /**
     * Met à jour les surlignages sur les outils de la barre d'outils pour refléter l'outil sélectionné.
     */

    private void updateToolHighlights() {
        for (Node child : toolBar.getChildren()) {
            if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;
                String id = imageView.getId();
                if (id != null && id.equals(boardDesignViewModel.getSelectedTool())) {
                    imageView.setStyle("-fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                } else {
                    imageView.setStyle(null);
                }
            }
        }
    }

    /**
     * Initialise le menu principal du jeu avec les options de fichier.
     * param primaryStage Le stage principal pour contexte.
     */
    private void initializeMenu(Stage primaryStage) {

        Menu fileMenu = new Menu("File");


        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");


        // Ajout des actions aux éléments de menu
        fileMenu.getItems().addAll(newItem, openItem, saveAsItem, exitItem);


        newItem.setOnAction(event -> handleNew());
        openItem.setOnAction(event -> handleOpen(primaryStage));
        saveAsItem.setOnAction(event -> handleSaveAs(primaryStage));
        exitItem.setOnAction(event -> primaryStage.close());


        menuBar.getMenus().add(fileMenu);
    }

    /**
     * Gère l'action de créer un nouveau projet Sokoban, avec confirmation pour sauvegarder les changements si nécessaire.
     */

    private void handleNew() {
        if (boardDesignViewModel.isGridChanged()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Changes");
            alert.setHeaderText("Do you want to save changes to the current grid?");
            alert.setContentText("Choose your option.");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes && boardDesignViewModel.isGridChanged()) {

                handleSaveAs(new Stage());
                requestNewGridDimensions();
            } else if (result.get() == buttonTypeNo) {
                requestNewGridDimensions();
            }

        } else {
            requestNewGridDimensions();
        }
    }

    /**
     * Demande les dimensions pour un nouveau plateau de jeu et les valide.
     */
    private void requestNewGridDimensions() {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Sokoban");
        dialog.setHeaderText("Give new game dimensions");

        // Configuration de l'interface pour saisir les dimensions
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField widthField = createNumericTextField();
        widthField.setPromptText("Width");

        TextField heightField = createNumericTextField();
        heightField.setPromptText("Height");

        Label widthErrorLabel = createErrorLabel("Width must be at least 10.");
        Label heightErrorLabel = createErrorLabel("Height must be at most 50.");

        gridPane.add(new Label("Width:"), 0, 0);
        gridPane.add(widthField, 1, 0);
        gridPane.add(widthErrorLabel, 1, 1);
        gridPane.add(new Label("Height:"), 0, 2);
        gridPane.add(heightField, 1, 2);
        gridPane.add(heightErrorLabel, 1, 3);

        Node okButton = createDialogButtons(dialog);

        // Configuration de l'interface pour saisir les dimensions
        widthField.textProperty().addListener((observable, oldValue, newValue) ->
                validateDimensionInput(newValue, widthErrorLabel, "Width must be at least 10.", okButton, heightField)
        );

        heightField.textProperty().addListener((observable, oldValue, newValue) ->
                validateDimensionInput(newValue, heightErrorLabel, "Height must be at most 50.", okButton, widthField)
        );

        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(widthField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(dimensions -> {
            int width = dimensions.getKey();
            int height = dimensions.getValue();
            if (boardDesignViewModel != null) {
                BoardViewModel4Design designViewModel =  boardDesignViewModel;
                designViewModel.resetGrid(width, height);
            } else {
                System.out.println("boardViewModel is not an instance of BoardViewModel4Design. resetGrid cannot be called.");
            }
            createGrid();
        });
    }

    /**
     * Crée des champs de texte qui n'acceptent que des nombres.
     * return TextField modifié pour accepter uniquement des entrées numériques.
     */
    private TextField createNumericTextField() {
        return new TextField() {
            @Override public void replaceText(int start, int end, String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceText(start, end, text);
                }
            }

            @Override public void replaceSelection(String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceSelection(text);
                }
            }
        };
    }


    /**
     * Crée une étiquette pour les messages d'erreur de validation.
     * param errorMessage Le message d'erreur à afficher.
     * return Label configuré pour l'affichage d'erreurs.
     */
    private Label createErrorLabel(String errorMessage) {
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        return errorLabel;
    }

    /**
     * Crée les boutons pour un dialogue avec validation des dimensions.
     * param dialog Le dialogue auquel les boutons seront ajoutés.
     * return Le bouton OK configuré pour être désactivé initialement.
     */
    private Node createDialogButtons(Dialog<Pair<Integer, Integer>> dialog) {
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK

        );
        okButton.setDisable(true);
        return okButton;
    }

    /**
     * Valide les entrées de dimensions et met à jour les messages d'erreur et l'état du bouton OK.
     * param newValue La nouvelle valeur saisie par l'utilisateur.
     * param errorLabel L'étiquette d'erreur associée au champ de texte.
     * param errorMessage Le message d'erreur à afficher si la validation échoue.
     * param okButton Le bouton OK à activer/désactiver basé sur la validité de l'entrée.
     * param otherField L'autre champ de texte à considérer pour la validation globale.
     */
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



    /**
     * Ouvre un dialogue de fichier pour charger un niveau de jeu existant depuis un fichier.
     * param primaryStage Le stage principal utilisé pour afficher la boîte de dialogue.
     */

    private void handleOpen(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sokoban files (*.skb, *.xsb)", "*.skb", "*.xsb");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            boardDesignViewModel.loadLevelFromFile(file);

        }

    }

    /**
     * Ouvre un dialogue de fichier pour sauvegarder le niveau actuel dans un fichier.
     * @param primaryStage Le stage principal utilisé pour afficher la boîte de dialogue.
     */
    private void handleSaveAs(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sokoban files (*.xsb)", "*.xsb");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showSaveDialog(getScene().getWindow());
        boardDesignViewModel.saveLevel(selectedFile);
    }
}
