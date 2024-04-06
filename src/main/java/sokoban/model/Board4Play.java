package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.*;

import java.util.Arrays;

public class Board4Play {

    public Grid4Play getGrid4Play() {
        return grid4Play;
    }

    public static Grid4Play grid4Play;
    public ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        return grid4Play.valueProperty(line, col);
    }

    public Board4Play(Board4Design board4Design) {
        grid4Play=new Grid4Play(board4Design.grid4Design);
    }


    public void play(int line, int col, Element toolValue) {


        if (line < 0 || line >= grid4Play.getGridWidth() || col < 0 || col >= grid4Play.getGridHeight()) {
            System.out.println("Indices hors limites : line=" + line + ", col=" + col);
            return; // Aucune valeur à retourner, la méthode peut être de type void.
        }

        // Accéder directement à la cellule pour manipuler ses états.
        grid4Play.play(line,col,toolValue);

        // Si la grille n'est pas pleine ou si la cellule n'est pas vide, procéder à la manipulation.

        // Cette méthode ne retourne plus de CellValue car cela n'a pas de sens avec la structure de données actuelle.
    }


    public void movePlayer(Direction direction) {
        int[] playerPosition = grid4Play.findPlayerPosition();
        System.out.println("Current player position: " + Arrays.toString(playerPosition));

        if (playerPosition == null) {
            System.out.println("Player not found.");
            return;
        }

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();
        System.out.println("Trying to move player to: " + newRow + ", " + newCol);

        if (!isMoveValid(newRow, newCol)) {
            System.out.println("Move to " + newRow + ", " + newCol + " is invalid.");
            return;
        }

        // If the move is valid, update the grid.
        System.out.println("Move is valid. Updating positions...");
        grid4Play.play(playerPosition[0], playerPosition[1], createElementFromCellValue(CellValue.EMPTY));  // Clear old position
        grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.PLAYER));  // Set new position

        System.out.println("Player moved to: " + newRow + ", " + newCol);
    }

    private boolean isPositionValid(int boxNewRow, int boxNewCol) {
        return boxNewRow >= 0 && boxNewRow < grid4Play.getGridHeight() && boxNewCol >= 0 && boxNewCol < grid4Play.getGridWidth();
    }

    public Element createElementFromCellValue(CellValue value) {
        switch (value) {
            case PLAYER:
                return new Player();
            case BOX:
                return new Box();
            // Add cases for other CellValue types
            case EMPTY:
            default:
                return new Ground(); // Assuming Ground represents an empty space
        }
    }
    private boolean isMoveValid(int newRow, int newCol) {
        // Check boundaries
        if (newRow < 0 || newRow >= grid4Play.getGridHeight() || newCol < 0 || newCol >= grid4Play.getGridWidth()) {
            return false;
        }
        // Check if the target cell is empty or contains a goal
        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        return targetCell.isEmpty() || targetCell.getValue().contains(CellValue.GOAL);
    }
    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
        private final int deltaRow;
        private final int deltaCol;
        Direction(int deltaRow, int deltaCol) {
            this.deltaRow = deltaRow;
            this.deltaCol = deltaCol;
        }
        public int getDeltaRow() {
            return deltaRow;
        }
        public int getDeltaCol() {
            return deltaCol;
        }
    }




}
