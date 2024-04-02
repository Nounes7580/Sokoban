package sokoban.viewmodel;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import sokoban.model.Board;
import sokoban.model.Board4Design;
import sokoban.model.CellValue;

public class GridViewModel {
    private final Board4Design board;
    private  BoardViewModel boardViewModel; // Add a reference to BoardViewModel


    GridViewModel(Board4Design board) {
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

    public int getGridWidth() {
        return board.getGrid().getGridWidth();
    }

    public int getGridHeight() {
        return board.getGrid().getGridHeight();
    }
    // Forward the observable properties

}
