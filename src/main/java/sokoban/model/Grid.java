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
    // Cette méthode retourne les coordonnées du joueur sous forme d'un tableau [x, y]
    // ou null si aucun joueur n'est trouvé.
    public int[] findPlayerPosition() {
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                if (matrix[i][j].getValue() == CellValue.PLAYER) {
                    return new int[]{i, j}; // Retourne les coordonnées du joueur
                }
            }
        }
        return null; // Aucun joueur trouvé
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

        // Gestion du déplacement du joueur
        if (toolValue == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                // Efface l'ancienne position du joueur
                matrix[playerPos[0]][playerPos[1]].setValue(CellValue.EMPTY);
            }
            // Place le joueur à la nouvelle position
            matrix[line][col].setValue(CellValue.PLAYER);
        } else if (toolValue == CellValue.WALL) {
            // Un mur remplace tout contenu existant
            matrix[line][col].setValue(CellValue.WALL);
        } else if (toolValue == CellValue.GOAL) {
            if (currentValue == CellValue.PLAYER) {
                // Si un goal est placé sur un joueur, définissez l'état à PLAYER_ON_GOAL
                matrix[line][col].setValue(CellValue.PLAYER_ON_GOAL);
            } else if (currentValue == CellValue.BOX) {
                // Si un goal est placé sur une boîte, définissez l'état à BOX_ON_GOAL
                matrix[line][col].setValue(CellValue.BOX_ON_GOAL);
            } else {
                // Placez simplement le goal si aucun joueur ou boîte n'est présent
                matrix[line][col].setValue(CellValue.GOAL);
            }
        } else if ((toolValue == CellValue.BOX) && currentValue == CellValue.GOAL) {
            // Si une boîte est placée sur un goal, définissez l'état à BOX_ON_GOAL
            matrix[line][col].setValue(CellValue.BOX_ON_GOAL);
        } else {
            // Pour tous les autres cas (cellule vide ou contenant un goal), placez l'élément sélectionné
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
