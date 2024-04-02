package sokoban.viewmodel;

import javafx.beans.Observable;
import javafx.beans.property.*;
import sokoban.model.*;
import javafx.beans.binding.LongBinding;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board4Design board;
    private final ObjectProperty<CellValue> selectedTool = new SimpleObjectProperty<>(CellValue.GROUND);
    private final StringProperty validationMessage = new SimpleStringProperty();


    public BoardViewModel(Board4Design board) {
        this.board = board;
        this.gridViewModel = new GridViewModel(board);
        this.gridViewModel.setBoardViewModel(this);
        updateValidationMessage();
        board.filledCellsCountProperty().addListener((observable, oldValue, newValue) -> {
            updateValidationMessage();
        });
        board.getGrid().gridChangedProperty().addListener((observable, oldValue, newValue) -> {
            updateValidationMessage();
        });
        // Écoutez les changements signalant que le board a été mis à jour.
        this.board.boardUpdatedProperty().addListener((obs, oldVal, newVal) -> {
            gridReset.set(!gridReset.get()); // Basculez pour garantir que la vue sera mise à jour.
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
    public int getGridWidth() {
        return board.getGrid().getGridWidth();
    }

    public int getGridHeight() {
        return board.getGrid().getGridHeight();
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
        return board.maxFilledCells();
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

    if (board.getGrid().getTargetCount() != board.getGrid().getBoxCount()) {
        message.append("\n• The number of targets and boxes must be equal");
    }


        validationMessage.set(message.toString());
    }


    public StringProperty validationMessageProperty() {
        return validationMessage;
    }
    public void resetGrid(int width, int height) {
        this.board.resetGrid(width, height);
        updateValidationMessage();
    }

    public boolean isGridChanged() {
        return board.getGrid().isGridChanged();
    }

    public boolean hasPlayer(){
        return board.getGrid().hasPlayer();
    }
    private final BooleanProperty gridReset = new SimpleBooleanProperty(false);
    public BooleanProperty gridResetProperty() {
        return gridReset;
    }
    public void loadLevelFromFile(File file) {
       board.handleOpen(file);
    }





    public Observable maxFilledCellsProperty() {
        return board.maxFilledCellsProperty();
    }

    public void saveLevel(File selectedFile){
        board.savelevel(selectedFile);
    }

    //Todo: à separer pour le jeu (boardview4play)
    public void movePlayer(Board4Play.Direction direction) {
        // Assuming you have a method in Board to move the player
    }
}
