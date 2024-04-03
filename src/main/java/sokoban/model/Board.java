package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.element.Element;


public abstract class Board {
    protected final BooleanProperty boardUpdated = new SimpleBooleanProperty(false);

    public BooleanProperty boardUpdatedProperty() {
        return boardUpdated;
    }


    protected static Grid grid;
    protected BooleanBinding isFull;
    protected LongProperty filledCellsCount = new SimpleLongProperty();
    protected IntegerProperty maxFilledCells = new SimpleIntegerProperty();

    protected Board(int width, int height) {
        grid = new Grid(width, height);
    }
    public abstract boolean isPositionValid(int line, int col);
    protected abstract long calculateFilledCells();



    public void setCellValue(int line, int col, Element newValue) {
        if (isPositionValid(line, col) && (!isFull.get() || !grid.valueProperty(line, col).getValue().isEmpty())) {
            grid.setCellValue(line, col, newValue);
            filledCellsCount.set(calculateFilledCells());
        }
    }




    /*public Boolean isFull() {
        return isFull.get();
    }*/
    public Grid getGrid() {
        return grid;
    }

    public ReadOnlyListProperty<Element> valueProperty(int line, int col) {
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



