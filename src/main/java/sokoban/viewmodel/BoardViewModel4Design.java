package sokoban.viewmodel;

import javafx.beans.Observable;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.Board4Design;
import sokoban.model.element.Element;

import java.io.File;



public class BoardViewModel4Design extends BoardViewModel{
    private final GridViewModel4Design gridViewModel;



    private final Board4Design board;
    private final ObjectProperty<Element> selectedTool = new SimpleObjectProperty<>();

    private final StringProperty validationMessage = new SimpleStringProperty();
    private final BooleanProperty gridReset = new SimpleBooleanProperty(false);

    public BoardViewModel4Design(Board4Design board) {
        this.board = board;
        this.gridViewModel = new GridViewModel4Design(board);
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
    @Override
    public BooleanProperty gridResetProperty() {
        return gridReset;
    }

    public  void resetGrid(int width, int height) {
        this.board.resetGrid(width, height);
        updateValidationMessage();
    }
    public Element getSelectedCellValue() {
        return selectedTool.getValue();
    }

    public ObjectProperty<Element> selectedToolProperty() {
        return selectedTool;
    }

    public void setSelectedTool(Element cell) {
        selectedTool.set(cell);
    }


    public  int getGridHeight() {
        return board.getGrid().getGridHeight();
    }

    public  int getGridWidth() {
        return board.getGrid().getGridWidth();
    }
    public Board4Design getBoard() {
        return board;
    }

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
    public GridViewModel4Design getGridViewModel() {
        return gridViewModel;
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public int maxFilledCells() {
        return board.maxFilledCells();
    }

    public StringProperty validationMessageProperty() {
        return validationMessage;
    }
    public boolean isGridChanged() {
        return board.getGrid().isGridChanged();
    }
    public boolean hasPlayer(){
        return board.getGrid().hasPlayer();
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

    public Element getSelectedTool() {
        return selectedTool.get();
    }

    public void incrementBoxCount() {
        board.incrementBoxCount();
    }
}