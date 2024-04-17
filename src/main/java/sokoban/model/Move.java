package sokoban.model;



import sokoban.model.element.*;
import sokoban.view.BoardView4Play;
import sokoban.viewmodel.BoardViewModel4Play;

import java.util.ArrayList;
import java.util.List;


public class Move implements Command {
    private Board4Play board;
    private Board4Play.Direction direction;
    private int[] previousPosition;
    private int previousMoveCount;
    private List<BoxState> previousBoxStates = new ArrayList<>();
    BoardView4Play boardView4Play;




/** Initialise le déplacement avec une référence au plateau de jeu (board) et la direction dans laquelle le déplacement doit être effectué (direction).
 Enregistre la position initiale du joueur et le nombre de mouvements actuel du jeu pour permettre une annulation (undo) efficace.
 Appelle captureInitialState pour enregistrer l'état initial des boîtes qui pourraient être déplacées par ce mouvement.
 Lève une exception si la position du joueur n'est pas trouvée, indiquant une erreur d'initialisation possible.**/
    public Move(Board4Play board, Board4Play.Direction direction) {
        this.board = board;
        this.direction = direction;
        this.previousPosition = board.getGrid4Play().findPlayerPosition();
        this.previousMoveCount = board.getMoveCount();
        captureInitialState();
        if (this.previousPosition == null) {
            throw new IllegalStateException("Player position could not be found, possibly not initialized.");
        }
    }
  /**  Capture l'état initial des boîtes qui seront affectées par le déplacement. Pour chaque boîte affectée par le déplacement, l'état est enregistré, y compris sa position et si elle était sur un objectif.**/
  private void captureInitialState() {
      int[] playerPos = board.getGrid4Play().findPlayerPosition();
      if (playerPos != null) {
          int newRow = playerPos[0] + direction.getDeltaRow();
          int newCol = playerPos[1] + direction.getDeltaCol();
          Cell cell = board.getGrid4Play().getCell(newRow, newCol);

          if (cell.hasElementOfType(Box.class)|| cell.hasElementOfType(Goal.class)) {
              Box box = (Box) cell.getElementOfType(Box.class);
              boolean wasOnGoal = cell.hasElementOfType(Goal.class);
              previousBoxStates.add(new BoxState(box, new int[]{newRow, newCol}, wasOnGoal));
          }
      }
  }

/** Exécutent le déplacement en appelant movePlayer sur Board4Play avec la direction donnée. redo fait la même chose car dans ce contexte, refaire un mouvement est identique à l'exécuter initialement.**/
    @Override
    public void execute() {
        if (board.canMove(direction)) {
            Board4Play.movePlayer(direction);
        }
        }
    @Override
    public void redo() {
        Board4Play.movePlayer(direction);
    }
/** Annule le mouvement. Cette méthode replace le joueur à sa position initiale et restaure l'état initial des boîtes déplacées.
 Réinitialise le compteur de mouvements et ajuste le nombre de boîtes sur les objectifs si nécessaire, en fonction de l'état initial enregistré des boîtes.**/
    public void undo() {

        board.undoMovePlayer(previousPosition, previousMoveCount);


        for (BoxState state : previousBoxStates) {

            int boxCurrentRow = state.position[0] + direction.getDeltaRow();
            int boxCurrentCol = state.position[1] + direction.getDeltaCol();
            Cell currentCell = board.getGrid4Play().getCell(boxCurrentRow, boxCurrentCol);

            Cell originalCell = board.getGrid4Play().getCell(state.position[0], state.position[1]);
            originalCell.setValue(state.box);



            if (state.wasOnGoal) {

                originalCell.setValue(new Goal());

            }
            if (state.box != null) {
                originalCell.addValue(state.box);
            }
            if (currentCell.hasElementOfType(Goal.class)) {

                currentCell.setValue(new Goal());
            } else {

                currentCell.setValue(new Ground());
            }

            if (!state.wasOnGoal && currentCell.hasElementOfType(Goal.class)) {
                board.decrementGoalsFilled();
                BoardViewModel4Play.getBoardView4Play().updateGoalsReached(board.getGoalsReached());

            }
            if (state.wasOnGoal && !currentCell.hasElementOfType(Goal.class)) {
                board.incrementGoalsFilled();
                BoardViewModel4Play.getBoardView4Play().updateGoalsReached(board.getGoalsReached());
            }

        }
    }
   /** Une classe utilitaire pour stocker l'état d'une boîte, y compris l'élément de la boîte, sa position,
    et si elle était sur un objectif au début du mouvement. Ceci est crucial pour pouvoir annuler les mouvements de boîtes correctement.**/
    static class BoxState {
        Element box;
        int[] position;
        boolean wasOnGoal;

        public BoxState(Element box, int[] position, boolean wasOnGoal) {
            this.box = box;
            this.position = position;
            this.wasOnGoal = wasOnGoal;
        }
    }
}


