package sokoban.viewmodel;

import javafx.beans.property.*;
import sokoban.model.Board4Design;
import sokoban.model.CellValue;
import javafx.beans.binding.BooleanBinding;
import sokoban.model.Cell;
import sokoban.model.element.Element;
import sokoban.model.element.Ground;


public abstract class CellViewModel {


    protected static final double DEFAULT_SCALE = 1;
    protected static final double EPSILON = 1e-3;
    protected   BoardViewModel boardViewModel; // Add a reference to BoardViewModel
    protected Element baseElement = new Ground(); // Pour l'élément de base (joueur ou boîte)
    protected boolean hasGoal = false; // Pour savoir si un goal est présent

    protected int line;
    protected int col;
    protected final Board4Design board;

    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(DEFAULT_SCALE);
    private final BooleanBinding mayIncrementScale = scale.lessThan(1 - EPSILON);
    private final BooleanBinding mayDecrementScale = scale.greaterThan(0.1 + EPSILON);



    private Element selectedTool = new Ground();

    public CellViewModel(int line, int col, Board4Design board) {
        this.line = line;
        this.col = col;
        this.board = board;
    }

    protected abstract void setBoardViewModel(BoardViewModel boardViewModel);

    public abstract void play();

    public abstract void addObject();




    // Méthode pour "supprimer" un objet de la cellule
    public abstract void deleteObject();


    // Méthode privée pour mettre à jour la valeur de la cellule
    protected abstract void updateCellValue(Element newValue);

    public abstract ReadOnlyListProperty<Element> valueProperty();

    protected abstract boolean isEmpty();

    public abstract void handleMouseReleased();

    public abstract void  resetScale();

    public abstract Element getSelectedTool();







}

