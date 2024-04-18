package sokoban.viewmodel;

import sokoban.model.Board4Design;

/**
 * Classe GridViewModel4Design fournit la couche de gestion entre le modèle de données de la grille
 * pour la conception et la représentation visuelle de cette grille dans l'interface utilisateur.
 */
public class GridViewModel4Design extends GridViewModel{

    protected final Board4Design board; // Référence au modèle de données de la grille utilisée pour la conception.

    /**
     * Constructeur qui initialise une nouvelle instance de GridViewModel4Design avec un plateau spécifié.
     * param board Le plateau de conception (Board4Design) sur lequel cette vue modèle opérera.
     */
    GridViewModel4Design(Board4Design board) {
        this.board = board;
    }


    /**
     * Définit le modèle de vue du plateau associé à cette grille.
     * param boardViewModel Le modèle de vue du plateau (BoardViewModel4Design) à associer.
     */
    @Override
    public void setBoardViewModel(BoardViewModel4Design boardViewModel) {
        this.boardViewModel = boardViewModel;
    }


    /**
     * Fournit un modèle de vue pour une cellule spécifique de la grille basée sur ses coordonnées.
     * param line La ligne de la cellule dans la grille.
     * param col La colonne de la cellule dans la grille.
     * return Un CellViewModel4Design pour la cellule spécifiée.
     */
    @Override
    public CellViewModel4Design getCellViewModel(int line, int col) {
        CellViewModel4Design cellViewModel = new CellViewModel4Design(line, col, board);
        cellViewModel.setBoardViewModel(this.boardViewModel);
        return cellViewModel;
    }
    /**
     * Obtient la largeur de la grille du modèle de données.
     * return La largeur de la grille.
     */
    @Override
    public int getGridWidth() {
        return board.getGrid().getGridWidth();
    }


    /**
     * Obtient la hauteur de la grille du modèle de données.
     * return La hauteur de la grille.
     */
    @Override
    public int getGridHeight() {
        return board.getGrid().getGridHeight();
    }


}
