package sokoban.model;

import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Ground;
import sokoban.model.element.Player;

public class Board4Play  {

    public Grid4Play grid4Play;

    public Board4Play(Board4Design board4Design) {
        grid4Play=new Grid4Play(board4Design.grid4Design);
    }




    /*public boolean isPositionValid(int line, int col) {
        // Validate position for gameplay
        return line >= 0 && line < grid4Play.getGridWidth() && col >= 0 && col < grid.getGridHeight();
    }

     */







    public void play(int line, int col, Element toolValue) {
        // Gameplay-specific implementation of play
    }

  /*  public void movePlayer(Direction direction) {
        int[] playerPosition = grid.findPlayerPosition();
        if (playerPosition == null) {
            // Handle the case where the player is not found
            return;
        }

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();

        // First, check if the move is valid
        if (isMoveValid(newRow, newCol)) {
            // Move the player
            grid.play(playerPosition[0], playerPosition[1], new Ground()); // Remove the player from the old position
            grid.play(newRow, newCol, new Player()); // Place the player in the new position
        }

        // After the move, the filled cells count might have changed
        filledCellsCount.set(calculateFilledCells());

        // Notify any observers that the grid has changed
        grid.triggerGridChange();
    }

    private boolean isMoveValid(int newRow, int newCol) {
        // Check boundaries
        if (newRow < 0 || newRow >= grid.getGridHeight() || newCol < 0 || newCol >= grid.getGridWidth()) {
            return false;
        }

        // Check if the target cell is empty or contains a goal
        Cell targetCell = grid.getMatrix()[newRow][newCol];
        return targetCell.isEmpty() || targetCell.getValue().contains(new Goal());
    }


   */

    public enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

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

    // Additional gameplay methods like movePlayer()
}
