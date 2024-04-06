package sokoban.model.element;

import sokoban.model.CellValue;

public class Box extends Element{
    private static int nextId =1;
    public Box() {

        super(CellValue.BOX);
        this.id = getNextId();
        System.out.println("Box created with ID: " + this.id);
    }
    public int getId() {
        return id;
    }
    private synchronized static int getNextId() {
        System.out.println("Assigning next ID: " + nextId);
        return nextId++; // Incrémente et renvoie le prochain ID
    }
    public static void resetIdCounter() {
        nextId = 1;
        System.out.println("ID counter reset to 1");
    }


}
