package sokoban.model;

import sokoban.commands.Command;
import sokoban.model.element.Element;

public class Move implements Command {
    private Grid4Play board;
    private final int startX;
    private int startY;
    private int endX;
    private int endY;
    private Element movedElement;


    public Move(Grid4Play board, int startX, int startY, int endX, int endY, Element movedElement) {
        this.board = board;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.movedElement = movedElement;
    }

    private Board4Play.Direction calculateDirection() {
        // Calculez la direction en fonction des coordonnées de départ et d'arrivée
        if (endX == startX && endY == startY + 1) return Board4Play.Direction.RIGHT;
        if (endX == startX && endY == startY - 1) return Board4Play.Direction.LEFT;
        if (endY == startY && endX == startX + 1) return Board4Play.Direction.DOWN;
        if (endY == startY && endX == startX - 1) return Board4Play.Direction.UP;
        return null;
    }

    @Override
    public boolean execute() {
        Board4Play.movePlayer(calculateDirection());
        return Board4Play.getLastMoveWasSuccessful();
    }

    @Override
    public void undo() {
        Board4Play.Direction inverseDirection = getInverseDirection(calculateDirection());
        Board4Play.movePlayer(inverseDirection);
        // Optionnellement vérifier si l'annulation a réussi
    }

    private Board4Play.Direction getInverseDirection(Board4Play.Direction direction) {
        switch (direction) {
            case UP:
                return Board4Play.Direction.DOWN;
            case DOWN:
                return Board4Play.Direction.UP;
            case LEFT:
                return Board4Play.Direction.RIGHT;
            case RIGHT:
                return Board4Play.Direction.LEFT;
            default:
                return null;
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
