package sokoban.viewmodel;

import javafx.beans.property.*;
import sokoban.model.Board4Design;
import sokoban.model.CellValue;
import javafx.beans.binding.BooleanBinding;
import sokoban.model.Cell;
import sokoban.model.element.Element;
import sokoban.model.element.Ground;


public class CellViewModel {
    private static final double DEFAULT_SCALE = 1;
    private static final double EPSILON = 1e-3;
    private  BoardViewModel boardViewModel; // Add a reference to BoardViewModel
    private Element baseElement = new Ground(); // Pour l'élément de base (joueur ou boîte)
    private boolean hasGoal = false; // Pour savoir si un goal est présent

    private int line;
    private int col;
    private final Board4Design board;
    public Cell cell;

    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(DEFAULT_SCALE);
    private final BooleanBinding mayIncrementScale = scale.lessThan(1 - EPSILON);
    private final BooleanBinding mayDecrementScale = scale.greaterThan(0.1 + EPSILON);



    private Element selectedTool = new Ground();

    public CellViewModel(int line, int col, Board4Design board) {
        this.line = line;
        this.col = col;
        this.board = board;
    }
    public void setBoardViewModel(BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
    }
    public void play() {
        if (boardViewModel instanceof BoardViewModel4Design) {
            BoardViewModel4Design designViewModel = (BoardViewModel4Design) boardViewModel;
            Element toolValue = designViewModel.getSelectedCellValue();
            board.play(line, col, toolValue);
        } else {
            // Handle the case when boardViewModel is not an instance of BoardViewModel4Design
            // This might involve logging an error, throwing an exception, or providing a fallback behavior
            System.out.println("boardViewModel is not an instance of BoardViewModel4Design. Cannot execute play action.");
        }
    }

    public void addObject() {
        if (boardViewModel instanceof BoardViewModel4Design) {
            BoardViewModel4Design designViewModel = (BoardViewModel4Design) boardViewModel;
            Element selectedTool = designViewModel.getSelectedTool();
            if (selectedTool.getType() == CellValue.PLAYER && designViewModel.hasPlayer()) {
                System.out.println("A player is already present on the grid. Cannot add another.");
                return;
            }
            if (selectedTool.getType() != CellValue.EMPTY && isEmpty()) {
                updateCellValue(selectedTool);
            }
        }
    }



    // Méthode pour "supprimer" un objet de la cellule
    public void deleteObject() {
        if (!isEmpty()) {
            Cell cell = board.getGrid().getMatrix()[line][col];
            cell.clearValues();
            board.getGrid().triggerGridChange(); // cela a permis la mise a jour lors de la suppression lors du drag
        }
    }

    // Méthode privée pour mettre à jour la valeur de la cellule
    private void updateCellValue(Element newValue) {
        board.setCellValue(line, col, newValue);

    }

    public ReadOnlyListProperty<Element> valueProperty() {
        return board.valueProperty(line, col);
    }

    public boolean isEmpty() {
        return board.isEmpty(line, col);
    }

    public SimpleDoubleProperty scaleProperty() {
        return scale;
    }

    public BooleanBinding mayIncrementScaleProperty() {
        return mayIncrementScale;
    }

    public BooleanBinding mayDecrementScaleProperty() {
        return mayDecrementScale;
    }




    public void handleMouseReleased() {

    }
    public void resetScale() {
        scale.set(DEFAULT_SCALE);
    }

    public Element getSelectedTool() {
        return this.selectedTool;
    }

    public void setSelectedTool(Element tool) {
        this.selectedTool = tool;
    }



}

