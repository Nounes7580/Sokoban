package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Player;

import java.util.Arrays;

public class Grid4Design extends Grid{
    protected LongBinding filledCellsCount;



    protected Cell4Design[][] cell4Design;

    public Grid4Design(){}

    public Grid4Design(int width, int height) {
        this.gridWidth.set(width);
        this.gridHeight.set(height);
        this.cell4Design = new Cell4Design[gridWidth.get()][gridHeight.get()];
        initializeCells();

        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(cell4Design)
                .flatMap(Arrays::stream)
                .filter(cell -> !(cell.isEmpty()))
                .count(), gridChanged);
    }


    protected void initializeCells() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                cell4Design[i][j] = new Cell4Design();
            }
        }
    }

    @Override
    public void resetGrid(int newWidth, int newHeight) {
        this.gridWidth.set(newWidth);
        this.gridHeight.set(newHeight);
        this.cell4Design = new Cell4Design[newWidth][newHeight];
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
                if (cell4Design[i][j].getValue().contains(new Player()) || (cell4Design[i][j].getValue().contains(new Player())&&cell4Design[i][j].getValue().contains(new Goal()))) {
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
    public Cell4Design getCell4Design(int i, int j) {
        return cell4Design[i][j];
    }
    @Override
   protected ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        if (line < 0 || col < 0 || line >= gridWidth.get() || col >= gridHeight.get()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return cell4Design[line][col].getValue();
    }

    @Override
    protected void play(int line, int col, Element toolValue) {
        // Assurez-vous que les coordonnées sont valides.
        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight()) {
            return; // Position invalide.
        }

        Cell cell = cell4Design[line][col];

        // Si c'est pour placer un joueur, on d'abord le joueur de sa position actuelle.
        if (toolValue.getType() == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                cell4Design[playerPos[0]][playerPos[1]].play(new Player());
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
        return cell4Design[line][col].isEmpty();
    }

    @Override
    public boolean hasPlayer() {
        return findPlayerPosition() != null;
    }

    @Override
    public boolean hasAtLeastOneTarget() {
        return Arrays.stream(cell4Design)
                .flatMap(Arrays::stream)
                .anyMatch(cell -> cell.getValue().contains(new Goal()));
    }

    @Override
    public boolean hasAtLeastOneBox() {
        return Arrays.stream(cell4Design)
                .flatMap(Arrays::stream)
                .anyMatch(cell -> cell.getValue().contains(new Box()));
    }

    @Override
    public long getTargetCount() {
        return Arrays.stream(cell4Design)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()))
                .count();
    }

    @Override
    public long getBoxCount() {
        return Arrays.stream(cell4Design)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Box()))
                .count();
    }

    @Override
    public boolean isGridChanged() {
        return gridChanged.get();
    }


    public Cell4Design[][] getMatrix() {
        return cell4Design;
    }


    @Override
    public void setCellValue(int line, int col, Element newValue) {
        if (line >= 0 && line < gridWidth.get() && col >= 0 && col < gridHeight.get()) {
            cell4Design[line][col].addValue(newValue);
            triggerGridChange();
        }
    }






}
