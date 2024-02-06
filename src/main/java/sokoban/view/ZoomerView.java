package sokoban.view;

import sokoban.model.CellValue;
import sokoban.viewmodel.CellViewModel;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ZoomerView extends AnchorPane {
    private final Button plusButton = new Button("+");
    private final Button minusButton = new Button("-");
    private final CellViewModel viewModel;
    private HBox scaleButtonsBox;

    public ZoomerView(CellViewModel viewModel) {
        this.viewModel = viewModel;

        layoutControls();
        configureBindings();
    }

    private void layoutControls() {
        plusButton.getStyleClass().add("scale-button");
        minusButton.getStyleClass().add("scale-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        scaleButtonsBox = new HBox(minusButton, spacer, plusButton);

        getChildren().add(scaleButtonsBox);
        setBottomAnchor(scaleButtonsBox, 2.0);
        setLeftAnchor(scaleButtonsBox, 2.0);
        setRightAnchor(scaleButtonsBox, 2.0);
    }

    private void configureBindings() {
        // gère le scroll
        setOnScroll(this::scrollChanged);

        // les boutons de mise à l'échelle ne sont visibles que si la case contient X et est survolée
        scaleButtonsBox.visibleProperty().bind(viewModel
                .valueProperty().isEqualTo(CellValue.X)
                .and(hoverProperty()));

        // gère la visibilité des boutons "+" et "-"
        plusButton.visibleProperty().bind(viewModel.mayIncrementScaleProperty());
        minusButton.visibleProperty().bind(viewModel.mayDecrementScaleProperty());

        // gère le clic sur les boutons "+" et "-"
        plusButton.setOnAction(e -> viewModel.incrementScale());
        minusButton.setOnAction(e -> viewModel.decrementScale());
    }

    private void scrollChanged(ScrollEvent event) {
        if (viewModel.isEmpty())
            return;
        // en fonction du signe du scroll, on augmente ou diminue l'échelle
        if (event.getDeltaY() < 0)
            viewModel.decrementScale();
        else
            viewModel.incrementScale();
        // pour indiquer que l'événement a été traité
        event.consume();
    }
}
