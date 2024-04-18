package sokoban.viewmodel;


import javafx.beans.property.BooleanProperty;
import javafx.scene.input.KeyCode;
import sokoban.model.*;
import sokoban.view.BoardView4Play;

/**
 * Cette classe sert de modèle de vue pour la partie "play" ,
 * en gérant les interactions entre la vue et le modèle de données.
 */

public class BoardViewModel4Play{
    private final CommandManager commandManager; // Gestionnaire des commandes pour les mouvements et actions de jeu.
    private final GridViewModel4Play gridViewModel4Play; // Modèle de vue de la grille pour la partie jeu.
    private final Board4Play board4Play; // Modèle du plateau de jeu.
    private final Board4Design board4Design; // Modèle du plateau de conception.
    private static BoardView4Play boardView4Play; // Vue associée à ce modèle.

    /**
     * Constructeur qui initialise le modèle de vue avec un Board4Design.
     * param board4Design Le plateau de conception qui sert de base au plateau de jeu.
     */
    public BoardViewModel4Play(Board4Design board4Design){
        this.board4Design=board4Design;
        this.board4Play = new Board4Play(board4Design); // Création du plateau de jeu à partir du modèle de conception.
        this.gridViewModel4Play = new GridViewModel4Play(board4Play); // Initialisation du modèle de vue de la grille.
        this.commandManager = new CommandManager(); // Initialisation du gestionnaire de commandes.
    }

    /**
     * Exécute un mouvement dans une direction donnée si le mouvement est possible.
     * param direction La direction du mouvement à exécuter.
     */
    public void executeMove(Direction direction) {
        if (getBoard4Play().canMove(convertDirection(direction))) {
            Command command = new Move(board4Play, convertDirection(direction));
            commandManager.executeCommand(command);
        }else {
            System.out.println("mouvement impossible");
        }

    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    /**
     * Convertit une Direction de l'énumération locale en une Direction du modèle Board4Play.
     * param direction La direction à convertir.
     * @return La direction convertie adaptée au modèle.
     */
    public Board4Play.Direction convertDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Board4Play.Direction.UP;
            case DOWN:
                return Board4Play.Direction.DOWN;
            case LEFT:
                return Board4Play.Direction.LEFT;
            case RIGHT:
                return Board4Play.Direction.RIGHT;
            default:
                return null;
        }
    }

    /**
     * Obtient la hauteur de la grille de jeu.
     * @
     * return La hauteur de la grille.
     */
    public  int getGridHeight() {
        return board4Play.getGrid4Play().getGridHeight();
    }

    /**
     * Obtient la largeur de la grille de jeu.
     * @return La largeur de la grille.
     */
    public  int getGridWidth() {
        return board4Play.getGrid4Play().getGridWidth();
    }

    /**
     * Obtient la direction correspondant à une touche spécifique.
     * param keyCode La touche pressée.
     * return La direction associée à cette touche.
     */
    public Direction getDirection(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
            case W:
                return Direction.UP;
            case DOWN:
            case S:
                return Direction.DOWN;
            case LEFT:
            case A:
                return Direction.LEFT;
            case RIGHT:
            case D:
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    /**
     * Obtient le modèle de vue de la grille pour la partie jeu.
     * return Le modèle de vue de la grille.
     */
    public GridViewModel4Play getGridViewModel4Play() {
        return gridViewModel4Play;
    }

    /**
     * Obtient le plateau de conception.
     * return Le plateau de conception.
     */
    public Board4Design getBoard() {
        return board4Design;
    }

    /**
     * Obtient le plateau de jeu.
     * return Le plateau de jeu.
     */
    public Board4Play getBoard4Play() {
        return board4Play;
    }


    /**
     * Obtient la vue associée à ce modèle de vue.
     * @return La vue du plateau de jeu.
     */
    public static BoardView4Play getBoardView4Play() {
        return boardView4Play;
    }

    /**
     * Obtient le nombre total de cibles dans la grille de jeu.
     * return Le nombre de cibles.
     */
    public long getTargetCount() {
        return board4Play.getGrid4Play().getTargetCount();
    }

    /**
     * Obtient le nombre de cibles actuellement atteintes dans la grille de jeu.
     * return Le nombre de cibles atteintes.
     */
    public int getGoalsReached() {
        return board4Play.getGoalsReached();
    }
}
