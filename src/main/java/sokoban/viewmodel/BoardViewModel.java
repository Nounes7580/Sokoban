package sokoban.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;
import sokoban.model.Grid;
import javafx.beans.binding.LongBinding;
import sokoban.model.ToolType;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private final ObjectProperty<ToolType> selectedTool = new SimpleObjectProperty<>(ToolType.TERRAIN);

    public BoardViewModel(Board board) {
        this.board = board;
        this.gridViewModel = new GridViewModel(board);
        this.gridViewModel.setBoardViewModel(this); // Establish the link after GridViewModel is constructed
    }
    public ObjectProperty<ToolType> selectedToolProperty() {
        return selectedTool;
    }

    public void setSelectedTool(ToolType tool) {
        selectedTool.set(tool);
    }

    public ToolType getSelectedTool() {
        return selectedTool.get();
    }
    public static int gridWidth() {
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight(); // Cette méthode doit être définie dans votre classe Grid.
    }
    public CellValue getSelectedCellValue() {
        switch (selectedTool.get()) {
            case TERRAIN:
                return CellValue.GROUND;
            case MUR:
                return CellValue.WALL;
            case JOUEUR:
                return CellValue.PLAYER;
            case CAISSE:
                return CellValue.BOX;
            case CIBLE:
                return CellValue.GOAL;
            default:
                return CellValue.EMPTY;
        }
    }



    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public int maxFilledCells() {
        return Board.maxFilledCells();
    }
}
