package sokoban.model;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;

import java.util.Arrays;

public class Grid {
    private final IntegerProperty gridWidth = new SimpleIntegerProperty();
    private final IntegerProperty gridHeight = new SimpleIntegerProperty();


    private BooleanProperty gridChanged = new SimpleBooleanProperty();
    private Cell[][] matrix;
    private final LongBinding filledCellsCount;

    // Constructeur adapté pour initialiser avec des dimensions spécifiques
    public Grid(int width, int height) {
        this.gridWidth.set(width);
        this.gridHeight.set(height);
        this.matrix = new Cell[gridWidth.get()][gridHeight.get()];
        initializeCells();

        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !(cell.isEmpty()))
                .count(), gridChanged);
    }

    private void initializeCells() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                matrix[i][j] = new Cell();
            }
        }
    }

    // Méthode pour réinitialiser la grille avec de nouvelles dimensions
    public void resetGrid(int newWidth, int newHeight) {
        this.gridWidth.set(newWidth);
        this.gridHeight.set(newHeight);
        this.matrix = new Cell[newWidth][newHeight];
        initializeCells();
        triggerGridChange();
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
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                if (matrix[i][j].getValue().contains(CellValue.PLAYER) || (matrix[i][j].getValue().contains(CellValue.PLAYER)&&matrix[i][j].getValue().contains(CellValue.GOAL))) {
                    return new int[]{i, j}; // Retourne les coordonnées du joueur
                }
            }
        }
        return null;
    }
    public  int getGridWidth() {
        return gridWidth.get();
    }

    public  int getGridHeight() {
        return gridHeight.get();
    }



    ReadOnlyListProperty<CellValue> valueProperty(int line, int col) {

        return matrix[line][col].getValue();
    }



    public void play(int line, int col, CellValue toolValue) {
        // Assurez-vous que les coordonnées sont valides.
        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight()) {
            return; // Position invalide.
        }

        Cell cell = matrix[line][col];

        // Si c'est pour placer un joueur, on d'abord le joueur de sa position actuelle.
        if (toolValue == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                matrix[playerPos[0]][playerPos[1]].play(CellValue.PLAYER);
            }
        }

        // Logique simplifiée pour l'ajout d'états dans la cellule.
        if (toolValue == CellValue.WALL || toolValue == CellValue.GROUND) {
            // Pour WALL et GROUND, on remplace tout les états existants.
            cell.play(toolValue);
        } else if (!cell.getValue().contains(toolValue)) {
            // Pour les autres états, on ajoute seulement s'ils ne sont pas déjà présents.
            // Cela évite les duplications pour des états comme GOAL qui peut être superposé avec PLAYER ou BOX.
            cell.play(toolValue);

        }

        // Si on ajoute autre chose qu'un GOAL, et que GOAL est déjà présent, on ne le retire pas.
        // Cela permet de garder le GOAL même quand on ajoute PLAYER ou BOX sur celui-ci.

        // invalide le compteur de cellules remplies et déclenche le changement de grille.
        filledCellsCount.invalidate();
        triggerGridChange();
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
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .anyMatch(cell -> cell.getValue().contains(CellValue.GOAL));
    }

    public boolean hasAtLeastOneBox() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .anyMatch(cell -> cell.getValue().contains(CellValue.BOX));
    }

    public long getTargetCount() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(CellValue.GOAL))
                .count();
    }

    public long getBoxCount() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(CellValue.BOX))
                .count();
    }

    public IntegerProperty gridWidthProperty() {
        return new SimpleIntegerProperty(gridWidth.get());
    }

    public IntegerProperty gridHeightProperty() {
        return new SimpleIntegerProperty(gridHeight.get());
    }

    public boolean isGridChanged() {
        return gridChanged.get();
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    public void setCellValue(int line, int col, CellValue newValue) {
        if (line >= 0 && line < gridWidth.get() && col >= 0 && col < gridHeight.get()) {
            matrix[line][col].addValue(newValue);
            triggerGridChange();
        }
    }


}
