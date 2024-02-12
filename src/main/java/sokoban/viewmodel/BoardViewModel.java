package sokoban.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;
import sokoban.model.Grid;
import javafx.beans.binding.LongBinding;


public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private final ObjectProperty<CellValue> selectedTool = new SimpleObjectProperty<>(CellValue.GROUND);
    private final StringProperty validationMessage = new SimpleStringProperty();


    public BoardViewModel(Board board) {
        this.board = board;
        this.gridViewModel = new GridViewModel(board);
        this.gridViewModel.setBoardViewModel(this); // Establish the link after GridViewModel is constructed
        board.filledCellsCountProperty().addListener((observable, oldValue, newValue) -> {
            updateValidationMessage();
        });

    }
    public ObjectProperty<CellValue> selectedToolProperty() {
        return selectedTool;
    }

    public void setSelectedTool(CellValue cell) {
        selectedTool.set(cell);
    }

    public CellValue getSelectedTool() {
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
            case GROUND:
                return CellValue.GROUND;
            case WALL:
                return CellValue.WALL;
            case PLAYER:
                return CellValue.PLAYER;
            case BOX:
                return CellValue.BOX;
            case GOAL:
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
    // Méthode pour mettre à jour le message de validation
    public void updateValidationMessage() {
        StringBuilder message = new StringBuilder();
        if (!board.getGrid().hasPlayer()) {
            message.append("\n• A player is required");
        }
        if (!board.getGrid().hasAtLeastOneTarget()) {
            message.append("\n• At least one target is required");
        }
        if (!board.getGrid().hasAtLeastOneBox()) {
            message.append("\n• At least one box is required");
        }

        validationMessage.set(message.toString());
    }


    public StringProperty validationMessageProperty() {
        return validationMessage;
    }

}
