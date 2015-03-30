package aima.gui.demo.search.tree.info.rectangular;

import aima.extra.instrument.search.TreeSearchCmdInstr;
import aima.gui.demo.search.problem.rectangular.AtVertex;
import aima.gui.demo.search.problem.rectangular.RectangularGridProblemController;
import aima.gui.demo.search.problem.rectangular.Vertex;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularStateSpaceInfoController implements TreeSearchAlgoSimulator.Observer<AtVertex> {
    //
    private static final Font _defaultLabelFont = Font.font(java.awt.Font.MONOSPACED, FontWeight.BOLD, 10);
    //
    @FXML private ScrollPane stateSpaceVisitedViewScrollPane;
    @FXML private Pane stateSpaceVisitedViewPane;
    //
    private TreeSearchAlgoSimulator<AtVertex> simulator;
    private RectangularGridProblemController problemController;
    private Map<AtVertex, Label> stateLabels = new HashMap<>();
    private Vertex[][] vertexes;

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

    @Override
    public void setSimulator(TreeSearchAlgoSimulator<AtVertex> simulator) {
        this.simulator = simulator;
        simulator.currentExecutionIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (this.simulator.isExecutionStarted() && this.simulator.getCurrentExecutionIndex() >= 0) {
                TreeSearchCmdInstr.Cmd<AtVertex> cmd = this.simulator.getExecuted().get(this.simulator.getCurrentExecutionIndex());
                Map<AtVertex, Integer> visited = cmd.statesVisitiedCounts();
                stateLabels.entrySet().forEach(e -> {
                    Integer cnt = visited.get(e.getKey());
                    if (cnt == null) {
                        e.getValue().setText("");
                        vertexes[e.getKey().x][e.getKey().y].setStrokeWidth(1);
                    }
                    else {
                        e.getValue().setText(""+cnt);
                        vertexes[e.getKey().x][e.getKey().y].setStrokeWidth(2);
                    }
                });
            }
            else {
                stateLabels.entrySet().forEach(e -> {
                    e.getValue().setText("");
                    vertexes[e.getKey().x][e.getKey().y].setStrokeWidth(1);
                });
            }
        });
    }

    private void setupStateSpaceRepresentation() {
        stateLabels.clear();
        vertexes = problemController.setupGrid(stateSpaceVisitedViewPane, stateSpaceVisitedViewScrollPane, vertex -> {
            Label l = new Label("");
            l.setFont(_defaultLabelFont);
            stateLabels.put(new AtVertex(vertex.x, vertex.y), l);
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
