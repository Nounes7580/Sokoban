package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.element.*;
import sokoban.view.BoardView4Play;


import java.util.Arrays;

import java.util.Stack;

public class Board4Play {
    private static int boxesOnGoals = 0;
    public static Grid4Play grid4Play;
    private static int moveCount = 0;
    private static boolean lastMoveWasSuccessful = false;

/** Accède et retourne la propriété des éléments dans la cellule spécifiée de la grille, permettant de lier ces éléments à la vue ou à d'autres composants. **/
    public ReadOnlyListProperty<Element> valueProperty(int line, int col) {
        return grid4Play.valueProperty(line, col);
    }

    /** Retourne l'instance de Grid4Play utilisée dans ce jeu. **/
    public Grid4Play getGrid4Play() {
        return grid4Play;
    }

    /** Initialise une nouvelle grille de jeu (Grid4Play) en utilisant la grille de conception fournie (Board4Design).
     Compte le nombre initial de boîtes placées sur des objectifs à l'initialisation. **/
    public Board4Play(Board4Design board4Design) {
        grid4Play=new Grid4Play(board4Design.grid4Design);
        boxesOnGoals = countInitialBoxesOnGoals();
    }

    private int countInitialBoxesOnGoals() {
        return (int) Arrays.stream(grid4Play.getMatrix())
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getValue().contains(new Goal()) && cell.getValue().contains(new Box()))
                .count();
    }
/** Permet de jouer ou d'interagir avec une cellule spécifique de la grille en plaçant un élément (Element) à la position spécifiée.
 Vérifie d'abord les limites pour s'assurer que la ligne et la colonne sont valides avant de faire le mouvement.**/
    public void play(int line, int col, Element toolValue) {


        if (line < 0 || line >= grid4Play.getGridWidth() || col < 0 || col >= grid4Play.getGridHeight()) {
            System.out.println("Indices hors limites : line=" + line + ", col=" + col);
            return; // Aucune valeur à retourner, la méthode peut être de type void.
        }

        // Accéder directement à la cellule pour manipuler ses états.
        grid4Play.play(line,col,toolValue);

        // Si la grille n'est pas pleine ou si la cellule n'est pas vide, procéder à la manipulation.
        // Cette méthode ne retourne plus de CellValue car cela n'a pas de sens avec la structure de données actuelle.
    }
