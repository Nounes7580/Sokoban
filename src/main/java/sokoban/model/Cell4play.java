package sokoban.model;

public class Cell4play extends Cell{

    public Cell4play(Cell4Design cell4Design){
        toolObject.addAll(cell4Design.getValue());
    }
}
