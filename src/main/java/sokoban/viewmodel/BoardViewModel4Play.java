package sokoban.viewmodel;

import javafx.beans.property.BooleanProperty;
import sokoban.model.Board4Design;
import sokoban.model.Board4Play;
import sokoban.view.BoardView4Play;


public class BoardViewModel4Play extends BoardViewModel {

    private GridViewModel4Play gridViewModel4Play;
    private Board4Play board4Play;
    private Board4Design board4Design;

    public BoardViewModel4Play(Board4Design board4Design){
     this.board4Design=board4Design;
     this.board4Play=new Board4Play(board4Design);
     this.gridViewModel4Play = new GridViewModel4Play(board4Play);
    }
    @Override
    public BooleanProperty gridResetProperty() {
        return null;
    }

    @Override
    public int getGridWidth() {
        return 0;
    }

    @Override
    public int getGridHeight() {
        return 0;
    }

    @Override
    public void updateValidationMessage() {

    }



}
