package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sokoban.model.Board4Design;
import sokoban.model.Cell;
import sokoban.model.CellValue;
import sokoban.model.Grid4Design;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.model.element.Ground;

public class CellViewModel4Design extends CellViewModel{

    private BoardViewModel4Design boardViewModel; // Add a reference to BoardViewModel

    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(DEFAULT_SCALE);

    private Element selectedTool = new Ground();
    public CellViewModel4Design(int line, int col, Board4Design board) {
        super(line, col, board);

    }

    @Override
    protected void setBoardViewModel(BoardViewModel4Design boardViewModel) {
        this.boardViewModel = (BoardViewModel4Design) boardViewModel;
    }

    @Override
    public void play() {
        if (boardViewModel != null) {
            Element toolValue = boardViewModel.getSelectedCellValue();
            board.play(line, col, toolValue);
        }
    }

    @Override
    public void addObject() {
        Element selectedTool = boardViewModel.getSelectedTool();

        System.out.println("Attempting to add: " + selectedTool.getType());

        // Check for the current presence of a player and handle the unique rules for a player
        if (selectedTool.getType() == CellValue.PLAYER && boardViewModel.hasPlayer()) {
            System.out.println("A player is already present on the grid. Cannot add another.");
            return;
        }

        // Check if adding a box, replace with a new instance (though this may be unnecessary unless the Box class contains unique states)
        if (selectedTool.getType() == CellValue.BOX) {
            Box newBox = new Box();
            selectedTool = newBox; // Update selectedTool with a new Box instance
        }

        System.out.println("Is cell empty? " + isEmpty());

        // Ensure we update the cell value correctly, allowing goal to combine with box/player
        if (selectedTool.getType() != CellValue.EMPTY) {
            updateCellValue(selectedTool);
        }
    }
    @Override
    public void deleteObject() {
        if (!isEmpty()) {
            Cell cell = board.getGrid().getMatrix()[line][col];
            if (cell.getValue().stream().anyMatch(e -> e instanceof Box)) {
                Grid4Design.decrementBoxCount(); // Decrement the box count
            }
            cell.clearValues();
            board.getGrid().triggerGridChange(); // cela a permis la mise a jour lors de la suppression lors du drag
        }
    }
    @Override
    protected void updateCellValue(Element newValue) {
        Cell cell = board.getGrid().getMatrix()[line][col];
        cell.addValue(newValue);  // This directly utilizes the addValue method of the Cell class
        board.getGrid().triggerGridChange(); // Trigger grid change to update view
    }
    @Override
    public ReadOnlyListProperty<Element> valueProperty() {
        return board.valueProperty(line, col);
    }

    @Override
    protected boolean isEmpty() {
        return board.isEmpty(line, col);
    }

    @Override
    public void handleMouseReleased() {

    }


    @Override
    public void resetScale() {
        scale.set(DEFAULT_SCALE);
    }

    @Override
    public Element getSelectedTool() {
        return this.selectedTool;
    }


}
