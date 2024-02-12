package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.Arrays;

public class Grid { //gérerait la structure de la grille de jeu qui contient les cellules(Cell)
    static int GRID_WIDTH = 10;
    static int GRID_HEIGHT = 15; // Renommé et utilisé

    private final Cell[][] matrix;
    private final LongBinding filledCellsCount;

    public Grid() {
        matrix = new Cell[GRID_WIDTH][GRID_HEIGHT];
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                matrix[i][j] = new Cell();

            }
        }

        // Calcul du nombre de cellules remplies. Pas de dépendances supplémentaires nécessaires ici.
        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue() != CellValue.EMPTY && cell.getValue() != CellValue.GROUND)
                .count());
    }

    public static int getGridWidth() {
        return GRID_WIDTH;
    }
    public static int getGridHeight() {
        return GRID_HEIGHT;
    }

    ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return matrix[line][col].valueProperty();
    }

    CellValue getValue(int line, int col) {
        return matrix[line][col].getValue();
    }

    void play(int line, int col, CellValue toolValue) {
        CellValue currentValue = getValue(line, col);

        if (toolValue == CellValue.WALL) {
            // Un mur remplace tout contenu existant.
            matrix[line][col].setValue(CellValue.WALL);
        } else if (toolValue == CellValue.GOAL) {
            if (currentValue == CellValue.PLAYER) {
                // Si un goal est placé sur un joueur, définissez l'état à PLAYER_ON_GOAL.
                matrix[line][col].setValue(CellValue.PLAYER_ON_GOAL);
            } else if (currentValue == CellValue.BOX) {
                // Si un goal est placé sur une boîte, définissez l'état à BOX_ON_GOAL.
                matrix[line][col].setValue(CellValue.BOX_ON_GOAL);
            } else {
                // Si un goal est placé sur une cellule vide ou contenant déjà un goal, placez simplement le goal.
                matrix[line][col].setValue(CellValue.GOAL);
            }
        } else if ((toolValue == CellValue.PLAYER || toolValue == CellValue.BOX) && currentValue == CellValue.GOAL) {
            // Si un joueur ou une boîte est placé sur un goal, gardez l'état à GOAL pour conserver la superposition visuelle.
            // Note: Vous pouvez choisir de définir explicitement à PLAYER_ON_GOAL ou BOX_ON_GOAL ici si cela correspond à votre logique d'affichage.
        } else {
            // Dans tous les autres cas, placez l'élément sélectionné.
            matrix[line][col].setValue(toolValue);
        }

        filledCellsCount.invalidate(); // Recalcule le nombre de cellules remplies.
    }

    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }

    public boolean isEmpty(int line, int col) {
        return matrix[line][col].isEmpty();
    }
}
