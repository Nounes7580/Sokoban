package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Ground;
import sokoban.model.element.Player;

public class Board4Play {

    public Grid4Play getGrid4Play() {
        return grid4Play;
    }

    public Grid4Play grid4Play;
    public ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        System.out.println(grid4Play.valueProperty(line, col));
        return grid4Play.valueProperty(line, col);
    }

    public Board4Play(Board4Design board4Design) {
        grid4Play=new Grid4Play(board4Design.grid4Design);
    }


    public void play(int line, int col, Element toolValue) {


        if (line < 0 || line >= grid4Play.getGridWidth() || col < 0 || col >= grid4Play.getGridHeight()) {
            System.out.println("Indices hors limites : line=" + line + ", col=" + col);
            return; // Aucune valeur à retourner, la méthode peut être de type void.
        }

        // Accéder directement à la cellule pour manipuler ses états.
        grid4Play.play(line,col,toolValue);

        // Si la grille n'est pas pleine ou si la cellule n'est pas vide, procéder à la manipulation.

        // Cette méthode ne retourne plus de CellValue car cela n'a pas de sens avec la structure de données actuelle.
    }

    /*public boolean isPositionValid(int line, int col) {
        // Validate position for gameplay
        return line >= 0 && line < grid4Play.getGridWidth() && col >= 0 && col < grid.getGridHeight();
    }

     */








    public enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        private final int deltaRow;
        private final int deltaCol;

        Direction(int deltaRow, int deltaCol) {
            this.deltaRow = deltaRow;
            this.deltaCol = deltaCol;
        }

        public int getDeltaRow() {
            return deltaRow;
        }

        public int getDeltaCol() {
            return deltaCol;
        }
    }

    // Additional gameplay methods like movePlayer()
}
