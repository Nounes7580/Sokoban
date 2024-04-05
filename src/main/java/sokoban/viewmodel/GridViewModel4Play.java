package sokoban.viewmodel;

import sokoban.model.Board4Play;

public class GridViewModel4Play{


    private Board4Play board4Play;
    GridViewModel4Play(Board4Play board) {
        this.board4Play=board;
    }

    public void setBoardViewModel(BoardViewModel boardViewModel) {

    }

    public CellViewModel4Play getCellViewModel(int line, int col) {
        return new CellViewModel4Play(line,col,board4Play);
    }

    public int getGridWidth() {
        return board4Play.getGrid4Play().getGridWidth();
    }

    public int getGridHeight() {
        return board4Play.getGrid4Play().getGridHeight();
    }
}
