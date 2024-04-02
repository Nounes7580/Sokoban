package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.view.BoardView;
import sokoban.viewmodel.BoardViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


/*public abstract class Board {

    private static Grid grid;
    private BooleanBinding isFull;
    private final LongProperty filledCellsCount = new SimpleLongProperty();
    private final IntegerProperty maxFilledCells = new SimpleIntegerProperty();
    private final BooleanProperty gridReset = new SimpleBooleanProperty(false);
    private final BooleanProperty boardUpdated = new SimpleBooleanProperty(false);

    public BooleanProperty boardUpdatedProperty() {
        return boardUpdated;
    }*/



    public abstract class Board {
       protected final BooleanProperty boardUpdated = new SimpleBooleanProperty(false);

       public BooleanProperty boardUpdatedProperty() {
           return boardUpdated;
       }
       protected void toggleBoardUpdated() {
           boardUpdated.set(!boardUpdated.get());
       }


       protected static Grid grid;
        protected BooleanBinding isFull;
        protected LongProperty filledCellsCount = new SimpleLongProperty();
        protected IntegerProperty maxFilledCells = new SimpleIntegerProperty();

        protected Board(int width, int height) {
            grid = new Grid(width, height);
            filledCellsCount.set(grid.filledCellsCountProperty().get());
            maxFilledCells.set(maxFilledCells());
            isFull = Bindings.createBooleanBinding(() -> filledCellsCount.get() >= maxFilledCells.get(), filledCellsCount, maxFilledCells);
            grid.filledCellsCountProperty().addListener((obs, oldCount, newCount) -> filledCellsCount.set(newCount.longValue()));
            maxFilledCells.addListener((obs, oldVal, newVal) -> isFull = filledCellsCount.greaterThanOrEqualTo(newVal.intValue()));
        }
       public abstract void resetGrid(int newWidth, int newHeight);
       public abstract boolean isPositionValid(int line, int col);
       protected abstract long calculateFilledCells();
       public abstract int maxFilledCells();



    public void setCellValue(int line, int col, CellValue newValue) {
        if (isPositionValid(line, col) && (!isFull.get() || !grid.valueProperty(line, col).getValue().isEmpty())) {
            grid.setCellValue(line, col, newValue);
            filledCellsCount.set(calculateFilledCells());
        }
    }




    public Boolean isFull() {
        return isFull.get();
    }
    public Grid getGrid() {
        return grid;
    }

    public ReadOnlyListProperty<CellValue> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
    }
    public IntegerProperty maxFilledCellsProperty() {
        return this.maxFilledCells;
    }



    }




