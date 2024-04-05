package sokoban.viewmodel;

import sokoban.model.Board4Play;

public class GridViewModel4Play extends GridViewModel{


    private Board4Play board4Play;
    GridViewModel4Play(Board4Play board) {
        this.board4Play=board;
    }

    @Override
    public void setBoardViewModel(BoardViewModel boardViewModel) {

    }

    @Override
    public CellViewModel getCellViewModel(int line, int col) {
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
}
