package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Ground;
import sokoban.model.element.Player;

public class Cell { //Représente une cellule dans la grille

    private final ListProperty<Element> toolObject = new SimpleListProperty<>(FXCollections.observableArrayList());
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


    public  void play(Element value) {
        if (toolObject.contains(value)) {
            removeValue(value);
        }
        else {
            addValue(value);
        }
    }

   public boolean isEmpty() {
        return toolObject.isEmpty() || (toolObject.contains(new Ground()) && toolObject.size() == 1);
    }




}
