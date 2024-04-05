package sokoban.viewmodel;

import sokoban.model.Board4Design;

public class GridViewModel4Design extends GridViewModel{

    protected final Board4Design board;
    GridViewModel4Design(Board4Design board) {
        this.board = board;
    }

    @Override
    public void setBoardViewModel(BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
    }

    @Override
    public CellViewModel getCellViewModel(int line, int col) {
        CellViewModel cellViewModel = new CellViewModel4Design(line, col, board);
        cellViewModel.setBoardViewModel(this.boardViewModel); // Set the boardViewModel
        return cellViewModel;
    }
    @Override
    public int getGridWidth() {
        return board.getGrid().getGridWidth();
    }

    @Override
    public int getGridHeight() {
        return board.getGrid().getGridHeight();
    }


}
