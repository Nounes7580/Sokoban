package sokoban.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;
import sokoban.model.Grid;
import javafx.beans.binding.LongBinding;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private final ObjectProperty<CellValue> selectedTool = new SimpleObjectProperty<>(CellValue.GROUND);
    private final StringProperty validationMessage = new SimpleStringProperty();


    public BoardViewModel(Board board) {
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

    if (board.getGrid().getTargetCount() != board.getGrid().getBoxCount()) {
        message.append("\n• The number of targets and boxes must be equal");
    }


        validationMessage.set(message.toString());
    }


    public StringProperty validationMessageProperty() {
        return validationMessage;
    }
    public void resetGrid(int width, int height) {
        this.board.getGrid().resetGrid(width, height);
        updateValidationMessage();
    }

    public boolean isGridChanged() {
        return board.getGrid().isGridChanged();
    }

    public boolean hasPlayer(){
        return board.getGrid().hasPlayer();
    }

    public void loadLevelFromFile(File file) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());

                int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);
                int maxHeight = lines.size();

                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    System.out.println("Loading line " + i + ": " + line);
                    for (int j = 0; j < line.length(); j++) {
                        if (i < maxHeight && j < maxWidth) {
                            char c = line.charAt(j);
                            CellValue cellValue = charToCellValue(c);
                            System.out.println("Setting cell [" + i + ", " + j + "] to " + cellValue);
                            board.setCellValue(i, j, cellValue);
                        }
                    }
                }

            } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private CellValue charToCellValue(char c) {
        switch (c) {
            case '#': return CellValue.WALL;
            case '@': return CellValue.PLAYER;
            case '$': return CellValue.BOX;
            case '.': return CellValue.GOAL;
            case '*': return CellValue.BOX_ON_GOAL;
            case '+': return CellValue.PLAYER_ON_GOAL;
            default: return CellValue.EMPTY;
        }
    }

}
