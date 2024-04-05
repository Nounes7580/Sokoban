package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Element;
import sokoban.model.element.Player;

import java.util.Arrays;

public class Grid4Play extends Grid{

    private Cell4play[][] cell4Play;




    public Grid4Play(Grid4Design grid4Design) {
        this.gridWidth.set(grid4Design.getGridWidth());
        this.gridHeight.set(grid4Design.getGridHeight());
        this.cell4Play = new Cell4play[gridWidth.get()][gridHeight.get()];
        initializeCells(grid4Design);
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
                    return new int[]{i, j};
                }
            }
        }
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
        // Assurez-vous que les coordonnées sont valides.
        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight()) {
            return; // Position invalide.
        }

         cell4Play[line][col].play(toolValue);

        // Si c'est pour placer un joueur, on d'abord le joueur de sa position actuelle.
        if (toolValue.getType() == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                cell4Play[playerPos[0]][playerPos[1]].play(new Player());
            }
        }

        // Logique simplifiée pour l'ajout d'états dans la cellule.
        if (toolValue.getType() == CellValue.WALL || toolValue.getType() == CellValue.GROUND) {
            // Pour WALL et GROUND, on remplace tout les états existants.
            cell4Play[line][col].play(toolValue);
        }

        // Si on ajoute autre chose qu'un GOAL, et que GOAL est déjà présent, on ne le retire pas.
        // Cela permet de garder le GOAL même quand on ajoute PLAYER ou BOX sur celui-ci.


        triggerGridChange();
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
        return gridChanged.get();
    }

   /* public Cell4Design[][] getMatrix() {
        return cell4Design;
    }

    */

    @Override
    public void setCellValue(int line, int col, Element newValue) {

    }
}
