package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Element;

import java.util.Arrays;

public class Grid4Play extends Grid{

    private Cell4play[][] cell4Play;




    public Grid4Play(Grid4Design grid4Design){
        this.gridWidth.set(grid4Design.getGridWidth());
        this.gridHeight.set(grid4Design.getGridHeight());
        this.cell4Play= new Cell4play[gridWidth.get()][gridHeight.get()];
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                cell4Play[i][j] = new Cell4play(grid4Design.getCell4Design(i,j));
            }
        }

        /*filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !(cell.isEmpty()))
                .count(), gridChanged);

         */
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

   /* public Cell4Design[][] getMatrix() {
        return cell4Design;
    }

    */

    @Override
    public void setCellValue(int line, int col, Element newValue) {

    }
}
