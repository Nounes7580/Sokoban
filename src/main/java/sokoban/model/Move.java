package sokoban.model;


public class Move implements Command {
    private Board4Play board;
    private Grid4Play grid4Play;
    private Board4Play.Direction direction;
    private int[] previousPosition;
    private int previousMoveCount;




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

        @Override
        public void undo() {
            board.undoMovePlayer(previousPosition, previousMoveCount);
        }

        @Override
        public void redo() {
            Board4Play.movePlayer(direction);
        }
    }

