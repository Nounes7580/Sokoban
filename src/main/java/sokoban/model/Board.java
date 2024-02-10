package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

public class Board {
    static final int MAX_FILLED_CELLS = 75;

    private final Grid grid = new Grid();
    private final BooleanBinding isFull;

    public Board() {
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
    }

    public CellValue play(int line, int col, CellValue toolValue) {
        // If the selected tool is ground (which represents clearing a cell),
        // and the cell is not already empty, clear the cell.
        if (toolValue == CellValue.GROUND && grid.getValue(line, col) != CellValue.EMPTY) {
            grid.play(line, col, CellValue.EMPTY);
        }
        // If the cell is empty and we are not trying to 'clear' it with ground (i.e., we are placing something),
        // and the board is not full, place the selected tool.
        else if (grid.getValue(line, col) == CellValue.EMPTY && !isFull() && toolValue != CellValue.GROUND) {
            grid.play(line, col, toolValue);
        }
        // Otherwise, do not change the cell.
        return grid.getValue(line, col);
    }

    public static int maxFilledCells() {
        return Board.MAX_FILLED_CELLS;
    }

    public Boolean isFull() {
        return isFull.get();
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
