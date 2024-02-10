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
        if (grid.getValue(line, col) == CellValue.EMPTY && !isFull()) {
            grid.play(line, col, toolValue);
        } else if (grid.getValue(line, col) != CellValue.EMPTY) {
            grid.play(line, col, CellValue.EMPTY); // Clear the cell if it's not empty
        }
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
