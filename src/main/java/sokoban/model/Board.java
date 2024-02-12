package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleLongProperty;

public class Board {
    static final int MAX_FILLED_CELLS = 75; //TODO: ca doit etre la moitie du nombre de cellules

    private final Grid grid = new Grid();
    private final BooleanBinding isFull;
    private final LongProperty filledCellsCount = new SimpleLongProperty();


    public Board() {
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
    }



    public CellValue play(int line, int col, CellValue toolValue) {
        CellValue currentValue = grid.getValue(line, col);
        // Si l'outil sélectionné est un goal et que la cellule contient déjà un joueur ou une boîte,
        // nous superposons le goal sur l'élément existant.
        if (toolValue == CellValue.GOAL && (currentValue == CellValue.PLAYER || currentValue == CellValue.BOX)) {
            grid.play(line, col, CellValue.GOAL); // Ici, vous devrez gérer la superposition.
        } else {
            // Dans les autres cas, si l'outil sélectionné n'est pas un goal ou si la cellule ne contient pas de joueur/boîte,
            // l'outil sélectionné remplace simplement la valeur existante.
            grid.play(line, col, toolValue);
        }
        // After play actions, call the updateValidationMessage to refresh the validation messages
        filledCellsCount.set(calculateFilledCells()); // You'd implement calculateFilledCells to return the correct count
        return grid.getValue(line, col);


    }

    private long calculateFilledCells() {
        return grid.filledCellsCountProperty().get();

    }

    public static int maxFilledCells() {
        return Board.MAX_FILLED_CELLS;
    }

    public Boolean isFull() {
        return isFull.get();
    }
    public Grid getGrid() {
        return grid;
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
    }
}