/** Déplace le joueur dans la direction spécifiée en tenant compte des obstacles et des règles de jeu.
 Vérifie si le mouvement est valide et déplace le joueur ainsi que les boîtes, si nécessaire.
 Met à jour le nombre de boîtes correctement placées sur les objectifs après le déplacement. **/
    public static void movePlayer(Direction direction) {

        if (boxesOnGoals == grid4Play.getTargetCount()) {
            System.out.println("All boxes are on the goals. No more moves allowed.");
            return;
        }

        int[] playerPosition = grid4Play.findPlayerPosition();
        if (playerPosition == null) {
            System.out.println("Player not found.");
            return;
        }

        Cell oldCell = grid4Play.getMatrix()[playerPosition[0]][playerPosition[1]];
        boolean isPlayerOnGoal = oldCell.hasElementOfType(Goal.class);

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();


        if (!isMoveValid(newRow, newCol, direction)) {
            System.out.println("Move to " + newRow + ", " + newCol + " is invalid.");
            return;
        }

        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        if (targetCell.hasElementOfType(Box.class)) {
            int boxNewRow = newRow + direction.getDeltaRow();
            int boxNewCol = newCol + direction.getDeltaCol();

            boolean isBoxOnGoal = targetCell.hasElementOfType(Goal.class);
            boolean willBoxBeOnGoal = grid4Play.getMatrix()[boxNewRow][boxNewCol].hasElementOfType(Goal.class);

            if (isPositionValid(boxNewRow, boxNewCol)) {
                // Retrieve the existing Box object from the targetCell
                Box box = (Box) targetCell.getElementOfType(Box.class);
                // Move the existing Box object to the new cell
                grid4Play.play(boxNewRow, boxNewCol, box);
                if (!isBoxOnGoal && willBoxBeOnGoal) {
                    boxesOnGoals++; // Increment the number of boxes on goals
                }
                if (isBoxOnGoal && !willBoxBeOnGoal) {
                    boxesOnGoals--; // Decrement the number of boxes on goals
                }
                if (!isBoxOnGoal) {
                    grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.EMPTY));
                }
                if (boxesOnGoals == grid4Play.getTargetCount()) {

                    BoardView4Play.displayYouWinLabel(moveCount);
                }
                grid4Play.addPlayerToCell(newRow, newCol);
                BoardView4Play.updateGoalsReached(boxesOnGoals);
                lastMoveWasSuccessful = true;
            } else {
                System.out.println("Invalid move: Box cannot be moved to (" + boxNewRow + ", " + boxNewCol + ")");
                return;
            }
        } else {
            grid4Play.play(newRow, newCol, createElementFromCellValue(CellValue.PLAYER));
        }
        // If the player is moving from a goal, keep the goal.
        if (isPlayerOnGoal) {
            grid4Play.play(playerPosition[0], playerPosition[1], new Goal());
            lastMoveWasSuccessful = true;
        } else {
            grid4Play.play(playerPosition[0], playerPosition[1], new Ground());
            lastMoveWasSuccessful = true;
        }
        System.out.println(lastMoveWasSuccessful);

        moveCount++;
        BoardView4Play.updateMovesLabel(moveCount);



    }

    public boolean canMove(Direction direction) {
        int[] playerPosition = grid4Play.findPlayerPosition();
        if (playerPosition == null) {
            System.out.println("Player not found.");
            return false;
        }

        int newRow = playerPosition[0] + direction.getDeltaRow();
        int newCol = playerPosition[1] + direction.getDeltaCol();



        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        if (targetCell.hasElementOfType(Wall.class)) {
            return false;
        }

        if (targetCell.hasElementOfType(Box.class)) {
            // Déterminez si la boîte peut être poussée.
            int nextRow = newRow + direction.getDeltaRow();
            int nextCol = newCol + direction.getDeltaCol();


            Cell nextCell = grid4Play.getMatrix()[nextRow][nextCol];
            // Vérifiez si la cellule derrière la boîte est libre pour la pousser
            if (!nextCell.isEmpty() && !nextCell.hasElementOfType(Goal.class)) {
                return false;
            }
        }

        return true;
    }


    public void setMoveCount(int moveCount) {
    }
    /** Annule le dernier mouvement du joueur, restaura la position précédente et ajuste le nombre de mouvements.
     Utilisée pour implémenter une fonctionnalité d'annulation dans le jeu.**/
    public void undoMovePlayer(int[] previousPosition, int previousMoveCount) {
        int[] currentPlayerPosition = grid4Play.findPlayerPosition();
        if (currentPlayerPosition == null) return;


        Element player = createElementFromCellValue(CellValue.PLAYER);
        Cell previousCell = grid4Play.getMatrix()[previousPosition[0]][previousPosition[1]];
        Cell currentCell = grid4Play.getMatrix()[currentPlayerPosition[0]][currentPlayerPosition[1]];


        boolean wasOnGoal = currentCell.hasElementOfType(Goal.class);


        if (!previousCell.hasElementOfType(Box.class)) {
            grid4Play.play(previousPosition[0], previousPosition[1], player);
        }


        grid4Play.play(currentPlayerPosition[0], currentPlayerPosition[1], wasOnGoal ? new Goal() : new Ground());

        // Restaure le nombre de mouvements
        moveCount += 5;
        BoardView4Play.updateMovesLabel(moveCount);
    }



