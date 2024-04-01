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



public class BoardView extends BorderPane {

    // ViewModel
    private final BoardViewModel boardViewModel;
    private GridView gridView; // Declare gridView as a member variable


    // Constantes de mise en page

    private static final int SCENE_MIN_WIDTH = 700;
    private static final int SCENE_MIN_HEIGHT = 600;

    // Composants principaux
    private final Label headerLabel = new Label("");
    private final HBox headerBox = new HBox();
    private final VBox toolBar = new VBox(); // La VBox pour la barre d'outils avec un espacement de 10
    // Label pour les messages de validation
    private final Label validationLabel = new Label();

    // Ajout pour la barre de menu
    private final MenuBar menuBar = new MenuBar();

    private final VBox topContainer = new VBox();

    private final HBox playButtonContainer = new HBox();


    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;

        setLeft(toolBar);
        createHeader(); // Ajoutez le label de validation dans cette méthode
        createGrid();
        createPlayButton();
        start(primaryStage);
        boardViewModel.gridResetProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                createGrid();
                boardViewModel.gridResetProperty().set(false); // Reset the property to false
            }
        });

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
         topContainer.getChildren().addAll(menuBar, createHeader());

        // Ajout du container combiné à l'interface utilisateur
        this.setTop(topContainer);

    }
    private void createPlayButton() {
        Button playButton = new Button("Play");
        playButton.setOnAction(event -> {
            // Si la grille a été modifiée, demandez si l'utilisateur souhaite sauvegarder les changements
            if (boardViewModel.isGridChanged()) {
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation Dialog");
                confirmationDialog.setHeaderText("Your board has been modified.");
                confirmationDialog.setContentText("Do you want to save your changes?");

                ButtonType buttonTypeYes = new ButtonType("Oui");
                ButtonType buttonTypeNo = new ButtonType("Non");
                ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

                Optional<ButtonType> result = confirmationDialog.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeYes) {
                    // Méthode pour sauvegarder
                    handleSaveAs(new Stage());
                    // TODO : Afficher la nouvelle vue après la sauvegarde
                } else if (result.isPresent() && result.get() == buttonTypeNo) {
                    // TODO : Afficher la nouvelle vue sans sauvegarder
                }
                // Si "Annuler" est choisi, fermez simplement la boîte de dialogue sans rien faire d'autre
            } else {
                // Si la grille n'a pas été modifiée, affichez directement la nouvelle vue
                // TODO: Afficher la nouvelle vue
            }
        });

        // Désactive le bouton "Play" basé sur le message de validation
        playButton.disableProperty().bind(
                boardViewModel.validationMessageProperty().isNotEmpty()
        );

        // Centre le bouton dans le conteneur
        playButtonContainer.getChildren().add(playButton);
        playButtonContainer.setAlignment(Pos.CENTER);
        playButtonContainer.setPadding(new Insets(0, 0, 10, 0));

        // Positionne le conteneur du bouton "Play" en bas du BorderPane
        setBottom(playButtonContainer);
    }



    private VBox createHeader() {
        headerLabel.textProperty().bind(Bindings.createStringBinding(() ->
                        "Number of filled cells: " + boardViewModel.filledCellsCountProperty().get() + " of " + boardViewModel.maxFilledCells(),
                boardViewModel.filledCellsCountProperty(),
                boardViewModel.maxFilledCellsProperty() // Assuming there's a property accessor for maxFilledCells in your ViewModel
        ));
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

    private ImageView createImageView(String resourcePath, CellValue toolType) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(resourcePath)));
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(toolBar.prefWidthProperty()); // Lie la largeur de l'ImageView à celle de la VBox

        return imageView;
    }
    private void initializeToolBar(Stage primaryStage) {
        // Définit l'alignement des outils à l'intérieur de la VBox
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(0, 0, 110, 50)); // Ajuste le padding
        toolBar.setSpacing(10);

        // Création et ajout des outils à la VBox en utilisant une méthode modifiée
        addToolToBar("/ground.png", CellValue.GROUND);
        addToolToBar("/wall.png", CellValue.WALL);
        addToolToBar("/player.png", CellValue.PLAYER);
        addToolToBar("/box.png", CellValue.BOX);
        addToolToBar("/goal.png", CellValue.GOAL);
    }

    private void addToolToBar(String imagePath, CellValue toolType) {
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
        container.setOnMouseClicked(e -> selectTool(toolType));

        // Ajoute le conteneur à la barre d'outils
        toolBar.getChildren().add(container);
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
        if (boardViewModel.isGridChanged()) { // Supposons que isGridChanged() vérifie si des changements ont été faits
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Changes");
            alert.setHeaderText("Do you want to save changes to the current grid?");
            alert.setContentText("Choose your option.");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                 // Méthode pour sauvegarder
                handleSaveAs(new Stage());
                requestNewGridDimensions(); // Créez une nouvelle grille après la sauvegarde
            } else if (result.get() == buttonTypeNo) {
                requestNewGridDimensions(); // Créez une nouvelle grille sans sauvegarder
            }
            // Si "Cancel" est choisi, ne rien faire pour revenir à la grille actuelle
        } else {
            requestNewGridDimensions(); // Directement demander les nouvelles dimensions si pas de changements
        }
    }


    private void requestNewGridDimensions() {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Sokoban");
        dialog.setHeaderText("Give new game dimensions");

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

        // Validation listeners
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
            boardViewModel.resetGrid(width, height);
            createGrid();
        });
    }

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





    private void handleOpen(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sokoban files (*.skb, *.xsb)", "*.skb", "*.xsb");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            boardViewModel.loadLevelFromFile(file);

        }

    }

    private void handleSaveAs(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        // Define the extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sokoban files (*.xsb)", "*.xsb");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showSaveDialog(getScene().getWindow());
        boardViewModel.saveLevel(selectedFile);
    }
}
