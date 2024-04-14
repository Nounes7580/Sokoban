package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.*;
import sokoban.view.BoardView4Play;

import java.util.Arrays;
import java.util.Stack;

public class Board4Play {
    private static int boxesOnGoals = 0;


    public Grid4Play getGrid4Play() {
        return grid4Play;
    }
    private static Stack<Move> undoStack = new Stack<>();
    private static Stack<Move> redoStack = new Stack<>();
    public static Grid4Play grid4Play;
    public ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        return grid4Play.valueProperty(line, col);
    }

    public Board4Play(Board4Design board4Design) {
        grid4Play=new Grid4Play(board4Design.grid4Design);
        boxesOnGoals = countInitialBoxesOnGoals();
    }
    private static int moveCount = 0;
    private int countInitialBoxesOnGoals() {
        return (int) Arrays.stream(grid4Play.getMatrix())
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()) && cell.getValue().contains(new Box()))
                .count();
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



    public static void movePlayer(Direction direction) {
        if (boxesOnGoals == grid4Play.getTargetCount()) {
            System.out.println("All boxes are on the goals. No more moves allowed.");
            return;
        }
        int[] playerPosition = grid4Play.findPlayerPosition();
        if (playerPosition == null) {
            System.out.println("Player not found.");
            return;
        }

        Cell oldCell = grid4Play.getMatrix()[playerPosition[0]][playerPosition[1]];
        boolean isPlayerOnGoal = oldCell.hasElementOfType(Goal.class);

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();

        if (!isMoveValid(newRow, newCol, direction)) {
            System.out.println("Move to " + newRow + ", " + newCol + " is invalid.");
            return;
        }
        Integer[] boxStart = null;
        Integer[] boxEnd = null;

        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        if (targetCell.hasElementOfType(Box.class)) {
            int boxNewRow = newRow + direction.getDeltaRow();
            int boxNewCol = newCol + direction.getDeltaCol();

            boolean isBoxOnGoal = targetCell.hasElementOfType(Goal.class);
            boolean willBoxBeOnGoal = grid4Play.getMatrix()[boxNewRow][boxNewCol].hasElementOfType(Goal.class);

            if (isPositionValid(boxNewRow, boxNewCol)) {
                // Retrieve the existing Box object from the targetCell
                Box box = (Box) targetCell.getElementOfType(Box.class);
                // Move the existing Box object to the new cell
                grid4Play.play(boxNewRow, boxNewCol, box);
                if (!isBoxOnGoal && willBoxBeOnGoal) {
                    boxesOnGoals++; // Increment the number of boxes on goals
                }
                if (isBoxOnGoal && !willBoxBeOnGoal) {
                    boxesOnGoals--; // Decrement the number of boxes on goals
                }
                if (!isBoxOnGoal) {
                    grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.EMPTY));
                }
                if (boxesOnGoals == grid4Play.getTargetCount()) {

                    BoardView4Play.displayYouWinLabel(moveCount);
                }
                grid4Play.addPlayerToCell(newRow, newCol);
                BoardView4Play.updateGoalsReached(boxesOnGoals);
            } else {
                System.out.println("Invalid move: Box cannot be moved to (" + boxNewRow + ", " + boxNewCol + ")");
                return;
            }
        } else {
            grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.PLAYER));
        }
        // If the player is moving from a goal, keep the goal.
        if (isPlayerOnGoal) {
            grid4Play.play(playerPosition[0], playerPosition[1], new Goal());
        } else {
            grid4Play.play(playerPosition[0], playerPosition[1], new Ground());
        }

        moveCount++;
        makeMove(playerPosition, new int[]{newRow, newCol}, boxStart, boxEnd);
    }

    public int setMoveCount(int moveCount) {
        return this.moveCount = moveCount;
    }

    public int getMoveCount() {
        return moveCount;
    }
    private static boolean isMoveValid(int newRow, int newCol, Direction direction) {
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


    private static boolean isPositionValid(int boxNewRow, int boxNewCol) {
        // Check if the new position is within the grid boundaries.
        if (boxNewRow < 0 || boxNewRow >= grid4Play.getGridWidth() || boxNewCol < 0 || boxNewCol >= grid4Play.getGridHeight()) {
            System.out.println("Box move out of bounds: (" + boxNewRow + ", " + boxNewCol + ")");
            return false;
        }

        // Retrieve the cell at the box's new position.
        Cell boxNewCell = grid4Play.getMatrix()[boxNewRow][boxNewCol];

        // Check if the new position is free (not containing a box or a wall).
        boolean isPositionFree = !(boxNewCell.hasElementOfType(Box.class) || boxNewCell.hasElementOfType(Wall.class));

        if (!isPositionFree) {
            System.out.println("Box move blocked by another element at: (" + boxNewRow + ", " + boxNewCol + ")");
        }

        return isPositionFree;
    }
    public static Element createElementFromCellValue(CellValue value) {
        switch (value) {
            case PLAYER:
                return new Player();
            case BOX:
                return new Box();
            case GOAL:
                return new Goal();
            // Add cases for other CellValue types
            case EMPTY:
            default:
                return new Ground(); // Assuming Ground represents an empty space
        }
    }

    public int getGoalsReached() {
        return boxesOnGoals;
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

    public void undo() {
        if (!undoStack.isEmpty()) {
            Move move = undoStack.pop();
            // Appliquez la logique pour inverser le mouvement basée sur `move`
            redoStack.push(move); // Ajoutez le mouvement à redoStack pour permettre le rétablissement
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Move move = redoStack.pop();
            // Appliquez la logique pour exécuter à nouveau le mouvement basée sur `move`
            undoStack.push(move); // Remettez le mouvement dans undoStack pour d'autres undos
        }
    }
    private static void makeMove(int[] playerStart, int[] playerEnd, Integer[] boxStart, Integer[] boxEnd) {
        Move move = new Move(playerStart, playerEnd, boxStart, boxEnd);
        undoStack.push(move);
        redoStack.clear(); // Nettoyez la pile redo à chaque nouveau mouvement
    }

}
