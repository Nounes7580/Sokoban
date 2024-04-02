package sokoban;

import javafx.application.Application;
import javafx.stage.Stage;
import sokoban.model.Board;
import sokoban.model.Board4Design;
import sokoban.view.BoardView;
import sokoban.view.BoardView4Design;
import sokoban.viewmodel.BoardViewModel;

public class SokobanApp extends Application  {

    @Override
    public void start(Stage primaryStage) {
        Board4Design board = new Board4Design(15, 10);
        BoardViewModel vm = new BoardViewModel(board);
        new BoardView4Design(primaryStage, vm);
    }

    public static void main(String[] args) {
        launch();
    }

}
