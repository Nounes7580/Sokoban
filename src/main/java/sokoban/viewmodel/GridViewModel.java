package sokoban.viewmodel;

import sokoban.model.Board4Design;

public abstract class GridViewModel {

    protected BoardViewModel4Design boardViewModel; // Add a reference to BoardViewModel



    public abstract void setBoardViewModel(BoardViewModel4Design boardViewModel);

    public abstract  CellViewModel4Design getCellViewModel(int line, int col);

   public abstract int getGridWidth();

    public abstract int getGridHeight();



}
