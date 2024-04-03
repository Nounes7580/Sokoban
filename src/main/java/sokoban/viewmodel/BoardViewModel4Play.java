package sokoban.viewmodel;

import javafx.beans.property.BooleanProperty;

public class BoardViewModel4Play extends BoardViewModel {
    @Override
    public BooleanProperty gridResetProperty() {
        return null;
    }

    @Override
    public int getGridWidth() {
        return 0;
    }

    @Override
    public int getGridHeight() {
        return 0;
    }

    @Override
    public void updateValidationMessage() {

    }



}
