package sokoban.model;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid4Play extends Grid{

    private Cell4play[][] cell4Play;



/** Initialise la grille de jeu avec les dimensions spécifiées par une grille de conception (Grid4Design).
 Crée un tableau bidimensionnel de Cell4play et initialise chaque cellule en fonction de la grille de conception. **/
    public Grid4Play(Grid4Design grid4Design) {
        this.gridWidth.set(grid4Design.getGridWidth());
        this.gridHeight.set(grid4Design.getGridHeight());
        this.cell4Play = new Cell4play[gridWidth.get()][gridHeight.get()];
        initializeCells(grid4Design);
        System.out.println("Grid Width: " + this.gridWidth.get());
        System.out.println("Grid Height: " + this.gridHeight.get());
    }

/** Initialise les cellules de la grille en les copiant ou en adaptant les cellules à partir de la grille de conception, permettant une transition des éléments de conception vers le jeu. **/
    private void initializeCells(Grid4Design grid4Design) {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                this.cell4Play[i][j] = new Cell4play(grid4Design.getCell4Design(i,j)); // Ou une autre logique pour initialiser chaque cellule
            }
        }
    }
    /** Retourne la cellule située à l'indice spécifié. Si l'indice est hors limites, affiche un message d'erreur.**/
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= gridWidth.get() || col < 0 || col >= gridHeight.get()) {
            System.out.println("Tentative d'accès hors des limites de la grille");
        }
        return cell4Play[row][col];
    }
/** Méthode abstraite à implémenter pour réinitialiser la grille avec de nouvelles dimensions.**/
    @Override
    protected void resetGrid(int newWidth, int newHeight) {

    }
/** Méthode abstraite à implémenter pour retourner la propriété booléenne qui indique si la grille a été modifiée.**/
    @Override
    public BooleanProperty gridChangedProperty() {
        return null;
    }
/** Déclenche une modification de la propriété gridChanged pour indiquer un changement dans la grille, utile pour les mises à jour de l'interface utilisateur.**/
    @Override
    public void triggerGridChange() {
        gridChanged.set(!gridChanged.get());
    }
/** Cherche et retourne les coordonnées du joueur dans la grille. Affiche un message si le joueur est trouvé ou non.**/
    @Override
    public int[] findPlayerPosition() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                if (cell4Play[i][j].getValue().stream().anyMatch(e -> e instanceof Player)) {
                    System.out.println("Player found at position (" + i + ", " + j + ")");
                    return new int[]{i, j};
                }
            }
        }
        System.out.println("Player not found.");
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
/** Retourne la propriété de liste en lecture seule des éléments contenus dans une cellule spécifiée, avec vérification des indices pour éviter les accès hors limites. **/
    @Override
    protected ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        if (line < 0 || col < 0 || line >= gridWidth.get() || col >= gridHeight.get()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return cell4Play[line][col].getValue();
    }
/** Applique un élément à une position spécifiée de la grille. Gère également la réinitialisation de la position du joueur si nécessaire.**/
    @Override
    protected void play(int line, int col, Element toolValue) {
        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight()) {
            return;
        }

        if (toolValue.getType() == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null && (playerPos[0] != col || playerPos[1] != line)) {
                cell4Play[playerPos[0]][playerPos[1]].play(new Ground());
            }
        }

        cell4Play[line][col].play(toolValue);

        triggerGridChange();
    }
    /** Méthode abstraite à implémenter pour fournir une liaison indiquant le nombre de cellules remplies.**/
    @Override
    protected LongBinding filledCellsCountProperty() {
        return null;
    }
/** Vérifie si une cellule spécifiée est vide, utilisée pour déterminer si des actions peuvent être effectuées dans cette cellule.**/
    @Override
    protected boolean isEmpty(int line, int col) {
        return cell4Play[line][col].getValue().isEmpty();
    }
/** Méthodes abstraites à implémenter pour vérifier la présence de joueurs, de cibles ou de boîtes dans la grille.**/
    @Override
    public boolean hasPlayer() {
        return false;
    }

    @Override
    public boolean hasAtLeastOneTarget() {
        return false;
    }

    @Override
    public boolean hasAtLeastOneBox() {
        return false;
    }
  /**  Calcule et retourne le nombre de cibles (Goal) présentes dans la grille. **/
    public long getTargetCount() {
        return Arrays.stream(cell4Play)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()))
                .count();
    }
/** Méthode abstraite à implémenter pour retourner le nombre de boîtes dans la grille.**/
    @Override
    public long getBoxCount() {
        return 0;
    }

/**Vérifie si la grille a été modifiée.**/
    @Override
    public boolean isGridChanged() {
        return gridChanged.get();
    }

/** Méthode abstraite à implémenter pour définir la valeur d'une cellule spécifique.**/
    @Override
    public void setCellValue(int line, int col, Element newValue) {

    }
/** Retourne le tableau bidimensionnel de cellules, utile pour les manipulations directes sur la grille.**/
    public Cell[][] getMatrix() {
        return cell4Play;
    }

/** Ajoute un joueur à une cellule spécifiée, en vérifiant si le déplacement est valide et si la cellule cible est appropriée (vide ou contient un objectif).**/
    public void addPlayerToCell(int newRow, int newCol) {
        if (newRow < 0 || newRow >= gridWidth.get() || newCol < 0 || newCol >= gridHeight.get()) {
            System.out.println("Move is invalid: Player out of bounds.");
            return;
        }

        Cell targetCell = cell4Play[newRow][newCol];
        System.out.println("Target cell value: " + targetCell.getValue());

        if (targetCell.isEmpty() || targetCell.hasElementOfType(Goal.class)) {
            play(newRow, newCol, new Player());
        }
    }

}
