package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;


public class Board {

    private static Grid grid;
    private final BooleanBinding isFull;
    private final LongProperty filledCellsCount = new SimpleLongProperty();
    private final IntegerProperty maxFilledCells = new SimpleIntegerProperty();


    public Board() {
        this.grid = new Grid(10, 15);

        filledCellsCount.set(grid.filledCellsCountProperty().get());


        grid.filledCellsCountProperty().addListener((obs, oldCount, newCount) -> {
            filledCellsCount.set(newCount.longValue());
        });

        isFull = filledCellsCount.greaterThanOrEqualTo(maxFilledCells());
    }

    public void resetGrid(int newWidth, int newHeight) {
        this.grid.resetGrid(newWidth, newHeight);
        this.maxFilledCells.set(newWidth * newHeight / 2);
        // Mise à jour immédiate du nombre de cellules remplies pour refléter la nouvelle grille
        this.filledCellsCount.set(grid.filledCellsCountProperty().get());
        isFull.invalidate();
    }





    public CellValue play(int line, int col, CellValue toolValue) {
        if (line < 0 || line >= grid.getGridWidth() || col < 0 || col >= grid.getGridHeight()) {
            System.out.println("Indices hors limites : line=" + line + ", col=" + col);
            return null;
        }
        if (!isFull.get() || grid.getValue(line, col) != CellValue.EMPTY) {
        CellValue currentValue = grid.getValue(line, col);
        // Si l'outil sélectionné est un goal et que la cellule contient déjà un joueur ou une boîte,
        // nous superposons le goal sur l'élément existant.
        if (toolValue == CellValue.GOAL && (currentValue == CellValue.PLAYER || currentValue == CellValue.BOX)) {
            grid.play(line, col, CellValue.GOAL);
        } else {
            grid.play(line, col, toolValue);
        }
        filledCellsCount.set(calculateFilledCells());
        }
        return grid.getValue(line, col);


    }
    public void setCellValue(int line, int col, CellValue newValue) {
        if (isPositionValid(line, col) && (!isFull.get() || grid.getValue(line, col) != CellValue.EMPTY)) {
            grid.setCellValue(line, col, newValue);
            filledCellsCount.set(calculateFilledCells());
        }
    }

    public Cell getCell(int line, int col) {
        // Vérifie si les indices sont dans les limites
        if (isPositionValid(line, col)) {
            return grid.getMatrix()[line][col];
        } else {
            return null;
        }
    }

    // Méthode pour vérifier si les coordonnées de la cellule sont valides
    public boolean isPositionValid(int line, int col) {
        // Retourne vrai si les coordonnées sont dans les limites de la grille
        return line >= 0 && line <= grid.getGridWidth() && col >= 0 && col <= grid.getGridHeight();
    }

    private long calculateFilledCells() {
        return grid.filledCellsCountProperty().get();

    }

    public static int maxFilledCells() {
        return (grid.getGridWidth() * grid.getGridHeight()) / 2;
    }

    public Boolean isFull() {
        return isFull.get();
    }
    public Grid getGrid() {
        return grid;
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
    }
    public IntegerProperty maxFilledCellsProperty() {
        return this.maxFilledCells;
    }

}
