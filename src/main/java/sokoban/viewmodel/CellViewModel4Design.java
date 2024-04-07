package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sokoban.model.Board4Design;
import sokoban.model.Cell;
import sokoban.model.CellValue;
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
        if (selectedTool.getType() == CellValue.PLAYER && boardViewModel.hasPlayer()) {
            System.out.println("A player is already present on the grid. Cannot add another.");
            return;
        }
        if (selectedTool.getType() != CellValue.EMPTY && isEmpty()) {
            updateCellValue(selectedTool);
        }
    }
    @Override
    public void deleteObject() {
        if (!isEmpty()) {
            Cell cell = board.getGrid().getMatrix()[line][col];
            cell.clearValues();
            board.getGrid().triggerGridChange(); // cela a permis la mise a jour lors de la suppression lors du drag
        }
    }
    @Override
    protected void updateCellValue(Element newValue) {
        board.setCellValue(line, col, newValue);

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