/** Retourne le nombre total de mouvements effectués jusqu'à présent. **/
    public int getMoveCount() {
        return moveCount;
    }
    /** Vérifie si un mouvement proposé est valide, en considérant la position et les règles spécifiques du jeu. **/
    private static boolean isMoveValid(int newRow, int newCol, Direction direction) {
        System.out.println("Checking move validity for: " + newRow + ", " + newCol);


        if (newRow < 0 || newRow >= grid4Play.getGridWidth() || newCol < 0 || newCol >= grid4Play.getGridHeight()) {
            System.out.println("Move is invalid: Player out of bounds.");
            return false;
        }

        Cell targetCell = grid4Play.getMatrix()[newRow][newCol];
        System.out.println("Target cell value: " + targetCell.getValue());


        if (targetCell.isEmpty() || targetCell.hasElementOfType(Goal.class)) {
            return true;
        }

        if (targetCell.hasElementOfType(Box.class)) {
            int boxNewRow = newRow + direction.getDeltaRow();
            int boxNewCol = newCol + direction.getDeltaCol();

            System.out.println("Checking next cell for box at: " + boxNewRow + ", " + boxNewCol);


            if (boxNewRow < 0 || boxNewRow >= grid4Play.getGridWidth() || boxNewCol < 0 || boxNewCol >= grid4Play.getGridHeight()) {
                System.out.println("Move is invalid: Box out of bounds.");
                return false;
            }

            Cell boxNextCell = grid4Play.getMatrix()[boxNewRow][boxNewCol];
            System.out.println("Next cell value for box: " + boxNextCell.getValue());


            boolean canMoveBox = (boxNextCell.isEmpty() || boxNextCell.hasElementOfType(Goal.class)) && !boxNextCell.hasElementOfType(Box.class);
            System.out.println("Can move box: " + canMoveBox);
            return canMoveBox;
        }

        return false;
    }

/** Vérifie si la nouvelle position pour une boîte est valide dans la grille, notamment si la position n'est pas bloquée par d'autres boîtes ou des murs. **/
    private static boolean isPositionValid(int boxNewRow, int boxNewCol) {

        if (boxNewRow < 0 || boxNewRow >= grid4Play.getGridWidth() || boxNewCol < 0 || boxNewCol >= grid4Play.getGridHeight()) {
            System.out.println("Box move out of bounds: (" + boxNewRow + ", " + boxNewCol + ")");
            return false;
        }


        Cell boxNewCell = grid4Play.getMatrix()[boxNewRow][boxNewCol];


        boolean isPositionFree = !(boxNewCell.hasElementOfType(Box.class) || boxNewCell.hasElementOfType(Wall.class));

        if (!isPositionFree) {
            System.out.println("Box move blocked by another element at: (" + boxNewRow + ", " + boxNewCol + ")");
        }

        return isPositionFree;
    }
    /** Crée et retourne un élément (Element) basé sur une valeur de cellule spécifiée (CellValue). Utilisé pour convertir des valeurs de cellules en éléments concrets. **/
    public static Element createElementFromCellValue(CellValue value) {
        switch (value) {
            case PLAYER:
                return new Player();
            case BOX:
                return new Box();
            case GOAL:
                return new Goal();

            case EMPTY:
            default:
                return new Ground();
        }
    }
/** Retourne le nombre actuel de boîtes placées correctement sur les objectifs. **/
    public int getGoalsReached() {
        return boxesOnGoals;
    }
/** Décrémente le compteur de boîtes sur les objectifs, généralement appelé lorsqu'une boîte est retirée d'un objectif. **/
    public void decrementGoalsFilled() {
        boxesOnGoals--;
    }


    public enum Direction {
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP(0, -1),
        DOWN(0, 1);
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
    /** Incrémente le compteur de boîtes sur les objectifs, généralement appelé lorsqu'une boîte est placée sur un objectif. **/
    public void incrementGoalsFilled() {
        boxesOnGoals++;
    }



}

