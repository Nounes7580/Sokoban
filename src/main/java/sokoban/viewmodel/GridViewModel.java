package sokoban.viewmodel;

import sokoban.model.Board;

public class GridViewModel {
    private final Board board;
    private  BoardViewModel boardViewModel; // Add a reference to BoardViewModel


    GridViewModel(Board board) {
        this.board = board;
    }
    public void setBoardViewModel(BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
    }

    public CellViewModel getCellViewModel(int line, int col) {
        CellViewModel cellViewModel = new CellViewModel(line, col, board);
        cellViewModel.setBoardViewModel(this.boardViewModel); // Set the boardViewModel
        return cellViewModel;
    }
}
