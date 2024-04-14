package sokoban.model;



import sokoban.model.element.*;

import java.util.ArrayList;
import java.util.List;


public class Move implements Command {
    private Board4Play board;
    private Board4Play.Direction direction;
    private int[] previousPosition;
    private int previousMoveCount;
    private List<BoxState> previousBoxStates = new ArrayList<>();




    public Move(Board4Play board, Board4Play.Direction direction) {
        this.board = board;
        this.direction = direction;
        this.previousPosition = board.getGrid4Play().findPlayerPosition(); // Capture the old player position
        this.previousMoveCount = board.getMoveCount();
        captureInitialState(); // Capture the state of all affected boxes
        if (this.previousPosition == null) {
            throw new IllegalStateException("Player position could not be found, possibly not initialized.");
        }
    }

    private void captureInitialState() {
        int[] playerPos = board.getGrid4Play().findPlayerPosition();
        if (playerPos != null) {
            int newRow = playerPos[0] + direction.getDeltaRow();
            int newCol = playerPos[1] + direction.getDeltaCol();
            if (board.getGrid4Play().getCell(newRow, newCol).hasElementOfType(Box.class)) {
                Box box = (Box) board.getGrid4Play().getCell(newRow, newCol).getElementOfType(Box.class);
                boolean wasOnGoal = board.getGrid4Play().getCell(newRow, newCol).hasElementOfType(Goal.class); // Correction ici
                previousBoxStates.add(new BoxState(box, new int[]{newRow, newCol}, wasOnGoal));
            }
        }
    }


    @Override
    public void execute() {
            Board4Play.movePlayer(direction);
        }

    @Override
    public void redo() {
        Board4Play.movePlayer(direction);
    }

    public void undo() {
        // Restaure le joueur à sa position précédente
        board.undoMovePlayer(previousPosition, previousMoveCount);

        // Restaure les états des boîtes
        for (BoxState state : previousBoxStates) {
            // Position actuelle de la boîte (après le mouvement)
            int boxCurrentRow = state.position[0] + direction.getDeltaRow();
            int boxCurrentCol = state.position[1] + direction.getDeltaCol();
            Cell currentCell = board.getGrid4Play().getCell(boxCurrentRow, boxCurrentCol);

            // Remet la boîte à sa position originale
            Cell originalCell = board.getGrid4Play().getCell(state.position[0], state.position[1]);
            originalCell.setValue(state.box);

            // Restaure l'état de la cellule actuelle en fonction de si elle avait un goal
            if (state.wasOnGoal) {
                // La boîte était sur un goal, donc la cellule originale doit être un goal aussi
                originalCell.setValue(new Goal());
                board.incrementGoalsFilled(); // Il faut incrémenter parce qu'on remet la boîte sur le goal
            }

            // La cellule actuelle devrait être soit un ground soit un goal, jamais rien d'autre
            if (currentCell.hasElementOfType(Goal.class)) {
                // Si le goal était déjà là, il reste
                currentCell.setValue(new Goal());
            } else {
                // Sinon, on met un ground
                currentCell.setValue(new Ground());
            }

            // Décrémente le compteur de boîtes sur les goals
            if (!state.wasOnGoal && currentCell.hasElementOfType(Goal.class)) {
                board.decrementGoalsFilled();
            }
        }
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


