package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;

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
            // Créer un écrivain de fichier
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

            // Parcourir la grille pour écrire chaque objet dans le fichier
            for (int row = 0; row < grid.getGridHeight(); row++) {
                for (int col = 0; col < grid.getGridWidth(); col++) {
                    ReadOnlyListProperty<CellValue> values = valueProperty(row, col);
                    char character;
                    if (values.isEmpty() || values.contains(CellValue.GROUND))
                        character = ' ';
                    else if (values.contains(CellValue.WALL))
                        character = '#';
                    else if (values.contains(CellValue.PLAYER) && values.contains(CellValue.GOAL))
                        character = '+';
                    else if (values.contains(CellValue.BOX) && values.contains(CellValue.GOAL))
                        character = '*';
                    else if (values.contains(CellValue.GOAL))
                        character = '.';
                    else if (values.contains(CellValue.BOX))
                        character = '$';
                    else if (values.contains(CellValue.PLAYER))
                        character = '@';
                    else
                        character = '9';

                    // Écrire le caractère dans le fichier
                    writer.write(character);

                }
                // Aller à la ligne suivante dans le fichier après chaque ligne de la grille
                writer.newLine();
            }

            // Fermer l'écrivain de fichier
            writer.close();

            // Message de succès
            System.out.println("Fichier sauvegardé avec succès : " + selectedFile.getAbsolutePath());
        } catch (IOException e) {
            // Gérer les exceptions d'entrée/sortie (par exemple, erreur d'écriture dans le fichier)
            e.printStackTrace();
        }
    }

    public void handleOpen(File selectedFile){

            try {
                List<String> lines = Files.readAllLines(selectedFile.toPath());

                int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);
                int maxHeight = lines.size();

                resetGrid(maxWidth, maxHeight); // Réinitialiser la grille avec les bonnes dimensions

                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    for (int j = 0; j < line.length(); j++) {
                        char c = line.charAt(j);
                        // Traiter chaque caractère et ajuster la cellule correspondante
                        switch (c) {
                            case '#':
                                getGrid().getMatrix()[i][j].getValue().add(CellValue.WALL);
                                break;
                            case '@':
                                getGrid().getMatrix()[i][j].getValue().add(CellValue.PLAYER);
                                break;
                            case '$':
                                getGrid().getMatrix()[i][j].getValue().add(CellValue.BOX);
                                break;
                            case '.':
                                getGrid().getMatrix()[i][j].getValue().add(CellValue.GOAL);
                                break;
                            case '*': // Boîte sur objectif
                                getGrid().getMatrix()[i][j].getValue().addAll(Arrays.asList(CellValue.BOX, CellValue.GOAL));
                                break;
                            case '+': // Joueur sur objectif
                                getGrid().getMatrix()[i][j].getValue().addAll(Arrays.asList(CellValue.PLAYER, CellValue.GOAL));
                                break;
                            default: // Espaces ou autres caractères sont traités comme vides ou ignorés
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}



