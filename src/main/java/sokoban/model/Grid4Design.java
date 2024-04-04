package sokoban.model;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Player;

import java.util.Arrays;

public class Grid4Design extends Grid{
    public Grid4Design(int width, int height) {
        super(width, height);
    }

    @Override
    protected void initializeCells() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                matrix[i][j] = new Cell4Design();
            }
        }
    }

    @Override
    public void resetGrid(int newWidth, int newHeight) {
        this.gridWidth.set(newWidth);
        this.gridHeight.set(newHeight);
        this.matrix = new Cell[newWidth][newHeight];
        initializeCells();
        triggerGridChange();
    }

    @Override
    public BooleanProperty gridChangedProperty() {
        return gridChanged;
    }

    @Override
    public void triggerGridChange() {
        gridChanged.set(!gridChanged.get()); // Change la valeur pour déclencher l'écouteur
    }

    @Override
    // Cette méthode retourne les coordonnées du joueur sous forme d'un tableau [x, y]
    // ou null si aucun joueur n'est trouvé.
    public int[] findPlayerPosition() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                if (matrix[i][j].getValue().contains(new Player()) || (matrix[i][j].getValue().contains(new Player())&&matrix[i][j].getValue().contains(new Goal()))) {
                    return new int[]{i, j}; // Retourne les coordonnées du joueur
                }
            }
        }
        return null;
    }


    @Override
    public   int getGridWidth() {
        return gridWidth.get();
    }

    @Override
    public int getGridHeight() {
        return gridHeight.get();
    }

    @Override
   protected ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        if (line < 0 || col < 0 || line >= gridWidth.get() || col >= gridHeight.get()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return matrix[line][col].getValue();
    }

    @Override
    protected void play(int line, int col, Element toolValue) {
        // Assurez-vous que les coordonnées sont valides.
        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight()) {
            return; // Position invalide.
        }

        Cell cell = matrix[line][col];

        // Si c'est pour placer un joueur, on d'abord le joueur de sa position actuelle.
        if (toolValue.getType() == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                matrix[playerPos[0]][playerPos[1]].play(new Player());
            }
        }

        // Logique simplifiée pour l'ajout d'états dans la cellule.
        if (toolValue.getType() == CellValue.WALL || toolValue.getType() == CellValue.GROUND) {
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
    @Override
    protected LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }

    @Override
    protected boolean isEmpty(int line, int col) {
        return matrix[line][col].isEmpty();
    }

    @Override
    public boolean hasPlayer() {
        return findPlayerPosition() != null;
    }

    @Override
    public boolean hasAtLeastOneTarget() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .anyMatch(cell -> cell.getValue().contains(new Goal()));
    }

    @Override
    public boolean hasAtLeastOneBox() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .anyMatch(cell -> cell.getValue().contains(new Box()));
    }

    @Override
    public long getTargetCount() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()))
                .count();
    }

    @Override
    public long getBoxCount() {
        return Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Box()))
                .count();
    }

    @Override
    public boolean isGridChanged() {
        return gridChanged.get();
    }

    @Override
    public Cell[][] getMatrix() {
        return matrix;
    }

    @Override
    public void setCellValue(int line, int col, Element newValue) {
        if (line >= 0 && line < gridWidth.get() && col >= 0 && col < gridHeight.get()) {
            matrix[line][col].addValue(newValue);
            triggerGridChange();
        }
    }






}
