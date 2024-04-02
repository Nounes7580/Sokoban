package sokoban.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sokoban.model.Board4Play;
import sokoban.viewmodel.BoardViewModel;

public class BoardView4Play extends BoardView{
    public BoardView4Play(Stage primaryStage, BoardViewModel boardViewModel) {
        super(primaryStage, boardViewModel);
    }

    //TODO: Separer ca pour le jeu et pour l'editeur
    protected void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            Board4Play.Direction direction = null;
            switch (event.getCode()) {
                case UP:
                    direction = Board4Play.Direction.UP;
                    break;
                case DOWN:
                    direction = Board4Play.Direction.DOWN;
                    break;
                case LEFT:
                    direction = Board4Play.Direction.LEFT;
                    break;
                case RIGHT:
                    direction = Board4Play.Direction.RIGHT;
                    break;
            }

            if (direction != null) {
                boardViewModel.movePlayer(direction);
            }
        });
    }

}
