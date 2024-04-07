package sokoban.model;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Ground;
import sokoban.model.element.Player;

import java.util.Arrays;

public class Grid4Play extends Grid{

    private Cell4play[][] cell4Play;




    public Grid4Play(Grid4Design grid4Design) {
        this.gridWidth.set(grid4Design.getGridWidth());
        this.gridHeight.set(grid4Design.getGridHeight());
        this.cell4Play = new Cell4play[gridWidth.get()][gridHeight.get()];
        initializeCells(grid4Design);
        System.out.println("Grid Width: " + this.gridWidth.get());
        System.out.println("Grid Height: " + this.gridHeight.get());
    }

        /*filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !(cell.isEmpty()))
                .count(), gridChanged);

         */

    private void initializeCells(Grid4Design grid4Design) {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                this.cell4Play[i][j] = new Cell4play(grid4Design.getCell4Design(i,j)); // Ou une autre logique pour initialiser chaque cellule
            }
        }
    }
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= gridWidth.get() || col < 0 || col >= gridHeight.get()) {
            throw new IndexOutOfBoundsException("Tentative d'accès hors des limites de la grille.");
        }
        return cell4Play[row][col];
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
        gridChanged.set(!gridChanged.get());
    }

    @Override
    public int[] findPlayerPosition() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                if (cell4Play[i][j].getValue().stream().anyMatch(e -> e instanceof Player)) {
                    System.out.println("Player found at position (" + i + ", " + j + ")");
                    return new int[]{i, j};
                }
            }
        }
        System.out.println("Player not found.");
        return null;
    }

    @Override
    public   int getGridWidth() {
        return gridWidth.get();
    }

    @Override
    public int getGridHeight() {
        return gridHeight.get();
    }

    @Override
    protected ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        if (line < 0 || col < 0 || line >= gridWidth.get() || col >= gridHeight.get()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return cell4Play[line][col].getValue();
    }

    @Override
    protected void play(int line, int col, Element toolValue) {
        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight()) {
            return; // Invalid position.
        }

        if (toolValue.getType() == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null && (playerPos[0] != col || playerPos[1] != line)) {
                cell4Play[playerPos[0]][playerPos[1]].play(new Ground()); // Clear old player position.
            }
        }

        cell4Play[line][col].play(toolValue); // Set new player position or update cell with new toolValue.

        triggerGridChange(); // Notify about grid change.
    }
    @Override
    protected LongBinding filledCellsCountProperty() {
        return null;
    }

    @Override
    protected boolean isEmpty(int line, int col) {
        return cell4Play[line][col].getValue().isEmpty();
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

    public long getTargetCount() {
        return Arrays.stream(cell4Play)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()))
                .count();
    }

    @Override
    public long getBoxCount() {
        return 0;
    }

    @Override
    public boolean isGridChanged() {
        return gridChanged.get();
    }

   /* public Cell4Design[][] getMatrix() {
        return cell4Design;
    }

    */

    @Override
    public void setCellValue(int line, int col, Element newValue) {

    }

    public Cell[][] getMatrix() {
        return cell4Play;
    }

    public void addPlayerToCell(int newRow, int newCol) {
        if (newRow < 0 || newRow >= gridWidth.get() || newCol < 0 || newCol >= gridHeight.get()) {
            System.out.println("Move is invalid: Player out of bounds.");
            return;
        }

        Cell targetCell = cell4Play[newRow][newCol];
        System.out.println("Target cell value: " + targetCell.getValue());

        // Check if the target cell is empty or a goal
        if (targetCell.isEmpty() || targetCell.hasElementOfType(Goal.class)) {
            play(newRow, newCol, new Player());
        }
    }

    public int getGoalsReached() {

        return 0;
    }

    public int getBoxesOnGoals() {
        return 0;
    }
}
