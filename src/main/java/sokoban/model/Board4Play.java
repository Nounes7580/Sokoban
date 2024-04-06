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
    private int moveCount = 0;


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
        if (playerPosition == null) {
            System.out.println("Player not found.");
            return;
        }

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();

        if (!isMoveValid(newRow, newCol, direction)) {
            System.out.println("Move to " + newRow + ", " + newCol + " is invalid.");
            return;
        }

        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        if (targetCell.hasElementOfType(Box.class)) {
            int boxNewRow = newRow + direction.getDeltaRow();
            int boxNewCol = newCol + direction.getDeltaCol();

            // Check if the new position for the box is valid
            if (!isPositionValid(boxNewRow, boxNewCol)) {
                System.out.println("Move is invalid: Box cannot be moved to " + boxNewRow + ", " + boxNewCol);
                return;
            }

            // Move the box
            System.out.println("Moving box to: (" + boxNewRow + ", " + boxNewCol + ")");
            grid4Play.play(boxNewRow, boxNewCol, createElementFromCellValue(CellValue.BOX));
            System.out.println("Clearing old box position at: (" + newRow + ", " + newCol + ")");
            grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.EMPTY));
        }

        // Move the player
        grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.PLAYER));
        grid4Play.play(playerPosition[0], playerPosition[1], createElementFromCellValue(CellValue.EMPTY));
//update the view
        moveCount++;
    }
    public int getMoveCount() {
        return moveCount;
    }
    private boolean isMoveValid(int newRow, int newCol, Direction direction) {
        System.out.println("Checking move validity for: " + newRow + ", " + newCol);

        // Validate player movement bounds
        if (newRow < 0 || newRow >= grid4Play.getGridWidth() || newCol < 0 || newCol >= grid4Play.getGridHeight()) {
            System.out.println("Move is invalid: Player out of bounds.");
            return false;
        }

        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        System.out.println("Target cell value: " + targetCell.getValue());

        // Check if the target cell is empty or a goal
        if (targetCell.isEmpty() || targetCell.hasElementOfType(Goal.class)) {
            return true;
        }

        // Handling box movement
        if (targetCell.hasElementOfType(Box.class)) {
            int boxNewRow = newRow + direction.getDeltaRow();
            int boxNewCol = newCol + direction.getDeltaCol();
            System.out.println("Checking next cell for box at: " + boxNewRow + ", " + boxNewCol);

            // Validate box movement bounds
            if (boxNewRow < 0 || boxNewRow >= grid4Play.getGridWidth() || boxNewCol < 0 || boxNewCol >= grid4Play.getGridHeight()) {
                System.out.println("Move is invalid: Box out of bounds.");
                return false;
            }

            Cell boxNextCell = grid4Play.getMatrix()[boxNewRow][boxNewCol];
            System.out.println("Next cell value for box: " + boxNextCell.getValue());

            // Check if the next cell is suitable for the box
            boolean canMoveBox = (boxNextCell.isEmpty() || boxNextCell.hasElementOfType(Goal.class)) && !boxNextCell.hasElementOfType(Box.class);
            System.out.println("Can move box: " + canMoveBox);
            return canMoveBox;
        }

        return false;
    }


    private boolean isPositionValid(int boxNewRow, int boxNewCol) {
        if (boxNewRow >= 0 && boxNewRow < grid4Play.getGridHeight() && boxNewCol >= 0 && boxNewCol < grid4Play.getGridWidth()) {
            Cell boxNewCell = grid4Play.getMatrix()[boxNewRow][boxNewCol];
            return !(boxNewCell.getValue().contains(CellValue.WALL) || boxNewCell.getValue().contains(CellValue.BOX));
        }
        return false;
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

    public enum Direction {
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP(0, -1),
        DOWN(0, 1);
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
