package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.view.BoardView;
import sokoban.viewmodel.BoardViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


public class Board {

    private static Grid grid;
    private BooleanBinding isFull;
    private final LongProperty filledCellsCount = new SimpleLongProperty();
    private final IntegerProperty maxFilledCells = new SimpleIntegerProperty();
    private final BooleanProperty gridReset = new SimpleBooleanProperty(false);
    private final BooleanProperty boardUpdated = new SimpleBooleanProperty(false);

    public BooleanProperty boardUpdatedProperty() {
        return boardUpdated;
    }

    public Board() {
        grid = new Grid(15, 10);
        filledCellsCount.set(grid.filledCellsCountProperty().get());
        maxFilledCells.set(maxFilledCells()); // Set maxFilledCells based on grid dimensions

        isFull = Bindings.createBooleanBinding(() ->
                        filledCellsCount.get() >= maxFilledCells.get(),
                filledCellsCount, maxFilledCells
        );

        grid.filledCellsCountProperty().addListener((obs, oldCount, newCount) -> {
            filledCellsCount.set(newCount.longValue());
        });
        maxFilledCells.addListener((obs, oldVal, newVal) -> {
            isFull = filledCellsCount.greaterThanOrEqualTo(newVal.intValue());
        });
    }



    public void resetGrid(int newWidth, int newHeight) {
        this.maxFilledCells.set(newWidth * newHeight / 2); // Update maxFilledCells based on new dimensions

        this.grid.resetGrid(newWidth, newHeight);
        this.maxFilledCells.set(newWidth * newHeight / 2);
        // Mise à jour immédiate du nombre de cellules remplies pour refléter la nouvelle grille
        this.filledCellsCount.set(grid.filledCellsCountProperty().get());

        // Update isFull binding
        isFull = Bindings.createBooleanBinding(() ->
                        filledCellsCount.get() >= maxFilledCells.get(),
                filledCellsCount, maxFilledCells
        );
    }




    public void play(int line, int col, CellValue toolValue) {
        System.out.println("filledCellsCount: " + filledCellsCount.get());
        System.out.println("maxFilledCells: " + maxFilledCells.get());
        System.out.println("isFull: " + isFull.get());

        if (line < 0 || line >= grid.getGridWidth() || col < 0 || col >= grid.getGridHeight()) {
            System.out.println("Indices hors limites : line=" + line + ", col=" + col);
            return; // Aucune valeur à retourner, la méthode peut être de type void.
        }

        // Accéder directement à la cellule pour manipuler ses états.
        Cell cell = grid.getMatrix()[line][col];

        // Si la grille n'est pas pleine ou si la cellule n'est pas vide, procéder à la manipulation.
        if (!isFull.get() || !cell.getValue().isEmpty()) {
            // Utiliser une méthode adaptée de Grid pour gérer l'ajout des états.
            grid.play(line, col, toolValue);
            filledCellsCount.set(calculateFilledCells()); // Recalculer après manipulation.
        }
        // Cette méthode ne retourne plus de CellValue car cela n'a pas de sens avec la structure de données actuelle.
    }

