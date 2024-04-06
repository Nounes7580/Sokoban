package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.Board4Design;
import sokoban.model.Board4Play;
import sokoban.model.element.Element;

public class CellViewModel4Play {
    private Board4Play board4Play;
    protected int line;
    protected int col;
    public CellViewModel4Play(int line, int col, Board4Play board) {
        this.line = line;
        this.col = col;
        this.board4Play = board;
    }


    protected void setBoardViewModel(BoardViewModel boardViewModel) {

    }

    public void play() {

    }


    public void addObject() {

    }

    public void deleteObject() {

    }

    public ReadOnlyListProperty<Element> valueProperty() {
        return board4Play.valueProperty(line, col);
    }

    protected boolean isEmpty() {
        return false;
    }

    public void handleMouseReleased() {

    }

    public void resetScale() {

    }

    public Element getSelectedTool() {
        return null;
    }
}
