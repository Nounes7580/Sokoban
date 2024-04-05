package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.*;

public class Board4Play {

    public Grid4Play getGrid4Play() {
        return grid4Play;
    }

    public static Grid4Play grid4Play;
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

    public static boolean isPositionValid(int line, int col) {
        return line >= 0 && line < grid4Play.getGridWidth() && col >= 0 && col < grid4Play.getGridHeight();
    }



    public static void movePlayer(Direction direction) {
        int[] playerPosition = grid4Play.findPlayerPosition();
        if (playerPosition == null) {
            System.out.println("Joueur introuvable");
            return;
        }

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();

        if (!isPositionValid(newRow, newCol)) {
            System.out.println("Déplacement invalide : hors limites");
            return;
        }

        Cell cell = grid4Play.getCell(newRow, newCol);
        if (cell.hasElementOfType(Box.class)) {
            if (canMoveBox(newRow, newCol, direction)) {
                moveBox(newRow, newCol, direction);
                grid4Play.setCellValue(playerPosition[0], playerPosition[1], new Ground());
                grid4Play.setCellValue(newRow, newCol, new Player());
            }
        } else if (cell.isEmpty() || cell.hasElementOfType(Goal.class)) {
            grid4Play.setCellValue(playerPosition[0], playerPosition[1], new Ground());
            grid4Play.setCellValue(newRow, newCol, new Player());
        }
    }


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
    private static boolean canMoveBox(int boxRow, int boxCol, Direction direction) {
        int newRow = boxRow + direction.getDeltaRow();
        int newCol = boxCol + direction.getDeltaCol();
        if (!isPositionValid(newRow, newCol)) {
            return false;
        }
        Cell cell = grid4Play.getCell(newRow, newCol);
        return cell.isEmpty() ||  cell.hasElementOfType(Goal.class);
    }

    private static void moveBox(int boxRow, int boxCol, Direction direction) {
        int newRow = boxRow + direction.getDeltaRow();
        int newCol = boxCol + direction.getDeltaCol();
        grid4Play.setCellValue(boxRow, boxCol, new Ground());
        grid4Play.setCellValue(newRow, newCol, new Box());
    }

}
