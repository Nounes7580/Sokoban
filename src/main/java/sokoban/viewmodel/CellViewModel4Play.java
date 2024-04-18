package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.Board4Design;
import sokoban.model.Board4Play;
import sokoban.model.CellValue;
import sokoban.model.element.Element;

/**
 * La classe CellViewModel4Play gère la logique de vue pour une cellule spécifique
 * dans le contexte de la partie jouable du jeu.
 */

public class CellViewModel4Play {
    private Board4Play board4Play; // Référence au modèle de plateau de jeu.
    protected int line; // Ligne de la cellule dans la grille.
    protected int col; // Colonne de la cellule dans la grille.

    /**
     * Constructeur qui initialise une instance de CellViewModel4Play avec des coordonnées spécifiques dans la grille et une référence au plateau de jeu.
     * param line Ligne de la cellule dans la grille.
     * param col Colonne de la cellule dans la grille.
     * param board Référence au modèle de plateau de jeu.
     */
    public CellViewModel4Play(int line, int col, Board4Play board) {
        this.line = line;
        this.col = col;
        this.board4Play = board;
    }

    /**
     * Méthode qui déclencherait des actions lorsqu'une cellule est jouée, par exemple, en réaction à un clic de souris.
     * Actuellement, cette méthode est vide car elle nécessiterait des règles spécifiques en fonction des interactions de jeu désirées.
     */


    /**
     * Renvoie une propriété qui est une liste observable des éléments contenus dans la cellule.
     * Cela permet à la vue de se lier aux données de la cellule et de se mettre à jour automatiquement lorsque les éléments de la cellule changent.
     * @return Une propriété en lecture seule listant les éléments dans la cellule.
     */
    public ReadOnlyListProperty<Element> valueProperty() {
        return board4Play.valueProperty(line, col);
    }


}
