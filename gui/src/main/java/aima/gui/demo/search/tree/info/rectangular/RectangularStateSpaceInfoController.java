package aima.gui.demo.search.tree.info.rectangular;

import aima.gui.demo.search.problem.rectangular.RectangularGridProblemController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularStateSpaceInfoController {
    //
    private static final Font _defaultLabelFont = Font.font(java.awt.Font.MONOSPACED, FontWeight.NORMAL, 9);
    //
    @FXML private ScrollPane stateSpaceVisitedViewScrollPane;
    @FXML private Pane stateSpaceVisitedViewPane;
    //
    private RectangularGridProblemController problemController;

    public void setProblemController(RectangularGridProblemController problemController) {
        this.problemController = problemController;


        stateSpaceVisitedViewScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {

                setupStateSpaceRepresentation();

                // Only do once
                stateSpaceVisitedViewScrollPane.viewportBoundsProperty().removeListener(this);
            }
        });
    }

    private void setupStateSpaceRepresentation() {
        problemController.setupGrid(stateSpaceVisitedViewPane, stateSpaceVisitedViewScrollPane, vertex -> {
            Label l = new Label("0");
            l.setFont(_defaultLabelFont);
            stateSpaceVisitedViewPane.getChildren().add(l);
            l.widthProperty().addListener((observable, oldValue, newValue) -> {
                l.setLayoutX(vertex.getCenterX() - (l.getWidth() / 2));
            });
            l.heightProperty().addListener((observable, oldValue, newValue) -> {
                l.setLayoutY(vertex.getCenterY()-(l.getHeight()/2));
            });
        });
    }
}
