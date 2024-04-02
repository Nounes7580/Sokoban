package sokoban.model;

public class Board4Play extends Board {

    public Board4Play(int width, int height) {
        super(width, height);
    }

    @Override
    public void resetGrid(int newWidth, int newHeight) {
        // Implementation for resetting grid in gameplay context
    }

    @Override
    public boolean isPositionValid(int line, int col) {
        // Validate position for gameplay
        return line >= 0 && line < grid.getGridWidth() && col >= 0 && col < grid.getGridHeight();
    }

    @Override
    protected long calculateFilledCells() {
        // Calculate filled cells specific to gameplay
        return grid.filledCellsCountProperty().get();
    }

    @Override
    public int maxFilledCells() {
        // Define maximum filled cells for gameplay
        return (grid.getGridWidth() * grid.getGridHeight()) / 2;
    }

    public void play(int line, int col, CellValue toolValue) {
        // Gameplay-specific implementation of play
    }

    public void movePlayer(Direction direction) {
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
            grid.play(playerPosition[0], playerPosition[1], CellValue.EMPTY); // Remove the player from the old position
            grid.play(newRow, newCol, CellValue.PLAYER); // Place the player in the new position
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
        return targetCell.isEmpty() || targetCell.getValue().contains(CellValue.GOAL);
    }


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
