package sokoban.viewmodel;

import sokoban.model.Board4Design;

public class GridViewModel4Play extends GridViewModel{
    GridViewModel4Play(Board4Design board) {
        super(board);
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
