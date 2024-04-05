package sokoban.viewmodel;

import javafx.beans.Observable;
import javafx.beans.property.*;
import sokoban.model.*;
import javafx.beans.binding.LongBinding;
import sokoban.model.element.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static sokoban.model.CellValue.*;


public abstract class BoardViewModel {
    public abstract BooleanProperty gridResetProperty();

    public BoardViewModel() {
    }
    public abstract int getGridWidth();

    public abstract int getGridHeight();






    // Méthode pour mettre à jour le message de validation
    public abstract void updateValidationMessage();  // Implemented differently based on the subclass




    private final BooleanProperty gridReset = new SimpleBooleanProperty(false);



    //Todo: à separer pour le jeu (boardview4play)

}
