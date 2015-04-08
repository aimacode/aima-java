package aima.gui.demo.search.tree.info.rectangular;

import aima.extra.instrument.search.TreeSearchInstrumented;
import aima.gui.demo.search.problem.rectangular.AtVertex;
import aima.gui.demo.search.problem.rectangular.RectangularGrid;
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
import javafx.scene.paint.Color;
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
    private RectangularGrid grid;
    private double defaultRadius = 0;

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
                TreeSearchInstrumented.Cmd<AtVertex> cmd = this.simulator.getExecuted().get(this.simulator.getCurrentExecutionIndex());
                Map<AtVertex, Integer> visited = cmd.statesVisitiedCounts();
                stateLabels.entrySet().forEach(e -> {
                    Integer cnt = visited.get(e.getKey());
                    e.getValue().setTextFill(Color.BLACK);
                    if (cnt == null) {
                        e.getValue().setText("");
                        if (cmd.statesInFrontierNotVisited().contains(e.getKey())) {
                            grid.vertexes[e.getKey().x][e.getKey().y].setFill(Color.WHITE);
                            grid.vertexes[e.getKey().x][e.getKey().y].setStroke(Color.BLACK);
                        }
                        else {
                            grid.vertexes[e.getKey().x][e.getKey().y].setFill(Color.LIGHTGRAY);
                            grid.vertexes[e.getKey().x][e.getKey().y].setStroke(Color.LIGHTGRAY);
                        }
                    }
                    else {
                        e.getValue().setTextFill(Color.WHITE);
                        e.getValue().setText("" + cnt);
                        grid.vertexes[e.getKey().x][e.getKey().y].setFill(Color.BLACK);
                        grid.vertexes[e.getKey().x][e.getKey().y].setStroke(Color.BLACK);
                    }
                    if (cmd.lastNodeVisited() != null && e.getKey().equals(cmd.lastNodeVisited().state())) {
                        grid.vertexes[e.getKey().x][e.getKey().y].setRadius(defaultRadius+3);
                    }
                    else {
                        grid.vertexes[e.getKey().x][e.getKey().y].setRadius(defaultRadius);
                    }
                });
            }
            else {
                stateLabels.entrySet().forEach(e -> {
                    e.getValue().setTextFill(Color.BLACK);
                    e.getValue().setText("");
                    grid.vertexes[e.getKey().x][e.getKey().y].setRadius(defaultRadius);
                    grid.vertexes[e.getKey().x][e.getKey().y].setFill(Color.LIGHTGRAY);
                    grid.vertexes[e.getKey().x][e.getKey().y].setStroke(Color.LIGHTGRAY);
                });
            }
        });
    }

    private void setupStateSpaceRepresentation() {
        stateLabels.clear();
        grid = problemController.setupGrid(stateSpaceVisitedViewPane, stateSpaceVisitedViewScrollPane,
                vertex -> {
                    defaultRadius = vertex.getRadius();
                    Label l = new Label("");
                    l.setFont(_defaultLabelFont);
                    stateLabels.put(new AtVertex(vertex.x, vertex.y), l);
                    stateSpaceVisitedViewPane.getChildren().add(l);
                    l.widthProperty().addListener((observable, oldValue, newValue) -> {
                        l.setLayoutX(vertex.getCenterX() - (l.getWidth() / 2));
                    });
                    l.heightProperty().addListener((observable, oldValue, newValue) -> {
                        l.setLayoutY(vertex.getCenterY() - (l.getHeight() / 2));
                    });
                    vertex.setFill(Color.LIGHTGRAY);
                    vertex.setStroke(Color.LIGHTGRAY);
                },
                edge -> {
                    edge.setFill(Color.LIGHTGRAY);
                    edge.setStroke(Color.LIGHTGRAY);
                });
    }
}
