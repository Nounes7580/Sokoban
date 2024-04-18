package sokoban.viewmodel;

import javafx.beans.Observable;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.Board4Design;
import sokoban.model.Grid;
import sokoban.model.element.Element;

import java.io.File;


/**
 * La classe BoardViewModel4Design fournit une couche d'abstraction entre le modèle de données de la grille de conception et la vue,
 * en facilitant la gestion des interactions de l'utilisateur et la mise à jour de l'état de la grille.
 */
public class BoardViewModel4Design extends BoardViewModel{


    private final GridViewModel4Design gridViewModel;



    private final Board4Design board;
    private final ObjectProperty<Element> selectedTool = new SimpleObjectProperty<>();

    private final StringProperty validationMessage = new SimpleStringProperty();
    private final BooleanProperty gridReset = new SimpleBooleanProperty(false);

    /**
     * Constructeur pour initialiser le modèle de vue avec un plateau spécifique de type Board4Design.
     * param board Plateau de conception qui est utilisé pour créer et manipuler la grille.
     */
    public BoardViewModel4Design(Board4Design board) {
        this.board = board;
        this.gridViewModel = new GridViewModel4Design(board);
        this.gridViewModel.setBoardViewModel(this);
        updateValidationMessage();
        board.filledCellsCountProperty().addListener((observable, oldValue, newValue) -> {
            updateValidationMessage();
        });
        board.getGrid().gridChangedProperty().addListener((observable, oldValue, newValue) -> {
            updateValidationMessage();
        });
        // Écoute pour détecter les mises à jour du plateau et déclencher un rafraîchissement de la grille dans la vue.
        this.board.boardUpdatedProperty().addListener((obs, oldVal, newVal) -> {
            gridReset.set(!gridReset.get()); // Basculez pour garantir que la vue sera mise à jour.
        });

    }

    /**
     * Accesseur pour la propriété indiquant si la grille a été réinitialisée.
     * @return Propriété booléenne indiquant si la grille a été réinitialisée.
     */
    @Override
    public BooleanProperty gridResetProperty() {
        return gridReset;
    }

    /**
     * Réinitialise la grille avec les dimensions spécifiées et met à jour le message de validation.
     * param width Largeur de la nouvelle grille.
     * param height Hauteur de la nouvelle grille.
     */
    public  void resetGrid(int width, int height) {
        this.board.resetGrid(width, height);
        updateValidationMessage();
    }

    /**
     * Renvoie l'élément actuellement sélectionné par l'outil dans la grille de conception.
     * return L'élément sélectionné actuellement.
     */
    public Element getSelectedCellValue() {
        return selectedTool.getValue();
    }

    /**
     * Définit l'outil actuellement sélectionné pour la manipulation de la grille.
     * param cell Élément à définir comme outil sélectionné.
     */
    public void setSelectedTool(Element cell) {
        selectedTool.set(cell);
    }

    /**
     * Accesseur pour la hauteur de la grille.
     * return Hauteur de la grille.
     */
    public  int getGridHeight() {
        return board.getGrid().getGridHeight();
    }

    /**
     * Accesseur pour la largeur de la grille.
     * return Largeur de la grille.
     */
    public  int getGridWidth() {
        return board.getGrid().getGridWidth();
    }

    /**
     * Accesseur pour le plateau de conception sous-jacent.
     * return Plateau de conception actuel.
     */
    public Board4Design getBoard() {
        return board;
    }


    /**
     * Met à jour le message de validation basé sur l'état actuel de la grille.
     */
    public void updateValidationMessage() {
        StringBuilder message = new StringBuilder();
        if (!board.getGrid().hasPlayer()) {
            message.append("\n• A player is required");
        }
        if (!board.getGrid().hasAtLeastOneTarget()) {
            message.append("\n• At least one target is required");
        }
        if (!board.getGrid().hasAtLeastOneBox()) {
            message.append("\n• At least one box is required");
        }

        if (board.getGrid().getTargetCount() != board.getGrid().getBoxCount()) {
            message.append("\n• The number of targets and boxes must be equal");
        }


        validationMessage.set(message.toString());
    }

    /**
     * Accesseur pour le modèle de vue de la grille associé.
     * @return Modèle de vue de la grille.
     */
    public GridViewModel4Design getGridViewModel() {
        return gridViewModel;
    }

    /**
     * Propriété pour compter les cellules remplies dans la grille.
     * @return Propriété de liaison pour le nombre de cellules remplies.
     */
    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    /**
     * Calcule le nombre maximal de cellules qui peuvent être remplies.
     * return Nombre maximal de cellules remplissables.
     */
    public int maxFilledCells() {
        return board.maxFilledCells();
    }

    /**
     * Accesseur pour la propriété du message de validation.
     * @return Propriété de chaîne pour le message de validation.
     */
    public StringProperty validationMessageProperty() {
        return validationMessage;
    }

    /**
     * Vérifie si la grille a été modifiée.
     * return Vrai si la grille a été modifiée, faux sinon.
     */
    public boolean isGridChanged() {
        return board.getGrid().isGridChanged();
    }

    /**
     * Vérifie si la grille contient un joueur.
     * return Vrai si un joueur est présent, faux sinon.
     */
    public boolean hasPlayer(){
        return board.getGrid().hasPlayer();
    }

    /**
     * Charge un niveau de jeu à partir d'un fichier.
     * param file Fichier à partir duquel charger le niveau.
     */
    public void loadLevelFromFile(File file) {
        board.handleOpen(file);
    }

    /**
     * Propriété observable pour le nombre maximal de cellules remplies.
     * @return Propriété observable.
     */
    public Observable maxFilledCellsProperty() {
        return board.maxFilledCellsProperty();
    }

    /**
     * Sauvegarde le niveau de jeu actuel dans un fichier.
     * param selectedFile Fichier dans lequel sauvegarder le niveau.
     */
    public void saveLevel(File selectedFile){
        board.savelevel(selectedFile);
    }


    /**
     * Accesseur pour l'outil sélectionné.
     * return Outil actuellement sélectionné.
     */
    public Element getSelectedTool() {
        return selectedTool.get();
    }


    /**
     * Propriété pour surveiller si la grille a été changée.
     * return Propriété booléenne en lecture seule indiquant si la grille a été modifiée.
     */
    public ReadOnlyBooleanProperty isGridChangedProperty(){
        return board.isGridChanged();
    }

}