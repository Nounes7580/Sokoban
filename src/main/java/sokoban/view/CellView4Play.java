package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sokoban.model.CellValue;
import sokoban.model.Grid4Design;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Play;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.CellViewModel4Play;
import javafx.scene.text.Font;

public class CellView4Play extends StackPane {

    // Définition des images statiques utilisées pour représenter différents éléments dans la cellule.
    protected static final Image playerImage = new Image("player.png");
    protected static final Image boxImage = new Image("box.png");
    protected static final Image goalImage = new Image("goal.png");
    protected static final Image groundImage = new Image("ground.png");
    protected static final Image wallImage = new Image("wall.png");

    // Modèle de données pour la cellule et propriétés de liaison pour la dimension de l'affichage.
    protected final CellViewModel4Play cellViewModel4Play;
    protected final DoubleBinding sizeProperty;

    // Image de fond pour chaque cellule et effet visuel pour le survol.
    protected final ImageView backgroundImageView = new ImageView(groundImage);
    protected ColorAdjust darkenEffect = new ColorAdjust();

    protected final ImageView imageView = new ImageView();
    protected final GridPane gridPane;
    protected int line;
    protected int col;


    /**
     * Constructeur qui initialise la cellule avec ses dépendances et configure son apparence et comportement.
     * param cellViewModel4Play Modèle de données de la cellule.
     * param sizeProperty Propriété de liaison pour la taille de la cellule.
     * param gridPane Grille contenant cette cellule.
     * param line Ligne de la grille où la cellule est située.
     * param col Colonne de la grille où la cellule est située.
     */
    CellView4Play(CellViewModel4Play cellViewModel4Play, DoubleBinding sizeProperty, GridPane gridPane, int line, int col) {
        this. cellViewModel4Play = cellViewModel4Play;
        this.sizeProperty = sizeProperty;
        this.line = line;
        this.col = col;
        this.gridPane = gridPane;
        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();


        cellViewModel4Play.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
    }
    /**
     * Configure les éléments visuels de base de la cellule, notamment l'image de fond.
     */
    protected void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        backgroundImageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(backgroundImageView);

        cellViewModel4Play.valueProperty().addListener((obs, oldVal, newVal) -> updateView(newVal));
        updateView(cellViewModel4Play.valueProperty());
    }
    /**
     * Ajoute une ImageView à la cellule.
     * param image Image à ajouter.
     */
    protected void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }

    /**
     * Met à jour la vue de la cellule en fonction des éléments qu'elle contient.
     * param list Liste observable des éléments dans la cellule.
     */
    protected void updateView(ObservableList<Element> list) {
        System.out.println("Updating view for cell at (" + line + ", " + col + ") with elements: " + list);
        getChildren().clear();
        getChildren().add(backgroundImageView);

        for (Element value : list) {
            System.out.println("Processing element type: " + value.getType() + " in cell at (" + line + ", " + col + ")");
            switch (value.getType()) {
                case PLAYER:
                    System.out.println("Adding player image to cell at (" + line + ", " + col + ")");
                    addImageView(playerImage);
                    break;
                case BOX:
                    System.out.println("Adding box image to cell at (" + line + ", " + col + ")");
                    addImageView(boxImage);
                    Box box = (Box) value;
                    if (box.getId() == 0) {
                        int newId = Grid4Design.incrementBoxCount();
                        box.setId(newId);
                    }
                    Label label = new Label(String.valueOf(box.getId()));
                    label.setFont(new Font("Arial", 20));
                    label.setTextFill(Color.BLACK);
                    label.setStyle("-fx-background-color: white; -fx-padding: 5px;");
                    getChildren().add(label);
                    break;
                case GOAL:
                    System.out.println("Adding goal image to cell at (" + line + ", " + col + ")");
                    addImageView(goalImage);
                    /*
                                    Goal goal = (Goal) value;
                if (goal.getId() == 0) {
                    int newId = Grid4Design.incrementGoalCount();
                    goal.setId(newId);
                }
                Label labelGoal = new Label(String.valueOf(goal.getId()));
                labelGoal.setFont(new Font("Arial", 10));
                labelGoal.setTextFill(Color.BLACK);
               // labelGoal.setStyle("-fx-background-color: white; -fx-padding: 5px;");
                getChildren().add(labelGoal);

                     */
                    break;
                    case WALL:

                    System.out.println("Adding wall image to cell at (" + line + ", " + col + ")");
                    addImageView(wallImage);
                    break;
                case GROUND:
                    System.out.println("Adding ground image to cell at (" + line + ", " + col + ")");
                    addImageView(groundImage);
                    break;
            }
        }

    }

    /**
     * Configure les propriétés d'une ImageView, notamment la liaison de sa taille.
     * param imageView L'ImageView à configurer.
     */
    protected void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
        System.out.println("Image view size: " + imageView.getFitWidth() + "x" + imageView.getFitHeight());
    }

    /**
     * Configure les propriétés de liaison et les réactions aux interactions.
     */
    protected void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(sizeProperty);
        backgroundImageView.fitHeightProperty().bind(sizeProperty);
        minWidthProperty().bind(sizeProperty);
        minHeightProperty().bind(sizeProperty);


        cellViewModel4Play.valueProperty().addListener((obs, old, newVal) -> updateView( newVal));




        hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                darkenEffect.setBrightness(-0.1);
                backgroundImageView.setEffect(darkenEffect);
                imageView.setEffect(darkenEffect);
            } else {
                darkenEffect.setBrightness(0);
                backgroundImageView.setEffect(null);
                imageView.setEffect(null);
            }
        });
    }




    /**
     * Réaction aux changements d'état de survol de la souris, ajustant la mise à l'échelle de l'élément visuel.
     * param obs L'observable qui a changé.
     * param oldVal Ancienne valeur de l'état de survol.
     * param newVal Nouvelle valeur de l'état de survol.
     */

}
