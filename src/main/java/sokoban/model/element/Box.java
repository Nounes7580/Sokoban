package sokoban.model.element;

import sokoban.model.CellValue;

public class Box extends Element{

    private int id;
    public Box() {
        super(CellValue.BOX);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {

            this.id = id;

    }

}
