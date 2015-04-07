package aima.gui.demo.search.problem.rectangular;

import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import aima.gui.demo.search.tree.info.rectangular.RectangularStateSpaceInfoController;
import aima.gui.support.fx.FXUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularGridProblemController implements TreeSearchAlgoSimulator.Observer<AtVertex> {
    @FXML private Label  problemTypeLabel;
    @FXML private Button optionsButton;
    @FXML private Button listProblemsButton;
    @FXML private ScrollPane problemViewScrollPane;
    @FXML private Pane problemViewPane;

    //
    private Vertex      startNode = null;
    private Set<Vertex> goalNodes = new HashSet<>();
    //
    private int vertexRadius              = 12;
    private int borderPadding             = 10;
    private int vertexRadiusSpacingFactor = 5;
    //
    private Paint defaultPaint    = Color.WHITE;
    private Image goalImage       = new Image(RectangularGridProblemController.class.getResourceAsStream("goal.png"));
    private Image startGoalImage  = new Image(RectangularGridProblemController.class.getResourceAsStream("startgoal.png"));
    private Paint startPaint      = Color.DARKGREEN;
    private Paint goalPaint       = new ImagePattern(goalImage, 0, 0, 16, 16, false);
    private Paint startGoalPaint  = new ImagePattern(startGoalImage, 0, 0, 16, 16, false);
    //
    private TreeSearchAlgoSimulator<AtVertex> simulator;
    private RectangularStateSpaceInfoController problemStateSpaceController;
    //
    private IntegerProperty xDimensionSize  = new SimpleIntegerProperty(5);
    private IntegerProperty yDimensionSize = new SimpleIntegerProperty(5);

    public int getXDimensionSize() {
        return xDimensionSize.get();
    }

    public IntegerProperty xDimensionSizeProperty() {
        return xDimensionSize;
    }

    public void setXDimensionSize(int xDimensionSize) {
        this.xDimensionSize.set(xDimensionSize);
    }


    public int getYDimensionSize() {
        return yDimensionSize.get();
    }

    public IntegerProperty yDimensionSizeProperty() {
        return yDimensionSize;
    }

    public void setYDimensionSize(int yDimensionSize) {
        this.yDimensionSize.set(yDimensionSize);
    }

    @Override
    public void setSimulator(TreeSearchAlgoSimulator<AtVertex> simulator) {
        this.simulator = simulator;
        problemStateSpaceController.setSimulator(simulator);
    }

    public Pane createSearchSpaceInfoRepresentation() throws IOException {
        FXMLLoader stateSpaceInfoLoader = new FXMLLoader(RectangularStateSpaceInfoController.class.getResource("rectangularstatespaceinfo.fxml"));
        Pane stateSpaceInfoPane = stateSpaceInfoLoader.load();

        problemStateSpaceController = stateSpaceInfoLoader.getController();
        problemStateSpaceController.setProblemController(this);

        return stateSpaceInfoPane;
    }

    public RectangularGrid setupGrid(Pane pane, ScrollPane containingScrollPane, Consumer<Vertex> vertexConsumer, Consumer<Edge> edgeConsumer) {
        pane.getChildren().clear();

        RectangularGrid grid = RectangularGrid.setupGrid(xDimensionSize.get(), yDimensionSize.get(),
                vertexRadius,  vertexRadiusSpacingFactor, borderPadding,
                pane, containingScrollPane,
                vertex -> {
                    vertexConsumer.accept(vertex);
                },
                edge -> {
                    edgeConsumer.accept(edge);
                });

        return grid;
    }

    public void setupProblem() {
        startNode = null;
        goalNodes.clear();

        setupGrid(problemViewPane, problemViewScrollPane, vertex -> {
            Tooltip t = new Tooltip("("+vertex.x+","+vertex.y+")");
            Tooltip.install(vertex, t);

            vertex.setFill(defaultPaint);
            vertex.setStroke(Color.BLACK);
            vertex.setStrokeWidth(1);

            vertex.setOnMouseClicked(me -> {
                Vertex clicked = (Vertex) me.getSource();

                if (startNode == null) {
                    startNode = clicked;
                    startNode.setFill(startPaint);
                    goalNodes.remove(startNode); // Ensure is not a goal
                } else if (startNode != null && clicked == startNode) {
                    if (goalNodes.contains(clicked)) {
                        startNode = null;
                        goalNodes.remove(clicked);
                        clicked.setFill(defaultPaint);
                    } else {
                        goalNodes.add(clicked);
                        clicked.setFill(startGoalPaint);
                    }
                } else {
                    if (goalNodes.contains(clicked)) {
                        clicked.setFill(defaultPaint);
                        goalNodes.remove(clicked);
                    } else {
                        clicked.setFill(goalPaint);
                        goalNodes.add(clicked);
                    }
                }

                if (startNode == null) {
                    simulator.setProblem(null);
                } else {
                    simulator.setProblem(new RectangularProblem(getXDimensionSize(), getYDimensionSize(),
                            new AtVertex(startNode.x, startNode.y),
                            goalNodes.stream().map(v -> new AtVertex(v.x, v.y)).collect(Collectors.toList())));
                }
            });
        },
        edge -> {
            edge.setStroke(Color.BLACK);
            edge.setStrokeWidth(1);
        });
    }

    @FXML
    private void initialize() {
        FXUtil.setDefaultButtonIcon(optionsButton, FontAwesomeIcons.GEAR);
        FXUtil.setDefaultButtonIcon(listProblemsButton, FontAwesomeIcons.BARS);


        optionsButton.setTooltip(new Tooltip("Configure Problem"));
        listProblemsButton.setTooltip(new Tooltip("Select Problem"));

        problemViewScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {

                setupProblem();

                // Only do once
                problemViewScrollPane.viewportBoundsProperty().removeListener(this);
            }
        });
    }
}
