package sokoban.viewmodel;

import javafx.application.Platform;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import sokoban.model.Board4Design;
import sokoban.model.Board4Play;
import sokoban.view.BoardView4Play;


public class BoardViewModel4Play{



    private GridViewModel4Play gridViewModel4Play;

    public Board4Play getBoard4Play() {
        return board4Play;
    }

    private Board4Play board4Play;



    private Board4Design board4Design;

    public BoardViewModel4Play(Board4Design board4Design){
     this.board4Design=board4Design;
     this.board4Play=new Board4Play(board4Design);
     this.gridViewModel4Play = new GridViewModel4Play(board4Play);
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
    public boolean hasPlayer(){
        return board4Play.getGrid4Play().hasPlayer();
    }




    public GridViewModel4Play getGridViewModel4Play() {
        return gridViewModel4Play;
    }
    public Board4Design getBoard() {
        return board4Design;
    }


    public void movePlayer(Board4Play.Direction direction) {
        System.out.println("ViewModel is attempting to move player: " + direction);
        if (board4Play != null) {
            board4Play.movePlayer(direction);

            Platform.runLater(() -> {
                // Assume there's a method in BoardView4Play to update the moves label
                BoardView4Play.updateMovesLabel(board4Play.getMoveCount());
            });


        } else {
            System.out.println("Error: board4Play is not initialized.");
        }
    }


    public long getTargetCount() {
        return board4Play.getGrid4Play().getTargetCount();
    }

    public int getGoalsReached() {
        return board4Play.getGoalsReached();
    }
}
