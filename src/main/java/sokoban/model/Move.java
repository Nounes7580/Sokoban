package sokoban.model;



import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.model.element.Player;

import java.util.ArrayList;
import java.util.List;


public class Move implements Command {
    private Board4Play board;
    private Grid4Play grid4Play;
    private Board4Play.Direction direction;
    private int[] previousPosition;
    private int previousMoveCount;
    private List<BoxState> previousBoxStates = new ArrayList<>();




    public Move(Board4Play board, Board4Play.Direction direction) {

            this.board = board;
            this.direction = direction;
            this.previousPosition = board.getGrid4Play().findPlayerPosition(); // Assurez-vous de sauvegarder l'ancienne position
            this.previousMoveCount = board.getMoveCount();
            if (this.previousPosition == null) {
                throw new IllegalStateException("Player position could not be found, possibly not initialized.");
            }
        }

        @Override
        public void execute() {
            Board4Play.movePlayer(direction);
        }

    public void undo() {
        board.undoMovePlayer(previousPosition, previousMoveCount);
        for (BoxState state : previousBoxStates) {
            board.getGrid4Play().getCell(state.position[0], state.position[1]).setValue(state.box);
        }
    }


        @Override
        public void redo() {
            Board4Play.movePlayer(direction);
        }

    static class BoxState {
        Element box; // L'élément boîte.
        int[] position; // La position [x, y] de la boîte.
        boolean wasOnGoal; // Indique si la boîte était sur un objectif.

        public BoxState(Element box, int[] position, boolean wasOnGoal) {
            this.box = box;
            this.position = position;
            this.wasOnGoal = wasOnGoal;
        }
    }
    }


