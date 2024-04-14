package sokoban.model;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.element.Box;
import sokoban.model.element.Element;
import sokoban.model.element.Goal;
import sokoban.model.element.Player;

import java.util.Arrays;

public abstract class Grid {
    protected final IntegerProperty gridWidth = new SimpleIntegerProperty();
    protected final IntegerProperty gridHeight = new SimpleIntegerProperty();


    protected BooleanProperty gridChanged = new SimpleBooleanProperty();



    protected abstract void resetGrid(int newWidth, int newHeight);

    public abstract BooleanProperty gridChangedProperty();

    public abstract void triggerGridChange();

    // Cette méthode retourne les coordonnées du joueur sous forme d'un tableau [x, y]
    // ou null si aucun joueur n'est trouvé.
    public abstract int[] findPlayerPosition();

    public abstract int getGridWidth();

    public abstract int getGridHeight();


    protected abstract  ReadOnlyListProperty<Element> valueProperty(int line, int col);

    protected abstract void play(int line, int col, Element toolValue);


    protected abstract LongBinding filledCellsCountProperty();


    protected abstract boolean isEmpty(int line, int col);

    public abstract boolean hasPlayer();


     public abstract boolean hasAtLeastOneTarget();

     public abstract boolean hasAtLeastOneBox();

    public abstract long getTargetCount();

    public abstract long getBoxCount();


    public abstract boolean isGridChanged();



    public abstract void setCellValue(int line, int col, Element newValue);




}
