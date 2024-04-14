package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import sokoban.model.element.*;

public abstract class Cell {
    private int row;
    private int column;
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public int[] getPosition() {
        return new int[] {row, column};
    }
    public int getRow() {
        return row;
    }
    public void setValue(Element element) {

        clearValues();
        addValue(element);
    }
    public int getColumn() {
        return column;
    }
    protected final ListProperty<Element> toolObject = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ReadOnlyListProperty<Element> getValue() {
        return toolObject;
    }
    public Cell(){}
    public void addValue(Element value){
        if (value.getType() == CellValue.GROUND || value.getType() == CellValue.WALL) {
            toolObject.clear();
            toolObject.add(value);
        } else if (value.getType() == CellValue.BOX || value.getType() == CellValue.PLAYER) {
            if (toolObject.contains(new Goal())) {
                toolObject.clear();
                toolObject.add(value);
                toolObject.add(new Goal());
            } else {
                toolObject.clear();
                toolObject.add(value);
            }
        } else if (value.getType() == CellValue.GOAL) {
            if (toolObject.contains(new Goal()) || toolObject.contains(new Player()) ||  toolObject.isEmpty()) {
                toolObject.add(value);
            } else {
                toolObject.clear();
                toolObject.add(value);
            }
        }
    }
    public void removeValue(Element value){
        toolObject.remove(value);
    }
    public void clearValues() {
        this.toolObject.clear();
    }
    // Dans la classe Cell, ajoutez une méthode contains pour vérifier la présence d'un type spécifique d'Element

    public boolean hasElementOfType(Class<? extends Element> type) {
        for (Element e : toolObject) {
            if (type.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
    public Element getElementOfType(Class<? extends Element> type) {
        for (Element e : toolObject) {
            if (type.isInstance(e)) {
                return e;
            }
        }
        return null;
    }

    public void play(Element value) {
        if (toolObject.contains(value)) {
            boolean isGoal = hasElementOfType(Goal.class);
            removeValue(value);
            if (isGoal && value.getType() == CellValue.BOX) {
                addValue(new Goal());
            }
        } else {
            addValue(value);
        }
    }

    public boolean isEmpty() {
        return toolObject.isEmpty() || (toolObject.contains(new Ground()) && toolObject.size() == 1);
    }



}
