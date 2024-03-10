package sokoban.viewmodel;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;

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

    public CellValue getCellValue(int line, int col) {
        return getCellViewModel(line, col).valueProperty().get();

    }

    public int getGridWidth() {
        return board.getGrid().getGridWidth();
    }

    public int getGridHeight() {
        return board.getGrid().getGridHeight();
    }
    // Forward the observable properties
    public IntegerProperty gridWidthProperty() {
        return board.getGrid().gridWidthProperty();
    }

    public IntegerProperty gridHeightProperty() {
        return board.getGrid().gridHeightProperty();
    }

}
