package sokoban.viewmodel;

import javafx.application.Platform;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.scene.input.KeyCode;
import sokoban.model.*;
import sokoban.view.BoardView4Play;


public class BoardViewModel4Play{
    private CommandManager commandManager;

    private GridViewModel4Play gridViewModel4Play;

    public void executeMove(Direction direction) {
        if (getBoard4Play().canMove(convertDirection(direction))) {
            Command command = new Move(board4Play, convertDirection(direction));
            commandManager.executeCommand(command);
        }else {
            System.out.println("mouvement impossible");
        }

    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public Board4Play.Direction convertDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Board4Play.Direction.UP;
            case DOWN:
                return Board4Play.Direction.DOWN;
            case LEFT:
                return Board4Play.Direction.LEFT;
            case RIGHT:
                return Board4Play.Direction.RIGHT;
            default:
                return null;
        }
    }

    private Board4Play board4Play;
    private Board4Design board4Design;
    private static BoardView4Play boardView4Play;

    public BoardViewModel4Play(Board4Design board4Design){
     this.board4Design=board4Design;
     this.board4Play=new Board4Play(board4Design);
     this.gridViewModel4Play = new GridViewModel4Play(board4Play);
        this.commandManager = new CommandManager(); // Initialize the CommandManager

    }
    public BooleanProperty gridResetProperty() {
        return null;
    }

    public  int getGridHeight() {
        return board4Play.getGrid4Play().getGridHeight();
    }

    public  int getGridWidth() {
        return board4Play.getGrid4Play().getGridWidth();
    }

    public Direction getDirection(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
            case W:
                return Direction.UP;
            case DOWN:
            case S:
                return Direction.DOWN;
            case LEFT:
            case A:
                return Direction.LEFT;
            case RIGHT:
            case D:
                return Direction.RIGHT;
            default:
                return null;
        }
    }
    public GridViewModel4Play getGridViewModel4Play() {
        return gridViewModel4Play;
    }
    public Board4Design getBoard() {
        return board4Design;
    }

    public Board4Play getBoard4Play() {
        return board4Play;
    }

    public static BoardView4Play getBoardView4Play() {
        return boardView4Play;
    }


    public long getTargetCount() {
        return board4Play.getGrid4Play().getTargetCount();
    }

    public int getGoalsReached() {
        return board4Play.getGoalsReached();
    }
}
