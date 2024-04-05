package sokoban.viewmodel;

import sokoban.model.Board4Design;

public abstract class GridViewModel {

    protected BoardViewModel boardViewModel; // Add a reference to BoardViewModel




    public abstract void setBoardViewModel(BoardViewModel boardViewModel);

    public abstract  CellViewModel getCellViewModel(int line, int col);

   public abstract int getGridWidth();

    public abstract int getGridHeight();



}
