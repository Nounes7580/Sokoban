package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.Board4Design;
import sokoban.model.element.Element;

public class CellViewModel4Play extends CellViewModel{
    public CellViewModel4Play(int line, int col, Board4Design board) {
        super(line, col, board);
    }

    @Override
    protected void setBoardViewModel(BoardViewModel boardViewModel) {

    }

    @Override
    public void play() {

    }

    @Override
    public void addObject() {

    }

    @Override
    public void deleteObject() {

    }

    @Override
    protected void updateCellValue(Element newValue) {

    }

    @Override
    public ReadOnlyListProperty<Element> valueProperty() {
        return null;
    }

    @Override
    protected boolean isEmpty() {
        return false;
    }

    @Override
    public void handleMouseReleased() {

    }

    @Override
    public void resetScale() {

    }

    @Override
    public Element getSelectedTool() {
        return null;
    }
}