    public void setCellValue(int line, int col, CellValue newValue) {
        if (isPositionValid(line, col) && (!isFull.get() || !grid.valueProperty(line, col).getValue().isEmpty())) {
            grid.setCellValue(line, col, newValue);
            filledCellsCount.set(calculateFilledCells());
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

    public  int maxFilledCells() {
        return (grid.getGridWidth() * grid.getGridHeight()) / 2;
    }

    public Boolean isFull() {
        return isFull.get();
    }
    public Grid getGrid() {
        return grid;
    }

    public ReadOnlyListProperty<CellValue> valueProperty(int line, int col) {
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

    public void savelevel(File selectedFile){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

            for (int col = 0; col < grid.getMatrix()[0].length; col++) {
                for (int row = 0; row < grid.getMatrix().length; row++) { // Direct order for rows
                    ReadOnlyListProperty<CellValue> values = valueProperty(row, col);
                    char character = determineCharacter(values);
                    writer.write(character);
                }
                writer.newLine();
            }

            writer.close();
            System.out.println("File saved successfully: " + selectedFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private char determineCharacter(ReadOnlyListProperty<CellValue> values) {
        if (values.contains(CellValue.WALL)) return '#';
        if (values.contains(CellValue.PLAYER)) return '@';
        if (values.contains(CellValue.BOX)) return '$';
        if (values.contains(CellValue.GOAL)) return '.';
        if (values.contains(CellValue.PLAYER) && values.contains(CellValue.GOAL)) return '+';
        if (values.contains(CellValue.BOX) && values.contains(CellValue.GOAL)) return '*';
        return ' ';  // Default character for empty cell or ground
    }


    public void handleOpen(File selectedFile){
        try {
            List<String> lines = Files.readAllLines(selectedFile.toPath());

            int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);
            int maxHeight = lines.size();

            resetGrid(maxWidth, maxHeight);

            boardUpdated.set(!boardUpdated.get());

            for (int i = 0; i < maxHeight; i++) {
                String line = lines.get(i);
                for (int j = 0; j < maxWidth && j < line.length(); j++) {
                    char c = line.charAt(j);
                    // i est pour les lignes, j est pour les colonnes


                    switch (c) {
                        case '#':
                            getGrid().getMatrix()[j][i].getValue().add(CellValue.WALL);
                            break;
                        case '@':
                            getGrid().getMatrix()[j][i].getValue().add(CellValue.PLAYER);
                            break;
                        case '$':
                            getGrid().getMatrix()[j][i].getValue().add(CellValue.BOX);
                            break;
                        case '.':
                            getGrid().getMatrix()[j][i].getValue().add(CellValue.GOAL);
                            break;
                        case '*':
                            getGrid().getMatrix()[j][i].getValue().addAll(Arrays.asList(CellValue.BOX, CellValue.GOAL));
                            break;
                        case '+':
                            getGrid().getMatrix()[j][i].getValue().addAll(Arrays.asList(CellValue.PLAYER, CellValue.GOAL));
                            break;
                        // Ajoutez plus de cas si nécessaire
                    }
                }
            }
            grid.triggerGridChange();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Todo: Implement tout ce qui est en bas est pour le déplacement du joueur
    public void movePlayer(Direction direction) {
        int[] playerPosition = grid.findPlayerPosition();
        if (playerPosition == null) {
            // Handle the case where the player is not found
            return;
        }

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();

        // First, check if the move is valid
        if (isMoveValid(newRow, newCol)) {
            // Move the player
            grid.play(playerPosition[0], playerPosition[1], CellValue.EMPTY); // Remove the player from the old position
            grid.play(newRow, newCol, CellValue.PLAYER); // Place the player in the new position
        }

        // After the move, the filled cells count might have changed
        filledCellsCount.set(calculateFilledCells());

        // Notify any observers that the grid has changed
        grid.triggerGridChange();
    }

    private boolean isMoveValid(int newRow, int newCol) {
        // Check boundaries
        if (newRow < 0 || newRow >= grid.getGridHeight() || newCol < 0 || newCol >= grid.getGridWidth()) {
            return false;
        }

        // Check if the target cell is empty or contains a goal
        Cell targetCell = grid.getMatrix()[newRow][newCol];
        return targetCell.isEmpty() || targetCell.getValue().contains(CellValue.GOAL);
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

    public void moveBox(Direction direction) {
        int[] playerPosition = grid.findPlayerPosition();
        if (playerPosition == null) return;

        int boxRow = playerPosition[0] + direction.getDeltaRow();
        int boxCol = playerPosition[1] + direction.getDeltaCol();

        int destinationRow = boxRow + direction.getDeltaRow();
        int destinationCol = boxCol + direction.getDeltaCol();

        // Vérifie si le mouvement est valide
        if (isValidMove(boxRow, boxCol, destinationRow, destinationCol)) {
            // Mettre à jour la grille
            grid.setCellValue(playerPosition[0], playerPosition[1], CellValue.EMPTY);
            grid.setCellValue(boxRow, boxCol, CellValue.PLAYER);
            grid.setCellValue(destinationRow, destinationCol, CellValue.BOX);

            // Peut-être notifier le système que la grille a changé
            grid.triggerGridChange();
        }
    }

    private boolean isValidMove(int boxRow, int boxCol, int destinationRow, int destinationCol) {
        // Vérifie si la destination est dans les limites de la grille
        if (destinationRow < 0 || destinationRow >= grid.getGridHeight() ||
                destinationCol < 0 || destinationCol >= grid.getGridWidth()) {
            return false;
        }

        // Vérifie si la destination est libre (ni mur ni caisse)
        Cell destinationCell = grid.getMatrix()[destinationRow][destinationCol];
        return !destinationCell.getValue().contains(CellValue.WALL) &&
                !destinationCell.getValue().contains(CellValue.BOX);
    }
}



