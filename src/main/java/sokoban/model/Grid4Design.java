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
    private static int boxCount = 1;
    protected Cell4Design[][] cell4Design;
/** Initialise la grille avec les dimensions spécifiées (width et height).
 Crée un tableau bidimensionnel de Cell4Design pour stocker les cellules de la grille.
 Initialise chaque cellule dans le tableau.
 Configure une liaison (filledCellsCount) qui calcule le nombre de cellules non vides.
**/
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
/** Décrémente le compteur de boîtes s'il est supérieur à 1. Cette méthode pourrait être utilisée pour ajuster le nombre de boîtes disponibles lors de la conception du niveau.**/
    public static void decrementBoxCount() {
        if (boxCount > 1) {
            boxCount--;
        }
    }

/** Initialise chaque cellule de la grille en créant de nouvelles instances de Cell4Design.**/
    protected void initializeCells() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                cell4Design[i][j] = new Cell4Design();
            }
        }
    }
/** Réinitialise la grille avec de nouvelles dimensions, recrée le tableau de cellules, réinitialise le compteur de boîtes à 1, et déclenche un changement de la grille. **/
    @Override
    public void resetGrid(int newWidth, int newHeight) {
        this.gridWidth.set(newWidth);
        this.gridHeight.set(newHeight);
        this.cell4Design = new Cell4Design[newWidth][newHeight];
        boxCount = 1;
        initializeCells();
        triggerGridChange();
    }
/** Retourne la propriété BooleanProperty qui indique si la grille a changé. Utile pour la liaison avec la vue.**/
    @Override
    public BooleanProperty gridChangedProperty() {
        return gridChanged;
    }
/** Change la valeur de la propriété gridChanged pour déclencher une notification de changement.**/
    @Override
    public void triggerGridChange() {
        gridChanged.set(!gridChanged.get());
    }
/** Cherche et retourne les coordonnées du joueur dans la grille, ou null si aucun joueur n'est trouvé. **/
    @Override
    public int[] findPlayerPosition() {
        for (int i = 0; i < gridWidth.get(); i++) {
            for (int j = 0; j < gridHeight.get(); j++) {
                if (cell4Design[i][j].getValue().contains(new Player()) || (cell4Design[i][j].getValue().contains(new Player())&&cell4Design[i][j].getValue().contains(new Goal()))) {
                    return new int[]{i, j};
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
    /** Retourne la cellule située aux coordonnées (i, j) dans la grille. **/
    public Cell4Design getCell4Design(int i, int j) {
        return cell4Design[i][j];
    }
    /** Retourne la propriété de liste en lecture seule contenant les éléments à une position spécifique. Lève une exception si les indices sont hors limites. **/
    @Override
   protected ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        if (line < 0 || col < 0 || line >= gridWidth.get() || col >= gridHeight.get()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return cell4Design[line][col].getValue();
    }
/** Traite un jeu d'éléments sur la grille, vérifie les conditions pour placer ou déplacer un joueur ou d'autres éléments, et gère les interactions complexes entre les éléments.**/
    @Override
    protected void play(int line, int col, Element toolValue) {

        if (line < 0 || line >= getGridWidth() || col < 0 || col >= getGridHeight() || toolValue == null) {
            return;
        }

        Cell cell = cell4Design[line][col];



        if (toolValue.getType() == CellValue.PLAYER) {
            int[] playerPos = findPlayerPosition();
            if (playerPos != null) {
                cell4Design[playerPos[0]][playerPos[1]].play(new Player());

            }

            if (toolValue.getType() == CellValue.WALL || toolValue.getType() == CellValue.GROUND) {

                cell.play(toolValue);
            } else if (!cell.getValue().contains(toolValue)) {

                cell.play(toolValue);
            }

            filledCellsCount.invalidate();
            triggerGridChange();
        }
    }
    /** Retourne une liaison qui calcule le nombre de cellules remplies dans la grille.**/
    @Override
    protected LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
/** Vérifie si une cellule spécifiée est vide.**/
    @Override
    protected boolean isEmpty(int line, int col) {
        return cell4Design[line][col].isEmpty();
    }
/** Méthodes qui vérifient la présence de joueurs, d'objectifs, ou de boîtes dans la grille.**/
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
    /** Calculent et retournent le nombre total d'objectifs présents dans la grille.**/
    @Override
    public long getTargetCount() {
        return Arrays.stream(cell4Design)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()))
                .count();
    }
    /** Calculent et retournent le nombre total de boîtes présents dans la grille.**/
    @Override
    public long getBoxCount() {
        return Arrays.stream(cell4Design)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Box()))
                .count();
    }
   /** Vérifie si la grille a subi des changements.**/
    @Override
    public boolean isGridChanged() {
        return gridChanged.get();
    }

/** Retourne le tableau de cellules de la grille, utilisé pour l'accès direct aux cellules.**/
    public Cell4Design[][] getMatrix() {
        return cell4Design;
    }

/** Définit la valeur d'une cellule spécifique, ajoutant un nouvel élément et déclenchant un changement de grille si les indices sont valides.**/
    @Override
    public void setCellValue(int line, int col, Element newValue) {
        if (line >= 0 && line < gridWidth.get() && col >= 0 && col < gridHeight.get()) {
            cell4Design[line][col].addValue(newValue);

            triggerGridChange();
        }
    }
/** Incrémente le compteur global de boîtes et retourne la nouvelle valeur.**/
    public static int incrementBoxCount() {;
        System.out.println("box"+boxCount);
        return boxCount++;
    }




}
