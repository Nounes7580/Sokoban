package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Arrays;

public class Grid { //gérerait la structure de la grille de jeu qui contient les cellules(Cell)
    public static int GRID_WIDTH = 10;
    public static int GRID_HEIGHT = 15; // Renommé et utilisé
    private BooleanProperty gridChanged = new SimpleBooleanProperty();


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
    public BooleanProperty gridChangedProperty() {
        return gridChanged;
    }
    private void triggerGridChange() {
        gridChanged.set(!gridChanged.get()); // Change la valeur pour déclencher l'écouteur
    }

    // Cette méthode retourne les coordonnées du joueur sous forme d'un tableau [x, y]
    // ou null si aucun joueur n'est trouvé.
    public int[] findPlayerPosition() {
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                if (matrix[i][j].getValue() == CellValue.PLAYER || matrix[i][j].getValue() == CellValue.PLAYER_ON_GOAL) {
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
            int[] playerPos = findPlayerPosition(); // Trouve la position actuelle du joueur.
            if (playerPos != null) {
                // Obtenez l'état de la cellule où le joueur était précédemment.
                CellValue previousPlayerCellState = getValue(playerPos[0], playerPos[1]);

                // Si le joueur était sur un goal, la cellule précédente devient juste un goal.
                if (previousPlayerCellState == CellValue.PLAYER_ON_GOAL) {
                    matrix[playerPos[0]][playerPos[1]].setValue(CellValue.GOAL);
                } else {
                    // Sinon, effacez simplement l'ancienne position du joueur.
                    matrix[playerPos[0]][playerPos[1]].setValue(CellValue.EMPTY);
                }
            }

            // Gère la nouvelle position du joueur
            if (currentValue == CellValue.GOAL) {
                matrix[line][col].setValue(CellValue.PLAYER_ON_GOAL);
            } else {
                matrix[line][col].setValue(CellValue.PLAYER);
            }
        } else if (toolValue == CellValue.WALL) {
            // Un mur remplace tout contenu existant
            matrix[line][col].setValue(CellValue.WALL);
        } else if (toolValue == CellValue.GOAL) {
            // Gère le placement du goal
            if (currentValue == CellValue.PLAYER) {
                matrix[line][col].setValue(CellValue.PLAYER_ON_GOAL);
            } else if (currentValue == CellValue.BOX) {
                matrix[line][col].setValue(CellValue.BOX_ON_GOAL);
            } else {
                matrix[line][col].setValue(CellValue.GOAL);
            }
        } else if (toolValue == CellValue.BOX) {
            // Gère le placement d'une boîte
            if (currentValue == CellValue.GOAL) {
                matrix[line][col].setValue(CellValue.BOX_ON_GOAL);
            } else {
                matrix[line][col].setValue(CellValue.BOX);
            }
        } else {
            // Pour les autres éléments, remplacez simplement le contenu existant
            matrix[line][col].setValue(toolValue);
        }

        filledCellsCount.invalidate(); // Recalcule le nombre de cellules remplies après chaque changement.
        triggerGridChange(); // Appelé après modification de la grille

    }


    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }

    public boolean isEmpty(int line, int col) {
        return matrix[line][col].isEmpty();
    }

    public boolean hasPlayer() {
        return findPlayerPosition() != null;
    }

    public boolean hasAtLeastOneTarget() {
        return Arrays.stream(matrix).flatMap(Arrays::stream).anyMatch(cell -> cell.getValue() == CellValue.GOAL || cell.getValue() == CellValue.PLAYER_ON_GOAL|| cell.getValue() == CellValue.BOX_ON_GOAL);
    }

    public boolean hasAtLeastOneBox() {
        return Arrays.stream(matrix).flatMap(Arrays::stream).anyMatch(cell -> cell.getValue() == CellValue.BOX || cell.getValue() == CellValue.BOX_ON_GOAL);
    }

    public long getTargetCount() {
        return Arrays.stream(matrix).flatMap(Arrays::stream).filter(cell -> cell.getValue() == CellValue.GOAL || cell.getValue() == CellValue.PLAYER_ON_GOAL|| cell.getValue() == CellValue.BOX_ON_GOAL).count();
    }
    public long getBoxCount() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue() == CellValue.BOX || cell.getValue() == CellValue.BOX_ON_GOAL)
                .count();
    }
    public Cell[][] getMatrix() {
        return matrix;
    }

}
