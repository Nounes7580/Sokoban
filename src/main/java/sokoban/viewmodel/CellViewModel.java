package sokoban.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class CellViewModel {
    private static final double DEFAULT_SCALE = 1;
    private static final double EPSILON = 1e-3;
    private  BoardViewModel boardViewModel; // Add a reference to BoardViewModel
    private CellValue baseElement = CellValue.EMPTY; // Pour l'élément de base (joueur ou boîte)
    private boolean hasGoal = false; // Pour savoir si un goal est présent

    private int line;
    private int col;
    private final Board board;

    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(DEFAULT_SCALE);
    private final BooleanBinding mayIncrementScale = scale.lessThan(1 - EPSILON);
    private final BooleanBinding mayDecrementScale = scale.greaterThan(0.1 + EPSILON);



    private CellValue selectedTool = CellValue.EMPTY;

    public CellViewModel(int line, int col, Board board) {
        this.line = line;
        this.col = col;
        this.board = board;
    }
    public void setBoardViewModel(BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
    }
    public void play() {
        if (boardViewModel != null) {
            CellValue toolValue = boardViewModel.getSelectedCellValue(); // Now we're calling the method on BoardViewModel
            board.play(line, col, toolValue);
        }
    }
    public void addObject() {
        CellValue selectedTool = boardViewModel.getSelectedTool(); // Obtenez l'outil actuellement sélectionné
        if (selectedTool == CellValue.PLAYER && boardViewModel.hasPlayer()) {
            System.out.println("A player is already present on the grid. Cannot add another.");
            return; // Sortie précoce si un joueur est déjà présent
        }
        if (selectedTool != CellValue.EMPTY && isEmpty()) {
            updateCellValue(selectedTool); // Mettez à jour la valeur de la cellule
        }
    }


    // Méthode pour "supprimer" un objet de la cellule
    public void deleteObject() {
        if (!isEmpty()) { // Vérifiez si la cellule n'est pas déjà vide
            updateCellValue(CellValue.EMPTY); // Définissez la cellule sur EMPTY
        }
    }

    // Méthode privée pour mettre à jour la valeur de la cellule
    private void updateCellValue(CellValue newValue) {
        board.setCellValue(line, col, newValue); // Supposons que cette méthode existe dans votre classe Board
        // Ici, vous pourriez également mettre à jour la vue ou notifier des observateurs
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty() {
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

    public CellValue getSelectedTool() {
        return this.selectedTool;
    }

    public void setSelectedTool(CellValue tool) {
        this.selectedTool = tool;
    }



}

