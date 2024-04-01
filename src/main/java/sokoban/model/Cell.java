package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class Cell { //Représente une cellule dans la grille

    private final ListProperty<CellValue> toolObject = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ReadOnlyListProperty<CellValue> getValue() {
        return toolObject;
    }
    public Cell(){}
    public void addValue(CellValue value){
        if (value == CellValue.GROUND || value == CellValue.WALL) {
            toolObject.clear();
            toolObject.add(value);
        } else if (value == CellValue.BOX || value == CellValue.PLAYER) {
            if (toolObject.contains(CellValue.GOAL)) {
                toolObject.clear();
                toolObject.add(value);
                toolObject.add(CellValue.GOAL);
            } else {
                toolObject.clear();
                toolObject.add(value);
            }
        } else if (value == CellValue.GOAL) {
            if (toolObject.contains(CellValue.BOX) || toolObject.contains(CellValue.PLAYER) ||  toolObject.isEmpty()) {
                toolObject.add(value);
            } else {
                toolObject.clear();
                toolObject.add(value);
            }
        }
    }
    public void removeValue(CellValue value){
        toolObject.remove(value);
    }
    public void clearValues() {
        this.toolObject.clear();
    }


    public  void play(CellValue value) {
        if (toolObject.contains(value)) {
            removeValue(value);
        }
        else {
            addValue(value);
        }
    }

   public boolean isEmpty() {
        return toolObject.isEmpty() || (toolObject.contains(CellValue.GROUND) && toolObject.size() == 1);
    }




}
