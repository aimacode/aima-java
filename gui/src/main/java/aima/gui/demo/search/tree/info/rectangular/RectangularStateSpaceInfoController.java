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
public class RectangularStateSpaceInfoController implements TreeSearchAlgoSimulator.Observer<String, AtVertex> {
    //
    private static final Font _defaultLabelFont = Font.font(java.awt.Font.MONOSPACED, FontWeight.BOLD, 10);
    //
    @FXML private ScrollPane stateSpaceVisitedViewScrollPane;
    @FXML private Pane stateSpaceVisitedViewPane;
    //
    private TreeSearchAlgoSimulator<String, AtVertex> simulator;
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
    public void setSimulator(TreeSearchAlgoSimulator<String, AtVertex> simulator) {
        this.simulator = simulator;
        simulator.currentExecutionIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (this.simulator.isExecutionStarted() && this.simulator.getCurrentExecutionIndex() >= 0) {
                TreeSearchInstrumented.Cmd<String, AtVertex> cmd = this.simulator.getExecuted().get(this.simulator.getCurrentExecutionIndex());
                Map<AtVertex, Integer> visited = cmd.statesVisitiedCounts();
                stateLabels.entrySet().forEach(e -> {
                    Vertex currentVertex = grid.vertexes[e.getKey().x][e.getKey().y];
                    Integer cnt = visited.get(e.getKey());
                    e.getValue().setTextFill(Color.BLACK);
                    if (cnt == null) {
                        e.getValue().setText("");
                        if (cmd.statesInFrontierNotVisited().containsKey(e.getKey())) {
                            currentVertex.setFill(Color.WHITE);
                            currentVertex.setStroke(Color.BLACK);
                        }
                        else {
                            currentVertex.setFill(Color.LIGHTGRAY);
                            currentVertex.setStroke(Color.LIGHTGRAY);
                        }
                    }
                    else {
                        e.getValue().setTextFill(Color.WHITE);
                        e.getValue().setText("" + cnt);
                        currentVertex.setFill(Color.BLACK);
                        currentVertex.setStroke(Color.BLACK);
                    }

                    // Indicate the last node visited by increasing its size
                    if (cmd.lastNodeVisited() != null && e.getKey().equals(cmd.lastNodeVisited().state())) {
                        currentVertex.setRadius(defaultRadius + 3);
                    }
                    else {
                        currentVertex.setRadius(defaultRadius);
                    }

                    // Update the edges based on how they are connected
                    currentVertex.getNeighbours().forEach(neighbour -> {
                        AtVertex neighbourAt = new AtVertex(neighbour.x, neighbour.y);
                        if (visited.containsKey(e.getKey()) && visited.containsKey(neighbourAt)) {
                            currentVertex.getEdgeFor(neighbour).setFill(Color.BLACK);
                            currentVertex.getEdgeFor(neighbour).setStroke(Color.BLACK);
                        }
                        else if (visited.containsKey(e.getKey()) && cmd.statesInFrontierNotVisited().containsKey(neighbourAt)) {
                            if (cmd.statesInFrontierNotVisited().get(neighbourAt).parent().state().equals(e.getKey())) {
                                currentVertex.getEdgeFor(neighbour).setFill(Color.BLACK);
                                currentVertex.getEdgeFor(neighbour).setStroke(Color.BLACK);
                            }
                            else {
                                currentVertex.getEdgeFor(neighbour).setFill(Color.LIGHTGRAY);
                                currentVertex.getEdgeFor(neighbour).setStroke(Color.LIGHTGRAY);
                            }
                        }
                        else if (!visited.containsKey(neighbourAt)) {
                            currentVertex.getEdgeFor(neighbour).setFill(Color.LIGHTGRAY);
                            currentVertex.getEdgeFor(neighbour).setStroke(Color.LIGHTGRAY);
                        }
                    });
                });
            }
            else {
                stateLabels.entrySet().forEach(e -> {
                    e.getValue().setTextFill(Color.BLACK);
                    e.getValue().setText("");
                    Vertex currentVertex = grid.vertexes[e.getKey().x][e.getKey().y];
                    currentVertex.setRadius(defaultRadius);
                    currentVertex.setFill(Color.LIGHTGRAY);
                    currentVertex.setStroke(Color.LIGHTGRAY);
                    currentVertex.getEdges().forEach(edge -> {
                        edge.setFill(Color.LIGHTGRAY);
                        edge.setStroke(Color.LIGHTGRAY);
                    });
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
