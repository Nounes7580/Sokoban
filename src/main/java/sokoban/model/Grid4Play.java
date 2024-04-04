package sokoban.model;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Element;

public class Grid4Play extends Grid{
    public Grid4Play(int width, int height) {
        super(width, height);
    }

    @Override
    protected void initializeCells() {

    }

    @Override
    protected void resetGrid(int newWidth, int newHeight) {

    }

    @Override
    public BooleanProperty gridChangedProperty() {
        return null;
    }

    @Override
    public void triggerGridChange() {

    }

    @Override
    public int[] findPlayerPosition() {
        return new int[0];
    }

    @Override
    public int getGridWidth() {
        return 0;
    }

    @Override
    public int getGridHeight() {
        return 0;
    }

    @Override
    protected ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        return null;
    }

    @Override
    protected void play(int line, int col, Element toolValue) {

    }

    @Override
    protected LongBinding filledCellsCountProperty() {
        return null;
    }

    @Override
    protected boolean isEmpty(int line, int col) {
        return false;
    }

    @Override
    public boolean hasPlayer() {
        return false;
    }

    @Override
    public boolean hasAtLeastOneTarget() {
        return false;
    }

    @Override
    public boolean hasAtLeastOneBox() {
        return false;
    }

    @Override
    public long getTargetCount() {
        return 0;
    }

    @Override
    public long getBoxCount() {
        return 0;
    }

    @Override
    public boolean isGridChanged() {
        return false;
    }

    @Override
    public Cell[][] getMatrix() {
        return new Cell[0][];
    }

    @Override
    public void setCellValue(int line, int col, Element newValue) {

    }
}
