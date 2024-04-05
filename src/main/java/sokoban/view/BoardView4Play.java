package sokoban.view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sokoban.model.Board4Play;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Play;

public class BoardView4Play extends BoardView{
    @Override
    protected double getToolbarWidth() {
        return 0;
    }

    @Override
    protected double getTopContainerHeight() {
        return 0;
    }

    @Override
    protected double getPlayButtonContainerHeight() {
        return 0;
    }

    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel4Play) {
        super(primaryStage, boardViewModel4Play);
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
              //  boardViewModel.movePlayer(direction);
            }
        });
    }

}
