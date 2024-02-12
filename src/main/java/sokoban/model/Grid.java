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

    void play(int line, int col, CellValue playerValue) {
        if (playerValue == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                // Joueur déjà présent, le déplacer
                matrix[playerPos[0]][playerPos[1]].setValue(CellValue.EMPTY); // Efface l'ancienne position du joueur
            }
            // Place le joueur à la nouvelle position
            matrix[line][col].setValue(playerValue);
        } else {
            // Votre logique existante pour les autres types de cellules
            matrix[line][col].setValue(playerValue);
        }
        filledCellsCount.invalidate(); // Recalculer le nombre de cellules remplies
    }


    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }

    public boolean isEmpty(int line, int col) {
        return matrix[line][col].isEmpty();
    }
}
