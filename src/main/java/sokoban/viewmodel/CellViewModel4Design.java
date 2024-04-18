package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sokoban.model.Board4Design;
import sokoban.model.Cell;
import sokoban.model.CellValue;
import sokoban.model.Grid4Design;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.model.element.Ground;

/**
 * CellViewModel4Design gère les interactions spécifiques à la cellule dans l'environnement de conception du jeu Sokoban.
 */

public class CellViewModel4Design extends CellViewModel{
    private BoardViewModel4Design boardViewModel; // Référence au modèle de vue du plateau pour accéder aux outils sélectionnés et autres fonctions.
    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(DEFAULT_SCALE); // Propriété pour gérer l'échelle de la vue cellule.
    private Element selectedTool = new Ground(); // Outil actuellement sélectionné, initialisé par défaut au sol.


    /**
     * Constructeur pour initialiser la cellule avec ses coordonnées et le plateau associé.
     * param line Ligne de la cellule dans la grille.
     * param col Colonne de la cellule dans la grille.
     * param board Plateau de conception qui contient cette cellule.
     */
    public CellViewModel4Design(int line, int col, Board4Design board) {
        super(line, col, board);

    }


    /**
     * Définit le modèle de vue du plateau associé à cette cellule.
     * param boardViewModel Modèle de vue du plateau pour interactions.
     */
    @Override
    protected void setBoardViewModel(BoardViewModel4Design boardViewModel) {
        this.boardViewModel = (BoardViewModel4Design) boardViewModel;
    }


    /**
     * Réalise une action dans la cellule, typiquement utilisée pour placer un élément sélectionné dans la cellule.
     */

    public void play() {
        if (boardViewModel != null) {
            Element toolValue = boardViewModel.getSelectedCellValue();
            board.play(line, col, toolValue);
        }
    }


    /**
     * Ajoute un objet dans la cellule en fonction de l'outil actuellement sélectionné dans le modèle de vue.
     */
    @Override
    public void addObject() {
        Element selectedTool = boardViewModel.getSelectedTool();

        System.out.println("Attempting to add: " + selectedTool.getType());

        // Check for the current presence of a player and handle the unique rules for a player
        if (selectedTool.getType() == CellValue.PLAYER && boardViewModel.hasPlayer()) {
            System.out.println("A player is already present on the grid. Cannot add another.");
            return;
        }

        // Check if adding a box, replace with a new instance (though this may be unnecessary unless the Box class contains unique states)
        if (selectedTool.getType() == CellValue.BOX) {
            Box newBox = new Box();
            selectedTool = newBox; // Update selectedTool with a new Box instance
        }

        System.out.println("Is cell empty? " + isEmpty());

        // Ensure we update the cell value correctly, allowing goal to combine with box/player
        if (selectedTool.getType() != CellValue.EMPTY) {
            updateCellValue(selectedTool);
        }
    }

    /**
     * Supprime l'objet dans la cellule, si la cellule n'est pas vide.
     */
    @Override
    public void deleteObject() {
        if (!isEmpty()) {
            Cell cell = board.getGrid().getMatrix()[line][col];
            if (cell.getValue().stream().anyMatch(e -> e instanceof Box)) {
                Grid4Design.decrementBoxCount(); // Decrement the box count
            }
            cell.clearValues();
            board.getGrid().triggerGridChange(); // cela a permis la mise a jour lors de la suppression lors du drag
        }
    }

    /**
     * Met à jour la valeur de la cellule avec un nouvel élément.
     * param newValue Nouvel élément à ajouter dans la cellule.
     */
    @Override
    protected void updateCellValue(Element newValue) {
        Cell cell = board.getGrid().getMatrix()[line][col];
        cell.addValue(newValue);  // This directly utilizes the addValue method of the Cell class
        board.getGrid().triggerGridChange(); // Trigger grid change to update view
    }

    /**
     * Retourne la propriété contenant la liste des éléments dans cette cellule.
     * return ReadOnlyListProperty des éléments dans la cellule.
     */
    @Override
    public ReadOnlyListProperty<Element> valueProperty() {
        return board.valueProperty(line, col);
    }


    /**
     * Vérifie si la cellule est vide.
     * return true si la cellule est vide, false sinon.
     */
    @Override
    protected boolean isEmpty() {
        return board.isEmpty(line, col);
    }


    /**
     * Gère l'événement de relâchement de la souris, spécifiquement pour finir une interaction en cours.
     */
    @Override
    public void handleMouseReleased() {

    }

    /**
     * Réinitialise l'échelle d'affichage de la cellule à la valeur par défaut.
     */
    @Override
    public void resetScale() {
        scale.set(DEFAULT_SCALE);
    }



}
